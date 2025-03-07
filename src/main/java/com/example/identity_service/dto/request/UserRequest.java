package com.example.identity_service.dto.request;

import com.example.identity_service.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Data
@Builder
public class UserRequest {
    private String id;
    private String username;
    private String address;
    private String name;
    private LocalDate dob;

    private Set<Role> roles;
}
