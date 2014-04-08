/**
 * 
 */
package com.eos.commons.jpa;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import com.eos.common.exception.EOSNotFoundException;

/**
 * @author santos.fabiano
 * 
 */
public abstract class AbstractDAO<T> {

	private Class<T> entityClazz;

	/**
	 * Default constructor.
	 * 
	 * @param entityClazz
	 */
	protected AbstractDAO(Class<T> entityClazz) {
		this.entityClazz = entityClazz;
	}

	/**
	 * Find the entity referenced by this DAO with the given id.
	 * 
	 * @param id
	 *            the entity id (PK)
	 * @return entity
	 * @throws EOSNotFoundException
	 *             If not found.
	 */
	public T find(Object id) throws EOSNotFoundException {
		T entity = null;
		try {
			entity = getEntityManager().find(entityClazz, id);
		} catch (PersistenceException e) {
			throw new EOSNotFoundException("No entity found with id: " + id);
		}
		// Still null, not found
		if (entity == null) {
			throw new EOSNotFoundException("No entity found with id: " + id);
		}

		return entity;
	}

	/**
	 * Find the entity referenced by this DAO with the given id. Returns null if
	 * not found.
	 * 
	 * @param id
	 *            the entity id (PK)
	 * @return Entity or null if not found.
	 */
	public T checkedFind(Object id) {
		try {
			return find(id);
		} catch (EOSNotFoundException e) {
			return null;
		}
	}

	/**
	 * Get the managed reference of the entity with the given id.
	 * 
	 * @param id
	 *            the entity id (PK)
	 * @return Entity reference.
	 * @throws PersistenceException
	 *             If not found.
	 */
	public T getReference(Object id) throws PersistenceException {
		return getEntityManager().getReference(entityClazz, id);
	}

	/**
	 * Get the managed reference of the entity with the given id. Returns null
	 * if not found.
	 * 
	 * @param id
	 *            the entity id (PK)
	 * @return Entity reference or null if not found.
	 */
	public T getCheckedReference(Object id) {
		try {
			return getReference(id);
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Persist an entity, creating a new one.
	 * 
	 * @param entity
	 */
	public void persist(T entity) {
		getEntityManager().persist(entity);
	}

	/**
	 * Updates the given entity.
	 * 
	 * @param entity
	 * @return entity
	 */
	public T merge(T entity) {
		return getEntityManager().merge(entity);
	}

	/**
	 * Deletes the given entity.
	 * 
	 * @param entity
	 */
	public void remove(T entity) {
		getEntityManager().remove(entity);
	}

	/**
	 * Get the entity manager.
	 * 
	 * @return Entity manager
	 */
	public abstract EntityManager getEntityManager();
}
