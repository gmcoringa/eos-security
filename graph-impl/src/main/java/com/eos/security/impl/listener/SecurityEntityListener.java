/**
 * 
 */
package com.eos.security.impl.listener;


/**
 * Listener to set tenant on all entities.
 * 
 * @author santos.fabiano
 * 
 */
public class SecurityEntityListener {

//	@PrePersist
//	public void prePersist(Object object) {
//		if (!AbstractTenantEntity.class.isAssignableFrom(object.getClass())) {
//			return;
//		}
//
//		AbstractTenantEntity entity = (AbstractTenantEntity) object;
//
//		if (entity.getTenantId() == null) {
//			entity.setTenantId(SessionContextManager.getCurrentSession()
//					.getTenant().getId());
//		}
//	}
}
