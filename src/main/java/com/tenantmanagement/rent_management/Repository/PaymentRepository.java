package com.tenantmanagement.rent_management.Repository;

import com.tenantmanagement.rent_management.Document.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaymentRepository extends MongoRepository<Payment,String> {

    public Optional<Payment> findByRazorpayOrderId(String orderId);
}
