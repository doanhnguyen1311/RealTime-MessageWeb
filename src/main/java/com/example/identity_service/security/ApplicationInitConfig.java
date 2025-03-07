package com.example.identity_service.security;


import com.example.identity_service.entity.Role;
import com.example.identity_service.entity.Roles;
import com.example.identity_service.entity.User;
import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ApplicationInitConfig {

    @Autowired private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner init(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if(!userRepository.existsByUsername("admin")) {
                var role = new HashSet<Role>();
                var adminRole = roleRepository.findByName(Roles.ADMIN.name());

                if(adminRole != null){
                    role.add(adminRole);
                }

                var user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("123"))
                        .roles(role)
                        .build();

                userRepository.save(user);
            }
        };
    }
}
