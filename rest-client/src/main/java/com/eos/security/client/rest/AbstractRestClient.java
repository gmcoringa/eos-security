package com.eos.security.client.rest;

import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRestClient {

	protected static final Logger log = LoggerFactory.getLogger(AbstractRestClient.class);

	private final ClientConnection connection;

	protected final Client client = ClientBuilder.newClient().register(JacksonFeature.class)
			.register(RestClientFilter.class);

	protected AbstractRestClient(ClientConnection connection) {
		this.connection = connection;
	}

	protected WebTarget createTarget(String path) {
		String uri = connection.getHost() + path;
		log.debug("Path: " + uri);
		return client.target(uri);
	}

	protected Builder setSession(Builder builder) {
		//if (RestClientContext.getInstance().isLogged()) {
		//	for (Cookie cookie : RestClientContext.getInstance().getSessionCookies()) {
		//		builder = builder.cookie(cookie);
		//	}
		//}

		return builder;
	}

	protected Cookie getCookie(String name, Response response) {
		for (Entry<String, NewCookie> entry : response.getCookies().entrySet()) {
			if (entry.getKey().equals(name)) {
				return entry.getValue();
			}
		}

		return null;
	}
}
