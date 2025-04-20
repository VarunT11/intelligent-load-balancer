package com.varun.intelligent_load_balancer.controller.admin;

import com.varun.intelligent_load_balancer.dto.routes.RouteDefinitionRequestDTO;
import com.varun.intelligent_load_balancer.dto.routes.RouteDefinitionResponseDTO;
import com.varun.intelligent_load_balancer.mapper.RouteDefinitionMapper;
import com.varun.intelligent_load_balancer.model.routes.RouteDefinition;
import com.varun.intelligent_load_balancer.service.routes.RouteDefinitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/routes*")
public class RouteDefinitionController {

    private final RouteDefinitionService routeDefinitionService;

    public RouteDefinitionController(RouteDefinitionService routeDefinitionService) {
        this.routeDefinitionService = routeDefinitionService;
    }

    @GetMapping("")
    public ResponseEntity<List<RouteDefinitionResponseDTO>> getAllRoutes() {
        List<RouteDefinitionResponseDTO> routesResponse =
                routeDefinitionService
                        .getAllRoutes().stream()
                        .map(RouteDefinitionMapper::toResponseDTO)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(routesResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDefinitionResponseDTO> getRoute(@PathVariable Long id) {
        Optional<RouteDefinition> routeDefinition = routeDefinitionService.getRouteById(id);
        return routeDefinition.map(definition -> ResponseEntity.ok(RouteDefinitionMapper.toResponseDTO(definition))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<RouteDefinitionResponseDTO> addNewRoute(@RequestBody RouteDefinitionRequestDTO routeDefinitionRequestDTO) {
        RouteDefinition routeDefinition = RouteDefinitionMapper.toEntity(routeDefinitionRequestDTO);
        RouteDefinition updatedRoute = routeDefinitionService.saveRoute(routeDefinition, true);
        return ResponseEntity.ok(RouteDefinitionMapper.toResponseDTO(updatedRoute));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RouteDefinitionResponseDTO> updateExistingRoute(@PathVariable Long id, @RequestBody RouteDefinitionRequestDTO routeDefinitionRequestDTO) {
        if(!routeDefinitionService.checkRouteExists(id)){
            return ResponseEntity.notFound().build();
        }
        RouteDefinition routeDefinition = RouteDefinitionMapper.toEntity(routeDefinitionRequestDTO);
        routeDefinition.setId(id);
        RouteDefinition updatedRoute = routeDefinitionService.saveRoute(routeDefinition, false);

        return ResponseEntity.ok(RouteDefinitionMapper.toResponseDTO(updatedRoute));
    }

    @DeleteMapping("/{id}")
    public void deleteRoute(@PathVariable Long id) {
        routeDefinitionService.deleteRouteById(id);
    }

}
