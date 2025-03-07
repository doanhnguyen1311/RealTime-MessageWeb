package com.example.identity_service.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntrospectResponse {
    boolean valid;
}
