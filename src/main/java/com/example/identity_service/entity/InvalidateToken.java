package com.example.identity_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InvalidateToken {

    @Id
    private String id;
    private Date expiryTime;
}
