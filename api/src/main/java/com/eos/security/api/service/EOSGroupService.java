/**
 * 
 */
package com.eos.security.api.service;

import java.util.List;

import com.eos.common.EOSLevel;
import com.eos.common.exception.EOSDuplicatedEntryException;
import com.eos.common.exception.EOSNotFoundException;
import com.eos.common.exception.EOSValidationException;
import com.eos.security.api.exception.EOSForbiddenException;
import com.eos.security.api.exception.EOSUnauthorizedException;
import com.eos.security.api.vo.EOSGroup;
import com.eos.security.api.vo.EOSUser;

/**
 * Group service utility, deals with Group and Group User relations.
 * 
 * @author fabiano.santos
 * 
 */
public interface EOSGroupService {

	// Group

	/**
	 * Create a new group.
	 * 
	 * @param group
	 *            Group info.
	 * @return The group created.
	 * @throws EOSDuplicatedEntryException
	 *             If the group already exists.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for group creation.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can create groups.
	 * @throws EOSValidationException
	 *             If the group contains any invalid field.
	 */
	public EOSGroup createGroup(EOSGroup group) throws EOSDuplicatedEntryException, EOSForbiddenException,
			EOSUnauthorizedException, EOSValidationException;

	/**
	 * Find a group.
	 * 
	 * @param groupId
	 *            the group Id
	 * @return Group information.
	 * @throws EOSForbiddenException
	 *             For {@link EOSLevel#INTERNAL} special permissions are
	 *             required.
	 * @throws EOSNotFoundException
	 *             If a group is not found with the given ID in the current
	 *             tenant.
	 */
	public EOSGroup findGroup(Long groupId) throws EOSForbiddenException, EOSNotFoundException;

	/**
	 * Find groups by its Ids.
	 * 
	 * @param groups
	 *            List of group Ids.
	 * @return List of groups.
	 * @throws EOSForbiddenException
	 *             For {@link EOSLevel#INTERNAL} special permissions are
	 *             required.
	 */
	public List<EOSGroup> findGroups(List<Long> groups) throws EOSForbiddenException;

	/**
	 * Update a group info.
	 * 
	 * @param group
	 *            Group info.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for group update.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can update groups.
	 * @throws EOSNotFoundException
	 *             If a group is not found with the given ID in the current
	 *             tenant.
	 * @throws EOSUnauthorizedException
	 *             If the group contains any invalid field.
	 */
	public void updateGroup(EOSGroup group) throws EOSForbiddenException, EOSUnauthorizedException,
			EOSNotFoundException, EOSValidationException;

	/**
	 * Deletes a group. All group user relation and role group relations are
	 * also deleted.
	 * 
	 * @param groupId
	 *            The group Id to be deleted.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission for group deletion.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can delete groups.
	 * @throws EOSNotFoundException
	 *             If the group is not found.
	 */
	public void deleteGroup(Long groupId) throws EOSForbiddenException, EOSUnauthorizedException, EOSNotFoundException;

	/**
	 * List groups filtered by level.
	 * 
	 * @param minimumLevel
	 *            Minimum group level, if null this value will be set to
	 *            {@link EOSLevel#PUBLIC}.
	 * @param maximumLevel
	 *            Maximum group level, if null this value will be set to
	 *            {@link EOSLevel#MAXIMUM}.
	 * @param limit
	 *            Maximum number of registers.
	 * @param offset
	 *            Initial point.
	 * @return Group list.
	 * @throws EOSForbiddenException
	 *             For {@link EOSLevel#INTERNAL} special permissions are
	 *             required.
	 */
	public List<EOSGroup> listGroups(Integer minimumLevel, Integer maximumLevel, int limit, int offset)
			throws EOSForbiddenException;

	// Group User

	/**
	 * Add users to the given group. If the user already belong to a group, this
	 * user is ignored.
	 * 
	 * @param groupId
	 *            The group id to add users.
	 * @param users
	 *            List of users member.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission to add users to a
	 *             group.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate groups.
	 */
	public void addUsersToGroup(Long groupId, List<String> users) throws EOSForbiddenException,
			EOSUnauthorizedException;

	/**
	 * Add the given user in all given groups. If the user already belong to a
	 * group, this group is ignored.
	 * 
	 * @param groups
	 *            List of groups to add the user.
	 * @param userLogin
	 *            User login to be added.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission to add users to a
	 *             group.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate groups.
	 */
	public void addUsersInGroup(List<Long> groups, String userLogin) throws EOSForbiddenException,
			EOSUnauthorizedException;

	/**
	 * Remove users from the given group.
	 * 
	 * @param groupId
	 *            The group id where the users will be removed.
	 * @param users
	 *            List of users to be removed.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission to remove users from
	 *             group.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate groups.
	 */
	public void removeUsersFromGroup(Long groupId, List<String> users) throws EOSForbiddenException,
			EOSUnauthorizedException;

	/**
	 * Remove the given user from all given groups.
	 * 
	 * @param groups
	 *            List of groups the remove user from.
	 * @param userLogin
	 *            User to be removed.
	 * @throws EOSForbiddenException
	 *             If the creator do not have permission to remove users from
	 *             group.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate groups.
	 */
	public void removeUserFromGroups(List<Long> groups, String userLogin) throws EOSForbiddenException,
			EOSUnauthorizedException;

	/**
	 * Remove the user from all groups that he/she belongs.
	 * 
	 * @param userLogin
	 *            User to be removed.
	 * @throws EOSForbiddenException
	 *             If the logged user do not have permission to remove users
	 *             from group.
	 * @throws EOSUnauthorizedException
	 *             Only authenticated users can manipulate groups.
	 */
	public void removeGroupsByUser(String userLogin) throws EOSForbiddenException, EOSUnauthorizedException;

	/**
	 * List group members.
	 * 
	 * @param groupId
	 *            the group id to list users.
	 * @param limit
	 *            Maximum number of registers.
	 * @param offset
	 *            Initial point.
	 * @return List of users.
	 */
	public List<EOSUser> listGroupUsers(Long groupId, int limit, int offset);

	/**
	 * List groups that the given user belongs.
	 * 
	 * @param userLogin
	 *            The user login.
	 * @param limit
	 *            Maximum number of registers.
	 * @param offset
	 *            Initial point.
	 * @return List of groups.
	 * @throws EOSForbiddenException
	 *             For {@link EOSLevel#INTERNAL} special permissions are
	 *             required.
	 */
	public List<EOSGroup> listUserGroups(String userLogin, int limit, int offset) throws EOSForbiddenException;

	/**
	 * Retrieve all group IDs that a user has.
	 * 
	 * @param userLogin
	 *            The user login.
	 * @return List of group IDs of a user.
	 */
	public List<Long> listUserGroupIds(String userLogin);
}
