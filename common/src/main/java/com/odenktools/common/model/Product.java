package com.odenktools.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.odenktools.common.enumtype.StockStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Handling Permission for END USER (Customer).
 *
 * @author Odenktools.
 */
@Getter
@Setter
@JsonSerialize
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "Product")
@Table(name = "products")
@JsonIgnoreProperties(
		ignoreUnknown = true,
		value = {"createdAt", "updatedAt"},
		allowGetters = true
)
@Builder
@SuppressWarnings("unused")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@Size(min = 2, max = 100)
	@Column(name = "product_name", unique = true, nullable = false, length = 100)
	private String productName;

	@Size(min = 2, max = 100)
	@Column(name = "sku", unique = true, nullable = false, length = 100)
	private String sku;

	@Size(min = 2, max = 100)
	@Column(name = "short_description", nullable = false)
	private String shortDescription;

	@Size(min = 2)
	@Column(name = "longDescription", nullable = false)
	private String longDescription;

	@Size(min = 2)
	@Column(name = "thumbnail_image_url", nullable = false)
	private String thumbnailImageUrl;

	@Column(name = "quantity", columnDefinition = "default int 0")
	private Integer quantity;

	@Enumerated(EnumType.STRING)
	@Column(name = "stock_status", length = 25)
	private StockStatus stockStatus;

	@Column(name = "price", columnDefinition = "default float 0.00")
	private BigDecimal price;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "is_active", columnDefinition = "default int 0")
	@JsonProperty("is_active")
	private int isActive;

	@Column(name = "is_deleted", columnDefinition = "default int 0")
	@JsonProperty("is_deleted")
	private int isDeleted;

	@Column(name = "version")
	@Version
	private Long version;

	/**
	 * Sets created_at before insert.
	 */
	@PrePersist
	public void setCreationDate() {

		this.createdAt = new Date();
	}
}

