package de.juwimm.cms.test.hibernate.safeguard.model;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.safeguard.model.RealmJdbcHbm;
import de.juwimm.cms.safeguard.model.RealmJdbcHbmDao;
import de.juwimm.cms.safeguard.model.RealmJdbcHbmImpl;
import de.juwimm.cms.test.hibernate.HbmTestImpl;

public class RealmJdbcDaoTest extends HbmTestImpl {

	@Autowired
	RealmJdbcHbmDao realmJdbcDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	/**
	 * Test Create
	 * expect: assign id
	 */
	public void testCreate() {
		RealmJdbcHbm realmJdbc = new RealmJdbcHbmImpl();
		realmJdbc.setRealmName("testName");

		try {
			realmJdbc = realmJdbcDao.create(realmJdbc);
			Assert.assertNotNull(realmJdbc.getJdbcRealmId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
