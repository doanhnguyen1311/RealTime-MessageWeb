package com.example.identity_service.repository;

import com.example.identity_service.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshRepository extends JpaRepository<RefreshToken, String> {
    boolean existsByUserId(String userID);

    void deleteAllByUserId(String id);

    RefreshToken findByUserId(String id);

    boolean existsByToken(String token);
}
