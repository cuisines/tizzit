package de.juwimm.cms.gui;

import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.jvnet.flamingo.common.CommandButtonDisplayState;
import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.JCommandMenuButton;
import org.jvnet.flamingo.common.icon.EmptyResizableIcon;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;
import org.jvnet.flamingo.common.model.ActionToggleButtonModel;


import de.juwimm.cms.common.Constants;
import de.juwimm.cms.deploy.actions.ExportFullThread;
import de.juwimm.cms.deploy.actions.ImportFullThread;
import de.juwimm.cms.gui.ribbon.CommandButton;
import de.juwimm.cms.gui.ribbon.CommandMenuButton;
import de.juwimm.cms.util.ActionHub;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class OptionsDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1042287156390559221L;
	private static Logger log = Logger.getLogger(OptionsDialog.class);
	
	private JCommandButton generalOption;
	private JCommandButton importExportOption;
	private JCommandButton userInformationOption;
	private Communication comm;
	
	private JButton clearCacheButton;
	private JButton reloadDcfButton;
	private JButton importButton;
	private JButton exportButton;
	
	private JPanel ribbonBandContainer;
	private JPanel optionsContainer;
	
	private JPanel generalPanel;
	private JPanel importExportPanel;
	private JPanel userInformationPanel;
	
	public static final String CMS_CLEAR_CACHE = "cmsclearcachenow!";
	public static final String CMS_RELOAD_DCF = "cmsreloaddcf";
	
	public static final String CMS_EXPORT_ALL = "cmsexportallthefuck";
	public static final String CMS_IMPORT_ALL = "cmsimportallthisshittystuffassoonaspossibletogetitrunning";
		/*
	JMenuItem mnuExtrasExportAll = new JMenuItem("Export complete Site");
			mnuExtrasExportAll.setActionCommand(PanMenubar.CMS_EXPORT_ALL);
			mnuExtrasExportAll.addActionListener(this);
			mnuExtras.add(mnuExtrasExportAll);
			JMenuItem mnuExtrasImportAll = new JMenuItem("Import complete Site");
			mnuExtrasImportAll.setActionCommand(PanMenubar.CMS_IMPORT_ALL);
			mnuExtrasImportAll.addActionListener(this);
			mnuExtras.add(mnuExtrasImportAll);
		 */
	
	public OptionsDialog(final Communication comm){
		super(UIConstants.getMainFrame(), Constants.rb.getString("menubar.extras.options"), true);
		this.comm = comm;
		Dimension size = new Dimension(250,200);
		this.setSize(size);
		this.setLocationRelativeTo(UIConstants.getMainFrame());
		this.setMinimumSize(size);
		this.setResizable(false);
		
		generalOption = new CommandButton("General",ImageWrapperResizableIcon.getIcon(UIConstants.RIBBON_OPTIONS.getImage(), new Dimension(32,32)));
		importExportOption = new CommandButton("Import/Export",ImageWrapperResizableIcon.getIcon(UIConstants.RIBBON_OPTIONS.getImage(), new Dimension(32,32)));
		userInformationOption = new CommandButton("User options",ImageWrapperResizableIcon.getIcon(UIConstants.RIBBON_OPTIONS.getImage(), new Dimension(32,32)));
		
		initOptionsRibbonsListeners();
		ribbonBandContainer = new JPanel();
		optionsContainer = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();				
		ribbonBandContainer.setLayout(gridBagLayout);
		GridBagConstraints generalOptionConstraint = new GridBagConstraints();
		GridBagConstraints importExportOptionConstraint = new GridBagConstraints();
		GridBagConstraints userInformationOptionConstraint = new GridBagConstraints();
		generalOptionConstraint.insets = new Insets(4,4,4,4);
		importExportOptionConstraint.insets = new Insets(4,4,4,4);
		userInformationOptionConstraint.insets = new Insets(4,4,4,4);
		generalOptionConstraint.anchor = GridBagConstraints.WEST;
		importExportOptionConstraint.anchor = GridBagConstraints.WEST;
		userInformationOptionConstraint.anchor = GridBagConstraints.WEST;
		
		importExportOptionConstraint.gridx = 1;
		userInformationOptionConstraint.gridx = 2;
		ribbonBandContainer.setBorder(BorderFactory.createLineBorder(Color.gray));
		ribbonBandContainer.add(generalOption,generalOptionConstraint);
		ribbonBandContainer.add(importExportOption,importExportOptionConstraint);
		ribbonBandContainer.add(userInformationOption,userInformationOptionConstraint);
		
		getContentPane().setLayout(new BorderLayout());
		
		JCommandMenuButton item = new CommandMenuButton("Testing",new EmptyResizableIcon(new Dimension(0,0)));				
		item.setDisplayState(CommandButtonDisplayState.MEDIUM);
		item.setHorizontalAlignment(SwingUtilities.LEFT);		
		
		getContentPane().add(ribbonBandContainer,BorderLayout.NORTH);		
		getContentPane().add(optionsContainer,BorderLayout.CENTER);
		
		generalPanel = new JPanel(new BorderLayout());
		importExportPanel = new JPanel(new BorderLayout());
		userInformationPanel = new JPanel(new BorderLayout());
		
		clearCacheButton = new JButton(Constants.rb.getString("menubar.extras.clearcache"));
		reloadDcfButton = new JButton(Constants.rb.getString("menubar.extras.reloadDcf"));
		generalPanel.add(clearCacheButton,BorderLayout.CENTER);
		generalPanel.add(reloadDcfButton,BorderLayout.SOUTH);
		
		importButton = new JButton("Import complete Site");
		exportButton = new JButton("Export complete Site");
		importExportPanel.add(importButton,BorderLayout.CENTER);
		importExportPanel.add(exportButton,BorderLayout.SOUTH);
		userInformationPanel.add(new JLabel("User options:"),BorderLayout.NORTH);
		userInformationPanel.add(new JLabel(Constants.rb.getString("menubar.extras.changePassword")),BorderLayout.SOUTH);
		optionsContainer.add(generalPanel);
		optionsContainer.add(importExportPanel);
		optionsContainer.add(userInformationPanel);
		generalPanel.setVisible(true);
		importExportPanel.setVisible(false);
		userInformationPanel.setVisible(false);
		initListeners();
				
	}


	private void initListeners(){
		clearCacheButton.addActionListener(this);
		reloadDcfButton.addActionListener(this);
		importButton.addActionListener(this);
		exportButton.addActionListener(this);
		
		clearCacheButton.setActionCommand(CMS_CLEAR_CACHE);
		reloadDcfButton.setActionCommand(CMS_RELOAD_DCF);
		importButton.setActionCommand(CMS_IMPORT_ALL);
		exportButton.setActionCommand(CMS_EXPORT_ALL);
	}
	/**
	 * 
	 */
	private void initOptionsRibbonsListeners() {
		generalOption.setActionModel(new ActionToggleButtonModel(false));
		importExportOption.setActionModel(new ActionToggleButtonModel(false));
		userInformationOption.setActionModel(new ActionToggleButtonModel(false));
		generalOption.getActionModel().setSelected(true);
		generalOption.getActionModel().addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				importExportOption.getActionModel().setSelected(false);
				generalOption.getActionModel().setSelected(true);
				userInformationOption.getActionModel().setSelected(false);
				generalPanel.setVisible(true);
				importExportPanel.setVisible(false);
				userInformationPanel.setVisible(false);
			}
        	
        });
        
		importExportOption.getActionModel().addActionListener(new ActionListener(){

			
			public void actionPerformed(ActionEvent e) {
				importExportOption.getActionModel().setSelected(true);
				generalOption.getActionModel().setSelected(false);
				userInformationOption.getActionModel().setSelected(false);
				generalPanel.setVisible(false);
				importExportPanel.setVisible(true);
				userInformationPanel.setVisible(false);
			}
        	
        });
        
		userInformationOption.getActionModel().addActionListener(new ActionListener(){

			
			public void actionPerformed(ActionEvent e) {
				importExportOption.getActionModel().setSelected(false);
				generalOption.getActionModel().setSelected(false);
				userInformationOption.getActionModel().setSelected(true);
				generalPanel.setVisible(false);
				importExportPanel.setVisible(false);
				userInformationPanel.setVisible(true);
			}
        	
        });
	}


	public void actionPerformed(ActionEvent e) {		
		if (e.getActionCommand().equals(CMS_CLEAR_CACHE)) {
			comm.clearSvgCache();
			try {
				if (comm.getDbHelper().clearMyCache()) {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("panel.cmsMenubar.cacheCleared"), 
							rb.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.cantClearCache"), 
							rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception exe) {
				if (log.isDebugEnabled()) {
					log.debug(exe.getMessage());
				}
			}
		} else if (e.getActionCommand().equals(CMS_RELOAD_DCF)) {
			PanLogin.loadTemplates(false);
			ActionHub.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.ACTION_VIEW_EDITOR));
		}else if (e.getActionCommand().equals(CMS_EXPORT_ALL)) {
			new ExportFullThread().start();
			//EXPORT ALL
		} else if (e.getActionCommand().equals(CMS_IMPORT_ALL)) {
			new ImportFullThread().start();
			//IMPORT ALL
		}
	}
	
}
