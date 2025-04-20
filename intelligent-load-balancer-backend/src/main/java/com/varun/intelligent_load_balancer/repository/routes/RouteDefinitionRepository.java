package com.varun.intelligent_load_balancer.repository.routes;

import com.varun.intelligent_load_balancer.model.routes.RouteDefinition;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteDefinitionRepository {
    List<RouteDefinition> findAll();

    Optional<RouteDefinition> findById(Long id);
    Optional<RouteDefinition> findByPath(String path);

    RouteDefinition save(RouteDefinition routeDefinition);

    void deleteById(Long id);
    void deleteByPath(String path);

    boolean existsById(Long id);
    boolean existsByPath(String path);
}
