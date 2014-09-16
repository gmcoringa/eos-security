/**
 * 
 */
package com.eos.security.impl.service.tenant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.common.EOSState;
import com.eos.common.exception.EOSDuplicatedEntryException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSValidationException;
import com.eos.common.util.StringUtil;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.service.EOSPermissionService;
import com.eos.security.api.service.EOSRoleService;
import com.eos.security.api.service.EOSSecurityService;
import com.eos.security.api.service.EOSTenantService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.service.TransactionManager;
import com.eos.security.impl.service.internal.EOSSystemConstants;
import com.eos.security.impl.service.internal.EOSValidator;
import com.eos.security.impl.service.role.RoleCreator;
import com.eos.security.impl.service.role.RoleCreatorFactory;
import com.eos.security.impl.session.SessionContextManager;

/**
 * @author santos.fabiano
 * 
 */
@Service
public class EOSTenantServiceImpl implements EOSTenantService {

	@Autowired
	private EOSTenantDAO tenantDAO;
	@Autowired
	private EOSTenantDataDAO tenantDataDAO;
	@Autowired
	private EOSUserService svcUser;
	@Autowired
	private EOSSecurityService svcSecurity;
	@Autowired
	private EOSRoleService svcRole;
	@Autowired
	private EOSPermissionService svcPermission;
	@Autowired
	TransactionManager transactionManager;

	private static final Logger log = LoggerFactory.getLogger(EOSTenantServiceImpl.class);

	// #################
	// Tenant
	// #################

	/**
	 * @see com.eos.security.api.service.EOSTenantService#createTenant(EOSTenant, Map, EOSUser)
	 */
	@Override
	public EOSTenant createTenant(EOSTenant tenant, Map<String, String> data, final EOSUser adminUser)
			throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException, EOSValidationException {
		EOSValidator.validateTenant(tenant);
		// svcSecurity.checkPermissions(true, false, "Tenant.Create");
		transactionManager.begin();

		// Set default state only if state is null
		if (tenant.getState() == null) {
			tenant.setState(EOSState.DISABLED);
		}

		tenantDAO.create(tenant);
		// Create meta data
		addTenantData(tenant.getAlias(), data);
		transactionManager.commit();
		// // Create administrator user with new tenant
		// try {
		// log.debug("Creating administrator for tenant " + tenant.getName());
		// svcSecurity.impersonate(EOSSystemConstants.LOGIN_SYSTEM_USER, EOSSystemConstants.ADMIN_TENANT,
		// entity.getId());
		// createDefaultRoles(tenant.getName(), entity.getId());
		// createTenantUsers(adminUser);
		// } catch (EOSDuplicatedEntryException | EOSForbiddenException | EOSUnauthorizedException
		// | EOSValidationException e) {
		// log.debug("User and/or tenant create failed");
		// throw e;
		// } catch (EOSNotFoundException e) {
		// throw new EOSRuntimeException("Tenant create: Impersonate user and/or tenant not found", e);
		// } finally {
		// try {
		// svcSecurity.deImpersonate();
		// } catch (EOSInvalidStateException e) {
		// throw new EOSRuntimeException("Failed to de-impersonate", e);
		// }
		// }
		//
		// // TODO messaging and cache
		log.info("Tenant created: " + tenant.toString());
		return tenant;
	}

	private void createTenantUsers(EOSUser admin) throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException, EOSNotFoundException {
		// Create user
		svcUser.createUser(admin, null);
		// Grant administrator role to him
		svcRole.addRolesToUser(admin.getLogin(), Arrays.asList(EOSSystemConstants.ROLE_TENANT_ADMIN));
		// Create anonymous user and grant role anonymous
		svcUser.createUser(svcUser.findTenantUser(EOSSystemConstants.LOGIN_ANONYMOUS, EOSSystemConstants.ADMIN_TENANT),
				null);
		svcRole.addRolesToUser(EOSSystemConstants.LOGIN_ANONYMOUS, Arrays.asList(EOSSystemConstants.ROLE_ANONYMOUS));
	}

	private void createDefaultRoles(String tenantName, Long tenantId) throws EOSDuplicatedEntryException,
			EOSForbiddenException, EOSUnauthorizedException, EOSValidationException {
		// see defaultRolePermissions.json file
		List<RoleCreator> roles = RoleCreatorFactory.getRoles();
		for (RoleCreator creator : roles) {
			svcRole.createRole(creator.getRole());
			svcPermission.addRolePermissions(creator.getRole().getCode(), creator.getPermissions());
		}
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#findTenant(java.lang.String)
	 */
	@Override
	public EOSTenant findTenant(String alias) throws EOSNotFoundException {
		// TODO cache
		transactionManager.begin();
		EOSTenant tenant = tenantDAO.find(alias);
		transactionManager.commit();

		if (tenant == null) {
			throw new EOSNotFoundException("Tenant not found, alias: " + alias);
		}

		return tenant;
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#findTenants(java.util.Set)
	 */
	@Override
	public Set<EOSTenant> findTenants(Set<String> aliases) {
		// TODO cache
		transactionManager.begin();
		Set<EOSTenant> tenants = tenantDAO.findTenants(aliases);
		transactionManager.commit();
		return tenants;
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#listTenants(java.util.Set, int, int)
	 */
	@Override
	public Set<EOSTenant> listTenants(Set<EOSState> states, int limit, int offset) {
		// TODO permission check for state != ACTIVE
		transactionManager.begin();
		Set<EOSTenant> tenants = tenantDAO.listTenants(states, limit, offset);
		transactionManager.commit();
		return tenants;
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#updateTenant(com.eos.security.api.vo.EOSTenant)
	 */
	@Override
	public void updateTenant(EOSTenant tenant) throws EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException, EOSNotFoundException {
		EOSValidator.validateTenant(tenant);
		// checkTenantPermission(tenant.getId(), "Tenant.Update");

		transactionManager.begin();
		tenantDAO.update(tenant);
		transactionManager.commit();
		log.debug("Tenant updated: " + tenant.toString());
		// TODO messaging
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#updateTenantState(java.lang.String, com.eos.common.EOSState)
	 */
	@Override
	public void updateTenantState(String alias, EOSState state) throws EOSForbiddenException, EOSUnauthorizedException,
			EOSNotFoundException {
		// svcSecurity.checkPermissions(true, false, "Tenant.Update.State");
		transactionManager.begin();
		tenantDAO.update(alias, state);
		transactionManager.commit();
		log.debug("Tenant state updated to " + state.name());
		// TODO messaging
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#purgeTenant(java.lang.String)
	 */
	@Override
	public void purgeTenant(String alias) throws EOSForbiddenException, EOSUnauthorizedException {
		// svcSecurity.checkPermissions(true, false, "Tenant.Delete");
		transactionManager.begin();
		tenantDAO.purgeTenant(alias);
		transactionManager.commit();
		log.info("Tenant purged :" + alias);
	}

	// #################
	// Tenant Data
	// #################

	/**
	 * @see com.eos.security.api.service.EOSTenantService#updateTenantData(java.lang.String, java.util.Map)
	 */
	@Override
	public void updateTenantData(String tenantAlias, Map<String, String> tenantData) throws EOSForbiddenException,
			EOSUnauthorizedException {
		// checkTenantPermission(tenantId, "Tenant.Update.Data");
		Set<String> keys = tenantData.keySet();
		// Look for data that already exists
		Map<String, String> dataFound = listTenantData(tenantAlias, keys);
		Set<String> remove = new HashSet<>();

		transactionManager.begin();
		// Updates
		log.debug("Starting Tenant data update ");
		for (Entry<String, String> entry : dataFound.entrySet()) {
			// Add removes to removal list
			if (StringUtil.isBlankOrNull(tenantData.get(entry.getKey()))) {
				remove.add(entry.getKey());
				log.debug("Tenant data set for removal: " + entry.getKey());
			} else {
				// Update
				tenantDataDAO.updateTenantData(tenantAlias, entry.getKey(), tenantData.get(entry.getKey()));
				log.debug("Tenant data [" + entry.getKey() + "] updated");
			}
			// Remove key pair value from tenantData map
			tenantData.remove(entry.getKey());
		}

		// Add new data
		addTenantData(tenantAlias, tenantData);
		// Remove removal list
		if (!remove.isEmpty()) {
			log.debug("Starting Tenant data removal ");
			tenantDataDAO.deleteTenantData(tenantAlias, remove);
		}

		transactionManager.commit();
		// TODO Remove tenant data cache using keys variable
	}

	/**
	 * Add new tenant data.
	 * 
	 * @param tenantAlias
	 *            The tenant alias.
	 * @param tenantData
	 *            Data map to be added.
	 */
	private void addTenantData(String tenantAlias, Map<String, String> tenantData) {
		// No tenant data, do nothing
		if (tenantData == null || tenantData.isEmpty()) {
			return;
		}

		for (Entry<String, String> data : tenantData.entrySet()) {
			// Skip empty keys or values
			if (StringUtil.isBlankOrNull(data.getKey()) || StringUtil.isBlankOrNull(data.getValue())) {
				continue;
			}

			tenantDataDAO.createTenantData(tenantAlias, data.getKey(), data.getValue());
		}
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#findTenantData(java.lang.String, java.lang.String)
	 */
	@Override
	public String findTenantData(String tenantAlias, String key) throws EOSForbiddenException, EOSUnauthorizedException {
		// checkTenantPermission(tenantId, "Tenant.View.Data");
		transactionManager.begin();
		String value = tenantDataDAO.findTenantDataValue(tenantAlias, key);
		transactionManager.commit();
		return value;
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#listTenantData(java.lang.String, java.util.Set)
	 */
	@Override
	public Map<String, String> listTenantData(String tenantAlias, Set<String> keys) throws EOSForbiddenException,
			EOSUnauthorizedException {
		// checkTenantPermission(tenantId, "Tenant.View.Data");
		transactionManager.begin();
		Map<String, String> metas = tenantDataDAO.findTenantDataValues(tenantAlias, keys);
		transactionManager.commit();
		return metas;
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#listTenantData(java.lang.String, int, int)
	 */
	@Override
	public Map<String, String> listTenantData(String tenantAlias, int limit, int offset) throws EOSForbiddenException,
			EOSUnauthorizedException {
		// checkTenantPermission(tenantId, "Tenant.View.Data");
		transactionManager.begin();
		Map<String, String> metas = tenantDataDAO.listTenantData(tenantAlias, limit, offset);
		transactionManager.commit();
		return metas;
	}

	// #################
	// Utilities
	// #################

	/**
	 * Validate tenant access, current tenant equals tenantId and permissions.
	 * 
	 * @param tenantAlias
	 *            The tenant to be validated
	 * @param permissions
	 *            Permissions to be validated.
	 * @throws EOSUnauthorizedException
	 *             If tenantId different from current tenant.
	 * @throws EOSForbiddenException
	 *             If the user has no permissions.
	 */
	private void checkTenantPermission(Long tenantAlias, String... permissions) throws EOSUnauthorizedException,
			EOSForbiddenException {
		if (!tenantAlias.equals(SessionContextManager.getCurrentTenantAlias())) {
			throw new EOSUnauthorizedException("Cross tenancy update not allowed");
		}
		svcSecurity.checkPermissions(permissions);
	}

}
