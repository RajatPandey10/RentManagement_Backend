package com.tenantmanagement.rent_management.Repository;

import com.tenantmanagement.rent_management.Document.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    public Optional<Payment> findByRazorpayOrderId(String orderId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.bill.id = :billId AND p.paymentStatus IN ('PENDING_VERIFICATION','SUCCESS')")
    Double getTotalActivePaymentsByBillId(Long billId);
}
