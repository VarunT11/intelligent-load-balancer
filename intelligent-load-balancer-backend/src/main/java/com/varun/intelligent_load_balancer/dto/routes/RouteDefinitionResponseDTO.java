package com.varun.intelligent_load_balancer.dto.routes;

import com.varun.intelligent_load_balancer.model.routes.LoadBalancingStrategyType;

import java.util.List;

public class RouteDefinitionResponseDTO {

    private Long id;
    private String routePath;
    private List<BackendDefinitionDTO> primaryBackends;
    private List<BackendDefinitionDTO> canaryBackends;
    private int canaryTrafficPercent;
    private LoadBalancingStrategyType strategyType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public List<BackendDefinitionDTO> getPrimaryBackends() {
        return primaryBackends;
    }

    public void setPrimaryBackends(List<BackendDefinitionDTO> primaryBackends) {
        this.primaryBackends = primaryBackends;
    }

    public List<BackendDefinitionDTO> getCanaryBackends() {
        return canaryBackends;
    }

    public void setCanaryBackends(List<BackendDefinitionDTO> canaryBackends) {
        this.canaryBackends = canaryBackends;
    }

    public int getCanaryTrafficPercent() {
        return canaryTrafficPercent;
    }

    public void setCanaryTrafficPercent(int canaryTrafficPercent) {
        this.canaryTrafficPercent = canaryTrafficPercent;
    }

    public LoadBalancingStrategyType getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(LoadBalancingStrategyType strategyType) {
        this.strategyType = strategyType;
    }
}
