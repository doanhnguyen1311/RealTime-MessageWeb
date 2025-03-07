package com.example.identity_service.dto.response;

import com.example.identity_service.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserResponse {
        private String id;
        private String username;
        private String address;
        private String name;
        private LocalDate dob;
        private Set<Role> roles;
}
