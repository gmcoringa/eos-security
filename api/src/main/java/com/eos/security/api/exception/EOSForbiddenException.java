/**
 * 
 */
package com.eos.security.api.exception;

import java.util.List;

import com.eos.common.exception.EOSError;
import com.eos.common.exception.EOSException;

/**
 * Exception indicating insufficient privileges for the action, so, access
 * denied.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSForbiddenException extends EOSException {

	private static final long serialVersionUID = -2563267223987453899L;

	/**
	 * @param message
	 */
	public EOSForbiddenException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EOSForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param errors
	 */
	public EOSForbiddenException(String message, List<EOSError> errors) {
		super(message, errors);
	}

	/**
	 * @param message
	 * @param cause
	 * @param errors
	 */
	public EOSForbiddenException(String message, Throwable cause,
			List<EOSError> errors) {
		super(message, cause, errors);
	}

}
