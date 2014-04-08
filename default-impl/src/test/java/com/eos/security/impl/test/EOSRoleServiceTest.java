/**
 * 
 */
package com.eos.security.impl.test;

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
import com.eos.security.api.service.EOSRoleService;
import com.eos.security.api.service.EOSSecurityService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.vo.EOSGroup;
import com.eos.security.api.vo.EOSRole;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.test.util.EOSTestUtil;

/**
 * Test class for Role services.
 * 
 * @author santos.fabiano
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-test.xml" })
public class EOSRoleServiceTest {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private EOSGroupService svcGroup;
	@Autowired
	private EOSSecurityService svcSecurity;
	@Autowired
	private EOSUserService svcUser;
	@Autowired
	private EOSRoleService svcRole;

	@Before
	public void setUp() throws EOSException {
		EOSTestUtil.setup(context);
	}

	// ROLE

	@Test
	public void testCreateFindRole() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException, EOSNotFoundException {
		EOSRole role = new EOSRole().setCode("createRole-code").setDescription("Create Description")
				.setLevel(EOSLevel.MAXIMUM.getLevel());

		role = svcRole.createRole(role);
		EOSRole find = svcRole.findRole(role.getCode());
		Assert.assertEquals("Create / find", role.getCode(), find.getCode());
	}

	@Test
	public void testFindMultiple() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException {
		EOSRole role1 = EOSTestUtil.createRole("mfindRole1", svcRole);
		EOSRole role2 = EOSTestUtil.createRole("mfindRole2", svcRole);
		List<String> codes = Arrays.asList(role1.getCode(), role2.getCode());
		List<EOSRole> roles = svcRole.findRoles(codes);
		Assert.assertEquals("Find Multiple size", 2, roles.size());
		Assert.assertTrue("Find Multiple contains", codes.contains(roles.get(0).getCode()));
		Assert.assertTrue("Find Multiple contains", codes.contains(roles.get(1).getCode()));
	}

	@Test
	public void testUpdateRole() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException, EOSNotFoundException {
		EOSRole role = EOSTestUtil.createRole("updateRole", svcRole);
		role.setDescription("Description updated");

		svcRole.updateRole(role);
		EOSRole updated = svcRole.findRole(role.getCode());
		Assert.assertEquals("Update Role code", role.getCode(), updated.getCode());
		Assert.assertEquals("Update role description", role.getDescription(), updated.getDescription());
	}

	@Test
	public void testDeleteRole() throws EOSDuplicatedEntryException, EOSForbiddenException, EOSUnauthorizedException,
			EOSValidationException {
		EOSRole role = EOSTestUtil.createRole("deleteRole", svcRole);
		svcRole.deleteRole(role.getCode());
		;
		try {
			EOSRole deleted = svcRole.findRole(role.getCode());
			Assert.assertNull("Delete Role", deleted);
		} catch (EOSNotFoundException e) {
			// exception expected
		}
	}

	// Role User

	@Test
	public void testAddListUserRoles() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSRole role1 = EOSTestUtil.createRole("addUserRoles1", svcRole);
		EOSRole role2 = EOSTestUtil.createRole("addUserRoles2", svcRole);
		EOSUser user = EOSTestUtil.createUser("addUserRoles", null, svcUser);
		List<String> codes = Arrays.asList(role1.getCode(), role2.getCode());

		svcRole.addRolesToUser(user.getLogin(), codes);
		List<EOSRole> roles = svcRole.listUserRoles(user.getLogin(), 5, 0);
		Assert.assertEquals("Add / List User Roles: size", 2, roles.size());
		Assert.assertTrue("Add / List User Roles: contains", codes.contains(roles.get(0).getCode()));
		Assert.assertTrue("Add / List User Roles: contains", codes.contains(roles.get(1).getCode()));
	}

	@Test
	public void testRemoveUserRoles() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSRole role1 = EOSTestUtil.createRole("delUserRoles1", svcRole);
		EOSRole role2 = EOSTestUtil.createRole("delUserRoles2", svcRole);
		EOSRole role3 = EOSTestUtil.createRole("delUserRoles3", svcRole);
		EOSUser user = EOSTestUtil.createUser("delUserRoles", null, svcUser);
		List<String> codes = Arrays.asList(role1.getCode(), role2.getCode(), role3.getCode());

		svcRole.addRolesToUser(user.getLogin(), codes);
		List<EOSRole> roles = svcRole.listUserRoles(user.getLogin(), 5, 0);
		Assert.assertEquals("Remove User Roles: size", 3, roles.size());
		// Now remove and test
		codes = Arrays.asList(role2.getCode(), role3.getCode());
		svcRole.removeRolesFromUser(user.getLogin(), codes);
		roles = svcRole.listUserRoles(user.getLogin(), 5, 0);
		Assert.assertEquals("Remove User Roles: size removed", 1, roles.size());
		Assert.assertEquals("Remove User Roles: contains", roles.get(0).getCode(), role1.getCode());
	}

	@Test
	public void testAddListRoleUsers() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSRole role = EOSTestUtil.createRole("addListRoleUsers", svcRole);
		EOSUser user1 = EOSTestUtil.createUser("addListRoleUsers1", null, svcUser);
		EOSUser user2 = EOSTestUtil.createUser("addListRoleUsers2", null, svcUser);
		List<String> logins = Arrays.asList(user1.getLogin(), user2.getLogin());
		svcRole.addUsersToRole(role.getCode(), logins);
		List<EOSUser> users = svcRole.listRoleUsers(role.getCode(), 5, 0);
		Assert.assertEquals("Add / List Role Users: size", 2, users.size());
		Assert.assertTrue("Add / List Role Users: contains", logins.contains(users.get(0).getLogin()));
		Assert.assertTrue("Add / List Role Users: contains", logins.contains(users.get(1).getLogin()));
	}

	@Test
	public void testRemoveRoleUsers() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSRole role = EOSTestUtil.createRole("delListRoleUsers", svcRole);
		EOSUser user1 = EOSTestUtil.createUser("delListRoleUsers1", null, svcUser);
		EOSUser user2 = EOSTestUtil.createUser("delListRoleUsers2", null, svcUser);
		EOSUser user3 = EOSTestUtil.createUser("delListRoleUsers3", null, svcUser);
		List<String> logins = Arrays.asList(user1.getLogin(), user2.getLogin(), user3.getLogin());
		svcRole.addUsersToRole(role.getCode(), logins);
		List<EOSUser> users = svcRole.listRoleUsers(role.getCode(), 5, 0);
		Assert.assertEquals("Remove Role Users: size", 3, users.size());
		// Now remove and test
		logins = Arrays.asList(user2.getLogin(), user3.getLogin());
		svcRole.removeUsersFromRole(role.getCode(), logins);
		users = svcRole.listRoleUsers(role.getCode(), 5, 0);
		Assert.assertEquals("Remove Role Users: size removed", 1, users.size());
		Assert.assertEquals("Remove Role Users: contains", users.get(0).getLogin(), user1.getLogin());
	}

	@Test
	public void testRemoveRolesByUser() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSUser user = EOSTestUtil.createUser("RemoveRolesByUser", null, svcUser);
		EOSRole role1 = EOSTestUtil.createRole("RemoveRolesByUser1", svcRole);
		EOSRole role2 = EOSTestUtil.createRole("RemoveRolesByUser2", svcRole);
		svcRole.addRolesToUser(user.getLogin(), Arrays.asList(role1.getCode(), role2.getCode()));
		List<EOSRole> roles = svcRole.listUserRoles(user.getLogin(), 5, 0);
		Assert.assertEquals("Remove Roles By User: size", 2, roles.size());
		// Remove and validate
		svcRole.removeRolesByUser(user.getLogin());
		roles = svcRole.listUserRoles(user.getLogin(), 5, 0);
		Assert.assertTrue("Remove Roles By User: empty", roles.isEmpty());
	}

	// Role Group

	@Test
	public void testAddListRoleGroups() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSRole role = EOSTestUtil.createRole("addListRoleGroups", svcRole);
		EOSGroup group1 = EOSTestUtil.createGroup("addListRoleGroups1", svcGroup);
		EOSGroup group2 = EOSTestUtil.createGroup("addListRoleGroups2", svcGroup);
		List<Long> groupIds = Arrays.asList(group1.getId(), group2.getId());
		svcRole.addGroupsToRole(role.getCode(), groupIds);
		List<EOSGroup> groups = svcRole.listRoleGroups(role.getCode(), 5, 0);
		Assert.assertEquals("Add/List Role Groups: size", 2, groups.size());
		Assert.assertTrue("Add / List Role Groups: contains", groupIds.contains(groups.get(0).getId()));
		Assert.assertTrue("Add / List Role Groups: contains", groupIds.contains(groups.get(1).getId()));
	}

	@Test
	public void testRemoveRoleGroups() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSRole role = EOSTestUtil.createRole("removeRoleGroups", svcRole);
		EOSGroup group1 = EOSTestUtil.createGroup("removeRoleGroups1", svcGroup);
		EOSGroup group2 = EOSTestUtil.createGroup("removeRoleGroups2", svcGroup);
		EOSGroup group3 = EOSTestUtil.createGroup("removeRoleGroups3", svcGroup);
		List<Long> groupIds = Arrays.asList(group1.getId(), group2.getId(), group3.getId());
		svcRole.addGroupsToRole(role.getCode(), groupIds);
		List<EOSGroup> groups = svcRole.listRoleGroups(role.getCode(), 5, 0);
		Assert.assertEquals("Remove Role Groups: size", 3, groups.size());
		// Build remove list and test it
		groupIds = Arrays.asList(group2.getId(), group3.getId());
		svcRole.removeGroupsFromRole(role.getCode(), groupIds);
		groups = svcRole.listRoleGroups(role.getCode(), 5, 0);
		Assert.assertEquals("Remove Role Groups: size removed", 1, groups.size());
		Assert.assertEquals("Remove Role Groups: contains", group1.getId(), groups.get(0).getId());
	}

	@Test
	public void testAddListGroupRoles() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSRole role1 = EOSTestUtil.createRole("addListGroupRoles1", svcRole);
		EOSRole role2 = EOSTestUtil.createRole("addListGroupRoles2", svcRole);
		EOSGroup group = EOSTestUtil.createGroup("addListGroupRoles", svcGroup);
		List<String> codes = Arrays.asList(role1.getCode(), role2.getCode());
		svcRole.addRolesToGroup(group.getId(), codes);
		List<EOSRole> roles = svcRole.listGroupRoles(group.getId(), 5, 0);
		Assert.assertEquals("Add / List Group Roles: size", 2, roles.size());
		Assert.assertTrue("Add / List Group Roles: contains", codes.contains(roles.get(0).getCode()));
		Assert.assertTrue("Add / List Group Roles: contains", codes.contains(roles.get(1).getCode()));
	}

	@Test
	public void testRemoveGroupRoles() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSRole role1 = EOSTestUtil.createRole("removeGroupRoles1", svcRole);
		EOSRole role2 = EOSTestUtil.createRole("removeGroupRoles2", svcRole);
		EOSRole role3 = EOSTestUtil.createRole("removeGroupRoles3", svcRole);
		EOSGroup group = EOSTestUtil.createGroup("removeGroupRoles", svcGroup);
		List<String> codes = Arrays.asList(role1.getCode(), role2.getCode(), role3.getCode());
		svcRole.addRolesToGroup(group.getId(), codes);
		List<EOSRole> roles = svcRole.listGroupRoles(group.getId(), 5, 0);
		Assert.assertEquals("Remove Group Roles: size", 3, roles.size());
		// Build remove list and test it
		codes = Arrays.asList(role2.getCode(), role3.getCode());
		svcRole.removeRolesFromGroup(group.getId(), codes);
		roles = svcRole.listGroupRoles(group.getId(), 5, 0);
		Assert.assertEquals("Remove Group Roles: size removed", 1, roles.size());
		Assert.assertEquals("Remove Group Roles: contains", role1.getCode(), roles.get(0).getCode());
	}

}
