package de.juwimm.cms.remote.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.easymock.EasyMock;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentHbmDao;
import de.juwimm.cms.model.ContentHbmImpl;
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
public class ViewServiceTest extends TestCase {

	private ViewComponentHbmDao viewComponentDaoMock;
	private ViewDocumentHbmDao viewDocumentDaoMock;
	private UnitHbmDao unitDaoMock;
	private ContentHbmDao contentDaoMock;
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
		viewComponentDaoMock = EasyMock.createMock(ViewComponentHbmDao.class);
		viewService = new ViewServiceSpringImpl();
		viewService.setViewComponentHbmDao(viewComponentDaoMock);
		viewDocumentDaoMock = EasyMock.createMock(ViewDocumentHbmDao.class);
		viewService.setViewDocumentHbmDao(viewDocumentDaoMock);
		unitDaoMock = EasyMock.createMock(UnitHbmDao.class);
		viewService.setUnitHbmDao(unitDaoMock);
		contentDaoMock = EasyMock.createMock(ContentHbmDao.class);
		viewService.setContentHbmDao(contentDaoMock);
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

		try {
			Assert.assertEquals(node.getNextNode(), viewComponent);
			Assert.assertEquals(viewComponent.getPrevNode(), node);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

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
}
