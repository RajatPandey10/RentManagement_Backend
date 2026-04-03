package com.tenantmanagement.rent_management.controller;


import com.tenantmanagement.rent_management.DTO.CreateBillRequest;
import com.tenantmanagement.rent_management.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bill")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getBillByUserId(@PathVariable String userId ){
        return ResponseEntity.status(HttpStatus.OK).body(billService.getBillByUserId(userId));
    }

    @PostMapping("/CreateBill")
    public ResponseEntity<?> createBillRequest(@RequestBody CreateBillRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(billService.createBillRequest(request));
    }



}
