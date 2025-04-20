package com.varun.intelligent_load_balancer.repository.metrics.postgres;

import com.varun.intelligent_load_balancer.model.metrics.MetricsSession;
import com.varun.intelligent_load_balancer.repository.metrics.MetricsSessionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("postgresMetricsSessionRepository")
public class PostgresMetricsSessionRepository implements MetricsSessionRepository {

    private final JpaMetricsSessionRepository jpaMetricsSessionRepository;

    public PostgresMetricsSessionRepository(JpaMetricsSessionRepository jpaMetricsSessionRepository){
        this.jpaMetricsSessionRepository = jpaMetricsSessionRepository;
    }

    @Override
    public MetricsSession saveSnapshot(MetricsSession metricsSession) {
        return jpaMetricsSessionRepository.save(metricsSession);
    }

    @Override
    public List<MetricsSession> listSnapshots() {
        return jpaMetricsSessionRepository.findAll();
    }

    @Override
    public Optional<MetricsSession> getSnapshotById(Long id) {
        return jpaMetricsSessionRepository.findById(id);
    }

    @Override
    public void deleteSnapshotById(Long id) {
        jpaMetricsSessionRepository.deleteById(id);
    }
}
