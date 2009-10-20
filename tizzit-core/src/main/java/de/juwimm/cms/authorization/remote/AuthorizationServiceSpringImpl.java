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
package de.juwimm.cms.authorization.remote;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.juwimm.cms.authorization.model.GroupHbm;
import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.vo.GroupValue;
import de.juwimm.cms.authorization.vo.UserLoginValue;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.UnitValue;

/**
 * @see de.juwimm.cms.authorization.remote.AuthorizationServiceSpring
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> ,
 *         Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id: AuthorizationServiceSpringImpl.java 26916 2008-04-30 17:51:21Z
 *          greivej $
 */
public class AuthorizationServiceSpringImpl extends AuthorizationServiceSpringBase {
	private static Log log = LogFactory.getLog(AuthorizationServiceSpringImpl.class);

	/**
	 * @see de.juwimm.cms.authorization.remote.AuthorizationServiceSpring#isUserInRole(java.lang.String)
	 */
	@Override
	protected boolean handleIsUserInRole(String roleName) throws Exception {
		try {
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			return super.getUserHbmDao().isInRole(user, roleName, user.getActiveSite());
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Could not get role for user", e);
			}
		}
		return false;
	}

	/**
	 * @see de.juwimm.cms.authorization.remote.AuthorizationServiceSpring#isUserInUnit(java.lang.Integer)
	 */
	@Override
	protected boolean handleIsUserInUnit(Integer unitId) throws Exception {
		try {
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			return getUserHbmDao().isInUnit(unitId, user);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Could not get unit for user", e);
			}
		}
		return false;
	}

	/**
	 * @see de.juwimm.cms.authorization.remote.AuthorizationServiceSpring#getGroups()
	 */
	@Override
	protected GroupValue[] handleGetGroups() throws Exception {
		try {
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			Collection coll = null;
			if (user.isMasterRoot()) {
				coll = super.getGroupHbmDao().findAll(user.getActiveSite().getSiteId());
			} else {
				coll = getUserHbmDao().getGroups4ActiveSite(user);
			}
			Iterator it = coll.iterator();
			GroupValue[] gvarr = new GroupValue[coll.size()];
			int i = 0;
			while (it.hasNext()) {
				gvarr[i++] = ((GroupHbm) it.next()).getGroupValue();
			}
			return gvarr;
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.authorization.remote.AuthorizationServiceSpring#getUnits()
	 */
	@Override
	protected UnitValue[] handleGetUnits() throws Exception {
		Vector<UnitValue> vec = new Vector<UnitValue>();
		try {
			if (log.isDebugEnabled()) log.debug("begin getUnits");
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			Iterator iterator = null;
			if (getUserHbmDao().isInRole(user, UserRights.SITE_ROOT, user.getActiveSite())) {
				iterator = super.getUnitHbmDao().findAll(user.getActiveSite().getSiteId()).iterator();
			} else {
				iterator = super.getUserHbmDao().getUnits4ActiveSite(user).iterator();
			}
			UnitHbm unit;
			while (iterator.hasNext()) {
				unit = (UnitHbm) iterator.next();
				UnitValue dao = getUnitHbmDao().getDao(unit);
				vec.addElement(dao);
			}
			if (log.isDebugEnabled()) log.debug("end getUnits");
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return vec.toArray(new UnitValue[0]);
	}

	/**
	 * @see de.juwimm.cms.authorization.remote.AuthorizationServiceSpring#getSites()
	 */
	@Override
	protected SiteValue[] handleGetSites() throws Exception {
		SiteValue[] retArr = null;
		try {
			if (log.isDebugEnabled()) log.debug("begin getSites for principal " + AuthenticationHelper.getUserName());
			UserHbm user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			Collection coll = null;
			if (user.isMasterRoot()) {
				coll = super.getSiteHbmDao().findAll();
			} else {
				coll = user.getSites();
			}
			int i = 0;
			retArr = new SiteValue[coll.size()];
			SiteHbm site;
			for (Iterator it = coll.iterator(); it.hasNext();) {
				site = (SiteHbm) it.next();
				retArr[i++] = site.getSiteValue();
			}
			if (log.isDebugEnabled()) log.debug("end getSites");
		} catch (Exception exe) {
			log.error("Unknown Error occured inside getSites: " + exe.getMessage());
		}
		return retArr;
	}

	/**
	 * @see de.juwimm.cms.authorization.remote.AuthorizationServiceSpring#login(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	@Override
	protected UserLoginValue handleLogin(String userName, String passwd, Integer siteId) throws Exception {
		// try {
		if (log.isDebugEnabled()) {
			log.debug("Try to login \"" + AuthenticationHelper.getUserName() + "\" at " + siteId);
		}

		SiteHbm site = null;
		try {
			site = super.getSiteHbmDao().load(siteId);
		} catch (Exception exe) {
			throw new SecurityException("Invalid SiteId");
		}
		UserHbm user = null;

		try {
			user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
		} catch (Exception ex) {
			throw new SecurityException("Invalid Principal");
		}
		if (!user.isMasterRoot() && !user.getSites().contains(site)) { throw new SecurityException("User is not a member of the given site!"); }
		user.setActiveSite(site);
		user.setLoginDate((System.currentTimeMillis()));
		LoginContext lc = new LoginContext("juwimm-cms-security-domain", new InternalCallbackHandler(passwd));
		lc.login();
		/*
		 * if(log.isDebugEnabled()) { Subject subj = lc.getSubject();
		 * Principal[] prip = (Principal[]) subj.getPrincipals().toArray(new
		 * Principal[0]); Group groupPrincipal = null; for(int i = 0; i <
		 * prip.length; i++) { if(prip[i] instanceof Group) { groupPrincipal =
		 * ((Group) prip[i]); Enumeration group = groupPrincipal.members();
		 * while(group.hasMoreElements()) { Principal rolePrincipal =
		 * ((Principal) group.nextElement()); String role =
		 * rolePrincipal.getName(); log.debug("User is in role: " + role); } }
		 * else { //log.warn("Found one Principal other then a group - is is: " +
		 * prip[i].getName()); } } }
		 */
		if (log.isInfoEnabled()) log.info("Login User " + user.getUserId() + " at site " + site.getSiteId() + " (" + site.getShortName().trim() + ")");
		// UserLoginValue ulv = user.getUserLoginValue();
		UserLoginValue ulv = super.getUserHbmDao().getUserLoginValue(user);
		ulv.setSiteName(site.getName());
		ulv.setSiteConfigXML(site.getConfigXML());
		return ulv;
		// } catch (Exception e) {
		// throw new UserException(e.getMessage());
		// }
	}

	/**
	 * @see de.juwimm.cms.authorization.remote.AuthorizationServiceSpring#logout()
	 */
	@Override
	protected void handleLogout() throws Exception {
		UserHbm user = null;
		try {
			user = super.getUserHbmDao().load(AuthenticationHelper.getUserName());
			if (log.isInfoEnabled()) log.info("Logout User " + user.getUserId());
			user.setLoginDate(0L);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	/**
	 * 
	 */
	private class InternalCallbackHandler implements CallbackHandler {
		private final String passwd;

		public InternalCallbackHandler(String passwd) {
			this.passwd = passwd;
		}

		public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
			for (int i = 0; i < callbacks.length; i++) {
				if (callbacks[i] instanceof NameCallback) {
					// prompt the user for a username
					NameCallback nc = (NameCallback) callbacks[i];
					nc.setName(AuthenticationHelper.getUserName());
				} else if (callbacks[i] instanceof PasswordCallback) {
					PasswordCallback pc = (PasswordCallback) callbacks[i];
					pc.setPassword(this.passwd.toCharArray());
				}
			}
		}
	}
}
