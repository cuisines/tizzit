package de.juwimm.cms.test.hibernate;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.HostHbm;
import de.juwimm.cms.model.HostHbmDao;
import de.juwimm.cms.model.HostHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmImpl;

public class HostDaoTest extends HbmTestImpl {

	@Autowired
	HostHbmDao hostDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	public void testLoad() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);
		site.setName("testSite");
		insertSite(site);

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		unit.setName("testUnit");
		unit.setSite(site);
		insertUnit(unit);

		HostHbm hostHbm = new HostHbmImpl();
		hostHbm.setLiveserver(true);
		hostHbm.setRedirectUrl("redirectUrl");
		hostHbm.setUnit(unit);
		hostHbm.setSite(site);
		insertHost(hostHbm);

		try {
			HostHbm loadHost = hostDao.load("testHost");
			Assert.assertNotNull(loadHost);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
