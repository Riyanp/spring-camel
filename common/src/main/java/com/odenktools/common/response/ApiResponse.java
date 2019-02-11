package com.odenktools.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Build API Response.
 *
 * @author Odenktools.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({
		"apiVersion",
		"method",
		"code",
		"status",
		"timestamp",
		"path",
		"message",
		"errors",
		"params",
		"pageable",
		"data"
})
public class ApiResponse implements Serializable {

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
	private DataObjectResponse results;

	@JsonProperty("pageable")
	private Pageable pageable;

	@JsonProperty("errors")
	private Map<String, Object> errors;

	private List<ApiSubError> subErrors;

	@JsonProperty("timestamp")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private final LocalDateTime timestamp = LocalDateTime.now();

	private String debugMessage;

	public ApiResponse(HttpStatus status) {

		this();
		this.status = status;
	}

	public ApiResponse(HttpStatus status, Throwable ex) {

		this();
		this.status = status;
		this.message = "Unexpected error";
		this.debugMessage = ex.getLocalizedMessage();
	}

	public ApiResponse(DataObjectResponse results) {

		this();
		this.results = results;
	}

	@SuppressWarnings("unused")
	public ApiResponse(HttpStatus status, String message) {

		this();
		this.status = status;
		this.message = message;
	}

	public ApiResponse(HttpStatus status, String message, Throwable ex) {

		this();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}

	private void addSubError(ApiSubError subError) {

		if (this.subErrors == null) {
			this.subErrors = new java.util.ArrayList<>();
		}
		this.subErrors.add(subError);
	}

	private void addValidationError(String object, String field, Object rejectedValue, String message) {

		this.addSubError(new ApiValidationError(object, field, rejectedValue, message));
	}

	private void addValidationError(String object, String message) {

		this.addSubError(new ApiValidationError(object, message));
	}

	private void addValidationError(FieldError fieldError) {

		this.addValidationError(
				fieldError.getObjectName(),
				fieldError.getField(),
				fieldError.getRejectedValue(),
				fieldError.getDefaultMessage());
	}

	@SuppressWarnings("unused")
	private void addValidationErrors(java.util.List<FieldError> fieldErrors) {

		fieldErrors.forEach(this::addValidationError);
	}

	private void addValidationError(ObjectError objectError) {

		this.addValidationError(
				objectError.getObjectName(),
				objectError.getDefaultMessage());
	}

	public void addValidationError(java.util.List<ObjectError> globalErrors) {

		globalErrors.forEach(this::addValidationError);
	}

	/**
	 * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
	 *
	 * @param cv the ConstraintViolation
	 */
	private void addValidationError(ConstraintViolation<?> cv) {

		this.addValidationError(
				cv.getRootBeanClass().getSimpleName(),
				((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
				cv.getInvalidValue(),
				cv.getMessage());
	}

	public void addValidationErrors(java.util.Set<ConstraintViolation<?>> constraintViolations) {

		constraintViolations.forEach(this::addValidationError);
	}


	abstract class ApiSubError {

	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	class ApiValidationError extends ApiSubError {

		private final String object;

		private final String message;

		private final String field;

		private final Object rejectedValue;

		ApiValidationError(String object, String message) {

			this.object = object;
			this.message = message;
			this.field = "";
			this.rejectedValue = "";
		}

		ApiValidationError(String object, String field, Object rejectedValue, String message) {

			this.object = object;
			this.field = field;
			this.rejectedValue = rejectedValue;
			this.message = message;
		}
	}
}
