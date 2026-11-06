package com.tienda.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tienda.demo.entity.Roles;
import com.tienda.demo.enums.RoleName;
import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    
    Optional<Roles> findByName(RoleName name);
}
