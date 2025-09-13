package com.taskmanager.gateway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class CustomSecurityException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CustomSecurityException.class);

	private final HttpStatus status;

	public CustomSecurityException(HttpStatus status) {
		super(status.getReasonPhrase());
		this.status = status;
	}

	public CustomSecurityException(HttpStatus status, String message) {
		super(message);
		this.status = status;
		logException(message, null);
	}

	public CustomSecurityException(HttpStatus status, String message, Exception cause) {
		super(message, cause);
		this.status = status;
		logException(message, cause);
	}

	public CustomSecurityException(HttpStatus status, Exception cause) {
		super(status.getReasonPhrase(), cause);
		this.status = status;
		logException(status.getReasonPhrase(), cause);
	}

	private void logException(String message, Throwable cause) {
		logger.error("Security exception occurred - Status: {}, Message: {}, Exception: {}", status, message, cause);
	}

	public HttpStatus getStatus() {
		return status;
	}
}
