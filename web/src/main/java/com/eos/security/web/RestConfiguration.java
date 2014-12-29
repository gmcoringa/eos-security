package com.eos.security.web;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ResourceConfig;
import org.scannotation.AnnotationDB;
import org.scannotation.WarUrlFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eos.common.exception.EOSException;

/**
 * Rest configuration. Setup rest resources.
 *
 */
public class RestConfiguration extends ResourceConfig {

	private static final Logger LOG = LoggerFactory.getLogger(RestConfiguration.class);
	private final ServletContext context;

	public RestConfiguration(@Context ServletContext context) {
		super();

		this.context = context;
		LOG.info("Starting resource setup");

		try {
			registerClasses(findResources());
		} catch (Exception e) {
			throw new EOSException("Failed to load rest resources.", e);
		}
	}

	private Set<Class<?>> findResources() throws IOException {
		AnnotationDB db = new AnnotationDB();
		db.setScanPackages(new String[] { "com.eos.security.web" });
		db.scanArchives(WarUrlFinder.findWebInfClassesPath(context));
		Set<String> classes = db.getAnnotationIndex().get(Path.class.getName());
		classes.addAll(db.getAnnotationIndex().get(Provider.class.getName()));

		Set<Class<?>> resources = new LinkedHashSet<>(classes.size());

		for (String clazz : classes) {
			try {
				resources.add(Class.forName(clazz));
				LOG.debug("Added resource {} to context.", clazz);
			} catch (ClassNotFoundException e) {
				LOG.warn("Resource not found for class {}. Ignoring", clazz);
				LOG.debug("Resource not found for class {}. Ignoring", clazz, e);
			}
		}

		return resources;
	}

}
