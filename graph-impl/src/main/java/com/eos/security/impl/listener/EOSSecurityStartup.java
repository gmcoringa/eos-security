/**
 * 
 */
package com.eos.security.impl.listener;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Service;

import com.eos.common.EOSState;
import com.eos.common.EOSUserType;
import com.eos.security.api.vo.EOSRole;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.service.DataBaseServer;
import com.eos.security.impl.service.TransactionManager;
import com.eos.security.impl.service.internal.EOSSystemConstants;
import com.eos.security.impl.service.internal.TransactionManagerImpl;
import com.eos.security.impl.service.permission.EOSPermissionDAO;
import com.eos.security.impl.service.role.EOSRoleDAO;
import com.eos.security.impl.service.role.EOSRoleUserDAO;
import com.eos.security.impl.service.tenant.EOSTenantDAO;
import com.eos.security.impl.service.user.EOSUserDAO;
import com.eos.security.impl.service.user.EOSUserTenantDAO;

/**
 * Startup service. Create application default tenant, user, role and permissions.
 * 
 * @author santos.fabiano
 * 
 */
@Service
public class EOSSecurityStartup implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(EOSSecurityStartup.class);

	@Autowired
	DataBaseServer dataBaseServer;
	@Autowired
	private EOSTenantDAO tenantDAO;
	@Autowired
	private EOSUserDAO userDAO;
	@Autowired
	private EOSUserTenantDAO userTenantDAO;
	@Autowired
	private EOSRoleDAO roleDAO;
	@Autowired
	private EOSRoleUserDAO roleUserDAO;
	@Autowired
	private EOSPermissionDAO permissionDAO;
	@Autowired
	private TransactionManager transactionManager;

	/**
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextEvent) {
		log.info("### Starting EOS-Security ###");
		// Register shutdown hook
		AbstractApplicationContext context = (AbstractApplicationContext) contextEvent.getApplicationContext();
		context.registerShutdownHook();
		SchemaUtil.createSchema(transactionManager);

		transactionManager.begin();
		// Manually create tenant to avoid security checks
		createTenant();
		// Manually create default users and roles to avoid security checks
		createUsers();
		transactionManager.commit();
		log.info("### EOS-Security startup complete ###");
	}

	private void createTenant() {
		// Default tenant
		EOSTenant tenant = tenantDAO.find(EOSSystemConstants.ADMIN_TENANT);
		// Found, then return
		if (tenant != null) {
			return;
		}

		log.debug("Creating EOS default tenant");
		tenant = new EOSTenant().setAlias(EOSSystemConstants.ADMIN_TENANT).setName("EOS Tenant")
				.setDescription("Administration tenant").setState(EOSState.ACTIVE);
		// Use merge to force id on entity
		tenantDAO.create(tenant);
	}

	private void createUsers() {
		// Create Administrator User
		EOSUser adminUser = createUser(EOSSystemConstants.LOGIN_SUPER_ADMIN, "Administrator", EOSUserType.USER);
		// Create Anonymous User
		createUser(EOSSystemConstants.LOGIN_ANONYMOUS, "Anonymous", EOSUserType.SYSTEM);

		// // Super Administrator role
		// EOSRoleEntity adminRole = createAdminRole();
		// // Roles to users
		// createRoleUser(adminUser.getLogin(), adminRole.getCode());
		// // Create system user
		// EOSUserTenantEntity systemUser = createUser(EOSSystemConstants.LOGIN_SYSTEM_USER, "System User",
		// EOSUserType.SYSTEM);
		// // system task user has the same permissions as super administrator
		// createRoleUser(systemUser.getLogin(), adminRole.getCode());
	}

	private EOSUser createUser(String login, String name, EOSUserType type) {
		EOSUser user = userDAO.findUser(login);
		String password = null;

		if (type == EOSUserType.USER) {
			// For all default users, set default password.
			password = DigestUtils.md5Hex("EOSpas$");
		}

		if (user == null) {
			log.debug("Creating EOS default user " + login);
			user = new EOSUser().setFirstName("EOS").setLastName(name).setLogin(login)
					.setEmail(login + "@eossecurity.com").setType(type);

			userDAO.createUser(user, password);
		}

		EOSUser userTenant = userTenantDAO.findByLogin(login, EOSSystemConstants.ADMIN_TENANT);

		if (userTenant == null) {
			log.debug("Creating EOS default user tenant " + login);
			userTenant = new EOSUser().setLogin(login).setState(EOSState.ACTIVE)
					.setTenantAlias(EOSSystemConstants.ADMIN_TENANT);
			userTenantDAO.createUser(userTenant);
		}

		return userTenant;
	}

	private EOSRole createAdminRole() {
		// EOSRoleEntity role = roleDAO.checkedFindByCode(EOSSystemConstants.ROLE_SUPER_ADMIN,
		// EOSSystemConstants.ADMIN_TENANT);
		//
		// if (role == null) {
		// log.debug("Creating EOS default administrator role ");
		// role = new EOSRoleEntity().setCode(EOSSystemConstants.ROLE_SUPER_ADMIN)
		// .setDescription("EOS Administrator Role").setLevel(EOSSystemConstants.INTERNAL_LEVEL);
		// role.setTenantId(EOSSystemConstants.ADMIN_TENANT);
		//
		// roleDAO.persist(role);
		//
		// // Create Admin permission
		// addSuperAdminPermissions(EOSSystemConstants.ROLE_SUPER_ADMIN);
		// }
		//
		// return role;
		return null;
	}

	private void addSuperAdminPermissions(String code) {
		// Set<String> perms = CollectionUtil.asSet(EOSKnownPermissions.PERMISSION_TENAT_ALL, "Tenant.Create",
		// "Tenant.Update.State", "Tenant.Delete");
		//
		// for (String permission : perms) {
		// EOSPermissionEntity permissionAll = new EOSPermissionEntity().setRoleCode(code).setPermission(permission);
		// permissionAll.setTenantId(EOSSystemConstants.ADMIN_TENANT);
		// permissionDAO.persist(permissionAll);
		// }
	}

	private void createRoleUser(String login, String code) {
		// EOSRoleUserEntity roleUser = roleUserDAO.findByUserAndRole(login, code, EOSSystemConstants.ADMIN_TENANT);
		//
		// if (roleUser == null) {
		// log.debug("Adding user " + login + "to role " + code);
		// roleUser = new EOSRoleUserEntity().setRoleCode(code).setUserLogin(login);
		// roleUser.setTenantId(EOSSystemConstants.ADMIN_TENANT);
		// roleUserDAO.persist(roleUser);
		// }
	}

}
