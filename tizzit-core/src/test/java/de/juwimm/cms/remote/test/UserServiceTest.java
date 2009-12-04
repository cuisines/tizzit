package de.juwimm.cms.remote.test;

import org.easymock.EasyMock;
import org.junit.Assert;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.authorization.model.UserHbmImpl;
import de.juwimm.cms.model.SiteHbm;
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

public class UserServiceTest extends AbstractServiceTest {

	UnitHbmDao unitDaoMock;
	UserHbmDao userDaoMock;
	UserServiceSpringImpl userService;
	TaskHbmDao taskDaoMock;
	ViewComponentHbmDao viewComponentDaoMock;

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
	}

	/**
	 * Test creation of tasks
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
	 * test AddViewComponentsToTask
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
}
