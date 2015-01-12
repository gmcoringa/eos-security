package com.eos.security.client.rest.user;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eos.common.exception.EOSException;
import com.eos.common.exception.EOSExceptionData;
import com.eos.security.api.web.resources.EOSUserCreateData;
import com.eos.security.client.rest.AbstractRestClient;
import com.eos.security.client.rest.ClientConnection;

public class UserClient extends AbstractRestClient {

	private static final Logger LOG = LoggerFactory.getLogger(UserClient.class);
	private static final String TENANT_PATH = "/user/";

	private UserClient(ClientConnection connection) {
		super(connection);
	}

	public void createUser(EOSUserCreateData user) {
		WebTarget target = createTarget(TENANT_PATH);

		try {
			Response response = setSession(target.request(MediaType.APPLICATION_JSON)).post(
					Entity.entity(user, MediaType.APPLICATION_JSON));

			if (response.getStatus() != Status.CREATED.getStatusCode()) {
				EOSExceptionData exceptionData = response.readEntity(EOSExceptionData.class);
				LOG.debug("Failed to create user {}. Response: {}.", user, exceptionData);
				throw new EOSException(exceptionData.getMessage(), exceptionData.getErrors());
			}

		} catch (JSONException e) {
			throw new EOSException("Failed to create user", e);
		}
	}

	public static UserClient create(ClientConnection connection) {
		return new UserClient(connection);
	}

}
