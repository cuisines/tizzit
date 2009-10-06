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
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package de.juwimm.cms.remote;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.Comparer;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.remote.AuthorizationServiceSpring;
import de.juwimm.cms.authorization.vo.GroupValue;
import de.juwimm.cms.authorization.vo.RoleValue;
import de.juwimm.cms.authorization.vo.UserLoginValue;
import de.juwimm.cms.authorization.vo.UserUnitsGroupsValue;
import de.juwimm.cms.authorization.vo.UserValue;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.components.vo.DepartmentValue;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.components.vo.TalktimeValue;
import de.juwimm.cms.exceptions.AlreadyCheckedOutException;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewDocumentHbm;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.safeguard.remote.AlreadyExistsException;
import de.juwimm.cms.safeguard.vo.ActiveRealmValue;
import de.juwimm.cms.safeguard.vo.RealmJaasValue;
import de.juwimm.cms.safeguard.vo.RealmJdbcValue;
import de.juwimm.cms.safeguard.vo.RealmLdapValue;
import de.juwimm.cms.safeguard.vo.RealmSimplePwUserValue;
import de.juwimm.cms.safeguard.vo.RealmSimplePwValue;
import de.juwimm.cms.search.beans.SearchengineService;
import de.juwimm.cms.search.vo.XmlSearchValue;
import de.juwimm.cms.vo.ContentValue;
import de.juwimm.cms.vo.ContentVersionValue;
import de.juwimm.cms.vo.DocumentSlimValue;
import de.juwimm.cms.vo.EditionValue;
import de.juwimm.cms.vo.HostValue;
import de.juwimm.cms.vo.PictureSlimValue;
import de.juwimm.cms.vo.PictureSlimstValue;
import de.juwimm.cms.vo.ShortLinkValue;
import de.juwimm.cms.vo.SiteGroupValue;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.TaskValue;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.cms.vo.ViewDocumentValue;
import de.juwimm.cms.vo.compound.ViewIdAndInfoTextValue;
import de.juwimm.cms.vo.compound.ViewIdAndUnitIdValue;

/**
 * Facade for all communication with the CMS-client
 * 
 * @see de.juwimm.cms.remote.ClientServiceSpring
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ClientServiceSpringImpl extends ClientServiceSpringBase {
	private static final long serialVersionUID = 7966414209973090698L;
	private static Log log = LogFactory.getLog(ClientServiceSpringImpl.class);

	@Autowired
	private SearchengineService searchengineService;

	@Override
	protected void handleReindexPage(Integer viewComponentId) throws Exception {
		Integer content = null;
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			if (view.getViewType() == Constants.VIEW_TYPE_INTERNAL_LINK || view.getViewType() == Constants.VIEW_TYPE_SYMLINK) {
				view = super.getViewComponentHbmDao().load(new Integer(view.getReference()));
				content = new Integer(view.getReference());
			} else {
				content = new Integer(view.getReference());
			}
			ContentHbm c = getContentHbmDao().load(content);
			c.setUpdateSearchIndex(true);
		} catch (Exception e) {
			log.error("Could not start the reindexing for page with vcid " + viewComponentId, e);
		}

	}

	@Override
	protected void handleReindexSite(Integer siteId) throws Exception {
		searchengineService.reindexSite(siteId);
	}

	@Override
	protected void handleAddAddress2Department(long departmentId, long addressId) throws Exception {
		getComponentsServiceSpring().addAddress2Department(Long.valueOf(departmentId), Long.valueOf(addressId));
	}

	@Override
	protected void handleAddAddress2Person(long personId, long addressId) throws Exception {
		getComponentsServiceSpring().addAddress2Person(Long.valueOf(personId), Long.valueOf(addressId));
	}

	@Override
	protected void handleAddAddress2Unit(int unitId, long addressId) throws Exception {
		getComponentsServiceSpring().addAddress2Unit(Integer.valueOf(unitId), Long.valueOf(addressId));
	}

	@Override
	protected long handleAddTalktime2Department(long departmentId, String talkTimeType, String talkTimes) throws Exception {
		return getComponentsServiceSpring().addTalktime2Department(Long.valueOf(departmentId), talkTimeType, talkTimes).longValue();
	}

	@Override
	protected long handleAddTalktime2Person(long personId, String talkTimeType, String talkTimes) throws Exception {
		return getComponentsServiceSpring().addTalktime2Person(Long.valueOf(personId), talkTimeType, talkTimes).longValue();
	}

	@Override
	protected long handleAddTalktime2Unit(int unitId, String talkTimeType, String talkTimes) throws Exception {
		return getComponentsServiceSpring().addTalktime2Unit(Integer.valueOf(unitId), talkTimeType, talkTimes).longValue();
	}

	@Override
	protected void handleAddUser2Unit(UserValue user, UnitValue unit) throws Exception {
		getUnitServiceSpring().addUser2Unit(unit, user.getUserName());
	}

	@Override
	protected void handleAddUserToGroup(GroupValue groupValue, String userName) throws Exception {
		groupValue.setRoles(new RoleValue[0]);
		getUserServiceSpring().addUserToGroup(groupValue, userName);
	}

	@Override
	protected void handleAddViewComponentsToTask(int taskId, Integer[] vcIds) throws Exception {
		getUserServiceSpring().addViewComponentsToTask(Integer.valueOf(taskId), vcIds);
	}

	@Override
	protected void handleCancelApproval(int viewComponentId) throws Exception {
		getViewServiceSpring().cancelApproval(Integer.valueOf(viewComponentId));
	}

	@Override
	protected void handleCheckIn(ContentValue contentDao) throws Exception {
		getContentServiceSpring().checkIn(contentDao);
	}

	@Override
	protected ContentValue handleCheckOut(int contentId, boolean force) throws Exception {
		try {
			ContentValue cdao = getContentServiceSpring().checkOut(contentId, force);
			// will be only called, if the content was been successfully
			// checkedOut!
			return cdao;
		} catch (Exception exception) {
			if (exception instanceof AlreadyCheckedOutException) { throw (AlreadyCheckedOutException) exception; }
			UserException ue = new UserException("Error checking out: " + exception.getMessage());
			log.error("Error checking out ", exception);
			throw ue;
		}
	}

	@Override
	protected ContentValue handleCreateContent(ContentValue dao) throws Exception {
		return getContentServiceSpring().createContent(dao);
	}

	@Override
	protected GroupValue handleCreateGroup(String groupName) throws Exception {
		return getUserServiceSpring().createGroup(groupName);
	}

	@Override
	protected int handleCreateTask(String receiverId, String receiverRole, int unitId, String comment, byte taskType) throws Exception {
		return getUserServiceSpring().createTask(receiverId, receiverRole, Integer.valueOf(unitId), comment, taskType).intValue();
	}

	@Override
	protected Integer handleCreateUnit(String unitName) throws Exception {
		return getUnitServiceSpring().createUnit(unitName);
	}

	@Override
	protected void handleCreateUser(String userName, String passwd, String firstName, String lastName, String email, Integer initialUnitId) throws Exception {
		getUserServiceSpring().createUser(userName, passwd, firstName, lastName, email, initialUnitId);
	}

	@Override
	protected ViewDocumentValue handleCreateViewDocument(ViewDocumentValue dao) throws Exception {
		return getViewServiceSpring().createViewDocument(dao);
	}

	@Override
	protected void handleDeleteUser(String userName) throws Exception {
		getUserServiceSpring().deleteUser(userName);
	}

	@Override
	protected ContentVersionValue[] handleGetAllContentVersions(int contentId) throws Exception {
		return getContentServiceSpring().getAllContentVersions(Integer.valueOf(contentId));
	}

	@Override
	protected Integer[] handleGetAllDocuments4Unit(int unitId) throws Exception {
		return getContentServiceSpring().getAllDocuments4Unit(Integer.valueOf(unitId));
	}

	@Override
	protected GroupValue[] handleGetAllGroups() throws Exception {
		GroupValue[] gv = new GroupValue[0];
		try {
			gv = getUserServiceSpring().getAllGroups();
		} catch (Exception exe) {
			log.error(exe);
		}
		return gv;
	}

	@Override
	protected GroupValue[] handleGetAllGroupsUsedInUnit(int unitId) throws Exception {
		GroupValue[] gv = new GroupValue[0];
		try {
			gv = getUserServiceSpring().getAllGroupsUsedInUnit(Integer.valueOf(unitId));
		} catch (Exception exe) {
			log.error(exe);
		}
		return gv;
	}

	@Override
	protected Integer[] handleGetAllPictures4Unit(int unitId) throws Exception {
		return getContentServiceSpring().getAllPictures4Unit(Integer.valueOf(unitId));
	}

	@Override
	protected RoleValue[] handleGetAllRoles() throws Exception {
		try {
			return getUserServiceSpring().getAllRoles();
		} catch (Exception exe) {
			log.error(exe);
		}
		return new RoleValue[0];
	}

	@Override
	protected DocumentSlimValue[] handleGetAllSlimDocumentValues(int unitId) throws Exception {
		return getContentServiceSpring().getAllSlimDocuments4Unit(unitId);
	}

	@Override
	protected PictureSlimstValue[] handleGetAllSlimPictures4Unit(int unitId) throws Exception {
		return getContentServiceSpring().getAllSlimPictures4Unit(unitId);
	}

	@Override
	protected TaskValue[] handleGetAllTasks() throws Exception {
		TaskValue[] tdao = getUserServiceSpring().getAllTasks();
		return tdao;
	}

	/**
	 * 
	 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @version $Id$
	 */
	private class TaskValueComparer implements Comparer {
		public int compare(Object objA, Object objB) {
			TaskValue uda = (TaskValue) objA;
			TaskValue ud = (TaskValue) objB;
			return new Long(uda.getCreationDate()).compareTo(new Long(ud.getCreationDate()));
		}
	}

	@Override
	protected UnitValue[] handleGetAllUnits() throws Exception {
		return getUnitServiceSpring().getAllUnits();
	}

	@Override
	protected UserValue[] handleGetAllUser() throws Exception {
		return getUserServiceSpring().getAllUser();
	}

	@Override
	protected UserValue[] handleGetAllUser(int groupId) throws Exception {
		return getUserServiceSpring().getAllUser4Group(groupId);
	}

	@Override
	protected UserValue[] handleGetAllUser4GroupAndUnit(int groupId, int unitId) throws Exception {
		return getUserServiceSpring().getAllUser4GroupAndUnit(Integer.valueOf(groupId), Integer.valueOf(unitId));
	}

	@Override
	protected UserValue[] handleGetAllUserForUnit(int unitId) throws Exception {
		return getUserServiceSpring().getAllUser4Unit(Integer.valueOf(unitId));
	}

	@Override
	protected ContentValue handleGetContent(Integer contentId) throws Exception {
		ContentValue current = getContentServiceSpring().getContent(contentId);
		return current;
	}

	@Override
	protected String handleGetContentTemplateName(int contentId) throws Exception {
		return getContentServiceSpring().getContentTemplateName(Integer.valueOf(contentId));
	}

	@Override
	protected ContentVersionValue handleGetContentVersion(int contentVersionId) throws Exception {
		try {
			return getContentServiceSpring().getContentVersion(Integer.valueOf(contentVersionId));
		} catch (Exception exe) {
			log.error("Error getting contentversion", exe);
			return null;
		}
	}

	@Override
	protected String handleGetDocumentName(int documentId) throws Exception {
		return getContentServiceSpring().getDocumentName(Integer.valueOf(documentId));
	}

	@Override
	protected GroupValue[] handleGetGroups4User(String userName) throws Exception {
		UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
		Integer siteId = user.getActiveSite().getSiteId();
		GroupValue[] gv = getUserServiceSpring().getGroups4UserInSite(userName, siteId);
		if (gv == null) gv = new GroupValue[0];
		return gv;
	}

	@Override
	protected UnitValue[] handleGetNotReferencedUnits(ViewDocumentValue viewDocument) throws Exception {
		return getContentServiceSpring().getNotReferencedUnits(viewDocument);
	}

	@Override
	protected String[] handleGetParents4ViewComponent(int viewComponentId) throws Exception {
		return getViewServiceSpring().getParents4ViewComponent(Integer.valueOf(viewComponentId));
	}

	@Override
	protected String handleGetPathForViewComponentId(int vcId) throws Exception {
		try {
			return getViewServiceSpring().getPathForViewComponentId(Integer.valueOf(vcId));
		} catch (Exception exe) {
			return "";
		}
	}

	@Override
	protected PictureSlimValue handleGetPicture(int pictureId) throws Exception {
		return getContentServiceSpring().getPicture(Integer.valueOf(pictureId));
	}

	@Override
	protected String handleGetPictureAltText(int pictureId) throws Exception {
		return getContentServiceSpring().getPictureAltText(Integer.valueOf(pictureId));
	}

	@Override
	protected byte[] handleGetPictureData(int pictureId) throws Exception {
		return getContentServiceSpring().getPictureData(Integer.valueOf(pictureId));
	}

	@Override
	protected String handleGetPictureFileName(int pictureId) throws Exception {
		return getContentServiceSpring().getPictureFileName(Integer.valueOf(pictureId));
	}

	@Override
	protected SiteValue[] handleGetSites() throws Exception {
		AuthorizationServiceSpring as = getAuthorizationServiceSpring();
		return as.getSites();
	}

	@Override
	protected UnitValue handleGetUnit(Integer unitId) throws Exception {
		return getComponentsServiceSpring().getUnit(Integer.valueOf(unitId));
	}

	@Override
	protected int handleGetUnit4ViewComponent(int viewComponentId) throws Exception {
		return getViewServiceSpring().getUnit4ViewComponent(Integer.valueOf(viewComponentId)).intValue();
	}

	@Override
	protected UnitValue[] handleGetUnits() throws Exception {
		return getAuthorizationServiceSpring().getUnits();
	}

	@Override
	protected ViewComponentValue handleGetViewComponent(int viewComponentId) throws Exception {
		return getViewServiceSpring().getViewComponent(Integer.valueOf(viewComponentId));
	}

	@Override
	protected ViewComponentValue handleGetViewComponentWithDepth(int viewComponentId, int depth) throws Exception {
		return getViewServiceSpring().getViewComponentWithDepth(Integer.valueOf(viewComponentId), depth);
	}

	@Override
	protected ViewComponentValue handleGetViewComponent4UnitWithDepth(int unitId, int depth, Integer viewDocumentId) throws Exception {
		Integer viewComponentId = getViewComponent4Unit(Integer.valueOf(unitId), Integer.valueOf(viewDocumentId)).getViewComponentId();
		return getViewComponentWithDepth(viewComponentId, depth);
	}

	@Override
	protected ViewComponentValue[] handleGetViewComponentsWithReferenceToViewComponentId(int viewComponentId) throws Exception {
		return getViewServiceSpring().getViewComponentsWithReferenceToViewComponentId(Integer.valueOf(viewComponentId));
	}

	@Override
	protected ViewDocumentValue handleGetViewDocument(String viewType, String language) throws Exception {
		return getViewServiceSpring().getViewDocument4ViewTypeAndLanguage(viewType, language);
	}

	@Override
	protected ViewDocumentValue[] handleGetViewDocuments() throws Exception {
		return getViewServiceSpring().getViewDocuments();
	}

	@Override
	protected boolean handleIsNewTask4User() throws Exception {
		UserServiceSpring us = getUserServiceSpring();
		return us.isNewTask4User().booleanValue();
	}

	@Override
	protected boolean handleIsUnitAndChangesParentUnitLeft(int viewComponentId) throws Exception {
		return getViewServiceSpring().isUnitAndChangesParentUnitLeft(Integer.valueOf(viewComponentId)).booleanValue();
	}

	@Override
	protected boolean handleIsUnitAndChangesParentUnitRight(int viewComponentId) throws Exception {
		return getViewServiceSpring().isUnitAndChangesParentUnitRight(Integer.valueOf(viewComponentId)).booleanValue();
	}

	@Override
	protected boolean handleIsUserInRole(UserValue uv, String role) throws Exception {
		if (uv == null) { return false; }
		if (uv.isMasterRoot()) { return true; }
		try {
			GroupValue[] gv = getUserServiceSpring().getGroups4User(uv.getUserName());
			if (gv == null) return false;
			for (int i = 0; i < gv.length; i++) {
				for (int j = 0; j < gv[i].getRoles().length; j++) {
					if (gv[i].getRoles()[j].getRoleId().equalsIgnoreCase(role)) { return true; }
				}
			}
		} catch (Exception exe) {
			log.error(exe);
		}
		return false;
	}

	@Override
	protected UserLoginValue handleLogin(String userName, String passwd, Integer siteId) throws Exception {
		return getAuthorizationServiceSpring().login(userName, passwd, siteId);
	}

	@Override
	protected ViewComponentValue handleMoveViewComponentDown(int viewComponentId) throws Exception {
		return getViewServiceSpring().moveViewComponentDown(Integer.valueOf(viewComponentId));
	}

	@Override
	protected ViewComponentValue handleMoveViewComponentLeft(int viewComponentId) throws Exception {
		return getViewServiceSpring().moveViewComponentLeft(Integer.valueOf(viewComponentId));
	}

	@Override
	protected ViewComponentValue handleMoveViewComponentRight(int viewComponentId) throws Exception {
		return getViewServiceSpring().moveViewComponentRight(Integer.valueOf(viewComponentId));
	}

	@Override
	protected ViewComponentValue handleMoveViewComponentUp(int viewComponentId) throws Exception {
		return getViewServiceSpring().moveViewComponentUp(Integer.valueOf(viewComponentId));
	}

	@Override
	protected void handleRemoveAddress(long addressId) throws Exception {
		getComponentsServiceSpring().removeAddress(Long.valueOf(addressId));
	}

	@Override
	protected void handleRemoveAllOldContentVersions(int contentId) throws Exception {
		try {
			getContentServiceSpring().removeAllOldContentVersions(Integer.valueOf(contentId));
		} catch (Exception exe) {
			log.error("Error removing all old contentversions", exe);
		}
	}

	@Override
	protected void handleRemoveContentVersion(int contentVersionId) throws Exception {
		try {
			getContentServiceSpring().removeContentVersion(Integer.valueOf(contentVersionId));
		} catch (Exception exe) {
			log.error("Error removing content version", exe);
		}
	}

	@Override
	protected void handleRemoveDepartment(long departmentId) throws Exception {
		getComponentsServiceSpring().removeDepartment(Long.valueOf(departmentId));
	}

	@Override
	protected void handleRemoveDocument(int documentId) throws Exception {
		getContentServiceSpring().removeDocument(Integer.valueOf(documentId));
	}

	@Override
	protected void handleRemoveGroup(int groupId) throws Exception {
		getUserServiceSpring().removeGroup(Integer.valueOf(groupId));
	}

	@Override
	protected void handleRemovePerson(long personId) throws Exception {
		getComponentsServiceSpring().removePerson(Long.valueOf(personId));
	}

	@Override
	protected void handleRemoveTalktime(long talktimeId) throws Exception {
		getComponentsServiceSpring().removeTalktime(Long.valueOf(talktimeId));
	}

	@Override
	protected void handleRemoveTask(Integer taskId) throws Exception {
		getUserServiceSpring().removeTask(taskId);
	}

	@Override
	protected void handleRemoveUnit(UnitValue unitDao) throws Exception {
		getUnitServiceSpring().removeUnit(unitDao);
	}

	@Override
	protected void handleRemoveUnit(int unitId) throws Exception {
		getComponentsServiceSpring().removeUnit(unitId);
	}

	@Override
	protected void handleRemoveUserFromGroup(GroupValue gv, String userName) throws Exception {
		gv.setRoles(new RoleValue[0]);
		getUserServiceSpring().removeUserFromGroup(gv, userName);
	}

	@Override
	protected void handleRemoveUserFromUnit(UnitValue unit, String userName) throws Exception {
		getUnitServiceSpring().removeUserFromUnit(unit, userName);
	}

	@Override
	protected void handleRemoveViewComponent(Integer viewComponentId, boolean force) throws Exception {
		getViewServiceSpring().removeViewComponent(viewComponentId, force);

	}

	@Override
	protected void handleRemoveViewComponentsFromTask(int taskId, Integer[] vcIds) throws Exception {
		getUserServiceSpring().removeViewComponentsFromTask(Integer.valueOf(taskId), vcIds);
	}

	@Override
	protected void handleRemoveViewDocument(int viewDocumentId) throws Exception {
		getViewServiceSpring().removeViewDocument(Integer.valueOf(viewDocumentId));
	}

	@Override
	protected void handleSaveContent(int contentId, String content) throws Exception {
		getContentServiceSpring().saveContent(Integer.valueOf(contentId), content);
	}

	@Override
	protected void handleSetDefaultViewDocument(int viewDocumentId) throws Exception {
		if (viewDocumentId > 0) {
			getViewServiceSpring().setDefaultViewDocument(Integer.valueOf(viewDocumentId));
		}
	}

	@Override
	protected void handleSetTaskViewed(Integer taskId) throws Exception {
		getUserServiceSpring().setTaskViewed(taskId);
	}

	@Override
	protected void handleSetUnit4ViewComponent(int unitId, ViewDocumentValue viewDocumentDao, int viewComponentId) throws Exception {
		getViewServiceSpring().setUnit4ViewComponent(Integer.valueOf(unitId), viewDocumentDao, Integer.valueOf(viewComponentId));
	}

	@Override
	protected void handleUpdateAddress(AddressValue addressDao) throws Exception {
		getComponentsServiceSpring().updateAddressData(addressDao);
	}

	@Override
	protected void handleUpdateDepartment(DepartmentValue departmentDao) throws Exception {
		getComponentsServiceSpring().updateDepartment(departmentDao);
	}

	@Override
	protected void handleUpdateGroup(GroupValue gv) throws Exception {
		getUserServiceSpring().updateGroup(gv);
	}

	@Override
	protected void handleUpdatePerson(PersonValue personDao) throws Exception {
		getComponentsServiceSpring().updatePerson(personDao);
	}

	@Override
	protected void handleUpdatePictureAltText(int pictureId, String altText) throws Exception {
		getContentServiceSpring().updatePictureAltText(Integer.valueOf(pictureId), altText);
	}

	@Override
	protected void handleUpdateStatus4ViewComponent(ViewComponentValue vcDao) throws Exception {
		getViewServiceSpring().updateStatus4ViewComponent(vcDao);
	}

	@Override
	protected void handleUpdateTalktime(TalktimeValue talktimeDao) throws Exception {
		getComponentsServiceSpring().updateTalktime(talktimeDao);
	}

	@Override
	protected void handleUpdateTemplate(int viewComponentId, String templateName) throws Exception {
		getContentServiceSpring().updateTemplate(Integer.valueOf(viewComponentId), templateName);
	}

	@Override
	protected void handleUpdateUnit(UnitValue dao) throws Exception {
		getUnitServiceSpring().updateUnit(dao);
	}

	@Override
	protected void handleUpdateUser(UserValue uv) throws Exception {
		getUserServiceSpring().updateUser(uv);
	}

	@Override
	protected void handleCheckIn4ContentId(Integer contentId) throws Exception {
		getContentServiceSpring().checkIn4ContentId(contentId);
	}

	@Override
	protected void handleLogout() throws Exception {
		getAuthorizationServiceSpring().logout();
	}

	@Override
	protected GroupValue[] handleGetGroups() throws Exception {
		return getAuthorizationServiceSpring().getGroups();
	}

	@Override
	protected void handleChangePassword4User(String userName, String passwdOld, String passwdNew) throws Exception {
		getUserServiceSpring().changePassword4User(userName, passwdNew, passwdNew);
	}

	@Override
	protected ViewComponentValue handleGetViewComponent4Unit(int unitId, Integer viewDocumentId) throws Exception {
		return getViewServiceSpring().getViewComponent4Unit(Integer.valueOf(unitId), viewDocumentId);
	}

	@Override
	protected Integer handleAddJaasRealmToSite(Integer siteId, RealmJaasValue jaasRealmValue) throws Exception {
		Integer pk = new Integer(-1);
		try {
			pk = getSafeguardServiceSpring().addJaasRealmToSite(siteId, jaasRealmValue);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return pk;
	}

	@Override
	protected void handleAddJaasRealmToVC(Integer viewComponentId, Integer jaasRealmId, String neededRole, Integer loginPageId) throws Exception {
		try {
			if (loginPageId == null) loginPageId = new Integer(-1);
			getSafeguardServiceSpring().assignJaasRealmToViewComponent(jaasRealmId, viewComponentId, neededRole, loginPageId);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@Override
	protected Integer handleAddLdapRealmToSite(Integer siteId, RealmLdapValue ldapRealmValue) throws Exception {
		Integer pk = new Integer(-1);
		try {
			pk = getSafeguardServiceSpring().addLdapRealmToSite(siteId, ldapRealmValue);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
		return pk;
	}

	@Override
	protected void handleAddLdapRealmToVC(Integer viewComponentId, Integer ldapRealmId, String neededRole, Integer loginPageId) throws Exception {
		try {
			if (loginPageId == null) loginPageId = new Integer(-1);
			getSafeguardServiceSpring().assignLdapRealmToViewComponent(ldapRealmId, viewComponentId, neededRole, loginPageId);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@Override
	protected int handleAddPicture2Unit(int unitId, byte[] thumbnail, byte[] picture, String mimeType, String altText, String pictureName) throws Exception {
		return getContentServiceSpring().addPicture2Unit(Integer.valueOf(unitId), thumbnail, picture, mimeType, altText, pictureName).intValue();
	}

	@Override
	protected int handleAddPictureWithPreview2Unit(int unitId, byte[] thumbnail, byte[] picture, byte[] preview, String mimeType, String altText, String pictureName) throws Exception {
		return getContentServiceSpring().addPictureWithPreview2Unit(Integer.valueOf(unitId), thumbnail, picture, preview, mimeType, altText, pictureName).intValue();
	}

	@Override
	protected void handleAddSimplePwRealmToVC(Integer simplePwRealmId, Integer viewComponentId, String neededRole, Integer loginPageId) throws Exception {
		if (loginPageId == null) loginPageId = new Integer(-1);
		getSafeguardServiceSpring().assignSimplePwRealmToViewComponent(simplePwRealmId, viewComponentId, neededRole, loginPageId);
	}

	@Override
	protected int handleAddSimpleRealmToSite(String realmName, int siteId, String loginPageId) throws Exception {
		int pk = -1;

		try {
			pk = getSafeguardServiceSpring().addSimpleRealmToSite(realmName, Integer.valueOf(siteId), loginPageId).intValue();
		} catch (AlreadyExistsException e) {
			log.error(e.getMessage());
		}

		return pk;
	}

	@Override
	protected Integer handleAddSqlDbRealmToSite(Integer siteId, RealmJdbcValue jdbcRealmValue) throws Exception {
		Integer ret = new Integer(-1);
		try {
			ret = getSafeguardServiceSpring().addJdbcRealmToSite(siteId, jdbcRealmValue);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}
		return ret;
	}

	@Override
	protected void handleAddSqlDbRealmToVC(Integer jdbcRealmId, Integer viewComponentId, String neededRole, Integer loginPageId) throws Exception {
		if (loginPageId == null) loginPageId = new Integer(-1);
		getSafeguardServiceSpring().assignJdbcRealmToViewComponent(jdbcRealmId, viewComponentId, neededRole, loginPageId);
	}

	@Override
	protected int handleAddUserToSimpleRealm(Integer simplePwRealmId, RealmSimplePwUserValue simplePwRealmUserValue) throws Exception {
		int pk = -1;

		try {
			pk = getSafeguardServiceSpring().addUserToSimpleRealm(simplePwRealmId, simplePwRealmUserValue).intValue();
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}

		return pk;
	}

	@Override
	protected long handleCreateAddress(AddressValue addressValue) throws Exception {
		return getComponentsServiceSpring().createAddress(addressValue).longValue();
	}

	@Override
	protected long handleCreateDepartment(String name, int unitId) throws Exception {
		return getComponentsServiceSpring().createDepartment(name, Integer.valueOf(unitId)).longValue();
	}

	@Override
	protected void handleCreateEdition(String comment, int rootViewComponentId, boolean deploy, boolean showMessage) throws Exception {
		try {
			getContentServiceSpring().createEdition(comment, Integer.valueOf(rootViewComponentId), deploy, showMessage);
		} catch (UserException ue) {
			log.error("Error creating edition", ue);
			throw ue;
		}
	}

	@Override
	protected HostValue handleCreateHost(String hostName) throws Exception {
		return (getAdministrationServiceSpring().createHost(hostName));
	}

	@Override
	protected long handleCreatePerson(PersonValue personValue) throws Exception {
		return getComponentsServiceSpring().createPerson(personValue).longValue();
	}

	@Override
	protected ShortLinkValue handleCreateShortLink(ShortLinkValue shortLinkValue) throws Exception {
		try {
			shortLinkValue = getMasterRootServiceSpring().createShortLink(shortLinkValue);
		} catch (Exception e) {
			log.error("Error creating shortLinkValue " + shortLinkValue.getShortLink() + ": " + e.getMessage(), e);
		}
		return shortLinkValue;
	}

	@Override
	protected SiteValue handleCreateSite(SiteValue siteValue) throws Exception {
		try {
			return getMasterRootServiceSpring().createSite(siteValue);
		} catch (Exception exe) {
			log.error("Error creating site", exe);
			return null;
		}
	}

	@Override
	protected SiteGroupValue handleCreateSiteGroup(SiteGroupValue value) throws Exception {
		try {
			return this.getMasterRootServiceSpring().createSiteGroup(value);
		} catch (Exception e) {
			log.error("Error creating SiteGroups: " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected void handleDeleteJaasRealm(Integer jaasRealmId) throws Exception {
		try {
			getSafeguardServiceSpring().deleteJaasRealm(jaasRealmId);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@Override
	protected void handleDeleteLdapRealm(Integer ldapRealmId) throws Exception {
		try {
			getSafeguardServiceSpring().deleteLdapRealm(ldapRealmId);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@Override
	protected boolean handleDeleteRealmAtVC(Integer viewComponentId) throws Exception {
		boolean ret = false;
		try {
			ret = getSafeguardServiceSpring().removeRealmFromViewComponent(viewComponentId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}
		return ret;
	}

	@Override
	protected void handleDeleteShortLink(ShortLinkValue shortLinkValue) throws Exception {
		try {
			getMasterRootServiceSpring().deleteShortLink(shortLinkValue);
		} catch (Exception e) {
			log.error("Error deleting shortLinkValue " + shortLinkValue.getShortLink() + ": " + e.getMessage(), e);
		}
	}

	@Override
	protected boolean handleDeleteSimplePwRealmUser(Integer simplePwRealmUserId) throws Exception {
		boolean del = false;
		try {
			del = getSafeguardServiceSpring().deleteSimplePwRealmUser(simplePwRealmUserId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}

		return del;
	}

	@Override
	protected boolean handleDeleteSqlDbRealm(Integer jdbcRealmId) throws Exception {
		boolean del = false;
		try {
			del = getSafeguardServiceSpring().deleteJdbcRealm(jdbcRealmId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}
		return del;
	}

	@Override
	protected void handleEditJaasRealm(RealmJaasValue jaasRealmValue) throws Exception {
		try {
			getSafeguardServiceSpring().editJaasRealm(jaasRealmValue);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@Override
	protected void handleEditLdapRealm(RealmLdapValue ldapRealmValue) throws Exception {
		try {
			getSafeguardServiceSpring().editLdapRealm(ldapRealmValue);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@Override
	protected void handleEditSimplePwRealm(RealmSimplePwValue simplePwRealmValue) throws Exception {
		getSafeguardServiceSpring().editSimplePwRealm(simplePwRealmValue);
	}

	@Override
	protected void handleEditSqlDbRealm(RealmJdbcValue jdbcRealmValue) throws Exception {
		try {
			getSafeguardServiceSpring().editJdbcRealm(jdbcRealmValue);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@Override
	protected InputStream handleExportXlsPersonData() throws Exception {
		try {
			log.info("exportXlsPersonData");
			return getAdministrationServiceSpring().exportXlsPersonData();
		} catch (Exception exe) {
			log.error("Error exporting PersonData", exe);
		}
		return null;
	}

	@Override
	protected ActiveRealmValue handleGetActiveRealm(Integer viewComponentId) throws Exception {
		ActiveRealmValue val = new ActiveRealmValue(false, false, false, false, false, -1, "", "", null);

		try {
			val = getSafeguardServiceSpring().getActiveRealm(viewComponentId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}

		return val;
	}

	@Override
	protected AddressValue handleGetAddress(long addressId) throws Exception {
		return getComponentsServiceSpring().getAddress(Long.valueOf(addressId));
	}

	@Override
	protected ViewIdAndInfoTextValue[] handleGetAllChildrenNamesWithUnit(int viewComponentId) throws Exception {
		ViewIdAndInfoTextValue[] retarr = null;
		try {
			retarr = getViewServiceSpring().getAllChildrenNamesWithUnit(Integer.valueOf(viewComponentId));
		} catch (Exception exe) {
			log.error("Error getallchildrennameswithunit", exe);
		}
		return retarr;
	}

	@Override
	protected HostValue[] handleGetAllHosts() throws Exception {
		HostValue[] hv = null;
		try {
			hv = getAdministrationServiceSpring().getAllHosts();
			if (hv == null) hv = new HostValue[0];
		} catch (UserException e) {
			log.error(e.getMessage());
		}
		return (hv);
	}

	@Override
	protected SiteValue[] handleGetAllNotAssignedSites() throws Exception {
		try {
			return this.getMasterRootServiceSpring().getAllNotAssignedSites2SiteGroups();
		} catch (Exception e) {
			log.error("Error getting all not assigned to a SiteGroup Sites: " + e.getMessage(), e);
		}
		return new SiteValue[0];
	}

	@Override
	protected SiteValue[] handleGetAllRelatedSites(int siteId) throws Exception {
		try {
			return this.getViewServiceSpring().getAllRelatedSites(Integer.valueOf(siteId));
		} catch (Exception e) {
			log.error("Error getting all related Sites for Site " + siteId + ": " + e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected ShortLinkValue[] handleGetAllShortLinks4Site(Integer siteId) throws Exception {
		try {
			return getMasterRootServiceSpring().getAllShortLinks4Site(siteId);
		} catch (Exception e) {
			log.error("Error getting all shortLinks for site " + siteId + ": " + e.getMessage(), e);
		}
		return new ShortLinkValue[0];
	}

	@Override
	protected SiteGroupValue[] handleGetAllSiteGroups() throws Exception {
		try {
			return this.getMasterRootServiceSpring().getAllSiteGroups();
		} catch (Exception e) {
			log.error("Error loading all SiteGroups: " + e.getMessage(), e);
		}
		return new SiteGroupValue[0];
	}

	@Override
	protected SiteValue[] handleGetAllSites() throws Exception {
		try {
			return getMasterRootServiceSpring().getAllSites();
		} catch (Exception exe) {
			log.error("Error get all sites", exe);
			return new SiteValue[0];
		}
	}

	@Override
	protected SiteValue[] handleGetAllSites4CurrentUser() throws Exception {
		try {
			return getUserServiceSpring().getAllSites4CurrentUser();
		} catch (Exception exe) {
			log.error("Error get all sites for current user", exe);
			return new SiteValue[0];
		}
	}

	@Override
	protected HostValue[] handleGetAllUnassignedHosts() throws Exception {
		HostValue[] hv = null;
		try {
			hv = getAdministrationServiceSpring().getAllUnassignedHosts();
			if (hv == null) hv = new HostValue[0];
		} catch (UserException e) {
			log.error(e.getMessage());
		}
		return (hv);
	}

	@Override
	protected UnitValue[] handleGetAllUnits4Site(Integer siteId) throws Exception {
		try {
			return getMasterRootServiceSpring().getAllUnits4Site(siteId);
		} catch (Exception e) {
			log.error("Error getting all units for site " + siteId + ": " + e.getMessage(), e);
		}
		return new UnitValue[0];
	}

	@Override
	protected UserValue[] handleGetAllUserOwnSites() throws Exception {
		try {
			return getUserServiceSpring().getAllUsers4OwnSites();
		} catch (Exception exe) {
			log.error("Error getAllUsers4OwnSites", exe);
			return new UserValue[0];
		}
	}

	@Override
	protected UserValue[] handleGetAllUsersForAllSites() throws Exception {
		try {
			return getMasterRootServiceSpring().getAllUserForAllSites();
		} catch (Exception exe) {
			log.error("Error getAllUsersForAllSites", exe);
			return new UserValue[0];
		}
	}

	@Override
	protected ViewComponentValue[] handleGetAllViewComponents4Status(Integer viewDocumentId, int status) throws Exception {
		return getViewServiceSpring().getAllViewComponents4Status(viewDocumentId, status);
	}

	/**
	 * @deprecated This was a migration method for migrating to new Axis model
	 */
	@Deprecated
	private Vector arrayToVector(Object[] objarr) {
		if (objarr != null) {
			Vector<Object> vec = new Vector<Object>(objarr.length);
			for (int i = 0; i < objarr.length; i++) {
				vec.add(objarr[i]);
			}
			return vec;
		}
		return new Vector();
	}

	@Override
	protected ViewIdAndUnitIdValue[] handleGetAllViewComponentsWithUnits(int viewComponentId) throws Exception {
		ViewIdAndUnitIdValue[] retVal = null;
		try {
			retVal = getViewServiceSpring().getAllViewComponentsWithUnits(Integer.valueOf(viewComponentId));
		} catch (Exception exe) {
			log.error("Error getting all vc with units", exe);
		}
		if (retVal == null) retVal = new ViewIdAndUnitIdValue[0];
		return retVal;
	}

	@Override
	protected ViewDocumentValue[] handleGetAllViewDocuments4Site(int siteId) throws Exception {
		try {
			return this.getViewServiceSpring().getViewDocuments4Site(Integer.valueOf(siteId));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected String[] handleGetAnchors(int contentId) throws Exception {
		String[] retArr = null;
		try {
			retArr = getContentServiceSpring().getAnchors(Integer.valueOf(contentId));
		} catch (Exception exe) {
			log.error("Error getting anchors", exe);
		}
		return retArr;
	}

	@Override
	protected String[] handleGetConnectedUsersForSite(int siteId) throws Exception {
		try {
			return getUserServiceSpring().getConnectedUsers4Site(Integer.valueOf(siteId));
		} catch (Exception exe) {
			log.error("Error getConnectedUsersForSite", exe);
			return new String[0];
		}
	}

	@Override
	protected DepartmentValue handleGetDepartment(long departmentId) throws Exception {
		return getComponentsServiceSpring().getDepartment(Long.valueOf(departmentId));
	}

	@Override
	protected DepartmentValue[] handleGetDepartments4Name(String name) throws Exception {
		return getComponentsServiceSpring().getDepartments4Name(name);
	}

	@Override
	protected Integer handleGetFirstProtectedParentId(Integer viewComponentId) throws Exception {
		try {
			return getSafeguardServiceSpring().getFirstProtectedParentId(viewComponentId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected HostValue[] handleGetHosts() throws Exception {
		HostValue[] hv = null;
		try {
			hv = getAdministrationServiceSpring().getHosts();
			if (hv == null) hv = new HostValue[0];
		} catch (UserException e) {
			log.error(e.getMessage());
		}
		return hv;
	}

	@Override
	protected HostValue[] handleGetHostsForSite(int siteId) throws Exception {
		HostValue[] hv = null;
		try {
			hv = getAdministrationServiceSpring().getHostsForSite(Integer.valueOf(siteId));
			if (hv == null) hv = new HostValue[0];
		} catch (UserException e) {
			log.error(e.getMessage());
		}
		return hv;
	}

	@Override
	protected RealmJaasValue[] handleGetJaasRealmsForSite(Integer siteId) throws Exception {
		RealmJaasValue[] val = null;
		try {
			val = getSafeguardServiceSpring().getJaasRealmsForSite(siteId);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
		return val;
	}

	@Override
	protected RealmLdapValue[] handleGetLdapRealmsForSite(Integer siteId) throws Exception {
		RealmLdapValue[] val = null;
		try {
			val = getSafeguardServiceSpring().getLdapRealmsForSite(siteId);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
		return val;
	}

	@Override
	protected PersonValue handleGetPerson(long personId) throws Exception {
		return getComponentsServiceSpring().getPerson(Long.valueOf(personId));
	}

	@Override
	protected PersonValue[] handleGetPerson4Name(String firstName, String lastName) throws Exception {
		return getComponentsServiceSpring().getPerson4Name(firstName, lastName);
	}

	@Override
	protected RealmSimplePwValue[] handleGetSimplePwRealmsForSite(Integer siteId) throws Exception {
		RealmSimplePwValue[] val = null;
		try {
			val = getSafeguardServiceSpring().getSimplePwRealmsForSite(siteId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}
		if (val == null) {
			val = new RealmSimplePwValue[0];
		}
		return val;
	}

	@Override
	protected RealmSimplePwValue[] handleGetSimplePwRealmsForUser(String user) throws Exception {
		RealmSimplePwValue[] val = null;
		val = getSafeguardServiceSpring().getSimplePwRealmsForUser(user);
		if (val == null) {
			val = new RealmSimplePwValue[0];
		}
		return val;
	}

	@Override
	protected String handleGetSite(String hostName) throws Exception {
		String result = "";
		try {
			result = getAdministrationServiceSpring().getSite4Host(hostName);
		} catch (UserException e) {
			log.error(e.getMessage());
		}
		return (result);
	}

	@Override
	protected String handleGetSiteConfig(int siteId) throws Exception {
		try {
			return getMasterRootServiceSpring().getSiteConfig(Integer.valueOf(siteId));
		} catch (Exception exe) {
			log.error("Error getsiteconfig", exe);
			return "";
		}
	}

	@Override
	protected SiteValue handleGetSiteForName(String siteName) throws Exception {
		SiteValue sv = null;
		try {
			sv = getAdministrationServiceSpring().getSiteForName(siteName);
		} catch (UserException e) {
			log.error(e.getMessage());
		}
		return (sv);
	}

	@Override
	protected SiteValue[] handleGetSites4SiteGroup(SiteGroupValue siteGroupValue) throws Exception {
		try {
			return this.getMasterRootServiceSpring().getSites4SiteGroup(siteGroupValue.getSiteGroupId());
		} catch (Exception e) {
			log.error("Error getting all Sites for SiteGroup " + siteGroupValue.getSiteGroupId() + ": " + e.getMessage(), e);
		}
		return new SiteValue[0];
	}

	@Override
	protected RealmJdbcValue[] handleGetSqlDbRealmsForSite(Integer siteId) throws Exception {
		RealmJdbcValue[] val = null;
		try {
			val = getSafeguardServiceSpring().getJdbcRealmsForSite(siteId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}
		return val;
	}

	@Override
	protected String handleGetStartPage(String hostName) throws Exception {
		String result = "";
		try {
			result = getAdministrationServiceSpring().getStartPage(hostName);
		} catch (UserException e) {
			log.error(e.getMessage());
		}
		return (result);
	}

	// TODO: replace by better algorithm with diffusion
	private int encodeSvgImageName(String svgImageName) {
		if (svgImageName != null && !"".equalsIgnoreCase(svgImageName)) return svgImageName.hashCode();

		return 0;
	}

	@Override
	protected TalktimeValue handleGetTalktime(long talktimeId) throws Exception {
		return getComponentsServiceSpring().getTalktime(Long.valueOf(talktimeId));
	}

	@Override
	protected UnitValue[] handleGetUnits4Name(String name) throws Exception {
		return getComponentsServiceSpring().getUnits4Name(name);
	}

	@Override
	protected UnitValue[] handleGetUnits4User(String userName) throws Exception {
		UnitValue[] units = getUserServiceSpring().getUnits4User(userName); // will fetch implicit the activesite of the asking user
		if (units == null) units = new UnitValue[0];
		return units;
	}

	@Override
	protected RealmSimplePwUserValue[] handleGetUserForSimplePwRealm(Integer simplePwRealmId) throws Exception {
		RealmSimplePwUserValue[] val = null;
		try {
			val = getSafeguardServiceSpring().getUserForSimplePwRealm(simplePwRealmId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}
		if (val == null) {
			val = new RealmSimplePwUserValue[0];
		}
		return val;
	}

	@Override
	protected UserUnitsGroupsValue[] handleGetUserUnitsGroups4UnitAndGroup(int unitId, int groupId) throws Exception {
		return getUserServiceSpring().getUserUnitsGroups4UnitAndGroup(Integer.valueOf(unitId), Integer.valueOf(groupId));
	}

	@Override
	protected ViewComponentValue[] handleGetViewComponents4Status(int unitId, int status, Integer viewDocumentId) throws Exception {
		return getViewServiceSpring().getAllViewComponents4UnitAndStatus(Integer.valueOf(unitId), viewDocumentId, status);
	}

	@Override
	protected ViewDocumentValue handleGetViewDocument4ViewComponent(int viewComponentId) throws Exception {
		try {
			return this.getViewServiceSpring().getViewDocument4ViewComponent(Integer.valueOf(viewComponentId));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected void handleImportEditionFromImport(InputStream fis, Integer unitId) throws Exception {
		try {
			getContentServiceSpring().importEdition(unitId, fis);
		} catch (Exception re) {
			log.error("Error importing edition from import", re);
		}
	}

	@Override
	protected void handleRemoveEdition(int editionId) throws Exception {
		getContentServiceSpring().removeEdition(Integer.valueOf(editionId));
	}

	@Override
	protected void handleRemoveHost(String hostName) throws Exception {
		try {
			getAdministrationServiceSpring().removeHost(hostName);
		} catch (UserException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	protected void handleRemovePicture(int pictureId) throws Exception {
		getContentServiceSpring().removePicture(Integer.valueOf(pictureId));
	}

	@Override
	protected void handleRemoveSite(int siteId) throws Exception {
		try {
			getUnitServiceSpring().removeUnits(getMasterRootServiceSpring().getAllUnits4Site(siteId));
			getMasterRootServiceSpring().deleteSite(Integer.valueOf(siteId));
		} catch (Exception exe) {
			log.error("Error removing site", exe);
		}
	}

	@Override
	protected void handleRemoveSiteFromHost(String hostName) throws Exception {
		try {
			getAdministrationServiceSpring().removeSite(hostName);
		} catch (UserException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	protected void handleRemoveSiteGroup(SiteGroupValue value) throws Exception {
		try {
			this.getMasterRootServiceSpring().deleteSiteGroup(value.getSiteGroupId());
		} catch (Exception e) {
			log.error("Error loading all SiteGroups: " + e.getMessage(), e);
		}
	}

	@Override
	protected void handleRemoveStartpageFromHost(String hostName) throws Exception {
		try {
			getAdministrationServiceSpring().removeStartPage(hostName);
		} catch (UserException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	protected HostValue handleSaveHost(HostValue hostValue) throws Exception {
		try {
			hostValue = getMasterRootServiceSpring().setHost(hostValue);
		} catch (Exception e) {
			log.error("Error saving host " + hostValue.getHostName() + ": " + e.getMessage(), e);
		}
		return hostValue;
	}

	@Override
	protected ShortLinkValue handleSaveShortLink(ShortLinkValue shortLinkValue) throws Exception {
		try {
			shortLinkValue = getMasterRootServiceSpring().setShortLink(shortLinkValue);
		} catch (Exception e) {
			log.error("Error saving shortLinkValue " + shortLinkValue.getShortLink() + ": " + e.getMessage(), e);
		}
		return shortLinkValue;
	}

	@Override
	protected ViewComponentValue handleSaveViewComponent(ViewComponentValue viewComponentValue) throws Exception {
		ViewComponentValue vcReturn = null;
		try {
			vcReturn = getViewServiceSpring().saveViewComponent(viewComponentValue);
		} catch (UserException ue) {
			/*
			 * if (this.isExceptionOfType(ue,
			 * "ViewComponentLinkNameAlreadyExisting")) {
			 * ViewComponentLinkNameAlreadyExisting pd = new
			 * ViewComponentLinkNameAlreadyExisting(); throw pd; } else if
			 * (this.isExceptionOfType(ue, "ViewComponentNotFound")) {
			 * ViewComponentNotFound pd = new
			 * ViewComponentNotFound(); throw pd; } else if
			 * (this.isExceptionOfType(ue, "ViewComponentLinkNameIsEmpty")) {
			 * ViewComponentLinkNameIsEmptyException pd = new
			 * ViewComponentLinkNameIsEmptyException(); throw pd; }
			 */
			log.error("Error saving vc", ue);
			throw ue;
		}
		return vcReturn;
	}

	@Override
	protected XmlSearchValue[] handleSearchXml(int siteId, String xpathQuery) throws Exception {
		try {
			XmlSearchValue[] value = getViewServiceSpring().searchXml(Integer.valueOf(siteId), xpathQuery);
			if (value != null) return value;
			return new XmlSearchValue[0];
		} catch (Exception e) {
			log.warn("Error calling searchXml - siteId " + siteId + " xPathQuery \"" + xpathQuery + "\": " + e.getMessage(), e);
			return new XmlSearchValue[0];
		}
	}

	@Override
	protected void handleSetConnectedUsersForSite(int siteId, String[] userIds) throws Exception {
		try {
			getUserServiceSpring().setConnectedUsers4Site(Integer.valueOf(siteId), userIds);
		} catch (Exception exe) {
			log.error("Error set connected users for site", exe);
		}
	}

	@Override
	protected void handleSetPicture4Person(long personId, Integer pictureId) throws Exception {
		getComponentsServiceSpring().setPicture4Person(new Long(personId), pictureId);
	}

	@Override
	protected void handleSetPicture4Unit(int unitId, int pictureId) throws Exception {
		getContentServiceSpring().setPicture4Unit(Integer.valueOf(unitId), Integer.valueOf(pictureId));
	}

	@Override
	protected void handleSetRedirectHost(String hostName, String redirectHostName) throws Exception {
		try {
			getAdministrationServiceSpring().setRedirectHostName(hostName, redirectHostName);
		} catch (UserException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	protected void handleSetRedirectUrl(String hostName, String redirectUrl) throws Exception {
		try {
			getAdministrationServiceSpring().setRedirectUrl(hostName, redirectUrl);
		} catch (UserException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	protected void handleSetSite(String hostName, String siteName) throws Exception {
		try {
			getAdministrationServiceSpring().setSiteByName(hostName, siteName);
		} catch (UserException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	protected void handleSetSite(String hostName, int siteId) throws Exception {
		try {
			getAdministrationServiceSpring().setSiteById(hostName, Integer.valueOf(siteId));
		} catch (UserException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	protected void handleSetSiteConfig(int siteId, String config) throws Exception {
		try {
			getMasterRootServiceSpring().setSiteConfig(Integer.valueOf(siteId), config);
		} catch (Exception exe) {
			log.error("Error set site config", exe);
		}
	}

	@Override
	protected void handleSetStartPage(String hostName, String vcId) throws Exception {
		try {
			getAdministrationServiceSpring().setStartPage(hostName, vcId);
		} catch (UserException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	protected void handleUpdateSite(SiteValue siteValue) throws Exception {
		try {
			getMasterRootServiceSpring().changeSite(siteValue);
		} catch (Exception exe) {
			log.error("Error updating site", exe);
		}
	}

	@Override
	protected void handleUpdateSiteGroup(SiteGroupValue value) throws Exception {
		try {
			this.getMasterRootServiceSpring().setSiteGroup(value);
		} catch (Exception e) {
			log.error("Error updating SiteGroups: " + e.getMessage(), e);
		}
	}

	@Override
	protected ViewComponentValue handleAddFirstViewComponent(int parentId, Integer viewDocumentId, String reference, String displayLinkName, String linkDescription) throws Exception {
		return getViewServiceSpring().addFirstViewComponent(Integer.valueOf(parentId), Integer.valueOf(viewDocumentId), reference, displayLinkName, linkDescription);
	}

	@Override
	protected ViewComponentValue[] handleGetAllViewComponents4UnitAndStatus(Integer unitId, Integer viewDocumentId, int status) throws Exception {
		try {
			Vector<ViewComponentValue> vec = new Vector<ViewComponentValue>();
			ViewComponentHbm view = super.getViewComponentHbmDao().find4Unit(unitId, viewDocumentId);
			this.getAllViewComponentsChildren4Status(view, vec, status, unitId);
			return vec.toArray(new ViewComponentValue[0]);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	private void getAllViewComponentsChildren4Status(ViewComponentHbm view, Vector<ViewComponentValue> vec, int status, Integer unitId) throws Exception {
		if (view.getStatus() == status && !view.isRoot()) {
			ViewComponentValue viewDao = view.getDeployDao();
			viewDao.setPath2Unit(this.getParents4View(view));
			vec.addElement(viewDao);
		}
		Iterator it = view.getChildren().iterator();
		while (it.hasNext()) {
			ViewComponentHbm vcl = (ViewComponentHbm) it.next();
			if (vcl.getAssignedUnit() == null || vcl.getAssignedUnit().getUnitId().equals(unitId)) {
				this.getAllViewComponentsChildren4Status(vcl, vec, status, unitId);
			}
		}
	}

	private String getParents4View(ViewComponentHbm viewComponent) {
		try {
			if (viewComponent.getParent().isRoot()) { return "\\"; }
		} catch (Exception ex) {
			return "\\";
		}
		Vector<ViewComponentHbm> vec = new Vector<ViewComponentHbm>();
		ViewComponentHbm parentView = viewComponent.getParent();

		while (parentView.getAssignedUnit() == null) {
			vec.addElement(parentView);
			parentView = parentView.getParent();
			try {
				if (parentView.isRoot()) {
					break;
				}
			} catch (Exception ex) {
				break;
			}
		}
		if (parentView.getAssignedUnit() != null) {
			vec.addElement(parentView);
		}
		StringBuffer sb = new StringBuffer("\\");

		for (int i = vec.size() - 1; i > -1; i--) {
			sb.append((vec.elementAt(i)).getUrlLinkName());
			if (i != 0) {
				sb.append("\\");
			}
		}
		sb.append("\\").append(viewComponent.getUrlLinkName());
		return sb.toString();
	}

	@Override
	protected SiteValue handleGetCurrentSite(Integer siteId) throws Exception {
		try {
			return getAdministrationServiceSpring().getSite(Integer.valueOf(siteId));
		} catch (Exception e) {
			log.error("Error getting current site: " + e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected EditionValue[] handleGetEditions(int unitId, Integer viewDocumentId) throws Exception {
		return getContentServiceSpring().getEditions(Integer.valueOf(unitId), Integer.valueOf(viewDocumentId));
	}

	@Override
	protected RealmSimplePwValue[] handleGetSimplePwRealms4CurrentUser(Integer activeSiteId) throws Exception {
		RealmSimplePwValue[] val = null;
		try {
			val = getSafeguardServiceSpring().getSimplePwRealms4CurrentUser(new Integer(activeSiteId));
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}
		return val;
	}

	@Override
	protected ViewDocumentValue handleGetViewDocument4ViewTypeAndLanguage(String viewType, String language) throws Exception {
		try {
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			ViewDocumentHbm vd = super.getViewDocumentHbmDao().findByViewTypeAndLanguage(viewType, language, user.getActiveSite().getSiteId());
			return vd.getDao();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	@Override
	protected ViewComponentValue handleInsertViewComponent(int childId, Integer viewDocumentId, String reference, String displayLinkName, String linkDescription, int positionId) throws Exception {
		return getViewServiceSpring().insertViewComponent(Integer.valueOf(childId), Integer.valueOf(viewDocumentId), reference, displayLinkName, linkDescription, positionId);
	}

	@Override
	protected XmlSearchValue[] handleSearchXmlByUnit(int unitId, Integer viewDocumentId, String xpathQuery, boolean parentSearch) throws Exception {
		try {
			XmlSearchValue[] value = getViewServiceSpring().searchXmlByUnit(Integer.valueOf(unitId), Integer.valueOf(viewDocumentId), xpathQuery, parentSearch);
			if (value != null) return value;
			return new XmlSearchValue[0];
		} catch (Exception e) {
			log.warn("Error calling searchXmlByUnit - unitId " + unitId + " xPathQuery \"" + xpathQuery + "\" parentSearch " + parentSearch + ": " + e.getMessage(), e);
			return new XmlSearchValue[0];
		}
	}

	@Override
	protected void handleSetActiveEdition(int editionId, boolean deploy) throws Exception {
		getContentServiceSpring().setActiveEdition(Integer.valueOf(editionId), true);
	}

	@Override
	protected InputStream handleExportEditionFull() throws Exception {
		return getContentServiceSpring().exportEditionFull();
	}

	@Override
	protected InputStream handleExportEditionUnit(Integer rootViewComponentId) throws Exception {
		return getContentServiceSpring().exportEditionUnit(rootViewComponentId);
	}

	@Override
	protected UserValue handleGetUserForId(String userId) throws Exception {
		UserHbm user = getUserHbmDao().load(userId);
		if (user != null) { return user.getUserValue(); }
		return null;
	}

	@Override
	protected TaskValue handleGetTaskForId(Integer taskId) throws Exception {
		TaskValue tv = getUserServiceSpring().getTaskForId(taskId);
		if (tv != null) { return tv; }
		return null;
	}

	@Override
	protected long handleCreateTalkTime(String talkTimes, String talkTimeType) throws Exception {
		Long id = getComponentsServiceSpring().createTalktime(talkTimeType, talkTimes);
		return id;
	}

	@Override
	protected Integer handleAddOrUpdateDocument(InputStream inputStream, Integer unitId, String fileName, String mimeType, Integer documentId) throws Exception {
		try {
			return getContentServiceSpring().addOrUpdateDocument(Integer.valueOf(unitId), fileName, mimeType, inputStream, documentId);
		} catch (Exception re) {
			log.error("Error importing document", re);
		}
		return null;
	}

	@Override
	protected ViewDocumentValue handleSetDefaultViewDocument(String viewType, String language, Integer siteId) throws Exception {
		return getViewServiceSpring().setDefaultViewDocument(viewType, language, siteId);
	}

	@Override
	protected ViewDocumentValue handleGetDefaultViewDocument4Site(Integer siteId) throws Exception {
		return getViewServiceSpring().getDefaultViewDocument4Site(siteId);
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.remote.ClientServiceSpringBase#handleUpdatePictureData(int, byte[], byte[])
	 */
	@Override
	protected void handleUpdatePictureData(int pictureId, byte[] picture, String mimeType, byte[] thumbnail) throws Exception {
		getContentServiceSpring().updatePictureData(Integer.valueOf(pictureId), picture, mimeType, thumbnail);
	}

	@Override
	protected ViewComponentValue[] handleGetViewComponentChildren(Integer viewComponentId) throws Exception {
		return getViewServiceSpring().getViewComponentChildren(viewComponentId);
	}

	@Override
	protected Integer handleGetPictureIdForUnitAndName(Integer unitId, String name) throws Exception {
		return getContentServiceSpring().getPictureIdForUnitAndName(unitId, name);
	}

	@Override
	protected Integer handleGetDocumentIdForNameAndUnit(String name, Integer unitId) throws Exception {
		return getContentServiceSpring().getDocumentIdForNameAndUnit(name, unitId);
	}

	@Override
	protected void handleSetLiveServer(String hostname, boolean liveServer) throws Exception {
		try {
			this.getAdministrationServiceSpring().setLiveServer(hostname, liveServer);
		} catch (Exception e) {
			log.error("Error setting live server");
		}

	}

	@Override
	protected ViewComponentValue[] handleMoveViewComponentsDown(Integer[] viewComponentsId) throws Exception {
		return getViewServiceSpring().moveViewComponentsDown(viewComponentsId);
	}

	@Override
	protected ViewComponentValue[] handleMoveViewComponentsLeft(Integer[] viewComponentsId) throws Exception {
		return getViewServiceSpring().moveViewComponentsLeft(viewComponentsId);
	}

	@Override
	protected ViewComponentValue[] handleMoveViewComponentsRight(Integer[] viewComponentsId) throws Exception {
		return getViewServiceSpring().moveViewComponentsRight(viewComponentsId);
	}

	@Override
	protected ViewComponentValue[] handleMoveViewComponentsUp(Integer[] viewComponentsId) throws Exception {
		return getViewServiceSpring().moveViewComponentsUp(viewComponentsId);
	}

	@Override
	protected HostValue handleCreateHost(HostValue hostValue) throws Exception {
		return getAdministrationServiceSpring().createHost(hostValue);
	}

	@Override
	protected void handleUpdateHost(HostValue hostValue) throws Exception {
		getAdministrationServiceSpring().updateHost(hostValue);

	}

	@Override
	protected Integer handleGetViewComponentChildrenNumber(Integer[] viewComponentsIds) throws Exception {
		return getViewServiceSpring().getViewComponentChildrenNumber(viewComponentsIds);
	}

	@Override
	protected List handleGetUnusedResources4Unit(Integer unitId) throws Exception {
		return getContentServiceSpring().getUnusedResources4Unit(unitId);
	}

	@Override
	protected void handleCopyViewComponentsToParent(Integer parentId, Integer[] viewComponentsIds, Integer position) throws Exception {
		getViewServiceSpring().copyViewComponentsToParent(parentId, viewComponentsIds, position);

	}
}
