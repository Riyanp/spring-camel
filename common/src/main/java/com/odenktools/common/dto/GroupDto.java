package com.odenktools.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Handling Group
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
public class GroupDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	@NotNull(message = "name cannot null")
	@NotEmpty(message = "name cannot empty")
	private String name;

	@NotNull(message = "code cannot null")
	@NotEmpty(message = "code cannot empty")
	private String code;

    private int isDeleted = 0;

}

