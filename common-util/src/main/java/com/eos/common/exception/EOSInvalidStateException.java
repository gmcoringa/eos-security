/**
 * 
 */
package com.eos.common.exception;

import java.util.List;

/**
 * Exception for invalid states.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSInvalidStateException extends EOSException {

	private static final long serialVersionUID = -4603754755716017246L;

	/**
	 * @param message
	 * @param cause
	 */
	public EOSInvalidStateException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param errors
	 */
	public EOSInvalidStateException(String message, Throwable cause,
			List<EOSError> errors) {
		super(message, cause, errors);
	}

	/**
	 * @param message
	 */
	public EOSInvalidStateException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param errors
	 */
	public EOSInvalidStateException(String message, List<EOSError> errors) {
		super(message, errors);
	}

}
