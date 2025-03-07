package com.example.identity_service.repository;

import com.example.identity_service.entity.InvalidateToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidateRepository extends JpaRepository<InvalidateToken, String> {
}
