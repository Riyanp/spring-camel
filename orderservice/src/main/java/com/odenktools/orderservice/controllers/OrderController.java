package com.odenktools.orderservice.controllers;

import com.odenktools.common.dto.ProductDto;
import com.odenktools.common.response.ApiResponse;
import com.odenktools.common.response.RestApiResponse;
import com.odenktools.orderservice.repository.OrderRepository;
import com.odenktools.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

	private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

	/**
	 * (1) DependencyInjection Fields.
	 */
	private final OrderService service;

	private final OrderRepository orderRepository;

	private String messages = "";

	private final RestTemplate restTemplate;

	@Autowired
	public OrderController(OrderService service, OrderRepository orderRepository, RestTemplate restTemplate) {
		this.service = service;
		this.orderRepository = orderRepository;
		this.restTemplate = restTemplate;
	}

	@RequestMapping(value = "/order/test",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> find(HttpServletRequest httpServletRequest) {

		ResponseEntity<RestApiResponse<ProductDto>> productResponse;

		try {
			productResponse = this.restTemplate.exchange(
					String.format("http://localhost:6010/api/v1/order?id=1dfe3fc4-c98e-45ea-a49a-996db3fa4b64"),
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
}
