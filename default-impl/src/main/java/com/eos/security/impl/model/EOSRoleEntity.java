/**
 * 
 */
package com.eos.security.impl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import com.eos.commons.jpa.EntityFieldSizes;

/**
 * Role entity.
 * 
 * @author santos.fabiano
 * 
 */
@Entity(name = "EOSRole")
@Table(name = "tbrole", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"code", "tenantid" }) })
@NamedQueries({
		@NamedQuery(name = EOSRoleEntity.QUERY_FIND, query = "SELECT t FROM EOSRole t "
				+ "WHERE t.code  = :code AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSRoleEntity.QUERY_FIND_MULTIPLE, query = "SELECT t FROM EOSRole t "
				+ "WHERE t.code  IN (:code) AND t.tenantId = :tenantId "
				+ "ORDER BY t.code "),
		@NamedQuery(name = EOSRoleEntity.QUERY_DELETE, query = "DELETE FROM EOSRole t "
				+ "WHERE t.code  = :code AND t.tenantId = :tenantId "),
		@NamedQuery(name = EOSRoleEntity.QUERY_LIST, query = "SELECT t FROM EOSRole t "
				+ "WHERE t.level >= :minLevel AND t.level <= :maxLevel "
				+ "AND t.tenantId = :tenantId ORDER BY t.code "),

})
public class EOSRoleEntity extends AbstractTenantEntity {

	private static final long serialVersionUID = -8299463092938779236L;

	public static final String QUERY_FIND = "EOSRole.FindByCode";
	public static final String QUERY_FIND_MULTIPLE = "EOSRole.FindByCodes";
	public static final String QUERY_DELETE = "EOSRole.DeleteByCode";
	public static final String QUERY_LIST = "EOSRole.ListAll";

	public static final String PARAM_CODE = "code";
	public static final String PARAM_MIN_LEVEL = "minLevel";
	public static final String PARAM_MAX_LEVEL = "maxLevel";

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.DATA_TINY)
	@Column(name = "code", nullable = false, updatable = false)
	private String code;

	@Size(max = EntityFieldSizes.DATA_SMALL)
	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "rolelevel", nullable = false)
	private Integer level;

	/**
	 * Default constructor.
	 */
	public EOSRoleEntity() {
		super();
	}

	/**
	 * Constructor with id.
	 * 
	 * @param id
	 *            The role ID.
	 */
	public EOSRoleEntity(Long id) {
		super();
		setId(id);
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
	public EOSRoleEntity setCode(String code) {
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
	public EOSRoleEntity setDescription(String description) {
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
	public EOSRoleEntity setLevel(Integer level) {
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
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
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
		EOSRoleEntity other = (EOSRoleEntity) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSRoleEntity [code=" + code + ", description=" + description
				+ ", level=" + level + ", getId()=" + getId()
				+ ", getTenantId()=" + getTenantId() + "]";
	}

}
