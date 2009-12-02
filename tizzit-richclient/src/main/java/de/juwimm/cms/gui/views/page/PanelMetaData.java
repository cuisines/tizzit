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
package de.juwimm.cms.gui.views.page;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.gui.controls.LoadableViewComponentPanel;
import de.juwimm.cms.vo.ViewComponentValue;

/**
 * <p>Title: Tizzit </p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanelMetaData extends JPanel implements LoadableViewComponentPanel {
	private static final long serialVersionUID = -6753251850039741721L;
	private static Logger log = Logger.getLogger(PanelMetaData.class);
	private final ResourceBundle rb = Constants.rb;
	private final JLabel jLabel1 = new JLabel();
	private ViewComponentValue vcd;
	private final JPanel jPanel1 = new JPanel();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private final JTextArea txtMetaData = new JTextArea();
	private TitledBorder titledBorder1;
	private final JPanel panDesc = new JPanel();
	private JScrollPane jScrollPane2 = new JScrollPane();
	private final JLabel jLabel3 = new JLabel();
	private final JTextArea txtDescription = new JTextArea();
	private TitledBorder titledBorder2;
	private Component component1;

	public PanelMetaData() {
		try {
			setDoubleBuffered(true);
			jbInit();
			addEditListener();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	private void jbInit() throws Exception {
		titledBorder1 = new TitledBorder(rb.getString("panel.panelMetaData.keywords"));
		titledBorder2 = new TitledBorder(rb.getString("panel.panelMetaData.description"));
		jScrollPane2 = new JScrollPane(txtDescription);
		jScrollPane1 = new JScrollPane(txtMetaData);

		component1 = Box.createVerticalStrut(8);
		this.setLayout(new GridBagLayout());
		jLabel1.setText(rb.getString("panel.panelMetaData.keywordsProsa"));
		jPanel1.setLayout(new GridBagLayout());
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane1.setDoubleBuffered(true);
		txtMetaData.setBorder(BorderFactory.createLoweredBevelBorder());
		txtMetaData.setDoubleBuffered(true);
		txtMetaData.setLineWrap(true);
		txtMetaData.setWrapStyleWord(true);
		jPanel1.setBorder(titledBorder1);
		panDesc.setLayout(new GridBagLayout());
		jScrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane2.setDoubleBuffered(true);
		jLabel3.setText(rb.getString("panel.panelMetaData.descriptionProsa"));
		txtDescription.setBorder(BorderFactory.createLoweredBevelBorder());
		txtDescription.setDoubleBuffered(true);
		txtDescription.setLineWrap(true);
		txtDescription.setWrapStyleWord(true);
		panDesc.setBorder(titledBorder2);
		this.add(jPanel1, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 10), 0, 0));
		jPanel1.add(jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 80));
		jPanel1.add(jLabel1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 10), 0, 0));
		this.add(panDesc, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panDesc.add(jScrollPane2, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 80));
		panDesc.add(jLabel3, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 10), 0, 0));
		this.add(component1, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	public void save() throws Exception {
		// MetaData and MetaDescription are NULL after a new create
		if ((vcd.getMetaData() != null && !vcd.getMetaData().equals(txtMetaData.getText())) || (vcd.getMetaDescription() != null && !vcd.getMetaDescription().equals(txtDescription.getText()))) {
			vcd.setStatus(Constants.DEPLOY_STATUS_EDITED);
		}
		this.vcd.setMetaData(this.txtMetaData.getText());
		this.vcd.setMetaDescription(this.txtDescription.getText());

	}

	public ViewComponentValue getViewComponent() {
		return this.vcd;
	}

	public void load(ViewComponentValue value) {
		this.vcd = value;
		this.txtMetaData.setText(this.vcd.getMetaData());
		this.txtDescription.setText(this.vcd.getMetaDescription());
	}

	public void unload() {
	}

	private final void addEditListener() {
		try {
			txtMetaData.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent ke) {
					if (!Constants.EDIT_CONTENT) {
						Constants.EDIT_CONTENT = true;
					}
				}
			});
			txtDescription.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent ke) {
					if (!Constants.EDIT_CONTENT) {
						Constants.EDIT_CONTENT = true;
					}
				}
			});
		} catch (Exception ex) {
		}
	}

}
