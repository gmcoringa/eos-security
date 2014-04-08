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
 * @author santos.fabiano
 * 
 */
@Entity(name = "EOSUserTenantData")
@Table(name = "tbusertenantdata", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"datakey", "login", "tenantid" }) })
@NamedQueries({
		@NamedQuery(name = EOSUserTenantDataEntity.QUERY_FIND, query = "SELECT d.value FROM EOSUserTenantData d "
				+ "WHERE d.login = :login AND d.tenantId = :tenantId AND d.key = :key"),
		@NamedQuery(name = EOSUserTenantDataEntity.QUERY_LIST, query = "SELECT d FROM EOSUserTenantData d "
				+ "WHERE d.login = :login AND d.tenantId = :tenantId ORDER BY d.key ASC"),
		@NamedQuery(name = EOSUserTenantDataEntity.QUERY_BY_KEY, query = "SELECT d FROM EOSUserTenantData d "
				+ "WHERE d.login = :login AND d.tenantId = :tenantId AND d.key IN (:key) "
				+ "ORDER BY d.key ASC"),
		@NamedQuery(name = EOSUserTenantDataEntity.QUERY_DELETE_BY_USER, query = "DELETE FROM EOSUserTenantData "
				+ "WHERE login = :login AND tenantId = :tenantId"),
		@NamedQuery(name = EOSUserTenantDataEntity.QUERY_DELETE_BY_KEY, query = "DELETE FROM EOSUserTenantData d "
				+ "WHERE d.login = :login AND d.tenantId = :tenantId AND d.key IN (:key)"),
		@NamedQuery(name = EOSUserTenantDataEntity.QUERY_UPDATE, query = "UPDATE EOSUserTenantData d "
				+ "SET value = :value WHERE d.login = :login AND d.tenantId = :tenantId "
				+ "AND d.key = :key") })
public class EOSUserTenantDataEntity extends AbstractTenantEntity {

	private static final long serialVersionUID = -6904337420530528582L;

	public static final String QUERY_FIND = "EOSUserTenantData.FindByKey";
	public static final String QUERY_LIST = "EOSUserTenantData.ListAll";
	public static final String QUERY_BY_KEY = "EOSUserTenantData.ListByKey";
	public static final String QUERY_DELETE_BY_USER = "EOSUserTenantData.DeleteByUser";
	public static final String QUERY_DELETE_BY_KEY = "EOSUserTenantData.DeleteByKeys";
	public static final String QUERY_UPDATE = "EOSUserTenantData.UpdateUserData";

	public static final String PARAM_LOGIN = "login";
	public static final String PARAM_KEY = "key";
	public static final String PARAM_VALUE = "value";

	@Column(name = "login", insertable = true, nullable = false, updatable = false)
	private String login;

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.DATA_SMALL)
	@Column(name = "datakey", unique = false, nullable = false)
	private String key;

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.BUF_LARGE)
	@Column(name = "datavalue")
	private String value;

	/**
	 * Default constructor.
	 */
	public EOSUserTenantDataEntity() {
		super();
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
	public EOSUserTenantDataEntity setLogin(String login) {
		this.login = login;
		return this;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public EOSUserTenantDataEntity setKey(String key) {
		this.key = key;
		return this;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public EOSUserTenantDataEntity setValue(String value) {
		this.value = value;
		return this;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		EOSUserTenantDataEntity other = (EOSUserTenantDataEntity) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSUserTenantDataEntity [login=" + login + ", key=" + key
				+ ", value=" + value + ", tenantId=" + getTenantId() + "]";
	}

}
