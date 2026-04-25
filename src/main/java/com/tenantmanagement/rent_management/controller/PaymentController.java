package com.tenantmanagement.rent_management.controller;

import com.razorpay.RazorpayException;
import com.tenantmanagement.rent_management.DTO.CreatePaymentRequest;
import com.tenantmanagement.rent_management.Document.Payment;
import com.tenantmanagement.rent_management.Document.User;
import com.tenantmanagement.rent_management.service.PaymentService;
import com.tenantmanagement.rent_management.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/payment")
@AllArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;


    @PostMapping("/createPayment/{userId}")
    public ResponseEntity<?> createUpiPayment(@RequestBody CreatePaymentRequest request, @PathVariable Long userId) throws RazorpayException {

        if(userId==null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if(request.getAmount() == 0){
            return ResponseEntity.badRequest().body("Please Enter a valid amount");
        }
        Payment payment = paymentService.createUpiPayment(request.getBillId(), request.getAmount());

        Map<String,Object> response = Map.of(
                "paymentId",payment.getRazorpayOrderId(),
                "amount",payment.getAmount(),
                "currency",payment.getCurrency(),
                "receipt",payment.getBill().getId()
        );


        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping("/verifyPayment")
    public ResponseEntity<?> verifyUpiPayment(@RequestBody Map<String,String> request) throws RazorpayException {

        String razorpayOrderId = request.get("razorpay_order_id");
        String razorpayPaymentId = request.get("razorpay_payment_id");
        String razorpaySignature = request.get("razorpay_signature");

        if(Objects.isNull(razorpayOrderId)
            || Objects.isNull(razorpayPaymentId)
            || Objects.isNull(razorpaySignature)){

            return ResponseEntity.badRequest().body(Map.of("message","Missing Required parameter"));
        }

        boolean isValid = paymentService.verifyUpiPayment(razorpayOrderId,razorpayPaymentId,razorpaySignature);

        if(isValid){
            return ResponseEntity.ok(Map.of(
                    "message","Payment verified successfully",
                    "status","success"
            ));
        }else {
            return ResponseEntity.badRequest().body(Map.of(
                    "message","Payment verification failed"
            ));
        }

    }

    @PostMapping("/createCashPayment/{userId}")
    public ResponseEntity<?> createCashPayment(@RequestBody CreatePaymentRequest request, @PathVariable Long userId){

        if(userId==null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if(request.getAmount() == 0){
            return ResponseEntity.badRequest().body("Please Enter a valid amount");
        }

        Payment payment = paymentService.createCashPayment(request.getBillId(), request.getAmount());

        Map<String,Object> response = Map.of(
                "amount",payment.getAmount(),
                "currency",payment.getCurrency(),
                "receipt",payment.getBill().getId()
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/verifyCashPayment/{paymentId}")
    public ResponseEntity<?> verifyCashPayment(@PathVariable Long paymentId){
        if(paymentId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed");
        }
        paymentService.verifyCashPayment(paymentId);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully varified");
    }

}
