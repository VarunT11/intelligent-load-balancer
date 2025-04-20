package com.varun.intelligent_load_balancer.service.proxy.loadBalancer.strategy;

import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;
import com.varun.intelligent_load_balancer.service.proxy.loadBalancer.LoadConnectionAwareStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class LeastConnectionsStrategy implements LoadConnectionAwareStrategy {

    private static final Logger log = LoggerFactory.getLogger(LeastConnectionsStrategy.class);
    private final Map<String, AtomicInteger> connectionCount = new ConcurrentHashMap<>();

    @Override
    public BackendDefinition selectBackend(List<BackendDefinition> backends, String routePath) {
        BackendDefinition selected = null;
        int minConnections = Integer.MAX_VALUE;

        List<BackendDefinition> leastConnectedBackends = new ArrayList<>();

        for (BackendDefinition backend : backends) {
            // Safely get or initialize connection counter
            AtomicInteger count = connectionCount.computeIfAbsent(backend.getId(), id -> new AtomicInteger(0));
            int currentConnections = count.get();
            // Update selected backend if this one has fewer connections
            if (currentConnections < minConnections) {
                selected = backend;
                minConnections = currentConnections;
                leastConnectedBackends.clear();
                leastConnectedBackends.add(backend);
            } else if (currentConnections == minConnections){
                leastConnectedBackends.add(backend);
            }
        }

        return leastConnectedBackends.get(new Random().nextInt(leastConnectedBackends.size()));
    }

    @Override
    public void simulateConnection(BackendDefinition backend) {
        int count = connectionCount.computeIfAbsent(backend.getId(), id -> new AtomicInteger(0)).incrementAndGet();
        if(count>2){
            log.info("Concurrent Connections for {} updated to {}",backend.getId(), count);
        }
    }

    @Override
    public void simulateDisconnect(BackendDefinition backend) {
        int count = connectionCount.get(backend.getId()).decrementAndGet();
    }

}
