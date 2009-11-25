package de.juwimm.cms.test.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentHbmDao;
import de.juwimm.cms.model.ContentHbmDaoImpl;
import de.juwimm.cms.model.ContentHbmImpl;
import de.juwimm.cms.model.ContentVersionHbm;
import de.juwimm.cms.model.ContentVersionHbmDao;
import de.juwimm.cms.model.ContentVersionHbmImpl;

public class ContentDaoTest extends HbmTestImpl {

	@Autowired
	ContentHbmDao contentHbmDao;

	ContentVersionHbmDao contentVersionDaoMock;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub
	}

	public void insertContent(ContentHbm content) {
		getJdbcTemplate().update(String.format("insert into content (content_id,status,template,UPDATE_SEARCH_INDEX) " + "values (%d,%d,'%s',%b)", content.getContentId(), content.getStatus(), content.getTemplate(), content.isUpdateSearchIndex()));
	}

	public void init() {
		ContentHbm content = new ContentHbmImpl();
		content.setContentId(1);
		content.setStatus(1);
		content.setTemplate("testTemplate");
		content.setUpdateSearchIndex(true);
		insertContent(content);
	}

	/**
	 * Test loading
	 */
	public void testLoad() {
		init();
		ContentHbm content = contentHbmDao.load(1);
		try {
			Assert.assertNotNull(content);
			Assert.assertEquals("testTemplate", content.getTemplate());
			Assert.assertEquals(1, content.getStatus());
			Assert.assertEquals(true, content.isUpdateSearchIndex());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test cloneContent
	 * expect: creation of a new Content who has different only the id  
	 */
	public void testCloneContent() {
		init();
		ContentHbm content = contentHbmDao.load(1);

		ContentVersionHbm contentVersion = new ContentVersionHbmImpl();
		contentVersion.setContentVersionId(1);
		contentVersion.setVersion("1");

		ContentVersionHbm clonedContentVersion = new ContentVersionHbmImpl();
		clonedContentVersion.setContentVersionId(2);
		clonedContentVersion.setVersion("1");

		Collection<ContentVersionHbm> collection = new ArrayList<ContentVersionHbm>();
		collection.add(contentVersion);
		content.setContentVersions(collection);

		contentVersionDaoMock = EasyMock.createMock(ContentVersionHbmDao.class);
		((ContentHbmDaoImpl) contentHbmDao).setContentVersionHbmDao(contentVersionDaoMock);

		Map picturesIds = null;
		Map documentsIds = null;
		Map personsIds = null;
		Integer unitId = 1;
		try {
			EasyMock.expect(contentVersionDaoMock.cloneContentVersion(content.getLastContentVersion(), picturesIds, documentsIds, personsIds, unitId)).andReturn(clonedContentVersion);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(contentVersionDaoMock);

		try {
			ContentHbm clonedContent = contentHbmDao.cloneContent(content, picturesIds, documentsIds, personsIds, 1);
			Assert.assertEquals("testTemplate", clonedContent.getTemplate());
			Assert.assertEquals(1, clonedContent.getStatus());
			Assert.assertEquals(true, clonedContent.isUpdateSearchIndex());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

}
