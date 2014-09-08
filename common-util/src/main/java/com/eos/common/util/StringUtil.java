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
	public static boolean isBlankOrNull(String value) {
		return value == null || value.trim().isEmpty();
	}
	

	public static boolean isNotBlankOrNull(String value) {
		return !isBlankOrNull(value);
	}
	

}
