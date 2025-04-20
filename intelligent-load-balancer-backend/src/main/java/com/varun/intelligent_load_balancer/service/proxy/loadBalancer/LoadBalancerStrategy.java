package com.varun.intelligent_load_balancer.service.proxy.loadBalancer;

import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;

import java.util.List;

public interface LoadBalancerStrategy {
    BackendDefinition selectBackend(List<BackendDefinition> backends, String routePath);
}
