/**
 * 
 */
package com.eos.security.web.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eos.common.exception.EOSException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.service.EOSSecurityService;
import com.eos.security.api.session.SessionContext;
import com.eos.security.web.RequestPipeline;
import com.eos.security.web.dto.EOSCredentials;

/**
 * @author santos.fabiano
 * 
 */
@Path("/access")
@Component
public class SecurityServiceRest {

	@Context
	private HttpServletRequest request;

	private static EOSSecurityService svcSecurity;

	@Autowired
	private void setRoleService(EOSSecurityService eosSecurityService) {
		svcSecurity = eosSecurityService;
	}

	@Path("/login")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void login(EOSCredentials credentials) throws EOSException {
		svcSecurity.login(credentials.getLogin(), credentials.getEmail(),
				credentials.getPassword(), credentials.isKeepConnected());
	}

	@Path("/logout")
	@PUT
	public void logout() throws EOSUnauthorizedException {
		svcSecurity.logout();
	}

	@Path("/context")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public SessionContext getSessionContext() throws EOSNotFoundException {
		String sessionId = request.getHeader(RequestPipeline.SESSION_HEADER);
		return svcSecurity.getSessionContext(sessionId);
	}
}
