/**
 * 
 */
package com.eos.security.web;

import java.io.IOException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * As is not possible to change the status code of a response without return a
 * {@link Response} object, this class do this work.
 * 
 * @author santos.fabiano
 * 
 */
@Provider
public class EOSRestFilter implements ContainerResponseFilter {

	private static final Logger log = LoggerFactory
			.getLogger(EOSRestFilter.class);

	/**
	 * Set response status code 201 (CREATED) for all post methods that are
	 * success.
	 * 
	 * @see javax.ws.rs.container.ContainerResponseFilter#filter(javax.ws.rs.container.ContainerRequestContext,
	 *      javax.ws.rs.container.ContainerResponseContext)
	 */
	@Override
	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException {
		log.debug("Response filtering");
		// POST method should return Response.Status.CREATED
		if (requestContext.getMethod().equals(HttpMethod.POST)
				&& responseContext.getStatus() == Response.Status.OK
						.getStatusCode()) {
			responseContext.setStatus(Response.Status.CREATED.getStatusCode());
		}
	}
}
