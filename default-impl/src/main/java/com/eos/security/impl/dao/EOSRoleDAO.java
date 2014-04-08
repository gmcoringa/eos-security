/**
 * 
 */
package com.eos.security.impl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.eos.common.exception.EOSNotFoundException;
import com.eos.commons.jpa.AbstractDAO;
import com.eos.security.impl.model.EOSRoleEntity;

/**
 * Role entity DAO.
 * 
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSRoleDAO extends AbstractDAO<EOSRoleEntity> {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public EOSRoleDAO() {
		super(EOSRoleEntity.class);
	}

	/**
	 * @see com.eos.commons.jpa.AbstractDAO#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public EOSRoleEntity checkedFindByCode(String code, Long tenantId) {
		try {
			return em
					.createNamedQuery(EOSRoleEntity.QUERY_FIND,
							EOSRoleEntity.class)
					.setParameter(EOSRoleEntity.PARAM_CODE, code)
					.setParameter(EOSRoleEntity.PARAM_TENANT, tenantId)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public EOSRoleEntity findByCode(String code, Long tenantId)
			throws EOSNotFoundException {
		try {
			return em
					.createNamedQuery(EOSRoleEntity.QUERY_FIND,
							EOSRoleEntity.class)
					.setParameter(EOSRoleEntity.PARAM_CODE, code)
					.setParameter(EOSRoleEntity.PARAM_TENANT, tenantId)
					.getSingleResult();
		} catch (NoResultException e) {
			throw new EOSNotFoundException("No role found with code " + code);
		}
	}

	public List<EOSRoleEntity> findByCodes(List<String> codes, Long tenantId) {
		return em
				.createNamedQuery(EOSRoleEntity.QUERY_FIND_MULTIPLE,
						EOSRoleEntity.class)
				.setParameter(EOSRoleEntity.PARAM_CODE, codes)
				.setParameter(EOSRoleEntity.PARAM_TENANT, tenantId)
				.getResultList();
	}

	public void deleteByCode(String code, Long tenantId) {
		em.createNamedQuery(EOSRoleEntity.QUERY_DELETE)
				.setParameter(EOSRoleEntity.PARAM_CODE, code)
				.setParameter(EOSRoleEntity.PARAM_TENANT, tenantId)
				.executeUpdate();
	}

	public List<EOSRoleEntity> listRoles(Long tenantId, Integer minimumLevel,
			Integer maximumLevel, int limit, int offset) {
		return em
				.createNamedQuery(EOSRoleEntity.QUERY_LIST, EOSRoleEntity.class)
				.setParameter(EOSRoleEntity.PARAM_TENANT, tenantId)
				.setParameter(EOSRoleEntity.PARAM_MIN_LEVEL, minimumLevel)
				.setParameter(EOSRoleEntity.PARAM_MAX_LEVEL, maximumLevel)
				.setFirstResult(offset).setMaxResults(limit).getResultList();
	}

}
