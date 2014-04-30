/**
 * 
 */
package com.eos.security.api.vo;

import java.io.Serializable;

import com.eos.common.EOSState;
import com.eos.common.EOSUserType;

/**
 * Object representing a User.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSUser implements Serializable {

	private static final long serialVersionUID = 8972392312592972574L;
	private String login;
	private String url;
	private String nickName;
	private String firstName;
	private String lastName;
	private String personalMail;
	private String email;
	private EOSState state;
	private EOSUserType type;
	private String tenantAlias;

	/**
	 * Default constructor.
	 */
	public EOSUser() {
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
	public EOSUser setLogin(String login) {
		this.login = login;
		return this;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public EOSUser setUrl(String url) {
		this.url = url;
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
	public EOSUser setNickName(String nickName) {
		this.nickName = nickName;
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
	public EOSUser setFirstName(String firstName) {
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
	public EOSUser setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	/**
	 * @return the personalMail
	 */
	public String getPersonalMail() {
		return personalMail;
	}

	/**
	 * @param personalMail
	 *            the personalMail to set
	 */
	public EOSUser setPersonalMail(String personalMail) {
		this.personalMail = personalMail;
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
	public EOSUser setEmail(String email) {
		this.email = email;
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
	public EOSUser setState(EOSState state) {
		this.state = state;
		return this;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public EOSUser setState(String state) {
		this.state = EOSState.valueOf(state);
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
	public EOSUser setType(EOSUserType type) {
		this.type = type;
		return this;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public EOSUser setType(String type) {
		this.type = EOSUserType.valueOf(type);
		return this;
	}

	/**
	 * @return the tenantAlias
	 */
	public String getTenantId() {
		return tenantAlias;
	}

	/**
	 * @param tenantAlias
	 *            the tenantAlias to set
	 */
	public EOSUser setTenantAlias(String tenantAlias) {
		this.tenantAlias = tenantAlias;
		return this;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((tenantAlias == null) ? 0 : tenantAlias.hashCode());
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
		EOSUser other = (EOSUser) obj;
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
		if (tenantAlias == null) {
			if (other.tenantAlias != null)
				return false;
		} else if (!tenantAlias.equals(other.tenantAlias))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSUser [login=" + login + ", url=" + url + ", nickName=" + nickName + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", personalMail=" + personalMail + ", email=" + email + ", state=" + state
				+ ", type=" + type + ", tenantAlias=" + tenantAlias + "]";
	}

}
