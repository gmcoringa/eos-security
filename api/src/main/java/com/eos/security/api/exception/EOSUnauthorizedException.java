/**
 * 
 */
package com.eos.security.api.exception;

import java.util.List;

import com.eos.common.exception.EOSError;
import com.eos.common.exception.EOSException;

/**
 * Exception indicating unauthorized access, usually user not authenticated.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSUnauthorizedException extends EOSException {

	private static final long serialVersionUID = -6751757166377514595L;

	/**
	 * @param message
	 */
	public EOSUnauthorizedException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EOSUnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param errors
	 */
	public EOSUnauthorizedException(String message, List<EOSError> errors) {
		super(message, errors);
	}

	/**
	 * @param message
	 * @param cause
	 * @param errors
	 */
	public EOSUnauthorizedException(String message, Throwable cause,
			List<EOSError> errors) {
		super(message, cause, errors);
	}

}
