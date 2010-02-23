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

import org.tizzit.util.XercesHelper;
import org.w3c.dom.Node;

import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanTeaser;

/**
 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 *
 */
public class Teaser extends AbstractModule {

	public static final String SCOPE_THIS = "this";
	public static final String SCOPE_PARENT = "parent";
	public static final String SCOPE_ROOT = "root";
	public static final String SCOPE_SITE = "site";

	private PanTeaser pan = new PanTeaser();

	/** 
	 * The defaults are:
	 * <ul>
	 * <li><b>name</b> = <code>TeaserProp</code>
	 * <li><b>unit</b> = <code>this</code>
	 * <li><b>randomizable</b> = <code>true</code>
	 * </ul>
	 * 
	 * @see de.juwimm.cms.content.modules.AbstractModule#setCustomProperties(String, Properties) */
	public void setCustomProperties(String propmodule, Properties props) {
		super.setCustomProperties(propmodule, props);
		if (propmodule.equalsIgnoreCase("SearchScope")) {
			String searchScopeNameValue = props.getProperty("name", "TeaserProp");
			String xpathTeaserElementValue = props.getProperty("xpathTeaserElement");
			String xpathTeaserNameValue = props.getProperty("xpathTeaserName");
			String xpathTeaserIdentifier = props.getProperty("xpathTeaserIdentifier");
			String unitValue = props.getProperty("unit", Teaser.SCOPE_THIS);
			String randomizeableValue = props.getProperty("randomizeable", "true");

			TeaserProperties teaserProps = new TeaserProperties(xpathTeaserElementValue, xpathTeaserNameValue, xpathTeaserIdentifier, unitValue, randomizeableValue);
			this.pan.addSearchScope(searchScopeNameValue, teaserProps);
		}
	}

	/** @see de.juwimm.cms.content.modules.Module#getIconImage() */
	public String getIconImage() {
		// is not used anyway...
		return "16_komp_anchor.gif";
	}

	/** @see de.juwimm.cms.content.modules.Module#getPaneImage() */
	public String getPaneImage() {
		// is not used anyway...
		return "16_komp_anchor.gif";
	}

	/** @see de.juwimm.cms.content.modules.Module#getProperties() */
	public Node getProperties() {
		Node node = this.pan.getProperties();
		setDescription(XercesHelper.getNodeValue(node));
		return node;
	}

	/** @see de.juwimm.cms.content.modules.Module#isModuleValid() */
	public boolean isModuleValid() {
		String errorMessage = this.pan.validateSelectedTeasers(isMandatory());
		setValidationError(errorMessage);
		if (errorMessage != null) {
			return false;
		}
		return true;
	}

	/** @see de.juwimm.cms.content.modules.Module#load() */
	public void load() {
	}

	/** @see de.juwimm.cms.content.modules.Module#recycle() */
	public void recycle() {
		this.pan.setProperties(null);
	}

	/** @see de.juwimm.cms.content.modules.Module#setEnabled(boolean) */
	public void setEnabled(boolean enabling) {
		this.pan.setEnabled(enabling);
	}

	/** @see de.juwimm.cms.content.modules.Module#setProperties(org.w3c.dom.Node) */
	public void setProperties(Node node) {
		setDescription(XercesHelper.getNodeValue(node));
		this.pan.setProperties(node);
	}

	/** @see de.juwimm.cms.content.modules.Module#viewModalUI(boolean) */
	public JDialog viewModalUI(boolean modal) {
		DlgModalModule frm = new DlgModalModule(this, pan, 190, 380, modal);
		frm.setVisible(true);
		return frm;
	}

	/** @see de.juwimm.cms.content.modules.Module#viewPanelUI() */
	public JPanel viewPanelUI() {
		return this.pan;
	}

	/**
	 * This type represents one block of teaser information taken from the mandator's dcf.<br>
	 * For example:<br>
	 * <code>
	 * &#60;property name="SearchScope"&#62;<br>
	 * &nbsp;&nbsp;&nbsp;&#60;name&#62;GlobaleTeaser&#60;/name&#62;<br>
	 * &nbsp;&nbsp;&nbsp;&#60;xpathTeaserElement&#62;//teaser&#60;/xpathTeaserElement&#62;<br>
	 * &nbsp;&nbsp;&nbsp;&#60;xpathTeaserName&#62;./name/content&#60;/xpathTeaserName&#62;<br>
	 * &nbsp;&nbsp;&nbsp;&#60;xpathTeaserIdentifier&#62;&#64;id&#60;/xpathTeaserIdentifier&#62;<br>
	 * &nbsp;&nbsp;&nbsp;&#60;unit&#62;root&#60;/unit&#62;<br>
	 * &nbsp;&nbsp;&nbsp;&#60;randomizable&#62;true&#60;/randomizable&#62;<br>
	 * &#60;/property&#62;
	 * </code>
	 * 
	 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
	 */
	public class TeaserProperties {

		private String xpathTeaserElement;
		private String xpathTeaserName;
		private String xpathTeaserIdentifier;
		private String unit;
		private Boolean randomizable;

		public TeaserProperties(String xpathTeaserElement, String xpathTeaserName, String xpathTeaserIdentifier, String unit, String randomizable) {
			this.xpathTeaserElement = xpathTeaserElement;
			this.xpathTeaserName = xpathTeaserName;
			this.xpathTeaserIdentifier = xpathTeaserIdentifier;
			this.unit = unit;
			this.randomizable = new Boolean(randomizable);
		}

		/**
		 * @return the randomizable
		 */
		public Boolean getRandomizable() {
			return randomizable;
		}

		/**
		 * @return the unit
		 */
		public String getUnit() {
			return unit;
		}

		/**
		 * @return the xpathTeaserElement
		 */
		public String getXpathTeaserElement() {
			return xpathTeaserElement;
		}

		/**
		 * @return the xpathTeaserIdentifier
		 */
		public String getXpathTeaserIdentifier() {
			return xpathTeaserIdentifier;
		}

		/**
		 * @return the xpathTeaserName
		 */
		public String getXpathTeaserName() {
			return xpathTeaserName;
		}

	}

}
