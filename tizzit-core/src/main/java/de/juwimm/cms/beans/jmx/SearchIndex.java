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
package de.juwimm.cms.beans.jmx;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import de.juwimm.cms.search.beans.SearchengineService;
import de.juwimm.cms.search.vo.XmlSearchValue;

/**
 * SearchengineIndexer is a timerbased JMX-bean, which will call the indexing tasks in the lucene package.
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JuwiMacMillan Group</p>
 * @author <a href="s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 */
@ManagedResource(objectName="org.cqcms:name=SearchIndex", description="Search Index Management")
public class SearchIndex {
	private static Logger log = Logger.getLogger(SearchIndex.class);
	@Autowired
	private SearchengineService searchengineService;
	private boolean enabled = false;

	@ManagedAttribute(description = "enabled")
	public boolean isEnabled() {
		return this.enabled;
	}

	@ManagedAttribute(description = "enabled")
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@ManagedOperation(description = "SearchengineIndexer Dynamic Manageable Bean for backgroud-creation of the SearchEngine per Site")
	public void reindexSite(Integer siteId) {
		try {
			searchengineService.reindexSite(siteId);
		} catch (Exception exe) {
			log.error("Error occured: ", exe);
		}
	}

	@ManagedOperation(description = "XMLSearch for Test and Debugging")
	public String startXMLSearch(Integer siteId, String xPathQuery) {
		String result = "";

		try {
			XmlSearchValue[] searchResult = searchengineService.searchXML(siteId, xPathQuery);
			for (int i = 0; i < searchResult.length; i++) {
				result += "\n\nEntry " + (i + 1) + "\n";
				result += "UnitId: " + searchResult[i].getUnitId().toString() + "\n";
				result += "InfoText: " + searchResult[i].getInfoText() + "\n";
				result += "Text: " + searchResult[i].getText() + "\n";
				result += "Content:\n";

				String content = searchResult[i].getContent();
				content = content.replace('<', '\u0085');
				content = content.replace('>', '\u0087');
				content = content.replace('&', '\u0038');

				result += content;
			}

			if (result.equals("")) result = "Nothing was found for XPathQuery=" + xPathQuery + " on site with siteId=" + siteId.toString();
		} catch (Exception exe) {
			log.error("Error occured", exe);
			result = "Error occured executing XMLSearch for XPathQuery=" + xPathQuery + " on site with siteId=" + siteId.toString();
		}

		return result;
	}
}