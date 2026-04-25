package com.tenantmanagement.rent_management.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tenantmanagement.rent_management.Enums.PaymentMode;
import com.tenantmanagement.rent_management.Enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;


    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    private String currency;

    private Double amount;



    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;


    private String receipt;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;


    private String transactionId;


    private LocalDateTime paidAt;

    @PrePersist
    public void onCreate() {
        if (paidAt == null) {
            paidAt = LocalDateTime.now();
        }
    }

    @Builder.Default
    private Boolean verifiedByAdmin = false;

    private LocalDateTime verifiedAt;



}
