/**
 * 
 */
package com.eos.security.impl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.eos.commons.jpa.AbstractDAO;
import com.eos.security.impl.model.EOSRoleUserEntity;

/**
 * Role User entity DAO.
 * 
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSRoleUserDAO extends AbstractDAO<EOSRoleUserEntity> {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public EOSRoleUserDAO() {
		super(EOSRoleUserEntity.class);
	}

	/**
	 * @see com.eos.commons.jpa.AbstractDAO#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public EOSRoleUserEntity findByUserAndRole(String login, String code, Long tenantId) {
		try {
			return em.createNamedQuery(EOSRoleUserEntity.QUERY_FIND, EOSRoleUserEntity.class)
					.setParameter(EOSRoleUserEntity.PARAM_USER, login).setParameter(EOSRoleUserEntity.PARAM_ROLE, code)
					.setParameter(EOSRoleUserEntity.PARAM_TENANT, tenantId).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void removeUsersFromRole(String code, List<String> users, Long tenantId) {
		em.createNamedQuery(EOSRoleUserEntity.QUERY_DELETE_USERS).setParameter(EOSRoleUserEntity.PARAM_ROLE, code)
				.setParameter(EOSRoleUserEntity.PARAM_USER, users)
				.setParameter(EOSRoleUserEntity.PARAM_TENANT, tenantId).executeUpdate();
	}

	public void removeRolesFromUser(List<String> roles, String user, Long tenantId) {
		em.createNamedQuery(EOSRoleUserEntity.QUERY_DELETE_ROLES).setParameter(EOSRoleUserEntity.PARAM_ROLE, roles)
				.setParameter(EOSRoleUserEntity.PARAM_USER, user)
				.setParameter(EOSRoleUserEntity.PARAM_TENANT, tenantId).executeUpdate();
	}

	public List<String> listUsersByRole(String code, Long tenantId) {
		return em.createNamedQuery(EOSRoleUserEntity.QUERY_LIST_USERS, String.class)
				.setParameter(EOSRoleUserEntity.PARAM_ROLE, code)
				.setParameter(EOSRoleUserEntity.PARAM_TENANT, tenantId).getResultList();
	}

	public List<String> listRolesByUser(String login, Long tenantId) {
		return em.createNamedQuery(EOSRoleUserEntity.QUERY_LIST_ROLES, String.class)
				.setParameter(EOSRoleUserEntity.PARAM_USER, login)
				.setParameter(EOSRoleUserEntity.PARAM_TENANT, tenantId).getResultList();
	}

	public void deleteByUser(String login, Long tenantId) {
		em.createNamedQuery(EOSRoleUserEntity.QUERY_DELETE_BY_USER)
				.setParameter(EOSRoleUserEntity.PARAM_TENANT, tenantId)
				.setParameter(EOSRoleUserEntity.PARAM_USER, login).executeUpdate();
	}

}
