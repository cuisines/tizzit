package de.juwimm.cms.remote.test;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Assert;

import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.remote.MasterRootServiceSpring;
import de.juwimm.cms.remote.MasterRootServiceSpringBase;
import de.juwimm.cms.remote.MasterRootServiceSpringImpl;
import de.juwimm.cms.vo.SiteValue;

public class MasterRootServiceTest extends TestCase {
	MasterRootServiceSpring masterRootService;
	SiteHbmDao siteDaoMock;

	@Override
	protected void setUp() throws Exception {
		masterRootService = new MasterRootServiceSpringImpl();
		siteDaoMock = EasyMock.createMock(SiteHbmDao.class);
		((MasterRootServiceSpringBase) masterRootService).setSiteHbmDao(siteDaoMock);

	}

	public void testCreateSite() {
		SiteValue site = new SiteValue();
		site.setSiteId(1);
		site.setName("testSite");
		site.setDcfUrl("testDcfUrl");
		site.setConfigXML("testConfigXml");
		site.setHelpUrl("testHelpUrl");
		site.setCacheExpire(1);
		site.setLastModifiedDate(0);
		site.setMandatorDir("testMandatorDirectory");
		site.setPageNameContent("testPageNameContent");
		site.setPageNameFull("testPageNameFull");
		site.setPageNameSearch("testPageNameSearch");
		site.setPreviewUrl("testPreviewUrl");
		site.setShortName("testShortName");

	}

	/**
	 * Test changeSite
	 * expect: replace old siteValue with new one
	 */
	public void testChangeSite() {
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);
		site.setName("testSite");
		site.setDcfUrl("testDcfUrl");
		site.setConfigXML("testConfigXml");
		site.setHelpUrl("testHelpUrl");
		site.setCacheExpire(1);
		site.setLastModifiedDate(0);
		site.setMandatorDir("testMandatorDirectory");
		site.setPageNameContent("testPageNameContent");
		site.setPageNameFull("testPageNameFull");
		site.setPageNameSearch("testPageNameSearch");
		site.setPreviewUrl("testPreviewUrl");
		site.setShortName("testShortName");

		SiteValue siteValue = new SiteValue();
		siteValue.setSiteId(1);
		siteValue.setName("testSiteValue");
		siteValue.setDcfUrl("testDcfUrlValue");
		siteValue.setConfigXML("testConfigXmlValue");
		siteValue.setHelpUrl("testHelpUrlValue");
		siteValue.setCacheExpire(1);
		siteValue.setLastModifiedDate(0);
		siteValue.setMandatorDir("testMandatorDirectoryValue");
		siteValue.setPageNameContent("testPageNameContentValue");
		siteValue.setPageNameFull("testPageNameFullValue");
		siteValue.setPageNameSearch("testPageNameSearchValue");
		siteValue.setPreviewUrl("testPreviewUrlValue");
		siteValue.setShortName("testShortNameValue");

		try {
			EasyMock.expect(siteDaoMock.load(1)).andReturn(site).times(2);
			siteDaoMock.update((SiteHbm) EasyMock.anyObject());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(siteDaoMock);

		try {
			masterRootService.changeSite(siteValue);
			site = siteDaoMock.load(1);
			Assert.assertEquals("testSiteValue", site.getName());
			Assert.assertEquals("testDcfUrlValue", site.getDcfUrl());
			Assert.assertEquals("testHelpUrlValue", site.getHelpUrl());
			Assert.assertEquals("testMandatorDirectoryValue", site.getMandatorDir());
			Assert.assertEquals("testPageNameContentValue", site.getPageNameContent());
			Assert.assertEquals("testPageNameFullValue", site.getPageNameFull());
			Assert.assertEquals("testPageNameSearchValue", site.getPageNameSearch());
			Assert.assertEquals("testPreviewUrlValue", site.getPreviewUrl());
			Assert.assertEquals("testShortNameValue", site.getShortName());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(siteDaoMock);

	}
}
