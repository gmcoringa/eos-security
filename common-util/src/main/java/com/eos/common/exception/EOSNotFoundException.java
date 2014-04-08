/**
 * 
 */
package com.eos.common.exception;

import java.util.List;

/**
 * Exception for not found items, entities, etc.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSNotFoundException extends EOSException {

	private static final long serialVersionUID = 1902405188953349003L;

	/**
	 * @param message
	 * @param cause
	 */
	public EOSNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param errors
	 */
	public EOSNotFoundException(String message, Throwable cause,
			List<EOSError> errors) {
		super(message, cause, errors);
	}

	/**
	 * @param message
	 */
	public EOSNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param errors
	 */
	public EOSNotFoundException(String message, List<EOSError> errors) {
		super(message, errors);
	}

}
