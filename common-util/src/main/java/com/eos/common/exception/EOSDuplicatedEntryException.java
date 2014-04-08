/**
 * 
 */
package com.eos.common.exception;

import java.util.List;

/**
 * Exception to indicate duplicated entries, the item to create mau alredy
 * exists.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSDuplicatedEntryException extends EOSException {

	private static final long serialVersionUID = -4041543559530633201L;

	/**
	 * @param message
	 * @param cause
	 */
	public EOSDuplicatedEntryException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param errors
	 */
	public EOSDuplicatedEntryException(String message, Throwable cause,
			List<EOSError> errors) {
		super(message, cause, errors);
	}

	/**
	 * @param message
	 */
	public EOSDuplicatedEntryException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param errors
	 */
	public EOSDuplicatedEntryException(String message, List<EOSError> errors) {
		super(message, errors);
	}

}
