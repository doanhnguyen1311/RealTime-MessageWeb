package com.example.identity_service.service;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.dto.request.RefreshRequest;
import com.example.identity_service.dto.response.AuthenticationResponse;
import com.example.identity_service.dto.response.IntrospectResponse;
import com.example.identity_service.entity.InvalidateToken;
import com.example.identity_service.entity.RefreshToken;
import com.example.identity_service.entity.User;
import com.example.identity_service.repository.InvalidateRepository;
import com.example.identity_service.repository.RefreshRepository;
import com.example.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    UserRepository userRepository;
    InvalidateRepository invalidateRepository;
    RefreshRepository refreshRepository;
    @NonFinal
    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Autowired
    public AuthenticationService(UserRepository userRepository, InvalidateRepository invalidateRepository, RefreshRepository refreshRepository) {
        this.userRepository = userRepository;
        this.invalidateRepository = invalidateRepository;
        this.refreshRepository = refreshRepository;
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        var signJWT = verifyToken(token, true);

        boolean isValid = invalidateRepository.existsById(signJWT.getJWTClaimsSet().getJWTID());

        boolean valid = false;

        if(!isValid){
            valid = true;
        }

        try{
            verifyToken(token, false);
        }
        catch(Exception e){
            isValid = false;
        }
        return IntrospectResponse.builder().valid(valid).build();
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {

        boolean isExpiryTime = checkExpiryToken(request.getAccessToken());

        boolean checkRefreshToken = checkRefreshToken(request.getRefreshToken());

        boolean checkExpiryRefreshToken = checkExpiryToken(request.getRefreshToken());

        log.info(String.valueOf(checkRefreshToken));

        if(checkExpiryRefreshToken)
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập lại");
        }

        var signAccessToken =SignedJWT.parse(request.getAccessToken());
        var signRefreshToken =SignedJWT.parse(request.getRefreshToken());

        var claimRefresh = signRefreshToken.getJWTClaimsSet();

        var claimAccess = signAccessToken.getJWTClaimsSet();

        if(!((claimRefresh.getSubject().equals(claimAccess.getSubject())) &&
                (claimRefresh.getIssuer().equals(claimAccess.getIssuer()))
                && (claimRefresh.getClaim("scope").toString().equals(claimAccess.getClaim("scope").toString()))
        ))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Lỗi authentication ");
        }

            if (isExpiryTime && checkRefreshToken) {

                var signJWT = verifyToken(request.getAccessToken(), true);

                var jit = signJWT.getJWTClaimsSet().getJWTID();

                var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

                InvalidateToken invalidateToken = InvalidateToken.builder().expiryTime(expiryTime).id(jit).build();

                invalidateRepository.save(invalidateToken);

                var username = signJWT.getJWTClaimsSet().getSubject();

                var user = userRepository.findByUsername(username);

                if (user == null) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Loi authentication");
                }

                var token = generateToken(user);

                return AuthenticationResponse.builder().token(token).authenticated(true).build();
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token chua het han");
            }
    }


    public boolean checkRefreshToken(String token){
        return refreshRepository.existsByToken(token);
    }


    public void logout(IntrospectRequest request) throws ParseException, JOSEException {
        var signJWT = verifyToken(request.getToken(), true);

        var jit = signJWT.getJWTClaimsSet().getJWTID();

        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        InvalidateToken invalidateToken = InvalidateToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidateRepository.save(invalidateToken);

    }

    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SECRET_KEY);
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expriryTime =
                (isRefresh)
                ?
                        new Date(
                                signedJWT
                                        .getJWTClaimsSet()
                                        .getIssueTime()
                                        .toInstant()
                                        .plus(15, ChronoUnit.MINUTES)
                                .toEpochMilli()
                        )
                        : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);

        if(!(verified && expriryTime.after(new Date()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not Valid Token");
        }

        return signedJWT;

    }

    public boolean checkExpiryToken(String token) throws ParseException, JOSEException {
        var signJWT = SignedJWT.parse(token);

        Date expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        if (expiryTime == null) {
            return true;
        }


        return expiryTime.before(new Date());
    }

    @Transactional
    public AuthenticationResponse authenticated(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername());
        if(user == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Loi authentication");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        var authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(refreshRepository.existsByUserId(user.getId()))
            refreshRepository.deleteAllByUserId(user.getId());

        var refreshToken = generateRefreshToken(user);
        var beanRefresh = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryTime(Instant.now().plus(60, ChronoUnit.DAYS))
                .build();

        refreshRepository.save(beanRefresh);

        if(authenticated) {
            var token = generateToken(user);
            return AuthenticationResponse.builder()
                    .authenticated(authenticated)
                    .token(token)
                    .build();
        }
        else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Loi authentication");
        }
    }

    public String generateToken(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet =
                new JWTClaimsSet.Builder()
                        .subject(user.getUsername())
                        .issuer("User")
                        .issueTime(new Date())
                        .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)))
                        .jwtID(UUID.randomUUID().toString())
                        .claim("scope", buildScope(user))
                        .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try{
            jwsObject.sign(new MACSigner(SECRET_KEY));
            return jwsObject.serialize();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String generateRefreshToken(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet =
                new JWTClaimsSet.Builder()
                        .subject(user.getUsername())
                        .issuer("User")
                        .issueTime(new Date())
                        .expirationTime(Date.from(Instant.now().plus(365, ChronoUnit.DAYS)))
                        .jwtID(UUID.randomUUID().toString())
                        .claim("scope", buildScope(user))
                        .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try{
            jwsObject.sign(new MACSigner(SECRET_KEY));
            return jwsObject.serialize();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");

        for(var item : user.getRoles()){
            stringJoiner.add(item.getName());
        }
        return stringJoiner.toString();
    }

}
