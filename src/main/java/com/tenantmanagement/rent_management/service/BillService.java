package com.tenantmanagement.rent_management.service;

import com.tenantmanagement.rent_management.DTO.BillResponse;
import com.tenantmanagement.rent_management.DTO.CreateBillRequest;
import com.tenantmanagement.rent_management.Document.Bill;
import com.tenantmanagement.rent_management.Document.User;
import com.tenantmanagement.rent_management.Repository.BillRepository;
import com.tenantmanagement.rent_management.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class BillService {

    private final UserRepository userRepository;

    private final BillRepository billRepository;

    public Double electricityAmount(CreateBillRequest request){
        Double totalElectricityBill = (request.getCurrentReading()-request.getPreviousReading())*request.getRatePerUnit();
        return totalElectricityBill;
    }

    public Double totalAmount(CreateBillRequest request){
        Double totalBill = electricityAmount(request) + request.getRentAmount();
        return totalBill;
    }

    public List<BillResponse> getBillByUserId(String userId){
        return toResponse(userId);
    }

    public List<BillResponse> toResponse(String userId){
        List<Bill> bills = billRepository.findByUserId(userId);
        return bills.stream().map((bill)-> BillResponse.builder()
                .month(bill.getMonth())
                .year(bill.getYear())
                .dueDate(bill.getDueDate())
                .rentAmount(bill.getRentAmount())
                .totalAmount(bill.getTotalAmount())
                .build()
        ).toList();

    }

}
