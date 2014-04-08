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
 * Role Permission entity model.
 * 
 * @author santos.fabiano
 * 
 */
@Entity(name = "EOSPermission")
@Table(name = "tbpermission", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"rolecode", "permission", "tenantid" }) })
@NamedQueries({
		@NamedQuery(name = EOSPermissionEntity.QUERY_LIST, query = "SELECT t.permission FROM EOSPermission t "
				+ "WHERE t.roleCode = :code AND t.tenantId = :tenantId ORDER BY t.permission ASC"),
		@NamedQuery(name = EOSPermissionEntity.QUERY_LIST_PERM, query = "SELECT t FROM EOSPermission t "
				+ "WHERE t.permission IN (:permission) AND t.tenantId = :tenantId "),
		@NamedQuery(name = EOSPermissionEntity.QUERY_REMOVE, query = "DELETE FROM EOSPermission t "
				+ "WHERE t.roleCode = :code AND t.permission IN (:permission) AND t.tenantId = :tenantId")

})
public class EOSPermissionEntity extends AbstractTenantEntity {

	private static final long serialVersionUID = -4228463366776986941L;

	public static final String QUERY_LIST = "EOSPermission.ListByRole";
	public static final String QUERY_LIST_PERM = "EOSPermission.ListByRoleAndPermission";
	public static final String QUERY_REMOVE = "EOSPermission.DeleteByRole";

	public static final String PARAM_ROLE = "code";
	public static final String PARAM_PERMISSION = "permission";

	@Column(name = "rolecode", nullable = false, updatable = false)
	private String roleCode;

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.DATA_TINY)
	@Column(name = "permission", nullable = false, updatable = false)
	private String permission;

	/**
	 * Default constructor.
	 */
	public EOSPermissionEntity() {
		super();
	}

	/**
	 * @return the roleCode
	 */
	public String getRoleCode() {
		return roleCode;
	}

	/**
	 * @param roleCode
	 *            the roleCode to set
	 */
	public EOSPermissionEntity setRoleCode(String roleCode) {
		this.roleCode = roleCode;
		return this;
	}

	/**
	 * @return the permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * @param permission
	 *            the permission to set
	 */
	public EOSPermissionEntity setPermission(String permission) {
		this.permission = permission;
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
				+ ((permission == null) ? 0 : permission.hashCode());
		result = prime * result
				+ ((roleCode == null) ? 0 : roleCode.hashCode());
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
		EOSPermissionEntity other = (EOSPermissionEntity) obj;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		if (roleCode == null) {
			if (other.roleCode != null)
				return false;
		} else if (!roleCode.equals(other.roleCode))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSPermissionEntity [roleCode=" + roleCode + ", permission="
				+ permission + ", getTenantId()=" + getTenantId() + "]";
	}

}
