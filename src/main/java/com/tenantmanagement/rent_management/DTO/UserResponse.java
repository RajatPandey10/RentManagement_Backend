package com.tenantmanagement.rent_management.DTO;

import com.tenantmanagement.rent_management.Document.User;
import com.tenantmanagement.rent_management.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String mobileNumber;
    private String email;
    private Role role;

}
