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
package de.juwimm.cms.common;

import java.io.File;
import java.util.Locale;

import org.tizzit.util.KeyOrderKeptHashMap;

/**
 * <p>Title: Tizzit</p>
 * <p>Description: Enterprise Content Management<</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class Constants {
	public static java.util.ResourceBundle rb;
	public static String CMS_VERSION = "";

	public static KeyOrderKeptHashMap CMS_AVAILABLE_DCF = new KeyOrderKeptHashMap();

	public static String CMS_PATH_WYSIWYGIMAGE = "";
	public static String CMS_PATH_DEMOPAGE = "";
	public static String CMS_PATH_DEMOPAGE_FULLFRAMESET = "index_1.html";
	public static String CMS_PATH_DEMOPAGE_CONTENT = ".html";
	public static String CMS_PATH_DCF = "";
	public static String CMS_PATH_HELP = "";
	public static String CMS_CUSTOMER_STRING = "";

	public static File LAST_LOCAL_UPLOAD_DIR = null;

	public static final String DB_PATH = System.getProperty("user.home") + System.getProperty("file.separator") + ".tizzitCache" + System.getProperty("file.separator");
	public static String SVG_CACHE = null;
	public static String URL_HOST = null;

	public static int SERVER_PORT = 80;
	public static boolean SERVER_SSL = false;
	public static String SERVER_HOST = "";

	public static int CMS_SCREEN_WIDTH = 1024;
	public static int CMS_SCREEN_HEIGHT = 768;

	/* Michaels Changes */
	public static boolean EDIT_CONTENT = false;
	public static boolean SHOW_CONTENT_FROM_DROPDOWN = false;
	/* bis hier */

	public static byte CMS_CLIENT_VIEW = -1;
	public static final byte CLIENT_VIEW_ADMIN = 1;
	public static final byte CLIENT_VIEW_CONTENT = 2;
	public static final byte CLIENT_VIEW_TASK = 3;
	public static final byte CLIENT_VIEW_LOGIN = 4;

	public static Locale CMS_LOCALE = Locale.getDefault();
	public static String CMS_LANGUAGE = CMS_LOCALE.getLanguage();
	public static final String CMS_CLIENT_OPERATING_SYSTEM = System.getProperty("os.name");

	public static final byte TASK_APPROVE = 0;
	public static final byte TASK_MESSAGE = 1;
	public static final byte TASK_REJECTED = 2;
	public static final byte TASK_SYSTEMMESSAGE_ERROR = 3;
	public static final byte TASK_SYSTEMMESSAGE_INFORMATION = 4;

	public static final byte TASK_STATUS_NEW = 0;
	public static final byte TASK_STATUS_VIEWED = 1;
	public static final byte TASK_STATUS_DONE = 2;

	public static final byte ONLINE_STATUS_OFFLINE = 0;
	public static final byte ONLINE_STATUS_ONLINE = 1;
	public static final byte ONLINE_STATUS_UNDEF = 2;

	public static final byte DEPLOY_COMMAND_ADD = 0;
	public static final byte DEPLOY_COMMAND_REMOVE = 1;
	public static final byte DEPLOY_COMMAND_DELETE = 2;
	public static final byte DEPLOY_COMMAND_MODIFY = 3;

	public static final int DEPLOY_TYPE_FULL = 0;
	public static final int DEPLOY_TYPE_UNIT = 1;
	public static final int DEPLOY_TYPE_PAGE = 2;
	public static final int DEPLOY_TYPE_ROOT = 3;

	/* This is the Status from viewcomponent.status */
	public static final byte DEPLOY_STATUS_EDITED = 0;
	public static final byte DEPLOY_STATUS_FOR_APPROVAL = 1;
	public static final byte DEPLOY_STATUS_APPROVED = 2;
	public static final byte DEPLOY_STATUS_FOR_DEPLOY = 3;
	public static final byte DEPLOY_STATUS_DEPLOYED = 4;

	/* This is the Field from viewcomponent.view_type */
	public static final byte VIEW_TYPE_UNIT = 1; // may be in use, but currently old parameter. Should be used again
	public static final byte VIEW_TYPE_INTERNAL_LINK = 2; // INTERNER LINK
	public static final byte VIEW_TYPE_EXTERNAL_LINK = 3; // EXTERNER LINK
	public static final byte VIEW_TYPE_CONTENT = 4;
	public static final byte VIEW_TYPE_SYMLINK = 6; // SYMLINK! :)
	public static final byte VIEW_TYPE_SEPARATOR = 7; // SEPARATOR

	/* viewComponent.display_settings */
	public static final byte DISPLAY_SETTING_NEW_NAVIGATION = 1;
	public static final byte DISPLAY_SETTING_NEW_WINDOW = 2;
	public static final byte DISPLAY_SETTING_SHOW_HEADER = 4;

	public static final String ENABLE_CHECKIN = "ci";
	public static final String ENABLE_CHECKOUT = "co";

	/**
	 * 
	 */
	public enum ACTION_STATE {
		CONTENT_APPROVE, CONTENT_4APPROVAL, CONTENT_CANCEL_APPROVAL, CONTENT_EDITED, CONTENT_FINISHED_LOADING, CHANGE_USERACCOUNTS, DEPLOY_STATUS_CHANGED, SHOW_TASK, SHOW_CONTENT, TASK_SELECT, TASK_DESELECT, TASK_VIEW_SELECTED, TASK_VIEW_DESELECTED, TASK_DONE, NEW_TASK_FOR_USER, CHECKIN, CHECKOUT, SAVE, EXIT, LOGOFF, LOGIN, CHANGE_PASSWORD, DEPLOY, SEND2EDITOR, CREATE_UNIT, CONTENT_SELECT, CONTENT_DESELECT, TREE_SELECT, TREE_SELECT_SAVE, TREE_DESELECT, TREE_JUMP, TREE_ENTRY_NAME, TREE_NODE_APPEND, TREE_NODE_BEFORE, TREE_NODE_AFTER, TREE_NODE_DELETE, TREE_LINK_BEFORE, TREE_LINK_AFTER, TREE_LINK_ADD, TREE_SEPARATOR_BEFORE, TREE_SEPARATOR_AFTER, TREE_SEPARATOR_ADD, TREE_JUMP_BEFORE, TREE_JUMP_AFTER, TREE_JUMP_ADD, TREE_SYMLINK_BEFORE, TREE_SYMLINK_AFTER, TREE_SYMLINK_ADD, VIEW_ADMIN, VIEW_ROOT, VIEW_EDITOR, SHOW_VERSION, PROPERTY_PROPAGATION, PROPERTY_DEPROPAGATION, PROPERTY_CONFIGURATION, ADD_BEFORE, ADD_AFTER, ADD_APPEND
	}

	/**
	 * 
	 */
	public enum OPERATING_SYSTEM {
		OS_WINDOWS, OS_LINUX, OS_MACOSX
	}

	public static final String ACTION_CONTENT_APPROVE = "cap";
	public static final String ACTION_CONTENT_4APPROVAL = "c4a";
	public static final String ACTION_CONTENT_CANCEL_APPROVAL = "cac";
	public static final String ACTION_CONTENT_EDITED = "ceed";

	public static final String ACTION_CONTENT_FINISHED_LOADING = "ACTION_CONTENT_FINISHED_LOADING";

	public static final String ACTION_CHANGE_USERACCOUNTS = "acuacc";

	public static final String ACTION_DEPLOY_STATUS_CHANGED = "adsc";

	public static final String ACTION_SHOW_TASK = "asta";
	public static final String ACTION_SHOW_CONTENT = "asco";

	public static final String ACTION_TASK_SELECT = "atss";
	public static final String ACTION_TASK_DESELECT = "atds";
	public static final String ACTION_TASK_VIEW_SELECTED = "atvs";
	public static final String ACTION_TASK_VIEW_DESELECTED = "atvds";
	public static final String ACTION_TASK_DONE = "atdo";
	public static final String ACTION_TASK_VIEW_COMPONENT_REFRESH = "atvcr";

	public static final String ACTION_NEW_TASK_FOR_USER = "antfu";

	public static final String ACTION_CHECKIN = "aci";
	public static final String ACTION_CHECKOUT = "aco";
	public static final String ACTION_SAVE = "sav";

	public static final String ACTION_EXIT = "ape";
	public static final String ACTION_LOGOFF = "apl";
	public static final String ACTION_LOGIN = "actionlogin";

	public static final String ACTION_CHANGE_PASSWORD = "achp";
	public static final String ACTION_SHOW_OPTIONS = "aso";

	public static final String ACTION_DEPLOY = "ady";
	public static final String ACTION_SEND2EDITOR = "as2e";

	public static final String ACTION_CREATE_UNIT = "cu";

	public static final String ACTION_CONTENT_SELECT = "cs";
	public static final String ACTION_CONTENT_DESELECT = "cds";

	public static final String ACTION_TREE_SELECT = "ts";
	public static final String ACTION_TREE_SELECT_SAVE = "tssave";
	public static final String ACTION_TREE_DESELECT = "tds";
	public static final String ACTION_TREE_JUMP = "tj";
	public static final String ACTION_TREE_ENTRY_NAME = "ten";
	public static final String ACTION_TREE_SET_NODE = "tsn";
	public static final String ACTION_TREE_CLICK_NODE = "tcn";
	public static final String ACTION_TREE_RESET_CONSTANTS_CONTENT_VIEW = "rccv";

	public static final String ACTION_TREE_NODE_APPEND = "tnap";
	public static final String ACTION_TREE_NODE_BEFORE = "tnb";
	public static final String ACTION_TREE_NODE_AFTER = "tna";
	public static final String ACTION_TREE_NODE_DELETE = "tnd";

	public static final String ACTION_TREE_LINK_BEFORE = "tlb";
	public static final String ACTION_TREE_LINK_AFTER = "tla";
	public static final String ACTION_TREE_LINK_ADD = "tlad";

	public static final String ACTION_TREE_SEPARATOR_BEFORE = "tslb";
	public static final String ACTION_TREE_SEPARATOR_AFTER = "tsa";
	public static final String ACTION_TREE_SEPARATOR_ADD = "tsad";

	public static final String ACTION_TREE_JUMP_BEFORE = "tjb";
	public static final String ACTION_TREE_JUMP_AFTER = "tja";
	public static final String ACTION_TREE_JUMP_ADD = "tjad";

	public static final String ACTION_TREE_SYMLINK_BEFORE = "tsymlinkb";
	public static final String ACTION_TREE_SYMLINK_AFTER = "tsymlinka";
	public static final String ACTION_TREE_SYMLINK_ADD = "tsymlinkad";

	public static final String ACTION_VIEW_ADMIN = "ADMIN_VIEW";
	public static final String ACTION_VIEW_ROOT = "ROOT_VIEW";
	public static final String ACTION_VIEW_EDITOR = "EDITOR_VIEW";
	public static final String ACTION_VIEW_EDITOR_WITH_SELECTION = "EDITOR_VIEW_WITH_SELECTION";

	public static final String ACTION_STATUSBAR_COUNT = "asbc";
	public static final String ACTION_SHOW_VERSION = "version";

	public static final String ACTION_TREE_REFRESH = "REFRESH";

	public static final String PROPERTY_PROPAGATION = "PROPERTY_PROPAGATION";
	public static final String PROPERTY_DEPROPAGATION = "PROPERTY_DEPROPAGATION";
	public static final String PROPERTY_CONFIGURATION = "PROPERTY_CONFIGURATION";

	public static final String ACTION_MAKE_VIEW_OFFLINE = "makeViewOffline";
	public static String VIEW_COMPONENT_TO_COPY = null;

	public static final int ADD_BEFORE = 0;
	public static final int ADD_AFTER = 1;
	public static final int ADD_APPEND = 2;

	public static final int OS_WINDOWS = 1;
	public static final int OS_LINUX = 2;
	public static final int OS_MACOSX = 3;

	public static final String PUBLISH_VERSION = "PUBLS";

	private Constants() {
	}

	public static boolean isClientOS(int operatingSystem) {
		switch (operatingSystem) {
			case Constants.OS_LINUX:
				if (CMS_CLIENT_OPERATING_SYSTEM.length() >= 5 && CMS_CLIENT_OPERATING_SYSTEM.equals("Linux")) {
					return true;
				}
				break;
			case OS_WINDOWS:
				if (CMS_CLIENT_OPERATING_SYSTEM.length() >= 7 && CMS_CLIENT_OPERATING_SYSTEM.substring(0, 7).equals("Windows")) {
					return true;
				}
				break;
			case OS_MACOSX:
				if (CMS_CLIENT_OPERATING_SYSTEM.startsWith("Mac")) {
					return true;
				}
				break;
			default:
		}
		return false;
	}

	public enum ResourceUsageState {
		Used("used"), Unsused("unused"), UsedInOlderVersions("usedInOlderVersions");
		private final String key;

		ResourceUsageState(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	public enum LiveserverDeployStatus {

		EditionCreated, CreateDeployFileForExport, TransmitDeployFile, FileDeployedOnLiveServer, ImportStarted, ImportCleanDatabase, ImportUnits, ImportResources, ImportDatabaseComponents, ImportViewComponents, ImportHosts, Exception;

	}
}
