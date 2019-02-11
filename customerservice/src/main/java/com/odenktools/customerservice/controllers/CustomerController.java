package com.odenktools.customerservice.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.odenktools.common.dto.CustomerDto;
import com.odenktools.common.model.Customer;
import com.odenktools.common.response.ApiResponse;
import com.odenktools.common.response.DataObjectResponse;
import com.odenktools.common.util.RequestUtils;
import com.odenktools.customerservice.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

	/**
	 * (1) DependencyInjection Fields.
	 */
	private final CustomerService customerService;

	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * Register a new Customer.
	 *
	 * @param request            CustomerDTO
	 * @param httpServletRequest servletrequest
	 * @return ResponseEntity
	 */
	@PostMapping(
			value = "/customer/register",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<?> createCustomer(@RequestBody @Valid CustomerDto request,
											HttpServletRequest httpServletRequest) {

		DataObjectResponse<CustomerDto> content = new DataObjectResponse<>();
		String messages = "";

		boolean isValidContraint = true;

		if (this.customerService.existsByEmailAddress(request.getEmail())) {

			messages = String.format("Customer with email ``%s`` already exist",
					request.getEmail());

			content.setContent(null);
			content.setCode(HttpStatus.CONFLICT.value());
			content.setMessage(messages);

			isValidContraint = false;

		} else if (this.customerService.existsByUsername(request.getUsername())) {
			messages = String.format("Customer with username ``%s`` already exist",
					request.getUsername());

			content.setContent(null);
			content.setCode(HttpStatus.CONFLICT.value());
			content.setMessage(messages);

			isValidContraint = false;

		} else if (this.customerService.existsByPhoneNumber(request.getPhoneNumber())) {
			messages = String.format("Customer with phone ``%s`` already exist",
					request.getPhoneNumber());

			content.setContent(null);
			content.setCode(HttpStatus.CONFLICT.value());
			content.setMessage(messages);

			isValidContraint = false;
		}

		if (isValidContraint) {
			messages = "Register success.";
			content.setContent(null);
			content.setCode(HttpStatus.CONFLICT.value());
			content.setMessage(messages);
			content.setData(this.customerService.registerCustomer(request));

			return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
					.status(HttpStatus.OK)
					.code(HttpStatus.OK.value())
					.message(messages)
					.params(null)
					.method(httpServletRequest.getMethod())
					.path(httpServletRequest.getRequestURI())
					.results(content)
					.build());

		} else {

			LOG.error("VALIDATION ERROR {}", messages);

			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					ApiResponse.builder()
							.status(HttpStatus.CONFLICT)
							.code(HttpStatus.CONFLICT.value())
							.message(messages)
							.params(null)
							.method(httpServletRequest.getMethod())
							.path(httpServletRequest.getRequestURI())
							.results(content)
							.build());
		}
	}

	/**
	 * Get all available product on database.
	 *
	 * @param username    search by username.
	 * @param email       search by email.
	 * @param phoneNumber search by phoneNumber.
	 * @param page        pagenumber. Default 0.
	 * @param size        display limit, Default 10.
	 * @param direction   sorting "ASC OR DESC", default to DESC.
	 * @return Groups Model.
	 */
	@RequestMapping(value = "/customer/list",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findAllData(@RequestParam(name = "username", defaultValue = "") String username,
										 @RequestParam(name = "email", defaultValue = "") String email,
										 @RequestParam(name = "phoneNumber", defaultValue = "") String phoneNumber,
										 @RequestParam(required = false, defaultValue = "0") Integer page,
										 @RequestParam(required = false, defaultValue = "10") Integer size,
										 @RequestParam(required = false, defaultValue = "DESC") String direction,
										 HttpServletRequest httpServletRequest) {

		List<String> sortProperties = new ArrayList<>();
		sortProperties.add("createdAt");
		Sort sort = new Sort(Sort.Direction.fromString(direction), sortProperties);

		Page<Customer> listData = this.customerService
				.findAllBy(username, email, phoneNumber,
						sort, PageRequest.of(page, size));

		List<Customer> dataList = listData.getContent();

		DataObjectResponse<Customer> content = new DataObjectResponse<>();
		content.setContent(dataList);
		content.setCode(HttpStatus.OK.value());
		content.setMessage("Ok!");

		ObjectNode objectNode = RequestUtils.getAllParams(httpServletRequest);

		LOG.info("HITTING PRODUCT_LIST PAGE =[{}], SIZE {}", page, size);

		if (objectNode != null) {
			return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
					.status(HttpStatus.OK)
					.code(HttpStatus.OK.value())
					.message("success.")
					.params(objectNode)
					.pageable(listData.getPageable())
					.method(httpServletRequest.getMethod())
					.path(httpServletRequest.getRequestURI())
					.results(content)
					.build()
			);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
					.status(HttpStatus.OK)
					.code(HttpStatus.OK.value())
					.params(null)
					.pageable(listData.getPageable())
					.method(httpServletRequest.getMethod())
					.path(httpServletRequest.getRequestURI())
					.results(content)
					.build()
			);
		}
	}

	/**
	 * Get Group detail.
	 *
	 * @param id id do you want to check.
	 * @return GroupDto
	 */
	@GetMapping(value = "/customer",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findGroupById(@RequestParam("id") String id,
										   HttpServletRequest httpServletRequest) {

		DataObjectResponse<Customer> content = new DataObjectResponse<>();
		String messages = "";

		Optional<Customer> customerOptional = this.customerService.findById(id);

		if (customerOptional.isPresent()) {

			Customer customer = customerOptional.get();

			messages = "success.";
			content.setContent(null);
			content.setCode(HttpStatus.OK.value());
			content.setMessage(messages);
			content.setData(customer);

			return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
					.status(HttpStatus.OK)
					.code(HttpStatus.OK.value())
					.message(messages)
					.params(null)
					.method(httpServletRequest.getMethod())
					.path(httpServletRequest.getRequestURI())
					.results(content)
					.build());
		}

		messages = "success.";
		content.setContent(null);
		content.setCode(HttpStatus.BAD_REQUEST.value());
		content.setMessage(messages);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.code(HttpStatus.BAD_REQUEST.value())
				.message(messages)
				.params(null)
				.method(httpServletRequest.getMethod())
				.path(httpServletRequest.getRequestURI())
				.results(content)
				.build());
	}

	@GetMapping(value = "/customer-balance",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findBalanceById(@RequestParam("id") String id,
										   HttpServletRequest httpServletRequest) {

		DataObjectResponse<HashMap<String, BigDecimal>> content = new DataObjectResponse<>();
		String messages = "";

		Optional<Customer> customerOptional = this.customerService.findById(id);

		if (customerOptional.isPresent()) {

			Customer customer = customerOptional.get();
			HashMap<String, BigDecimal> amount = new HashMap<>();
			amount.put("AmountBalanced", customer.getAmountBalanced());

			messages = "success.";
			content.setContent(null);
			content.setCode(HttpStatus.OK.value());
			content.setMessage(messages);
			content.setData(amount);

			return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
					.status(HttpStatus.OK)
					.code(HttpStatus.OK.value())
					.message(messages)
					.params(null)
					.method(httpServletRequest.getMethod())
					.path(httpServletRequest.getRequestURI())
					.results(content)
					.build());
		}

		messages = "success.";
		content.setContent(null);
		content.setCode(HttpStatus.BAD_REQUEST.value());
		content.setMessage(messages);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.code(HttpStatus.BAD_REQUEST.value())
				.message(messages)
				.params(null)
				.method(httpServletRequest.getMethod())
				.path(httpServletRequest.getRequestURI())
				.results(content)
				.build());
	}
}
