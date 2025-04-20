package com.varun.intelligent_load_balancer.model.circuit;

public class CircuitMetadata {
    private CircuitState circuitState;
    private long lastFailureTime;
    private int failureCount;

    public CircuitMetadata(){
        resetCircuitState();
    }

    public void resetCircuitState() {
        circuitState = CircuitState.CLOSED;
        lastFailureTime = 0;
        failureCount = 0;
    }

    public CircuitState getCircuitState() {
        return circuitState;
    }

    public void setCircuitState(CircuitState circuitState) {
        this.circuitState = circuitState;
    }

    public long getLastFailureTime() {
        return lastFailureTime;
    }

    public void setLastFailureTime(long lastFailureTime) {
        this.lastFailureTime = lastFailureTime;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }
}
