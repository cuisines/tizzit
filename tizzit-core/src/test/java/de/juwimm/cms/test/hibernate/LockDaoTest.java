package de.juwimm.cms.test.hibernate;

import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.authorization.model.UserHbmImpl;
import de.juwimm.cms.model.LockHbm;
import de.juwimm.cms.model.LockHbmDao;
import de.juwimm.cms.model.LockHbmDaoImpl;
import de.juwimm.cms.model.LockHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmImpl;

public class LockDaoTest extends HbmTestImpl {

	@Autowired
	LockHbmDao lockDao;
	UserHbmDao userDaoMock;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		super.mockAuthetication();

		userDaoMock = EasyMock.createMock(UserHbmDao.class);
		((LockHbmDaoImpl) lockDao).setUserHbmDao(userDaoMock);

	}

	private void insertUser(String userName, Integer activeSiteId) {
		getJdbcTemplate().update(String.format("insert into usr " + "(user_id,first_name,last_name,email,login_date,active_site_id_fk,masterRoot,passwd) values " + "('%s','%s','%s','%s',0,%d,1,'123')", userName, userName, userName, userName + "@juwimm.de", activeSiteId));
	}

	public void insertSite(SiteHbm site) {
		getJdbcTemplate().update(String.format("insert into site " + "(site_id,site_name,site_short,mandator_dir,WYSIWYG_IMAGE_URL,HELP_URL,DCF_URL,PREVIEW_URL,PAGE_NAME_FULL,PAGE_NAME_CONTENT,PAGE_NAME_SEARCH,LAST_MODIFIED_DATE,UPDATE_SITE_INDEX, EXTERNAL_SITE_SEARCH) values " + "(%d,'%s','%s','c:/mandatorDir','WYSIWYG_IMAGE_URL','HELP_URL','DCF_URL','PREVIEW_URL','page.html','content.html','search.html',0,0,0)", site.getSiteId(), site.getName(), site.getName()));
	}

	private void insertLock(LockHbm lock) {
		getJdbcTemplate().update(String.format("insert into locks (lock_id,create_date,owner_id_fk) " + "values (%d,'0','testUser')", lock.getLockId()));
	}

	private void init() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);
		insertSite(site);
		insertUser("testUser", 1);

		LockHbm lock = new LockHbmImpl();
		lock.setLockId(1);
		lock.setCreateDate(0);
		insertLock(lock);

		LockHbm lock1 = new LockHbmImpl();
		lock1.setLockId(2);
		lock1.setCreateDate(0);
		insertLock(lock1);
	}

	/**
	 * Test Create
	 * expect: assign id and the current user as the owner for the lock
	 */
	public void testCreate() {
		LockHbm lock = new LockHbmImpl();

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");

		try {
			EasyMock.expect(userDaoMock.load(EasyMock.eq("testUser"))).andReturn(user);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);

		try {
			lock = lockDao.create(lock);
			Assert.assertNotNull(lock.getLockId());
			Assert.assertEquals(user.getUserId(), lock.getOwner().getUserId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(userDaoMock);
	}

	/**
	 * Test GetAll
	 * expect: 
	 */
	public void testGetAll() {
		init();
		try {
			Collection collection = lockDao.findAll();
			Assert.assertEquals(2, collection.size());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}
}
