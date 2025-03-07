package com.example.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512)
    private String token;
    private Instant expiryTime;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

}
