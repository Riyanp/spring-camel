package com.odenktools.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

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
public class CustomerDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	@NotNull(message = "username cannot null")
	@NotEmpty(message = "username cannot empty")
	private String username;

	@NotNull(message = "email cannot null")
	@NotEmpty(message = "email cannot empty")
	private String email;

	@NotNull(message = "fullName cannot null")
	@NotEmpty(message = "fullName cannot empty")
	private String fullName;

	@NotNull(message = "phoneNumber cannot null")
	@NotEmpty(message = "phoneNumber cannot empty")
	private String phoneNumber;

	@NotNull(message = "password cannot null")
	@NotEmpty(message = "password cannot empty")
	private String password;

	private BigDecimal amountBalanced;

	private int isActive = 1;

	private int isDeleted = 0;
}

