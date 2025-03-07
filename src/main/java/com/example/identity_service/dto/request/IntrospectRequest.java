package com.example.identity_service.dto.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntrospectRequest {
    private String token;
}
