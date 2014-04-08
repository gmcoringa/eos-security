/**
 * 
 */
package com.eos.security.impl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Role Group entity model.
 * 
 * @author santos.fabiano
 * 
 */
@Entity(name = "EOSRoleGroup")
@Table(name = "tbrolegroup", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"rolecode", "groupid", "tenantid" }) })
@NamedQueries({
		@NamedQuery(name = EOSRoleGroupEntity.QUERY_REMOVE_GROUPS, query = "DELETE FROM EOSRoleGroup t "
				+ "WHERE t.roleCode = :code AND t.groupId IN (:groupId) AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSRoleGroupEntity.QUERY_REMOVE_ROLES, query = "DELETE FROM EOSRoleGroup t "
				+ "WHERE t.groupId = :groupId AND t.roleCode IN (:code) AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSRoleGroupEntity.QUERY_LIST_GROUPS, query = "SELECT t.groupId From EOSRoleGroup t "
				+ "WHERE t.roleCode = :code AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSRoleGroupEntity.QUERY_LIST_ROLES, query = "SELECT t.roleCode FROM EOSRoleGroup t "
				+ "WHERE t.groupId = :groupId AND t.tenantId = :tenantId ORDER BY t.roleCode"),
		@NamedQuery(name = EOSRoleGroupEntity.QUERY_LIST_ROLE_GROUP, query = "SELECT t FROM EOSRoleGroup t "
				+ "WHERE t.groupId IN (:groupId) AND t.roleCode IN (:code) AND t.tenantId = :tenantId ")

})
public class EOSRoleGroupEntity extends AbstractTenantEntity {

	private static final long serialVersionUID = 4416227584848597730L;

	public static final String QUERY_REMOVE_GROUPS = "EOSRoleGroup.RemoveGroupsFromRole";
	public static final String QUERY_REMOVE_ROLES = "EOSRoleGroup.RemoveRoleFromGroups";
	public static final String QUERY_LIST_GROUPS = "EOSRoleGroup.ListGroupsFromRole";
	public static final String QUERY_LIST_ROLES = "EOSRoleGroup.ListRoleFromGroups";
	public static final String QUERY_LIST_ROLE_GROUP = "EOSRoleGroup.ListByRoleAndGroup";

	public static final String PARAM_ROLE = "code";
	public static final String PARAM_GROUP = "groupId";

	@Column(name = "rolecode", nullable = false, updatable = false)
	private String roleCode;

	@Column(name = "groupid", insertable = true, nullable = false, updatable = false)
	private Long groupId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "groupid", referencedColumnName = "id", insertable = false, nullable = false, updatable = false)
	private EOSGroupEntity group;

	/**
	 * Default constructor.
	 */
	public EOSRoleGroupEntity() {
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
	public EOSRoleGroupEntity setRoleCode(String roleCode) {
		this.roleCode = roleCode;
		return this;
	}

	/**
	 * @return the groupId
	 */
	public Long getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public EOSRoleGroupEntity setGroupId(Long groupId) {
		if (groupId == null) {
			group = null;
		} else {
			group = new EOSGroupEntity(groupId);
		}

		this.groupId = groupId;
		return this;
	}

	/**
	 * @return the group
	 */
	public EOSGroupEntity getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public EOSRoleGroupEntity setGroup(EOSGroupEntity group) {
		if (group == null) {
			groupId = null;
		} else {
			groupId = group.getId();
		}

		this.group = group;
		return this;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
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
		EOSRoleGroupEntity other = (EOSRoleGroupEntity) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
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
		return "EOSRoleGroupEntity [roleCode=" + roleCode + ", groupId="
				+ groupId + ", getTenantId()=" + getTenantId() + "]";
	}

}
