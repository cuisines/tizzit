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
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanDocumentSymbol extends JPanel {
	private static Logger log = Logger.getLogger(PanDocumentSymbol.class);
	private JLabel lblFileName = new JLabel();
	private JToggleBtt btnFile = new JToggleBtt(this);
	private BorderLayout borderLayout1 = new BorderLayout();

	public PanDocumentSymbol() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}

	void jbInit() throws Exception {
		this.setBorder(null);
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				selectedFileName(e);
			}
		});
		this.setLayout(borderLayout1);
		getFileNameLabel().setOpaque(true);
		getFileNameLabel().setBackground(SystemColor.text);
		getFileNameLabel().setForeground(SystemColor.textText);
		getFileNameLabel().setHorizontalAlignment(SwingConstants.CENTER);
		getFileNameLabel().setHorizontalTextPosition(SwingConstants.CENTER);
		getFileNameLabel().setText("blabla.pdf");
		getFileNameLabel().addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				selectedFileName(e);
			}
		});
		getFileButton().setBorder(new EmptyBorder(0, 0, 0, 0));
		getFileButton().setOpaque(true);
		getFileButton().setBackground(SystemColor.text);
		this.add(getFileButton(), BorderLayout.WEST);
		this.add(getFileNameLabel(), BorderLayout.CENTER);
	}

	private void selectedFileName(MouseEvent e) {
		getFileButton().doClick();
		getFileNameLabel().setBackground(SystemColor.textHighlight);
		getFileNameLabel().setForeground(SystemColor.textHighlightText);
		getFileNameLabel().validate();
		getFileNameLabel().updateUI();
		ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "CLICK");
		getFileButton().fireActionPerformedT(ae);
	}

	/**
	 * @param lblFileName The lblFileName to set.
	 */
	public void setFileNameLabel(JLabel lblFileName) {
		this.lblFileName = lblFileName;
	}

	/**
	 * @return Returns the lblFileName.
	 */
	public JLabel getFileNameLabel() {
		return lblFileName;
	}

	/**
	 * @param btnFile The btnFile to set.
	 */
	public void setFileButton(JToggleBtt btnFile) {
		this.btnFile = btnFile;
	}

	/**
	 * @return Returns the btnFile.
	 */
	public JToggleBtt getFileButton() {
		return btnFile;
	}
	
	/**
	 * 
	 */
	public class JToggleBtt extends JToggleButton {
		private PanDocumentSymbol tt;

		public JToggleBtt(PanDocumentSymbol tt) {
			this.tt = tt;
		}

		public void doClick() {
			super.setSelected(true);
			tt.getFileNameLabel().setBackground(SystemColor.textHighlight);
			tt.getFileNameLabel().setForeground(SystemColor.textHighlightText);
			tt.getFileNameLabel().validate();
			tt.getFileNameLabel().updateUI();
		}

		public void unClick() {
			tt.getFileNameLabel().setBackground(SystemColor.text);
			tt.getFileNameLabel().setForeground(SystemColor.textText);
			tt.getFileNameLabel().validate();
			tt.getFileNameLabel().updateUI();
		}

		public void fireActionPerformedT(ActionEvent ae) {
			fireActionPerformed(ae);
		}
	}
}