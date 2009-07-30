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

import static de.juwimm.cms.client.beans.Application.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.frame.helper.FlashFilter;
import de.juwimm.cms.content.frame.helper.ImagePreview;
import de.juwimm.cms.content.frame.helper.Utils;
import de.juwimm.cms.gui.FrmProgressDialog;
import de.juwimm.cms.gui.LookAndFeel;
import de.juwimm.cms.gui.table.DocumentTableModel;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.DocumentSlimValue;
import de.juwimm.swing.DropDownHolder;

/**
 * The panel for choosing and configuring a flash file.
 *  
 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 */
public class PanFlash extends JPanel {
	
	private static Logger log = Logger.getLogger(PanFlash.class);
	private static boolean TEST = false;
	
	private FlashProperties flashProperties = new FlashProperties();
	
	private ResourceBundle rb = Constants.rb;
	private Integer documentId;
	private JPanel panBottom = new JPanel();
	private JButton btnDelete = new JButton();
	private JScrollPane scrollPane = new JScrollPane();
	private JPanel panFileIcons = new JPanel();
	private JPanel panFileAction = new JPanel();
	private ButtonGroup buttonGroup = new ButtonGroup();
	private Integer intActUnit;
	private Integer intRootUnit;
	private int anzahlItems;
	private Communication communication = null;
	private JComboBox cboRegion = new JComboBox();
	private JButton btnUpload = new JButton();
	private JPanel panFlashProperties = null;
	
	private JLabel lblLinkDescription = null;
	private JLabel lblPlayerVersion = null;
	private JLabel lblAutostart = null;
	private JLabel lblWidth = null;
	private JLabel lblHeight = null;
	private JLabel lblLoop = null;
	private JLabel lblFlashVars = null;
	private JLabel lblQuality = null;
	private JLabel lblScale = null;
	private JLabel lblTransparency = null;
	
	private JTextField txtFlashLinkDesc = null;
	private JComboBox cmbPlayerVersion = null;
	private JCheckBox chkAutostart = null;
	private JTextField txtWidth = null;
	private JTextField txtHeight = null;
	private JCheckBox chkLoop = null;
	private JTextField txtFlashVars = null;
	private JComboBox cmbQuality = null;
	private JComboBox cmbScale = null;
	private JComboBox cmbTransparency = null;

	private JTable tblFlashfiles = new JTable();
	private DocumentTableModel tblFlashfilesModel = null;
	private TableSorter tblFlashfilesSorter = null;
	private PanViewSelect panViewSelect = null;
	private JToggleButton btnListView = null;
	private JToggleButton btnSymbolView = null;
	private int maxButtonWidth = 0;
	
	public PanFlash() {
		try {
			this.setLayout(new BorderLayout());
			// panBottm trägt das Properties-Panel und das Panel für die Buttons 
			this.panBottom.setLayout(new BorderLayout());
			
			this.panFlashProperties = new JPanel();
			this.panFlashProperties.setLayout(new GridBagLayout());
			GridBagConstraints gbConstraints = new GridBagConstraints();
			this.panFlashProperties.setBorder(new TitledBorder(rb.getString("panel.content.flash.properties")));
			
			this.panFileAction.add(this.btnUpload);
			this.panFileAction.add(this.btnDelete);		
			this.panBottom.add(this.panFileAction, BorderLayout.NORTH);
			
			Dimension prefSize = new Dimension(300, 20);

			gbConstraints.gridx = 0;
			gbConstraints.gridy = 0;
			gbConstraints.anchor = GridBagConstraints.LINE_START;
			gbConstraints.insets = new Insets(5, 5, 0, 0);
			gbConstraints.weightx = 0.0;
			this.lblLinkDescription = new JLabel(Messages.getString("panel.content.documents.linkdescription"), SwingConstants.LEFT);
			this.panFlashProperties.add(this.lblLinkDescription, gbConstraints);
			
			gbConstraints.gridx = 1;
			gbConstraints.gridy = 0;
			gbConstraints.weightx = 1.0;
			this.txtFlashLinkDesc = new JTextField();
			this.txtFlashLinkDesc.setPreferredSize(prefSize);
			this.panFlashProperties.add(this.txtFlashLinkDesc, gbConstraints);
			
			gbConstraints.gridx = 0;
			gbConstraints.gridy = 1;
			gbConstraints.weightx = 0.0;
			this.lblPlayerVersion = new JLabel(rb.getString("panel.content.flash.properties.playerversion"), SwingConstants.LEFT);
			this.panFlashProperties.add(this.lblPlayerVersion, gbConstraints);
			
			gbConstraints.gridx = 1;
			gbConstraints.gridy = 1;
			gbConstraints.weightx = 1.0;
			this.cmbPlayerVersion = new JComboBox();
			this.cmbPlayerVersion.setPreferredSize(prefSize);
			this.panFlashProperties.add(this.cmbPlayerVersion, gbConstraints);
			
			gbConstraints.gridx = 0;
			gbConstraints.gridy = 2;
			gbConstraints.weightx = 0.0;
			this.lblAutostart = new JLabel(rb.getString("panel.content.flash.properties.autostart"), SwingConstants.LEFT);
			this.panFlashProperties.add(this.lblAutostart, gbConstraints);
			
			gbConstraints.gridx = 1;
			gbConstraints.gridy = 2;
			gbConstraints.weightx = 1.0;
			this.chkAutostart = new JCheckBox();
			// TODO Werte aus dcf einfügen - evtl. CheckBoxes?
			this.panFlashProperties.add(this.chkAutostart, gbConstraints);
			
			gbConstraints.gridx = 0;
			gbConstraints.gridy = 3;
			gbConstraints.weightx = 0.0;
			this.lblWidth = new JLabel(rb.getString("panel.content.flash.properties.width"), SwingConstants.LEFT);
			this.panFlashProperties.add(this.lblWidth, gbConstraints);
			
			gbConstraints.gridx = 1;
			gbConstraints.gridy = 3;
			gbConstraints.weightx = 1.0;
			this.txtWidth = new JTextField("0");
			this.txtWidth.setPreferredSize(prefSize);
			this.panFlashProperties.add(this.txtWidth, gbConstraints);
			
			gbConstraints.gridx = 0;
			gbConstraints.gridy = 4;
			gbConstraints.weightx = 0.0;
			this.lblHeight = new JLabel(rb.getString("panel.content.flash.properties.height"), SwingConstants.LEFT);
			this.panFlashProperties.add(this.lblHeight, gbConstraints);
			
			gbConstraints.gridx = 1;
			gbConstraints.gridy = 4;
			gbConstraints.weightx = 1.0;
			this.txtHeight = new JTextField("0");
			this.txtHeight.setPreferredSize(prefSize);
			this.panFlashProperties.add(this.txtHeight, gbConstraints);
			
			gbConstraints.gridx = 0;
			gbConstraints.gridy = 5;
			gbConstraints.weightx = 0.0;
			this.lblLoop = new JLabel(rb.getString("panel.content.flash.properties.loop"), SwingConstants.LEFT);
			this.panFlashProperties.add(this.lblLoop, gbConstraints);
			
			gbConstraints.gridx = 1;
			gbConstraints.gridy = 5;
			gbConstraints.weightx = 1.0;
			this.chkLoop = new JCheckBox();
			this.panFlashProperties.add(this.chkLoop, gbConstraints);
			
			gbConstraints.gridx = 0;
			gbConstraints.gridy = 6;
			gbConstraints.weightx = 0.0;
			this.lblFlashVars = new JLabel(rb.getString("panel.content.flash.properties.variables"), SwingConstants.LEFT);
			this.lblFlashVars.setToolTipText(rb.getString("panel.content.flash.properties.variables.tooltip"));
			this.panFlashProperties.add(this.lblFlashVars, gbConstraints);
			
			gbConstraints.gridx = 1;
			gbConstraints.gridy = 6;
			gbConstraints.weightx = 1.0;
			this.txtFlashVars = new JTextField();
			this.txtFlashVars.setToolTipText(rb.getString("panel.content.flash.properties.variables.tooltip"));
			this.txtFlashVars.setPreferredSize(prefSize);
			this.panFlashProperties.add(this.txtFlashVars, gbConstraints);
			
			gbConstraints.gridx = 0;
			gbConstraints.gridy = 7;
			gbConstraints.weightx = 0.0;
			this.lblQuality = new JLabel(rb.getString("panel.content.flash.properties.quality"), SwingConstants.LEFT);
			this.panFlashProperties.add(this.lblQuality, gbConstraints);
			
			gbConstraints.gridx = 1;
			gbConstraints.gridy = 7;
			gbConstraints.weightx = 1.0;
			this.cmbQuality = new JComboBox();
			this.cmbQuality.setPreferredSize(prefSize);
			this.panFlashProperties.add(this.cmbQuality, gbConstraints);
			
			gbConstraints.gridx = 0;
			gbConstraints.gridy = 8;
			gbConstraints.weightx = 0.0;
			this.lblScale = new JLabel(rb.getString("panel.content.flash.properties.scale"), SwingConstants.LEFT);
			this.panFlashProperties.add(this.lblScale, gbConstraints);
			
			gbConstraints.gridx = 1;
			gbConstraints.gridy = 8;
			gbConstraints.weightx = 1.0;
			this.cmbScale = new JComboBox();
			this.cmbScale.setPreferredSize(prefSize);
			this.panFlashProperties.add(this.cmbScale, gbConstraints);
			
			gbConstraints.gridx = 0;
			gbConstraints.gridy = 9;
			gbConstraints.weightx = 0.0;
			this.lblTransparency = new JLabel(rb.getString("panel.content.flash.properties.transparency"), SwingConstants.LEFT);
			this.panFlashProperties.add(lblTransparency, gbConstraints);
			
			gbConstraints.gridx = 1;
			gbConstraints.gridy = 9;
			gbConstraints.weightx = 1.0;
			this.cmbTransparency = new JComboBox();
			this.cmbTransparency.setPreferredSize(prefSize);
			this.panFlashProperties.add(this.cmbTransparency, gbConstraints);
			this.panBottom.add(this.panFlashProperties, BorderLayout.CENTER);
			
			this.panFileIcons.setLayout(new FlowLayout(FlowLayout.LEFT));
			this.panFileIcons.setBackground(SystemColor.text);
				
			this.add(cboRegion, BorderLayout.NORTH);
			this.add(scrollPane, BorderLayout.CENTER);
			this.add(getViewSelectPan(), BorderLayout.WEST);
			this.add(this.panBottom, BorderLayout.SOUTH);
			
			scrollPane.getViewport().add(tblFlashfiles, null);
			tblFlashfiles.getSelectionModel().addListSelectionListener(new FlashfileListSelectionListener());
			tblFlashfiles.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			btnDelete.setText(Messages.getString("panel.content.documents.deleteDocument"));
			btnUpload.setText(Messages.getString("panel.content.documents.addDocument"));
			
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
		this.panFileIcons.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				resizeScrollpane();
			}
		});
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				resizeScrollpane();
			}
		});
		if (!TEST) {
			communication = ((Communication) getBean(Beans.COMMUNICATION));
			intActUnit = new Integer(((ContentManager) getBean(Beans.CONTENT_MANAGER)).getActUnitId());
			intRootUnit = new Integer(((ContentManager) getBean(Beans.CONTENT_MANAGER)).getRootUnitId());
		} else {
			intActUnit = new Integer(8);
			intRootUnit = new Integer(9);
		}
		
		this.cboRegion.addItem(new ComboModel(Messages.getString("panel.content.documents.files4ThisUnit"), intActUnit));
		this.cboRegion.addItem(new ComboModel(Messages.getString("panel.content.documents.files4AllUnits"), intRootUnit));
		cboRegion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regionSelected();
			}
		});
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnUploadActionPerformed(e);
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteActionPerformed(e);
			}
		});
		//regionSelected();
	}
	
	public String validateFlashProps() {
		String errormessage = null;
		if (this.documentId == null) {
			errormessage = rb.getString("panel.content.flash.no.document");
		}
		
		if (this.txtWidth.getText() != null && this.txtHeight.getText() != null) {
			try {
				@SuppressWarnings("unused")
				Short shortTest = new Short(this.txtWidth.getText());
				if (shortTest.intValue() < 1) {
					throw new NumberFormatException();
				}
				shortTest = new Short(this.txtHeight.getText());
				if (shortTest.intValue() < 1) {
					throw new NumberFormatException();
				}
				if (log.isDebugEnabled()) log.debug(shortTest); 
			} catch (NumberFormatException exception) {
				if (errormessage == null) {
					errormessage = rb.getString("panel.content.flash.size.numberformat");
				} else {
					errormessage = errormessage + "\n" + rb.getString("panel.content.flash.size.numberformat");
				}
			}
		} else {
			if (errormessage == null) {
				errormessage = rb.getString("panel.content.flash.size.error");
			} else {
				errormessage = errormessage + "\n" + rb.getString("panel.content.flash.size.error");
			}
		}
		String text = this.txtFlashVars.getText();
		int equalsCount = 0;
		int semicolonCount = 0;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '=') {
				equalsCount++;
				
			} else if (text.charAt(i) == ';') {
				semicolonCount++;
			}
		}
		if (equalsCount > 1) {
			if (semicolonCount != (equalsCount - 1)) {
				if (errormessage == null) {
					errormessage = rb.getString("panel.content.flash.properties.variables.error");
				} else {
					errormessage = errormessage + "\n" + rb.getString("panel.content.flash.properties.variables.error");
				}
			}
		}
		return errormessage;
	}
	
	/**
	 * Re-initializes this panel with all properties that were read from the mandator's dcf.
	 */
	public void reinitialize() {
		String text = null;
		this.documentId = null;

		this.cmbPlayerVersion.removeAllItems();
		for (int i = 0; i < this.flashProperties.playerVersions.length; i++) {
			text = this.flashProperties.playerVersions[i];
			this.cmbPlayerVersion.addItem(new DropDownHolder(text, text));
		}
		if (this.flashProperties.playerVersions.length > 0 ) {
			this.cmbPlayerVersion.setSelectedIndex(this.flashProperties.playerVersions.length - 1);
		}
		
		this.chkAutostart.setSelected(this.flashProperties.autostartDefault);
		this.lblAutostart.setVisible(this.flashProperties.autostartChoosable);
		this.chkAutostart.setVisible(this.flashProperties.autostartChoosable);
		
		this.chkLoop.setSelected(this.flashProperties.loopDefault);
		this.lblLoop.setVisible(this.flashProperties.loopChoosable);
		this.chkLoop.setVisible(this.flashProperties.loopChoosable);
		
		this.txtFlashVars.setText(this.flashProperties.flashVariablesDefault);
		this.lblFlashVars.setVisible(this.flashProperties.flashVarsChoosable);
		this.txtFlashVars.setVisible(this.flashProperties.flashVarsChoosable);
		
		this.cmbQuality.removeAllItems();
		for (int i = 0; i <this.flashProperties.qualities.length; i++) {
			text = this.flashProperties.qualities[i];
			this.cmbQuality.addItem(new DropDownHolder(text, text));
			if (text.equals(this.flashProperties.qualityDefault)) {
				this.cmbQuality.setSelectedIndex(i);
			}
		}
		this.lblQuality.setVisible(this.flashProperties.qualityChoosable);
		this.cmbQuality.setVisible(this.flashProperties.qualityChoosable);
		
		this.cmbScale.removeAllItems();
		for (int i = 0; i < this.flashProperties.scales.length; i++) {
			text = this.flashProperties.scales[i];
			this.cmbScale.addItem(new DropDownHolder(text, text));
			if (text.equals(this.flashProperties.scaleDefault)) {
				this.cmbScale.setSelectedIndex(i);
			}
		}
		this.lblScale.setVisible(this.flashProperties.scaleChoosable);
		this.cmbScale.setVisible(this.flashProperties.scaleChoosable);
		
		this.cmbTransparency.removeAllItems();
		for (int i = 0; i < this.flashProperties.transparencies.length; i++) {
			text = this.flashProperties.transparencies[i];
			this.cmbTransparency.addItem(new DropDownHolder(text, text));
			if (text.equals(this.flashProperties.transparencyDefault)) {
				this.cmbTransparency.setSelectedIndex(i);
			}
		}
		this.lblTransparency.setVisible(this.flashProperties.transparencyChoosable);
		this.cmbTransparency.setVisible(this.flashProperties.transparencyChoosable);
		this.panFlashProperties.invalidate();
		this.repaint();
	}
	
	public void resizeScrollpane() {
		int width = ((int) scrollPane.getSize().getWidth()) - 20;
		int fitinrow = 1;
		int height = 1;
		double dblSmall = 0;
		try {
			fitinrow = width / this.maxButtonWidth;
		} catch (ArithmeticException e) {
		}
		if (fitinrow < 1)
			fitinrow = 1;
		try {
			dblSmall = ((double) anzahlItems / (double) fitinrow) - (anzahlItems / fitinrow);
		} catch (ArithmeticException e) {
		}
		if (dblSmall < 1 && dblSmall > 0) {
			try {
				height = ((anzahlItems / fitinrow) + 1) * 100;
			} catch (ArithmeticException e) {
			}
		} else {
			try {
				height = (anzahlItems / fitinrow) * 100;
			} catch (ArithmeticException e) {
			}
		}
		this.panFileIcons.setPreferredSize(new Dimension(width, height));
	}

	private void loadThumbs(Integer unit) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			tblFlashfilesModel = new DocumentTableModel();
			tblFlashfilesSorter = new TableSorter(tblFlashfilesModel, tblFlashfiles.getTableHeader());
			tblFlashfiles.getSelectionModel().clearSelection();
			tblFlashfiles.setModel(tblFlashfilesSorter);
			DocumentSlimValue[] dsva = communication.getAllSlimDocumentValues(unit);
			this.panFileIcons.removeAll();
			anzahlItems = 0;
			buttonGroup = new ButtonGroup();
			for (int i = 0; i < dsva.length; i++) {
				DocumentSlimValue dsv = dsva[i];
				tblFlashfilesModel.addRow(dsv);
				PanDocumentSymbol pan = new PanDocumentSymbol();
				// pan.setPreferredSize(new Dimension(95,95));
				pan.getFileButton().setIcon(Utils.getIcon4Extension(Utils.getExtension(dsv.getDocumentName())));
				pan.getFileNameLabel().setText(dsv.getDocumentName());
				pan.getFileButton().setActionCommand(Integer.toString(dsv.getDocumentId()));
				pan.getFileButton().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						btnFileActionPerformed(e);
					}
				});
				if (documentId != null && (documentId.intValue() == dsv.getDocumentId())) {
					pan.getFileButton().doClick();
					selectFlashfile(documentId);
				}
				this.panFileIcons.add(pan, null);
				anzahlItems++;
				buttonGroup.add(pan.getFileButton());
				this.setMaxButtonWidth(pan);
			}
			this.panFileIcons.validate();
			this.panFileIcons.repaint();
			resizeScrollpane();
		} catch (Exception exe) {
			log.error("Error loading thumbs", exe);
		}
		this.setCursor(Cursor.getDefaultCursor());
	}

	private void btnFileActionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("CLICK")) {
			Enumeration en = buttonGroup.getElements();
			while (en.hasMoreElements()) {
				PanDocumentSymbol.JToggleBtt btn = (PanDocumentSymbol.JToggleBtt) en.nextElement();
				if (!btn.isSelected()) {
					btn.unClick();
				} else {
					btn.doClick();
				}
			}
			documentId = new Integer(buttonGroup.getSelection().getActionCommand());
			selectFlashfile(documentId);
		} else {
			ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "CLICK");
			((PanDocumentSymbol.JToggleBtt) e.getSource()).fireActionPerformedT(ae);
		}
	}

	public void regionSelected() {
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		Integer intSelItem = ((ComboModel) this.cboRegion.getSelectedItem()).getRegionId();
		loadThumbs(intSelItem);
		try {
			if (intRootUnit.equals(intSelItem)) {
				if (communication.isUserInRole(UserRights.SITE_ROOT) || intActUnit.equals(intRootUnit)) {
					this.btnDelete.setVisible(true);
					this.btnUpload.setVisible(true);
				} else {
					this.btnDelete.setVisible(false);
					this.btnUpload.setVisible(false);
				}
			} else {
				this.btnDelete.setVisible(true);
				this.btnUpload.setVisible(true);
			}
		} catch (Exception exception) {
			log.error(exception);
		} finally {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	private void btnDeleteActionPerformed(ActionEvent e) {
		try {
			DocumentSlimValue currDoc = null;
			String actionCommand = buttonGroup.getSelection().getActionCommand();
			int rowInModel = tblFlashfilesModel.getRowForDocument(Integer.valueOf(actionCommand));
			if (rowInModel >= 0) {
				currDoc = (DocumentSlimValue) tblFlashfilesModel.getValueAt(rowInModel, 4);
			}
			String tmp = actionCommand;
			if (currDoc != null && currDoc.getDocumentName() != null && !("".equalsIgnoreCase(currDoc.getDocumentName()))) {
				tmp += " (" + currDoc.getDocumentName() + ")";
			}
			int selection = JOptionPane.showConfirmDialog(this, 
					Messages.getString("panel.content.documents.deleteThisDocument", tmp),
					Messages.getString("panel.content.documents.deleteDocument"), 
					JOptionPane.WARNING_MESSAGE,
					JOptionPane.YES_NO_OPTION);
			if (selection == JOptionPane.YES_OPTION) {
				communication.removeDocument(Integer.valueOf(actionCommand).intValue());
				loadThumbs(((ComboModel) this.cboRegion.getSelectedItem()).getRegionId());
			}
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	private void btnUploadActionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ComboModel cb = (ComboModel) cboRegion.getSelectedItem();
				upload(cb.getView(), cb.getRegionId());
				loadThumbs(cb.getRegionId());
			}
		});
	}

	private void upload(String prosa, Integer unit) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		JFileChooser fileChooser = new JFileChooser();
		int filterLength = fileChooser.getChoosableFileFilters().length;
		FileFilter[] fileFilter = fileChooser.getChoosableFileFilters();
		for (int i = 0; i < filterLength; i++) {
			fileChooser.removeChoosableFileFilter(fileFilter[i]);
		}
		fileChooser.addChoosableFileFilter(new FlashFilter());
		fileChooser.setAccessory(new ImagePreview(fileChooser));
		fileChooser.setDialogTitle(prosa);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setCurrentDirectory(Constants.LAST_LOCAL_UPLOAD_DIR);
		int returnVal = fileChooser.showDialog(this, Messages.getString("panel.content.documents.addDocument"));

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] files = fileChooser.getSelectedFiles();
			if ((files != null) && (files.length > 0)) {
				for (int i = (files.length - 1); i >= 0; i--) {
					File file = files[i];
					FrmProgressDialog progressDialog = new FrmProgressDialog(Messages.getString("panel.content.documents.addDocument"),
							Messages.getString("panel.content.upload.ParseFile"), 100);
					progressDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
					try {
						String extension = Utils.getExtension(file);
		
						String mimetype = Utils.getMimeType4Extension(extension);
						if (mimetype.equals("")) {
							mimetype = "application/octet-stream";
						}
						progressDialog.setProgress(Messages.getString("panel.content.upload.Uploading"), 50);
						this.documentId = this.communication.addOrUpdateDocument(file, unit, file.getName(), mimetype, null);
					} catch (Exception exception) {
						log.error("Error upload document " + file.getName(), exception);
					} finally {
						progressDialog.setProgress(Messages.getString("panel.content.upload.Finished"), 100);
						progressDialog.dispose();
						progressDialog.setCursor(Cursor.getDefaultCursor());
					}
				}
			}
			Constants.LAST_LOCAL_UPLOAD_DIR = fileChooser.getCurrentDirectory();
		}
		this.setCursor(Cursor.getDefaultCursor());
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream inputStream = new FileInputStream(file);
		long length = file.length();

		if (length > Integer.MAX_VALUE) { 
			throw new IOException(Messages.getString("exception.fileTooBig"));
		}
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = inputStream.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		if (offset < bytes.length) { 
			throw new IOException(Messages.getString("exception.cantReadFullFile", file.getName()));
		}
		inputStream.close();
		return bytes;
	}

	public Integer getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(Integer docid) {
		this.documentId = docid;
		//loadThumbs(intActUnit);
	}

	public String getFlashfileName() {
		try {
			return communication.getDocumentName(getDocumentId().intValue());
		} catch (Exception exe) {
			return "";
		}
	}

	private void selectFlashfile(Integer documentId) {
		int rowInModel = tblFlashfilesModel.getRowForDocument(documentId);
		if (rowInModel >= 0) {
			int rowInView = tblFlashfilesSorter.getRowInView(rowInModel);
			tblFlashfiles.getSelectionModel().setSelectionInterval(rowInView, rowInView);
		}
	}

    private JPanel getViewSelectPan() {
        if (panViewSelect == null) {
            panViewSelect = new PanViewSelect();
            panViewSelect.setPreferredSize(new Dimension(26, scrollPane.getHeight()));
            btnListView = new JToggleButton(UIConstants.BTN_LIST_VIEW, true);
            btnListView.setToolTipText(rb.getString("PanDocument.view.list"));
            btnListView.setPreferredSize(new Dimension(UIConstants.BTN_LIST_VIEW.getIconHeight() + 10, UIConstants.BTN_LIST_VIEW.getIconWidth() + 10));
            btnSymbolView = new JToggleButton(UIConstants.BTN_SYMBOL_VIEW, false);
            btnSymbolView.setToolTipText(rb.getString("PanDocument.view.symbol"));
            btnSymbolView.setPreferredSize(new Dimension(UIConstants.BTN_SYMBOL_VIEW.getIconHeight() + 10, UIConstants.BTN_SYMBOL_VIEW.getIconWidth() + 10));
            btnSymbolView.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Component currentComponent = scrollPane.getViewport().getView();
					if (currentComponent != null) {
						scrollPane.getViewport().remove(currentComponent);
					}
					scrollPane.getViewport().add(panFileIcons, null);
				}
            });
            btnListView.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Component currentComponent = scrollPane.getViewport().getView();
					if (currentComponent != null) {
						scrollPane.getViewport().remove(currentComponent);
					}
					scrollPane.getViewport().add(tblFlashfiles, null);
				}
            });
            panViewSelect.addButton(btnListView);
            panViewSelect.addButton(btnSymbolView);
        }
        return panViewSelect;
    }

    private void setMaxButtonWidth(JPanel panel) {
    	int currentWidth = panel.getPreferredSize().width;
    	if (this.maxButtonWidth < currentWidth)
    		this.maxButtonWidth = currentWidth;
    }

	/**
	 * Returns the flash description text that is displayed within the WYSIWYG editor instead of the flash file itself.
	 * 
	 * @return the flash description text
	 */
	public String getFlashLinkDescription() {
		return this.txtFlashLinkDesc.getText();
	}
    
	/**
	 * Sets the flash description that is displayed within the WYSIWYG editor instead of the flash file itself.
	 * 
	 * @param docDesc the flash description
	 */
	public void setFlashLinkDescription(String docDesc) {
		this.txtFlashLinkDesc.setText(docDesc);
	}
	
	/**
	 * Returns a boolean indicating whether the flash movie should start automatically.
	 * 
	 * @return a boolean indicating whether the flash movie should start automatically
	 */
	public boolean getAutostart() {
		return chkAutostart.isSelected();
	}

	/**
	 * Sets the autostart mode of the flash movie.
	 * 
	 * @param a boolean indicating whether the flash movie should start automatically
	 */
	public void setAutostart(boolean autostart) {
		this.chkAutostart.setSelected(autostart);
	}

	/**
	 * Returns a boolean indicating whether the flash movie should repeat automatically.
	 * 
	 * @return a boolean indicating whether the flash movie is supposed to repeat automatically
	 */
	public boolean getLoop() {
		return chkLoop.isSelected();
	}

	/**
	 * Sets the loop mode of the flash movie.
	 * 
	 * @param a boolean indicating whether the flash movie is supposed to repeat automatically
	 */
	public void setLoop(boolean loop) {
		this.chkLoop.setSelected(loop);
	}

	/**
	 * Returns the recommended player version for this flash file.
	 * 
	 * @return  the recommended player version for this flash file
	 */
	public String getPlayerVersion() {
		return ((DropDownHolder) cmbPlayerVersion.getSelectedItem()).toString();
	}

	/**
	 * Sets the recommended player version for this flash file. 
	 * If the specified value is not part of the list of available playerVersions, 
	 * the specified argument is ignored.
	 * 
	 * @param the recommended player version for this flash file
	 */
	public void setPlayerVersion(String playerVersion) {
		for (int i = 0, count = this.cmbPlayerVersion.getItemCount(); i < count; i++) {
			if (((DropDownHolder) this.cmbPlayerVersion.getItemAt(i)).toString().equals(playerVersion)) {
				this.cmbPlayerVersion.setSelectedIndex(i);
				return;
			}
		}
	}

	/**
	 * Returns the recommended quality for play-back.
	 * 
	 * @return the quality for play-back
	 */
	public String getQuality() {
		return ((DropDownHolder) cmbQuality.getSelectedItem()).toString();
	}

	/**
	 * Sets the recommeded quality for play-back.
	 * 
	 * @param quality the recommended quality
	 */
	public void setQuality(String quality) {
		for (int i = 0, count = this.cmbQuality.getItemCount(); i < count; i++) {
			if (((DropDownHolder) this.cmbQuality.getItemAt(i)).toString().equals(quality)) {
				this.cmbQuality.setSelectedIndex(i);
				return;
			}
		}
	}

	/**
	 * Returns the selected scale value for this flash movie
	 * 
	 * @return the scale value
	 */
	public String getScale() {
		return ((DropDownHolder) cmbScale.getSelectedItem()).toString();
	}

	/**
	 * Sets the scale value for this flash movie.
	 * 
	 * @param scale the scale value to set
	 */
	public void setScale(String scale) {
		for (int i = 0, count = this.cmbScale.getItemCount(); i < count; i++) {
			if (((DropDownHolder) this.cmbScale.getItemAt(i)).toString().equals(scale)) {
				this.cmbScale.setSelectedIndex(i);
				return;
			}
		}
	}

	/**
	 * Returns the transparency for the selected flash movie.
	 * 
	 * @return the transparency value for the selected flash movie
	 */
	public String getTransparency() {
		return ((DropDownHolder) cmbTransparency.getSelectedItem()).toString();
	}

	/**
	 * Sets the transparency value.
	 * 
	 * @param transparency the transparency value to set
	 */
	public void setTransparency(String transparency) {
		for (int i = 0, count = this.cmbTransparency.getItemCount(); i < count; i++) {
			if (((DropDownHolder) this.cmbTransparency.getItemAt(i)).toString().equals(transparency)) {
				this.cmbTransparency.setSelectedIndex(i);
				return;
			}
		}
	}

	/**
	 * Returns the variables that were set for the flash player.
	 * 
	 * @return the flash variables
	 */
	public String getFlashVars() {
		return txtFlashVars.getText();
	}

	/**
	 * Sets the variables that are passed to the flash player.
	 * 
	 * @param flashVars the variables to set
	 */
	public void setFlashVars(String flashVars) {
		this.txtFlashVars.setText(flashVars);
	}

	/**
	 * Returns the height of the flash movie
	 * 
	 * @return the height
	 */
	public String getFlashHeight() {
		return txtHeight.getText();
	}

	/**
	 * Sets the height of the flash movie.
	 * 
	 * @param txtHeight the height to set
	 */
	public void setFlashHeight(String height) {
		this.txtHeight.setText(height);
	}

	/**
	 * Returns the width of the flash movie.
	 * 
	 * @return the width as a String
	 */
	public String getFlashWidth() {
		return txtWidth.getText();
	}

	/**
	 * Sets the width of this flash movie.
	 * 
	 * @param width the width to set
	 */
	public void setFlashWidth(String width) {
		this.txtWidth.setText(width);
	}

	/** @see javax.swing.JComponent#setEnabled(boolean) */
	public void setEnabled(boolean enabling) {
		super.setEnabled(enabling);
	}
		
	/**
	 * Returns the flash properties configured by the mandator's dcf.
	 * 
	 * @return the flash properties used by this panel
	 */
	public FlashProperties getFlashProperties() {
		return this.flashProperties;
	}
	
	/**
	 * A type for holding the region ID together with a textual description. This may be used for combo boxes.
	 */
	public final class ComboModel {
		private String strView;
		private Integer intRegionId;

		public ComboModel(String view, Integer regionid) {
			this.strView = view;
			this.intRegionId = regionid;
		}

		public String getView() {
			return this.strView;
		}

		public Integer getRegionId() {
			return this.intRegionId;
		}

		public String toString() {
			return this.strView;
		}
	}
	
	/**
	 * A {@link ListSelectionListener} implementation for flash files lists. 
	 * Whenever the user selects an entry from the flash files list, all necessary information about this file is gathered.
	 */
	private class FlashfileListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				return;
			}
			int selectedRow = tblFlashfiles.getSelectedRow();
			if (selectedRow >= 0) {
				DocumentSlimValue documentVO = (DocumentSlimValue) tblFlashfilesSorter.getValueAt(selectedRow, 4);
				documentId = documentVO.getDocumentId();
			} 
			if (documentId != null) {
				Enumeration enumeration = buttonGroup.getElements();
				while (enumeration.hasMoreElements()) {
					PanDocumentSymbol.JToggleBtt toggleBtt = (PanDocumentSymbol.JToggleBtt) enumeration.nextElement();
					if (Integer.valueOf(toggleBtt.getActionCommand()).intValue() == documentId.intValue()) {
						toggleBtt.doClick();
					} else {
						toggleBtt.unClick();
					}
				}
			}
		}
	}
	
	
	/**
	 * This type represents the Flash information taken from the mandator's dcf.
	 * It provides reasonable default values for those properties that were not set 
	 * in the dcf (default values <b>bold</b>):
	 * <ul>
	 * <li><code>PlayerVersion = 5.0.0.0, 6.0.0.0, <b>7.0.0.0</b>, 8.0.0.0, 9.0.0.0</code></li>
	 * <li><code>Autostart = true, <b>false</b></code></li>
	 * <li><code>Loop = true, <b>false</b></code></li>
	 * <li><code>FlashVariables = <b>[none]</b></code></li>
	 * <li><code>Quality = low, medium, high, <b>best</b></code></li>
	 * <li><code>Scale = showall, noborder, <b>exactfit</b></code></li>
	 * <li><code>Transparency = <b>[none]</b>, window, opaque, transparent</code></li>
	 * </ul>
	 * All properties specified here are used for all flash files for this mandator!
	 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
	 */
	public class FlashProperties {
		
		private String[] playerVersions = {"5.0.0.0", "6.0.0.0", "7.0.0.0", "8.0.0.0", "9.0.0.0"};//$NON-NLS-5$
		private String playerVersionDefault = "7.0.0.0";
		private boolean autostartChoosable = true;
		private boolean autostartDefault = false;
		private boolean loopChoosable = true;
		private boolean loopDefault = false;
		private String[] flashVariables = null;
		private String flashVariablesDefault = "";
		private boolean flashVarsChoosable = true;
		private String[] qualities = {"low", "medium", "high", "best"};
		private boolean qualityChoosable = true;
		private String qualityDefault = "best";
		private String[] scales = {"showall, noborder, exactfit"};
		private boolean scaleChoosable = true;
		private String scaleDefault = "exactfit";
		private String[] transparencies = {"[none], window, opaque, transparent"};
		private boolean transparencyChoosable = true;
		private String transparencyDefault = "";

		/**
		 * Returns a boolean indicating whether the autostart value for flash movies should be
		 * configurable in the admin client for this mandator.
		 * 
		 * @return the autostartChoosable
		 */
		public boolean getAutostartChoosable() {
			return this.autostartChoosable;
		}

		/**
		 * When set to false, the editor won't be able to choose the autostart mode.
		 * The default value will be used.
		 * 
		 * @param autostartChoosable the autostartChoosable to set
		 */
		public void setAutostartChoosable(boolean autostartChoosable) {
			this.autostartChoosable = autostartChoosable;
		}

		/**
		 * Returns a boolean indicating whether the flash movie is started automatically per default for this mandator.
		 * 
		 * @return the autostart value
		 */
		public boolean getAutostartDefault() {
			return this.autostartDefault;
		}

		/**
		 * Sets the autostart value for this mandator's flash movies.
		 * 
		 * @param autostartDefault the autostart value
		 */
		public void setAutostartDefault(boolean autostartDefault) {
			this.autostartDefault = autostartDefault;
		}

		/**
		 * Returns all flash variables that are passed to the player.
		 * 
		 * @return the flash variables that are passed to the player
		 */
		public String[] getFlashVariables() {
			return this.flashVariables;
		}

		/**
		 * Sets the flash variables that are passed to the player.
		 * 
		 * @param flashVariables the flashVariables to set
		 */
		public void setFlashVariables(String[] flashVariables) {
			this.flashVariables = flashVariables;
		}
		
		/**
		 * Returns a boolean indicating whether the editor may enter variables for the flash player. 
		 * 
		 * @return a boolean indicating whether the editor may enter flash variables
		 */
		public boolean getFlashVarsChoosable() {
			return this.flashVarsChoosable;
		}

		/**
		 * When set to false, the editor won't be able to enter any flash variables.
		 * The default value will be used.
		 * 
		 * @param flashVarsChoosable the flashVarsChoosable to set
		 */
		public void setFlashVarsChoosable(boolean flashVarsChoosable) {
			this.flashVarsChoosable = flashVarsChoosable;
		}

		/**
		 * Returns the default variables that are passed to the player.
		 * 
		 * @return the default flash variables
		 */
		public String getFlashVariablesDefault() {
			return this.flashVariablesDefault;
		}

		/**
		 * Sets the default variables that are passed to the player.
		 * 
		 * @param flashVariablesDefault the default flash variables to set
		 */
		public void setFlashVariablesDefault(String flashVariablesDefault) {
			this.flashVariablesDefault = flashVariablesDefault;
		}

		/**
		 * When set to false, the editor won't be able to choose the loop mode.
		 * The default value will be used.
		 * 
		 * @return the loopChoosable
		 */
		public boolean getLoopChoosable() {
			return this.loopChoosable;
		}

		/**
		 * @param loopChoosable the loopChoosable to set
		 */
		public void setLoopChoosable(boolean loopChoosable) {
			this.loopChoosable = loopChoosable;
		}

		/**
		 * @return the loopDefault
		 */
		public boolean getLoopDefault() {
			return this.loopDefault;
		}

		/**
		 * @param loopDefault the loopDefault to set
		 */
		public void setLoopDefault(boolean loopDefault) {
			this.loopDefault = loopDefault;
		}

		/**
		 * @return the playerVersions
		 */
		public String[] getPlayerVersions() {
			return this.playerVersions;
		}

		/**
		 * @param playerVersions the playerVersions to set
		 */
		public void setPlayerVersions(String[] playerVersions) {
			this.playerVersions = playerVersions;
		}
		

		/**
		 * @return the playerVersionDefault
		 */
		public String getPlayerVersionDefault() {
			return playerVersionDefault;
		}

		/**
		 * @param playerVersionDefault the playerVersionDefault to set
		 */
		public void setPlayerVersionDefault(String playerVersionDefault) {
			for (int i = 0; i < playerVersions.length; i++) {
				if (playerVersions[i].equals(playerVersionDefault)) {
					this.playerVersionDefault = playerVersionDefault;
					return;
				}
			}
		}

		/**
		 * Returns the qualities the flash movies should be played back with.
		 * 
		 * @return the qualities
		 */
		public String[] getQualities() {
			return this.qualities;
		}

		/**
		 * @param qualities the qualities to set
		 */
		public void setQualities(String[] qualities) {
			this.qualities = qualities;
		}

		/**
		 * When set to false, the editor won't be able to choose the quality settings.
		 * The default value will be used.
		 * 
		 * @return the qualityChoosable
		 */
		public boolean getQualityChoosable() {
			return this.qualityChoosable;
		}

		/**
		 * @param qualityChoosable the qualityChoosable to set
		 */
		public void setQualityChoosable(boolean qualityChoosable) {
			this.qualityChoosable = qualityChoosable;
		}

		/**
		 * Returns the default quality value all flash movies of this mandator should be played back with.
		 * 
		 * @return the default quality
		 */
		public String getQualityDefault() {
			return this.qualityDefault;
		}

		/**
		 * Sets the default quality value all flash movies of this mandator should be played back with.
		 * If the specified value is not part of the list of possible qualities, the specified value is ignored.
		 * 
		 * @param qualityDefault the default quality to set
		 */
		public void setQualityDefault(String qualityDefault) {
			for (int i = 0; i < qualities.length; i++) {
				if (qualities[i].equalsIgnoreCase(qualityDefault)) {
					this.qualityDefault = qualityDefault;
					return;
				}
			}
		}

		/**
		 * Returns whether the scale should be choosable within the admin client.
		 * 
		 * @return a boolean indicating whether the scale is choosable from within the client
		 */
		public boolean getScaleChoosable() {
			return this.scaleChoosable;
		}

		/**
		 * When set to false, the editor won't be able to choose the scale settings.
		 * The default value will be used.
		 * 
		 * @param scaleChoosable a boolean indicating if the scale should be choosable
		 */
		public void setScaleChoosable(boolean scaleChoosable) {
			this.scaleChoosable = scaleChoosable;
		}

		/**
		 * Returns all scales the flash movie may be played back with.
		 * 
		 * @return the scales
		 */
		public String[] getScales() {
			return this.scales;
		}

		/**
		 * Sets the scales the flash movie may be played back with.
		 * 
		 * @param scales the scales to set
		 */
		public void setScales(String[] scales) {
			this.scales = scales;
		}
		
		/**
		 * Returns the default scale.
		 * 
		 * @return the default scale
		 */
		public String getScaleDefault() {
			return scaleDefault;
		}

		/**
		 * Sets the default scale.
		 * If the specified value is not part of the list of possible scales, the specified value is ignored.
		 * 
		 * @param scaleDefault the default scale to set
		 */
		public void setScaleDefault(String scaleDefault) {
			for (int i = 0; i < scales.length; i++) {
				if (scales[i].equals(scaleDefault)) {
					this.scaleDefault = scaleDefault;
					return;
				}
			}
		}

		/**
		 * Returns all possible values for transparencies.
		 * 
		 * @return the transparencies
		 */
		public String[] getTransparencies() {
			return this.transparencies;
		}

		/**
		 * Sets all possible values for transparencies.
		 * 
		 * @param transparencies the transparencies to set
		 */
		public void setTransparencies(String[] transparencies) {
			this.transparencies = transparencies;
		}

		/**
		 * When set to false, the editor won't be able to choose the transparency settings.
		 * The default value will be used.
		 * 
		 * @return the transparencyChoosable
		 */
		public boolean getTransparencyChoosable() {
			return this.transparencyChoosable;
		}

		/**
		 * Returns a boolean indicating whether the transparency value may be set from within the admin client.
		 * 
		 * @param transparencyChoosable the transparencyChoosable to set
		 */
		public void setTransparencyChoosable(boolean transparencyChoosable) {
			this.transparencyChoosable = transparencyChoosable;
		}

		/**
		 * Returns the transparency default value.
		 * 
		 * @return the transparency default value
		 */
		public String getTransparencyDefault() {
			return transparencyDefault;
		}

		/**
		 * Sets the transparency default value.
		 * If the specified value is not part of the list of possible transparency values, the specified value is ignored. 
		 * 
		 * @param transparencyDefault the transparencyDefault to set
		 */
		public void setTransparencyDefault(String transparencyDefault) {
			for (int i = 0; i < transparencies.length; i++) {
				if (transparencies[i].equals(transparencyDefault)) {
					this.transparencyDefault = transparencyDefault;
					return;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Flash Test");
		JFrame.setDefaultLookAndFeelDecorated(false);
		try {
			LookAndFeel.switchTo(LookAndFeel.determineLookAndFeel());
		} catch (NullPointerException exception) {
			// ignore. Yes, that's ugly - but we don't have a de.juwimm.cms.Main object right here
		}
		
		frame.setResizable(true);
		Constants.rb = ResourceBundle.getBundle("de.juwimm.cms.CMS", Constants.CMS_LOCALE);
		UIConstants.loadImages();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PanFlash panFlash = new PanFlash();
		PanFlash.TEST = true;
		frame.getContentPane().add(panFlash);
		frame.pack();
		frame.setVisible(true);
	}
}
