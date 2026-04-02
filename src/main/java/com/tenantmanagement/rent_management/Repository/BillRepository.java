package com.tenantmanagement.rent_management.Repository;

import com.tenantmanagement.rent_management.Document.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BillRepository extends MongoRepository<Bill,String> {

    public List<Bill> findByUserId(String userId);
}
