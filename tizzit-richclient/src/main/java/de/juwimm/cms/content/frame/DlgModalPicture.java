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
package de.juwimm.cms.content.frame;

import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import de.juwimm.cms.content.panel.PanPicture;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class DlgModalPicture extends JDialog {
	private static Logger log = Logger.getLogger(DlgModalPicture.class);
	private JPanel panButtons = new JPanel();
	private JButton btnOk = new JButton();
	private JButton btnCancel = new JButton();
	private PanPicture rootPanel = new PanPicture();
	private EventListenerList listenerList = new EventListenerList();

	public DlgModalPicture() {
		super(UIConstants.getMainFrame(), true);
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	public DlgModalPicture(ActionListener al) {
		this();
		this.addActionListener(al);
		int height = 290;
		int width = 440;
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		this.setSize(width, height);
		this.setLocation((screenWidth / 2) - (width / 2), (screenHeight / 2) - (height / 2));
		this.setTitle(rb.getString("dialog.title"));
		this.getRootPane().setDefaultButton(btnOk);
	}

	public void addActionListener(ActionListener al) {
		this.listenerList.add(ActionListener.class, al);
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(new BorderLayout());
		btnOk.setText(rb.getString("dialog.ok"));
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOkActionPerformed(e);
			}
		});
		btnCancel.setText(rb.getString("dialog.cancel"));
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed(e);
			}
		});
		panButtons.setLayout(new GridBagLayout());
		this.getContentPane().add(panButtons, BorderLayout.SOUTH);
		panButtons.add(btnOk, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
		panButtons.add(btnCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 1, 0));
		this.getContentPane().add(getRootPanel(), BorderLayout.CENTER);
	}

	void btnOkActionPerformed(ActionEvent e) {
		ActionEvent ae = new ActionEvent(this.getRootPanel(), ActionEvent.ACTION_PERFORMED, "SAVE");
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			((ActionListener) listeners[i + 1]).actionPerformed(ae);
		}
		this.setVisible(false);
		this.dispose();
	}

	void btnCancelActionPerformed(ActionEvent e) {
		this.setVisible(false);
		this.dispose();
	}

	/**
	 * @param rootPanel The rootPanel to set.
	 */
	public void setRootPanel(PanPicture rootPanel) {
		this.rootPanel = rootPanel;
	}

	/**
	 * @return Returns the rootPanel.
	 */
	public PanPicture getRootPanel() {
		return rootPanel;
	}
}