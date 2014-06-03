/**
 * 
 */
package com.eos.security.impl.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eos.common.EOSState;
import com.eos.common.exception.EOSDuplicatedEntryException;
import com.eos.common.exception.EOSException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.service.EOSGroupService;
import com.eos.security.api.service.EOSRoleService;
import com.eos.security.api.service.EOSSecurityService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.vo.EOSGroup;
import com.eos.security.api.vo.EOSRole;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.dao.EOSUserTenantDAO;
import com.eos.security.impl.test.util.EOSTestUtil;

/**
 * Test class for user service.
 * 
 * @author santos.fabiano
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-test.xml" })
public class EOSUserServiceTest {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private EOSUserService svcUser;
	@Autowired
	private EOSSecurityService svcSecurity;
	@Autowired
	private EOSUserTenantDAO userTenantDAO;
	@Autowired
	private EOSGroupService svcGroup;
	@Autowired
	private EOSRoleService svcRole;

	@Before
	public void setUp() throws EOSException {
		EOSTestUtil.setup(context);
	}

	@Test
	public void testCreateUser() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException {
		EOSUser user = new EOSUser();
		user.setLogin("user-create").setEmail("user@create.com").setFirstName("Create F").setLastName("Create L")
				.setNickName("Create N").setPersonalMail("create@personal.com");

		user = svcUser.createUser(user, null);
		Assert.assertEquals("Expected state after creation", EOSState.INACTIVE, user.getState());
	}

	@Test
	public void testFindUser() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSNotFoundException, EOSValidationException {
		EOSUser user = EOSTestUtil.createUser("find", null, svcUser);
		EOSUser find = svcUser.findUser(user.getLogin());
		Assert.assertNotNull("Find User", find);
		Assert.assertEquals("Find User", user.getLogin(), find.getLogin());
	}

	@Test
	public void testFindTenantUser() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSNotFoundException, EOSValidationException {
		EOSUser user = EOSTestUtil.createUser("findtenant", null, svcUser);
		EOSUser find = svcUser.findTenantUser(user.getLogin(), "notFoundUser");
		Assert.assertNull("Find Tenant User other tenant", find);
		// Now find for the current tenant
		find = svcUser.findTenantUser(user.getLogin(), user.getTenantAlias());
		Assert.assertNotNull("Find Tenant User current tenant", find);
		Assert.assertEquals("Find User", user.getLogin(), find.getLogin());

	}

//	@Test
	public void testFindUsers() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException {
		EOSUser user1 = EOSTestUtil.createUser("finds1", null, svcUser);
		EOSUser user2 = EOSTestUtil.createUser("finds2", null, svcUser);

		List<EOSUser> users = svcUser.findUsers(Arrays.asList(user1.getLogin(), user2.getLogin()));
		Assert.assertEquals("Find users list size", 2, users.size());
		Assert.assertEquals("Find users User1", users.get(0).getLogin(), user1.getLogin());
		Assert.assertEquals("Find users User2", users.get(1).getLogin(), user2.getLogin());
	}

//	@Test
	public void testUpdateUser() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSNotFoundException, EOSValidationException {
		EOSUser user = EOSTestUtil.createUser("update", null, svcUser);
		// Change names and emails
		user.setFirstName("Updated F. Name").setLastName("Update L. Name").setEmail("updated@tenant.mail")
				.setPersonalMail("updated@personal.mail");

		svcUser.updateUser(user);
		// force cache clearing
		// clearCache();
		EOSUser saved = svcUser.findUser(user.getLogin());
		Assert.assertEquals("Update user: first name", user.getFirstName(), saved.getFirstName());
		Assert.assertEquals("Update user: last name", user.getLastName(), saved.getLastName());
		Assert.assertEquals("Update user: tenant mail", user.getEmail(), saved.getEmail());
		Assert.assertEquals("Update user: personal mail", user.getPersonalMail(), saved.getPersonalMail());
	}

//	@Test
	public void testUpdateState() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException, EOSNotFoundException {
		EOSUser user = EOSTestUtil.createUser("update-state", null, svcUser);
		svcUser.updateUserState(user.getLogin(), EOSState.DISABLED);
		EOSUser updated = svcUser.findUser(user.getLogin());
		Assert.assertEquals("Update user state", EOSState.DISABLED, updated.getState());
	}

//	@Test
	public void testPurgeUser() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException, EOSNotFoundException {
		Map<String, String> userData = new HashMap<>(2);
		userData.put("key1", "value1");
		userData.put("key2", "value2");
		EOSUser user = EOSTestUtil.createUser("purgeUser", userData, svcUser);
		List<String> logins = Arrays.asList(user.getLogin());
		// Create user relations
		EOSGroup group = EOSTestUtil.createGroup("purgeUser", svcGroup);
		EOSRole role = EOSTestUtil.createRole("purgeUser", svcRole);
		svcGroup.addUsersToGroup(group.getId(), logins);
		svcRole.addUsersToRole(role.getCode(), logins);
		// disable user
		svcUser.updateUserState(user.getLogin(), EOSState.DISABLED);
		// Purge it and validate
		svcUser.purgeUser(user.getLogin());
		try {
			user = svcUser.findUser(user.getLogin());
			Assert.assertNull("Purge User: find purged", user);
		} catch (EOSNotFoundException e) {
			// exception expected :)
		}
	}

//	@Test
	public void testListUser() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException {
		EOSTestUtil.createUser("list-1", null, svcUser);
		EOSTestUtil.createUser("list-2", null, svcUser);
		List<EOSUser> users = svcUser.listUsers(null, 5, 0);
		Assert.assertTrue("List users size", users.size() >= 2);
	}

//	@Test
	public void testSetPassword() throws EOSException {
		EOSUser user = EOSTestUtil.createUser("setPassword", null, svcUser);
		String password = "password";
		svcUser.setUserPassword(user.getLogin(), null, password);

		try {
			// EOSTestUtil.setup(context, user.getTenantId(), user);
			svcUser.setUserPassword(user.getLogin(), password, "newPassword");
		} catch (EOSForbiddenException | EOSUnauthorizedException | EOSValidationException e) {
			Assert.fail("Logged user change password failed: " + e.getMessage());
		}
		// Restore context.
		EOSTestUtil.setup(context);
	}

//	@Test
	public void testCheckForLogin() throws EOSException {
		EOSUser user = EOSTestUtil.createUser("checkForLogin", null, svcUser);
		String password = "password";
		svcUser.setUserPassword(user.getLogin(), null, password);
		EOSUser checked = svcUser.checkForLogin(user.getLogin(), null, password);
		Assert.assertEquals("Checked user", checked.getLogin(), user.getLogin());
	}

	// User Data

//	@Test
	public void createUserData() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException {
		Map<String, String> userData = new HashMap<>(1);
		userData.put("key", "value");

		EOSUser user = EOSTestUtil.createUser("create-user-data", userData, svcUser);
		String value = svcUser.findUserData(user.getLogin(), "key");
		Assert.assertEquals("Create User Data", "value", value);
	}

//	@Test
	public void updateUserData() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException {
		Map<String, String> userData = new HashMap<>(2);
		userData.put("key1", "value1");
		userData.put("key2", "value2");

		EOSUser user = EOSTestUtil.createUser("update-user-data", userData, svcUser);
		Assert.assertEquals("Update user data: create-size", 2, svcUser.listUserData(user.getLogin(), 5, 0).size());

		// Clear and rebuild user data
		userData.clear();
		userData.put("key1", "newValue");
		userData.put("key2", ""); // Set to be removed
		userData.put("key3", "value3"); // New value
		userData.put("key4", "value4"); // New value

		// Validations
		svcUser.updateUserData(user.getLogin(), userData);
		userData = svcUser.listUserData(user.getLogin(), 5, 0);
		Assert.assertEquals("user data update size", 3, userData.size());
		// user data with key1 check new value
		String value = svcUser.findUserData(user.getLogin(), "key1");
		Assert.assertEquals("tenant data update key1", "newValue", value);
	}

//	@Test
	public void listUserData() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException {
		Map<String, String> userData = new HashMap<>(2);
		userData.put("key1", "value1");
		userData.put("key2", "value2");

		EOSUser user = EOSTestUtil.createUser("list-user-data", userData, svcUser);
		Assert.assertEquals("Update user data: create-size", 2, svcUser.listUserData(user.getLogin(), 5, 0).size());
	}

//	@Test
	public void listUserDataByKey() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		Map<String, String> userData = new HashMap<>(3);
		userData.put("key1", "value1");
		userData.put("key2", "value2");
		userData.put("key3", "value3");
		List<String> keys = new ArrayList<>();
		keys.add("key1");
		keys.add("key2");

		EOSUser user = EOSTestUtil.createUser("list-key-user-data", userData, svcUser);
		// Clear user data after creation
		userData.clear();
		// reload user data
		userData = svcUser.listUserData(user.getLogin(), keys);
		Assert.assertEquals("Update user data: create-size", 3, svcUser.listUserData(user.getLogin(), 5, 0).size());
		Assert.assertTrue("User data - contains key1", userData.containsKey("key1"));
		Assert.assertTrue("User data - contains key1", userData.containsKey("key2"));
	}

}
