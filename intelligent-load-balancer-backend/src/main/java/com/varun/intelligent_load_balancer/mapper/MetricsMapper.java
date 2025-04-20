package com.varun.intelligent_load_balancer.mapper;

import com.varun.intelligent_load_balancer.dto.metrics.MetricsSessionResponseDTO;
import com.varun.intelligent_load_balancer.dto.metrics.MetricsSnapshotDTO;
import com.varun.intelligent_load_balancer.model.metrics.Metrics;
import com.varun.intelligent_load_balancer.model.metrics.MetricsSession;

public class MetricsMapper {

    public static MetricsSnapshotDTO mapToMetricsSnapshotDTO(Metrics metrics, Long liveMetricsStartTime){
        MetricsSnapshotDTO dto = new MetricsSnapshotDTO();
        dto.setLiveMetricsStartTime(liveMetricsStartTime);
        dto.setTotalRequests(metrics.getTotalRequestsByBackendId());
        dto.setSuccessfulRequests(metrics.getSuccessfulRequestsByBackendId());
        dto.setFailedRequests(metrics.getFailedRequestsByBackendId());
        dto.setCanaryHits(metrics.getCanaryHitsByRoutePath());
        return dto;
    }

    public static MetricsSessionResponseDTO mapToMetricsSessionResponseDTO(MetricsSession metricsSession){
        MetricsSessionResponseDTO dto = new MetricsSessionResponseDTO();
        dto.setId(metricsSession.getId());
        dto.setSessionName(metricsSession.getSessionName());
        dto.setStartTime(metricsSession.getStartTime());
        dto.setEndTime(metricsSession.getEndTime());
        dto.setSnapshot(getMetricsSnapshotFromMetricsSession(metricsSession));
        return dto;
    }

    private static MetricsSnapshotDTO getMetricsSnapshotFromMetricsSession(MetricsSession metricsSession){
        MetricsSnapshotDTO dto = new MetricsSnapshotDTO();
        dto.setTotalRequests(metricsSession.getTotalRequests());
        dto.setSuccessfulRequests(metricsSession.getSuccessfulRequests());
        dto.setFailedRequests(metricsSession.getFailedRequests());
        dto.setCanaryHits(metricsSession.getCanaryHits());
        return dto;
    }

}
