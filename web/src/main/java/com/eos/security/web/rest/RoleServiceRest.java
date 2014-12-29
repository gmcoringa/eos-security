/**
 * 
 */
package com.eos.security.web.rest;

import java.util.List;
import java.util.Set;

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

import com.eos.common.exception.EOSDuplicatedEntryException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.service.EOSPermissionService;
import com.eos.security.api.service.EOSRoleService;
import com.eos.security.api.vo.EOSGroup;
import com.eos.security.api.vo.EOSRole;
import com.eos.security.api.vo.EOSUser;

/**
 * @author santos.fabiano
 * 
 */
@Path("/role")
@Component
public class RoleServiceRest {

	private static EOSRoleService svcRole;
	private static EOSPermissionService svcPermission;

	@Context
	private HttpServletResponse response;

	@Autowired
	private void setRoleService(EOSRoleService eosRoleService) {
		svcRole = eosRoleService;
	}

	@Autowired
	private void setPermissionService(EOSPermissionService eosPermissionService) {
		svcPermission = eosPermissionService;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public EOSRole createRole(EOSRole role) throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		response.setStatus(Response.Status.CREATED.getStatusCode());
		return svcRole.createRole(role);
	}

	@Path("/{code}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateRole(@PathParam("code") String code, EOSRole role) throws EOSForbiddenException,
			EOSUnauthorizedException, EOSNotFoundException, EOSValidationException {
		svcRole.updateRole(role);
	}

	@Path("/{code}")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeRole(@PathParam("code") String code) throws EOSForbiddenException, EOSUnauthorizedException {
		svcRole.deleteRole(code);
	}

	@Path("/{code}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public EOSRole findRole(@PathParam("code") String code) throws EOSForbiddenException, EOSNotFoundException {
		return svcRole.findRole(code);
	}

	@Path("/find")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<EOSRole> findRoles(@QueryParam("code") List<String> codes) throws EOSForbiddenException {
		return svcRole.findRoles(codes);
	}

	@Path("/list")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<EOSRole> listRoles(@QueryParam("minimumLevel") Integer minimumLevel,
			@QueryParam("maximumLevel") Integer maximumLevel, @QueryParam("limit") @DefaultValue("20") int limit,
			@QueryParam("offset") @DefaultValue("0") int offset) throws EOSUnauthorizedException {
		return svcRole.listRoles(minimumLevel, maximumLevel, limit, offset);
	}

	// Role user

	@Path("{code}/user")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<EOSUser> listRoleUsers(@PathParam("code") String code,
			@QueryParam("limit") @DefaultValue("20") int limit, @QueryParam("offset") @DefaultValue("0") int offset) {
		return svcRole.listRoleUsers(code, limit, offset);
	}

	@Path("{code}/user")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void addUsers(@PathParam("code") String code, List<String> users) throws EOSForbiddenException,
			EOSUnauthorizedException {
		svcRole.addUsersToRole(code, users);
	}

	@Path("{code}/user")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeUsers(@PathParam("code") String code, List<String> users) throws EOSForbiddenException,
			EOSUnauthorizedException {
		svcRole.removeUsersFromRole(code, users);
	}

	// Role Group

	@Path("{code}/group")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<EOSGroup> listGroups(@PathParam("code") String code,
			@QueryParam("limit") @DefaultValue("20") int limit, @QueryParam("offset") @DefaultValue("0") int offset)
			throws EOSForbiddenException {
		return svcRole.listRoleGroups(code, limit, offset);
	}

	@Path("{code}/group")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void addGroups(@PathParam("code") String code, List<Long> groups) throws EOSForbiddenException,
			EOSUnauthorizedException {
		svcRole.addGroupsToRole(code, groups);
	}

	@Path("{code}/group")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeGroups(@PathParam("code") String code, List<Long> groups) throws EOSForbiddenException,
			EOSUnauthorizedException {
		svcRole.removeGroupsFromRole(code, groups);
	}

	// Permissions

	@Path("{code}/permission")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public Set<String> listPermissions(@PathParam("code") String code,
			@QueryParam("limit") @DefaultValue("20") int limit, @QueryParam("offset") @DefaultValue("0") int offset) {
		return svcPermission.listRolePermissions(code, limit, offset);
	}

	@Path("{code}/permission")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void addPermissions(@PathParam("code") String code, Set<String> permissions) throws EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		svcPermission.addRolePermissions(code, permissions);
	}

	@Path("{code}/permission")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public void removePermissions(@PathParam("code") String code, Set<String> permissions)
			throws EOSForbiddenException, EOSUnauthorizedException {
		svcPermission.removeRolePermission(code, permissions);
	}

}
