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
/*
 * Created on 10.12.2004
 */
package de.juwimm.cms.cocoon.transformation;

import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.ServiceableTransformer;
import org.apache.cocoon.webapps.session.ContextManager;
import org.apache.cocoon.webapps.session.context.SessionContext;
import org.apache.log4j.Logger;
import org.tizzit.util.Base64;
import org.tizzit.util.xml.SAXHelper;
import org.w3c.dom.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


/**
 * This transformer fills the tag <code>authentication</code> in the content with information of the logged-in user.</br>
 * Please use the UnitTransformer after this one to fill the unit-data.</br>
 * This transformer must be used inside a CmsJaasSecurityGenerator, which generates the authentication-information.</br>
 * It results the sites, units, groups and roles for the logged-in user.
 * <p><h5>Configuration:</h5>
 * <pre>
 * &lt;map:transformer name="authentication" src="de.juwimm.cms.cocoon.transformation.AuthenticationTransformer"/&gt;
 * </pre>
 * </p>
 * <p><h5>Usage:</h5>
 * Just put code like this in your pipeline:
 * <pre>
 * &lt;map:transform type="authentication"/&gt;
 * </pre>
 * </p>
 * <p><h5>Result:</h5>
 * <pre>
 * &lt;authentication&gt;
 *  &lt;id&gt;name-of-logged-in-user&lt;/id&gt;
 *  &lt;unit id="413" siteId="12" disableParsing="false" disableUrlResolve="false"/&gt;
 *  &lt;unit id="404" siteId="12" disableParsing="false" disableUrlResolve="false"/&gt;
 *  &lt;unit id="593" siteId="12" disableParsing="false" disableUrlResolve="false"/&gt;
 *  &lt;unit id="407" siteId="12" disableParsing="false" disableUrlResolve="false"/&gt;
 *  &lt;unit id="402" siteId="12" disableParsing="false" disableUrlResolve="false"/&gt;
 *  &lt;unit id="575" siteId="17" disableParsing="false" disableUrlResolve="false"/&gt;
 *  &lt;unit id="648" siteId="17" disableParsing="false" disableUrlResolve="false"/&gt;
 *  &lt;unit id="578" siteId="17" disableParsing="false" disableUrlResolve="false"/&gt;
 *  &lt;unit id="564" siteId="17" disableParsing="false" disableUrlResolve="false"/&gt;
 *  &lt;unit id="577" siteId="17" disableParsing="false" disableUrlResolve="false"/&gt;
 *  &lt;unit id="649" siteId="17" disableParsing="false" disableUrlResolve="false"/&gt;
 *  &lt;unit id="599" siteId="17" disableParsing="false" disableUrlResolve="false"/&gt;
 *  &lt;unit id="576" siteId="17" disableParsing="false" disableUrlResolve="false"/&gt;
 *  &lt;roleInformation id="pageStatusbar" siteId="12"/&gt;
 *  &lt;roleInformation id="siteRoot" siteId="17"/&gt;
 *  &lt;roleInformation id="makeInvisible" siteId="12"/&gt;
 *  &lt;roleInformation id="openNewNavigation" siteId="12"/&gt;
 *  &lt;roleInformation id="manageHosts" siteId="17"/&gt;
 *  &lt;roleInformation id="manageHosts" siteId="12"/&gt;
 *  &lt;roleInformation id="viewWebstats" siteId="12"/&gt;
 *  &lt;roleInformation id="viewMetadata" siteId="12"/&gt;
 *  &lt;siteInformation id="17"&gt;
 *      &lt;sitename&gt;QM and Development&lt;/sitename&gt;
 *      &lt;siteshort&gt;qmd&lt;/siteshort&gt;
 *  &lt;/siteInformation&gt;
 *  &lt;siteInformation id="7"&gt;
 *      &lt;sitename&gt;kdo&lt;/sitename&gt;
 *      &lt;siteshort&gt;kdo&lt;/siteshort&gt;
 *  &lt;/siteInformation&gt;
 *  &lt;siteInformation id="12"&gt;
 *      &lt;sitename&gt;UKW&lt;/sitename&gt;
 *      &lt;siteshort&gt;ukw&lt;/siteshort&gt;
 *  &lt;/siteInformation&gt;
 *  &lt;siteInformation id="6"&gt;
 *      &lt;sitename&gt;kdo-ki&lt;/sitename&gt;
 *      &lt;siteshort&gt;kdo-ki&lt;/siteshort&gt;
 *  &lt;/siteInformation&gt;
 *  &lt;groupInformation id="33" siteId="12"&gt;
 *      &lt;groupname&gt;Autor&lt;/groupname&gt;
 *  &lt;/groupInformation&gt;
 *  &lt;groupInformation id="32" siteId="12"&gt;
 *      &lt;groupname&gt;Test-Hosts verwalten&lt;/groupname&gt;
 *  &lt;/groupInformation&gt;
 *  &lt;groupInformation id="31" siteId="17"&gt;
 *      &lt;groupname&gt;Hosts verwalten&lt;/groupname&gt;
 *  &lt;/groupInformation&gt;
 *  &lt;groupInformation id="19" siteId="17"&gt;
 *      &lt;groupname&gt;siteRootGroup&lt;/groupname&gt;
 *  &lt;/groupInformation&gt;
 *  &lt;groupInformation id="11" siteId="12"&gt;
 *      &lt;groupname&gt;siteRoot&lt;/groupname&gt;
 *  &lt;/groupInformation&gt;
 *  &lt;role&gt;openNewNavigation&lt;/role&gt;
 *  &lt;role&gt;viewMetadata&lt;/role&gt;
 *  &lt;role&gt;makeInvisible&lt;/role&gt;
 *  &lt;role&gt;pageStatusbar&lt;/role&gt;
 *  &lt;role&gt;manageHosts&lt;/role&gt;
 *  &lt;role&gt;viewWebstats&lt;/role&gt;
 * &lt;/authentication&gt;
 * </pre>
 * </p>
 * <p>
 * If you want to use the UnitTransformer please use a xslt-transformation to change the<br/>
 * values of the attributes &quot;disableParsing&quot; and &quot;disableUrlResolve&quot; of the &quot;unit&quot;-elements.
 * </p>
 * 
 * @see de.juwimm.cms.cocoon.transformation.UnitTransformer
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @since ConQuest 2.0
 */
public class AuthenticationTransformer extends ServiceableTransformer {
	private static Logger log = Logger.getLogger(AuthenticationTransformer.class);
	private SessionContext sessContext = null;
	private ContextManager cm = null;

	/* (non-Javadoc)
	 * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(
	 * org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
	 */
	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
		try {
			this.cm = (ContextManager) this.manager.lookup(ContextManager.ROLE);
			sessContext = cm.getContext("authentication");
		} catch (ServiceException e) {
			log.error(e.getMessage());
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		Node idN = null;
		contentHandler.startElement(uri, localName, qName, attrs);
		if (localName.equals("authentication")) {
			if (this.sessContext == null) {
				if (log.isDebugEnabled()) log.debug("No Logged-in User");
			} else {
				try {
					idN = sessContext.getXML("/authentication/ID");
				} catch (ProcessingException e) {
					log.error("An error occured: " + e.getMessage());
				}
				if (idN != null) {
					String id = idN.getFirstChild().getNodeValue();
					contentHandler.startElement(uri, "id", "id", new AttributesImpl());
					contentHandler.characters(id.toCharArray(), 0, id.length());
					contentHandler.endElement(uri, "id", "id");
				}
				try {
					DocumentFragment df = sessContext.getXML("/authentication/roles");
					if (df != null) {
						NodeList nl = df.getChildNodes();
						for (int i = 0; i < nl.getLength(); i++) {
							Node role = nl.item(i);
							String roleType = role.getFirstChild().getNodeValue();
							if (roleType.startsWith("unit_")) {
								AttributesImpl newAttr = new AttributesImpl();
								StringTokenizer st = new StringTokenizer(roleType, "_");
								if (st.hasMoreTokens()) {
									// skip "unit"
									st.nextToken();
									if (st.hasMoreTokens()) {
										String unitId = st.nextToken();
										SAXHelper.setSAXAttr(newAttr, "id", unitId);
									}
									if (st.hasMoreTokens()) {
										// skip unitName
										st.nextToken();
									}
									if (st.hasMoreTokens()) {
										// skip "site"
										st.nextToken();
										if (st.hasMoreTokens()) {
											String siteId = st.nextToken();
											SAXHelper.setSAXAttr(newAttr, "siteId", siteId);
										}
									}
								}
								SAXHelper.setSAXAttr(newAttr, "disableParsing", "false");
								SAXHelper.setSAXAttr(newAttr, "disableUrlResolve", "false");
								contentHandler.startElement(uri, "unit", "unit", newAttr);
								contentHandler.endElement(uri, "unit", "unit");
							} else if (roleType.startsWith("group_")) {
								AttributesImpl newAttr = new AttributesImpl();
								String groupName = "";
								StringTokenizer st = new StringTokenizer(roleType, "_");
								if (st.hasMoreTokens()) {
									// skip "group"
									st.nextToken();
									if (st.hasMoreTokens()) {
										String groupId = st.nextToken();
										SAXHelper.setSAXAttr(newAttr, "id", groupId);
									}
									if (st.hasMoreTokens()) {
										groupName = st.nextToken();
									}
									if (st.hasMoreTokens()) {
										// skip "site"
										st.nextToken();
										if (st.hasMoreTokens()) {
											String siteId = st.nextToken();
											SAXHelper.setSAXAttr(newAttr, "siteId", siteId);
										}
									}
								}
								contentHandler.startElement(uri, "groupInformation", "groupInformation", newAttr);
								String cData = Base64.decodeToString(groupName);
								contentHandler.startElement(uri, "groupname", "groupname", new AttributesImpl());
								contentHandler.characters(cData.toCharArray(), 0, cData.length());
								contentHandler.endElement(uri, "groupname", "groupname");
								contentHandler.endElement(uri, "groupInformation", "groupInformation");
							} else if (roleType.startsWith("site_")) {
								AttributesImpl newAttr = new AttributesImpl();
								String siteName = "";
								String siteShort = "";
								StringTokenizer st = new StringTokenizer(roleType, "_");
								if (st.hasMoreTokens()) {
									// skip "site"
									st.nextToken();
									if (st.hasMoreTokens()) {
										String siteId = st.nextToken();
										SAXHelper.setSAXAttr(newAttr, "id", siteId);
									}
									if (st.hasMoreTokens()) {
										siteName = st.nextToken();
									}
									if (st.hasMoreTokens()) {
										siteShort = st.nextToken();
									}
								}
								contentHandler.startElement(uri, "siteInformation", "siteInformation", newAttr);
								String cData = Base64.decodeToString(siteName);
								contentHandler.startElement(uri, "sitename", "sitename", new AttributesImpl());
								contentHandler.characters(cData.toCharArray(), 0, cData.length());
								contentHandler.endElement(uri, "sitename", "sitename");
								cData = Base64.decodeToString(siteShort);
								contentHandler.startElement(uri, "siteshort", "siteshort", new AttributesImpl());
								contentHandler.characters(cData.toCharArray(), 0, cData.length());
								contentHandler.endElement(uri, "siteshort", "siteshort");
								contentHandler.endElement(uri, "siteInformation", "siteInformation");
							} else if (roleType.startsWith("role_")) {
								AttributesImpl newAttr = new AttributesImpl();
								StringTokenizer st = new StringTokenizer(roleType, "_");
								if (st.hasMoreTokens()) {
									// skip "role"
									st.nextToken();
									if (st.hasMoreTokens()) {
										String roleId = st.nextToken();
										SAXHelper.setSAXAttr(newAttr, "id", roleId);
									}
									if (st.hasMoreTokens()) {
										if (st.hasMoreTokens()) {
											String siteId = st.nextToken();
											SAXHelper.setSAXAttr(newAttr, "siteId", siteId);
										}
									}
								}
								contentHandler.startElement(uri, "roleInformation", "roleInformation", newAttr);
								contentHandler.endElement(uri, "roleInformation", "roleInformation");
							} else {
								contentHandler.startElement(uri, "role", "role", new AttributesImpl());
								contentHandler.characters(roleType.toCharArray(), 0, roleType.length());
								contentHandler.endElement(uri, "role", "role");
							}
						}
					}
				} catch (ProcessingException e1) {
					log.error(e1.getMessage());
				}
			}
		}
	}

}
