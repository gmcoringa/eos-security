package com.eos.security.client.rest;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter for dealing with cookie information.
 */
@Provider
public class RestClientFilter implements ClientRequestFilter, ClientResponseFilter {

    private static final Logger log = LoggerFactory.getLogger(RestClientFilter.class);

    /**
     * @see javax.ws.rs.client.ClientRequestFilter#filter(javax.ws.rs.client.ClientRequestContext)
     */
    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {

        for (Cookie cookie : requestContext.getCookies().values()) {
            log.debug("Request Cookie: " + cookie.toString());
        }
    }

    /**
     * @see javax.ws.rs.client.ClientResponseFilter#filter(javax.ws.rs.client.ClientRequestContext,
     *      javax.ws.rs.client.ClientResponseContext)
     */
    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {

        log.debug("Response status: " + responseContext.getStatus());

        for (Cookie cookie : responseContext.getCookies().values()) {
            log.debug("Response Cookie: " + cookie.toString());
        }
    }

}
