
package com.tenantmanagement.rent_management.Repository;

import com.tenantmanagement.rent_management.Document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    //    public Boolean MobileNumber(String number);
    public boolean existsByMobileNumber(String mobileNumber);

    public boolean existsByEmail(String email);

    public  Optional<User> findByEmail(String email);

    public Optional<User> findById(String Id);
}