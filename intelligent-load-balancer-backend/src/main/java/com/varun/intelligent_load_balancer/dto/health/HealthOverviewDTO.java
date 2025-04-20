package com.varun.intelligent_load_balancer.dto.health;

import java.time.LocalDateTime;
import java.util.List;

public class HealthOverviewDTO {

    private LocalDateTime lastCheckedAt;
    private List<BackendHealthDTO> healthyBackends;
    private List<BackendHealthDTO> unhealthyBackends;

    public HealthOverviewDTO(){

    }

    public HealthOverviewDTO(LocalDateTime lastCheckedAt, List<BackendHealthDTO> healthyBackends, List<BackendHealthDTO> unhealthyBackends) {
        this.lastCheckedAt = lastCheckedAt;
        this.healthyBackends = healthyBackends;
        this.unhealthyBackends = unhealthyBackends;
    }

    public LocalDateTime getLastCheckedAt() {
        return lastCheckedAt;
    }

    public void setLastCheckedAt(LocalDateTime lastCheckedAt) {
        this.lastCheckedAt = lastCheckedAt;
    }

    public List<BackendHealthDTO> getHealthyBackends() {
        return healthyBackends;
    }

    public void setHealthyBackends(List<BackendHealthDTO> healthyBackends) {
        this.healthyBackends = healthyBackends;
    }

    public List<BackendHealthDTO> getUnhealthyBackends() {
        return unhealthyBackends;
    }

    public void setUnhealthyBackends(List<BackendHealthDTO> unhealthyBackends) {
        this.unhealthyBackends = unhealthyBackends;
    }
}
