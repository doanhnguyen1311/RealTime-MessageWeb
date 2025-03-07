package com.example.identity_service.dto.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshRequest {

    private String refreshToken;
    private String accessToken;
}
