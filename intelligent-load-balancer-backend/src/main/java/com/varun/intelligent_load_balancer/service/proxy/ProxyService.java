package com.varun.intelligent_load_balancer.service.proxy;

import com.varun.intelligent_load_balancer.exception.NoHealthyBackendException;
import com.varun.intelligent_load_balancer.exception.RouteNotFoundException;
import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;
import com.varun.intelligent_load_balancer.model.routes.RouteDefinition;
import com.varun.intelligent_load_balancer.model.routes.RoutedBackendResult;
import com.varun.intelligent_load_balancer.service.metrics.MetricsService;
import com.varun.intelligent_load_balancer.service.proxy.canaryRouting.CanaryRoutingService;
import com.varun.intelligent_load_balancer.service.proxy.circuitBreaker.CircuitBreakerService;
import com.varun.intelligent_load_balancer.service.proxy.healthCheck.HealthCheckService;
import com.varun.intelligent_load_balancer.service.proxy.loadBalancer.LoadBalancerService;
import com.varun.intelligent_load_balancer.service.proxy.response.ProxyResponseService;
import com.varun.intelligent_load_balancer.service.routes.RouteDefinitionService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProxyService {

    private static final Logger log = LoggerFactory.getLogger(ProxyService.class.getName());

    private final RouteDefinitionService routeDefinitionService;
    private final LoadBalancerService loadBalancerService;
    private final ProxyResponseService proxyResponseService;
    private final CircuitBreakerService circuitBreakerService;
    private final CanaryRoutingService canaryRoutingService;
    private final HealthCheckService healthCheckService;

    public ProxyService(
            LoadBalancerService loadBalancerService,
            RouteDefinitionService routeDefinitionService,
            ProxyResponseService proxyResponseService,
            CircuitBreakerService circuitBreakerService,
            CanaryRoutingService canaryRoutingService,
            HealthCheckService healthCheckService
    ) {
        this.loadBalancerService = loadBalancerService;
        this.routeDefinitionService = routeDefinitionService;
        this.proxyResponseService = proxyResponseService;
        this.circuitBreakerService = circuitBreakerService;
        this.canaryRoutingService = canaryRoutingService;
        this.healthCheckService = healthCheckService;
    }

    public ResponseEntity<?> proxyRequest(
            HttpHeaders headers,
            String body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        String path = request.getRequestURI();
//        log.info("Request received for path {}",path);

        RouteDefinition route;
        try {
            route = findRouteFromRequestPath(path);
//            log.info("Route found with Path {}", route.getRoutePath());
        } catch (RouteNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No route found with path: " + path);
        }

        RoutedBackendResult backendResult;
        try {
            backendResult = getBackend(route);
        } catch (NoHealthyBackendException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("No backends configured for route: " + path);
        }

        return proxyResponseService.forwardRequest(backendResult.getBackendDefinition(), path, headers, body, method, backendResult.isCanary(), route);
    }

    private RouteDefinition findRouteFromRequestPath(String path) {
        String matchedPath = routeDefinitionService.findMatchedPath(path);
        if (matchedPath == null) {
            throw new RouteNotFoundException(path);
        }

        Optional<RouteDefinition> routeObj = routeDefinitionService.getRouteByName(matchedPath);
        if (routeObj.isEmpty()) {
            throw new RouteNotFoundException(matchedPath);
        }

        return routeObj.get();
    }

    private RoutedBackendResult getBackend(RouteDefinition route) {
//        log.info("Finding Backends for Route {}", route.getRoutePath());
        List<BackendDefinition> primaryBackends = getHealthyBackends(route.getPrimaryBackends());
        List<BackendDefinition> canaryBackends = getHealthyBackends(route.getCanaryBackends());

//        log.info("Total Available Backends, Primary: {}, Canary: {}", primaryBackends.size(), canaryBackends.size());

        boolean isCanary = canaryRoutingService.isRequestCanaryCandidate(canaryBackends, route.getCanaryTrafficPercent());
        List<BackendDefinition> backends = isCanary ? canaryBackends : primaryBackends;

//        log.info("Choosing {} Backends", !isCanary?"Primary":"Canary");

        if (backends == null || backends.isEmpty()) {
            throw new NoHealthyBackendException(route.getRoutePath());
        }

        BackendDefinition routedBackend = loadBalancerService.chooseBackend(backends, route.getRoutePath(), route.getStrategy());
        return new RoutedBackendResult(routedBackend, isCanary);
    }

    private List<BackendDefinition> getHealthyBackends(List<BackendDefinition> backends){
        return backends.stream()
                .filter(healthCheckService::isHealthy)
                .filter(circuitBreakerService::isCircuitClosed)
                .toList();
    }

}
