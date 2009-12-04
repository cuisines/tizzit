package de.juwimm.cms.test.hibernate;

import java.util.ArrayList;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.authorization.model.RoleHbm;
import de.juwimm.cms.authorization.model.RoleHbmDao;
import de.juwimm.cms.authorization.model.RoleHbmImpl;
import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.authorization.model.UserHbmDaoImpl;
import de.juwimm.cms.authorization.model.UserHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmImpl;

public class UserDaoTest extends HbmTestImpl {

	@Autowired
	UserHbmDao userDao;
	RoleHbmDao roleDaoMock;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	private void insertUser(String userName, Integer activeSiteId) {
		getJdbcTemplate().update(String.format("insert into usr " + "(user_id,first_name,last_name,email,login_date,active_site_id_fk,masterRoot,passwd) values " + "('%s','%s','%s','%s',0,%d,1,'123')", userName, userName, userName, userName + "@juwimm.de", activeSiteId));
	}

	public void insertSite(SiteHbm site) {
		getJdbcTemplate().update(String.format("insert into site " + "(site_id,site_name,site_short,mandator_dir,WYSIWYG_IMAGE_URL,HELP_URL,DCF_URL,PREVIEW_URL,PAGE_NAME_FULL,PAGE_NAME_CONTENT,PAGE_NAME_SEARCH,LAST_MODIFIED_DATE) values " + "(%d,'%s','%s','c:/mandatorDir','WYSIWYG_IMAGE_URL','HELP_URL','DCF_URL','PREVIEW_URL','page.html','content.html','search.html',0)", site.getSiteId(), site.getName(), site.getName()));
	}

	/**
	 * Test isUserInRole
	 * expect: for a user with siteRoot and unitAdmin when interrogated for siteRoot to return true
	 *		   for MasterRoot to have rights regardless of the sent as parameter role	
	 */
	public void testIsUserInRole() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);
		site.setName("testSite");
		insertSite(site);

		insertUser("testUser", 1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setMasterRoot(false);

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
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);
		site.setName("testSite");
		insertSite(site);

		insertUser("testUser", 1);

		Collection units = new ArrayList<UnitHbm>();

		UnitHbm unit1 = new UnitHbmImpl();
		unit1.setUnitId(1);

		UnitHbm unit2 = new UnitHbmImpl();
		unit2.setUnitId(2);

		units.add(unit1);
		units.add(unit2);

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
