package com.tenantmanagement.rent_management.Repository;

import com.tenantmanagement.rent_management.Document.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment,String> {
}
