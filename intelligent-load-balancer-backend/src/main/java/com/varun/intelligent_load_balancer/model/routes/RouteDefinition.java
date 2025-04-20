package com.varun.intelligent_load_balancer.model.routes;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class RouteDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String routePath;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="route_primary_backends", joinColumns = @JoinColumn(name="route_id"))
    private List<BackendDefinition> primaryBackends;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="route_canary_backends", joinColumns = @JoinColumn(name="route_id"))
    private List<BackendDefinition> canaryBackends;

    private Integer canaryTrafficPercent;

    @Enumerated(EnumType.STRING)
    private LoadBalancingStrategyType strategy;

    public List<BackendDefinition> getPrimaryBackends() {
        return primaryBackends;
    }

    public void setPrimaryBackends(List<BackendDefinition> primaryBackends) {
        this.primaryBackends = primaryBackends;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public List<BackendDefinition> getCanaryBackends() {
        return canaryBackends;
    }

    public void setCanaryBackends(List<BackendDefinition> canaryBackends) {
        this.canaryBackends = canaryBackends;
    }

    public Integer getCanaryTrafficPercent() {
        return canaryTrafficPercent;
    }

    public void setCanaryTrafficPercent(Integer canaryTrafficPercent) {
        this.canaryTrafficPercent = canaryTrafficPercent;
    }

    public LoadBalancingStrategyType getStrategy() {
        return strategy;
    }

    public void setStrategy(LoadBalancingStrategyType strategy) {
        this.strategy = strategy;
    }
}
