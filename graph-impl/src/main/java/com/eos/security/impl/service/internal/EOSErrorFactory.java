/**
 * 
 */
package com.eos.security.impl.service.internal;

import java.util.ArrayList;
import java.util.List;

import com.eos.common.exception.EOSError;

/**
 * Error factory.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSErrorFactory {

	private final List<EOSError> errors;

	/**
	 * Default constructor.
	 */
	public EOSErrorFactory() {
		super();
		errors = new ArrayList<>(16);
	}

	/**
	 * Add a new error.
	 * 
	 * @param code
	 *            The error code.
	 * @param message
	 *            The error message.
	 * @return This error factory.
	 */
	public EOSErrorFactory addError(Integer code, String message) {
		return addError(new EOSError(code, message));
	}

	/**
	 * Add a new error.
	 * 
	 * @param error
	 *            The error object.
	 * @return This error factory.
	 */
	public EOSErrorFactory addError(EOSError error) {
		if (error != null) {
			errors.add(error);
		}

		return this;

	}

	/**
	 * Verify if this factory has any errors.
	 * 
	 * @return True if has errors, false otherwise.
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	/**
	 * @return the errors
	 */
	public final List<EOSError> getErrors() {
		return errors;
	}

	/**
	 * Build an error factory.
	 * 
	 * @return An error factory instance.
	 */
	public static EOSErrorFactory build() {
		return new EOSErrorFactory();
	}
}
