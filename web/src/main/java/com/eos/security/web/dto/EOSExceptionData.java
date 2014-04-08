/**
 * 
 */
package com.eos.security.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.eos.common.exception.EOSError;

/**
 * Exception data object.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSExceptionData implements Serializable {

	private static final long serialVersionUID = -809463789192953938L;
	private String message;
	private List<EOSError> errors = new ArrayList<>();

	/**
	 * Default constructor.
	 */
	public EOSExceptionData() {
		super();
	}

	/**
	 * Constructor with parameters.
	 * 
	 * @param message
	 * @param errors
	 */
	public EOSExceptionData(String message, List<EOSError> errors) {
		this();
		setMessage(message);
		setErrors(errors);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public EOSExceptionData setMessage(String message) {
		this.message = message;
		return this;
	}

	/**
	 * @return the errors
	 */
	public List<EOSError> getErrors() {
		return errors;
	}

	/**
	 * @param errors
	 *            the errors to set
	 */
	public EOSExceptionData setErrors(List<EOSError> errors) {
		if (errors == null) {
			this.errors.clear();
		} else {
			this.errors = errors;
		}

		return this;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExceptionData [message=" + message + ", errors=" + errors + "]";
	}

}
