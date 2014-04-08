/**
 * 
 */
package com.eos.common.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @author santos.fabiano
 * 
 */
public class EOSException extends Exception {

	private static final long serialVersionUID = 6001569929386767813L;
	private List<EOSError> errors = new ArrayList<>();

	/**
	 * @param message
	 * @param cause
	 */
	public EOSException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor with error list.
	 * 
	 * @param message
	 * @param cause
	 * @param errors
	 */
	public EOSException(String message, Throwable cause, List<EOSError> errors) {
		this(message, cause);
		this.errors = errors;
	}

	/**
	 * @param message
	 */
	public EOSException(String message) {
		super(message);
	}

	/**
	 * Constructor with error list.
	 * 
	 * @param message
	 * @param errors
	 */
	public EOSException(String message, List<EOSError> errors) {
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
