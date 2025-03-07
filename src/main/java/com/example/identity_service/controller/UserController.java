package com.example.identity_service.controller;

import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.dto.request.UserCreatingRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.APIResponse;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.service.UserService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    @GetMapping("/getUsers")
    public APIResponse<?> ListUsers(){
        return APIResponse.<List<User>>builder().message("ok").data(userService.getListUsers()).build();
    }

    @PostMapping("/add")
    public APIResponse<?> addUser(@RequestBody UserCreatingRequest userCreatingRequest){
        return APIResponse.<UserResponse>builder().message("ok").data(userService.addUser(userCreatingRequest)).build();
    }

    @PutMapping("/update")
    public APIResponse<?> updateUser(@RequestBody UserUpdateRequest userUpdateRequest){
        return APIResponse.<UserResponse>builder().message("ok").data(userService.updateUser(userUpdateRequest)).build();
    }

    @GetMapping("/myInfo")
    public APIResponse<?> myInfo() throws ParseException, JOSEException {
        return APIResponse.<UserResponse>builder().data(userService.MyInfo()).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        if (!userService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userService.delete(id);
        return ResponseEntity.ok("User deleted successfully");
    }


}
