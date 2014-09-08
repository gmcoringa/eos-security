/**
 * 
 */
package com.eos.security.impl.service;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.common.EOSUserType;
import com.eos.common.exception.EOSException;
import com.eos.common.exception.EOSInvalidStateException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.util.CollectionUtil;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.service.EOSPermissionService;
import com.eos.security.api.service.EOSSecurityService;
import com.eos.security.api.service.EOSTenantService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.session.SessionContext;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.service.internal.EOSKnownPermissions;
import com.eos.security.impl.service.internal.EOSSystemConstants;
import com.eos.security.impl.session.EOSSession;
import com.eos.security.impl.session.SessionContextManager;

/**
 * EOS Security Service implementation.
 * 
 * @author santos.fabiano
 * 
 */
@Service
public class EOSSecurityServiceImpl implements EOSSecurityService {

	private static final Logger log = LoggerFactory.getLogger(EOSSecurityServiceImpl.class);

	private EOSTenantService svcTenant;
	private EOSUserService svcUser;
	private EOSPermissionService svcPermission;
	private Deque<SessionContext> impersonates = new LinkedList<>();

	@Autowired
	public void setTenantService(EOSTenantService svcTenant) {
		this.svcTenant = svcTenant;
	}

	@Autowired
	public void setUserService(EOSUserService svcUser) {
		this.svcUser = svcUser;
	}

	@Autowired
	public void setPermissionService(EOSPermissionService svcPermission) {
		this.svcPermission = svcPermission;
	}

	/**
	 * @see com.eos.security.api.service.EOSSecurityService#createSessionContext(java.lang.String, java.lang.String)
	 */
	@Override
	public final SessionContext createSessionContext(String sessionId, String tenantAlias) {
		try {
			return createSessionContext(sessionId, tenantAlias,
					svcUser.findTenantUser(EOSSystemConstants.LOGIN_ANONYMOUS, EOSSystemConstants.ADMIN_TENANT));
		} catch (EOSNotFoundException e) {
			// Should never happens
			log.debug("Anonymous user not found");
			throw new EOSException("Anonymouos user not found", e);
		}
	}

	/**
	 * Creates and setup a session context.
	 * 
	 * @param sessionId
	 *            The session ID to be set.
	 * @param tenantId
	 *            The tenant ID to be used with the session.
	 * @param user
	 *            The user to be used with the session.
	 * @return The session context created.
	 * @throws EOSNotFoundException
	 *             If not tenant found with the given ID.
	 */
	private final SessionContext createSessionContext(final String sessionId, String tenantId, final EOSUser user)
			throws EOSNotFoundException {
		// final SessionContext context = new SessionContext(svcTenant.findTenant(tenantId), user);
		final SessionContext context = new SessionContext(new EOSTenant(), user);
		final EOSSession session = EOSSession.getContext();
		// Set current session
		session.setSessionId(sessionId).setSession(context);
		// Add to local session cache
		SessionContextManager.setSession(sessionId, context);
		// TODO use cache to store session
		return session.getSession();
	}

	/**
	 * @see com.eos.security.api.service.EOSSecurityService#getSessionContext(java.lang.String)
	 */
	@Override
	public final SessionContext getSessionContext(String sessionId) throws EOSNotFoundException {
		// TODO use cache to store session
		SessionContext session = SessionContextManager.getSession(sessionId);

		// If session do not exist, create default session and return
		if (session == null) {
			throw new EOSNotFoundException("Session not found: " + sessionId);
		} else {
			// return current session
			return session;
		}
	}

	/**
	 * @see com.eos.security.api.service.EOSSecurityService#setupSession(java.lang.String)
	 */
	@Override
	public void setupSession(String sessionId) throws EOSNotFoundException {
		SessionContext context = getSessionContext(sessionId);
		final EOSSession session = EOSSession.getContext();
		// Set current session
		session.setSessionId(sessionId).setSession(context);
	}

	/**
	 * @see com.eos.security.api.service.EOSSecurityService#login(java.lang.String, java.lang.String, java.lang.String,
	 *      boolean)
	 */
	@Override
	public void login(String login, String email, String password, boolean keepConnected) throws EOSException {
		// Validation done by user service
		final EOSUser user = svcUser.checkForLogin(login, email, password);
		// Retrieve current session info
		final String sessionId = SessionContextManager.getCurrentSessionId();
		final String tenantAlias = SessionContextManager.getCurrentTenantAlias();
		// Create new one with logged and current session id
		createSessionContext(sessionId, tenantAlias, user);
		// TODO do something with keepConnected
	}

	/**
	 * @see com.eos.security.api.service.EOSSecurityService#logout()
	 */
	@Override
	public void logout() throws EOSUnauthorizedException {
		checkLogged();
		// Retrieve current session info
		final String tenantAlias = SessionContextManager.getCurrentTenantAlias();
		// Just create a new one with current tenant
		createSessionContext(UUID.randomUUID().toString(), tenantAlias);
		// TODO expires old session
	}

	/**
	 * @see com.eos.security.api.service.EOSSecurityService#isLogged()
	 */
	@Override
	public boolean isLogged() {
		return !SessionContextManager.getCurrentUserLogin().equals(EOSSystemConstants.LOGIN_ANONYMOUS);
	}

	/**
	 * @see com.eos.security.api.service.EOSSecurityService#checkLogged()
	 */
	@Override
	public void checkLogged() throws EOSUnauthorizedException {
		if (!isLogged()) {
			throw new EOSUnauthorizedException("No user logged");
		}

	}

	/**
	 * @see com.eos.security.api.service.EOSSecurityService#impersonate(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void impersonate(String login, String userTenantAlias, String sessionTenantAlias)
			throws EOSForbiddenException, EOSNotFoundException {

		final EOSSession session = EOSSession.getContext();
		final String currentSessionId = session.getSessionId();
		final SessionContext currentContext = session.getSession();
		final EOSUser user = svcUser.findTenantUser(login, userTenantAlias);

		if (user.getType() != EOSUserType.SYSTEM) {
			throw new EOSForbiddenException("User not a system user");
		}

		if (sessionTenantAlias == null) {
			sessionTenantAlias = currentContext.getTenant().getAlias();
		}

		impersonates.push(currentContext);

		if (log.isDebugEnabled()) {
			log.debug("Impersonating: " + user.toString() + " on tenant: " + sessionTenantAlias);
		}

		// Setup session with the same session ID
		createSessionContext(currentSessionId, sessionTenantAlias, user);
	}

	/**
	 * @see com.eos.security.api.service.EOSSecurityService#deImpersonate()
	 */
	@Override
	public void deImpersonate() throws EOSInvalidStateException {

		if (impersonates.isEmpty()) {
			throw new EOSInvalidStateException("There is no impersonated sessions");
		}

		final EOSSession session = EOSSession.getContext();
		final SessionContext oldContext = impersonates.poll();
		final String currentSessionId = session.getSessionId();

		if (log.isDebugEnabled()) {
			log.debug("De-Impersonate - restoring old context: " + oldContext.toString());
		}

		// Restore session
		try {
			createSessionContext(currentSessionId, oldContext.getTenant().getAlias(), oldContext.getUser());
		} catch (EOSNotFoundException e) {
			// Should never happens
			throw new EOSException("Tenant not found", e);
		}
	}

	/**
	 * @see com.eos.security.api.service.EOSSecurityService#checkPermissions(java.lang.String[])
	 */
	@Override
	public void checkPermissions(String... permissions) throws EOSForbiddenException, EOSUnauthorizedException {
		checkPermissions(true, true, permissions);
	}

	/**
	 * @see com.eos.security.api.service.EOSSecurityService#checkPermissions(boolean, boolean, java.lang.String[])
	 */
	@Override
	public void checkPermissions(boolean verifyLoggedUser, boolean verifyHierarchical, String... permissions)
			throws EOSForbiddenException, EOSUnauthorizedException {

		if (verifyLoggedUser) {
			checkLogged();
		}

		Set<String> perms = CollectionUtil.asSet(permissions);

		if (verifyHierarchical) {
			perms.add(EOSKnownPermissions.PERMISSION_TENAT_ALL);
		}

		for (String permission : permissions) {
			perms.add(permission);
		}

		Map<String, Boolean> hasPerms = svcPermission
				.hasPermissions(SessionContextManager.getCurrentUserLogin(), perms);
		for (Boolean hasPerm : hasPerms.values()) {
			// Permission found, leave this method
			if (hasPerm) {
				return;
			}
		}

		// Still in this method, no permissions found
		throw new EOSForbiddenException("User is not allowed");
	}
}
