package com.eos.security.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.eos.security.api.web.resources.EOSUserCreateData;
import com.eos.security.client.rest.ClientConnection;
import com.eos.security.client.rest.user.UserClient;
import com.eos.security.web.EOSApplication;
import com.eos.security.web.rest.util.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EOSApplication.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=8899", "management.port=9988" })
@Profile("integration")
public class UserServiceRestTest {

	private static final String SERVER = "http://localhost:8899";

	private ClientConnection connection;

	@Before
	public void setup() {
		connection = ClientConnection.create(SERVER);
	}

	@Test
	public void shouldCreateUser() {
		UserClient userClient = UserClient.create(connection);
		EOSUserCreateData userCreateData = new EOSUserCreateData().setUser(TestUtil.buildUser("create-user"));

		userClient.createUser(userCreateData);
	}
}
