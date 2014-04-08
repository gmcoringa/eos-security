/**
 * 
 */
package com.eos.security.impl.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eos.common.EOSLevel;
import com.eos.common.exception.EOSDuplicatedEntryException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.service.EOSGroupService;
import com.eos.security.api.service.EOSRoleService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.vo.EOSGroup;
import com.eos.security.api.vo.EOSRole;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.dao.EOSRoleDAO;
import com.eos.security.impl.dao.EOSRoleGroupDAO;
import com.eos.security.impl.dao.EOSRoleUserDAO;
import com.eos.security.impl.model.EOSRoleEntity;
import com.eos.security.impl.model.EOSRoleGroupEntity;
import com.eos.security.impl.model.EOSRoleUserEntity;
import com.eos.security.impl.service.internal.EOSValidator;
import com.eos.security.impl.session.SessionContextManager;

/**
 * Default Role service implementation.
 * 
 * @author santos.fabiano
 * 
 */
@Service
public class EOSRoleServiceImpl implements EOSRoleService {

	private static final Logger log = LoggerFactory.getLogger(EOSRoleServiceImpl.class);

	private EOSRoleDAO roleDAO;
	private EOSRoleUserDAO roleUserDAO;
	private EOSRoleGroupDAO roleGroupDAO;
	private EOSUserService svcUser;
	private EOSGroupService svcGroup;

	@Autowired
	public void setRoleDAO(EOSRoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

	@Autowired
	public void setRoleUserDAO(EOSRoleUserDAO roleUserDAO) {
		this.roleUserDAO = roleUserDAO;
	}

	@Autowired
	public void setRoleGroupDAO(EOSRoleGroupDAO roleGroupDAO) {
		this.roleGroupDAO = roleGroupDAO;
	}

	@Autowired
	public void setUserService(EOSUserService svcUser) {
		this.svcUser = svcUser;
	}

	@Autowired
	public void setGroupService(EOSGroupService svcGroup) {
		this.svcGroup = svcGroup;
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#createRole(com.eos.security.api.vo.EOSRole)
	 */
	@Override
	@Transactional
	public EOSRole createRole(EOSRole role) throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		// TODO security and messaging
		EOSValidator.validateRole(role);
		EOSRoleEntity entity = new EOSRoleEntity().setCode(role.getCode()).setDescription(role.getDescription())
				.setLevel(role.getLevel());

		roleDAO.persist(entity);
		return role;
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#updateRole(com.eos.security.api.vo.EOSRole)
	 */
	@Override
	@Transactional
	public void updateRole(EOSRole role) throws EOSForbiddenException, EOSUnauthorizedException, EOSNotFoundException,
			EOSValidationException {
		// TODO security and messaging
		EOSValidator.validateRole(role);
		EOSRoleEntity entity = roleDAO.findByCode(role.getCode(), SessionContextManager.getCurrentTenantId());
		entity.setDescription(role.getDescription()).setLevel(role.getLevel());
		roleDAO.merge(entity);

		if (log.isDebugEnabled()) {
			log.debug("Created role: " + role.toString());
		}
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#deleteRole(java.lang.String)
	 */
	@Override
	@Transactional
	public void deleteRole(String code) throws EOSForbiddenException, EOSUnauthorizedException {
		// TODO security, validation and messaging
		roleDAO.deleteByCode(code, SessionContextManager.getCurrentTenantId());
		log.debug("Deleted role: " + code);
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#findRole(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public EOSRole findRole(String code) throws EOSForbiddenException, EOSNotFoundException {
		// TODO security, validation
		return entityToVO(roleDAO.findByCode(code, SessionContextManager.getCurrentTenantId()));
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#findRoles(java.util.List)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<EOSRole> findRoles(List<String> codes) throws EOSForbiddenException {
		// TODO security, validation
		if (codes == null || codes.isEmpty()) {
			return Collections.emptyList();
		}

		List<EOSRoleEntity> entities = roleDAO.findByCodes(codes, SessionContextManager.getCurrentTenantId());
		List<EOSRole> roles = new ArrayList<>(entities.size());

		for (EOSRoleEntity entity : entities) {
			roles.add(entityToVO(entity));
		}

		return roles;
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#listRoles(java.lang.Integer,
	 *      java.lang.Integer, int, int)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<EOSRole> listRoles(Integer minimumLevel, Integer maximumLevel, int limit, int offset)
			throws EOSUnauthorizedException {
		// TODO security, validation
		if (minimumLevel == null) {
			minimumLevel = EOSLevel.PUBLIC.getLevel();
		}

		if (maximumLevel == null) {
			maximumLevel = EOSLevel.MAXIMUM.getLevel();
		}

		List<EOSRoleEntity> entities = roleDAO.listRoles(SessionContextManager.getCurrentTenantId(), minimumLevel,
				maximumLevel, limit, offset);
		List<EOSRole> roles = new ArrayList<>(entities.size());

		for (EOSRoleEntity entity : entities) {
			roles.add(entityToVO(entity));
		}

		return roles;
	}

	// Role User

	/**
	 * @see com.eos.security.api.service.EOSRoleService#addUsersToRole(java.lang.String,
	 *      java.util.List)
	 */
	@Override
	@Transactional
	public void addUsersToRole(String code, List<String> users) throws EOSForbiddenException, EOSUnauthorizedException {
		// TODO security, validation and messaging, user has role

		for (String login : users) {
			createRoleUser(code, login);
		}
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#addRolesToUser(java.lang.String,
	 *      java.util.List)
	 */
	@Override
	@Transactional
	public void addRolesToUser(String login, List<String> roles) throws EOSForbiddenException, EOSUnauthorizedException {
		// TODO security, validation and messaging, user has role

		for (String code : roles) {
			createRoleUser(code, login);
		}
	}

	private void createRoleUser(String code, String userLogin) {
		EOSRoleUserEntity entity = new EOSRoleUserEntity();
		entity.setRoleCode(code).setUserLogin(userLogin);

		roleUserDAO.persist(entity);
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#removeUsersFromRole(java.lang.String,
	 *      java.util.List)
	 */
	@Override
	@Transactional
	public void removeUsersFromRole(String code, List<String> users) throws EOSForbiddenException,
			EOSUnauthorizedException {
		// TODO security, validation and messaging
		roleUserDAO.removeUsersFromRole(code, users, SessionContextManager.getCurrentTenantId());
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#removeRolesFromUser(java.lang.String,
	 *      java.util.List)
	 */
	@Override
	@Transactional
	public void removeRolesFromUser(String login, List<String> roles) throws EOSForbiddenException,
			EOSUnauthorizedException {
		// TODO security, validation and messaging
		roleUserDAO.removeRolesFromUser(roles, login, SessionContextManager.getCurrentTenantId());
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#removeRolesByUser(java.lang.String)
	 */
	@Override
	@Transactional
	public void removeRolesByUser(String login) throws EOSForbiddenException, EOSUnauthorizedException {
		// TODO security, validation
		roleUserDAO.deleteByUser(login, SessionContextManager.getCurrentTenantId());
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#listRoleUsers(java.lang.String,
	 *      int, int)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<EOSUser> listRoleUsers(String code, int limit, int offset) {
		// TODO security, validation
		List<String> logins = roleUserDAO.listUsersByRole(code, SessionContextManager.getCurrentTenantId());
		// TODO List of groups as sub lists using limit and offset
		return svcUser.findUsers(logins);
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#listUserRoles(java.lang.String,
	 *      int, int)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<EOSRole> listUserRoles(String login, int limit, int offset) throws EOSForbiddenException {
		// TODO security, validation, cache user roles
		List<String> roles = roleUserDAO.listRolesByUser(login, SessionContextManager.getCurrentTenantId());
		// TODO limit and offset applied on cached user roles
		return findRoles(roles);
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#listUserRoleCodes(java.lang.String)
	 */
	@Override
	public List<String> listUserRoleCodes(String login) {
		// TODO security, validation, cache user roles
		return roleUserDAO.listRolesByUser(login, SessionContextManager.getCurrentTenantId());
	}

	// Role Group

	/**
	 * @see com.eos.security.api.service.EOSRoleService#addGroupsToRole(java.lang.String,
	 *      java.util.List)
	 */
	@Override
	@Transactional
	public void addGroupsToRole(String code, List<Long> groups) throws EOSForbiddenException, EOSUnauthorizedException {
		// TODO security, validation and messaging
		for (Long groupId : groups) {
			createRoleGroup(code, groupId);
		}
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#addRolesToGroup(java.lang.Long,
	 *      java.util.List)
	 */
	@Override
	@Transactional
	public void addRolesToGroup(Long groupId, List<String> codes) throws EOSForbiddenException,
			EOSUnauthorizedException {
		// TODO security, validation and messaging
		for (String code : codes) {
			createRoleGroup(code, groupId);
		}
	}

	private void createRoleGroup(String code, Long groupId) {
		EOSRoleGroupEntity entity = new EOSRoleGroupEntity().setGroupId(groupId).setRoleCode(code);
		roleGroupDAO.persist(entity);
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#removeGroupsFromRole(java.lang.String,
	 *      java.util.List)
	 */
	@Override
	@Transactional
	public void removeGroupsFromRole(String code, List<Long> groups) throws EOSForbiddenException,
			EOSUnauthorizedException {
		// TODO security, validation and messaging
		roleGroupDAO.removeRoleFromGroups(SessionContextManager.getCurrentTenantId(), code, groups);
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#removeRolesFromGroup(java.lang.Long,
	 *      java.util.List)
	 */
	@Override
	@Transactional
	public void removeRolesFromGroup(Long groupId, List<String> codes) throws EOSForbiddenException,
			EOSUnauthorizedException {
		// TODO security, validation and messaging
		roleGroupDAO.removeGroupFromRoles(SessionContextManager.getCurrentTenantId(), groupId, codes);
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#listRoleGroups(java.lang.String,
	 *      int, int)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<EOSGroup> listRoleGroups(String code, int limit, int offset) throws EOSForbiddenException {
		// TODO security, validation
		List<Long> groups = roleGroupDAO.listRoleGroups(SessionContextManager.getCurrentTenantId(), code);
		// TODO List of groups as sub lists using limit and offset
		return svcGroup.findGroups(groups);
	}

	/**
	 * @see com.eos.security.api.service.EOSRoleService#listGroupRoles(java.lang.Long,
	 *      int, int)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<EOSRole> listGroupRoles(Long groupId, int limit, int offset) throws EOSForbiddenException {
		// TODO security, validation, cache
		List<String> codes = roleGroupDAO.listGroupRoles(SessionContextManager.getCurrentTenantId(), groupId);
		// TODO List of codes as sub lists using limit and offset
		return findRoles(codes);
	}

	// Util

	private EOSRole entityToVO(EOSRoleEntity entity) {
		if (entity == null) {
			return null;
		}

		EOSRole role = new EOSRole().setCode(entity.getCode()).setDescription(entity.getDescription())
				.setLevel(entity.getLevel()).setTenantId(entity.getTenantId());

		return role;
	}

}
