
package com.tenantmanagement.rent_management.Repository;

import com.tenantmanagement.rent_management.Document.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    //    public Boolean MobileNumber(String number);
    public boolean existsByMobileNumber(String mobileNumber);

    public boolean existsByEmail(String email);

    public  Optional<User> findByEmail(String email);

    public Optional<User> findById(Long Id);
}