/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package de.juwimm.cms.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.ejb.CreateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.authorization.JbossFlushAuthCache;
import de.juwimm.cms.authorization.model.GroupHbm;
import de.juwimm.cms.authorization.model.GroupHbmImpl;
import de.juwimm.cms.authorization.model.RoleHbm;
import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.model.UserHbmImpl;
import de.juwimm.cms.authorization.vo.GroupValue;
import de.juwimm.cms.authorization.vo.RoleValue;
import de.juwimm.cms.authorization.vo.UserUnitsGroupsValue;
import de.juwimm.cms.authorization.vo.UserValue;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.SequenceHbmDao;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.TaskHbm;
import de.juwimm.cms.model.TaskHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.TaskValue;
import de.juwimm.cms.vo.UnitValue;

/**
 * @see de.juwimm.cms.remote.UserServiceSpring
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> company
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id: UserServiceSpringImpl.java 12 2009-07-31 09:01:25Z
 *          rene.hertzfeldt $
 */
public class UserServiceSpringImpl extends UserServiceSpringBase {
	private static Log log = LogFactory.getLog(UserServiceSpringImpl.class);
	private final JbossFlushAuthCache authCache = new JbossFlushAuthCache();

	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	/**
	 * Creates a new user in the current site<br>
	 * <b>SECURITY INFORMATION:</b> Available only to: <i>createUser,
	 * siteRoot</i>
	 * 
	 * @todo Currently a User will be created inside the Database. Here we need
	 *       an abstraction layer for JAAS style of user creation.
	 * @param userName
	 *            The username
	 * @param passwd
	 *            The Password
	 * @param firstName
	 *            The first name of the newly created user
	 * @param lastName
	 *            The last name
	 * @param email
	 *            The email address for sending informational emails
	 * @throws CreateException
	 *             CreateException if the Username is already existent
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#createUser(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void handleCreateUser(String userName, String passwd, String firstName, String lastName, String email, Integer initialUnitId) throws Exception {
		if (log.isDebugEnabled()) log.debug("begin createUser");
		ArrayList<String> assertFields = new ArrayList<String>(6);
		if (userName == null || userName.equalsIgnoreCase("")) assertFields.add("user.userName");
		if (firstName == null || firstName.equalsIgnoreCase("")) assertFields.add("user.firstName");
		if (lastName == null || lastName.equalsIgnoreCase("")) assertFields.add("user.lastName");
		if (passwd == null || passwd.equalsIgnoreCase("")) assertFields.add("user.passwd");
		// Bug 4078 if (email == null || email.equalsIgnoreCase(""))
		// assertFields.add("user.email");
		if (assertFields.size() > 0) {
			StringBuffer list = new StringBuffer();
			for (int i = 0; i < assertFields.size(); i++) {
				if (i != 0) {
					list.append(",");
				}
				list.append(assertFields.get(i));
			}
			throw new UserException("NeededFieldsMissingException" + list.toString());
		}
		try {
			UserHbm user = new UserHbmImpl();
			user.setUserId(userName);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			user = super.getUserHbmDao().create(user);
			if (initialUnitId != null && initialUnitId.intValue() != 0) {
				try {
					UnitHbm unit = super.getUnitHbmDao().load(initialUnitId);
					user.addUnit(unit);
				} catch (Exception exe) {
					log.error("Can't attach User " + userName + " to Unit " + initialUnitId + " while creating the User because of: " + exe.getMessage());
					log.error("Error occured", exe);
				}
			}
			// adding new user to site of called user
			UserHbm caller = getUserHbmDao().load(AuthenticationHelper.getUserName());
			SiteHbm callerSite = caller.getActiveSite();
			user.setActiveSite(callerSite);
			user.getSites().add(callerSite);
			this.changePassword4User(user.getUserId(), passwd, passwd);
		} catch (Exception e) {
			throw new UserException(e.getMessage(), e);
		}
		if (log.isDebugEnabled()) {
			log.debug("end createUser");
		}
	}

	/**
	 * Creates a new Task.<br>
	 * You need to provide either the receiverGroup or the receiverUserId.
	 * 
	 * @param receiverUserId
	 *            The UserName who should receive this Task
	 * @param receiverRole
	 *            If there is no ReceiverUserId given, this Task has been
	 *            submitted to a Group of people. In this case this is a Group
	 *            of people with this specified Rights inside the specified
	 *            Unit.
	 * @param unitId
	 *            The Unit this Task belongs to. In general the active unit the
	 *            user is actually in.
	 * @param comment
	 *            An comment regarding this Task
	 * @param taskType
	 *            The Tasktype as defined in
	 * @see de.juwimm.cms.common.Constants#TASK_MESSAGE TASK_*
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#createTask(java.lang.String,
	 *      java.lang.String, java.lang.Integer, java.lang.String, byte)
	 */
	@Override
	protected Integer handleCreateTask(String receiverUserId, String receiverRole, Integer unitId, String comment, byte taskType) throws Exception {
		TaskHbm task = new TaskHbmImpl();

		TaskValue taskValue = new TaskValue();
		taskValue.setComment(comment);
		taskValue.setUnit(super.getUnitHbmDao().load(unitId).getUnitSlimValue());
		taskValue.setTaskType(taskType);
		try {
			task.setSender(super.getUserHbmDao().load(AuthenticationHelper.getUserName()));
			if (receiverUserId != null) {
				UserHbm receiver = super.getUserHbmDao().load(receiverUserId);
				task.setReceiver(receiver);
				task.setTaskValue(taskValue);
				task = super.getTaskHbmDao().create(task);
			} else {
				taskValue.setReceiverRole(receiverRole);
				task.setTaskValue(taskValue);
				task = super.getTaskHbmDao().create(task);
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return task.getTaskId();
	}

	/**
	 * Relates some ViewComponents to this Task.
	 * 
	 * @param taskId
	 *            TaskId of the related Task
	 * @param vcIds
	 *            Integer Array of ViewComponents to relate.
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#addViewComponentsToTask(java.lang.Integer,
	 *      java.lang.Integer[])
	 */
	@Override
	protected void handleAddViewComponentsToTask(Integer taskId, Integer[] vcIds) throws Exception {
		UserHbm user = null;
		TaskHbm task = null;
		try {
			user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			task = super.getTaskHbmDao().load(taskId);
			if (task != null) {

				if (!getUserHbmDao().isInRole(user, UserRights.SITE_ROOT, user.getActiveSite()) && !user.equals(task.getReceiver()) && !user.equals(task.getSender()) && !getUserHbmDao().isInRole(user, task.getReceiverRole(), user.getActiveSite())) { throw new SecurityException("User is not responsible to change this Task. RECEIVER:" + task.getReceiver() + " SENDER:" + task.getSender() + " RECEIVERROLE:" + task.getReceiverRole() + " THIS USER:" + user.getUserId()); }

				Collection<ViewComponentHbm> coll = task.getViewComponents();
				for (int i = 0; i < vcIds.length; i++) {
					ViewComponentHbm vc = super.getViewComponentHbmDao().load(vcIds[i]);
					coll.add(vc);
				}
				task.setViewComponents(coll);
			} else {
				log.warn("Task with Id " + taskId + " was not found");
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Removes some ViewComponents from this Task.
	 * 
	 * @param taskId
	 *            TaskId of the related Task
	 * @param vcIds
	 *            Integer Array of ViewComponents to remove.
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#removeViewComponentsFromTask(java.lang.Integer,
	 *      java.lang.Integer[])
	 */
	@Override
	protected void handleRemoveViewComponentsFromTask(Integer taskId, Integer[] vcIds) throws Exception {
		UserHbm user = null;
		TaskHbm task = null;
		try {
			user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			task = super.getTaskHbmDao().load(taskId);
			if (task != null) {
				if (!!getUserHbmDao().isInRole(user, UserRights.SITE_ROOT, user.getActiveSite()) && !user.equals(task.getReceiver()) && !user.equals(task.getSender()) && !!getUserHbmDao().isInRole(user, task.getReceiverRole(), user.getActiveSite())) { throw new SecurityException("User is not responsible to change this Task. RECEIVER:" + task.getReceiver() + " SENDER:" + task.getSender() + " RECEIVERROLE:" + task.getReceiverRole() + " THIS USER:" + user.getUserId()); }
				try {
					Collection<ViewComponentHbm> coll = task.getViewComponents();
					for (int i = 0; i < vcIds.length; i++) {
						ViewComponentHbm vc = super.getViewComponentHbmDao().load(vcIds[i]);
						coll.remove(vc);
					}
					task.setViewComponents(coll);
				} catch (Exception exe) {
					log.error("Error occured", exe);
				}

			} else {
				log.warn("Task with Id " + taskId + " was not found");
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Returns all users regardings the active site the logged user is in or if
	 * the logged in user is<br/>
	 * only unitAdmin, all Users for all Units he is in.<br/>
	 * The unitAdmin will not see SiteAdmins in his list, even they can see all
	 * Units. <b>SECURITY INFORMATION:</b> Available only to: <i>siteRoot,
	 * unitAdmin</i>
	 * 
	 * @return Returns all UserValue Objects in an Array. Is empty if nobody was
	 *         found.
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#getAllUser()
	 */
	@Override
	protected UserValue[] handleGetAllUser() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("begin getAllUser");
		}
		UserValue[] itarr = null;
		try {
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			if (getUserHbmDao().isInRole(user, UserRights.SITE_ROOT, user.getActiveSite())) {
				Collection coll = null;
				int siz = 0;
				coll = super.getUserHbmDao().findAll(user.getActiveSite().getSiteId());
				siz = coll.size();
				itarr = new UserValue[siz];
				if (siz > 0) {
					Iterator it = coll.iterator();
					for (int i = 0; i < siz; i++) {
						itarr[i] = ((UserHbm) it.next()).getUserValue();
					}
				}
			} else {
				Hashtable<String, UserValue> userMap = new Hashtable<String, UserValue>();
				Iterator unitsIt = user.getUnits().iterator();
				while (unitsIt.hasNext()) {
					UnitHbm unit = (UnitHbm) unitsIt.next();
					Collection users = unit.getUsers();
					Iterator<UserHbm> userIt = users.iterator();
					while (userIt.hasNext()) {
						UserValue current = userIt.next().getUserValue();
						userMap.put(current.getUserName(), current);
					}
				}
				itarr = userMap.values().toArray(new UserValue[0]);
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return itarr;
	}

	/**
	 * Returns all groups regardings the active site the logged user is in.<br>
	 * <b>SECURITY INFORMATION:</b> Available only to: <i>siteRoot</i>
	 * 
	 * @return Returns all GroupValue Objects in an Array. Is empty if nobody
	 *         was found.
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#getAllGroups()
	 */
	@Override
	protected GroupValue[] handleGetAllGroups() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("begin getAllGroups");
		}
		Collection coll = null;
		int siz = 0;
		try {
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			coll = super.getGroupHbmDao().findAll(user.getActiveSite().getSiteId());
			siz = coll.size();
			GroupValue[] itarr = new GroupValue[siz];
			if (siz > 0) {
				Iterator it = coll.iterator();
				for (int i = 0; i < siz; i++) {
					itarr[i] = ((GroupHbm) it.next()).getGroupValue();
				}
			}
			return itarr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Returns all groups users of this unit are member of.<br>
	 * 
	 * @return Returns all GroupValue Objects in an Array. Is empty if nobody
	 *         was found.
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#getAllGroupsUsedInUnit(java.lang.Integer)
	 */
	@Override
	protected GroupValue[] handleGetAllGroupsUsedInUnit(Integer unitId) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("begin getAllGroupsUsedInUnit");
		}
		Hashtable<Integer, GroupValue> groupsTable = new Hashtable<Integer, GroupValue>();
		try {
			UnitHbm unit = super.getUnitHbmDao().load(unitId);
			Iterator<UserHbm> usIt = unit.getUsers().iterator();
			UserHbm principal = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			if (log.isDebugEnabled()) {
				log.debug("Principal: " + principal);
			}
			UserHbm user = null;
			while (usIt.hasNext()) {
				user = usIt.next();
				Iterator<GroupHbm> grpIt = super.getUserHbmDao().getGroups4ActiveSite(user).iterator();
				GroupHbm grp = null;
				GroupValue gv = null;
				while (grpIt.hasNext()) {
					grp = grpIt.next();
					gv = grp.getGroupValue();
					if (gv != null) {
						groupsTable.put(gv.getGroupId(), gv);
					}
				}
			}
		} catch (Exception e) {
			log.error("Error while getting groups for unit", e);
			throw new UserException(e.getMessage());
		}
		return groupsTable.values().toArray(new GroupValue[groupsTable.size()]);
	}

	/**
	 * Creates a new Group<br>
	 * <b>SECURITY INFORMATION:</b> Available only to: <i>siteRoot</i>
	 * 
	 * @param groupName
	 *            The name of the new Group
	 * @return Returns the new created GroupValue
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#createGroup(java.lang.String)
	 */
	@Override
	protected GroupValue handleCreateGroup(String groupName) throws Exception {
		try {
			UserHbm caller = getUserHbmDao().load(AuthenticationHelper.getUserName());
			GroupHbm temp = new GroupHbmImpl();
			temp.setGroupName(groupName);
			temp.setGroupId(sequenceHbmDao.getNextSequenceNumber("group.group_id"));
			temp.setSite(caller.getActiveSite());
			return super.getGroupHbmDao().create(temp).getGroupValue();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Removes a Group<br>
	 * <b>SECURITY INFORMATION:</b> Available only to: <i>siteRoot</i>
	 * 
	 * @param groupId
	 *            The Id of the Group
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#removeGroup(java.lang.Integer)
	 */
	@Override
	protected void handleRemoveGroup(Integer groupId) throws Exception {
		try {
			super.getGroupHbmDao().remove(groupId);
			this.authCache.flushAuthCache();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Updates a Group<br>
	 * <b>SECURITY INFORMATION:</b> Available only to: <i>siteRoot</i>
	 * 
	 * @param gv
	 *            The GroupValue
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#updateGroup(de.juwimm.cms.authorization.vo.GroupValue)
	 */
	@Override
	protected void handleUpdateGroup(GroupValue gv) throws Exception {
		try {
			GroupHbm gl = super.getGroupHbmDao().load(gv.getGroupId());
			gl.setGroupValue(gv);
			Set<RoleHbm> vec = new HashSet<RoleHbm>();
			try {
				for (int i = 0; i < gv.getRoles().length; i++) {
					vec.add(super.getRoleHbmDao().load(gv.getRoles()[i].getRoleId()));
				}
			} catch (Exception e) {
				log.info("No Roles to set");
			}
			gl.setRoles(vec);
			this.authCache.flushAuthCache();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Returns all roles.<br>
	 * 
	 * @return Returns all RoleValue Objects in an Array. Is empty if nobody was
	 *         found.
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#getAllRoles()
	 */
	@Override
	protected RoleValue[] handleGetAllRoles() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("begin getAllRoles");
		}
		Collection coll = null;
		int siz = 0;
		try {
			coll = super.getRoleHbmDao().findAll();
			siz = coll.size();
			RoleValue[] itarr = new RoleValue[siz];
			if (siz > 0) {
				Iterator it = coll.iterator();
				for (int i = 0; i < siz; i++) {
					itarr[i] = ((RoleHbm) it.next()).getRoleValue();
				}
			}
			return itarr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Returns all users regardings the active site and the given Group.<br>
	 * If the Group will be <b>null</b>, it will return all users. <b>SECURITY
	 * INFORMATION:</b> Available only to: <i>siteRoot</i>
	 * 
	 * @param groupId
	 *            The Group
	 * @return Returns all UserValue Objects in an Array. Is empty if nobody was
	 *         found.
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#getAllUser4Group(java.lang.Integer)
	 */
	@Override
	protected UserValue[] handleGetAllUser4Group(Integer groupId) throws Exception {
		if (groupId == null) { return getAllUser(); }
		try {
			GroupHbm group = super.getGroupHbmDao().load(groupId);
			Collection coll = group.getUsers();
			int siz = coll.size();
			UserValue[] itarr = new UserValue[siz];
			if (siz > 0) {
				Iterator it = coll.iterator();
				for (int i = 0; i < siz; i++) {
					itarr[i] = ((UserHbm) it.next()).getUserValue();
				}
			}
			return itarr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Returns all users regardings the active site, the given Group and the
	 * given unit.<br>
	 * 
	 * @param groupId
	 *            The Group
	 * @param unitId
	 *            The Unit
	 * @return Returns all UserValue Objects in an Array. Is empty if nobody was
	 *         found.
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#getAllUser4GroupAndUnit(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	protected UserValue[] handleGetAllUser4GroupAndUnit(Integer groupId, Integer unitId) throws Exception {
		Vector<UserValue> vec = new Vector<UserValue>();
		try {
			UserHbm userMe = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			if (getUserHbmDao().isInUnit(unitId, userMe)) {
				UnitHbm unit = super.getUnitHbmDao().load(unitId);
				Iterator it = unit.getUsers().iterator();
				while (it.hasNext()) {
					UserHbm user = (UserHbm) it.next();
					if (user.isInGroup(groupId) && !!getUserHbmDao().isInRole(user, UserRights.SITE_ROOT, user.getActiveSite())) {
						vec.addElement(user.getUserValue());
					}
				}
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return vec.toArray(new UserValue[0]);
	}

	/**
	 * Returns all Groups for a related user in the actual site <b>SECURITY
	 * INFORMATION:</b> Available only to: <i>siteRoot, unitAdmin</i>
	 * 
	 * @param userName
	 *            The related username
	 * @return An Array of GroupValue Objects
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#getGroups4User(java.lang.String)
	 */
	@Override
	protected GroupValue[] handleGetGroups4User(String userName) throws Exception {
		try {
			UserHbm user = super.getUserHbmDao().load(userName);
			Collection coll = null;
			if (user.isMasterRoot()) {
				coll = super.getGroupHbmDao().findAll(user.getActiveSite().getSiteId());
			} else {
				coll = super.getUserHbmDao().getGroups4ActiveSite(user);
			}
			GroupValue[] gvarr = new GroupValue[coll.size()];
			Iterator it = coll.iterator();
			for (int i = 0; i < coll.size(); i++) {
				GroupHbm gl = (GroupHbm) it.next();
				gvarr[i] = gl.getGroupValue();
			}
			return gvarr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Returns all Groups for a related user in the actual site <b>SECURITY
	 * INFORMATION:</b> Available only to: <i>siteRoot, unitAdmin</i>
	 * 
	 * @param userName
	 *            The related username
	 * @return An Array of GroupValue Objects
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#getUnits4User(java.lang.String)
	 */
	@Override
	protected UnitValue[] handleGetUnits4User(String userName) throws Exception {
		UserHbm user = super.getUserHbmDao().load(userName);
		Collection coll = null;
		if (user.isMasterRoot() && user.getActiveSite() != null) {
			coll = super.getUnitHbmDao().findAll(user.getActiveSite().getSiteId());
		} else {
			coll = super.getUserHbmDao().getUnits4ActiveSite(user);
		}
		UnitValue[] uarr = new UnitValue[coll.size()];
		Iterator it = coll.iterator();
		UnitHbm unit = null;
		for (int i = 0; i < coll.size(); i++) {
			unit = (UnitHbm) it.next();
			uarr[i] = getUnitHbmDao().getDao(unit);
		}
		return uarr;
	}

	/**
	 * Returns all users regardings the active site and the given unit.<br>
	 * If the Group will be <b>null</b>, it will return all users for this unit.<br>
	 * PLEASE NOTE: DOES NOT RETURN SITEROOTS! <b>SECURITY INFORMATION:</b>
	 * Available only to: <i>siteRoot, unitAdmin</i>
	 * 
	 * @param unitId
	 *            The Unit
	 * @return Returns all UserValue Objects in an Array. Is empty if nobody was
	 *         found.
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#getAllUser4Unit(java.lang.Integer)
	 */
	@Override
	protected UserValue[] handleGetAllUser4Unit(Integer unitId) throws Exception {
		if (unitId == null) { return new UserValue[0]; }
		Vector<UserValue> userValues = new Vector<UserValue>();
		try {
			UserHbm userMe = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			if (getUserHbmDao().isInUnit(unitId, userMe)) {
				UnitHbm unit = super.getUnitHbmDao().load(unitId);
				userValues.addAll(this.getUsers4Unit(userMe, unit));
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return userValues.toArray(new UserValue[0]);
	}

	private Collection getUsers4Unit(UserHbm user, UnitHbm unit) {
		ArrayList<UserValue> usersList = new ArrayList<UserValue>();
		if (getUserHbmDao().isInUnit(unit.getUnitId(), user)) {
			Collection connectedUsers = unit.getSite().getUsers();
			Iterator<UserHbm> it = unit.getUsers().iterator();
			UserHbm currentUser = null;
			UserHbm principal = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			while (it.hasNext()) {
				currentUser = it.next();
				if (connectedUsers.contains(currentUser)) {
					if (super.getUserHbmDao().isInRole(user, UserRights.SITE_ROOT, principal.getActiveSite())) {
						usersList.add(currentUser.getUserValue());
					}
				}
			}
		}
		return usersList;
	}

	/**
	 * Get all Tasks for the actual User.<br>
	 * If a <i>siteRoot</i> is currently logged in, he will get all Tasks.
	 * 
	 * @return An Array of TaskValue Objects
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#getAllTasks()
	 */
	@Override
	protected TaskValue[] handleGetAllTasks() throws Exception {
		UserHbm user = null;
		try {
			user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			int siz = 0;
			Iterator it = null;
			if (!getUserHbmDao().isInRole(user, UserRights.SITE_ROOT, user.getActiveSite())) {
				if (log.isDebugEnabled()) log.debug("SiteRoot requested All Tasks");
				Collection coll = super.getTaskHbmDao().findAll(user.getActiveSite().getSiteId());
				siz = coll.size();
				it = coll.iterator();
				TaskValue[] itarr = new TaskValue[siz];
				if (siz > 0) {
					for (int i = 0; i < siz; i++) {
						itarr[i] = ((TaskHbm) it.next()).getTaskValue();
					}
				}
				return itarr;
			}
			if (log.isDebugEnabled()) log.debug("User requested Tasks");
			Vector<TaskValue> vec = new Vector<TaskValue>();
			Collection coll = super.getTaskHbmDao().findAll(user.getActiveSite().getSiteId());
			it = coll.iterator();

			while (it.hasNext()) {
				TaskHbm task = (TaskHbm) it.next();
				if (user.equals(task.getReceiver()) || (!getUserHbmDao().isInRole(user, task.getReceiverRole(), user.getActiveSite()) && (task.getUnit() == null || getUserHbmDao().isInUnit(task.getUnit().getUnitId(), user)))) {
					vec.add(task.getTaskValue());
				}
			}
			return vec.toArray(new TaskValue[vec.size()]);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Remove a Task from the TaskQueue<br>
	 * <b>SECURITY INFORMATION:</b> You only can remove Tasks, if you're the
	 * sender, the receiver, <i>siteRoot</i> or in the receiver group.
	 * 
	 * @param taskId
	 *            The TaskId to remove
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#removeTask(java.lang.Integer)
	 */
	@Override
	protected void handleRemoveTask(Integer taskId) throws Exception {
		TaskHbm task = null;
		task = super.getTaskHbmDao().load(taskId);
		if (task != null) {
			UserHbm user = null;
			try {
				user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			} catch (Exception exe) {
				log.error("Error thrown while returning logged in user: " + exe.getMessage());
				// context.setRollbackOnly();
				throw new UserException("Can't find the actual logged in user. Check the logs and retry");
			}
			if (!!getUserHbmDao().isInRole(user, UserRights.SITE_ROOT, user.getActiveSite()) && !user.equals(task.getReceiver()) && !user.equals(task.getSender()) && !!getUserHbmDao().isInRole(user, task.getReceiverRole(), user.getActiveSite())) {
				// context.setRollbackOnly();
				throw new SecurityException("User is not responsible to delete this Task. RECEIVER:" + task.getReceiver().getUserId() + " SENDER:" + task.getSender().getUserId() + " RECEIVERROLE:" + task.getReceiverRole() + " THIS USER:" + user.getUserId());
			}
			try {
				super.getTaskHbmDao().remove(task);
			} catch (Exception e) {
				throw new UserException(e.getMessage());
			}
		}
	}

	/**
	 * Sets the given task as viewed, if the user has the required permissions
	 * therefor.
	 * 
	 * @param taskId
	 *            The TaskId, from which task the Status should be changed to
	 *            Constants.TASK_STATUS_VIEWED
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#setTaskViewed(java.lang.Integer)
	 */
	@Override
	protected void handleSetTaskViewed(Integer taskId) throws Exception {
		try {
			UserHbm user = null;
			TaskHbm task = null;
			user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			task = super.getTaskHbmDao().load(taskId);
			if (task != null) {
				if (!!getUserHbmDao().isInRole(user, UserRights.SITE_ROOT, user.getActiveSite()) && !user.equals(task.getReceiver()) && !user.equals(task.getSender()) && !!getUserHbmDao().isInRole(user, task.getReceiverRole(), user.getActiveSite())) { throw new SecurityException("User is not responsible to change this Task. RECEIVER:" + task.getReceiver() + " SENDER:" + task.getSender() + " RECEIVERROLE:" + task.getReceiverRole() + " THIS USER:" + user.getUserId()); }
				task.setStatus(Constants.TASK_STATUS_VIEWED);
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Returns true, if there is any new Task for this User.<br>
	 * This will also return true, if the User is <i>siteRoot</i> and anybody
	 * has got a new Task.
	 * 
	 * @return An Boolean Object containing true/false if the active user has a
	 *         new task.
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#isNewTask4User()
	 */
	@Override
	protected Boolean handleIsNewTask4User() throws Exception {
		try {
			TaskValue[] td = this.getAllTasks();
			for (int i = 0; i < td.length; i++) {
				if (td[i].getStatus() == Constants.TASK_STATUS_NEW) { return new Boolean(true); }
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return new Boolean(false);
	}

	/**
	 * Updates a User.<br>
	 * For security reasons, only the following fields are updated:
	 * <ul>
	 * <li>FirstName</li>
	 * <li>LastName</li>
	 * <li>EMail</li>
	 * <li>ConfigXML (only if parameter is not null and not empty)</li>
	 * </ul>
	 * <b>SECURITY INFORMATION:</b> Available only to: <i>changeUser,
	 * siteRoot</i>
	 * 
	 * @param userValue
	 *            UserValue Object representing the user
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#updateUser(de.juwimm.cms.authorization.vo.UserValue)
	 */
	@Override
	protected void handleUpdateUser(UserValue userValue) throws Exception {
		try {
			UserHbm user = null;
			user = super.getUserHbmDao().load(userValue.getUserName());
			if (user != null) {
				if (this.hasRightsForChangeUser(user)) {
					user.setFirstName(userValue.getFirstName());
					user.setLastName(userValue.getLastName());
					user.setEmail(userValue.getEmail());
					if (userValue.getConfigXML() != null && !userValue.getConfigXML().equals("")) {
						user.setConfigXML(userValue.getConfigXML());
					}
					this.authCache.flushAuthCache();
				} else {
					throw new SecurityException("Not enough permissions to change the user:" + userValue.getUserName() + " with credential:" + AuthenticationHelper.getUserName());
				}
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Internal Function for returning the rights a user has to change another
	 * users data.<br>
	 * Returns true if:
	 * <ul>
	 * <li>Authenticated User is <i>siteRoot</i></li>
	 * <li>Authenticated User is <i>masterRoot</i></li>
	 * <li>Authenticated User is in the same unit as the given user</li>
	 * <li>Authenticated User has at least the same rights level. F.e. a
	 * <i>siteRoot</i> can only be edited by a user with <i>masterRoot-</i> or
	 * <i>siteRoot-</i>Permissions</li>
	 * 
	 * @param userGiven
	 *            The user to change
	 * @return Returns true, if the authenticated user has the rights to change
	 *         this user
	 */
	private boolean hasRightsForChangeUser(UserHbm userGiven) {
		UserHbm userMe = null;
		try {
			userMe = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
		} catch (Exception exe) {
			log.error("Unknown error while resolving myown user: " + AuthenticationHelper.getUserName());
		}
		boolean hasRights = false;
		if ((getUserHbmDao().isInRole(userMe, UserRights.SITE_ROOT, userMe.getActiveSite()) && !userGiven.isMasterRoot()) || userMe.isMasterRoot()) {
			hasRights = true;
		}
		if (!hasRights) {
			if (userGiven.isMasterRoot() && !userMe.isMasterRoot()) { return false; }
			if (!getUserHbmDao().isInRole(userGiven, UserRights.SITE_ROOT, userGiven.getActiveSite())) { return false; // don't edit a user with higher rights
			}
		}
		if (!hasRights) {
			// Only change user, if the given user is in one of the same units
			// as the authenticated user
			Iterator it = userGiven.getUnits().iterator();
			while (it.hasNext()) {
				UnitHbm unit = (UnitHbm) it.next();
				if (userMe.getUnits().contains(unit)) {
					hasRights = true;
					break;
				}
			}
		}
		if (log.isDebugEnabled()) log.debug("User " + userMe.getUserId() + " has right: " + hasRights + " for changing " + userGiven.getUserId());
		return hasRights;
	}

	/**
	 * Adds a User to a specific Group. <b>SECURITY INFORMATION:</b> Available
	 * only to: <i>changeUser, siteRoot</i>
	 * 
	 * @param groupValue
	 *            The Group
	 * @param userName
	 *            The Use
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#addUserToGroup(de.juwimm.cms.authorization.vo.GroupValue,
	 *      java.lang.String)
	 */
	@Override
	protected void handleAddUserToGroup(GroupValue groupValue, String userName) throws Exception {
		try {
			UserHbm user = null;
			user = super.getUserHbmDao().load(userName);
			if (user != null) {
				if (this.hasRightsForChangeUser(user)) {
					GroupHbm group = super.getGroupHbmDao().load(groupValue.getGroupId());
					getUserHbmDao().addGroup(group, AuthenticationHelper.getUserName(), userName);
					this.authCache.flushAuthCache();
				} else {
					throw new SecurityException("Not enough permissions to change the user:" + userName + " with credential:" + AuthenticationHelper.getUserName());
				}
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Removes a User from a specific Group. <b>SECURITY INFORMATION:</b>
	 * Available only to: <i>changeUser, siteRoot</i>
	 * 
	 * @param gv
	 *            The Group
	 * @param userName
	 *            The User
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#removeUserFromGroup(de.juwimm.cms.authorization.vo.GroupValue,
	 *      java.lang.String)
	 */
	@Override
	protected void handleRemoveUserFromGroup(GroupValue gv, String userName) throws Exception {
		try {
			UserHbm user = null;
			user = super.getUserHbmDao().load(userName);
			if (user != null) {
				if (this.hasRightsForChangeUser(user)) {
					GroupHbm gl = super.getGroupHbmDao().load(gv.getGroupId());
					user.dropGroup(gl);
					this.authCache.flushAuthCache();
				} else {
					throw new SecurityException("Not enough permissions to change the user:" + userName + " with credential:" + AuthenticationHelper.getUserName());
				}
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Deletes an useraccount.
	 * 
	 * @param userId
	 *            The UserId of the user that should be deleted.
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#deleteUser(java.lang.String)
	 */
	@Override
	protected void handleDeleteUser(String userId) throws Exception {
		try {
			UserHbm user = null;
			user = super.getUserHbmDao().load(userId);
			if (user != null && this.hasRightsForChangeUser(user)) {
				super.getUserHbmDao().remove(user);
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * Changes the Password of a given useraccount.<br>
	 * <b>SECURITY INFORMATION:</b> Available only to: <i>changeUser,
	 * siteRoot</i>
	 * 
	 * @param userName
	 *            The UserName of the user which should get a new Password
	 * @param oldPassword
	 *            The old password
	 * @param newPassword
	 *            The new password
	 * @return True, if it was possible to change the password.
	 * 
	 * @see de.juwimm.cms.remote.UserServiceSpring#changePassword4User(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	protected boolean handleChangePassword4User(String userName, String oldPassword, String newPassword) throws Exception {
		if (log.isDebugEnabled()) log.debug("begin changePassword for anotherone");

		UserHbm userGiven = super.getUserHbmDao().load(userName);
		String newPasswdEncoded = userGiven.encrypt(newPassword);

		if (hasRightsForChangeUser(userGiven)) {
			userGiven.setPasswd(newPasswdEncoded);
			if (log.isDebugEnabled()) log.debug("end changePassword succ");
			return true;
		}

		if (log.isDebugEnabled()) log.debug("end changePassword nosucc");
		return false;
	}

	/**
	 * @see de.juwimm.cms.remote.UserServiceSpring#getAllSites4CurrentUser()
	 */
	@Override
	protected SiteValue[] handleGetAllSites4CurrentUser() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("begin getAllSites4CurrentUser");
		}
		SiteValue[] itarr = null;
		try {
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			if (super.getUserHbmDao().isInRole(user, UserRights.SITE_ROOT, user.getActiveSite())) {
				Collection coll = null;
				Collection<SiteValue> sites = new ArrayList<SiteValue>();
				try {
					coll = user.getSites();
					// coll = getSiteHbmDao().findAll4User(user.getUserId());
				} catch (Exception exe) {
					log.warn("Error while executing the finder \"findAll4User\" in getAllSites4CurrentUser: " + exe.getMessage());
				}
				Iterator it = coll.iterator();
				while (it.hasNext()) {
					SiteHbm currentSite = (SiteHbm) it.next();
					if (this.isUserInRole(user.getUserId(), currentSite.getSiteId(), "siteRoot")) {
						sites.add((currentSite).getSiteValue());
					}
				}
				itarr = sites.toArray(new SiteValue[0]);
			}
		} catch (Exception exe) {
			log.error("Error occured", exe);
		}
		return itarr;
	}

	private boolean isUserInRole(String userName, Integer siteId, String role) throws Exception {
		if (userName == null) { return false; }
		UserHbm user = super.getUserHbmDao().load(userName);
		if (user.isMasterRoot()) { return true; }
		try {
			GroupValue[] gv = this.getGroups4UserInSite(userName, siteId);
			for (int i = 0; i < gv.length; i++) {
				for (int j = 0; j < gv[i].getRoles().length; j++) {
					if (gv[i].getRoles()[j].getRoleId().equalsIgnoreCase(role)) { return true; }
				}
			}
		} catch (Exception exe) {
			log.error(exe);
		}
		return false;
	}

	/**
	 * @see de.juwimm.cms.remote.UserServiceSpring#getAllUsers4OwnSites()
	 */
	@Override
	protected UserValue[] handleGetAllUsers4OwnSites() throws Exception {
		UserValue[] itarr = null;
		SiteValue[] mySites = this.getAllSites4CurrentUser();
		HashSet<UserValue> userSet = new HashSet<UserValue>();
		try {
			for (int i = (mySites.length - 1); i >= 0; i--) {
				Collection users = super.getUserHbmDao().findAll(mySites[i].getSiteId());
				Iterator it = users.iterator();
				while (it.hasNext()) {
					UserHbm user = (UserHbm) it.next();
					if (!user.isMasterRoot()) {
						userSet.add(user.getUserValue());
					}
				}
			}
		} catch (Exception e) {
			log.error("Error occured", e);
		}
		itarr = userSet.toArray(new UserValue[0]);
		return itarr;
	}

	/**
	 * @see de.juwimm.cms.remote.UserServiceSpring#getGroups4UserInSite(java.lang.String,
	 *      java.lang.Integer)
	 */
	@Override
	protected GroupValue[] handleGetGroups4UserInSite(String userName, Integer siteId) throws Exception {
		GroupValue[] gvarr = null;
		try {
			UserHbm user = super.getUserHbmDao().load(userName);
			Collection coll = null;
			if (user.isMasterRoot()) {
				coll = super.getGroupHbmDao().findAll(siteId);
			} else {
				coll = super.getGroupHbmDao().findByUserAndSite(userName, siteId);
			}
			gvarr = new GroupValue[coll.size()];
			Iterator it = coll.iterator();
			for (int i = 0; i < coll.size(); i++) {
				GroupHbm group = (GroupHbm) it.next();
				gvarr[i] = group.getGroupValue();
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return gvarr;
	}

	/**
	 * @see de.juwimm.cms.remote.UserServiceSpring#getConnectedUsers4Site(java.lang.Integer)
	 */
	@Override
	protected String[] handleGetConnectedUsers4Site(Integer siteId) throws Exception {
		Collection coll = null;
		int siz = 0;
		try {
			SiteHbm site = super.getSiteHbmDao().load(siteId);
			coll = site.getUsers();
			siz = coll.size();
		} catch (Exception exe) {
			log.warn("Error while executing the finder \"findAll\" in getConnectedUsersForSite: " + exe.getMessage());
		}
		ArrayList<String> al = new ArrayList<String>(siz);
		if (siz > 0) {
			Iterator it = coll.iterator();
			for (int i = 0; i < siz; i++) {
				UserHbm user = (UserHbm) it.next();
				if (!user.isMasterRoot()) {
					al.add(user.getUserId());
				}
			}
		}
		return al.toArray(new String[0]);
	}

	/**
	 * @see de.juwimm.cms.remote.UserServiceSpring#setConnectedUsers4Site(java.lang.Integer,
	 *      java.lang.String[])
	 */
	@Override
	protected void handleSetConnectedUsers4Site(Integer siteId, String[] userIds) throws Exception {
		Collection<UserHbm> usersIn = null;
		Collection<String> usersRemove = new ArrayList<String>();
		Collection<String> usersNew = new ArrayList<String>();
		Collection<String> users = new ArrayList<String>();

		// Collections are nicer to handle
		for (int i = 0; i < userIds.length; i++) {
			users.add(userIds[i]);
		}

		SiteHbm site = null;
		try {
			site = super.getSiteHbmDao().load(siteId);
			usersIn = site.getUsers();

		} catch (Exception exe) {
			log.warn("Error while executing the finder \"findAll\" in getConnectedUsersForSite: " + exe.getMessage());
		}

		// compare old user list with new one -> element in both means stay,
		// just in old means del
		for (UserHbm user : usersIn) {
			if (users.contains(user.getUserId())) {
				users.remove(user.getUserId());
			} else {
				usersRemove.add(user.getUserId());
			}
		}

		// users still in list are the really new ones - create them
		for (String userId : users) {
			UserHbm user = super.getUserHbmDao().load(userId);
			user.getSites().add(site);
		}

		// remove the users deleted users
		for (String userId : usersRemove) {
			UserHbm user = super.getUserHbmDao().load(userId);
			user.getSites().remove(site);
		}
	}

	@Override
	protected SiteValue[] handleGetAllSites4CurrentUserWithRole(String requestedRole) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("begin getAllSites4CurrentUserWithRole: " + requestedRole);
		}
		SiteValue[] itarr = null;
		try {
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			if (super.getUserHbmDao().isInRole(user, requestedRole, user.getActiveSite())) {
				Collection<SiteHbm> coll = null;
				Collection<SiteValue> sites = new ArrayList<SiteValue>();
				try {
					if (user.isMasterRoot()) {
						coll = super.getSiteHbmDao().findAll();
					} else {
						coll = super.getSiteHbmDao().findAll4User(user.getUserId());
					}
				} catch (Exception exe) {
					log.warn("Error while executing the finder \"findAll4User\" in getAllSites4CurrentUserWithRole: " + exe.getMessage());
				}
				Iterator it = coll.iterator();
				while (it.hasNext()) {
					SiteHbm currentSite = (SiteHbm) it.next();
					if (this.isUserInRole(user.getUserId(), currentSite.getSiteId(), requestedRole)) {
						sites.add((currentSite).getSiteValue());
					}
				}
				itarr = sites.toArray(new SiteValue[0]);
			} else {
				// at least add current site
				itarr = new SiteValue[] {user.getActiveSite().getSiteValue()};
			}
		} catch (Exception exe) {
			log.error("Error occured", exe);
		}
		return itarr;
	}

	private Integer getActiveSiteId() throws UserException {
		try {
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			return user.getActiveSite().getSiteId();
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Could not get id of active site", e);
				throw new UserException(e.getMessage());
			}
		}
		return null;
	}

	@Override
	protected UserUnitsGroupsValue[] handleGetUserUnitsGroups4UnitAndGroup(Integer unitId, Integer groupId) throws Exception {
		Vector<UserUnitsGroupsValue> vec = new Vector<UserUnitsGroupsValue>();
		try {
			ArrayList<UserHbm> userList = new ArrayList<UserHbm>();

			UserHbm userLogged = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			if (!isUserInRole(userLogged.getUserId(), this.getActiveSiteId(), UserRights.SITE_ROOT)) {
				if (isUserInRole(userLogged.getUserId(), this.getActiveSiteId(), UserRights.UNIT_ADMIN)) {
					Iterator unitsIt = userLogged.getUnits().iterator();
					while (unitsIt.hasNext()) {
						UnitHbm unit = (UnitHbm) unitsIt.next();
						Collection users = unit.getUsers();
						Iterator<UserHbm> userIt = users.iterator();
						while (userIt.hasNext()) {
							UserHbm current = userIt.next();
							//not allowed to see user in higher roles than himself 
							if (!isUserInRole(current.getUserId(), this.getActiveSiteId(), UserRights.SITE_ROOT)) {
								userList.add(current);
							}
						}
					}
				}

			} else {
				if (unitId != null && unitId.intValue() > 0 && groupId != null && groupId.intValue() > 0) {
					userList.addAll(super.getUserHbmDao().findAll4UnitAndGroup(unitId, groupId));
				} else if (unitId != null && unitId.intValue() > 0) {
					userList.addAll(super.getUserHbmDao().findAll4Unit(unitId));
				} else if (groupId != null && groupId.intValue() > 0) {
					userList.addAll(super.getUserHbmDao().findAll4Group(groupId));
				} else {
					userList.addAll(super.getUserHbmDao().findAll(this.getActiveSiteId()));
				}
			}
			Iterator<UserHbm> it = userList.iterator();
			while (it.hasNext()) {
				UserHbm user = it.next();
				vec.add(getUserHbmDao().getUserUnitsGroupsValue(user));
				// vec.add(user.getUserUnitsGroupsValue());
			}
		} catch (Exception e) {
			throw new UserException(e.getMessage(), e);
		}
		return vec.toArray(new UserUnitsGroupsValue[0]);
	}

	@Override
	protected TaskValue handleGetTaskForId(Integer taskId) throws Exception {
		TaskHbm tvHbm = getTaskHbmDao().load(taskId);
		if (tvHbm != null) { return tvHbm.getTaskValue(); }
		return null;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.UserServiceSpringBase#handleIsUserInUnit(java.lang.String, java.lang.Integer)
	 */
	@Override
	protected Boolean handleIsUserInUnit(String userId, Integer unitId) throws Exception {
		return getUserHbmDao().load(userId).isInUnit(unitId);
	}

}
