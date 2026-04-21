package com.tenantmanagement.rent_management.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.tenantmanagement.rent_management.Document.Bill;
import com.tenantmanagement.rent_management.Document.Payment;
import com.tenantmanagement.rent_management.Document.User;
import com.tenantmanagement.rent_management.Enums.BillStatus;
import com.tenantmanagement.rent_management.Enums.PaymentMode;
import com.tenantmanagement.rent_management.Enums.PaymentStatus;
import com.tenantmanagement.rent_management.Repository.BillRepository;
import com.tenantmanagement.rent_management.Repository.PaymentRepository;
import com.tenantmanagement.rent_management.Repository.UserRepository;
import com.tenantmanagement.rent_management.exception.BadRequestException;
import com.tenantmanagement.rent_management.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
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



    public Payment createUpiPayment(String billId, Double amount) throws RazorpayException {

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
        request.put("receipt",billId);

        Order order = client.orders.create(request);

        Payment payment = Payment.builder()
                .userId(bill.getUserId())
                .billId(billId)
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

                updateBillAfterPayment(payment.getBillId(),paidPayment.getAmount());
                return true;
            }
            return false;



        }catch (Exception e){
            return false;
        }
    }

    public Payment createCashPayment(String billId, Double amount){

        Bill bill = billRepository.findById(billId)
                .orElseThrow(()-> new ResourceNotFoundException("Bill not found for Bill Id: "+billId));

        Payment payment = Payment.builder()
                .userId(bill.getUserId())
                .billId(billId)
                .amount(amount)
                .currency("INR")
                .paymentMode(PaymentMode.CASH)
                .paymentStatus(PaymentStatus.PENDING_VERIFICATION)
                .build();

        return paymentRepository.save(payment);
    }

    public void verifyCashPayment(String paymentId){
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment Id is invalid"));

        payment.setVerifiedByAdmin(true);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setVerifiedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        updateBillAfterPayment(payment.getBillId(),payment.getAmount());
    }

    public void updateBillAfterPayment(String billId, Double amount) {
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
