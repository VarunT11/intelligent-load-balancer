package com.varun.intelligent_load_balancer.service.metrics;

public interface MetricsObserver {
    void onRequestComplete(String backendId, boolean isSuccess, boolean isCanary, String routePath);
}
