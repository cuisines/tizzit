package de.juwimm.cms.test.hibernate;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.model.SiteHbmImpl;

public class SiteDaoTest extends HbmTestImpl {

	@Autowired
	SiteHbmDao siteDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub
	}

	public void insertSite(SiteHbm site) {
		getJdbcTemplate().update(String.format("insert into site " + "(site_id,site_name,site_short,mandator_dir,WYSIWYG_IMAGE_URL,HELP_URL,DCF_URL,PREVIEW_URL,PAGE_NAME_FULL,PAGE_NAME_CONTENT,PAGE_NAME_SEARCH,LAST_MODIFIED_DATE) values " + "(%d,'%s','%s','c:/mandatorDir','WYSIWYG_IMAGE_URL','HELP_URL','DCF_URL','PREVIEW_URL','page.html','content.html','search.html',0)", site.getSiteId(), site.getName(), site.getName()));
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

	public void testFindAll() {
		init();
		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(2);
		site.setName("testSite");
		insertSite(site);

		try {
			Assert.assertEquals(2, siteDao.findAll().size());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}
}
