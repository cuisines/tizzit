package de.juwimm.cms.test.hibernate;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;

import de.juwimm.cms.model.ContentVersionHbm;
import de.juwimm.cms.model.ContentVersionHbmDao;
import de.juwimm.cms.model.ContentVersionHbmImpl;

public class ContentVersionDaoTest extends HbmTestImpl {

	@Autowired
	ContentVersionHbmDao contentVersionDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		super.mockAuthetication();
	}

	public void insertContentVersion(ContentVersionHbm contentVersion) {
		getJdbcTemplate().update(String.format("insert into contentversion (content_version_id,version,heading,text,create_date,creator) " + "values (%d,'%s','%s','%s',%d,'%s')", contentVersion.getContentVersionId(), contentVersion.getVersion(), contentVersion.getHeading(), contentVersion.getText(), contentVersion.getCreateDate(), contentVersion.getCreator()));
	}

	public void init() {
		ContentVersionHbm contentVersion = new ContentVersionHbmImpl();
		contentVersion.setContentVersionId(1);
		contentVersion.setCreateDate(0);
		contentVersion.setCreator("testCreator");
		contentVersion.setHeading("testHeading");
		contentVersion.setText("testText");
		contentVersion.setVersion("1");
		insertContentVersion(contentVersion);
	}

	/**
	 * Test Load
	 * expect: loading correctly of properties
	 */
	public void testLoad() {
		init();
		ContentVersionHbm contentVersion = contentVersionDao.load(1);
		try {
			assertNotNull(contentVersion);
			Assert.assertEquals(0, contentVersion.getCreateDate());
			Assert.assertEquals("testCreator", contentVersion.getCreator());
			Assert.assertEquals("testHeading", contentVersion.getHeading());
			Assert.assertEquals("testText", contentVersion.getText());
			Assert.assertEquals("1", contentVersion.getVersion());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test Create
	 * expect: create a content version, set the creator and version
	 */
	public void testCreate() {
		super.mockAuthetication();

		ContentVersionHbm contentVersion = new ContentVersionHbmImpl();
		contentVersion.setContentVersionId(1);

		try {
			contentVersion = contentVersionDao.create(contentVersion);
			Assert.assertNotNull(contentVersion);
			Assert.assertEquals("testUser", contentVersion.getCreator());
			Assert.assertEquals("1", contentVersion.getVersion());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

	/**
	 * Test toXML
	 * expect: generate the xml in the correct format
	 */
	public void testToXml() {
		ContentVersionHbm contentVersion = new ContentVersionHbmImpl();
		contentVersion.setContentVersionId(1);
		contentVersion.setCreator("testUser");
		contentVersion.setHeading("testHeading");
		contentVersion.setText("testText");
		contentVersion.setCreateDate(0);
		contentVersion.setVersion("1");

		try {
			String result = contentVersionDao.toXml(contentVersion);
			String shouldBe = "<contentVersion id=\"1\">\n<heading><![CDATA[testHeading]]></heading>\n<creator><userName>testUser</userName></creator>\n<createDate>01.01.1970</createDate>\n<text>dGVzdFRleHQ=</text>\n<version>1</version>\n</contentVersion>\n";
			Assert.assertEquals(shouldBe, result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test CreateFromXml
	 * expect: from a given xml create a content version with those given properties
	 */
	public void testCreateFromXml() {
		String xml = "<contentVersion id=\"1\">\n<heading><![CDATA[testHeading]]></heading>\n<creator><userName>testUser</userName></creator>\n<createDate>01.01.1970</createDate>\n<text>dGVzdFRleHQ=</text>\n<version>1</version>\n</contentVersion>\n";
		Document element = null;

		try {
			element = XercesHelper.string2Dom(xml);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		try {
			ContentVersionHbm contentVersion = contentVersionDao.createFromXml(element.getDocumentElement(), true, false);
			Assert.assertNotNull(contentVersion);
			Assert.assertEquals(new Integer(1), contentVersion.getContentVersionId());
			Assert.assertEquals("testUser", contentVersion.getCreator());
			Assert.assertEquals("testHeading", contentVersion.getHeading());
			Assert.assertEquals("testText", contentVersion.getText());
			Assert.assertEquals("1", contentVersion.getVersion());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test CloneContentVersion
	 * expect: create a new contentVersion with the same properties without cloning the media too. 
	 */
	public void testCloneContentVersion() {
		ContentVersionHbm oldContentVersion = new ContentVersionHbmImpl();
		oldContentVersion.setContentVersionId(1);
		oldContentVersion.setCreator("testUser");
		oldContentVersion.setText("testText");
		oldContentVersion.setCreateDate(0);
		oldContentVersion.setHeading("testHeading");
		oldContentVersion.setVersion("1");

		try {
			ContentVersionHbm newContentVersion = contentVersionDao.cloneContentVersion(oldContentVersion, null, null, null, null);
			Assert.assertNotNull(newContentVersion);
			Assert.assertNotSame(oldContentVersion.getContentVersionId().intValue(), newContentVersion.getContentVersionId().intValue());
			Assert.assertNotSame(oldContentVersion.getCreateDate(), newContentVersion.getCreateDate());
			Assert.assertEquals("testUser", newContentVersion.getCreator());
			Assert.assertEquals("testText", newContentVersion.getText());
			Assert.assertEquals("testHeading", newContentVersion.getHeading());
			Assert.assertEquals("1", newContentVersion.getVersion());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
