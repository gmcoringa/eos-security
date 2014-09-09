/**
 * 
 */
package com.eos.security.impl.test;

import java.util.Arrays;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.eos.common.exception.EOSDuplicatedEntryException;
import com.eos.common.exception.EOSException;
import com.eos.common.exception.EOSValidationException;
import com.eos.common.util.CollectionUtil;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.service.EOSGroupService;
import com.eos.security.api.service.EOSPermissionService;
import com.eos.security.api.service.EOSRoleService;
import com.eos.security.api.service.EOSSecurityService;
import com.eos.security.api.service.EOSUserService;
import com.eos.security.api.vo.EOSGroup;
import com.eos.security.api.vo.EOSRole;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.test.util.EOSTestUtil;

/**
 * Test class for Permission Service.
 * 
 * @author santos.fabiano
 * 
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/spring-test.xml" })
@Ignore
public class EOSPermissionServiceTest {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private EOSSecurityService svcSecurity;
	@Autowired
	private EOSRoleService svcRole;
	@Autowired
	private EOSUserService svcUser;
	@Autowired
	private EOSGroupService svcGroup;
	@Autowired
	private EOSPermissionService svcPermission;

	/*
	@Before
	public void setUp() throws EOSException {
		EOSTestUtil.setup(context);
	}

	@Test
	public void testAddListPermissions() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSRole role = EOSTestUtil.createRole("addListPermissions", svcRole);
		Set<String> permissions = CollectionUtil.asSet("addListPermissions1", "addListPermissions2");
		svcPermission.addRolePermissions(role.getCode(), permissions);
		Set<String> permList = svcPermission.listRolePermissions(role.getCode(), 5, 0);
		Assert.assertEquals("Add / List Permissions: size", 2, permList.size());
		Assert.assertTrue("Add / List Permissions: contains", permissions.containsAll(permList));
	}

	@Test
	public void testRemovePermissions() throws EOSForbiddenException, EOSUnauthorizedException,
			EOSDuplicatedEntryException, EOSValidationException {
		EOSRole role = EOSTestUtil.createRole("removePermissions", svcRole);
		Set<String> permissions = CollectionUtil
				.asSet("removePermissions1", "removePermissions2", "removePermissions3");
		svcPermission.addRolePermissions(role.getCode(), permissions);
		Set<String> permList = svcPermission.listRolePermissions(role.getCode(), 5, 0);
		Assert.assertEquals("Remove Permissions: size", 3, permList.size());
		// Remove and test it
		permissions = CollectionUtil.asSet("removePermissions2", "removePermissions3");
		svcPermission.removeRolePermission(role.getCode(), permissions);
		permList = svcPermission.listRolePermissions(role.getCode(), 5, 0);
		Assert.assertEquals("Remove Permissions: size", 1, permList.size());
		Assert.assertTrue("Remove Permissions: contains", permList.contains("removePermissions1"));
	}

	@Test
	public void testHasRolePermission() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSUser user = EOSTestUtil.createUser("hasRolePermission", null, svcUser);
		EOSRole role = EOSTestUtil.createRole("hasRolePermission", svcRole);
		svcRole.addUsersToRole(role.getCode(), Arrays.asList(user.getLogin()));
		Set<String> permissions = CollectionUtil.asSet("hasRolePermission1", "hasRolePermission2");
		svcPermission.addRolePermissions(role.getCode(), permissions);
		Assert.assertTrue("Has role permission", svcPermission.hasPermission(user.getLogin(), "hasRolePermission1"));
		Assert.assertFalse("Hasn't role permission", svcPermission.hasPermission(user.getLogin(), "hasRolePermission3"));
	}

	@Test
	public void testHasGroupRolePermission() throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException {
		EOSUser user = EOSTestUtil.createUser("hasGroupRolePermission", null, svcUser);
		EOSRole role = EOSTestUtil.createRole("hasGroupRolePermission", svcRole);
		EOSGroup group = EOSTestUtil.createGroup("hasGroupRolePermission", svcGroup);
		svcGroup.addUsersToGroup(group.getId(), Arrays.asList(user.getLogin()));
		svcRole.addRolesToGroup(group.getId(), Arrays.asList(role.getCode()));
		Set<String> permissions = CollectionUtil.asSet("hasGroupRolePermission1", "hasGroupRolePermission2");
		svcPermission.addRolePermissions(role.getCode(), permissions);
		Assert.assertTrue("Has group permission",
				svcPermission.hasPermission(user.getLogin(), "hasGroupRolePermission1"));
		Assert.assertFalse("Hasn't group permission",
				svcPermission.hasPermission(user.getLogin(), "hasGroupRolePermission3"));
	}
	*/
}
