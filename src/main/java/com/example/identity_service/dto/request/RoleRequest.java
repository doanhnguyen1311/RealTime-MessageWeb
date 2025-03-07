package com.example.identity_service.dto.request;


import com.example.identity_service.entity.Permission;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Builder
@Getter
@Setter
public class RoleRequest {
    private String name;
    private String description;
    private Set<Permission> permissions;
}
