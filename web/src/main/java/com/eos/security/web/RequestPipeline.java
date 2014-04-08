/**
 * 
 */
package com.eos.security.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.util.StringUtil;
import com.eos.security.api.service.EOSSecurityService;

/**
 * @author santos.fabiano
 * 
 */
@Component
public class RequestPipeline implements Filter {

	private static final Logger log = LoggerFactory
			.getLogger(RequestPipeline.class);

	public static final String SESSION_HEADER = "ppk";

	@Autowired
	private EOSSecurityService svcSecurity;

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// Nothing for now

	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		final HttpServletRequest httpReq = (HttpServletRequest) request;
		final HttpServletResponse httpRes = (HttpServletResponse) response;
		String uri = httpReq.getRequestURI();

		if (uri.startsWith(RestSecurityConfiguration.REST_PATH)) {
			// Setup context for all rest requests
			try {
				setupContext(httpReq, httpRes);
			} catch (EOSNotFoundException e) {
				log.debug("Invalid session ID", e);
				httpRes.setStatus(HttpServletResponse.SC_NOT_FOUND);
				// TODO setup response entity
				// httpRes.setContentType(MediaType.APPLICATION_JSON);
				return;
			}
		}

		chain.doFilter(request, response);
	}

	private void setupContext(HttpServletRequest request,
			HttpServletResponse response) throws EOSNotFoundException {
		String sessionId = getSessionCookie(request);

		if (!StringUtil.isEmpty(sessionId)) {
			svcSecurity.setupSession(sessionId);
		}
	}

	private String getSessionCookie(HttpServletRequest request) {
		return request.getHeader(SESSION_HEADER);
	}

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		// Nothing for now

	}

}
