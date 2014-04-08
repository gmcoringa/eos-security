/**
 * 
 */
package com.eos.security.api.service;

import java.util.List;
import java.util.Map;

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
	 * Creates a new tenant.
	 * 
	 * @param tenant
	 * @param data
	 * @param adminUser
	 * @return The tenant created.
	 * @throws EOSDuplicatedEntryException
	 *             If an tenant already exists.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for tenant creation.
	 *             Usually only a super user can create a tenant.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can create other tenant.
	 * @throws EOSValidationException
	 *             If tenant contains invalid fields.
	 */
	public EOSTenant createTenant(EOSTenant tenant, Map<String, String> data,
			EOSUser adminUser) throws EOSDuplicatedEntryException,
			EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException;

	/**
	 * Finds a tenant by its id.
	 * 
	 * @param tenantId
	 * @return the tenant.
	 * @throws EOSValidationException
	 *             If no tenant exists with the given tenant ID.
	 */
	public EOSTenant findTenant(Long tenantId) throws EOSNotFoundException;

	/**
	 * Find all tenants that match the given id list.
	 * 
	 * @param tenantIds
	 *            List of tenant ids.
	 * @return List of tenant found.
	 */
	public List<EOSTenant> findTenants(List<Long> tenantIds);

	/**
	 * List tenants. Listing tenants not active requires higher permission than
	 * usual.
	 * 
	 * @param states
	 *            If null, only active tenants are returned.
	 * @param limit
	 *            Maximum number of registers.
	 * @param offset
	 *            Initial point.
	 * @return List of tenants.
	 */
	public List<EOSTenant> listTenants(List<EOSState> states, int limit,
			int offset);

	/**
	 * Updates a tenant. Only name and description are update. For state change
	 * see {@link EOSTenantService#updateTenantState(Long, EOSState)}.
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
	public void updateTenant(EOSTenant tenant) throws EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException,
			EOSNotFoundException;

	/**
	 * Change a tenant state.
	 * 
	 * @param tenantId
	 *            The tenant Id.
	 * @param state
	 *            The new state.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for tenant update.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can update other tenant.
	 * @throws EOSValidationException
	 *             If no tenant exists with the given tenant ID.
	 */
	public void updateTenantState(Long tenantId, EOSState state)
			throws EOSForbiddenException, EOSUnauthorizedException,
			EOSNotFoundException;

	/**
	 * Purge (physical delete) the tenant and all related data.
	 * 
	 * @param tenantId
	 *            The tenant id.
	 * @throws EOSForbiddenException
	 *             If the user do not have permission for tenant purge.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can purge tenants.
	 */
	public void purgeTenant(Long tenantId) throws EOSForbiddenException,
			EOSUnauthorizedException;

	// Tenant Data

	/**
	 * Adds tenant data to the given tenant. If the data value exists, then an
	 * update will be performed. If value is null, then the key, value pair will
	 * be removed.
	 * 
	 * @param tenantId
	 *            Id of the tenant.
	 * @param tenantData
	 *            Tenant data key value.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for tenant data
	 *             manipulation.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulation tenant data.
	 */
	public void updateTenantData(Long tenantId, Map<String, String> tenantData)
			throws EOSForbiddenException, EOSUnauthorizedException;

	/**
	 * Find a tenant data based on its key.
	 * 
	 * @param tenantId
	 *            Id of the tenant to list its data.
	 * @param key
	 *            Tenant data key to be found.
	 * @return Tenant data value.
	 * @throws EOSForbiddenException
	 *             If the authenticated user do not have permission for tenant
	 *             data listing.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can list tenant data.
	 */
	public String findTenantData(Long tenantId, String key)
			throws EOSForbiddenException, EOSUnauthorizedException;

	/**
	 * List tenant data based on key list.
	 * 
	 * @param tenantId
	 *            Id of the tenant to list its data.
	 * @param keys
	 *            List of keys.
	 * @return Map with key value pair of tenant data.
	 * @throws EOSForbiddenException
	 *             If the authenticated user do not have permission for tenant
	 *             data listing.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can list tenant data.
	 */
	public Map<String, String> listTenantData(Long tenantId, List<String> keys)
			throws EOSForbiddenException, EOSUnauthorizedException;

	/**
	 * List all tenant data.
	 * 
	 * @param tenantId
	 *            Id of the tenant to list its data.
	 * @param limit
	 *            Maximum number of registers.
	 * @param offset
	 *            Initial point.
	 * @return Map with key value pair of tenant data.
	 * @throws EOSForbiddenException
	 *             If the authenticated user do not have permission for tenant
	 *             data listing.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can list tenant data.
	 */
	public Map<String, String> listTenantData(Long tenantId, int limit,
			int offset) throws EOSForbiddenException, EOSUnauthorizedException;

}
