package com.odenktools.customerservice.service;

import com.odenktools.common.dto.CustomerDto;
import com.odenktools.common.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface CustomerService {

	/**
	 * Verify email address is unique.
	 *
	 * @param email customer email.
	 * @return Boolean
	 */
	Boolean existsByEmailAddress(String email);

	/**
	 * Verify username is unique.
	 *
	 * @param username customer username
	 * @return Boolean
	 */
	Boolean existsByUsername(String username);

	Boolean existsByPhoneNumber(String phoneNumber);

	Optional<Customer> findById(String id);

	Page<Customer> findAllBy(String username, String email, String phoneNumber, Sort sort, Pageable pageable);

	/**
	 * Register a new Customer.
	 *
	 * @param request CustomerDTO.
	 */
	CustomerDto registerCustomer(CustomerDto request);
}
