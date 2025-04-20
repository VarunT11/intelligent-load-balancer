package com.varun.intelligent_load_balancer.service.proxy.circuitBreaker;

import com.varun.intelligent_load_balancer.model.circuit.CircuitMetadata;
import com.varun.intelligent_load_balancer.model.circuit.CircuitState;
import com.varun.intelligent_load_balancer.model.config.SystemProperty;
import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;
import com.varun.intelligent_load_balancer.service.config.SystemPropertyService;
import com.varun.intelligent_load_balancer.service.proxy.ServiceHealthObserver;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CircuitBreakerService implements ServiceHealthObserver {

    private static final String FAILURE_THRESHOLD_PROPERTY_KEY = "circuit.failure.threshold";
    private static final int DEFAULT_FAILURE_THRESHOLD = 3;

    private final Map<String, CircuitMetadata> circuitMap = new ConcurrentHashMap<>();
    private final SystemPropertyService systemPropertyService;

    public CircuitBreakerService(SystemPropertyService systemPropertyService){
        this.systemPropertyService = systemPropertyService;
    }

    @Override
    public void onSuccess(BackendDefinition backend) {
        resetCircuitState(backend);
    }

    @Override
    public void onFailure(BackendDefinition backend) {
        CircuitMetadata circuitMetadata = getCircuitMetadata(backend.getUrl());
        circuitMetadata.setFailureCount(circuitMetadata.getFailureCount() + 1);
        circuitMetadata.setLastFailureTime(System.currentTimeMillis());

        int failureThreshold = getFailureThreshold();

        if (circuitMetadata.getFailureCount() >= failureThreshold) {
            circuitMetadata.setCircuitState(CircuitState.OPEN);
        }
    }

    public boolean isCircuitClosed(BackendDefinition backend){
        return getCircuitMetadata(backend.getUrl()).getCircuitState() == CircuitState.CLOSED;
    }

    public void resetCircuitState(BackendDefinition backend){
        CircuitMetadata metadata = getCircuitMetadata(backend.getUrl());
        metadata.resetCircuitState();
    }

    public CircuitState getCircuitState(BackendDefinition backend){
        CircuitMetadata metadata = getCircuitMetadata(backend.getUrl());
        return metadata.getCircuitState();
    }

    private int getFailureThreshold(){
        Optional<SystemProperty> refreshIntervalSystemProperty = systemPropertyService.getPropertyByKey(FAILURE_THRESHOLD_PROPERTY_KEY);
        if(refreshIntervalSystemProperty.isEmpty()){
            SystemProperty systemProperty = new SystemProperty(FAILURE_THRESHOLD_PROPERTY_KEY, String.valueOf(DEFAULT_FAILURE_THRESHOLD), "Created by default");
            systemPropertyService.saveProperty(systemProperty, true);
            return DEFAULT_FAILURE_THRESHOLD;
        } else {
            return Integer.parseInt(refreshIntervalSystemProperty.get().getValue());
        }
    }

    public void openCircuit(BackendDefinition backend){
        CircuitMetadata metadata = getCircuitMetadata(backend.getUrl());
        metadata.setCircuitState(CircuitState.OPEN);
    }

    public void forceOpenCircuit(String backendUrl){
        CircuitMetadata metadata = getCircuitMetadata(backendUrl);
        metadata.setCircuitState(CircuitState.OPEN);
    }

    public void forceCloseCircuit(String backendUrl){
        CircuitMetadata metadata = getCircuitMetadata(backendUrl);
        metadata.resetCircuitState();
    }

    private CircuitMetadata getCircuitMetadata(String backendUrl){
        return circuitMap.computeIfAbsent(backendUrl, k -> new CircuitMetadata());
    }
}
