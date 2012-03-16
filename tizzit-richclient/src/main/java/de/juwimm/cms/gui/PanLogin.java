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

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.exceptions.InvalidUsernameException;
import de.juwimm.cms.exceptions.LocalizedException;
import de.juwimm.cms.exceptions.NoSitesException;
import de.juwimm.cms.gui.controls.UnloadablePanel;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.ConfigReader;
import de.juwimm.cms.util.Parameters;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.util.UserConfig;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.swing.CustomComboBoxModel;
import de.juwimm.swing.DropDownHolder;

/**
 * <p>Title: Tizzit </p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2002, 2003</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanLogin extends JPanel implements UnloadablePanel {
	private static final long serialVersionUID = 2869508681065150395L;
	private static Logger log = Logger.getLogger(PanLogin.class);
	private final Communication communication = ((Communication) getBean(Beans.COMMUNICATION));
	private JLabel lblLogo;
	private final JTextField txtUserName = new JTextField();
	private final JLabel lblHeadline = new JLabel();
	private final JPanel panel = new JPanel();
	private final JButton cmdLogin = new JButton(UIConstants.BTN_LOGIN);
	private final JLabel lblUsername = new JLabel();
	private final JPasswordField txtPassword = new JPasswordField();
	private final JLabel lblPassword = new JLabel();
	private final JLabel lblDomain = new JLabel();
	private final JProgressBar progressBar = new JProgressBar();
	private final JComboBox cboDomains = new JComboBox();
	private int activeSiteId;

	public PanLogin() {
		try {
			setDoubleBuffered(true);
			jbInit();
			lblLogo = new JLabel();
			lblUsername.setText(rb.getString("panel.login.username"));
			lblPassword.setText(rb.getString("panel.login.password"));
			lblDomain.setText(rb.getString("panel.login.domain"));
			lblHeadline.setText(rb.getString("panel.login.message"));
			cmdLogin.setText(rb.getString("panel.login.loginButton"));
			txtPassword.addKeyListener(new MyKeyListener(this));
			txtUserName.addKeyListener(new MyKeyListener(this));
			cboDomains.addKeyListener(new MyKeyListener(this));
			this.lblDomain.setVisible(false);
			this.cboDomains.setVisible(false);
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		lblLogo = new JLabel();
		this.setLayout(new GridBagLayout());

		lblLogo.setVerticalAlignment(SwingConstants.BOTTOM);
		lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblLogo.setIcon(new ImageIcon(getClass().getResource("/images/tizzit_450x121.png")));
		lblHeadline.setText("Benutzeranmeldung");
		lblHeadline.setFont(new java.awt.Font("Dialog", 1, 20));
		lblHeadline.setHorizontalAlignment(SwingConstants.CENTER);
		panel.setLayout(new GridBagLayout());
		panel.setPreferredSize(new Dimension(220, 200));
		cmdLogin.setHorizontalAlignment(SwingConstants.CENTER);
		cmdLogin.setIcon(new ImageIcon(getClass().getResource("/images/button/login.png")));
		cmdLogin.setMnemonic('0');
		cmdLogin.setSelected(false);
		cmdLogin.setText("loginButton");
		cmdLogin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmdLoginActionPerformed();
			}
		});
		cmdLogin.addKeyListener(new MyKeyListener(this));
		lblUsername.setText("username");
		lblPassword.setText("password");
		lblDomain.setText("Domain");
		txtUserName.setPreferredSize(new Dimension(6, 21));
		txtPassword.setPreferredSize(new Dimension(6, 21));
		progressBar.setDoubleBuffered(true);

		this.add(lblLogo, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(panel, new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 50, 0));
		panel.add(lblHeadline, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(30, 10, 0, 10), 0, 0));
		panel.add(lblUsername, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panel.add(txtUserName, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 10), 0, 4));
		panel.add(lblPassword, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panel.add(txtPassword, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 10), 0, 4));
		panel.add(cmdLogin, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 10), 0, 0));
		panel.add(lblDomain, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		panel.add(progressBar, new GridBagConstraints(0, 5, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(30, 10, 0, 10), 0, 0));
		panel.add(cboDomains, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 10), 0, 4));
	}

	void cmdLoginActionPerformed() {
		char[] password = this.txtPassword.getPassword();
		if (password == null || password.length == 0) {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("panel.login.loginError") + " \n" + rb.getString("exception.PasswordRequired"), rb.getString("msgbox.title.loginFailed"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		this.progressBar.setIndeterminate(true);
		setEnabled(false);

		try {
			if (!this.cboDomains.isVisible()) {
				SiteValue[] sv = this.communication.getSites(txtUserName.getText(), String.copyValueOf(txtPassword.getPassword()));
				if (sv == null || sv.length <= 0 || sv[0] == null) {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("panel.login.loginError") + " \n" + rb.getString("exception.NoSites"), rb.getString("msgbox.title.loginFailed"), JOptionPane.ERROR_MESSAGE);
				} else if (sv.length > 1) {
					Arrays.sort(sv, new SiteValueComparator());
					this.progressBar.setIndeterminate(false);
					this.lblDomain.setVisible(true);
					this.cboDomains.setModel(new CustomComboBoxModel(sv, "getName"));
					this.cboDomains.setVisible(true);
					this.txtPassword.setVisible(false);
					this.txtUserName.setVisible(false);
					this.lblUsername.setVisible(false);
					this.lblPassword.setVisible(false);
					this.setCursor(Cursor.getDefaultCursor());
					setEnabled(true);
					return;
				} else {
					//only one site
					this.activeSiteId = sv[0].getSiteId();
					login();
				}
			} else {
				this.activeSiteId = ((SiteValue) ((DropDownHolder) cboDomains.getSelectedItem()).getObject()).getSiteId();
				this.cboDomains.setEnabled(false);
				login();
			}
		} catch (InvalidUsernameException iu) {
			this.progressBar.setIndeterminate(false);
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("panel.login.loginError") + " \n" + rb.getString("exception.InvalidUsernameOrPassword"), rb.getString("msgbox.title.loginFailed"), JOptionPane.ERROR_MESSAGE);
			this.txtPassword.setText("");
			this.txtPassword.requestFocus();
		} catch (NoSitesException se) {
			this.progressBar.setIndeterminate(false);
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("panel.login.loginError") + " \n" + rb.getString("exception.NoSites"), rb.getString("msgbox.title.loginFailed"), JOptionPane.ERROR_MESSAGE);
		} catch (LocalizedException exe) {
			this.progressBar.setIndeterminate(false);
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("panel.login.loginError") + " \n\"" + exe.getLocalizedMessage() + "\"\n", rb.getString("msgbox.title.loginFailed"), JOptionPane.ERROR_MESSAGE);
		} catch (Exception exe) {
			log.error("Login Error", exe);
			this.progressBar.setIndeterminate(false);
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("panel.login.loginError") + " \n\"" + exe.getMessage() + "\"\n", rb.getString("msgbox.title.loginFailed"), JOptionPane.ERROR_MESSAGE);
		} finally {
			this.setCursor(Cursor.getDefaultCursor());
			this.progressBar.setIndeterminate(false);
			setEnabled(true);
		}
	}

	/**
	 * 
	 */
	class MyKeyListener extends KeyAdapter {

		public MyKeyListener(PanLogin panLogin) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (!String.copyValueOf(txtPassword.getPassword()).equals("") && !txtUserName.getText().equals("")) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							cmdLoginActionPerformed();
						}
					});
				}
				if (e.getSource().equals(txtUserName) && !txtUserName.getText().equals("")) {
					txtPassword.requestFocus();
				} else {
					txtUserName.requestFocus();
				}
			}
		}
	}

	public void init() {
		txtUserName.setText("");
		txtPassword.setText("");
		activeSiteId = 0;
		this.cboDomains.removeAllItems();
		txtUserName.requestFocus();
		this.lblDomain.setVisible(false);
		this.cboDomains.setVisible(false);
		this.txtPassword.setVisible(true);
		this.txtUserName.setVisible(true);
		this.lblUsername.setVisible(true);
		this.lblPassword.setVisible(true);
		this.setCursor(Cursor.getDefaultCursor());
		this.cmdLogin.setEnabled(true);
		this.txtUserName.requestFocusInWindow();
	}

	@Override
	public void setEnabled(boolean enabling) {
		this.cmdLogin.setEnabled(enabling);
		this.txtPassword.setEnabled(enabling);
		this.txtUserName.setEnabled(enabling);
	}

	private void login() {
		try {
			this.communication.login(txtUserName.getText(), String.copyValueOf(txtPassword.getPassword()), activeSiteId);

			UserConfig uc = UserConfig.getInstance();
			//LookAndFeel.switchTo(uc.getConfigNodeValue(UserConfig.USERCONF_CURRENT_SKIN), false);
			String preferredLanguage = uc.getConfigNodeValue(UserConfig.USERCONF_PREFERRED_LANGUAGE);

			if (preferredLanguage == null || preferredLanguage.equals("")) {
				preferredLanguage = "de";
			}
			Constants.CMS_LOCALE = new Locale(preferredLanguage);
			Locale.setDefault(Constants.CMS_LOCALE);
			log.info("Current Language: " + Constants.CMS_LOCALE.getLanguage());
			Constants.CMS_LANGUAGE = Constants.CMS_LOCALE.getLanguage();
			Constants.rb = ResourceBundle.getBundle("CMS", Constants.CMS_LOCALE);

			//LOAD CONFIG
			// Read parameters from configurationfile to Contants class

			SiteValue currentSite = this.communication.getCurrentSite();

			try {
				Constants.CMS_PATH_WYSIWYGIMAGE = currentSite.getWysiwygImageUrl();
				Parameters.loadRolesetForActiveSite();
				Constants.CMS_PATH_DCF = currentSite.getDcfUrl();
				Constants.CMS_PATH_DEMOPAGE = currentSite.getPreviewUrlWorkServer();
				Constants.CMS_PATH_HELP = currentSite.getHelpUrl();
			} catch (Exception exe) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), Constants.rb.getString("exception.cantCatchConfigurationFromServer"));
				System.exit(1);
			}

			try {
				String ff = currentSite.getPageNameFull();
				if (!ff.trim().equals("")) {
					Constants.CMS_PATH_DEMOPAGE_FULLFRAMESET = ff;
				}
				ff = currentSite.getPageNameContent();
				if (!ff.trim().equals("")) {
					Constants.CMS_PATH_DEMOPAGE_CONTENT = ff;
				}
			} catch (Exception exe) {
			}

			if (log.isDebugEnabled()) {
				log.debug("DCF Path: " + Constants.CMS_PATH_DCF);
			}
			Constants.CMS_AVAILABLE_DCF.clear();
			this.communication.getDbHelper().createTables(); // clear cache
			this.communication.setLoggedIn(true);
			PanLogin.loadTemplates(true);

			this.progressBar.setIndeterminate(false);
			//STARTUP THE SYSTEM
			ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_LOGIN));
		} catch (Exception exe) {
			this.progressBar.setIndeterminate(false);
			log.error("Error during startup phase", exe);
		}
	}

	public void unload() {
	}

	/**
	 * Method cleans the cache of all available templates and reloads all templates in the selected language
	 */
	public static void loadTemplates(boolean isWaiting) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				Constants.CMS_AVAILABLE_DCF.clear();
				NodeList nl = null;

				try {
					URL dcfUrl = new URL(Constants.CMS_PATH_DCF + "dcf.xml?lang=" + Constants.CMS_LOCALE.getLanguage());
					ConfigReader cr = new ConfigReader(dcfUrl, ConfigReader.CONF_NODE_DCF);
					nl = cr.getConfigSubelements("//availableTemplates");
				} catch (Exception exe) {
					log.error("Error getting configreader for dcf.xml with path " + Constants.CMS_PATH_DCF + " and language " + Constants.CMS_LOCALE.getLanguage(), exe);
				}
				if (nl == null || nl.getLength() < 1) {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.TEMPLATE_NOT_FOUND"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				} else {
					for (int i = 0; i < nl.getLength(); i++) {
						Node elm = nl.item(i);
						if (elm instanceof Element && ((Element)elm).getNodeName().equals("item")) {
							String desc = XercesHelper.getNodeValue(elm);
							String ndeKey = ((Element) elm).getAttribute("filename");
							String editableBy = ((Element) elm).getAttribute("editableBy");
							String titleRequired = ((Element) elm).getAttribute("titleRequired");
							String role = ((Element) elm).getAttribute("role");
							String defaultTemplate=((Element) elm).getAttribute("default");

							if (editableBy == null || editableBy.equals("")) {
								editableBy = "";
							}
							if (titleRequired == null || titleRequired.equals("")) {
								titleRequired = "false";
							}
							HashMap<String, String> val = new HashMap<String, String>();
							val.put("description", desc);
							val.put("role", role);
							val.put("editableBy", editableBy);
							val.put("titleRequired", titleRequired);
							val.put("default", defaultTemplate);
							//key is filename without .xml, val is Description/Role Hashmap
							Constants.CMS_AVAILABLE_DCF.put(ndeKey, val);
							try {
								((Communication) getBean(Beans.COMMUNICATION)).getDCF(ndeKey);
							} catch (Exception exe) {
								log.error("Error during loading of template " + ndeKey, exe);
							}
						} else if(elm instanceof Element && ((Element)elm).getNodeName().equals("category")){
							String ndeKey = ((Element) elm).getAttribute("name");
							HashMap<String, String> val = new HashMap<String, String>();
							val.put("description", Constants.CMS_AVAILABLE_DCF_CATEGORY_NODE);
							Constants.CMS_AVAILABLE_DCF.put(ndeKey, val);
						} else if(elm instanceof Element){
							log.error("Invalid node in template list " + elm.getNodeName());
						}
					}
				}
			}
		});
		t.setName("LoadTemplatesRunner");
		t.setPriority(Thread.NORM_PRIORITY);
		t.start();
		if (isWaiting) {
			if (log.isDebugEnabled()) log.debug("Waiting for " + t.getName() + " to finish work...");
			try {
				t.join(5 * 1000L);
			} catch (InterruptedException e) {
				log.warn("Error waiting for " + t.getName() + ": " + e.getMessage(), e);
			}
		}
	}

	private final class SiteValueComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			SiteValue v1 = (SiteValue) o1;
			SiteValue v2 = (SiteValue) o2;
			if (v1 == null || v1.getName() == null) return -1;
			if (v2 == null || v2.getName() == null) return 1;
			return v1.getName().toLowerCase().compareTo(v2.getName().toLowerCase());
		}

	}

}
