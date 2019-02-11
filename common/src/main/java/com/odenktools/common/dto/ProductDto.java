package com.odenktools.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odenktools.common.enumtype.StockStatus;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Handling Customer
 *
 * @author Odenktools
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
public class ProductDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	@NotNull(message = "productName cannot null")
	@NotEmpty(message = "productName cannot empty")
	private String productName;

	@NotNull(message = "shortDescription cannot null")
	@NotEmpty(message = "shortDescription cannot empty")
	private String shortDescription;

	@NotNull(message = "longDescription cannot null")
	@NotEmpty(message = "longDescription cannot empty")
	private String longDescription;

	@NotNull(message = "thumbnailImageUrl cannot null")
	@NotEmpty(message = "thumbnailImageUrl cannot empty")
	private String thumbnailImageUrl;

	@NotNull(message = "quantity cannot null")
	private Integer quantity;

	@NotNull(message = "stockStatus cannot null")
	private StockStatus stockStatus;

	@NotNull(message = "price cannot null")
	private BigDecimal price;

	private int isActive;

	private int isDeleted;
}

