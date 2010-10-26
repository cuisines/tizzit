package de.juwimm.cms.test.hibernate;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.AccessRoleHbm;
import de.juwimm.cms.model.AccessRoleHbmDao;
import de.juwimm.cms.model.ViewComponentHbmDao;

public class AccessRoleDaoTest extends HbmTestImpl {

	@Autowired
	AccessRoleHbmDao accessRoleDao;
	ViewComponentHbmDao viewComponentDao;

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
	}

	public void insertAccessRole(AccessRoleHbm aRole) {
		this.getJdbcTemplate().update(String.format("insert into accessrole " + "(role_id) values " + "('%s')", aRole.getRoleId()));
	}

	public void initializeServiceBeans() {

	}

	//	public void init() {		
	//		
	//	}
	/**
	 * Test Create
	 * expect: assign id
	 * 		   has root unit assigned so only assign last modified date 	
	 */
	public void testCreate() {
		Assert.assertNotNull("AccessRoleDao must not be null !", accessRoleDao);
		AccessRoleHbm aRole = AccessRoleHbm.Factory.newInstance();
		aRole.setRoleId("testAccessRole1");
		aRole = accessRoleDao.create(aRole);
		Assert.assertNotNull("Role could not be created.", aRole);

		Assert.assertEquals("was expecting 1 element in accessrole.", 1, accessRoleDao.findAll().size());
	}

	public void testLoad() {
		try {
			Assert.assertNotNull("AccessRoleDao must not be null !", accessRoleDao);
			AccessRoleHbm aRole = AccessRoleHbm.Factory.newInstance();
			aRole.setRoleId("testAccessRole4");
			insertAccessRole(aRole);

			aRole = null;
			aRole = accessRoleDao.load("testAccessRole4");
			Assert.assertNotNull("could not load testAccessRole4", aRole);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test FindAll
	 * expect: 2 roles found
	 */
	public void testFindAll() {
		Assert.assertNotNull("AccessRoleDao must not be null !", accessRoleDao);
		AccessRoleHbm aRole = AccessRoleHbm.Factory.newInstance();
		aRole.setRoleId("testAccessRole3");
		insertAccessRole(aRole);

		Assert.assertEquals(1, accessRoleDao.findAll().size());
	}

}
