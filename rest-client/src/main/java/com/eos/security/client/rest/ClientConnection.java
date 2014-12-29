package com.eos.security.client.rest;

/**
 * 
 * EOS Rest client connection. Holds session login and cookies.
 *
 */
public class ClientConnection {

	private final String host;

	private ClientConnection(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public static ClientConnection create(String host) {
		return new ClientConnection(host);
	}
}
