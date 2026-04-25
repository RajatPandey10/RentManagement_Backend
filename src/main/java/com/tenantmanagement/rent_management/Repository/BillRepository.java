package com.tenantmanagement.rent_management.Repository;

import com.tenantmanagement.rent_management.Document.Bill;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill,Long> {

    public List<Bill> findByUserId(Long userId);

    public boolean findByMonth(int month);
    public boolean existsByMonthAndYear(int year, int months);

}
