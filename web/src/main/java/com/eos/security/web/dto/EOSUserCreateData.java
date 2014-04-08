/**
 * 
 */
package com.eos.security.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eos.security.api.vo.EOSUser;

/**
 * @author santos.fabiano
 * 
 */
public class EOSUserCreateData implements Serializable {

	private static final long serialVersionUID = -6146379812194050268L;
	private EOSUser user;
	private Map<String, String> userData;
	private List<Long> groups;
	private List<String> roles;

	/**
	 * Default constructor.
	 */
	public EOSUserCreateData() {
		super();
		userData = new HashMap<>(4);
		groups = new ArrayList<>(5);
		roles = new ArrayList<>(5);
	}

	/**
	 * @return the user
	 */
	public EOSUser getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public EOSUserCreateData setUser(EOSUser user) {
		this.user = user;
		return this;
	}

	/**
	 * @return the userData
	 */
	public Map<String, String> getUserData() {
		return userData;
	}

	/**
	 * @param userData
	 *            the userData to set
	 */
	public EOSUserCreateData setUserData(Map<String, String> userData) {
		if (userData == null || userData.isEmpty()) {
			this.userData.clear();
		} else {
			this.userData = userData;
		}

		return this;
	}

	/**
	 * @return the groups
	 */
	public List<Long> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public EOSUserCreateData setGroups(List<Long> groups) {
		if (groups == null || groups.isEmpty()) {
			this.groups.clear();
		} else {
			this.groups = groups;
		}

		return this;
	}

	/**
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public EOSUserCreateData setRoles(List<String> roles) {
		if (roles == null || roles.isEmpty()) {
			this.roles.clear();
		} else {
			this.roles = roles;
		}

		return this;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSUserCreateData [user=" + user + ", userData=" + userData
				+ ", groups=" + groups + ", roles=" + roles + "]";
	}

}
