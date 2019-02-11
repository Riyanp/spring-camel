package com.odenktools.common.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Get Response from API.
 *
 * @param <T>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestApiResponse<T> {

	@JsonProperty("apiVersion")
	private final String apiVersion = "1.0";

	@JsonProperty("method")
	private String method;

	@JsonProperty("params")
	private Object params;

	@JsonProperty("code")
	private Integer code;

	@JsonProperty("status")
	private HttpStatus status;

	@JsonProperty("path")
	private String path;

	@JsonProperty("message")
	private String message;

	@JsonProperty("results")
	private DataObjectResponse<T> results;

	@JsonProperty("pageable")
	private Pageable pageable;

	@JsonProperty("errors")
	private Map<String, Object> errors;

	/**
	 * Add Error
	 *
	 * @param key
	 * @param error
	 */
	public void addError(String key, Object error) {
		initializeErrorsIfNull();
		this.errors.put(key, error);
	}

	/**
	 * InitializeErrorIfNull
	 */
	private void initializeErrorsIfNull() {
		if (this.errors == null) this.errors = new HashMap<>();
	}

	// -- static
	public static <T> RestApiResponse<T> ok(DataObjectResponse<T> results) {
		//Assert.notNull(data, "Data must not null");
		return RestApiResponse.status(HttpStatus.OK, results);
	}

	public static <T> RestApiResponse<T> okOrNotFound(@Nullable DataObjectResponse<T> results) {
		if (Objects.isNull(results)) {
			return RestApiResponse.status(HttpStatus.NOT_FOUND, null);
		} else {
			return RestApiResponse.ok(results);
		}
	}

	static <T> RestApiResponse<T> status(HttpStatus httpStatus, @Nullable DataObjectResponse<T> results) {
		return RestApiResponse.<T>builder()
				.code(httpStatus.value())
				.status(httpStatus)
				.results(results)
				.build();
	}
	// -- end static
}
