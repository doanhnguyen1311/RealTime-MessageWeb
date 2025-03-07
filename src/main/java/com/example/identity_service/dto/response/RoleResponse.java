package com.example.identity_service.dto.response;

import com.example.identity_service.entity.Permission;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class RoleResponse {
    private String name;
    private String description;
    private Set<Permission> permissions;
}
