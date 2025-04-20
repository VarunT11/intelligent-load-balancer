package com.varun.intelligent_load_balancer.controller.admin;

import com.varun.intelligent_load_balancer.dto.config.SystemPropertyRequestDTO;
import com.varun.intelligent_load_balancer.dto.config.SystemPropertyResponseDTO;
import com.varun.intelligent_load_balancer.mapper.SystemPropertyMapper;
import com.varun.intelligent_load_balancer.model.config.SystemProperty;
import com.varun.intelligent_load_balancer.service.config.SystemPropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/configs")
public class AdminConfigController {

    private final SystemPropertyService systemPropertyService;

    public AdminConfigController(SystemPropertyService systemPropertyService){
        this.systemPropertyService = systemPropertyService;
    }

    @GetMapping("")
    public ResponseEntity<List<SystemPropertyResponseDTO>> getAllProperties(){
        List<SystemPropertyResponseDTO> allProperties = systemPropertyService.getAllProperties()
                .stream().map(SystemPropertyMapper::mapToResponseDTO)
                .toList();
        return ResponseEntity.ok(allProperties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SystemPropertyResponseDTO> getProperty(@PathVariable Long id){
        Optional<SystemProperty> property = systemPropertyService.getPropertyById(id);
        return property.map(systemProperty -> ResponseEntity.ok(SystemPropertyMapper.mapToResponseDTO(systemProperty))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<SystemPropertyResponseDTO> addNewProperty(@RequestBody SystemPropertyRequestDTO requestDTO){
        SystemProperty requestProperty = SystemPropertyMapper.mapToSystemProperty(requestDTO);
        SystemProperty newProperty = systemPropertyService.saveProperty(requestProperty, true);
        return ResponseEntity.ok(SystemPropertyMapper.mapToResponseDTO(newProperty));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SystemPropertyResponseDTO> updateProperty(@PathVariable Long id, @RequestBody SystemPropertyRequestDTO requestDTO){
        if(!systemPropertyService.checkPropertyExistsById(id)){
            return ResponseEntity.notFound().build();
        }
        SystemProperty requestProperty = SystemPropertyMapper.mapToSystemProperty(requestDTO);
        requestProperty.setId(id);
        SystemProperty updatedProperty = systemPropertyService.saveProperty(requestProperty, false);
        return ResponseEntity.ok(SystemPropertyMapper.mapToResponseDTO(updatedProperty));
    }

}
