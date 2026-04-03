package com.tenantmanagement.rent_management.service;

import com.tenantmanagement.rent_management.DTO.BillResponse;
import com.tenantmanagement.rent_management.DTO.CreateBillRequest;
import com.tenantmanagement.rent_management.Document.Bill;
import com.tenantmanagement.rent_management.Document.User;
import com.tenantmanagement.rent_management.Repository.BillRepository;
import com.tenantmanagement.rent_management.Repository.UserRepository;
import com.tenantmanagement.rent_management.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class BillService {

    private final UserRepository userRepository;

    private final BillRepository billRepository;

    public List<BillResponse> toResponse(String userId){
        List<Bill> bills = billRepository.findByUserId(userId);
        return bills.stream().map((bill)-> BillResponse.builder()
                .userId(userId)
                .month(bill.getMonth())
                .year(bill.getYear())
                .dueDate(bill.getDueDate())
                .rentAmount(bill.getRentAmount())
                .totalAmount(bill.getTotalAmount())
                .electricityAmount(bill.getElectricityAmount())
                .build()
        ).toList();

    }

    public Double electricityAmount(CreateBillRequest request){
        return (request.getCurrentReading()-request.getPreviousReading())*request.getRatePerUnit();
    }

    public Double totalAmount(CreateBillRequest request){
        return electricityAmount(request) + request.getRentAmount();
    }

    public List<BillResponse> getBillByUserId(String userId){
        User existingUser = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found for userId "+userId));

        return toResponse(userId);
    }

    public List<BillResponse> createBillRequest(CreateBillRequest request){
        User existingUser = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new  ResourceNotFoundException("User not found for userId "+request.getUserId()));

        Bill newBill = Bill.builder()
                .month(request.getMonth())
                .userId(request.getUserId())
                .year(request.getYear())
                .rentAmount(request.getRentAmount())
                .currentReading(request.getCurrentReading())
                .previousReading(request.getPreviousReading())
                .electricityAmount(electricityAmount(request))
                .totalAmount(totalAmount(request))
                .penaltyRemovedByAdmin(false)
                .ratePerUnit(request.getRatePerUnit())
                .penaltyAmount(0)
                .discountAmount(0)
                .status(Bill.Status.UNPAID)
                .dueDate(LocalDateTime.now().plusDays(10))
                .build();
        billRepository.save(newBill);


        return toResponse(request.getUserId());

    }



}
