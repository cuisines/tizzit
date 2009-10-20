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
package de.juwimm.cms.util;

import static de.juwimm.cms.common.Constants.rb;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import org.andromda.spring.RemoteServiceLocator;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.SpringSecurityException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.rcp.RemoteAuthenticationManager;
import org.tizzit.util.ArraySorter;
import org.tizzit.util.Comparer;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.Ostermiller.util.Browser;

import de.juwimm.cms.Messages;
import de.juwimm.cms.authorization.vo.GroupValue;
import de.juwimm.cms.authorization.vo.RoleValue;
import de.juwimm.cms.authorization.vo.UserLoginValue;
import de.juwimm.cms.authorization.vo.UserUnitsGroupsValue;
import de.juwimm.cms.authorization.vo.UserValue;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.components.vo.DepartmentValue;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.components.vo.TalktimeValue;
import de.juwimm.cms.content.frame.DlgModal;
import de.juwimm.cms.exceptions.AlreadyCheckedOutException;
import de.juwimm.cms.exceptions.InvalidUsernameException;
import de.juwimm.cms.exceptions.NeededFieldsMissingException;
import de.juwimm.cms.exceptions.NoSitesException;
import de.juwimm.cms.exceptions.UnitnameIsAlreadyUsedException;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.exceptions.ViewComponentLinkNameAlreadyExisting;
import de.juwimm.cms.exceptions.ViewComponentLinkNameIsEmptyException;
import de.juwimm.cms.exceptions.ViewComponentNotFound;
import de.juwimm.cms.gui.PanCheckInPages;
import de.juwimm.cms.gui.event.ExitEvent;
import de.juwimm.cms.gui.event.ExitListener;
import de.juwimm.cms.gui.table.ModifiedPagesTableModel;
import de.juwimm.cms.gui.tree.CmsTreeModel;
import de.juwimm.cms.gui.tree.PageNode;
import de.juwimm.cms.http.HttpClientWrapper;
import de.juwimm.cms.remote.ClientServiceSpring;
import de.juwimm.cms.safeguard.vo.ActiveRealmValue;
import de.juwimm.cms.safeguard.vo.RealmJaasValue;
import de.juwimm.cms.safeguard.vo.RealmJdbcValue;
import de.juwimm.cms.safeguard.vo.RealmLdapValue;
import de.juwimm.cms.safeguard.vo.RealmSimplePwUserValue;
import de.juwimm.cms.safeguard.vo.RealmSimplePwValue;
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
 * Facade Class for communicating with the server.
 * 
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class Communication implements ExitListener, ActionListener {
	private static Logger log = Logger.getLogger(Communication.class);
	private UserLoginValue user = null;
	private GroupValue[] groups = null;
	private boolean loggedIn = false;
	private int activeSiteId = -1;
	private CmsTreeModel treeModel;
	private boolean isSessionInitialized = false;
	private ViewDocumentValue viewDocument = null;
	private int selectedUnitId;
	private DbHelper dbHelper = null;
	/* This ArrayList contains the ContentId (Integer) of all Contents who
	 * haven't been checked in. Needed for checking them in after unclean exit
	 */
	private static ArrayList<Integer> checkOutPages = new ArrayList<Integer>();
	private ClientServiceSpring clientService;

	public Communication() {

	}

	public Communication initialize() {
		if (!isSessionInitialized) {
			ActionHub.addExitListener(this);
			isSessionInitialized = true;
		}
		return this;
	}

	public ClientServiceSpring getClientService() {
		return clientService;
	}

	public void setClientService(ClientServiceSpring clientService) {
		this.clientService = clientService;
	}

	/**
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		ActionHub.fireActionPerformed(e);
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public int getViewDocumentId() {
		return viewDocument.getViewDocumentId().intValue();
	}

	public ViewDocumentValue getViewDocument() {
		return viewDocument;
	}

	public void setViewDocument(ViewDocumentValue vdDao) {
		this.viewDocument = vdDao;
	}

	public boolean isUserInRole(String role) {
		if (user == null) { return false; }
		if (user.getUser().isMasterRoot()) { return true; }
		if (isUserInRolePhysically(role)) { return true; }
		if (isUserInRolePhysically(UserRights.SITE_ROOT)) { return true; }
		return false;
	}

	public boolean isUserInRolePhysically(String role) {
		if (groups == null) return false;
		for (int i = 0; i < groups.length; i++) {
			RoleValue[] roles = groups[i].getRoles();
			if (roles == null) continue;
			for (int j = 0; j < roles.length; j++) {
				if (roles[j].getRoleId().equalsIgnoreCase(role)) { return true; }
			}
		}
		return false;
	}

	public boolean isUserInRole(UserValue uv, String role) {
		if (uv == null) { return false; }
		if (uv.isMasterRoot()) { return true; }
		try {
			GroupValue[] gv = getClientService().getGroups4User(uv.getUserName());
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

	public GroupValue[] getGroups() {
		return groups;
	}

	public GroupValue[] getGroups4User(String userName) throws Exception {
		GroupValue[] gv = getClientService().getGroups4User(userName);
		if (gv == null) gv = new GroupValue[0];
		return gv;
	}

	public void removeUserFromGroup(GroupValue gv, String userName) throws Exception {
		gv.setRoles(new RoleValue[0]);
		getClientService().removeUserFromGroup(gv, userName);
	}

	public void addUserToGroup(GroupValue groupValue, String usrName) throws Exception {
		groupValue.setRoles(new RoleValue[0]);
		getClientService().addUserToGroup(groupValue, usrName);
	}

	public RoleValue[] getRolesMe() {
		ArrayList<String> al = new ArrayList<String>(groups.length);
		for (int i = 0; i < groups.length; i++) {
			for (int j = 0; j < groups[i].getRoles().length; j++) {
				al.add(groups[i].getRoles()[j].getRoleId());
			}
		}
		return al.toArray(new RoleValue[0]);
	}

	public RoleValue[] getAllRoles() {
		try {
			return getClientService().getAllRoles();
		} catch (Exception exe) {
			log.error(exe);
		}
		return new RoleValue[0];
	}

	public GroupValue[] getAllGroups() {
		GroupValue[] gv = new GroupValue[0];
		try {
			gv = getClientService().getAllGroups();
		} catch (Exception exe) {
			log.error(exe);
		}
		return gv;
	}

	public GroupValue[] getAllGroupsUsedInUnit(int unitId) {
		GroupValue[] gv = new GroupValue[0];
		try {
			gv = getClientService().getAllGroupsUsedInUnit(Integer.valueOf(unitId));
		} catch (Exception exe) {
			log.error(exe);
		}
		return gv;
	}

	public GroupValue createGroup(String groupName) throws Exception {
		return getClientService().createGroup(groupName);
	}

	public void removeGroup(int groupId) throws Exception {
		getClientService().removeGroup(Integer.valueOf(groupId));
	}

	public void updateGroup(GroupValue gv) throws Exception {
		getClientService().updateGroup(gv);
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

	public void showBrowserWindow(int vcId, boolean showInFrame) {
		String strHost = Constants.CMS_PATH_DEMOPAGE;
		String page = "";

		if (showInFrame) {
			page = Constants.CMS_PATH_DEMOPAGE_FULLFRAMESET;
		} else {
			page = Constants.CMS_PATH_DEMOPAGE_CONTENT;
		}

		try {
			Browser.displayURL(strHost + getViewDocument().getLanguage() + "/" + getPathForViewComponentId(vcId) + "/" + page);
		} catch (IOException exe) {
			log.error("Show browser error occures", exe);
		}
	}

	public String getPathForViewComponentId(int vcId) {
		try {
			return getClientService().getPathForViewComponentId(Integer.valueOf(vcId));
		} catch (Exception exe) {
			return "";
		}
	}

	public String getDCF(String dcfname) throws Exception {
		String dcf = getDbHelper().getDCF(dcfname);
		if (dcf == null || dcf.equalsIgnoreCase("")) {
			dcf = HttpClientWrapper.getInstance().getString(Constants.CMS_PATH_DCF + dcfname + ".xml?lang=" + Constants.CMS_LOCALE.getLanguage());
			getDbHelper().setDCF(dcfname, dcf);
		}
		return dcf;
	}

	public boolean exitPerformed(ExitEvent e) {
		log.info("Exit-event started");
		int result = JOptionPane.showConfirmDialog(UIConstants.getMainFrame(), rb.getString("dialog.exit.text"), rb.getString("dialog.exit"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			if (!checkOutPages.isEmpty()) {
				ArrayList<ContentValue> pageList = new ArrayList<ContentValue>();
				Iterator it = checkOutPages.iterator();
				while (it.hasNext()) {
					try {
						ContentValue current = getClientService().getContent(((Integer) it.next()));
						pageList.add(current);
					} catch (Exception ex) {
						String msg = Messages.getString("exception.checkingInAllRemainingPages", Integer.toString(checkOutPages.size()));
						log.info(msg);
						JOptionPane.showMessageDialog(UIConstants.getMainFrame(), msg, rb.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
						UIConstants.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						while (it.hasNext()) {
							Integer contentId = (Integer) it.next();
							try {
								msg = Messages.getString("exception.checkingInAllRemainingPagesStatusbar", contentId.toString());
								UIConstants.setActionStatus(msg);
								getClientService().checkIn4ContentId(contentId);
							} catch (Exception exe) {
								log.error("Exit event error", exe);
							}
						}
						checkOutPages = new ArrayList<Integer>();
						UIConstants.getMainFrame().setCursor(Cursor.getDefaultCursor());
						try {
							if (log.isDebugEnabled()) {
								log.debug("Calling logout on server");
							}
							getClientService().logout();
						} catch (Exception exe) {
						}
						log.info("Goodbye!");
						loggedIn = false;
						return true;
					}
				}
				PanCheckInPages pan = new PanCheckInPages(pageList);
				if (rb == null) rb = Constants.rb;
				DlgModal dlg = new DlgModal(pan, 300, 500, rb.getString("DlgModal.checkin"));
				dlg.addOkListener(new OkListener(pan, dlg));
				dlg.setVisible(true);
			}
			try {
				if (log.isDebugEnabled()) {
					log.debug("Calling logout on server");
				}
				getClientService().logout();
			} catch (Exception exe) {
			}
			log.info("Goodbye!");
			loggedIn = false;
			return true;
		}
		return false;
	}

	public void setSelectedUnitId(int unitId) {
		selectedUnitId = unitId;
	}

	public int getSelectedUnitId() {
		return selectedUnitId;
	}

	public UserValue getUser() {
		if (user != null) { return user.getUser(); }
		return null;
	}

	public UserValue getUserByName(String userName) {
		return getClientService().getUserForId(userName);
	}

	public String getSiteConfigXML() {
		return user.getSiteConfigXML();
	}

	public String getSiteName() {
		return user.getSiteName();
	}

	public int getSiteId() {
		return activeSiteId;
	}

	public boolean isUserInUnit(int unitId) {
		//getClientService().isUserInUnit(user.getUser().getUserName(), unitId);
		UnitValue[] uv = user.getUnits();
		if (uv == null) { return false; }
		for (int i = 0; i < uv.length; i++) {
			if (uv[i].getUnitId().intValue() == unitId) { return true; }
		}
		return false;
	}

	public DbHelper getDbHelper() {
		if (dbHelper == null) {
			dbHelper = new DbHelper();
		}
		return dbHelper;
	}

	public SiteValue[] getSites(String userName, String passwd) throws InvalidUsernameException, NoSitesException {
		log.info("Trying to fetching Sites for User " + userName);
		try {
			SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
			RemoteAuthenticationManager remoteAuthenticationService = RemoteServiceLocator.instance().getRemoteAuthenticationService();
			GrantedAuthority[] authorities = remoteAuthenticationService.attemptAuthentication(userName, String.valueOf(passwd));
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userName, String.valueOf(passwd), authorities));
			log.debug(SecurityContextHolder.getContext().getAuthentication());
		} catch (SpringSecurityException e) {
			log.info("authentication failed: " + e.getMessage());
			throw new InvalidUsernameException();
		}

		try {
			ClientServiceSpring cs = getClientService();
			SiteValue[] sites = cs.getSites();
			return sites;
		} catch (Exception e) {
			//	    e.printStackTrace();
			throw new NoSitesException("No sites associated?", e);
		}
	}

	public UserLoginValue login(String userName, String passwd, int activeSiteId) throws UserException {
		try {
			log.info("Trying to logging in User " + userName + " at activeSiteId " + activeSiteId);
			this.activeSiteId = activeSiteId;
			user = getClientService().login(userName, passwd, Integer.valueOf(activeSiteId));
			groups = getClientService().getGroups();
		} catch (Exception ue) {
			log.error("Error getting login", ue);
			throw new UserException(ue);
		}
		log.info("Login succeeded");
		return user;
	}

	public UnitValue[] getUnits() throws Exception {
		UnitValue[] uda = getClientService().getUnits();
		if (uda != null) Arrays.sort(uda, new UnitDaoComparer());
		return uda;
	}

	/**
	 * 
	 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @version $Id$
	 */
	public static class UnitDaoComparer extends UserValue implements Comparator<UnitValue> {
		private static final long serialVersionUID = 1329857419797304096L;

		public int compare(UnitValue objA, UnitValue objB) {
			return objA.getName().toLowerCase().compareTo(objB.getName().toLowerCase());
		}
	}

	public UnitValue[] getAllUnits() throws Exception {
		UnitValue[] uda = getClientService().getAllUnits();
		if (uda != null) Arrays.sort(uda, new UnitDaoComparer());
		return uda;
	}

	public UnitValue getUnit(int unitId) throws Exception {
		return getClientService().getUnit(Integer.valueOf(unitId));
	}

	public UserValue[] getUser4Unit(int unitId) throws Exception {
		return getClientService().getAllUserForUnit(Integer.valueOf(unitId));
	}

	public void updateUser(UserValue uv) throws Exception {
		getClientService().updateUser(uv);
	}

	public void updateUserMe() {
		try {
			getClientService().updateUser(getUser());
		} catch (Exception exe) {
			log.error(exe);
		}
	}

	public void changePassword(String userName, String passwdNew) throws Exception {
		getClientService().changePassword4User(userName, passwdNew, passwdNew);
		if (userName.equals(user.getUser().getUserName())) {
			getSites(userName, passwdNew);
		}
	}

	public void createUser(String userName, String passwd, String firstName, String lastName, String email, int initialUnitId) throws NeededFieldsMissingException, Exception {
		initialUnitId = (initialUnitId < 0) ? 0 : initialUnitId;
		try {
			getClientService().createUser(userName, passwd, firstName, lastName, email, Integer.valueOf(initialUnitId));
		} catch (Exception ue) {
			/*
			 * if (this.isExceptionOfType(ue, "NeededFieldsMissingException")) {
			 * AxisFault af = (AxisFault) ue; String reason =
			 * af.getFaultReason(); String expected =
			 * Communication.USER_EXCEPTION + "NeededFieldsMissingException";
			 * NeededFieldsMissingException nfme = new
			 * NeededFieldsMissingException(); reason =
			 * reason.substring(expected.length());
			 * nfme.setMissingFields(reason); throw nfme; }
			 */
			// log.error("Error creating user", ue);
			throw new UserException(ue);
		}
	}

	public Integer createUnit(String unitName) throws UnitnameIsAlreadyUsedException, UserException {
		try {
			return getClientService().createUnit(unitName);
		} catch (Exception ue) {
			/*
			 * if (this.isExceptionOfType(ue, "UnitnameIsAlreadyUsedException")) {
			 * UnitnameIsAlreadyUsedException
			 * unitNameInUseExeption = new UnitnameIsAlreadyUsedException();
			 * throw unitNameInUseExeption; }
			 */
			throw new UserException(ue);
		}
	}

	public void removeUnit(UnitValue unitDao) throws Exception {
		getClientService().removeUnit(unitDao);
	}

	public void removeUnit(int unitId) throws Exception {
		getClientService().removeUnit(Integer.valueOf(unitId));
	}

	public void removeTask(int taskId) throws Exception {
		getClientService().removeTask(Integer.valueOf(taskId));
	}

	public void setTaskViewed(int taskId) throws Exception {
		getClientService().setTaskViewed(Integer.valueOf(taskId));
	}

	public void removePerson(long personId) throws Exception {
		getClientService().removePerson(Long.valueOf(personId));
	}

	public void removeDepartment(long departmentId) throws Exception {
		getClientService().removeDepartment(Long.valueOf(departmentId));
	}

	public void removeAddress(long addressId) throws Exception {
		getClientService().removeAddress(Long.valueOf(addressId));
	}

	public void removeTalktime(long talktimeId) throws Exception {
		getClientService().removeTalktime(Long.valueOf(talktimeId));
	}

	public long addTalktime2Person(long personId, String talkTimeType, String talkTimes) throws Exception {
		return getClientService().addTalktime2Person(personId, talkTimeType, talkTimes);
	}

	public long addTalktime2Department(long departmentId, String talkTimeType, String talkTimes) throws Exception {
		return getClientService().addTalktime2Department(Long.valueOf(departmentId), talkTimeType, talkTimes);
	}

	public long addTalktime2Unit(int unitId, String talkTimeType, String talkTimes) throws Exception {
		return getClientService().addTalktime2Unit(Integer.valueOf(unitId), talkTimeType, talkTimes);
	}

	public void addAddress2Person(long personId, long addressId) throws Exception {
		getClientService().addAddress2Person(Long.valueOf(personId), Long.valueOf(addressId));
	}

	public void addAddress2Department(long departmentId, long addressId) throws Exception {
		getClientService().addAddress2Department(Long.valueOf(departmentId), Long.valueOf(addressId));
	}

	public void addAddress2Unit(int unitId, long addressId) throws Exception {
		getClientService().addAddress2Unit(Integer.valueOf(unitId), Long.valueOf(addressId));
	}

	public void updateUnit(UnitValue dao) throws Exception {
		getClientService().updateUnit(dao);
	}

	public void updatePerson(PersonValue personDao) throws Exception {
		getClientService().updatePerson(personDao);
	}

	public void updateDepartment(DepartmentValue departmentDao) throws Exception {
		getClientService().updateDepartment(departmentDao);
	}

	public void updateAddress(AddressValue addressData) throws Exception {
		getClientService().updateAddress(addressData);
	}

	public void updateTalktime(TalktimeValue ttData) throws Exception {
		getClientService().updateTalktime(ttData);
	}

	public void deleteUser(String userName) throws Exception {
		getClientService().deleteUser(userName);
	}

	public void removeUserFromUnit(UnitValue unit) throws Exception {
		getClientService().removeUserFromUnit(unit, user.getUser().getUserName());
	}

	public void removeUserFromUnit(UnitValue unit, String userNameId) throws Exception {
		getClientService().removeUserFromUnit(unit, userNameId);
	}

	public void addUser2Unit(UserValue user, UnitValue unit) throws Exception {
		getClientService().addUser2Unit(user, unit);
	}

	public int getUnit4ViewComponent(int viewComponentId) throws Exception {
		return getClientService().getUnit4ViewComponent(Integer.valueOf(viewComponentId));
	}

	public ViewDocumentValue[] getViewDocuments() throws Exception {
		return getClientService().getViewDocuments();
	}

	public ViewDocumentValue getViewDocument(String viewType, String language) throws Exception {
		return getClientService().getViewDocument4ViewTypeAndLanguage(viewType, language);
	}

	public UserValue[] getAllUser() throws Exception {
		UserValue[] ud = getClientService().getAllUser();
		if (ud != null) Arrays.sort(ud, new UserDaoComparer());
		return ud;
	}

	/**
	 * 
	 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @version $Id$
	 */
	private class UserDaoComparer extends UserValue implements Comparator<UserValue> {
		private static final long serialVersionUID = 2497414650147720673L;

		public int compare(UserValue firstUser, UserValue secondUser) {
			int retVal = firstUser.getUserName().toLowerCase().compareTo(secondUser.getUserName().toLowerCase());
			if (retVal != 0) { return retVal; }
			if (firstUser.getFirstName() == null) return -1;
			if (secondUser.getFirstName() == null) return 1;
			return firstUser.getFirstName().toLowerCase().compareTo(secondUser.getFirstName().toLowerCase());
		}
	}

	private void replaceTaskStrings(TaskValue[] tdao) {
		for (int i = 0; i < tdao.length; i++) {
			if (tdao[i].getTaskType() == Constants.TASK_SYSTEMMESSAGE_ERROR || tdao[i].getTaskType() == Constants.TASK_SYSTEMMESSAGE_INFORMATION) {
				try {
					String comment = tdao[i].getComment();
					if (tdao[i].getTaskType() == Constants.TASK_SYSTEMMESSAGE_ERROR) {
						try {
							comment = rb.getString("SYSTEMMESSAGE_ERROR." + comment);
						} catch (Exception exe) {
							comment = Messages.getString("exception.UnknownError", comment);
						}
					} else {
						comment = rb.getString("SYSTEMMESSAGE_INFORMATION." + comment);
					}
					tdao[i].setComment(comment);
				} catch (Exception exe) {
				}
				tdao[i].setTaskType(Constants.TASK_MESSAGE);
			}
		}
	}

	public TaskValue[] getAllTasks() throws Exception {
		TaskValue[] tdao = getClientService().getAllTasks();
		if (tdao != null) {
			replaceTaskStrings(tdao);
			ArraySorter.sort(tdao, new TaskValueComparer());
		}
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

	public boolean isNewTask4User() throws Exception {
		return getClientService().isNewTask4User();
	}

	public void reindexSite(Integer siteId) throws Exception {
		getClientService().reindexSite(siteId);
	}

	public void reindexContent(Integer vcId) throws Exception {
		getClientService().reindexPage(vcId);
	}

	public UserValue[] getAllUser(int groupId) throws Exception {
		return getClientService().getAllUser(Integer.valueOf(groupId));
	}

	public UserValue[] getAllUser(int groupId, int unitId) {
		UserValue[] uv = new UserValue[0];
		try {
			uv = getClientService().getAllUser4GroupAndUnit(Integer.valueOf(groupId), Integer.valueOf(unitId));
		} catch (Exception exe) {
			log.error(exe);
		}
		return uv;
	}

	public ViewDocumentValue createViewDocument(ViewDocumentValue vDao) throws Exception {
		return getClientService().createViewDocument(vDao);
	}

	public void setDefaultViewDocument(int viewDocumentId) throws Exception {
		if (viewDocumentId > 0) {
			getClientService().setDefaultViewDocument(Integer.valueOf(viewDocumentId));
		}
	}

	public ContentValue createContent(ContentValue cDao) throws Exception {
		return getClientService().createContent(cDao);
	}

	public void setUnit4ViewComponent(int unitId, ViewDocumentValue viewDocumentDao, int viewComponentId) throws Exception {
		getClientService().setUnit4ViewComponent(Integer.valueOf(unitId), viewDocumentDao, Integer.valueOf(viewComponentId));
	}

	public void removeViewDocument(int viewDocumentId) throws Exception {
		getClientService().removeViewDocument(Integer.valueOf(viewDocumentId));
	}

	@SuppressWarnings("unchecked")
	public boolean removeViewComponent(int intViewComponentId, String viewComponentName, byte onlineState) {
		boolean retVal = false;

		try {
			// CHECK IF THIS NODE CONTAINS SUBNODES
			ViewIdAndInfoTextValue[] str = getAllChildrenNamesWithUnit(intViewComponentId);
			String units = "";
			if (str != null && str.length > 0) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < str.length; i++) {
					sb.append(str[i].getInfoText().trim()).append("\n");
				}
				units = sb.toString();
			}
			if (!units.equalsIgnoreCase("")) {
				if (!isUserInRole(UserRights.SITE_ROOT)) {
					// dazwischen, damit sparen wir uns das zweite...
					String msg = Messages.getString("comm.removevc.containsunitsandcannotremove", units);
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), msg, rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}
				units = Messages.getString("comm.removevc.header_units", units);
			}

			String refVcs = "";
			ViewComponentValue[] refDao = getViewComponentsWithReferenceToViewComponentId(intViewComponentId);
			if (refDao != null && refDao.length > 0) {
				StringBuffer sb = new StringBuffer();
				for (int j = 0; j < refDao.length; j++) {
					if (refDao[j].getViewType() == Constants.VIEW_TYPE_INTERNAL_LINK) {
						// InternalLink in the Tree
						sb.append(Messages.getString("comm.removevc.refvc.internallink", ("\"" + refDao[j].getDisplayLinkName() + "\""), ("\"" + refDao[j].getMetaData().trim() + "\""))).append("\n");
					} else if (refDao[j].getViewType() == Constants.VIEW_TYPE_SYMLINK) {
						// reference though Symlink in the Tree
						sb.append(Messages.getString("comm.removevc.refvc.symlink", ("\"" + refDao[j].getDisplayLinkName() + "\""), ("\"" + refDao[j].getMetaData().trim() + "\""))).append("\n");
					} else {
						// reference through links in the content
						sb.append(Messages.getString("comm.removevc.refvc.content", ("\"" + refDao[j].getDisplayLinkName() + "\""), ("\"" + refDao[j].getMetaData().trim() + "\""))).append("\n");
					}
				}
				refVcs = sb.toString();
			}

			if (!refVcs.equals("")) {
				refVcs = Messages.getString("comm.removevc.header_refvcs", refVcs);
			}

			String teaserText = "";
			boolean teaserReferenced = false;
			try {
				StringBuilder refTeaser = new StringBuilder("");
				XmlSearchValue[] xmlSearchValues = this.searchXml(getSiteId(), "//teaserRef");
				if (xmlSearchValues != null && xmlSearchValues.length > 0) {
					// herausfinden, ob es sich um DIESEN Teaser handelt
					String resultRootStartElement = "<searchTeaserResult>";
					String resultRootEndElement = "</searchTeaserResult>";
					for (int i = 0; i < xmlSearchValues.length; i++) {
						StringBuilder stringBuilder = new StringBuilder(xmlSearchValues[i].getContent());
						stringBuilder.insert(0, resultRootStartElement);
						stringBuilder.append(resultRootEndElement);
						Document doc = XercesHelper.string2Dom(stringBuilder.toString());
						Iterator<Element> teaserIterator = XercesHelper.findNodes(doc, "searchTeaserResult/teaserRef");
						while (teaserIterator.hasNext()) {
							Element element = teaserIterator.next();
							String viewComponentIdValue = element.getAttribute("viewComponentId");
							if (viewComponentIdValue != null && viewComponentIdValue.trim().length() > 0) {
								if (intViewComponentId == (new Integer(viewComponentIdValue)).intValue()) {
									teaserReferenced = true;
									refTeaser.append(this.getPathForViewComponentId(xmlSearchValues[i].getViewComponentId().intValue()) + "\n");
								}
							}
						}
					}
					if (teaserReferenced) {
						teaserText = Messages.getString("comm.removevc.header.teaser", refTeaser.toString());
					}
				}
			} catch (Exception exception) {
				log.error(exception.getMessage(), exception);
			}
			String msgHeader = "";
			if (onlineState == Constants.ONLINE_STATUS_UNDEF || onlineState == Constants.ONLINE_STATUS_OFFLINE) {
				msgHeader = rb.getString("comm.removevc.header_offline");
			} else {
				msgHeader = rb.getString("comm.removevc.header_online");
			}

			String msgstr = msgHeader + units + refVcs + teaserText;

			int i = JOptionPane.showConfirmDialog(UIConstants.getMainFrame(), msgstr, rb.getString("dialog.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

			if (i == JOptionPane.YES_OPTION) {
				if (onlineState == Constants.ONLINE_STATUS_UNDEF || onlineState == Constants.ONLINE_STATUS_OFFLINE) {
					getClientService().removeViewComponent(Integer.valueOf(intViewComponentId), true);
					/*
					 * } else { // nothing at the moment, the code for here is
					 * currently in PanTree actionViewComponentPerformed
					 */
				}
				retVal = true;
			}
		} catch (Exception exe) {
			log.error("Error removing vc", exe);
		}

		if (retVal) {
			try {
				checkOutPages.remove(new Integer(getViewComponent(intViewComponentId).getReference()));
			} catch (Exception exe) {
			}
			UIConstants.setStatusInfo(rb.getString("comm.removevc.statusinfo"));
		}
		return retVal;
	}

	/**
	 * delete in case of multiselect
	 * @param entriesPath
	 * @return
	 */
	public ArrayList<TreePath> removeViewComponents(TreePath[] entriesPath) {
		boolean localVal;
		ArrayList<TreePath> deleted = new ArrayList<TreePath>();

		//		Arrays.<ViewComponentValue> sort(listofVC.toArray(arraysViewComponents), new Comparator<ViewComponentValue>() {
		//			public int compare(ViewComponentValue o1, ViewComponentValue o2) {
		//				if ((o1 != null) && (o2 != null)) { return (int) (Integer.valueOf(o2.getViewLevel()) - Integer.valueOf(o1.getViewLevel()));
		//
		//				}
		//				return 0;
		//			}
		//
		//		});
		try {
			for (TreePath treePath : entriesPath) {
				localVal = true;
				PageNode local = (PageNode) treePath.getLastPathComponent();
				byte onlineState = local.getOnline();
				int intViewComponentId = local.getViewComponent().getViewComponentId();
				try {
					ViewIdAndInfoTextValue[] str = getAllChildrenNamesWithUnit(intViewComponentId);
					String units = "";
					if (str != null && str.length > 0) {
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < str.length; i++) {
							sb.append(str[i].getInfoText().trim()).append("\n");
						}
						units = sb.toString();
					}
					if (!units.equalsIgnoreCase("")) {
						if (!isUserInRole(UserRights.SITE_ROOT)) {
							// dazwischen, damit sparen wir uns das zweite...
							String msg = Messages.getString("comm.removevc.containsunitsandcannotremove", units);
							JOptionPane.showMessageDialog(UIConstants.getMainFrame(), msg, rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
							localVal = false;
						}
						units = Messages.getString("comm.removevc.header_units", units);
					}
					if (localVal) {
						if (onlineState == Constants.ONLINE_STATUS_UNDEF || onlineState == Constants.ONLINE_STATUS_OFFLINE) {
							try {
								getClientService().removeViewComponent(Integer.valueOf(intViewComponentId), true);
							} catch (Exception e) {
							}
						}
					}
				} catch (Exception exe) {
					log.error("Error removing vc", exe);
				}
				if (localVal) {
					try {
						checkOutPages.remove(new Integer(getViewComponent(intViewComponentId).getReference()));
					} catch (Exception exe) {
					}
					UIConstants.setStatusInfo(rb.getString("comm.removevc.statusinfo"));
				}
				if (localVal) deleted.add(treePath);

			}
		} catch (Exception e) {
			log.error("Error at multiple delete");
		}
		return deleted;
	}

	public ViewComponentValue[] getViewComponentsWithReferenceToViewComponentId(int viewComponentId) {
		ViewComponentValue[] vcv = null;
		try {
			vcv = getClientService().getViewComponentsWithReferenceToViewComponentId(Integer.valueOf(viewComponentId));
		} catch (Exception exe) {
		}
		if (vcv == null) vcv = new ViewComponentValue[0];
		return vcv;
	}

	public ViewComponentValue[] getViewComponedntsThatReferenceThisTeaser(int viewComponentId) {
		ViewComponentValue[] vcv = null;
		// try {
		// FIXME
		// }
		return vcv;
	}

	public ViewComponentValue getViewComponent(int viewComponentId) throws Exception {
		return getClientService().getViewComponent(Integer.valueOf(viewComponentId));
	}

	public synchronized ViewComponentValue getViewComponent(int viewComponentId, int depth) throws Exception {
		return getClientService().getViewComponentWithDepth(Integer.valueOf(viewComponentId), depth);
	}

	public ViewComponentValue moveViewComponentUp(int viewComponentId) throws Exception {
		return getClientService().moveViewComponentUp(Integer.valueOf(viewComponentId));
	}

	public ViewComponentValue moveViewComponentDown(int viewComponentId) throws Exception {
		return getClientService().moveViewComponentDown(Integer.valueOf(viewComponentId));
	}

	public ViewComponentValue moveViewComponentLeft(int viewComponentId) throws Exception {
		// send mail on moving viewcomponent with unit
		if (!this.isUnitAndChangesParentUnitLeft(viewComponentId)) {
			log.error(user.getUser().getUserName() + ": moved viewcomponent " + viewComponentId + " left into another unit!");
		}
		return getClientService().moveViewComponentLeft(Integer.valueOf(viewComponentId));
	}

	public ViewComponentValue moveViewComponentRight(int viewComponentId) throws Exception {
		// send mail on moving viewcomponent with unit
		if (!this.isUnitAndChangesParentUnitRight(viewComponentId)) {
			log.error(user.getUser().getUserName() + ": moved viewcomponent " + viewComponentId + " right into another unit!");
		}
		return getClientService().moveViewComponentRight(Integer.valueOf(viewComponentId));
	}

	private boolean isUnitAndChangesParentUnitLeft(int viewComponentId) throws Exception {
		return getClientService().isUnitAndChangesParentUnitLeft(Integer.valueOf(viewComponentId));
	}

	private boolean isUnitAndChangesParentUnitRight(int viewComponentId) throws Exception {
		return getClientService().isUnitAndChangesParentUnitRight(Integer.valueOf(viewComponentId));
	}

	public void cancelApproval(int viewComponentId) throws Exception {
		getClientService().cancelApproval(Integer.valueOf(viewComponentId));
	}

	public void updateStatus4ViewComponent(ViewComponentValue vcDao) throws Exception {
		if (log.isDebugEnabled()) log.debug("updateStatus4ViewComponent");
		getClientService().updateStatus4ViewComponent(vcDao);
	}

	public int createTask(String receiverId, String receiverRole, int unitId, String comment, byte taskType) throws Exception {
		// Funzt das jetzt mit null-Pointer?
		/*
		 * int unit = -1; if(a_UnitId != null) { unit = a_UnitId.intValue(); }
		 */
		return getClientService().createTask(receiverId, receiverRole, Integer.valueOf(unitId), comment, taskType);
	}

	public void addViewComponentsToTask(int taskId, Integer[] vcIds) throws Exception {
		getClientService().addViewComponentsToTask(Integer.valueOf(taskId), vcIds);
	}

	public void removeViewComponentsFromTask(int taskId, Integer[] vcIds) throws Exception {
		getClientService().removeViewComponentsFromTask(Integer.valueOf(taskId), vcIds);
	}

	public ViewComponentValue getViewComponent4Unit(int unitId) throws Exception {
		return getClientService().getViewComponent4Unit(Integer.valueOf(unitId), Integer.valueOf(this.getViewDocumentId()));
	}

	public ViewComponentValue getViewComponent4Unit(int unitId, int depth) throws Exception {
		Integer viewComponentId = getClientService().getViewComponent4Unit(Integer.valueOf(unitId), Integer.valueOf(this.getViewDocumentId())).getViewComponentId();
		return getClientService().getViewComponentWithDepth(viewComponentId, depth);
	}

	public String[] getParents4ViewComponent(int viewComponentId) throws Exception {
		String[] vcdarr = null;
		try {
			vcdarr = getClientService().getParents4ViewComponent(Integer.valueOf(viewComponentId));
			if (vcdarr == null) vcdarr = new String[0];
		} catch (Exception exe) {
			vcdarr = new String[0];
		}
		return vcdarr;
	}

	public ContentValue getContent(int contentId) throws Exception {
		ContentValue cdao = getClientService().getContent(Integer.valueOf(contentId));
		if (log.isDebugEnabled()) {
			log.debug("getContent() " + cdao.getHeading());
		}
		return cdao;
	}

	public void updateTemplate(int viewComponentId, String templateName) throws Exception {
		getClientService().updateTemplate(Integer.valueOf(viewComponentId), templateName);
		getDbHelper().setTemplateName(viewComponentId, templateName);
	}

	public String getContentTemplateName(int contentId) throws Exception {
		String templateName = getDbHelper().getTemplateName(contentId);
		if (templateName == null) {
			templateName = getClientService().getContentTemplateName(Integer.valueOf(contentId));
			getDbHelper().setTemplateName(contentId, templateName);
		}
		return templateName;
	}

	/**
	 * @param contentId
	 * @return ContentVersionValue[]
	 */
	public ContentVersionValue[] getAllContentVersionsForContent(int contentId) {
		// erst ID Liste ziehen, danach dann in DbHelper �berpr�fen, ob schon
		// vorhanden.
		try {
			ContentVersionValue[] cvd = null;
			/*
			 * if(getDbHelper().checkContentVersionsForContentAviable(contentId)) {
			 * int[] intContents =
			 * getContentSession().getAllContentVersionsId(contentId); cvd = new
			 * ContentVersionDao[intContents.length]; for(int i=0;i<intContents.length;i++) {
			 * cvd[i] = getDbHelper().getContentVersion(intContents[i]);
			 * if(cvd[i]==null) { cvd[i] =
			 * getContentSession().getContentVersion(intContents[i]);
			 * getDbHelper().setContentVersion(cvd[i].getContentVersionId(),
			 * contentId, cvd[i]); } } }else{
			 */
			cvd = getClientService().getAllContentVersions(Integer.valueOf(contentId));
			if (cvd == null) cvd = new ContentVersionValue[0];
			/*
			 * for(int i=0;i<cvd.length;i++)
			 * getDbHelper().setContentVersion(cvd[i].getContentVersionId(),
			 * contentId, cvd[i]); }
			 */
			return cvd;
		} catch (Exception exe) {
			log.error("Error getting all contentversions for content", exe);
			return null;
		}
	}

	public ContentVersionValue getContentVersion(int contentVersionId) {
		try {
			return getClientService().getContentVersion(Integer.valueOf(contentVersionId));
		} catch (Exception exe) {
			log.error("Error getting contentversion", exe);
			return null;
		}
	}

	public void removeAllOldContentVersions(int contentId) {
		try {
			getClientService().removeAllOldContentVersions(Integer.valueOf(contentId));
		} catch (Exception exe) {
			log.error("Error removing all old contentversions", exe);
		}
	}

	public void removeContentVersion(int contentVersionId) {
		try {
			getClientService().removeContentVersion(Integer.valueOf(contentVersionId));
		} catch (Exception exe) {
			log.error("Error removing content version", exe);
		}

	}

	public Vector getNotReferencedUnits(ViewDocumentValue viewDocumentDao) throws Exception {
		UnitValue[] ud = getClientService().getNotReferencedUnits(viewDocumentDao);
		return arrayToVector(ud);
	}

	public Integer[] getAllPictures4Unit(int unitId) throws Exception {
		Integer[] udd = getClientService().getAllPictures4Unit(Integer.valueOf(unitId));
		if (udd == null) udd = new Integer[0];
		return udd;
	}

	public Vector getAllDocuments4Unit(int unitId) throws Exception {
		Integer[] udd = getClientService().getAllDocuments4Unit(Integer.valueOf(unitId));
		return arrayToVector(udd);
	}

	public DocumentSlimValue[] getAllSlimDocumentValues(Integer unitId) throws Exception {
		DocumentSlimValue[] dsv = getClientService().getAllSlimDocumentValues(unitId);
		if (dsv == null) dsv = new DocumentSlimValue[0];
		return dsv;
	}

	public PictureSlimstValue[] getAllSlimPictureValues(Integer unitId) throws Exception {
		PictureSlimstValue[] psv = getClientService().getAllSlimPictures4Unit(unitId);
		if (psv == null) psv = new PictureSlimstValue[0];
		return psv;
	}

	public ContentValue checkOut(int contentId, boolean force) throws UserException, AlreadyCheckedOutException {
		try {
			ContentValue cdao = getClientService().checkOut(Integer.valueOf(contentId), force);
			// will be only called, if the content was been successfully
			// checkedOut!
			checkOutPages.add(Integer.valueOf(contentId));
			return cdao;
		} catch (Exception exception) {
			if (exception instanceof AlreadyCheckedOutException) { throw (AlreadyCheckedOutException) exception; }
			if (exception.getCause() != null && exception.getCause() instanceof AlreadyCheckedOutException) { throw (AlreadyCheckedOutException) exception.getCause(); }
			UserException ue = new UserException("Error checking out: " + exception.getMessage());
			log.error("Error checking out ", exception);
			throw ue;
		}
	}

	/**
	 * check-in or save
	 */
	public void checkIn(ContentValue contentDao) throws Exception {
		if (!contentDao.isCreateNewVersion()) {
			dbHelper.deleteContentVersions(contentDao.getContentId().intValue());
		}
		try {
			checkOutPages.remove(new Integer(contentDao.getContentId().intValue()));
		} catch (Exception exe) {
		}
		getClientService().checkIn(contentDao);
		if (log.isDebugEnabled()) {
			log.debug("contentDao " + contentDao.getHeading());
		}
	}

	/**
	 * cancel
	 */
	public void checkIn(int contentId) {
		try {
			getClientService().checkIn4ContentId(Integer.valueOf(contentId));
			checkOutPages.remove(new Integer(contentId));
		} catch (Exception exe) {
			log.error("Error checking in", exe);
		}
	}

	public void saveContent(int contentId, String content) throws Exception {
		getClientService().saveContent(Integer.valueOf(contentId), content);
	}

	public void removeDocument(int documentId) throws Exception {
		getClientService().removeDocument(Integer.valueOf(documentId));
	}

	/**
	 * @deprecated Causes a OutOfMemory on the Serverside if called many times.
	 * @param documentId
	 * @return the name of this document
	 * @throws java.lang.Exception
	 */
	@Deprecated
	public String getDocumentName(int documentId) throws Exception {
		if (getDbHelper().checkDocumentName(documentId)) {
			String tempString = getClientService().getDocumentName(Integer.valueOf(documentId));
			getDbHelper().setDocumentName(documentId, tempString);
			return tempString;
		}
		return getDbHelper().getDocumentName(documentId);
	}

	public byte[] getPictureData(int pictureId) throws Exception {
		return getClientService().getPictureData(Integer.valueOf(pictureId));
	}

	public PictureSlimValue getPicture(int pictureId) throws Exception {
		return getClientService().getPicture(Integer.valueOf(pictureId));
	}

	public String getFileName(int pictureId) throws Exception {
		return getClientService().getPictureFileName(Integer.valueOf(pictureId));
	}

	public String getAltText(int pictureId) throws Exception {
		return getClientService().getPictureAltText(Integer.valueOf(pictureId));
	}

	public void updatePictureAltText(int pictureId, String altText) throws Exception {
		getClientService().updatePictureAltText(Integer.valueOf(pictureId), altText);
	}

	public void updatePictureData(int pictureId, byte[] picture, String mimeType, byte[] thumbnail) throws Exception {
		getClientService().updatePictureData(Integer.valueOf(pictureId), picture, mimeType, thumbnail);
	}

	public byte[] getThumbnail(int pictureId) throws Exception {
		String thumbPath = "ejbimage?typ=t&id=" + pictureId;
		File svgFile = new File(Constants.SVG_CACHE + this.encodeSvgImageName(thumbPath) + ".png");
		byte[] svgByte = null;
		if (svgFile.exists()) {
			svgByte = new byte[(int) svgFile.length()];
			try {
				new FileInputStream(svgFile).read(svgByte);
			} catch (Exception exe) {
				throw new UserException("First found image cant be found anymore at location " + svgFile.getAbsoluteFile());
			}
		} else {
			try {
				File cacheDir = new File(Constants.SVG_CACHE);
				cacheDir.mkdirs();

				// Read the response body.
				svgByte = getClientService().getPicture(pictureId).getThumbnail();
				// svgByte =
				// getContentService().getThumbnail(Integer.valueOf(pictureId));

				OutputStream out = new BufferedOutputStream(new FileOutputStream(svgFile));
				out.write(svgByte, 0, svgByte.length);
				out.flush();
				out.close();
			} catch (Exception e) {
				log.error("Image with Id " + pictureId + " has not been found");
			}
		}
		return svgByte;
	}

	public void removePicture(int pictureId) throws Exception {
		getClientService().removePicture(Integer.valueOf(pictureId));
	}

	public int addPicture2Unit(int unitId, byte[] thumbnail, byte[] picture, String mimeType, String altText, String pictureName) throws Exception {
		return getClientService().addPicture2Unit(Integer.valueOf(unitId), thumbnail, picture, mimeType, altText, pictureName);
	}

	public int addPicture2Unit(int unitId, byte[] thumbnail, byte[] picture, byte[] preview, String mimeType, String altText, String pictureName) throws Exception {
		return getClientService().addPictureWithPreview2Unit(Integer.valueOf(unitId), thumbnail, picture, preview, mimeType, altText, pictureName);
	}

	public void setPicture4Unit(int unitId, int pictureId) throws Exception {
		getClientService().setPicture4Unit(Integer.valueOf(unitId), Integer.valueOf(pictureId));
	}

	public void setPicture4Person(long personId, int pictureId) throws Exception {
		getClientService().setPicture4Person(Long.valueOf(personId), Integer.valueOf(pictureId));
	}

	public ViewComponentValue saveViewComponent(ViewComponentValue viewComponentValue) throws ViewComponentLinkNameAlreadyExisting, ViewComponentLinkNameIsEmptyException, ViewComponentNotFound, UserException {
		ViewComponentValue vcReturn = null;
		try {
			vcReturn = getClientService().saveViewComponent(viewComponentValue);
		} catch (Exception ue) {
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
			throw new UserException(ue);
		}
		return vcReturn;
	}

	public Object[] getPerson4Name(String firstName, String lastName) throws Exception {
		return getClientService().getPerson4Name(firstName, lastName);
	}

	/**
	 * Component department
	 * 
	 * @param name
	 * @throws Exception
	 */
	public long createDepartment(String name, int unitId) throws Exception {
		return getClientService().createDepartment(name, Integer.valueOf(unitId));
	}

	public long createPerson(PersonValue value) throws Exception {
		return getClientService().createPerson(value);
	}

	/**
	 * Component address
	 */
	public long createAddress(AddressValue value) throws Exception {
		return getClientService().createAddress(value);
	}

	/**
	 * Component get all departments
	 * 
	 * @return departments daos
	 * @throws Exception
	 */
	public Vector getDepartments4Name(String name) throws Exception {
		return arrayToVector(getClientService().getDepartments4Name(name));
	}

	/**
	 * Component get all departments
	 * 
	 * @return departments daos
	 * @throws Exception
	 */
	public DepartmentValue getDepartment(long departmentId) throws Exception {
		return getClientService().getDepartment(Long.valueOf(departmentId));
	}

	/**
	 * Component get all units
	 * 
	 * @return unit daos
	 * @throws Exception
	 */
	public Vector getUnits4Name(String name) throws Exception {
		return arrayToVector(getClientService().getUnits4Name(name));
	}

	public UnitValue[] getUnits4User(String userName) throws Exception {
		UnitValue[] units = getClientService().getUnits4User(userName);
		if (units == null) units = new UnitValue[0];
		return units;
	}

	/**
	 * Component get person
	 * 
	 * @param personId
	 * @return personValue
	 * @throws Exception
	 */
	public PersonValue getPerson(long personId) throws Exception {
		return getClientService().getPerson(Long.valueOf(personId));
	}

	public Vector getAllViewComponents4Status(int status) throws Exception {
		return arrayToVector(getClientService().getAllViewComponents4Status(Integer.valueOf(this.getViewDocumentId()), status));
	}

	/**
	 * 
	 * @param unitId
	 * @return Editions related to the given UnitID and the selected
	 *         ViewDocumentId
	 * @throws Exception
	 */
	public Vector getEditions(int unitId) throws Exception {
		EditionValue[] ud = getClientService().getEditions(Integer.valueOf(unitId), Integer.valueOf(this.getViewDocumentId()));
		return arrayToVector(ud);
	}

	/*
	 * Deploy
	 */
	public void createEdition(String comment, int rootViewComponentId, boolean deploy, boolean showMessage) throws UserException {
		try {
			getClientService().createEdition(comment, Integer.valueOf(rootViewComponentId), deploy, showMessage);
		} catch (Exception ue) {
			log.error("Error creating edition", ue);
			throw new UserException(ue);
		}
	}

	public void removeEdition(int editionId) throws Exception {
		getClientService().removeEdition(Integer.valueOf(editionId));
	}

	public void setActiveEdition(int editionId) throws Exception {
		getClientService().setActiveEdition(Integer.valueOf(editionId), true);
	}

	/**
	 * 
	 * @param unitId
	 * @param status
	 * @return vector of ViewComponentValue
	 */
	public Vector getViewComponents4Status(int unitId, int status) throws Exception {
		return arrayToVector(getClientService().getAllViewComponents4UnitAndStatus(Integer.valueOf(unitId), Integer.valueOf(this.getViewDocumentId()), status));
	}

	/**
	 * Returns all children of a ViewComponent, that contains a unit.
	 * 
	 * @param viewComponentId
	 *                The viewComponentId
	 * @return Array of Strings[][]
	 *         <ul>
	 *         <li>[i][0] contains ViewComponentId</li>
	 *         <li>[i][1] contains UnitId</li>
	 *         <li>[i][2] contains UnitName</li>
	 *         </ul>
	 */
	public ViewIdAndUnitIdValue[] getAllViewComponentsWithUnits(int viewComponentId) {
		ViewIdAndUnitIdValue[] retVal = null;
		try {
			retVal = getClientService().getAllViewComponentsWithUnits(Integer.valueOf(viewComponentId));
		} catch (Exception exe) {
			log.error("Error getting all vc with units", exe);
		}
		if (retVal == null) retVal = new ViewIdAndUnitIdValue[0];
		return retVal;
	}

	/**
	 * Component get address
	 * 
	 * @param addressId
	 * @return address data
	 * @throws Exception
	 */
	public AddressValue getAddress(long addressId) throws Exception {
		return getClientService().getAddress(Long.valueOf(addressId));
	}

	/**
	 * Component get Talktime
	 * 
	 * @param talktimeId
	 * @return talktime data
	 * @throws Exception
	 */
	public TalktimeValue getTalktime(long talktimeId) throws Exception {
		return getClientService().getTalktime(Long.valueOf(talktimeId));
	}

	public ViewComponentValue insertViewComponent(int childId, String reference, String displayLinkName, String linkDescription, int position) throws Exception {
		return getClientService().insertViewComponent(Integer.valueOf(childId), Integer.valueOf(this.getViewDocumentId()), reference, displayLinkName, linkDescription, position);
	}

	public ViewComponentValue addFirstViewComponent(int parentId, String reference, String displayLinkName, String linkDescription) throws Exception {
		return getClientService().addFirstViewComponent(Integer.valueOf(parentId), Integer.valueOf(this.getViewDocumentId()), reference, displayLinkName, linkDescription);
	}

	public void setTreeModel(CmsTreeModel treeModel) {
		this.treeModel = treeModel;
	}

	public void setStatus4ViewComponentId(int vcId, int status) {
		try {
			PageNode entry = treeModel.findEntry4Id((PageNode) treeModel.getRoot(), vcId);
			entry.setStatus(status);
		} catch (Exception ex) {
		}
	}

	public PageNode findEntry4Id(int id) {
		return treeModel.findEntry4Id((PageNode) treeModel.getRoot(), id);
	}

	public ViewIdAndInfoTextValue[] getAllChildrenNamesWithUnit(int viewComponentId) {
		ViewIdAndInfoTextValue[] retarr = null;
		try {
			retarr = getClientService().getAllChildrenNamesWithUnit(Integer.valueOf(viewComponentId));
		} catch (Exception exe) {
			log.error("Error getallchildrennameswithunit", exe);
		}
		return retarr;
	}

	public String[] getAnchors(int contentId) {
		String[] retArr = null;
		try {
			retArr = getClientService().getAnchors(Integer.valueOf(contentId));
		} catch (Exception exe) {
			log.error("Error getting anchors", exe);
		}
		return retArr;
	}

	public SiteValue createSite(SiteValue vo) {
		try {
			return getClientService().createSite(vo);
		} catch (Exception exe) {
			log.error("Error creating site", exe);
			return null;
		}
	}

	public void removeSite(int siteId) {
		try {
			getClientService().removeSite(Integer.valueOf(siteId));
		} catch (Exception exe) {
			log.error("Error removing site", exe);
		}
	}

	public void updateSite(SiteValue vo) {
		try {
			getClientService().updateSite(vo);
		} catch (Exception exe) {
			log.error("Error updating site", exe);
		}
	}

	public SiteValue[] getAllSites() {
		try {
			return getClientService().getAllSites();
		} catch (Exception exe) {
			log.error("Error get all sites", exe);
			return new SiteValue[0];
		}
	}

	public SiteValue[] getAllSites4CurrentUser() {
		try {
			return getClientService().getAllSites4CurrentUser();
		} catch (Exception exe) {
			log.error("Error get all sites for current user", exe);
			return new SiteValue[0];
		}
	}

	public UserValue[] getAllUsersForAllSites() {
		try {
			return getClientService().getAllUsersForAllSites();
		} catch (Exception exe) {
			log.error("Error getAllUsersForAllSites", exe);
			return new UserValue[0];
		}
	}

	public UserValue[] getAllUsers4OwnSites() {
		try {
			return getClientService().getAllUserOwnSites();
		} catch (Exception exe) {
			log.error("Error getAllUsers4OwnSites", exe);
			return new UserValue[0];
		}
	}

	public String[] getConnectedUsersForSite(int siteId) {
		try {
			return getClientService().getConnectedUsersForSite(Integer.valueOf(siteId));
		} catch (Exception exe) {
			log.error("Error getConnectedUsersForSite", exe);
			return new String[0];
		}
	}

	public void setConnectedUsersForSite(int siteId, String[] userIds) {
		try {
			getClientService().setConnectedUsersForSite(Integer.valueOf(siteId), userIds);
		} catch (Exception exe) {
			log.error("Error set connected users for site", exe);
		}
	}

	public void setSiteConfig(int siteId, String config) {
		try {
			getClientService().setSiteConfig(Integer.valueOf(siteId), config);
			if (siteId == activeSiteId) {
				user.setSiteConfigXML(config);
				Parameters.loadRolesetForActiveSite();
			}
		} catch (Exception exe) {
			log.error("Error set site config", exe);
		}
	}

	public String getSiteConfig(int siteId) {
		try {
			return getClientService().getSiteConfig(Integer.valueOf(siteId));
		} catch (Exception exe) {
			log.error("Error getsiteconfig", exe);
			return "";
		}
	}

	public HostValue createHost(String hostName) throws Exception {
		return (getClientService().createHost(hostName));
	}

	public HostValue[] getAllHosts() {
		HostValue[] hv = null;
		try {
			hv = getClientService().getAllHosts();
			if (hv == null) hv = new HostValue[0];
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return (hv);
	}

	public HostValue[] getHosts() {
		HostValue[] hv = null;
		try {
			hv = getClientService().getHosts();
			if (hv == null) hv = new HostValue[0];
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return hv;
	}

	public HostValue[] getAllUnassignedHosts() {
		HostValue[] hv = null;
		try {
			hv = getClientService().getAllUnassignedHosts();
			if (hv == null) hv = new HostValue[0];
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return (hv);
	}

	public HostValue[] getHostsForSite(int siteId) {
		HostValue[] hv = null;
		try {
			hv = getClientService().getHostsForSite(Integer.valueOf(siteId));
			if (hv == null) hv = new HostValue[0];
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return hv;
	}

	public void removeHost(String hostName) {
		try {
			getClientService().removeHost(hostName);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public SiteValue getSiteForName(String siteName) {
		SiteValue sv = null;
		try {
			sv = getClientService().getSiteForName(siteName);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return (sv);
	}

	public void setSite(String hostName, String siteName) {
		try {
			getClientService().setSite(hostName, siteName);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void setSite(String hostName, int siteId) {
		try {
			getClientService().setSite(hostName, Integer.valueOf(siteId));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void setStartPage(String hostName, String vcId) {
		try {
			getClientService().setStartPage(hostName, vcId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public String getSite(String hostName) {
		String result = "";
		try {
			result = getClientService().getSite(hostName);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return (result);
	}

	public SiteValue getCurrentSite() {
		try {
			return getClientService().getCurrentSite(Integer.valueOf(this.getSiteId()));
		} catch (Exception e) {
			log.error("Error getting current site: " + e.getMessage(), e);
		}
		return null;
	}

	public String getStartPage(String hostName) {
		String result = "";
		try {
			result = getClientService().getStartPage(hostName);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return (result);
	}

	public void removeSiteFromHost(String hostName) {
		try {
			getClientService().removeSiteFromHost(hostName);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void removeStartpageFromHost(String hostName) {
		try {
			getClientService().removeStartpageFromHost(hostName);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void setRedirectUrl(String hostName, String redirectUrl) {
		try {
			getClientService().setRedirectUrl(hostName, redirectUrl);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void setRedirectHost(String hostName, String redirectHostName) {
		try {
			getClientService().setRedirectHost(hostName, redirectHostName);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void setLiveServer(String hostName, boolean liveServer) {
		try {
			getClientService().setLiveServer(hostName, liveServer);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/* Safeguard start */

	public void addSimplePwRealmToVC(Integer simplePwRealmId, Integer viewComponentId, String neededRole, Integer loginPageId) throws Exception {
		if (loginPageId == null) loginPageId = new Integer(-1);
		getClientService().addSimplePwRealmToVC(simplePwRealmId, viewComponentId, neededRole, loginPageId);
	}

	public void editSimplePwRealm(RealmSimplePwValue simplePwRealmValue) {
		getClientService().editSimplePwRealm(simplePwRealmValue);
	}

	public void addSqlDbRealmToVC(Integer jdbcRealmId, Integer viewComponentId, String neededRole, Integer loginPageId) throws Exception {
		if (loginPageId == null) loginPageId = new Integer(-1);
		getClientService().addSqlDbRealmToVC(jdbcRealmId, viewComponentId, neededRole, loginPageId);
	}

	public int addSimpleRealmToSite(String realmName, int siteId, String loginPageId) {
		int pk = -1;

		try {
			pk = getClientService().addSimpleRealmToSite(realmName, Integer.valueOf(siteId), loginPageId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return pk;
	}

	public RealmSimplePwValue[] getSimplePwRealmsForUser(String user) {
		RealmSimplePwValue[] val = null;
		val = getClientService().getSimplePwRealmsForUser(user);
		if (val == null) {
			val = new RealmSimplePwValue[0];
		}
		return val;
	}

	public RealmSimplePwValue[] getSimplePwRealmsForSite(Integer siteId) {
		RealmSimplePwValue[] val = null;
		try {
			val = getClientService().getSimplePwRealmsForSite(siteId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}
		if (val == null) {
			val = new RealmSimplePwValue[0];
		}
		return val;
	}

	public RealmSimplePwValue[] getSimplePwRealms4CurrentUser() {
		RealmSimplePwValue[] val = null;
		try {
			val = getClientService().getSimplePwRealms4CurrentUser(new Integer(activeSiteId));
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}

		return val;

	}

	public RealmSimplePwUserValue[] getUserForSimplePwRealm(Integer simplePwRealmId) {
		RealmSimplePwUserValue[] val = null;
		try {
			val = getClientService().getUserForSimplePwRealm(simplePwRealmId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}
		if (val == null) {
			val = new RealmSimplePwUserValue[0];
		}
		return val;
	}

	public int addUserToSimpleRealm(Integer simplePwRealmId, RealmSimplePwUserValue simplePwRealmUserValue) {
		int pk = -1;

		try {
			pk = getClientService().addUserToSimpleRealm(simplePwRealmId, simplePwRealmUserValue);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}

		return pk;
	}

	public boolean deleteSimplePwRealmUser(Integer simplePwRealmUserId) {
		boolean del = false;

		try {
			del = getClientService().deleteSimplePwRealmUser(simplePwRealmUserId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}

		return del;
	}

	public ActiveRealmValue getActiveRealm(Integer viewComponentId) {
		ActiveRealmValue val = new ActiveRealmValue(false, false, false, false, false, -1, "", "", null);

		try {
			val = getClientService().getActiveRealm(viewComponentId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}

		return val;
	}

	public Integer getFirstProtectedParentId(Integer viewComponentId) {
		try {
			return getClientService().getFirstProtectedParentId(viewComponentId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public boolean deleteRealmAtVC(Integer viewComponentId) {
		boolean ret = false;

		try {
			ret = getClientService().deleteRealmAtVC(viewComponentId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}

		return ret;

	}

	public Integer addSqlDbRealmToSite(Integer siteId, RealmJdbcValue jdbcRealmValue) {
		Integer ret = new Integer(-1);

		try {
			ret = getClientService().addSqlDbRealmToSite(siteId, jdbcRealmValue);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}

		return ret;
	}

	public RealmJdbcValue[] getSqlDbRealmsForSite(Integer siteId) {
		RealmJdbcValue[] val = null;

		try {
			val = getClientService().getSqlDbRealmsForSite(siteId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}

		return val;
	}

	public boolean deleteSqlDbRealm(Integer jdbcRealmId) {
		boolean del = false;

		try {
			del = getClientService().deleteSqlDbRealm(jdbcRealmId);
		} catch (RuntimeException rex) {
			log.error(rex.getMessage());
		}

		return del;
	}

	public void editSqlDbRealm(RealmJdbcValue jdbcRealmValue) {
		try {
			getClientService().editSqlDbRealm(jdbcRealmValue);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	public Integer addLdapRealmToSite(Integer siteId, RealmLdapValue ldapRealmValue) {
		Integer pk = new Integer(-1);

		try {
			pk = getClientService().addLdapRealmToSite(siteId, ldapRealmValue);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}

		return pk;
	}

	public void deleteLdapRealm(Integer ldapRealmId) {
		try {
			getClientService().deleteLdapRealm(ldapRealmId);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	public void editLdapRealm(RealmLdapValue ldapRealmValue) {
		try {
			getClientService().editLdapRealm(ldapRealmValue);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	public RealmLdapValue[] getLdapRealmsForSite(Integer siteId) {
		RealmLdapValue[] val = null;

		try {
			val = getClientService().getLdapRealmsForSite(siteId);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}

		return val;
	}

	public void addLdapRealmToVC(Integer viewComponentId, Integer ldapRealmId, String neededRole, Integer loginPageId) {
		try {
			if (loginPageId == null) loginPageId = new Integer(-1);
			getClientService().addLdapRealmToVC(viewComponentId, ldapRealmId, neededRole, loginPageId);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	public Integer addJaasRealmToSite(Integer siteId, RealmJaasValue jaasRealmValue) {
		Integer pk = new Integer(-1);

		try {
			pk = getClientService().addJaasRealmToSite(siteId, jaasRealmValue);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}

		return pk;
	}

	public void deleteJaasRealm(Integer jaasRealmId) {
		try {
			getClientService().deleteJaasRealm(jaasRealmId);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	public void editJaasRealm(RealmJaasValue jaasRealmValue) {
		try {
			getClientService().editJaasRealm(jaasRealmValue);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	public RealmJaasValue[] getJaasRealmsForSite(Integer siteId) {
		RealmJaasValue[] val = null;

		try {
			val = getClientService().getJaasRealmsForSite(siteId);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}

		return val;
	}

	public void addJaasRealmToVC(Integer viewComponentId, Integer jaasRealmId, String neededRole, Integer loginPageId) {
		try {
			if (loginPageId == null) loginPageId = new Integer(-1);
			getClientService().addJaasRealmToVC(viewComponentId, jaasRealmId, neededRole, loginPageId);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	/* Safeguard end */

	/**
	 * 
	 */
	private class OkListener implements ActionListener {
		private final PanCheckInPages pan;
		private final JDialog dlg;

		public OkListener(PanCheckInPages pan, JDialog dlg) {
			this.pan = pan;
			this.dlg = dlg;
		}

		public void actionPerformed(ActionEvent e) {
			dlg.setVisible(false);
			ModifiedPagesTableModel model = pan.getModel();
			Iterator it = model.getDataVector().iterator();
			while (it.hasNext()) {
				Object[] elements = (Object[]) it.next();
				if (((Boolean) elements[0]).booleanValue()) {
					try {
						checkIn((ContentValue) elements[4]);
					} catch (Exception ex) {
						log.error(ex.getMessage());
					}
				} else {
					try {
						checkIn(((ContentValue) elements[4]).getContentId().intValue());
					} catch (Exception ex) {
						log.error(ex.getMessage());
					}
				}
			}
			try {
				if (log.isDebugEnabled()) {
					log.debug("Calling logout on server");
				}
				getClientService().logout();
			} catch (Exception exe) {
			}
			checkOutPages.clear();
			log.info("Goodbye!");
		}

	}

	public String getSvgUrl(String svgContent) throws UserException {
		File svgFile = new File(Constants.SVG_CACHE + this.encodeSvgImageName(svgContent) + ".png");
		if (svgFile.exists()) { return svgFile.getAbsolutePath(); }
		try {
			fetchRemoteSvg(svgContent, svgFile);
		} catch (Exception e) {
			UserException ue = new UserException("Error fetching svg-url: " + e.getMessage());
			if (log.isDebugEnabled()) {
				log.debug("svgContent: " + svgContent);
				log.debug("svgFile: " + Constants.SVG_CACHE + this.encodeSvgImageName(svgContent) + ".png");
				log.debug("svgUrl: " + Constants.CMS_PATH_WYSIWYGIMAGE + svgContent);
			}
			log.error("Error fetching svg-url: ", e);
			throw ue;
		}
		return svgFile.getAbsolutePath();
	}

	private byte[] fetchRemoteSvg(String svgUrl, File svgFile) throws Exception {
		// create directory if it does not exist
		File cacheDir = new File(Constants.SVG_CACHE);
		cacheDir.mkdirs();

		// Read the response body.
		byte[] svgData = HttpClientWrapper.getInstance().getByte(Constants.CMS_PATH_WYSIWYGIMAGE + svgUrl);
		// svgData = this.getSizedThumbNail(svgData);

		OutputStream out = new BufferedOutputStream(new FileOutputStream(svgFile));
		out.write(svgData, 0, svgData.length);
		out.flush();
		out.close();

		return svgData;
	}

	public void clearSvgCache() {
		File cacheDir = new File(Constants.SVG_CACHE);
		this.delete(cacheDir);
	}

	/**
	 * Recursively deletes a directory (or file)
	 * 
	 * @param file
	 */
	private void delete(File file) {
		if (file.isDirectory()) {
			String[] list = file.list();
			for (int i = 0; i < list.length; i++) {
				delete(new File(file, list[i]));
			}
		}
		file.delete();
	}

	// Admin SiteGroups
	public SiteValue[] getAllNotAssignedSites() {
		try {
			return this.getClientService().getAllNotAssignedSites();
		} catch (Exception e) {
			log.error("Error getting all not assigned to a SiteGroup Sites: " + e.getMessage(), e);
		}
		return new SiteValue[0];
	}

	public SiteValue[] getSites4SiteGroup(SiteGroupValue siteGroupValue) {
		try {
			return this.getClientService().getSites4SiteGroup(siteGroupValue);
		} catch (Exception e) {
			log.error("Error getting all Sites for SiteGroup " + siteGroupValue.getSiteGroupId() + ": " + e.getMessage(), e);
		}
		return new SiteValue[0];
	}

	public SiteGroupValue[] getAllSiteGroups() {
		try {
			return this.getClientService().getAllSiteGroups();
		} catch (Exception e) {
			log.error("Error loading all SiteGroups: " + e.getMessage(), e);
		}
		return new SiteGroupValue[0];
	}

	public SiteGroupValue createSiteGroup(SiteGroupValue value) {
		try {
			return this.getClientService().createSiteGroup(value);
		} catch (Exception e) {
			log.error("Error creating SiteGroups: " + e.getMessage(), e);
			return null;
		}
	}

	public void updateSiteGroup(SiteGroupValue value) {
		try {
			this.getClientService().updateSiteGroup(value);
		} catch (Exception e) {
			log.error("Error updating SiteGroups: " + e.getMessage(), e);
		}
	}

	public void removeSiteGroup(SiteGroupValue value) {
		try {
			this.getClientService().removeSiteGroup(value);
		} catch (Exception e) {
			log.error("Error loading all SiteGroups: " + e.getMessage(), e);
		}
	}

	// -- Admin SiteGroups

	public SiteValue[] getAllRelatedSites(int siteId) {
		try {
			return this.getClientService().getAllRelatedSites(Integer.valueOf(siteId));
		} catch (Exception e) {
			log.error("Error getting all related Sites for Site " + siteId + ": " + e.getMessage(), e);
		}
		return null;
	}

	public ViewDocumentValue[] getAllViewDocuments4Site(int siteId) {
		try {
			return this.getClientService().getAllViewDocuments4Site(Integer.valueOf(siteId));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public ViewDocumentValue getViewDocument4ViewComponent(int viewComponentId) {
		try {
			return this.getClientService().getViewDocument4ViewComponent(Integer.valueOf(viewComponentId));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public UserUnitsGroupsValue[] getUserUnitsGroups4UnitAndGroup(int unitId, int groupId) throws Exception {
		return getClientService().getUserUnitsGroups4UnitAndGroup(Integer.valueOf(unitId), Integer.valueOf(groupId));
	}

	// TODO: replace by better algorithm with diffusion
	private int encodeSvgImageName(String svgImageName) {
		if (svgImageName != null && !"".equalsIgnoreCase(svgImageName)) return svgImageName.hashCode();

		return 0;
	}

	/**
	 * @see de.juwimm.cms.util.Communication#searchXmlByUnit(int,
	 *      java.lang.String, boolean)
	 */
	public XmlSearchValue[] searchXmlByUnit(int unitId, String xpathQuery, boolean parentSearch) throws Exception {
		try {
			XmlSearchValue[] value = getClientService().searchXmlByUnit(Integer.valueOf(unitId), Integer.valueOf(this.getViewDocumentId()), xpathQuery, parentSearch);
			if (value != null) return value;
			return new XmlSearchValue[0];
		} catch (Exception e) {
			log.warn("Error calling searchXmlByUnit - unitId " + unitId + " xPathQuery \"" + xpathQuery + "\" parentSearch " + parentSearch + ": " + e.getMessage(), e);
			return new XmlSearchValue[0];
		}
	}

	/** @see de.juwimm.cms.util.Communication#searchXml(int, java.lang.String) */
	public XmlSearchValue[] searchXml(int siteId, String xpathQuery) throws Exception {
		try {
			XmlSearchValue[] value = getClientService().searchXml(Integer.valueOf(siteId), xpathQuery);
			if (value != null) return value;
			return new XmlSearchValue[0];
		} catch (Exception e) {
			log.warn("Error calling searchXml - siteId " + siteId + " xPathQuery \"" + xpathQuery + "\": " + e.getMessage(), e);
			return new XmlSearchValue[0];
		}
	}

	public UnitValue[] getAllUnits4Site(Integer siteId) {
		try {
			return getClientService().getAllUnits4Site(siteId);
		} catch (Exception e) {
			log.error("Error getting all units for site " + siteId + ": " + e.getMessage(), e);
		}
		return new UnitValue[0];
	}

	public ShortLinkValue[] getAllShortLinks4Site(Integer siteId) {
		try {
			return getClientService().getAllShortLinks4Site(siteId);
		} catch (Exception e) {
			log.error("Error getting all shortLinks for site " + siteId + ": " + e.getMessage(), e);
		}
		return new ShortLinkValue[0];
	}

	public HostValue saveHost(HostValue hostValue) {
		try {
			hostValue = getClientService().saveHost(hostValue);
		} catch (Exception e) {
			log.error("Error saving host " + hostValue.getHostName() + ": " + e.getMessage(), e);
		}
		return hostValue;
	}

	public ShortLinkValue createShortLink(ShortLinkValue shortLinkValue) {
		try {
			shortLinkValue = getClientService().createShortLink(shortLinkValue);
		} catch (Exception e) {
			log.error("Error creating shortLinkValue " + shortLinkValue.getShortLink() + ": " + e.getMessage(), e);
		}
		return shortLinkValue;
	}

	public ShortLinkValue saveShortLink(ShortLinkValue shortLinkValue) {
		try {
			shortLinkValue = getClientService().saveShortLink(shortLinkValue);
		} catch (Exception e) {
			log.error("Error saving shortLinkValue " + shortLinkValue.getShortLink() + ": " + e.getMessage(), e);
		}
		return shortLinkValue;
	}

	public void deleteShortLink(ShortLinkValue shortLinkValue) {
		try {
			getClientService().deleteShortLink(shortLinkValue);
		} catch (Exception e) {
			log.error("Error deleting shortLinkValue " + shortLinkValue.getShortLink() + ": " + e.getMessage(), e);
		}
	}

	public void exportXlsPersonData(File outputFile) {
		try {
			log.info("exportXlsPersonData");
			ClientServiceSpring cs = getClientService();
			InputStream is = cs.exportXlsPersonData();
			FileOutputStream fos = new FileOutputStream(outputFile);
			int read = 0;
			while ((read = is.read()) != -1) {
				fos.write(read);
			}
			fos.close();
		} catch (Exception exe) {
			log.error("Error exporting PersonData", exe);
		}
	}

	public Integer addOrUpdateDocument(File file, int unitId, String fileName, String mimeType, Integer documentId) {
		InputStream fis = null;
		try {
			fis = new BufferedInputStream(new FileInputStream(file));
			return getClientService().addOrUpdateDocument(fis, unitId, fileName, mimeType, documentId);
		} catch (Exception re) {
			log.error("Error importing document " + re.getMessage());
		} finally {
			IOUtils.closeQuietly(fis);
		}
		return null;
	}

	/*
	 * Export of an Edition
	 */
	public void createEditionForExport(File outputFile, int viewComponentIdWithUnit) throws Exception {
		log.info("createEditionForExport ");

		InputStream edition = null;
		if (viewComponentIdWithUnit <= 0) {
			edition = getClientService().exportEditionFull();
		} else {
			edition = getClientService().exportEditionUnit(Integer.valueOf(viewComponentIdWithUnit));
		}
		log.info("got answer... ");

		if (log.isDebugEnabled()) log.debug("tmpFile " + outputFile.getName());
		FileOutputStream fos = new FileOutputStream(outputFile);
		IOUtils.copyLarge(edition, fos);
		IOUtils.closeQuietly(edition);
		IOUtils.closeQuietly(fos);
		outputFile = null;
		System.gc();
	}

	public void importEditionFromImport(File file, Integer viewComponentId, boolean useNewIds) {
		InputStream fis = null;
		try {
			fis = new BufferedInputStream(new FileInputStream(file));
			getClientService().importEditionFromImport(fis, viewComponentId, useNewIds);
		} catch (Exception re) {
			log.error("Error importing edition from import", re);
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}

	public void setDefaultViewDocument(String viewType, String language, Integer siteId) {
		getClientService().setDefaultViewDocument(viewType, language, siteId);
	}

	public ViewDocumentValue getDefaultViewDocument4Site(Integer siteId) {
		return getClientService().getDefaultViewDocument4Site(siteId);
	}

	public ViewComponentValue[] getViewComponentChildren(Integer viewComponentId) {
		return getClientService().getViewComponentChildren(viewComponentId);
	}

	public Integer getPictureIdForUnitAndName(Integer unitId, String name) {
		return getClientService().getPictureIdForUnitAndName(unitId, name);
	}

	public Integer getDocumentIdForNameAndUnit(String name, Integer unitId) {
		return getClientService().getDocumentIdForNameAndUnit(name, unitId);
	}

	public ViewComponentValue[] moveViewComponentsUp(Integer[] viewComponentsId) {
		return getClientService().moveViewComponentsUp(viewComponentsId);
	}

	public ViewComponentValue[] moveViewComponentsDown(Integer[] viewComponentsId) {
		return getClientService().moveViewComponentsDown(viewComponentsId);
	}

	public ViewComponentValue[] moveViewComponentsLeft(Integer[] viewComponentsId) {
		return getClientService().moveViewComponentsLeft(viewComponentsId);
	}

	public ViewComponentValue[] moveViewComponentsRight(Integer[] viewComponentsId) {
		return getClientService().moveViewComponentsRight(viewComponentsId);
	}

	public HostValue createHost(HostValue hostValue) {
		return getClientService().createHost(hostValue);
	}

	public void updateHost(HostValue hostValue) {
		getClientService().updateHost(hostValue);
	}

	public List getUnsusedResources4Unit(Integer unitId) {
		return getClientService().getUnusedResources4Unit(unitId);
	}

	public Integer getViewComponentChildrenNumber(Integer[] viewComponentsIds) {
		return getClientService().getViewComponentChildrenNumber(viewComponentsIds);
	}

	public void removeResources(Integer[] pictureIds, Integer[] documentIds) {
		getClientService().removeResources(pictureIds, documentIds);
	}

	public ViewComponentValue[] copyViewComponentToParent(Integer parentId, Integer[] viewComponentsIds, Integer position) {
		return getClientService().copyViewComponentsToParent(parentId, viewComponentsIds, position);
	}
	
	public void removePublishContentVersion(Integer contentId){
		getClientService().removePublishContentVersion(contentId);
	}
}