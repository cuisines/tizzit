package de.juwimm.cms.gui;

import static de.juwimm.cms.common.Constants.rb;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.jvnet.flamingo.common.AbstractCommandButton;
import org.jvnet.flamingo.common.CommandButtonDisplayState;
import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.JCommandMenuButton;
import org.jvnet.flamingo.common.RichTooltip;
import org.jvnet.flamingo.common.icon.EmptyResizableIcon;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;
import org.jvnet.flamingo.common.model.ActionToggleButtonModel;
import org.jvnet.flamingo.common.popup.JCommandPopupMenu;
import org.jvnet.flamingo.common.popup.JPopupPanel;
import org.jvnet.flamingo.common.popup.PopupPanelCallback;
import org.jvnet.flamingo.ribbon.JRibbonBand;
import org.jvnet.flamingo.ribbon.RibbonElementPriority;
import org.jvnet.flamingo.ribbon.RibbonTask;

import com.Ostermiller.util.Browser;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.gui.event.ViewComponentEvent;
import de.juwimm.cms.gui.ribbon.CommandButton;
import de.juwimm.cms.gui.ribbon.CommandMenuButton;
import de.juwimm.cms.gui.ribbon.Ribbon;
import de.juwimm.cms.gui.ribbon.RibbonBand;
import de.juwimm.cms.gui.tree.PageNode;
import de.juwimm.cms.gui.tree.TreeNode;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.util.UserConfig;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class PanRibbon extends Ribbon implements ActionListener {
	private static final long serialVersionUID = -6958197452475472452L;
	private static Logger log = Logger.getLogger(PanRibbon.class);
	public static final String CMS_LANG_DE = "cmslanguagegerman";
	public static final String CMS_LANG_EN = "cmslanguageenglish";

	JCommandButton newContentButton;
	JCommandButton refreshTreeButton;
	private JCommandButton moveButton = null;
	private Communication comm;

	private JCommandButton deleteNodeButton;
	private JCommandButton releaseSiteButton;
	private JCommandButton reviseSiteButton;
	private JCommandButton deployButton;
	private JCommandButton checkInButton;
	private JCommandButton checkOutButton;
	private JCommandButton editViewButton;
	private JCommandButton taskViewButton;
	private JCommandButton adminViewButton;
	private JCommandButton languageButton;
	private JCommandButton helpButton;
	private JCommandButton exitButton;
	private JCommandButton logoutButton;
	private JCommandButton optionsButton;
	private JCommandButton directHelpButton;
	private JCommandButton infoButton;

	private JRibbonBand editBand;
	private JRibbonBand publishBand;
	private JRibbonBand viewSelectBand;
	private JRibbonBand optionsBand;
	private JRibbonBand exitBand;

	JCommandMenuButton newAfter;
	JCommandMenuButton newBefore;
	JCommandMenuButton newAppend;

	JCommandMenuButton symLinkAfter;
	JCommandMenuButton symLinkBefore;
	JCommandMenuButton symLinkAppend;

	JCommandMenuButton internAfter;
	JCommandMenuButton internBefore;
	JCommandMenuButton internAppend;

	JCommandMenuButton externAfter;
	JCommandMenuButton externBefore;
	JCommandMenuButton externAppend;

	JCommandMenuButton separatorAfter;
	JCommandMenuButton separatorBefore;
	JCommandMenuButton separatorAppend;

	JCommandMenuButton upItem;
	JCommandMenuButton downItem;
	JCommandMenuButton leftItem;
	JCommandMenuButton rightItem;

	public PanRibbon(Communication comm) {
		this.comm = comm;
		constructButtons();
		arrangeButtons();
		addListeners(comm);
	}

	/**
	 * 
	 */
	private void addListeners(ActionListener actionListener) {
		//add listeners
		logoutButton.addActionListener(actionListener);
		exitButton.addActionListener(actionListener);
		editViewButton.addActionListener(actionListener);
		taskViewButton.addActionListener(actionListener);
		adminViewButton.addActionListener(actionListener);
		deployButton.addActionListener(actionListener);
		releaseSiteButton.addActionListener(actionListener);
		reviseSiteButton.addActionListener(actionListener);
		//we don't want to show progressbar on showing options dialog
		optionsButton.addActionListener(this);
		infoButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				showVersion(e);
			}

		});

		checkInButton.addActionListener(actionListener);
		checkInButton.getActionModel().setActionCommand(Constants.ACTION_CHECKIN);

		checkOutButton.addActionListener(actionListener);
		checkOutButton.getActionModel().setActionCommand(Constants.ACTION_CHECKOUT);

		//add action commands
		logoutButton.getActionModel().setActionCommand(Constants.ACTION_LOGOFF);
		exitButton.getActionModel().setActionCommand(Constants.ACTION_EXIT);
		editViewButton.getActionModel().setActionCommand(Constants.ACTION_VIEW_EDITOR);
		taskViewButton.getActionModel().setActionCommand(Constants.ACTION_SHOW_TASK);
		deployButton.getActionModel().setActionCommand(Constants.ACTION_DEPLOY);
		releaseSiteButton.getActionModel().setActionCommand(Constants.ACTION_CONTENT_APPROVE);
		reviseSiteButton.getActionModel().setActionCommand(Constants.ACTION_CONTENT_CANCEL_APPROVAL);
		optionsButton.getActionModel().setActionCommand(Constants.ACTION_SHOW_OPTIONS);

		if (comm.isUserInRole(UserRights.SITE_ROOT)) {
			adminViewButton.getActionModel().setActionCommand(Constants.ACTION_VIEW_ROOT);
		} else {
			adminViewButton.getActionModel().setActionCommand(Constants.ACTION_VIEW_ADMIN);
		}

		helpButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				helpActionPerformed(e);
			}
		});

		deleteNodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.DELETE));
			}
		});

		refreshTreeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionHub.fireActionPerformed(new ActionEvent(refreshTreeButton, ActionEvent.ACTION_PERFORMED, Constants.ACTION_TREE_REFRESH));
			}
		});

	}

	private void addButton(JCommandButton component, int index, JRibbonBand band) {
		GridBagConstraints compnentConstraints = new GridBagConstraints();
		compnentConstraints.gridx = index;
		compnentConstraints.gridy = 0;
		RichTooltip tooltip = new RichTooltip();
		tooltip.setTitle(band.getTitle());
		tooltip.addDescriptionSection(component.getText());
		component.setActionRichTooltip(tooltip);
		band.addCommandButton( component, RibbonElementPriority.TOP);
	}

	private void arrangeButtons() {
		editBand.startGroup();
		this.addButton(newContentButton, 0, editBand);
		this.addButton(moveButton, 1, editBand);
		this.addButton(refreshTreeButton, 2, editBand);
		this.addButton(deleteNodeButton, 3, editBand);
		editBand.startGroup();
		this.addButton(checkOutButton, 5, editBand);
		this.addButton(checkInButton, 6, editBand);

		this.addButton(reviseSiteButton, 1, publishBand);
		this.addButton(releaseSiteButton, 2, publishBand);
		this.addButton(deployButton, 3, publishBand);

		this.addButton(editViewButton, 0, viewSelectBand);
		this.addButton(taskViewButton, 1, viewSelectBand);
		this.addButton(adminViewButton, 2, viewSelectBand);

		this.addButton(languageButton, 0, optionsBand);
		this.addButton(optionsButton, 1, optionsBand);
		this.addButton(helpButton, 2, optionsBand);
		this.addButton(directHelpButton, 3, optionsBand);
		this.addButton(infoButton, 4, optionsBand);

		this.addButton(logoutButton, 3, exitBand);
		this.addButton(exitButton, 4, exitBand);

		initViewSelectionGroup();

		//is not visible thanks to UI implementation
		RibbonTask task = new RibbonTask("Tizzit", editBand, publishBand, viewSelectBand, optionsBand, exitBand);
		this.addTask(task);

	}

	private void initViewSelectionGroup() {
		editViewButton.setActionModel(new ActionToggleButtonModel(false));
		editViewButton.getActionModel().setSelected(true);
		editViewButton.getActionModel().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				taskViewButton.getActionModel().setSelected(false);
				editViewButton.getActionModel().setSelected(true);
				adminViewButton.getActionModel().setSelected(false);
			}

		});

		if (adminViewButton.isEnabled()) {
			adminViewButton.setActionModel(new ActionToggleButtonModel(false));
			adminViewButton.getActionModel().addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					taskViewButton.getActionModel().setSelected(false);
					editViewButton.getActionModel().setSelected(false);
					adminViewButton.getActionModel().setSelected(true);
					System.err.println("admin " + adminViewButton.isEnabled());

				}

			});
		}

		taskViewButton.setActionModel(new ActionToggleButtonModel(false));
		taskViewButton.getActionModel().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				taskViewButton.getActionModel().setSelected(true);
				editViewButton.getActionModel().setSelected(false);
				adminViewButton.getActionModel().setSelected(false);

			}

		});
	}

	private void initButtons() {
		newContentButton = createButton(Constants.rb.getString("ribbon.new"), UIConstants.RIBBON_NEW);
		refreshTreeButton = createButton(Constants.rb.getString("ribbon.ACTION_TREE_REFRESH"), UIConstants.RIBBON_ACTION_TREE_REFRESH);
		deleteNodeButton = createButton(Constants.rb.getString("dialog.delete"), UIConstants.RIBBON_TREE_NODE_DELETE);
		releaseSiteButton = createButton(Constants.rb.getString("menubar.publish.release"), UIConstants.RIBBON_RELEASE_SITE);
		reviseSiteButton = createButton(Constants.rb.getString("menubar.publish.revise"), UIConstants.RIBBON_REVISE_SITE);
		deployButton = createButton(Constants.rb.getString("actions.ACTION_DEPLOY"), UIConstants.RIBBON_ACTION_DEPLOY);
		checkInButton = createButton(Constants.rb.getString("actions.ACTION_CHECKIN"), UIConstants.RIBBON_ACTION_CHECKIN);
		checkOutButton = createButton(Constants.rb.getString("actions.ACTION_CHECKOUT"), UIConstants.RIBBON_ACTION_CHECKOUT);
		languageButton = createButton(Constants.rb.getString("ribbon.language"), UIConstants.RIBBON_LANGUAGE);
		editViewButton = createButton(Constants.rb.getString("menubar.view.editor"), UIConstants.RIBBON_EDIT_VIEW);
		taskViewButton = createButton(Constants.rb.getString("menubar.view.task"), UIConstants.RIBBON_TASK_VIEW);
		adminViewButton = createButton(Constants.rb.getString("menubar.view.admin"), UIConstants.RIBBON_ADMIN_VIEW);
		helpButton = createButton(Constants.rb.getString("menubar.questionMark.help"), UIConstants.RIBBON_HELP);
		directHelpButton = createButton(Constants.rb.getString("menubar.questionMark.contextHelp"), UIConstants.RIBBON_HELP);
		infoButton = createButton(Constants.rb.getString("menubar.questionMark.about"), UIConstants.RIBBON_INFO);

		logoutButton = createButton(Constants.rb.getString("menubar.file.logoff"), UIConstants.RIBBON_LOGOUT);
		exitButton = createButton(Constants.rb.getString("menubar.file.quit"), UIConstants.RIBBON_EXIT);
		optionsButton = createButton(Constants.rb.getString("menubar.extras.options"), UIConstants.RIBBON_OPTIONS);
		moveButton = createButton(Constants.rb.getString("menubar.file.move"), UIConstants.RIBBON_MOVE);

		
		
	}		

	private JCommandButton createButton(String text, ImageIcon img) {
		return new CommandButton(text, ImageWrapperResizableIcon.getIcon(img.getImage(), new Dimension(img.getIconWidth(), img.getIconHeight())));

	}

	private void constructButtons() {
		initButtons();
		checkInButton.setEnabled(false);
		checkOutButton.setEnabled(false);
		adminViewButton.setEnabled(false);

		if ((comm.isUserInRole(UserRights.SITE_ROOT)) || (comm.isUserInRole(UserRights.UNIT_ADMIN))) {
			adminViewButton.setEnabled(true);
		}
		editBand = new RibbonBand(Constants.rb.getString("ribbonBand.edit"), new EmptyResizableIcon(0));
		publishBand = new RibbonBand(Constants.rb.getString("ribbonBand.publish"), new EmptyResizableIcon(0));
		viewSelectBand = new RibbonBand(Constants.rb.getString("ribbonBand.view"), new EmptyResizableIcon(0));
		optionsBand = new RibbonBand(Constants.rb.getString("ribbonBand.options"), new EmptyResizableIcon(0));
		exitBand = new RibbonBand(Constants.rb.getString("ribbonBand.exit"), new EmptyResizableIcon(0));

		newAfter = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_NODE_AFTER"), UIConstants.RIBBON_ACTION_TREE_NODE_AFTER);
		newBefore = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_NODE_BEFORE"), UIConstants.RIBBON_ACTION_TREE_NODE_BEFORE);
		newAppend = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_NODE_APPEND"), UIConstants.RIBBON_ACTION_TREE_NODE_APPEND);

		symLinkAfter = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_SYMLINK_AFTER"), UIConstants.RIBBON_ACTION_TREE_NODE_AFTER);
		symLinkBefore = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_SYMLINK_BEFORE"), UIConstants.RIBBON_ACTION_TREE_NODE_BEFORE);
		symLinkAppend = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_SYMLINK_ADD"), UIConstants.RIBBON_ACTION_TREE_NODE_APPEND);

		internAfter = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_JUMP_AFTER"), UIConstants.RIBBON_ACTION_TREE_NODE_AFTER);
		internBefore = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_JUMP_BEFORE"), UIConstants.RIBBON_ACTION_TREE_NODE_BEFORE);
		internAppend = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_JUMP_ADD"), UIConstants.RIBBON_ACTION_TREE_NODE_APPEND);

		externAfter = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_LINK_AFTER"), UIConstants.RIBBON_ACTION_TREE_NODE_AFTER);
		externBefore = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_LINK_BEFORE"), UIConstants.RIBBON_ACTION_TREE_NODE_BEFORE);
		externAppend = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_LINK_ADD"), UIConstants.RIBBON_ACTION_TREE_NODE_APPEND);

		separatorAfter = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_SEPARATOR_AFTER"), UIConstants.RIBBON_ACTION_TREE_NODE_AFTER);
		separatorBefore = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_SEPARATOR_BEFORE"), UIConstants.RIBBON_ACTION_TREE_NODE_BEFORE);
		separatorAppend = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.ACTION_TREE_SEPARATOR_ADD"), UIConstants.RIBBON_ACTION_TREE_NODE_APPEND);

		newContentButton.setPopupCallback(new ButtonPopupPanelCallback() {

			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				if (loaded == false) {
					addNewHeader(Constants.rb.getString("actions.TOOLBAR_CONTENT"), UIConstants.RIBBON_CONTENT, 0);
					addSubItem(newAfter, 1);
					addSubItem(newBefore, 2);
					addSubItem(newAppend, 3);
					addNewHeader(Constants.rb.getString("actions.TOOLBAR_SYMLINK"), UIConstants.RIBBON_TOOLBAR_SYMLINK, 4);
					addSubItem(symLinkAfter, 5);
					addSubItem(symLinkBefore, 6);
					addSubItem(symLinkAppend, 7);
					addNewHeader(Constants.rb.getString("actions.TOOLBAR_JUMP"), UIConstants.RIBBON_TOOLBAR_JUMP, 8);
					addSubItem(internAfter, 9);
					addSubItem(internBefore, 10);
					addSubItem(internAppend, 11);
					addNewHeader(Constants.rb.getString("actions.TOOLBAR_LINK"), UIConstants.RIBBON_TOOLBAR_LINK, 12);
					addSubItem(externAfter, 13);
					addSubItem(externBefore, 14);
					addSubItem(externAppend, 15);
					addNewHeader(Constants.rb.getString("actions.TOOLBAR_SEPARATOR"), UIConstants.RIBBON_TOOLBAR_SEPARATOR, 16);
					addSubItem(separatorAfter, 17);
					addSubItem(separatorBefore, 18);
					addSubItem(separatorAppend, 19);
					this.addListeners();
				}
				return super.getPopupPanel(commandButton);
			}

			private void addListeners() {
				addListener(newAppend, comm, Constants.ACTION_TREE_NODE_APPEND);
				addListener(newBefore, comm, Constants.ACTION_TREE_NODE_BEFORE);
				addListener(newAfter, comm, Constants.ACTION_TREE_NODE_AFTER);

				addListener(symLinkAppend, comm, Constants.ACTION_TREE_SYMLINK_ADD);
				addListener(symLinkBefore, comm, Constants.ACTION_TREE_SYMLINK_BEFORE);
				addListener(symLinkAfter, comm, Constants.ACTION_TREE_SYMLINK_AFTER);

				addListener(internAfter, comm, Constants.ACTION_TREE_JUMP_AFTER);
				addListener(internBefore, comm, Constants.ACTION_TREE_JUMP_BEFORE);
				addListener(internAppend, comm, Constants.ACTION_TREE_JUMP_ADD);

				addListener(externAfter, comm, Constants.ACTION_TREE_LINK_AFTER);
				addListener(externBefore, comm, Constants.ACTION_TREE_LINK_BEFORE);
				addListener(externAppend, comm, Constants.ACTION_TREE_LINK_ADD);

				addListener(separatorAfter, comm, Constants.ACTION_TREE_SEPARATOR_AFTER);
				addListener(separatorBefore, comm, Constants.ACTION_TREE_SEPARATOR_BEFORE);
				addListener(separatorAppend, comm, Constants.ACTION_TREE_SEPARATOR_ADD);
			}

		});
		languageButton.setPopupCallback(new ButtonPopupPanelCallback() {
			JCommandMenuButton deItem;
			JCommandMenuButton enItem;

			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				if (loaded == false) {
					deItem = getSimpleItem("de");
					enItem = getSimpleItem("en");
					addItem(deItem, 0);
					addItem(enItem, 1);
					addListeners();

				}
				return super.getPopupPanel(commandButton);
			}

			private void addListeners() {
				addListener(deItem, comm, CMS_LANG_DE);
				addListener(enItem, comm, CMS_LANG_EN);
			}
		});

		upItem = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.MOVE_UP"), UIConstants.RIBBON_UP);
		downItem = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.MOVE_DOWN"), UIConstants.RIBBON_DOWN);
		leftItem = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.MOVE_LEFT"), UIConstants.RIBBON_LEFT);
		rightItem = ButtonPopupPanelCallback.getImageItem(Constants.rb.getString("actions.MOVE_RIGHT"), UIConstants.RIBBON_RIGHT);
		moveButton.setPopupCallback(new ButtonPopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				if (loaded == false) {
					addItem(upItem, 0);
					addItem(downItem, 1);
					addItem(leftItem, 2);
					addItem(rightItem, 3);
					this.addListeners();
				}
				return super.getPopupPanel(commandButton);
			}

			private void addListeners() {
				leftItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setMoveButtonsEnabled(false);
						ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.MOVE_LEFT));
					}
				});

				rightItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setMoveButtonsEnabled(false);
						ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.MOVE_RIGHT));
					}
				});

				upItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setMoveButtonsEnabled(false);
						ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.MOVE_UP));
					}
				});

				downItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setMoveButtonsEnabled(false);
						ActionHub.fireViewComponentPerformed(new ViewComponentEvent(ViewComponentEvent.MOVE_DOWN));
					}
				});

			}
		});
		moveButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		newContentButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		languageButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);

	}

	static class ButtonPopupPanelCallback implements PopupPanelCallback {
		JCommandPopupMenu contentPopup = null;
		Boolean loaded = false;

		public ButtonPopupPanelCallback() {
			contentPopup = new JCommandPopupMenu();
			contentPopup.setLayout(new GridBagLayout());
		}

		public JPopupPanel getPopupPanel(JCommandButton commandButton) {
			loaded = true;
			return contentPopup;
		}

		protected void addNewHeader(String text, Icon icon, int index) {
			GridBagConstraints headerConstraints = new GridBagConstraints(0, index, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
			JLabel header = new JLabel();
			header.setText(text);
			header.setIcon(icon);
			header.setIconTextGap(8);
			header.setForeground(new Color(0x757575));
			contentPopup.add(header, headerConstraints);
		}

		public static JCommandMenuButton getImageItem(String text, ImageIcon icon) {
			JCommandMenuButton item = new CommandMenuButton(text, ImageWrapperResizableIcon.getIcon(icon.getImage(), new Dimension(16, 16)));
			item.setDisplayState(CommandButtonDisplayState.MEDIUM);
			item.setHorizontalAlignment(SwingUtilities.LEFT);
			return item;
		}

		protected JCommandMenuButton getSimpleItem(String text) {
			JCommandMenuButton item = new CommandMenuButton(text, new EmptyResizableIcon(new Dimension(0, 0)));
			item.setDisplayState(CommandButtonDisplayState.MEDIUM);
			item.setHorizontalAlignment(SwingUtilities.LEFT);
			return item;
		}

		protected void addSubItem(JCommandMenuButton item, int index) {
			GridBagConstraints itemConstraints = new GridBagConstraints(0, index, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 25, 0, 5), 0, 0);
			contentPopup.add(item, itemConstraints);
		}

		protected void addItem(JCommandMenuButton item, int index) {
			GridBagConstraints itemConstraints = new GridBagConstraints(0, index, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
			contentPopup.add(item, itemConstraints);
		}

		protected void addListener(JCommandMenuButton item, ActionListener listener, String command) {
			item.addActionListener(listener);
			item.getActionModel().setActionCommand(command);
		}
	}

	void helpActionPerformed(ActionEvent e) {
		try {
			this.getParent().setCursor(new Cursor(Cursor.WAIT_CURSOR));
			Browser.displayURL(Constants.CMS_PATH_HELP);

		} catch (IOException exe) {
			log.error("HELPError", exe);
		} finally {
			this.getParent().setCursor(Cursor.getDefaultCursor());
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Constants.ACTION_TREE_DESELECT)) {
			deleteNodeButton.setEnabled(false);
			newBefore.setEnabled(false);
			newAfter.setEnabled(false);
			newAppend.setEnabled(false);

			internBefore.setEnabled(false);
			internAfter.setEnabled(false);
			internAppend.setEnabled(false);

			separatorBefore.setEnabled(false);
			separatorAfter.setEnabled(false);
			separatorAppend.setEnabled(false);

			externBefore.setEnabled(false);
			externAfter.setEnabled(false);
			externAppend.setEnabled(false);

			symLinkBefore.setEnabled(false);
			symLinkAfter.setEnabled(false);
			symLinkAppend.setEnabled(false);

			checkInButton.setEnabled(false);
			checkOutButton.setEnabled(false);
		} else if (e.getActionCommand().equals(Constants.ACTION_TREE_SELECT) || e.getActionCommand().equals(Constants.ACTION_DEPLOY_STATUS_CHANGED)) {
			TreeNode entry = (TreeNode) e.getSource();
			updateRibbonButtonsOnSelect(entry);
		} else if (e.getActionCommand().equals(Constants.ENABLE_CHECKIN)) {
			checkInButton.setEnabled(true);
			checkOutButton.setEnabled(false);
			//TODO find these buttons
			//mnuPublishLetRelease.setEnabled(false);
			releaseSiteButton.setEnabled(false);
			reviseSiteButton.setEnabled(false);
		} else if (e.getActionCommand().equals(Constants.ENABLE_CHECKOUT)) {
			checkInButton.setEnabled(false);
			checkOutButton.setEnabled(true);
			updateRibbonButtonsOnSelect(PanTree.getSelectedEntry());
		} else if (e.getActionCommand().equals(Constants.ACTION_CONTENT_SELECT) || e.getActionCommand().equals(Constants.ACTION_CONTENT_EDITED)) {
			checkInButton.setEnabled(false);
			checkOutButton.setEnabled(false);
		} else if (e.getActionCommand().equals(Constants.ACTION_CONTENT_DESELECT)) {
			checkInButton.setEnabled(false);
			checkOutButton.setEnabled(false);
		} else if (e.getActionCommand().equals(Constants.ACTION_SHOW_TASK)) {
			setEnableEditPublishButtons(false);
			//either is in edit view or task or admin it the buttons init with disabled
			checkInButton.setEnabled(false);
			checkOutButton.setEnabled(false);
		} else if (e.getActionCommand().equals(CMS_LANG_DE)) {
			try {
				Constants.CMS_LOCALE = Locale.GERMAN;
				Constants.CMS_LANGUAGE = Constants.CMS_LOCALE.getLanguage();
				Constants.rb = ResourceBundle.getBundle("de.juwimm.cms.CMS", Constants.CMS_LOCALE);
				UserConfig.getInstance().setConfigNodeValue(UserConfig.USERCONF_PREFERRED_LANGUAGE, Constants.CMS_LOCALE.toString());
				UserConfig.getInstance().saveChanges();
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.firstRestartApp"), rb.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception exe) {
				if (log.isDebugEnabled()) {
					log.debug(exe.getMessage());
				}
			}
		} else if (e.getActionCommand().equals(CMS_LANG_EN)) {
			try {
				Constants.CMS_LOCALE = Locale.ENGLISH;
				Constants.CMS_LANGUAGE = Constants.CMS_LOCALE.getLanguage();
				Constants.rb = ResourceBundle.getBundle("de.juwimm.cms.CMS", Constants.CMS_LOCALE);
				UserConfig.getInstance().setConfigNodeValue(UserConfig.USERCONF_PREFERRED_LANGUAGE, Constants.CMS_LOCALE.toString());
				UserConfig.getInstance().saveChanges();
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.firstRestartApp"), rb.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception exe) {
				if (log.isDebugEnabled()) {
					log.debug(exe.getMessage());
				}
			}
		} else if (e.getActionCommand().equals(Constants.ACTION_SHOW_OPTIONS)) {
			OptionsDialog optionsDialog = new OptionsDialog(comm);
			optionsDialog.setVisible(true);
		}
	}

	private void setMoveButtonsEnabled(boolean enabled) {
		moveButton.setEnabled(enabled);
		upItem.setEnabled(enabled);
		rightItem.setEnabled(enabled);
		upItem.setEnabled(enabled);
		downItem.setEnabled(enabled);
	}

	public void setView(boolean showContent) {
		if (showContent) {
			showEditor();
		} else {
			setEnableEditPublishButtons(false);
			//either is in edit view or task or admin it the buttons init with disabled
			checkInButton.setEnabled(false);
			checkOutButton.setEnabled(false);
		}
		this.validate();
		this.repaint();
	}

	private void showEditor() {
		setEnableEditPublishButtons(true);
		newBefore.setEnabled(false);
		newAfter.setEnabled(false);
		newAppend.setEnabled(false);
		internBefore.setEnabled(false);
		internAfter.setEnabled(false);
		internAppend.setEnabled(false);
		externBefore.setEnabled(false);
		externAfter.setEnabled(false);
		externAppend.setEnabled(false);
		symLinkBefore.setEnabled(false);
		symLinkAfter.setEnabled(false);
		symLinkAppend.setEnabled(false);
		separatorBefore.setEnabled(false);
		separatorAfter.setEnabled(false);
		separatorAppend.setEnabled(false);

		leftItem.setEnabled(false);
		upItem.setEnabled(false);
		downItem.setEnabled(false);
		rightItem.setEnabled(false);

		deleteNodeButton.setEnabled(false);
		refreshTreeButton.setEnabled(true);
		deployButton.setEnabled(true);
	}

	private void updateRibbonButtonsOnSelect(TreeNode currentEntry) {
		if (currentEntry == null) return;
		setEnableEditPublishButtons(true);
		leftItem.setEnabled(currentEntry.isMoveableToLeft());
		upItem.setEnabled(currentEntry.isMoveableToUp());
		downItem.setEnabled(currentEntry.isMoveableToDown());
		rightItem.setEnabled(currentEntry.isMoveableToRight());

		deleteNodeButton.setEnabled(currentEntry.isDeleteable());

		boolean append = currentEntry.isAppendingAllowed();
		newAppend.setEnabled(append);
		internAppend.setEnabled(append);
		separatorAppend.setEnabled(append);
		externAppend.setEnabled(append);
		symLinkAppend.setEnabled(append);

		boolean afterBefore = !currentEntry.isRoot() && currentEntry instanceof PageNode;

		newBefore.setEnabled(afterBefore);
		newAfter.setEnabled(afterBefore);
		internBefore.setEnabled(afterBefore);
		internAfter.setEnabled(afterBefore);
		separatorBefore.setEnabled(afterBefore);
		separatorAfter.setEnabled(afterBefore);
		externBefore.setEnabled(afterBefore);
		externAfter.setEnabled(afterBefore);
		symLinkBefore.setEnabled(afterBefore);
		symLinkAfter.setEnabled(afterBefore);
		//TODO find these buttons
		//mnuPublishLetRelease.setEnabled(false);
		releaseSiteButton.setEnabled(false);
		reviseSiteButton.setEnabled(false);

		if (currentEntry instanceof PageNode) {
			switch (((PageNode) currentEntry).getStatus()) {
				case Constants.DEPLOY_STATUS_EDITED:
					if (!comm.isUserInRole(UserRights.APPROVE)) {
						releaseSiteButton.setEnabled(false);
					} else {
						releaseSiteButton.setEnabled(true);
					}
					//					mnuPublishLetRelease.setEnabled(true);
					reviseSiteButton.setEnabled(false);
					break;
				case Constants.DEPLOY_STATUS_FOR_APPROVAL:
					//					mnuPublishLetRelease.setEnabled(false);
					reviseSiteButton.setEnabled(true);

					if (!comm.isUserInRole(UserRights.APPROVE)) {
						releaseSiteButton.setEnabled(false);
					} else {
						releaseSiteButton.setEnabled(true);
					}
					break;
				default:
					//					mnuPublishLetRelease.setEnabled(false);
					releaseSiteButton.setEnabled(false);
					reviseSiteButton.setEnabled(false);
					break;
			}
		}
	}

	private void setEnableEditPublishButtons(boolean state) {
		newContentButton.setEnabled(state);
		moveButton.setEnabled(state);
		refreshTreeButton.setEnabled(state);
		deleteNodeButton.setEnabled(state);
		releaseSiteButton.setEnabled(state);
		reviseSiteButton.setEnabled(state);
		deployButton.setEnabled(state);
		releaseSiteButton.setEnabled(state);
		//TODO
		//		mnuPublishLetRelease.setEnabled(false);

	}

	void showVersion(ActionEvent e) {
		FrmVersion dialog = new FrmVersion(Constants.CMS_VERSION);
		int height = 300;
		int width = 450;
		int midHeight = UIConstants.getMainFrame().getY() + (UIConstants.getMainFrame().getHeight() / 2);
		int midWidth = UIConstants.getMainFrame().getX() + (UIConstants.getMainFrame().getWidth() / 2);
		dialog.setSize(width, height);
		dialog.setLocation(midWidth - width / 2, midHeight - height / 2);
		dialog.setVisible(true);
	}
}
