package com.varun.intelligent_load_balancer.service.proxy.loadBalancer.strategy;

import com.varun.intelligent_load_balancer.model.metrics.Metrics;
import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;
import com.varun.intelligent_load_balancer.service.metrics.MetricsService;
import com.varun.intelligent_load_balancer.service.proxy.loadBalancer.LoadBalancerStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WeightedRoundRobinStrategy implements LoadBalancerStrategy {

    private final MetricsService metricsService;

    @Autowired
    public WeightedRoundRobinStrategy(MetricsService metricsService){
        this.metricsService = metricsService;
    }

    @Override
    public BackendDefinition selectBackend(List<BackendDefinition> backends, String routePath) {
        Map<String,Long> successMap = metricsService.getLiveSnapshot().getSuccessfulRequestsByBackendId();
        Map<BackendDefinition, Double> weightMap = new HashMap<>();

        long totalWeight = 0;
        for(BackendDefinition backend: backends){
            long weight = successMap.getOrDefault(backend.getId(), 1L);
            totalWeight += weight;
            weightMap.put(backend, (double) weight);
        }

        for(Map.Entry<BackendDefinition, Double> entry : weightMap.entrySet()){
            weightMap.put(entry.getKey(), entry.getValue() / totalWeight);
        }

        double r = Math.random();
        double cumulative = 0.0;

        for(Map.Entry<BackendDefinition, Double> entry : weightMap.entrySet()){
            cumulative += entry.getValue();
            if(r<=cumulative){
                return entry.getKey();
            }
        }

        return backends.getFirst();
    }
}
