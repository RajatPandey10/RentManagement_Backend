package com.tenantmanagement.rent_management.service;

import com.tenantmanagement.rent_management.DTO.LoginRequest;
import com.tenantmanagement.rent_management.DTO.RegisterRequest;
import com.tenantmanagement.rent_management.DTO.UserResponse;
import com.tenantmanagement.rent_management.Document.User;
import com.tenantmanagement.rent_management.Repository.UserRepository;
import com.tenantmanagement.rent_management.exception.InvalidCredentialsException;
import com.tenantmanagement.rent_management.exception.ResourceExistsException;
import com.tenantmanagement.rent_management.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                .id(newUser.getId())
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
            throw new ResourceExistsException("Phone no is already exists");
        }
        User newUser = toDocument(request);
        userRepository.save(newUser);

        return toResponse(newUser);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();

    }

    public UserResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->  new ResourceNotFoundException("Email is invalid"));

        if(!user.getPassword().equals(request.getPassword())){
            throw new InvalidCredentialsException("Invalid password");
        }

        return toResponse(user);
    }

}
