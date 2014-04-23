/**
 * 
 */
package com.eos.security.impl.dao;

import org.springframework.stereotype.Repository;

/**
 * Group DAO.
 * 
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSGroupDAO  {

//	@PersistenceContext
//	private EntityManager em;
//
//	protected EOSGroupDAO() {
//		super(EOSGroupEntity.class);
//	}
//
//	/**
//	 * @see com.eos.commons.jpa.AbstractDAO#getEntityManager()
//	 */
//	@Override
//	public EntityManager getEntityManager() {
//		return em;
//	}
//
//	public EOSGroupEntity find(Long groupId, Long tenantId)
//			throws EOSNotFoundException {
//		try {
//			return em
//					.createNamedQuery(EOSGroupEntity.QUERY_FIND,
//							EOSGroupEntity.class)
//					.setParameter(EOSGroupEntity.PARAM_ID, groupId)
//					.setParameter(EOSGroupEntity.PARAM_TENANT, tenantId)
//					.getSingleResult();
//		} catch (NoResultException e) {
//			throw new EOSNotFoundException("Group not found for id:" + groupId
//					+ ", tenantId: " + tenantId, e);
//		}
//	}
//
//	public EOSGroupEntity checkedFind(Long groupId, Long tenantId) {
//		try {
//			return em
//					.createNamedQuery(EOSGroupEntity.QUERY_FIND,
//							EOSGroupEntity.class)
//					.setParameter(EOSGroupEntity.PARAM_ID, groupId)
//					.setParameter(EOSGroupEntity.PARAM_TENANT, tenantId)
//					.getSingleResult();
//		} catch (NoResultException e) {
//			return null;
//		}
//	}
//
//	public List<EOSGroupEntity> find(List<Long> groups, Long tenantId) {
//		return em
//				.createNamedQuery(EOSGroupEntity.QUERY_FIND_MULTIPLE,
//						EOSGroupEntity.class)
//				.setParameter(EOSGroupEntity.PARAM_ID, groups)
//				.setParameter(EOSGroupEntity.PARAM_TENANT, tenantId)
//				.getResultList();
//
//	}
//
//	public List<EOSGroupEntity> list(Integer minLevel, Integer maxLevel,
//			int limit, int offset, Long tenantId) {
//		return em
//				.createNamedQuery(EOSGroupEntity.QUERY_LIST,
//						EOSGroupEntity.class)
//				.setParameter(EOSGroupEntity.PARAM_MIN_LEVEL, minLevel)
//				.setParameter(EOSGroupEntity.PARAM_MAX_LEVEL, maxLevel)
//				.setParameter(EOSGroupEntity.PARAM_TENANT, tenantId)
//				.setFirstResult(offset).setMaxResults(limit).getResultList();
//
//	}
}
