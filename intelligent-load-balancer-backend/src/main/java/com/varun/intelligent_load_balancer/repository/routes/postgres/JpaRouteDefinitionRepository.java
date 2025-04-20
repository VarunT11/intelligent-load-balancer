package com.varun.intelligent_load_balancer.repository.routes.postgres;

import com.varun.intelligent_load_balancer.model.routes.RouteDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRouteDefinitionRepository extends JpaRepository<RouteDefinition, Long> {
    Optional<RouteDefinition> findByRoutePath(String routePath);
    boolean existsByRoutePath(String routePath);
    void deleteByRoutePath(String routePath);
}
