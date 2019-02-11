package com.odenktools.orderservice.service;

import com.odenktools.common.dto.CreateCartItemCommand;
import com.odenktools.common.dto.ProductDto;
import com.odenktools.common.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface CartService {

	CreateCartItemCommand createCartItem(CreateCartItemCommand createCartItemCommand, String customerId);

	Boolean isProductAlreadyInCart(String productId, String customerId);
}
