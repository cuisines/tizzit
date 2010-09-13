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
import de.juwimm.cms.vo.ContentValue;

public class ContentDaoTest extends HbmTestImpl {

	@Autowired
	ContentHbmDao contentHbmDao;

	ContentVersionHbmDao contentVersionDaoMock;

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		super.mockAuthetication();
		contentVersionDaoMock = EasyMock.createMock(ContentVersionHbmDao.class);
		((ContentHbmDaoImpl) contentHbmDao).setContentVersionHbmDao(contentVersionDaoMock);

	}

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub
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
	 * Test Load
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

	/**
	 * Test SetLatestContentVersionAsPublishVersion
	 * expect: the version for the contentVersion is not 'PUBLS'
	 * 		   don't update document use count
	 * 		   no exception thrown	
	 */
	public void testSetLatestContentVersionAsPublishVersion() {
		init();
		/**Use this method to mock AuthenticationHelper.getUserName()*/

		ContentVersionHbm contentVersion = new ContentVersionHbmImpl();
		contentVersion.setContentVersionId(1);
		contentVersion.setCreateDate(0);
		contentVersion.setCreator("testUser");
		contentVersion.setHeading("testHeading");
		contentVersion.setText("testText");
		contentVersion.setVersion("1");
		insertContentVersion(contentVersion, 1);

		try {
			EasyMock.expect(contentVersionDaoMock.create((ContentVersionHbm) EasyMock.anyObject())).andReturn(contentVersion);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(contentVersionDaoMock);

		try {
			contentHbmDao.setLatestContentVersionAsPublishVersion(1);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(contentVersionDaoMock);
	}

	/**
	 * Test SetLatestContentVersionAsPublishVersion
	 * expect: the version for the contentVersion is 'PUBLS'
	 * 		   update document use count
	 * 		   no exception thrown	
	 */
	public void testSetLatestContentVersionAsPublishVersion1() {
		init();
		/**Use this method to mock AuthenticationHelper.getUserName()*/

		ContentVersionHbm contentVersion = new ContentVersionHbmImpl();
		contentVersion.setContentVersionId(1);
		contentVersion.setCreateDate(0);
		contentVersion.setCreator("testUser");
		contentVersion.setHeading("testHeading");
		contentVersion.setText("testText");
		contentVersion.setVersion("PUBLS");
		insertContentVersion(contentVersion, 1);

		ContentVersionHbm latestVersion = new ContentVersionHbmImpl();
		latestVersion.setContentVersionId(2);
		latestVersion.setCreateDate(0);
		latestVersion.setCreator("testUser");
		latestVersion.setHeading("testHeading");
		latestVersion.setText("testText");
		latestVersion.setVersion("1");
		insertContentVersion(latestVersion, 1);

		try {
			contentHbmDao.setLatestContentVersionAsPublishVersion(1);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

	/**
	 * Test toXml
	 * expect: the xml in the correct format
	 */
	public void testToXml() {
		ContentHbm content = new ContentHbmImpl();
		content.setContentId(1);
		content.setStatus(1);
		content.setTemplate("testTemplate");
		content.setUpdateSearchIndex(true);

		try {
			String result = contentHbmDao.toXml(content);
			String expectedResult = "<content id=\"1\">\n\t<template>testTemplate</template>\n\t<status>1</status>\n</content>\n";
			Assert.assertNotNull(result);
			Assert.assertEquals(expectedResult, result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test CreateWithContentVersion
	 * expect: on creating a content create automatically a contentVersion too
	 */
	public void testCreateWithContentVersion() {
		ContentValue contentValue = new ContentValue();
		contentValue.setContentId(1);
		contentValue.setContentText("testText");
		contentValue.setTemplate("testTemplate");

		ContentVersionHbm contentVersion = new ContentVersionHbmImpl();
		contentVersion.setContentVersionId(1);

		try {
			EasyMock.expect(contentVersionDaoMock.create((ContentVersionHbm) EasyMock.anyObject())).andReturn(contentVersion);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		try {
			ContentHbm content = contentHbmDao.createWithContentVersion(contentValue, "testUser");
			Assert.assertEquals(1, content.getContentVersions().size());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

}
