package com.tenantmanagement.rent_management.service;

import com.tenantmanagement.rent_management.DTO.BillResponse;
import com.tenantmanagement.rent_management.DTO.CreateBillRequest;
import com.tenantmanagement.rent_management.Document.Bill;
import com.tenantmanagement.rent_management.Document.User;
import com.tenantmanagement.rent_management.Enums.BillStatus;
import com.tenantmanagement.rent_management.Repository.BillRepository;
import com.tenantmanagement.rent_management.Repository.UserRepository;
import com.tenantmanagement.rent_management.exception.BadRequestException;
import com.tenantmanagement.rent_management.exception.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillService {

    private final UserRepository userRepository;

    private final BillRepository billRepository;

    public List<BillResponse> toResponse(Long userId){
        List<Bill> bills = billRepository.findByUserId(userId);
        return bills.stream().map((bill)-> BillResponse.builder()
                .id(bill.getId())
                .userId(userId)
                .month(bill.getMonth())
                .year(bill.getYear())
                .dueDate(bill.getDueDate())
                .rentAmount(bill.getRentAmount())
                .totalAmount(bill.getTotalAmount())
                .electricityAmount(bill.getElectricityAmount())
                .status(BillStatus.UNPAID)
                .build()
        ).toList();

    }

    public Double electricityAmount(CreateBillRequest request){
        return (request.getCurrentReading()-request.getPreviousReading())*request.getRatePerUnit();
    }

    public Double totalAmount(CreateBillRequest request){
        return electricityAmount(request) + request.getRentAmount();
    }

    public List<BillResponse> getBillByUserId(Long userId){
        User existingUser = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found for userId "+userId));

        return toResponse(userId);
    }

    public List<BillResponse> createBillRequest(CreateBillRequest request){
        User existingUser = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new  ResourceNotFoundException("User not found for userId "+request.getUserId()));

        int units = request.getCurrentReading() - request.getPreviousReading();
        double electricity = units * request.getRatePerUnit();
        double total = electricity + request.getRentAmount();

        boolean exists = billRepository.existsByMonthAndYear(request.getMonth(), request.getYear());
        if (exists) {
            throw new BadRequestException("Bill already generated for this month and year");
        }

        Bill newBill = Bill.builder()
                .month(request.getMonth())
                .user(existingUser)
                .year(request.getYear())

                // RENT + ELECTRICITY
                .rentAmount(request.getRentAmount())
                .currentReading(request.getCurrentReading())
                .previousReading(request.getPreviousReading())
                .unitsConsumed(units)
                .ratePerUnit(request.getRatePerUnit())
                .electricityAmount(electricity)

                // TOTAL
                .totalAmount(total)
                .penaltyAmount(0)
                .discountAmount(0)

                // PAYMENT TRACKING (CRITICAL)
                .paidAmount(0)
                .remainingAmount(total)
                .carryForwardAmount(0)

                // STATUS
                .status(BillStatus.UNPAID)

                .penaltyRemovedByAdmin(false)
                .dueDate(LocalDateTime.now().plusDays(10))
                .build();
        billRepository.save(newBill);


        return toResponse(request.getUserId());

    }



}
