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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.modules.AbstractModule;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanPublication extends JPanel {
	private static Logger log = Logger.getLogger(PanPublication.class);
	private JTextField txtAutor = new JTextField();
	private JTextField txtVeroeffentlicht = new JTextField();
	private JTextField txtDatum = new JTextField();
	private JTextArea txtInhalt = new JTextArea();
	private JTextField txtTel = new JTextField();
	private JTextField txtEmail = new JTextField();
	private JTextField txtHomepage = new JTextField();
	private JTextField txtSonstiges = new JTextField();
	private JTextField txtSubhead = new JTextField();
	private EventListenerList listenerList = new EventListenerList();
	private JTextField txtHead = new JTextField();
	private JLabel jLabel5 = new JLabel();
	private JLabel jLabel6 = new JLabel();
	private JLabel jLabel8 = new JLabel();
	private JLabel jLabel9 = new JLabel();
	private JLabel jLabel10 = new JLabel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private JLabel jLabel18 = new JLabel();
	private JLabel jLabel1 = new JLabel();
	private JLabel jLabel2 = new JLabel();
	private JLabel jLabel3 = new JLabel();
	private JLabel jLabel4 = new JLabel();
	private JLabel jLabel7 = new JLabel();

	public PanPublication() {
		try {
			jbInit();
			jLabel5.setText(rb.getString("panel.content.publication.phoneNumber"));
			jLabel6.setText(rb.getString("panel.content.publication.email"));
			jLabel8.setText(rb.getString("panel.content.publication.website"));
			jLabel9.setText(rb.getString("panel.content.publication.others"));
			jLabel10.setText(rb.getString("panel.content.publication.subTitle"));
			jLabel18.setText(rb.getString("panel.content.publication.publishedIn"));
			jLabel1.setText(rb.getString("panel.content.publication.datePublication"));
			jLabel2.setText(rb.getString("panel.content.publication.summary"));
			jLabel3.setText(rb.getString("panel.content.publication.title"));
			jLabel4.setText(rb.getString("panel.content.publication.author"));
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}

	private void jbInit() throws Exception {
		JScrollPane scr1 = new JScrollPane();
		this.setLayout(gridBagLayout2);

		txtInhalt.setLineWrap(true);
		jLabel5.setText("Telefonnummer");
		jLabel6.setToolTipText("");
		jLabel6.setText("E-Mail-Adresse");
		jLabel8.setToolTipText("");
		jLabel8.setText("URL der Veröffentlichung");
		jLabel9.setText("Sonstiges");
		jLabel9.setToolTipText("");
		jLabel10.setToolTipText("");
		jLabel10.setText("Untertitel");
		scr1.setBorder(BorderFactory.createLoweredBevelBorder());
		jLabel18.setText("Veröffentlicht in");
		jLabel1.setText("Datum / Ausgabe");
		jLabel2.setText("Zusammenfassung");
		jLabel3.setText("Titel der Veröffentlichung");
		jLabel4.setText("Autor");
		txtHomepage.setText("http://");
		jLabel7.setText("(Bitte mit \"http://\" eingeben)");
		this.add(txtAutor, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 9), 269, 1));
		this.add(txtSonstiges, new GridBagConstraints(1, 10, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 10, 9), 270, 1));
		this.add(txtEmail, new GridBagConstraints(1, 9, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 9), 270, 1));
		this.add(txtSubhead, new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 9), 270, 1));
		this.add(jLabel10, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 3));
		this.add(txtVeroeffentlicht, new GridBagConstraints(1, 3, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 9), 270, 1));
		this.add(txtDatum, new GridBagConstraints(1, 4, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 9), 270, 1));
		this.add(scr1, new GridBagConstraints(1, 7, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 10, 0, 9), 0, 52));
		this.add(jLabel6, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 11, 3));
		this.add(jLabel9, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 10, 0), 0, 3));
		this.add(jLabel5, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 1, 3));
		this.add(txtTel, new GridBagConstraints(1, 8, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 9), 270, 1));
		this.add(txtHead, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 9), 269, 1));
		this.add(jLabel18, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 6, 0));
		this.add(jLabel1, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
		this.add(jLabel3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
		this.add(jLabel4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
		this.add(jLabel8, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 3));
		this.add(txtHomepage, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 10), 270, 1));
		this.add(jLabel7, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 0), 0, 0));
		this.add(jLabel2, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));

		scr1.getViewport().add(txtInhalt);
	}

	public String getValidationError() {
		String errMsg = "";
		try {
			//Verify der Felder
			if (!this.txtHomepage.getText().equals("")) { //this field is not mandatory
				new URL(this.txtHomepage.getText());
			}
		} catch (java.net.MalformedURLException me) {
			errMsg = rb.getString("exception.LinkURLnotValid");
		}
		return errMsg;
	}

	public void setProperties(Node prop) {
		if (prop != null) {
			try {
				this.txtAutor.setText(XercesHelper.getNodeValue(prop, "./autor"));
			} catch (Exception e) {
			}
			try {
				this.txtVeroeffentlicht.setText(XercesHelper.getNodeValue(prop, "./veroeffentlicht-in"));
			} catch (Exception e) {
			}
			try {
				this.txtDatum.setText(XercesHelper.getNodeValue(prop, "./datum"));
			} catch (Exception e) {
			}
			try {
				this.txtInhalt.setText(XercesHelper.getNodeValue(prop, "./inhalt"));
			} catch (Exception e) {
			}
			try {
				this.txtTel.setText(XercesHelper.getNodeValue(prop, "./pubtel"));
			} catch (Exception e) {
			}
			try {
				this.txtEmail.setText(XercesHelper.getNodeValue(prop, "./pubemail"));
			} catch (Exception e) {
			}
			try {
				this.txtHomepage.setText(XercesHelper.getNodeValue(prop, "./pubhomepage"));
			} catch (Exception e) {
			}
			try {
				this.txtSonstiges.setText(XercesHelper.getNodeValue(prop, "./pubsonstiges"));
			} catch (Exception e) {
			}
			try {
				this.txtSubhead.setText(XercesHelper.getNodeValue(prop, "./pubsubhead"));
			} catch (Exception e) {
			}
			try {
				this.txtHead.setText(XercesHelper.getNodeValue(prop, "./pubhead"));
			} catch (Exception e) {
			} 
		}
	}

	public Node getProperties() {
		Element elm = ContentManager.getDomDoc().createElement("publikation-komp");
		Element autor = ContentManager.getDomDoc().createElement("autor");
		Element veroeffentlicht = ContentManager.getDomDoc().createElement("veroeffentlicht-in");
		Element datum = ContentManager.getDomDoc().createElement("datum");
		Element inhalt = ContentManager.getDomDoc().createElement("inhalt");
		Element pubtel = ContentManager.getDomDoc().createElement("pubtel");
		Element pubemail = ContentManager.getDomDoc().createElement("pubemail");
		Element pubhomepage = ContentManager.getDomDoc().createElement("pubhomepage");
		Element pubsonstiges = ContentManager.getDomDoc().createElement("pubsonstiges");
		Element pubhead = ContentManager.getDomDoc().createElement("pubhead");
		Element pubsubhead = ContentManager.getDomDoc().createElement("pubsubhead");
		if (!this.txtAutor.getText().equals("")) {
			CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txtAutor.getText());
			autor.appendChild(txtNode);
			elm.appendChild(autor);
		}
		if (!this.txtVeroeffentlicht.getText().equals("")) {
			CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txtVeroeffentlicht.getText());
			veroeffentlicht.appendChild(txtNode);
			elm.appendChild(veroeffentlicht);
		}
		if (!this.txtDatum.getText().equals("")) {
			CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txtDatum.getText());
			datum.appendChild(txtNode);
			elm.appendChild(datum);
		}
		if (!this.txtInhalt.getText().equals("")) {
			CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txtInhalt.getText());
			inhalt.appendChild(txtNode);
			elm.appendChild(inhalt);
		}
		if (!this.txtTel.getText().equals("")) {
			CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txtTel.getText());
			pubtel.appendChild(txtNode);
			elm.appendChild(pubtel);
		}
		if (!this.txtEmail.getText().equals("")) {
			CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txtEmail.getText());
			pubemail.appendChild(txtNode);
			elm.appendChild(pubemail);
		}
		if (!this.txtHomepage.getText().equals("")) {
			CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txtHomepage.getText());
			pubhomepage.appendChild(txtNode);
			pubhomepage.setAttribute("url", AbstractModule.getURLURLEncoded(this.txtHomepage.getText()));
			pubhomepage.setAttribute("target", "_blank");
			elm.appendChild(pubhomepage);
		}
		if (!this.txtSonstiges.getText().equals("")) {
			CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txtSonstiges.getText());
			pubsonstiges.appendChild(txtNode);
			elm.appendChild(pubsonstiges);
		}
		if (!this.txtHead.getText().equals("")) {
			CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txtHead.getText());
			pubhead.appendChild(txtNode);
			elm.appendChild(pubhead);
		}
		if (!this.txtSubhead.getText().equals("")) {
			CDATASection txtNode = ContentManager.getDomDoc().createCDATASection(this.txtSubhead.getText());
			pubsubhead.appendChild(txtNode);
			elm.appendChild(pubsubhead);
		}
		return elm;
	}
}