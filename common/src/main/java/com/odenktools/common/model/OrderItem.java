package com.odenktools.common.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "order_items")
public class OrderItem implements Serializable {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	@Column(name = "product_id")
	private String productId;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "quantity")
	private Integer quantity;

	// this is original price
	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "row_total")
	private BigDecimal rowTotal;

	@Column(name = "fixed_row_total")
	private BigDecimal fixedRowTotal;

	@Column(name = "date_created")
	@Temporal(TemporalType.DATE)
	private final Date dateCreated = new Date();

	@Version
	@Column(name = "version")
	private Long version;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private final Date createdAt = new Date();
}
