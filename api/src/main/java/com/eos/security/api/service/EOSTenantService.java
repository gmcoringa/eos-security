/**
 * 
 */
package com.eos.security.api.service;

import java.util.Map;
import java.util.Set;

import com.eos.common.EOSState;
import com.eos.common.exception.EOSDuplicatedEntryException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;

/**
 * Tenant service utility.
 * 
 * @author fabiano.santos
 * 
 */
public interface EOSTenantService {

	// Tenant

	/**
	 * Creates a new tenant. Alias of the tenant [unique]. Only characters, number, "_", "-" and "." are allowed.
	 * 
	 * @param tenant
	 *            Tenant information.
	 * @param data
	 *            Extra meta-data information.
	 * @param adminUser
	 *            Tenant administrator user.
	 * @return The tenant created.
	 * @throws EOSDuplicatedEntryException
	 *             If an tenant already exists.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for tenant creation. Usually only a super user can create a
	 *             tenant.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can create other tenant.
	 * @throws EOSValidationException
	 *             If tenant contains invalid fields.
	 */
	public EOSTenant createTenant(EOSTenant tenant, Map<String, String> data, EOSUser adminUser)
			throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException, EOSValidationException;

	/**
	 * Finds a tenant by its alias.
	 * 
	 * @param alias
	 * @return the tenant.
	 * @throws EOSValidationException
	 *             If no tenant exists with the given tenant alias.
	 */
	public EOSTenant findTenant(String alias) throws EOSNotFoundException;

	/**
	 * Find all tenants that match the given alias list.
	 * 
	 * @param alias
	 *            List of tenant aliases.
	 * @return List of tenant found.
	 */
	public Set<EOSTenant> findTenants(Set<String> aliases);

	/**
	 * List tenants. Listing tenants not active requires higher permission than usual.
	 * 
	 * @param states
	 *            If null, only active tenants are returned.
	 * @param limit
	 *            Maximum number of registers.
	 * @param offset
	 *            Initial point.
	 * @return List of tenants.
	 */
	public Set<EOSTenant> listTenants(Set<EOSState> states, int limit, int offset);

	/**
	 * Updates a tenant. Only name and description are update. For state change see
	 * {@link EOSTenantService#updateTenantState(Long, EOSState)}.
	 * 
	 * @param tenant
	 *            The tenant info.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for tenant update.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can update other tenant.
	 * @throws EOSValidationException
	 *             If tenant contains invalid fields.
	 * @throws EOSValidationException
	 *             If no tenant exists with the given tenant ID.
	 */
	public void updateTenant(EOSTenant tenant) throws EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException, EOSNotFoundException;

	/**
	 * Change a tenant state.
	 * 
	 * @param alias
	 *            The tenant alias.
	 * @param state
	 *            The new state.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for tenant update.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can update other tenant.
	 * @throws EOSValidationException
	 *             If no tenant exists with the given tenant alias.
	 */
	public void updateTenantState(String alias, EOSState state) throws EOSForbiddenException, EOSUnauthorizedException,
			EOSNotFoundException;

	/**
	 * Purge (physical delete) the tenant and all related data.
	 * 
	 * @param alias
	 *            The tenant alias.
	 * @throws EOSForbiddenException
	 *             If the user do not have permission for tenant purge.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can purge tenants.
	 */
	public void purgeTenant(String alias) throws EOSForbiddenException, EOSUnauthorizedException;

	// Tenant Data

	/**
	 * Adds tenant data to the given tenant. If the data value exists, then an update will be performed. If value is
	 * null, then the key, value pair will be removed.
	 * 
	 * @param alias
	 *            Alias of the tenant.
	 * @param tenantData
	 *            Tenant data key value.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for tenant data manipulation.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulation tenant data.
	 */
	public void updateTenantData(String alias, Map<String, String> tenantData) throws EOSForbiddenException,
			EOSUnauthorizedException;

	/**
	 * Find a tenant data based on its key.
	 * 
	 * @param alias
	 *            Alias of the tenant to list its data.
	 * @param key
	 *            Tenant data key to be found.
	 * @return Tenant data value.
	 * @throws EOSForbiddenException
	 *             If the authenticated user do not have permission for tenant data listing.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can list tenant data.
	 */
	public String findTenantData(String alias, String key) throws EOSForbiddenException, EOSUnauthorizedException;

	/**
	 * List tenant data based on key list.
	 * 
	 * @param alias
	 *            alias of the tenant to list its data.
	 * @param keys
	 *            List of keys.
	 * @return Map with key value pair of tenant data.
	 * @throws EOSForbiddenException
	 *             If the authenticated user do not have permission for tenant data listing.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can list tenant data.
	 */
	public Map<String, String> listTenantData(String alias, Set<String> keys) throws EOSForbiddenException,
			EOSUnauthorizedException;

	/**
	 * List all tenant data.
	 * 
	 * @param alias
	 *            Alias of the tenant to list its data.
	 * @param limit
	 *            Maximum number of registers.
	 * @param offset
	 *            Initial point.
	 * @return Map with key value pair of tenant data.
	 * @throws EOSForbiddenException
	 *             If the authenticated user do not have permission for tenant data listing.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can list tenant data.
	 */
	public Map<String, String> listTenantData(String alias, int limit, int offset) throws EOSForbiddenException,
			EOSUnauthorizedException;

}
