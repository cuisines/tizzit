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

import static de.juwimm.cms.common.Constants.*;

import java.util.Iterator;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.Messages;
import de.juwimm.cms.content.panel.PanIteration;
import de.juwimm.util.XercesHelper;

/**
 * This is the Iteration-wrapper module.<br>
 * It can't be shown inside a Textfield, but it can containing Textfields and all<br>
 * referring modules who can be used in a JPanel.<br>
 * The Iteration will be configured with an additional parameter inside the DCF<br>
 * named "iterationElements".
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class Iteration extends AbstractModule {
	private static Logger log = Logger.getLogger(Iteration.class);
	private int intMincount = 0;
	private int intMaxcount = 0;
	private TreeMap<String, Module> tmItElements = new TreeMap<String, Module>();
	private TreeMap<String, String> tmItElementsRootnodeName = new TreeMap<String, String>();
	private TreeMap<Integer, String> tmItElementsOrder = new TreeMap<Integer, String>();
	private TreeMap<String, Module> tmItElementsOriginal = new TreeMap<String, Module>();

	private ModuleFactory mf = new ModuleFactoryIterationImpl();
	private boolean finishedIterationElements = false;
	private boolean finishedSetProperties = false;

	private PanIteration panIt = new PanIteration();
	private String descriptionDcfname = "newsname";

	/**
	 * Allowed Methods are: <br>
	 * <i>mincount</i> Parameter: <i>value</i><br>
	 * <i>maxcount</i> Parameter: <i>value</i><br>
	 * The value could be "0", this means that there is no max or mincount set.
	 * @param Methodname The name of the Method
	 * @param parameters All containing Parameters. Here only "value" is allowed.
	 */
	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
		if (methodname.equalsIgnoreCase("mincount")) {
			this.intMincount = Integer.parseInt(parameters.getProperty("value"));
		} else if (methodname.equalsIgnoreCase("maxcount")) {
			this.intMaxcount = Integer.parseInt(parameters.getProperty("value"));
		} else if (methodname.equalsIgnoreCase("descriptionDcfname")) {
			this.descriptionDcfname = parameters.getProperty("value");
		}
	}

	public void setEnabled(boolean enabling) {
		try {
			Thread t = new Thread("Iteration::setEnabled") {
				public void run() {
					try {
						while (!finishedIterationElements || !finishedSetProperties) {
							Thread.sleep(20);
						}
					} catch (Exception exe) {
						log.error("Error during sleep periode of the setEnabled task", exe);
					}
				}
			};
			t.setPriority(Thread.NORM_PRIORITY);
			t.start();
			t.join();
		} catch (Exception exe) {
		}

		panIt.setEnabled(enabling);
		Iterator itt = this.tmItElementsOrder.keySet().iterator();
		while (itt.hasNext()) {
			Integer dcfnameId = (Integer) itt.next();
			String dcfname = this.tmItElementsOrder.get(dcfnameId);
			Module module = tmItElements.get(dcfname);
			try {
				module.setEnabled(enabling);
			} catch (Exception exe) {
				log.error("error setting the module enabled " + module, exe);
			}
		}
	}

	/**
	 * Here we go. If the Standard ModuleFactory has found a Module named Iteration,
	 * it will call this function. The Iteration-Module itselfs has to parse all needed
	 * informations about the other Modules through a ModuleFactory.
	 * This has to be done <b>before</b> the setProperties would be called.
	 * @param itEl The Node containing the IterationElements with dcfnames
	 */
	public void setIterationElements(Node itEl) {
		waitWhileCustomConfigurationIsntReady();
		Iterator results = XercesHelper.findNodes(itEl, "./*[@dcfname]");
		int i = 0;
		while (results.hasNext()) {
			i++;
			Element el = (Element) results.next();
			try {
				Module module = mf.getModuleInstance(el, null);
				tmItElements.put(el.getAttribute("dcfname"), module);
				tmItElementsOriginal.put(el.getAttribute("dcfname"), (Module) module.clone());
				tmItElementsRootnodeName.put(el.getAttribute("dcfname"), el.getNodeName());
				tmItElementsOrder.put(new Integer(i), el.getAttribute("dcfname"));
				panIt.addPanel(module);
			} catch (Exception exe) {
				log.error("Error in setIterationElements", exe);
			}
		}
		panIt.setProperties(tmItElementsOrder, tmItElements, tmItElementsRootnodeName, tmItElementsOriginal,
				descriptionDcfname, intMincount, intMaxcount);
		panIt.revalidate();
		finishedIterationElements = true;
	}

	public JPanel viewPanelUI() {
		return this.panIt;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void load() {
	}

	public Node getProperties() {
		return panIt.getProperties();
	}

	public void setProperties(Node node) {
		finishedSetProperties = false;
		if (node != null && node.hasChildNodes()) {
			panIt.reset();

			Iterator it = XercesHelper.findNodes(node, "./item");
			int itemNo = 0;
			while (it.hasNext()) {
				itemNo++;
				Element item = (Element) it.next();
				PanIteration.IterationItem mo = panIt.new IterationItem();
				// MO::Label
				try {
					Element descNode = (Element) XercesHelper.findNode(item, "./*[@dcfname='" + descriptionDcfname + "']");
					mo.setLabel(getURLDecoded(descNode.getAttribute("description")));
					if (mo.getLabel().equals("")) {
						//interim for migrating elements without description node
						String desc = XercesHelper.getNodeValue(descNode);
						mo.setLabel(desc);
						descNode.setAttribute("description", getURLEncoded(desc));
					}
				} catch (Exception exe) {
					mo.setLabel("Item " + itemNo);
					log.error("Can't find descriptionDcfname '" + descriptionDcfname + "' in Template");
				}
				// MO::Timestamp
				String timestamp = item.getAttribute("timestamp");
				if (timestamp.equals("")) timestamp = "" + System.currentTimeMillis();
				mo.setTimestamp(new Long(timestamp).longValue());
				// MO::Item
				mo.setItem(item);
				panIt.addItem(mo);
			}
		}
		selectFirst();
		//finishedSetProperties = true;
	}
	
	public void selectFirst() {
		Thread selectItem = new Thread(Thread.currentThread().getThreadGroup().getParent(), new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						panIt.selectItem(0, true);
					}
				});
				finishedSetProperties = true;
			}
		}, "jökelthread");
		selectItem.setPriority(Thread.MIN_PRIORITY);
		selectItem.start();
	}

	/**
	 * @todo: JOptionpane showing
	 * @return true or false
	 */
	public boolean isModuleValid() {
		setValidationError("");
		if (this.intMincount > 0 && panIt.getItemCount() < this.intMincount) {
			// too less elements
			String prepend = Messages.getString("exception.IterationTooLessElements",
					Integer.toString(panIt.getItemCount()), Integer.toString(this.intMincount));
			appendValidationError(prepend);
		}
		if (this.intMaxcount != 0 && this.intMaxcount < panIt.getItemCount()) {
			// should not occure, nomally - but this shows too much elements
			//appendValidationError(rb.getString(""));
			String prepend = Messages.getString("exception.IterationTooMuchElements",
					Integer.toString(panIt.getItemCount()), Integer.toString(this.intMaxcount));
			appendValidationError(prepend);
		}

		// now validating of ALL Modules contained in this Iteration
		String errorMsg = panIt.isValidA();

		if (!errorMsg.equals("")) {
			appendValidationError(rb.getString("exception.IterationValidationFailed"));
			appendValidationError(errorMsg);
		}

		if (getValidationError().equals("")) {
			return true;
		}
		return false;
	}

	public JDialog viewModalUI(boolean modal) {
		// This can't be implemented by this Module because of it's wrapper-functionality
		return null;
	}

	public String getIconImage() {
		return "";
	}

	public String getPaneImage() {
		return "";
	}
	
	public void recycle() {
		this.panIt.reset();
	}
}