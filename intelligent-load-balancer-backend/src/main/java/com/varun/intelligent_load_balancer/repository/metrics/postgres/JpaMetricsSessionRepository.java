package com.varun.intelligent_load_balancer.repository.metrics.postgres;

import com.varun.intelligent_load_balancer.model.metrics.MetricsSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMetricsSessionRepository extends JpaRepository<MetricsSession, Long> {
}
