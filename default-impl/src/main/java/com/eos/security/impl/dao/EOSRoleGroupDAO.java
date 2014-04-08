/**
 * 
 */
package com.eos.security.impl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.eos.commons.jpa.AbstractDAO;
import com.eos.security.impl.model.EOSRoleGroupEntity;

/**
 * Role Group DAO.
 * 
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSRoleGroupDAO extends AbstractDAO<EOSRoleGroupEntity> {

	@PersistenceContext
	private EntityManager em;

	protected EOSRoleGroupDAO() {
		super(EOSRoleGroupEntity.class);
	}

	/**
	 * @see com.eos.commons.jpa.AbstractDAO#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public void removeRoleFromGroups(Long tenantId, String code,
			List<Long> groups) {
		em.createNamedQuery(EOSRoleGroupEntity.QUERY_REMOVE_GROUPS)
				.setParameter(EOSRoleGroupEntity.PARAM_ROLE, code)
				.setParameter(EOSRoleGroupEntity.PARAM_GROUP, groups)
				.setParameter(EOSRoleGroupEntity.PARAM_TENANT, tenantId)
				.executeUpdate();
	}

	public void removeGroupFromRoles(Long tenantId, Long groupId,
			List<String> codes) {
		em.createNamedQuery(EOSRoleGroupEntity.QUERY_REMOVE_ROLES)
				.setParameter(EOSRoleGroupEntity.PARAM_ROLE, codes)
				.setParameter(EOSRoleGroupEntity.PARAM_GROUP, groupId)
				.setParameter(EOSRoleGroupEntity.PARAM_TENANT, tenantId)
				.executeUpdate();
	}

	public List<String> listGroupRoles(Long tenantId, Long groupId) {
		return em
				.createNamedQuery(EOSRoleGroupEntity.QUERY_LIST_ROLES,
						String.class)
				.setParameter(EOSRoleGroupEntity.PARAM_GROUP, groupId)
				.setParameter(EOSRoleGroupEntity.PARAM_TENANT, tenantId)
				.getResultList();
	}

	public List<Long> listRoleGroups(Long tenantId, String code) {
		return em
				.createNamedQuery(EOSRoleGroupEntity.QUERY_LIST_GROUPS,
						Long.class)
				.setParameter(EOSRoleGroupEntity.PARAM_ROLE, code)
				.setParameter(EOSRoleGroupEntity.PARAM_TENANT, tenantId)
				.getResultList();
	}

	public List<EOSRoleGroupEntity> listByRoleAndGroup(Long tenantId,
			List<String> codes, List<Long> groups) {
		return em
				.createNamedQuery(EOSRoleGroupEntity.QUERY_LIST_ROLE_GROUP,
						EOSRoleGroupEntity.class)
				.setParameter(EOSRoleGroupEntity.PARAM_GROUP, groups)
				.setParameter(EOSRoleGroupEntity.PARAM_ROLE, codes)
				.setParameter(EOSRoleGroupEntity.PARAM_TENANT, tenantId)
				.getResultList();
	}
}
