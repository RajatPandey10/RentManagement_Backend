package com.tenantmanagement.rent_management.Document;

import com.tenantmanagement.rent_management.Enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mobileNumber;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;

    private int rentAmount;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;



    private double electricityRatePerUnt;


    private LocalDateTime createdAt;
    @PostPersist
    public void createAt(){
        createdAt = LocalDateTime.now();
    }

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<Bill> bills;

    @OneToMany(mappedBy = "user")
    private List<Complaints> complaints;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

}
