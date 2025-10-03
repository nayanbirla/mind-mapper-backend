package com.mindmapper.repository;

import com.mindmapper.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Custom query methods can be defined here if needed
    // For example, to find a role by name:
    // Optional<Role> findByRoleName(String roleName);
}
