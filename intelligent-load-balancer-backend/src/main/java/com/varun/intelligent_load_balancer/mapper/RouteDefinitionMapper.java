package com.varun.intelligent_load_balancer.mapper;

import com.varun.intelligent_load_balancer.dto.routes.BackendDefinitionDTO;
import com.varun.intelligent_load_balancer.dto.routes.RouteDefinitionRequestDTO;
import com.varun.intelligent_load_balancer.dto.routes.RouteDefinitionResponseDTO;
import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;
import com.varun.intelligent_load_balancer.model.routes.RouteDefinition;

import java.util.List;
import java.util.stream.Collectors;

public class RouteDefinitionMapper {

    public static RouteDefinition toEntity(RouteDefinitionRequestDTO dto){
        RouteDefinition entity = new RouteDefinition();
        entity.setRoutePath(dto.getRoutePath());
        entity.setPrimaryBackends(mapToBackendEntityList(dto.getPrimaryBackends()));
        entity.setCanaryBackends(mapToBackendEntityList(dto.getCanaryBackends()));
        entity.setCanaryTrafficPercent(dto.getCanaryTrafficPercent());
        entity.setStrategy(dto.getStrategyType());
        return entity;
    }

    public static RouteDefinitionResponseDTO toResponseDTO(RouteDefinition entity){
        RouteDefinitionResponseDTO dto = new RouteDefinitionResponseDTO();
        dto.setId(entity.getId());
        dto.setRoutePath(entity.getRoutePath());
        dto.setPrimaryBackends(mapToBackendDTOList(entity.getPrimaryBackends()));
        dto.setCanaryBackends(mapToBackendDTOList(entity.getCanaryBackends()));
        dto.setCanaryTrafficPercent(entity.getCanaryTrafficPercent());
        dto.setStrategyType(entity.getStrategy());
        return dto;
    }

    private static List<BackendDefinition> mapToBackendEntityList(List<BackendDefinitionDTO> dtos){
        return dtos == null ? List.of() : dtos.stream()
                .map(dto -> new BackendDefinition(dto.getId(), dto.getUrl()))
                .collect(Collectors.toList());
    }

    private static List<BackendDefinitionDTO> mapToBackendDTOList(List<BackendDefinition> backends){
        return backends == null ? List.of() : backends.stream()
                .map(backend -> new BackendDefinitionDTO(backend.getId(), backend.getUrl()))
                .collect(Collectors.toList());
    }

}
