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
package de.juwimm.cms.content.modules;

import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.event.EditpaneFiredEvent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class Line extends AbstractModule {
	private String iconImage = "16_linie_einfuegen.gif";
	private String paneImage = "16_linie_einfuegen.gif";

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
		if (methodname.equals("image")) {
			if (parameters.containsKey("iconImage")) iconImage = (String) parameters.get("iconImage");
			if (parameters.containsKey("paneImage")) paneImage = (String) parameters.get("paneImage");
		}
	}

	/**
	 * Line if only for use inside the WYSIWYG.
	 * Therefor it will return true
	 * @return true
	 */
	public boolean isModuleValid() {
		return true;
	}

	public JPanel viewPanelUI() {
		/**@todo Implement this de.juwimm.cms.content.modules.Module abstract method*/
		throw new UnsupportedOperationException("Method viewPanelUI() not yet implemented.");
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void load() {
	}

	public Node getProperties() {
		Element elm = ContentManager.getDomDoc().createElement("line");
		return elm;
	}

	public void setProperties(Node node) {
	}

	public JDialog viewModalUI(boolean modal) {
		EditpaneFiredEvent efe = new EditpaneFiredEvent(this);
		runEditpaneFiredEvent(efe);
		setSaveable(true);
		return null;
	}

	public String getIconImage() {
		return iconImage;
	}

	public String getPaneImage() {
		return paneImage;
	}

	public void setEnabled(boolean enabling) {
		throw new UnsupportedOperationException("Method setEnabled() not yet implemented.");
	}
	
	public void recycle() {
	
	}
}