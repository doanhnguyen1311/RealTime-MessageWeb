package com.example.identity_service.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)

public class APIResponse<T> {
    private int code = 1000;
    private String message;
    private T data;


}
