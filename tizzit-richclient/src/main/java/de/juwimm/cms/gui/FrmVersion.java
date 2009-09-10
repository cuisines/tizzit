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
package de.juwimm.cms.gui;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.apache.log4j.Logger;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 *
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 * $Date: 2004-08-27 16:21:40 +0200 (Fr, 27 Aug 2004) $
 */
public class FrmVersion extends JFrame {
	private Logger log = Logger.getLogger(FrmVersion.class);
	private JLabel lblVersionInfo = new JLabel();
	private boolean startup = false;
	private JLabel lblBackground = new JLabel();
	private JTextField jTextField1 = new JTextField();
	private JButton closeButton;

	public FrmVersion() {
		try {
			lblBackground.setDoubleBuffered(true);
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
		closeButton.setVisible(false);
	}	

	public FrmVersion(String version) {
		this();
		lblVersionInfo.setText(version);
		this.jTextField1.setVisible(false);
		closeButton.setVisible(true);
		
	}

	public void setStartup() {
		startup = true;
	}

	public void setStatusInfo(String status) {
		this.jTextField1.setText(" " + status);
		jTextField1.setCaretPosition(0);
	}

	private void jbInit() throws Exception {
		lblBackground.setBounds(new Rectangle(0, 0, 450, 300));
		lblBackground.setIcon(new ImageIcon(getClass().getResource("/images/tizzit_450x300.gif")));
		lblBackground.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				disposeMe();
			}
		});

		lblBackground.setBackground(Color.darkGray);

		lblVersionInfo.setFont(new java.awt.Font("SansSerif", 1, 12));
		lblVersionInfo.setForeground(new Color(92,92,92));
		lblVersionInfo.setText(Constants.CMS_VERSION);
		lblVersionInfo.setHorizontalAlignment(SwingConstants.CENTER);
		//lblVersionInfo.setBounds(new Rectangle(25, 170, 398, 24)); Label.setBounds(186, 100, 220, 30);
		
		lblVersionInfo.setBounds(new Rectangle(261, 193, 220, 30));
		
		lblVersionInfo.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				disposeMe();
			}
		});
		this.setContentPane(lblBackground);
		this.getContentPane().setBackground(Color.white);
		jTextField1.setBackground(Color.white);
		jTextField1.setBorder(BorderFactory.createLineBorder(Color.black));
		jTextField1.setEditable(false);
		jTextField1.setText("Loading...");
		jTextField1.setHorizontalAlignment(SwingConstants.LEFT);
		jTextField1.setBounds(new Rectangle(0, 275, 350, 25));
		
		this.getContentPane().add(lblVersionInfo, null);
		this.getContentPane().add(jTextField1, null);
		closeButton=new JButton(UIConstants.BTN_CLOSE);
		closeButton.setHorizontalAlignment(SwingConstants.CENTER);
		closeButton.setBounds(new Rectangle(430, 3, 16, 16));
		closeButton.setFocusable(false);
		closeButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				disposeMe();				
			}
		});
		this.getContentPane().add(closeButton, null);
		
		this.setResizable(false);
		this.setUndecorated(true);
	}

	public void disposeMe() {
		if (!startup) {
			this.setVisible(false);
			this.dispose();
		}
	}

	public void disposeMe(boolean kill) {
		this.setVisible(false);
		this.dispose();
	}
}