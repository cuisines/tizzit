package de.juwimm.cms.remote.test;

import java.util.ArrayList;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Assert;

import de.juwimm.cms.authorization.model.GroupHbm;
import de.juwimm.cms.authorization.model.GroupHbmDao;
import de.juwimm.cms.authorization.model.GroupHbmImpl;
import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.authorization.model.UserHbmImpl;
import de.juwimm.cms.authorization.vo.GroupValue;
import de.juwimm.cms.authorization.vo.UserValue;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.TaskHbm;
import de.juwimm.cms.model.TaskHbmDao;
import de.juwimm.cms.model.TaskHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmDao;
import de.juwimm.cms.model.UnitHbmImpl;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewComponentHbmDao;
import de.juwimm.cms.model.ViewComponentHbmImpl;
import de.juwimm.cms.remote.UserServiceSpringImpl;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.TaskValue;
import de.juwimm.cms.vo.UnitValue;

public class UserServiceTest extends AbstractServiceTest {

	UnitHbmDao unitDaoMock;
	UserHbmDao userDaoMock;
	UserServiceSpringImpl userService;
	TaskHbmDao taskDaoMock;
	ViewComponentHbmDao viewComponentDaoMock;
	GroupHbmDao groupDaoMock;
	SiteHbmDao siteDaoMock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		userService = new UserServiceSpringImpl();
		unitDaoMock = EasyMock.createMock(UnitHbmDao.class);
		userService.setUnitHbmDao(unitDaoMock);

		userDaoMock = EasyMock.createMock(UserHbmDao.class);
		userService.setUserHbmDao(userDaoMock);

		taskDaoMock = EasyMock.createMock(TaskHbmDao.class);
		userService.setTaskHbmDao(taskDaoMock);

		viewComponentDaoMock = EasyMock.createMock(ViewComponentHbmDao.class);
		userService.setViewComponentHbmDao(viewComponentDaoMock);

		groupDaoMock = EasyMock.createMock(GroupHbmDao.class);
		userService.setGroupHbmDao(groupDaoMock);

		siteDaoMock = EasyMock.createMock(SiteHbmDao.class);
		userService.setSiteHbmDao(siteDaoMock);

	}

	/**
	 * Test creation of tasks
	 * expect: the receiverUserId is not null, and to the task the receiver is assigned
	 */
	public void testCreateTask() {
		String receiverUserId = "testUserReceiver";
		String receiverRole = "receiverRoleTest";
		Integer unitId = 1;
		String comment = "testComment";
		byte taskType = 1;

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");

		UserHbm userReceiver = new UserHbmImpl();
		userReceiver.setUserId("testUserReceiver");

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		unit.setName("testUnit");

		TaskHbm task = new TaskHbmImpl();
		task.setTaskId(1);
		task.setComment(comment);
		task.setTaskType(taskType);
		task.setUnit(unit);
		task.setReceiver(userReceiver);

		try {
			EasyMock.expect(unitDaoMock.load(EasyMock.anyInt())).andReturn(unit);
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user);
			EasyMock.expect(userDaoMock.load("testUserReceiver")).andReturn(userReceiver);
			EasyMock.expect(taskDaoMock.create((TaskHbm) EasyMock.anyObject())).andReturn(task);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(unitDaoMock);
		EasyMock.replay(userDaoMock);
		EasyMock.replay(taskDaoMock);

		try {
			int id = userService.createTask(receiverUserId, receiverRole, unitId, comment, taskType);
			Assert.assertEquals(1, id);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(unitDaoMock);
		EasyMock.verify(userDaoMock);
		EasyMock.verify(taskDaoMock);
	}

	/**
	 * Test creation of tasks
	 * expect: no receiver user, just the role required is assigned
	 */
	public void testCreateTask1() {
		String receiverUserId = null;
		String receiverRole = "receiverRoleTest";
		Integer unitId = 1;
		String comment = "testComment";
		byte taskType = 1;

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");

		UserHbm userReceiver = new UserHbmImpl();
		userReceiver.setUserId("testUserReceiver");

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		unit.setName("testUnit");

		TaskHbm task = new TaskHbmImpl();
		task.setTaskId(1);
		task.setComment(comment);
		task.setTaskType(taskType);
		task.setUnit(unit);
		task.setReceiver(userReceiver);

		try {
			EasyMock.expect(unitDaoMock.load(EasyMock.anyInt())).andReturn(unit);
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user);
			EasyMock.expect(taskDaoMock.create((TaskHbm) EasyMock.anyObject())).andReturn(task);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(unitDaoMock);
		EasyMock.replay(userDaoMock);
		EasyMock.replay(taskDaoMock);

		try {
			int id = userService.createTask(receiverUserId, receiverRole, unitId, comment, taskType);
			Assert.assertEquals(1, id);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(unitDaoMock);
		EasyMock.verify(userDaoMock);
		EasyMock.verify(taskDaoMock);
	}

	/**
	 * test AddViewComponentsToTask
	 * expect: add correctly view component to task.
	 */
	public void testAddViewComponentsToTask() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(true);
		user.setActiveSite(site);

		UserHbm sender = new UserHbmImpl();
		sender.setUserId("senderUser");

		UserHbm receiver = new UserHbmImpl();
		receiver.setUserId("receiverUser");

		TaskHbm task = new TaskHbmImpl();
		task.setTaskId(1);
		task.setReceiver(receiver);
		task.setSender(sender);
		task.setReceiverRole("testReceiverRole");

		ViewComponentHbm firstViewComponent = new ViewComponentHbmImpl();
		firstViewComponent.setViewComponentId(1);

		ViewComponentHbm secondViewComponent = new ViewComponentHbmImpl();
		secondViewComponent.setViewComponentId(2);

		try {
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user);
			EasyMock.expect(taskDaoMock.load(EasyMock.eq(1))).andReturn(task);
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(firstViewComponent);
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(2))).andReturn(secondViewComponent);
			EasyMock.expect(userDaoMock.isInRole(user, "siteRoot", site)).andReturn(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);
		EasyMock.replay(taskDaoMock);
		EasyMock.replay(viewComponentDaoMock);

		Integer[] vcIds = new Integer[] {1, 2};

		try {
			userService.addViewComponentsToTask(1, vcIds);

		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(userDaoMock);
		EasyMock.verify(taskDaoMock);
		EasyMock.verify(viewComponentDaoMock);

	}

	/**
	 * test AddViewComponentsToTask
	 * expect: user is not site root. Throw  SecurityException.
	 */
	public void testAddViewComponentsToTask1() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(false);
		user.setActiveSite(site);

		UserHbm sender = new UserHbmImpl();
		sender.setUserId("senderUser");

		UserHbm receiver = new UserHbmImpl();
		receiver.setUserId("receiverUser");
		receiver.setMasterRoot(false);

		TaskHbm task = new TaskHbmImpl();
		task.setTaskId(1);
		task.setReceiver(receiver);
		task.setSender(sender);
		task.setReceiverRole("testReceiverRole");

		ViewComponentHbm firstViewComponent = new ViewComponentHbmImpl();
		firstViewComponent.setViewComponentId(1);

		ViewComponentHbm secondViewComponent = new ViewComponentHbmImpl();
		secondViewComponent.setViewComponentId(2);

		try {
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user);
			EasyMock.expect(taskDaoMock.load(EasyMock.eq(1))).andReturn(task);
			EasyMock.expect(userDaoMock.isInRole(user, "siteRoot", site)).andReturn(false);
			EasyMock.expect(userDaoMock.isInRole((UserHbm) EasyMock.anyObject(), (String) EasyMock.anyObject(), EasyMock.eq(site))).andReturn(false);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);
		EasyMock.replay(taskDaoMock);

		Integer[] vcIds = new Integer[] {1, 2};

		try {
			userService.addViewComponentsToTask(1, vcIds);
		} catch (Exception e) {
			if (e instanceof UserException) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false);
			}
		}

		EasyMock.verify(userDaoMock);
		EasyMock.verify(taskDaoMock);
	}

	/**
	 * test RemoveViewComponent from task
	 * expect: add correctly view component to task.
	 */
	public void testRemoveViewComponentsFromTask() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(true);
		user.setActiveSite(site);

		UserHbm sender = new UserHbmImpl();
		sender.setUserId("senderUser");

		UserHbm receiver = new UserHbmImpl();
		receiver.setUserId("receiverUser");

		TaskHbm task = new TaskHbmImpl();
		task.setTaskId(1);
		task.setReceiver(receiver);
		task.setSender(sender);
		task.setReceiverRole("testReceiverRole");

		ViewComponentHbm firstViewComponent = new ViewComponentHbmImpl();
		firstViewComponent.setViewComponentId(1);

		ViewComponentHbm secondViewComponent = new ViewComponentHbmImpl();
		secondViewComponent.setViewComponentId(2);

		try {
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user);
			EasyMock.expect(taskDaoMock.load(EasyMock.eq(1))).andReturn(task);
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(firstViewComponent);
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(2))).andReturn(secondViewComponent);
			EasyMock.expect(userDaoMock.isInRole(EasyMock.eq(user), EasyMock.eq("siteRoot"), EasyMock.eq(site))).andReturn(true);
			EasyMock.expect(userDaoMock.isInRole((UserHbm) EasyMock.anyObject(), (String) EasyMock.anyObject(), (SiteHbm) EasyMock.anyObject())).andReturn(false);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);
		EasyMock.replay(taskDaoMock);
		EasyMock.replay(viewComponentDaoMock);

		Integer[] vcIds = new Integer[] {1, 2};

		try {
			userService.removeViewComponentsFromTask(1, vcIds);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(userDaoMock);
		EasyMock.verify(taskDaoMock);
		EasyMock.verify(viewComponentDaoMock);

	}

	/**
	 * test RemoveViewComponentsFromTask
	 * expect: user is not site root. Throw  SecurityException.
	 */
	public void testRemoveViewComponentsFromTask1() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(false);
		user.setActiveSite(site);

		UserHbm sender = new UserHbmImpl();
		sender.setUserId("senderUser");

		UserHbm receiver = new UserHbmImpl();
		receiver.setUserId("receiverUser");
		receiver.setMasterRoot(false);

		TaskHbm task = new TaskHbmImpl();
		task.setTaskId(1);
		task.setReceiver(receiver);
		task.setSender(sender);
		task.setReceiverRole("testReceiverRole");

		ViewComponentHbm firstViewComponent = new ViewComponentHbmImpl();
		firstViewComponent.setViewComponentId(1);

		ViewComponentHbm secondViewComponent = new ViewComponentHbmImpl();
		secondViewComponent.setViewComponentId(2);

		try {
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user);
			EasyMock.expect(taskDaoMock.load(EasyMock.eq(1))).andReturn(task);
			EasyMock.expect(userDaoMock.isInRole(EasyMock.eq(user), EasyMock.eq("siteRoot"), EasyMock.eq(site))).andReturn(false);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);
		EasyMock.replay(taskDaoMock);

		Integer[] vcIds = new Integer[] {1, 2};

		try {
			userService.removeViewComponentsFromTask(1, vcIds);
		} catch (Exception e) {
			if (e instanceof UserException) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false);
			}
		}

		EasyMock.verify(userDaoMock);
		EasyMock.verify(taskDaoMock);
	}

	/**
	 * Test GetAllUser
	 * expect: user is siteRoot, return all users for that site
	 */
	public void testGetAllUser() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(true);
		user.setActiveSite(site);

		UserHbm user1 = new UserHbmImpl();
		user1.setUserId("testUser1");

		UserHbm user2 = new UserHbmImpl();
		user2.setUserId("testUser2");

		Collection<UserHbm> coll = new ArrayList<UserHbm>();

		coll.add(user1);
		coll.add(user2);
		try {
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user);
			EasyMock.expect(userDaoMock.isInRole(EasyMock.eq(user), EasyMock.eq("siteRoot"), EasyMock.eq(site))).andReturn(true);
			EasyMock.expect(userDaoMock.findAll(EasyMock.eq(site.getSiteId()))).andReturn(coll);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);

		try {
			UserValue[] users = userService.getAllUser();
			Assert.assertEquals(2, users.length);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(userDaoMock);
	}

	/**
	 * Test GetAllUser
	 * expect: user is siteRoot, return all users for that site
	 */
	public void testGetAllUser1() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(true);
		user.setActiveSite(site);

		UserHbm user1 = new UserHbmImpl();
		user1.setUserId("testUser1");

		UserHbm user2 = new UserHbmImpl();
		user2.setUserId("testUser2");

		Collection<UserHbm> coll = new ArrayList<UserHbm>();
		coll.add(user1);
		coll.add(user2);

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		unit.setUsers(coll);

		Collection<UnitHbm> units = new ArrayList<UnitHbm>();
		units.add(unit);

		user.setUnits(units);

		try {
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user);
			EasyMock.expect(userDaoMock.isInRole(EasyMock.eq(user), EasyMock.eq("siteRoot"), EasyMock.eq(site))).andReturn(false);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);

		try {
			UserValue[] users = userService.getAllUser();
			Assert.assertEquals(2, users.length);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(userDaoMock);
	}

	/**
	 * Test GetAllGroupsUsedInUnit
	 * expect: for the unit, get the assigned users and for them get the groups
	 */
	public void testGetAllGroupsUsedInUnit() {
		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");

		Collection<UserHbm> users = new ArrayList<UserHbm>();
		users.add(user);
		unit.setUsers(users);

		GroupHbm group1 = new GroupHbmImpl();
		group1.setGroupId(1);
		group1.setGroupName("testGroup1");

		GroupHbm group2 = new GroupHbmImpl();
		group2.setGroupId(2);
		group2.setGroupName("testGroup2");

		Collection<GroupHbm> groups = new ArrayList<GroupHbm>();
		groups.add(group1);
		groups.add(group2);

		try {
			EasyMock.expect(unitDaoMock.load(EasyMock.eq(1))).andReturn(unit);
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user);
			EasyMock.expect(userDaoMock.getGroups4ActiveSite(EasyMock.eq(user))).andReturn(groups);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(unitDaoMock);
		EasyMock.replay(userDaoMock);

		try {
			GroupValue[] values = userService.getAllGroupsUsedInUnit(unit.getUnitId());
			Assert.assertEquals(2, values.length);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test GetAllUser4GroupAndUnit
	 */
	public void testGetAllUser4GroupAndUnit() {
		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");

		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		GroupHbm group = new GroupHbmImpl();
		group.setGroupId(1);

		Collection<GroupHbm> groups = new ArrayList<GroupHbm>();
		groups.add(group);

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);

		UserHbm user1 = new UserHbmImpl();
		user1.setUserId("user1");
		user1.setGroups(groups);
		user1.setActiveSite(site);

		UserHbm user2 = new UserHbmImpl();
		user2.setUserId("user2");
		user2.setGroups(groups);
		user2.setActiveSite(site);

		Collection<UserHbm> users = new ArrayList<UserHbm>();
		users.add(user2);
		users.add(user1);

		unit.setUsers(users);

		try {
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user);
			EasyMock.expect(userDaoMock.isInUnit(EasyMock.eq(unit.getUnitId()), EasyMock.eq(user))).andReturn(true);
			EasyMock.expect(unitDaoMock.load(EasyMock.eq(unit.getUnitId()))).andReturn(unit);
			EasyMock.expect(userDaoMock.isInRole(EasyMock.eq(user2), EasyMock.eq(UserRights.SITE_ROOT), EasyMock.eq(site))).andReturn(true);
			EasyMock.expect(userDaoMock.isInRole(EasyMock.eq(user1), EasyMock.eq(UserRights.SITE_ROOT), EasyMock.eq(site))).andReturn(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);
		EasyMock.replay(unitDaoMock);

		try {
			UserValue[] result = userService.getAllUser4GroupAndUnit(group.getGroupId(), unit.getUnitId());
			Assert.assertEquals(2, result.length);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(userDaoMock);
		EasyMock.verify(unitDaoMock);
	}

	/**
	 * test GetGroups4User
	 * expect: user is MasterRoot so expect to get all the groups for the site
	 */
	public void testGetGroups4User() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(true);
		user.setActiveSite(site);

		GroupHbm group1 = new GroupHbmImpl();
		group1.setGroupId(1);

		GroupHbm group2 = new GroupHbmImpl();
		group2.setGroupId(2);

		Collection<GroupHbm> groups = new ArrayList<GroupHbm>();
		groups.add(group1);
		groups.add(group2);

		try {
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user);
			EasyMock.expect(groupDaoMock.findAll(EasyMock.eq(1))).andReturn(groups);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(userDaoMock);
		EasyMock.replay(groupDaoMock);

		try {
			GroupValue[] result = userService.getGroups4User("testUser");
			Assert.assertEquals(2, result.length);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * test GetGroups4User
	 * expect: user is not MasterRoot so expect to get all the groups for the active site
	 */
	public void testGetGroups4User1() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(false);
		user.setActiveSite(site);

		GroupHbm group1 = new GroupHbmImpl();
		group1.setGroupId(1);

		GroupHbm group2 = new GroupHbmImpl();
		group2.setGroupId(2);

		Collection<GroupHbm> groups = new ArrayList<GroupHbm>();
		groups.add(group1);
		groups.add(group2);

		try {
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user);
			EasyMock.expect(userDaoMock.getGroups4ActiveSite(EasyMock.eq(user))).andReturn(groups);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(userDaoMock);
		EasyMock.replay(groupDaoMock);

		try {
			GroupValue[] result = userService.getGroups4User("testUser");
			Assert.assertEquals(2, result.length);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * test GetUnits4User
	 * expect: user is MasterRoot so get all units for site
	 */
	public void testGetUnits4User() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(true);
		user.setActiveSite(site);

		UnitHbm unit1 = new UnitHbmImpl();
		unit1.setUnitId(1);

		UnitValue unit1Value = new UnitValue();
		unit1Value.setUnitId(1);

		UnitHbm unit2 = new UnitHbmImpl();
		unit2.setUnitId(2);

		UnitValue unit2Value = new UnitValue();
		unit2Value.setUnitId(2);

		Collection<UnitHbm> units = new ArrayList<UnitHbm>();
		units.add(unit1);
		units.add(unit2);

		try {
			EasyMock.expect(userDaoMock.load(EasyMock.eq("testUser"))).andReturn(user);
			EasyMock.expect(unitDaoMock.findAll(EasyMock.eq(user.getActiveSite().getSiteId()))).andReturn(units);
			EasyMock.expect(unitDaoMock.getDao(EasyMock.eq(unit1))).andReturn(unit1Value);
			EasyMock.expect(unitDaoMock.getDao(EasyMock.eq(unit2))).andReturn(unit2Value);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);
		EasyMock.replay(unitDaoMock);

		try {
			UnitValue[] result = userService.getUnits4User("testUser");
			Assert.assertEquals(2, result.length);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(userDaoMock);
		EasyMock.verify(unitDaoMock);

	}

	/**
	 * Test GetUnits4User
	 * expect: user is not MasterRoot so get all units for the active site
	 */
	public void testGetUnits4User1() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(false);
		user.setActiveSite(site);

		UnitHbm unit1 = new UnitHbmImpl();
		unit1.setUnitId(1);

		UnitValue unit1Value = new UnitValue();
		unit1Value.setUnitId(1);

		UnitHbm unit2 = new UnitHbmImpl();
		unit2.setUnitId(2);

		UnitValue unit2Value = new UnitValue();
		unit2Value.setUnitId(2);

		Collection<UnitHbm> units = new ArrayList<UnitHbm>();
		units.add(unit1);
		units.add(unit2);

		try {
			EasyMock.expect(userDaoMock.load(EasyMock.eq("testUser"))).andReturn(user);
			EasyMock.expect(userDaoMock.getUnits4ActiveSite(EasyMock.eq(user))).andReturn(units);
			EasyMock.expect(unitDaoMock.getDao(EasyMock.eq(unit1))).andReturn(unit1Value);
			EasyMock.expect(unitDaoMock.getDao(EasyMock.eq(unit2))).andReturn(unit2Value);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);
		EasyMock.replay(unitDaoMock);

		try {
			UnitValue[] result = userService.getUnits4User("testUser");
			Assert.assertEquals(2, result.length);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(userDaoMock);
		EasyMock.verify(unitDaoMock);
	}

	/**
	 * Test getAllTasks
	 */
	public void testGetAllTasks() {
		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");

		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		user.setActiveSite(site);

		TaskHbm task1 = new TaskHbmImpl();
		task1.setTaskId(1);

		TaskHbm task2 = new TaskHbmImpl();
		task2.setTaskId(2);

		Collection<TaskHbm> tasks = new ArrayList<TaskHbm>();
		tasks.add(task1);
		tasks.add(task2);

		try {
			EasyMock.expect(userDaoMock.load(EasyMock.eq("testUser"))).andReturn(user);
			EasyMock.expect(userDaoMock.isInRole(EasyMock.eq(user), EasyMock.eq(UserRights.SITE_ROOT), EasyMock.eq(user.getActiveSite()))).andReturn(false);
			EasyMock.expect(taskDaoMock.findAll(EasyMock.eq(user.getActiveSite().getSiteId()))).andReturn(tasks);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);
		EasyMock.replay(taskDaoMock);

		try {
			TaskValue[] result = userService.getAllTasks();
			Assert.assertEquals(2, result.length);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(userDaoMock);
		EasyMock.verify(taskDaoMock);
	}

	/**
	 * Test getAllTasks
	 * expect: return tasks that have the receiver the current user
	 */
	public void testGetAllTasks1() {
		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");

		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		user.setActiveSite(site);

		TaskHbm task1 = new TaskHbmImpl();
		task1.setTaskId(1);
		task1.setReceiverRole("testRole");
		task1.setReceiver(user);

		TaskHbm task2 = new TaskHbmImpl();
		task2.setTaskId(2);
		task2.setReceiverRole("testRole");
		task2.setReceiver(user);

		Collection<TaskHbm> tasks = new ArrayList<TaskHbm>();
		tasks.add(task1);
		tasks.add(task2);

		try {
			EasyMock.expect(userDaoMock.load(EasyMock.eq("testUser"))).andReturn(user);
			EasyMock.expect(userDaoMock.isInRole(EasyMock.eq(user), EasyMock.eq(UserRights.SITE_ROOT), EasyMock.eq(user.getActiveSite()))).andReturn(true);
			EasyMock.expect(taskDaoMock.findAll(EasyMock.eq(user.getActiveSite().getSiteId()))).andReturn(tasks);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);
		EasyMock.replay(taskDaoMock);

		try {
			TaskValue[] result = userService.getAllTasks();
			Assert.assertEquals(2, result.length);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(userDaoMock);
		EasyMock.verify(taskDaoMock);
	}

	/**
	 * test UpdateUser
	 * expect: user has rights to modify and the modifications are made
	 */
	public void testUpdateUser() {
		UserHbm user = new UserHbmImpl();
		user.setUserId("userGiven");
		user.setMasterRoot(true);

		UserValue userGiven = new UserValue();
		userGiven.setUserName("userGiven");
		userGiven.setFirstName("testFirstName");
		userGiven.setLastName("testLastName");
		userGiven.setEmail("testEmail@yahoo.com");

		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm userGivenHbm = new UserHbmImpl();
		userGivenHbm.setUserId("userGiven");
		userGivenHbm.setMasterRoot(false);

		UserHbm currentUser = new UserHbmImpl();
		currentUser.setUserId("testUser");
		currentUser.setActiveSite(site);

		try {
			EasyMock.expect(userDaoMock.load(EasyMock.eq("userGiven"))).andReturn(userGivenHbm);
			EasyMock.expect(userDaoMock.load(EasyMock.eq("testUser"))).andReturn(currentUser);
			EasyMock.expect(userDaoMock.isInRole(EasyMock.eq(currentUser), EasyMock.eq(UserRights.SITE_ROOT), EasyMock.eq(site))).andReturn(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);

		try {
			userService.updateUser(userGiven);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(userDaoMock);
	}

	/**
	 * Test UpdateUser
	 * expect: user has no rights to modify so exception is thrown 
	 */
	public void testUpdateUser1() {
		UserHbm user = new UserHbmImpl();
		user.setUserId("userGiven");
		user.setMasterRoot(true);

		UserValue userGiven = new UserValue();
		userGiven.setUserName("userGiven");
		userGiven.setFirstName("testFirstName");
		userGiven.setLastName("testLastName");
		userGiven.setEmail("testEmail@yahoo.com");

		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm userGivenHbm = new UserHbmImpl();
		userGivenHbm.setUserId("userGiven");
		userGivenHbm.setMasterRoot(false);
		userGivenHbm.setActiveSite(site);

		UserHbm currentUser = new UserHbmImpl();
		currentUser.setUserId("testUser");
		currentUser.setActiveSite(site);

		try {
			EasyMock.expect(userDaoMock.load(EasyMock.eq("userGiven"))).andReturn(userGivenHbm);
			EasyMock.expect(userDaoMock.load(EasyMock.eq("testUser"))).andReturn(currentUser);
			EasyMock.expect(userDaoMock.isInRole(EasyMock.eq(currentUser), EasyMock.eq(UserRights.SITE_ROOT), EasyMock.eq(site))).andReturn(false);
			EasyMock.expect(userDaoMock.isInRole(EasyMock.eq(userGivenHbm), EasyMock.eq(UserRights.SITE_ROOT), EasyMock.eq(site))).andReturn(false);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);

		try {
			userService.updateUser(userGiven);
		} catch (Exception e) {
			if (e instanceof UserException) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false);
			}
		}
		EasyMock.verify(userDaoMock);
	}

	/**
	 * Test changePassword4User
	 * expect: user is authorized to change the password and expect to return true
	 */
	public void testChangePassword4User() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setActiveSite(site);
		user.setMasterRoot(false);

		UserHbm userToChange = new UserHbmImpl();
		userToChange.setUserId("user");
		userToChange.setMasterRoot(false);
		userToChange.setActiveSite(site);

		try {
			EasyMock.expect(userDaoMock.load(EasyMock.eq("user"))).andReturn(userToChange);
			EasyMock.expect(userDaoMock.load(EasyMock.eq("testUser"))).andReturn(user);
			EasyMock.expect(userDaoMock.isInRole(EasyMock.eq(user), EasyMock.eq(UserRights.SITE_ROOT), EasyMock.eq(site))).andReturn(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);

		try {
			boolean result = userService.changePassword4User("user", "testOld", "testNew");
			Assert.assertTrue(result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(userDaoMock);
	}

	/**
	 * Test getAllSites4CurrentUser
	 * expect: user is SITE_ROOT, he gets all the sites assigned to him 
	 */
	public void testGetAllSites4CurrentUser() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(3);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(true);
		user.setActiveSite(site);

		SiteHbm site1 = new SiteHbmImpl();
		site1.setSiteId(1);

		SiteHbm site2 = new SiteHbmImpl();
		site2.setSiteId(2);

		Collection<SiteHbm> sites = new ArrayList<SiteHbm>();
		sites.add(site1);
		sites.add(site2);

		user.setSites(sites);
		try {
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user).times(3);
			EasyMock.expect(userDaoMock.isInRole(EasyMock.eq(user), EasyMock.eq(UserRights.SITE_ROOT), EasyMock.eq(site))).andReturn(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);
		try {
			SiteValue[] result = userService.getAllSites4CurrentUser();
			Assert.assertEquals(2, result.length);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(userDaoMock);
	}

	/**
	 * Test getConnectedUsers4Site
	 * expect: there are 2 users assigned to the site. First one is not master root
	 *         and the second one is master root. The result should be formed only by the 
	 *         user who is not master root 
	 */
	public void testGetConnectedUsers4Site() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user1 = new UserHbmImpl();
		user1.setUserId("user1");
		user1.setMasterRoot(false);

		UserHbm user2 = new UserHbmImpl();
		user2.setUserId("user2");
		user2.setMasterRoot(true);

		Collection<UserHbm> users = new ArrayList<UserHbm>();
		users.add(user1);
		users.add(user2);

		site.setUsers(users);

		try {
			EasyMock.expect(siteDaoMock.load(1)).andReturn(site);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(siteDaoMock);

		try {
			String[] result = userService.getConnectedUsers4Site(site.getSiteId());
			Assert.assertEquals(1, result.length);
			Assert.assertEquals("user1", result[0]);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(siteDaoMock);
	}

	/**
	 * Test SetConnectedUsers4Site
	 * 
	 */
	public void testSetConnectedUsers4Site() {
		String[] userIds = new String[] {"test1", "test2", "test3"};

		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm test1 = new UserHbmImpl();
		test1.setUserId("test1");

		UserHbm test2 = new UserHbmImpl();
		test2.setUserId("test2");

		UserHbm test3 = new UserHbmImpl();
		test3.setUserId("test3");

		UserHbm user1 = new UserHbmImpl();
		user1.setUserId("user1");
		user1.setMasterRoot(false);

		UserHbm user2 = new UserHbmImpl();
		user2.setUserId("user2");
		user2.setMasterRoot(true);

		Collection<UserHbm> users = new ArrayList<UserHbm>();
		users.add(user1);
		users.add(user2);

		site.setUsers(users);
		try {
			EasyMock.expect(siteDaoMock.load(1)).andReturn(site);
			EasyMock.expect(userDaoMock.load("test1")).andReturn(test1);
			EasyMock.expect(userDaoMock.load("test2")).andReturn(test2);
			EasyMock.expect(userDaoMock.load("test3")).andReturn(test3);
			EasyMock.expect(userDaoMock.load("user1")).andReturn(user1);
			EasyMock.expect(userDaoMock.load("user2")).andReturn(user2);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(siteDaoMock);
		EasyMock.replay(userDaoMock);

		try {
			userService.setConnectedUsers4Site(site.getSiteId(), userIds);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(siteDaoMock);
		EasyMock.verify(userDaoMock);
	}

}
