package com.tenantmanagement.rent_management.DTO;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {

    private Double amount;
    private String billId;

}
