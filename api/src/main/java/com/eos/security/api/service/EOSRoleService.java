/**
 * 
 */
package com.eos.security.api.service;

import java.util.List;

import com.eos.common.EOSLevel;
import com.eos.common.exception.EOSDuplicatedEntryException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.vo.EOSGroup;
import com.eos.security.api.vo.EOSRole;
import com.eos.security.api.vo.EOSUser;

/**
 * Role service utility, deals with role, role user relations, role group
 * relations and role permissions.
 * 
 * @author fabiano.santos
 * 
 */
public interface EOSRoleService {

	// Role

	/**
	 * Creates a new role.
	 * 
	 * @param role
	 *            The role info.
	 * @return The created role.
	 * @throws EOSDuplicatedEntryException
	 *             If the role already exists.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for role creation.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can create roles.
	 * @throws EOSValidationException
	 *             If the role contains any invalid field. For group code only
	 *             number, characters, dot and hyphen are allowed.
	 */
	public EOSRole createRole(EOSRole role) throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException;

	/**
	 * Updates the role info.
	 * 
	 * @param role
	 *            The role info.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for role update.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can update roles.
	 * @throws EOSNotFoundException
	 *             If the role with the given code do not exists.
	 * @throws EOSValidationException
	 *             If the role contains any invalid field.
	 */
	public void updateRole(EOSRole role) throws EOSForbiddenException, EOSUnauthorizedException, EOSNotFoundException,
			EOSValidationException;

	/**
	 * Delete the given role and all its relations (Users, Groups, Permissions).
	 * 
	 * @param code
	 *            The role code.
	 * @throws EOSForbiddenException
	 *             If the user do not have permission for role deletion.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can delete roles.
	 */
	public void deleteRole(String code) throws EOSForbiddenException, EOSUnauthorizedException;

	/**
	 * Find a role by its code.
	 * 
	 * @param code
	 *            The role code.
	 * @return Role information.
	 * @throws EOSForbiddenException
	 *             For {@link EOSLevel#INTERNAL} special permissions are
	 *             required.
	 * @throws EOSNotFoundException
	 *             If the role with the given code do not exists.
	 */
	public EOSRole findRole(String code) throws EOSForbiddenException, EOSNotFoundException;

	/**
	 * Find roles by their code.
	 * 
	 * @param codes
	 *            List of role codes.
	 * @return List of roles found.
	 * @throws EOSForbiddenException
	 *             For {@link EOSLevel#INTERNAL} special permissions are
	 *             required.
	 */
	public List<EOSRole> findRoles(List<String> codes) throws EOSForbiddenException;

	/**
	 * List roles filtered by level.
	 * 
	 * @param minimumLevel
	 *            Minimum group level, if null this value will be set to
	 *            {@link EOSLevel#PUBLIC}.
	 * @param maximumLevel
	 *            Maximum group level, if null this value will be set to
	 *            {@link EOSLevel#MAXIMUM}.
	 * @param limit
	 *            Maximum number of registers.
	 * @param offset
	 *            Initial point.
	 * @return Group list.
	 * @throws EOSUnauthorizedException
	 *             For {@link EOSLevel#INTERNAL} is required special
	 *             permissions.
	 */
	public List<EOSRole> listRoles(Integer minimumLevel, Integer maximumLevel, int limit, int offset)
			throws EOSUnauthorizedException;

	// Role User

	/**
	 * Add users to a role.
	 * 
	 * @param code
	 *            The role code to add users.
	 * @param users
	 *            List of users to be added.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission to add users to a role.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate roles.
	 */
	public void addUsersToRole(String code, List<String> users) throws EOSForbiddenException, EOSUnauthorizedException;

	/**
	 * Add the roles to a user.
	 * 
	 * @param login
	 *            User login who will receive roles.
	 * @param roles
	 *            Role list to be added to the user.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission to add roles to user.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate roles.
	 */
	public void addRolesToUser(String login, List<String> roles) throws EOSForbiddenException, EOSUnauthorizedException;

	/**
	 * Remove users from a role.
	 * 
	 * @param code
	 *            The role code where the users will be removed.
	 * @param users
	 *            The list of users to be removed.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission to remove users from a
	 *             role.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate roles.
	 */
	public void removeUsersFromRole(String code, List<String> users) throws EOSForbiddenException,
			EOSUnauthorizedException;

	/**
	 * Remove roles from a user.
	 * 
	 * @param login
	 *            User who will have roles removed.
	 * @param roles
	 *            Roles to be removed from the user.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission to remove roles from a
	 *             user.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate roles.
	 */
	public void removeRolesFromUser(String login, List<String> roles) throws EOSForbiddenException,
			EOSUnauthorizedException;

	/**
	 * Remove the user from all roles that he/she belongs.
	 * 
	 * @param login
	 *            User who will have roles removed.
	 * @throws EOSForbiddenException
	 *             If the logged user do not have permission to remove roles
	 *             from a user.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate roles.
	 */
	public void removeRolesByUser(String login) throws EOSForbiddenException, EOSUnauthorizedException;

	/**
	 * List users from a role.
	 * 
	 * @param code
	 *            The role code.
	 * @param limit
	 *            Maximum number of registers.
	 * @param offset
	 *            Initial point.
	 * @return List of users.
	 */
	public List<EOSUser> listRoleUsers(String code, int limit, int offset);

	/**
	 * List all roles that the given user has.
	 * 
	 * @param login
	 *            User login.
	 * @param limit
	 *            Maximum number of registers.
	 * @param offset
	 *            Initial point.
	 * @return List of roles from the given user.
	 * @throws EOSForbiddenException
	 *             For {@link EOSLevel#INTERNAL} special permissions are
	 *             required.
	 */
	public List<EOSRole> listUserRoles(String login, int limit, int offset) throws EOSForbiddenException;

	/**
	 * Retrieve all codes from all roles that a user has.
	 * 
	 * @param login
	 *            User login.
	 * @return List of role codes from a user.
	 */
	public List<String> listUserRoleCodes(String login);

	// Role Group

	/**
	 * Add groups to a role.
	 * 
	 * @param code
	 *            The role code.
	 * @param groups
	 *            The list of groups to be added.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission to add groups to a
	 *             role.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate roles.
	 */
	public void addGroupsToRole(String code, List<Long> groups) throws EOSForbiddenException, EOSUnauthorizedException;

	/**
	 * Add roles to a group.
	 * 
	 * @param groupId
	 *            The group ID to add roles.
	 * @param codes
	 *            List of role codes to be added to a group.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission to add roles to a
	 *             group.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate roles and groups.
	 */
	public void addRolesToGroup(Long groupId, List<String> codes) throws EOSForbiddenException,
			EOSUnauthorizedException;

	/**
	 * Remove groups from a role.
	 * 
	 * @param code
	 *            The role code.
	 * @param groups
	 *            List of groups to be removed.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission to remove groups from a
	 *             role.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate roles.
	 */
	public void removeGroupsFromRole(String code, List<Long> groups) throws EOSForbiddenException,
			EOSUnauthorizedException;

	/**
	 * Remove roles from a group.
	 * 
	 * @param groupId
	 *            The group ID to remove roles from.
	 * @param codes
	 *            Role codes to be removed from a group.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission to remove roles from a
	 *             group.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate roles / groups.
	 */
	public void removeRolesFromGroup(Long groupId, List<String> codes) throws EOSForbiddenException,
			EOSUnauthorizedException;

	/**
	 * List groups of a role.
	 * 
	 * @param code
	 *            The role code.
	 * @param limit
	 *            Maximum number of registers.
	 * @param offset
	 *            Initial point.
	 * @return List of groups.
	 * @throws EOSForbiddenException
	 *             For {@link EOSLevel#INTERNAL} special permissions are
	 *             required.
	 */
	public List<EOSGroup> listRoleGroups(String code, int limit, int offset) throws EOSForbiddenException;

	/**
	 * List all roles from a group.
	 * 
	 * @param groupId
	 *            The group ID to list roles.
	 * @param limit
	 *            Maximum number of registers.
	 * @param offset
	 *            Initial point.
	 * @return List of roles.
	 * @throws EOSForbiddenException
	 *             For {@link EOSLevel#INTERNAL} special permissions are
	 *             required.
	 */
	public List<EOSRole> listGroupRoles(Long groupId, int limit, int offset) throws EOSForbiddenException;
}
