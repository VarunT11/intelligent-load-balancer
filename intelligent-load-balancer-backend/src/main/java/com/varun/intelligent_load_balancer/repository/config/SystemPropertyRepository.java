package com.varun.intelligent_load_balancer.repository.config;

import com.varun.intelligent_load_balancer.model.config.SystemProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemPropertyRepository {
    List<SystemProperty> getAllProperties();

    Optional<SystemProperty> getSystemPropertyById(Long id);
    Optional<SystemProperty> getSystemPropertyByKey(String key);

    SystemProperty saveSystemProperty(SystemProperty systemProperty);

    boolean checkPropertyExistsById(Long id);
    boolean checkPropertyExistsByKey(String key);
}
