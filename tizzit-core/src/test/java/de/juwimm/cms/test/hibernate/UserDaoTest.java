package de.juwimm.cms.test.hibernate;

import java.util.ArrayList;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.authorization.model.GroupHbm;
import de.juwimm.cms.authorization.model.GroupHbmDao;
import de.juwimm.cms.authorization.model.GroupHbmImpl;
import de.juwimm.cms.authorization.model.RoleHbm;
import de.juwimm.cms.authorization.model.RoleHbmDao;
import de.juwimm.cms.authorization.model.RoleHbmImpl;
import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.authorization.model.UserHbmDaoImpl;
import de.juwimm.cms.authorization.model.UserHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmDao;
import de.juwimm.cms.model.UnitHbmImpl;

public class UserDaoTest extends HbmTestImpl {

	@Autowired
	UserHbmDao userDao;
	@Autowired
	SiteHbmDao siteDao;

	RoleHbmDao roleDaoMock;
	GroupHbmDao groupDaoMock;
	UnitHbmDao unitDaoMock;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		mockAuthetication();

		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);
		site.setName("testSite");
		insertSite(site);

		insertUser("testUser", 1);

		groupDaoMock = EasyMock.createMock(GroupHbmDao.class);
		((UserHbmDaoImpl) userDao).setGroupHbmDao(groupDaoMock);

		unitDaoMock = EasyMock.createMock(UnitHbmDao.class);
		((UserHbmDaoImpl) userDao).setUnitHbmDao(unitDaoMock);

	}

	private void insertUser(String userName, Integer activeSiteId) {
		getJdbcTemplate().update(String.format("insert into usr " + "(user_id,first_name,last_name,email,login_date,active_site_id_fk,masterRoot,passwd) values " + "('%s','%s','%s','%s',0,%d,1,'123')", userName, userName, userName, userName + "@juwimm.de", activeSiteId));
	}

	public void insertSite(SiteHbm site) {
		getJdbcTemplate().update(String.format("insert into site " + "(site_id,site_name,site_short,mandator_dir,WYSIWYG_IMAGE_URL,HELP_URL,DCF_URL,PREVIEW_URL,PAGE_NAME_FULL,PAGE_NAME_CONTENT,PAGE_NAME_SEARCH,LAST_MODIFIED_DATE,UPDATE_SITE_INDEX, EXTERNAL_SITE_SEARCH) values " + "(%d,'%s','%s','c:/mandatorDir','WYSIWYG_IMAGE_URL','HELP_URL','DCF_URL','PREVIEW_URL','page.html','content.html','search.html',0,0,0)", site.getSiteId(), site.getName(), site.getName()));
	}

	/**
	 *  Test Create
	 *  expect: set userId to lower case
	 */
	public void testCreate() {
		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");

		try {
			user = userDao.create(user);
			Assert.assertNotNull(user);
			Assert.assertEquals("testuser", user.getUserId());

		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 *  Test Create
	 *  expect: assign userId
	 */
	public void testCreate1() {
		UserHbm user = new UserHbmImpl();

		try {
			user = userDao.create(user);
			Assert.assertNotNull(user);
			Assert.assertNotNull(user.getUserId());

		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test GetGroups4ActiveSite
	 * expect: user is masterRoot so he gets all the groups
	 */
	public void testGetGroups4ActiveSite() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setActiveSite(site);
		user.setMasterRoot(true);

		GroupHbm group1 = new GroupHbmImpl();
		group1.setGroupId(1);

		GroupHbm group2 = new GroupHbmImpl();
		group2.setGroupId(2);

		Collection<GroupHbm> groups = new ArrayList<GroupHbm>();
		groups.add(group1);
		groups.add(group2);

		try {
			EasyMock.expect(groupDaoMock.findAll(EasyMock.eq(1))).andReturn(groups);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(groupDaoMock);

		try {
			Collection result = userDao.getGroups4ActiveSite(user);
			Assert.assertEquals(2, result.size());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(groupDaoMock);
	}

	/**
	 * Test GetGroups4ActiveSite
	 * expect: user is not masterRoot so he gets the groups for user and site
	 */
	public void testGetGroups4ActiveSite1() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setActiveSite(site);
		user.setMasterRoot(false);

		GroupHbm group1 = new GroupHbmImpl();
		group1.setGroupId(1);

		GroupHbm group2 = new GroupHbmImpl();
		group2.setGroupId(2);

		Collection<GroupHbm> groups = new ArrayList<GroupHbm>();
		groups.add(group1);
		groups.add(group2);

		try {
			EasyMock.expect(groupDaoMock.findByUserAndSite(EasyMock.eq("testUser"), EasyMock.eq(1))).andReturn(groups);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(groupDaoMock);

		try {
			Collection result = userDao.getGroups4ActiveSite(user);
			Assert.assertEquals(2, result.size());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(groupDaoMock);
	}

	/**
	 * Test GetUnits4ActiveSite
	 * expect: user is master root so return all units for site
	 */
	public void testGetUnits4ActiveSite() {
		SiteHbm site = siteDao.load(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setActiveSite(site);
		user.setMasterRoot(true);

		Collection collection = new ArrayList<UnitHbm>();

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		unit.setName("testName");

		collection.add(unit);

		try {
			EasyMock.expect(unitDaoMock.findAll(EasyMock.eq(site.getSiteId()))).andReturn(collection);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(unitDaoMock);

		try {
			Collection result = userDao.getUnits4ActiveSite(user);
			Assert.assertNotNull(result);
			Assert.assertEquals(1, result.size());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(unitDaoMock);

	}

	/**
	 * Test GetUnits4ActiveSite
	 * expect: user is not master root or siteRoot so return all units for user and site
	 */
	public void testGetUnits4ActiveSite1() {
		SiteHbm site = siteDao.load(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setActiveSite(site);
		user.setMasterRoot(false);

		Collection collection = new ArrayList<UnitHbm>();

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		unit.setName("testName");

		collection.add(unit);

		try {
			EasyMock.expect(unitDaoMock.findByUserAndSite(EasyMock.eq("testUser"), EasyMock.eq(site.getSiteId()))).andReturn(collection);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(unitDaoMock);

		try {
			Collection result = userDao.getUnits4ActiveSite(user);
			Assert.assertNotNull(result);
			Assert.assertEquals(1, result.size());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(unitDaoMock);

	}

	/**
	 * Test isInRole
	 * expect: for a user with siteRoot and unitAdmin when interrogated for siteRoot to return true
	 *		   for MasterRoot to have rights regardless of the sent as parameter role	
	 */
	public void testIsInRole() {
		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(false);

		SiteHbm site = siteDao.load(1);

		UserHbm userMasterRoot = new UserHbmImpl();
		userMasterRoot.setUserId("testUser");
		userMasterRoot.setMasterRoot(true);

		Collection roles = new ArrayList<RoleHbm>();
		RoleHbm role1 = new RoleHbmImpl();
		RoleHbm role2 = new RoleHbmImpl();
		role1.setRoleId("siteRoot");
		role2.setRoleId("unitAdmin");
		roles.add(role1);
		roles.add(role2);

		roleDaoMock = EasyMock.createMock(RoleHbmDao.class);
		((UserHbmDaoImpl) userDao).setRoleHbmDao(roleDaoMock);

		try {
			EasyMock.expect(roleDaoMock.findByUserAndSite("testUser", 1)).andReturn(roles);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(roleDaoMock);

		try {
			boolean result = userDao.isInRole(user, "siteRoot", site);
			Assert.assertTrue(result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		try {
			boolean result = userDao.isInRole(userMasterRoot, "siteRoot", site);
			Assert.assertTrue(result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test isInUnit
	 * expect: for a user who is masterRoot return true
	 *         for a user who is site_root return true
	 *         for a user who is assigned to the unit sent as parameter return true 
	 *  
	 */
	public void testIsInUnit() {
		Collection units = new ArrayList<UnitHbm>();

		UnitHbm unit1 = new UnitHbmImpl();
		unit1.setUnitId(1);

		UnitHbm unit2 = new UnitHbmImpl();
		unit2.setUnitId(2);

		units.add(unit1);
		units.add(unit2);

		SiteHbm site = siteDao.load(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(false);
		user.setActiveSite(site);

		UserHbm userMasterRoot = new UserHbmImpl();
		userMasterRoot.setUserId("testUser");
		userMasterRoot.setMasterRoot(true);

		UserHbm userTestUnits = new UserHbmImpl();
		userTestUnits.setUserId("testUserUnits");
		userTestUnits.setUnits(units);
		userTestUnits.setMasterRoot(false);
		userTestUnits.setActiveSite(site);

		Collection roles = new ArrayList<RoleHbm>();
		RoleHbm role1 = new RoleHbmImpl();
		RoleHbm role2 = new RoleHbmImpl();
		role1.setRoleId("siteRoot");
		role2.setRoleId("unitAdmin");
		roles.add(role1);
		roles.add(role2);

		roleDaoMock = EasyMock.createMock(RoleHbmDao.class);
		((UserHbmDaoImpl) userDao).setRoleHbmDao(roleDaoMock);

		Collection emptyRoles = new ArrayList<RoleHbm>();

		try {
			EasyMock.expect(roleDaoMock.findByUserAndSite("testUser", 1)).andReturn(roles);
			EasyMock.expect(roleDaoMock.findByUserAndSite("testUserUnits", 1)).andReturn(emptyRoles);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(roleDaoMock);

		/**test for MasterRoot*/
		try {
			boolean result = userDao.isInUnit(1, userMasterRoot);
			Assert.assertTrue(result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		/**test for site_root*/
		try {
			boolean result = userDao.isInUnit(1, user);
			Assert.assertTrue(result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		/**test in the same unit*/
		try {
			boolean result = userDao.isInUnit(1, userTestUnits);
			Assert.assertTrue(result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
