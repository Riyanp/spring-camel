package com.odenktools.common.dto;

import java.math.BigDecimal;
import java.util.List;

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
public class CreateOrderCommand {

	private String id;

	private String accountId;

	private String customerEmail;

	private BigDecimal subtotal;

	private BigDecimal grandTotal;

	private Integer totalItemCount;

	private List<CartItemQueryDto> cartItems;

}
