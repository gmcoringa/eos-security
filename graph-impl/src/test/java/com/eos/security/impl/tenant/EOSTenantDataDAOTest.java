package com.eos.security.impl.tenant;

import static com.eos.security.impl.test.util.EOSTestUtil.registerScope;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eos.security.impl.service.DataBaseServer;
import com.eos.security.impl.service.TransactionManager;
import com.eos.security.impl.service.internal.EOSTransactionThreadLocalScope;
import com.eos.security.impl.service.internal.TransactionManagerImpl;
import com.eos.security.impl.service.tenant.EOSTenantDAO;
import com.eos.security.impl.service.tenant.EOSTenantDataDAO;
import com.eos.security.impl.test.util.TestDataBaseServer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { EOSTenantDAO.class, EOSTenantDataDAO.class, TransactionManagerImpl.class,
		TestDataBaseServer.class, EOSTransactionThreadLocalScope.class })
public class EOSTenantDataDAOTest {

	@Autowired
	private EOSTenantDAO tenantDAO;
	@Autowired
	private EOSTenantDataDAO tenantDataDAO;
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
	public void shouldCreateTenantData() {
		String key = "create-key", value = "create-value", tenantAlias = "data-create";
		manager.begin();
		EOSTenantDAOUtil.create(tenantAlias, tenantDAO);
		tenantDataDAO.createTenantData(tenantAlias, key, value);
		String created = tenantDataDAO.findTenantDataValue(tenantAlias, key);
		manager.commit();

		assertEquals("DAO - create tenant data", value, created);
	}

	@Test
	public void shouldUpdateTenantData() {
		String key = "update-key", value = "update-value", newValue = "new-value", tenantAlias = "data-update";
		manager.begin();
		EOSTenantDAOUtil.create(tenantAlias, tenantDAO);
		tenantDataDAO.createTenantData(tenantAlias, key, value);
		tenantDataDAO.updateTenantData(tenantAlias, key, newValue);
		String updated = tenantDataDAO.findTenantDataValue(tenantAlias, key);
		manager.commit();

		assertEquals("DAO - update tenant data", newValue, updated);
	}

	@Test
	public void shouldDeleteTenantData() {
		String key = "delete-key", value = "delete-value", tenantAlias = "data-delete";
		manager.begin();
		EOSTenantDAOUtil.create(tenantAlias, tenantDAO);
		tenantDataDAO.createTenantData(tenantAlias, key, value);
		tenantDataDAO.deleteTenantData(tenantAlias, Sets.newSet(key));
		String deleted = tenantDataDAO.findTenantDataValue(tenantAlias, key);
		manager.commit();

		Assert.assertNull("DAO - delete tenant data", deleted);
	}

	@Test
	public void shouldFindTenantData() {
		String key = "find-key", value = "find-value", tenantAlias = "data-find";
		manager.begin();
		EOSTenantDAOUtil.create(tenantAlias, tenantDAO);
		tenantDataDAO.createTenantData(tenantAlias, key, value);
		String found = tenantDataDAO.findTenantDataValue(tenantAlias, key);
		manager.commit();

		assertEquals("DAO - find tenant data", value, found);
	}

	@Test
	public void shouldFindTenantDataByKeys() {
		String tenantAlias =  "data-find-keys";
		Map<String, String> expected = new HashMap<>(2);
		expected.put("key1", "value1");
		expected.put("key2", "value2");
		
		
		manager.begin();
		EOSTenantDAOUtil.create(tenantAlias, tenantDAO);
		
		for(Entry<String, String> entry : expected.entrySet()){
			tenantDataDAO.createTenantData(tenantAlias, entry.getKey(), entry.getValue());
		}
		
		Map<String, String> found = tenantDataDAO.findTenantDataValues(tenantAlias, expected.keySet());
		manager.commit();

		assertEquals("DAO - find tenant data by keys", expected, found);
	}

	@Test
	public void shouldListAllTenantData() {
		String tenantAlias =  "data-list";
		Map<String, String> expected = new HashMap<>(2);
		expected.put("key1", "value1");
		expected.put("key2", "value2");
		
		
		manager.begin();
		EOSTenantDAOUtil.create(tenantAlias, tenantDAO);
		
		for(Entry<String, String> entry : expected.entrySet()){
			tenantDataDAO.createTenantData(tenantAlias, entry.getKey(), entry.getValue());
		}
		
		Map<String, String> found = tenantDataDAO.listTenantData(tenantAlias, 5, 0);
		manager.commit();

		assertEquals("DAO - find tenant data by keys", expected, found);
	}
}
