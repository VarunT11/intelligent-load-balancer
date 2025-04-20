package com.varun.intelligent_load_balancer.service.proxy.loadBalancer.strategy;

import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;
import com.varun.intelligent_load_balancer.service.proxy.loadBalancer.LoadBalancerStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RoundRobinStrategy implements LoadBalancerStrategy {

    private final Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    @Override
    public BackendDefinition selectBackend(List<BackendDefinition> backends, String routePath) {
        if(backends==null || backends.isEmpty()){
            throw new IllegalArgumentException("No healthy backends available for route: " + routePath);
        }

        counters.putIfAbsent(routePath, new AtomicInteger(0));
        AtomicInteger counter = counters.get(routePath);
        int index = counter.updateAndGet(i -> (i + 1) % backends.size());
        if(index >= backends.size()){
            counter.set(0);
            index = 0;
        }
        return backends.get(index);
    }
}
