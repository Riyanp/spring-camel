package com.odenktools.master.repository;

import com.odenktools.common.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query
    boolean existsById(String id);

	Boolean existsByEmail(String emailAddress);

	Boolean existsByUsername(String username);

	Boolean existsByPhoneNumber(String phoneNumber);
}
