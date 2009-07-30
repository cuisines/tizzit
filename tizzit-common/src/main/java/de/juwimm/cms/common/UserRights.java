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

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2002, 2003</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class UserRights {
	/* Assignable Rights */
	public static final String SYMLINK = "symlink";
	public static final String SEPARATOR = "separator";
	public static final String DEPLOY = "deploy";
	public static final String APPROVE = "approve";

	public static final String USER_CREATE = "createUser";
	public static final String USER_DELETE = "deleteUser";
	public static final String USER_CHANGE = "changeUser";

	public static final String UNIT_ADMIN = "unitAdmin";
	public static final String SITE_ROOT = "siteRoot";

	public static final String PAGE_MAKE_INVISIBLE = "makeInvisible";
	public static final String PAGE_OPEN_NEW_NAVIGATION = "openNewNavigation";
	public static final String PAGE_VIEW_METADATA = "viewMetadata";
	public static final String PAGE_STATUSBAR = "pageStatusbar";
	public static final String PAGE_SHOWTYPE = "viewShowtype";
	public static final String PAGE_EDIT_URL_LINKNAME = "editUrlLinkname";
	public static final String PAGE_CHANGE_SEARCH_INDEXED = "changeSearchIndexed";
	public static final String PAGE_CHANGE_XML_SEARCH_INDEXED = "changeXmlSearchIndexed";
	public static final String PAGE_UPDATE_LAST_MODIFIED_DATE = "updatePageLastModifiedDate";

	public static final String VIEW_WEBSTATS = "viewWebstats";
	
	public static final String MANAGE_HOSTS = "manageHosts";
	public static final String MANAGE_SAFEGUARD = "manageSafeGuard";

}
