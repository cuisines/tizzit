package de.juwimm.cms.test.hibernate;

import java.util.Collection;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.model.UserHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.TaskHbm;
import de.juwimm.cms.model.TaskHbmDao;
import de.juwimm.cms.model.TaskHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmImpl;

public class TaskDaoTest extends HbmTestImpl {

	@Autowired
	TaskHbmDao taskDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	/**
	 * Test findAll for tasks
	 * 
	 */
	public void testFindAll() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);
		site.setName("testSite");
		insertSite(site);

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		unit.setName("testUnit");
		unit.setSite(site);
		insertUnit(unit);

		insertUser("receiver", 1);
		insertUser("sender", 1);

		UserHbm sender = new UserHbmImpl();
		sender.setUserId("sender");

		UserHbm receiver = new UserHbmImpl();
		receiver.setUserId("receiver");

		TaskHbm task = new TaskHbmImpl();
		task.setTaskId(1);
		task.setSender(sender);
		task.setUnit(unit);
		task.setReceiver(receiver);

		insertTask(task);

		try {
			Collection tasks = taskDao.findAll(1);
			Assert.assertEquals(1, tasks.size());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

	/**
	 * test FindBySender
	 * expect: return only the task with the specified sender
	 */
	public void testFindBySender() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(2);
		site.setName("testSite2");
		insertSite(site);

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(2);
		unit.setName("testUnit2");
		unit.setSite(site);
		insertUnit(unit);

		insertUser("receiverFindBySender", 2);
		insertUser("senderFindBySender", 2);

		UserHbm sender = new UserHbmImpl();
		sender.setUserId("senderFindBySender");

		UserHbm receiver = new UserHbmImpl();
		receiver.setUserId("receiverFindBySender");

		TaskHbm task = new TaskHbmImpl();
		task.setTaskId(2);
		task.setSender(sender);
		task.setUnit(unit);
		task.setReceiver(receiver);

		insertTask(task);

		try {
			Collection tasks = taskDao.findBySender("senderFindBySender");
			Assert.assertEquals(1, tasks.size());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * test FindByReceiver
	 * expect: return only the task with the specified receiver
	 * */
	public void testFindByReceiver() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(3);
		site.setName("testSiteFindByReceiver");
		insertSite(site);

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(3);
		unit.setName("testUnitFindByReceiver");
		unit.setSite(site);
		insertUnit(unit);

		insertUser("receiverFindByReceiver", 3);
		insertUser("senderFindByReceiver", 3);

		UserHbm sender = new UserHbmImpl();
		sender.setUserId("senderFindByReceiver");

		UserHbm receiver = new UserHbmImpl();
		receiver.setUserId("receiverFindByReceiver");

		TaskHbm task = new TaskHbmImpl();
		task.setTaskId(3);
		task.setSender(sender);
		task.setUnit(unit);
		task.setReceiver(receiver);

		insertTask(task);

		insertUser("receiverOneFindByReceiver", 3);
		insertUser("senderOneFindByReceiver", 3);

		UserHbm senderOne = new UserHbmImpl();
		senderOne.setUserId("senderOneFindByReceiver");

		UserHbm receiverOne = new UserHbmImpl();
		receiverOne.setUserId("receiverOneFindByReceiver");

		TaskHbm taskOne = new TaskHbmImpl();
		taskOne.setTaskId(4);
		taskOne.setSender(senderOne);
		taskOne.setUnit(unit);
		taskOne.setReceiver(receiverOne);

		insertTask(taskOne);

		try {
			Collection tasks = taskDao.findByReceiver("receiverFindByReceiver");
			Assert.assertEquals(1, tasks.size());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
