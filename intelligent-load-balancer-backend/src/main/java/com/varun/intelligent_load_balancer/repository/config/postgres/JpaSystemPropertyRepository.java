package com.varun.intelligent_load_balancer.repository.config.postgres;

import com.varun.intelligent_load_balancer.model.config.SystemProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaSystemPropertyRepository extends JpaRepository<SystemProperty, Long> {
    Optional<SystemProperty> findByKey(String key);
    boolean existsByKey(String key);
}
