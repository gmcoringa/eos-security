/**
 * 
 */
package com.eos.security.impl.dao;

import org.springframework.stereotype.Repository;

/**
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSTenantDataDAO  {

//	public void updateTenantData(Long tenantId, String key, String value) {
//		em.createNamedQuery(EOSTenantDataEntity.QUERY_UPDATE)
//				.setParameter(EOSTenantDataEntity.PARAM_TENANT, tenantId)
//				.setParameter(EOSTenantDataEntity.PARAM_KEY, key)
//				.setParameter(EOSTenantDataEntity.PARAM_VALUE, value)
//				.executeUpdate();
//	}
//
//	public void deleteTenantData(Long tenantId, List<String> keys) {
//		em.createNamedQuery(EOSTenantDataEntity.QUERY_DELETE_KEYS)
//				.setParameter(EOSTenantDataEntity.PARAM_TENANT, tenantId)
//				.setParameter(EOSTenantDataEntity.PARAM_KEY, keys)
//				.executeUpdate();
//	}
//
//	public String findTenantDataValue(Long tenantId, String key) {
//		try {
//			return em
//					.createNamedQuery(EOSTenantDataEntity.QUERY_FIND,
//							String.class)
//					.setParameter(EOSTenantDataEntity.PARAM_TENANT, tenantId)
//					.setParameter(EOSTenantDataEntity.PARAM_KEY, key)
//					.getSingleResult();
//		} catch (PersistenceException e) {
//			return null;
//		}
//	}
//
//	public List<EOSTenantDataEntity> findTenantDataValues(Long tenantId,
//			List<String> keys) {
//		return em
//				.createNamedQuery(EOSTenantDataEntity.QUERY_FIND_BY_KEYS,
//						EOSTenantDataEntity.class)
//				.setParameter(EOSTenantDataEntity.PARAM_TENANT, tenantId)
//				.setParameter(EOSTenantDataEntity.PARAM_KEY, keys)
//				.getResultList();
//	}
//
//	public List<EOSTenantDataEntity> listTenantData(Long tenantId, int limit,
//			int offset) {
//		return em
//				.createNamedQuery(EOSTenantDataEntity.QUERY_LIST,
//						EOSTenantDataEntity.class)
//				.setParameter(EOSTenantDataEntity.PARAM_TENANT, tenantId)
//				.setFirstResult(offset).setMaxResults(limit).getResultList();
//	}
}
