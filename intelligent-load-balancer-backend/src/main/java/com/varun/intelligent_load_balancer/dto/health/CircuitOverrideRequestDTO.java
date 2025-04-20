package com.varun.intelligent_load_balancer.dto.health;

import jakarta.validation.constraints.NotBlank;

public class CircuitOverrideRequestDTO {

    @NotBlank
    private String backendUrl;
    private String reason;

    public CircuitOverrideRequestDTO(){

    }

    public String getBackendUrl() {
        return backendUrl;
    }

    public void setBackendUrl(String backendUrl) {
        this.backendUrl = backendUrl;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
