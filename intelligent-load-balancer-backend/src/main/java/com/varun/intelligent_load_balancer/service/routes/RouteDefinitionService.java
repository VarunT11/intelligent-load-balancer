package com.varun.intelligent_load_balancer.service.routes;

import com.varun.intelligent_load_balancer.model.routes.RouteDefinition;
import com.varun.intelligent_load_balancer.repository.routes.RouteDefinitionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteDefinitionService {

    private final RouteDefinitionRepository routeDefinitionRepository;
    private final RouteValidationService routeValidationService;

    public RouteDefinitionService(RouteDefinitionRepository routeDefinitionRepository, RouteValidationService routeValidationService) {
        this.routeDefinitionRepository = routeDefinitionRepository;
        this.routeValidationService = routeValidationService;
    }

    public List<RouteDefinition> getAllRoutes() {
        return routeDefinitionRepository.findAll();
    }

    public Optional<RouteDefinition> getRouteById(Long id){
        return routeDefinitionRepository.findById(id);
    }

    public Optional<RouteDefinition> getRouteByName(String routeName) {
        return routeDefinitionRepository.findByPath(routeName);
    }

    public RouteDefinition saveRoute(RouteDefinition routeDefinition, boolean isNewRoute) {
        routeValidationService.validateRoute(routeDefinition, isNewRoute);
        return routeDefinitionRepository.save(routeDefinition);
    }

    public void deleteRouteById(Long id){
        routeDefinitionRepository.deleteById(id);
    }

    public void deleteRoute(String routeName) {
        routeDefinitionRepository.deleteByPath(routeName);
    }

    public boolean checkRouteExists(Long id){
        return routeDefinitionRepository.existsById(id);
    }

    public boolean checkRouteExists(String routeName) {
        return routeDefinitionRepository.existsByPath(routeName);
    }

    public String findMatchedPath(String path){
        List<RouteDefinition> routes = getAllRoutes();
        for(RouteDefinition route:routes){
            if(path.startsWith(route.getRoutePath())) return route.getRoutePath();
        }
        return null;
    }

}
