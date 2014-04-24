/**
 * 
 */
package com.eos.security.impl.service;

import java.util.Arrays;
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
import com.eos.security.api.service.TransactionManager;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.dao.EOSTenantDAO;
import com.eos.security.impl.dao.EOSTenantDataDAO;
import com.eos.security.impl.service.internal.EOSSystemConstants;
import com.eos.security.impl.service.internal.EOSValidator;
import com.eos.security.impl.service.internal.RoleCreator;
import com.eos.security.impl.service.internal.RoleCreatorFactory;
import com.eos.security.impl.service.internal.TransactionManagerImpl;
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
		TransactionManager manager = TransactionManagerImpl.get().begin();

		// Set default state only if state is null
		if (tenant.getState() == null) {
			tenant.setState(EOSState.DISABLED);
		}

		tenant = tenantDAO.create(tenant);
		manager.commit();
		//
		// tenantDAO.persist(entity);
		// // Create meta data
		// addTenantData(entity.getId(), data);
		// tenantDAO.getEntityManager().flush();
		// tenant.setId(entity.getId());
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
		// log.info("Tenant created: " + tenant.toString());
		// return tenant;
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
		TransactionManager manager = TransactionManagerImpl.get().begin();
		EOSTenant tenant = tenantDAO.find(alias);
		manager.commit();

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
		TransactionManager manager = TransactionManagerImpl.get().begin();
		Set<EOSTenant> tenants = tenantDAO.findTenants(aliases);
		manager.commit();
		return tenants;
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#listTenants(java.util.Set, int, int)
	 */
	@Override
	public Set<EOSTenant> listTenants(Set<EOSState> states, int limit, int offset) {
		// TODO permission check for state != ACTIVE
		TransactionManager manager = TransactionManagerImpl.get().begin();
		Set<EOSTenant> tenants = tenantDAO.listTenants(states, limit, offset);
		manager.commit();
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

		TransactionManager manager = TransactionManagerImpl.get().begin();
		tenantDAO.update(tenant);
		manager.commit();
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
		TransactionManager manager = TransactionManagerImpl.get().begin();
		tenantDAO.update(alias, state);
		manager.commit();
		log.debug("Tenant state updated to " + state.name());
		// TODO messaging
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#purgeTenant(java.lang.String)
	 */
	@Override
	public void purgeTenant(String alias) throws EOSForbiddenException, EOSUnauthorizedException {
		// svcSecurity.checkPermissions(true, false, "Tenant.Delete");
		TransactionManager manager = TransactionManagerImpl.get().begin();
		tenantDAO.purgeTenant(alias);
		manager.commit();
		log.info("Tenant purged :" + alias);
	}

	// #################
	// Tenant Data
	// #################

	/**
	 * @see com.eos.security.api.service.EOSTenantService#updateTenantData(java.lang.String, java.util.Map)
	 */
	@Override
	public void updateTenantData(String tenantId, Map<String, String> tenantData) throws EOSForbiddenException,
			EOSUnauthorizedException {
		// checkTenantPermission(tenantId, "Tenant.Update.Data");
		// List<String> keys = new ArrayList<>(tenantData.size());
		// keys.addAll(tenantData.keySet());
		// // Look for data that already exists
		// Map<String, String> dataFound = listTenantData(tenantId, keys);
		// List<String> remove = new ArrayList<>();
		//
		// // Updates
		// log.debug("Starting Tenant data update ");
		// for (Entry<String, String> entry : dataFound.entrySet()) {
		// // Add removes to removal list
		// if (StringUtil.isEmpty(tenantData.get(entry.getKey()))) {
		// remove.add(entry.getKey());
		// log.debug("Tenant data set for removal: " + entry.getKey());
		// } else {
		// // Update
		// tenantDataDAO.updateTenantData(tenantId, entry.getKey(), entry.getValue());
		// log.debug("Tenant data [" + entry.getKey() + "] updated");
		// }
		// // Remove key pair value from tenantData map
		// tenantData.remove(entry.getKey());
		// }
		//
		// // Add new data
		// addTenantData(tenantId, tenantData);
		// // Remove removal list
		// if (!remove.isEmpty()) {
		// log.debug("Starting Tenant data removal ");
		// tenantDataDAO.deleteTenantData(tenantId, remove);
		// }

		// TODO Remove tenant data cache using keys variable
	}

	/**
	 * Add new tenant data.
	 * 
	 * @param tenantId
	 *            The tenant id.
	 * @param tenantData
	 *            Data map to be added.
	 */
	private void addTenantData(String tenantId, Map<String, String> tenantData) {
		// No tenant data, do nothing
		if (tenantData == null || tenantData.isEmpty()) {
			return;
		}

		for (Entry<String, String> data : tenantData.entrySet()) {
			// Skip empty keys or values
			if (StringUtil.isEmpty(data.getKey()) || StringUtil.isEmpty(data.getValue())) {
				continue;
			}
			// EOSTenantDataEntity entity = new EOSTenantDataEntity();
			// entity.setTenantId(tenantId);
			// entity.setKey(data.getKey());
			// entity.setValue(data.getValue());
			// tenantDataDAO.persist(entity);
		}
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#findTenantData(java.lang.String, java.lang.String)
	 */
	@Override
	public String findTenantData(String tenantId, String key) throws EOSForbiddenException, EOSUnauthorizedException {
		// checkTenantPermission(tenantId, "Tenant.View.Data");
		// return tenantDataDAO.findTenantDataValue(tenantId, key);
		return null;
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#listTenantData(java.lang.String, java.util.Set)
	 */
	@Override
	public Map<String, String> listTenantData(String tenantId, Set<String> keys) throws EOSForbiddenException,
			EOSUnauthorizedException {
		// checkTenantPermission(tenantId, "Tenant.View.Data");
		// List<EOSTenantDataEntity> datas = tenantDataDAO.findTenantDataValues(tenantId, keys);
		// return dataEntityToMap(datas);
		return null;
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#listTenantData(java.lang.String, int, int)
	 */
	@Override
	public Map<String, String> listTenantData(String tenantId, int limit, int offset) throws EOSForbiddenException,
			EOSUnauthorizedException {
		// checkTenantPermission(tenantId, "Tenant.View.Data");
		// List<EOSTenantDataEntity> datas = tenantDataDAO.listTenantData(tenantId, limit, offset);
		// return dataEntityToMap(datas);
		return null;
	}

	// #################
	// Utilities
	// #################

	/**
	 * Validate tenant access, current tenant equals tenantId and permissions.
	 * 
	 * @param tenantId
	 *            The tenant to be validated
	 * @param permissions
	 *            Permissions to be validated.
	 * @throws EOSUnauthorizedException
	 *             If tenantId different from current tenant.
	 * @throws EOSForbiddenException
	 *             If the user has no permissions.
	 */
	private void checkTenantPermission(Long tenantId, String... permissions) throws EOSUnauthorizedException,
			EOSForbiddenException {
		if (!tenantId.equals(SessionContextManager.getCurrentTenantId())) {
			throw new EOSUnauthorizedException("Cross tenancy update not allowed");
		}
		svcSecurity.checkPermissions(permissions);
	}

}
