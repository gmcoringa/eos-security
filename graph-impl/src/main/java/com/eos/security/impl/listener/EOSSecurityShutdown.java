/**
 * 
 */
package com.eos.security.impl.listener;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.eos.security.impl.service.internal.DataBaseServer;

/**
 * @author fabiano.santos
 * 
 */
@Service
public class EOSSecurityShutdown {

	private static final Logger log = LoggerFactory.getLogger(EOSSecurityShutdown.class);

	@PreDestroy
	public void destroy() {
		log.info("### Shutingdown EOS-Security ###");
		DataBaseServer.shutdown();
		log.info("### Shutdown EOS-Security Complete ###");
	}

}
