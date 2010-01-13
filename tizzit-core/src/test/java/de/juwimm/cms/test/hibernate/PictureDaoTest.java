package de.juwimm.cms.test.hibernate;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.PictureHbm;
import de.juwimm.cms.model.PictureHbmDao;
import de.juwimm.cms.model.PictureHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmImpl;

public class PictureDaoTest extends HbmTestImpl {

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	@Autowired
	PictureHbmDao pictureDao;

	/**
	 * Test Create
	 * expect: create a picture, set the id, set time stamp
	 * 		   throw no exception
	 */
	public void testCreate() {
		PictureHbm picture = new PictureHbmImpl();

		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);

		byte[] thumbnail = new byte[] {0};
		picture.setThumbnail(thumbnail);
		picture.setAltText("altText");
		picture.setHeight(10);
		picture.setWidth(10);
		picture.setPictureName("testName");
		picture.setUnit(unit);
		picture.setMimeType("testMimeType");
		picture.setPreview(new byte[] {0});
		picture.setPicture(new byte[] {0});

		try {
			picture = pictureDao.create(picture);
			Assert.assertNotNull(picture.getPictureId());
			Assert.assertNotNull(picture.getTimeStamp());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
