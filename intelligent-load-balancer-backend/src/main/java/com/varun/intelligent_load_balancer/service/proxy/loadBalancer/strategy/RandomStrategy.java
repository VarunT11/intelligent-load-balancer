package com.varun.intelligent_load_balancer.service.proxy.loadBalancer.strategy;

import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;
import com.varun.intelligent_load_balancer.service.proxy.loadBalancer.LoadBalancerStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class RandomStrategy implements LoadBalancerStrategy {

    private final Random random = new Random();

    @Override
    public BackendDefinition selectBackend(List<BackendDefinition> backends, String routePath) {
        return backends.get(random.nextInt(backends.size()));
    }
}
