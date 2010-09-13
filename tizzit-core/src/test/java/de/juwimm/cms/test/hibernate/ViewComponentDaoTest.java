package de.juwimm.cms.test.hibernate;

import java.util.Map;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentHbmDao;
import de.juwimm.cms.model.ContentHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmImpl;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewComponentHbmDao;
import de.juwimm.cms.model.ViewComponentHbmDaoImpl;
import de.juwimm.cms.model.ViewComponentHbmImpl;
import de.juwimm.cms.model.ViewDocumentHbm;
import de.juwimm.cms.model.ViewDocumentHbmImpl;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbm;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbmImpl;

public class ViewComponentDaoTest extends HbmTestImpl {

	@Autowired
	ViewComponentHbmDao viewComponentDao;
	ContentHbmDao contentDaoMock;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	public void init() {
		
		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		UnitHbm unit = new UnitHbmImpl();
		SiteHbm site = new SiteHbmImpl();
		ContentHbm content = new ContentHbmImpl();
		ViewDocumentHbm viewDocument = new ViewDocumentHbmImpl();
		Realm2viewComponentHbm realm = new Realm2viewComponentHbmImpl();

		site.setSiteId(1);
		site.setName("testSite");
		insertSite(site);

		unit.setUnitId(1);
		unit.setName("testUnit");
		unit.setSite(site);
		insertUnit(unit);

		content.setContentId(1);
		content.setStatus(0);
		content.setTemplate("testTemplate");
		insertContent(content);

		viewDocument.setViewDocumentId(1);
		viewDocument.setLanguage("testLanguage");
		viewDocument.setSite(site);
		viewDocument.setViewType("testViewType");
		viewDocument.setViewComponent(viewComponent);
		insertViewDocument(viewDocument);

		realm.setRealm2viewComponentId(1);
		realm.setRoleNeeded("testRoleNeeded");
		realm.setViewComponent(viewComponent);

		viewComponent.setViewComponentId(1);
		viewComponent.setDisplayLinkName("testDisplayLinkName");
		viewComponent.setLinkDescription("testLinkDescription");
		viewComponent.setUrlLinkName("testUrlLinkName");
		viewComponent.setApprovedLinkName("testApprovedLinkName");
		viewComponent.setMetaData("testMetaData");
		viewComponent.setMetaDescription("testMetaDescription");
		viewComponent.setAssignedUnit(unit);
		viewComponent.setReference(content.getContentId().toString());
		viewComponent.setRealm2vc(realm);
		viewComponent.setViewDocument(viewDocument);
		insertViewComponent(viewComponent);
	}

	/**
	 * Test the correct loading of a viewComponent
	 */
	public void testLoad() {
		init();
		ViewComponentHbm component = viewComponentDao.load(1);
		Assert.assertNotNull(component);
		Assert.assertEquals("testDisplayLinkName", component.getDisplayLinkName());
		Assert.assertEquals("testLinkDescription", component.getLinkDescription());
		Assert.assertEquals("testUrlLinkName", component.getUrlLinkName());
		Assert.assertEquals("testApprovedLinkName", component.getApprovedLinkName());
		Assert.assertEquals("testMetaData", component.getMetaData());
		Assert.assertEquals("testMetaDescription", component.getMetaDescription());
		Assert.assertEquals("1", component.getReference());
		Assert.assertEquals(0, component.getStatus());
		Assert.assertEquals(3, component.getShowType());
		Assert.assertEquals(1, component.getViewType());
		Assert.assertEquals(true, component.isVisible());
		Assert.assertEquals(true, component.isSearchIndexed());
		Assert.assertEquals(true, component.isXmlSearchIndexed());
		Assert.assertEquals(0, component.getOnlineStart());
		Assert.assertEquals(0, component.getOnlineStop());
		Assert.assertEquals(0, component.getOnline());
		Assert.assertEquals(0, component.getDeployCommand());
		Assert.assertEquals(0, component.getCreateDate());
		Assert.assertEquals(0, component.getLastModifiedDate());
		Assert.assertEquals(0, component.getDisplaySettings());
		Assert.assertEquals(0, component.getUserLastModifiedDate());
	}

	/**
	 * Test CloneViewComponent
	 * expect: a new viewComponent whose properties have the same
	 *	      values as the corresponding ones from the original 	
	 */
	public void testCloneViewComponent() {
		ViewComponentHbm original = new ViewComponentHbmImpl();
		ViewDocumentHbm viewDocument = new ViewDocumentHbmImpl();

		ContentHbm content = new ContentHbmImpl();
		content.setContentId(1);

		ContentHbm clonedContent = new ContentHbmImpl();
		clonedContent.setContentId(2);

		viewDocument.setViewDocumentId(1);
		original.setViewComponentId(1);
		original.setDisplayLinkName("testDisplayLinkName");
		original.setLinkDescription("testLinkDescription");
		original.setUrlLinkName("testUrlLinkName");
		original.setApprovedLinkName("testApprovedLinkName");
		original.setMetaData("testMetaData");
		original.setMetaDescription("testMetaDescription");
		original.setReference("1");
		original.setViewDocument(viewDocument);
		original.setCreateDate(0);
		original.setDisplaySettings((byte) 0);
		original.setLastModifiedDate(0);
		original.setOnline((byte) 0);
		original.setOnlineStart(0);
		original.setOnlineStop(0);
		original.setSearchIndexed(true);
		original.setShowType((byte) 0);
		original.setStatus(1);
		original.setUserLastModifiedDate(0);
		original.setViewIndex("2");
		original.setViewLevel("2");
		original.setViewType((byte) 0);
		original.setVisible(true);

		contentDaoMock = EasyMock.createMock(ContentHbmDao.class);
		((ViewComponentHbmDaoImpl) viewComponentDao).setContentHbmDao(contentDaoMock);

		Map picturesIds = null;
		Map documentsIds = null;
		Map personsIds = null;
		Integer unitId = 1;
		try {
			EasyMock.expect(contentDaoMock.load(EasyMock.eq(1))).andReturn(content);
			EasyMock.expect(contentDaoMock.cloneContent(content, picturesIds, documentsIds, personsIds, unitId)).andReturn(clonedContent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(contentDaoMock);

		try {
			ViewComponentHbm copiedValue = viewComponentDao.cloneViewComponent(original, picturesIds, documentsIds, personsIds, 1);
			Assert.assertEquals(copiedValue.getDisplayLinkName(), "copy_" + original.getDisplayLinkName());
			Assert.assertEquals(copiedValue.getLinkDescription(), original.getLinkDescription());
			Assert.assertEquals(copiedValue.getDeployCommand(), original.getDeployCommand());
			Assert.assertEquals(copiedValue.getDisplaySettings(), original.getDisplaySettings());
			Assert.assertEquals(copiedValue.getLastModifiedDate(), original.getLastModifiedDate());
			Assert.assertEquals(copiedValue.getMetaData(), original.getMetaData());
			Assert.assertEquals(copiedValue.getMetaDescription(), original.getMetaDescription());
			Assert.assertEquals(copiedValue.getOnline(), original.getOnline());
			Assert.assertEquals(copiedValue.getOnlineStart(), original.getOnlineStart());
			Assert.assertEquals(copiedValue.getOnlineStop(), original.getOnlineStop());
			Assert.assertEquals(copiedValue.getOnline(), original.getOnline());
			Assert.assertEquals(copiedValue.getReference(), "2");
			Assert.assertEquals(copiedValue.getShowType(), original.getShowType());
			Assert.assertEquals(copiedValue.getStatus(), original.getStatus());
			Assert.assertEquals(copiedValue.getUrlLinkName(), "copy_" + original.getUrlLinkName());
			Assert.assertEquals(copiedValue.getUserLastModifiedDate(), original.getUserLastModifiedDate());
			Assert.assertEquals(copiedValue.getViewIndex(), original.getViewIndex());
			Assert.assertEquals(copiedValue.getViewLevel(), original.getViewLevel());
			Assert.assertEquals(copiedValue.getViewType(), original.getViewType());
			Assert.assertEquals(copiedValue.getViewDocument(), original.getViewDocument());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(contentDaoMock);
	}

	/**
	 *  Test Create
	 *  expect: return a viewComponentHbm object with the id and properties set
	 */
	public void testCreate() {
		String reference = "1";
		String displayLinkName = "testDisplayLinkName";
		String linkDescription = "testLinkDescription";
		Integer viewComponentId = null;

		ViewDocumentHbm viewDocument = new ViewDocumentHbmImpl();
		viewDocument.setViewDocumentId(1);

		try {
			ViewComponentHbm viewComponent = viewComponentDao.create(viewDocument, reference, displayLinkName, linkDescription, viewComponentId);
			Assert.assertNotNull(viewComponent.getCreateDate());
			Assert.assertNotNull(viewComponent.getUserLastModifiedDate());
			Assert.assertEquals("testDisplayLinkName", viewComponent.getDisplayLinkName());
			Assert.assertEquals("testLinkDescription", viewComponent.getLinkDescription());
			Assert.assertEquals(0, viewComponent.getOnline());
			Assert.assertEquals(0, viewComponent.getOnlineStart());
			Assert.assertEquals(0, viewComponent.getOnlineStop());
			Assert.assertEquals(0, viewComponent.getShowType());
			Assert.assertEquals("2", viewComponent.getUrlLinkName());
			Assert.assertEquals(0, viewComponent.getStatus());
			Assert.assertEquals("1", viewComponent.getReference());
			Assert.assertEquals(true, viewComponent.isSearchIndexed());
			Assert.assertEquals(true, viewComponent.isVisible());
			Assert.assertEquals(true, viewComponent.isXmlSearchIndexed());
			Assert.assertNotNull(viewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}
}
