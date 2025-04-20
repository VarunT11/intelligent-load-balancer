package com.varun.intelligent_load_balancer.service.proxy.response;

import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;
import com.varun.intelligent_load_balancer.model.routes.RouteDefinition;
import com.varun.intelligent_load_balancer.service.metrics.MetricsObserver;
import com.varun.intelligent_load_balancer.service.proxy.ServiceHealthObserver;
import com.varun.intelligent_load_balancer.service.proxy.loadBalancer.LoadBalancerService;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class ProxyResponseService {

    private final RestTemplate restTemplate;
    private final ServiceHealthObserver serviceHealthObserver;
    private final MetricsObserver metricsObserver;
    private final LoadBalancerService loadBalancerService;

    public ProxyResponseService(ServiceHealthObserver serviceHealthObserver, MetricsObserver metricsObserver, LoadBalancerService loadBalancerService) {
        this.restTemplate = new RestTemplate();
        this.serviceHealthObserver = serviceHealthObserver;
        this.metricsObserver = metricsObserver;
        this.loadBalancerService = loadBalancerService;

        this.restTemplate.setErrorHandler(response -> response.getStatusCode().is5xxServerError());
    }

    public ResponseEntity<?> forwardRequest(BackendDefinition backend, String path, HttpHeaders headers,
                                            String body, HttpMethod method, boolean isCanary, RouteDefinition route) {
        loadBalancerService.recordConnectionStart(backend, route.getStrategy());

        HttpEntity<String> proxyRequest = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    backend.getUrl() + path,
                    method,
                    proxyRequest,
                    String.class
            );

            onRequestSuccessful(backend, route, isCanary);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(response.getHeaders())
                    .body(response.getBody());
        } catch (Exception e) {
            onRequestFailure(backend, route, isCanary);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Backend Error: " + e.getMessage());
        }
    }

    private void onRequestSuccessful(BackendDefinition backend, RouteDefinition route, boolean isCanary){
        serviceHealthObserver.onSuccess(backend);
        metricsObserver.onRequestComplete(backend.getId(), true, isCanary, route.getRoutePath());
        loadBalancerService.recordConnectionEnd(backend, route.getStrategy());
    }

    private void onRequestFailure(BackendDefinition backend, RouteDefinition route, boolean isCanary){
        serviceHealthObserver.onFailure(backend);
        metricsObserver.onRequestComplete(backend.getId(), false, isCanary, route.getRoutePath());
        loadBalancerService.recordConnectionEnd(backend, route.getStrategy());
    }

}
