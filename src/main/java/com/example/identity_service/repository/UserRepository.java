package com.example.identity_service.repository;

import com.example.identity_service.dto.request.UserRequest;
import com.example.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
