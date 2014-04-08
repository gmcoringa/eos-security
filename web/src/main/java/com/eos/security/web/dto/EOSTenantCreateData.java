/**
 * 
 */
package com.eos.security.web.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;

/**
 * @author santos.fabiano
 * 
 */
public class EOSTenantCreateData implements Serializable {

	private static final long serialVersionUID = -1429913793666655180L;
	private EOSTenant tenant;
	private EOSUser adminUser;
	private Map<String, String> data;

	/**
	 * Default constructor.
	 */
	public EOSTenantCreateData() {
		data = new HashMap<>();
	}

	/**
	 * @return the tenant
	 */
	public EOSTenant getTenant() {
		return tenant;
	}

	/**
	 * @param tenant
	 *            the tenant to set
	 */
	public EOSTenantCreateData setTenant(EOSTenant tenant) {
		this.tenant = tenant;
		return this;
	}

	/**
	 * @return the adminUser
	 */
	public EOSUser getAdminUser() {
		return adminUser;
	}

	/**
	 * @param adminUser
	 *            the adminUser to set
	 */
	public EOSTenantCreateData setAdminUser(EOSUser adminUser) {
		this.adminUser = adminUser;
		return this;
	}

	/**
	 * @return the data
	 */
	public Map<String, String> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public EOSTenantCreateData setData(Map<String, String> data) {
		if (data == null) {
			this.data.clear();
		} else {

			this.data = data;
		}
		return this;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TenantCreateData [tenant=" + tenant + ", adminUser="
				+ adminUser + ", data=" + data + "]";
	}

}
