package com.example.identity_service.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {
    private LocalDateTime time;
    private int code;
    private String message;
}
