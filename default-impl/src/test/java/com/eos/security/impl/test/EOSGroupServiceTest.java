/**
 * 
 */
package com.eos.security.impl.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eos.common.EOSLevel;
import com.eos.common.exception.EOSDuplicatedEntryException;
import com.eos.common.exception.EOSException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.service.EOSGroupService;
import com.eos.security.api.service.EOSSecurityService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.vo.EOSGroup;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.test.util.EOSTestUtil;

/**
 * Test class for Group Service.
 * 
 * @author santos.fabiano
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-test.xml" })
public class EOSGroupServiceTest {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private EOSGroupService svcGroup;
	@Autowired
	private EOSSecurityService svcSecurity;
	@Autowired
	private EOSUserService svcUser;

	@Before
	public void setUp() throws EOSException {
		EOSTestUtil.setup(context);
	}

	@Test
	public void testCreateGroup() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException {
		EOSGroup group = new EOSGroup().setName("Test Create").setDescription("Create description")
				.setLevel(EOSLevel.PUBLIC.getLevel());
		group = svcGroup.createGroup(group);
		Assert.assertNotNull("Create: group Id not null", group.getId());
		Assert.assertNotNull("Create: tenant Id not null", group.getTenantId());
	}

	@Test
	public void testFindGroup() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSNotFoundException, EOSValidationException {
		EOSGroup group = EOSTestUtil.createGroup("Find", svcGroup);
		EOSGroup find = svcGroup.findGroup(group.getId());
		Assert.assertEquals("Find Group", group.getId(), find.getId());
	}

	@Test
	public void testFindMultiple() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException {
		EOSGroup group1 = EOSTestUtil.createGroup("Find Multiple 1", svcGroup);
		EOSGroup group2 = EOSTestUtil.createGroup("Find Multiple 2", svcGroup);
		List<Long> ids = new ArrayList<>(2);

		ids.add(group1.getId());
		ids.add(group2.getId());

		List<EOSGroup> groups = svcGroup.findGroups(ids);
		Assert.assertEquals("Mult find size", 2, groups.size());
		Assert.assertEquals(group1.getId(), groups.get(0).getId());
		Assert.assertEquals(group2.getId(), groups.get(1).getId());
	}

	@Test
	public void testListGroups() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException {
		EOSTestUtil.createGroup("List 1", svcGroup);
		EOSTestUtil.createGroup("List 2", svcGroup);
		List<EOSGroup> groups = svcGroup.listGroups(null, null, 50, 0);
		Assert.assertTrue("Size higher or equals than 2", groups.size() >= 2);
	}

	@Test
	public void testDeleteGroup() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSNotFoundException, EOSValidationException {
		EOSGroup group = EOSTestUtil.createGroup("Delete", svcGroup);
		svcGroup.deleteGroup(group.getId());
		EOSGroup deleted = null;

		try {
			deleted = svcGroup.findGroup(group.getId());
		} catch (EOSNotFoundException e) {
			// exception expected
		}

		Assert.assertNull(deleted);
	}

	/**
	 * Tests add multiples users to one group. Also tests listing users by
	 * group.
	 * 
	 * @throws EOSDuplicatedEntryException
	 * @throws EOSForbiddenException
	 * @throws EOSUnauthorizedException
	 * @throws EOSValidationException
	 */
	@Test
	public void testAddFindUsersGroup() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSGroup group = EOSTestUtil.createGroup("Add Users", svcGroup);
		EOSUser user1 = EOSTestUtil.createUser("groupaddUsers1", null, svcUser);
		EOSUser user2 = EOSTestUtil.createUser("groupaddUsers2", null, svcUser);
		List<String> userLogins = Arrays.asList(user1.getLogin(), user2.getLogin());

		svcGroup.addUsersToGroup(group.getId(), userLogins);
		List<EOSUser> users = svcGroup.listGroupUsers(group.getId(), 5, 0);
		Assert.assertEquals("Group Users Add size", 2, users.size());
		// Validate users
		Assert.assertTrue("Group Users Add contains", userLogins.contains(users.get(0).getLogin()));
		Assert.assertTrue("Group Users Add contains", userLogins.contains(users.get(1).getLogin()));
	}

	/**
	 * Tests add one user in multiples groups. Also tests listing groups by
	 * user.
	 * 
	 * @throws EOSDuplicatedEntryException
	 * @throws EOSForbiddenException
	 * @throws EOSUnauthorizedException
	 * @throws EOSValidationException
	 */
	@Test
	public void testAddFindGroupsUser() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSUser user = EOSTestUtil.createUser("addgroupsUser", null, svcUser);
		EOSGroup group1 = EOSTestUtil.createGroup("Add Find Groups User 1", svcGroup);
		EOSGroup group2 = EOSTestUtil.createGroup("Add Find Groups User 2", svcGroup);
		List<Long> groupIds = Arrays.asList(group1.getId(), group2.getId());
		// Test it
		svcGroup.addUsersInGroup(groupIds, user.getLogin());
		// Validate
		List<EOSGroup> groups = svcGroup.listUserGroups(user.getLogin(), 5, 0);
		Assert.assertEquals("User Groups Add size", 2, groups.size());
		Assert.assertTrue("User Groups contains", groupIds.contains(groups.get(0).getId()));
		Assert.assertTrue("User Groups contains", groupIds.contains(groups.get(1).getId()));
	}

	/**
	 * Tests remove multiples users from one group. Also tests listing users by
	 * group.
	 * 
	 * @throws EOSDuplicatedEntryException
	 * @throws EOSForbiddenException
	 * @throws EOSUnauthorizedException
	 * @throws EOSValidationException
	 */
	@Test
	public void testRemoveUsersFromGroup() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSGroup group = EOSTestUtil.createGroup("Remove Users", svcGroup);
		EOSUser user1 = EOSTestUtil.createUser("groupremoveUsers1", null, svcUser);
		EOSUser user2 = EOSTestUtil.createUser("groupremoveUsers2", null, svcUser);
		EOSUser user3 = EOSTestUtil.createUser("groupremoveUsers3", null, svcUser);
		List<String> userLogins = Arrays.asList(user1.getLogin(), user2.getLogin(), user3.getLogin());

		svcGroup.addUsersToGroup(group.getId(), userLogins);
		List<EOSUser> users = svcGroup.listGroupUsers(group.getId(), 5, 0);
		Assert.assertEquals("Group Users Remove size", 3, users.size());
		// Remove users
		userLogins = Arrays.asList(user2.getLogin(), user3.getLogin());
		svcGroup.removeUsersFromGroup(group.getId(), userLogins);
		// Validate users, only user1 must be on list
		users = svcGroup.listGroupUsers(group.getId(), 5, 0);
		Assert.assertEquals("Group Users Remove size", 1, users.size());
		Assert.assertEquals("Group Users Remove validate", users.get(0).getLogin(), user1.getLogin());
	}

	/**
	 * Tests remove one user from multiples groups. Also tests listing groups by
	 * user.
	 * 
	 * @throws EOSDuplicatedEntryException
	 * @throws EOSForbiddenException
	 * @throws EOSUnauthorizedException
	 * @throws EOSValidationException
	 */
	@Test
	public void testRemoveUserFromGroups() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSUser user = EOSTestUtil.createUser("removegroupsUser", null, svcUser);
		EOSGroup group1 = EOSTestUtil.createGroup("Remove Groups User 1", svcGroup);
		EOSGroup group2 = EOSTestUtil.createGroup("Remove Groups User 2", svcGroup);
		EOSGroup group3 = EOSTestUtil.createGroup("Remove Groups User 3", svcGroup);
		List<Long> groupIds = Arrays.asList(group1.getId(), group2.getId(), group3.getId());
		// Test it
		svcGroup.addUsersInGroup(groupIds, user.getLogin());
		// Validate
		List<EOSGroup> groups = svcGroup.listUserGroups(user.getLogin(), 5, 0);
		Assert.assertEquals("User Groups Remove size", 3, groups.size());
		// Remove groups
		groupIds = Arrays.asList(group2.getId(), group3.getId());
		svcGroup.removeUserFromGroups(groupIds, user.getLogin());
		// Validate groups, only group 1 must be on list
		groups = svcGroup.listUserGroups(user.getLogin(), 5, 0);
		Assert.assertEquals("User Groups Remove size", 1, groups.size());
		Assert.assertEquals("User Groups  Remove validate", groups.get(0).getId(), group1.getId());
	}

	@Test
	public void testRemoveGroupsByUser() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSUser user = EOSTestUtil.createUser("removeGroupsByUser", null, svcUser);
		EOSGroup group1 = EOSTestUtil.createGroup("Remove Groups By User 1", svcGroup);
		EOSGroup group2 = EOSTestUtil.createGroup("Remove Groups By User 2", svcGroup);
		List<Long> groupIds = Arrays.asList(group1.getId(), group2.getId());
		// Test it
		svcGroup.addUsersInGroup(groupIds, user.getLogin());
		// Validate
		List<EOSGroup> groups = svcGroup.listUserGroups(user.getLogin(), 5, 0);
		Assert.assertEquals("User Groups Remove By User size", 2, groups.size());
		// Remove groups
		svcGroup.removeGroupsByUser(user.getLogin());
		// Validate
		groups = svcGroup.listUserGroups(user.getLogin(), 5, 0);
		Assert.assertTrue("User Groups Remove By User: empty list", groups.isEmpty());

	}

}
