/**
 * 
 */
package com.eos.security.impl.service.internal;

/**
 * System constants.
 * 
 * @author santos.fabiano
 * 
 */
public interface EOSSystemConstants {

	/**
	 * Global tenant alias.
	 */
	String ADMIN_TENANT = "eosTenant";
	/**
	 * Super Administrator user login.
	 */
	String LOGIN_SUPER_ADMIN = "eosadmin";
	/**
	 * Anonymous user login.
	 */
	String LOGIN_ANONYMOUS = "anonymous";
	/**
	 * Security task user login.
	 */
	String LOGIN_SYSTEM_USER = "security.user";
	/**
	 * Super Administrator role code.
	 */
	String ROLE_SUPER_ADMIN = "EOSSuperAdmin";
	/**
	 * Anonymous role code.
	 */
	String ROLE_ANONYMOUS = "EOSAnonymous";
	/**
	 * Tenant administrator role code.
	 */
	String ROLE_TENANT_ADMIN = "EOSAdmin";
	/**
	 * Tenant user role code.
	 */
	String ROLE_TENANT_USER = "EOSUser";
	/**
	 * Administrative internal level.
	 */
	Integer INTERNAL_LEVEL = 1;
}
