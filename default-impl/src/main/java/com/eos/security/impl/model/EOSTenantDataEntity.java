/**
 * 
 */
package com.eos.security.impl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import com.eos.commons.jpa.EntityFieldSizes;

/**
 * @author santos.fabiano
 * 
 */
@Entity(name = "EOSTenantData")
@Table(name = "tbtenantdata", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"datakey", "tenantid" }) })
@NamedQueries({
		@NamedQuery(name = EOSTenantDataEntity.QUERY_FIND, query = "SELECT t.value "
				+ "FROM EOSTenantData t WHERE t.tenantId = :tenantId AND t.key = :key "),
		@NamedQuery(name = EOSTenantDataEntity.QUERY_FIND_BY_KEYS, query = "SELECT t "
				+ "FROM EOSTenantData t WHERE t.tenantId = :tenantId AND t.key IN (:key) "),
		@NamedQuery(name = EOSTenantDataEntity.QUERY_LIST, query = "SELECT t "
				+ "FROM EOSTenantData t WHERE t.tenantId = :tenantId ORDER By t.key ASC "),
		@NamedQuery(name = EOSTenantDataEntity.QUERY_UPDATE, query = "UPDATE EOSTenantData t "
				+ "SET t.value = :value WHERE t.tenantId = :tenantId AND t.key = :key "),
		@NamedQuery(name = EOSTenantDataEntity.QUERY_DELETE_KEYS, query = "DELETE FROM EOSTenantData t "
				+ "WHERE t.tenantId = :tenantId AND t.key IN (:key) ") })
public class EOSTenantDataEntity extends AbstractTenantEntity {

	private static final long serialVersionUID = 5179827678841512823L;

	public static final String QUERY_FIND = "EOSTenantData.Find";
	public static final String QUERY_FIND_BY_KEYS = "EOSTenantData.FindByKeys";
	public static final String QUERY_LIST = "EOSTenantData.List";
	public static final String QUERY_UPDATE = "EOSTenantData.Update";
	public static final String QUERY_DELETE_KEYS = "EOSTenantData.DeleteByKeys";

	public static final String PARAM_KEY = "key";
	public static final String PARAM_VALUE = "value";

	@ManyToOne(optional = false)
	@JoinColumn(referencedColumnName = "id", name = "tenantid", insertable = false, nullable = false, updatable = false)
	private EOSTenantEntity tenant;

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.DATA_SMALL)
	@Column(name = "datakey", unique = false, nullable = false)
	private String key;

	@Size(min = EntityFieldSizes.MINIMUM, max = EntityFieldSizes.BUF_LARGE)
	@Column(name = "datavalue")
	private String value;

	/**
	 * Default constructor.
	 */
	public EOSTenantDataEntity() {
		super();
	}

	/**
	 * @return the tenant
	 */
	public EOSTenantEntity getTenant() {
		return tenant;
	}

	/**
	 * @param tenant
	 *            the tenant to set
	 */
	public void setTenant(EOSTenantEntity tenant) {
		this.tenant = tenant;

		if (tenant == null) {
			setTenantId(null);
		} else {
			setTenantId(tenant.getId());
		}
	}

	/**
	 * @see AbstractTenantEntity#setTenantId(Long)
	 */
	@Override
	public void setTenantId(Long tenantId) {
		super.setTenantId(tenantId);
		tenant = new EOSTenantEntity(tenantId);
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		EOSTenantDataEntity other = (EOSTenantDataEntity) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSTenantDataEntity [key=" + key + ", value=" + value
				+ ", getTenantId()=" + getTenantId() + "]";
	}

}
