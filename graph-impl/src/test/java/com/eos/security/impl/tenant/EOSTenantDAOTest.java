package com.eos.security.impl.tenant;

import static com.eos.security.impl.tenant.EOSTenantDAOUtil.create;
import static com.eos.security.impl.test.util.EOSTestUtil.buildTenant;
import static com.eos.security.impl.test.util.EOSTestUtil.registerScope;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eos.common.EOSState;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.impl.service.DataBaseServer;
import com.eos.security.impl.service.TransactionManager;
import com.eos.security.impl.service.internal.EOSTransactionThreadLocalScope;
import com.eos.security.impl.service.tenant.EOSTenantDAO;
import com.eos.security.impl.test.util.TestDataBaseServer;
import com.eos.security.impl.transaction.TransactionManagerImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { EOSTenantDAO.class, TransactionManagerImpl.class, TestDataBaseServer.class,
		EOSTransactionThreadLocalScope.class })
public class EOSTenantDAOTest {

	@Autowired
	private EOSTenantDAO tenantDAO;
	@Autowired
	private DataBaseServer dataBaseServer;
	@Autowired
	ApplicationContext context;
	@Autowired
	private TransactionManager manager;

	@PostConstruct
	public void init() {
		registerScope(context, dataBaseServer);
	}

	@Test
	public void shouldCreateTenant() {
		EOSTenant tenant = buildTenant("create-tenant").setState(EOSState.DISABLED);
		manager.begin();
		EOSTenant created = tenantDAO.create(tenant);
		manager.commit();
		assertEquals("DAO - create tenant", tenant, created);
	}

	@Test
	public void shouldFindTenantByAlias() {
		manager.begin();
		String alias = "find-tenant";
		EOSTenant tenant = create(alias, tenantDAO);
		EOSTenant found = tenantDAO.find(alias);
		manager.commit();
		assertEquals("DAO - find tenant", tenant, found);
	}

	@Test
	public void shouldFindTenantByAliases() {
		manager.begin();
		Set<EOSTenant> tenants = Sets.newSet(create("find-alias1", tenantDAO), create("find-alias2", tenantDAO));
		Set<EOSTenant> tenantsFound = tenantDAO.findTenants(Sets.newSet("find-alias1", "find-alias2"));
		manager.commit();
		assertEquals("DAO - find tenant by aliases", tenants, tenantsFound);
	}

	@Test
	public void shouldUpdateTenantExceptAlias() {
		manager.begin();
		String alias = "update-tenant";
		EOSTenant tenant = create(alias, tenantDAO).setDescription("Updated description").setName("updated name")
				.setState(EOSState.ACTIVE);
		EOSTenant updated = tenantDAO.update(tenant);
		manager.commit();
		assertNotEquals("DAO - update tenant", tenant, updated);
		tenant.setState(EOSState.DISABLED);
		assertEquals("DAO - update tenant", tenant, updated);

	}

	@Test
	public void shouldUpdateOnlyTenantState() {
		manager.begin();
		String alias = "update-tenant-state";
		EOSTenant tenant = create(alias, tenantDAO);
		EOSTenant updated = tenantDAO.update(alias, EOSState.ACTIVE);
		manager.commit();
		assertNotEquals("DAO - update tenant states", tenant, updated);
		assertEquals("DAO - update tenant states", EOSState.ACTIVE, updated.getState());
	}

	@Test
	public void shouldListAllTenants() {
		manager.begin();
		Set<EOSTenant> tenants = Sets.newSet(create("list-alias1", tenantDAO), create("list-alias2", tenantDAO));
		Set<EOSTenant> tenantsFound = tenantDAO.listTenants(null, 200, 0);
		manager.commit();
		assertTrue("DAO - list all tenants", tenantsFound.containsAll(tenants));
	}

	@Test
	public void shouldListTenantsByState() {
		manager.begin();
		EOSTenant active = create("listState-alias1", EOSState.ACTIVE, tenantDAO);
		EOSTenant disabled = create("listState-alias1", EOSState.DISABLED, tenantDAO);
		EOSTenant inactive = create("listState-alias1", EOSState.INACTIVE, tenantDAO);
		Set<EOSTenant> tenantsFound = tenantDAO.listTenants(Sets.newSet(EOSState.ACTIVE, EOSState.INACTIVE), 200, 0);
		manager.commit();

		assertTrue("DAO - list by state active", tenantsFound.contains(active));
		assertTrue("DAO - list by state inactive", tenantsFound.contains(inactive));
		assertFalse("DAO - list by state disabled", tenantsFound.contains(disabled));
	}

	@Test
	public void shouldPurgeTenant() {
		String tenantAlias = "purge-tenant";
		manager.begin();
		create(tenantAlias, tenantDAO);
		tenantDAO.purgeTenant(tenantAlias);
		EOSTenant purged = tenantDAO.find(tenantAlias);
		manager.commit();

		assertNull("DAO - purge tenant", purged);
	}
}
