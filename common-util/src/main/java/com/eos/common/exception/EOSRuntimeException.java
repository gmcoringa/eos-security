/**
 * 
 */
package com.eos.common.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Application runtime exception.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 3561099809436775804L;
	private List<EOSError> errors = new ArrayList<>();

	/**
	 * @param message
	 * @param cause
	 */
	public EOSRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor with error list.
	 * 
	 * @param message
	 * @param cause
	 * @param errors
	 */
	public EOSRuntimeException(String message, Throwable cause,
			List<EOSError> errors) {
		this(message, cause);
		this.errors = errors;
	}

	/**
	 * @param message
	 */
	public EOSRuntimeException(String message) {
		super(message);
	}

	/**
	 * Constructor with error list.
	 * 
	 * @param message
	 * @param errors
	 */
	public EOSRuntimeException(String message, List<EOSError> errors) {
		this(message);
		this.errors = errors;
	}

	/**
	 * Get list of errors.
	 * 
	 * @return List of errors.
	 */
	public List<EOSError> getErrors() {
		return errors;
	}

}
