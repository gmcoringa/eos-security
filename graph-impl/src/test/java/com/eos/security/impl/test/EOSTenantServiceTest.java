/**
 * 
 */
package com.eos.security.impl.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eos.common.EOSState;
import com.eos.common.exception.EOSException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.security.api.service.EOSSecurityService;
import com.eos.security.api.service.EOSTenantService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.test.util.EOSTestUtil;

/**
 * @author santos.fabiano
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-test.xml" })
public class EOSTenantServiceTest {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private EOSTenantService svcTenant;
	@Autowired
	private EOSUserService svcUser;
	@Autowired
	private EOSSecurityService svcSecurity;

	private EOSUser getUser(String tenantMail) {
		return new EOSUser().setLogin("test.user").setFirstName("Test").setLastName("User")
				.setPersonalMail("test@personal.com").setEmail(tenantMail).setState(EOSState.ACTIVE);
	}

	@Before
	public void setUp() throws EOSException {
		// EOSTestUtil.setup(context);
	}

	// Tenant

	@Test
	public void testTenantCreate() throws EOSException {
		EOSTenant tenant = new EOSTenant();
		tenant.setAlias("test-create").setName("Test create tenant").setDescription("Create tenant description");
		EOSUser admin = getUser("test@create.mail");
		tenant = svcTenant.createTenant(tenant, null, admin);
		Assert.assertNotNull("Create tenant", tenant.getAlias());
		// Assert.assertNotNull("Create tenant - Admin User", svcUser.findTenantUser(admin.getLogin(), tenant.getId()));
	}

	@Test
	public void testFindTenant() throws EOSException {
		String alias = svcTenant.createTenant(
				new EOSTenant().setAlias("test-find").setName("Test find tenant")
						.setDescription("Test find description"), null, getUser("test@find.mail")).getAlias();
		EOSTenant tenant = svcTenant.findTenant(alias);
		Assert.assertNotNull("Find tenant: id not null", tenant.getAlias());
		Assert.assertEquals("Find tenant id equals", tenant.getAlias(), alias);
	}

	@Test
	public void testFindTenants() throws EOSException {
		Set<String> aliases = new HashSet<>(2);
		aliases.add(svcTenant.createTenant(
				new EOSTenant().setAlias("findtenants1").setName("Test find tenants 1")
						.setDescription("Test find description"), null, getUser("test@find1.mail")).getAlias());
		aliases.add(svcTenant.createTenant(
				new EOSTenant().setAlias("findtenants2").setName("Test find tenants 2")
						.setDescription("Test find description"), null, getUser("test@find2.mail")).getAlias());
		Set<EOSTenant> tenants = svcTenant.findTenants(aliases);
		Assert.assertEquals("Find tenants size", 2, tenants.size());
	}

	@Test
	public void testListTenants() throws EOSException {
		Set<EOSTenant> createds = new HashSet<>(2);
		createds.add(svcTenant.createTenant(new EOSTenant().setAlias("listTenant1").setName("Test list tenant 1")
				.setDescription("Test list description 1"), null, getUser("test@list1.mail")));
		createds.add(svcTenant.createTenant(new EOSTenant().setAlias("listTenant2").setName("Test list tenant 2")
				.setDescription("Test list description 2"), null, getUser("test@list2.mail")));
		Set<EOSTenant> tenants = svcTenant.listTenants(null, 5, 0);
		Assert.assertTrue("List tenants higher than 1", tenants.size() > 1);
		Assert.assertTrue("Tenants contains", tenants.containsAll(createds));
	}

	@Test
	public void testUpdateTenant() throws EOSException {
		EOSUser admin = getUser("test@update.mail");
		String alias = svcTenant.createTenant(
				new EOSTenant().setAlias("updateTenant").setName("Test update tenant")
						.setDescription("Test update tenant description"), null, admin).getAlias();

		try {
			EOSTestUtil.setup(context, alias, admin);
			// Perform update
			EOSTenant updated = new EOSTenant().setAlias(alias).setName("Test update tenant: UPDATED")
					.setDescription("Test update tenant description: UPDATED");
			svcTenant.updateTenant(updated);
			EOSTenant tenant = svcTenant.findTenant(alias);

			Assert.assertEquals(updated.getName(), tenant.getName());
			Assert.assertEquals(updated.getDescription(), tenant.getDescription());
		} finally {
			// Restore context.
			EOSTestUtil.setup(context);
		}
	}

	@Test
	public void testUpdateTenantState() throws EOSException {
		String alias = svcTenant.createTenant(
				new EOSTenant().setAlias("updateTenantState").setName("Test update tenant state")
						.setDescription("Test update tenant state description"), null, getUser("test@stateup.mail"))
				.getAlias();
		svcTenant.updateTenantState(alias, EOSState.INACTIVE);
		EOSTenant tenant = svcTenant.findTenant(alias);
		Assert.assertEquals(tenant.getState(), EOSState.INACTIVE);
	}

	@Test
	public void testTenantPurge() throws EOSException {
		Map<String, String> meta = new HashMap<>(2);
		meta.put("purgeKey1", "purgeValue1");
		meta.put("purgeKey2", "purgeValue2");

		String alias = svcTenant.createTenant(
				new EOSTenant().setAlias("purgeTenant").setName("Test purge tenant")
						.setDescription("Test purge tenant description"), meta, getUser("test@purgetenant.mail"))
				.getAlias();
		svcTenant.purgeTenant(alias);
		try {
			EOSTenant tenant = svcTenant.findTenant(alias);
			Assert.assertNull("Not found purged tenant", tenant);
			Assert.fail("Tenant purged should not be found");
		} catch (EOSNotFoundException e) {
			// Nothing, exception espected
		}
	}

	// Tenant Data

	@Test
	public void testCreateTenantData() throws EOSException {
		EOSTenant tenant = new EOSTenant();
		EOSUser admin = getUser("test@createdata.mail");
		Map<String, String> tenantData = new HashMap<>(2);
		tenantData.put("keyCreate", "valueCreate");
		tenant.setAlias("testMetaCreate").setName("Create tenant data")
				.setDescription("Create tenant data description");
		tenant = svcTenant.createTenant(tenant, tenantData, admin);

		try {
			EOSTestUtil.setup(context, tenant.getAlias(), admin);
			String value = svcTenant.findTenantData(tenant.getAlias(), "keyCreate");
			Assert.assertEquals("tenant data valule", "valueCreate", value);
		} finally {
			// Restore context.
			EOSTestUtil.setup(context);
		}
	}

	@Test
	public void testUpdateTenantData() throws EOSException {
		EOSTenant tenant = new EOSTenant();
		EOSUser admin = getUser("test@updatedata.mail");
		Map<String, String> tenantData = new HashMap<>(2);
		tenantData.put("key1", "value1");
		tenantData.put("key2", "value2");

		tenant.setAlias("tenantUpdateData").setName("Update tenant data")
				.setDescription("Update tenant data description");
		tenant = svcTenant.createTenant(tenant, tenantData, admin);

		try {
			EOSTestUtil.setup(context, tenant.getAlias(), admin);
			tenantData = svcTenant.listTenantData(tenant.getAlias(), 5, 0);
			Assert.assertEquals("tenant data size", 2, tenantData.size());
			// Clear and rebuild tenant data
			tenantData.clear();
			tenantData.put("key1", "newValue");
			tenantData.put("key2", ""); // Set to be removed
			tenantData.put("key3", "value3"); // New value
			tenantData.put("key4", "value4"); // New value

			// Validations
			svcTenant.updateTenantData(tenant.getAlias(), tenantData);
			tenantData = svcTenant.listTenantData(tenant.getAlias(), 5, 0);
			Assert.assertEquals("tenant data update size", 3, tenantData.size());
			// tenant data with key1 check new value
			String value = svcTenant.findTenantData(tenant.getAlias(), "key1");
			Assert.assertEquals("tenant data update key1", "newValue", value);
		} finally {
			// Restore context.
			EOSTestUtil.setup(context);
		}
	}

	// @Test
	// public void testListTenantDataByKeys() throws EOSException {
	// EOSTenant tenant = new EOSTenant();
	// EOSUser admin = getUser("test@listdatakey.mail");
	// Map<String, String> tenantData = new HashMap<>(2);
	// tenantData.put("key1", "value1");
	// tenantData.put("key2", "value2");
	//
	// tenant.setName("List tenant data keys").setDescription("List tenant data keys description");
	// tenant = svcTenant.createTenant(tenant, tenantData, admin);
	// try {
	// EOSTestUtil.setup(context, tenant.getId(), admin);
	// List<String> keys = new ArrayList<>(2);
	// keys.addAll(tenantData.keySet());
	// tenantData = svcTenant.listTenantData(tenant.getId(), keys);
	//
	// Assert.assertTrue("List tenat keys", tenantData.containsKey("key1"));
	// Assert.assertTrue("List tenat keys", tenantData.containsKey("key2"));
	// } finally {
	// // Restore context.
	// EOSTestUtil.setup(context);
	// }
	// }
	//
	// @Test
	// public void testListTenantData() throws EOSException {
	// EOSTenant tenant = new EOSTenant();
	// EOSUser admin = getUser("test@listdata.mail");
	// Map<String, String> tenantData = new HashMap<>(2);
	// tenantData.put("key1", "value1");
	// tenantData.put("key2", "value2");
	// tenantData.put("key3", "value3");
	// tenantData.put("key4", "value4");
	//
	// tenant.setName("List tenant data keys").setDescription("List tenant data keys description");
	// tenant = svcTenant.createTenant(tenant, tenantData, admin);
	//
	// try {
	// EOSTestUtil.setup(context, tenant.getId(), admin);
	// tenantData = svcTenant.listTenantData(tenant.getId(), 5, 0);
	// Assert.assertEquals("tenant data size", 4, tenantData.size());
	// } finally {
	// // Restore context.
	// EOSTestUtil.setup(context);
	// }
	// }

}
