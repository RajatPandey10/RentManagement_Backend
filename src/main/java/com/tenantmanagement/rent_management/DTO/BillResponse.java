package com.tenantmanagement.rent_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BillResponse {

    private String userId;
    private int month;
    private int year;

    private LocalDateTime dueDate;

    private double rentAmount;
    private double electricityAmount;

    private double totalAmount;

}
