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
package de.juwimm.cms.content;

import static de.juwimm.cms.client.beans.Application.getBean;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.content.frame.FrmValidationError;
import de.juwimm.cms.content.modules.Iteration;
import de.juwimm.cms.content.modules.Module;
import de.juwimm.cms.content.modules.ModuleFactory;
import de.juwimm.cms.content.modules.ModuleFactoryStandardImpl;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.ContentErrorListener;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ContentValue;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class ContentManager {
	private static Logger log = Logger.getLogger(ContentManager.class);
	/** Contains every whole Panel by key TemplateName, e.g. "standard" */
	private Hashtable<String, JPanel> htTemplatePanel = new Hashtable<String, JPanel>();
	/** Contains every DCF-Document by key TemplateName, e.g. "standard" */
	private Hashtable<String, Document> htTemplateDcfDocument = null;
	/** Contains every ModuleFactory by key TemplateName, e.g. "standard" */
	private Hashtable<String, ModuleFactory> htTemplateModuleFactory = null;
	/** Contains a HashTable with key DCFName Value DCFElement for every TemplateName */
	private Hashtable<String, Hashtable> htTemplateModuleNodes = null;

	private String strLastDcfName = null;

	private static Document domDoc = null; //initial value needed
	private ViewComponentValue viewComponentValue;
	private int rootUnitId;
	private int actUnitId;
	private int actVCID;
	private Communication comm = null;
	private TransformerFactory transformerFactory = TransformerFactory.newInstance();

	public ContentManager() {
		htTemplatePanel = new Hashtable<String, JPanel>();
		htTemplateDcfDocument = new Hashtable<String, Document>();
		htTemplateModuleFactory = new Hashtable<String, ModuleFactory>();
		htTemplateModuleNodes = new Hashtable<String, Hashtable>();
		strLastDcfName = "";
		domDoc = XercesHelper.getNewDocument();
	}

	public void setCommunication(Communication comm) {
		this.comm = comm;
	}

	/**	 
	 * @param contentValue
	 * @param scrollPane
	 * @param viewComponentValue
	 */
	public void createDCFInstance(ContentValue contentValue, JScrollPane scrollPane, ViewComponentValue viewComponentValue) {
		rootUnitId = 0;
		actUnitId = 0;
		this.viewComponentValue = viewComponentValue;
		String strDcfName = contentValue.getTemplate();
		String strXmlContent = contentValue.getContentText();
		strLastDcfName = strDcfName;
		// precache the content and put it in a Hashtable - so it isn't needed
		// to walk again and again with XPath through the full content and search for
		// dcfname attributes
		Hashtable<String, Element> htModuleDcfNameDcfElement = new Hashtable<String, Element>();
		try {
			if (strXmlContent != null && !strXmlContent.equals("")) {
				Document contentdoc = null;
				try {
					contentdoc = XercesHelper.string2Dom(strXmlContent);
				} catch (org.xml.sax.SAXParseException sax) {
					log.error("Creating DOM document failed: " + strXmlContent, sax);
				}
				if (contentdoc != null) {
					Iterator results = XercesHelper.findNodes(contentdoc, "//*[@dcfname]");
					while (results.hasNext()) {
						Element el = (Element) results.next();
						String moduleDcfName = el.getAttribute("dcfname");
						if (moduleDcfName != null && !moduleDcfName.equals("") && !moduleDcfName.startsWith("de.")) {
							if (log.isDebugEnabled()) log.debug("In Content: moduleDcfName " + moduleDcfName);
							if (!htModuleDcfNameDcfElement.containsKey(moduleDcfName)) htModuleDcfNameDcfElement.put(moduleDcfName, el);
						}
					}
				}
			}
		} catch (Exception exe) {
			log.error("Error creating DCFInstance", exe);
		}

		JPanel myDCFPanel = null;

		if (htTemplatePanel.containsKey(strDcfName)) {
			myDCFPanel = htTemplatePanel.get(strDcfName);
			scrollPane.setDoubleBuffered(true);
			scrollPane.getViewport().add(myDCFPanel);
			myDCFPanel.repaint();
			scrollPane.repaint();
			UIConstants.repaintApp();
			ModuleFactory moduleFactory = htTemplateModuleFactory.get(strDcfName);
			moduleFactory.reconfigureModules(htModuleDcfNameDcfElement);
			domDoc = htTemplateDcfDocument.get(strDcfName);
			myDCFPanel.repaint();
		} else {
			try {
				String dcf = ((Communication) getBean(Beans.COMMUNICATION)).getDCF(strDcfName);

				myDCFPanel = new JPanel();
				myDCFPanel.setDoubleBuffered(true);
				myDCFPanel.setLayout(new GridBagLayout());
				myDCFPanel.setBackground(UIConstants.backgroundBaseColor);

				scrollPane.setDoubleBuffered(true);
				scrollPane.getViewport().add(myDCFPanel);
				scrollPane.repaint();
				UIConstants.repaintApp();
				ModuleFactory moduleFactory = new ModuleFactoryStandardImpl();

				Document doc = XercesHelper.string2Dom(dcf);
				domDoc = doc;
				Hashtable<String, Element> moduleNodes = new Hashtable<String, Element>();

				Iterator results = XercesHelper.findNodes(doc, "//*[name()='dcfConfig']");
				int i = 0;
				while (results.hasNext()) {
					Element el = (Element) ((Element) results.next()).getParentNode();
					if (!el.getParentNode().getNodeName().equals("iterationElements")) {
						String moduleDcfName = el.getAttribute("dcfname");
						if (moduleDcfName != null && !moduleDcfName.equals("")) {
							if (log.isDebugEnabled()) log.debug("In DCF: moduleDcfName " + moduleDcfName);
							Module module = moduleFactory.getModuleInstance(el, htModuleDcfNameDcfElement.get(moduleDcfName));
							GridBagConstraints gbc = new GridBagConstraints();
							gbc.gridx = 0;
							gbc.gridy = i++;
							gbc.insets = new Insets(0, 0, 4, 0);
							gbc.fill = java.awt.GridBagConstraints.BOTH;
							gbc.weightx = 1.0D;
							myDCFPanel.add(moduleFactory.getPanelForModule(module), gbc);
							moduleNodes.put(moduleDcfName, el);
						}
					}
				}

				Iterator it = moduleFactory.getAllModules();
				while (it.hasNext()) {
					Module mod = (Module) it.next();
					if (mod instanceof Iteration) {
						((Iteration) mod).selectFirst();
					}
				}

				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;
				gbc.gridy = i++;
				gbc.insets = new Insets(0, 0, 0, 0);
				gbc.fill = java.awt.GridBagConstraints.BOTH;
				gbc.weightx = 1.0D;
				gbc.weighty = 1.0D;
				myDCFPanel.add(Box.createGlue(), gbc);

				htTemplateModuleNodes.put(strDcfName, moduleNodes);
				htTemplateModuleFactory.put(strDcfName, moduleFactory);
				htTemplatePanel.put(strDcfName, myDCFPanel);
				htTemplateDcfDocument.put(strDcfName, doc);
			} catch (Exception exe) {
				log.error("Error creating DCF instance", exe);
			}
		}
		scrollPane.repaint();
	}

	/**
	 * Returns {@code true} if the complete DCF is valid.<br>
	 * If there is an invalid Module found, it will show an errormessage to the user.
	 *
	 * @return a boolean indicating if the DCF is valid
	 */
	public boolean isModuleValid() {
		ModuleFactory mf = htTemplateModuleFactory.get(strLastDcfName);
		if (mf != null) {
			String errMsg = mf.isModuleValid();
			if (errMsg.equals("")) {
				return true;
			}
			new FrmValidationError(errMsg);
			return false;
		}
		return true;
	}

	public String getContent(String title) throws Exception {
		ModuleFactory mf = htTemplateModuleFactory.get(strLastDcfName);
		GetContentHandler gch = new GetContentHandler(mf, title);
		Document docdcf = htTemplateDcfDocument.get(strLastDcfName); //.cloneNode(true);
		SAXResult result = new SAXResult(gch);
		Source source = new DOMSource(docdcf);
		Transformer xform = transformerFactory.newTransformer();
		ContentErrorListener cel = new ContentErrorListener();
		xform.setErrorListener(cel);
		xform.transform(source, result);
		if (cel.getExceptionGot() != null) {
			throw cel.getExceptionGot();
		}
		return gch.getContent();
	}

	public int getRootUnitId() {
		try {
			int rootViewComponentId = comm.getViewDocument().getViewId().intValue();
			rootUnitId = comm.getUnit4ViewComponent(rootViewComponentId);
		} catch (Exception exe) {
			log.error("Error returning rootUnitId", exe);
		}
		return rootUnitId;
	}

	public int getActUnitId() {
		if (actUnitId == 0) {
			try {
				actUnitId = comm.getUnit4ViewComponent(this.viewComponentValue.getViewComponentId());
			} catch (Exception exe) {
				log.error("Error returning actUnitId", exe);
			}
		}
		return actUnitId;
	}

	public int getActViewComponentThatIsAUnit() {
		if (actVCID == 0) {
			try {
				int unitid = getActUnitId();
				actVCID = comm.getViewComponent4Unit(unitid).getViewComponentId();
			} catch (Exception exe) {
				log.error("Error returning actVC with unit", exe);
			}
		}
		return actVCID;
	}

	/**
	 * Enables or disables the modules inside the panels
	 * 
	 * @param enabling
	 */
	public void setEnabled(boolean enabling) {
		ModuleFactory mf = htTemplateModuleFactory.get(strLastDcfName);
		mf.setEnabled(enabling);
	}

	public void recycleActiveDcf() {
		if (strLastDcfName != null && !"".equalsIgnoreCase(strLastDcfName)) {
			ModuleFactory mf = htTemplateModuleFactory.get(strLastDcfName);
			Iterator<Module> it = mf.getAllModules();
			while (it.hasNext()) {
				Module mod = it.next();
				mod.recycle();
			}
		}
	}

	/**
	 * Returns the DOM {@link Document}.
	 * 
	 * @return the document
	 */
	public static Document getDomDoc() {
		return domDoc;
	}
	
	public void clearCurrentContentInfo(){
		htTemplatePanel = new Hashtable<String, JPanel>();
		htTemplateDcfDocument = new Hashtable<String, Document>();
		htTemplateModuleFactory = new Hashtable<String, ModuleFactory>();
		htTemplateModuleNodes = new Hashtable<String, Hashtable>();
		strLastDcfName = "";
		domDoc = XercesHelper.getNewDocument();
	}

}