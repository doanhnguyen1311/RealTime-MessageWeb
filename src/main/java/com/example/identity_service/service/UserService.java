package com.example.identity_service.service;


import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.dto.request.UserCreatingRequest;
import com.example.identity_service.dto.request.UserRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.Role;
import com.example.identity_service.entity.Roles;
import com.example.identity_service.entity.User;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.node_repository.UserNodeRepository;
import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserNodeRepository userNodeRepository;


    public List<User> getListUsers(){
        return userRepository.findAll();
    }

    public UserResponse addUser(UserCreatingRequest userRequest){
        boolean bool = userRepository.existsByUsername(userRequest.getUsername());

        User user = new User();

        Set<Role> roles = new HashSet<>();

        Role role = roleRepository.findByName(Roles.USER.name());

        if(role == null){
            throw  new RuntimeException("Role not found");
        }

        roles.add(role);

        user.setRoles(roles);

        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        if(bool){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username đã tồn tại");
        }
        else {
        userMapper.createUser(user, userRequest);
        var userResponse = userMapper.toUserResponse(userRepository.save(user));

        var userNode = userNodeRepository.createUserNode(userResponse.getId(),  userResponse.getName());


            return userResponse;
        }
    }


    // encode
    public String encode(String password){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.encode(password);
    }

    //checkPass
    public boolean checkPass(String rawPass, String encode){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(rawPass, encode);
    }

    public UserResponse updateUser(UserUpdateRequest userRequest){
        User user = userRepository.findByUsername(userRequest.getUsername());

        if(user.getUsername() == null){
            throw new RuntimeException("Sai thong tin");
        }
        else {
            userMapper.updateUser(user, userMapper.toUserUpdate(user));

            if(userRequest.getName() != null ){
                user.setName(userRequest.getName());
            }
            if(userRequest.getAddress() != null){
                user.setAddress(userRequest.getAddress());
            }
            if(userRequest.getDob() != null){
                user.setDob(userRequest.getDob());
            }

            return userMapper.toUserResponse(userRepository.save(user));
        }

    }

    public boolean existsById(String id){
        return userRepository.existsById(id);
    }

    public UserResponse getUserById(String id){
        User user = userRepository.findById(id) .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có user"));

        var userRes = userMapper.toUserResponse(user);

        return userRes;
    }

    public UserResponse MyInfo() throws ParseException, JOSEException {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        var user = userRepository.findByUsername(name);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có thông tin");
        }

        return userMapper.toUserResponse(user);
    }

    public void delete(String id){
        userRepository.deleteById(id);
    }

    @PostAuthorize("returnObject.username = authentication.name")
    public UserResponse getUserByID(String id){
        User user = userRepository.findById(id) .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có user"));

        return userMapper.toUserResponse(user);
    }
}
