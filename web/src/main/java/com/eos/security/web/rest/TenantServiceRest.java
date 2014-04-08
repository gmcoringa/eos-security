/**
 * 
 */
package com.eos.security.web.rest;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eos.common.EOSState;
import com.eos.common.exception.EOSException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.service.EOSTenantService;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.web.dto.EOSTenantCreateData;

/**
 * Tenant rest services.
 * 
 * @author santos.fabiano
 * 
 */
@Path("/tenant")
@Component
public class TenantServiceRest {

	private static EOSTenantService svcTenant;

	@Context
	private HttpServletResponse response;

	@Autowired
	private void setTenantService(EOSTenantService eosTenantService) {
		svcTenant = eosTenantService;
	}

	@Path("/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public EOSTenant createTenant(EOSTenantCreateData data) throws EOSException {

		// Create tenant
		EOSTenant tenant = svcTenant.createTenant(data.getTenant(), data.getData(), data.getAdminUser());
		response.setStatus(Response.Status.CREATED.getStatusCode());
		return tenant;
	}

	@Path("/{tenantId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public EOSTenant findTenant(@PathParam("tenant") Long tenantId) throws EOSNotFoundException {
		EOSTenant tenant = svcTenant.findTenant(tenantId);

		if (tenant == null) {
			throw new EOSNotFoundException("Tenant not found");
		}

		return tenant;
	}

	@Path("/list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<EOSTenant> listTenants(@QueryParam("state") List<EOSState> states,
			@QueryParam("limit") @DefaultValue("20") int limit, @QueryParam("offset") @DefaultValue("0") int offset) {
		return svcTenant.listTenants(states, limit, offset);
	}

	@Path("/{tenantId}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateTenant(@PathParam("tenant") Long tenantId, EOSTenant tenant) throws EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException, EOSNotFoundException {
		tenant.setId(tenantId);
		svcTenant.updateTenant(tenant);
	}

	@Path("/{tenantId}/active")
	@PUT
	public void activeTenant(@PathParam("tenant") Long tenantId) throws EOSForbiddenException,
			EOSUnauthorizedException, EOSNotFoundException {
		svcTenant.updateTenantState(tenantId, EOSState.ACTIVE);
	}

	@Path("/{tenantId}")
	@DELETE
	public void disableTenant(@PathParam("tenant") Long tenantId) throws EOSForbiddenException,
			EOSUnauthorizedException, EOSNotFoundException {
		svcTenant.updateTenantState(tenantId, EOSState.DISABLED);
	}

	@Path("/{tenantId}/purge")
	@DELETE
	public void purgeTenant(@PathParam("tenant") Long tenantId) throws EOSForbiddenException, EOSUnauthorizedException {
		svcTenant.purgeTenant(tenantId);
	}

	// TENANT DATA

	@Path("/{tenantId}/data")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateTenantData(@PathParam("tenantId") Long tenantId, Map<String, String> data)
			throws EOSForbiddenException, EOSUnauthorizedException {
		svcTenant.updateTenantData(tenantId, data);
	}

	@Path("/{tenantId}/data")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, String> listTenantData(@PathParam("tenantId") Long tenantId,
			@QueryParam("limit") @DefaultValue("20") int limit, @QueryParam("offset") @DefaultValue("0") int offset)
			throws EOSForbiddenException, EOSUnauthorizedException {
		return svcTenant.listTenantData(tenantId, limit, offset);
	}

	@Path("/{tenantId}/data/find")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, String> listTenantData(@PathParam("tenantId") Long tenantId, @QueryParam("key") List<String> keys)
			throws EOSForbiddenException, EOSUnauthorizedException {
		return svcTenant.listTenantData(tenantId, keys);
	}
}
