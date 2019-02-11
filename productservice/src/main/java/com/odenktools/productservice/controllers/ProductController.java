package com.odenktools.productservice.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.odenktools.common.dto.ProductDto;
import com.odenktools.common.model.Product;
import com.odenktools.common.response.ApiResponse;
import com.odenktools.common.response.DataObjectResponse;
import com.odenktools.common.response.RestApiResponse;
import com.odenktools.common.util.RequestUtils;
import com.odenktools.productservice.repository.ProductRepository;
import com.odenktools.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

	/**
	 * (1) DependencyInjection Fields.
	 */
	private final ProductService service;

	private final ProductRepository productRepository;

	private String messages = "";

	private final RestTemplate restTemplate;

	@Autowired
	public ProductController(ProductService service, ProductRepository productRepository, RestTemplate restTemplate) {
		this.service = service;
		this.productRepository = productRepository;
		this.restTemplate = restTemplate;
	}

	@RequestMapping(value = "/product/test",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> find(HttpServletRequest httpServletRequest) {

		ResponseEntity<RestApiResponse<ProductDto>> productResponse;

		try {
			productResponse = this.restTemplate.exchange(
					String.format("http://localhost:6010/api/v1/product?id=1dfe3fc4-c98e-45ea-a49a-996db3fa4b64"),
					HttpMethod.GET,
					HttpEntity.EMPTY,
					new ParameterizedTypeReference<RestApiResponse<ProductDto>>() {
					});
			LOG.info(productResponse.getBody().getApiVersion());
			String accountId = Objects.requireNonNull(productResponse.getBody().getResults().getData().getId());

		} catch (HttpClientErrorException e) {
			LOG.error("ERROR {}", e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
				.status(HttpStatus.OK)
				.code(HttpStatus.OK.value())
				.message("success.")
				.params(null)
				.pageable(null)
				.method(httpServletRequest.getMethod())
				.path(httpServletRequest.getRequestURI())
				.results(null)
				.build()
		);
	}

	/**
	 * Get all available product on database.
	 *
	 * @param productName search by productName.
	 * @param page        pagenumber. Default 0.
	 * @param size        display limit, Default 10.
	 * @param direction   sorting "ASC OR DESC", default to DESC.
	 * @return Groups Model.
	 */
	@RequestMapping(value = "/product/list",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findAllData(@RequestParam(name = "productName", defaultValue = "") String productName,
										 @RequestParam(required = false, defaultValue = "0") Integer page,
										 @RequestParam(required = false, defaultValue = "10") Integer size,
										 @RequestParam(required = false, defaultValue = "DESC") String direction,
										 HttpServletRequest httpServletRequest) {

		List<String> sortProperties = new ArrayList<>();
		sortProperties.add("createdAt");
		Sort sort = new Sort(Sort.Direction.fromString(direction), sortProperties);

		Page<Product> listData = this.service
				.findAllBy(productName, sort, PageRequest.of(page, size));

		List<Product> dataList = listData.getContent();

		DataObjectResponse<Product> content = new DataObjectResponse<>();
		content.setContent(dataList);
		content.setCode(HttpStatus.OK.value());
		content.setMessage("Ok!");

		ObjectNode objectNode = RequestUtils.getAllParams(httpServletRequest);


		LOG.info("HITTING PRODUCT_LIST PAGE =[{}], SIZE {}", page, size);

		if (objectNode != null) {
			return ResponseEntity.status(HttpStatus.OK).body(
					ApiResponse.builder()
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
			return ResponseEntity.status(HttpStatus.OK).body(
					ApiResponse.builder()
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
	 * Create a new product.
	 *
	 * @param request            ProductDto
	 * @param httpServletRequest HTTP Request
	 * @return ResponseEntity
	 */
	@PostMapping(
			value = "/product/create",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<?> create(@RequestBody @Valid ProductDto request,
									HttpServletRequest httpServletRequest) {

		DataObjectResponse<ProductDto> content = new DataObjectResponse<>();

		if (this.productRepository.existsByProductName(request.getProductName())) {

			this.messages = String.format("Product with name ``%s`` already exist",
					request.getProductName());

			content.setContent(null);
			content.setCode(HttpStatus.CONFLICT.value());
			content.setMessage(this.messages);

			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					ApiResponse.builder()
							.status(HttpStatus.CONFLICT)
							.code(HttpStatus.CONFLICT.value())
							.message(this.messages)
							.params(null)
							.method(httpServletRequest.getMethod())
							.path(httpServletRequest.getRequestURI())
							.results(content)
							.build());
		}

		this.messages = "Successful created a new product.";
		content.setContent(null);
		content.setCode(HttpStatus.OK.value());
		content.setMessage(this.messages);
		content.setData(this.service.createProduct(request));

		return ResponseEntity.status(HttpStatus.OK).body(
				ApiResponse.builder()
						.status(HttpStatus.OK)
						.code(HttpStatus.OK.value())
						.message(this.messages)
						.params(null)
						.method(httpServletRequest.getMethod())
						.path(httpServletRequest.getRequestURI())
						.results(content)
						.build());
	}

	/**
	 * Get Product detail.
	 *
	 * @param id id do you want to check.
	 * @return GroupDto
	 */
	@GetMapping(value = "/product",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findGroupById(@RequestParam("id") String id,
										   HttpServletRequest httpServletRequest) {

		DataObjectResponse<Product> content = new DataObjectResponse<>();
		String messages = "";

		Optional<Product> productOptional = this.productRepository.findById(id);

		if (productOptional.isPresent()) {

			Product product = productOptional.get();

			messages = "Success.";
			content.setContent(null);
			content.setCode(HttpStatus.OK.value());
			content.setMessage(messages);
			content.setData(product);

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
