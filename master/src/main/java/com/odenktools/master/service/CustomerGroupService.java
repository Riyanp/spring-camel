package com.odenktools.master.service;

import com.odenktools.common.dto.CustomerGroupDto;
import com.odenktools.common.dto.GroupDto;
import com.odenktools.common.model.CustomerGroup;
import com.odenktools.common.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface CustomerGroupService {

    Boolean existsByGroupId(String group_id);

    Boolean existsByCustomerId(String customer_id);

	Optional<CustomerGroup> findById(String id);

	Page<CustomerGroup> findAllBy(String id, Sort sort, Pageable pageable);

	/**
	 * Register a new Customer.
	 *
	 * @param request CustomerDTO.
	 */
    CustomerGroupDto registerCustomerGroup(CustomerGroupDto request);

    /**
     * Update existing Group.
     * @param request
     * @return
     */
    CustomerGroupDto updateCustomerGroup(CustomerGroupDto request);
}
