/**
 * 
 */
package com.eos.security.api.vo;

import java.io.Serializable;

/**
 * Object representing a Role.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSRole implements Serializable {

	private static final long serialVersionUID = -129495512001526149L;
	private String code;
	private String description;
	private Integer level;
	private Long tenantId;

	/**
	 * Default constructor.
	 */
	public EOSRole() {
		super();
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public EOSRole setCode(String code) {
		this.code = code;
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
	public EOSRole setDescription(String description) {
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
	public EOSRole setLevel(Integer level) {
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
	public EOSRole setTenantId(Long tenantId) {
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
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		EOSRole other = (EOSRole) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
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
		return "EOSRole [name=" + code + ", description=" + description
				+ ", level=" + level + ", tenantId=" + tenantId + "]";
	}

}
