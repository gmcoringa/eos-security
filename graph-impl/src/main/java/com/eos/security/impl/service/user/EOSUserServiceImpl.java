/**
 * 
 */
package com.eos.security.impl.service.user;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.common.EOSState;
import com.eos.common.EOSUserType;
import com.eos.common.exception.EOSDuplicatedEntryException;
import com.eos.common.exception.EOSException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.service.EOSGroupService;
import com.eos.security.api.service.EOSRoleService;
import com.eos.security.api.service.EOSSecurityService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.service.TransactionManager;
import com.eos.security.impl.service.internal.EOSSystemConstants;
import com.eos.security.impl.service.internal.EOSValidator;
import com.eos.security.impl.session.SessionContextManager;

/**
 * Default user service implementation.
 * 
 * @author santos.fabiano
 * 
 */
@Service
public class EOSUserServiceImpl implements EOSUserService {

	private static final Logger log = LoggerFactory.getLogger(EOSUserServiceImpl.class);

	@Autowired
	private EOSUserDAO userDAO;
	@Autowired
	private EOSUserTenantDAO userTenantDAO;
	@Autowired
	private EOSUserTenantDataDAO userTenantDataDAO;
	@Autowired
	private EOSSecurityService svcSecurity;
	@Autowired
	private EOSGroupService svcGroup;
	@Autowired
	private EOSRoleService svcRole;
	@Autowired
	TransactionManager transactionManager;

	/**
	 * @see com.eos.security.api.service.EOSUserService#createUser(com.eos.security.api.vo.EOSUser, Map)
	 */
	@Override
	public EOSUser createUser(EOSUser user, Map<String, String> userData) throws EOSDuplicatedEntryException,
			EOSForbiddenException, EOSUnauthorizedException, EOSValidationException {
		// TODO security
		transactionManager.begin();
		// Override tenant alias
		user.setTenantAlias(SessionContextManager.getCurrentTenantAlias());
		EOSValidator.validateUser(user);
		EOSUser tenantUser = userTenantDAO.findByLogin(user.getLogin(), SessionContextManager.getCurrentTenantAlias());

		if (tenantUser != null) {
			throw new EOSDuplicatedEntryException("There a user with the given login within the given tenant");
		}

		EOSUser eosUser = userDAO.findUser(user.getLogin());

		if (eosUser == null) {
			log.debug("User entity not found, creating it");
			eosUser = userDAO.createUser(user, null);
		}

		tenantUser = userTenantDAO.createUser(user);

		// Override state and tenant
		user.setState(tenantUser.getState());

		// User data
		if (userData != null && !userData.isEmpty()) {
			addUserTenantData(user.getLogin(), userData);
		}

		// Normal users add default user role
		if (user.getType() == EOSUserType.USER) {
			svcRole.addRolesToUser(user.getLogin(), Arrays.asList(EOSSystemConstants.ROLE_TENANT_USER));
		}

		transactionManager.commit();
		return user;
	}

	/**
	 * @see com.eos.security.api.service.EOSUserService#findUser(java.lang.String)
	 */
	@Override
	public EOSUser findUser(String login) throws EOSNotFoundException {
		transactionManager.begin();
		EOSUser user = findTenantUser(login, SessionContextManager.getCurrentTenantAlias());
		transactionManager.commit();
		return user;
	}

	/**
	 * @see com.eos.security.api.service.EOSUserService#findTenantUser(java.lang.String, java.lang.String)
	 */
	@Override
	public EOSUser findTenantUser(String login, String tenantAlias) throws EOSNotFoundException {
		// TODO Validations, cache and security
		transactionManager.begin();
		EOSUser user = compose(userTenantDAO.findByLogin(login, tenantAlias), userDAO.findUser(login));
		transactionManager.commit();
		return user;
	}

	/**
	 * @see com.eos.security.api.service.EOSUserService#findUsers(java.util.List)
	 */
	@Override
	public List<EOSUser> findUsers(List<String> logins) {
		// TODO Validations, cache and security
		// List<EOSUserTenantEntity> entities = userTenantDAO.findByLogins(logins,
		// SessionContextManager.getCurrentTenantId());
		// List<EOSUser> users = new ArrayList<>(entities.size());
		//
		// for (EOSUserTenantEntity entity : entities) {
		// users.add(entityToVo(entity));
		// }
		//
		// return users;
		return null;
	}

	/**
	 * @see com.eos.security.api.service.EOSUserService#listUsers(java.util.List, int, int)
	 */
	@Override
	public List<EOSUser> listUsers(List<EOSState> states, int limit, int offset) {
		// TODO Validations and security
		// if (states == null || states.isEmpty()) {
		// states = Arrays.asList(EOSState.values());
		// }
		//
		// List<EOSUserTenantEntity> entities = userTenantDAO.list(states, SessionContextManager.getCurrentTenantId(),
		// limit, offset);
		// List<EOSUser> users = new ArrayList<>(entities.size());
		//
		// for (EOSUserTenantEntity entity : entities) {
		// users.add(entityToVo(entity));
		// }
		//
		// return users;
		return null;
	}

	/**
	 * @see com.eos.security.api.service.EOSUserService#updateUser(com.eos.security.api.vo.EOSUser)
	 */
	@Override
	public void updateUser(EOSUser user) throws EOSForbiddenException, EOSUnauthorizedException, EOSNotFoundException,
			EOSValidationException {
		// TODO security
		// EOSValidator.validateUser(user);
		// EOSUserTenantEntity entity = userTenantDAO.findByLogin(user.getLogin(),
		// SessionContextManager.getCurrentTenantId());
		// // Find UserEntity, because the attached one in UserTenant isn't managed
		// EOSUserEntity userEntity = userDAO.checkedFind(user.getLogin());
		//
		// if (entity == null || userEntity == null) {
		// throw new EOSNotFoundException("User not found with login: " + user.getLogin());
		// }
		//
		// // UserTenantEntity
		// entity.setNickName(user.getNickName()).setTenantMail(user.getEmail());
		// // UserEntity
		// userEntity.setEmail(user.getPersonalMail()).setFirstName(user.getFirstName()).setLastName(user.getLastName());
		//
		// userDAO.merge(userEntity);
		// userTenantDAO.merge(entity);
	}

	/**
	 * @see com.eos.security.api.service.EOSUserService#updateUserState(java.lang.String, com.eos.common.EOSState)
	 */
	@Override
	public void updateUserState(String login, EOSState state) throws EOSForbiddenException, EOSUnauthorizedException,
			EOSNotFoundException {
		// TODO security and messaging
		// EOSUser user = findUser(login);
		//
		// if (user.getState() != state) {
		// userTenantDAO.updateState(login, SessionContextManager.getCurrentTenantId(), state);
		// }
	}

	/**
	 * @see com.eos.security.api.service.EOSUserService#purgeUser(java.lang.String)
	 */
	@Override
	public void purgeUser(String login) throws EOSForbiddenException, EOSUnauthorizedException, EOSNotFoundException {
		// TODO security and messaging
		// Only disabled users can be deleted
		// if (findUser(login).getState() != EOSState.DISABLED) {
		// throw new EOSForbiddenException("Invalid state for removal", EOSErrorFactory.build()
		// .addError(EOSErrorCodes.INVALID_STATE, "Only users with DISABLED state can be purged").getErrors());
		// }
		//
		// final Long tenantId = SessionContextManager.getCurrentTenantId();
		// // First remove user from all groups and roles
		// svcGroup.removeGroupsByUser(login);
		// svcRole.removeRolesByUser(login);
		// // Remove all user data
		// userTenantDataDAO.clearUserData(login, tenantId);
		// // Remove user from current tenant
		// userTenantDAO.deleteUser(login, tenantId);
		// // If the user do exists on other tenants, delete global reference
		// if (userTenantDAO.countUsers(login) == 0) {
		// userDAO.deleteUser(login);
		// }
	}

	/**
	 * @see com.eos.security.api.service.EOSUserService#setUserPassword(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void setUserPassword(String login, String oldPassword, String newPassword) throws EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		// EOSUserEntity entity = userDAO.checkedFind(login);
		// String newDigested = DigestUtils.md5Hex(newPassword);
		//
		// if (StringUtil.isEmpty(oldPassword)) {
		// svcSecurity.checkPermissions(EOSKnownPermissions.PASSWORD_UPDATE);
		// } else {
		// // Verify credentials
		// svcSecurity.checkLogged();
		// if (!SessionContextManager.getCurrentUserLogin().equals(login)) {
		// throw new EOSForbiddenException("You cannot change the password for other users!");
		// }
		//
		// // Verify password match
		// String oldDigested = DigestUtils.md5Hex(oldPassword);
		// if (!oldDigested.equals(entity.getPassword())) {
		// List<EOSError> errors = Arrays.asList(new EOSError(EOSErrorCodes.INVALID_PASSWORD, "Invalid password"));
		// throw new EOSValidationException("Password mismatch", errors);
		// }
		// }
		//
		// EOSValidator.validatePassword(newPassword);
		// entity.setPassword(newDigested);
		// userDAO.merge(entity);
	}

	/**
	 * @see com.eos.security.api.service.EOSUserService#checkForLogin(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public EOSUser checkForLogin(String login, String email, String password) throws EOSException {
		// EOSUserEntity entity = null;
		// EOSUser user = findUser(login, email, entity);
		//
		// if (user.getState() != EOSState.ACTIVE && user.getType() != EOSUserType.USER) {
		// throw new EOSException("User not found or invalid");
		// }
		//
		// if (entity == null) {
		// entity = userDAO.find(user.getLogin());
		// }
		// // Verify password match
		// String digested = DigestUtils.md5Hex(password);
		// if (!digested.equals(entity.getPassword())) {
		// throw new EOSException("User not found or invalid");
		// }
		//
		// return user;
		return null;
	}

	// private EOSUser findUser(String login, String email, EOSUserEntity entity) throws EOSException {
	// EOSUser user = null;
	//
	// if (!StringUtil.isEmpty(login)) {
	// try {
	// user = findUser(login);
	// } catch (EOSNotFoundException e) {
	// log.debug("Check for login: not found by login");
	// }
	//
	// if (user != null) {
	// return user;
	// }
	// }
	//
	// if (!StringUtil.isEmpty(email)) {
	// log.warn("Finding user by tenant e-mail: " + email);
	// // Not found, try by tenant e-mail
	// user = entityToVo(userTenantDAO.findByEMail(email, SessionContextManager.getCurrentTenantId()));
	// if (user != null) {
	// return user;
	// }
	//
	// // Still not found, try by personal e-mail
	// entity = userDAO.findByEMail(email);
	// if (entity != null) {
	// log.warn("Finding user by personal e-mail: " + email);
	// user = findUser(entity.getLogin());
	// }
	// }
	//
	// // not found or not found in the current tenant
	// if (user == null) {
	// throw new EOSException("User not found or invalid");
	// }
	//
	// return user;
	// }

	// private EOSUser entityToVo(EOSUserTenantEntity entity) {
	// if (entity == null) {
	// return null;
	// }
	//
	// return new EOSUser().setLogin(entity.getLogin()).setFirstName(entity.getUser().getFirstName())
	// .setLastName(entity.getUser().getLastName()).setPersonalMail(entity.getUser().getEmail())
	// .setNickName(entity.getNickName()).setEmail(entity.getTenantMail()).setState(entity.getState())
	// .setType(entity.getUser().getType()).setTenantId(entity.getTenantId());
	//
	// }

	// User Data

	/**
	 * @see com.eos.security.api.service.EOSUserService#updateUserData(java.lang.String, java.util.Map)
	 */
	@Override
	public void updateUserData(String login, Map<String, String> userData) throws EOSForbiddenException,
			EOSUnauthorizedException {
		// TODO security check
		// final Long tenantId = SessionContextManager.getCurrentTenantId();
		// List<String> keys = new ArrayList<>(userData.size());
		// keys.addAll(userData.keySet());
		// // Look for data that already exists
		// Map<String, String> dataFound = listUserData(login, keys);
		// List<String> remove = new ArrayList<>();
		//
		// // Updates
		// log.debug("Starting User Tenant data update ");
		// for (Entry<String, String> entry : dataFound.entrySet()) {
		// // Add removes to removal list
		// if (StringUtil.isEmpty(userData.get(entry.getKey()))) {
		// remove.add(entry.getKey());
		// log.debug("User Tenant data set for removal: " + entry.getKey());
		// } else {
		// // Update
		// userTenantDataDAO.updateUserData(login, entry.getKey(), entry.getValue(), tenantId);
		// log.debug("User Tenant data [" + entry.getKey() + "] updated");
		// }
		// // Remove key pair value from tenantData map
		// userData.remove(entry.getKey());
		// }
		//
		// // Add new data
		// addUserTenantData(login, userData);
		// // Remove removal list
		// if (!remove.isEmpty()) {
		// log.debug("Starting Tenant data removal ");
		// userTenantDataDAO.deleteUserData(login, remove, tenantId);
		// }

		// TODO Remove user tenant data cache using keys variable
	}

	private void addUserTenantData(String login, Map<String, String> userData) {
		log.debug("Adding user data to user " + login);

		// for (Entry<String, String> entry : userData.entrySet()) {
		// EOSUserTenantDataEntity entity = new EOSUserTenantDataEntity().setLogin(login).setKey(entry.getKey())
		// .setValue(entry.getValue());
		// userTenantDataDAO.persist(entity);
		// }
	}

	/**
	 * @see com.eos.security.api.service.EOSUserService#findUserData(java.lang.String, java.lang.String)
	 */
	@Override
	public String findUserData(String login, String key) {
		// TODO security check
		// return userTenantDataDAO.findUserData(login, key, SessionContextManager.getCurrentTenantId());
		return null;
	}

	/**
	 * @see com.eos.security.api.service.EOSUserService#listUserData(java.lang.String, java.util.List)
	 */
	@Override
	public Map<String, String> listUserData(String login, List<String> keys) {
		// TODO security check
		// return entitiesToMap(userTenantDataDAO.listUserData(login, keys,
		// SessionContextManager.getCurrentTenantId()));
		return null;
	}

	/**
	 * @see com.eos.security.api.service.EOSUserService#listUserData(java.lang.String, int, int)
	 */
	@Override
	public Map<String, String> listUserData(String login, int limit, int offset) {
		// TODO security check
		// return entitiesToMap(userTenantDataDAO.listUserData(login, limit, offset,
		// SessionContextManager.getCurrentTenantId()));
		return null;
	}

	// private Map<String, String> entitiesToMap(List<EOSUserTenantDataEntity> entities) {
	// Map<String, String> userData = new HashMap<>(entities.size());
	//
	// for (EOSUserTenantDataEntity entity : entities) {
	// userData.put(entity.getKey(), entity.getValue());
	// }
	//
	// return userData;
	// }

	// User Permission

	/**
	 * @see com.eos.security.api.service.EOSUserService#hasPermission(java.lang.String, java.util.List)
	 */
	@Override
	public Map<String, Boolean> hasPermission(String login, List<String> permissions) {
		// TODO Auto-generated method stub
		return null;
	}

	// *******************
	// Utilities
	// *******************

	/**
	 * Compose a tenant user with a user. If tenant user is null, then null is returned.
	 * 
	 * @param tenantUser
	 *            - Tenant User
	 * @param user
	 *            - User (global)
	 * @return TenantUser composition or null if tenant user is null.
	 */
	private EOSUser compose(EOSUser tenantUser, EOSUser user) {
		if (tenantUser == null) {
			return null;
		}

		return user.setNickName(tenantUser.getNickName()).setEmail(tenantUser.getEmail())
				.setState(tenantUser.getState()).setTenantAlias(tenantUser.getTenantAlias());
	}

}
