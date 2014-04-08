/**
 * 
 */
package com.eos.common.util;

/**
 * String utilities.
 * 
 * @author fabiano.santos
 * 
 */
public class StringUtil {

	/**
	 * Validate if the given value is null or empty.
	 * 
	 * @param value
	 *            String to be validated.
	 * @return True if value is empty or null, false otherwise.
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

}
