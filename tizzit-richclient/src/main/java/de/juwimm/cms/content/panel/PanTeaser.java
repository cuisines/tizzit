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

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Application;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.modules.Teaser;
import de.juwimm.cms.content.modules.Teaser.TeaserProperties;
import de.juwimm.cms.search.vo.XmlSearchValue;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.swing.AbstractPickListModel;
import de.juwimm.swing.DropDownHolder;
import de.juwimm.swing.PickListData;
import de.juwimm.swing.PickListPanel;
import de.juwimm.swing.SortableListModel;

/**
 * The panel for displaying the teaser within the client.
 * 
 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 *
 */
@SuppressWarnings("serial")
public class PanTeaser extends JPanel {

	private Communication communication;
	private Logger log = Logger.getLogger(PanTeaser.class);

	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

	private JPanel pnlDefinedTeaser;
	private JPanel pnlHeaderLeft;
	private PickListPanel pickListPanel;

	private JPanel pnlRandomTeaser;
	private JPanel pnlHeaderRight;
	private JPanel pnlRandomChoice;

	private Dimension minDefinedTeaserDim = new Dimension(250, 210);
	private Dimension prefDefinedTeaserDim = new Dimension(280, 250);
	private Dimension maxDefinedTeaserDim = new Dimension(800, 600);

	private Dimension minRandomTeaserDim = new Dimension(100, 210);
	private Dimension prefRandomTeaserDim = new Dimension(170, 250);
	private Dimension maxRandomTeaserDim = new Dimension(800, 600);

	private ButtonGroup buttonGroup;
	private PickListData pickListData;
	private String currentSearchScope;
	private boolean isInitialized = false;

	/** All search scopes (defined by the dcfConfig for this mandator). */
	private ArrayList<String> searchScopes = new ArrayList<String>();

	/** All {@link JRadioButton}s that are displayed for the available search scopes. 
	 * Each JRadioButton has an {@code ActionCommand} named after the search scope the JRadioButton represents. */
	private ArrayList<JRadioButton> radioButtonList = new ArrayList<JRadioButton>();

	/** All random teaser's {@link JComboBox}es that are displayed for the available search scopes */
	private ArrayList<JComboBox> comboBoxList = new ArrayList<JComboBox>();

	/** A container for all teaser properties (defined by the dcfConfig's {@code <properties>} element for this mandator).  */
	private Hashtable<String, Teaser.TeaserProperties> hshTeaserProps = new Hashtable<String, Teaser.TeaserProperties>();

	/**
	 * Default constructor.
	 */
	public PanTeaser() {
		try {
			this.setLayout(new BorderLayout());
			ArrayList<String> dummySearchScopes = new ArrayList<String>();
			this.communication = ((Communication) getBean(Beans.COMMUNICATION));

			Dimension headerDim = new Dimension(40, 60);
			Dimension comboDim = new Dimension(130, 20);

			// **********************************************************
			// Left: Explicitly chosen teaser
			// **********************************************************
			this.pnlHeaderLeft = new JPanel();
			this.buttonGroup = new ButtonGroup();
			JLabel lblFixedTeaser = new JLabel(rb.getString("content.modules.teaser.fixed.text"));
			this.pnlHeaderLeft.add(lblFixedTeaser);

			for (int i = 0, count = dummySearchScopes.size(); i < count; i++) {
				final String searchScope = dummySearchScopes.get(i);

				JRadioButton btnRadio = new JRadioButton(searchScope, false);
				btnRadio.setActionCommand(searchScope);
				btnRadio.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						if (!evt.getActionCommand().equals(currentSearchScope)) {
							searchTeasersForScope();
						}
					}
				});
				btnRadio.setBorderPainted(false);
				btnRadio.setOpaque(false);
				this.buttonGroup.add(btnRadio);
				this.pnlHeaderLeft.add(btnRadio);
			}

			this.pnlHeaderLeft.setPreferredSize(headerDim);
			this.pnlHeaderLeft.setMinimumSize(headerDim);
			this.pnlHeaderLeft.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), new TitledBorder(rb.getString("content.modules.teaser.fixed.title"))));

			this.pickListData = new PickListData(new SortableListModel(), new SortableListModel());
			this.pickListData.setLeftLabel(rb.getString("content.modules.teaser.added.teaser"));
			this.pickListData.setRightLabel(rb.getString("content.modules.teaser.available.teaser"));
			this.pickListPanel = new PickListPanel(this.pickListData, true);

			this.pnlDefinedTeaser = new JPanel();
			this.pnlDefinedTeaser.setLayout(new BorderLayout());
			this.pnlDefinedTeaser.setMinimumSize(this.minDefinedTeaserDim);
			this.pnlDefinedTeaser.setPreferredSize(this.prefDefinedTeaserDim);
			this.pnlDefinedTeaser.setMaximumSize(this.maxDefinedTeaserDim);
			this.pnlDefinedTeaser.setOpaque(true);

			// **********************************************************
			// Right: Random teaser
			// **********************************************************
			JLabel lblRandomTeaser = new JLabel(rb.getString("content.modules.teaser.random.text"));
			this.pnlHeaderRight = new JPanel();
			this.pnlHeaderRight.setPreferredSize(headerDim);
			this.pnlHeaderRight.setMinimumSize(headerDim);
			this.pnlHeaderRight.add(lblRandomTeaser);
			this.pnlHeaderRight.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), new TitledBorder(rb.getString("content.modules.teaser.random.title"))));

			this.pnlRandomChoice = new JPanel();
			this.pnlRandomChoice.setLayout(new BoxLayout(this.pnlRandomChoice, BoxLayout.Y_AXIS));

			for (int i = 0, count = dummySearchScopes.size(); i < count; i++) {
				this.pnlRandomChoice.add(Box.createRigidArea(new Dimension(0, 5)));

				JPanel pnlContainer = new JPanel();
				pnlContainer.setMaximumSize(comboDim);
				pnlContainer.setLayout(new GridLayout(0, 2));

				JLabel lblSearchScope = new JLabel(dummySearchScopes.get(i));
				JComboBox cbox = new JComboBox(buildComboBoxModel(5));
				cbox.setName(dummySearchScopes.get(i));
				this.comboBoxList.add(cbox);
				cbox.setPreferredSize(comboDim);
				cbox.setMaximumSize(comboDim);

				pnlContainer.add(lblSearchScope);
				pnlContainer.add(cbox);
				this.pnlRandomChoice.add(pnlContainer);
			}

			this.pnlRandomTeaser = new JPanel();
			this.pnlRandomTeaser.setLayout(new BorderLayout());
			this.pnlRandomTeaser.setMinimumSize(this.minRandomTeaserDim);
			this.pnlRandomTeaser.setPreferredSize(this.prefRandomTeaserDim);
			this.pnlRandomTeaser.setMaximumSize(this.maxRandomTeaserDim);
			this.pnlRandomTeaser.setOpaque(true);

			this.pnlHeaderLeft.setBackground(UIConstants.backgroundBaseColor);
			this.pnlHeaderRight.setBackground(UIConstants.backgroundBaseColor);

			this.pnlDefinedTeaser.add(this.pnlHeaderLeft, BorderLayout.NORTH);
			this.pnlDefinedTeaser.add(this.pickListPanel, BorderLayout.CENTER);

			this.pnlRandomTeaser.add(this.pnlHeaderRight, BorderLayout.NORTH);
			this.pnlRandomTeaser.add(this.pnlRandomChoice, BorderLayout.CENTER);

			this.splitPane.setLeftComponent(this.pnlDefinedTeaser);
			this.splitPane.setRightComponent(this.pnlRandomTeaser);
			this.splitPane.setOneTouchExpandable(true);
			this.splitPane.setResizeWeight(1.0);

			this.add(this.splitPane, BorderLayout.CENTER);
		} catch (Exception exception) {
			log.error("Initialization error", exception);
		}
	}

	/**
	 * Adds the specified {@link TeaserProperties} to the list of managed teasers.
	 * 
	 * @param name the properties' name
	 * @param teaserProperties the {@code TeaserProperties} to add
	 */
	public void addSearchScope(String name, Teaser.TeaserProperties teaserProperties) {
		String unit = teaserProperties.getUnit();
		if (!this.searchScopes.contains(unit)) {
			this.searchScopes.add(unit);
		}
		this.hshTeaserProps.put(name, teaserProperties);
	}

	/**
	 * Re-initializes this panel's view. This should be called after all possible search scopes were added.
	 * Creates the radio buttons that enable the user to change search scopes (as defined by this mandator's dcfConfig).
	 * Creates the drop down boxes that enable the user to choose randomized teasers.
	 */
	public void reInitialize() {
		Dimension comboDim = new Dimension(130, 20);

		this.radioButtonList.clear();
		this.comboBoxList.clear();
		this.pnlHeaderLeft.removeAll();
		this.pnlRandomChoice.removeAll();
		this.buttonGroup = new ButtonGroup();

		JLabel lblFixedTeaser = new JLabel(rb.getString("content.modules.teaser.fixed.text"));
		this.pnlHeaderLeft.add(lblFixedTeaser);

		// **********************************************************
		// Left: Explicitly chosen teaser
		// **********************************************************
		for (int i = 0, count = searchScopes.size(); i < count; i++) {
			final String searchScope = this.searchScopes.get(i);
			JRadioButton btnRadio = new JRadioButton(searchScope, false);
			btnRadio.setActionCommand(searchScope);
			btnRadio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					currentSearchScope = searchScope;
					searchTeasersForScope();
				}
			});
			btnRadio.setBorderPainted(false);
			btnRadio.setOpaque(false);
			this.buttonGroup.add(btnRadio);
			this.pnlHeaderLeft.add(btnRadio);
			this.radioButtonList.add(btnRadio);
		}

		for (int i = 0, count = this.radioButtonList.size(); i < count; i++) {
			if (this.radioButtonList.get(i).isSelected()) {
				this.currentSearchScope = radioButtonList.get(i).getActionCommand();
				break;
			}
		}

		// **********************************************************
		// Right: Random teaser
		// **********************************************************
		for (int i = 0, count = this.searchScopes.size(); i < count; i++) {
			this.pnlRandomChoice.add(Box.createRigidArea(new Dimension(0, 10)));

			String currentSearchScope = this.searchScopes.get(i);
			int maxRandomTeasersForScope = getRandomizableTeaserCount(currentSearchScope);

			JPanel pnlContainer = new JPanel();
			pnlContainer.setMaximumSize(comboDim);
			pnlContainer.setLayout(new GridLayout(0, 2));

			JLabel lblSearchScope = new JLabel(currentSearchScope);
			JComboBox cbox = new JComboBox(buildComboBoxModel(maxRandomTeasersForScope));
			cbox.setName(this.searchScopes.get(i));
			this.comboBoxList.add(cbox);
			cbox.setPreferredSize(comboDim);
			cbox.setMaximumSize(comboDim);

			pnlContainer.add(lblSearchScope);
			pnlContainer.add(cbox);
			this.pnlRandomChoice.add(pnlContainer);
		}
		this.splitPane.resetToPreferredSizes();
		this.validate();
		this.isInitialized = true;
		reset();
	}

	/**
	 * Unloads the current search scope and removes all elements from the pick list panel.
	 */
	private void reset() {
		this.currentSearchScope = null;
		this.pickListData.getLstLeftModel().removeAllElements();
		this.pickListData.getLstRightModel().removeAllElements();
	}

	/**
	 * Selects the radio button for the current search scope,
	 * calls the search for the available teasers,
	 * and sets the maximum values for the random teaser's combo boxes.  
	 */
	public void set() {
		if (this.searchScopes.contains(Teaser.SCOPE_THIS)) {
			this.currentSearchScope = Teaser.SCOPE_THIS;
		} else if (this.searchScopes.contains(Teaser.SCOPE_PARENT)) {
			this.currentSearchScope = Teaser.SCOPE_PARENT;
		} else if (this.searchScopes.contains(Teaser.SCOPE_ROOT)) {
			this.currentSearchScope = Teaser.SCOPE_ROOT;
		} else {
			this.currentSearchScope = Teaser.SCOPE_SITE;
		}
		for (int i = 0, count = this.radioButtonList.size(); i < count; i++) {
			if (this.radioButtonList.get(i).getActionCommand().equals(this.currentSearchScope)) {
				this.radioButtonList.get(i).setSelected(true);
				this.searchTeasersForScope();
				break;
			}
		}
		for (int i = 0, count = this.comboBoxList.size(); i < count; i++) {
			// max value
			JComboBox jcb = this.comboBoxList.get(i);
			int maxCount = getRandomizableTeaserCount(jcb.getName());
			jcb.setModel(buildComboBoxModel(maxCount + 1));
		}
	}

	/**
	 * Is called each time this teaser panel is displayed within the client.
	 * 
	 * @param node
	 */
	public void setProperties(Node node) {
		if (log.isDebugEnabled()) {
			log.debug("setProperties() called with node: " + (node == null ? "null" : node.getNodeName()));
		}
		if (!this.isInitialized) {
			reInitialize();
		}
		reset();
		if (node != null) {
			set();
			// Random teaser:
			Iterator randomRefIterator = XercesHelper.findNodes(node, "./teaserRandomized");
			while (randomRefIterator.hasNext()) {
				Element elmRandomTeaser = (Element) randomRefIterator.next();
				int count = 0;
				String countString = "";
				String searchScope = "";
				try {
					searchScope = XercesHelper.findNode(elmRandomTeaser, "./unit").getFirstChild().getNodeValue();
					countString = XercesHelper.findNode(elmRandomTeaser, "./count").getFirstChild().getNodeValue();
				} catch (Exception exception) {
					log.error(exception.getMessage(), exception);
				}
				if (searchScope.length() > 0) {
					try {
						count = Integer.parseInt(countString);
					} catch (NumberFormatException exception) {
						log.error("Error parsing String to int: " + countString);
					}
				}

				// die ComboBoxen auf die gespeicherten Werte setzen
				for (int i = 0, maxI = this.comboBoxList.size(); i < maxI; i++) {
					if (this.comboBoxList.get(i).getName().equals(searchScope)) {
						JComboBox comboBoxToSet = this.comboBoxList.get(i);
						if (count < comboBoxToSet.getItemCount()) {
							comboBoxToSet.getModel().setSelectedItem(comboBoxToSet.getItemAt(count));
						} else {
							comboBoxToSet.getModel().setSelectedItem(comboBoxToSet.getItemAt(comboBoxToSet.getItemCount() - 1));
						}
					}
				}
			}
			Iterator refIterator = XercesHelper.findNodes(node, "./teaserRef");
			while (refIterator.hasNext()) {
				Element elmTeaser = (Element) refIterator.next();

				String referencedComponentIdString = elmTeaser.getAttribute("viewComponentId");
				String xpathTeaserElement = elmTeaser.getAttribute("xpathTeaserElement");
				String teaserName = elmTeaser.getAttribute("teaserName");

				String xpathIdentifier = elmTeaser.getAttribute("xpathTeaserIdentifier");
				if (xpathIdentifier.length() == 0) {
					xpathIdentifier = null;
				}
				String idString = elmTeaser.getAttribute("teaserIdentifier");
				if (idString.length() == 0) {
					idString = null;
				}
				String xpathTeaserName = elmTeaser.getAttribute("xpathTeaserName");
				if (xpathTeaserName.length() == 0) {
					xpathTeaserName = null;
				}
				try {
					int referencedComponentId = Integer.parseInt(referencedComponentIdString);
					TeaserValue teaserValue = new TeaserValue(referencedComponentId, teaserName, xpathTeaserElement, xpathTeaserName, xpathIdentifier, idString);
					this.chooseTeaser(teaserValue);
				} catch (NumberFormatException exception) {
					log.error("Error parsing teaser's component id: " + referencedComponentIdString, exception);
				}
			}
		}
	}

	public Node getProperties() {
		Node teaserIncludeNode = ContentManager.getDomDoc().createElement("teaserInclude");
		AbstractPickListModel selectedTeasers = this.pickListData.getLstLeftModel();

		for (int i = 0, count = selectedTeasers.getSize(); i < count; i++) {
			Element elmTeaser = ContentManager.getDomDoc().createElement("teaserRef");
			TeaserValue teaserValue = (TeaserValue) ((DropDownHolder) selectedTeasers.getElementAt(i)).getObject();

			// mandatory attribute
			Attr attrViewComponentId = ContentManager.getDomDoc().createAttribute("viewComponentId");
			attrViewComponentId.setValue(Integer.toString(teaserValue.getViewComponentId()));
			elmTeaser.setAttributeNode(attrViewComponentId);

			// mandatory attribute
			Attr attrXpathTeaserElement = ContentManager.getDomDoc().createAttribute("xpathTeaserElement");
			attrXpathTeaserElement.setValue(teaserValue.getXpathTeaserElement());
			elmTeaser.setAttributeNode(attrXpathTeaserElement);

			// mandatory attribute
			Attr attrTeaserName = ContentManager.getDomDoc().createAttribute("teaserName");
			attrTeaserName.setValue(teaserValue.getTeaserName());
			elmTeaser.setAttributeNode(attrTeaserName);

			if (teaserValue.getTeaserIdentifier() != null) {
				Attr attrTeaserIdentifier = ContentManager.getDomDoc().createAttribute("teaserIdentifier");
				attrTeaserIdentifier.setValue(teaserValue.getTeaserIdentifier());
				elmTeaser.setAttributeNode(attrTeaserIdentifier);
			}

			if (teaserValue.getXpathTeaserIdentifier() != null) {
				Attr attrXpathTeaserIdentifier = ContentManager.getDomDoc().createAttribute("xpathTeaserIdentifier");
				attrXpathTeaserIdentifier.setValue(teaserValue.getXpathTeaserIdentifier());
				elmTeaser.setAttributeNode(attrXpathTeaserIdentifier);
			}

			teaserIncludeNode.appendChild(elmTeaser);
		}

		for (int i = 0, count = this.comboBoxList.size(); i < count; i++) {
			int chosenValue = ((Integer) ((DropDownHolder) this.comboBoxList.get(i).getModel().getSelectedItem()).getObject()).intValue();
			if (chosenValue > 0) {
				JComboBox comboBox = this.comboBoxList.get(i);
				String comboBoxName = comboBox.getName(); // name = search scope
				String xpathTeaserElement = null;

				Enumeration teaserPropsEnumeration = this.hshTeaserProps.elements();
				while (teaserPropsEnumeration.hasMoreElements()) {
					Teaser.TeaserProperties currentTeaserProps = (Teaser.TeaserProperties) teaserPropsEnumeration.nextElement();
					if (currentTeaserProps.getUnit().equals(comboBoxName) && currentTeaserProps.getRandomizable().booleanValue()) {
						xpathTeaserElement = currentTeaserProps.getXpathTeaserElement();
					}
				}
				if (xpathTeaserElement == null) {
					if (log.isDebugEnabled()) {
						log.debug("Could not find xpathTeaserElement for " + comboBoxName + "'s randomized teaser");
					}
					continue;
				}

				Element elmRandomTeaser = ContentManager.getDomDoc().createElement("teaserRandomized");
				Attr attrXPath = ContentManager.getDomDoc().createAttribute("xpathTeaserElement");
				attrXPath.setValue(xpathTeaserElement);
				elmRandomTeaser.setAttributeNode(attrXPath);

				Element elmRandomCount = ContentManager.getDomDoc().createElement("count");
				elmRandomCount.appendChild(ContentManager.getDomDoc().createTextNode(Integer.toString(chosenValue)));
				elmRandomTeaser.appendChild(elmRandomCount);

				Element elmUnit = ContentManager.getDomDoc().createElement("unit");
				elmUnit.appendChild(ContentManager.getDomDoc().createTextNode(comboBox.getName()));
				elmRandomTeaser.appendChild(elmUnit);

				teaserIncludeNode.appendChild(elmRandomTeaser);
			}
		}
		return teaserIncludeNode;
	}

	/** @see java.awt.Component#isModuleValid() */
	public boolean isModuleValid() {
		return true;
	}

	public String validateSelectedTeasers() {
		long startTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("Start validateSelectedTeasers()");
		}

		boolean isValid = true;
		// Hashtable: key = xpathTeaserElement (z.B. "//teaser"), value = ArrayList<TeaserValue> die das key-xpathTeaserElement beinhalten
		Hashtable<String, ArrayList<TeaserValue>> referencedTeasers = new Hashtable<String, ArrayList<TeaserValue>>();
		AbstractPickListModel selectedTeasers = this.pickListData.getLstLeftModel();

		for (int i = 0, count = selectedTeasers.getSize(); i < count; i++) {
			TeaserValue teaserValue = (TeaserValue) ((DropDownHolder) selectedTeasers.getElementAt(i)).getObject();
			String xpathTeaserElement = teaserValue.getXpathTeaserElement();
			ArrayList<TeaserValue> arrayListTeaserValues = referencedTeasers.get(xpathTeaserElement) == null ? new ArrayList<TeaserValue>() : referencedTeasers.get(xpathTeaserElement);
			arrayListTeaserValues.add(teaserValue);
			referencedTeasers.put(xpathTeaserElement, arrayListTeaserValues);
		}

		// Check if referenced teasers still exist: 
		try {
			Enumeration keyEnumeration = referencedTeasers.keys();
			if (referencedTeasers.isEmpty()) {
				return null;
			}
			StringBuilder invalidTeasers = new StringBuilder();
			while (keyEnumeration.hasMoreElements()) {

				String xpathString = (String) keyEnumeration.nextElement();
				ArrayList<TeaserValue> arrayListReferencedTeasers = referencedTeasers.get(xpathString);

				// Start search (just once per xpath term!)
				XmlSearchValue[] xmlSearchValuesAllTeaserForXpath = this.communication.searchXml(this.communication.getSiteId(), xpathString);

				// outer loop: all referenced teasers
				for (int i = 0, count = arrayListReferencedTeasers.size(); i < count; i++) {
					boolean referencedTeaserExists = false;
					TeaserValue refTeaser = arrayListReferencedTeasers.get(i);
					// inner loop: all found teasers for the xpath term
					for (int j = 0; j < xmlSearchValuesAllTeaserForXpath.length; j++) {
						if (refTeaser.getViewComponentId() == xmlSearchValuesAllTeaserForXpath[j].getViewComponentId()) {
							if (refTeaser.getXpathTeaserIdentifier() != null) {
								if (refTeaser.getTeaserIdentifier() != null) {
									ArrayList<TeaserValue> resultList = createTeaserValueListFromXml(xmlSearchValuesAllTeaserForXpath[j], xpathString, refTeaser.getXpathTeaserIdentifier(), refTeaser.getXpathTeaserName());
									for (int k = 0; k < resultList.size(); k++) {
										if (resultList.get(k).getTeaserIdentifier() != null && resultList.get(k).getTeaserIdentifier().equals(refTeaser.getTeaserIdentifier())) {
											referencedTeaserExists = true;
											break; // inner loop
										}
									}
								} else {
									log.error("XPath for teaser identifier specified but no teaser identifier itself!");
								}
							} // if no xpath term was specified for teaserIdentifier, matching the viewComponentId is enough: 
							else {
								referencedTeaserExists = true;
							}
							if (referencedTeaserExists) {
								break; // outer loop
							}
						}
					}
					if (!referencedTeaserExists) {
						invalidTeasers.append("\n" + refTeaser.getTeaserName());
					}
					isValid = isValid ? referencedTeaserExists : false;
				}
			}
			if (!isValid) {
				invalidTeasers.insert(0, rb.getString("content.modules.teaser.invalid"));
				long milliseconds = System.currentTimeMillis() - startTime;
				if (log.isDebugEnabled()) {
					log.debug("Finished validateSelectedTeasers(): Calculation took " + milliseconds + " milliseconds");
				}
				return invalidTeasers.toString();
			}
		} catch (Exception exception) {
			log.error(exception);
		}
		long milliseconds = System.currentTimeMillis() - startTime;
		if (log.isDebugEnabled()) {
			log.debug("Finished validateSelectedTeasers(): Calculation took " + milliseconds + " milliseconds");
		}
		return null;
	}

	public void setEnabled(boolean enabling) {
		super.setEnabled(enabling);
		this.splitPane.setOneTouchExpandable(enabling);
		this.splitPane.setEnabled(enabling);
		this.pickListPanel.setEnabled(enabling);
		for (JRadioButton radioButton : this.radioButtonList) {
			radioButton.setEnabled(enabling);
		}
		for (JComboBox comboBox : this.comboBoxList) {
			comboBox.setEnabled(enabling);
		}
	}

	private DefaultComboBoxModel buildComboBoxModel(int maxCount) {
		DropDownHolder[] dropDownHolderArray = new DropDownHolder[maxCount];
		for (int i = 0; i < maxCount; i++) {
			dropDownHolderArray[i] = new DropDownHolder(new Integer(i), Integer.toString(i));
		}
		return new DefaultComboBoxModel(dropDownHolderArray);
	}

	private int getRandomizableTeaserCount(String searchScope) {
		int result = 0;
		int unitId = -666;
		boolean parentSearch = false;
		boolean siteSearch = false;
		ContentManager contentManager = (ContentManager) Application.getBean(Beans.CONTENT_MANAGER);
		if (searchScope.equals(Teaser.SCOPE_THIS)) {
			unitId = contentManager.getActUnitId();
		} else if (searchScope.equals(Teaser.SCOPE_ROOT)) {
			// TODO if root.unitId == this.unitId the searching can be stopped
			unitId = contentManager.getRootUnitId();
		} else if (searchScope.equals(Teaser.SCOPE_PARENT)) {
			unitId = contentManager.getActUnitId();
			parentSearch = true;
		} else if (searchScope.equals(Teaser.SCOPE_SITE)) {
			unitId = 0;
			siteSearch = true;
		}
		if (unitId != -666) {
			// all teaserProperties for the specific SearchScope
			ArrayList<Teaser.TeaserProperties> randomizableTeaserPropsForScope = new ArrayList<TeaserProperties>();

			Enumeration teaserPropsEnumeration = this.hshTeaserProps.elements();
			while (teaserPropsEnumeration.hasMoreElements()) {
				Teaser.TeaserProperties currentTeaserProps = (Teaser.TeaserProperties) teaserPropsEnumeration.nextElement();
				if (currentTeaserProps.getUnit().equals(searchScope) && currentTeaserProps.getRandomizable().booleanValue()) {
					randomizableTeaserPropsForScope.add(currentTeaserProps);
				}
			}
			if (!randomizableTeaserPropsForScope.isEmpty()) {
				for (int i = 0, count = randomizableTeaserPropsForScope.size(); i < count; i++) {
					String xPath = randomizableTeaserPropsForScope.get(i).getXpathTeaserElement();

					XmlSearchValue[] searchResult = null;
					try {
						if (siteSearch) {
							searchResult = this.communication.searchXml(this.communication.getSiteId(), xPath);
						} else {
							searchResult = this.communication.searchXmlByUnit(unitId, xPath, parentSearch);
						}
						if (searchResult != null) {
							for (int j = 0; j < searchResult.length; j++) {
								String resultRootStartElement = "<searchTeaserResult>";
								String resultRootEndElement = "</searchTeaserResult>";
								String lastElementSearchedFor = xPath.substring(xPath.lastIndexOf('/'), xPath.length());

								StringBuilder stringBuilder = new StringBuilder(searchResult[j].getContent());
								stringBuilder.insert(0, resultRootStartElement);
								stringBuilder.append(resultRootEndElement);
								Document doc = XercesHelper.string2Dom(stringBuilder.toString());

								Iterator teaserIterator = XercesHelper.findNodes(doc, "searchTeaserResult" + lastElementSearchedFor);
								while (teaserIterator.hasNext()) {
									teaserIterator.next();
									result++;
								}
							}
						}
					} catch (Exception exception) {
						String searchFunction = siteSearch ? "searchXml(siteId, xPath)" : "searchXmlByUnit(unitId, xPath)";
						log.error("An error occurred executing " + searchFunction + ": ", exception);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Searches for teasers belonging to {@code this.currentSearchScope}. 
	 * This method should only be called when the currentSearchScope did change.
	 */
	private void searchTeasersForScope() {
		try {
			int unitId = -666;
			boolean parentSearch = false;
			boolean siteSearch = false;
			ContentManager contentManager = (ContentManager) Application.getBean(Beans.CONTENT_MANAGER);
			if (this.currentSearchScope.equals(Teaser.SCOPE_THIS)) {
				unitId = contentManager.getActUnitId();
			} else if (this.currentSearchScope.equals(Teaser.SCOPE_ROOT)) {
				unitId = contentManager.getRootUnitId();
			} else if (this.currentSearchScope.equals(Teaser.SCOPE_PARENT)) {
				unitId = contentManager.getActUnitId();
				parentSearch = true;
			} else if (this.currentSearchScope.equals(Teaser.SCOPE_SITE)) {
				unitId = 0;
				siteSearch = true;
			}

			if (unitId != -666) {
				if (log.isDebugEnabled()) {
					log.debug("searchTeasersForScope() unitId = " + unitId);
				}
				// delete all existing teasers for the old search scope
				this.pickListData.getLstRightModel().removeAllElements();

				// all teaserProperties for current search scope
				ArrayList<Teaser.TeaserProperties> teaserPropsForScope = new ArrayList<TeaserProperties>();

				Enumeration teaserPropsEnumeration = this.hshTeaserProps.elements();
				while (teaserPropsEnumeration.hasMoreElements()) {
					Teaser.TeaserProperties currentTeaserProps = (Teaser.TeaserProperties) teaserPropsEnumeration.nextElement();
					if (currentTeaserProps.getUnit().equals(this.currentSearchScope)) {
						teaserPropsForScope.add(currentTeaserProps);
					}
				}
				if (!teaserPropsForScope.isEmpty()) {
					for (int i = 0, count = teaserPropsForScope.size(); i < count; i++) {
						TeaserProperties props = teaserPropsForScope.get(i);
						String xPath = props.getXpathTeaserElement();
						String xPathIdentifier = props.getXpathTeaserIdentifier();
						String xPathTeaserName = props.getXpathTeaserName();

						XmlSearchValue[] searchResult = null;
						if (siteSearch) {
							searchResult = this.communication.searchXml(this.communication.getSiteId(), xPath);
						} else {
							searchResult = this.communication.searchXmlByUnit(unitId, xPath, parentSearch);
						}
						if (searchResult != null) {
							for (int j = 0; j < searchResult.length; j++) {
								ArrayList<TeaserValue> teaserValueList = createTeaserValueListFromXml(searchResult[j], xPath, xPathIdentifier, xPathTeaserName);

								for (int k = 0; k < teaserValueList.size(); k++) {
									TeaserValue teaserValue = teaserValueList.get(k);

									boolean teaserAlreadyChosen = false;
									Iterator chosenIterator = this.pickListData.getLstLeftModel().iterator();
									while (chosenIterator.hasNext()) {
										DropDownHolder ddh = (DropDownHolder) chosenIterator.next();
										TeaserValue chosenTeaserValue = (TeaserValue) ddh.getObject();
										if (teaserValue.getViewComponentId() == chosenTeaserValue.getViewComponentId()) {
											if (teaserValue.getTeaserIdentifier() != null) {
												if (teaserValue.getTeaserIdentifier().equals(chosenTeaserValue.getTeaserIdentifier())) {
													teaserAlreadyChosen = true;
												}
											} else {
												if (chosenTeaserValue.teaserIdentifier == null) {
													teaserAlreadyChosen = true;
												}
											}
										}
									}
									if (!teaserAlreadyChosen) {
										DropDownHolder dropDownHolder = new DropDownHolder(teaserValue, teaserValue.getTeaserName());
										this.pickListData.getLstRightModel().addElement(dropDownHolder);
									}
									this.pickListData.setModified(true);
									this.validate();
									this.repaint();
								}
							}
						}
					}
				}
			}
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	private ArrayList<TeaserValue> createTeaserValueListFromXml(XmlSearchValue xmlSearchValue, String xpathTeaserElement, String xpathIdentifier, String xpathTeaserName) {
		ArrayList<TeaserValue> result = new ArrayList<TeaserValue>();
		if (xmlSearchValue != null) {
			try {
				String resultRootStartElement = "<searchTeaserResult>";
				String resultRootEndElement = "</searchTeaserResult>";
				String lastElementSearchedFor = xpathTeaserElement.substring(xpathTeaserElement.lastIndexOf('/'), xpathTeaserElement.length());

				StringBuilder stringBuilder = new StringBuilder(xmlSearchValue.getContent());
				stringBuilder.insert(0, resultRootStartElement);
				stringBuilder.append(resultRootEndElement);
				Document doc = XercesHelper.string2Dom(stringBuilder.toString());

				Iterator teaserIterator = XercesHelper.findNodes(doc, "/" + lastElementSearchedFor);
				String identifierValue = null;
				String teaserNameValue = null;
				while (teaserIterator.hasNext()) {
					Node node = (Node) teaserIterator.next();
					if (xpathIdentifier != null) {
						if (xpathIdentifier.contains("@")) {
							// look for attribute
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								Element element = (Element) node;
								int atIndex = xpathTeaserName.indexOf("@") + 1;
								identifierValue = element.getAttribute(xpathIdentifier.substring(atIndex));
							} else {
								log.error("Node " + node.getNodeName() + " should contain attribute " + xpathIdentifier + " - but is no element node");
							}
						} else {
							// look for child element
							Node identifierNode = XercesHelper.findNode(node, xpathIdentifier);
							if (identifierNode != null) {
								identifierValue = identifierNode.getFirstChild().getNodeValue();
							} else {
								log.error("Node " + node.getNodeName() + " should contain element " + xpathIdentifier + " but it does not");
							}
						}
					}
					if (xpathTeaserName == null) {
						// if dcf does not define neither an attribute nor an element for specifying the teaser's name 
						teaserNameValue = xmlSearchValue.getText();
						teaserNameValue = teaserNameValue.replaceAll("ä", "ae");
						teaserNameValue = teaserNameValue.replaceAll("ö", "oe");
						teaserNameValue = teaserNameValue.replaceAll("ü", "ue");
						teaserNameValue = teaserNameValue.replaceAll("Ä", "Ae");
						teaserNameValue = teaserNameValue.replaceAll("Ö", "Oe");
						teaserNameValue = teaserNameValue.replaceAll("Ü", "Ue");
						teaserNameValue = teaserNameValue.replaceAll("ß", "ss");
						teaserNameValue.replaceAll("[^A-Za-z_0-9\\.-]", "");
					} else {
						if (xpathTeaserName.contains("@")) {
							// look for attribute
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								Element element = (Element) node;
								int atIndex = xpathTeaserName.indexOf("@") + 1;
								teaserNameValue = element.getAttribute(xpathTeaserName.substring(atIndex));
							} else {
								log.error("Node " + node.getNodeName() + " should contain attribute " + xpathTeaserName + " - but is no element node");
							}
						} else {
							// look for child element
							Node identifierNode = XercesHelper.findNode(node, xpathTeaserName);
							if (identifierNode != null) {
								identifierValue = identifierNode.getFirstChild().getNodeValue();
							} else {
								log.error("Node " + node.getNodeName() + " should contain element " + xpathTeaserName + " but it does not");
							}
						}
					}
					result.add(new TeaserValue(xmlSearchValue.getViewComponentId(), teaserNameValue, xpathTeaserElement, xpathTeaserName, xpathIdentifier, identifierValue));
				}
			} catch (Exception exception) {
				log.error(exception.getMessage(), exception);
			}
		}
		return result;
	}

	/**
	 * Adds the specified {@link TeaserValue} to the pick list panel's "chosen"-side. 
	 * The teaser needs not necessarily being mentioned on the pick list panel's "available"-side,
	 * but if it is, it will be removed from there.
	 * 
	 * @param teaserValue the {@code TeaserValue} to add
	 */
	private void chooseTeaser(TeaserValue teaserValue) {
		// if chosen teaserRef is inside the pool of choosable teasers: take it from there
		Iterator availableIterator = this.pickListData.getLstRightModel().iterator();
		boolean foundInAvailables = false;
		while (availableIterator.hasNext()) {
			DropDownHolder availableObject = (DropDownHolder) availableIterator.next();
			TeaserValue availableTeaserValue = (TeaserValue) availableObject.getObject();

			if (availableTeaserValue.getViewComponentId() == teaserValue.getViewComponentId()) {
				boolean teaserFound = true;
				// more than one teaser per component?
				if (teaserValue.teaserIdentifier != null) {
					if (!teaserValue.teaserIdentifier.equals(availableTeaserValue.teaserIdentifier)) {
						teaserFound = false;
					}
				}
				if (teaserFound) {
					this.pickListData.getLstRightModel().removeElement(availableObject);
					this.pickListData.getLstLeftModel().addElement(availableObject);
				}
				foundInAvailables = teaserFound;
			}
		}
		// if chosen teaserRef is not part of the list of choosable teasers: create new element
		if (!foundInAvailables) {
			DropDownHolder ddh = new DropDownHolder(teaserValue, teaserValue.getTeaserName());
			this.pickListData.getLstLeftModel().addElement(ddh);
		}
	}

	/**
	 * Type for holding Teaser references (may be wrapped by a {@link DropDownHolder}).
	 * This type is used for displaying teasers inside the pick list panel.
	 * 
	 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
	 *
	 */
	public class TeaserValue {
		private int viewComponentId;
		private String teaserName;
		private String xpathTeaserName;
		private String teaserIdentifier;
		private String xpathTeaserElement;
		private String xpathTeaserIdentifier;

		/**
		 * Value constructor.
		 * 
		 * @param viewComponentId the component's ID that contains the teaser
		 * @param teaserName the component's name
		 * @param xpathTeaserElement the xpath term for finding the teaser
		 * @param xpathTeaserName the xpath term for the teaser's name (headline)
		 * @param xpathTeaserIdentifier the xpath term for identifiying a teaser within multiple teaser elements
		 * @param teaserIdentifier the value identifiying the teaser within multiple teaser elements
		 */
		public TeaserValue(int viewComponentId, String teaserName, String xpathTeaserElement, String xpathTeaserName, String xpathTeaserIdentifier, String teaserIdentifier) {
			this.viewComponentId = viewComponentId;
			this.teaserName = teaserName;
			this.xpathTeaserName = xpathTeaserName;
			this.teaserIdentifier = teaserIdentifier;
			this.xpathTeaserElement = xpathTeaserElement;
			this.xpathTeaserIdentifier = xpathTeaserIdentifier;
		}

		/**
		 * @return the teaserIdentifier
		 */
		public String getTeaserIdentifier() {
			return teaserIdentifier;
		}

		/**
		 * @return the viewComponentId
		 */
		public int getViewComponentId() {
			return viewComponentId;
		}

		/**
		 * @return the xpathTeaserName
		 */
		public String getXpathTeaserName() {
			return xpathTeaserName;
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
		 * @return the teaserName
		 */
		public String getTeaserName() {
			return teaserName;
		}

	}
}
