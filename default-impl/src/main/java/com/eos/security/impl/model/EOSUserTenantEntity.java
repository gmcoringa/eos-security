/**
 * 
 */
package com.eos.security.impl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import com.eos.common.EOSState;
import com.eos.commons.jpa.EntityFieldSizes;

/**
 * User tenant entity.
 * 
 * @author santos.fabiano
 * 
 */
@Entity(name = "EOSUserTenant")
@Table(name = "tbusertenant", uniqueConstraints = { @UniqueConstraint(columnNames = { "login", "tenantid" }) })
@NamedQueries({
		@NamedQuery(name = EOSUserTenantEntity.QUERY_FIND, query = "SELECT t FROM EOSUserTenant t "
				+ "WHERE t.login = :login AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSUserTenantEntity.QUERY_FIND_MULTIPLE, query = "SELECT t FROM EOSUserTenant t "
				+ "WHERE t.login IN (:login) AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSUserTenantEntity.QUERY_FIND_BY_EMAIL, query = "SELECT t FROM EOSUserTenant t "
				+ "WHERE t.tenantMail = :email AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSUserTenantEntity.QUERY_LIST, query = "SELECT t FROM EOSUserTenant t "
				+ "WHERE t.state IN (:state) AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSUserTenantEntity.QUERY_UPDATE_STATE, query = "UPDATE EOSUserTenant t "
				+ "SET t.state = :state WHERE t.login = :login AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSUserTenantEntity.QUERY_DELETE_USER, query = "DELETE FROM EOSUserTenant t "
				+ "WHERE t.login = :login AND t.tenantId = :tenantId"),
		@NamedQuery(name = EOSUserTenantEntity.QUERY_COUNT_BY_LOGIN, query = "SELECT count(login) FROM EOSUserTenant t "
				+ "WHERE t.login = :login")

})
public class EOSUserTenantEntity extends AbstractTenantEntity {

	private static final long serialVersionUID = 3877045991804448137L;

	public static final String QUERY_FIND = "EOSUserTenant.FindByLogin";
	public static final String QUERY_FIND_MULTIPLE = "EOSUserTenant.FindUsersByLogin";
	public static final String QUERY_FIND_BY_EMAIL = "EOSUserTenant.FindByEMail";
	public static final String QUERY_LIST = "EOSUserTenant.ListAll";
	public static final String QUERY_UPDATE_STATE = "EOSUserTenant.UpdateState";
	public static final String QUERY_DELETE_USER = "EOSUserTenant.DeleteUser";
	public static final String QUERY_COUNT_BY_LOGIN = "EOSUserTenant.CountByLogin";

	public static final String PARAM_LOGIN = "login";
	public static final String PARAM_STATE = "state";
	public static final String PARAM_EMAIL = "email";

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.DATA_TINY)
	@Column(name = "login", nullable = false, updatable = false)
	private String login;

	@ManyToOne(optional = false)
	@JoinColumn(name = "login", referencedColumnName = "login", insertable = false, nullable = false, updatable = false)
	private EOSUserEntity user;

	@Size(max = EntityFieldSizes.DATA_TINY)
	@Column(name = "nickname", nullable = true)
	private String nickName;

	@Size(max = EntityFieldSizes.DATA_SMALL)
	@Column(name = "tenantmail", nullable = true)
	private String tenantMail;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "userstate", nullable = false)
	private EOSState state;

	/**
	 * Default constructor.
	 */
	public EOSUserTenantEntity() {
		super();
		state = EOSState.INACTIVE;
	}

	/**
	 * Constructor with id.
	 * 
	 * @param id
	 *            The user tenant id.
	 */
	public EOSUserTenantEntity(Long id) {
		super();
		setId(id);
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public EOSUserTenantEntity setLogin(String login) {
		this.login = login;
		user = new EOSUserEntity(login);
		return this;
	}

	/**
	 * @return the user
	 */
	public EOSUserEntity getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public EOSUserTenantEntity setUser(EOSUserEntity user) {
		this.user = user;

		if (user != null) {
			login = user.getLogin();
		} else {
			login = null;
		}
		return this;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName
	 *            the nickName to set
	 */
	public EOSUserTenantEntity setNickName(String nickName) {
		this.nickName = nickName;
		return this;
	}

	/**
	 * @return the tenantMail
	 */
	public String getTenantMail() {
		return tenantMail;
	}

	/**
	 * @param tenantMail
	 *            the tenantMail to set
	 */
	public EOSUserTenantEntity setTenantMail(String tenantMail) {
		this.tenantMail = tenantMail;
		return this;
	}

	/**
	 * @return the state
	 */
	public EOSState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public EOSUserTenantEntity setState(EOSState state) {
		this.state = state;
		return this;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		EOSUserTenantEntity other = (EOSUserTenantEntity) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSUserTenantEntity [login=" + login + ", user=" + user + ", nickName=" + nickName + ", tenantMail="
				+ tenantMail + ", getId()=" + getId() + ", state=" + state + "]";
	}

}
