package com.example.identity_service.controller;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.dto.request.RefreshRequest;
import com.example.identity_service.dto.response.APIResponse;
import com.example.identity_service.dto.response.AuthenticationResponse;
import com.example.identity_service.dto.response.IntrospectResponse;
import com.example.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public APIResponse<?> getToken(@RequestBody AuthenticationRequest request) {
        return APIResponse.<AuthenticationResponse>builder()
                .data(authenticationService.authenticated(request))
                .build();
    }

    @PostMapping("/introspect")
    public APIResponse<?> getIntrospectToken(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return APIResponse.<IntrospectResponse>builder().data(authenticationService.introspect(request)).build();
    }

    @PostMapping("/refreshToken")
    public APIResponse<?> refreshToken(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return APIResponse.<AuthenticationResponse>builder().data(authenticationService.refreshToken(request)).build();
    }

    @PostMapping("/logout")
    public APIResponse<Void> logout(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return APIResponse.<Void>builder().build();
    }
}
