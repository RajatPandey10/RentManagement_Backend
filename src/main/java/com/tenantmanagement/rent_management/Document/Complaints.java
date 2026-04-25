package com.tenantmanagement.rent_management.Document;


import com.tenantmanagement.rent_management.Enums.ComplaintStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "complaints")
public class Complaints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private ComplaintStatus status;

    @CreatedDate
    private LocalDateTime createdAt;


    private LocalDateTime resolveAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }


}
