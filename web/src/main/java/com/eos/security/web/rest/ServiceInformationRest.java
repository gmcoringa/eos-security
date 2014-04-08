/**
 * 
 */
package com.eos.security.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.eos.security.web.dto.ServiceStatus;

/**
 * Service information.
 * 
 * @author santos.fabiano
 * 
 */
@Path("/service")
@Component
public class ServiceInformationRest {

	private static final ServiceStatus status = new ServiceStatus(
			"EOS-Security", "0.0-1", "RUNNING");

	@GET
	@Path("/status")
	@Produces(MediaType.APPLICATION_JSON)
	public ServiceStatus getServiceStatus() {
		// TODO
		return status;
	}
}
