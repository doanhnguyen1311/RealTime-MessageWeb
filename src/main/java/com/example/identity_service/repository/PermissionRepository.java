package com.example.identity_service.repository;

import com.example.identity_service.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
