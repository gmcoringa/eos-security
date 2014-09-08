/**
 * 
 */
package com.eos.security.impl.service.permission;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.common.exception.EOSValidationException;
import com.eos.common.util.CollectionUtil;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.service.EOSGroupService;
import com.eos.security.api.service.EOSPermissionService;
import com.eos.security.api.service.EOSRoleService;
import com.eos.security.impl.service.internal.EOSValidator;
import com.eos.security.impl.service.role.EOSRoleGroupDAO;

/**
 * Permission Service default implementation.
 * 
 * @author santos.fabiano
 * 
 */
@Service
public class EOSPermissionServiceImpl implements EOSPermissionService {

	private static final Logger log = LoggerFactory.getLogger(EOSPermissionServiceImpl.class);

	private EOSPermissionDAO permissionDAO;
	private EOSRoleService svcRole;
	private EOSGroupService svcGroup;
	private EOSRoleGroupDAO roleGroupDAO;

	@Autowired
	public void setPermissionDAO(EOSPermissionDAO permissionDAO) {
		this.permissionDAO = permissionDAO;
	}

	@Autowired
	public void setRoleService(EOSRoleService svcRole) {
		this.svcRole = svcRole;
	}

	@Autowired
	public void setGroupService(EOSGroupService svcGroup) {
		this.svcGroup = svcGroup;
	}

	@Autowired
	public void setRoleGroupDAO(EOSRoleGroupDAO roleGroupDAO) {
		this.roleGroupDAO = roleGroupDAO;
	}

	/**
	 * @see com.eos.security.api.service.EOSPermissionService#addRolePermissions(java.lang.String,
	 *      java.util.Set)
	 */
	@Override
	public void addRolePermissions(String code, Set<String> permissions) throws EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		// TODO security, cache and messaging
		EOSValidator.validatePermissions(permissions);
//		for (String permission : permissions) {
//			createRolePermission(code, permission);
//		}
	}

	private void createRolePermission(String code, String permission) {
//		EOSPermissionEntity entity = new EOSPermissionEntity().setRoleCode(code).setPermission(permission);
//		permissionDAO.persist(entity);

		if (log.isDebugEnabled()) {
			log.debug("Adding permission: " + permission + " to role: " + code);
		}
	}

	/**
	 * @see com.eos.security.api.service.EOSPermissionService#removeRolePermission(java.lang.String,
	 *      java.util.Set)
	 */
	@Override
	public void removeRolePermission(String code, Set<String> permissions) throws EOSForbiddenException,
			EOSUnauthorizedException {
		// TODO validation, security, cache and messaging
//		permissionDAO.deleteRolePermissions(SessionContextManager.getCurrentTenantId(), code, permissions);
	}

	/**
	 * @see com.eos.security.api.service.EOSPermissionService#listRolePermissions(java.lang.String,
	 *      int, int)
	 */
	@Override
	public Set<String> listRolePermissions(String code, int limit, int offset) {
		// TODO validation, security, cache
//		Set<String> permissions = new HashSet<>(permissionDAO.listRolePermissions(
//				SessionContextManager.getCurrentTenantId(), code));
//		// TODO limit and offset as sublist
//		return permissions;
		return null;
	}

	/**
	 * @see com.eos.security.api.service.EOSPermissionService#hasPermission(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean hasPermission(String login, String permission) {
		Map<String, Boolean> permMap = hasPermissions(login, CollectionUtil.asSet(permission));
		return permMap.get(permission);
	}

	/**
	 * @see com.eos.security.api.service.EOSPermissionService#hasPermissions(java.lang.String,
	 *      java.util.Set)
	 */
	@Override
	public Map<String, Boolean> hasPermissions(String login, Set<String> permissions) {
		// TODO validation, security, cache
		Map<String, Boolean> ret = new HashMap<>(permissions.size());
		// List all roles that have the required permissions
//		List<EOSPermissionEntity> entities = permissionDAO.listEntities(SessionContextManager.getCurrentTenantId(),
//				permissions);
//		// first fin direct user role permissions
//		if (entities != null && !entities.isEmpty()) {
//			directUserRolePermission(ret, login, permissions, entities);
//			// Now look for group role association with remaining permissions
//			directUserGroupPermission(ret, login, permissions, entities);
//		}
//		// Done it, set not found ones
//		for (String permission : permissions) {
//			ret.put(permission, Boolean.FALSE);
//		}

		return ret;
	}

//	private void directUserRolePermission(Map<String, Boolean> userPermission, String login, Set<String> permissions,
//			List<EOSPermissionEntity> rolePermissions) {
//
//		List<EOSPermissionEntity> found = new ArrayList<>(rolePermissions.size());
//		List<String> permFound = new ArrayList<>(permissions.size());
//		List<String> userRoles = svcRole.listUserRoleCodes(login);
//		// No roles for this user, return
//		if (userRoles == null || userRoles.isEmpty()) {
//			return;
//		}
//
//		for (EOSPermissionEntity entity : rolePermissions) {
//			if (userRoles.contains(entity.getRoleCode())) {
//				userPermission.put(entity.getPermission(), Boolean.TRUE);
//				// Remove already found permissions
//				permissions.remove(entity.getPermission());
//				// List of found permissions
//				permFound.add(entity.getPermission());
//			}
//		}
//		// Remove all role permissions entities found
//		for (EOSPermissionEntity entity : rolePermissions) {
//			if (!permFound.contains(entity.getPermission())) {
//				found.add(entity);
//			}
//		}
//		rolePermissions = found;
//	}
//
//	private void directUserGroupPermission(Map<String, Boolean> userPermission, String login, Set<String> permissions,
//			List<EOSPermissionEntity> rolePermissions) {
//		List<Long> groups = svcGroup.listUserGroupIds(login);
//		// No user groups, return
//		if (groups == null || groups.isEmpty()) {
//			return;
//		}
//		// Build role list for the following query
//		List<String> roles = new ArrayList<>(rolePermissions.size());
//		for (EOSPermissionEntity entity : rolePermissions) {
//			roles.add(entity.getRoleCode());
//		}
//		// Get all user groups that have any of the roles
//		List<EOSRoleGroupEntity> roleGroups = roleGroupDAO.listByRoleAndGroup(
//				SessionContextManager.getCurrentTenantId(), roles, groups);
//		// No groups with any of the given roles, return
//		if (roleGroups == null || roleGroups.isEmpty()) {
//			return;
//		}
//
//		for (EOSPermissionEntity entity : rolePermissions) {
//			for (EOSRoleGroupEntity roleGroup : roleGroups) {
//				if (roleGroup.getRoleCode().equals(entity.getRoleCode())) {
//					userPermission.put(entity.getPermission(), Boolean.TRUE);
//					// Remove already found permissions
//					permissions.remove(entity.getPermission());
//					break;
//				}
//			}
//		}
//	}
}
