/**
 * 
 */
package com.eos.security.impl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eos.commons.jpa.AbstractDAO;
import com.eos.security.impl.model.EOSTenantDataEntity;

/**
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSTenantDataDAO extends AbstractDAO<EOSTenantDataEntity> {

	@PersistenceContext
	private EntityManager em;

	public EOSTenantDataDAO() {
		super(EOSTenantDataEntity.class);
	}

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@Transactional
	public void updateTenantData(Long tenantId, String key, String value) {
		em.createNamedQuery(EOSTenantDataEntity.QUERY_UPDATE)
				.setParameter(EOSTenantDataEntity.PARAM_TENANT, tenantId)
				.setParameter(EOSTenantDataEntity.PARAM_KEY, key)
				.setParameter(EOSTenantDataEntity.PARAM_VALUE, value)
				.executeUpdate();
	}

	@Transactional
	public void deleteTenantData(Long tenantId, List<String> keys) {
		em.createNamedQuery(EOSTenantDataEntity.QUERY_DELETE_KEYS)
				.setParameter(EOSTenantDataEntity.PARAM_TENANT, tenantId)
				.setParameter(EOSTenantDataEntity.PARAM_KEY, keys)
				.executeUpdate();
	}

	@Transactional(readOnly = true)
	public String findTenantDataValue(Long tenantId, String key) {
		try {
			return em
					.createNamedQuery(EOSTenantDataEntity.QUERY_FIND,
							String.class)
					.setParameter(EOSTenantDataEntity.PARAM_TENANT, tenantId)
					.setParameter(EOSTenantDataEntity.PARAM_KEY, key)
					.getSingleResult();
		} catch (PersistenceException e) {
			return null;
		}
	}

	@Transactional(readOnly = true)
	public List<EOSTenantDataEntity> findTenantDataValues(Long tenantId,
			List<String> keys) {
		return em
				.createNamedQuery(EOSTenantDataEntity.QUERY_FIND_BY_KEYS,
						EOSTenantDataEntity.class)
				.setParameter(EOSTenantDataEntity.PARAM_TENANT, tenantId)
				.setParameter(EOSTenantDataEntity.PARAM_KEY, keys)
				.getResultList();
	}

	@Transactional(readOnly = true)
	public List<EOSTenantDataEntity> listTenantData(Long tenantId, int limit,
			int offset) {
		return em
				.createNamedQuery(EOSTenantDataEntity.QUERY_LIST,
						EOSTenantDataEntity.class)
				.setParameter(EOSTenantDataEntity.PARAM_TENANT, tenantId)
				.setFirstResult(offset).setMaxResults(limit).getResultList();
	}
}
