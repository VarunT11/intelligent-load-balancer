package com.varun.intelligent_load_balancer.service.metrics;

import com.varun.intelligent_load_balancer.exception.InvalidSessionNameException;
import com.varun.intelligent_load_balancer.model.metrics.Metrics;
import com.varun.intelligent_load_balancer.model.metrics.MetricsSession;
import com.varun.intelligent_load_balancer.repository.metrics.MetricsSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MetricsService implements MetricsObserver {

    private Metrics metrics;
    private Long liveMetricsStartTime;
    private final MetricsSessionRepository metricsSessionRepository;

    public MetricsService(MetricsSessionRepository metricsSessionRepository){
        this.metricsSessionRepository = metricsSessionRepository;
        resetLiveMetrics();
    }

    public Metrics getLiveSnapshot(){
        return metrics;
    }

    public List<MetricsSession> getAllSnapshots(){
        return metricsSessionRepository.listSnapshots();
    }

    public Optional<MetricsSession> getSnapshotById(Long id){
        return metricsSessionRepository.getSnapshotById(id);
    }

    public MetricsSession saveSnapshot(String sessionName){
        if(sessionName.isEmpty()){
            throw new InvalidSessionNameException("Session Name cannot be empty");
        }
        MetricsSession metricsSession = new MetricsSession(sessionName, liveMetricsStartTime, metrics);
        return metricsSessionRepository.saveSnapshot(metricsSession);
    }

    public void deleteSnapshotById(Long id){
        metricsSessionRepository.deleteSnapshotById(id);
    }

    @Override
    public void onRequestComplete(String backendId, boolean isSuccess, boolean isCanary, String routePath) {
        recordRequest(backendId,isSuccess,isCanary,routePath);
    }

    public void recordRequest(String backendId, boolean isSuccess, boolean isCanary, String routePath){
        metrics.recordNewRequest(backendId);

        if(isSuccess) metrics.recordSuccessfulRequest(backendId);
        else metrics.recordFailedRequest(backendId);

        if(isCanary) metrics.recordCanaryHit(routePath);
    }

    public void resetLiveMetrics(){
        this.liveMetricsStartTime = System.currentTimeMillis();
        this.metrics = new Metrics();
    }

    public Long getLiveMetricsStartTime(){
        return liveMetricsStartTime;
    }
}
