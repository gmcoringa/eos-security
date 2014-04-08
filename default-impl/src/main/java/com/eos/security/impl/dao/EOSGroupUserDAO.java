/**
 * 
 */
package com.eos.security.impl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.eos.commons.jpa.AbstractDAO;
import com.eos.security.impl.model.EOSGroupUserEntity;

/**
 * EOS Group User DAO.
 * 
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSGroupUserDAO extends AbstractDAO<EOSGroupUserEntity> {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	protected EOSGroupUserDAO() {
		super(EOSGroupUserEntity.class);
	}

	/**
	 * @see com.eos.commons.jpa.AbstractDAO#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public List<String> listUserLogins(Long tenantId, Long groupId) {
		return em.createNamedQuery(EOSGroupUserEntity.QUERY_GROUP_USERS, String.class)
				.setParameter(EOSGroupUserEntity.PARAM_TENANT, tenantId)
				.setParameter(EOSGroupUserEntity.PARAM_GROUP, groupId).getResultList();
	}

	public List<Long> listGroupIds(Long tenantId, String userLogin) {
		return em.createNamedQuery(EOSGroupUserEntity.QUERY_USER_GROUPS, Long.class)
				.setParameter(EOSGroupUserEntity.PARAM_TENANT, tenantId)
				.setParameter(EOSGroupUserEntity.PARAM_USER, userLogin).getResultList();
	}

	public void removeUsersFromGroup(Long tenantId, Long groupId, List<String> userLogins) {
		em.createNamedQuery(EOSGroupUserEntity.QUERY_REMOVE_USERS)
				.setParameter(EOSGroupUserEntity.PARAM_TENANT, tenantId)
				.setParameter(EOSGroupUserEntity.PARAM_GROUP, groupId)
				.setParameter(EOSGroupUserEntity.PARAM_USER, userLogins).executeUpdate();
	}

	public void removeUserFromGroups(Long tenantId, List<Long> groups, String userLogin) {
		em.createNamedQuery(EOSGroupUserEntity.QUERY_REMOVE_GROUPS)
				.setParameter(EOSGroupUserEntity.PARAM_TENANT, tenantId)
				.setParameter(EOSGroupUserEntity.PARAM_GROUP, groups)
				.setParameter(EOSGroupUserEntity.PARAM_USER, userLogin).executeUpdate();
	}

	public void deleteByUser(Long tenantId, String userLogin) {
		em.createNamedQuery(EOSGroupUserEntity.QUERY_DELETE_BY_USER)
				.setParameter(EOSGroupUserEntity.PARAM_TENANT, tenantId)
				.setParameter(EOSGroupUserEntity.PARAM_USER, userLogin).executeUpdate();
	}

}
