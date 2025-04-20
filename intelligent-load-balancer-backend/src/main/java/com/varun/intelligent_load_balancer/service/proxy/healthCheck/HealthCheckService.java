package com.varun.intelligent_load_balancer.service.proxy.healthCheck;

import com.varun.intelligent_load_balancer.dto.health.HealthOverviewDTO;
import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;
import org.springframework.stereotype.Service;

@Service
public interface HealthCheckService {
    boolean isHealthy(BackendDefinition backendDefinition);
    HealthOverviewDTO getHealthOverview();
    HealthOverviewDTO forceCheck();
}
