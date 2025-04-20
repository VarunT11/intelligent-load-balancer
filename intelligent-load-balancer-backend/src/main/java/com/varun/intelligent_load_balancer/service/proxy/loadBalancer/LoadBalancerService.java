package com.varun.intelligent_load_balancer.service.proxy.loadBalancer;

import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;
import com.varun.intelligent_load_balancer.model.routes.LoadBalancingStrategyType;
import com.varun.intelligent_load_balancer.service.proxy.loadBalancer.strategy.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LoadBalancerService {

    private final Map<LoadBalancingStrategyType, LoadBalancerStrategy> strategyMap;
    private static final Logger log = LoggerFactory.getLogger(LoadBalancerService.class.getName());

    public LoadBalancerService(
            RoundRobinStrategy roundRobinStrategy,
            RandomStrategy randomStrategy,
            LeastConnectionsStrategy leastConnectionsStrategy,
            WeightedRoundRobinStrategy weightedRoundRobinStrategy
    ){
        strategyMap = Map.of(
                LoadBalancingStrategyType.ROUND_ROBIN, roundRobinStrategy,
                LoadBalancingStrategyType.RANDOM, randomStrategy,
                LoadBalancingStrategyType.LEAST_CONNECTIONS, leastConnectionsStrategy,
                LoadBalancingStrategyType.WEIGHTED_ROUND_ROBIN, weightedRoundRobinStrategy
        );
    }

    public BackendDefinition chooseBackend(List<BackendDefinition> healthyBackends, String routeKey, LoadBalancingStrategyType strategyType){
        LoadBalancerStrategy strategy = strategyMap.get(strategyType);
//        log.info("Choosing Backend using {} strategy", strategyType.name());
        if(strategy == null) throw new IllegalArgumentException("Unsupported strategy: " + strategyType);
        return strategy.selectBackend(healthyBackends, routeKey);
    }

    public void recordConnectionStart(BackendDefinition backend, LoadBalancingStrategyType strategyType){
        LoadBalancerStrategy strategy = strategyMap.get(strategyType);
        if(strategy instanceof LoadConnectionAwareStrategy trackable){
            trackable.simulateConnection(backend);
        }
    }

    public void recordConnectionEnd(BackendDefinition backend, LoadBalancingStrategyType strategyType){
        LoadBalancerStrategy strategy = strategyMap.get(strategyType);
        if(strategy instanceof LoadConnectionAwareStrategy trackable){
            trackable.simulateDisconnect(backend);
        }
    }

}
