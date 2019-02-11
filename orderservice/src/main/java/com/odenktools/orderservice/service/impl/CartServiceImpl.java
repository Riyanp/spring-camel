package com.odenktools.orderservice.service.impl;

import com.odenktools.common.dto.CreateCartItemCommand;
import com.odenktools.common.filter.QueryFilter;
import com.odenktools.common.model.Cart;
import com.odenktools.common.model.CartItem;
import com.odenktools.common.model.Order;
import com.odenktools.orderservice.repository.CartItemRepository;
import com.odenktools.orderservice.repository.CartRepository;
import com.odenktools.orderservice.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class CartServiceImpl extends QueryFilter<Cart> implements CartService {

	private final CartRepository cartRepository;

	private final CartItemRepository cartItemRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository) {
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
	}

	@Override
	public CreateCartItemCommand createCartItem(CreateCartItemCommand createCartItemCommand, String customerId) {

		Optional<Cart> cartOptional = cartRepository.findByAccountIdAndFetchCartItems(customerId);
		if (!cartOptional.isPresent()) {
			log.error("NOT PRESENT");
			return createCartItemCommand;
		}
		Set<CartItem> cartItems = cartOptional.get().getCartItems();
		for (CartItem cartItem : cartItems) {
			if (cartItem.getProductId().equalsIgnoreCase(createCartItemCommand.getProductId())) {
				return createCartItemCommand;
			}
		}

		CartItem cartItem = CartItem.builder()
				.productId(createCartItemCommand.getProductId())
				.quantity(createCartItemCommand.getQuantity())
				.productName(createCartItemCommand.getProductName())
				.cart(cartOptional.get())
				.build();

		cartItemRepository.save(cartItem);
		createCartItemCommand.setId(cartItem.getId());
		return createCartItemCommand;
	}

	@Override
	public Boolean isProductAlreadyInCart(String productId, String customerId) {
		return null;
	}


	/*@Override
	public CreateOrderDto createOrder(CreateOrderDto request) {

		Order order = Order.builder()
				.productName(request.getProductName())
				.sku(new Slugify().slugify(request.getProductName()))
				.shortDescription(request.getShortDescription())
				.longDescription(request.getLongDescription())
				.quantity(request.getQuantity())
				.stockStatus(request.getStockStatus())
				.thumbnailImageUrl(request.getThumbnailImageUrl())
				.price(request.getPrice())
				.isActive(1)
				.isDeleted(0)
				.build();

		// -- save
		request.setId(this.orderRepository.save(product).getId());

		return request;
	}*/
}
