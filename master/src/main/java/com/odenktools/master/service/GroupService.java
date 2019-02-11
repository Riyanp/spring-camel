package com.odenktools.master.service;

import com.odenktools.common.dto.GroupDto;
import com.odenktools.common.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface GroupService {

	Boolean existsByName(String name);

	Boolean existsById(String id);

	Optional<Group> findById(String id);

	Page<Group> findAllBy(String name, Sort sort, Pageable pageable);

	/**
	 * Register a new Customer.
	 *
	 * @param request CustomerDTO.
	 */
	GroupDto createGroup(GroupDto request);

    /**
     * Update existing Group.
     * @param request
     * @return
     */
    GroupDto updateGroup(GroupDto request);

    Boolean softDeleteGroup(String id);

    Boolean hardDeleteGroup(String id);
}
