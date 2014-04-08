/**
 * 
 */
package com.eos.security.impl.test;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eos.common.exception.EOSException;
import com.eos.security.api.service.EOSPermissionService;
import com.eos.security.api.service.EOSSecurityService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.service.internal.EOSSystemConstants;
import com.eos.security.impl.session.SessionContextManager;
import com.eos.security.impl.test.util.EOSTestUtil;

/**
 * Test class for security service. Only test meaningful methods are tested.
 * <p>
 * Create context are not tested, because all test use it before start their
 * execution.
 * </p>
 * <p>
 * Check permission methods aren't tested because is only a call to
 * {@link EOSPermissionService}.
 * </p>
 * 
 * @author santos.fabiano
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-test.xml" })
public class EOSSecurityServiceTest {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private EOSUserService svcUser;
	@Autowired
	private EOSSecurityService svcSecurity;

	@Before
	public void setUp() throws EOSException {
		EOSTestUtil.setup(context);
	}

	@Test
	public void testLoginLogout() throws EOSException {
		// Setup puts SYS Administrator as logged user
		Assert.assertTrue("LoginLogout: admin", svcSecurity.isLogged());
		svcSecurity.logout();
		// Perform logout
		Assert.assertFalse("LoginLogout: anonymouos", svcSecurity.isLogged());
		svcSecurity.login(EOSSystemConstants.LOGIN_SUPER_ADMIN, null,
				"EOSpas$", false);
		Assert.assertTrue("LoginLogout: admin", svcSecurity.isLogged());
	}

	@Test
	public void testLoginByPersonalEmail() throws EOSException {
		EOSUser user = EOSTestUtil.createUser("emailPersonalLogin", null,
				svcUser);
		String password = "EMail-Login$1";
		svcUser.setUserPassword(user.getLogin(), null, password);
		// Setup puts SYS Administrator as logged user
		svcSecurity.logout();
		// Perform logout
		Assert.assertFalse("LoginPersonal: anonymouos", svcSecurity.isLogged());
		svcSecurity.login(null, user.getPersonalMail(), password, false);
		Assert.assertTrue("LoginPersonal: user logged", svcSecurity.isLogged());
	}

	@Test
	public void testLoginByTenantEmail() throws EOSException {
		EOSUser user = EOSTestUtil
				.createUser("emailTenantLogin", null, svcUser);
		String password = "EMail-Login$2";
		svcUser.setUserPassword(user.getLogin(), null, password);
		// Setup puts SYS Administrator as logged user
		svcSecurity.logout();
		// Perform logout
		Assert.assertFalse("LoginTenant: anonymouos", svcSecurity.isLogged());
		svcSecurity.login(null, user.getEmail(), password, false);
		Assert.assertTrue("LoginTenant: user logged", svcSecurity.isLogged());
	}

	@Test
	public void testIsLogged() throws EOSException {
		// Setup puts SYS Administrator as logged user
		Assert.assertTrue("IsLogged: admin", svcSecurity.isLogged());
		// Create default session without user
		svcSecurity.createSessionContext(UUID.randomUUID().toString(),
				EOSSystemConstants.ADMIN_TENANT);
		Assert.assertFalse("IsLogged: anonymouos", svcSecurity.isLogged());
		Assert.assertEquals("IsLoggded anonymous",
				EOSSystemConstants.LOGIN_ANONYMOUS,
				SessionContextManager.getCurrentUserLogin());
	}

	@Test
	public void testImpersonateDeImpersonate() throws EOSException {
		String beforeImperonate = SessionContextManager.getCurrentUserLogin();
		svcSecurity.impersonate(EOSSystemConstants.LOGIN_SYSTEM_USER,
				SessionContextManager.getCurrentTenantId(), null);
		Assert.assertEquals("Impersonated",
				EOSSystemConstants.LOGIN_SYSTEM_USER,
				SessionContextManager.getCurrentUserLogin());
		svcSecurity.deImpersonate();
		Assert.assertEquals("De-Impersonated", beforeImperonate,
				SessionContextManager.getCurrentUserLogin());
	}
}
