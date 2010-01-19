package de.juwimm.cms.test.hibernate;

import java.util.Collection;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.DocumentHbm;
import de.juwimm.cms.model.DocumentHbmDao;
import de.juwimm.cms.model.DocumentHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmImpl;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class DocumentDaoTest extends HbmTestImpl {

	@Autowired
	DocumentHbmDao documentDao;

	public void initializeServiceBeans() {
		// I don't use it

	}

	public void insertDocument(DocumentHbm document) {
		getJdbcTemplate().update(String.format("insert into document" + "(document_id,mime_type,time_stamp,document_name,use_count_last_version,use_count_publish_version,update_Search_Index,unit_id_fk) " + "values (%d,'%s',0,'%s',1,1,0,%d)", document.getDocumentId(), document.getMimeType(), document.getDocumentName(), document.getUnit().getUnitId()));
	}

	public void insertUnit(UnitHbm unit) {
		getJdbcTemplate().update(String.format("insert into unit (unit_id,name,last_Modified_date,site_id_fk) " + "values (%d,'%s',0,%d)", unit.getUnitId(), unit.getName(), unit.getSite().getSiteId()));
	}

	public void insertSite(SiteHbm site) {
		getJdbcTemplate().update(String.format("insert into site " + "(site_id,site_name,site_short,mandator_dir,WYSIWYG_IMAGE_URL,HELP_URL,DCF_URL,PREVIEW_URL,PAGE_NAME_FULL,PAGE_NAME_CONTENT,PAGE_NAME_SEARCH,LAST_MODIFIED_DATE) values " + "(%d,'%s','%s','c:/mandatorDir','WYSIWYG_IMAGE_URL','HELP_URL','DCF_URL','PREVIEW_URL','page.html','content.html','search.html',0)", site.getSiteId(), site.getName(), site.getName()));

	}

	public void init() {
		DocumentHbm documentInserted = new DocumentHbmImpl();
		UnitHbm unit = new UnitHbmImpl();
		SiteHbm site = new SiteHbmImpl();

		site.setSiteId(1);
		site.setName("testSite");
		insertSite(site);

		unit.setUnitId(1);
		unit.setName("testUnit");
		unit.setSite(site);
		insertUnit(unit);

		documentInserted.setDocumentName("testDocument");
		documentInserted.setMimeType("txt");
		documentInserted.setDocumentId(1);
		documentInserted.setUnit(unit);
		insertDocument(documentInserted);
	}

	/**
	 * Test Load
	 */
	public void testLoad() {
		init();
		DocumentHbm document = documentDao.load(1);
		Assert.assertNotNull(document);
		Assert.assertEquals("testDocument", document.getDocumentName());
	}

	/**
	 * Test FindAll
	 */
	public void testFindAll() {
		init();
		Collection documents = documentDao.findAll(1);
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
	}

	/**
	 * Test ToXml
	 * expect: from a document generate the xml 
	 */
	public void testToXml() {
		init();
		documentDao.setDocumentContent(1, new byte[] {0});
		try {
			String result = documentDao.toXml(1, 0);
			String expectedResult = "<document id=\"1\" mimeType=\"txt\" unitId=\"1\">\n\t<file>AA==</file>\n\t<name><![CDATA[testDocument]]></name>\n</document>\n";
			Assert.assertNotNull(result);
			Assert.assertEquals(expectedResult, result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

	/**
	 * Test SetDocumentContent
	 * expect: after setting the document content. The value retrieved 
	 *         from the document content is the one set
	 *         no exception thrown  
	 */
	public void testSetDocumentContent() {
		init();
		try {
			documentDao.setDocumentContent(1, new byte[] {1});
			byte[] expectedResult = documentDao.getDocumentContent(1);
			Assert.assertNotNull(expectedResult);
			Assert.assertEquals(1, expectedResult[0]);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test CloneDocument
	 * expect: create a new DocumentHbm with the same values for the properties
	 *  
	 */
	public void testCloneDocument() {
		init();
		documentDao.setDocumentContent(1, new byte[] {1});
		UnitHbm newUnit = new UnitHbmImpl();
		newUnit.setUnitId(1);
		try {
			Integer result = documentDao.cloneDocument(1, newUnit);
			Assert.assertNotNull(result);
			DocumentHbm oldDocument = documentDao.load(1);
			DocumentHbm document = documentDao.load(result);
			Assert.assertEquals(document.getDocumentName(), oldDocument.getDocumentName());
			Assert.assertEquals(document.getMimeType(), oldDocument.getMimeType());
			Assert.assertEquals(document.getUnit().getUnitId(), newUnit.getUnitId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
