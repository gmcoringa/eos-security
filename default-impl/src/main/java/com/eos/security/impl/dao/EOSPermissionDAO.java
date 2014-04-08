/**
 * 
 */
package com.eos.security.impl.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.eos.commons.jpa.AbstractDAO;
import com.eos.security.impl.model.EOSPermissionEntity;

/**
 * Role Permission DAO.
 * 
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSPermissionDAO extends AbstractDAO<EOSPermissionEntity> {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public EOSPermissionDAO() {
		super(EOSPermissionEntity.class);
	}

	/**
	 * @see com.eos.commons.jpa.AbstractDAO#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public void deleteRolePermissions(Long tenantId, String code, Set<String> permissions) {
		em.createNamedQuery(EOSPermissionEntity.QUERY_REMOVE).setParameter(EOSPermissionEntity.PARAM_ROLE, code)
				.setParameter(EOSPermissionEntity.PARAM_PERMISSION, permissions)
				.setParameter(EOSPermissionEntity.PARAM_TENANT, tenantId).executeUpdate();
	}

	public List<String> listRolePermissions(Long tenantId, String code) {
		return em.createNamedQuery(EOSPermissionEntity.QUERY_LIST, String.class)
				.setParameter(EOSPermissionEntity.PARAM_ROLE, code)
				.setParameter(EOSPermissionEntity.PARAM_TENANT, tenantId).getResultList();
	}

	public List<EOSPermissionEntity> listEntities(Long tenantId, Set<String> permissions) {
		return em.createNamedQuery(EOSPermissionEntity.QUERY_LIST_PERM, EOSPermissionEntity.class)
				.setParameter(EOSPermissionEntity.PARAM_PERMISSION, permissions)
				.setParameter(EOSPermissionEntity.PARAM_TENANT, tenantId).getResultList();
	}

}
