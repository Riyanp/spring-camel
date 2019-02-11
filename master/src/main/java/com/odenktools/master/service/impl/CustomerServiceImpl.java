package com.odenktools.master.service.impl;

import com.mysema.query.jpa.impl.JPAQuery;
import com.odenktools.common.dto.CustomerDto;
import com.odenktools.common.filter.QueryFilter;
import com.odenktools.common.model.Customer;
import com.odenktools.common.model.QCustomer;
import com.odenktools.common.response.RestResponsePage;
import com.odenktools.master.repository.CustomerRepository;
import com.odenktools.master.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CustomerServiceImpl extends QueryFilter<Customer> implements CustomerService {

	private final CustomerRepository repository;

	@PersistenceContext
	private EntityManager entityManager;

	public CustomerServiceImpl(CustomerRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Customer> findById(String id) {
		return this.repository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Customer> findAllBy(String username, String email, String phoneNumber, Sort sort, Pageable pageable) {
		QCustomer qCustomer = QCustomer.customer;
		JPAQuery query = new JPAQuery(this.entityManager).from(qCustomer);
		query.where(qCustomer.isActive.eq(1));
		query.where(qCustomer.isDeleted.eq(0));
		if (username != null && !username.isEmpty()) {
			query.where(qCustomer.username.containsIgnoreCase(username));
		}
		if (email != null && !email.isEmpty()) {
			query.where(qCustomer.email.containsIgnoreCase(email));
		}
		if (phoneNumber != null && !phoneNumber.isEmpty()) {
			query.where(qCustomer.phoneNumber.containsIgnoreCase(phoneNumber));
		}
		long total = query.count();
		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());
		for (Sort.Order order : sort) {
			query.orderBy(this.toOrderSpecifier(Customer.class, qCustomer.getMetadata(), order));
		}
		return new RestResponsePage<>(query.list(qCustomer), pageable, total);
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean existsByEmailAddress(String email) {
		return this.repository.existsByEmail(email);
	}

	@Override
	public Boolean existsByUsername(String username) {
		return this.repository.existsByUsername(username);
	}

	@Override
	public Boolean existsByPhoneNumber(String phoneNumber) {
		return this.repository.existsByPhoneNumber(phoneNumber);
	}

    @Override
    public Boolean existsById(String id) {
        return this.repository.existsById(id);
    }

    @Override
	@Transactional
	public CustomerDto registerCustomer(CustomerDto request) {

		Customer customer = Customer.builder()
				.email(request.getEmail())
				.phoneNumber(request.getPhoneNumber())
				.username(request.getUsername())
				.fullName(request.getFullName())
				.password(request.getPassword())
				.amountBalanced(new BigDecimal(0.00))
				.isDeleted(0)
				.isActive(request.getIsActive())
				.build();

		// -- save
		request.setAmountBalanced(new BigDecimal(0.00));
		request.setId(repository.save(customer).getId());

		return request;
	}
}
