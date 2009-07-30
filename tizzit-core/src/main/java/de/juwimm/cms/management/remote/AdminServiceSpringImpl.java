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
package de.juwimm.cms.management.remote;

import java.util.*;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.beans.foreign.support.MiniViewComponent;
import de.juwimm.cms.beans.foreign.support.MiniViewComponentComparator;
import de.juwimm.cms.beans.vo.LogfileValue;
import de.juwimm.cms.components.model.PersonHbm;
import de.juwimm.cms.model.HostHbm;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.search.beans.SearchengineService;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.util.HexConverter;

/**
 * At present this is the only Service the AdminClient is using.
 * 
 * @see de.juwimm.cms.management.remote.AdminServiceSpring
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class AdminServiceSpringImpl extends AdminServiceSpringBase {
	private static Logger log = Logger.getLogger(AdminServiceSpringImpl.class);
	
	@Autowired
	private SearchengineService searchengineService;

	@Override
	public String handleGetSiteList() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			Collection<SiteHbm> sites = super.getSiteHbmDao().findAll();
			for (SiteHbm site : sites) {
				sb.append("<tr><td>(" + site.getSiteId() + ")</td><td><b>" + site.getName()
						+ "</b></td><td>&#160;</td></tr>");
				Collection<HostHbm> hosts = super.getHostHbmDao().findAll(site.getSiteId());
				for (HostHbm host : hosts) {
					String hostName = "http://" + host.getHostName();
					sb.append("<tr><td>&#160;</td><td>&#160;</td><td><a href=\"" + hostName + "\">" + hostName
							+ "</a></td></tr>");
				}
			}
			sb.append("</table>");
		} catch (Exception exe) {
			log.error("Error ", exe);
		}
		return sb.toString();
	}

	@Override
	protected void handleStartLogfileParsing() throws Exception {
		new Thread() {
			@Override
			public void run() {
				getLogfileServiceSpring().startParsing();
			}
		}.start();
	}

	@Override
	protected LogfileValue handleGetLogfileValue() throws Exception {
		return super.getLogfileServiceSpring().getLogfileValue();
	}

	@Override
	protected void handleSetLogfileValue(LogfileValue lv) throws Exception {
		super.getLogfileServiceSpring().setLogfileValue(lv);
	}

	@Override
	protected SiteValue[] handleGetAllSites() throws Exception {
		return super.getMasterRootServiceSpring().getAllSites();
	}

	@Override
	protected SiteValue handleCreateSite(SiteValue siteValue) throws Exception {
		return super.getMasterRootServiceSpring().createSite(siteValue);
	}

	@Override
	protected void handleUpdateSite(SiteValue siteValue) throws Exception {
		super.getMasterRootServiceSpring().changeSite(siteValue);
	}

	@Override
	protected void handleDeleteSite(SiteValue siteValue) throws Exception {
		super.getMasterRootServiceSpring().deleteSite(siteValue.getSiteId());
	}

	@Override
	protected String handleGetSiteConfig(SiteValue siteValue) throws Exception {
		return super.getMasterRootServiceSpring().getSiteConfig(siteValue.getSiteId());
	}

	@Override
	protected void handleSetSiteConfig(SiteValue siteValue, String config) throws Exception {
		super.getMasterRootServiceSpring().setSiteConfig(siteValue.getSiteId(), config);
	}

	@Override
	protected void handleStartSearchIndexer() throws Exception {
		new Thread() {
			@Override
			public void run() {
//				getSearchengineServiceSpring().startIndexer();
			}
		}.start();
	}

	@Override
	protected void handleStartSearchIndexer(Integer siteId) throws Exception {
		new Thread(new StartSearchIndexer(siteId)).start();
	}

	private class StartSearchIndexer implements Runnable {
		private Integer siteId;

		public StartSearchIndexer(Integer siteId) {
			this.siteId = siteId;
		}

		public void run() {
			try {
				searchengineService.reindexSite(this.siteId);
			} catch (Exception e) {
				log.error("Error", e);
			}
		}

	}

	@Override
	protected void handleStartUpdateDocumentUseCount() throws Exception {
		//searchengineService.startUpdateDocumentUseCount();
	}

	@Override
	protected void handleStartUpdateDocumentUseCount(Integer siteId) throws Exception {
		//searchengineService.startUpdateDocumentUseCount(siteId);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleStartTreeRepair(Integer parentId) throws Exception {
		List<MiniViewComponent> lst = super.getConQuestSqlDaoSpring().getViewComponentByParentId(parentId);
		Collections.sort(lst, new MiniViewComponentComparator());

		MiniViewComponent prevMvc = lst.get(0);
		for (MiniViewComponent currentMvc : lst) {
			if (prevMvc.equals(currentMvc)) continue;
			super.getConQuestSqlDaoSpring().updateMvc(prevMvc, currentMvc);
			prevMvc = currentMvc;
		}

		super.getConQuestSqlDaoSpring().updateFirstMvc(lst.get(0));
		super.getConQuestSqlDaoSpring().updateLastMvc(lst.get(lst.size() - 1));
		super.getConQuestSqlDaoSpring().updateParentMvc(parentId.intValue(), lst.get(0));
	}

	@Override
	protected void handleImportActiveDirectoryPersonData(Map properties) throws Exception {
		Hashtable env = new Hashtable();
		env.put(Context.SECURITY_AUTHENTICATION, properties.get(Context.SECURITY_AUTHENTICATION));
		env.put(Context.INITIAL_CONTEXT_FACTORY, properties.get(Context.INITIAL_CONTEXT_FACTORY));
		env.put(Context.PROVIDER_URL, properties.get(Context.PROVIDER_URL));
		env.put(Context.SECURITY_PRINCIPAL, properties.get(Context.SECURITY_PRINCIPAL));
		env.put(Context.SECURITY_CREDENTIALS, properties.get(Context.SECURITY_CREDENTIALS));
		try {
			StringBuffer sb = new StringBuffer();
			DirContext ctx = new InitialDirContext(env);
			String base = (String) properties.get("baseDN");
			String importUnitId = (String) properties.get("importUnitId");
			Integer unitId = null;
			try {
				unitId = Integer.valueOf(importUnitId);
			} catch (Exception e) {
				log.error("Error parsing unitId " + importUnitId + ": " + e.getMessage() + "\nImport is CANCELED!");
				return;
			}
			String filter = "(&(objectclass=person))"; 
			String[] attrNames = {"cn", "mail", "title", "objectclass", "whenChanged", "objectGUID"};

			SearchControls ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			ctls.setReturningAttributes(attrNames);
			ctls.setReturningObjFlag(true);

			NamingEnumeration members = ctx.search(base, filter, ctls);
			while (members.hasMoreElements()) {
				SearchResult result = (SearchResult) members.nextElement();
				DirContext entry = (DirContext) result.getObject();
				sb.append("\n").append(entry.getNameInNamespace()).append("\n");
				PersonHbm personAD = PersonHbm.Factory.newInstance();
				Attributes attrs = result.getAttributes();
				/*
				{
					// fetch and save values
					Attribute objectId = attrs.get("objectGUID");
					personAD.setExternalId(HexConverter.stringToHex((String) objectId.get()));
					String whenChanged = (String) attrs.get("whenChanged").get();
					String lastModified = whenChanged.substring(0, whenChanged.indexOf('.'));
					personAD.setLastModifiedDate(Long.parseLong(lastModified));
				}
				PersonValue personCQ = null;
				boolean toImport = false;
				try {
					personCQ = super.getComponentsServiceSpring().getPersonByExternalId(personAD.getExternalId());
				} catch (Exception e) {
					if (log.isDebugEnabled()) log.debug("Person " + personAD.getExternalId() + " does not exist: " + e.getMessage(), e);
					toImport = true;
				}
				if (personCQ == null) toImport = true;
				if (!toImport) {
					toImport = (personAD.getLastModifiedDate() > personCQ.getLastModifiedDate());
				}
				if (toImport) {
					// TODO fill all attributes, create person (and address) and assign to unit
				}
				*/
				NamingEnumeration aEnum = attrs.getAll();
				while (aEnum.hasMoreElements()) {
					Attribute att = (Attribute) aEnum.nextElement();
					if ("objectGUID".equalsIgnoreCase(att.getID())) {
						sb.append(HexConverter.stringToHex((String) att.get())).append("\n");
					} else if ("whenChanged".equalsIgnoreCase(att.getID())) {
						String whenChanged = (String) att.get();
						String lastModified = whenChanged.substring(0, whenChanged.indexOf('.'));
						Long.parseLong(lastModified);
					}
					sb.append(att).append("\n");
				}
			}
			ctx.close();
			log.info(sb.toString());
			// TODO check for updates and import fetched data
		} catch (NamingException e) {
			log.error("Problem getting attribute: " + e.getMessage());
			if (log.isDebugEnabled()) log.debug("Problem getting attribute: " + e.getMessage(), e);
		}

	}

}
