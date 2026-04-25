package com.tenantmanagement.rent_management.Repository;

import com.tenantmanagement.rent_management.Document.Complaints;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ComplaintRepository extends JpaRepository<Complaints,Long> {

}
