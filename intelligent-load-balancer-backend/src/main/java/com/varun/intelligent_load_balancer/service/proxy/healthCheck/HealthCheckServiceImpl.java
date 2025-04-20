package com.varun.intelligent_load_balancer.service.proxy.healthCheck;

import com.varun.intelligent_load_balancer.dto.health.BackendHealthDTO;
import com.varun.intelligent_load_balancer.dto.health.HealthOverviewDTO;
import com.varun.intelligent_load_balancer.model.config.SystemProperty;
import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;
import com.varun.intelligent_load_balancer.model.routes.RouteDefinition;
import com.varun.intelligent_load_balancer.service.config.SystemPropertyService;
import com.varun.intelligent_load_balancer.service.proxy.circuitBreaker.CircuitBreakerService;
import com.varun.intelligent_load_balancer.service.routes.RouteDefinitionService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HealthCheckServiceImpl implements HealthCheckService {

    private static final String REFRESH_INTERVAL_PROPERTY_KEY = "health.check.interval";
    private static final int DEFAULT_REFRESH_INTERVAL_SECONDS = 15;

    private final Set<BackendDefinition> unHealthyBackends = ConcurrentHashMap.newKeySet();
    private final RestTemplate restTemplate = new RestTemplate();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final RouteDefinitionService routeDefinitionService;
    private final CircuitBreakerService circuitBreakerService;
    private final SystemPropertyService systemPropertyService;

    private volatile LocalDateTime lastCheckedAt;

    public HealthCheckServiceImpl(RouteDefinitionService routeDefinitionService, CircuitBreakerService circuitBreakerService, SystemPropertyService systemPropertyService){
        this.routeDefinitionService = routeDefinitionService;
        this.circuitBreakerService = circuitBreakerService;
        this.systemPropertyService = systemPropertyService;
        startHealthChecks();
    }

    private void startHealthChecks() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkAllBackends();
            } catch (Exception e){
                System.err.println("Health Check failed: " + e.getMessage());
            }
        },0, getRefreshInterval(), TimeUnit.SECONDS);
    }

    @Override
    public boolean isHealthy(BackendDefinition backendDefinition) {
        return !unHealthyBackends.contains(backendDefinition);
    }

    @Override
    public HealthOverviewDTO getHealthOverview() {
        List<RouteDefinition> routes = routeDefinitionService.getAllRoutes();
        Set<BackendDefinition> allBackends = routes.stream()
                .flatMap(route -> Stream.concat(route.getPrimaryBackends().stream(), route.getCanaryBackends().stream()))
                .collect(Collectors.toSet());

        List<BackendHealthDTO> healthy = new ArrayList<>();
        List<BackendHealthDTO> unhealthy = new ArrayList<>();

        for(BackendDefinition backend: allBackends){
            boolean isHealthy = isHealthy(backend);
            String circuitState = circuitBreakerService.getCircuitState(backend).name();

            BackendHealthDTO dto = new BackendHealthDTO();
            dto.setUrl(backend.getUrl());
            dto.setCircuitState(circuitState);
            dto.setStatus(isHealthy?"HEALTHY" : "UNHEALTHY");
            if(!isHealthy){
                dto.setReason("Connection timeout or 5xx Response");
                unhealthy.add(dto);
            } else {
                healthy.add(dto);
            }
        }

        return new HealthOverviewDTO(lastCheckedAt, healthy, unhealthy);
    }

    @Override
    public HealthOverviewDTO forceCheck() {
        checkAllBackends();
        return getHealthOverview();
    }

    private void checkAllBackends(){
        List<RouteDefinition> allRoutes = routeDefinitionService.getAllRoutes();
        Set<BackendDefinition> allBackends = allRoutes.stream()
                .flatMap(route -> Stream.concat(route.getPrimaryBackends().stream(), route.getCanaryBackends().stream()))
                .collect(Collectors.toSet());

        for(BackendDefinition backend: allBackends){
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(backend.getUrl() + "/health", String.class);
                if(!response.getStatusCode().is2xxSuccessful()){
                    unHealthyBackends.add(backend);
                    circuitBreakerService.openCircuit(backend);
                } else {
                    unHealthyBackends.remove(backend);
                    circuitBreakerService.resetCircuitState(backend);
                }
            } catch (Exception e){
                unHealthyBackends.add(backend);
                circuitBreakerService.openCircuit(backend);
            }
        }
        this.lastCheckedAt = LocalDateTime.now();
    }

    private int getRefreshInterval(){
        Optional<SystemProperty> refreshIntervalSystemProperty = systemPropertyService.getPropertyByKey(REFRESH_INTERVAL_PROPERTY_KEY);
        if(refreshIntervalSystemProperty.isEmpty()){
            SystemProperty systemProperty = new SystemProperty(REFRESH_INTERVAL_PROPERTY_KEY, String.valueOf(DEFAULT_REFRESH_INTERVAL_SECONDS), "Created by Default");
            systemPropertyService.saveProperty(systemProperty, true);
            return DEFAULT_REFRESH_INTERVAL_SECONDS;
        } else {
            try {
                return Integer.parseInt(refreshIntervalSystemProperty.get().getValue());
            } catch (NumberFormatException e){
                return DEFAULT_REFRESH_INTERVAL_SECONDS;
            }
        }
    }
}
