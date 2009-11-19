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

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.GetContentHandler;
import de.juwimm.cms.content.frame.FrmStickyPad;
import de.juwimm.cms.exceptions.AlreadyCheckedOutException;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.gui.controls.LoadableViewComponentPanel;
import de.juwimm.cms.gui.event.ExitEvent;
import de.juwimm.cms.gui.event.ExitListener;
import de.juwimm.cms.gui.views.PanContentView;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.PropertyActionEvent;
import de.juwimm.cms.util.PropertyConfigurationEvent;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.ContentValue;
import de.juwimm.cms.vo.ContentVersionValue;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.swing.DropDownHolder;

/**
 * <p>Title: Tizzit </p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanelContent extends JPanel implements LoadableViewComponentPanel, ActionListener, ExitListener {
	private static final long serialVersionUID = 1068224704851470409L;
	private static Logger log = Logger.getLogger(PanelContent.class);
	public static final String PROP_CHECKIN = "PanelContent.ACTION_CHECKIN";
	public static final String PROP_CHECKOUT = "PanelContent.ACTION_CHECKOUT";
	private static final String ACTION_CONTENT_VERSION_SELECTED = "CONTENT_VERSION_SELECTED";
	private final SimpleDateFormat sdf = new SimpleDateFormat(rb.getString("General.ShortDateTimeFormat"));

	private final ReentrantLock lock = new ReentrantLock();
	private ContentValue contentValue = null;
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private ViewComponentValue loadedViewComponentValue = null;
	private final JScrollPane jScrollpane = new JScrollPane();
	private ContentManager contentManager = ((ContentManager) getBean(Beans.CONTENT_MANAGER));
	private final JPanel jPanel1 = new JPanel();
	private final JLabel lblHeader = new JLabel();
	private final JTextField txtHeadline = new JTextField();
	private final JButton btnDeleteAllOldContentVersions = new JButton();
	private final JLabel lblContentVersions = new JLabel();
	private final JButton btnDeleteSelectedContentVersion = new JButton();
	private final JComboBox cboContentVersions = new JComboBox();
	private final JPanel panContentHeader = new JPanel(); // contains the ContentVersion-DropDown and the Page-Title
	private JEditorPane txtEditor = null;

	private boolean loadFromDropDown = false;
	private boolean dropdownEnabled = false;
	private boolean hasBeenSavedAndCanCheckinWithoutSave = false;
	private boolean hasFirstSaveDoneAndDoNotCreateNewContentVersion = false;
	private boolean removeLock = false;
	private boolean isCheckedOut = false;
	private boolean isEdit = false;

	public PanelContent(PanContentView panContent) {
		this();
		ActionHub.addActionListener(this);
		ActionHub.addExitListener(this);
	}

	public PanelContent() {
		try {
			jbInit();
			cboContentVersions.addActionListener(this);
			cboContentVersions.setActionCommand(ACTION_CONTENT_VERSION_SELECTED);
			btnDeleteSelectedContentVersion.setText(rb.getString("panel.panelContent.btnDeleteSelectedContentVersion"));
			lblContentVersions.setText(rb.getString("panel.panelContent.lblContentVersions"));
			btnDeleteAllOldContentVersions.setText(rb.getString("panel.panelContent.btnDeleteAllOldContentVersions"));
			lblHeader.setText(rb.getString("panel.panelContent.lblHeader"));
			jScrollpane.setDoubleBuffered(true);
			jScrollpane.setWheelScrollingEnabled(false);
			jScrollpane.addMouseWheelListener(new MouseWheelListener() {
				public void mouseWheelMoved(MouseWheelEvent event) {
					if (event.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
						Point act = jScrollpane.getViewport().getViewPosition();
						int clicks = event.getUnitsToScroll();
						Point newPt = new Point((int) act.getX(), (int) act.getY() + 20 * clicks);
						int visibleHeight = (int) jScrollpane.getViewport().getExtentSize().getHeight();
						int viewHeight = (int) jScrollpane.getViewport().getViewSize().getHeight();
						if (newPt.getY() + visibleHeight > viewHeight) {
							newPt.y = viewHeight - visibleHeight;
						}
						if (newPt.getY() < 0) {
							newPt.y = 0;
						}
						jScrollpane.getViewport().setViewPosition(newPt);
					}
				}
			});
			HeadLineDocument hld = new HeadLineDocument();
			txtHeadline.setDocument(hld);
		} catch (Exception exe) {
			log.error("Initialization Error", exe);
		}
	}

	void jbInit() throws Exception {
		jPanel1.setLayout(new BorderLayout());
		panContentHeader.setPreferredSize(new Dimension(60, 60));
		panContentHeader.setOpaque(true);
		panContentHeader.setMinimumSize(new Dimension(1, 1));
		panContentHeader.setLayout(new GridBagLayout());
		btnDeleteSelectedContentVersion.setText("Delete this");
		btnDeleteSelectedContentVersion.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteSelectedContentVersionActionPerformed(e);
			}
		});
		btnDeleteSelectedContentVersion.setActionCommand("jButton1");
		lblContentVersions.setText("Old Contentversions");
		btnDeleteAllOldContentVersions.setText("Delete all old");
		btnDeleteAllOldContentVersions.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteAllOldContentVersionsActionPerformed(e);
			}
		});
		this.setLayout(new BorderLayout());

		lblHeader.setText("Überschrift / Seitentitel");
		txtHeadline.setEditable(false);
		txtHeadline.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				txtHeadlineKeyTyped(e);
			}
		});

		this.add(jPanel1, BorderLayout.NORTH);
		panContentHeader.add(cboContentVersions, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 5), 276, 0));
		panContentHeader.add(lblContentVersions, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 6, 0, 12), 4, 0));
		panContentHeader.add(btnDeleteAllOldContentVersions, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));
		panContentHeader.add(btnDeleteSelectedContentVersion, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));
		panContentHeader.add(txtHeadline, new GridBagConstraints(1, 1, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(7, 0, 6, 5), 489, 0));
		panContentHeader.add(lblHeader, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 6, 6, 0), 6, 4));
		this.add(jScrollpane, BorderLayout.CENTER);
		jPanel1.add(panContentHeader, BorderLayout.NORTH);
	}

	public void checkIn() { // this is for cancel
		comm.checkIn(contentValue.getContentId().intValue());
	}

	/** 
	 * Saves the content.
	 * 
	 * @see de.juwimm.cms.gui.controls.LoadableViewComponentPanel#save() */
	public void save() {
		this.lock.lock();
		Constants.EDIT_CONTENT = false;
		isEdit = false;
		UIConstants.setActionStatus(rb.getString("statusinfo.content.save"));
		if (contentManager != null && contentManager.isModuleValid()) {
			String newContent = null;
			boolean ok = true;

			if (contentValue != null && contentValue.getTemplate() != null) {
				HashMap<String, String> val = (HashMap<String, String>) Constants.CMS_AVAILABLE_DCF.get(contentValue.getTemplate());
				String titleRequired = val.get("titleRequired");
				String headline = txtHeadline.getText();

				if (titleRequired != null && titleRequired.equalsIgnoreCase("true") && (headline == null || headline.equals(""))) {
					log.error("dcf-titleRequired set, but headline empty");
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.HeadlineRequired"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
					ok = false;
				}
			}
			if (ok) {
				ActionHub.configureProperty(PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "false");
				ActionHub.configureProperty(PROP_CHECKOUT, PropertyConfigurationEvent.PROP_ENABLE, "false");

				try {
					newContent = contentManager.getContent(txtHeadline.getText());
					if (newContent == null || "".equalsIgnoreCase(newContent)) {
						throw new Exception("Content is Empty");
					}
				} catch (Exception exe) {
					log.error("Content was not valid: " + newContent, exe);
					// set focus on txtHeadline?
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.ContentNotValid"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				}
				if (newContent != null && !newContent.equals("") && contentValue != null && isContentCheckedOutByMyself(contentValue)) {
					try {
						if (contentValue.getHeading() == null && txtHeadline.getText() != null && txtHeadline.getText().length() > 0 || contentValue.getContentText() == null && newContent != null && newContent.length() > 0 || contentValue.getHeading() != null && !contentValue.getHeading().equals(txtHeadline.getText()) || contentValue.getContentText() != null && !contentValue.getContentText().equals(newContent) || loadFromDropDown) {
							loadedViewComponentValue.setStatus(Constants.DEPLOY_STATUS_EDITED);
							ActionHub.fireActionPerformed(new ActionEvent(loadedViewComponentValue, ActionEvent.ACTION_PERFORMED, Constants.ACTION_TREE_ENTRY_NAME));
							log.info("Setting to edited!");
						}

						contentValue.setHeading(txtHeadline.getText());
						contentValue.setContentText(newContent);

						if (hasFirstSaveDoneAndDoNotCreateNewContentVersion && !removeLock) {
							log.info("No new version");
							contentValue.setCreateNewVersion(false);
						} else {
							log.info("New version");
							contentValue.setCreateNewVersion(true);
						}
						comm.checkIn(contentValue);
						hasBeenSavedAndCanCheckinWithoutSave = true;
						log.debug("check-in success");
						if (!removeLock) {
							try {
								log.debug("try to remove lock");
								comm.checkOut(contentValue.getContentId().intValue(), false);
								log.debug("finished checkout");
							} catch (Exception exe) {
								log.error(exe);
							}
						}

						if (!hasFirstSaveDoneAndDoNotCreateNewContentVersion) {
							hasFirstSaveDoneAndDoNotCreateNewContentVersion = true;
						}

						if (Constants.SHOW_CONTENT_FROM_DROPDOWN) {
							Constants.SHOW_CONTENT_FROM_DROPDOWN = false;
						}
						dropdownEnabled = false;
						loadFromDropDown = false;
						updateContentVersions(new Integer(loadedViewComponentValue.getReference()).intValue(), false);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(UIConstants.getMainFrame(), ex.getMessage(), "CMS", JOptionPane.ERROR_MESSAGE);
					}
					//To make the XML editor refresh properly
					if (txtEditor != null) {
						txtEditor.setText(newContent);
						txtEditor.setCaretPosition(0);
					}
				}
				ActionHub.configureProperty(PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "true");
			}
			removeLock = false;
		}
		UIConstants.setActionStatus("");
		this.lock.unlock();
	}

	public void setEditorPane(JEditorPane txtEditor) {
		this.txtEditor = txtEditor;
	}

	public void load(ViewComponentValue viewComponentToLoad) {
		load(viewComponentToLoad, false);
	}

	public void load(ViewComponentValue viewComponentToLoad, boolean fromDropDown) {
		this.lock.lock();
		this.loadedViewComponentValue = viewComponentToLoad;
		synchronized (viewComponentToLoad) {
			ActionHub.registerProperty(PropertyActionEvent.TYPE_ITEM, PROP_CHECKOUT, null, "Check Out", rb.getString("actions.ACTION_CHECKOUT"), Constants.ACTION_CHECKOUT);//$NON-NLS-2$
			ActionHub.registerProperty(PropertyActionEvent.TYPE_ITEM, PROP_CHECKIN, null, "Check In", rb.getString("actions.ACTION_CHECKIN"), Constants.ACTION_CHECKIN);//$NON-NLS-2$
			ActionHub.configureProperty(PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "false");
			ActionHub.configureProperty(PROP_CHECKOUT, PropertyConfigurationEvent.PROP_ENABLE, "false");
			loadFromDropDown = fromDropDown;

			log.info("Loading " + viewComponentToLoad.getDisplayLinkName());
			UIConstants.setActionStatus(rb.getString("statusinfo.content"));
			this.hasBeenSavedAndCanCheckinWithoutSave = false;
			this.hasFirstSaveDoneAndDoNotCreateNewContentVersion = false;
			try {
				if (!this.loadFromDropDown) {
					this.loadedViewComponentValue = viewComponentToLoad;
					// the actual loading...
					this.contentValue = comm.getContent(new Integer(viewComponentToLoad.getReference()).intValue());
					this.dropdownEnabled = false;
					updateContentVersions(new Integer(viewComponentToLoad.getReference()).intValue(), false);
				} else { // this.loadFromDropDown
					try {
						int cvId = ((ContentVersionValue) ((DropDownHolder) cboContentVersions.getSelectedItem()).getObject()).getContentVersionId();
						ContentVersionValue cvd = comm.getContentVersion(cvId);
						contentValue.setHeading(cvd.getHeading());
						contentValue.setContentText(cvd.getText());
					} catch (Exception exe) {
						JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.ViewComponentNotFound"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
						log.error("Tried to get another content from dropdown with no success. Selected:" + cboContentVersions.getSelectedItem() + " vc:" + loadedViewComponentValue.getViewComponentId());//$NON-NLS-2$
					}
				}

				if (txtEditor != null) {
					txtEditor.setText(contentValue.getContentText());
					txtEditor.setCaretPosition(0);
				}
				contentManager.createDCFInstance(contentValue, jScrollpane, viewComponentToLoad);
				txtHeadline.setText(contentValue.getHeading());
			} catch (Exception exe) {
				log.error("Error in LoadRunner", exe);
			} finally {
				UIConstants.setActionStatus("");
			}

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_CONTENT_FINISHED_LOADING));
				}
			});

			//If the content inside the stickypad is not empty, it should
			//open when we check out the page.
			if (FrmStickyPad.getInstance() != null) {
				if (!FrmStickyPad.getInstance().checkStickyPadEmpty()) {
					FrmStickyPad.getInstance().showStickyPad();
				} else {
					FrmStickyPad.getInstance().closeStickyPad();
				}
			}

			if (!loadFromDropDown) {
				if (isContentCheckedOutByMyself(contentValue)) {
					contentManager.setEnabled(true);
					txtHeadline.setEditable(true);
					isCheckedOut = true;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ENABLE_CHECKIN));
							ActionHub.configureProperty(PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "true");
							ActionHub.configureProperty(PROP_CHECKOUT, PropertyConfigurationEvent.PROP_ENABLE, "false");
						}
					});
				} else {
					contentManager.setEnabled(false);
					txtHeadline.setEditable(false);
					isCheckedOut = false;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ENABLE_CHECKOUT));
							ActionHub.configureProperty(PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "false");
							ActionHub.configureProperty(PROP_CHECKOUT, PropertyConfigurationEvent.PROP_ENABLE, "true");
						}
					});
				}
			}
		}
		this.lock.unlock();
	}

	/** 
	 * The unloading is done by a special {@link Thread} named "UnloadContent". 
	 * As the Event Dispatch Thread uses this class' lock in order to indicate that some work is still done on the content,
	 * the unloading of content needs to be done in a different thread that is forced to wait until the lock is released again.
	 * 
	 * @see de.juwimm.cms.gui.controls.UnloadablePanel#unload() */
	public void unload() {
		Thread unloadThread = new Thread("UnloadContent") {
			@Override
			public void run() {
				lock.lock();
				ActionHub.unregisterProperty("PanelContent.ACTION_CHECKIN");
				ActionHub.unregisterProperty("PanelContent.ACTION_CHECKOUT");
				FrmStickyPad.getInstance().closeStickyPad();
				contentManager.recycleActiveDcf();
				lock.unlock();
			}
		};
		unloadThread.setPriority(Thread.MIN_PRIORITY);
		unloadThread.start();
	}

	public void unloadAll() {
		this.unload();
		this.contentManager = null;
		this.contentManager = ((ContentManager) getBean(Beans.CONTENT_MANAGER));
	}

	public boolean getIsCheckedOut() {
		return this.isCheckedOut;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Constants.ACTION_CHECKOUT)) {
			try {
				contentValue = comm.checkOut(contentValue.getContentId().intValue(), false);
				ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ENABLE_CHECKIN));
				ActionHub.configureProperty(PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "true");
				ActionHub.configureProperty(PROP_CHECKOUT, PropertyConfigurationEvent.PROP_ENABLE, "false");
				contentManager.setEnabled(true);
				txtHeadline.setEditable(true);
				isCheckedOut = true;
			} catch (AlreadyCheckedOutException ae) {
				if (comm.isUserInRole(UserRights.UNIT_ADMIN)) {
					String lockOwner = ae.getMessage();
					if (lockOwner != null && lockOwner.indexOf(":") >= 0) {
						lockOwner = lockOwner.substring(lockOwner.indexOf(":") + 1);
					}
					int i = JOptionPane.showConfirmDialog(UIConstants.getMainFrame(), Messages.getString("panel.panelContent.checkedOutAndRootCheckinQuestion", lockOwner), rb.getString("dialog.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

					if (i == JOptionPane.YES_OPTION) {
						try {
							// Root can checkout already checked out pages with the "force".
							// Note: Also on serverside this is only allowed to roots and admins!
							contentValue = comm.checkOut(contentValue.getContentId().intValue(), true);
							ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ENABLE_CHECKIN));
							ActionHub.configureProperty(PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "true");
							ActionHub.configureProperty(PROP_CHECKOUT, PropertyConfigurationEvent.PROP_ENABLE, "false");
							contentManager.setEnabled(true);
							txtHeadline.setEditable(true);
						} catch (Exception exe) {
							log.error("Force checkout error", exe);
						}
					}
				} else {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), Messages.getString("panel.panelContent.checkedOut", ae.getMessage()), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				}
			} catch (UserException ex) {
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), ex.getMessage(), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getActionCommand().equals(Constants.ACTION_CHECKIN)) {
			Constants.EDIT_CONTENT = false;
			this.lock.lock();
			if (this.contentManager.isModuleValid()) {
				contentManager.setEnabled(false);
				txtHeadline.setEditable(false);
				isCheckedOut = false;
				isEdit = false;
				if (!hasBeenSavedAndCanCheckinWithoutSave) {
					log.info("Calling save");
					removeLock = true;
					save();
				} else {
					try {
						log.info("Check-in");
						String newContent = this.contentManager.getContent(txtHeadline.getText());
						contentValue.setHeading(txtHeadline.getText());
						contentValue.setContentText(newContent);
						contentValue.setCreateNewVersion(false);
						comm.checkIn(contentValue);
					} catch (Exception exe) {
					}
					dropdownEnabled = false;
					updateContentVersions(new Integer(loadedViewComponentValue.getReference()).intValue(), false);
				}
				// if save did not succeed for example validation error for example headline required
				if (!hasBeenSavedAndCanCheckinWithoutSave) {
					contentManager.setEnabled(true);
					txtHeadline.setEditable(true);
					isCheckedOut = true;
					isEdit = true;
					Constants.EDIT_CONTENT = true;
				} else {
					ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ENABLE_CHECKOUT));
					ActionHub.configureProperty(PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "false");
					ActionHub.configureProperty(PROP_CHECKOUT, PropertyConfigurationEvent.PROP_ENABLE, "true");
				}
			}
			this.lock.unlock();
		} else if (e.getActionCommand().equals(Constants.ACTION_CONTENT_SELECT) || e.getActionCommand().equals(Constants.ACTION_CONTENT_EDITED)) {
			ActionHub.configureProperty(PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "false");
			ActionHub.configureProperty(PROP_CHECKOUT, PropertyConfigurationEvent.PROP_ENABLE, "false");
		} else if (e.getActionCommand().equals(Constants.ACTION_CONTENT_DESELECT)) {
			ActionHub.configureProperty(PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "false");
			ActionHub.configureProperty(PROP_CHECKOUT, PropertyConfigurationEvent.PROP_ENABLE, "false");
		} else if (e.getActionCommand().equals(ACTION_CONTENT_VERSION_SELECTED)) {
			if (log.isDebugEnabled()) log.debug("ACTION_CONTENT_VERSION_SELECTED");
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			System.err.println("Waiting cursor in actionPerformed(ActionEvent ACTION_CONTENT_VERSION_SELECTED");
			try {
				if (((DropDownHolder) cboContentVersions.getSelectedItem()).toString().startsWith(rb.getString("panel.panelContent.actualVersion"))) {
					btnDeleteSelectedContentVersion.setEnabled(false);
				} else {
					btnDeleteSelectedContentVersion.setEnabled(true);
				}
				if (dropdownEnabled) {
					load(loadedViewComponentValue, true);
				}
			} catch (Exception exe) {
			} finally {
				setCursor(Cursor.getDefaultCursor());
				System.err.println("Reset waiting cursor in actionPerformed(ActionEvent ACTION_CONTENT_VERSION_SELECTED");
				if (getIsCheckedOut()) {
					ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ENABLE_CHECKIN));
					ActionHub.configureProperty(PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "true");
					ActionHub.configureProperty(PROP_CHECKOUT, PropertyConfigurationEvent.PROP_ENABLE, "false");
				} else {
					ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ENABLE_CHECKOUT));
					ActionHub.configureProperty(PROP_CHECKIN, PropertyConfigurationEvent.PROP_ENABLE, "false");
					ActionHub.configureProperty(PROP_CHECKOUT, PropertyConfigurationEvent.PROP_ENABLE, "true");
				}
			}
		} else if (e.getActionCommand().equals(Constants.ACTION_NEW_TASK_FOR_USER)) {
			if (!isEdit && !getIsCheckedOut() && e.getID() == 1) { // we save here in the ID, if it should switch
				ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_SHOW_TASK));
			}
		}
	}

	public void setContent(String strContent) {
		try {
			GetContentHandler.validateContent(strContent);
			contentValue.setContentText(strContent);
			comm.saveContent(contentValue.getContentId().intValue(), strContent);
		} catch (Exception exe) {
			log.error("Content not valid error", exe);
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.ContentNotValid"), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean exitPerformed(ExitEvent e) {
		return true;
	}

	public String getTemplate() {
		return contentValue.getTemplate();
	}

	void btnDeleteSelectedContentVersionActionPerformed(ActionEvent e) {
		ContentVersionValue dao = (ContentVersionValue) ((DropDownHolder) this.cboContentVersions.getSelectedItem()).getObject();
		comm.removeContentVersion(dao.getContentVersionId());
		this.dropdownEnabled = false;
		// This is necessary to get the current content viewed inside the DCF after refreshing all Items in the DropDown
		updateContentVersions(new Integer(loadedViewComponentValue.getReference()).intValue(), true);
	}

	void btnDeleteAllOldContentVersionsActionPerformed(ActionEvent e) {
		comm.removeAllOldContentVersions(new Integer(loadedViewComponentValue.getReference()).intValue());
		this.dropdownEnabled = false;
		updateContentVersions(new Integer(loadedViewComponentValue.getReference()).intValue(), true);
	}

	private void updateContentVersions(int contentId, boolean selectLastItem) {
		if (log.isDebugEnabled()) log.debug("Update content versions");
		cboContentVersions.removeActionListener(this);
		cboContentVersions.removeAllItems();

		ContentVersionValue[] cvdarr = comm.getAllContentVersionsForContent(contentId);
		DropDownHolder ddh = null;
		for (int i = 0; i < cvdarr.length; i++) {
			/*
			String view = cvdarr[i].getVersionComment();
			if (cvdarr.length - 1 == i) {
				view = rb.getString("panel.panelContent.actualVersion") + " - " + view;
			}
			ddh = new DropDownHolder(cvdarr[i], view);
			*/
			ddh = new DropDownHolder(cvdarr[i], this.getDisplayName(cvdarr[i], cvdarr.length - 1 == i));
			cboContentVersions.addItem(ddh);
			cboContentVersions.setSelectedItem(ddh);
			btnDeleteSelectedContentVersion.setEnabled(false);
		}
		cboContentVersions.addActionListener(this);
		dropdownEnabled = true;
		if (selectLastItem && ddh != null) cboContentVersions.setSelectedItem(ddh);
	}

	private String getDisplayName(ContentVersionValue value, boolean newest) {
		StringBuffer sb = new StringBuffer();
		if (newest) {
			sb.append(rb.getString("panel.panelContent.actualVersion"));
		} else {
			sb.append(value.getVersion());
		}
		sb.append(" - ").append(value.getCreator()).append(" (").append(this.sdf.format(new Date(value.getCreateDate()))).append(")");

		return sb.toString();
	}

	void txtHeadlineKeyTyped(KeyEvent e) {
		isEdit = true;
	}

	/**
	 * Returns {@code true} if the content is locked for editing for the current user.
	 * 
	 * @param contentValue the content to check
	 * @return a boolean indicating if the content was checked out by the current user 
	 */
	private boolean isContentCheckedOutByMyself(ContentValue contentValue) {
		return (contentValue.getLock() != null && contentValue.getLock().getOwner().equals(comm.getUser().getUserName()));
	}

	/**
	 * {@link PlainDocument} implementation that allows insertion only for strings with a length up to 255 characters.
	 */
	private final class HeadLineDocument extends PlainDocument {
		private static final long serialVersionUID = 1640019981148695451L;
		private final int max = 255;

		@Override
		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null) {
				return;
			}
			if ((getLength() + str.length()) <= this.max) {
				super.insertString(offset, str, attr);
			}
		}
	}
}