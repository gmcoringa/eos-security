/**
 * 
 */
package com.eos.security.api.session;

import java.io.Serializable;

import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;

/**
 * Session context information.
 * 
 * @author fabiano.santos
 * 
 */
public final class SessionContext implements Serializable {

	private static final long serialVersionUID = 4026095071319340697L;

	private final EOSTenant tenant;
	private final EOSUser user;

	/**
	 * Default constructor.
	 * 
	 * @param tenant
	 * @param user
	 */
	public SessionContext(EOSTenant tenant, EOSUser user) {
		super();
		this.tenant = tenant;
		this.user = user;
	}

	/**
	 * @return the tenant
	 */
	public EOSTenant getTenant() {
		return tenant;
	}

	/**
	 * @return the user
	 */
	public EOSUser getUser() {
		return user;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tenant == null) ? 0 : tenant.hashCode());
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SessionContext other = (SessionContext) obj;
		if (tenant == null) {
			if (other.tenant != null)
				return false;
		} else if (!tenant.equals(other.tenant))
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
		return "SessionContext [tenant=" + tenant + ", user=" + user + "]";
	}

}
