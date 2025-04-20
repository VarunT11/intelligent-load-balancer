package com.varun.intelligent_load_balancer.repository.routes.postgres;

import com.varun.intelligent_load_balancer.model.routes.RouteDefinition;
import com.varun.intelligent_load_balancer.repository.routes.RouteDefinitionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("postgresRouteDefinitionRepository")
public class PostgresRouteDefinitionRepository implements RouteDefinitionRepository {

    private final JpaRouteDefinitionRepository jpaRepository;

    public PostgresRouteDefinitionRepository(JpaRouteDefinitionRepository jpaRepository){
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<RouteDefinition> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<RouteDefinition> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<RouteDefinition> findByPath(String path) {
        return jpaRepository.findByRoutePath(path);
    }

    @Override
    public RouteDefinition save(RouteDefinition routeDefinition) {
        return jpaRepository.save(routeDefinition);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteByPath(String path) {
        jpaRepository.deleteByRoutePath(path);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByPath(String path) {
        return jpaRepository.existsByRoutePath(path);
    }
}
