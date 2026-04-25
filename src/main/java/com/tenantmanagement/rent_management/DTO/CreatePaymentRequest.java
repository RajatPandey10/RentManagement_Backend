package com.tenantmanagement.rent_management.DTO;

import lombok.*;

@Data

public class CreatePaymentRequest {

    private Double amount;
    private Long billId;

}
