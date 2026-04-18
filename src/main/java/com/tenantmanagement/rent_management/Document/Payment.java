package com.tenantmanagement.rent_management.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    private String userId;
    private String billId;

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    private String currency;

    private Double amount;



    private PaymentMode paymentMode;
    public enum PaymentMode{
        UPI,CASH
    }

    private String receipt;

    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    public enum PaymentStatus{
        PENDING,SUCCESS,FAILED,PENDING_VERIFICATION
    }

    private String transactionId;

    private LocalDateTime paidAt;

    @Builder.Default
    private Boolean verifiedByAdmin = false;

    private LocalDateTime verifiedAt;



}
