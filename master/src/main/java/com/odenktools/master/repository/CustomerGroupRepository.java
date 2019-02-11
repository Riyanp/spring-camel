package com.odenktools.master.repository;

import com.odenktools.common.model.CustomerGroup;
import com.odenktools.common.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, String> {

	Boolean existsByGroupId(String group_id);

	Boolean existsByCustomerId(String customer_id);

    Optional<CustomerGroup> findByGroupId(String group_id);
}
