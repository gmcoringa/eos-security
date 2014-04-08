/**
 * 
 */
package com.eos.common.exception;

import java.util.List;

/**
 * @author fabiano.santos
 * 
 */
public class EOSIllegalArgumentException extends EOSException {

	private static final long serialVersionUID = 5089256101410659226L;

	/**
	 * @param message
	 * @param cause
	 */
	public EOSIllegalArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param errors
	 */
	public EOSIllegalArgumentException(String message, Throwable cause,
			List<EOSError> errors) {
		super(message, cause, errors);
	}

	/**
	 * @param message
	 */
	public EOSIllegalArgumentException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param errors
	 */
	public EOSIllegalArgumentException(String message, List<EOSError> errors) {
		super(message, errors);
	}

}
