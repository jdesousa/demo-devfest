package fr.leroymerlin.demodevfest.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents informations about rest error.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
				getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ErrorResource {

	/**
	 * The human readable HTTP error status.
	 */
	@JsonProperty("status")
	private HttpStatus status;

	/**
	 * The date when the error occurred.
	 */
	@JsonProperty("timestamp")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
	private LocalDateTime timestamp = LocalDateTime.now();

	/**
	 * The global error message. Ex : "Validation errors".
	 */
	@JsonProperty("message")
	private String message;

	/**
	 * The eventual debug message (use with caution).
	 */
	@JsonProperty("debugMessage")
	private String debugMessage;

	/**
	 * The list of errors that occurred.
	 * Ex :
	 * <ul>
	 * <li>Value '' for 'myresource.name' is invalid. The value may not be empty</li>
	 * <li>Value '50' for 'myresource.size' is invalid. The value may not be greater than 30</li>
	 * </ul>
	 */
	@JsonProperty("subErrors")
	private List<String> subErrors;

	/**
	 * Add a error message to the list of the sub errors.
	 *
	 * @param message
	 * 		the message to add to the list.
	 *
	 * @return the {@link ErrorResource} instance, so the method can be fluently used.
	 */
	public ErrorResource addSubError(String message) {

		if (this.subErrors == null) {
			this.subErrors = new ArrayList<>();
		}

		this.subErrors.add(message);

		return this;
	}
}
