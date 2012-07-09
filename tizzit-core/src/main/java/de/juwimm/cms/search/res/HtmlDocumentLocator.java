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
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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
public class HtmlDocumentLocator {
	private static Logger log = Logger.getLogger(HtmlDocumentLocator.class);
	@Autowired
	private UnitHbmDao unitHbmDao;
	@Autowired
	private ContentHbmDao contentHbmDao;
	@Autowired
	private Realm2viewComponentHbmDao realm2VcHbmDao;

	public Document getResource(File file, String url, Date modifiedDate, ViewComponentHbm vcl, ViewDocumentHbm vdl) throws IOException, InterruptedException {
		String cleanUrl = vdl.getLanguage() + "/" + vcl.getPath();
		Document resource = new Document();// resourceFactory.createResource("HtmlSearchValue");
		resource.add(new Field("alias", "HtmlSearchValue", Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
		resource.add(new Field("url", cleanUrl, Field.Store.YES, Field.Index.ANALYZED));
		resource.add(new Field("siteId", vdl.getSite().getSiteId().toString(), Field.Store.YES, Field.Index.ANALYZED));
		resource.add(new Field("language", vdl.getLanguage(), Field.Store.YES, Field.Index.ANALYZED));
		resource.add(new Field("viewtype", vdl.getViewType(), Field.Store.YES, Field.Index.ANALYZED));
		resource.add(new Field("modified", DateTools.timeToString(modifiedDate.getTime(), DateTools.Resolution.MINUTE), Field.Store.YES, Field.Index.ANALYZED));
		resource.add(new Field("uid", url, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
		resource.add(new Field("viewComponentId", vcl.getViewComponentId().toString(), Field.Store.YES, Field.Index.ANALYZED));
		try {
			Integer unitId = vcl.getViewComponentUnit().getUnit4ViewComponent();
			if (unitId != null) {
				UnitHbm unit = unitHbmDao.load(unitId);
				resource.add(new Field("unitId", unit.getUnitId().toString(), Field.Store.YES, Field.Index.ANALYZED));
				resource.add(new Field("unitName", unit.getName(), Field.Store.YES, Field.Index.ANALYZED));
			}
		} catch (Exception exe) {
			if (log.isDebugEnabled()) log.debug("unitId could not be loaded for vcId: " + vcl.getViewComponentId(), exe);
		}
		try {
			ContentHbm content = contentHbmDao.load(new Integer(vcl.getReference()));
			resource.add(new Field("template", content.getTemplate(), Field.Store.YES, Field.Index.ANALYZED));
		} catch (Exception exe) {
			if (log.isDebugEnabled()) log.debug("template could not be loaded for vcId: " + vcl.getViewComponentId(), exe);
		}
		if (log.isDebugEnabled()) log.debug("looking for realm now: ");
		try {
			String realm = getRealm4Vc(vcl);
			if (realm != null) {
				if (log.isDebugEnabled()) log.debug("realm is: " + realm);
				resource.add(new Field("realm", realm, Field.Store.YES, Field.Index.ANALYZED));
			} else {
				if (log.isDebugEnabled()) log.debug("page not protected - adding 'nullValue' for the search query.");
				resource.add(new Field("realm", Constants.SEARCH_INDEX_NULL, Field.Store.YES, Field.Index.ANALYZED));
			}
		} catch (Exception exe) {
			if (log.isDebugEnabled()) log.debug("realms could not be loaded for vcId: " + vcl.getViewComponentId(), exe);
		}
		InputStream inputStream=new FileInputStream(file);
		HTMLParser parser = new HTMLParser(inputStream);
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
		//if (log.isDebugEnabled()) 
		log.info("createRealmRoleCombo realm: " + realm + " roles: " + rolesNeeded);
		if (rolesNeeded == null) return realm;
		String result = "";
		String[] roles = null;
		roles = rolesNeeded.split(Constants.SAFEGUARD_ROLE_SEPARATOR);
		for (int i = 0; i < roles.length; i++) {
			log.info("found: >" + roles[i] + "<");
			if (!roles[i].trim().isEmpty()) {
				result = result + " " + realm + "_" + roles[i].trim();
				log.info("result is now: >" + result + "<");
			}
		}
		return result;
	}

	public Document getExternalResource(String url, Reader htmlContent) throws IOException, InterruptedException {
		Document resource = new Document();
		resource.add(new Field("alias", "HtmlSearchValue", Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
		resource.add(new Field("url", url, Field.Store.YES, Field.Index.ANALYZED));
		resource.add(new Field("uid", url, Field.Store.YES, Field.Index.ANALYZED));
		HTMLParser parser = new HTMLParser(htmlContent);
		return parseHtml(resource, parser);
	}

	public String stripNonValidXMLCharacters(String in) {
		if (in == null) return "";
		String stripped = in.replaceAll("[^\\u0009\\u000a\\u000d\\u0020-\\ud7ff\\e0000-\\ufffd]", "").replaceAll("[&<>]", "");
		return stripped;
	}

	private Document parseHtml(Document resource, HTMLParser parser) throws IOException, InterruptedException {
		Reader reader = parser.getReader();
		StringWriter sw = new StringWriter();
			org.apache.commons.io.IOUtils.copy(reader, sw);
		String sresult = sw.toString();

		if (log.isDebugEnabled()) log.debug("Saving tokenized HTML value into searchengine: " + sresult);
		resource.add(new Field("contents", stripNonValidXMLCharacters(sresult), Field.Store.YES, Field.Index.ANALYZED));

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
		resource.add(new Field("metadata", metadata, Field.Store.YES, Field.Index.ANALYZED));

		// Add the summary as a field that is stored and returned with
		// hit documents for display.
		resource.add(new Field("summary", parser.getSummary(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));

		// Add the title as a field that it can be searched and that is stored.
		resource.add(new Field("title", parser.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
		reader.close();
		return resource;
	}
}
