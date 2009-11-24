package de.juwimm.cms.test.hibernate;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewComponentHbmImpl;
import de.juwimm.cms.model.ViewDocumentHbm;
import de.juwimm.cms.model.ViewDocumentHbmDao;
import de.juwimm.cms.model.ViewDocumentHbmImpl;

public class ViewDocumentDaoTest extends HbmTestImpl {

	@Autowired
	ViewDocumentHbmDao viewDocumentDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	public void insertViewDocument(ViewDocumentHbm viewDocument) {
		getJdbcTemplate().update(String.format("insert into viewdocument (view_document_id,language,view_type,site_id_fk) " + "values (%d,'%s','%s',%d)", viewDocument.getViewDocumentId(), viewDocument.getLanguage(), viewDocument.getViewType(), viewDocument.getSite().getSiteId()));
	}

	public void insertSite(SiteHbm site) {
		getJdbcTemplate().update(String.format("insert into site " + "(site_id,site_name,site_short,mandator_dir,WYSIWYG_IMAGE_URL,HELP_URL,DCF_URL,PREVIEW_URL,PAGE_NAME_FULL,PAGE_NAME_CONTENT,PAGE_NAME_SEARCH,LAST_MODIFIED_DATE) values " + "(%d,'%s','%s','c:/mandatorDir','WYSIWYG_IMAGE_URL','HELP_URL','DCF_URL','PREVIEW_URL','page.html','content.html','search.html',0)", site.getSiteId(), site.getName(), site.getName()));

	}

	public void init() {
		ViewDocumentHbm viewDocument = new ViewDocumentHbmImpl();
		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		SiteHbm site = new SiteHbmImpl();

		site.setSiteId(1);
		site.setName("testSite");
		insertSite(site);

		viewComponent.setViewComponentId(1);

		viewDocument.setViewDocumentId(1);
		viewDocument.setLanguage("testLanguage");
		viewDocument.setSite(site);
		viewDocument.setViewType("testViewType");
		insertViewDocument(viewDocument);
	}

	public void testLoad() {
		init();
		ViewDocumentHbm viewDocument = viewDocumentDao.load(1);
		Assert.assertNotNull(viewDocument);
		Assert.assertEquals("testaLanguage", viewDocument.getLanguage());
		Assert.assertEquals("testViewType", viewDocument.getViewType());

	}

}
