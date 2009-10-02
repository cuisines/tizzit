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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.components.model.AddressHbm;
import de.juwimm.cms.components.model.PersonHbm;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.HostHbm;
import de.juwimm.cms.model.HostHbmDao;
import de.juwimm.cms.model.HostHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.vo.HostValue;
import de.juwimm.cms.vo.SiteValue;

/**
 * @see de.juwimm.cms.remote.AdministrationServiceSpring
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class AdministrationServiceSpringImpl extends AdministrationServiceSpringBase {
	private static Log log = LogFactory.getLog(AdministrationServiceSpringImpl.class);

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#createHost(java.lang.String)
	 */
	@Override
	protected HostValue handleCreateHost(String hostName) throws Exception {
		HostHbm host = new HostHbmImpl();
		host.setHostName(hostName);
		HostHbmDao hostHbmDao = getHostHbmDao();
		host = hostHbmDao.create(host);
		return host.getHostValue();
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#getAllHosts()
	 */
	@Override
	protected HostValue[] handleGetAllHosts() throws Exception {
		UserHbm user = null;
		try {
			user = getUserHbmDao().load(AuthenticationHelper.getUserName());
		} catch (Exception exe) {
			log.error(exe.getMessage());
		}

		HostValue[] hostArray = null;
		try {
			Collection coll = getHostHbmDao().findAll();
			hostArray = new HostValue[coll.size()];
			Iterator it = coll.iterator();
			int i = 0;
			while (it.hasNext()) {
				HostHbm hl = (HostHbm) it.next();
				SiteHbm sl = hl.getSite();
				if (sl == null) {
					hostArray[i++] = hl.getHostValue();
				} else {
					if (user.getSites().contains(sl) || user.isMasterRoot()) {
						hostArray[i++] = hl.getHostValue();
					}
				}
			}
		} catch (Exception exe) {
			log.error("Error occured", exe);
			throw new UserException(exe.getMessage());
		}
		return (hostArray);
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#getHosts()
	 */
	@Override
	protected HostValue[] handleGetHosts() throws Exception {
		UserHbm user = null;
		try {
			user = getUserHbmDao().load(AuthenticationHelper.getUserName());
		} catch (Exception exe) {
			log.error(exe.getMessage());
		}

		HostValue[] hostArray = null;
		try {
			Collection coll = getHostHbmDao().findAll(user.getActiveSite().getSiteId());
			hostArray = new HostValue[coll.size()];
			Iterator it = coll.iterator();
			int i = 0;
			while (it.hasNext()) {
				HostHbm hl = (HostHbm) it.next();
				hostArray[i++] = hl.getHostValue();
			}
		} catch (Exception exe) {
			log.error("Error occured", exe);
			throw new UserException(exe.getMessage());
		}
		return (hostArray);
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#getSiteForName(java.lang.String)
	 */
	@Override
	protected SiteValue handleGetSiteForName(String siteName) throws Exception {
		SiteHbm siteHbm = getSiteHbmDao().findByName(siteName);
		return siteHbm.getSiteValue();
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#removeHost(java.lang.String)
	 */
	@Override
	protected void handleRemoveHost(String hostName) throws Exception {
		HostHbm hostToRemove = getHostHbmDao().load(hostName);
		getHostHbmDao().remove(hostToRemove);
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#setSiteByName(java.lang.String, java.lang.String)
	 */
	@Override
	protected void handleSetSiteByName(String hostName, String siteName) throws Exception {
		HostHbm host = getHostHbmDao().load(hostName);
		SiteHbm site = getSiteHbmDao().findByName(siteName);
		host.setSite(site);
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#setSiteById(java.lang.String, java.lang.Integer)
	 */
	@Override
	protected void handleSetSiteById(String hostName, Integer siteId) throws Exception {
		HostHbm host = getHostHbmDao().load(hostName);
		SiteHbm site = getSiteHbmDao().load(siteId);
		host.setSite(site);
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#setStartPage(java.lang.String, java.lang.String)
	 */
	@Override
	protected void handleSetStartPage(String hostName, String vcId) throws Exception {
		HostHbm host = getHostHbmDao().load(hostName);
		if (vcId != null) {
			ViewComponentHbm startPage = getViewComponentHbmDao().load(Integer.valueOf(vcId));
			host.setStartPage(startPage);
		} else {
			host.setStartPage(null);
		}
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#getStartPage(java.lang.String)
	 */
	@Override
	protected String handleGetStartPage(String hostName) throws Exception {
		HostHbm host = getHostHbmDao().load(hostName);
		if (host != null && host.getStartPage() != null) { return host.getStartPage().getViewComponentId().toString(); }
		return "";
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#getSite4Host(java.lang.String)
	 */
	@Override
	protected String handleGetSite4Host(String hostName) throws Exception {
		HostHbm host = getHostHbmDao().load(hostName);
		if (host != null) {
			try {
				return host.getSite().getSiteId().toString();
			} catch (Exception e) {
				if (log.isErrorEnabled()) {
					log.error("Could not get site for host: " + hostName, e);
				}
			}
		}
		return "";
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#getHostsForSite(java.lang.Integer)
	 */
	@Override
	protected HostValue[] handleGetHostsForSite(Integer siteId) throws Exception {
		HostValue[] hostValues = null;
		if (siteId == null) {
			hostValues = getHosts();
		} else {
			try {
				SiteHbm site = getSiteHbmDao().load(siteId);
				Collection<HostHbm> hosts = site.getHost();
				hostValues = new HostValue[hosts.size()];
				int i = 0;
				for (HostHbm h : hosts) {
					hostValues[i++] = h.getHostValue();
				}
			} catch (Exception e) {
				if (log.isErrorEnabled()) {
					log.error("Could not get hosts for site with id: " + siteId, e);
				}
			}
		}
		return hostValues;
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#removeSite(java.lang.String)
	 */
	@Override
	protected void handleRemoveSite(String hostName) throws Exception {
		HostHbm host = getHostHbmDao().load(hostName);
		host.setSite(null);
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#removeStartPage(java.lang.String)
	 */
	@Override
	protected void handleRemoveStartPage(String hostName) throws Exception {
		HostHbm host = getHostHbmDao().load(hostName);
		host.setStartPage(null);
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#getAllUnassignedHosts()
	 */
	@Override
	protected HostValue[] handleGetAllUnassignedHosts() throws Exception {
		HostValue[] hostArray = null;
		try {
			Collection coll = getHostHbmDao().findAllUnassigned();
			hostArray = new HostValue[coll.size()];
			Iterator it = coll.iterator();
			int i = 0;
			while (it.hasNext()) {
				HostHbm hl = (HostHbm) it.next();
				hostArray[i++] = hl.getHostValue();
			}
		} catch (Exception exe) {
			log.error("Error occured", exe);
			throw new UserException(exe.getMessage());
		}
		return hostArray;
	}

	/**
	 * @see de.juwimm.cms.remote.AdministrationServiceSpring#exportXlsPersonData()
	 */
	@Override
	protected InputStream handleExportXlsPersonData() throws Exception {
		try {
			log.info("exportXlsPersonData " + AuthenticationHelper.getUserName());
			File fle = File.createTempFile("XlsPersonData", ".xml.gz");
			FileOutputStream fout = new FileOutputStream(fle);
			PrintStream out = new PrintStream(fout, true, "UTF-8");

			UserHbm invoker = getUserHbmDao().load(AuthenticationHelper.getUserName());
			SiteHbm site = invoker.getActiveSite();
			if (log.isDebugEnabled()) log.debug("Invoker is: " + invoker.getUserId() + " within Site " + site.getName());
			// header
			out.println("Titel,Vorname,Nachname,Adresse,PLZ,Ort,Telefon 1,Telefon 2,Fax,e-Mail,Einrichtung");
			Iterator<UnitHbm> it = getUnitHbmDao().findAll(site.getSiteId()).iterator();
			while (it.hasNext()) {
				UnitHbm currentUnit = it.next();

				Collection<PersonHbm> persons = getPersonHbmDao().findByUnit(currentUnit.getUnitId());
				for (PersonHbm currentPerson : persons) {
					Iterator<AddressHbm> addressIt = currentPerson.getAddresses().iterator();
					boolean hasAddress = false;
					while (addressIt.hasNext()) {
						hasAddress = true;
						AddressHbm currentAddress = addressIt.next();
						out.print(currentPerson.getTitle() == null ? "," : currentPerson.getTitle() + ",");
						out.print(currentPerson.getFirstname() == null ? "," : currentPerson.getFirstname() + ",");
						out.print(currentPerson.getLastname() == null ? "," : currentPerson.getLastname() + ",");
						String street = currentAddress.getStreet();
						String streetNo = currentAddress.getStreetNr();
						if (street == null) street = "";
						if (streetNo == null) streetNo = "";
						out.print(street + " " + streetNo + ",");
						out.print(currentAddress.getZipCode() == null ? "," : currentAddress.getZipCode() + ",");
						out.print(currentAddress.getCity() == null ? "," : currentAddress.getCity() + ",");
						out.print(currentAddress.getPhone1() == null ? "," : currentAddress.getPhone1() + ",");
						out.print(currentAddress.getPhone2() == null ? "," : currentAddress.getPhone2() + ",");
						out.print(currentAddress.getFax() == null ? "," : currentAddress.getFax() + ",");
						out.print(currentAddress.getEmail() == null ? "," : currentAddress.getEmail() + ",");
						out.println(currentUnit.getName().trim());
					}
					if (!hasAddress) {
						out.print(currentPerson.getTitle() == null ? "," : currentPerson.getTitle() + ",");
						out.print(currentPerson.getFirstname() == null ? "," : currentPerson.getFirstname() + ",");
						out.print(currentPerson.getLastname() == null ? "," : currentPerson.getLastname() + ",,,,,,,,");
						out.println(currentUnit.getName().trim());
					}
				}
			}
			if (log.isDebugEnabled()) log.debug("Finished exportXlsPersonData");
			out.flush();
			out.close();
			out = null;
			return new FileInputStream(fle);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	@Override
	protected SiteValue handleGetSite(Integer siteId) throws Exception {
		SiteHbm site = getSiteHbmDao().load(siteId);
		if (site != null) { return site.getSiteValue(); }
		return null;
	}

	@Override
	protected void handleSetRedirectUrl(String hostName, String redirectUrl) throws Exception {
		if (log.isDebugEnabled()) log.debug("setRedirectUrl host: " + hostName + ", redirectUrl: " + redirectUrl);
		HostHbm host = null;
		try {
			host = getHostHbmDao().load(hostName);
			if (redirectUrl != null) {
				host.setRedirectUrl(redirectUrl);
			} else {
				host.setRedirectUrl(null);
			}
		} catch (Exception e) {
			throw new UserException("Host \"" + hostName + "\" not found!\n" + e.getMessage());
		}
	}

	@Override
	protected void handleSetRedirectHostName(String hostName, String redirectHostName) throws Exception {
		if (log.isDebugEnabled()) log.debug("setRedirectHostName host: " + hostName + ", redirectHost: " + redirectHostName);
		HostHbm host = null;
		try {
			host = getHostHbmDao().load(hostName);
			if (redirectHostName != null) {
				host.setRedirectHostName(getHostHbmDao().load(redirectHostName));
			} else {
				host.setRedirectHostName(null);
			}
		} catch (Exception e) {
			throw new UserException("Host \"" + hostName + "\" or \"" + redirectHostName + "\" not found!\n" + e.getMessage());
		}
	}

	@Override
	protected void handleSetLiveServer(String hostname, boolean liveServer) throws Exception {
		if (log.isDebugEnabled()) log.debug("setLiveServer host: " + hostname);
		HostHbm host = null;
		try {
			host = getHostHbmDao().load(hostname);
			host.setLiveserver(liveServer);
		} catch (Exception e) {
			throw new UserException("Error in setLiveServer" + e.getMessage());
		}

	}

	@Override
	protected HostValue handleCreateHost(HostValue hostValue) throws Exception {
		HostHbm host = new HostHbmImpl();
		try {
			host.setHostName(hostValue.getHostName());
			host = this.createHostHbmFromValue(hostValue, host);
			HostHbmDao hostHbmDao = getHostHbmDao();
			host = hostHbmDao.create(host);
		} catch (Exception e) {
			throw new UserException("Error at creating host" + e.getMessage());

		}
		return host.getHostValue();
	}

	@Override
	protected void handleUpdateHost(HostValue hostValue) throws Exception {
		try {
			HostHbm hostHbm = null;
			if (hostValue != null && hostValue.getHostName() != null) {
				hostHbm = getHostHbmDao().load(hostValue.getHostName());
				hostHbm = this.createHostHbmFromValue(hostValue, hostHbm);
			}
			getHostHbmDao().update(hostHbm);
		} catch (Exception e) {
			throw new UserException("Error at updating host" + e.getMessage());
		}

	}

	private HostHbm createHostHbmFromValue(HostValue hostValue, HostHbm host) {
		HostHbm hostHbm = host;
		hostHbm.setLiveserver(hostValue.getLiveServer());
		if (hostValue.getRedirectHostName() != null) {
			hostHbm.setRedirectHostName(getHostHbmDao().load(hostValue.getRedirectHostName()));
		} else {
			hostHbm.setRedirectHostName(null);
		}
		hostHbm.setRedirectUrl(hostValue.getRedirectUrl());
		if (hostValue.getSiteId() != null) {
			SiteHbm site = getSiteHbmDao().load(hostValue.getSiteId());
			hostHbm.setSite(site);
		} else {
			hostHbm.setSite(null);
		}
		if (hostValue.getStartPageId() != null) {
			ViewComponentHbm startPage = getViewComponentHbmDao().load(hostValue.getStartPageId());
			hostHbm.setStartPage(startPage);
		} else {
			hostHbm.setStartPage(null);
		}
		if (hostValue.getUnitId() != null) {
			UnitHbm unit = getUnitHbmDao().load(hostValue.getUnitId());
			hostHbm.setUnit(unit);
		} else {
			hostHbm.setUnit(null);
		}

		return hostHbm;
	}

}
