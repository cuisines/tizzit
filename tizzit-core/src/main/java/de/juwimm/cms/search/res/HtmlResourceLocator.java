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
package de.juwimm.cms.search.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.lucene.document.DateTools;
import org.compass.core.CompassSession;
import org.compass.core.Resource;
import org.compass.core.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.XercesHelper;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentHbmDao;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmDao;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewDocumentHbm;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbm;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbmDao;
import de.juwimm.cms.safeguard.model.RealmJaasHbm;
import de.juwimm.cms.safeguard.model.RealmJdbcHbm;
import de.juwimm.cms.safeguard.model.RealmLdapHbm;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbm;
import de.juwimm.cms.search.res.html.HTMLParser;

/**
 * Helper for getting injected the Compass-Instances from Spring and offering getters for them
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class HtmlResourceLocator {
	private static Logger log = Logger.getLogger(HtmlResourceLocator.class);
	@Autowired
	private UnitHbmDao unitHbmDao;
	@Autowired
	private ContentHbmDao contentHbmDao;
	@Autowired
	private Realm2viewComponentHbmDao realm2VcHbmDao;

	public Resource getResource(CompassSession session, File file, String url, Date modifiedDate, ViewComponentHbm vcl, ViewDocumentHbm vdl) throws IOException, InterruptedException {
		ResourceFactory resourceFactory = session.resourceFactory();
		Resource resource = resourceFactory.createResource("HtmlSearchValue");
		resource.addProperty("url", url);
		resource.addProperty("siteId", vdl.getSite().getSiteId().toString());
		resource.addProperty("language", vdl.getLanguage());
		resource.addProperty("viewtype", vdl.getViewType());
		resource.addProperty("modified", DateTools.timeToString(modifiedDate.getTime(), DateTools.Resolution.MINUTE));
		resource.addProperty("uid", url);
		resource.addProperty("viewComponentId", vcl.getViewComponentId());
		try {
			Integer unitId = vcl.getUnit4ViewComponent();
			if (unitId != null) {
				UnitHbm unit = unitHbmDao.load(unitId);
				resource.addProperty("unitId", unit.getUnitId().toString());
				resource.addProperty("unitName", unit.getName());
			}
		} catch (Exception exe) {
			if (log.isDebugEnabled()) log.debug("unitId could not be loaded for vcId: " + vcl.getViewComponentId(), exe);
		}
		try {
			ContentHbm content = contentHbmDao.load(new Integer(vcl.getReference()));
			resource.addProperty("template", content.getTemplate());
		} catch (Exception exe) {
			if (log.isDebugEnabled()) log.debug("template could not be loaded for vcId: " + vcl.getViewComponentId(), exe);
		}
		if (log.isDebugEnabled()) log.debug("looking for realm now: ");
		try {
			String realm = getRealm4Vc(vcl);
			if (realm != null) {
				if (log.isDebugEnabled()) log.debug("realm is: " + realm);
				resource.addProperty("realm", realm);
			} else {
				if (log.isDebugEnabled()) log.debug("page not protected - adding 'nullValue' for the search query.");
				resource.addProperty("realm", Constants.SEARCH_INDEX_NULL);
			}
		} catch (Exception exe) {
			if (log.isDebugEnabled()) log.debug("realms could not be loaded for vcId: " + vcl.getViewComponentId(), exe);
		}
		HTMLParser parser = new HTMLParser(new FileInputStream(file));
		return parseHtml(resource, parser);
	}

	private String getRealm4Vc(ViewComponentHbm vc) {
		if (vc == null) return null;
		Realm2viewComponentHbm realm2vcHmb = realm2VcHbmDao.findByViewComponent(vc.getViewComponentId());
		if (realm2vcHmb == null) {
			return getRealm4Vc(vc.getParent());
		}
		RealmJdbcHbm jdbc = realm2vcHmb.getJdbcRealm();
		if (jdbc != null) return createRealmRoleCombo("JDBC_" + jdbc.getJdbcRealmId(), realm2vcHmb.getRoleNeeded());
		RealmSimplePwHbm simple = realm2vcHmb.getSimplePwRealm();
		if (simple != null) return createRealmRoleCombo("SIMPLEPW_" + simple.getSimplePwRealmId(), realm2vcHmb.getRoleNeeded());
		RealmLdapHbm ldap = realm2vcHmb.getLdapRealm();
		if (ldap != null) return createRealmRoleCombo("LDAP_" + ldap.getLdapRealmId(), realm2vcHmb.getRoleNeeded());
		RealmJaasHbm jaas = realm2vcHmb.getJaasRealm();
		if (jaas != null) return createRealmRoleCombo("JAAS_" + jaas.getJaasRealmId(), realm2vcHmb.getRoleNeeded());
		return null;
	}

	private String createRealmRoleCombo(String realm, String rolesNeeded) {
		if (log.isDebugEnabled()) log.debug("createRealmRoleCombo realm: " + realm + " roles: " + rolesNeeded);
		if (rolesNeeded == null) return realm;
		String result = realm;
		String[] roles = null;
		roles = rolesNeeded.split(Constants.SAFEGUARD_ROLE_SEPARATOR);
		for (int i = 0; i < roles.length; i++) {
			if (!roles[i].isEmpty()) result = result + " " + realm + "_" + roles[i].trim();
		}
		return result;
	}

	public Resource getExternalResource(CompassSession session, String url, Reader htmlContent) throws IOException, InterruptedException {
		ResourceFactory resourceFactory = session.resourceFactory();
		Resource resource = resourceFactory.createResource("HtmlSearchValue");
		resource.addProperty("url", url);
		resource.addProperty("uid", url);
		HTMLParser parser = new HTMLParser(htmlContent);
		return parseHtml(resource, parser);
	}

	public String stripNonValidXMLCharacters(String in) {
		if (in == null) return "";
		String stripped = in.replaceAll("[^\\u0009\\u000a\\u000d\\u0020-\\ud7ff\\e0000-\\ufffd]", "").replaceAll("[&<>]", "");
		return stripped;
	}

	private Resource parseHtml(Resource resource, HTMLParser parser) throws IOException, InterruptedException {
		Reader reader = parser.getReader();
		StringWriter sw = new StringWriter();
		org.apache.commons.io.IOUtils.copy(reader, sw);
		String sresult = sw.toString();

		if (log.isDebugEnabled()) log.debug("Saving tokenized HTML value into searchengine: " + sresult);
		resource.addProperty("contents", stripNonValidXMLCharacters(sresult));

		Properties prop = parser.getMetaTags();
		Collection metafields = prop.values();
		String metadata = "";
		Iterator it = metafields.iterator();
		while (it.hasNext()) {
			StringTokenizer st = new StringTokenizer((String) it.next(), ",");
			while (st.hasMoreElements()) {
				String token = st.nextToken().trim();
				metadata += token + " ";
			}
		}
		// tidy the metadata
		metadata = XercesHelper.html2utf8string(metadata);
		resource.addProperty("metadata", metadata);

		// Add the summary as a field that is stored and returned with
		// hit documents for display.
		resource.addProperty("summary", parser.getSummary());

		// Add the title as a field that it can be searched and that is stored.
		resource.addProperty("title", parser.getTitle());
		reader.close();
		return resource;
	}
}
