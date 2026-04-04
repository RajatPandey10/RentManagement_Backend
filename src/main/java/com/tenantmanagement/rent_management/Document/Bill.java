package com.tenantmanagement.rent_management.Document;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "bills")
public class Bill {

    @Id
    private String id;

    private String userId;

    private int month;
    private int year;

    // RENT
    private double rentAmount;

    // ELECTRICITY
    private int previousReading;
    private int currentReading;
    private int unitsConsumed;
    private double ratePerUnit;
    private double electricityAmount;

    // CALCULATIONS
    private double totalAmount;
    private double discountAmount;
    private double penaltyAmount;

    // PAYMENT TRACKING
    private double paidAmount;
    private double remainingAmount;
    private double carryForwardAmount;

    // STATUS
    private Status status;

    public enum Status {
        PAID,
        PARTIAL,
        UNPAID,
        PENDING_VERIFICATION
    }

    // FLAGS
    private boolean penaltyRemovedByAdmin;

    private LocalDateTime dueDate;

    @CreatedDate
    private LocalDateTime generatedAt;
}