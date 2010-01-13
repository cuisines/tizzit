package de.juwimm.cms.test.hibernate.safeguard.model;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.safeguard.model.RealmSimplePwHbm;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbmDao;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbmImpl;
import de.juwimm.cms.test.hibernate.HbmTestImpl;

public class RealmSimplePwDaoTest extends HbmTestImpl {

	@Autowired
	RealmSimplePwHbmDao realmSimplePwDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	/**
	 * Test Create
	 * expect: assign id
	 */
	public void testCreate() {
		RealmSimplePwHbm realmSimplePw = new RealmSimplePwHbmImpl();
		realmSimplePw.setRealmName("testName");

		try {
			realmSimplePw = realmSimplePwDao.create(realmSimplePw);
			Assert.assertNotNull(realmSimplePw.getSimplePwRealmId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
