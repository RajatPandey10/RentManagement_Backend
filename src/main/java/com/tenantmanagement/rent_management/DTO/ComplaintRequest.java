package com.tenantmanagement.rent_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ComplaintRequest {
    private String title;
    private String description;
}
