package com.example.identity_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.Set;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Role {
    @Id
    private String name;
    private String description;

    @ManyToMany // mối quan hệ nhiều nhiều
    Set<Permission> permissions;
}
