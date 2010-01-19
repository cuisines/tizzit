package de.juwimm.cms.remote.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.easymock.EasyMock;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.authorization.model.UserHbmImpl;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentHbmDao;
import de.juwimm.cms.model.ContentHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmDao;
import de.juwimm.cms.model.UnitHbmImpl;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewComponentHbmDao;
import de.juwimm.cms.model.ViewComponentHbmImpl;
import de.juwimm.cms.model.ViewDocumentHbm;
import de.juwimm.cms.model.ViewDocumentHbmDao;
import de.juwimm.cms.model.ViewDocumentHbmImpl;
import de.juwimm.cms.remote.ViewServiceSpringImpl;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class ViewServiceTest extends AbstractServiceTest {

	private ViewComponentHbmDao viewComponentDaoMock;
	private ViewDocumentHbmDao viewDocumentDaoMock;
	private UnitHbmDao unitDaoMock;
	private ContentHbmDao contentDaoMock;
	private UserHbmDao userDaoMock;
	private ViewServiceSpringImpl viewService;
	private ViewComponentHbm root;
	private ViewComponentHbm first;
	private ViewComponentHbm second;
	private ViewComponentHbm third;
	private ViewComponentHbm fourth;
	private ViewComponentHbm three_first;
	private ViewComponentHbm three_second;
	private ViewComponentHbm three_one_first;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		viewComponentDaoMock = EasyMock.createMock(ViewComponentHbmDao.class);
		viewService = new ViewServiceSpringImpl();
		viewService.setViewComponentHbmDao(viewComponentDaoMock);

		viewDocumentDaoMock = EasyMock.createMock(ViewDocumentHbmDao.class);
		viewService.setViewDocumentHbmDao(viewDocumentDaoMock);

		unitDaoMock = EasyMock.createMock(UnitHbmDao.class);
		viewService.setUnitHbmDao(unitDaoMock);

		contentDaoMock = EasyMock.createMock(ContentHbmDao.class);
		viewService.setContentHbmDao(contentDaoMock);

		userDaoMock = EasyMock.createMock(UserHbmDao.class);
		viewService.setUserHbmDao(userDaoMock);
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

	private ViewComponentHbm createViewComponent(Integer id, String urlLinkName) {
		ViewComponentHbm viewComponent = ViewComponentHbm.Factory.newInstance();
		viewComponent.setViewComponentId(id);
		viewComponent.setUrlLinkName(urlLinkName);
		return viewComponent;
	}

	private ViewComponentHbm createViewComponent(Integer id, String urlLinkName, Collection children, ViewComponentHbm prevNode, ViewComponentHbm nextNode, ViewComponentHbm firstChild, ViewComponentHbm parent) {
		ViewComponentHbm viewComponent = ViewComponentHbm.Factory.newInstance();
		viewComponent.setViewComponentId(id);
		viewComponent.setUrlLinkName(urlLinkName);
		viewComponent.setParent(parent);
		viewComponent.setChildren(children);
		viewComponent.setNextNode(nextNode);
		viewComponent.setPrevNode(prevNode);
		viewComponent.setFirstChild(firstChild);
		return viewComponent;
	}

	/**
	 * Create a tree with four children to be used in testing movement
	 * 
	 */
	private void createTestTree() {
		root = createViewComponent(1, "root_url", null, null, null, null, null);
		first = createViewComponent(2, "first_url", null, null, null, null, root);
		second = createViewComponent(3, "second_url", null, first, null, null, root);
		third = createViewComponent(4, "third_url", null, second, null, null, root);
		fourth = createViewComponent(5, "fourth_url", null, third, null, null, root);
		three_first = createViewComponent(6, "three_first_url", null, null, null, null, third);
		three_second = createViewComponent(7, "three_second_url", null, three_first, null, null, third);
		three_one_first = createViewComponent(8, "three_first_first_url", null, null, null, null, three_first);

		List firstLevel = new ArrayList();
		firstLevel.add(first);
		firstLevel.add(second);
		firstLevel.add(third);
		firstLevel.add(fourth);
		List secondLevel = new ArrayList();
		secondLevel.add(three_first);
		secondLevel.add(three_second);
		List thirdLevel = new ArrayList();
		thirdLevel.add(three_one_first);

		root.setChildren(firstLevel);
		root.setFirstChild(first);
		first.setNextNode(second);
		second.setNextNode(third);
		second.setPrevNode(first);
		third.setNextNode(fourth);
		third.setPrevNode(second);
		three_first.setNextNode(three_second);
		third.setChildren(secondLevel);
		third.setFirstChild(three_first);
		three_first.setChildren(thirdLevel);
		three_first.setFirstChild(three_one_first);
	}

	/**
	 * move the second to the third place
	 * expect: 	parent stays the same for all
	 * 			children stay the same for all
	 * 			next for all except last change
	 * 			previous for all except first change 
	 */
	public void testMoveComponentDown() {
		createTestTree();
		EasyMock.expect(viewComponentDaoMock.load(3)).andReturn(second);
		EasyMock.replay(viewComponentDaoMock);
		try {
			ViewComponentValue afterMove = viewService.moveViewComponentDown(3);
			Assert.assertEquals(afterMove.getParentId(), root.getViewComponentId());
			Assert.assertEquals(afterMove.getNextId(), fourth.getViewComponentId());
			Assert.assertEquals(afterMove.getPrevId(), third.getViewComponentId());
			Assert.assertEquals(first.getNextNode(), third);
			Assert.assertEquals(fourth.getPrevNode(), second);
			Assert.assertEquals(third.getNextNode(), second);
			Assert.assertEquals(third.getPrevNode(), first);
		} catch (UserException e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * move the second to the first place
	 * expect: 	parent stays the same for all
	 * 			children stay the same for all
	 * 			next for first changes
	 * 			previous for third changes
	 * 			next and previous for second changes 
	 * 			parent first child changes
	 */
	public void testMoveComponentUp() {
		createTestTree();
		EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(3))).andReturn(second);
		EasyMock.replay(viewComponentDaoMock);
		try {
			ViewComponentValue afterMove = viewService.moveViewComponentUp(3);
			Assert.assertEquals(first.getNextNode(), third);
			Assert.assertEquals(third.getPrevNode(), first);
			Assert.assertEquals(afterMove.getPrevId(), null);
			Assert.assertEquals(afterMove.getNextId(), first.getViewComponentId());
		} catch (UserException e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(viewComponentDaoMock);

	}

	/**
	 * move the first to the right
	 * expect: 	parent stays the same for all except second
	 * 			children stay the same for all except first
	 * 			next and previous for second changes
	 * 			previous for third changes
	 * 			next for first changes
	 */
	public void testMoveComponentRight() {
		createTestTree();
		EasyMock.expect(viewComponentDaoMock.load(3)).andReturn(second);
		EasyMock.replay(viewComponentDaoMock);
		try {
			ViewComponentValue afterMove = viewService.moveViewComponentRight(3);
			Assert.assertEquals(afterMove.getNextId(), null);
			Assert.assertEquals(afterMove.getPrevId(), null);
			Assert.assertEquals(first.getNextNode(), third);
			Assert.assertEquals(third.getPrevNode(), first);
			Assert.assertEquals(afterMove.getParentId(), first.getViewComponentId());
		} catch (UserException e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(viewComponentDaoMock);

	}

	/**
	 * move the first child of the third node to left(node three_first)
	 * expect: children modify for the third mode
	 * 		   previous for fourth changes
	 * 		   next for third changes
	 * 		   children for root change
	 * 		   previous node for three_second is null	
	 * 		   for the moved node the previous and next nodes change		
	 */
	public void testMoveComponentLeft() {
		createTestTree();
		EasyMock.expect(viewComponentDaoMock.load(6)).andReturn(three_first);
		EasyMock.replay(viewComponentDaoMock);
		try {
			ViewComponentValue afterMove = viewService.moveViewComponentLeft(6);
			Assert.assertEquals(fourth.getPrevNode().getViewComponentId(), afterMove.getViewComponentId());
			Assert.assertEquals(third.getNextNode().getViewComponentId(), afterMove.getViewComponentId());
			Assert.assertEquals(afterMove.getPrevId(), third.getViewComponentId());
			Assert.assertEquals(afterMove.getNextId(), fourth.getViewComponentId());
			Assert.assertEquals(three_second.getPrevNode(), null);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * Remove the second view component 
	 * expect: for the first node the nextNode changes
	 * 		   for the third node the prevNode changes
	 */
	public void testRemoveViewComponent() {
		createTestTree();
		Integer id = 3;
		EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(id))).andReturn(second);
		viewComponentDaoMock.remove(EasyMock.eq(id));
		EasyMock.replay(viewComponentDaoMock);
		try {
			viewService.removeViewComponent(second.getViewComponentId(), true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * Insert view component
	 * expect: modfication of prev and next nodes depending on the position where 
	 *         the new node is inserted
	 */
	public void testInsertViewComponent() {
		Integer id = 9;
		Integer viewDocumentId = 1;
		ViewComponentHbm viewComponent = ViewComponentHbm.Factory.newInstance();
		viewComponent.setViewComponentId(9);
		viewComponent.setUrlLinkName("testForInsertUrlLinkName");
		viewComponent.setLinkDescription("testLinkDescription");
		ViewComponentHbm node = new ViewComponentHbmImpl();
		ViewComponentHbm parent = new ViewComponentHbmImpl();
		node.setParent(parent);
		parent.setFirstChild(node);

		EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(id))).andReturn(node).times(2);
		EasyMock.expect(viewDocumentDaoMock.load(EasyMock.eq(viewDocumentId))).andReturn(new ViewDocumentHbmImpl()).times(2);
		EasyMock.expect(viewComponentDaoMock.create((ViewDocumentHbm) EasyMock.anyObject(), (String) EasyMock.anyObject(), (String) EasyMock.anyObject(), (String) EasyMock.anyObject(), EasyMock.anyInt())).andReturn(viewComponent).times(2);
		EasyMock.expect(viewComponentDaoMock.create((ViewComponentHbm) EasyMock.anyObject())).andReturn(viewComponent).times(2);
		EasyMock.replay(viewComponentDaoMock);
		EasyMock.replay(viewDocumentDaoMock);

		/**test add */
		try {
			viewService.insertViewComponent(id, viewDocumentId, "2", "testText", "testInfo", Constants.ADD_AFTER);
		} catch (UserException e) {
			Assert.assertTrue(false);
		}

		Assert.assertEquals(node.getNextNode(), viewComponent);
		Assert.assertEquals(viewComponent.getPrevNode(), node);

		/**test add before*/
		try {
			viewService.insertViewComponent(id, viewDocumentId, "2", "testText", "testInfo", Constants.ADD_BEFORE);
			Assert.assertEquals(node.getPrevNode(), viewComponent);
			Assert.assertEquals(parent.getFirstChild(), viewComponent);
			Assert.assertEquals(viewComponent.getNextNode(), node);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDaoMock);
		EasyMock.verify(viewDocumentDaoMock);

	}

	/**
	 * Test getting children  for viewComponent
	 * expect: children returned properly
	 */
	public void testGetViewComponentChildren() {
		Integer parentId = 1;
		Integer firstChildId = 11;
		Integer secondChildId = 12;
		ViewComponentHbm parent = new ViewComponentHbmImpl();
		parent.setViewComponentId(parentId);
		ViewComponentHbm firstChild = new ViewComponentHbmImpl();
		firstChild.setViewComponentId(firstChildId);
		ViewComponentHbm secondChild = new ViewComponentHbmImpl();
		secondChild.setViewComponentId(secondChildId);
		parent.addChild(firstChild);
		parent.setFirstChild(firstChild);
		parent.addChild(secondChild);
		ViewComponentValue[] children;
		EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(parentId))).andReturn(parent);
		EasyMock.replay(viewComponentDaoMock);

		try {
			children = viewService.getViewComponentChildren(parentId);
			Assert.assertEquals(2, children.length);
			Assert.assertEquals(firstChild.getViewComponentId(), children[0].getViewComponentId());
			Assert.assertEquals(secondChild.getViewComponentId(), children[1].getViewComponentId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * Test getViewComponentChildren
	 * expect: view component is leaf so throw exception with message "node is a leaf"
	 */
	public void testGetViewComponentChildren1() {
		ViewComponentHbm view = new ViewComponentHbmImpl();
		view.setViewComponentId(1);
		view.setFirstChild(null);

		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(view);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);

		try {
			viewService.getViewComponentChildren(1);
		} catch (Exception e) {
			if (e instanceof UserException) {
				if (e.getMessage().equalsIgnoreCase("node is a leaf.")) {
					Assert.assertTrue(true);
				} else {
					Assert.assertTrue(false);
				}
			} else {
				Assert.assertTrue(false);
			}
		}

		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * Test copyViewComponent. Having 2 nodes copy the child of the first
	 * to the second node
	 * expect: the list of children of the second node changes
	 * 		   and the first child is the second node	
	 * 
	 */
	public void testCopyViewComponentToParent() {
		createTestTree();
		Integer firstNodeId = 1;
		Integer secondNodeId = 2;
		Integer copyViewComponentId = 3;
		UnitHbm unit = new UnitHbmImpl();
		Integer unitId = 1;
		unit.setUnitId(unitId);

		ViewComponentHbm firstNode = new ViewComponentHbmImpl();
		firstNode.setViewComponentId(firstNodeId);
		firstNode.setAssignedUnit(unit);

		ViewComponentHbm secondNode = new ViewComponentHbmImpl();
		secondNode.setViewComponentId(secondNodeId);
		secondNode.setAssignedUnit(unit);

		ViewComponentHbm copyViewComponent = new ViewComponentHbmImpl();
		copyViewComponent.setViewComponentId(copyViewComponentId);
		copyViewComponent.setReference("1");
		copyViewComponent.setParent(firstNode);
		Map picIds = null;
		Map docIds = null;
		Map personIds = null;

		ViewComponentHbm copiedViewComponent = new ViewComponentHbmImpl();
		copiedViewComponent.setViewComponentId(2);
		copiedViewComponent.setReference("1");

		ContentHbm content = new ContentHbmImpl();
		Integer contentId = 1;
		content.setContentId(contentId);

		Integer[] viewComponentsIds = new Integer[] {copyViewComponentId};
		ViewComponentValue[] values;
		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(firstNodeId))).andReturn(firstNode).times(1);
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(copyViewComponentId))).andReturn(copyViewComponent).times(2);
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(secondNodeId))).andReturn(secondNode).times(2);
			EasyMock.expect(unitDaoMock.load(EasyMock.eq(unitId))).andReturn(unit);
			EasyMock.expect(contentDaoMock.load(EasyMock.eq(contentId))).andReturn(content);
			EasyMock.expect(viewComponentDaoMock.cloneViewComponent(copyViewComponent, picIds, docIds, personIds, unitId)).andReturn(copiedViewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);
		EasyMock.replay(unitDaoMock);
		EasyMock.replay(contentDaoMock);

		try {
			values = viewService.copyViewComponentsToParent(secondNodeId, viewComponentsIds, Constants.ADD_AFTER);
			ViewComponentValue value = values[0];
			Assert.assertEquals(secondNode.getFirstChild().getViewComponentId(), value.getViewComponentId());

		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDaoMock);
		EasyMock.verify(unitDaoMock);
		EasyMock.verify(contentDaoMock);
	}

	/**
	 * Test saveViewComponent
	 * expect: modifications made to be saved
	 */
	public void testSaveViewComponent() {
		ViewComponentValue viewComponentValue = new ViewComponentValue();
		viewComponentValue.setViewComponentId(1);
		viewComponentValue.setDisplayLinkName("testDisplayLinkName");
		viewComponentValue.setLinkDescription("testLinkDescription");
		viewComponentValue.setUrlLinkName("testUrlLinkName");
		viewComponentValue.setApprovedLinkName("testApprovedLinkName");
		viewComponentValue.setMetaData("testMetaData");
		viewComponentValue.setMetaDescription("testMetaDescription");
		viewComponentValue.setReference("0");
		viewComponentValue.setCreateDate(0);
		viewComponentValue.setDisplaySettings((byte) 0);
		viewComponentValue.setLastModifiedDate(0);
		viewComponentValue.setOnline((byte) 0);
		viewComponentValue.setOnlineStart(0);
		viewComponentValue.setOnlineStop(0);
		viewComponentValue.setSearchIndexed(true);
		viewComponentValue.setShowType((byte) 0);
		viewComponentValue.setStatus(0);
		viewComponentValue.setUserLastModifiedDate(0);
		viewComponentValue.setViewIndex("0");
		viewComponentValue.setViewLevel("0");
		viewComponentValue.setViewType((byte) 0);
		viewComponentValue.setVisible(true);

		ViewComponentHbm viewComponentHbm = new ViewComponentHbmImpl();
		viewComponentHbm.setViewComponentId(1);
		viewComponentHbm.setDisplayLinkName("initialDisplayLinkName");
		viewComponentHbm.setLinkDescription("initialLinkDescription");
		viewComponentHbm.setUrlLinkName("initialUrlLinkName");
		viewComponentHbm.setApprovedLinkName("initialApprovedLinkName");
		viewComponentHbm.setMetaData("initialMetaData");
		viewComponentHbm.setMetaDescription("initialMetaDescription");
		viewComponentHbm.setReference("1");
		viewComponentHbm.setCreateDate(1);
		viewComponentHbm.setDisplaySettings((byte) 1);
		viewComponentHbm.setLastModifiedDate(1);
		viewComponentHbm.setOnline((byte) 0);
		viewComponentHbm.setOnlineStart(1);
		viewComponentHbm.setOnlineStop(1);
		viewComponentHbm.setSearchIndexed(false);
		viewComponentHbm.setShowType((byte) 1);
		viewComponentHbm.setStatus(0);
		viewComponentHbm.setUserLastModifiedDate(0);
		viewComponentHbm.setViewIndex("1");
		viewComponentHbm.setViewLevel("1");
		viewComponentHbm.setViewType((byte) 0);
		viewComponentHbm.setVisible(false);

		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(viewComponentHbm);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(viewComponentDaoMock);

		try {
			ViewComponentValue viewComponentValueTest = viewService.saveViewComponent(viewComponentValue);
			Assert.assertNotNull(viewComponentValueTest);
			Assert.assertEquals(viewComponentValueTest.getDisplayLinkName(), viewComponentValue.getDisplayLinkName());
			Assert.assertEquals(viewComponentValueTest.getLinkDescription(), viewComponentValue.getLinkDescription());
			Assert.assertEquals(viewComponentValueTest.getDeployCommand(), viewComponentValue.getDeployCommand());
			Assert.assertEquals(viewComponentValueTest.getDisplaySettings(), viewComponentValue.getDisplaySettings());
			Assert.assertNotSame(viewComponentValueTest.getLastModifiedDate(), viewComponentValue.getLastModifiedDate());
			Assert.assertEquals(viewComponentValueTest.getMetaData(), viewComponentValue.getMetaData());
			Assert.assertEquals(viewComponentValueTest.getMetaDescription(), viewComponentValue.getMetaDescription());
			Assert.assertEquals(viewComponentValueTest.getOnline(), viewComponentValue.getOnline());
			Assert.assertEquals(viewComponentValueTest.getOnlineStart(), viewComponentValue.getOnlineStart());
			Assert.assertEquals(viewComponentValueTest.getOnlineStop(), viewComponentValue.getOnlineStop());
			Assert.assertEquals(viewComponentValueTest.getOnline(), viewComponentValue.getOnline());
			Assert.assertEquals(viewComponentValueTest.getReference(), viewComponentValue.getReference());
			Assert.assertEquals(viewComponentValueTest.getShowType(), viewComponentValue.getShowType());
			Assert.assertEquals(viewComponentValueTest.getStatus(), viewComponentValue.getStatus());
			Assert.assertEquals(viewComponentValueTest.getUrlLinkName(), viewComponentValue.getUrlLinkName());
			Assert.assertEquals(viewComponentValueTest.getUserLastModifiedDate(), viewComponentValue.getUserLastModifiedDate());
			Assert.assertEquals(viewComponentValueTest.getViewIndex(), viewComponentValue.getViewIndex());
			Assert.assertEquals(viewComponentValueTest.getViewLevel(), viewComponentValue.getViewLevel());
			Assert.assertEquals(viewComponentValueTest.getViewType(), viewComponentValue.getViewType());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * test AddFirstViewComponent
	 * 
	 */
	public void testAddFirstViewComponent() {
		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		ViewComponentHbm child = new ViewComponentHbmImpl();
		child.setViewComponentId(2);

		ViewComponentHbm newNode = new ViewComponentHbmImpl();
		newNode.setViewComponentId(3);

		ViewDocumentHbm viewDocument = new ViewDocumentHbmImpl();
		viewDocument.setViewDocumentId(1);

		String strReference = "testReference";
		String strText = "testText";
		String strInfo = "testInfo";

		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(viewComponent);
			EasyMock.expect(viewDocumentDaoMock.load(EasyMock.eq(1))).andReturn(viewDocument);
			EasyMock.expect(viewComponentDaoMock.create(EasyMock.eq(viewDocument), EasyMock.eq(strReference), EasyMock.eq(strText), EasyMock.eq(strInfo), (Integer) EasyMock.eq(null))).andReturn(newNode);
			EasyMock.expect(viewComponentDaoMock.create(EasyMock.eq(newNode))).andReturn(newNode);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);
		EasyMock.replay(viewDocumentDaoMock);

		try {
			ViewComponentValue result = viewService.addFirstViewComponent(viewComponent.getViewComponentId(), viewDocument.getViewDocumentId(), strReference, strText, strInfo);
			Assert.assertNotNull(result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(viewComponentDaoMock);
		EasyMock.verify(viewDocumentDaoMock);

	}

	/**
	 * Test GetViewComponentForLanguageOrShortlink
	 */
	public void testGetViewComponentForLanguageOrShortlink() {
		String viewType = "testViewType";
		String language = "testLanguage";

		ViewDocumentHbm viewDocument = new ViewDocumentHbmImpl();
		viewDocument.setViewDocumentId(1);

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		viewDocument.setViewComponent(viewComponent);

		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		try {
			EasyMock.expect(viewDocumentDaoMock.findByViewTypeAndLanguage(viewType, language, site.getSiteId())).andReturn(viewDocument);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewDocumentDaoMock);

		try {
			String[] value = viewService.getViewComponentForLanguageOrShortlink(viewType, language, site.getSiteId());
			Assert.assertNotNull(value);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewDocumentDaoMock);

	}

	/**
	 * Test SetDefaultViewDocument
	 * expect: no exception thrown
	 */
	public void testSetDefaultViewDocument() {
		ViewDocumentHbm viewDocument = new ViewDocumentHbmImpl();
		viewDocument.setViewDocumentId(1);

		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");
		user.setActiveSite(site);

		try {
			EasyMock.expect(viewDocumentDaoMock.load(EasyMock.eq(1))).andReturn(viewDocument);
			EasyMock.expect(userDaoMock.load(EasyMock.eq("testUser"))).andReturn(user);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewDocumentDaoMock);
		EasyMock.replay(userDaoMock);

		try {
			viewService.setDefaultViewDocument(1);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewDocumentDaoMock);
		EasyMock.verify(userDaoMock);
	}

	/**
	 * Test getViewComponent4UnitViewComponent
	 * expect: view component has a unit assigned and the id of the view component is returned
	 */
	public void testGetViewComponent4UnitViewComponent() {
		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);

		ViewComponentHbm parent = new ViewComponentHbmImpl();
		parent.setViewComponentId(2);

		ViewComponentHbm view = new ViewComponentHbmImpl();
		view.setViewComponentId(1);
		view.setParent(parent);
		view.setAssignedUnit(unit);

		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(view);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);

		try {
			Integer id = viewService.getViewComponent4UnitViewComponent(1);
			Assert.assertEquals(new Integer(1), id);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test getViewComponent4UnitViewComponent
	 * expect: view component has no unit assigned, it is not a root
	 *         return the parent's id   
	 **/
	public void testGetViewComponent4UnitViewComponent1() {
		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);

		ViewComponentHbm parent = new ViewComponentHbmImpl();
		parent.setViewComponentId(2);
		parent.setAssignedUnit(unit);

		ViewComponentHbm view = new ViewComponentHbmImpl();
		view.setViewComponentId(1);
		view.setParent(parent);
		view.setAssignedUnit(null);

		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(view);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);

		try {
			Integer id = viewService.getViewComponent4UnitViewComponent(1);
			Assert.assertEquals(new Integer(2), id);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test getViewComponent4UnitViewComponent
	 * expect: view component has no unit assigned, it is root 
	 *         return null
	 **/
	public void testGetViewComponent4UnitViewComponent2() {
		ViewComponentHbm view = new ViewComponentHbmImpl();
		view.setViewComponentId(1);
		view.setAssignedUnit(null);

		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(view);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);

		try {
			Integer id = viewService.getViewComponent4UnitViewComponent(1);
			Assert.assertNull(id);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test GetAllViewComponents4Status
	 * expect: viewComp1 is root, viewComp2 is a child of viewComp1.
	 * 		   return the viewComponentValue corresponding to viewComp2 
	 */
	public void testGetAllViewComponents4Status() {
		ViewComponentHbm viewComp1 = new ViewComponentHbmImpl();
		viewComp1.setViewComponentId(1);
		viewComp1.setParent(null);
		viewComp1.setViewType((byte) 0);
		viewComp1.setDeployCommand((byte) 0);
		viewComp1.setOnline((byte) 0);
		viewComp1.setDisplayLinkName("testDisplayLinkName1");

		ViewComponentHbm viewComp2 = new ViewComponentHbmImpl();
		viewComp2.setViewComponentId(2);
		viewComp2.setParent(viewComp1);
		viewComp2.setViewType((byte) 0);
		viewComp2.setDeployCommand((byte) 0);
		viewComp2.setOnline((byte) 0);
		viewComp2.setDisplayLinkName("testDisplayLinkName2");

		Collection<ViewComponentHbm> collection = new ArrayList<ViewComponentHbm>();
		collection.add(viewComp1);
		collection.add(viewComp2);

		Integer viewDocumentId = 1;
		Integer status = 1;
		try {
			EasyMock.expect(viewComponentDaoMock.findByStatus(EasyMock.eq(1), EasyMock.eq(1))).andReturn(collection);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(viewComponentDaoMock);

		try {
			ViewComponentValue[] values = viewService.getAllViewComponents4Status(viewDocumentId, status);
			Assert.assertEquals(1, values.length);
			Assert.assertEquals(new Integer(2), values[0].getViewComponentId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * Test GetAllViewComponents4Status
	 * expect: parent is root, viewComp2 is a child of viewComp1.
	 *		   test also the private method getParentsForView	
	 * 		   return the viewComponentValues corresponding to viewComp1 and viewComp2 
	 * 		   pathToUnit for viewComp2 should be "\\comp1UrlLinkName\\comp2UrlLinkName"	
	 */
	public void testGetAllViewComponents4Status1() {
		ViewComponentHbm parent = new ViewComponentHbmImpl();
		parent.setViewComponentId(3);
		parent.setUrlLinkName("parentUrlLinkName");

		ViewComponentHbm viewComp1 = new ViewComponentHbmImpl();
		viewComp1.setViewComponentId(1);
		viewComp1.setParent(parent);
		viewComp1.setViewType((byte) 0);
		viewComp1.setDeployCommand((byte) 0);
		viewComp1.setOnline((byte) 0);
		viewComp1.setUrlLinkName("comp1UrlLinkName");
		viewComp1.setDisplayLinkName("testDisplayLinkName1");

		ViewComponentHbm viewComp2 = new ViewComponentHbmImpl();
		viewComp2.setViewComponentId(2);
		viewComp2.setParent(viewComp1);
		viewComp2.setViewType((byte) 0);
		viewComp2.setDeployCommand((byte) 0);
		viewComp2.setOnline((byte) 0);
		viewComp2.setUrlLinkName("comp2UrlLinkName");
		viewComp2.setDisplayLinkName("testDisplayLinkName2");

		Collection<ViewComponentHbm> collection = new ArrayList<ViewComponentHbm>();
		collection.add(viewComp1);
		collection.add(viewComp2);

		Integer viewDocumentId = 1;
		Integer status = 1;
		try {
			EasyMock.expect(viewComponentDaoMock.findByStatus(EasyMock.eq(1), EasyMock.eq(1))).andReturn(collection);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(viewComponentDaoMock);

		try {
			ViewComponentValue[] values = viewService.getAllViewComponents4Status(viewDocumentId, status);
			Assert.assertEquals(2, values.length);
			Assert.assertEquals(new Integer(1), values[0].getViewComponentId());
			Assert.assertEquals(new Integer(2), values[1].getViewComponentId());
			Assert.assertEquals("\\comp1UrlLinkName\\comp2UrlLinkName", values[1].getPath2Unit());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * Test GetParents4ViewComponent
	 * expect:Parent1 is the parent of parent2. Parent2 is the
	 *        parent of viewComponent. Expect a vector of 2 elements.
	 */
	public void testGetParents4ViewComponent() {
		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		ViewComponentHbm parent1 = new ViewComponentHbmImpl();
		parent1.setViewComponentId(2);

		ViewComponentHbm parent2 = new ViewComponentHbmImpl();
		parent2.setViewComponentId(3);

		parent2.setParent(parent1);
		viewComponent.setParent(parent2);

		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(viewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);

		try {
			String[] values = viewService.getParents4ViewComponent(1);
			Assert.assertEquals(values.length, 2);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * Test IsUnitAndChangesParentUnitLeft
	 * expect: current is root, expect false
	 */
	public void testIsUnitAndChangesParentUnitLeft() {
		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(viewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);

		try {
			boolean result = viewService.isUnitAndChangesParentUnitLeft(1);
			Assert.assertFalse(result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * Test IsUnitAndChangesParentUnitLeft
	 * expect: parent of current is root, expect false
	 */
	public void testIsUnitAndChangesParentUnitLeft1() {
		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);
		viewComponent.setAssignedUnit(unit);

		ViewComponentHbm parent = new ViewComponentHbmImpl();
		parent.setViewComponentId(2);

		viewComponent.setParent(parent);

		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(viewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);

		try {
			boolean result = viewService.isUnitAndChangesParentUnitLeft(1);
			Assert.assertFalse(result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * Test IsUnitAndChangesParentUnitLeft
	 * expect: parent of current is not root 
	 *         and the parent and grandparent of current are in the same unit
	 *         so expect true
	 */
	public void testIsUnitAndChangesParentUnitLeft2() {
		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);
		viewComponent.setAssignedUnit(unit);

		ViewComponentHbm parent = new ViewComponentHbmImpl();
		parent.setViewComponentId(2);

		ViewComponentHbm root = new ViewComponentHbmImpl();
		root.setViewComponentId(3);
		root.setAssignedUnit(unit);

		parent.setParent(root);
		viewComponent.setParent(parent);

		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(viewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);

		try {
			boolean result = viewService.isUnitAndChangesParentUnitLeft(1);
			Assert.assertTrue(result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * Test IsUnitAndChangesParentUnitRight
	 * expect: current is root so return false
	 */
	public void testIsUnitAndChangesParentUnitRight() {
		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(viewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);

		try {
			boolean result = viewService.isUnitAndChangesParentUnitRight(1);
			Assert.assertFalse(result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * Test IsUnitAndChangesParentUnitRight
	 * expect: previous node is null, so return false
	 */
	public void testIsUnitAndChangesParentUnitRight1() {
		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);
		viewComponent.setAssignedUnit(unit);

		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(viewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);

		try {
			boolean result = viewService.isUnitAndChangesParentUnitRight(1);
			Assert.assertFalse(result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * Test IsUnitAndChangesParentUnitLeft
	 * expect: parent of current is not root 
	 *         and the parent and the previous node of current are in the same unit
	 *         so expect true
	 */
	public void testIsUnitAndChangesParentUnitRight2() {
		UnitHbm unit = new UnitHbmImpl();
		unit.setUnitId(1);

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);
		viewComponent.setAssignedUnit(unit);

		ViewComponentHbm parent = new ViewComponentHbmImpl();
		parent.setViewComponentId(2);
		parent.setAssignedUnit(unit);

		ViewComponentHbm prev = new ViewComponentHbmImpl();
		prev.setViewComponentId(3);
		prev.setAssignedUnit(unit);

		viewComponent.setParent(parent);
		viewComponent.setPrevNode(prev);

		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(viewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);

		try {
			boolean result = viewService.isUnitAndChangesParentUnitRight(1);
			Assert.assertTrue(result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDaoMock);
	}
}
