package com.tenantmanagement.rent_management.Repository;

import com.tenantmanagement.rent_management.Document.Complaints;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ComplaintRepository extends MongoRepository<Complaints,String> {
}
