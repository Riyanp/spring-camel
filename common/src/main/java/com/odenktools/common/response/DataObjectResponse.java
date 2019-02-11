package com.odenktools.common.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Get Response from API.
 * @param <T>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataObjectResponse<T> {

	@JsonProperty("code")
	private Integer code;

	@JsonProperty("message")
	private String message;

	@JsonProperty("content")
	private List<T> content;

	@JsonProperty("data")
	private T data;
}