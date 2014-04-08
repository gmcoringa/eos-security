/**
 * 
 */
package com.eos.security.impl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.eos.common.EOSUserType;
import com.eos.commons.jpa.AbstractEntity;
import com.eos.commons.jpa.EntityFieldSizes;

/**
 * Global user entity, visible to all tenants.
 * 
 * @author santos.fabiano
 * 
 */
@Entity(name = "EOSUser")
@Table(name = "tbuser")
@NamedQueries({
		@NamedQuery(name = EOSUserEntity.QUERY_FIND_BY_EMAIL, query = "SELECT t FROM EOSUser t "
				+ "WHERE t.email = :email"),
		@NamedQuery(name = EOSUserEntity.QUERY_DELETE, query = "DELETE FROM EOSUser t " + "WHERE t.login = :login")

})
public class EOSUserEntity extends AbstractEntity {

	private static final long serialVersionUID = -7976368002799372189L;

	public static final String QUERY_FIND_BY_EMAIL = "EOSUser.FindByEMail";
	public static final String QUERY_DELETE = "EOSUser.DeleteByLogin";

	public static final String PARAM_LOGIN = "login";
	public static final String PARAM_EMAIL = "email";

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.DATA_TINY)
	@Id
	@Column(name = "login", updatable = false, nullable = false)
	private String login;

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.DATA_SMALL)
	@Column(name = "email", unique = true, updatable = true, nullable = false)
	private String email;

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.DATA_SMALL)
	@Column(name = "firstname", nullable = false)
	private String firstName;

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.DATA_SMALL)
	@Column(name = "lastname", nullable = false)
	private String lastName;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "usertype", nullable = false)
	private EOSUserType type;

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.DATA_TINY)
	@Column(name = "password", nullable = true)
	private String password;

	/**
	 * Default constructor.
	 */
	public EOSUserEntity() {
		super();
		type = EOSUserType.USER;
	}

	/**
	 * Constructor with login.
	 * 
	 * @param login
	 */
	public EOSUserEntity(String login) {
		this();
		setLogin(login);
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
	public EOSUserEntity setLogin(String login) {
		this.login = login;
		return this;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public EOSUserEntity setEmail(String email) {
		this.email = email;
		return this;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public EOSUserEntity setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public EOSUserEntity setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	/**
	 * @return the type
	 */
	public EOSUserType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public EOSUserEntity setType(EOSUserType type) {
		this.type = type;
		return this;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public EOSUserEntity setPassword(String password) {
		this.password = password;
		return this;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
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
		EOSUserEntity other = (EOSUserEntity) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSUserEntity [login=" + login + ", email=" + email + ", firstName=" + firstName + ", lastName="
				+ lastName + ", type=" + type + "]";
	}

}
