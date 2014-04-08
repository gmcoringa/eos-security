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

/**
 * Role user relation entity.
 * 
 * @author santos.fabiano
 * 
 */
@Entity(name = "EOSRoleUser")
@Table(name = "tbroleuser", uniqueConstraints = { @UniqueConstraint(columnNames = { "rolecode", "userlogin", "tenantid" }) })
@NamedQueries({
		@NamedQuery(name = EOSRoleUserEntity.QUERY_FIND, query = "SELECT t FROM EOSRoleUser t "
				+ "WHERE t.userLogin = :login AND t.roleCode = :code AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSRoleUserEntity.QUERY_LIST_USERS, query = "SELECT t.userLogin FROM EOSRoleUser t "
				+ "WHERE t.roleCode = :code AND t.tenantId = :tenantId " + "ORDER BY t.userLogin ASC"),
		@NamedQuery(name = EOSRoleUserEntity.QUERY_LIST_ROLES, query = "SELECT t.roleCode FROM EOSRoleUser t "
				+ "WHERE t.userLogin = :login AND t.tenantId = :tenantId " + "ORDER BY t,roleCode ASC"),
		@NamedQuery(name = EOSRoleUserEntity.QUERY_DELETE_USERS, query = "DELETE FROM EOSRoleUser t "
				+ "WHERE t.roleCode = :code AND t.userLogin IN (:login) AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSRoleUserEntity.QUERY_DELETE_ROLES, query = "DELETE FROM EOSRoleUser t "
				+ "WHERE t.roleCode IN (:code) AND t.userLogin = :login AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSRoleUserEntity.QUERY_DELETE_BY_USER, query = "DELETE FROM EOSRoleUser t "
				+ "WHERE t.userLogin = :login AND t.tenantId = :tenantId")

})
public class EOSRoleUserEntity extends AbstractTenantEntity {

	private static final long serialVersionUID = 6787741943234171944L;

	public static final String QUERY_FIND = "EOSRoleUser.FindByUserAndRole";
	public static final String QUERY_LIST_USERS = "EOSRoleUser.ListUsersByURole";
	public static final String QUERY_LIST_ROLES = "EOSRoleUser.ListRolesByUUser";
	public static final String QUERY_DELETE_USERS = "EOSRoleUser.RemoveUsersFromRole";
	public static final String QUERY_DELETE_ROLES = "EOSRoleUser.RemoveRolesFromUser";
	public static final String QUERY_DELETE_BY_USER = "EOSRoleUser.DeleteByUser";

	public static final String PARAM_USER = "login";
	public static final String PARAM_ROLE = "code";

	@Column(name = "userlogin", nullable = false, updatable = false)
	private String userLogin;

	@Column(name = "rolecode", nullable = false, updatable = false)
	private String roleCode;

	/**
	 * Default constructor.
	 */
	public EOSRoleUserEntity() {
		super();
	}

	/**
	 * @return the userLogin
	 */
	public String getUserLogin() {
		return userLogin;
	}

	/**
	 * @param userLogin
	 *            the userLogin to set
	 */
	public EOSRoleUserEntity setUserLogin(String userLogin) {
		this.userLogin = userLogin;
		return this;
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
	public EOSRoleUserEntity setRoleCode(String roleCode) {
		this.roleCode = roleCode;
		return this;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((roleCode == null) ? 0 : roleCode.hashCode());
		result = prime * result + ((userLogin == null) ? 0 : userLogin.hashCode());
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
		EOSRoleUserEntity other = (EOSRoleUserEntity) obj;
		if (roleCode == null) {
			if (other.roleCode != null)
				return false;
		} else if (!roleCode.equals(other.roleCode))
			return false;
		if (userLogin == null) {
			if (other.userLogin != null)
				return false;
		} else if (!userLogin.equals(other.userLogin))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSRoleUserEntity [userLogin=" + userLogin + ", roleCode=" + roleCode + ", getTenantId()="
				+ getTenantId() + "]";
	}

}
