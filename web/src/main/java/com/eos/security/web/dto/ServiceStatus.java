/**
 * 
 */
package com.eos.security.web.dto;

import java.io.Serializable;

/**
 * Service status. Information about a service.
 * 
 * @author santos.fabiano
 * 
 */
public class ServiceStatus implements Serializable {

	private static final long serialVersionUID = -7255707976504658388L;
	private String service;
	private String version;
	private String status;

	/**
	 * Default constructor.
	 */
	public ServiceStatus() {
		super();
	}

	/**
	 * Constructor with fields.
	 * 
	 * @param service
	 * @param version
	 * @param status
	 */
	public ServiceStatus(String service, String version, String status) {
		this();
		this.service = service;
		this.version = version;
		this.status = status;
	}

	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public ServiceStatus setService(String service) {
		this.service = service;
		return this;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public ServiceStatus setVersion(String version) {
		this.version = version;
		return this;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public ServiceStatus setStatus(String status) {
		this.status = status;
		return this;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServiceStatus [service=" + service + ", version=" + version
				+ ", status=" + status + "]";
	}

}
