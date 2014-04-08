/**
 * 
 */
package com.eos.security.impl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.eos.commons.jpa.EntityFieldSizes;

/**
 * Group entity model.
 * 
 * @author santos.fabiano
 * 
 */
@Entity(name = "EOSGroup")
@Table(name = "tbgroup")
@NamedQueries({
		@NamedQuery(name = EOSGroupEntity.QUERY_FIND, query = "SELECT t FROM EOSGroup t "
				+ "WHERE t.id = :id AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSGroupEntity.QUERY_FIND_MULTIPLE, query = "SELECT t FROM EOSGroup t "
				+ "WHERE t.id IN (:id) AND t.tenantId = :tenantId "
				+ "ORDER BY t.name ASC"),
		@NamedQuery(name = EOSGroupEntity.QUERY_LIST, query = "SELECT t FROM EOSGroup t "
				+ "WHERE t.level >= :minLevel AND t.level <= :maxLevel AND t.tenantId = :tenantId "
				+ "ORDER BY t.name ASC") })
public class EOSGroupEntity extends AbstractTenantEntity {

	private static final long serialVersionUID = 8891336936121623535L;

	public static final String QUERY_FIND = "EOSGroup.Find";
	public static final String QUERY_FIND_MULTIPLE = "EOSGroup.FindMultiple";
	public static final String QUERY_LIST = "EOSGroup.ListByLevel";

	public static final String PARAM_MIN_LEVEL = "minLevel";
	public static final String PARAM_MAX_LEVEL = "maxLevel";

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.DATA_TINY)
	@Column(name = "name", nullable = false, updatable = true)
	private String name;

	@Size(max = EntityFieldSizes.DATA_STANDART)
	@Column(name = "description", nullable = true, updatable = true)
	private String description;

	@Column(name = "grouplevel", nullable = false, updatable = true)
	private Integer level;

	/**
	 * Default constructor.
	 */
	public EOSGroupEntity() {
		super();
	}

	/**
	 * Constructor with ID.
	 * 
	 * @param id
	 *            Group ID.
	 */
	public EOSGroupEntity(Long id) {
		super();
		setId(id);
	}

	/**
	 * Retrieve the group name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets group name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public EOSGroupEntity setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Retrieve group description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets group description
	 * 
	 * @param description
	 *            the description to set
	 */
	public EOSGroupEntity setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Retrieve group level.
	 * 
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * Sets group level.
	 * 
	 * @param level
	 *            the level to set
	 */
	public EOSGroupEntity setLevel(Integer level) {
		this.level = level;
		return this;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EOSGroupEntity other = (EOSGroupEntity) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSGroupEntity [name=" + name + ", level=" + level
				+ ", getId()=" + getId() + ", getTenantId()=" + getTenantId()
				+ "]";
	}

}
