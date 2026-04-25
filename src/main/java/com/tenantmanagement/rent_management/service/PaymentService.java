package com.tenantmanagement.rent_management.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.tenantmanagement.rent_management.Document.Bill;
import com.tenantmanagement.rent_management.Document.Payment;
import com.tenantmanagement.rent_management.Enums.BillStatus;
import com.tenantmanagement.rent_management.Enums.PaymentMode;
import com.tenantmanagement.rent_management.Enums.PaymentStatus;
import com.tenantmanagement.rent_management.Repository.BillRepository;
import com.tenantmanagement.rent_management.Repository.PaymentRepository;
import com.tenantmanagement.rent_management.exception.BadRequestException;
import com.tenantmanagement.rent_management.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;

    @Value("${razorpay.key.id}")
    public String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    public String razorpaySecretKey;



    public Payment createUpiPayment(Long billId, Double amount) throws RazorpayException {

        Bill bill = billRepository.findById(billId)
                .orElseThrow(()-> new ResourceNotFoundException("Bill not found for billId "+billId));

        if(bill.getRemainingAmount()==0){
            throw new BadRequestException("Your Rent is already paid");
        }

        int amountInPaise = (int)(amount*100);

        RazorpayClient client = new RazorpayClient(razorpayKeyId,razorpaySecretKey);

        JSONObject request = new JSONObject();

        request.put("amount",amountInPaise);
        request.put("currency","INR");
        request.put("receipt",String.valueOf(billId));

        Order order = client.orders.create(request);

        Payment payment = Payment.builder()
                .user(bill.getUser())
                .bill(bill)
                .amount(amount)
                .currency("INR")
                .paymentMode(PaymentMode.UPI)
                .paymentStatus(PaymentStatus.PENDING)
                .razorpayOrderId(order.get("id"))

                .build();

        return paymentRepository.save(payment);

    }


    public boolean verifyUpiPayment(String razorpayOrderId,
                                 String razorpayPaymentId,
                                 String razorpaySignature) throws RazorpayException{


        try{
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id",razorpayOrderId);
            attributes.put("razorpay_payment_id",razorpayPaymentId);
            attributes.put("razorpay_signature",razorpaySignature);

            boolean isValidSignature = Utils.verifyPaymentSignature(attributes,razorpaySecretKey);

            if(isValidSignature){
                Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                        .orElseThrow(()-> new BadRequestException("Payment not found"));
                payment.setRazorpayPaymentId(razorpayPaymentId);
                payment.setRazorpaySignature(razorpaySignature);

                payment.setPaymentMode(PaymentMode.UPI);
                payment.setPaidAt(LocalDateTime.now());
                Payment paidPayment = paymentRepository.save(payment);

                updateBillAfterPayment(payment.getBill().getId(),paidPayment.getAmount());
                return true;
            }
            return false;



        }catch (Exception e){
            return false;
        }
    }

    public Payment createCashPayment(Long billId, Double amount){

        Bill bill = billRepository.findById(billId)
                .orElseThrow(()-> new ResourceNotFoundException("Bill not found for Bill Id: "+billId));


        Double totalReserved = paymentRepository.getTotalActivePaymentsByBillId(billId);

        double remaining = bill.getTotalAmount() - totalReserved;

        if (remaining <= 0) {
            throw new BadRequestException("Bill already fully paid");
        }

        if (amount > remaining) {
            throw new BadRequestException("Amount exceeds remaining bill amount");
        }


        Payment payment = Payment.builder()
                .user(bill.getUser())
                .bill(bill)
                .amount(amount)
                .currency("INR")
                .paymentMode(PaymentMode.CASH)
                .paymentStatus(PaymentStatus.PENDING_VERIFICATION)
                .build();

        return paymentRepository.save(payment);
    }

    public void verifyCashPayment(Long paymentId){
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment Id is invalid"));

        PaymentMode  mode = payment.getPaymentMode();

        if(payment.getPaymentStatus() == PaymentStatus.SUCCESS){
            throw new BadRequestException("Already Verified");
        }

        if(mode == PaymentMode.UPI){
            throw  new BadRequestException("can't verified UPI pay method");
        }
        Bill bill = payment.getBill();

        double remaining = bill.getRemainingAmount();

        // 🔥 IMPORTANT CHECK
        if(payment.getAmount() > remaining){
            throw new BadRequestException("Verification exceeds remaining bill amount");
        }


        payment.setVerifiedByAdmin(true);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setVerifiedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        updateBillAfterPayment(payment.getBill().getId(),payment.getAmount());
    }

    public void updateBillAfterPayment(Long billId, Double amount) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(()-> new ResourceNotFoundException("Bill not found"));

        double newPaid = bill.getPaidAmount() + amount;
        bill.setPaidAmount(newPaid);;

        double remaining = bill.getTotalAmount() - newPaid;

        bill.setRemainingAmount(remaining);

        if(remaining==0){
            bill.setStatus(BillStatus.PAID);
        }else {
            bill.setStatus(BillStatus.PARTIAL);
        }

        billRepository.save(bill);
    }

}
