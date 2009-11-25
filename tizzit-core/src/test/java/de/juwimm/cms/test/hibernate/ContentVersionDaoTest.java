package de.juwimm.cms.test.hibernate;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.ContentVersionHbm;
import de.juwimm.cms.model.ContentVersionHbmDao;
import de.juwimm.cms.model.ContentVersionHbmImpl;

public class ContentVersionDaoTest extends HbmTestImpl {

	@Autowired
	ContentVersionHbmDao contentVersionDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

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
	 * expect: loading correctly of propertiess
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

}
