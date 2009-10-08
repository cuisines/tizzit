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
package de.juwimm.cms.search.xmldb;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring;
import de.juwimm.cms.search.vo.XmlSearchValue;

/**
 *
 * @author <a href="mailto:j2ee@juwimm.com">Sascha-Matthias Kulawik</a>
 * company Juwi|MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since cqcms-core 27.03.2009
 */
public class XmlDbFactory {
	private static Logger log = Logger.getLogger(XmlDbFactory.class);
	private static XmlDb instance = null;

	private TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring = null;

	@Autowired
	public void setTizzitPropertiesBeanSpring(TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring) {
		this.tizzitPropertiesBeanSpring = tizzitPropertiesBeanSpring;
	}

	public TizzitPropertiesBeanSpring getTizzitPropertiesBeanSpring() {
		return tizzitPropertiesBeanSpring;
	}

	public XmlDb getInstance() {
		if (instance == null) {
			String clazz = getTizzitPropertiesBeanSpring().getSearch().getXmlDb();
			log.info("Resolving XmlDb Instance for class " + clazz);
			if (clazz == null || "".equals(clazz)) {
				log.warn("No useful classname found for XmlDb Search - XmlSearch disabled!");
				instance = new NullXmlDb();
			} else {
				//instance = (XmlDb) springPluginServerClassloadi	ngHelper.loadServerClass(clazz);
				try {
				//	instance = (XmlDb) ClassloadingHelper.getInstance(clazz, tizzitPropertiesBeanSpring);
				} catch (Exception exe) {
					instance = null;
				}
			}
		}
		return instance;
	}

	/**
	 *
	 * @author <a href="mailto:j2ee@juwimm.com">Sascha-Matthias Kulawik</a>
	 * company Juwi|MacMillan Group GmbH, Walsrode, Germany
	 * @version $Id$
	 * @since cqcms-core 27.03.2009
	 */
	private static class NullXmlDb implements XmlDb {
		private static Logger log = Logger.getLogger(NullXmlDb.class);

		public void deleteXml(Integer siteId, Integer viewComponentId) {
			if (log.isDebugEnabled()) log.debug("NullXmlDb does not support deleteXml");
		}

		public boolean saveXml(Integer siteId, Integer viewComponentId, String contentText, Map<String, String> metaAttributes) {
			if (log.isDebugEnabled()) log.debug("NullXmlDb does not support saveXml");
			return true;
		}

		public XmlSearchValue[] searchXml(Integer siteId, String xpathQuery) {
			if (log.isDebugEnabled()) log.debug("NullXmlDb does not support searchXml");
			return new XmlSearchValue[0];
		}

		public XmlSearchValue[] searchXmlByUnit(Integer unitId, Integer viewDocumentId, String xpathQuery) {
			if (log.isDebugEnabled()) log.debug("NullXmlDb does not support searchXml");
			return new XmlSearchValue[0];
		}
	}
}
