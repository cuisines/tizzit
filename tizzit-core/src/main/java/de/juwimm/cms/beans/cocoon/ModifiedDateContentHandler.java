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
package de.juwimm.cms.beans.cocoon;

import java.util.Date;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.juwimm.cms.beans.PluginManagement;
import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.plugins.server.TizzitPlugin;

/**
 * Specialized ContentHandler for calculating the lastModifiedDate of a content-page.</br>
 * It analyses the content for special tags like &quot;aggregation&quot; or &quot;navigation&quot;</br>
 * and decides dynamically which date to return as last-modified. The age of the current page may be high,</br>
 * but this lastModifiedDate must sometimes be newer for caching
 * 
 * @author <a href="mailto:sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public final class ModifiedDateContentHandler extends DefaultHandler {
	private static Logger log = Logger.getLogger(ModifiedDateContentHandler.class);
	private Date modifiedDate = null;
	private Date startTime = null;
	private Integer viewComponentId = null;
	private WebServiceSpring webServiceSpring = null;
	private Integer siteId = null;
	private boolean isLiveserver = false;
	private PluginManagement pluginManagement;

	public void destroy() {
		if (log.isDebugEnabled()) log.debug("calling destructor on ModifiedDateContentHandler");
	}
	
	public PluginManagement getPluginManagement() {
		return pluginManagement;
	}

	public void setPluginManagement(PluginManagement pluginManagement) {
		this.pluginManagement = pluginManagement;
	}

	public WebServiceSpring getWebServiceSpring() {
		return webServiceSpring;
	}

	public void setWebServiceSpring(WebServiceSpring webServiceSpring) {
		this.webServiceSpring = webServiceSpring;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if (!this.modifiedDate.equals(this.startTime)) {
			if (qName.equalsIgnoreCase("aggregation")) {
				this.modifiedDate = this.startTime;
			} else if (qName.equalsIgnoreCase("unitMembersList")) {
				this.modifiedDate = this.startTime;
			} else if (qName.equalsIgnoreCase("membersList")) {
				this.modifiedDate = this.startTime;
			} else if (qName.equalsIgnoreCase("navigation")) {
				String since = atts.getValue("since");
				int depth = -1;
				try {
					depth = new Integer(atts.getValue("depth")).intValue();
				} catch (Exception exe) {
				}
				int ifDistanceToNavigationRoot = -1;
				try {
					ifDistanceToNavigationRoot = new Integer(atts.getValue("ifDistanceToNavigationRoot")).intValue();
				} catch (Exception exe) {
				}

				try {
					if (ifDistanceToNavigationRoot == -1 || this.webServiceSpring.getNavigationRootDistance4VCId(this.viewComponentId) >= ifDistanceToNavigationRoot) {
						Date navDate = this.webServiceSpring.getNavigationAge(this.viewComponentId);
						if (this.modifiedDate.compareTo(navDate) <= 0) {
							this.modifiedDate = navDate;
						}
					}
				} catch (Exception exe) {
				}
			} else if (qName.equalsIgnoreCase("navigationBackward")) {
				this.modifiedDate = this.startTime;
			} else if (qName.equalsIgnoreCase("fulltextsearch")) {
				this.modifiedDate = this.startTime;
			}
			if (uri != null && uri.startsWith(de.juwimm.cms.plugins.Constants.PLUGIN_NAMESPACE)) {
				TizzitPlugin plugin = getPluginManagement().getPlugin(uri);
				if (plugin != null && plugin.isCacheable()) {
					Date pluginDate = plugin.getLastModifiedDate();
					if (this.modifiedDate.compareTo(pluginDate) <= 0) {
						this.modifiedDate = pluginDate;
					}
				} else {
					this.modifiedDate = this.startTime;
				}
			}
			if (this.modifiedDate.equals(this.startTime)) { throw new SAXException("this content is NOT cachable!"); // this is my break
			}
		}
	}

	/**
	 * @return the calculated lastModifiedDate
	 */
	public final Date getModifiedDate() {
		return this.modifiedDate;
	}

	/**
	 * Set the lastModifiedDate of the current page from the contentVersion</br>
	 * This value is needed for internal comparisons
	 * @param modifiedDate
	 */
	public final void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * Set the liveServer-property</br>
	 * The calculated value for the lastModifiedDate may sometimes depend on this flag
	 * @param isLiveserver
	 */
	public void setLiveserver(boolean isLiveserver) {
		this.isLiveserver = isLiveserver;
	}

	/**
	 * Set the current siteId</br>
	 * Needed if current page contains plugins
	 * @param siteId
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	/**
	 * Set the viewComponentId of the current page</br>
	 * Needed for calculating lastModifiedDate if page contains navigation
	 * @param viewComponentId
	 */
	public void setViewComponentId(Integer viewComponentId) {
		this.viewComponentId = viewComponentId;
	}
 
	@Override
	public void startDocument() throws SAXException {
		// this.modifiedDate = new Date(System.currentTimeMillis());
		this.startTime = new Date(System.currentTimeMillis());
	}

}