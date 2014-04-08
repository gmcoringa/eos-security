/**
 * 
 */
package com.eos.common.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author santos.fabiano
 * 
 */
public class CollectionUtil {

	/**
	 * Build an set.
	 * 
	 * @param values
	 *            Values to be added in a set.
	 * @return A set containing all given values.
	 */
	@SafeVarargs
	public static <T> Set<T> asSet(T... values) {
		if (values == null || values.length == 0) {
			return Collections.emptySet();
		} else {
			// A lit bit higher than load factor
			int size = (int) Math.floor(values.length * 1.4);
			Set<T> set = new HashSet<>(size);
			for (T value : values) {
				set.add(value);
			}

			return set;
		}
	}
}
