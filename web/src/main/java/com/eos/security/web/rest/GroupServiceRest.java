/**
 * 
 */
package com.eos.security.web.rest;

import java.util.List;

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
import com.eos.security.api.service.EOSGroupService;
import com.eos.security.api.service.EOSRoleService;
import com.eos.security.api.vo.EOSGroup;
import com.eos.security.api.vo.EOSRole;
import com.eos.security.api.vo.EOSUser;

/**
 * Group Rest Service.
 * 
 * @author santos.fabiano
 * 
 */
@Path("/group")
@Component
public class GroupServiceRest {

	private static EOSGroupService svcGroup;
	private static EOSRoleService svcRole;

	@Context
	private HttpServletResponse response;

	@Autowired
	private void setGroupService(EOSGroupService eosGroupService) {
		svcGroup = eosGroupService;
	}

	@Autowired
	private void setRoleService(EOSRoleService eosRoleService) {
		svcRole = eosRoleService;
	}

	// Group

	@Path("/{groupId}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateGroup(@PathParam("groupId") Long groupId, EOSGroup group)
			throws EOSForbiddenException, EOSUnauthorizedException,
			EOSNotFoundException, EOSValidationException {
		group.setId(groupId);
		svcGroup.updateGroup(group);
	}

	@Path("/{groupId}")
	@DELETE
	public void deleteGroup(@PathParam("groupId") Long groupId)
			throws EOSForbiddenException, EOSUnauthorizedException,
			EOSNotFoundException {
		svcGroup.deleteGroup(groupId);
	}

	@Path("/{groupId}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public EOSGroup findGroup(@PathParam("groupId") Long groupId)
			throws EOSForbiddenException, EOSNotFoundException {
		return svcGroup.findGroup(groupId);
	}

	@Path("/find")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<EOSGroup> findGroups(@QueryParam("groupId") List<Long> groups)
			throws EOSForbiddenException {
		return svcGroup.findGroups(groups);
	}

	@Path("/list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<EOSGroup> listGroups(
			@QueryParam("miniLevel") Integer minimumLevel,
			@QueryParam("maxLevel") Integer maximumLevel,
			@QueryParam("limit") @DefaultValue("20") int limit,
			@QueryParam("offset") @DefaultValue("0") int offset)
			throws EOSForbiddenException {
		return svcGroup.listGroups(minimumLevel, maximumLevel, limit, offset);
	}

	@Path("")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public EOSGroup createGroup(EOSGroup group)
			throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		group = svcGroup.createGroup(group);
		response.setStatus(Response.Status.CREATED.getStatusCode());
		return group;
	}

	// Group User

	@Path("/{groupId}/user")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void addUsers(@PathParam("groupId") Long groupId, List<String> users)
			throws EOSForbiddenException, EOSUnauthorizedException {
		svcGroup.addUsersToGroup(groupId, users);
	}

	@Path("/{groupId}/user")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeUsers(@PathParam("id") Long groupId, List<String> users)
			throws EOSForbiddenException, EOSUnauthorizedException {
		svcGroup.removeUsersFromGroup(groupId, users);
	}

	@Path("/{groupId}/user")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<EOSUser> listUsers(@PathParam("groupId") Long groupId,
			@QueryParam("limit") @DefaultValue("20") int limit,
			@QueryParam("offset") @DefaultValue("0") int offset) {
		return svcGroup.listGroupUsers(groupId, limit, offset);
	}

	// Group Role

	@Path("/{groupId}/role")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void addRoles(@PathParam("groupId") Long groupId, List<String> codes)
			throws EOSForbiddenException, EOSUnauthorizedException {
		svcRole.addRolesToGroup(groupId, codes);
	}

	@Path("/{groupId}/role")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeRoles(@PathParam("groupId") Long groupId,
			List<String> codes) throws EOSForbiddenException,
			EOSUnauthorizedException {
		svcRole.removeRolesFromGroup(groupId, codes);
	}

	@Path("/{groupId}/role")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<EOSRole> listRole(@PathParam("groupId") Long groupId,
			@QueryParam("limit") @DefaultValue("20") int limit,
			@QueryParam("offset") @DefaultValue("0") int offset)
			throws EOSForbiddenException {
		return svcRole.listGroupRoles(groupId, limit, offset);
	}

}
