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

import static de.juwimm.cms.client.beans.Application.getBean;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.frame.DlgPdfDocumentPassword;
import de.juwimm.cms.content.frame.DlgSaveDocument;
import de.juwimm.cms.content.frame.helper.DocumentFilter;
import de.juwimm.cms.content.frame.helper.ImagePreview;
import de.juwimm.cms.content.frame.helper.Utils;
import de.juwimm.cms.content.panel.util.DocumentPreviewComponent;
import de.juwimm.cms.content.panel.util.PdfPreviewFrame;
import de.juwimm.cms.exceptions.InvalidSizeException;
import de.juwimm.cms.gui.FrmProgressDialog;
import de.juwimm.cms.gui.controls.FileTransferHandler;
import de.juwimm.cms.gui.table.DocumentTableModel;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.gui.views.PanContentView;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.PdfUtils;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.DocumentSlimValue;

/**
 * 
 * @author <a href="sascha-matthias.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @author <a href="carsten.schalm@juwimm.com">Carsten Schalm</a>
 * @version $Id$
 */
public class PanDocuments extends JPanel {
	private static Logger log = Logger.getLogger(PanDocuments.class);
	private final ResourceBundle rb = Constants.rb;
	private Integer intDocId;
	private final JPanel panBottom = new JPanel();
	private final JButton btnDelete = new JButton();
	private final JScrollPane scrollPane = new JScrollPane();
	private final JPanel panDocumentButtons = new JPanel();
	private final FlowLayout panDocumentsLayout = new FlowLayout();
	private final JPanel panFileAction = new JPanel();
	private ButtonGroup bgrp = new ButtonGroup();
	private final Integer intActUnit;
	private final Integer intRootUnit;
	private int anzahlItems;
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private final ContentManager contentManager = ((ContentManager) getBean(Beans.CONTENT_MANAGER));
	private final JComboBox cboRegion = new JComboBox();
	private final JButton btnAdd = new JButton();
	private final JButton btnUpdate = new JButton();
	private final JButton btnPreview = new JButton();
	private final JPanel panLinkName = new JPanel();
	private final JTextField txtDocumentDesc = new JTextField();
	private final JLabel lbLinkDescription = new JLabel();
	private final BorderLayout panLinkNameLayout = new BorderLayout();
	private final JCheckBox cbxDisplayTypeInline = new JCheckBox();

	private final JTable tblDocuments = new JTable();
	private DocumentTableModel tblDocumentsModel = null;
	private TableSorter tblDocumentSorter = null;
	private PanViewSelect panViewSelect = null;
	private JToggleButton btnListView = null;
	private JToggleButton btnSymbolView = null;
	private int maxButtonWidth = 0;
	private boolean isDataActualization = false;
	boolean showRegionCombo = true;
	private String selectedDocName;
	private String mimeType = "";
	private Integer viewComponentId;

	public PanDocuments(boolean showRegionCombo) {
		this.showRegionCombo = showRegionCombo;
		viewComponentId = PanContentView.getInstance().getViewComponent().getViewComponentId();
		try {
			jbInit();
			tblDocuments.getSelectionModel().addListSelectionListener(new DocumentListSelectionListener());
			tblDocuments.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			btnDelete.setText(Messages.getString("panel.content.documents.deleteDocument"));
			btnAdd.setText(Messages.getString("panel.content.documents.addDocument"));
			btnPreview.setText(Messages.getString("dialog.preview"));
			btnUpdate.setText(Messages.getString("panel.content.documents.updateDocument"));
			btnUpdate.setVisible(false);
			lbLinkDescription.setText(Messages.getString("panel.content.documents.linkdescription"));
			cbxDisplayTypeInline.setText(rb.getString("content.modules.externalLink.displayTypeInline"));
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
		this.panDocumentButtons.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizeScrollpane();
			}
		});
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizeScrollpane();
			}
		});
		intActUnit = new Integer(((ContentManager) getBean(Beans.CONTENT_MANAGER)).getActUnitId());
		intRootUnit = new Integer(((ContentManager) getBean(Beans.CONTENT_MANAGER)).getRootUnitId());
		this.cboRegion.addItem(new CboModel(Messages.getString("panel.content.documents.files4ThisUnit"), intActUnit, true));
		this.cboRegion.addItem(new CboModel(Messages.getString("panel.content.documents.files4AllUnits"), intRootUnit, true));
		this.cboRegion.addItem(new CboModel(Messages.getString("panel.content.documents.files4ThisComponent"), viewComponentId, false));

		cboRegion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regionSelected();
			}
		});
		cboRegion.setSelectedIndex(2);

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddActionPerformed(e);
			}
		});
		btnPreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPreviewActionPerformed(e);
			}
		});
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnUpdateActionPerformed(e);
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteActionPerformed(e);
			}
		});
		//regionSelected();
	}

	/**
	 */
	public final class CboModel {
		private final String strView;
		private final Integer intRegionId;
		private final boolean isUnit;

		public CboModel(String view, Integer regionid, boolean isUnit) {
			this.isUnit = isUnit;
			this.strView = view;
			this.intRegionId = regionid;
		}

		public String getView() {
			return this.strView;
		}

		public Integer getRegionId() {
			return this.intRegionId;
		}

		@Override
		public String toString() {
			return this.strView;
		}
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
		if (fitinrow < 1) fitinrow = 1;
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
		panDocumentButtons.setPreferredSize(new Dimension(width, height));
	}

	private void jbInit() throws Exception {
		mimeType = "";
		btnDelete.setText("Datei löschen");
		panBottom.setLayout(new BorderLayout());
		panDocumentButtons.setLayout(panDocumentsLayout);
		btnAdd.setText("Datei hinzufügen");
		lbLinkDescription.setText(" Linkbeschreibung  ");
		this.setLayout(new BorderLayout());
		panLinkName.setLayout(panLinkNameLayout);
		panLinkNameLayout.setHgap(5);
		panLinkNameLayout.setVgap(5);
		panDocumentsLayout.setAlignment(FlowLayout.LEFT);
		panDocumentButtons.setBackground(SystemColor.text);
		this.add(panBottom, BorderLayout.SOUTH);
		panBottom.add(panFileAction, BorderLayout.EAST);
		panFileAction.add(btnUpdate, null);
		panFileAction.add(btnAdd, null);
		panFileAction.add(btnDelete, null);
		panFileAction.add(btnPreview, null);
		panBottom.add(panLinkName, BorderLayout.NORTH);
		panLinkName.add(lbLinkDescription, BorderLayout.WEST);
		panLinkName.add(txtDocumentDesc, BorderLayout.CENTER);
		panBottom.add(cbxDisplayTypeInline, BorderLayout.WEST);
		this.add(scrollPane, BorderLayout.CENTER);
		if (showRegionCombo) {
			this.add(cboRegion, BorderLayout.NORTH);
		}

		this.add(getViewSelectPan(), BorderLayout.WEST);
		scrollPane.getViewport().add(tblDocuments, null);
		scrollPane.setTransferHandler(new FileTransferHandler(this));
	}

	private void loadThumbs(Integer unit) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			tblDocumentsModel = new DocumentTableModel();
			tblDocumentSorter = new TableSorter(tblDocumentsModel, tblDocuments.getTableHeader());
			tblDocuments.getSelectionModel().clearSelection();
			tblDocuments.setModel(tblDocumentSorter);
			DocumentSlimValue[] dsva = null;
			if (((CboModel) cboRegion.getSelectedItem()).isUnit) {
				dsva = comm.getAllSlimDocumentValues(unit);
			} else {
				dsva = comm.getAllSlimDocumentValues4ViewComponent(viewComponentId);
			}
			panDocumentButtons.removeAll();
			anzahlItems = 0;
			bgrp = new ButtonGroup();
			for (int i = 0; i < dsva.length; i++) {
				DocumentSlimValue dsv = dsva[i];
				tblDocumentsModel.addRow(dsv);
				PanDocumentSymbol pan = new PanDocumentSymbol();
				// pan.setPreferredSize(new Dimension(95,95));
				pan.getFileButton().setIcon(Utils.getIcon4Extension(Utils.getExtension(dsv.getDocumentName())));
				pan.getFileNameLabel().setText(dsv.getDocumentName());
				pan.getFileButton().setActionCommand(dsv.getDocumentId() + "");
				pan.getFileButton().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						btnFileActionPerformed(e);
					}
				});
				if (intDocId != null && (intDocId.intValue() == dsv.getDocumentId())) {
					pan.getFileButton().doClick();
					mimeType = dsv.getMimeType();
					selectDocument(intDocId);
				}
				panDocumentButtons.add(pan, null);
				anzahlItems++;
				bgrp.add(pan.getFileButton());
				this.setMaxButtonWidth(pan);
			}
			panDocumentButtons.validate();
			panDocumentButtons.repaint();
			resizeScrollpane();
		} catch (Exception exe) {
			log.error("Error loading thumbs", exe);
		}
		this.setCursor(Cursor.getDefaultCursor());
	}

	private void btnFileActionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("CLICK")) {
			Enumeration en = bgrp.getElements();
			while (en.hasMoreElements()) {
				PanDocumentSymbol.JToggleBtt btn = (PanDocumentSymbol.JToggleBtt) en.nextElement();
				if (!btn.isSelected()) {
					btn.unClick();
				} else {
					btn.doClick();
				}
			}
			intDocId = new Integer(bgrp.getSelection().getActionCommand());
			selectDocument(intDocId);
		} else {
			ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "CLICK");
			((PanDocumentSymbol.JToggleBtt) e.getSource()).fireActionPerformedT(ae);
		}
	}

	public void regionSelected() {
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		Integer intSelItem = ((CboModel) this.cboRegion.getSelectedItem()).getRegionId();
		loadThumbs(intSelItem);
		try {
			if (intRootUnit.equals(intSelItem)) {
				if (comm.isUserInRole(UserRights.SITE_ROOT) || intActUnit.equals(intRootUnit)) {
					this.btnDelete.setVisible(true);
					this.btnAdd.setVisible(true);
					this.btnPreview.setVisible(true);
				} else {
					this.btnDelete.setVisible(false);
					this.btnAdd.setVisible(false);
					this.btnPreview.setVisible(false);
				}
			} else {
				this.btnDelete.setVisible(true);
				this.btnAdd.setVisible(true);
				this.btnPreview.setVisible(true);
			}
		} catch (Exception ex) {
		}
		setCursor(Cursor.getDefaultCursor());
	}

	private void btnDeleteActionPerformed(ActionEvent e) {
		try {
			DocumentSlimValue currDoc = null;
			String acc = bgrp.getSelection().getActionCommand();
			int rowInModel = tblDocumentsModel.getRowForDocument(Integer.valueOf(acc));
			if (rowInModel >= 0) {
				currDoc = (DocumentSlimValue) tblDocumentsModel.getValueAt(rowInModel, 4);
			}
			String tmp = acc;
			if (currDoc != null && currDoc.getDocumentName() != null && !"".equalsIgnoreCase(currDoc.getDocumentName())) {
				tmp += " (" + currDoc.getDocumentName() + ")";
			}
			/*
			if (currDoc != null && (currDoc.getUseCountLastVersion() + currDoc.getUseCountPublishVersion()) > 0) {
				JOptionPane.showMessageDialog(this,
						SwingMessages.getString("panel.content.documents.deleteButInUse", tmp), 
						SwingMessages.getString("panel.content.documents.deleteDocument"), 
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			*/
			int ret = JOptionPane.showConfirmDialog(this, Messages.getString("panel.content.documents.deleteThisDocument", tmp), Messages.getString("panel.content.documents.deleteDocument"), JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION) {
				comm.removeDocument(Integer.valueOf(acc).intValue());
				loadThumbs(((CboModel) this.cboRegion.getSelectedItem()).getRegionId());
			}
		} catch (NullPointerException ex) {
		} catch (Exception ex) {
			if (ex.getMessage().contains("validation exception")) {
				JOptionPane.showConfirmDialog(this, rb.getString("panel.content.documents.delete.exception"), rb.getString("panel.content.documents.deleteDocument"), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			} else {
				log.warn("exception on delete document");
				if (log.isDebugEnabled()) {
					log.debug(ex);
				}
			}
		}
	}

	private void btnAddActionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CboModel cb = (CboModel) cboRegion.getSelectedItem();
				isDataActualization = false;
				if (cb.isUnit) {
					upload(cb.getView(), cb.getRegionId(), null, intDocId);
				} else {
					upload(cb.getView(), null, cb.getRegionId(), intDocId);
				}
				loadThumbs(cb.getRegionId());
			}
		});
	}

	/**
	 * Method called when pressing <b>Preview</b> button in Document selection panel. It uses <i>SwingUtilities.invokeLater</i> in order to display a separate {@link JFrame} with the content of the document.
	 * @param e
	 */
	private void btnPreviewActionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DocumentSlimValue currDoc = null;
				String acc = bgrp.getSelection().getActionCommand();
				int rowInModel = tblDocumentsModel.getRowForDocument(Integer.valueOf(acc));
				if (rowInModel >= 0) {
					currDoc = (DocumentSlimValue) tblDocumentsModel.getValueAt(rowInModel, 4);
				}
				String tmp = acc;
				if (currDoc != null && currDoc.getDocumentName() != null && !currDoc.getDocumentName().isEmpty()) {
					tmp += " (" + currDoc.getDocumentName() + ")";
				}
				if (log.isDebugEnabled()) {
					log.info(tmp);
				}
				//load a pdf from a byte buffer
				byte[] data = comm.getDocumentBytes(currDoc.getDocumentId());
				DocumentPreviewComponent component = new PdfPreviewFrame();
				component.setDocumentContent(data);
				component.displayComponent();
			}
		});
	}

	private void btnUpdateActionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CboModel cb = (CboModel) cboRegion.getSelectedItem();
				/**overwrite the content of the document*/
				isDataActualization = true;
				if (cb.isUnit) {
					upload(cb.getView(), cb.getRegionId(), null, intDocId);
				} else {
					upload(cb.getView(), null, cb.getRegionId(), intDocId);
				}
				loadThumbs(cb.getRegionId());
			}
		});
	}

	private void upload(String prosa, Integer unit, Integer viewComponentId, Integer documentId) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		JFileChooser fc = new JFileChooser();
		int ff = fc.getChoosableFileFilters().length;
		FileFilter[] fft = fc.getChoosableFileFilters();
		for (int i = 0; i < ff; i++) {
			fc.removeChoosableFileFilter(fft[i]);
		}
		fc.addChoosableFileFilter(new DocumentFilter());
		fc.setAccessory(new ImagePreview(fc));
		fc.setDialogTitle(prosa);
		fc.setMultiSelectionEnabled(true);
		fc.setCurrentDirectory(Constants.LAST_LOCAL_UPLOAD_DIR);
		int returnVal = fc.showDialog(this, Messages.getString("panel.content.documents.addDocument"));

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] files = fc.getSelectedFiles();
			uploadFiles(files, unit, viewComponentId, documentId);
			Constants.LAST_LOCAL_UPLOAD_DIR = fc.getCurrentDirectory();
		}
		this.setCursor(Cursor.getDefaultCursor());
	}

	/**
	 *
	 * @param files
	 * @param unit
	 * @param documentId
	 */
	public void uploadFiles(File[] files, Integer unit, Integer viewComponentId, Integer documentId) {
		if ((files != null) && (files.length > 0)) {
			for (int i = (files.length - 1); i >= 0; i--) {
				File file = files[i];

				FrmProgressDialog prog = new FrmProgressDialog(Messages.getString("panel.content.documents.addDocument"), Messages.getString("panel.content.upload.ParseFile"), 100);
				prog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				try {
					String fext = Utils.getExtension(file);

					String mimetype = Utils.getMimeType4Extension(fext);
					if (mimetype.equals("")) {
						mimetype = "application/octet-stream";
					}
					prog.setProgress(Messages.getString("panel.content.upload.Uploading"), 50);
					String password=null;
					if(mimetype.equals("application/pdf") && PdfUtils.isPassswordProtected(file)){
						
						DlgPdfDocumentPassword dlgPdfDocumentPassword=new DlgPdfDocumentPassword(password);
						dlgPdfDocumentPassword.setSize(350, 280);
						dlgPdfDocumentPassword.setLocationRelativeTo(UIConstants.getMainFrame());

						dlgPdfDocumentPassword.setModal(true);
						dlgPdfDocumentPassword.setVisible(true);
						dlgPdfDocumentPassword.pack();
						password=dlgPdfDocumentPassword.getPassword();
					}
					int existingDocId = -1;
					if (unit != null) {
						existingDocId = comm.getDocumentIdForNameAndUnit(file.getName(), unit);
					} else if (viewComponentId != null) {
						existingDocId = comm.getDocumentIdForNameAndViewComponent(file.getName(), viewComponentId);
					} else {
						throw new RuntimeException("There must be at least one of unitId or viewComponentId present in method call");
					}
					//this.intDocId = this.comm.addOrUpdateDocument(file, unit, file.getName(), mimetype, documentId);
					if (!isDataActualization) {
						if (existingDocId == 0) {
							try{
								this.intDocId = this.comm.addOrUpdateDocument(file, unit, viewComponentId, file.getName(), mimetype, documentId, password);
							} catch (InvalidSizeException e) {
								JOptionPane.showMessageDialog(UIConstants.getMainFrame(), e.getMessage(), rb.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						} else {
							DlgSaveDocument saveDialog = new DlgSaveDocument(file, unit, file.getName(), mimetype, existingDocId);
							int frameHeight = 280;
							int frameWidth = 350;
							saveDialog.setSize(frameWidth, frameHeight);
							saveDialog.setLocationRelativeTo(UIConstants.getMainFrame());
							saveDialog.setModal(true);
							saveDialog.setVisible(true);
						}
					} else {
						if ((existingDocId == 0) || (file.getName().equalsIgnoreCase(selectedDocName))) {
							try{
								this.intDocId = this.comm.addOrUpdateDocument(file, unit, viewComponentId, file.getName(), mimetype, documentId, password);
							} catch (InvalidSizeException e) {
								JOptionPane.showMessageDialog(UIConstants.getMainFrame(), e.getMessage(), rb.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						} else if ((existingDocId != 0) && (!file.getName().equalsIgnoreCase(selectedDocName))) {
							JOptionPane.showMessageDialog(UIConstants.getMainFrame(), Messages.getString("dialog.saveDocument.imposibleToOverwrite"), rb.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
						}
					}

				} catch (Exception exe) {
					log.error("Error upload document " + file.getName(), exe);
				} finally {
					prog.setProgress(Messages.getString("panel.content.upload.Finished"), 100);
					prog.dispose();
					prog.setCursor(Cursor.getDefaultCursor());
				}
			}
		}
	}

	public void refreshList() {
		loadThumbs(((CboModel) this.cboRegion.getSelectedItem()).getRegionId());
	}

	public Integer getDocumentId() {
		return this.intDocId;
	}

	public void setDocumentId(Integer docid) {
		this.intDocId = docid;
		//loadThumbs(intActUnit);
	}

	public String getDocumentName() {
		try {
			return comm.getDocumentName(getDocumentId().intValue());
		} catch (Exception exe) {
			return "";
		}
	}

	public String getDocumentDescription() {
		return this.txtDocumentDesc.getText();
	}

	public void setDocumentDescription(String docDesc) {
		txtDocumentDesc.setText(docDesc);
	}

	/**
	 * 
	 */
	private class DocumentListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				return;
			}
			if (tblDocuments.getSelectedRow() >= 0) {
				DocumentSlimValue vo = (DocumentSlimValue) tblDocumentSorter.getValueAt(tblDocuments.getSelectedRow(), 4);
				intDocId = vo.getDocumentId();
				selectedDocName = vo.getDocumentName();
				String linkDesc = vo.getDocumentName();
				if (linkDesc.lastIndexOf(".") != -1) {
					linkDesc = linkDesc.substring(0, linkDesc.lastIndexOf("."));
				}
				txtDocumentDesc.setText(linkDesc);
				mimeType = vo.getMimeType();
			}
			btnUpdate.setVisible(false);
			if (intDocId != null) {
				Integer intSelItem = ((CboModel) cboRegion.getSelectedItem()).getRegionId();
				if (intRootUnit.equals(intSelItem)) {
					if (comm.isUserInRole(UserRights.SITE_ROOT) || intActUnit.equals(intRootUnit)) {
						btnUpdate.setVisible(true);
					} else {
						btnUpdate.setVisible(false);
					}
				} else {
					btnUpdate.setVisible(true);
				}
				Enumeration en = bgrp.getElements();
				while (en.hasMoreElements()) {
					PanDocumentSymbol.JToggleBtt btn = (PanDocumentSymbol.JToggleBtt) en.nextElement();
					if (Integer.valueOf(btn.getActionCommand()).intValue() == intDocId.intValue()) {
						btn.doClick();
					} else {
						btn.unClick();
					}
				}
			}
		}
	}

	private void selectDocument(Integer documentId) {
		int rowInModel = tblDocumentsModel.getRowForDocument(documentId);
		if (rowInModel >= 0) {
			int rowInView = tblDocumentSorter.getRowInView(rowInModel);
			tblDocuments.getSelectionModel().setSelectionInterval(rowInView, rowInView);
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
					Component current = scrollPane.getViewport().getView();
					if (current != null) {
						scrollPane.getViewport().remove(current);
					}
					scrollPane.getViewport().add(panDocumentButtons, null);
				}
			});
			btnListView.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Component current = scrollPane.getViewport().getView();
					if (current != null) {
						scrollPane.getViewport().remove(current);
					}
					scrollPane.getViewport().add(tblDocuments, null);
				}
			});
			panViewSelect.addButton(btnListView);
			panViewSelect.addButton(btnSymbolView);
		}
		return panViewSelect;
	}

	private void setMaxButtonWidth(JPanel pan) {
		int currentWidth = pan.getPreferredSize().width;
		if (this.maxButtonWidth < currentWidth) this.maxButtonWidth = currentWidth;
	}

	public JCheckBox getCbxDisplayTypeInline() {
		return cbxDisplayTypeInline;
	}

	public void setDisplayTypeEditable(boolean editable) {
		this.cbxDisplayTypeInline.setVisible(editable);
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

}