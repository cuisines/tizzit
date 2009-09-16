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
package de.juwimm.cms.content.modules;

import java.util.Iterator;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanFlash;
import de.juwimm.cms.content.panel.PanOnlyButton;
import de.juwimm.cms.content.panel.PanFlash.FlashProperties;

/**
 * 
 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class Flash extends AbstractModule {

	private static Logger log = Logger.getLogger(Flash.class);
	private PanFlash flashPanel = new PanFlash();
	private PanOnlyButton linkPanel = null;
	private boolean enabled = false;

	/** @see de.juwimm.cms.content.modules.Module#getIconImage() */
	public String getIconImage() {
		return "16_flash.png";
	}

	/** @see de.juwimm.cms.content.modules.Module#getPaneImage() */
	public String getPaneImage() {
		try {
			return getURLEncodedISO("svgflash." + getDescription());
		} catch (Exception exe) {
			log.error("Error getting pane image", exe);
			return "16_flash.png";
		}
	}

	/** @see de.juwimm.cms.content.modules.Module#getProperties() */
	public Node getProperties() {
		// TODO mal debuggen und klären, wer das root-Element ersetzt...
		Element rootElement = ContentManager.getDomDoc().createElement("root");
		if (this.flashPanel.getDocumentId() != null) {
			if (this.flashPanel.getDocumentId().intValue() > 0) {
				Element flashElement = ContentManager.getDomDoc().createElement("flash");
				flashElement.setAttribute("src", this.flashPanel.getDocumentId() + "");
				flashElement.setAttribute("flashName", this.flashPanel.getFlashfileName());
				flashElement.setAttribute("width", this.flashPanel.getFlashWidth());
				flashElement.setAttribute("height", this.flashPanel.getFlashHeight());
				flashElement.setAttribute("playerVersion", this.flashPanel.getPlayerVersion());
				flashElement.setAttribute("loop", String.valueOf(this.flashPanel.getLoop()));
				flashElement.setAttribute("autostart", String.valueOf(this.flashPanel.getAutostart()));
				flashElement.setAttribute("quality", this.flashPanel.getQuality());
				flashElement.setAttribute("scale", this.flashPanel.getScale());
				flashElement.setAttribute("transparency", this.flashPanel.getTransparency());
				
				if (this.flashPanel.getFlashVars() != null && this.flashPanel.getFlashVars().length() > 0) {
					Element flashVarsElement = ContentManager.getDomDoc().createElement("variables");
					String[] flashVarArray = this.flashPanel.getFlashVars().split(";");
					String[] nameValuePair = new String[2];
					for (int i = 0; i < flashVarArray.length; i++) {
						nameValuePair = flashVarArray[i].split("=");
						Element singleFlashVarElement = ContentManager.getDomDoc().createElement("variable");
						singleFlashVarElement.setAttribute("name", nameValuePair[0].trim());
						CDATASection txtVariableValue = ContentManager.getDomDoc().createCDATASection(nameValuePair[1].trim());
						singleFlashVarElement.appendChild(txtVariableValue);
						flashVarsElement.appendChild(singleFlashVarElement);
					}
					flashElement.appendChild(flashVarsElement);
				}
				setDescription(this.flashPanel.getFlashLinkDescription());
				CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.flashPanel.getFlashLinkDescription());
				flashElement.appendChild(txtNode);
				rootElement.appendChild(flashElement);
			}
		}
		return rootElement;
	}
	
	/** @see de.juwimm.cms.content.modules.Module#setProperties(org.w3c.dom.Node) */
	public void setProperties(Node node) {
		this.flashPanel.reinitialize();
		if (node != null) {
			try {
				Element flashElement = (Element) XercesHelper.findNode(node, "./flash");
				if (flashElement != null && flashElement.getAttribute("src") != null) {
					this.flashPanel.setDocumentId(new Integer(flashElement.getAttribute("src")));
					this.flashPanel.setFlashWidth(flashElement.getAttribute("width"));
					this.flashPanel.setFlashHeight(flashElement.getAttribute("height"));
					this.flashPanel.setPlayerVersion(flashElement.getAttribute("playerVersion"));
					this.flashPanel.setLoop(evaluateBoolean(flashElement.getAttribute("loop")));
					this.flashPanel.setAutostart(evaluateBoolean(flashElement.getAttribute("autostart")));
					this.flashPanel.setQuality(flashElement.getAttribute("quality"));
					this.flashPanel.setScale(flashElement.getAttribute("scale"));
					this.flashPanel.setTransparency(flashElement.getAttribute("transparency"));
					boolean variablesExist = false;
					Node childNode = flashElement.getFirstChild();
					if (childNode != null && childNode.getNodeName().equals("variables")) {
						variablesExist = true;
						StringBuilder variablesBuilder = new StringBuilder();
						Iterator variablesIterator = XercesHelper.findNodes(childNode, "variable");
						while (variablesIterator.hasNext()) {
							Element variableElement = (Element) variablesIterator.next();
							variablesBuilder.append(variableElement.getAttribute("name") + "=");
							variablesBuilder.append(variableElement.getFirstChild().getNodeValue());
							if (variablesIterator.hasNext()) {
								variablesBuilder.append(";");
							}
						}
						this.flashPanel.setFlashVars(variablesBuilder.toString());
					}
					if (variablesExist) {
						String test = flashElement.getLastChild().getNodeValue();
						this.flashPanel.setFlashLinkDescription(test);
					} else {
						this.flashPanel.setFlashLinkDescription(XercesHelper.getNodeValue(flashElement));	
					}
				}
			} catch (Exception exception) {
				log.error(exception);
			}
		}
		if (this.linkPanel == null) {
			this.linkPanel = new PanOnlyButton(this, true);
		}
//		this.linkPanel.updateLinks();
	}

	/** @see de.juwimm.cms.content.modules.Module#isModuleValid() */
	public boolean isModuleValid() {
		String errormessage = null;
		if (isMandatory()) {
			errormessage = this.flashPanel.validateFlashProps(); 
		}
		setValidationError(errormessage);
		return (errormessage == null);
	}

	/** @see de.juwimm.cms.content.modules.Module#load() */
	public void load() {
		// TODO Auto-generated method stub
	}

	/** @see de.juwimm.cms.content.modules.Module#recycle() */
	public void recycle() {
		setProperties(null);
	}

	/** @see de.juwimm.cms.content.modules.Module#setEnabled(boolean) */
	public void setEnabled(boolean enabling) {
		this.flashPanel.setEnabled(enabling);
		if (this.linkPanel != null) {
			this.linkPanel.setEnabled(enabling);
		}
		this.enabled = enabling;
	}

	/** @see de.juwimm.cms.content.modules.Module#viewModalUI(boolean) */
	public JDialog viewModalUI(boolean modal) {
		int frameHeight = 500;
		int frameWidth = 500;
		DlgModalModule modalModule = new DlgModalModule(this, this.flashPanel, frameHeight, frameWidth, modal);
		this.flashPanel.regionSelected(); // Kopiert aus Document Vorlage...
		modalModule.setVisible(true);
		return modalModule;
	}

	/** 
	 * Is called whenever the Flash component should be operable by a discrete component within the client
	 * and not by a button being part of the WYSIWYG editor.
	 * 
	 * @see de.juwimm.cms.content.modules.Module#viewPanelUI() */
	public JPanel viewPanelUI() {
		if (this.linkPanel == null) {
			this.linkPanel = new PanOnlyButton(this, true);
		}
		this.linkPanel.setEnabled(this.enabled);
		return this.linkPanel;
	}

	/**
	 * Reads the configuration from this mandator's dcf and configures the {@link PanFlash.FlashProperties} accordingly
	 *  
	 * @see de.juwimm.cms.content.modules.AbstractModule#setCustomProperties(java.lang.String, java.util.Properties) */
	public void setCustomProperties(String propmodule, Properties props) {
		super.setCustomProperties(propmodule, props);
		if (log.isDebugEnabled()) {
			log.debug("Start setting custom properties: " + propmodule);
		}
		FlashProperties properties = this.flashPanel.getFlashProperties();

		if (propmodule.equalsIgnoreCase("PlayerVersion")) {
			if (props.getProperty("versions") != null) {
				 // "\s" matches whitespace chars: \t, \n, \x0B, \f, \r
				String commaseparatedValues = props.getProperty("versions").replaceAll("\\s", "");
				properties.setPlayerVersions(commaseparatedValues.split(";"));
			}
		}
		if (propmodule.equalsIgnoreCase("Autostart")) {
			if (props.getProperty("choosable") != null) {
				properties.setAutostartChoosable(evaluateBoolean(props.getProperty("choosable")));
			}
			if (props.getProperty("default") != null) {
				properties.setAutostartDefault(evaluateBoolean(props.getProperty("default")));
			}
		}
		if (propmodule.equalsIgnoreCase("Loop")) {
			if (props.getProperty("choosable") != null) {
				properties.setLoopChoosable(evaluateBoolean(props.getProperty("choosable")));
			}
			if (props.getProperty("default") != null) {
				properties.setLoopDefault(evaluateBoolean(props.getProperty("default")));
			}
		}
		if (propmodule.equalsIgnoreCase("FlashVariables")) {
			String commaseparatedValues = props.getProperty("variables");
			if (commaseparatedValues != null) {
				// matches 0 - n whitespaces + comma + 0 - n whitespaces
				commaseparatedValues = commaseparatedValues.replaceAll("\\s*;\\s*", ";"); 
				properties.setFlashVariables(commaseparatedValues.split(";"));
			}
			if (props.getProperty("choosable") != null) {
				properties.setFlashVarsChoosable(evaluateBoolean(props.getProperty("choosable")));
			}
		}
		if (propmodule.equalsIgnoreCase("Quality")) {
			if (props.getProperty("choosable") != null) {
				properties.setQualityChoosable(evaluateBoolean(props.getProperty("choosable")));
			}
			if (props.getProperty("values") != null ) {
				String commaseparatedValues = props.getProperty("values").replaceAll("\\s", "");
				properties.setQualities(commaseparatedValues.split(";"));
			}
			if (props.getProperty("default") != null) {
				properties.setQualityDefault(props.getProperty("default"));
			}
		}
		if (propmodule.equalsIgnoreCase("Scale")) {
			if(props.getProperty("choosable") != null) {
				properties.setScaleChoosable(evaluateBoolean(props.getProperty("choosable")));
			}
			if (props.getProperty("values") != null) { 
				String commaseparatedValues = props.getProperty("values").replaceAll("\\s", "");
				properties.setScales(commaseparatedValues.split(";"));
			}
			if (props.getProperty("default") != null) {
				properties.setScaleDefault(props.getProperty("default"));
			}
		}
		if (propmodule.equalsIgnoreCase("Transparency")) {
			if (props.getProperty("choosable") != null) {
				properties.setTransparencyChoosable(evaluateBoolean(props.getProperty("choosable")));
			}
			if (props.getProperty("values") != null) {
				String commaseparatedValues = props.getProperty("values").replaceAll("\\s", "");
				properties.setTransparencies(commaseparatedValues.split(";"));
			}
			if (props.getProperty("default") != null) {
				properties.setTransparencyDefault(props.getProperty("default"));
			}
		}
		if (propmodule.equals("CustomConfigurationReady")) {
			this.flashPanel.reinitialize();
			if (log.isDebugEnabled()) {
				log.debug("Setting custom properties: DONE");
			}
		}
	}
	
	/**
	 * Returns {@code true} if the specified text reads "true" or "yes", {@code false} otherwise
	 * 
	 * @param text the {@link String} describing a boolean value
	 * @return the boolean value described by the string
	 */
	private boolean evaluateBoolean(String text) {
		if (text == null) {
			return false;
		}
		text = text.trim();
		if (text.length() == 0) {
			return false;
		}
		return (text.equalsIgnoreCase("true") || (text.equalsIgnoreCase("yes")));
	}

}
