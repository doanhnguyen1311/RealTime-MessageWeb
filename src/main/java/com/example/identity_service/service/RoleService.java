package com.example.identity_service.service;

import com.example.identity_service.dto.request.RoleRequest;
import com.example.identity_service.dto.response.RoleResponse;
import com.example.identity_service.entity.Permission;
import com.example.identity_service.entity.Role;
import com.example.identity_service.mapper.RoleMapper;
import com.example.identity_service.repository.PermissionRepository;
import com.example.identity_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionRepository permissionRepository;

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    /*
    public RoleResponse createRole(RoleRequest roleRequest) {
        var role = roleMapper.toRole(roleRequest);

        Set<Permission> permissions = permissionRepository.findAllByName(roleRequest.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        if(roleRepository.existsByName(( roleRequest.getName()))) {
            throw new RuntimeException("Role name already exists");
        }

        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());

        return roleMapper.toRoleResponse(roleRepository.save(role));

    }*/

    public void deleteRole(String role) {
        roleRepository.deleteByName(role);
    }
}
