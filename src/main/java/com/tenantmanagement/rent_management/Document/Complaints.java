package com.tenantmanagement.rent_management.Document;


import com.tenantmanagement.rent_management.Enums.ComplaintStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "complaints")
public class Complaints {

    @Id
    private String id;

    private String userId;
    private String title;
    private String description;

    private ComplaintStatus status;

    @CreatedDate
    private LocalDateTime createdAt;


    private LocalDateTime resolveAt;

}
