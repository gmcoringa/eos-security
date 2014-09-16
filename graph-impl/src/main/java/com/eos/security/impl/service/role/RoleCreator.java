/**
 * 
 */
package com.eos.security.impl.service.role;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.eos.security.api.vo.EOSRole;

/**
 * @author santos.fabiano
 * 
 */
public class RoleCreator implements Serializable {

	private static final long serialVersionUID = 1492428555177415823L;

	private EOSRole role;
	private Set<String> permissions;

	/**
	 * Default constructor.
	 */
	public RoleCreator() {
		super();
		permissions = new HashSet<>(8);
	}

	/**
	 * @return the role
	 */
	public final EOSRole getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public final void setRole(EOSRole role) {
		this.role = role;
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
	public RoleCreator setPermissions(Set<String> permissions) {
		this.permissions = permissions;
		return this;
	}
}
