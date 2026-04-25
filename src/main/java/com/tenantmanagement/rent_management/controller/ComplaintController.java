package com.tenantmanagement.rent_management.controller;

import com.tenantmanagement.rent_management.DTO.ComplaintRequest;
import com.tenantmanagement.rent_management.Document.Complaints;
import com.tenantmanagement.rent_management.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping("/createComplaint/{userId}")
    public ResponseEntity<?> createComplaint(@RequestBody ComplaintRequest request,
                                             @PathVariable Long userId){
        Complaints complaint = complaintService.createComplaint(userId, request.getTitle(), request.getDescription());

        Map<String,Object> response = Map.of(
                "userId",userId,
                "Title", complaint.getTitle(),
                "Description", complaint.getDescription()

        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/allComplaints")
    public ResponseEntity<?> getAllComplaints(){
        return ResponseEntity.status(HttpStatus.OK).body(complaintService.getAllComplaints());
    }

    @PostMapping("/updateComplaints/{complaintId}")
    public ResponseEntity<?> updateComplaint(@PathVariable Long complaintId){
        Complaints complaints = complaintService.updateThroughAdmin(complaintId);
        Map<String,Object> response = Map.of(
                "ComplaintId",complaintId,
                "Status", String.valueOf(complaints.getStatus())
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }




}
