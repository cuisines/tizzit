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

	public void insertHost(HostHbm hostHbm) {
		getJdbcTemplate().update(String.format("insert into host " + "(host_name,site_id_fk,unit_id_fk,redirect_url,liveserver) values " + "('testHost',%d,%d,'%s',%b)", hostHbm.getSite().getSiteId(), hostHbm.getUnit().getUnitId(), hostHbm.getRedirectUrl(), hostHbm.isLiveserver()));
	}

	public void insertSite(SiteHbm site) {
		getJdbcTemplate().update(String.format("insert into site " + "(site_id,site_name,site_short,mandator_dir,WYSIWYG_IMAGE_URL,HELP_URL,DCF_URL,PREVIEW_URL,PAGE_NAME_FULL,PAGE_NAME_CONTENT,PAGE_NAME_SEARCH,LAST_MODIFIED_DATE) values " + "(%d,'%s','%s','c:/mandatorDir','WYSIWYG_IMAGE_URL','HELP_URL','DCF_URL','PREVIEW_URL','page.html','content.html','search.html',0)", site.getSiteId(), site.getName(), site.getName()));
	}

	public void insertUnit(UnitHbm unit) {
		getJdbcTemplate().update(String.format("insert into unit (unit_id,name,last_Modified_date,site_id_fk) " + "values (%d,'%s',0,%d)", unit.getUnitId(), unit.getName(), unit.getSite().getSiteId()));
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
