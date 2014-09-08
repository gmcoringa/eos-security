/**
 * 
 */
package com.eos.security.impl.tenant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.mockito.runners.MockitoJUnitRunner;

import com.eos.common.EOSState;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.service.EOSPermissionService;
import com.eos.security.api.service.EOSRoleService;
import com.eos.security.api.service.EOSSecurityService;
import com.eos.security.api.service.EOSTenantService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.service.TransactionManager;
import com.eos.security.impl.service.tenant.EOSTenantDAO;
import com.eos.security.impl.service.tenant.EOSTenantDataDAO;
import com.eos.security.impl.service.tenant.EOSTenantServiceImpl;

/**
 * @author santos.fabiano
 * 
 */
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
	

	// Tenant

	@Test(expected = EOSValidationException.class)
	public void shouldThrowEOSValidationExceptionWhenAliasIsInvalid() {
		EOSTenant tenant = new EOSTenant().setAlias("test-create#").setName("Test create tenant")
				.setDescription("Create tenant description");
		EOSUser admin = buildUser("test@create.mail");
		svcTenant.createTenant(tenant, null, admin);
	}

	@Test
	public void shouldCreateTenantWithStateDisabled() {
		EOSTenant tenant = new EOSTenant().setAlias("test-tenant-disabled").setName("Test create tenant")
				.setDescription("Create tenant description");
		EOSUser admin = buildUser("test@create.mail");
		EOSTenant createdTenant = svcTenant.createTenant(tenant, null, admin);

		assertNotNull("Create tenant", createdTenant.getAlias());
		assertEquals("Create tenant - state", tenant.getState(), createdTenant.getState());
	}

	@Test
	public void shouldCreateTenantWithUnchangedState() {
		EOSTenant tenant = new EOSTenant().setAlias("test-unchanged-state").setName("Test create tenant")
				.setDescription("Create tenant description").setState(EOSState.ACTIVE);
		EOSUser admin = buildUser("test@create.mail");
		Map<String, String> tenantData = new HashMap<>(2);
		tenantData.put("key1", "value1");
		tenantData.put("key2", "value2");

		EOSTenant createdTenant = svcTenant.createTenant(tenant, tenantData, admin);

		assertNotNull("Create tenant", tenant.getAlias());
		assertEquals("Create tenant", tenant, createdTenant);
		verify(tenantDataDAO, Mockito.atLeast(2)).createTenantData(anyString(), anyString(), anyString());
	}
	
	@Test
	public void shouldFindTenantWhenItExists() {
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
	public void shouldFindMultipleTenants() {
		Set<String> aliases = Sets.newSet("findTenant1", "findTenant2");
		Set<EOSTenant> tenants = Sets.newSet(buildTenant("findTenant1"), buildTenant("findTenant2"));
		when(tenantDAO.findTenants(aliases)).thenReturn(tenants);
		Set<EOSTenant> tenantsFound = svcTenant.findTenants(aliases);
		
		assertEquals("Find tenants size", tenants.size(), tenantsFound.size());
		Assert.assertTrue("Find Tenants contains all", tenants.containsAll(tenantsFound));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldListTenants() {
		Set<EOSTenant> createds = Sets.newSet(
				svcTenant.createTenant(new EOSTenant().setAlias("listTenant1").setName("Test list tenant 1")
						.setDescription("Test list description 1"), null, buildUser("test@list1.mail")),
				svcTenant.createTenant(new EOSTenant().setAlias("listTenant2").setName("Test list tenant 2")
						.setDescription("Test list description 2"), null, buildUser("test@list2.mail")));

		when(tenantDAO.listTenants(anySet(), anyInt(), anyInt())).thenReturn(createds);
		Set<EOSTenant> tenants = svcTenant.listTenants(null, 15, 0);
		Assert.assertTrue("List tenants higher than 1", tenants.size() > 1);
		Assert.assertTrue("Tenants contains", tenants.containsAll(createds));
	}

	@Test
	public void shouldUpdateTenant() {
		EOSTenant updated = new EOSTenant().setAlias("updateTenant").setName("Test update tenant: UPDATED")
				.setDescription("Test update tenant description: UPDATED");
		svcTenant.updateTenant(updated);
		verify(tenantDAO, atLeastOnce()).update(updated);
	}

	@Test
	public void shouldUpdateTenantState() {
		String alias = "updateTenantState";
		svcTenant.updateTenantState(alias, EOSState.INACTIVE);
		verify(tenantDAO, atLeastOnce()).update(alias, EOSState.INACTIVE);
	}

	@Test
	public void shouldPurgeTenant() {
		String alias = "purgeTenant";
		svcTenant.purgeTenant(alias);
		verify(tenantDAO, atLeastOnce()).purgeTenant(alias);
	}

	// Tenant Data

	/*
	@Test
	public void testUpdateTenantData() {
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
	public void testListTenantDataByKeys() {
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
	public void testListTenantData() {
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
	// Util
	
	private EOSUser buildUser(String tenantMail) {
		return new EOSUser().setLogin("test.user").setFirstName("Test").setLastName("User")
				.setPersonalMail("test@personal.com").setEmail(tenantMail).setState(EOSState.ACTIVE);
	}
	
	private EOSTenant buildTenant(String alias) {
		return new EOSTenant().setAlias(alias).setName("Tenant name for" + alias)
				.setDescription("Tenant description for " + alias);
	}
	
}
