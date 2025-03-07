package com.example.identity_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Permission {
    @Id
    private String name;

    private String description;
}
