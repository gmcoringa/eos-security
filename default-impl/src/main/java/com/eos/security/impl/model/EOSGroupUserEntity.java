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

/**
 * Group User entity model.
 * 
 * @author fabiano.vicente
 * 
 */
@Entity(name = "EOSGroupUser")
@Table(name = "tbgroupuser")
@NamedQueries({
		@NamedQuery(name = EOSGroupUserEntity.QUERY_GROUP_USERS, query = "SELECT t.userLogin FROM EOSGroupUser t "
				+ "WHERE t.tenantId = :tenantId AND t.groupId = :groupId ORDER BY t.userLogin "),
		@NamedQuery(name = EOSGroupUserEntity.QUERY_USER_GROUPS, query = "SELECT t.groupId FROM EOSGroupUser t "
				+ "WHERE t.tenantId = :tenantId AND t.userLogin = :login ORDER BY t.groupId "),
		@NamedQuery(name = EOSGroupUserEntity.QUERY_REMOVE_USERS, query = "DELETE FROM EOSGroupUser t "
				+ "WHERE t.tenantId = :tenantId AND t.groupId = :groupId AND t.userLogin IN (:login) "),
		@NamedQuery(name = EOSGroupUserEntity.QUERY_REMOVE_GROUPS, query = "DELETE FROM EOSGroupUser t "
				+ "WHERE t.tenantId = :tenantId AND t.groupId IN (:groupId) AND t.userLogin = :login "),
		@NamedQuery(name = EOSGroupUserEntity.QUERY_DELETE_BY_USER, query = "DELETE FROM EOSGroupUser t "
				+ "WHERE t.tenantId = :tenantId AND t.userLogin = :login ")

})
public class EOSGroupUserEntity extends AbstractTenantEntity {

	private static final long serialVersionUID = 7626578005288178045L;

	public static final String QUERY_GROUP_USERS = "EOSGroupUser.ListUsersByGroup";
	public static final String QUERY_USER_GROUPS = "EOSGroupUser.ListGroupsByUser";
	public static final String QUERY_REMOVE_USERS = "EOSGroupUser.ReomveUsersFromGroup";
	public static final String QUERY_REMOVE_GROUPS = "EOSGroupUser.ReomveUserFromGroups";
	public static final String QUERY_DELETE_BY_USER = "EOSGroupUser.DeleteByUser";

	public static final String PARAM_USER = "login";
	public static final String PARAM_GROUP = "groupId";

	@Column(name = "groupid", insertable = true, nullable = false, updatable = false)
	private Long groupId;

	@ManyToOne(optional = false)
	@JoinColumn(referencedColumnName = "id", name = "groupid", insertable = false, nullable = false, updatable = false)
	private EOSGroupEntity group;

	@Column(name = "userlogin", insertable = true, nullable = false, updatable = false)
	private String userLogin;

	/**
	 * Default constructor.
	 */
	public EOSGroupUserEntity() {
		super();
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
	public EOSGroupUserEntity setGroupId(Long groupId) {
		this.groupId = groupId;
		if (groupId == null) {
			group = null;
		} else {
			group = new EOSGroupEntity(groupId);
		}

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
	public EOSGroupUserEntity setGroup(EOSGroupEntity group) {
		this.group = group;

		if (group == null) {
			groupId = null;
		} else {
			groupId = group.getId();
		}

		return this;
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
	public EOSGroupUserEntity setUserLogin(String userLogin) {
		this.userLogin = userLogin;
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
		EOSGroupUserEntity other = (EOSGroupUserEntity) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
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
		return "EOSGroupUserEntity [groupId=" + groupId + ", userLogin=" + userLogin + "]";
	}

}
