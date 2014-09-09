/**
 * 
 */
package com.eos.security.impl.tenant;

import static com.eos.security.impl.test.util.EOSTestUtil.buildTenant;
import static com.eos.security.impl.test.util.EOSTestUtil.buildUser;
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
	public void shouldThrowEOSNotFoundExceptionWhenTenantDosNotExists() {
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

	@Test
	public void shouldRemoveUpdateAndCreateTenantData() {
		String tenantAlias = "tenantUpdateData";
		Set<String> keys = Sets.newSet("key1", "key2", "key3", "key4", "key5");
		Set<String> removeKeys = Sets.newSet("key3", "key4");
		Map<String, String> currentTenantData = new HashMap<>(4);
		currentTenantData.put("key1", "value1");
		currentTenantData.put("key2", "value2");
		currentTenantData.put("key3", "value3");
		currentTenantData.put("key4", "value4");
		when(tenantDataDAO.findTenantDataValues(tenantAlias, keys)).thenReturn(currentTenantData);

		// Test it
		Map<String, String> updateTenantData = new HashMap<>(5);
		updateTenantData.put("key1", "value1");
		updateTenantData.put("key2", "newValue2");
		updateTenantData.put("key3", null);
		updateTenantData.put("key4", "");
		updateTenantData.put("key5", "value5");
		svcTenant.updateTenantData(tenantAlias, updateTenantData);

		verify(tenantDataDAO, atLeastOnce()).updateTenantData(tenantAlias, "key2", "newValue2");
		verify(tenantDataDAO, atLeastOnce()).createTenantData(tenantAlias, "key5", "value5");
		verify(tenantDataDAO, atLeastOnce()).deleteTenantData(tenantAlias, removeKeys);
	}

	@Test
	public void shouldFindTenantData() {
		String tenantAlias = "findTenantData", key = "findKey", value = "value";
		when(tenantDataDAO.findTenantDataValue(tenantAlias, key)).thenReturn(value);

		// Test it
		String tenantData = svcTenant.findTenantData(tenantAlias, key);
		assertEquals("Find tenant data", value, tenantData);
	}

	@Test
	public void shouldFindTenantDataByKeys() {
		String tenantAlias = "findTenantDataByKeys";
		Set<String> keys = Sets.newSet("key1", "key2", "key3");
		Map<String, String> expectedTenantData = new HashMap<>(4);
		expectedTenantData.put("key1", "value1");
		expectedTenantData.put("key2", "value2");
		expectedTenantData.put("key3", "value3");
		expectedTenantData.put("key4", "value4");
		when(tenantDataDAO.findTenantDataValues(tenantAlias, keys)).thenReturn(expectedTenantData);

		// Test it
		Map<String, String> tenantData = svcTenant.listTenantData(tenantAlias, keys);
		assertEquals("List tenant data by keys", expectedTenantData, tenantData);
	}

	@Test
	public void shouldListTenantData() {
		String tenantAlias = "listTenantData";
		Map<String, String> expectedTenantData = new HashMap<>(3);
		expectedTenantData.put("key1", "value1");
		expectedTenantData.put("key2", "value2");
		expectedTenantData.put("key3", "value3");
		when(tenantDataDAO.listTenantData(tenantAlias, 5, 0)).thenReturn(expectedTenantData);

		// Test it
		Map<String, String> tenantData = svcTenant.listTenantData(tenantAlias, 5, 0);
		assertEquals("List tenant data by keys", expectedTenantData, tenantData);
	}

	// Utilities

}
