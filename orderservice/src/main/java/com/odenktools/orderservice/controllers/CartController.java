package com.odenktools.orderservice.controllers;

import com.odenktools.common.dto.CreateCartItemCommand;
import com.odenktools.common.dto.ProductDto;
import com.odenktools.common.response.ApiResponse;
import com.odenktools.common.response.DataObjectResponse;
import com.odenktools.common.response.RestApiResponse;
import com.odenktools.orderservice.repository.CartRepository;
import com.odenktools.orderservice.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class CartController {

	private static final Logger LOG = LoggerFactory.getLogger(CartController.class);

	private final CartService cartService;

	private final CartRepository cartRepository;

	private final RestTemplate restTemplate;

	@Autowired
	public CartController(CartService cartService, CartRepository cartRepository, RestTemplate restTemplate) {
		this.cartService = cartService;
		this.cartRepository = cartRepository;
		this.restTemplate = restTemplate;
	}

	@PostMapping(
			value = "/cart/add",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity createCartItem(
			@RequestHeader(value = "customerId") String customerId,
			@RequestBody @Valid CreateCartItemCommand createCartItemCommand,
			HttpServletRequest httpServletRequest) {

		DataObjectResponse<CreateCartItemCommand> content = new DataObjectResponse<>();
		ResponseEntity<RestApiResponse<ProductDto>> productResponse = null;
		String messages = "";

		try {
			productResponse = this.restTemplate.exchange(
					String.format("http://localhost:6010/api/v1/product?id=%s", createCartItemCommand.getProductId()),
					HttpMethod.GET,
					HttpEntity.EMPTY,
					new ParameterizedTypeReference<RestApiResponse<ProductDto>>() {
					});
			LOG.info(productResponse.getBody().getApiVersion());

			//String productId = Objects.requireNonNull(productResponse.getBody().getResults().getData().getId());

			String productName = Objects.requireNonNull(productResponse
					.getBody()
					.getResults()
					.getData()
					.getProductName());

			createCartItemCommand.setProductName(productName);

			messages = "Add to cart success.";
			content.setContent(null);
			content.setCode(HttpStatus.OK.value());
			content.setMessage(messages);
			content.setData(this.cartService.createCartItem(createCartItemCommand, customerId));

			return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
					.status(HttpStatus.OK)
					.code(HttpStatus.OK.value())
					.message(messages)
					.params(null)
					.method(httpServletRequest.getMethod())
					.path(httpServletRequest.getRequestURI())
					.results(content)
					.build());

		} catch (HttpClientErrorException e) {
			LOG.error("ERROR {}", e.getMessage());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					ApiResponse.builder()
							.status(HttpStatus.BAD_REQUEST)
							.code(HttpStatus.BAD_REQUEST.value())
							.message("Product Not Found.")
							.params(null)
							.method(httpServletRequest.getMethod())
							.path(httpServletRequest.getRequestURI())
							.results(content)
							.build());
		}
	}
}
