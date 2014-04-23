/**
 * 
 */
package com.eos.security.impl.service.permissions;

import java.util.HashSet;
import java.util.Set;

/**
 * Permission mapper for default roles.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSPermission {

	private String role;
	private Set<String> permissions;

	public EOSPermission() {
		super();
		permissions = new HashSet<>(8);
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public EOSPermission setRole(String role) {
		this.role = role;
		return this;
	}

	/**
	 * @return the permissions
	 */
	public Set<String> getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	public EOSPermission setPermissions(Set<String> permissions) {
		if (permissions == null || permissions.isEmpty()) {
			this.permissions.clear();
		} else {
			this.permissions = permissions;
		}

		return this;
	}
}
