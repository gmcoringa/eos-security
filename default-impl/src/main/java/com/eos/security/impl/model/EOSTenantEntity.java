/**
 * 
 */
package com.eos.security.impl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.eos.common.EOSState;
import com.eos.commons.jpa.AbstractEntity;
import com.eos.commons.jpa.EntityFieldSizes;

/**
 * @author santos.fabiano
 * 
 */
@Entity(name = "EOSTenant")
@Table(name = "tbtenant")
@NamedQueries({
		@NamedQuery(name = EOSTenantEntity.QUERY_LIST, query = EOSTenantEntity.BASE_QUERY
				+ "WHERE t.state IN (:state) ORDER BY t.name ASC "),
		@NamedQuery(name = EOSTenantEntity.QUERY_LIST_BY_IDS, query = EOSTenantEntity.BASE_QUERY
				+ "WHERE t.id IN (:id) ORDER BY t.name ASC "),
		@NamedQuery(name = EOSTenantEntity.QUERY_PURGE, query = "DELETE FROM EOSTenant t "
				+ "WHERE t.id = :id ") })
public class EOSTenantEntity extends AbstractEntity {

	private static final long serialVersionUID = 5605873490812706289L;

	protected static final String BASE_QUERY = "SELECT t FROM EOSTenant t ";
	public static final String QUERY_LIST = "EOSTenant.List";
	public static final String QUERY_LIST_BY_IDS = "EOSTenant.ListByIds";
	public static final String QUERY_PURGE = "EOSTenant.Purge";

	public static final String PARAM_STATE = "state";
	public static final String PARAM_ID = "id";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.DATA_SMALL)
	@Column(name = "name", nullable = false)
	private String name;

	@Size(max = EntityFieldSizes.DATA_LARGE)
	@Column(name = "description", nullable = true)
	private String description;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "state", nullable = false)
	private EOSState state;

	/**
	 * Default constructor.
	 */
	public EOSTenantEntity() {
		super();
		state = EOSState.INACTIVE;
	}

	/**
	 * Constructor with id.
	 * 
	 * @param id
	 *            Entity id.
	 */
	public EOSTenantEntity(Long id) {
		this();
		setId(id);
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
	public EOSTenantEntity setId(Long id) {
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
	public EOSTenantEntity setName(String name) {
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
	public EOSTenantEntity setDescription(String description) {
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
	public EOSTenantEntity setState(EOSState state) {
		this.state = state;
		return this;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		EOSTenantEntity other = (EOSTenantEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (state != other.state)
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSTenantEntity [id=" + id + ", name=" + name
				+ ", description=" + description + ", state=" + state
				+ ", getCreateDate()=" + getCreateDate() + ", getUpdateDate()="
				+ getUpdateDate() + "]";
	}

}
