/**
 * 
 */
package com.eos.commons.jpa;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Default entity listener.
 * 
 * @author santos.fabiano
 * 
 */
public class EntityListener {

	@PrePersist
	public void prePersist(Object object) {

		if (!AbstractEntity.class.isAssignableFrom(object.getClass())) {
			return;
		}

		AbstractEntity entity = (AbstractEntity) object;
		Date date = Calendar.getInstance().getTime();
		entity.setCreateDate(date);
		entity.setUpdateDate(date);
	}

	@PreUpdate
	public void preUpdate(Object object) {

		if (!AbstractEntity.class.isAssignableFrom(object.getClass())) {
			return;
		}

		AbstractEntity entity = (AbstractEntity) object;
		entity.setUpdateDate(Calendar.getInstance().getTime());
	}

}
