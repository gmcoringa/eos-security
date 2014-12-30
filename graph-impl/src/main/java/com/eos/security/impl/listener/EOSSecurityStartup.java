/**
 * 
 */
package com.eos.security.impl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Service;

import com.eos.security.impl.service.DataBaseServer;
import com.eos.security.impl.service.internal.EOSTransactionThreadLocalScope;

/**
 * Startup service. Create application default tenant, user, role and permissions.
 * 
 * @author santos.fabiano
 * 
 */
@Service
public class EOSSecurityStartup implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(EOSSecurityStartup.class);

	@Autowired @Qualifier("${database.mode}")
	private DataBaseServer dataBaseServer;
	@Autowired
	private EOSSecurityInitializer initializer;
	

	/**
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextEvent) {
		log.info("### Starting EOS-Security ###");
		// Register shutdown hook
		AbstractApplicationContext context = (AbstractApplicationContext) contextEvent.getApplicationContext();
		context.registerShutdownHook();
		// Register scope before inject resolve bean
		EOSTransactionThreadLocalScope scope = new EOSTransactionThreadLocalScope(dataBaseServer);
		context.getBeanFactory().registerScope(EOSTransactionThreadLocalScope.SCOPE_NAME, scope);
		
		initializer.init();
		log.info("### EOS-Security startup complete ###");
	}

}
