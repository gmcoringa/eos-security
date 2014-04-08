/**
 * 
 */
package com.eos.security.web;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;

import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.eos.security.web.util.ByClassComparator;
import com.eos.security.web.util.ClassComparator;

/**
 * Rest application configuration.
 * 
 * @author santos.fabiano
 * 
 */
@ApplicationPath(RestSecurityConfiguration.REST_PATH)
@Component
public class RestSecurityConfiguration extends Application {

	public static final String REST_PATH = "/api";
	public static final String SESSION_COOKIE_NAME = "ppk";
	public static final String TENANT_HEADER = "X-Tenant";

	private static final Logger log = LoggerFactory
			.getLogger(RestSecurityConfiguration.class);

	private static final Set<Object> singletons = new ConcurrentSkipListSet<>(
			new ByClassComparator());
	private static final Set<Class<?>> classes = new ConcurrentSkipListSet<>(
			new ClassComparator());
	private static ApplicationContext context;

	/**
	 * Default constructor.
	 */
	public RestSecurityConfiguration() {
		super();
	}

	/**
	 * @see javax.ws.rs.core.Application#getClasses()
	 */
	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

	/**
	 * @see javax.ws.rs.core.Application#getSingletons()
	 */
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

	/**
	 * Scan classes within this class path that are annotated with @Path.
	 */
	private void initClasses() {
		log.info("Initializing Rest classes loockup");
		AnnotationDB annonDB = new AnnotationDB();
		try {
			// scan classes within this class path
			annonDB.scanArchives(ClasspathUrlFinder.findClassBase(this
					.getClass()));
			Set<String> classNames = annonDB.getAnnotationIndex().get(
					Path.class.getName());

			if (classNames == null || classNames.isEmpty()) {
				log.warn("No classes annotated with @Path found");
				return;
			}

			// Add providers
			classNames.addAll(annonDB.getAnnotationIndex().get(
					Provider.class.getName()));

			for (String className : classNames) {
				Class<?> clazz = Class.forName(className);
				log.debug("Adding class: " + clazz.getName());
				classes.add(clazz);
			}
		} catch (IOException | ClassNotFoundException e) {
			log.error("Failed to load rest classes", e);
		}
	}

	/**
	 * Initialize singletons rest classes. Lookup for Spring beans.
	 */
	private void initSingletons() {
		log.info("Initializng Rest singletons");

		for (Class<?> clazz : classes) {
			try {
				log.debug("Initializing " + clazz.getName());
				if (clazz.isAnnotationPresent(Component.class)) {
					// lookup for spring bean
					singletons.add(context.getBean(clazz));
				} else {
					// Not a spring bean, create new instance
					singletons.add(clazz.newInstance());
				}
			} catch (BeansException | InstantiationException
					| IllegalAccessException e) {
				log.error("Service not found for " + clazz.getName());
				log.debug("Failed to load service", e);
			}
		}
	}

	/**
	 * Set the application context.
	 * 
	 * @param applicationContext
	 *            The application context to be set.
	 */
	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		log.info("Initializng EOS-Security-Web application context");
		context = applicationContext;
	}

	/**
	 * Initialize rest services.
	 */
	@PostConstruct
	public void init() {
		initClasses();
		initSingletons();
	}
}
