package com.tenantmanagement.rent_management.service;

import com.tenantmanagement.rent_management.DTO.RegisterRequest;
import com.tenantmanagement.rent_management.DTO.UserResponse;
import com.tenantmanagement.rent_management.Document.User;
import com.tenantmanagement.rent_management.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse toResponse(User newUser){
        return UserResponse.builder()
                .email(newUser.getEmail())
                .mobileNumber(newUser.getMobileNumber())
                .name(newUser.getName())
                .role(newUser.getRole())
                .build();
    }

    public User toDocument(RegisterRequest request){
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .mobileNumber(request.getMobileNumber())
                .role(User.Role.USER)
                .password(request.getPassword())
                .build();
    }

    public UserResponse createUser(RegisterRequest request){
        if(userRepository.existsByMobileNumber(request.getMobileNumber())){
            throw new RuntimeException("Phone no is already exists");
        }

        User newUser = toDocument(request);
        userRepository.save(newUser);

        return toResponse(newUser);
    }



}
