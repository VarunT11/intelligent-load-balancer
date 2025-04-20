package com.varun.intelligent_load_balancer.dto.metrics;

import java.util.Map;

public class MetricsSnapshotDTO {

    private Long liveMetricsStartTime;
    private Map<String,Long> totalRequests;
    private Map<String,Long> successfulRequests;
    private Map<String,Long> failedRequests;
    private Map<String,Long> canaryHits;

    public MetricsSnapshotDTO(){

    }

    public MetricsSnapshotDTO(Long liveMetricsStartTime, Map<String, Long> totalRequests, Map<String, Long> successfulRequests, Map<String, Long> failedRequests, Map<String, Long> canaryHits) {
        this.liveMetricsStartTime = liveMetricsStartTime;
        this.totalRequests = totalRequests;
        this.successfulRequests = successfulRequests;
        this.failedRequests = failedRequests;
        this.canaryHits = canaryHits;
    }

    public Map<String, Long> getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(Map<String, Long> totalRequests) {
        this.totalRequests = totalRequests;
    }

    public Map<String, Long> getSuccessfulRequests() {
        return successfulRequests;
    }

    public void setSuccessfulRequests(Map<String, Long> successfulRequests) {
        this.successfulRequests = successfulRequests;
    }

    public Map<String, Long> getFailedRequests() {
        return failedRequests;
    }

    public void setFailedRequests(Map<String, Long> failedRequests) {
        this.failedRequests = failedRequests;
    }

    public Map<String, Long> getCanaryHits() {
        return canaryHits;
    }

    public void setCanaryHits(Map<String, Long> canaryHits) {
        this.canaryHits = canaryHits;
    }

    public Long getLiveMetricsStartTime() {
        return liveMetricsStartTime;
    }

    public void setLiveMetricsStartTime(Long liveMetricsStartTime) {
        this.liveMetricsStartTime = liveMetricsStartTime;
    }
}
