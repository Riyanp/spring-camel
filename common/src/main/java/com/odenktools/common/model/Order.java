package com.odenktools.common.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "orders")
public class Order implements Serializable {
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@Column(name = "customer_id")
	private String customerId;

	@Column(name = "customer_email", length = 150)
	private String customerEmail;

	@Column(name = "subtotal")
	private BigDecimal subtotal;

	@Column(name = "grand_total", scale = 2, precision = 13)
	private BigDecimal grandTotal;

	@Column(name = "total_item_count")
	private Integer totalItemCount;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_created")
	private final Date dateCreated = new Date();

	@Version
	@Column(name = "version")
	private Long version;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private final Date createdDate = new Date();

	@Column(name = "updated_date")
	private Date updatedDate;

	//@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderItem> orderItems;
}
