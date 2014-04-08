/**
 * 
 */
package com.eos.security.impl.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eos.common.EOSState;
import com.eos.common.exception.EOSDuplicatedEntryException;
import com.eos.common.exception.EOSInvalidStateException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSRuntimeException;
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
import com.eos.security.impl.dao.EOSTenantDAO;
import com.eos.security.impl.dao.EOSTenantDataDAO;
import com.eos.security.impl.model.EOSTenantDataEntity;
import com.eos.security.impl.model.EOSTenantEntity;
import com.eos.security.impl.service.internal.EOSSystemConstants;
import com.eos.security.impl.service.internal.EOSValidator;
import com.eos.security.impl.service.internal.RoleCreator;
import com.eos.security.impl.service.internal.RoleCreatorFactory;
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
	 * @see com.eos.security.api.service.EOSTenantService#createTenant(EOSTenant,
	 *      Map, EOSUser)
	 */
	@Override
	@Transactional
	public EOSTenant createTenant(EOSTenant tenant, Map<String, String> data, final EOSUser adminUser)
			throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException, EOSValidationException {
		EOSValidator.validateTenant(tenant);
		svcSecurity.checkPermissions(true, false, "Tenant.Create");
		EOSTenantEntity entity = new EOSTenantEntity();

		entity.setName(tenant.getName());
		entity.setDescription(tenant.getDescription());
		// Only set state if not null
		if (tenant.getState() != null) {
			entity.setState(tenant.getState());
		}

		tenantDAO.persist(entity);
		// Create meta data
		addTenantData(entity.getId(), data);
		tenantDAO.getEntityManager().flush();
		tenant.setId(entity.getId());
		// Create administrator user with new tenant
		try {
			log.debug("Creating administrator for tenant " + tenant.getName());
			svcSecurity.impersonate(EOSSystemConstants.LOGIN_SYSTEM_USER, EOSSystemConstants.ADMIN_TENANT,
					entity.getId());
			createDefaultRoles(tenant.getName(), entity.getId());
			createTenantUsers(adminUser);
		} catch (EOSDuplicatedEntryException | EOSForbiddenException | EOSUnauthorizedException
				| EOSValidationException e) {
			log.debug("User and/or tenant create failed");
			throw e;
		} catch (EOSNotFoundException e) {
			throw new EOSRuntimeException("Tenant create: Impersonate user and/or tenant not found", e);
		} finally {
			try {
				svcSecurity.deImpersonate();
			} catch (EOSInvalidStateException e) {
				throw new EOSRuntimeException("Failed to de-impersonate", e);
			}
		}

		// TODO messaging and cache
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
	 * @see com.eos.security.api.service.EOSTenantService#findTenant(java.lang.Long)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public EOSTenant findTenant(Long tenantId) throws EOSNotFoundException {
		// TODO cache
		return entityToVO(tenantDAO.find(tenantId));
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#findTenants(java.util.List)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<EOSTenant> findTenants(List<Long> tenantIds) {
		// TODO cache
		List<EOSTenantEntity> entities = tenantDAO.findTenants(tenantIds);
		List<EOSTenant> tenants = new ArrayList<>(entities.size());

		for (EOSTenantEntity entity : entities) {
			tenants.add(entityToVO(entity));
		}

		return tenants;
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#listTenants(java.util.List,
	 *      int, int)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<EOSTenant> listTenants(List<EOSState> states, int limit, int offset) {
		List<EOSTenantEntity> entities = tenantDAO.listTenants(states, limit, offset);
		List<EOSTenant> tenants = new ArrayList<>(entities.size());

		for (EOSTenantEntity entity : entities) {
			tenants.add(entityToVO(entity));
		}

		return tenants;
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#updateTenant(com.eos.security.api.vo.EOSTenant)
	 */
	@Override
	@Transactional
	public void updateTenant(EOSTenant tenant) throws EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException, EOSNotFoundException {
		EOSValidator.validateTenant(tenant);
		checkTenantPermission(tenant.getId(), "Tenant.Update");
		// DO a find, then update, so hibernate listeners are fired.
		EOSTenantEntity entity = tenantDAO.find(tenant.getId());

		entity.setName(tenant.getName());
		entity.setDescription(tenant.getDescription());
		tenantDAO.merge(entity);
		log.debug("Tenant updated: " + tenant.toString());
		// TODO messaging
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#updateTenantState(java.lang.Long,
	 *      com.eos.common.EOSState)
	 */
	@Override
	@Transactional
	public void updateTenantState(Long tenantId, EOSState state) throws EOSForbiddenException,
			EOSUnauthorizedException, EOSNotFoundException {
		// DO a find, then update, so hibernate listeners are fired.
		EOSTenantEntity entity = tenantDAO.find(tenantId);
		svcSecurity.checkPermissions(true, false, "Tenant.Update.State");

		entity.setState(state);
		tenantDAO.merge(entity);
		log.debug("Tenant state updated to " + state.name());
		// TODO messaging
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#purgeTenant(java.lang.Long)
	 */
	@Override
	@Transactional
	public void purgeTenant(Long tenantId) throws EOSForbiddenException, EOSUnauthorizedException {
		svcSecurity.checkPermissions(true, false, "Tenant.Delete");
		// TODO Auto-generated method stub

	}

	// #################
	// Tenant Data
	// #################

	/**
	 * @see com.eos.security.api.service.EOSTenantService#updateTenantData(java.lang.Long,
	 *      java.util.Map)
	 */
	@Override
	@Transactional
	public void updateTenantData(Long tenantId, Map<String, String> tenantData) throws EOSForbiddenException,
			EOSUnauthorizedException {
		checkTenantPermission(tenantId, "Tenant.Update.Data");
		List<String> keys = new ArrayList<>(tenantData.size());
		keys.addAll(tenantData.keySet());
		// Look for data that already exists
		Map<String, String> dataFound = listTenantData(tenantId, keys);
		List<String> remove = new ArrayList<>();

		// Updates
		log.debug("Starting Tenant data update ");
		for (Entry<String, String> entry : dataFound.entrySet()) {
			// Add removes to removal list
			if (StringUtil.isEmpty(tenantData.get(entry.getKey()))) {
				remove.add(entry.getKey());
				log.debug("Tenant data set for removal: " + entry.getKey());
			} else {
				// Update
				tenantDataDAO.updateTenantData(tenantId, entry.getKey(), entry.getValue());
				log.debug("Tenant data [" + entry.getKey() + "] updated");
			}
			// Remove key pair value from tenantData map
			tenantData.remove(entry.getKey());
		}

		// Add new data
		addTenantData(tenantId, tenantData);
		// Remove removal list
		if (!remove.isEmpty()) {
			log.debug("Starting Tenant data removal ");
			tenantDataDAO.deleteTenantData(tenantId, remove);
		}

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
	private void addTenantData(Long tenantId, Map<String, String> tenantData) {
		// No tenant data, do nothing
		if (tenantData == null || tenantData.isEmpty()) {
			return;
		}

		for (Entry<String, String> data : tenantData.entrySet()) {
			// Skip empty keys or values
			if (StringUtil.isEmpty(data.getKey()) || StringUtil.isEmpty(data.getValue())) {
				continue;
			}
			EOSTenantDataEntity entity = new EOSTenantDataEntity();
			entity.setTenantId(tenantId);
			entity.setKey(data.getKey());
			entity.setValue(data.getValue());
			tenantDataDAO.persist(entity);
		}
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#findTenantData(java.lang.Long,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String findTenantData(Long tenantId, String key) throws EOSForbiddenException, EOSUnauthorizedException {
		checkTenantPermission(tenantId, "Tenant.View.Data");
		return tenantDataDAO.findTenantDataValue(tenantId, key);
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#listTenantData(java.lang.Long,
	 *      java.util.List)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Map<String, String> listTenantData(Long tenantId, List<String> keys) throws EOSForbiddenException,
			EOSUnauthorizedException {
		checkTenantPermission(tenantId, "Tenant.View.Data");
		List<EOSTenantDataEntity> datas = tenantDataDAO.findTenantDataValues(tenantId, keys);
		return dataEntityToMap(datas);
	}

	/**
	 * @see com.eos.security.api.service.EOSTenantService#listTenantData(java.lang.Long,
	 *      int, int)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Map<String, String> listTenantData(Long tenantId, int limit, int offset) throws EOSForbiddenException,
			EOSUnauthorizedException {
		checkTenantPermission(tenantId, "Tenant.View.Data");
		List<EOSTenantDataEntity> datas = tenantDataDAO.listTenantData(tenantId, limit, offset);
		return dataEntityToMap(datas);
	}

	// #################
	// Utilities
	// #################

	/**
	 * Convert a tenant entity to {@link EOSTenant}.
	 * 
	 * @param entity
	 *            Entity to be converted.
	 * @return {@link EOSTenant} representing the entity.
	 */
	private EOSTenant entityToVO(EOSTenantEntity entity) {
		if (entity == null) {
			return null;
		}

		EOSTenant tenant = new EOSTenant();
		tenant.setId(entity.getId());
		tenant.setName(entity.getName());
		tenant.setDescription(entity.getDescription());
		tenant.setState(entity.getState());

		return tenant;
	}

	/**
	 * Converts {@link EOSTenantDataEntity} list to a map.
	 * 
	 * @param datas
	 *            Data entity list.
	 * @return Map of tenant data.
	 */
	private Map<String, String> dataEntityToMap(List<EOSTenantDataEntity> datas) {
		Map<String, String> dataMap = new HashMap<>(datas.size());

		for (EOSTenantDataEntity data : datas) {
			dataMap.put(data.getKey(), data.getValue());
		}

		return dataMap;
	}

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
