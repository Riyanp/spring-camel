package com.odenktools.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
@Setter
@JsonSerialize
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "Customer")
@Table(name = "customers")
@JsonIgnoreProperties(
		ignoreUnknown = true,
		value = {"createdAt", "updatedAt"},
		allowGetters = true
)
@Builder
@SuppressWarnings("unused")
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@Size(min = 2, max = 50)
	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Size(min = 2, max = 100)
	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Size(min = 2, max = 100)
	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Size(min = 2, max = 18)
	@Column(name = "phone_number", unique = true, nullable = false)
	private String phoneNumber;

	@Size(max = 255)
	@Column(name = "password", unique = true, nullable = false)
	private String password;

	@Column(name = "amount_balanced", columnDefinition = "default float 0.00")
	private BigDecimal amountBalanced;

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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public BigDecimal getAmountBalanced() {
        return amountBalanced;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public int getIsActive() {
        return isActive;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    @JsonIgnore
    public Long getVersion() {
        return version;
    }
}

