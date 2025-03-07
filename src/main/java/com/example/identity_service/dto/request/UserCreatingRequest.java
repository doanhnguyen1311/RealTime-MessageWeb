package com.example.identity_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

// con thieu validation
public class UserCreatingRequest {

    private String username;
    private String password;
    private String address;
    private String name;
    private LocalDate dob;
}
