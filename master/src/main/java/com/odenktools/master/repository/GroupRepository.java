package com.odenktools.master.repository;

import com.odenktools.common.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {

	Boolean existsByName(String name);
}
