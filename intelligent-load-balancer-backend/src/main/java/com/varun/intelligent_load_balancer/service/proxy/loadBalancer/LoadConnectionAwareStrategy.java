package com.varun.intelligent_load_balancer.service.proxy.loadBalancer;

import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;

public interface LoadConnectionAwareStrategy extends LoadBalancerStrategy {
    void simulateConnection(BackendDefinition backend);
    void simulateDisconnect(BackendDefinition backend);
}
