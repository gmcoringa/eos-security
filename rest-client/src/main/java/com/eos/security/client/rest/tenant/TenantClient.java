/**
 * 
 */
package com.eos.security.client.rest.tenant;

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
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.client.rest.AbstractRestClient;
import com.eos.security.client.rest.ClientConnection;

/**
 * Tenant rest client.
 *
 */
public class TenantClient extends AbstractRestClient {

	private static final Logger LOG = LoggerFactory.getLogger(TenantClient.class);
	private static final String TENANT_PATH = "/tenant/";

	public TenantClient(ClientConnection connection) {
		super(connection);
	}

	public void createTenant(EOSTenantCreateData tenant) {
		WebTarget target = createTarget(TENANT_PATH);

		try {
			Response response = setSession(target.request(MediaType.APPLICATION_JSON)).post(
					Entity.entity(tenant, MediaType.APPLICATION_JSON));

			if (response.getStatus() != Status.CREATED.getStatusCode()) {
//				throw new EOSException("Status: " + response.getStatus() + ", message: " + response.readEntity(String.class));
				EOSExceptionData exceptionData = response.readEntity(EOSExceptionData.class);
				LOG.debug("Failed to create tenant {}. Response: {}.", tenant, exceptionData);
				throw new EOSException(exceptionData.getMessage(), exceptionData.getErrors());
			}

		} catch (JSONException e) {
			throw new EOSException("Failed to create tenant", e);
		}
	}

	public EOSTenant findTenant(String alias) {
		WebTarget target = createTarget(TENANT_PATH + "/{alias}").resolveTemplate("alias", alias);

		try {
			Response response = setSession(target.request(MediaType.APPLICATION_JSON)).get();

			if (response.getStatus() != Status.OK.getStatusCode()) {
				EOSException exception = response.readEntity(EOSException.class);
				LOG.debug("Failed to find tenant {}.", response.readEntity(String.class));
				throw exception;
			}

			return response.readEntity(EOSTenant.class);
		} catch (JSONException e) {
			LOG.debug("Failed to create tenant with alias: {}. Response: {}.", alias, e);
			throw new EOSException("Failed to create tenant", e);
		}

	}

	public static TenantClient create(ClientConnection connection) {
		return new TenantClient(connection);
	}
}
