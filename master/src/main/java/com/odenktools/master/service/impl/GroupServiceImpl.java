package com.odenktools.master.service.impl;

import com.mysema.query.jpa.impl.JPAQuery;
import com.odenktools.common.dto.GroupDto;
import com.odenktools.common.filter.QueryFilter;
import com.odenktools.common.model.Group;
import com.odenktools.common.model.QGroup;
import com.odenktools.common.response.RestResponsePage;
import com.odenktools.master.repository.GroupRepository;
import com.odenktools.master.service.GroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Service
public class GroupServiceImpl extends QueryFilter<Group> implements GroupService {

    private final GroupRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public GroupServiceImpl(GroupRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Boolean existsByName(String name) {
        return this.repository.existsByName(name);
    }

    @Override
    @Transactional
    public Boolean existsById(String id) {
        return this.repository.existsById(id);
    }

    @Override
    @Transactional
    public Optional<Group> findById(String id) {
        return this.repository.findById(id);
    }

    @Override
    @Transactional
    public Page<Group> findAllBy(String name, Sort sort, Pageable pageable) {
        QGroup qGroup = QGroup.group;
        JPAQuery query = new JPAQuery(this.entityManager).from(qGroup);
        if (name != null && !name.isEmpty()) {
            query.where(qGroup.name.containsIgnoreCase(name));
        }
        long total = query.count();
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());
        for (Sort.Order order : sort) {
            query.orderBy(this.toOrderSpecifier(Group.class, qGroup.getMetadata(), order));
        }
        return new RestResponsePage<>(query.list(qGroup), pageable, total);
    }

    @Override
    @Transactional
    public GroupDto createGroup(GroupDto request) {
        Group group = Group.builder()
                .name(request.getName())
                .code(request.getCode())
                .build();

        // -- save
        request.setId(repository.save(group).getId());

        return request;
    }

    @Override
    @Transactional
    public GroupDto updateGroup(GroupDto request) {
        Group group = repository.findById(request.getId()).orElseThrow(RuntimeException::new);
        group.setName(request.getName());
        group.setCode(request.getCode());

        return request;
    }

    @Override
    @Transactional
    public Boolean softDeleteGroup(String id) {
        Optional<Group> groupOptional = repository.findById(id);
        if(groupOptional.isPresent()) {
            Group group = groupOptional.get();
            group.setIsDeleted(1);
            repository.save(group);

            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean hardDeleteGroup(String id) {
        Optional<Group> groupOptional = repository.findById(id);
        if(groupOptional.isPresent()) {
            Group group = groupOptional.get();
            group.setIsDeleted(1);
            repository.delete(group);

            return true;
        }
        return false;
    }
}
