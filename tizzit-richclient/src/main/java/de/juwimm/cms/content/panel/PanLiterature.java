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

import static de.juwimm.cms.common.Constants.*;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.modules.AbstractModule;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanLiterature extends JPanel {
	private static Logger log = Logger.getLogger(PanLiterature.class);
	private JTextField txtLinkName = new JTextField();
	private JTextField txtLinkURL = new JTextField();
	private JTextField txtYear = new JTextField();
	private JTextField txtAutor = new JTextField();
	private JTextField txtBeschreibung = new JTextField();
	private JTextField txtTitel = new JTextField();
	private JTextField txtVeroeffentlicht = new JTextField();
	private JButton btnCancel = new JButton();
	private JPanel panButtons = new JPanel();
	private JButton btnOk = new JButton();
	private JToggleButton jToggleButton1 = new JToggleButton();
	private EventListenerList listenerList = new EventListenerList();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private JLabel jLabel1 = new JLabel();
	private ButtonGroup buttonGroup1 = new ButtonGroup();
	private JRadioButton jRadioButton1 = new JRadioButton();
	private JRadioButton jRadioButton2 = new JRadioButton();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JTextArea txtZitat = new JTextArea();

	public PanLiterature() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	private void jbInit() throws Exception {
		JLabel jLabel8 = new JLabel();
		JLabel jLabel7 = new JLabel();
		JLabel jLabel6 = new JLabel();
		JLabel jLabel5 = new JLabel();
		JLabel jLabel4 = new JLabel();
		JLabel jLabel3 = new JLabel();
		JLabel jLabel2 = new JLabel();
		txtAutor.setMinimumSize(new Dimension(300, 21));
		txtAutor.setPreferredSize(new Dimension(300, 21));
		txtTitel.setMinimumSize(new Dimension(300, 21));
		txtTitel.setPreferredSize(new Dimension(300, 21));
		jLabel8.setText(rb.getString("panel.panLiterature.others"));
		jLabel7.setText(rb.getString("panel.panLiterature.linkName"));
		jLabel6.setText(rb.getString("panel.panLiterature.linkUrl"));
		jLabel5.setText(rb.getString("panel.panLiterature.publishedIn"));
		this.setLayout(gridBagLayout2);
		jLabel4.setText(rb.getString("panel.panLiterature.title"));
		txtVeroeffentlicht.setMinimumSize(new Dimension(300, 21));
		txtVeroeffentlicht.setPreferredSize(new Dimension(300, 21));
		jLabel3.setText(rb.getString("panel.panLiterature.autor"));
		jLabel2.setText(rb.getString("panel.panLiterature.year"));

		txtLinkURL.setText("http://");
		jLabel1.setToolTipText("");
		jLabel1.setText(rb.getString("panel.panLiterature.quotation"));
		jRadioButton1.setText(rb.getString("panel.panLiterature.withQuotationMark"));
		jRadioButton2.setSelected(true);
		jRadioButton2.setText(rb.getString("panel.panLiterature.withoutQuotationMark"));
		txtZitat.setLineWrap(true);
		txtZitat.setWrapStyleWord(true);
		this.add(jLabel8, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 3), 24, 1));
		this.add(jLabel5, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 5, 0, 1), 0, 0));
		this.add(jLabel6, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 60), 5, 0));
		this.add(jLabel7, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 60), 9, 0));
		this.add(txtBeschreibung, new GridBagConstraints(1, 8, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 351, 1));
		this.add(txtLinkName, new GridBagConstraints(1, 7, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 352, 0));
		this.add(txtLinkURL, new GridBagConstraints(1, 6, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 323, 0));
		this.add(txtVeroeffentlicht, new GridBagConstraints(1, 5, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		this.add(jRadioButton1, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 15, 0));
		this.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 60), 0, 0));
		this.add(jLabel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 60), 0, 0));
		this.add(jLabel2, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(10, 5, 0, 60), 0, 0));
		this.add(txtAutor, new GridBagConstraints(1, 2, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 5), 0, 0));
		this.add(txtTitel, new GridBagConstraints(1, 3, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		this.add(txtYear, new GridBagConstraints(1, 4, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 200), 150, 0));
		this.add(jLabel1, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 3, 0, 1), 0, 0));
		this.add(jScrollPane1, new GridBagConstraints(3, 0, 1, 2, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 190, 25));
		jScrollPane1.getViewport().add(txtZitat, null);
		this.add(jRadioButton2, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 4, 0));
		buttonGroup1.add(jRadioButton1);
		buttonGroup1.add(jRadioButton2);
	}

	public Node getProperties() {
		Element elm = ContentManager.getDomDoc().createElement("literaturangabe");

		Element zitat = ContentManager.getDomDoc().createElement("literatur-zitat");
		Text txtNode = ContentManager.getDomDoc().createTextNode(this.txtZitat.getText());
		zitat.appendChild(txtNode);
		zitat.setAttribute("quotEnabled", checkQuotNeeded());
		elm.appendChild(zitat);

		Element autor = ContentManager.getDomDoc().createElement("literatur-author");
		txtNode = ContentManager.getDomDoc().createTextNode(this.txtAutor.getText());
		autor.appendChild(txtNode);
		elm.appendChild(autor);

		Element titel = ContentManager.getDomDoc().createElement("literatur-titel");
		txtNode = ContentManager.getDomDoc().createTextNode(this.txtTitel.getText());
		titel.appendChild(txtNode);
		elm.appendChild(titel);

		Element aufzaehlung = ContentManager.getDomDoc().createElement("literatur-year");
		txtNode = ContentManager.getDomDoc().createTextNode(this.txtYear.getText());
		aufzaehlung.appendChild(txtNode);
		elm.appendChild(aufzaehlung);

		Element veroeffentlicht = ContentManager.getDomDoc().createElement("literatur-veroeffentlicht-in");
		txtNode = ContentManager.getDomDoc().createTextNode(this.txtVeroeffentlicht.getText());
		veroeffentlicht.appendChild(txtNode);
		elm.appendChild(veroeffentlicht);

		Element link = ContentManager.getDomDoc().createElement("literatur-link");
		txtNode = ContentManager.getDomDoc().createTextNode(this.txtLinkName.getText());
		link.appendChild(txtNode);
		link.setAttribute("target", "_blank");
		link.setAttribute("url", AbstractModule.getURLURLEncoded(this.txtLinkURL.getText()));
		elm.appendChild(link);

		Element sonstiges = ContentManager.getDomDoc().createElement("literatur-sonstiges");
		txtNode = ContentManager.getDomDoc().createTextNode(this.txtBeschreibung.getText());
		sonstiges.appendChild(txtNode);
		elm.appendChild(sonstiges);
		return elm;
	}

	public void setProperties(Node prop) {
		if (prop != null) {
			try {
				this.txtZitat.setText(XercesHelper.getNodeValue(prop, "./literatur-zitat"));
				enableQuoteRadioButton(XercesHelper.findNode(prop, "./literatur-zitat")
						.getAttributes().getNamedItem("quotEnabled").getNodeValue());
				this.txtAutor.setText(XercesHelper.getNodeValue(prop, "./literatur-author"));
				this.txtTitel.setText(XercesHelper.getNodeValue(prop, "./literatur-titel"));
				this.txtYear.setText(XercesHelper.getNodeValue(prop, "./literatur-year"));
				this.txtVeroeffentlicht.setText(XercesHelper.getNodeValue(prop, "./literatur-veroeffentlicht-in"));
				this.txtLinkURL.setText(AbstractModule.getURLDecoded(XercesHelper.findNode(prop, "./literatur-link")
						.getAttributes().getNamedItem("url").getNodeValue()));
				this.txtLinkName.setText(XercesHelper.getNodeValue(prop, "./literatur-link"));
				this.txtBeschreibung.setText(XercesHelper.getNodeValue(prop, "./literatur-sonstiges"));
			} catch (Exception exe) {
				log.error("Error setting properties", exe);
			}
		}
	}

	public String checkQuotNeeded() {
		String zitatNeeded = "";
		if (jRadioButton1.isSelected()) {
			zitatNeeded = "true";
		} else {
			zitatNeeded = "false";
		}
		return zitatNeeded;
	}

	public void enableQuoteRadioButton(String enabled) {
		if (enabled.equals("true")) {
			jRadioButton1.setSelected(true);
		} else {
			jRadioButton2.setSelected(true);
		}
	}
}