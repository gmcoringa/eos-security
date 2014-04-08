/**
 * 
 */
package com.eos.security.impl.service.internal;

/**
 * All known permissions for Security Module. Changes here should also update
 * the default role permissions (see resource defaultRolePermissions.json).
 * 
 * @author santos.fabiano
 * 
 */
public interface EOSKnownPermissions {

	/**
	 * Global access permission for a tenant.
	 */
	String PERMISSION_TENAT_ALL = "EOS.Tenant.Permission.ALL";
	String PASSWORD_UPDATE = "User.Upate.Password";
}
