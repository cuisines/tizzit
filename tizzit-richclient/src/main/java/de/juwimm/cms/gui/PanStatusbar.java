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

import static de.juwimm.cms.client.beans.Application.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.*;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.http.HttpClientWrapper;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.MessageDeleter;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.TaskValue;
import de.juwimm.util.DateConverter;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanStatusbar extends JPanel implements ActionListener {
	private static Logger log = Logger.getLogger(PanStatusbar.class);
	private MessageDeleter messageDeleter;
	private JPanel panMessage = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JLabel lblMessage = new JLabel();
	private JLabel lblDate = new JLabel();
	private JPanel panTask = new JPanel();
	private JLabel lblTask = new JLabel();
	private JPanel jPanel5 = new JPanel();
	private JProgressBar jProgressBar1 = new JProgressBar();
	private BorderLayout borderLayout5 = new BorderLayout();
	private boolean worker = false;
	private JPanel panUser = new JPanel();
	private JLabel lblUser = new JLabel();
	private JPanel panSSL = new JPanel();
	private JLabel lblServername = new JLabel();
	private JLabel lblProxy = new JLabel();
	private BorderLayout borderLayout2 = new BorderLayout();
	private JPanel panMandant = new JPanel();
	private JLabel lblMandant = new JLabel();

	public PanStatusbar() {
		try {
			setDoubleBuffered(true);
			jbInit();
			lblMandant.setIcon(UIConstants.ICON_MANDANT);
			lblUser.setIcon(UIConstants.ICON_USER);
			this.jProgressBar1.setValue(0);
			lblDate.setText(DateConverter.getSql2String(new Date(System.currentTimeMillis())));
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	public void showWorker(boolean enabled) {
		SwingUtilities.invokeLater(new ShowProgressRunnable(enabled));
	}

	/**
	 * 
	 */
	private class ShowProgressRunnable implements Runnable {
		private boolean enabled = false;

		public ShowProgressRunnable(boolean enabled) {
			this.enabled = enabled;
		}

		public void run() {
			if (worker != enabled) {
				jProgressBar1.setIndeterminate(enabled);
				if (enabled) {
					jProgressBar1.setValue(100);
				} else {
					jProgressBar1.setValue(0);
				}
				jProgressBar1.validate();
				jProgressBar1.repaint();
				worker = enabled;
			}
		}
	}

	void jbInit() throws Exception {
		java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		panMessage.setBorder(BorderFactory.createLoweredBevelBorder());
		panMessage.setMinimumSize(new Dimension(4, 21));
		panMessage.setOpaque(false);
		panMessage.setPreferredSize(new Dimension(4, 21));
		panMessage.setLayout(borderLayout1);
		lblDate.setBorder(BorderFactory.createLoweredBevelBorder());
		lblDate.setOpaque(false);
		lblDate.setHorizontalAlignment(SwingConstants.CENTER);
		lblDate.setText("21.08.2003");
		panTask.setBorder(BorderFactory.createLoweredBevelBorder());
		panTask.setMinimumSize(new Dimension(21, 21));
		panTask.setOpaque(false);
		panTask.setPreferredSize(new Dimension(21, 21));
		panTask.setLayout(new BorderLayout());
		lblTask.setHorizontalAlignment(SwingConstants.CENTER);
		jPanel5.setLayout(borderLayout5);
		jProgressBar1.setPreferredSize(new Dimension(100, 16));
		jProgressBar1.setBorderPainted(false);
		panUser.setLayout(new BorderLayout());
		lblUser.setOpaque(false);
		lblUser.setRequestFocusEnabled(true);
		lblUser.setToolTipText("");
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblUser.setText("");
		panUser.setBorder(BorderFactory.createLoweredBevelBorder());
		borderLayout2.setHgap(5);
		borderLayout2.setVgap(5);
		panMandant.setBorder(BorderFactory.createLoweredBevelBorder());
		panMandant.setLayout(new BorderLayout());
		lblMandant.setHorizontalAlignment(SwingConstants.CENTER);
		lblMandant.setText(" ");
		lblServername.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblDate, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 10, 0));
		this.add(panTask, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 0));
		panTask.add(lblTask, BorderLayout.CENTER);
		this.add(panMessage, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 5));
		panMessage.add(lblMessage, BorderLayout.CENTER);
		panMessage.add(jPanel5, BorderLayout.EAST);
		jPanel5.add(jProgressBar1, BorderLayout.CENTER);
		this.add(panUser, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 0));
		panUser.add(lblUser, BorderLayout.CENTER);
		borderLayout1.setHgap(5);
		lblProxy.setIcon(UIConstants.ICON_PROXY);
		lblProxy.setText("");
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.ipadx = 5;
		gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 0);
		gridBagConstraints1.weightx = 1.0D;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.gridwidth = 1;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.insets = new java.awt.Insets(0, 5, 0, 0);
		this.add(panMandant, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 0));
		panMandant.add(lblMandant, BorderLayout.CENTER);
		panSSL.setLayout(new GridBagLayout());
		panSSL.add(lblProxy, gridBagConstraints2);
		panSSL.add(lblServername, gridBagConstraints1);
		panSSL.setBorder(BorderFactory.createLoweredBevelBorder());
		this.add(panSSL, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 0));
	}

	public void removeMessage() {
		lblMessage.setText("");
	}

	public void setMessage(String s) {
		if (messageDeleter != null && messageDeleter.isAlive()) {
			messageDeleter.addTime();
		} else {
			messageDeleter = new MessageDeleter(this);
			messageDeleter.start();
		}
		if (!s.equals(lblMessage.getText())) {
			lblMessage.setText(s);
		}
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals(Constants.ACTION_TASK_SELECT)) {
			if (((TaskValue) e.getSource()).getStatus() == Constants.TASK_STATUS_NEW) {
				lblTask.setIcon(null);
			}
		} else if (action.equals(Constants.ACTION_NEW_TASK_FOR_USER)) {
			lblTask.setIcon(UIConstants.ICON_NEW_TASK);
		} else if (action.equals(Constants.ACTION_LOGOFF)) {
			lblTask.setIcon(null);
		} else if (action.equals(Constants.ACTION_VIEW_EDITOR)) {
			HttpClientWrapper hcl = HttpClientWrapper.getInstance();
			lblProxy.setVisible(hcl.isUsingProxy());
			if (hcl.isUsingProxy()) {
				lblProxy.setToolTipText(Messages.getString("PanStatusbar.proxyConnected", hcl.getProxyServer()));
			}
			lblServername.setIcon(UIConstants.ICON_ENCRYPTED);
			lblServername.setToolTipText(Messages.getString("panel.statusbar.ssl_yes.tooltip"));

			lblServername.setText(Constants.SERVER_HOST);
			Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
			lblUser.setText(comm.getUser().getUserName());
			String siteDetails = null;
			if (log.isDebugEnabled()) {
				siteDetails = String.valueOf(comm.getSiteId()) + ": " + comm.getSiteName();
			} else {
				siteDetails = comm.getSiteName();
			}
			lblMandant.setText(siteDetails);
		}
	}

	public boolean isTaskShown() {
		return (lblTask.getIcon() != null) ? true : false;
	}
} //  @jve:decl-index=0:visual-constraint="10,304"