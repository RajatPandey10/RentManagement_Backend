package com.tenantmanagement.rent_management.service;

import com.tenantmanagement.rent_management.Document.Complaints;
import com.tenantmanagement.rent_management.Document.User;
import com.tenantmanagement.rent_management.Enums.ComplaintStatus;
import com.tenantmanagement.rent_management.Repository.ComplaintRepository;
import com.tenantmanagement.rent_management.Repository.PaymentRepository;
import com.tenantmanagement.rent_management.Repository.UserRepository;
import com.tenantmanagement.rent_management.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;



    public Complaints createComplaint(Long userId, String title, String description){
        User existingUser = userRepository.findById(userId).
                orElseThrow(()-> new ResourceNotFoundException("User not found for userId"+userId));

        Complaints complaint = Complaints.builder()
                .title(title)
                .description(description)
                .user(existingUser)
                .status(ComplaintStatus.OPEN)
                .build();

        complaintRepository.save(complaint);

//        sent message on whatsapp and email about complaints
        return complaint;

    }

    public List<Complaints> getAllComplaints(){
        return complaintRepository.findAll();
    }


    public Complaints updateThroughAdmin(Long complaintId){
        Complaints complaints = complaintRepository.findById(complaintId)
                .orElseThrow(()-> new ResourceNotFoundException("Complaint does not found!!"));

        complaints.setStatus(ComplaintStatus.RESOLVED);
        complaints.setResolveAt(LocalDateTime.now());
        complaintRepository.save(complaints);
        return complaints;
    }



}
