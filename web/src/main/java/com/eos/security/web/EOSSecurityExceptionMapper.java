/**
 * 
 */
package com.eos.security.web;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eos.common.exception.EOSError;
import com.eos.common.exception.EOSErrorCodes;
import com.eos.common.exception.EOSException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.web.util.WebUtils;

/**
 * Mapper to handle exceptions.
 * 
 * @author santos.fabiano
 * 
 */
@Provider
public class EOSSecurityExceptionMapper implements ExceptionMapper<Exception> {

	private static final Logger log = LoggerFactory.getLogger(EOSSecurityExceptionMapper.class);

	private static final int SC_UNPROCESSABLE_ENTITY = 422;

	/**
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Exception)
	 */
	@Override
	public Response toResponse(Exception exception) {
		log.error("Returning exception as JSON.", exception);
		ResponseBuilder builder = null;
		EOSException eosException = null;

		if (EOSNotFoundException.class.isAssignableFrom(exception.getClass())) {
			builder = Response.status(HttpServletResponse.SC_NOT_FOUND);
			eosException = (EOSException) exception;
		} else if (EOSForbiddenException.class.isAssignableFrom(exception.getClass())) {
			builder = Response.status(HttpServletResponse.SC_FORBIDDEN);
			eosException = (EOSException) exception;
		} else if (EOSUnauthorizedException.class.isAssignableFrom(exception.getClass())) {
			builder = Response.status(HttpServletResponse.SC_UNAUTHORIZED);
			eosException = (EOSException) exception;
		} else if (EOSException.class.isAssignableFrom(exception.getClass())) {
			builder = Response.status(HttpServletResponse.SC_BAD_REQUEST);
			eosException = (EOSException) exception;
		} else if (EOSValidationException.class.isAssignableFrom(exception.getClass())) {
			builder = Response.status(SC_UNPROCESSABLE_ENTITY);
			eosException = (EOSException) exception;
		} else {
			builder = Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			EOSError error = new EOSError(EOSErrorCodes.GENERIC, exception.getMessage());
			eosException = new EOSException(exception.getMessage(), Arrays.asList(error));
		}

		builder.type(MediaType.APPLICATION_JSON).entity(WebUtils.formatResponse(eosException));
		return builder.build();
	}

}
