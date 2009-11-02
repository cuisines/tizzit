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
/**
 *
 */
package de.juwimm.cms.cocoon.support;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;

import de.juwimm.cms.beans.WebServiceSpring;

/**
 * This type allows easy creation of DOM documents providing the document's URL or viewComponentId.
 * In addition, it may be used for extracting the parameters from an iteration.
 *
 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 * @version $Id$
 */
public class FlowscriptUtils extends org.tizzit.cocoon.generic.support.FlowscriptUtils {
	private static Logger log = Logger.getLogger(FlowscriptUtils.class);
	private WebServiceSpring webSpringBean = null;

	/**
	 * Needed for internal Spring configuration.
	 *
	 * @param webSpringBean the {@link WebServiceSpring} to set
	 */
	public void setWebSpringBean(WebServiceSpring webSpringBean) {
		this.webSpringBean = webSpringBean;
	}

	/**
	 * Returns a Conquest content as a DOM document.
	 *
	 * @param viewComponentId the content's viewComponentId
	 * @param liveserver return published version or just the latest version ?
	 * @return a DOM document
	 */
	public Document getDomDocument(Integer viewComponentId, boolean liveserver) {
		Document result = null;
		try {
			String content = this.webSpringBean.getContent(viewComponentId, liveserver);
			// TODO [CH] Umgang mit services festlegen
			// log.debug("vorher: SystemProperty javax.xml.parsers.DocumentBuilderFactory = " + System.getProperty("javax.xml.parsers.DocumentBuilderFactory"));
			// System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
			// log.debug("nachher: SystemProperty javax.xml.parsers.DocumentBuilderFactory = " + System.getProperty("javax.xml.parsers.DocumentBuilderFactory"));
			result = XercesHelper.string2Dom(content);
			return result;
		} catch (Exception exception) {
			log.error("Error creating DOM document for viewComponentId " + viewComponentId, exception);
		}
		return result;
	}

	/**
	 * Returns the extracted key-value pairs from the view component's iteration as a HashMap.
	 * The view component is supposed to contain the following structure:
	 * <code><br>
	 * &lt;item&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&lt;param-name&gt;parameterName&lt;/param-name&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&lt;param-value&gt;parameterValue&lt;/param-value&gt;<br>
	 * &lt;/item&gt;
	 *
	 * @param viewComponentId the ConQuest viewComponentId
	 * @return a {@link HashMap} containing the parameter names and its values or an
	 * empty HashMap if no key-value pairs were found.
	 */
	public HashMap<String, String> getParamHashmap(Integer viewComponentId) {
		HashMap<String, String> result = new HashMap<String, String>();
		try {
			String content = this.webSpringBean.getContent(viewComponentId, true);
			Document doc = XercesHelper.string2Dom(content);
			result = getParamHashmap(doc);
		} catch (Exception exception) {
			log.error("Error creating DOM document for viewComponentId " + viewComponentId, exception);
		}
		return result;
	}
}
