/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.juwimm.cms.remote.test;

import org.apache.log4j.Logger;
import org.junit.Ignore;

@Ignore
public class ClientServiceTest extends AbstractRemoteInterfaceTest {

	private static Logger log = Logger.getLogger(ClientServiceTest.class);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		log.debug("Setup finished");
	}

	//	public void testSetup() {
	//		ClientServiceSpring cs = getClientService();
	//		assertNotNull("Instance of ClientService should not be null", cs);
	//	}

	//	public void testGetAllGroups() {
	//		ClientServiceSpring cs = getClientService();
	//		GroupValue[] groupValues = cs.getAllGroups();
	//		assertNotNull(groupValues);
	//		assertTrue(groupValues.length == 2);
	//		log.debug("Number of groups: " + groupValues.length);
	//	}
	//
	//	public void testExportEditionUnit() throws IOException {
	//		ClientServiceSpring cs = getClientService();
	//		InputStream raw = cs.exportEditionUnit(1284);
	//		assertNotNull(raw);
	//		InputStream in = new BufferedInputStream(raw);
	//		String file = "C:\\conquest-test\\test-export.xml.gz";
	//		int count, offset = 0;
	//		byte[] buff = new byte[8192];
	//		FileOutputStream out = null;
	//		try {
	//			log.debug("Creating output file: " + file);
	//			out = new FileOutputStream(file);
	//		} catch (FileNotFoundException e) {
	//			log.error("Could not create file", e);
	//		}
	//		while ((count = in.read(buff)) > 0) {
	//			out.write(buff, offset, count);
	//			offset += count;
	//		}
	//		System.out.println("\n\n##########################\n\n" + count + "\n\n##########################\n\n");
	//		out.flush();
	//		out.close();
	//		in.close();
	//		File f = new File(file);
	//		assertTrue(f.exists());
	//		long length = f.length();
	//		System.out.println("\n\n##########################\n\n" + length + "\n\n##########################\n\n");
	//		assertTrue(length > 100);
	//	}
	//
	//	public void testIsUserInRole() {
	//		// TODO - Sollte mit einem nicht-MasterRoot-User getestet werden
	//		ClientServiceSpring cs = getClientService();
	//		boolean b = cs.isUserInRole(uv, "siteRoot");
	//		assertTrue("System user owns role siteRoot", b);
	//		b = cs.isUserInRole(uv, "unknownRole");
	//		//	assertFalse("Role does not exist", b);
	//	}
	//
	//	public void testGetGroups4User() {
	//		ClientServiceSpring cs = getClientService();
	//		GroupValue[] gv = cs.getGroups4User(testUserName);
	//		assertNotNull(gv);
	//		assertTrue(gv.length > 1);
	//		log.debug("Number of groups 4 user " + testUserName + ": " + gv.length);
	//	}

	//    public void removeUserFromGroup(de.juwimm.cms.authorization.vo.GroupValue gv, java.lang.String userName);
	//
	//    /**
	//     * 
	//     */
	//    public void addUserToGroup(de.juwimm.cms.authorization.vo.GroupValue groupValue, java.lang.String userName);
	//

	//	public void testGetAllRoles() {
	//		ClientServiceSpring cs = getClientService();
	//		RoleValue[] rv = cs.getAllRoles();
	//		assertNotNull(rv);
	//		assertEquals(20, rv.length);
	//	}
	//
	//	public void testGetAllGroupsUsedInUnit() {
	//		ClientServiceSpring cs = getClientService();
	//		GroupValue[] gv = cs.getAllGroupsUsedInUnit(3227);
	//		assertNotNull(gv);
	//		assertTrue(gv.length >= 1);
	//		assertEquals(4, gv.length);
	//	}

	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.authorization.vo.GroupValue createGroup(java.lang.String groupName);
	//
	//    /**
	//     * 
	//     */
	//    public void removeGroup(int groupId);
	//
	//    /**
	//     * 
	//     */
	//    public void updateGroup(de.juwimm.cms.authorization.vo.GroupValue gv);
	//

	//	public void testGetPathForViewComponentId() {
	//		ClientServiceSpring cs = getClientService();
	//		String path = cs.getPathForViewComponentId(1284);
	//		assertNotNull(path);
	//		assertFalse(path.equals(""));
	//	}
	//
	//	public void testGetContent() {
	//		ClientServiceSpring cs = getClientService();
	//		ContentValue cv = cs.getContent(1122);
	//		assertNotNull(cv);
	//		assertEquals("arzt", cv.getTemplate());
	//	}
	//
	//	public void testGetSites() {
	//		ClientServiceSpring cs = getClientService();
	//		SiteValue[] sv = cs.getSites();
	//		assertNotNull(sv);
	//		assertTrue(sv.length == 86);
	//	}
	//
	//	public void testLogin() {
	//		ClientServiceSpring cs = getClientService();
	//		UserLoginValue ulv = cs.login(testUserName, testPassword, testSiteId);
	//		SiteValue[] sv = cs.getSites();
	//		SiteValue temp = null;
	//		for (int i = 0; i < sv.length; i++) {
	//			SiteValue s = sv[i];
	//			if (s.getSiteId().equals(testSiteId)) {
	//				temp = s;
	//				break;
	//			}
	//		}
	//		assertNotNull(ulv);
	//		assertEquals(testUserName, ulv.getUser().getUserName());
	//		assertEquals(temp.getName(), ulv.getSiteName());
	//	}
	//
	//	public void testGetUnits() {
	//		ClientServiceSpring cs = getClientService();
	//		UnitValue[] uv = cs.getUnits();
	//		assertNotNull(uv);
	//		assertEquals(16, uv.length);
	//	}
	//
	//	public void testGetAllUnits() {
	//		ClientServiceSpring cs = getClientService();
	//		UnitValue[] uv = cs.getAllUnits();
	//		assertNotNull(uv);
	//		assertEquals(16, uv.length);
	//	}
	//
	//	public void testGetUnit() {
	//		ClientServiceSpring cs = getClientService();
	//		UnitValue u = cs.getUnit(4087);
	//		assertNotNull(u);
	//		assertEquals("Ohrensausen (Tinnitus)", u.getName());
	//	}
	//
	//	public void testGetAllUserForUnit() {
	//		ClientServiceSpring cs = getClientService();
	//		UserValue[] uv = cs.getAllUserForUnit(3227);
	//		assertNotNull(uv);
	//		assertTrue(uv.length > 0);
	//	}
	//
	//	public void testUpdateUser() {
	//		ClientServiceSpring cs = getClientService();
	//		UserValue uv = createTestUser();
	//		assertNotNull(uv);
	//		String newFirstName = "NewFirstName";
	//		String newLastName = "NewLastName";
	//		String newEmail = "NewEmail";
	//		uv.setFirstName(newFirstName);
	//		uv.setLastName(newLastName);
	//		uv.setEmail(newEmail);
	//		cs.updateUser(uv);
	//		uv = cs.getUserForId(uv.getUserName());
	//		assertNotNull(uv);
	//		assertEquals(newFirstName, uv.getFirstName());
	//		assertEquals(newLastName, uv.getLastName());
	//		assertEquals(newEmail, uv.getEmail());
	//		this.deleteTestUser(uv.getUserName());
	//	}
	//
	//	//    public void changePassword(java.lang.String userName, java.lang.String passwdNew);
	//	//
	//
	//	public void testCreateUser() {
	//		String userName = "JuwiMMTestUser";
	//		String passwd = "testPass";
	//		String firstName = "firstName";
	//		String lastName = "lastName";
	//		String email = "email@email.com";
	//		Integer unitId = 3227;
	//		ClientServiceSpring cs = getClientService();
	//		cs.createUser(userName, passwd, firstName, lastName, email, unitId);
	//		UserValue uv = cs.getUserForId(userName);
	//		assertNotNull(uv);
	//		assertEquals(userName, uv.getUserName());
	//		assertEquals(firstName, uv.getFirstName());
	//		cs.deleteUser(userName);
	//	}
	//
	//	public void testCreateUnit() {
	//		ClientServiceSpring cs = getClientService();
	//		String testUnitName = "testUnitName";
	//		Integer unitId = cs.createUnit(testUnitName);
	//		assertNotNull(unitId);
	//		assertTrue(unitId > 0);
	//		cs.removeUnit(unitId);
	//		UnitValue unit = cs.getUnit(unitId);
	//		assertNull(unit);
	//	}
	//
	//	public void testRemoveUnit() {
	//		ClientServiceSpring cs = getClientService();
	//		String testUnitName = "testUnitName";
	//		Integer unitId = cs.createUnit(testUnitName);
	//		assertNotNull(unitId);
	//		UnitValue unit = cs.getUnit(unitId);
	//		assertNotNull(unit);
	//		assertEquals(testUnitName, unit.getName());
	//		cs.removeUnit(unit);
	//		unit = cs.getUnit(unitId);
	//		assertNull(unit);
	//	}
	//
	//	public void testRemoveTask() {
	//		ClientServiceSpring cs = getClientService();
	//		int taskId = this.createTestTask();
	//		assertNotNull(taskId);
	//		assertTrue(taskId > 0);
	//		TaskValue tv = cs.getTaskForId(taskId);
	//		assertNotNull(tv);
	//		cs.removeTask(taskId);
	//		tv = cs.getTaskForId(taskId);
	//		assertNull(tv);
	//	}
	//
	//	public void testSetTaskViewed() {
	//		ClientServiceSpring cs = getClientService();
	//		int taskId = this.createTestTask();
	//		assertTrue(taskId > 0);
	//		cs.setTaskViewed(taskId);
	//		TaskValue tv = cs.getTaskForId(taskId);
	//		assertNotNull(tv);
	//		assertEquals(Constants.TASK_STATUS_VIEWED, tv.getStatus());
	//		removeTask(taskId);
	//	}
	//
	//	public void testRemovePerson() {
	//		ClientServiceSpring cs = getClientService();
	//		PersonValue pv = createTestPerson();
	//		long id = cs.createPerson(pv);
	//		assertNotNull(id);
	//		assertTrue(id > 0);
	//		cs.removePerson(id);
	//		pv = cs.getPerson(id);
	//		assertNull(pv);
	//	}
	//
	//	public void testRemoveDepartment() {
	//		ClientServiceSpring cs = getClientService();
	//		long departmentId = createTestDepartment();
	//		assertNotNull(departmentId);
	//		assertTrue(departmentId > 0);
	//		cs.removeDepartment(departmentId);
	//		DepartmentValue dv = cs.getDepartment(departmentId);
	//		assertNull(dv);
	//	}
	//
	//	public void testRemoveAddress() {
	//		ClientServiceSpring cs = getClientService();
	//		long addressId = createTestAddress();
	//		assertNotNull(addressId);
	//		cs.removeAddress(addressId);
	//		AddressValue av = cs.getAddress(addressId);
	//		assertNull(av);
	//	}
	//
	//	public void testRemoveTalktime() {
	//		ClientServiceSpring cs = getClientService();
	//		long id = createTestTalktime();
	//		TalktimeValue tv = cs.getTalktime(id);
	//		assertNotNull(tv);
	//		assertEquals(talkTimes, tv.getTalkTimes());
	//		cs.removeTalktime(id);
	//		tv = cs.getTalktime(id);
	//		assertNull(tv);
	//	}

	//    public long addTalktime2Person(long personId, java.lang.String talkTimeType, java.lang.String talkTimes);
	//
	//    /**
	//     * 
	//     */
	//    public long addTalktime2Department(long departmentId, java.lang.String talkTimeType, java.lang.String talkTimes);
	//
	//    /**
	//     * 
	//     */
	//    public long addTalktime2Unit(int unitId, java.lang.String talkTimeType, java.lang.String talkTimes);
	//
	//    /**
	//     * 
	//     */
	//    public void addAddress2Person(long personId, long addressId);
	//
	//    /**
	//     * 
	//     */
	//    public void addAddress2Department(long departmentId, long addressId);
	//
	//    /**
	//     * 
	//     */
	//    public void addAddress2Unit(int unitIt, long addressId);
	//
	//    /**
	//     * 
	//     */
	//    public void updateUnit(de.juwimm.cms.vo.UnitValue dao);

	//	public void testUpdatePerson() {
	//	  ClientServiceSpring cs = getClientService();
	//	  long personId = 550;
	//	  createT
	//	  PersonValue pv = cs.getPerson(personId);
	//	  assertEquals("Holstiege", pv.getLastname());
	//	  PersonValue backup = new PersonValue();
	//	  pv.copy(backup);
	//	  String newLastName = "Sein neuer Nachname";
	//	  pv.setLastname(newLastName);
	//	  cs.updatePerson(pv);
	//	  pv = cs.getPerson(personId);
	//	  assertEquals(newLastName, pv.getLastname());
	//	  cs.updatePerson(backup);
	//}

	//    public void updateDepartment(de.juwimm.cms.components.vo.DepartmentValue departmentDao);
	//
	//    /**
	//     * 
	//     */
	//    public void updateAddress(de.juwimm.cms.components.vo.AddressValue addressDao);
	//
	//    /**
	//     * 
	//     */
	//    public void updateTalktime(de.juwimm.cms.components.vo.TalktimeValue talktimeDao);
	//

	//	public void testDeleteUser() {
	//		String userName = "JuwiMMTestUser";
	//		String passwd = "testPass";
	//		String firstName = "firstName";
	//		String lastName = "lastName";
	//		String email = "email@email.com";
	//		Integer unitId = 3227;
	//		ClientServiceSpring cs = getClientService();
	//		cs.createUser(userName, passwd, firstName, lastName, email, unitId);
	//		UserValue uv = cs.getUserForId(userName);
	//		assertNotNull(uv);
	//		assertEquals(userName, uv.getUserName());
	//		assertEquals(firstName, uv.getFirstName());
	//		cs.deleteUser(userName);
	//		uv = cs.getUserForId(userName);
	//		assertNull(uv);
	//	}

	//    public void removeUserFromUnit(de.juwimm.cms.vo.UnitValue unit, java.lang.String userName);
	//
	//    /**
	//     * 
	//     */
	//    public void addUser2Unit(de.juwimm.cms.authorization.vo.UserValue user, de.juwimm.cms.vo.UnitValue unit);
	//

	//	public void testGetUnit4ViewComponent() {
	//		ClientServiceSpring cs = getClientService();
	//		int unitId = cs.getUnit4ViewComponent(1284);
	//		assertTrue(unitId == 128);
	//	}
	//
	//	public void testGetViewDocuments() {
	//		ClientServiceSpring cs = getClientService();
	//		ViewDocumentValue[] vd = cs.getViewDocuments();
	//		assertNotNull(vd);
	//		assertEquals(1, vd.length);
	//	}
	//
	//	public void testGetViewDocument() {
	//		ClientServiceSpring cs = getClientService();
	//		ViewDocumentValue vd = cs.getViewDocument("browser", "deutsch");
	//		assertNotNull(vd);
	//		assertEquals(new Integer(36122), vd.getViewId());
	//	}
	//
	//	public void testGetAllUser() {
	//		ClientServiceSpring cs = getClientService();
	//		UserValue[] uv = cs.getAllUser();
	//		assertNotNull(uv);
	//		assertEquals(5, uv.length);
	//	}
	//
	//	public void testGetAllTasks() {
	//		ClientServiceSpring cs = getClientService();
	//		TaskValue[] tv = cs.getAllTasks();
	//		assertNotNull(tv);
	//		assertEquals(0, tv.length);
	//	}

	//
	//    /**
	//     * 
	//     */
	//    public boolean isNewTask4User();
	//

	//	public void testGetAllUserWithinGroup() {
	//		ClientServiceSpring cs = getClientService();
	//		UserValue[] uv = cs.getAllUser(36);
	//		assertNotNull(uv);
	//		assertEquals(6, uv.length);
	//	}
	//
	//	public void testGetAllUser4GroupAndUnit() {
	//		ClientServiceSpring cs = getClientService();
	//		UserValue[] uv = cs.getAllUser4GroupAndUnit(36, 3227);
	//		assertNotNull(uv);
	//		assertEquals(0, uv.length);
	//	}

	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.ViewDocumentValue createViewDocument(de.juwimm.cms.vo.ViewDocumentValue vDao);
	//
	//    /**
	//     * 
	//     */
	//    public void setDefaultViewDocument(int viewDocumentId);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.ContentValue createContent(de.juwimm.cms.vo.ContentValue cDao);
	//
	//    /**
	//     * 
	//     */
	//    public void setUnit4ViewComponent(int unitId, de.juwimm.cms.vo.ViewDocumentValue viewDocumentDao, int viewComponentId);
	//
	//    /**
	//     * 
	//     */
	//    public void removeViewDocument(int viewDocumentId);
	//
	//    /**
	//     * 
	//     */
	//    public void removeViewComponent(java.lang.Integer viewComponentId, boolean force);

	//	public void testGetViewComponentsWithReferenceToViewComponentId() {
	//		ClientServiceSpring cs = getClientService();
	//		ViewComponentValue[] vc = cs.getViewComponentsWithReferenceToViewComponentId(1274);
	//		assertNotNull(vc);
	//		assertEquals(1, vc.length);
	//	}
	//
	//	public void testGetViewComponent() {
	//		ClientServiceSpring cs = getClientService();
	//		ViewComponentValue vc = cs.getViewComponent(1274);
	//		assertNotNull(vc);
	//		assertEquals("M", vc.getDisplayLinkName());
	//		assertEquals(new Integer(1), vc.getParentId());
	//	}
	//
	//	public void testGetViewComponentWithDepth() {
	//		ClientServiceSpring cs = getClientService();
	//		ViewComponentValue vc = cs.getViewComponentWithDepth(1274, 2);
	//		assertNotNull(vc);
	//		assertEquals("M", vc.getDisplayLinkName());
	//		assertEquals(17, vc.getChildren().length);
	//	}

	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.ViewComponentValue moveViewComponentUp(int viewComponentId);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.ViewComponentValue moveViewComponentDown(int viewComponentId);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.ViewComponentValue moveViewComponentLeft(int viewComponentId);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.ViewComponentValue moveViewComponentRight(int viewComponentId);
	//
	//    /**
	//     * 
	//     */
	//    public boolean isUnitAndChangesParentUnitLeft(int viewComponentId);
	//
	//    /**
	//     * 
	//     */
	//    public boolean isUnitAndChangesParentUnitRight(int viewComponentId);
	//
	//    /**
	//     * 
	//     */
	//    public void cancelApproval(int viewComponentId);
	//
	//    /**
	//     * 
	//     */
	//    public void updateStatus4ViewComponent(de.juwimm.cms.vo.ViewComponentValue vcDao);
	//
	//    /**
	//     * 
	//     */
	//    public int createTask(java.lang.String receiverId, java.lang.String receiverRole, int unitId, java.lang.String comment, byte taskType);
	//
	//    /**
	//     * 
	//     */
	//    public void addViewComponentsToTask(int taskId, java.lang.Integer[] vcIds);
	//
	//    /**
	//     * 
	//     */
	//    public void removeViewComponentsFromTask(int taskId, java.lang.Integer[] vcIds);
	//

	//	public void testGetViewComponent4Unit() {
	//		ClientServiceSpring cs = getClientService();
	//		ViewComponentValue vc = cs.getViewComponent4Unit(128, 1);
	//		assertNotNull(vc);
	//		log.debug("ViewComponentId: " + vc.getViewComponentId());
	//	}
	//
	//	public void testGetViewComponent4UnitWithDepth() {
	//		ClientServiceSpring cs = getClientService();
	//		ViewComponentValue vc = cs.getViewComponent4UnitWithDepth(128, 2, 1);
	//		assertNotNull(vc);
	//		log.debug("ViewComponentId: " + vc.getViewComponentId());
	//	}
	//
	//	public void testGetParents4ViewComponent() {
	//		ClientServiceSpring cs = getClientService();
	//		String[] s = cs.getParents4ViewComponent(1262);
	//		assertNotNull(s);
	//		assertEquals(5, s.length);
	//	}

	//
	//    /**
	//     * 
	//     */
	//    public void updateTemplate(int viewComponentId, java.lang.String templateName);
	//

	//	public void testGetContentTemplateName(int contentId) {
	//		ClientServiceSpring cs = getClientService();
	//		String template = cs.getContentTemplateName(1131);
	//		assertNotNull(template);
	//		assertFalse(template.equals(""));
	//		assertEquals("ursachen", template);
	//	}
	//
	//	public void testGetAllContentVersions(int contentId) {
	//		ClientServiceSpring cs = getClientService();
	//		ContentVersionValue[] cv = cs.getAllContentVersions(1131);
	//		assertNotNull(cv);
	//		assertEquals(3, cv.length);
	//	}
	//
	//	public void testGetContentVersion(int contentVersionId) {
	//		ClientServiceSpring cs = getClientService();
	//		ContentVersionValue cv = cs.getContentVersion(1131);
	//		assertNotNull(cv);
	//		assertEquals(new Integer(245795), cv.getContentVersionId());
	//		assertEquals("PUBLS", cv.getVersion());
	//	}

	//
	//    /**
	//     * 
	//     */
	//    public void removeAllOldContentVersions(int contentId);
	//
	//    /**
	//     * 
	//     */
	//    public void removeContentVersion(int contentVersionId);
	//

	//	public void testGetNotReferencedUnits() {
	//		ClientServiceSpring cs = getClientService();
	//		ViewDocumentValue viewDocument = cs.getViewDocument("browser", "deutsch");
	//		UnitValue[] uv = cs.getNotReferencedUnits(viewDocument);
	//		assertNotNull(uv);
	//		assertEquals(2, uv.length);
	//	}
	//
	//	public void testGetAllPictures4Unit(int unitId) {
	//		ClientServiceSpring cs = getClientService();
	//		Integer[] ids = cs.getAllPictures4Unit(128);
	//		assertNotNull(ids);
	//		assertEquals(3, ids.length);
	//	}
	//
	//	public void testGetAllDocuments4Unit() {
	//		ClientServiceSpring cs = getClientService();
	//		Integer[] ids = cs.getAllDocuments4Unit(3227);
	//		assertNotNull(ids);
	//		assertEquals(26, ids.length);
	//	}
	//
	//	public void testGetAllSlimDocumentValues() {
	//		ClientServiceSpring cs = getClientService();
	//		DocumentSlimValue[] dv = cs.getAllSlimDocumentValues(3227);
	//		assertNotNull(dv);
	//		assertEquals(26, dv.length);
	//	}
	//
	//	public void testGetAllSlimPictures4Unit() {
	//		ClientServiceSpring cs = getClientService();
	//		PictureSlimstValue[] pv = cs.getAllSlimPictures4Unit(3227);
	//		assertNotNull(pv);
	//		assertEquals(330, pv.length);
	//	}

	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.ContentValue checkOut(int contentId, boolean force);
	//
	//    /**
	//     * 
	//     */
	//    public void checkIn(de.juwimm.cms.vo.ContentValue contentDao);
	//
	//    /**
	//     * 
	//     */
	//    public void saveContent(int contentId, java.lang.String content);
	//
	//    /**
	//     * 
	//     */
	//    public void removeDocument(int documentId);

	//	public void testGetDocumentName() {
	//		ClientServiceSpring cs = getClientService();
	//		String name = cs.getDocumentName(485);
	//		assertNotNull(name);
	//		assertFalse(name.equals(""));
	//		assertEquals("hannover_langenhagen.pdf", name);
	//	}
	//
	//	public void testGetPictureData() {
	//		ClientServiceSpring cs = getClientService();
	//		byte[] data = cs.getPictureData(9646);
	//		assertNotNull(data);
	//	}
	//
	//	public void testGetPicture() {
	//		ClientServiceSpring cs = getClientService();
	//		PictureSlimValue pv = cs.getPicture(9646);
	//		assertNotNull(pv);
	//		assertEquals("image/jpeg", pv.getMimeType());
	//		assertEquals(new Integer(103), pv.getWidth());
	//	}
	//
	//	public void testGetPictureFileName() {
	//		ClientServiceSpring cs = getClientService();
	//		String name = cs.getPictureFileName(18536);
	//		assertNotNull(name);
	//		assertFalse(name.equals(""));
	//		assertEquals("sign_up_pencil.jpg", name);
	//	}
	//
	//	public void testGetPictureAltText() {
	//		ClientServiceSpring cs = getClientService();
	//		String text = cs.getPictureAltText(18536);
	//		assertNotNull(text);
	//		assertFalse(text.equals(""));
	//		assertEquals("test_alt_text", text);
	//	}

	//
	//    /**
	//     * 
	//     */
	//    public void updatePictureAltText(int pictureId, java.lang.String altText);
	//
	//    /**
	//     * 
	//     */
	//    public void checkIn4ContentId(java.lang.Integer contentId);
	//

	//	public void testLogout() {
	//		ClientServiceSpring cs = getClientService();
	//		cs.logout();
	//		UserValue user = cs.getUserForId("a");
	//		assertEquals(0L, user.getLoginDate());
	//	}
	//
	//	public void testGetGroups() {
	//		ClientServiceSpring cs = getClientService();
	//		GroupValue[] gv = cs.getGroups();
	//		assertNotNull(gv);
	//		assertEquals(2, gv.length);
	//	}

	//    public void changePassword4User(java.lang.String userName, java.lang.String passwdOld, java.lang.String passwdNew);
	//
	//    /**
	//     * 
	//     */
	//    public void removeUnit(int unitId);
	//
	//    /**
	//     * 
	//     */
	//    public void removePicture(int pictureId);
	//
	//    /**
	//     * 
	//     */
	//    public int addPicture2Unit(int unitId, byte[] thumbnail, byte[] picture, java.lang.String mimeType);
	//
	//    /**
	//     * 
	//     */
	//    public int addPicture2Unit(int unitId, byte[] thumbnail, byte[] picture, java.lang.String mimeType, java.lang.String altText, java.lang.String pictureName);
	//
	//    /**
	//     * 
	//     */
	//    public int addPictureWithPreview2Unit(int unitId, byte[] thumbnail, byte[] picture, byte[] preview, java.lang.String mimeType, java.lang.String altText, java.lang.String pictureName);
	//
	//    /**
	//     * 
	//     */
	//    public void setPicture4Unit(int unitId, int pictureId);
	//

	//	public void testSetPicture4Person() {
	//		ClientServiceSpring cs = getClientService();
	//		long personId = 517;
	//		PersonValue p = cs.getPerson(personId);
	//		Integer currentPictureId = p.getImageId();
	//		assertEquals(new Integer(8092), currentPictureId);
	//		cs.setPicture4Person(517, 9771);
	//		p = cs.getPerson(personId);
	//		assertEquals(new Integer(9771), p.getImageId());
	//		cs.setPicture4Person(personId, currentPictureId);
	//		p = cs.getPerson(personId);
	//		assertEquals(currentPictureId, p.getImageId());
	//	}

	//    public de.juwimm.cms.vo.ViewComponentValue saveViewComponent(de.juwimm.cms.vo.ViewComponentValue viewComponentValue);
	//
	//	public void testGetPerson4Name() {
	//		ClientServiceSpring cs = getClientService();
	//		Object[] persons = cs.getPerson4Name("Doris", "Bergs");
	//		assertNotNull(persons);
	//		assertEquals(1, persons.length);
	//	}
	//
	//	public void testCreateDepartment() {
	//		ClientServiceSpring cs = getClientService();
	//		long departmentId = createTestDepartment();
	//		assertNotNull(departmentId);
	//		assertTrue(departmentId > 0);
	//		DepartmentValue department = cs.getDepartment(departmentId);
	//		assertNotNull(department);
	//		assertEquals(departmentName, department.getName());
	//		assertEquals(departmentUnitId, department.getUnitId());
	//		cs.removeDepartment(departmentId);
	//		department = cs.getDepartment(departmentId);
	//		assertNull(department);
	//	}
	//
	//	public void testCreatePerson() {
	//		ClientServiceSpring cs = getClientService();
	//		PersonValue pv = createTestPerson();
	//		long personId = cs.createPerson(pv);
	//		assertNotNull(personId);
	//		assertTrue(personId > 0);
	//		PersonValue pvSaved = cs.getPerson(personId);
	//		assertNotNull(pvSaved);
	//		assertEquals(pv.getFirstname(), pvSaved.getFirstname());
	//		assertEquals(pv.getLastname(), pvSaved.getLastname());
	//		cs.removePerson(personId);
	//		pvSaved = cs.getPerson(personId);
	//		assertNull(pvSaved);
	//	}

	//    public long createAddress(de.juwimm.cms.components.vo.AddressValue addressValue);

	//	public void testGetDepartments4Name() {
	//		ClientServiceSpring cs = getClientService();
	//		DepartmentValue[] dv = cs.getDepartments4Name("Department");
	//		assertNotNull(dv);
	//		assertEquals(1, dv.length);
	//		DepartmentValue d = dv[0];
	//		assertEquals(new Integer(4600), d.getUnitId());
	//	}
	//
	//	public void testGetDepartment(long departmentId) {
	//		ClientServiceSpring cs = getClientService();
	//		DepartmentValue d = cs.getDepartment(4600);
	//		assertEquals(new Integer(4600), d.getUnitId());
	//		assertEquals("Department", d.getName());
	//	}
	//
	//	public void testGetUnits4Name(java.lang.String name) {
	//		ClientServiceSpring cs = getClientService();
	//		UnitValue[] uv = cs.getUnits4Name("Florafarm");
	//		assertNotNull(uv);
	//		assertEquals(1, uv.length);
	//		assertEquals(new Integer(3223), uv[0].getUnitId());
	//	}

	//    public de.juwimm.cms.vo.UnitValue[] getUnits4User(java.lang.String name);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.components.vo.PersonValue getPerson(long personId);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.ViewComponentValue[] getAllViewComponents4Status(java.lang.Integer viewDocumentId, int status);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.EditionValue[] getEditions(int unitId, java.lang.Integer viewDocumentId);
	//
	//    /**
	//     * 
	//     */
	//    public void createEdition(java.lang.String comment, int rootViewComponentId, boolean deploy, boolean showMessage);
	//
	//    /**
	//     * 
	//     */
	//    public void removeEdition(int editionId);
	//
	//    /**
	//     * 
	//     */
	//    public void setActiveEdition(int editionId, boolean deploy);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.ViewComponentValue[] getViewComponents4Status(int unitId, int status, java.lang.Integer viewDocumentId);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.compound.ViewIdAndUnitIdValue[] getAllViewComponentsWithUnits(int viewComponentId);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.components.vo.AddressValue getAddress(long addressId);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.components.vo.TalktimeValue getTalktime(long talktimeId);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.ViewComponentValue insertViewComponent(int childId, java.lang.Integer viewDocumentId, java.lang.String reference, java.lang.String displayLinkName, java.lang.String linkDescription, int positionId);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.ViewComponentValue addFirstViewComponent(int parentId, java.lang.Integer viewDocumentId, java.lang.String reference, java.lang.String displayLinkName, java.lang.String linkDescription);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.vo.compound.ViewIdAndInfoTextValue[] getAllChildrenNamesWithUnit(int viewComponentId);
	//
	//    /**
	//     * 
	//     */
	//    public java.lang.String[] getAnchors(int contentId);
	//
	//    /**
	//     * 
	//     */
	//    public de.juwimm.cms.components.vo.PersonToUnitLinkValue[] getPersonLinks4Unit(int unitId);
	//
	//  
	//    public int addPersonLink2Unit(int unitId, long personId, int roleType);
	//
	//    public void removePersonLinkFromUnit(int personLinkId);
	//
	//    public de.juwimm.cms.vo.SiteValue createSite(de.juwimm.cms.vo.SiteValue siteValue);
	//
	//
	//    public void removeSite(int siteId);
	//
	//  
	//    public void updateSite(de.juwimm.cms.vo.SiteValue siteValue);
	//
	//    public de.juwimm.cms.vo.SiteValue[] getAllSites();
	//
	//    public de.juwimm.cms.vo.SiteValue[] getAllSites4CurrentUser();
	//
	//    public de.juwimm.cms.authorization.vo.UserValue[] getAllUsersForAllSites();
	//
	//    public de.juwimm.cms.authorization.vo.UserValue[] getAllUserOwnSites();
	//
	//    public java.lang.String[] getConnectedUsersForSite(int siteId);
	//
	//    public void setConnectedUsersForSite(int siteId, java.lang.String[] userIds);
	//
	//    public void setSiteConfig(int siteId, java.lang.String config);
	//
	//    public java.lang.String getSiteConfig(int siteId);
	//
	//    public de.juwimm.cms.vo.HostValue createHost(java.lang.String hostName);
	//
	//    public de.juwimm.cms.vo.HostValue[] getAllHosts();
	//
	//
	//    public de.juwimm.cms.vo.HostValue[] getHosts();
	//
	//
	//    public de.juwimm.cms.vo.HostValue[] getAllUnassignedHosts();
	//
	//
	//    public de.juwimm.cms.vo.HostValue[] getHostsForSite(int siteId);
	//
	// 
	//    public void removeHost(java.lang.String hostName);
	//
	//
	//    public de.juwimm.cms.vo.SiteValue getSiteForName(java.lang.String siteName);
	//
	// 
	//    public void setSite(java.lang.String hostName, java.lang.String siteName);
	//
	// 
	//    public void setSite(java.lang.String hostName, int siteId);
	//
	//    public void setStartPage(java.lang.String hostName, java.lang.String vcId);
	//
	//   
	//    public java.lang.String getSite(java.lang.String hostName);
	//
	// 
	//    public de.juwimm.cms.vo.SiteValue getCurrentSite(java.lang.Integer siteId);
	//
	//
	//    public java.lang.String getStartPage(java.lang.String hostName);
	//
	//
	//    public void removeSiteFromHost(java.lang.String hostName);
	//
	//    public void removeStartpageFromHost(java.lang.String hostName);
	//
	//    public void setRedirectUrl(java.lang.String hostName, java.lang.String redirectUrl);
	//
	//    public void setRedirectHost(java.lang.String hostName, java.lang.String redirectHostName);
	//
	//    public void addSimplePwRealmToVC(java.lang.Integer simplePwRealmId, java.lang.Integer viewComponentId, java.lang.String neededRole, java.lang.Integer loginPageId);
	//
	//
	//    public void editSimplePwRealm(de.juwimm.cms.safeguard.vo.RealmSimplePwValue simplePwRealmValue);
	//
	//
	//    public void addSqlDbRealmToVC(java.lang.Integer jdbcRealmId, java.lang.Integer viewComponentId, java.lang.String neededRole, java.lang.Integer loginPageId);
	//
	//    public int addSimpleRealmToSite(java.lang.String realmName, int siteId, java.lang.String loginPageId);
	//
	//    public de.juwimm.cms.safeguard.vo.RealmSimplePwValue[] getSimplePwRealmsForUser(java.lang.String user);
	//
	//    public de.juwimm.cms.safeguard.vo.RealmSimplePwValue[] getSimplePwRealmsForSite(java.lang.Integer siteId);
	//
	//
	//    public de.juwimm.cms.safeguard.vo.RealmSimplePwValue[] getSimplePwRealms4CurrentUser(java.lang.Integer activeSiteId);
	//
	//    public de.juwimm.cms.safeguard.vo.RealmSimplePwUserValue[] getUserForSimplePwRealm(java.lang.Integer simplePwRealmId);
	//
	//    public int addUserToSimpleRealm(java.lang.Integer simplePwRealmId, de.juwimm.cms.safeguard.vo.RealmSimplePwUserValue simplePwRealmUserValue);
	//
	//    public boolean deleteSimplePwRealmUser(java.lang.Integer simplePwRealmUserId);
	//
	//    public de.juwimm.cms.safeguard.vo.ActiveRealmValue getActiveRealm(java.lang.Integer viewComponentId);
	//
	//    public java.lang.Integer getFirstProtectedParentId(java.lang.Integer viewComponentId);
	//
	//    public boolean deleteRealmAtVC(java.lang.Integer viewComponentId);
	//
	//
	//    public java.lang.Integer addSqlDbRealmToSite(java.lang.Integer siteId, de.juwimm.cms.safeguard.vo.RealmJdbcValue jdbcRealmValue);
	//
	//
	//    public de.juwimm.cms.safeguard.vo.RealmJdbcValue[] getSqlDbRealmsForSite(java.lang.Integer siteId);
	//
	//
	//    public boolean deleteSqlDbRealm(java.lang.Integer jdbcRealmId);
	//
	//    public void editSqlDbRealm(de.juwimm.cms.safeguard.vo.RealmJdbcValue jdbcRealmValue);
	//
	//    public java.lang.Integer addLdapRealmToSite(java.lang.Integer siteId, de.juwimm.cms.safeguard.vo.RealmLdapValue ldapRealmValue);
	//
	//    public void deleteLdapRealm(java.lang.Integer ldapRealmId);
	//
	//  
	//    public void editLdapRealm(de.juwimm.cms.safeguard.vo.RealmLdapValue ldapRealmValue);
	//
	// 
	//    public de.juwimm.cms.safeguard.vo.RealmLdapValue[] getLdapRealmsForSite(java.lang.Integer siteId);
	//
	// 
	// 
	//    public void addLdapRealmToVC(java.lang.Integer viewComponentId, java.lang.Integer ldapRealmId, java.lang.String neededRole, java.lang.Integer loginPageId);
	//
	//    
	//    public java.lang.Integer addJaasRealmToSite(java.lang.Integer siteId, de.juwimm.cms.safeguard.vo.RealmJaasValue jaasRealmValue);
	//
	//   
	//    public void deleteJaasRealm(java.lang.Integer jaasRealmId);
	//
	//    
	//    public void editJaasRealm(de.juwimm.cms.safeguard.vo.RealmJaasValue jaasRealmValue);
	//
	//    
	//    public de.juwimm.cms.safeguard.vo.RealmJaasValue[] getJaasRealmsForSite(java.lang.Integer siteId);
	//
	//   
	//    public void addJaasRealmToVC(java.lang.Integer viewComponentId, java.lang.Integer jaasRealmId, java.lang.String neededRole, java.lang.Integer loginPageId);
	//
	//   
	//    public void clearSvgCache();
	//
	//   
	//    public de.juwimm.cms.vo.SiteValue[] getAllNotAssignedSites();
	//
	//    
	//    public de.juwimm.cms.vo.SiteValue[] getSites4SiteGroup(de.juwimm.cms.vo.SiteGroupValue siteGroupValue);
	//
	//   
	//    public de.juwimm.cms.vo.SiteGroupValue[] getAllSiteGroups();
	//
	//    public de.juwimm.cms.vo.SiteGroupValue createSiteGroup(de.juwimm.cms.vo.SiteGroupValue value);
	//
	//    
	//    public void updateSiteGroup(de.juwimm.cms.vo.SiteGroupValue value);
	//
	//    
	//    public void removeSiteGroup(de.juwimm.cms.vo.SiteGroupValue value);
	//
	//    
	//    public de.juwimm.cms.vo.SiteValue[] getAllRelatedSites(int siteId);
	//
	//   
	//    public de.juwimm.cms.vo.ViewDocumentValue[] getAllViewDocuments4Site(int siteId);
	//
	//   
	//    public de.juwimm.cms.vo.ViewDocumentValue getViewDocument4ViewComponent(int viewComponentId);
	//
	//    
	//    public de.juwimm.cms.authorization.vo.UserUnitsGroupsValue[] getUserUnitsGroups4UnitAndGroup(int unitId, int groupId);
	//
	//   
	//    public de.juwimm.cms.search.vo.XmlSearchValue[] searchXmlByUnit(int unitId, java.lang.Integer viewDocumentId, java.lang.String xpathQuery, boolean parentSearch);
	//
	//   
	//    public de.juwimm.cms.search.vo.XmlSearchValue[] searchXml(int siteId, java.lang.String xpathQuery);
	//
	// 
	//    public de.juwimm.cms.vo.UnitValue[] getAllUnits4Site(java.lang.Integer siteId);
	//
	//   
	//    public de.juwimm.cms.vo.ShortLinkValue[] getAllShortLinks4Site(java.lang.Integer siteId);
	//
	//   
	//    public de.juwimm.cms.vo.HostValue saveHost(de.juwimm.cms.vo.HostValue hostValue);
	//
	// 
	//    public de.juwimm.cms.vo.ShortLinkValue createShortLink(de.juwimm.cms.vo.ShortLinkValue shortLinkValue);
	//
	// 
	//    public de.juwimm.cms.vo.ShortLinkValue saveShortLink(de.juwimm.cms.vo.ShortLinkValue shortLinkValue);
	//
	//  
	//    public void deleteShortLink(de.juwimm.cms.vo.ShortLinkValue shortLinkValue);
	//
	//    public void exportXlsPersonData(java.io.File outputFile);
	//
	// 
	//    public java.lang.Integer importDocument(java.io.File file, int unitId, java.lang.String fileName, java.lang.String mimeType);
	//
	//    public void createEditionForExport(java.io.File outputFile, int viewComponentIdWithUnit);
	//
	//   
	//    public void importEditionFromImport(java.io.File file, int unitId);
	//
	//    
	//    public de.juwimm.cms.vo.ViewDocumentValue getViewDocument4ViewTypeAndLanguage(java.lang.String viewType, java.lang.String language);
	//
	//  
	//    public de.juwimm.cms.vo.ViewComponentValue[] getAllViewComponents4UnitAndStatus(java.lang.Integer unitId, java.lang.Integer viewDocumentId, int status);
	//
	//   
	//    public java.io.InputStream exportEditionFull();
	//
	//  
	//    public java.io.InputStream exportEditionUnit(java.lang.Integer rootViewComponentId);
	//
	// 
}
