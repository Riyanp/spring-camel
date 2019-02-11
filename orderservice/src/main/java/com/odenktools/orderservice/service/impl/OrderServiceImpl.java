package com.odenktools.orderservice.service.impl;

import com.odenktools.common.filter.QueryFilter;
import com.odenktools.common.model.Order;
import com.odenktools.orderservice.repository.OrderRepository;
import com.odenktools.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Slf4j
public class OrderServiceImpl extends QueryFilter<Order> implements OrderService {

	private final OrderRepository orderRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
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
