package com.varun.intelligent_load_balancer.dto.metrics;

public class MetricsSessionResponseDTO {

    private Long id;
    private String sessionName;
    private Long startTime;
    private Long endTime;
    private MetricsSnapshotDTO snapshot;

    public MetricsSessionResponseDTO(){

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

    public MetricsSnapshotDTO getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(MetricsSnapshotDTO snapshot) {
        this.snapshot = snapshot;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
