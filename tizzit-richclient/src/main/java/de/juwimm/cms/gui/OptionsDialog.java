package de.juwimm.cms.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;


import de.juwimm.cms.common.Constants;
import de.juwimm.cms.gui.ribbon.CommandButton;
import de.juwimm.cms.util.UIConstants;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class OptionsDialog extends JDialog {

	private static final long serialVersionUID = 1042287156390559221L;
	JCommandButton generalOption;
	JCommandButton importExportOption;
	JCommandButton userInformationOption;
	
	JPanel ribbonBandContainer;
	JPanel optionsContainer;
	
	JPanel generalPanel;
	JPanel importExportPanel;
	JPanel userInformationPanel;
		
	
	public OptionsDialog(){
		super(UIConstants.getMainFrame(), Constants.rb.getString("menubar.extras.options"), true);
		generalOption = new CommandButton("General",ImageWrapperResizableIcon.getIcon(UIConstants.RIBBON_OPTIONS.getImage(), new Dimension(32,32)));
		importExportOption = new CommandButton("Import/Export",ImageWrapperResizableIcon.getIcon(UIConstants.RIBBON_OPTIONS.getImage(), new Dimension(32,32)));
		userInformationOption = new CommandButton("User options",ImageWrapperResizableIcon.getIcon(UIConstants.RIBBON_OPTIONS.getImage(), new Dimension(32,32)));
		ribbonBandContainer = new JPanel();
		optionsContainer = new JPanel();
		
		ribbonBandContainer.setBorder(BorderFactory.createLineBorder(Color.gray));
		ribbonBandContainer.add(generalOption);
		ribbonBandContainer.add(importExportOption);
		ribbonBandContainer.add(userInformationOption);
		
		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints ribbonBandConstraints = new GridBagConstraints();
		GridBagConstraints optionsConstraints = new GridBagConstraints();
		optionsConstraints.gridy = 1;
		
		
		getContentPane().add(ribbonBandContainer,ribbonBandConstraints);		
		getContentPane().add(optionsContainer,optionsConstraints);
		
		generalPanel = new JPanel(new BorderLayout());
		importExportPanel = new JPanel(new BorderLayout());
		userInformationPanel = new JPanel(new BorderLayout());
		
		generalPanel.add(new JLabel("General options:"),BorderLayout.NORTH);
		generalPanel.add(new JLabel(Constants.rb.getString("menubar.extras.clearcache")),BorderLayout.CENTER);
		generalPanel.add(new JLabel(Constants.rb.getString("menubar.extras.reloadDcf")),BorderLayout.SOUTH);
		
		
		importExportPanel.add(new JLabel("Import/export options:"),BorderLayout.NORTH);
		importExportPanel.add(new JLabel("Export complete Site"),BorderLayout.CENTER);
		importExportPanel.add(new JLabel("Import complete Site"),BorderLayout.SOUTH);
		userInformationPanel.add(new JLabel("User options:"),BorderLayout.NORTH);
		userInformationPanel.add(new JLabel(Constants.rb.getString("menubar.extras.changePassword")),BorderLayout.SOUTH);
		optionsContainer.add(generalPanel);
		optionsContainer.add(importExportPanel);
		optionsContainer.add(userInformationPanel);
		generalPanel.setVisible(true);
		importExportPanel.setVisible(false);
		userInformationPanel.setVisible(false);
		
		generalOption.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				generalPanel.setVisible(true);
				importExportPanel.setVisible(false);
				userInformationPanel.setVisible(false);
				pack();
			}
		});
		
		importExportOption.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				generalPanel.setVisible(false);
				importExportPanel.setVisible(true);
				userInformationPanel.setVisible(false);
				pack();
			}
		});
		
		userInformationOption.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				generalPanel.setVisible(false);
				importExportPanel.setVisible(false);
				userInformationPanel.setVisible(true);
				pack();
			}
		});
		pack();
		
	}
	
}
