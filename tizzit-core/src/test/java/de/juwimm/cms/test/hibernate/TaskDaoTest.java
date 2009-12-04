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

	private void insertTask(TaskHbm task) {
		getJdbcTemplate().update(String.format("insert into task " + "(task_id,task_type,receiver_role,unit_id_fk,receiver,sender,user_comment,status,creation_date) values " + "(%d,1,'receiverRole',%d,'%s','%s','comment',1,1)", task.getTaskId(), task.getUnit().getUnitId(), task.getReceiver().getUserId(), task.getSender().getUserId()));
	}

	private void insertUser(String userName, Integer activeSiteId) {
		getJdbcTemplate().update(String.format("insert into usr " + "(user_id,first_name,last_name,email,login_date,active_site_id_fk,masterRoot,passwd) values " + "('%s','%s','%s','%s',0,%d,1,'123')", userName, userName, userName, userName + "@juwimm.de", activeSiteId));
	}

	public void insertSite(SiteHbm site) {
		getJdbcTemplate().update(String.format("insert into site " + "(site_id,site_name,site_short,mandator_dir,WYSIWYG_IMAGE_URL,HELP_URL,DCF_URL,PREVIEW_URL,PAGE_NAME_FULL,PAGE_NAME_CONTENT,PAGE_NAME_SEARCH,LAST_MODIFIED_DATE) values " + "(%d,'%s','%s','c:/mandatorDir','WYSIWYG_IMAGE_URL','HELP_URL','DCF_URL','PREVIEW_URL','page.html','content.html','search.html',0)", site.getSiteId(), site.getName(), site.getName()));
	}

	public void insertUnit(UnitHbm unit) {
		getJdbcTemplate().update(String.format("insert into unit (unit_id,name,last_Modified_date,site_id_fk) " + "values (%d,'%s',0,%d)", unit.getUnitId(), unit.getName(), unit.getSite().getSiteId()));
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
