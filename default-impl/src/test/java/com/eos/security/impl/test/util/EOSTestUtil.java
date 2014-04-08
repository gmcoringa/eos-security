/**
 * 
 */
package com.eos.security.impl.test.util;

import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationContext;

import com.eos.common.EOSLevel;
import com.eos.common.EOSState;
import com.eos.common.exception.EOSDuplicatedEntryException;
import com.eos.common.exception.EOSException;
import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.service.EOSGroupService;
import com.eos.security.api.service.EOSRoleService;
import com.eos.security.api.service.EOSTenantService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.session.SessionContext;
import com.eos.security.api.vo.EOSGroup;
import com.eos.security.api.vo.EOSRole;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.service.internal.EOSSystemConstants;
import com.eos.security.impl.session.EOSSession;
import com.eos.security.impl.session.SessionContextManager;

/**
 * Test utilities.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSTestUtil {

	/**
	 * Setup session context for testing. Set default tenant and SYSTEM
	 * administrator user.
	 * 
	 * @param appContext
	 *            Application Context
	 * @throws EOSException
	 *             If any problem occurs.
	 */
	public static void setup(ApplicationContext appContext) throws EOSException {
		// Already setup, return
		if (SessionContextManager.getCurrentTenantId() != null
				&& SessionContextManager.getCurrentTenantId() == EOSSystemConstants.ADMIN_TENANT
				&& SessionContextManager.getCurrentUserLogin() != null
				&& EOSSystemConstants.LOGIN_SUPER_ADMIN
						.equals(SessionContextManager.getCurrentUserLogin())) {
			return;
		}

		final EOSUserService svcUser = appContext.getBean(EOSUserService.class);
		EOSUser user = svcUser.findTenantUser(
				EOSSystemConstants.LOGIN_SUPER_ADMIN,
				EOSSystemConstants.ADMIN_TENANT);
		setup(appContext, EOSSystemConstants.ADMIN_TENANT, user);
	}

	/**
	 * Setup session for the given user and tenant.
	 * 
	 * @param appContext
	 *            Application Context
	 * @param tenantId
	 *            The tenant to be set on context.
	 * @param user
	 *            The user to be set on context.
	 * @throws EOSException
	 *             If any problem occurs.
	 */
	public static void setup(ApplicationContext appContext,
			final Long tenantId, final EOSUser user) throws EOSException {
		final EOSTenantService svcTenant = appContext
				.getBean(EOSTenantService.class);
		String sessionId = SessionContextManager.getCurrentSessionId();
		final SessionContext context = new SessionContext(
				svcTenant.findTenant(tenantId), user);
		final EOSSession session = EOSSession.getContext();

		if (sessionId == null) {
			sessionId = UUID.randomUUID().toString();
		}

		// Set current session
		session.setSessionId(sessionId).setSession(context);
		// Add to local session cache
		SessionContextManager.setSession(sessionId, context);
	}

	public static EOSUser createUser(String prefix,
			Map<String, String> userData, EOSUserService svcUser)
			throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSUser user = new EOSUser();
		user.setLogin(prefix + "-create").setEmail(prefix + "@create.com")
				.setFirstName(prefix + " First").setLastName(prefix + " Last")
				.setNickName(prefix + " Nick")
				.setPersonalMail(prefix + "@personal.com")
				.setState(EOSState.ACTIVE);

		user = svcUser.createUser(user, userData);
		return user;
	}

	public static EOSGroup createGroup(String identifier,
			EOSGroupService svcGroup) throws EOSDuplicatedEntryException,
			EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException {
		EOSGroup group = new EOSGroup().setName("Test " + identifier)
				.setDescription("Test description " + identifier)
				.setLevel(EOSLevel.PUBLIC.getLevel());

		return svcGroup.createGroup(group);
	}

	public static EOSRole createRole(String identifier, EOSRoleService svcRole)
			throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSRole role = new EOSRole().setCode(identifier + "-code")
				.setDescription(identifier + " Description")
				.setLevel(EOSLevel.MAXIMUM.getLevel());
		return svcRole.createRole(role);
	}
}
