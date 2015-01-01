package com.eos.security.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.eos.common.EOSUserType;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.client.rest.ClientConnection;
import com.eos.security.client.rest.tenant.EOSTenantCreateData;
import com.eos.security.client.rest.tenant.TenantClient;
import com.eos.security.web.EOSApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EOSApplication.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=8899", "management.port=9988" })
@Profile("integration")
public class TenantServiceRestTest {

	private static final String SERVER = "http://localhost:8899";

	private ClientConnection connection;

	@Before
	public void setup() {
		connection = ClientConnection.create(SERVER);
	}

	@Test
	public void shouldCreateTenant() {
		TenantClient tenantClient = TenantClient.create(connection);
		EOSTenant tenant = new EOSTenant().setAlias("createTenant").setName("Create Tenant")
				.setDescription("Create Tenant Description");
		EOSTenantCreateData tenantCreateData = new EOSTenantCreateData().setTenant(tenant).setAdminUser(
				buildUser("create"));

		tenantClient.createTenant(tenantCreateData);
	}

	private EOSUser buildUser(String label) {
		return new EOSUser().setEmail(label + "@tenanttest.com").setFirstName("First " + label)
				.setLastName("Last " + label).setLogin("login_" + label).setNickName("Nick " + label)
				.setPersonalMail("personal@testtenant.com").setType(EOSUserType.USER);

	}
}
