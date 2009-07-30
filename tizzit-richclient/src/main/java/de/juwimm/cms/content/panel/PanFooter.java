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

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanFooter extends JPanel {
	private static Logger log = Logger.getLogger(PanFooter.class);
	private JCheckBox chkPDF = new JCheckBox();
	private JCheckBox chkBrochure = new JCheckBox();
	private JCheckBox chkPrintView = new JCheckBox();
	private JCheckBox chkMailToAFriend = new JCheckBox();
	private JCheckBox chkFavorites = new JCheckBox();
	private JCheckBox chkZoom = new JCheckBox();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private Box leftCenter;
	private Box rightCenter;

	public PanFooter() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("initialization problem", exe);
		}
	}

	private void jbInit() throws Exception {
		leftCenter = Box.createHorizontalBox();
		rightCenter = Box.createHorizontalBox();
		this.setLayout(gridBagLayout1);
		chkPDF.setActionCommand("pdf");
		chkPDF.setText(rb.getString("content.modules.footer.asPDF"));
		chkBrochure.setActionCommand("brochure");
		chkBrochure.setText(rb.getString("content.modules.footer.asBrochure"));
		chkPrintView.setActionCommand("printView");
		chkPrintView.setText(rb.getString("content.modules.footer.printView"));
		chkMailToAFriend.setActionCommand("mail-to-a-friend");
		chkMailToAFriend.setText(rb.getString("content.modules.footer.mailToAFriend"));
		chkFavorites.setActionCommand("favorite");
		chkFavorites.setText(rb.getString("content.modules.footer.favorites"));
		chkZoom.setActionCommand("zoom");
		chkZoom.setText(rb.getString("content.modules.footer.zoom"));
		this.add(chkPrintView, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
		this.add(chkFavorites, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 0), 0, 0));
		this.add(chkMailToAFriend, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
		this.add(chkPDF, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 0), 0, 0));
		this.add(chkZoom, new GridBagConstraints(3, 0, 1, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
		this.add(chkBrochure, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 0), 0, 0));
		this.add(leftCenter, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(rightCenter, new GridBagConstraints(4, 0, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	public Node getProperties() {
		Element elm = ContentManager.getDomDoc().createElement("footer");
		if (chkPDF.isSelected()) {
			Element pdf = ContentManager.getDomDoc().createElement("pdf");
			elm.appendChild(pdf);
		}
		if (chkMailToAFriend.isSelected()) {
			Element mailtoafriend = ContentManager.getDomDoc().createElement("mail-to-a-friend");
			elm.appendChild(mailtoafriend);
		}
		if (chkFavorites.isSelected()) {
			Element favorite = ContentManager.getDomDoc().createElement("favorite");
			elm.appendChild(favorite);
		}
		if (chkPrintView.isSelected()) {
			Element printView = ContentManager.getDomDoc().createElement("printView");
			elm.appendChild(printView);
		}
		if (chkZoom.isSelected()) {
			Element zoom = ContentManager.getDomDoc().createElement("zoom");
			elm.appendChild(zoom);
		}
		if (chkBrochure.isSelected()) {
			Element brochure = ContentManager.getDomDoc().createElement("brochure");
			elm.appendChild(brochure);
		}
		return elm;
	}

	public void setProperties(Node nde) {
		if (nde != null) {
			try {
				if (XercesHelper.findNode(nde, "./pdf") != null) {
					this.chkPDF.setSelected(true);
				} else {
					this.chkPDF.setSelected(false);
				}
				if (XercesHelper.findNode(nde, "./mail-to-a-friend") != null) {
					this.chkMailToAFriend.setSelected(true);
				} else {
					this.chkMailToAFriend.setSelected(false);
				}
				if (XercesHelper.findNode(nde, "./favorite") != null) {
					this.chkFavorites.setSelected(true);
				} else {
					this.chkFavorites.setSelected(false);
				}
				if (XercesHelper.findNode(nde, "./printView") != null) {
					this.chkPrintView.setSelected(true);
				} else {
					this.chkPrintView.setSelected(false);
				}
				if (XercesHelper.findNode(nde, "./zoom") != null) {
					this.chkZoom.setSelected(true);
				} else {
					this.chkZoom.setSelected(false);
				}
				if (XercesHelper.findNode(nde, "./brochure") != null) {
					this.chkBrochure.setSelected(true);
				} else {
					this.chkBrochure.setSelected(false);
				}
			} catch (Exception exe) {
				log.error("Error setting properties in footer", exe);
			}
		}
		this.validate();
		this.repaint();
	}

	public void setEnabled(boolean enabling) {
		this.chkPDF.setEnabled(enabling);
		this.chkMailToAFriend.setEnabled(enabling);
		this.chkFavorites.setEnabled(enabling);
		this.chkPrintView.setEnabled(enabling);
		this.chkZoom.setEnabled(enabling);
		this.chkBrochure.setEnabled(enabling);
	}

}