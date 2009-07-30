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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.juwimm.util.XercesHelper;

public class SmallSiteConfigReader {
	public static final String CONF_NODE_DEFAULT = "//config/default/";
	private Document confdoc = null;
	private String relativeNodePath = "//config";

	protected SmallSiteConfigReader() {
	}

	public SmallSiteConfigReader(String configfile) {
		try {
			setConfdoc(XercesHelper.string2Dom(configfile));
			this.setRelativeNodePath("//config/default/");
		} catch (Exception exe) {
		}
	}

	public String getConfigElementValue(String strNode) {
		return XercesHelper.getNodeValue(this.getConfdoc().getFirstChild(), this.getRelativeNodePath() + strNode);
	}

	public String getConfigAttribute(String strNode, String strAttrName) {
		String retVal = "";
		try {
			Element elm = (Element) XercesHelper.findNode(this.getConfdoc().getFirstChild(), this.getRelativeNodePath() + strNode);
			retVal = elm.getAttribute(strAttrName);
		} catch (Exception exe) {
		}
		return retVal;
	}

	/**
	 * @param confdoc The confdoc to set.
	 */
	protected void setConfdoc(Document confdoc) {
		this.confdoc = confdoc;
	}

	/**
	 * @return Returns the confdoc.
	 */
	protected Document getConfdoc() {
		return confdoc;
	}

	/**
	 * @param relativeNodePath The relativeNodePath to set.
	 */
	protected void setRelativeNodePath(String relativeNodePath) {
		this.relativeNodePath = relativeNodePath;
	}

	/**
	 * @return Returns the relativeNodePath.
	 */
	protected String getRelativeNodePath() {
		return relativeNodePath;
	}
}
