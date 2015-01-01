package com.eos.security.web;

import java.io.IOException;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ResourceConfig;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eos.common.exception.EOSException;

/**
 * Rest configuration. Setup rest resources.
 *
 */
@Component
public class RestConfiguration extends ResourceConfig {

	private static final Logger LOG = LoggerFactory.getLogger(RestConfiguration.class);

	public RestConfiguration() {
		super();

		LOG.info("Starting resource setup");

		try {
			registerClasses(findResources());
		} catch (Exception e) {
			throw new EOSException("Failed to load rest resources.", e);
		}
	}

	private Set<Class<?>> findResources() throws IOException {
		Reflections reflections = new Reflections("com.eos.security.web");
		Set<Class<?>> resources = reflections.getTypesAnnotatedWith(Path.class);
		resources.addAll(reflections.getTypesAnnotatedWith(Provider.class));

		return resources;
	}

}
