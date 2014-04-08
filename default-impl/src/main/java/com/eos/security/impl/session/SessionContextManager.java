/**
 * 
 */
package com.eos.security.impl.session;

import java.util.concurrent.ConcurrentHashMap;

import com.eos.security.api.session.SessionContext;

/**
 * Session context manger. Get current session.
 * 
 * @author santos.fabiano
 * 
 */
public class SessionContextManager {

	private static final ConcurrentHashMap<String, SessionContext> sessions = new ConcurrentHashMap<>();

	public static SessionContext getSession(final String sessionId) {
		return sessions.get(sessionId);
	}

	public static void setSession(final String sessionId, SessionContext session) {
		sessions.putIfAbsent(sessionId, session);
	}

	public static SessionContext getCurrentSession() {
		return EOSSession.getContext().getSession();
	}

	public static String getCurrentSessionId() {
		return EOSSession.getContext().getSessionId();
	}

	public static Long getCurrentTenantId() {
		SessionContext context = EOSSession.getContext().getSession();

		if (context != null && context.getTenant() != null) {
			return context.getTenant().getId();
		} else {
			return null;
		}
	}

	public static String getCurrentUserLogin() {
		SessionContext context = EOSSession.getContext().getSession();

		if (context != null && context.getUser() != null) {
			return context.getUser().getLogin();
		} else {
			return null;
		}
	}
}
