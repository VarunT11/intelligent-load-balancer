package com.varun.intelligent_load_balancer.model.routes;

public enum LoadBalancingStrategyType {
    ROUND_ROBIN,
    RANDOM,
    LEAST_CONNECTIONS,
    WEIGHTED_ROUND_ROBIN
}
