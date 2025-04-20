package com.varun.intelligent_load_balancer.model.metrics;

import jakarta.persistence.*;

import java.util.Map;

@Entity
public class MetricsSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionName;

    private Long startTime;

    private Long endTime;

    @ElementCollection
    private Map<String, Long> totalRequests;

    @ElementCollection
    private Map<String, Long> successfulRequests;

    @ElementCollection
    private Map<String, Long> failedRequests;

    @ElementCollection
    private Map<String, Long> canaryHits;

    public MetricsSession(){

    }

    public MetricsSession (String sessionName, Long startTime, Metrics metrics){
        this.sessionName = sessionName;
        this.startTime = startTime;
        this.endTime = System.currentTimeMillis();
        this.totalRequests = metrics.getTotalRequestsByBackendId();
        this.successfulRequests = metrics.getSuccessfulRequestsByBackendId();
        this.failedRequests = metrics.getFailedRequestsByBackendId();
        this.canaryHits = metrics.getCanaryHitsByRoutePath();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
}
