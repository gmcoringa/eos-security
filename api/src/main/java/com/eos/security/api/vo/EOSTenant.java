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
	private Long id;
	private String name;
	private String description;
	private EOSState state;
	private EOSImagePaths logos;

	/**
	 * Default constructor.
	 */
	public EOSTenant() {
		super();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public EOSTenant setId(Long id) {
		this.id = id;
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
	 * @return the logos
	 */
	public EOSImagePaths getLogos() {
		return logos;
	}

	/**
	 * @param logos
	 *            the logos to set
	 */
	public EOSTenant setLogos(EOSImagePaths logos) {
		this.logos = logos;
		return this;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSTenant [id=" + id + ", name=" + name + ", description="
				+ description + ", state=" + state + "]";
	}

}
