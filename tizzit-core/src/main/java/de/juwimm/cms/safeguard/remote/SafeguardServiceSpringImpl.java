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
package de.juwimm.cms.safeguard.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbm;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbmImpl;
import de.juwimm.cms.safeguard.model.RealmJaasHbm;
import de.juwimm.cms.safeguard.model.RealmJaasHbmImpl;
import de.juwimm.cms.safeguard.model.RealmJdbcHbm;
import de.juwimm.cms.safeguard.model.RealmJdbcHbmImpl;
import de.juwimm.cms.safeguard.model.RealmLdapHbm;
import de.juwimm.cms.safeguard.model.RealmLdapHbmImpl;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbm;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbmImpl;
import de.juwimm.cms.safeguard.model.RealmSimplePwUserHbm;
import de.juwimm.cms.safeguard.model.RealmSimplePwUserHbmDao;
import de.juwimm.cms.safeguard.model.RealmSimplePwUserHbmImpl;
import de.juwimm.cms.safeguard.realmlogin.JAASRealmLoginManager;
import de.juwimm.cms.safeguard.realmlogin.LDAPRealmLoginManager;
import de.juwimm.cms.safeguard.realmlogin.SafeguardLoginManager;
import de.juwimm.cms.safeguard.realmlogin.SimplePwRealmLoginManager;
import de.juwimm.cms.safeguard.realmlogin.SqlDbRealmLoginManager;
import de.juwimm.cms.safeguard.vo.ActiveRealmValue;
import de.juwimm.cms.safeguard.vo.RealmJaasValue;
import de.juwimm.cms.safeguard.vo.RealmJdbcValue;
import de.juwimm.cms.safeguard.vo.RealmLdapValue;
import de.juwimm.cms.safeguard.vo.RealmSimplePwUserValue;
import de.juwimm.cms.safeguard.vo.RealmSimplePwValue;

/**
 * @see de.juwimm.cms.safeguard.remote.SafeguardServiceSpring
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class SafeguardServiceSpringImpl extends SafeguardServiceSpringBase {
	private static Logger log = Logger.getLogger(SafeguardServiceSpringImpl.class);

	@Override
	protected byte handleLogin(String userName, String password, Integer viewComponentId) {
		if (log.isDebugEnabled()) log.debug("trying login safeguard user " + userName);
		byte login = SafeguardLoginManager.LOGIN_UNAUTHENTICATED;
		try {
			SafeguardLoginManager loginManager = this.getSafeguardLoginManager(userName, password, viewComponentId);
			if (loginManager != null) {
				login = loginManager.login();
			} else {
				login = SafeguardLoginManager.LOGIN_SUCCESSFULLY;
			}
		} catch (Exception e) {
			log.error("Error during log-in on SafeGuard: " + e.getMessage(), e);
		}
		return login;
	}

	@Override
	protected boolean handleIsSafeguardAuthenticationNeeded(Integer viewComponentId, Map safeGuardCookieMap) throws Exception {
		if (log.isDebugEnabled()) log.debug("start isSafeguardAuthenticationNeeded for ViewComponent " + viewComponentId);
		boolean isProtected = false;
		// login-pages must always be accessible
		if (this.isLoginPage(viewComponentId)) {
			if (log.isDebugEnabled()) {
				log.debug("ViewComponent " + viewComponentId + " is a login-page!");
			}
			return false;
		}

		ActiveRealmValue realm = getActiveRealm(viewComponentId);

		if (!realm.isRealmNone()) {
			// if the cookieMap contains the realmKey the user is already authenticated for this page. Therefore pretend that the page is not protected
			if (realm.getRoleNeeded() != null && realm.getRoleNeeded().length() > 0) {
				// append the required role to the realmkey
				realm.setRealmKey(realm.getRealmKey() + "_" + realm.getRoleNeeded().trim().toLowerCase());
			}
			if (!safeGuardCookieMap.containsKey(realm.getRealmKey())) {
				isProtected = true;
			} else {
				if (log.isDebugEnabled()) {
					log.debug("ViewComponent " + viewComponentId + " is protected but user is already authorized");
				}
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("ViewComponent " + viewComponentId + " is protected " + isProtected);
		}
		return isProtected;
	}

	@Override
	protected String handleGetRealmIdAndType(Integer viewComponentId) throws Exception {
		return this.getActiveRealm(viewComponentId).getRealmKey();
	}

	@Override
	protected Integer handleAddJdbcRealmToSite(Integer siteId, RealmJdbcValue realmJdbcValue) throws Exception {
		Integer realmId = null;
		try {
			RealmJdbcHbm realm = super.getRealmJdbcHbmDao().create(this.createRealmJdbcHbm(siteId, realmJdbcValue));
			realmId = realm.getJdbcRealmId();
		} catch (Exception ex) {
			log.error("Could not add jaas realm to site", ex);
		}
		return realmId;
	}

	private RealmJdbcHbm createRealmJdbcHbm(Integer siteId, RealmJdbcValue value) {
		RealmJdbcHbm realm = new RealmJdbcHbmImpl();
		realm.setJdbcRealmId(value.getJdbcRealmId());
		realm.setJndiName(value.getJndiName());
		realm.setLoginPageId(value.getLoginPageId());
		realm.setRealmName(value.getRealmName());
		realm.setSite(super.getSiteHbmDao().load(siteId));
		realm.setStatementRolePerUser(value.getStatementRolePerUser());
		realm.setStatementUser(value.getStatementUser());
		return realm;
	}

	private RealmLdapHbm createRealmLdapHbm(Integer siteId, RealmLdapValue value) {
		RealmLdapHbm realm = new RealmLdapHbmImpl();
		realm.setLdapAuthenticationType(value.getLdapAuthenticationType());
		realm.setLdapPrefix(value.getLdapPrefix());
		realm.setLdapRealmId(value.getLdapRealmId());
		realm.setLdapSuffix(value.getLdapSuffix());
		realm.setLdapUrl(value.getLdapUrl());
		realm.setLoginPageId(value.getLoginPageId());
		realm.setRealmName(value.getRealmName());
		realm.setSite(super.getSiteHbmDao().load(siteId));
		return realm;
	}

	@Override
	protected Integer handleAddLdapRealmToSite(Integer siteId, RealmLdapValue realmLdapValue) throws Exception {
		Integer realmId = null;
		try {
			RealmLdapHbm ldap = super.getRealmLdapHbmDao().create(this.createRealmLdapHbm(siteId, realmLdapValue));
			realmId = ldap.getLdapRealmId();
		} catch (Exception ex) {
			log.error("Could not add ldap realm to site", ex);
		}
		return realmId;
	}

	private RealmSimplePwUserHbm createRealmSimplePwUserHbmFromValue(Integer simplePwRealmId, RealmSimplePwUserValue value) {
		RealmSimplePwUserHbm realmSimplePwUser = new RealmSimplePwUserHbmImpl();
		realmSimplePwUser.setPassword(value.getPassword());
		realmSimplePwUser.setRoles(value.getRoles());
		realmSimplePwUser.setUserName(value.getUserName());
		if (simplePwRealmId != null) {
			realmSimplePwUser.setSimplePwRealm(super.getRealmSimplePwHbmDao().load(simplePwRealmId));
		}
		return realmSimplePwUser;
	}

	@Override
	protected Integer handleAddUserToSimpleRealm(Integer simplePwRealmId, RealmSimplePwUserValue realmSimplePwUserValue) throws Exception {
		Integer pk = null;
		RealmSimplePwUserHbmDao realmSimplePwUserHbmDao = super.getRealmSimplePwUserHbmDao();
		RealmSimplePwUserHbm realmSimplePwUserHbm = realmSimplePwUserHbmDao.findByUsernameAndRealmId(realmSimplePwUserValue.getUserName(), simplePwRealmId);
		if (realmSimplePwUserHbm != null) {
			pk = realmSimplePwUserHbm.getSimplePwRealmUserId();
		} else {
			RealmSimplePwUserHbm user = this.createRealmSimplePwUserHbmFromValue(simplePwRealmId, realmSimplePwUserValue);
			user = realmSimplePwUserHbmDao.create(user);
			pk = user.getSimplePwRealmUserId();
		}
		return pk;
	}

	private Realm2viewComponentHbm createRealm2ViewComponentHbmFromJdbcValue(RealmJdbcValue value, Integer viewComponentId, String roleNeeded) {
		Realm2viewComponentHbm realm = new Realm2viewComponentHbmImpl();
		try {
			realm.setLoginPage(super.getViewComponentHbmDao().load(Integer.valueOf(value.getLoginPageId())));
		} catch (NumberFormatException e) {
			if (log.isDebugEnabled()) log.debug("Could not set the login viewComponent", e);
		}
		realm.setRoleNeeded(roleNeeded);
		realm.setViewComponent(super.getViewComponentHbmDao().load(viewComponentId));
		if (value.getJdbcRealmId() != null) {
			realm.setJdbcRealm(super.getRealmJdbcHbmDao().load(value.getJdbcRealmId()));
		}
		return realm;
	}

	@Override
	protected void handleAssignJdbcRealmToViewComponent(Integer jdbcRealmId, Integer viewComponentId, String roleNeeded, Integer loginPageId) throws Exception {
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			RealmJdbcHbm sqlRealm = super.getRealmJdbcHbmDao().load(jdbcRealmId);
			Realm2viewComponentHbm realm2viewComponent = null;
			try {
				realm2viewComponent = super.getRealm2viewComponentHbmDao().findByViewComponent(viewComponentId);
				//				this.clearRealm2viewComponentRelations(realm2viewComponent);
				realm2viewComponent.setViewComponent(view);
				realm2viewComponent.setJdbcRealm(sqlRealm);
				realm2viewComponent.setRoleNeeded(roleNeeded);
				view.setRealm2vc(realm2viewComponent);
			} catch (Exception ex) {
				realm2viewComponent = super.getRealm2viewComponentHbmDao().create(this.createRealm2ViewComponentHbmFromJdbcValue(sqlRealm.getRealmJdbcValue(), viewComponentId, roleNeeded));
			}
			realm2viewComponent.setSimplePwRealm(null);
			realm2viewComponent.setLdapRealm(null);
			realm2viewComponent.setJaasRealm(null);
			if (loginPageId != null && loginPageId.intValue() != -1) {
				try {
					ViewComponentHbm loginPage = super.getViewComponentHbmDao().load(loginPageId);
					realm2viewComponent.setLoginPage(loginPage);
				} catch (Exception e) {
					log.warn("Error setting loginPage " + loginPageId + " for JdbcRealm " + jdbcRealmId + ": " + e.getMessage(), e);
				}
			} else {
				realm2viewComponent.setLoginPage(null);
			}
		} catch (Exception ex) {
			log.error("Could not create new Realm2viewComponent: " + ex.getMessage());
		}
	}

	//TODO: look into returnvalue...
	private Realm2viewComponentHbm createRealm2ViewComponentFromLdapRealmValue(RealmLdapValue value, Integer viewComponentId, String roleNeeded) {
		Realm2viewComponentHbm realm = new Realm2viewComponentHbmImpl();
		if (value.getLdapRealmId() != null) {
			realm.setLdapRealm(super.getRealmLdapHbmDao().load(value.getLdapRealmId()));
		}
		realm.setRoleNeeded(roleNeeded);
		try {
			realm.setLoginPage(super.getViewComponentHbmDao().load(Integer.valueOf(value.getLoginPageId())));
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("Could not set the loginPage", e);
		}
		realm.setViewComponent(super.getViewComponentHbmDao().load(viewComponentId));
		return null;
	}

	@Override
	protected void handleAssignLdapRealmToViewComponent(Integer ldapRealmId, Integer viewComponentId, String roleNeeded, Integer loginPageId) throws Exception {
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			RealmLdapHbm ldapRealm = super.getRealmLdapHbmDao().load(ldapRealmId);
			Realm2viewComponentHbm realm2viewComponent = null;
			try {
				realm2viewComponent = super.getRealm2viewComponentHbmDao().findByViewComponent(viewComponentId);
				//				this.clearRealm2viewComponentRelations(realm2viewComponent);
				realm2viewComponent.setViewComponent(view);
				realm2viewComponent.setLdapRealm(ldapRealm);
				realm2viewComponent.setRoleNeeded(roleNeeded);
				view.setRealm2vc(realm2viewComponent);
			} catch (Exception ex) {
				realm2viewComponent = super.getRealm2viewComponentHbmDao().create(this.createRealm2ViewComponentFromLdapRealmValue(ldapRealm.getRealmLdapValue(), viewComponentId, roleNeeded));
			}
			realm2viewComponent.setJdbcRealm(null);
			realm2viewComponent.setSimplePwRealm(null);
			realm2viewComponent.setJaasRealm(null);
			if (loginPageId != null && loginPageId.intValue() != -1) {
				try {
					ViewComponentHbm loginPage = super.getViewComponentHbmDao().load(loginPageId);
					realm2viewComponent.setLoginPage(loginPage);
				} catch (Exception e) {
					log.warn("Error setting loginPage " + loginPageId + " for LdapRealm " + ldapRealmId + ": " + e.getMessage(), e);
				}
			} else {
				realm2viewComponent.setLoginPage(null);
			}
		} catch (Exception ex) {
			log.error("Could not create new Realm2viewComponent: " + ex.getMessage());
		}
	}

	private Realm2viewComponentHbm createRealm2ViewComponentHbmFromValue(RealmSimplePwValue value, Integer simplePwRealmId, Integer viewComponentId, String roleNeeded) {
		Realm2viewComponentHbm realm2Vc = new Realm2viewComponentHbmImpl();
		if (viewComponentId != null) {
			realm2Vc.setViewComponent(super.getViewComponentHbmDao().load(viewComponentId));
		}
		realm2Vc.setRoleNeeded(roleNeeded);
		if (value.getLoginPageId() != null) {
			realm2Vc.setLoginPage(super.getViewComponentHbmDao().load(Integer.valueOf(value.getLoginPageId())));
		}
		realm2Vc.setSimplePwRealm(super.getRealmSimplePwHbmDao().load(simplePwRealmId));
		return realm2Vc;
	}

	@Override
	protected void handleAssignSimplePwRealmToViewComponent(Integer simplePwRealmId, Integer viewComponentId, String roleNeeded, Integer loginPageId) throws Exception {
		try {
			ViewComponentHbm viewComponent = super.getViewComponentHbmDao().load(viewComponentId);
			RealmSimplePwHbm simpleRealm = super.getRealmSimplePwHbmDao().load(simplePwRealmId);
			Realm2viewComponentHbm realm2viewComponent = null;
			try {
				realm2viewComponent = super.getRealm2viewComponentHbmDao().findByViewComponent(viewComponentId);
				//				this.clearRealm2viewComponentRelations(realm2viewComponent);

				realm2viewComponent.setViewComponent(viewComponent);
				realm2viewComponent.setSimplePwRealm(simpleRealm);
				realm2viewComponent.setRoleNeeded(roleNeeded);
				viewComponent.setRealm2vc(realm2viewComponent);
			} catch (Exception ex) {
				realm2viewComponent = this.createRealm2ViewComponentHbmFromValue(simpleRealm.getRealmSimplePwValue(), simplePwRealmId, viewComponentId, roleNeeded);
				realm2viewComponent = super.getRealm2viewComponentHbmDao().create(realm2viewComponent);
				viewComponent.setRealm2vc(realm2viewComponent);
			}
			realm2viewComponent.setJdbcRealm(null);
			realm2viewComponent.setLdapRealm(null);
			realm2viewComponent.setJaasRealm(null);
			if (loginPageId != null && loginPageId.intValue() != -1) {
				try {
					ViewComponentHbm loginPage = super.getViewComponentHbmDao().load(loginPageId);
					realm2viewComponent.setLoginPage(loginPage);
				} catch (Exception e) {
					log.warn("Error setting loginPage " + loginPageId + " for SimplePwRealm " + simplePwRealmId + ": " + e.getMessage(), e);
				}
			} else {
				realm2viewComponent.setLoginPage(null);
			}
		} catch (Exception ex) {
			log.error("Could not create new Realm2viewComponent: " + ex.getMessage(), ex);
		}
	}

	@Override
	protected boolean handleDeleteJdbcRealm(Integer jdbcRealmId) throws Exception {
		boolean del = false;
		try {
			super.getRealmJdbcHbmDao().remove(jdbcRealmId);
			del = true;
		} catch (Exception ex) {
			log.error("Could not delete jdbc realm with id " + jdbcRealmId, ex);
		}
		return del;
	}

	@Override
	protected boolean handleDeleteLdapRealm(Integer realmLdapId) throws Exception {
		boolean del = false;
		try {
			super.getRealmLdapHbmDao().remove(realmLdapId);
			del = true;
		} catch (Exception ex) {
			log.error("Could not delete ldapRealm with id " + realmLdapId, ex);
		}
		return del;
	}

	@Override
	protected void handleEditJdbcRealm(RealmJdbcValue realmJdbcValue) throws Exception {
		try {
			RealmJdbcHbm realm = super.getRealmJdbcHbmDao().load(realmJdbcValue.getJdbcRealmId());
			realm.setRealmJdbcValue(realmJdbcValue);
		} catch (Exception ex) {
			log.error("Could not save RealmJdbc: " + ex.getMessage());
		}
	}

	@Override
	protected void handleEditLdapRealm(RealmLdapValue realmLdapValue) throws Exception {
		try {
			RealmLdapHbm realm = super.getRealmLdapHbmDao().load(realmLdapValue.getLdapRealmId());
			realm.setRealmLdapValue(realmLdapValue);
		} catch (Exception ex) {
			log.error("Could not save RealmLdap: " + ex.getMessage());
		}
	}

	@Override
	protected void handleEditSimplePwRealm(RealmSimplePwValue realmSimplePwValue) throws Exception {
		try {
			RealmSimplePwHbm realm = super.getRealmSimplePwHbmDao().load(realmSimplePwValue.getSimplePwRealmId());
			realm.setRealmSimplePwValue(realmSimplePwValue);
		} catch (Exception ex) {
			log.error("Could not save RealmSimplePw: " + ex.getMessage());
		}
	}

	@Override
	protected RealmJdbcValue[] handleGetJdbcRealmsForSite(Integer siteId) throws Exception {
		RealmJdbcValue[] val = null;
		try {
			Collection col = super.getRealmJdbcHbmDao().findBySiteId(siteId);
			val = new RealmJdbcValue[col.size()];
			int counter = 0;
			Iterator<RealmJdbcHbm> it = col.iterator();
			while (it.hasNext()) {
				RealmJdbcHbm realm = it.next();
				val[counter++] = realm.getRealmJdbcValue();
			}
		} catch (Exception ex) {
			log.warn("Could not get all RealmJdbc for site " + siteId + ": " + ex.getMessage());
		}
		return val;
	}

	@Override
	protected RealmLdapValue[] handleGetLdapRealmsForSite(Integer siteId) throws Exception {
		RealmLdapValue[] val = null;
		try {
			Collection<RealmLdapHbm> col = super.getRealmLdapHbmDao().findBySiteId(siteId);
			final int size = col.size();
			val = new RealmLdapValue[size];
			int counter = 0;
			Iterator<RealmLdapHbm> it = col.iterator();
			while (it.hasNext()) {
				RealmLdapHbm realm = it.next();
				val[counter++] = realm.getRealmLdapValue();
			}
		} catch (Exception ex) {
			log.warn("Could not get all RealmLdap for site " + siteId + ": " + ex.getMessage());
		}

		return val;
	}

	@Override
	protected boolean handleRemoveRealmFromViewComponent(Integer viewComponentId) throws Exception {
		boolean del = false;
		try {
			Realm2viewComponentHbm realm2viewComponent = super.getRealm2viewComponentHbmDao().findByViewComponent(viewComponentId);
			ViewComponentHbm viewComponent = realm2viewComponent.getViewComponent();
			super.getRealm2viewComponentHbmDao().remove(realm2viewComponent);
			viewComponent.setRealm2vc(null);
			del = true;
		} catch (Exception ex) {
			if (log.isDebugEnabled()) {
				log.debug("No Realm at ViewComponent " + viewComponentId + ": " + ex.getMessage());
			}
		}

		return del;
	}

	@Override
	protected RealmSimplePwValue[] handleGetSimplePwRealmsForUser(String userName) throws Exception {
		RealmSimplePwValue[] val = null;
		//		try {
		//			Collection<RealmSimplePwHbm> col = super.getUserHbmDao().load(userName).getSimplePwRealms();
		//			final int size = col.size();
		//			val = new RealmSimplePwValue[size];
		//
		//			Iterator<RealmSimplePwHbm> it = col.iterator();
		//			int counter = 0;
		//
		//			while (it.hasNext()) {
		//				RealmSimplePwHbm realm = it.next();
		//				val[counter] = realm.getRealmSimplePwValue();
		//				counter++;
		//			}
		//
		//			return val;
		//		} catch (Exception ex) {
		//			log.warn("Could not get RealmSimplePw for user " + userName + ": " + ex.getMessage());
		//			val = new RealmSimplePwValue[0];
		//		}
		return val;
	}

	@Override
	protected RealmSimplePwValue[] handleGetSimplePwRealms4CurrentUser(Integer siteId) throws Exception {
		RealmSimplePwValue[] val = null;
		UserHbm user = null;
		try {
			user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			Collection<RealmSimplePwHbm> col = super.getRealmSimplePwHbmDao().findByOwnerAndSite(user.getUserId(), siteId);
			List<RealmSimplePwValue> l = new ArrayList<RealmSimplePwValue>();
			Iterator<RealmSimplePwHbm> it = col.iterator();
			while (it.hasNext()) {
				RealmSimplePwHbm realm = it.next();
				l.add(realm.getRealmSimplePwValue());
			}
			val = l.toArray(new RealmSimplePwValue[0]);
		} catch (Exception ex) {
			log.warn("Could not get RealmSimplePw for User " + user.getUserId() + " and Site " + siteId + ": " + ex.getMessage());
			val = new RealmSimplePwValue[0];
		}
		return val;
	}

	@Override
	protected RealmSimplePwValue[] handleGetSimplePwRealmsForSite(Integer siteId) throws Exception {
		RealmSimplePwValue[] val = null;
		try {
			Collection<RealmSimplePwHbm> col = super.getRealmSimplePwHbmDao().findBySiteId(siteId);
			List<RealmSimplePwValue> l = new ArrayList<RealmSimplePwValue>();
			Iterator<RealmSimplePwHbm> it = col.iterator();
			while (it.hasNext()) {
				RealmSimplePwHbm realm = it.next();
				l.add(realm.getRealmSimplePwValue());
			}
			val = l.toArray(new RealmSimplePwValue[0]);
		} catch (Exception ex) {
			log.warn("Could not get RealmSimplePw for Site " + siteId + ": " + ex.getMessage());
			val = new RealmSimplePwValue[0];
		}
		return val;
	}

	@Override
	protected RealmSimplePwUserValue[] handleGetUserForSimplePwRealm(Integer simplePwRealmId) throws Exception {
		RealmSimplePwUserValue[] users = null;
		try {
			RealmSimplePwHbm realm = super.getRealmSimplePwHbmDao().load(simplePwRealmId);
			Collection<RealmSimplePwUserHbm> col = realm.getSimplePwRealmUsers();
			final int usersize = col.size();
			users = new RealmSimplePwUserValue[usersize];
			Iterator<RealmSimplePwUserHbm> it = col.iterator();
			int usercounter = 0;
			while (it.hasNext()) {
				RealmSimplePwUserHbm tempUser = it.next();
				users[usercounter++] = tempUser.getRealmSimplePwUserValue();
			}
		} catch (Exception ex) {
			log.warn("Could not get all Users for RealmSimplePw " + simplePwRealmId + ": " + ex.getMessage());
			users = new RealmSimplePwUserValue[0];
		}
		return users;
	}

	@Override
	protected boolean handleDeleteSimplePwRealmUser(Integer simplePwRealmUserId) throws Exception {
		boolean deleted = false;
		try {
			super.getRealmSimplePwUserHbmDao().remove(simplePwRealmUserId);
			deleted = true;
		} catch (Exception e) {
			log.error("Could not remove RealmSimplePwUser Object with id " + simplePwRealmUserId, e);
		}
		return deleted;
	}

	@Override
	protected ActiveRealmValue handleGetActiveRealm(Integer viewComponentId) throws Exception {
		if (log.isDebugEnabled()) log.debug("getActiveRealm started");
		ActiveRealmValue activeRealmValue = new ActiveRealmValue(false, false, false, false, false, -1, "", "", null);
		try {
			Realm2viewComponentHbm realm2viewComponent = getRealm2viewComponentHbmDao().findByViewComponent(viewComponentId);
			if (realm2viewComponent != null && realm2viewComponent.getRealm2viewComponentId() != null) {
				activeRealmValue.setRoleNeeded(realm2viewComponent.getRoleNeeded());
				if (realm2viewComponent.getLoginPage() != null) activeRealmValue.setLoginPageId(realm2viewComponent.getLoginPage().getViewComponentId());
				if (realm2viewComponent.getSimplePwRealm() != null) {
					if (log.isDebugEnabled()) log.debug("getSimplePwRealm found");
					activeRealmValue.setRealmSimplePw(true);
					activeRealmValue.setRealmNone(false);
					activeRealmValue.setRealmId(realm2viewComponent.getSimplePwRealm().getSimplePwRealmId().intValue());
					activeRealmValue.setRealmKey("SIMPLEPW_" + activeRealmValue.getRealmId());
				} else if (realm2viewComponent.getJdbcRealm() != null) {
					if (log.isDebugEnabled()) log.debug("getJdbcRealm found");
					activeRealmValue.setRealmJdbc(true);
					activeRealmValue.setRealmNone(false);
					activeRealmValue.setRealmId(realm2viewComponent.getJdbcRealm().getJdbcRealmId().intValue());
					activeRealmValue.setRealmKey("JDBC_" + activeRealmValue.getRealmId());
				} else if (realm2viewComponent.getLdapRealm() != null) {
					if (log.isDebugEnabled()) log.debug("getLdapRealm found");
					activeRealmValue.setRealmLdap(true);
					activeRealmValue.setRealmNone(false);
					activeRealmValue.setRealmId(realm2viewComponent.getLdapRealm().getLdapRealmId().intValue());
					activeRealmValue.setRealmKey("LDAP_" + activeRealmValue.getRealmId());
				} else if (realm2viewComponent.getJaasRealm() != null) {
					if (log.isDebugEnabled()) log.debug("getJaasRealm found");
					activeRealmValue.setRealmJaas(true);
					activeRealmValue.setRealmNone(false);
					activeRealmValue.setRealmId(realm2viewComponent.getJaasRealm().getJaasRealmId().intValue());
					activeRealmValue.setRealmKey("JAAS_" + activeRealmValue.getRealmId());
				}
			} else {
				activeRealmValue.setRealmNone(true);
				if (log.isDebugEnabled()) log.debug("no realm directly at " + viewComponentId + ", checking parent..."); //$NON-NLS-1$ //$NON-NLS-2$
				// check if parent is protected
				try {
					ViewComponentHbm current = getViewComponentHbmDao().load(viewComponentId);
					if (!current.isRoot()) {
						Integer parentId = current.getParent().getViewComponentId();
						activeRealmValue = this.getActiveRealm(parentId);
					}
				} catch (Exception e) {
					log.warn("Error calling getActiveRealm for parent of " + viewComponentId, e); //$NON-NLS-1$
				}
			}
		} catch (Exception e) {
			log.warn("Error in getActiveRealm: " + e.getMessage(), e);
		}

		return activeRealmValue;
	}

	@Override
	protected Integer handleGetFirstProtectedParentId(Integer viewComponentId) throws Exception {
		Integer firstProtectedVC = null;
		if (viewComponentId != null && viewComponentId.intValue() > 0) {
			try {
				ViewComponentHbm viewComponent = super.getViewComponentHbmDao().load(viewComponentId);
				Integer parentId = null;
				if (!viewComponent.isRoot()) {
					parentId = viewComponent.getParent().getViewComponentId();
					if (this.getActiveRealm(parentId).isRealmNone()) {
						firstProtectedVC = this.getFirstProtectedParentId(parentId);
					} else {
						firstProtectedVC = parentId;
					}
				}
			} catch (Exception e) {
				log.error("Error searching first protected parent for ViewComponent " + viewComponentId + ": " + e.getMessage(), e);
			}
		}
		return firstProtectedVC != null ? firstProtectedVC : new Integer(-1);
	}

	private RealmSimplePwHbm createRealmSimplePwHbm(String realmName, String owner, Integer siteId, String loginPageId) {
		RealmSimplePwHbm realm = new RealmSimplePwHbmImpl();
		realm.setRealmName(realmName);
		realm.setSite(super.getSiteHbmDao().load(siteId));
		realm.setLoginPageId(loginPageId);
		return realm;
	}

	@Override
	protected Integer handleAddSimpleRealmToSite(String realmName, Integer siteId, String loginPageId) throws Exception {
		Integer pk = null;
		String owner = AuthenticationHelper.getUserName();
		RealmSimplePwHbm realm = null;
		Collection realmCollection = super.getRealmSimplePwHbmDao().findBySiteAndName(siteId, realmName);
		if ((realmCollection != null) && (realmCollection.size() > 0)) { throw new AlreadyExistsException("A Realm with the Name " + realmName + " for user " + owner + " already exists!"); }
		try {
			realm = super.getRealmSimplePwHbmDao().create(this.createRealmSimplePwHbm(realmName, owner, siteId, loginPageId));
			pk = realm.getSimplePwRealmId();
		} catch (Exception e) {
			log.error("Could not add simpleRealm to site", e);
		}
		return pk;
	}

	@Override
	protected Integer handleAddJaasRealmToSite(Integer siteId, RealmJaasValue value) throws Exception {
		Integer realmId = null;
		try {
			RealmJaasHbm realm = new RealmJaasHbmImpl();
			realm.setJaasPolicyName(value.getJaasPolicyName());
			realm.setJaasRealmId(value.getJaasRealmId());
			realm.setLoginPageId(value.getLoginPageId());
			realm.setRealmName(value.getRealmName());
			realm.setSite(super.getSiteHbmDao().load(siteId));
			realm = super.getRealmJaasHbmDao().create(realm);
			realmId = realm.getJaasRealmId();
		} catch (Exception ex) {
			log.error("Could not add jaas realm to site", ex);
		}
		return realmId;
	}

	@Override
	protected void handleEditJaasRealm(RealmJaasValue realmJaasValue) throws Exception {
		try {
			RealmJaasHbm realm = super.getRealmJaasHbmDao().load(realmJaasValue.getJaasRealmId());
			realm.setRealmJaasValue(realmJaasValue);
		} catch (Exception ex) {
			log.error("Could not save RealmJaas: " + ex.getMessage());
		}
	}

	@Override
	protected boolean handleDeleteJaasRealm(Integer realmJaasId) throws Exception {
		boolean del = false;
		try {
			super.getRealmJaasHbmDao().remove(realmJaasId);
			del = true;
		} catch (Exception ex) {
			log.error("Could not remove jaas realm with id " + realmJaasId, ex);
		}
		return del;
	}

	@Override
	protected RealmJaasValue[] handleGetJaasRealmsForSite(Integer siteId) throws Exception {
		RealmJaasValue[] val = null;
		try {
			Collection<RealmJaasHbm> col = super.getRealmJaasHbmDao().findBySiteId(siteId);
			val = new RealmJaasValue[col.size()];
			int counter = 0;
			Iterator<RealmJaasHbm> it = col.iterator();
			RealmJaasHbm realm = null;
			while (it.hasNext()) {
				realm = it.next();
				val[counter++] = realm.getRealmJaasValue();
			}
		} catch (Exception ex) {
			log.warn("Could not get all RealmJaas for site " + siteId + ": " + ex.getMessage());
		}
		return val;
	}

	@Override
	protected void handleAssignJaasRealmToViewComponent(Integer jaasRealmId, Integer viewComponentId, String roleNeeded, Integer loginPageId) throws Exception {
		try {
			ViewComponentHbm view = super.getViewComponentHbmDao().load(viewComponentId);
			RealmJaasHbm jaasRealm = super.getRealmJaasHbmDao().load(jaasRealmId);
			Realm2viewComponentHbm realm2viewComponent = null;
			try {
				realm2viewComponent = super.getRealm2viewComponentHbmDao().findByViewComponent(viewComponentId);
				//				this.clearRealm2viewComponentRelations(realm2viewComponent);
				realm2viewComponent.setViewComponent(view);
				realm2viewComponent.setJaasRealm(jaasRealm);
				realm2viewComponent.setRoleNeeded(roleNeeded);
				view.setRealm2vc(realm2viewComponent);
			} catch (Exception ex) {
				realm2viewComponent = super.getRealm2viewComponentHbmDao().create(this.createRealm2viewComponentHbmFromJaasValue(jaasRealm.getRealmJaasValue(), viewComponentId, roleNeeded));
			}
			realm2viewComponent.setJdbcRealm(null);
			realm2viewComponent.setSimplePwRealm(null);
			realm2viewComponent.setLdapRealm(null);
			if (loginPageId != null && loginPageId.intValue() != -1) {
				try {
					ViewComponentHbm loginPage = super.getViewComponentHbmDao().load(loginPageId);
					realm2viewComponent.setLoginPage(loginPage);
				} catch (Exception e) {
					log.warn("Error setting loginPage " + loginPageId + " for JaasRealm " + jaasRealmId + ": " + e.getMessage(), e);
				}
			} else {
				realm2viewComponent.setLoginPage(null);
			}
		} catch (Exception ex) {
			log.error("Could not create new Realm2viewComponent: " + ex.getMessage());
		}

	}

	private Realm2viewComponentHbm createRealm2viewComponentHbmFromJaasValue(RealmJaasValue value, Integer viewComponentId, String roleNeeded) {
		Realm2viewComponentHbm realm = new Realm2viewComponentHbmImpl();
		try {
			realm.setLoginPage(super.getViewComponentHbmDao().load(Integer.valueOf(value.getLoginPageId())));
		} catch (Exception e) {
		}
		realm.setRoleNeeded(roleNeeded);
		if (value.getJaasRealmId() != null) {
			realm.setJaasRealm(super.getRealmJaasHbmDao().load(value.getJaasRealmId()));
		}
		realm.setViewComponent(super.getViewComponentHbmDao().load(viewComponentId));
		return realm;
	}

	private boolean isLoginPage(Integer viewComponentId) {
		try {
			ViewComponentHbm viewComponent = super.getViewComponentHbmDao().load(viewComponentId);
			if (viewComponent.getRealm4login().size() > 0) return true;
			if (super.getRealmSimplePwHbmDao().findByLoginPage(viewComponentId.toString()).size() > 0) return true;
			if (super.getRealmJdbcHbmDao().findByLoginPage(viewComponentId.toString()).size() > 0) return true;
			if (super.getRealmJaasHbmDao().findByLoginPage(viewComponentId.toString()).size() > 0) return true;
			if (super.getRealmLdapHbmDao().findByLoginPage(viewComponentId.toString()).size() > 0) return true;
		} catch (Exception e) {
			log.error("Error checking if viewComponent " + viewComponentId + " is a login-page: " + e.getMessage(), e);
		}
		return false;
	}

	private SafeguardLoginManager getSafeguardLoginManager(String userName, String password, Integer viewComponentId) {
		SafeguardLoginManager loginManager = null;
		try {
			ActiveRealmValue realm = this.getActiveRealm(viewComponentId);
			if (!realm.isRealmNone()) {
				if (realm.isRealmJdbc()) {
					loginManager = new SqlDbRealmLoginManager(getRealmJdbcHbmDao(), Integer.valueOf(realm.getRealmId()), userName, password, realm.getRoleNeeded());
				} else if (realm.isRealmSimplePw()) {
					loginManager = new SimplePwRealmLoginManager(getRealmSimplePwUserHbmDao(), Integer.valueOf(realm.getRealmId()), userName, password, realm.getRoleNeeded());
				} else if (realm.isRealmLdap()) {
					loginManager = new LDAPRealmLoginManager(getRealmLdapHbmDao(), Integer.valueOf(realm.getRealmId()), userName, password, realm.getRoleNeeded());
				} else if (realm.isRealmJaas()) {
					loginManager = new JAASRealmLoginManager(getRealmJaasHbmDao(), Integer.valueOf(realm.getRealmId()), userName, password, realm.getRoleNeeded());
				}
			}
		} catch (Exception e) {
			log.error("Error during determination of SafeguardLoginManager: " + e.getMessage(), e);
		}
		return loginManager;
	}

	@Override
	protected String[] handleGetRoles4UserAndRealm(String userName, String password, Integer viewComponentId) throws Exception {
		ArrayList<String> rolesList = new ArrayList<String>();
		try {
			SafeguardLoginManager loginManager = this.getSafeguardLoginManager(userName, password, viewComponentId);
			if (loginManager != null) {
				String realmKey = this.getRealmIdAndType(viewComponentId);
				rolesList.add(realmKey); // in case a page is protected but does not require any role
				Iterator<String> it = loginManager.getRoles().iterator();
				while (it.hasNext()) {
					String role = it.next();
					rolesList.add(realmKey + "_" + role);
				}
			}
		} catch (Exception e) {
			log.error("Error getting all roles for user: " + e.getMessage(), e);
		}

		return rolesList.toArray(new String[0]);
	}

	@Override
	protected String handleGetLoginPath(Integer viewComponentId) throws Exception {
		String loginPageVcId = null;
		String loginPath = null;
		try {
			ActiveRealmValue realm = this.getActiveRealm(viewComponentId);
			if (!realm.isRealmNone()) {
				Integer vcId = null;
				if (realm.getLoginPageId() != null) {
					vcId = realm.getLoginPageId();
				} else {
					if (realm.isRealmJdbc()) {
						RealmJdbcHbm jdbcRealm = super.getRealmJdbcHbmDao().load(Integer.valueOf(realm.getRealmId()));
						loginPageVcId = jdbcRealm.getLoginPageId();
					} else if (realm.isRealmSimplePw()) {
						RealmSimplePwHbm simplePwRealm = super.getRealmSimplePwHbmDao().load(Integer.valueOf(realm.getRealmId()));
						loginPageVcId = simplePwRealm.getLoginPageId();
					} else if (realm.isRealmLdap()) {
						RealmLdapHbm ldapRealm = super.getRealmLdapHbmDao().load(Integer.valueOf(realm.getRealmId()));
						loginPageVcId = ldapRealm.getLoginPageId();
					} else if (realm.isRealmJaas()) {
						RealmJaasHbm jaasRealm = super.getRealmJaasHbmDao().load(Integer.valueOf(realm.getRealmId()));
						loginPageVcId = jaasRealm.getLoginPageId();
					} else {
						log.warn("undefined Realm at ViewComponent " + viewComponentId);
					}
					vcId = Integer.valueOf(loginPageVcId);
				}
				loginPath = super.getViewComponentHbmDao().load(vcId).getPath();
				if (log.isDebugEnabled()) log.debug("SafeGuard login-path: " + loginPath);
			}
		} catch (Exception e) {
			log.warn("Error getting SafeGuard login-path: " + e.getMessage(), e);
		}
		return loginPath;
	}

	@Override
	protected String handleFilterNavigation(String navigationXml, Map safeGuardMap) throws Exception {
		Document navigationDom = XercesHelper.string2Dom(navigationXml);
		Iterator<Node> it = XercesHelper.findNodes(navigationDom, "//viewcomponent");
		while (it.hasNext()) {
			try {
				Element ndeViewComponent = (Element) it.next();
				Integer viewComponentId = Integer.valueOf(ndeViewComponent.getAttribute("id"));
				ActiveRealmValue realm = this.getActiveRealm(viewComponentId);
				Element elmProtected = navigationDom.createElement("protected");
				Text txtProtected = navigationDom.createTextNode(Boolean.toString(!realm.isRealmNone()));
				elmProtected.appendChild(txtProtected);
				ndeViewComponent.appendChild(elmProtected);
				Element elmAccess = navigationDom.createElement("userHasRightToAccess");
				Text txtAccess = null;
				if (safeGuardMap.size() == 0) {
					txtAccess = navigationDom.createTextNode("notloggedin");
				} else {
					boolean isAccessible = !this.isSafeguardAuthenticationNeeded(viewComponentId, safeGuardMap);
					txtAccess = navigationDom.createTextNode(Boolean.toString(isAccessible));
				}
				elmAccess.appendChild(txtAccess);
				ndeViewComponent.appendChild(elmAccess);
				if (!realm.isRealmNone()) {
					String requiredRole = realm.getRoleNeeded();
					Element elmRequiredRole = navigationDom.createElement("requiredRole");
					if (requiredRole == null) requiredRole = "";
					CDATASection txtRequiredRole = navigationDom.createCDATASection(requiredRole);
					elmRequiredRole.appendChild(txtRequiredRole);
					ndeViewComponent.appendChild(elmRequiredRole);
				}
			} catch (Exception e) {
				log.error("Error checking protection: " + e.getMessage(), e);
			}
		}
		return XercesHelper.doc2String(navigationDom);
	}

	@Override
	protected void handleDeleteSimplePwRealm(Integer simplePwRealmId) throws Exception {
		super.getRealmSimplePwHbmDao().remove(simplePwRealmId);
	}

}