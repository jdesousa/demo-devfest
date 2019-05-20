package fr.leroymerlin.demodevfest.controllers.exceptions;

import fr.leroymerlin.demodevfest.model.ErrorResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

/**
 * Handle all exceptions thrown by controllers and return a proper EntityResponse with HttpStatus.
 * <ul>
 * <li>NotFoundException returns 404</li>
 * <li>WebExchangeBindException (validation error) and decoding exceptions return 400</li>
 * <li>All other exceptions return 503</li>
 * </ul>
 */
@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler({ DecodingException.class })
	protected ResponseEntity<ErrorResource> handleBadRequest(Exception ex) {

		ErrorResource error = new ErrorResource();
		error.setStatus(HttpStatus.BAD_REQUEST);
		error.setMessage(String.valueOf(ex));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<ErrorResource> handleNotFound(ResourceNotFoundException ex) {

		ErrorResource error = new ErrorResource();
		error.setStatus(HttpStatus.NOT_FOUND);
		error.setMessage("Resource not found");

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(WebExchangeBindException.class)
	protected ResponseEntity<ErrorResource> handleBadRequest(WebExchangeBindException ex) {

		ErrorResource error = new ErrorResource();
		error.setStatus(HttpStatus.BAD_REQUEST);
		error.setMessage("Validation errors");

		ex.getFieldErrors().forEach(fe -> error.addSubError(
			String.format("Value '%s' for '%s.%s' is invalid. The value %s",
				fe.getRejectedValue(),
				fe.getObjectName(),
				fe.getField(),
				fe.getDefaultMessage())
			));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(Throwable.class)
	protected ResponseEntity<ErrorResource> handleInternalError(Throwable t) {

		log.error("An unexpected error occurred.", t);

		ErrorResource error = new ErrorResource();
		error.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
		error.setMessage(String.valueOf(t));

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
	}
}
