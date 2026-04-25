package com.tenantmanagement.rent_management.Document;



import com.tenantmanagement.rent_management.Enums.BillStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int month;
    private int year;

    // RENT
    private double rentAmount;

    // ELECTRICITY
    private int previousReading;
    private int currentReading;
    private int unitsConsumed;
    private double ratePerUnit;
    private double electricityAmount;

    // CALCULATIONS
    private double totalAmount;
    private double discountAmount;
    private double penaltyAmount;

    // PAYMENT TRACKING
    private double paidAmount;
    private double remainingAmount;
    private double carryForwardAmount;

    // STATUS
    @Enumerated(EnumType.STRING)
    private BillStatus status;


    // FLAGS
    private boolean penaltyRemovedByAdmin;

    private LocalDateTime dueDate;


    private LocalDateTime generatedAt;


    @PrePersist
    public void onCreate() {
        generatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "bill")
    private List<Payment> payments;
}