/**
 * 
 */
package com.eos.security.api.vo;

import java.io.Serializable;

/**
 * Object representing a group.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSGroup implements Serializable {

	private static final long serialVersionUID = 446209266578497252L;
	private Long id;
	private String name;
	private String description;
	private Integer level;
	private Long tenantId;

	/**
	 * Default constructor.
	 */
	public EOSGroup() {
		super();
	}

	/**
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            the id to set
	 */
	public EOSGroup setId(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public EOSGroup setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public EOSGroup setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public EOSGroup setLevel(Integer level) {
		this.level = level;
		return this;
	}

	/**
	 * @return the tenantId
	 */
	public Long getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId
	 *            the tenantId to set
	 */
	public EOSGroup setTenantId(Long tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((tenantId == null) ? 0 : tenantId.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EOSGroup other = (EOSGroup) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tenantId == null) {
			if (other.tenantId != null)
				return false;
		} else if (!tenantId.equals(other.tenantId))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSGroup [id=" + id + ", name=" + name + ", description="
				+ description + ", level=" + level + ", tenantId=" + tenantId
				+ "]";
	}

}
