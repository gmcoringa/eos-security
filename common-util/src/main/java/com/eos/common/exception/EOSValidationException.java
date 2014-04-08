/**
 * 
 */
package com.eos.common.exception;

import java.util.List;

/**
 * Validation exception errors.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSValidationException extends EOSException {

	private static final long serialVersionUID = -4048023560004425493L;

	/**
	 * Constructor with error list.
	 * 
	 * @param message
	 * @param errors
	 */
	public EOSValidationException(String message, List<EOSError> errors) {
		super(message, errors);
	}

	/**
	 * Constructor with error list.
	 * 
	 * @param message
	 * @param cause
	 * @param errors
	 */
	public EOSValidationException(String message, Throwable cause,
			List<EOSError> errors) {
		super(message, cause, errors);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public EOSValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param message
	 */
	public EOSValidationException(String message) {
		super(message);
	}

}
