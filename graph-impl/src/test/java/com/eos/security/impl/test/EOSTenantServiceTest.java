/**
 * 
 */
package com.eos.security.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.eos.common.EOSState;
import com.eos.common.exception.EOSException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.service.EOSPermissionService;
import com.eos.security.api.service.EOSRoleService;
import com.eos.security.api.service.EOSSecurityService;
import com.eos.security.api.service.EOSTenantService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.dao.EOSTenantDAO;
import com.eos.security.impl.dao.EOSTenantDataDAO;
import com.eos.security.impl.service.EOSTenantServiceImpl;
import com.eos.security.impl.service.TransactionManager;
import com.eos.security.impl.test.util.EOSTestUtil;

/**
 * @author santos.fabiano
 * 
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/spring-test.xml" })
@RunWith(MockitoJUnitRunner.class)
public class EOSTenantServiceTest {

	@Mock
	private EOSTenantDAO tenantDAO;
	@Mock
	private EOSTenantDataDAO tenantDataDAO;
	@Mock
	private EOSUserService svcUser;
	@Mock
	private EOSSecurityService svcSecurity;
	@Mock
	private EOSRoleService svcRole;
	@Mock
	private EOSPermissionService svcPermission;
	@Mock
	TransactionManager transactionManager;
	
	@InjectMocks
	private EOSTenantService svcTenant = new EOSTenantServiceImpl();
	
	private EOSUser buildUser(String tenantMail) {
		return new EOSUser().setLogin("test.user").setFirstName("Test").setLastName("User")
				.setPersonalMail("test@personal.com").setEmail(tenantMail).setState(EOSState.ACTIVE);
	}
	
	private EOSTenant buildTenant(String alias) {
		return new EOSTenant().setAlias(alias).setName("Tenant name for" + alias)
				.setDescription("Tenant description for " + alias);
	}

	// Tenant

	@Test(expected = EOSValidationException.class)
	public void shouldThrowEOSValidationExceptionWhenAliasIsInvalid() throws EOSException {
		EOSTenant tenant = new EOSTenant().setAlias("test-create").setName("Test create tenant")
				.setDescription("Create tenant description");
		EOSUser admin = buildUser("test@create.mail");
		EOSTenant createdTenant = svcTenant.createTenant(tenant, null, admin);

		assertNotNull("Create tenant", createdTenant.getAlias());
		assertEquals("Create tenant - state", tenant.getState(), createdTenant.getState());
	}

	@Test
	public void shouldCreateTenantWithStateDisabled() throws EOSException {
		EOSTenant tenant = new EOSTenant().setAlias("test-create").setName("Test create tenant")
				.setDescription("Create tenant description");
		EOSUser admin = buildUser("test@create.mail");
		EOSTenant createdTenant = svcTenant.createTenant(tenant, null, admin);

		assertNotNull("Create tenant", createdTenant.getAlias());
		assertEquals("Create tenant - state", tenant.getState(), createdTenant.getState());
	}

	@Test
	public void shouldCreateTenantWithUnchangedState() throws EOSException {
		EOSTenant tenant = new EOSTenant().setAlias("test-create").setName("Test create tenant")
				.setDescription("Create tenant description").setState(EOSState.ACTIVE);
		EOSUser admin = buildUser("test@create.mail");
		EOSTenant createdTenant = svcTenant.createTenant(tenant, null, admin);

		assertNotNull("Create tenant", tenant.getAlias());
		assertEquals("Create tenant", tenant, createdTenant);
	}
	
	@Test
	public void shouldFindTenantWhenItExists() throws EOSException {
		String tenantAlias = "test-find";
		EOSTenant tenant = buildTenant(tenantAlias);
		when(tenantDAO.find(tenantAlias)).thenReturn(tenant);
		EOSTenant found = svcTenant.findTenant(tenantAlias);
		
		assertEquals("Find tenant id equals", tenant, found);
	}
	
	@Test(expected = EOSNotFoundException.class)
	public void shouldThrowEOSNotFoundExceptionWhenTenantDosNotExists(){
		svcTenant.findTenant("test-find");
	}

	@Test
	public void shouldFindMultipleTenants() throws EOSException {
		Set<String> aliases = Sets.newSet("findTenant1", "findTenant2");
		Set<EOSTenant> tenants = Sets.newSet(buildTenant("findTenant1"), buildTenant("findTenant2"));
		when(tenantDAO.findTenants(aliases)).thenReturn(tenants);
		Set<EOSTenant> tenantsFound = svcTenant.findTenants(aliases);
		
		assertEquals("Find tenants size", tenants.size(), tenantsFound.size());
		Assert.assertTrue("Find Tenants contains all", tenants.containsAll(tenantsFound));
	}

	/*
	@Test
	public void testListTenants() throws EOSException {
		Set<EOSTenant> createds = new HashSet<>(2);
		createds.add(svcTenant.createTenant(new EOSTenant().setAlias("listTenant1").setName("Test list tenant 1")
				.setDescription("Test list description 1"), null, getUser("test@list1.mail")));
		createds.add(svcTenant.createTenant(new EOSTenant().setAlias("listTenant2").setName("Test list tenant 2")
				.setDescription("Test list description 2"), null, getUser("test@list2.mail")));
		Set<EOSTenant> tenants = svcTenant.listTenants(null, 15, 0);
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
			// Nothing, exception expected
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
			tenantData.put("key1", "newValue"); // updated
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

	@Test
	public void testListTenantDataByKeys() throws EOSException {
		EOSTenant tenant = new EOSTenant();
		EOSUser admin = getUser("test@listdatakey.mail");
		Map<String, String> tenantData = new HashMap<>(3);
		tenantData.put("key1", "value1");
		tenantData.put("key2", "value2");
		tenantData.put("key3", "value3");

		tenant.setAlias("tenantListByKeys").setName("List tenant data keys")
				.setDescription("List tenant data keys description");
		tenant = svcTenant.createTenant(tenant, tenantData, admin);
		try {
			EOSTestUtil.setup(context, tenant.getAlias(), admin);
			Set<String> keys = new HashSet<>(2);
			keys.add("key1");
			keys.add("key2");
			tenantData = svcTenant.listTenantData(tenant.getAlias(), keys);

			Assert.assertTrue("List tenat keys", tenantData.containsKey("key1"));
			Assert.assertTrue("List tenat keys", tenantData.containsKey("key2"));
		} finally {
			// Restore context.
			EOSTestUtil.setup(context);
		}
	}

	@Test
	public void testListTenantData() throws EOSException {
		EOSTenant tenant = new EOSTenant();
		EOSUser admin = getUser("test@listdata.mail");
		Map<String, String> tenantData = new HashMap<>(2);
		tenantData.put("key1", "value1");
		tenantData.put("key2", "value2");
		tenantData.put("key3", "value3");
		tenantData.put("key4", "value4");

		tenant.setAlias("listTenantData").setName("List tenant data keys")
				.setDescription("List tenant data keys description");
		tenant = svcTenant.createTenant(tenant, tenantData, admin);

		try {
			EOSTestUtil.setup(context, tenant.getAlias(), admin);
			tenantData = svcTenant.listTenantData(tenant.getAlias(), 5, 0);
			Assert.assertEquals("tenant data size", 4, tenantData.size());
		} finally {
			// Restore context.
			EOSTestUtil.setup(context);
		}
	}
*/
}
