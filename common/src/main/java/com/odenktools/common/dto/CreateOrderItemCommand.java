package com.odenktools.common.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderItemCommand {

	private String id;

	private String orderId;

	private String productId;

	private String productName;

	private Integer quantity;

	private BigDecimal price;

	private BigDecimal rowTotal;

	private BigDecimal fixedRowTotal;

}
