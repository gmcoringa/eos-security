/**
 * 
 */
package com.eos.security.impl.service.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.neo4j.graphdb.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to convert nodes to POJO objects.
 * 
 * @author fabiano.santos
 * 
 */
public class ReflectionUtil {

	private static final Logger log = LoggerFactory.getLogger(ReflectionUtil.class);

	public static <T> T convert(Node node, Class<T> clazz) {
		try {
			T object = clazz.newInstance();

			for (Field field : clazz.getDeclaredFields()) {
				// Ignore transient fields or array fields
				// TODO deal with arrays
				if (Modifier.isTransient(field.getModifiers()) || field.getDeclaringClass().isArray()) {
					continue;
				}
				// Ignore not found values
				String value = (String) node.getProperty(field.getName(), null);
				if (value == null) {
					continue;
				}

				setValue(field, object, value);
			}

			return object;
		} catch (InstantiationException | IllegalAccessException | SecurityException e) {
			log.error("Failed to convert node", e);
			return null;
		}
	}

	private static <T> void setValue(Field field, T object, String value) {
		if (field.isEnumConstant()) {
			try {
				field.getDeclaringClass().getMethod("valueOf", String.class).invoke(object, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				log.debug("Failed to set value[" + value + "] to enum field: " + field.getName());
			}
		} else {
			try {
				Constructor<?> constructor = field.getDeclaringClass().getConstructor(String.class);
				field.set(object, constructor.newInstance(value));
			} catch (NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException
					| InstantiationException | InvocationTargetException e) {
				log.debug("Failed to set value[" + value + "] to field: " + field.getName());
			}
		}
	}

}
