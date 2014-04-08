/**
 * 
 */
package com.eos.security.web.util;

import com.eos.common.exception.EOSException;
import com.eos.security.web.dto.EOSExceptionData;

/**
 * Utilities for web module.
 * 
 * @author santos.fabiano
 * 
 */
public class WebUtils {

	/**
	 * Extract errors from an exception.
	 * 
	 * @param exception
	 *            Exception to extract errors.
	 * @return Return exception data errors object.
	 */
	public static EOSExceptionData formatResponse(EOSException exception) {
		return new EOSExceptionData(exception.getMessage(),
				exception.getErrors());
	}
}
