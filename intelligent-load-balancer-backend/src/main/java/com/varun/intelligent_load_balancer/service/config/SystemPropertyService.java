package com.varun.intelligent_load_balancer.service.config;

import com.varun.intelligent_load_balancer.exception.InvalidPropertyException;
import com.varun.intelligent_load_balancer.model.config.SystemProperty;
import com.varun.intelligent_load_balancer.repository.config.SystemPropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SystemPropertyService {

    private final SystemPropertyRepository systemPropertyRepository;

    public SystemPropertyService(SystemPropertyRepository systemPropertyRepository){
        this.systemPropertyRepository = systemPropertyRepository;
    }

    public List<SystemProperty> getAllProperties(){
        return systemPropertyRepository.getAllProperties();
    }

    public Optional<SystemProperty> getPropertyById(Long id){
        return systemPropertyRepository.getSystemPropertyById(id);
    }

    public Optional<SystemProperty> getPropertyByKey(String key){
        return systemPropertyRepository.getSystemPropertyByKey(key);
    }

    public SystemProperty saveProperty(SystemProperty systemProperty, boolean isNew){
        validateNewSystemProperty(systemProperty, isNew);
        return systemPropertyRepository.saveSystemProperty(systemProperty);
    }

    public boolean checkPropertyExistsById(Long id){
        return systemPropertyRepository.checkPropertyExistsById(id);
    }

    private void validateNewSystemProperty(SystemProperty property, boolean isNew){
        String key = property.getKey();

        if(isNew && systemPropertyRepository.checkPropertyExistsByKey(key)){
            throw new InvalidPropertyException("Given Property Key: " + key + " already exists. Please enter a new Property Key");
        }

        if(key.isEmpty()){
            throw new InvalidPropertyException("Key cannot be empty, please provide a Unique Key");
        }

    }

}
