/**
 * 
 */
package com.eos.security.api.service;

import com.eos.common.EOSUserType;
import com.eos.common.exception.EOSException;
import com.eos.common.exception.EOSInvalidStateException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.session.SessionContext;

/**
 * Security functions, as access and user authorization.
 * 
 * @author fabiano.santos
 * 
 */
public interface EOSSecurityService {

	/**
	 * Create a session for the given tenant and session Id.
	 * 
	 * @param sessionId
	 *            The session ID.
	 * @param tenantAlias
	 *            The tenant for this session.
	 * @return Session context created.
	 */
	public SessionContext createSessionContext(String sessionId, String tenantAlias);

	/**
	 * Retrieve session context information based on session Id.
	 * 
	 * @param sessionId
	 *            The session Id.
	 * @return Session context.
	 * @throws EOSNotFoundException
	 *             If no session with the given id was found.
	 */
	public SessionContext getSessionContext(String sessionId) throws EOSNotFoundException;

	/**
	 * Setup current session information for the given session ID.
	 * 
	 * @param sessionId
	 *            The session ID.
	 * @throws EOSNotFoundException
	 *             If no session with the given id was found.
	 */
	public void setupSession(String sessionId) throws EOSNotFoundException;

	/**
	 * Perform the user login. This service also loads the {@link SessionContext}.
	 * <p>
	 * First try to find the user by login, if not found, try to find by any of his e-mails.
	 * </p>
	 * 
	 * @param login
	 *            The user login.
	 * @param email
	 *            The user personal or tenant e-mail.
	 * @param password
	 *            The user password.
	 * @param keepConnected
	 *            True if is desired a long live session.
	 * @throws EOSException
	 *             If user not found or invalid password.
	 */
	public void login(String login, String email, String password, boolean keepConnected) throws EOSException;

	/**
	 * Perform the user logout, also clean up the {@link SessionContext}.
	 * 
	 * @throws EOSUnauthorizedException
	 *             If user not logged.
	 */
	public void logout() throws EOSUnauthorizedException;

	/**
	 * Verify if any user is logged.
	 * 
	 * @return True if an user is logged, false otherwise.
	 */
	public boolean isLogged();

	/**
	 * Verify if there is any logged user.
	 * 
	 * @throws EOSUnauthorizedException
	 *             If no user is not logged.
	 */
	public void checkLogged() throws EOSUnauthorizedException;

	/**
	 * Impersonate the given user, so all subsequent calls use the given user as the logged one. Only system users can
	 * be impersonated.
	 * 
	 * @param login
	 *            Login of a system user.
	 * @param userTenantAlias
	 *            The tenant alias where to find the given user.
	 * @param sessionTenantAlias
	 *            Alias of the tenant to be set. Optional, if null no changes are performed for tenant.
	 * @throws EOSForbiddenException
	 *             If the user is not a {@link EOSUserType#SYSTEM}.
	 * @throws EOSNotFoundException
	 *             If the user with the given userTenantId parameter do not exists, or if the tenant with
	 *             sessionTenantId parameter do not exists.
	 */
	public void impersonate(String login, String userTenantAlias, String sessionTenantAlias)
			throws EOSForbiddenException, EOSNotFoundException;

	/**
	 * Restore an impersonated user, ends an impersonated session, setting back user and tenant.
	 * 
	 * @throws EOSInvalidStateException
	 *             If no impersonated session exists.
	 */
	public void deImpersonate() throws EOSInvalidStateException;

	/**
	 * Verify if the logged user has any the given permissions. Calling this method is the same as
	 * checkPermissions(true, true, String...).
	 * 
	 * @see EOSSecurityService#checkPermissions(boolean, boolean, String...).
	 * @param permissions
	 *            List of permissions to be checked.
	 * @throws EOSForbiddenException
	 *             If the user do not have any of the given permissions or any of the hierarchical permissions.
	 * @throws EOSUnauthorizedException
	 *             If the user is not logged.
	 */
	public void checkPermissions(String... permissions) throws EOSForbiddenException, EOSUnauthorizedException;

	/**
	 * Performs the following validations:
	 * <ul>
	 * <li>Check if the user has any of the given permissions, if not throws EOSForbiddenException.</li>
	 * <li>If the parameter verifyLoggedUser is true, checks if the user is logged, if not throws
	 * EOSUnauthorizedException.</li>
	 * <li>If the parameter verifyHierarchical, before validate permissions, verify if the user has tenant
	 * administration permission or super administrator permission.</li>
	 * <ul>
	 * 
	 * <p>
	 * The validation or is:
	 * </p>
	 * <ul>
	 * <li>Verify user logged.</li>
	 * <li>Verify hierarchical permissions.</li>
	 * <li>Verify permissions.</li>
	 * <ul>
	 * 
	 * @param verifyLoggedUser
	 *            Verify if the user is logged.
	 * @param verifyHierarchical
	 *            Verify hierarchical permissions, as tenant administrator and super administrator.
	 * @param permissions
	 *            List of permissions to be checked.
	 * @throws EOSForbiddenException
	 *             If the user do not have any of the given permissions or any of the hierarchical permissions.
	 * @throws EOSUnauthorizedException
	 *             If the user is not logged.
	 */
	public void checkPermissions(boolean verifyLoggedUser, boolean verifyHierarchical, String... permissions)
			throws EOSForbiddenException, EOSUnauthorizedException;

}
