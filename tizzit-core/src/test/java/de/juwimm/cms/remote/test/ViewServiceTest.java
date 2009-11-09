package de.juwimm.cms.remote.test;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.easymock.EasyMock;

import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewComponentHbmDao;
import de.juwimm.cms.remote.ViewServiceSpringImpl;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class ViewServiceTest extends TestCase {

	private ViewComponentHbmDao viewComponentDaoMock;
	private ViewServiceSpringImpl viewService;

	@Override
	protected void setUp() throws Exception {
		viewComponentDaoMock = EasyMock.createMock(ViewComponentHbmDao.class);
		viewService = new ViewServiceSpringImpl();
		viewService.setViewComponentHbmDao(viewComponentDaoMock);
	}

	/**
	 * Test for TIZZIT-273
	 */
	public void testCheckForUniqueUrlLinkName1() {
		try {
			Assert.assertEquals("root", viewService.checkForUniqueUrlLinkName(1, null, "root"));
		} catch (UserException e) {
			Assert.assertTrue(false);
		}
	}

	public void testCheckForUniqueUrlLinkName2() {
		ViewComponentHbm root = ViewComponentHbm.Factory.newInstance();
		ViewComponentHbm firstChild = createViewComponent(2, "child_1");
		root.setFirstChild(firstChild);
		root.addChild(firstChild);
		root.addChild(createViewComponent(3, "child_2"));
		root.addChild(createViewComponent(4, "child_5"));
		root.addChild(createViewComponent(5, "child_7"));
		EasyMock.expect(viewComponentDaoMock.load(EasyMock.anyInt())).andReturn(root);
		EasyMock.replay(viewComponentDaoMock);
		try {
			Assert.assertEquals("child_2_1", viewService.checkForUniqueUrlLinkName(4, 1, "child_2"));
		} catch (UserException e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(viewComponentDaoMock);
	}

	private static ViewComponentHbm createViewComponent(Integer id, String urlLinkName) {
		ViewComponentHbm viewComponent = ViewComponentHbm.Factory.newInstance();
		viewComponent.setViewComponentId(id);
		viewComponent.setUrlLinkName(urlLinkName);
		return viewComponent;
	}
}
