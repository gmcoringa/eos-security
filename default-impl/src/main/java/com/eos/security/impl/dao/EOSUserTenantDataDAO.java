/**
 * 
 */
package com.eos.security.impl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Repository;

import com.eos.commons.jpa.AbstractDAO;
import com.eos.security.impl.model.EOSUserTenantDataEntity;

/**
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSUserTenantDataDAO extends AbstractDAO<EOSUserTenantDataEntity> {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public EOSUserTenantDataDAO() {
		super(EOSUserTenantDataEntity.class);
	}

	/**
	 * @see com.eos.commons.jpa.AbstractDAO#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public void updateUserData(String login, String key, String value, Long tenantId) {
		em.createNamedQuery(EOSUserTenantDataEntity.QUERY_UPDATE)
				.setParameter(EOSUserTenantDataEntity.PARAM_LOGIN, login)
				.setParameter(EOSUserTenantDataEntity.PARAM_KEY, key)
				.setParameter(EOSUserTenantDataEntity.PARAM_VALUE, value)
				.setParameter(EOSUserTenantDataEntity.PARAM_TENANT, tenantId).executeUpdate();
	}

	public void deleteUserData(String login, List<String> keys, Long tenantId) {
		em.createNamedQuery(EOSUserTenantDataEntity.QUERY_DELETE_BY_KEY)
				.setParameter(EOSUserTenantDataEntity.PARAM_LOGIN, login)
				.setParameter(EOSUserTenantDataEntity.PARAM_KEY, keys)
				.setParameter(EOSUserTenantDataEntity.PARAM_TENANT, tenantId).executeUpdate();
	}

	public String findUserData(String login, String key, Long tenantId) {
		try {
			return em.createNamedQuery(EOSUserTenantDataEntity.QUERY_FIND, String.class)
					.setParameter(EOSUserTenantDataEntity.PARAM_LOGIN, login)
					.setParameter(EOSUserTenantDataEntity.PARAM_KEY, key)
					.setParameter(EOSUserTenantDataEntity.PARAM_TENANT, tenantId).getSingleResult();
		} catch (PersistenceException e) {
			return null;
		}
	}

	public List<EOSUserTenantDataEntity> listUserData(String login, int limit, int offset, Long tenantId) {
		return em.createNamedQuery(EOSUserTenantDataEntity.QUERY_LIST, EOSUserTenantDataEntity.class)
				.setParameter(EOSUserTenantDataEntity.PARAM_LOGIN, login)
				.setParameter(EOSUserTenantDataEntity.PARAM_TENANT, tenantId).setFirstResult(offset)
				.setMaxResults(limit).getResultList();
	}

	public List<EOSUserTenantDataEntity> listUserData(String login, List<String> keys, Long tenantId) {
		return em.createNamedQuery(EOSUserTenantDataEntity.QUERY_BY_KEY, EOSUserTenantDataEntity.class)
				.setParameter(EOSUserTenantDataEntity.PARAM_LOGIN, login)
				.setParameter(EOSUserTenantDataEntity.PARAM_KEY, keys)
				.setParameter(EOSUserTenantDataEntity.PARAM_TENANT, tenantId).getResultList();
	}

	public void clearUserData(String login, Long tenantId) {
		em.createNamedQuery(EOSUserTenantDataEntity.QUERY_DELETE_BY_USER)
				.setParameter(EOSUserTenantDataEntity.PARAM_TENANT, tenantId)
				.setParameter(EOSUserTenantDataEntity.PARAM_LOGIN, login).executeUpdate();
	}

}
