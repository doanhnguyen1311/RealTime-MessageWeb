package com.example.identity_service.repository;

import com.example.identity_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);
}
