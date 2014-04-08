/**
 * 
 */
package com.eos.security.impl.session;

import com.eos.security.api.session.SessionContext;

/**
 * @author santos.fabiano
 * 
 */
public class EOSSession {

	private static final ThreadLocal<EOSSession> context;

	private SessionContext session;
	private String sessionId;

	static {
		context = new ThreadLocal<>();
	}

	/**
	 * @return the session
	 */
	public SessionContext getSession() {
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public EOSSession setSession(SessionContext session) {
		this.session = session;
		return this;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public EOSSession setSessionId(String sessionId) {
		this.sessionId = sessionId;
		return this;
	}

	/**
	 * @return the context
	 */
	public static EOSSession getContext() {
		EOSSession ses = context.get();

		if (ses == null) {
			ses = new EOSSession();
			context.set(ses);
		}

		return ses;
	}

}
