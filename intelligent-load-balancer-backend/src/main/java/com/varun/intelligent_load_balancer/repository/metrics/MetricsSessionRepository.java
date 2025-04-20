package com.varun.intelligent_load_balancer.repository.metrics;

import com.varun.intelligent_load_balancer.model.metrics.MetricsSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetricsSessionRepository {
    MetricsSession saveSnapshot(MetricsSession metricsSession);
    List<MetricsSession> listSnapshots();
    Optional<MetricsSession> getSnapshotById(Long id);
    void deleteSnapshotById(Long id);
}
