package com.eos.security.web.util;

import java.util.Comparator;

/**
 * Compares Objects based on there exact classes.
 * 
 * @author santos.fabiano
 */
public class ByClassComparator implements Comparator<Object> {

	private final ClassComparator inner = new ClassComparator();

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object object, Object other) {
		if (object == null && other == null) {
			return 0;
		} else if (object == null || other == null) {
			return -1;
		}
		
		return inner.compare(object.getClass(), other.getClass());
	}

}
