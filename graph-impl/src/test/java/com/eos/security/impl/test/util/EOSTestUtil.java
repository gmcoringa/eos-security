/**
 * 
 */
package com.eos.security.impl.test.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.eos.common.EOSState;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.service.DataBaseServer;
import com.eos.security.impl.service.internal.EOSTransactionThreadLocalScope;

/**
 * Test utilities.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSTestUtil {

	public static void registerScope(ApplicationContext context, DataBaseServer dataBaseServer) {
		AbstractApplicationContext applicationContext = (AbstractApplicationContext) context;
		applicationContext.getBeanFactory().registerScope(EOSTransactionThreadLocalScope.SCOPE_NAME,
				new EOSTransactionThreadLocalScope(dataBaseServer));
	}

	public static EOSUser buildUser(String tenantMail) {
		return new EOSUser().setLogin("test.user").setFirstName("Test").setLastName("User")
				.setPersonalMail("test@personal.com").setEmail(tenantMail).setState(EOSState.ACTIVE);
	}

	public static EOSTenant buildTenant(String alias) {
		return new EOSTenant().setAlias(alias).setName("Tenant name for" + alias)
				.setDescription("Tenant description for " + alias);
	}

}
