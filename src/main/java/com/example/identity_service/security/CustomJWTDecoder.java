package com.example.identity_service.security;


import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Slf4j
@Component
public class CustomJWTDecoder implements JwtDecoder {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var response =
                    authenticationService.introspect(IntrospectRequest.builder().token(token).build());
            log.error("Kiem tra token: {}", response);
            if (!response.isValid()) {
                throw new JwtException("token Invalid");
            }
        } catch (JwtException e) {
            throw new JwtException("loi: " + e.getMessage(), e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HS512");
            nimbusJwtDecoder =
                    NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}