package com.varun.intelligent_load_balancer.repository.config.postgres;

import com.varun.intelligent_load_balancer.model.config.SystemProperty;
import com.varun.intelligent_load_balancer.repository.config.SystemPropertyRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("postgresSystemPropertyRepository")
public class PostgresSystemPropertyRepository implements SystemPropertyRepository {

    private final JpaSystemPropertyRepository jpaSystemPropertyRepository;

    public PostgresSystemPropertyRepository(JpaSystemPropertyRepository jpaSystemPropertyRepository){
        this.jpaSystemPropertyRepository = jpaSystemPropertyRepository;
    }

    @Override
    public List<SystemProperty> getAllProperties() {
        return jpaSystemPropertyRepository.findAll();
    }

    @Override
    public Optional<SystemProperty> getSystemPropertyById(Long id) {
        return jpaSystemPropertyRepository.findById(id);
    }

    @Override
    public Optional<SystemProperty> getSystemPropertyByKey(String key) {
        return jpaSystemPropertyRepository.findByKey(key);
    }

    @Override
    public SystemProperty saveSystemProperty(SystemProperty systemProperty) {
        return jpaSystemPropertyRepository.save(systemProperty);
    }

    @Override
    public boolean checkPropertyExistsById(Long id) {
        return jpaSystemPropertyRepository.existsById(id);
    }

    @Override
    public boolean checkPropertyExistsByKey(String key) {
        return jpaSystemPropertyRepository.existsByKey(key);
    }
}
