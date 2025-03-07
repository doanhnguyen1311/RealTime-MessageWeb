package com.example.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.Token;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String username;
    private String password;
    private String address;
    private String name;
    private LocalDate dob;

    @ManyToMany
    private Set<Role> roles;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RefreshToken> token;
}
