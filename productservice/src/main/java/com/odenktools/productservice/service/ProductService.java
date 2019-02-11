package com.odenktools.productservice.service;

import com.odenktools.common.dto.ProductDto;
import com.odenktools.common.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ProductService {

	Boolean existsByProductName(String productName);

	Boolean isProductWithIdExist(String productId);

	Page<Product> findAllBy(String productName, Sort sort, Pageable pageable);

	/**
	 * Register a new Customer.
	 * @param request CustomerDTO.
	 */
	ProductDto createProduct(ProductDto request);
}
