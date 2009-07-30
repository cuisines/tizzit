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
package de.juwimm.cms.util;

import org.apache.log4j.Logger;

/**
 * 
 * @author <a href="mailto:michael.meyer@juwimm.com">Michael Meyer</a><br/>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public final class ToXmlHelper {
	private static Logger log = Logger.getLogger(ToXmlHelper.class);

	public ToXmlHelper() {
	}

	/**
	 * 
	 * @param nodename
	 * @param nodevalue
	 * @return
	 */
	public String getXMLNode(String nodename, String nodevalue) {
		StringBuffer buf = new StringBuffer("<");
		buf.append(nodename);
		buf.append(">");
		buf.append(nodevalue);
		buf.append("</");
		buf.append(nodename);
		buf.append(">");

		if (log.isDebugEnabled()) {
			log.debug("NODENAME " + nodename);
			log.debug("NODEVALUE " + nodevalue);
			log.debug(buf.toString());
		}

		return buf.toString();
	}

}
