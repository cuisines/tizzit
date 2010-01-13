package de.juwimm.cms.test.hibernate.safeguard.model;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.safeguard.model.RealmJaasHbm;
import de.juwimm.cms.safeguard.model.RealmJaasHbmDao;
import de.juwimm.cms.safeguard.model.RealmJaasHbmDaoImpl;
import de.juwimm.cms.safeguard.model.RealmJaasHbmImpl;
import de.juwimm.cms.test.hibernate.HbmTestImpl;

public class RealmJaasDaoTest extends HbmTestImpl {

	@Autowired
	RealmJaasHbmDao realmJaasDao;
	SiteHbmDao siteDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();

		siteDao = EasyMock.createMock(SiteHbmDao.class);
		((RealmJaasHbmDaoImpl) realmJaasDao).setSiteHbmDao(siteDao);
	}

	/**
	 * Test Create
	 * expect: assign id
	 */
	public void testCreate() {
		RealmJaasHbm realmJaas = new RealmJaasHbmImpl();
		realmJaas.setRealmName("testName");

		try {
			realmJaas = realmJaasDao.create(realmJaas);
			Assert.assertNotNull(realmJaas);
			Assert.assertNotNull(realmJaas.getJaasRealmId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
