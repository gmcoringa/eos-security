/**
 * 
 */
package com.eos.security.web.util;

import java.util.Comparator;

/**
 * Compares class definitions.
 * 
 * @author santos.fabiano
 */
public class ClassComparator implements Comparator<Class<?>> {

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Class<?> clazz, Class<?> other) {
		if (clazz == null && other == null) {
			return 0;
		} else if (clazz == null || other == null) {
			return -1;
		}

		return clazz.getName().compareTo(other.getName());
	}

}
