/**
 * 
 */
package com.eos.security.impl.test;

import java.util.Collection;
import java.util.HashSet;
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
import com.eos.security.api.service.EOSSecurityService;
import com.eos.security.api.service.EOSTenantService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;

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
		Collection<EOSTenant> tenants = svcTenant.findTenants(aliases);
		Assert.assertEquals("Find tenants size", 2, tenants.size());
	}

	// @Test
	// public void testListTenants() throws EOSException {
	// svcTenant.createTenant(new EOSTenant().setName("Test list tenant 1").setDescription("Test list description 1"),
	// null, getUser("test@list1.mail")).getId();
	// svcTenant.createTenant(new EOSTenant().setName("Test list tenant 2").setDescription("Test list description 2"),
	// null, getUser("test@list2.mail")).getId();
	// List<EOSTenant> tenants = svcTenant.listTenants(null, 5, 0);
	// Assert.assertTrue("List tenants higher than 1", tenants.size() > 1);
	// }
	//
	// @Test
	// public void testUpdateTenant() throws EOSException {
	// EOSUser admin = getUser("test@update.mail");
	// Long tenantId = svcTenant.createTenant(
	// new EOSTenant().setName("Test update tenant").setDescription("Test update tenant description"), null,
	// admin).getId();
	//
	// try {
	// EOSTestUtil.setup(context, tenantId, admin);
	// // Perform update
	// EOSTenant updated = new EOSTenant().setId(tenantId).setName("Test update tenant: UPDATED")
	// .setDescription("Test update tenant description: UPDATED");
	// svcTenant.updateTenant(updated);
	// EOSTenant tenant = svcTenant.findTenant(tenantId);
	//
	// Assert.assertEquals(updated.getName(), tenant.getName());
	// Assert.assertEquals(updated.getDescription(), tenant.getDescription());
	// } finally {
	// // Restore context.
	// EOSTestUtil.setup(context);
	// }
	// }
	//
	// @Test
	// public void testUpdateTenantState() throws EOSException {
	// Long tenantId = svcTenant.createTenant(
	// new EOSTenant().setName("Test update tenant state").setDescription(
	// "Test update tenant state description"), null, getUser("test@stateup.mail")).getId();
	// svcTenant.updateTenantState(tenantId, EOSState.DISABLED);
	// EOSTenant tenant = svcTenant.findTenant(tenantId);
	// Assert.assertEquals(tenant.getState(), EOSState.DISABLED);
	// }
	//
	// @Test
	// public void testTenantPurge() {
	// // TODO
	// Assert.assertTrue("TODO pruge tests", true);
	// }
	//
	// // Tenant Data
	//
	// @Test
	// public void testCreateTenantData() throws EOSException {
	// EOSTenant tenant = new EOSTenant();
	// EOSUser admin = getUser("test@createdata.mail");
	// Map<String, String> tenantData = new HashMap<>(2);
	// tenantData.put("key1", "value1");
	// tenantData.put("key2", "value2");
	// tenant.setName("Create tenant data").setDescription("Create tenant data description");
	// tenant = svcTenant.createTenant(tenant, tenantData, admin);
	//
	// try {
	// EOSTestUtil.setup(context, tenant.getId(), admin);
	// tenantData = svcTenant.listTenantData(tenant.getId(), 5, 0);
	// Assert.assertEquals("tenant data size", 2, tenantData.size());
	// } finally {
	// // Restore context.
	// EOSTestUtil.setup(context);
	// }
	// }
	//
	// @Test
	// @Transactional(propagation = Propagation.NOT_SUPPORTED)
	// public void testUpdateTenantData() throws EOSException {
	// EOSTenant tenant = new EOSTenant();
	// EOSUser admin = getUser("test@updatedata.mail");
	// Map<String, String> tenantData = new HashMap<>(2);
	// tenantData.put("key1", "value1");
	// tenantData.put("key2", "value2");
	//
	// tenant.setName("Update tenant data").setDescription("Update tenant data description");
	// tenant = svcTenant.createTenant(tenant, tenantData, admin);
	//
	// try {
	// EOSTestUtil.setup(context, tenant.getId(), admin);
	// tenantData = svcTenant.listTenantData(tenant.getId(), 5, 0);
	// Assert.assertEquals("tenant data size", 2, tenantData.size());
	// // Clear and rebuild tenant data
	// tenantData.clear();
	// tenantData.put("key1", "newValue");
	// tenantData.put("key2", ""); // Set to be removed
	// tenantData.put("key3", "value3"); // New value
	// tenantData.put("key4", "value4"); // New value
	//
	// // Validations
	// svcTenant.updateTenantData(tenant.getId(), tenantData);
	// tenantData = svcTenant.listTenantData(tenant.getId(), 5, 0);
	// Assert.assertEquals("tenant data update size", 3, tenantData.size());
	// // tenant data with key1 check new value
	// // Not working, fetching old value
	// // String value = svcTenant.findTenantData(tenant.getId(), "key1");
	// // Assert.assertEquals("tenant data update key1", "newValue",
	// // value);
	// } finally {
	// // Restore context.
	// EOSTestUtil.setup(context);
	// }
	// }
	//
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