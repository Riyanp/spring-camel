package com.odenktools.master.service.impl;

import com.mysema.query.jpa.impl.JPAQuery;
import com.odenktools.common.dto.CustomerGroupDto;
import com.odenktools.common.filter.QueryFilter;
import com.odenktools.common.model.CustomerGroup;
import com.odenktools.common.model.QCustomerGroup;
import com.odenktools.common.response.RestResponsePage;
import com.odenktools.master.repository.CustomerGroupRepository;
import com.odenktools.master.repository.CustomerRepository;
import com.odenktools.master.repository.GroupRepository;
import com.odenktools.master.service.CustomerGroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Service
public class CustomerGroupServiceImpl extends QueryFilter<CustomerGroup> implements CustomerGroupService {

    private final CustomerGroupRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerGroupServiceImpl(CustomerGroupRepository repository, GroupRepository groupRepository, CustomerRepository customerRepository) {
        this.repository = repository;
    }

    @Override
    public Boolean existsByGroupId(String group_id) {
        return this.repository.existsByGroupId(group_id);
    }

    @Override
    public Boolean existsByCustomerId(String customer_id) {
        return this.repository.existsByCustomerId(customer_id);
    }

    @Override
    @Transactional
    public Optional<CustomerGroup> findById(String id) {
        return this.repository.findById(id);
    }

    @Override
    @Transactional
    public Page<CustomerGroup> findAllBy(String id, Sort sort, Pageable pageable) {
        QCustomerGroup qCustomerGroup = QCustomerGroup.customerGroup;
        JPAQuery query = new JPAQuery(this.entityManager).from(qCustomerGroup);
        if (id != null && !id.isEmpty()) {
            query.where(qCustomerGroup.id.containsIgnoreCase(id));
        }
        long total = query.count();
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());
        for (Sort.Order order : sort) {
            query.orderBy(this.toOrderSpecifier(CustomerGroup.class, qCustomerGroup.getMetadata(), order));
        }
        return new RestResponsePage<>(query.list(qCustomerGroup), pageable, total);
    }

    @Override
    public CustomerGroupDto registerCustomerGroup(CustomerGroupDto request) {
        CustomerGroup customerGroup = CustomerGroup.builder()
                .groupId(request.getGroupId())
                .customerId(request.getCustomerId())
                .build();

        // -- save
        request.setId(repository.save(customerGroup).getId());

        return request;
    }

    @Override
    public CustomerGroupDto updateCustomerGroup(CustomerGroupDto request) {
        return null;
    }

//    @Override
//    public CustomerGroupDto updateCustomerGroup(CustomerGroupDto request) {
//        return null;
//    }
//
//    @Override
//    @Transactional
//    public GroupDto createGroup(GroupDto request) {
//        Group group = Group.builder()
//                .name(request.getName())
//                .build();
//
//        // -- save
//        request.setId(repository.save(group).getId());
//
//        return request;
//    }
//
//    @Override
//    @Transactional
//    public GroupDto updateGroup(GroupDto request) {
//        Group group = repository.findById(request.getId()).orElseThrow(RuntimeException::new);
//
//        group.setName(request.getName());
//
//        return request;
//    }
}
