package com.tenantmanagement.rent_management.controller;

import com.tenantmanagement.rent_management.DTO.LoginRequest;
import com.tenantmanagement.rent_management.DTO.RegisterRequest;
import com.tenantmanagement.rent_management.DTO.UserResponse;
import com.tenantmanagement.rent_management.Document.User;
import com.tenantmanagement.rent_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest request){
        return ResponseEntity.status(201).body(userService.createUser(request));
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginRequest(@RequestBody LoginRequest request){
        return ResponseEntity.ok(userService.login(request));
    }



    @GetMapping()
    public ResponseEntity<?> getALlUsers(){
        try{
            List<User> users = userService.getAllUser();
            return ResponseEntity.ok(users);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }
}


