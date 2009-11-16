package de.juwimm.cms.model;

import junit.framework.Assert;
import junit.framework.TestCase;
import de.juwimm.cms.vo.EditionValue;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class EditionTest extends TestCase {

	public void testToDao1() {
		EditionHbm edition = EditionHbm.Factory.newInstance();
		edition.setUnitId(1);
		edition.setDeployStatus("Exception;StatusWithError;extra1;extra2;extra3".getBytes());
		EditionValue editionValue = edition.getDao();

		Assert.assertTrue(editionValue.isException());
		Assert.assertEquals("StatusWithError", editionValue.getDeployState());
		String[] extraMessages = editionValue.getExtraMessages();
		Assert.assertEquals(3, extraMessages.length);
		Assert.assertEquals("extra1", extraMessages[0]);
		Assert.assertEquals("extra2", extraMessages[1]);
		Assert.assertEquals("extra3", extraMessages[2]);
	}

	public void testToDao2() {
		EditionHbm edition = EditionHbm.Factory.newInstance();
		edition.setUnitId(1);
		edition.setDeployStatus("StatusWithError;extra1;extra2;extra3".getBytes());
		EditionValue editionValue = edition.getDao();

		Assert.assertFalse(editionValue.isException());
		Assert.assertEquals("StatusWithError", editionValue.getDeployState());
		String[] extraMessages = editionValue.getExtraMessages();
		Assert.assertEquals(3, extraMessages.length);
		Assert.assertEquals("extra1", extraMessages[0]);
		Assert.assertEquals("extra2", extraMessages[1]);
		Assert.assertEquals("extra3", extraMessages[2]);
	}

	public void testToDao3() {
		EditionHbm edition = EditionHbm.Factory.newInstance();
		edition.setUnitId(1);
		edition.setDeployStatus(null);
		EditionValue editionValue = edition.getDao();

		Assert.assertFalse(editionValue.isException());
		Assert.assertEquals(null, editionValue.getDeployState());
		String[] extraMessages = editionValue.getExtraMessages();
		Assert.assertNull(extraMessages);
	}

	public void testSetExceptionMessage1() {
		EditionHbm edition = EditionHbm.Factory.newInstance();
		edition.setUnitId(1);
		edition.setDeployStatus("StatusWithError;extra1;extra2;extra3".getBytes());
		edition.setExceptionMessage("exceptionMessageeeee");
		EditionValue editionValue = edition.getDao();

		Assert.assertTrue(editionValue.isException());
		Assert.assertEquals("StatusWithError", editionValue.getDeployState());
		String[] extraMessages = editionValue.getExtraMessages();
		Assert.assertEquals(1, extraMessages.length);
		Assert.assertEquals("exceptionMessageeeee", extraMessages[0]);
	}

	public void testSetExceptionMessage2() {
		EditionHbm edition = EditionHbm.Factory.newInstance();
		edition.setUnitId(1);
		edition.setDeployStatus("Exception;StatusWithError;oldErrorMessage".getBytes());
		edition.setExceptionMessage("exceptionMessageeeee");
		EditionValue editionValue = edition.getDao();

		Assert.assertTrue(editionValue.isException());
		Assert.assertEquals("StatusWithError", editionValue.getDeployState());
		String[] extraMessages = editionValue.getExtraMessages();
		Assert.assertEquals(1, extraMessages.length);
		Assert.assertEquals("exceptionMessageeeee", extraMessages[0]);
	}

	public void testSetExceptionMessage3() {
		EditionHbm edition = EditionHbm.Factory.newInstance();
		edition.setUnitId(1);
		edition.setDeployStatus("".getBytes());
		edition.setExceptionMessage("exceptionMessageeeee");
		EditionValue editionValue = edition.getDao();

		Assert.assertTrue(editionValue.isException());
		Assert.assertEquals("", editionValue.getDeployState());
		String[] extraMessages = editionValue.getExtraMessages();
		Assert.assertEquals(1, extraMessages.length);
		Assert.assertEquals("exceptionMessageeeee", extraMessages[0]);
	}
}
