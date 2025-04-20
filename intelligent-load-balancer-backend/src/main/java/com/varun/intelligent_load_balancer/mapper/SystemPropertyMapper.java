package com.varun.intelligent_load_balancer.mapper;

import com.varun.intelligent_load_balancer.dto.config.SystemPropertyRequestDTO;
import com.varun.intelligent_load_balancer.dto.config.SystemPropertyResponseDTO;
import com.varun.intelligent_load_balancer.model.config.SystemProperty;

public class SystemPropertyMapper {

    public static SystemPropertyResponseDTO mapToResponseDTO(SystemProperty systemProperty){
        SystemPropertyResponseDTO responseDTO = new SystemPropertyResponseDTO();
        responseDTO.setId(systemProperty.getId());
        responseDTO.setKey(systemProperty.getKey());
        responseDTO.setValue(systemProperty.getValue());
        responseDTO.setDescription(systemProperty.getDescription());
        return responseDTO;
    }

    public static SystemProperty mapToSystemProperty(SystemPropertyRequestDTO requestDTO){
        SystemProperty systemProperty = new SystemProperty();
        systemProperty.setKey(requestDTO.getKey());
        systemProperty.setValue(requestDTO.getValue());
        systemProperty.setDescription("Added by ADMIN Panel");
        return systemProperty;
    }

}
