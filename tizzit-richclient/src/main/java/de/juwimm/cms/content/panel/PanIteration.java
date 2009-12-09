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
package de.juwimm.cms.content.panel;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.Messages;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.modules.AbstractModule;
import de.juwimm.cms.content.modules.Module;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.swing.NoResizeScrollPane;

/**
 * <p>Title: Tizzit</p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanIteration extends JPanel {
	private static Logger log = Logger.getLogger(PanIteration.class);
	private int intMincount = 0;
	private int intMaxcount = 0;
	private int lastSelectedItem = -1;
	private String labelNodeDcfname = "";
	private boolean enabled = true;
	private boolean forceLoad = false;
	private boolean movingItem = false;
	private boolean enabledSelectThread = true;
	private Vector<Integer> unkissed = new Vector<Integer>();
	private final DefaultListModel lstModel = new DefaultListModel();
	private final BorderLayout borderLayout1 = new BorderLayout();
	private TreeMap<String, Module> tmItElements = new TreeMap<String, Module>();
	private TreeMap<String, String> tmItElementsRootnodeName = new TreeMap<String, String>();
	private TreeMap<Integer, String> tmItElementsOrder = new TreeMap<Integer, String>();
	private TreeMap<String, Module> tmItElementsOriginal = new TreeMap<String, Module>();
	private final GridBagLayout gblNorth = new GridBagLayout();
	private final GridBagLayout gblPan = new GridBagLayout();
	private final JButton btnUp = new JButton();
	private final JButton btnDeleteMe = new JButton();
	private final JPanel panNorth = new JPanel();
	private final JButton btnDown = new JButton();
	private final JButton btnNew = new JButton();
	private final JList lstIteration = new JList();
	private final JPanel pan = new JPanel();
	private final JSplitPane jSplitPane1 = new JSplitPane();
	private final NoResizeScrollPane jScrollPane1 = new NoResizeScrollPane();

	public PanIteration() {
		try {
			jbInit();
			btnDeleteMe.setIcon(UIConstants.MODULE_ITERATION_CONTENT_DELETE);
			btnNew.setIcon(UIConstants.MODULE_ITERATION_CONTENT_ADD);
			btnUp.setIcon(UIConstants.MODULE_ITERATION_CONTENT_UP);
			btnDown.setIcon(UIConstants.MODULE_ITERATION_CONTENT_DOWN);
			//this.jScrollPane1.setPreferredSize(new Dimension(jScrollPane1.getWidth(), 100));
			this.jScrollPane1.ignoreHeight(false);
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	private void jbInit() throws Exception {
		this.setLayout(borderLayout1);

		btnUp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnUpActionPerformed(e);
			}
		});
		btnUp.setMaximumSize(new Dimension(25, 25));
		btnUp.setMinimumSize(new Dimension(25, 25));
		btnUp.setPreferredSize(new Dimension(25, 25));
		btnDeleteMe.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteMeActionPerformed(e);
			}
		});
		btnDeleteMe.setMaximumSize(new Dimension(25, 25));
		btnDeleteMe.setMinimumSize(new Dimension(25, 25));
		btnDeleteMe.setPreferredSize(new Dimension(25, 25));
		btnDeleteMe.setMnemonic('0');
		panNorth.setLayout(gblNorth);
		btnDown.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDownActionPerformed(e);
			}
		});
		btnDown.setMaximumSize(new Dimension(25, 25));
		btnDown.setMinimumSize(new Dimension(25, 25));
		btnDown.setPreferredSize(new Dimension(25, 25));
		btnNew.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewActionPerformed(e);
			}
		});
		btnNew.setMaximumSize(new Dimension(25, 25));
		btnNew.setMinimumSize(new Dimension(25, 25));
		btnNew.setPreferredSize(new Dimension(25, 25));

		lstIteration.setModel(lstModel);
		lstIteration.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstIteration.getSelectionModel().addListSelectionListener(new MyListSelectionListener());

		pan.setDoubleBuffered(true);
		pan.setLayout(gblPan);
		jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jSplitPane1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				splitPanePropertyChange(e);
			}
		});
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		borderLayout1.setHgap(5);
		this.add(panNorth, BorderLayout.NORTH);
		panNorth.add(btnDeleteMe, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 5, 2, 0), 0, 0));
		panNorth.add(btnUp, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 0), 0, 0));
		panNorth.add(btnDown, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 0), 0, 0));
		panNorth.add(btnNew, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 0), 0, 0));
		this.add(jSplitPane1, BorderLayout.CENTER);
		jSplitPane1.add(jScrollPane1, JSplitPane.TOP);
		jSplitPane1.add(pan, JSplitPane.BOTTOM);
		jScrollPane1.getViewport().add(lstIteration, null);
	}

	void btnUpActionPerformed(ActionEvent e) {
		int selId = this.lstIteration.getSelectedIndex();
		saveItem(selId);
		Object upper = lstModel.getElementAt(selId - 1);
		lstModel.setElementAt(lstModel.getElementAt(selId), selId - 1);
		lstModel.setElementAt(upper, selId);

		movingItem = true;
		selectItem(selId - 1, true);
		repaintButtons();
	}

	void btnDownActionPerformed(ActionEvent e) {
		int selId = this.lstIteration.getSelectedIndex();
		saveItem(selId);
		Object lower = lstModel.getElementAt(selId + 1);
		lstModel.setElementAt(lstModel.getElementAt(selId), selId + 1);
		lstModel.setElementAt(lower, selId);

		movingItem = true;
		selectItem(selId + 1, true);
		repaintButtons();
	}

	void btnNewActionPerformed(ActionEvent e) {
		IterationItem mo = new IterationItem();
		Element item = ContentManager.getDomDoc().createElement("item");

		Iterator it = tmItElementsOrder.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			String dcfname = tmItElementsOrder.get(key);
			Module module = tmItElementsOriginal.get(dcfname);
			Module moduleVisible = tmItElements.get(dcfname);

			Element conf = (Element) module.getProperties();
			if (item.getOwnerDocument() != conf.getOwnerDocument()) {
				conf = (Element) item.getOwnerDocument().importNode(conf, true);
			}
			conf = (Element) XercesHelper.renameNode(conf, this.tmItElementsRootnodeName.get(dcfname));
			conf.setAttribute("dcfname", dcfname);
			conf.setAttribute("description", module.getDescription());
			item.appendChild(conf);

			moduleVisible.setProperties(conf);
			moduleVisible.setDescription(module.getDescription());
		}
		mo.setItem(item);
		mo.setLabel("UNBENANNT");
		mo.setTimestamp(System.currentTimeMillis());
		int insertIdx = 0;
		int selIdx = lstIteration.getSelectedIndex() + 1;
		int max = lstModel.getSize();
		if (selIdx >= 0 && selIdx < max) {
			insertIdx = selIdx;
		} else {
			insertIdx = max;
		}
		lstModel.add(insertIdx, mo);
		insertIdx = lstModel.indexOf(mo);
		lastSelectedItem = selIdx;
		selectItem(insertIdx, true);
		repaintButtons();
	}

	void btnDeleteMeActionPerformed(ActionEvent e) {
		int selId = this.lstIteration.getSelectedIndex();
		lastSelectedItem = -1;
		if (selId > -1) {
			lstModel.remove(selId);
			if (lstModel.getSize() > selId) {
				selectItem(selId, true);
			} else {
				if (lstModel.getSize() > 0) {
					selectItem(lstModel.getSize() - 1, true);
				}
			}
			repaintButtons();
		}
	}

	/**
	 * Here is still a Bug regarding the synchronization. Therefor I've disabled the Thread (run() instead of start())
	 */
	private class MyListSelectionListener implements ListSelectionListener {
		public synchronized void valueChanged(ListSelectionEvent e) {
			if (lstIteration.getSelectedIndex() > -1 && !e.getValueIsAdjusting()) {
				if (enabledSelectThread) {
					enabledSelectThread = false;
					String valid = isValidA();
					if (!valid.equals("") && !forceLoad) {
						selectItem(lastSelectedItem, true);
						ActionHub.showMessageDialog(valid, JOptionPane.ERROR_MESSAGE);
					} else {
						setCursor(new Cursor(Cursor.WAIT_CURSOR));
						repaintButtons();
						loadSelectedItem();
						lastSelectedItem = lstIteration.getSelectedIndex();
						setCursor(Cursor.getDefaultCursor());
					}
					forceLoad = false;
					enabledSelectThread = true;
				} else {
					loadSelectedItem();
					lastSelectedItem = lstIteration.getSelectedIndex();
				}
			}
		}
	}

	private void repaintButtons() {
		if (enabled) {
			this.btnUp.setEnabled(true);
			this.btnDown.setEnabled(true);
			this.btnDeleteMe.setEnabled(true);
			this.btnNew.setEnabled(true);

			// First Element
			if (lstIteration.getSelectedIndex() < 0 || lstIteration.getSelectedValue().equals(lstModel.firstElement())) {
				this.btnUp.setEnabled(false);
			} else {
				this.btnUp.setEnabled(true);
			}
			// Last Element
			if (lstIteration.getSelectedIndex() < 0 || lstIteration.getSelectedValue().equals(lstModel.lastElement())) {
				this.btnDown.setEnabled(false);
			} else {
				this.btnDown.setEnabled(true);
			}
			// No entries
			if (lstModel.getSize() <= 1) {
				this.btnDeleteMe.setEnabled(false);
			} else {
				this.btnDeleteMe.setEnabled(true);
			}
			// Nothing selected
			if (lstIteration.getSelectedIndex() < 0) {
				this.btnDeleteMe.setEnabled(false);
			}
			// mincount has been set
			if (intMincount > 0) {
				if (lstModel.getSize() <= intMincount) {
					this.btnDeleteMe.setEnabled(false);
				}
			}
			// maxcount has been set
			if (intMaxcount > 0) {
				if (lstModel.getSize() >= intMaxcount) {
					this.btnNew.setEnabled(false);
				}
			}
		}
	}

	private void saveItem(int id) {
		if (log.isDebugEnabled()) log.debug("saveItem " + id);
		IterationItem moPrev = (IterationItem) lstModel.get(id);
		Element item = ContentManager.getDomDoc().createElement("item");

		Iterator it = tmItElementsOrder.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			String dcfname = tmItElementsOrder.get(key);
			Module module = tmItElements.get(dcfname);

			Element conf = (Element) module.getProperties();
			if (item.getOwnerDocument() != conf.getOwnerDocument()) {
				conf = (Element) item.getOwnerDocument().importNode(conf, true);
			}
			conf = (Element) XercesHelper.renameNode(conf, this.tmItElementsRootnodeName.get(dcfname));
			conf.setAttribute("dcfname", dcfname);
			conf.setAttribute("description", AbstractModule.getURLEncoded(module.getDescription()));
			//conf.setAttribute("label", module.getLabel());
			item.appendChild(conf);

			if (dcfname.equals(labelNodeDcfname)) {
				moPrev.setLabel(module.getDescription());
			}
		}
		moPrev.setItem(item);
		lstModel.set(id, moPrev);
	}

	private synchronized void loadSelectedItem() {
		if ((enabled && !movingItem) && lastSelectedItem >= 0) { //&& lstIteration.getSelectedIndex()!=lastSelectedItem
			saveItem(lastSelectedItem);
		}

		IterationItem mo = (IterationItem) lstIteration.getSelectedValue();
		if (mo != null) {
			Node item = mo.getItem();

			if (tmItElementsOrder != null) {
				Iterator it = tmItElementsOrder.keySet().iterator();
				while (it.hasNext()) {
					Object key = it.next();
					String dcfname = tmItElementsOrder.get(key);
					Module module = tmItElements.get(dcfname);
					try {
						Node conf = XercesHelper.findNode(item, "./*[@dcfname='" + dcfname + "']");
						module.setProperties(conf);
					} catch (Exception exe) {
						log.error("Could not set Properties for Module " + module.getName(), exe);
					}
				}
				unkissed.removeElement(new Integer(lstIteration.getSelectedIndex()));
			}
			movingItem = false;
		} else {
			log.warn("loadSelectedItem reported a ModelObject null-object");
		}
	}

	/**
	 * This is for initial calls of this Panel.<br>
	 * All Elements will be resetted.
	 * @param tmItElementsOrder
	 * @param tmItElements
	 * @param labelNodeDcfname
	 */
	public void setProperties(TreeMap tmItElementsOrder, TreeMap<String, Module> tmItElements, TreeMap<String, String> tmItElementsRootnodeName, TreeMap<String, Module> tmItElementsOriginal, String labelNodeDcfname, int intMincount, int intMaxcount) {
		this.tmItElementsOrder = tmItElementsOrder;
		this.tmItElements = tmItElements;
		this.labelNodeDcfname = labelNodeDcfname;
		this.tmItElementsRootnodeName = tmItElementsRootnodeName;
		this.tmItElementsOriginal = tmItElementsOriginal;
		this.intMincount = intMincount;
		this.intMaxcount = intMaxcount;
	}

	public void reset() {
		if (lstIteration.getSelectedValue() != null) {
			// one item is selected
			if (tmItElementsOrder != null) {
				Iterator it = tmItElementsOrder.keySet().iterator();
				while (it.hasNext()) {
					Object key = it.next();
					String dcfname = tmItElementsOrder.get(key);
					Module module = tmItElements.get(dcfname);
					module.recycle();
				}
			}
		}
		lastSelectedItem = -1;
		this.lstModel.clear();
		unkissed = new Vector<Integer>();
	}

	/**
	 * Returns the Properties of the Iteration Panel
	 * @todo: MYSQL PATCH BECAUSE LACK OF UTF-8 SUPPORT
	 * @return
	 */
	public Node getProperties() {
		int selectedId = lstIteration.getSelectedIndex();
		Node root = ContentManager.getDomDoc().createElement("root");

		for (int i = 0; i < lstModel.getSize(); i++) {
			IterationItem mo = (IterationItem) lstModel.get(i);

			if (selectedId == i) {
				// first save the actual visible one
				saveItem(i);
			}

			Element item = (Element) mo.getItem();

			if (root.getOwnerDocument() != item.getOwnerDocument()) {
				item = (Element) root.getOwnerDocument().importNode(item, true);
			}
			item.setAttribute("timestamp", "" + mo.getTimestamp());
			item.setAttribute("id", "" + (i + 1));
			item.setAttribute("description", AbstractModule.getURLEncoded(mo.getLabel()));
			root.appendChild(item);
		}
		if (log.isDebugEnabled()) log.debug(XercesHelper.node2string(root));
		return root;
	}

	private int panCount = 0;

	public void addPanel(Module module) {
		ContentBorderIterationAtomPanel cbi = new ContentBorderIterationAtomPanel();
		cbi.setLabel(module.getLabel());
		cbi.setContentModulePanel(module.viewPanelUI());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = panCount++;
		gbc.insets = new java.awt.Insets(0, 0, 0, 0);
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.weightx = 1.0D;
		pan.add(cbi, gbc);
	}

	public void addItem(IterationItem mo) {
		this.lstModel.addElement(mo);
		unkissed.add(new Integer(lstModel.getSize() - 1));
	}

	public void selectItem(int id, boolean ignoreErrors) {
		if (lstModel.getSize() > id) {
			forceLoad = ignoreErrors;
			lstIteration.setSelectedIndex(id);
		}
	}

	public int getItemCount() {
		return lstModel.getSize();
	}

	public synchronized String isValidA() {
		StringBuffer errorPerId = new StringBuffer();
		try {
			Iterator modIt = this.tmItElements.values().iterator();
			while (modIt.hasNext()) {
				Module mod = (Module) modIt.next();
				if (!mod.isModuleValid()) {
					errorPerId.append(Messages.getString("content.moduleFactory.validationPrepend", mod.getLabel()) + "\n");
					errorPerId.append(mod.getValidationError() + "\n");
				}
			}
		} catch (Exception exe) {
			System.err.println("Error at isValidA: " + exe.getMessage());
		}
		return errorPerId.toString();
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.btnUp.setEnabled(enabled);
		this.btnDown.setEnabled(enabled);
		this.btnNew.setEnabled(enabled);
		this.btnDeleteMe.setEnabled(enabled);
		//lstIteration.setEnabled(enabled);
		this.enabled = enabled;
		if (enabled) {
			repaintButtons();
		}
	}

	/**
	 * 
	 */
	public final class IterationItem {
		private String label = "";
		private long timestamp = 0;
		private Node item;

		public void setLabel(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		@Override
		public String toString() {
			if (label.equals("")) {
				return "UNBENANNT";
			}
			return label;
		}

		public void setItem(Node item) {
			this.item = item;
		}

		public Node getItem() {
			return item;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public long getTimestamp() {
			return timestamp;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) return false;
			if (!(obj instanceof PanIteration)) return false;
			boolean retVal = true;
			if (!(obj instanceof IterationItem)) return false;
			IterationItem mo = (IterationItem) obj;
			if (!mo.getLabel().equals(label)) {
				return false;
			}
			if (mo.getTimestamp() != timestamp) {
				return false;
			}
			if (mo.getItem() != null && item != null) {
				if (!mo.getItem().equals(item)) {
					retVal = false;
				}
			} else if (mo.getItem() != item) {
				retVal = false;
			}
			return retVal;
		}

		@Override
		public int hashCode() {
			return label.hashCode() + item.hashCode() + (int) timestamp;
		}
	}

	private void splitPanePropertyChange(PropertyChangeEvent e) {
		/*	if (e.getPropertyName().equals("dividerLocation") && preferredPanHeight > 0) {
			 if(log.isDebugEnabled()) log.debug(e.getPropertyName()+" preferredPanHeight "+preferredPanHeight);
			 Dimension panDim  = new Dimension((int)pan.getSize().getWidth(), (int)preferredPanHeight);
			 pan.setSize(panDim);
			 panDim.setSize((int)pan.getSize().getWidth(),(int)(preferredPanHeight +panNorth.getSize().getHeight()));
			 JPanel pparent = (JPanel) this.getParent();
			 JPanel ppparent = (JPanel) pparent.getParent();
			 ppparent.remove(pparent);
			 pparent.setSize(panDim);
			 ppparent.add(pparent);
			 ppparent.revalidate();
			 ppparent.repaint();
			 
		}*/
	}
}