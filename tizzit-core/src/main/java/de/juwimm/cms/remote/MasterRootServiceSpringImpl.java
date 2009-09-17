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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.authorization.model.UserHbmImpl;
import de.juwimm.cms.authorization.vo.UserValue;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.HostHbm;
import de.juwimm.cms.model.SequenceHbmDao;
import de.juwimm.cms.model.ShortLinkHbm;
import de.juwimm.cms.model.SiteGroupHbm;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.vo.HostValue;
import de.juwimm.cms.vo.ShortLinkValue;
import de.juwimm.cms.vo.SiteGroupValue;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.UnitValue;

/**
 * @see de.juwimm.cms.remote.MasterRootServiceSpring
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class MasterRootServiceSpringImpl extends MasterRootServiceSpringBase {
	private static final Logger log = Logger.getLogger(MasterRootServiceSpringImpl.class);

	@Autowired
	private SequenceHbmDao sequenceHbmDao;

	/**
	 * @see de.juwimm.cms.remote.MasterRootServiceSpring#createSite(de.juwimm.cms.vo.SiteValue)
	 */
	@Override
	protected SiteValue handleCreateSite(SiteValue vo) throws Exception {
		try {
			SiteHbm siteHbm = new SiteHbmImpl();
			if (vo.getSiteId() == null) {
				vo.setSiteId(sequenceHbmDao.getNextSequenceNumber("site.site_id"));
			}
			siteHbm.setSiteValue(vo);
			SiteHbmDao siteDao = super.getSiteHbmDao();
			SiteHbm newSiteHbm = siteDao.create(siteHbm);
			return newSiteHbm.getSiteValue();
		} catch (Exception e) {
			log.error("Error creating site: " + e.getMessage(), e);
		}
		return vo;
	}

	/**
	 * @see de.juwimm.cms.remote.MasterRootServiceSpring#deleteSite(java.lang.Integer)
	 */
	@Override
	protected void handleDeleteSite(Integer siteId) throws Exception {
		getViewDocumentHbmDao().remove(getViewDocumentHbmDao().findAll(siteId));
		getSiteHbmDao().remove(siteId);
	}

	/**
	 * @see de.juwimm.cms.remote.MasterRootServiceSpring#changeSite(de.juwimm.cms.vo.SiteValue)
	 */
	@Override
	protected void handleChangeSite(SiteValue vo) throws Exception {
		SiteHbm siteHbm = super.getSiteHbmDao().load(vo.getSiteId());
		siteHbm.setSiteValue(vo);
		super.getSiteHbmDao().update(siteHbm);
	}

	/**
	 * @see de.juwimm.cms.remote.MasterRootServiceSpring#getAllSites()
	 */
	@Override
	protected SiteValue[] handleGetAllSites() throws Exception {
		SiteValue[] sites = null;
		try {
			Collection<SiteHbm> sitesList = super.getSiteHbmDao().findAll();
			sites = new SiteValue[sitesList.size()];
			Iterator<SiteHbm> it = sitesList.iterator();
			int i = 0;
			while (it.hasNext()) {
				SiteHbm site = it.next();
				sites[i++] = site.getSiteValue();
			}
		} catch (Exception e) {
			log.error("Error loading all sites: " + e.getMessage(), e);
		}
		return sites;
	}

	/**
	 * @see de.juwimm.cms.remote.MasterRootServiceSpring#getAllUserForAllSites()
	 */
	@Override
	protected UserValue[] handleGetAllUserForAllSites() throws Exception {
		try {
			Collection coll = null;
			int siz = 0;
			try {
				coll = super.getUserHbmDao().findAll();
				siz = coll.size();
			} catch (Exception exe) {
				log.warn("Error while executing the finder \"findAll\" in getAllUserForAllSites: " + exe.getMessage());
			}
			ArrayList<UserValue> al = new ArrayList<UserValue>(siz);
			Iterator it = coll.iterator();
			while (it.hasNext()) {
				UserHbmImpl user = (UserHbmImpl) it.next();
				if (!user.isMasterRoot()) {
					al.add(user.getUserValue());
				}
			}
			return al.toArray(new UserValue[0]);
		} catch (Exception exe) {
			log.error("ERROR", exe);
			throw new UserException(exe.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.MasterRootServiceSpring#getSiteConfig(java.lang.Integer)
	 */
	@Override
	protected String handleGetSiteConfig(Integer siteId) throws Exception {
		return super.getSiteHbmDao().load(siteId).getConfigXML();
	}

	/**
	 * @see de.juwimm.cms.remote.MasterRootServiceSpring#setSiteConfig(java.lang.Integer, java.lang.String)
	 */
	@Override
	protected void handleSetSiteConfig(Integer siteId, String config) throws Exception {
		super.getSiteHbmDao().load(siteId).setConfigXML(config);
	}

	/**
	 * @see de.juwimm.cms.remote.MasterRootServiceSpring#getAllHostsForSite(java.lang.Integer)
	 */
	@Override
	protected HostValue[] handleGetAllHostsForSite(Integer siteId) throws Exception {
		try {
			Collection hosts4Site = super.getHostHbmDao().findAll(siteId);
			HostValue[] retArr = new HostValue[hosts4Site.size()];
			Iterator it = hosts4Site.iterator();
			int i = 0;
			while (it.hasNext()) {
				HostHbm temp = (HostHbm) it.next();
				retArr[i] = temp.getHostValue();
				i++;
			}
			return retArr;
		} catch (Exception exe) {
			log.error("ERROR", exe);
			throw new UserException(exe.getMessage());
		}
	}

	@Override
	protected SiteGroupValue[] handleGetAllSiteGroups() throws Exception {
		ArrayList<SiteGroupValue> siteGroupList = new ArrayList<SiteGroupValue>();
		try {
			Iterator it = super.getSiteGroupHbmDao().findAll().iterator();
			while (it.hasNext()) {
				SiteGroupHbm siteGroup = (SiteGroupHbm) it.next();
				siteGroupList.add(siteGroup.getSiteGroupValue());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return siteGroupList.toArray(new SiteGroupValue[0]);
	}

	@Override
	protected SiteGroupValue handleCreateSiteGroup(SiteGroupValue vo) throws Exception {
		if (vo != null) {
			try {
				SiteGroupHbm siteGroup = SiteGroupHbm.Factory.newInstance();
				if (vo.getSiteGroupId() == null) {
					vo.setSiteGroupId(sequenceHbmDao.getNextSequenceNumber("sitegroup.site_group_id"));
				}
				getSiteGroupHbmDao().setSiteGroupValue(vo, siteGroup);
				//				siteGroup.setSiteGroupValue(vo);
				siteGroup = super.getSiteGroupHbmDao().create(siteGroup);
				vo = siteGroup.getSiteGroupValue();
			} catch (Exception e) {
				log.error("Error creating new SiteGroup: " + e.getMessage(), e);
			}
		}
		return vo;
	}

	@Override
	protected void handleSetSiteGroup(SiteGroupValue vo) throws Exception {
		if (vo != null) {
			try {
				SiteGroupHbm siteGroup = super.getSiteGroupHbmDao().load(vo.getSiteGroupId());
				getSiteGroupHbmDao().setSiteGroupValue(vo, siteGroup);
				//				siteGroup.setSiteGroupValue(vo);
			} catch (Exception e) {
				log.error("Error setting SiteGroupValue: " + e.getMessage(), e);
			}
		}
	}

	@Override
	protected void handleDeleteSiteGroup(Integer siteGroupId) throws Exception {
		if (siteGroupId != null) {
			try {
				super.getSiteGroupHbmDao().remove(siteGroupId);
			} catch (Exception e) {
				log.error("Error removing SiteGroup: " + e.getMessage(), e);
			}
		}
	}

	@Override
	protected SiteValue[] handleGetSites4SiteGroup(Integer siteGroupId) throws Exception {
		if (siteGroupId != null) {
			try {
				Collection sitesList = super.getSiteGroupHbmDao().load(siteGroupId).getSites();
				SiteValue[] siteValueList = new SiteValue[sitesList.size()];
				int i = 0;
				Iterator it = sitesList.iterator();
				while (it.hasNext()) {
					SiteHbm site = (SiteHbm) it.next();
					siteValueList[i] = site.getSiteValue();
					i++;
				}
				return siteValueList;
			} catch (Exception e) {
				log.error("Error getting all Sites for SiteGroup: " + e.getMessage(), e);
			}
		}
		return new SiteValue[0];
	}

	@Override
	protected SiteValue[] handleGetAllNotAssignedSites2SiteGroups() throws Exception {
		try {
			Collection sitesList = super.getSiteHbmDao().findAllNotAssigned2SiteGroups();
			SiteValue[] siteValueList = new SiteValue[sitesList.size()];
			int i = 0;
			Iterator it = sitesList.iterator();
			while (it.hasNext()) {
				SiteHbm site = (SiteHbm) it.next();
				siteValueList[i++] = site.getSiteValue();
			}
			return siteValueList;
		} catch (Exception e) {
			log.error("Error getting all not assigned to a SiteGroup Sites: " + e.getMessage(), e);
		}
		return new SiteValue[0];
	}

	@Override
	protected ShortLinkValue handleCreateShortLink(ShortLinkValue shortLinkValue) throws Exception {
		if (shortLinkValue != null) {
			try {
				ShortLinkHbm shortLink = ShortLinkHbm.Factory.newInstance();
				if (shortLinkValue.getShortLinkId() == null) shortLinkValue.setShortLinkId(sequenceHbmDao.getNextSequenceNumber("shortlink.short_link_id"));
				getShortLinkHbmDao().setShortLinkValue(shortLinkValue, shortLink);
				//				shortLink.setShortLinkValue(shortLinkValue);
				shortLink = super.getShortLinkHbmDao().create(shortLink);
				shortLinkValue = shortLink.getShortLinkValue();
			} catch (Exception e) {
				log.error("Error creating new ShortLink: " + e.getMessage(), e);
				throw new UserException("Error creating new ShortLink: " + e.getMessage());
			}
		}
		return shortLinkValue;
	}

	@Override
	protected ShortLinkValue handleSetShortLink(ShortLinkValue shortLinkValue) throws Exception {
		ShortLinkValue result = null;
		try {
			ShortLinkHbm shortLink = super.getShortLinkHbmDao().load(shortLinkValue.getShortLinkId());
			getShortLinkHbmDao().setShortLinkValue(shortLinkValue, shortLink);
			//			shortLink.setShortLinkValue(shortLinkValue);
			result = shortLink.getShortLinkValue();
		} catch (Exception e) {
			log.error("Error setting ShortLinkValue: " + e.getMessage(), e);
			throw new UserException("Error setting ShortLinkValue: " + e.getMessage());
		}
		return result;
	}

	@Override
	protected void handleDeleteShortLink(ShortLinkValue shortLinkValue) throws Exception {
		if (shortLinkValue != null) {
			try {
				super.getShortLinkHbmDao().remove(shortLinkValue.getShortLinkId());
			} catch (Exception e) {
				log.error("Error deleting ShortLink: " + e.getMessage(), e);
				throw new UserException("Error deleting ShortLink: " + e.getMessage());
			}
		}
	}

	@Override
	protected ShortLinkValue[] handleGetAllShortLinks4Site(Integer siteId) throws Exception {
		try {
			Collection shortLinks4Site = super.getShortLinkHbmDao().findAll(siteId);
			ShortLinkValue[] result = new ShortLinkValue[shortLinks4Site.size()];
			Iterator it = shortLinks4Site.iterator();
			int i = 0;
			while (it.hasNext()) {
				ShortLinkHbm shortLink = (ShortLinkHbm) it.next();
				result[i++] = shortLink.getShortLinkValue();
			}
			return result;
		} catch (Exception e) {
			log.error("Error getting all ShortLinkValues for Site " + siteId + ": " + e.getMessage(), e);
			throw new UserException("Error getting all ShortLinkValues for Site " + siteId + ": " + e.getMessage());
		}
	}

	@Override
	protected UnitValue[] handleGetAllUnits4Site(Integer siteId) throws Exception {
		try {
			Collection units4Site = super.getUnitHbmDao().findAll(siteId);
			UnitValue[] result = new UnitValue[units4Site.size()];
			Iterator it = units4Site.iterator();
			int i = 0;
			while (it.hasNext()) {
				UnitHbm unit = (UnitHbm) it.next();
				result[i++] = getUnitHbmDao().getDao(unit);
			}
			return result;
		} catch (Exception e) {
			log.error("Error getting all UnitValues for Site " + siteId + ": " + e.getMessage(), e);
			throw new UserException("Error getting all UnitValues for Site " + siteId + ": " + e.getMessage());
		}
	}

	@Override
	protected HostValue handleSetHost(HostValue hostValue) throws Exception {
		HostValue result = null;
		try {
			HostHbm host = super.getHostHbmDao().load(hostValue.getHostName());
			getHostHbmDao().setHostValue(hostValue, host);
			result = host.getHostValue();
		} catch (Exception e) {
			log.error("Error updating host " + hostValue.getHostName() + ": " + e.getMessage(), e);
		}
		return result;
	}

}
