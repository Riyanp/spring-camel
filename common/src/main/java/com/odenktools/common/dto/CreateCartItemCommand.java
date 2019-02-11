package com.odenktools.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCartItemCommand {

	private String id;

	private String productId;

	private String productName;

	private Integer quantity;
}
