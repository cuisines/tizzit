package de.juwimm.cms.test.hibernate.safeguard.model;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.safeguard.model.RealmLdapHbm;
import de.juwimm.cms.safeguard.model.RealmLdapHbmDao;
import de.juwimm.cms.safeguard.model.RealmLdapHbmImpl;
import de.juwimm.cms.test.hibernate.HbmTestImpl;

public class RealmLdapDaoTest extends HbmTestImpl {

	@Autowired
	RealmLdapHbmDao realmLdapDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	/**
	 * Test Create
	 * expect: assign id
	 */
	public void testCreate() {
		RealmLdapHbm realmLdap = new RealmLdapHbmImpl();
		realmLdap.setRealmName("testName");

		try {
			realmLdap = realmLdapDao.create(realmLdap);
			Assert.assertNotNull(realmLdap.getLdapRealmId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
