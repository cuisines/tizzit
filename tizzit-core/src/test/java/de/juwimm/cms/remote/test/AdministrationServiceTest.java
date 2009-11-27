package de.juwimm.cms.remote.test;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Assert;

import de.juwimm.cms.model.HostHbm;
import de.juwimm.cms.model.HostHbmDao;
import de.juwimm.cms.model.HostHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmDao;
import de.juwimm.cms.model.UnitHbmImpl;
import de.juwimm.cms.remote.AdministrationServiceSpringImpl;
import de.juwimm.cms.vo.HostValue;

public class AdministrationServiceTest extends TestCase {

	AdministrationServiceSpringImpl administrationService;
	SiteHbmDao siteDaoMock;
	HostHbmDao hostDaoMock;
	UnitHbmDao unitDaoMock;

	@Override
	protected void setUp() throws Exception {
		administrationService = new AdministrationServiceSpringImpl();
		siteDaoMock = EasyMock.createMock(SiteHbmDao.class);
		administrationService.setSiteHbmDao(siteDaoMock);
		hostDaoMock = EasyMock.createMock(HostHbmDao.class);
		administrationService.setHostHbmDao(hostDaoMock);
		unitDaoMock = EasyMock.createMock(UnitHbmDao.class);
		administrationService.setUnitHbmDao(unitDaoMock);
	}

	/**
	 * Test getHostsForSite
	 * expect: for a given site with 2 hosts to return a collection containing them
	 */
	public void testGetHostsForSite() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);
		site.setName("testSite");

		SiteHbm siteSecond = new SiteHbmImpl();
		siteSecond.setSiteId(2);
		siteSecond.setName("testSiteSecond");

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		unit.setName("testUnit");
		unit.setSite(site);

		HostHbm hostHbmFirst = new HostHbmImpl();
		hostHbmFirst.setLiveserver(true);
		hostHbmFirst.setRedirectUrl("redirectUrlFirst");
		hostHbmFirst.setUnit(unit);
		hostHbmFirst.setSite(site);

		HostHbm hostHbmSecond = new HostHbmImpl();
		hostHbmSecond.setLiveserver(true);
		hostHbmSecond.setRedirectUrl("redirectUrlSecond");
		hostHbmSecond.setUnit(unit);
		hostHbmSecond.setSite(site);

		HostHbm hostHbmThird = new HostHbmImpl();
		hostHbmThird.setLiveserver(true);
		hostHbmThird.setRedirectUrl("redirectUrlThird");
		hostHbmThird.setUnit(unit);
		hostHbmThird.setSite(siteSecond);

		site.getHost().add(hostHbmFirst);
		site.getHost().add(hostHbmSecond);

		siteSecond.getHost().add(hostHbmThird);

		try {
			EasyMock.expect(siteDaoMock.load(EasyMock.eq(1))).andReturn(site);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(siteDaoMock);

		try {
			HostValue[] hosts = administrationService.getHostsForSite(site.getSiteId());
			Assert.assertNotNull(hosts);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test UpdateHost
	 * expect: new values for the properties are set right
	 */
	public void testUpdateHost() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);
		site.setName("testSite");

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);
		unit.setName("testUnit");
		unit.setSite(site);

		HostHbm hostHbm = new HostHbmImpl();
		hostHbm.setHostName("testHost");
		hostHbm.setLiveserver(true);
		hostHbm.setRedirectUrl("redirectUrl");
		hostHbm.setUnit(unit);
		hostHbm.setSite(site);

		HostValue hostValue = new HostValue();
		hostValue.setHostName("testHost");
		hostValue.setRedirectUrl("updateRedirectUrl");
		hostValue.setLiveServer(false);
		hostValue.setUnitId(unit.getUnitId());
		hostValue.setSiteId(site.getSiteId());
		try {
			EasyMock.expect(hostDaoMock.load(EasyMock.eq("testHost"))).andReturn(hostHbm);
			EasyMock.expect(siteDaoMock.load(EasyMock.eq(1))).andReturn(site);
			EasyMock.expect(unitDaoMock.load(EasyMock.eq(1))).andReturn(unit);
			hostDaoMock.update((HostHbm) EasyMock.anyObject());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(hostDaoMock);
		EasyMock.replay(siteDaoMock);
		EasyMock.replay(unitDaoMock);

		try {
			administrationService.updateHost(hostValue);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}
}
