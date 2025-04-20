package com.varun.intelligent_load_balancer.controller.admin;

import com.varun.intelligent_load_balancer.dto.health.CircuitOverrideRequestDTO;
import com.varun.intelligent_load_balancer.dto.health.HealthOverviewDTO;
import com.varun.intelligent_load_balancer.service.proxy.circuitBreaker.CircuitBreakerService;
import com.varun.intelligent_load_balancer.service.proxy.healthCheck.HealthCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/health")
public class HealthController {

    private final HealthCheckService healthCheckService;
    private final CircuitBreakerService circuitBreakerService;

    public HealthController(HealthCheckService healthCheckService, CircuitBreakerService circuitBreakerService){
        this.healthCheckService = healthCheckService;
        this.circuitBreakerService = circuitBreakerService;
    }

    @GetMapping
    public ResponseEntity<HealthOverviewDTO> getHealthStatus(){
        return ResponseEntity.ok(healthCheckService.getHealthOverview());
    }

    @PostMapping("/refresh")
    public ResponseEntity<HealthOverviewDTO> refreshHealthCheck(){
        return ResponseEntity.ok(healthCheckService.forceCheck());
    }

    @PostMapping("/circuit/open")
    public ResponseEntity<String> openCircuit(@RequestBody CircuitOverrideRequestDTO requestDTO){
        String backendUrl = requestDTO.getBackendUrl();
        circuitBreakerService.forceOpenCircuit(requestDTO.getBackendUrl());
        return ResponseEntity.ok("Opened the circuit for " + backendUrl);
    }

    @PostMapping("/circuit/close")
    public ResponseEntity<String> closeCircuit(@RequestBody CircuitOverrideRequestDTO requestDTO){
        String backendUrl = requestDTO.getBackendUrl();
        circuitBreakerService.forceCloseCircuit(requestDTO.getBackendUrl());
        return ResponseEntity.ok("Closed the circuit for " + backendUrl);
    }
}
