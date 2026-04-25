package com.tenantmanagement.rent_management.DTO;

import com.tenantmanagement.rent_management.Document.Bill;
import com.tenantmanagement.rent_management.Enums.BillStatus;
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

    private Long id;
    private Long userId;
    private int month;
    private int year;

    private LocalDateTime dueDate;

    private double rentAmount;
    private double electricityAmount;

    private double totalAmount;

    private BillStatus status;


}
