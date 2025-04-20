package com.varun.intelligent_load_balancer.model.metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Metrics {

    private final Map<String, Long> totalRequestsByBackendId;
    private final Map<String, Long> successfulRequestsByBackendId;
    private final Map<String, Long> failedRequestsByBackendId;
    private final Map<String, Long> canaryHitsByRoutePath;

    public Metrics(){
        totalRequestsByBackendId = new ConcurrentHashMap<>();
        successfulRequestsByBackendId = new ConcurrentHashMap<>();
        failedRequestsByBackendId = new ConcurrentHashMap<>();
        canaryHitsByRoutePath = new ConcurrentHashMap<>();
    }

    public void recordNewRequest(String id){
        totalRequestsByBackendId.merge(id, 1L, Long::sum);
    }

    public void recordSuccessfulRequest(String id){
        successfulRequestsByBackendId.merge(id, 1L, Long::sum);
    }

    public void recordFailedRequest(String id){
        failedRequestsByBackendId.merge(id, 1L, Long::sum);
    }

    public void recordCanaryHit(String id){
        canaryHitsByRoutePath.merge(id, 1L, Long::sum);
    }

    public Map<String, Long> getTotalRequestsByBackendId() {
        return totalRequestsByBackendId;
    }

    public Map<String, Long> getSuccessfulRequestsByBackendId() {
        return successfulRequestsByBackendId;
    }

    public Map<String, Long> getFailedRequestsByBackendId() {
        return failedRequestsByBackendId;
    }

    public Map<String, Long> getCanaryHitsByRoutePath() {
        return canaryHitsByRoutePath;
    }
}
