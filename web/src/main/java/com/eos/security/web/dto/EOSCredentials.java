/**
 * 
 */
package com.eos.security.web.dto;

import java.io.Serializable;

/**
 * @author santos.fabiano
 * 
 */
public class EOSCredentials implements Serializable {

	private static final long serialVersionUID = 3503202671155201724L;
	
	private String login;
	private String password;
	private String email;
	private Boolean keepConnected = Boolean.FALSE;

	/**
	 * Default constructor.
	 */
	public EOSCredentials() {
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
	public void setLogin(String login) {
		this.login = login;
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
	public void setPassword(String password) {
		this.password = password;
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
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the keepConnected
	 */
	public Boolean isKeepConnected() {
		return keepConnected;
	}

	/**
	 * @param keepConnected
	 *            the keepConnected to set
	 */
	public void setKeepConnected(Boolean keepConnected) {
		this.keepConnected = keepConnected;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSCredentials [login=" + login + ", password=" + password
				+ ", email=" + email + ", keepConnected=" + keepConnected + "]";
	}

}
