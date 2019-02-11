package com.odenktools.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Handling Permission for END USER (CustomerGroup).
 *
 * @author Odenktools.
 */
@Getter
@Setter
@JsonSerialize
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "CustomerGroup")
@Table(name = "customer_groups")
@JsonIgnoreProperties(
		ignoreUnknown = true,
		value = {"createdAt", "updatedAt"},
		allowGetters = true
)
@Builder
@SuppressWarnings("unused")
public class CustomerGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "group_id")
    private String groupId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

	/**
	 * Sets created_at before insert.
	 */
	@PrePersist
	public void setCreationDate() {

		this.createdAt = new Date();
	}
}

