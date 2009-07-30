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

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import de.juwimm.cms.search.vo.XmlSearchValue;

/**
 * 
 * @author <a href="mailto:j2ee@juwimm.com">Sascha-Matthias Kulawik</a>
 * company Juwi|MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since cqcms-core 27.03.2009
 */
public interface XmlDb {
	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS", Locale.GERMAN);

	public XmlSearchValue[] searchXml(Integer siteId, String xpathQuery);

	public boolean saveXml(Integer siteId, Integer viewComponentId, String contentText, Map<String, String> metaAttributes);

	public void deleteXml(Integer siteId, Integer viewComponentId);

	public XmlSearchValue[] searchXmlByUnit(Integer unitId, Integer viewDocumentId, String xpathQuery);

}
