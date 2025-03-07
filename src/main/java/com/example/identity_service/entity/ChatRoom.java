package com.example.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Tên nhóm chat

    @ManyToMany
    private Set<User> members;
}
