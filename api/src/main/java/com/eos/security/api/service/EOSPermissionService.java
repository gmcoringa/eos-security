/**
 * 
 */
package com.eos.security.api.service;

import java.util.Map;
import java.util.Set;

import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;

/**
 * Permission service, deals with role permission relation and user / group
 * permissioning.
 * 
 * @author santos.fabiano
 * 
 */
public interface EOSPermissionService {

	// Role Permission

	/**
	 * Add permissions to a role. If the role already has any permission no
	 * further action will performed.
	 * 
	 * @param code
	 *            The role code.
	 * @param permissions
	 *            List of permissions to be added.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for role manipulation.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate roles.
	 * @throws EOSValidationException
	 *             If the permission is invalid. Only number, characters, dot
	 *             and hyphen are allowed.
	 */
	public void addRolePermissions(String code, Set<String> permissions) throws EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException;

	/**
	 * Remove permissions from a role.
	 * 
	 * @param code
	 *            The role code.
	 * @param permissions
	 *            List of permissions to be removed.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for role manipulation.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate roles.
	 */
	public void removeRolePermission(String code, Set<String> permissions) throws EOSForbiddenException,
			EOSUnauthorizedException;

	/**
	 * List all permissions of a role.
	 * 
	 * @param code
	 *            The role code.
	 * @param limit
	 *            Maximum number of registers.
	 * @param offset
	 *            Initial point.
	 * @return List of permissions.
	 */
	public Set<String> listRolePermissions(String code, int limit, int offset);

	/**
	 * Validates the given user has a permission. Validates access through role
	 * group and user group relations.
	 * 
	 * @param login
	 *            User login.
	 * @param permission
	 *            Permission to be verified.
	 * @return True if the user has the permission, false otherwise.
	 */
	public boolean hasPermission(String login, String permission);

	/**
	 * Validates the given user has the permissions. Validates access through
	 * role group and user group relations.
	 * 
	 * @param login
	 *            User login.
	 * @param permissions
	 *            Permission list to be verified.
	 * @return Map with the permission string as key and the value indicates if
	 *         the user has or not.
	 */
	public Map<String, Boolean> hasPermissions(String login, Set<String> permissions);

}
