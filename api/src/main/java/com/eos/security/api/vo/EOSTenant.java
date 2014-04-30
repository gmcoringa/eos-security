/**
 * 
 */
package com.eos.security.api.vo;

import java.io.Serializable;

import com.eos.common.EOSState;

/**
 * Object representing a Tenant.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSTenant implements Serializable {

	private static final long serialVersionUID = -8523729550013969250L;
	private String alias;
	private String name;
	private String description;
	private EOSState state;

	/**
	 * Default constructor.
	 */
	public EOSTenant() {
		super();
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public EOSTenant setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public EOSTenant setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public EOSTenant setDescription(String description) {
		this.description = description;
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
	public EOSTenant setState(EOSState state) {
		this.state = state;
		return this;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public EOSTenant setState(String state) {
		this.state = EOSState.valueOf(state);
		return this;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
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
		EOSTenant other = (EOSTenant) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSTenant [alias=" + alias + ", name=" + name + ", description=" + description + ", state=" + state
				+ "]";
	}

}
