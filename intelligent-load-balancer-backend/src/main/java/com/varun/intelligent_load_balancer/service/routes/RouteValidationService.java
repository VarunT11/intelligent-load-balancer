package com.varun.intelligent_load_balancer.service.routes;

import com.varun.intelligent_load_balancer.exception.InvalidRouteDefinitionException;
import com.varun.intelligent_load_balancer.exception.NoHealthyBackendException;
import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;
import com.varun.intelligent_load_balancer.model.routes.LoadBalancingStrategyType;
import com.varun.intelligent_load_balancer.model.routes.RouteDefinition;
import com.varun.intelligent_load_balancer.repository.routes.RouteDefinitionRepository;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class RouteValidationService {

    private final RouteDefinitionRepository routeDefinitionRepository;

    public RouteValidationService(RouteDefinitionRepository routeDefinitionRepository){
        this.routeDefinitionRepository = routeDefinitionRepository;
    }

    public void validateRoute(RouteDefinition routeDefinition, boolean isNewRoute){
        validateRoutePath(routeDefinition.getRoutePath(), isNewRoute);
        validatePrimaryBackends(routeDefinition.getPrimaryBackends());
        validateCanaryBackends(routeDefinition.getCanaryBackends(), routeDefinition.getCanaryTrafficPercent());
        validateRouteStrategy(routeDefinition.getStrategy());
    }

    private void validateRoutePath(String path, boolean isNewRoute){
        if(!path.startsWith("/")){
            throw new InvalidRouteDefinitionException("RoutePath " + path + " does not starts with /");
        }

        if(isNewRoute && routeDefinitionRepository.existsByPath(path)){
            throw new InvalidRouteDefinitionException("RoutePath " + path + " already exists. Please use a different Route Path");
        }
    }

    private void validatePrimaryBackends(List<BackendDefinition> primaryBackends){
        if(primaryBackends.isEmpty()){
            throw new InvalidRouteDefinitionException("No Primary Backends found for the current Route. Please add at least 1 Backend to Continue");
        }

        for(BackendDefinition backendDefinition:primaryBackends){
            validateBackendDefinition(backendDefinition);
        }
    }

    private void validateCanaryBackends(List<BackendDefinition> canaryBackends, int canaryTrafficPercent){
        for(BackendDefinition backendDefinition:canaryBackends){
            validateBackendDefinition(backendDefinition);
        }

        int count = canaryBackends.size();
        if(canaryTrafficPercent>0 && count==0){
            throw new InvalidRouteDefinitionException("No Canary Backends found for the current Route when the Canary Traffic Percent > 0. Please add at least 1 Canary Backend to Continue");
        }
    }

    private void validateRouteStrategy(LoadBalancingStrategyType strategyType){
        if(strategyType==null){
            throw new InvalidRouteDefinitionException("No Load Balancer Strategy mentioned for the current Route. Please add a Strategy to continue");
        }
    }

    private void validateBackendDefinition(BackendDefinition backendDefinition){
        String id = backendDefinition.getId();
        String url = backendDefinition.getUrl();

        if(id.isEmpty()){
            throw new InvalidRouteDefinitionException("You must provide an Id with the Backend");
        }

        validateUrl(url);
    }

    private void validateUrl(String url){
        try {
            URL parsed = new URL(url);
            String protocol = parsed.getProtocol();
            if(!(protocol.equals("http") || protocol.equals("https"))){
                throw new MalformedURLException();
            }
        } catch (MalformedURLException e){
            throw new InvalidRouteDefinitionException("Provided URL: " + url + " is invalid");
        }
    }

}
