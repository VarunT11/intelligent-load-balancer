package com.varun.intelligent_load_balancer.model.routes;

public class RoutedBackendResult {

    private final BackendDefinition backendDefinition;
    private final boolean isCanary;

    public RoutedBackendResult(BackendDefinition backendDefinition, boolean isCanary) {
        this.backendDefinition = backendDefinition;
        this.isCanary = isCanary;
    }

    public BackendDefinition getBackendDefinition() {
        return backendDefinition;
    }

    public boolean isCanary() {
        return isCanary;
    }
}
