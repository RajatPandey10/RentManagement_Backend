package com.tenantmanagement.rent_management.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateBillRequest {

    private String userId;
    private int month;
    private int year;

    private double rentAmount;
    private int previousReading;
    private int currentReading;

    private double ratePerUnit;
//    private LocalDateTime dueDate;

}
