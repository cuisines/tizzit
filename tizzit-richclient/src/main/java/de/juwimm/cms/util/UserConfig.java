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

import static de.juwimm.cms.client.beans.Application.*;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Beans;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Revision: 3168 $
 */
public final class UserConfig extends ConfigReader {
	private static Logger log = Logger.getLogger(UserConfig.class);
	public static final String USERCONF_CURRENT_SKIN = "currentSkin";
	public static final String USERCONF_PREFERRED_LANGUAGE = "preferredLanguage";
	private static UserConfig instance = null;
	private static String lastUserName = "";
	private static Communication comm = ((Communication) getBean(Beans.COMMUNICATION));

	private UserConfig() {
		super(comm.getUser().getConfigXML(), ConfigReader.CONF_NODE_DEFAULT);

	}

	/**
	 * Returns an Instance of UserConfig to get and set the UserConfig.<br>
	 * This Method guarranties, that the config parameters are related to <br>
	 * the actual logged in user - even with login/logouts.
	 * @return The Instance of UserConfig
	 */
	public static UserConfig getInstance() {
		if (!lastUserName.equals(comm.getUser().getUserName()) || instance == null) {
			lastUserName = comm.getUser().getUserName();
			String confDoc = comm.getUser().getConfigXML();
			if (confDoc == null || confDoc.equalsIgnoreCase("")) {
				confDoc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><config><default></default></config>";
				comm.getUser().setConfigXML(confDoc);
			}
			instance = new UserConfig();
		}
		return instance;
	}

	public void setConfigNodeValue(String node, String value) {
		try {
			Node nde = XercesHelper.findNode(this.getConfdoc(), this.getRelativeNodePath() + node);
			if (nde == null) {
				nde = XercesHelper.findNode(this.getConfdoc(), this.getRelativeNodePath().substring(0, this.getRelativeNodePath()
						.length() - 1));
				Element elm = this.getConfdoc().createElement(node);
				CDATASection cdata = this.getConfdoc().createCDATASection(value);
				nde.appendChild(elm);
				elm.appendChild(cdata);
			} else {
				CDATASection cdata = this.getConfdoc().createCDATASection(value);
				if (nde.getFirstChild() == null) {
					nde.appendChild(cdata);
				} else {
					nde.replaceChild(cdata, nde.getFirstChild());
				}
			}
		} catch (Exception exe) {
			log.error("Error setting config node", exe);
		}
	}

	public void saveChanges() {
		String conf = XercesHelper.doc2String(this.getConfdoc());
		comm.getUser().setConfigXML(conf);
		comm.updateUserMe();
	}

}
