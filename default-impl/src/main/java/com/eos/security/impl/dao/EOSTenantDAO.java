/**
 * 
 */
package com.eos.security.impl.dao;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.eos.common.EOSState;
import com.eos.commons.jpa.AbstractDAO;
import com.eos.security.impl.model.EOSTenantEntity;

/**
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSTenantDAO extends AbstractDAO<EOSTenantEntity> {

	@PersistenceContext
	private EntityManager em;

	public EOSTenantDAO() {
		super(EOSTenantEntity.class);
	}

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public List<EOSTenantEntity> listTenants(List<EOSState> states, int limit,
			int offset) {
		if (states == null || states.isEmpty()) {
			states = Arrays.asList(EOSState.values());
		}

		// TODO limit validation
		return em
				.createNamedQuery(EOSTenantEntity.QUERY_LIST,
						EOSTenantEntity.class)
				.setParameter(EOSTenantEntity.PARAM_STATE, states)
				.setFirstResult(offset).setMaxResults(limit).getResultList();
	}

	public void purgeTenant(Long id) {
		em.createNamedQuery(EOSTenantEntity.QUERY_PURGE).setParameter("id", id)
				.executeUpdate();
	}

	public List<EOSTenantEntity> findTenants(List<Long> ids) {
		return em
				.createNamedQuery(EOSTenantEntity.QUERY_LIST_BY_IDS,
						EOSTenantEntity.class)
				.setParameter(EOSTenantEntity.PARAM_ID, ids).getResultList();
	}
}
