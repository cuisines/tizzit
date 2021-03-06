package de.juwimm.cms.test.hibernate;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.model.SiteHbmDaoImpl;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmDao;
import de.juwimm.cms.model.UnitHbmImpl;
import de.juwimm.cms.test.hibernate.HbmTestImpl;

public class SiteDaoTest extends HbmTestImpl {

	@Autowired
	SiteHbmDao siteDao;

	UnitHbmDao unitDao;

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();

		unitDao = EasyMock.createMock(UnitHbmDao.class);
		((SiteHbmDaoImpl) siteDao).setUnitHbmDao(unitDao);
		
	}

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub
	}

	public void init() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);
		site.setName("testSite");
		insertSite(site);
	}

	public void testLoad() {
		init();
		try {
			SiteHbm site = siteDao.load(1);
			Assert.assertNotNull(site);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test FindAll
	 * expect: 2 sites found
	 */
	public void testFindAll() {
		init();
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(2);
		site.setName("testSite");
		insertSite(site);
		Assert.assertEquals(2, siteDao.findAll().size());
		//		try {
		//			
		//		} catch (Exception e) {
		//			Assert.assertTrue(false);
		//		}
	}

	/**
	 * Test Create
	 * expect: assign id
	 * 		   has root unit assigned so only assign last modified date 	
	 */
	public void testCreate() {
		UnitHbm rootUnit = new UnitHbmImpl();
		rootUnit.setUnitId(1);

		SiteHbm site = new SiteHbmImpl();
		site.setRootUnit(rootUnit);
		site.setName("testSite");
		site.setShortName("testShortName");
		site.setMandatorDir("testMandatoryDir");
		site.setWysiwygImageUrl("testImageUrl");
		site.setHelpUrl("testHelpUrl");
		site.setDcfUrl("testDcfUrl");
		site.setPreviewUrlLiveServer("testPreviewUrlLive");
		site.setPreviewUrlWorkServer("testPreviewUrlWork");
		site.setPageNameFull("testPageName");
		site.setPageNameContent("testPageNameContent");
		site.setPageNameSearch("testPageNameSearch");
		site.setExternalSiteSearch(false);
		site.setUpdateSiteIndex(false);

		try {
			site = siteDao.create(site);
			Assert.assertNotNull(site.getSiteId());
			Assert.assertNotNull(site.getLastModifiedDate());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test Create
	 * expect: assign id
	 * 		   doesn't have root unit assigned so create root unit
	 *         assign last modified date 	
	 */
	public void testCreate1() {
		UnitHbm rootUnit = new UnitHbmImpl();
		rootUnit.setUnitId(1);

		SiteHbm site = new SiteHbmImpl();
		site.setName("testSite");
		site.setShortName("testShortName");
		site.setMandatorDir("testMandatoryDir");
		site.setWysiwygImageUrl("testImageUrl");
		site.setHelpUrl("testHelpUrl");
		site.setDcfUrl("testDcfUrl");
		site.setPreviewUrlLiveServer("testPreviewUrlLive");
		site.setPreviewUrlWorkServer("testPreviewUrlWork");
		site.setPageNameFull("testPageName");
		site.setPageNameContent("testPageNameContent");
		site.setPageNameSearch("testPageNameSearch");
		site.setExternalSiteSearch(false);
		site.setUpdateSiteIndex(false);

		try {
			EasyMock.expect(unitDao.create((UnitHbm) EasyMock.anyObject())).andReturn(rootUnit);
			unitDao.update((UnitHbm) EasyMock.anyObject());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(unitDao);

		try {
			site = siteDao.create(site);
			Assert.assertNotNull(site.getSiteId());
			Assert.assertNotNull(site.getRootUnit());
			Assert.assertNotNull(site.getLastModifiedDate());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(unitDao);
	}
}
