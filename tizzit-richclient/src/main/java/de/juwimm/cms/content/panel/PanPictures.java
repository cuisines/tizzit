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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.frame.DlgPictureBrowser;
import de.juwimm.cms.gui.table.PictureTableModel;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.PictureSlimstValue;

/**
 * 
 * @author <a href="sascha-matthias.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @author <a href="carsten.schalm@juwimm.com">Carsten Schalm</a>
 * @version $Id: PanDocuments.java 919 2010-09-02 16:10:39Z rene.hertzfeldt $
 */
public class PanPictures extends JPanel {
	private static Logger log = Logger.getLogger(DlgPictureBrowser.class);
	private final ResourceBundle rb = Constants.rb;
	private final EventListenerList listenerList = new EventListenerList();
	private final JPanel panBottom = new JPanel();
	private final JButton btnDelete = new JButton();
	private final JButton btnOk = new JButton();
	private final JButton btnCancel = new JButton();
	private final JScrollPane spMain = new JScrollPane();
	private final JPanel panPictureButtons = new JPanel();
	private final JPanel panPictures = new JPanel();
	private final JProgressBar progressBar = new JProgressBar();
	private final JPanel panLeftBottomButton = new JPanel();
	private final JPanel panRightBottomButton = new JPanel();
	private ButtonGroup bgrp = new ButtonGroup();

	private final int intActUnit;
	private final int intRootUnit;
	private Integer viewComponentId;
	private int anzahlItems;
	private boolean showRegionCombo = true;
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private final JComboBox cboRegion = new JComboBox();

	private final JTable tblPictures = new JTable();
	private PictureTableModel tblPicturesModel = null;
	private TableSorter tblPictureSorter = null;
	private PanViewSelect panViewSelect = null;
	private JToggleButton btnListView = null;
	private JToggleButton btnSymbolView = null;
	private final SimpleDateFormat sdf = new SimpleDateFormat(rb.getString("General.ShortDateTimeFormat"));
	private Integer selectedPictureId = null;

	private Thread loadThumbsRunnerThread = null;

	/**
	 * 
	 */
	public final class CboModel {
		private final String strView;
		private final int intRegionId;
		private final boolean isUnit;

		public CboModel(String view, int regionid, boolean isUnit) {
			this.strView = view;
			this.intRegionId = regionid;
			this.isUnit = isUnit;
		}

		public String getView() {
			return this.strView;
		}

		public int getRegionId() {
			return this.intRegionId;
		}

		public boolean isUnit() {
			return isUnit;
		}

		@Override
		public String toString() {
			return this.strView;
		}
	}

	public void resizeScrollpane() {
		int width = ((int) panPictures.getSize().getWidth()) - 20;
		int fitinrow = 1;
		int height = 1;
		double dblSmall = 0;
		try {
			if (width > 100) fitinrow = width / 100;
		} catch (ArithmeticException e) {
			log.error("caught ArithmeticException ", e);
		}
		try {
			if (anzahlItems != 0) dblSmall = ((double) anzahlItems / (double) fitinrow) - (anzahlItems / fitinrow);
		} catch (ArithmeticException e) {
			log.error("caught ArithmeticException ", e);
		}
		if (dblSmall < 1 && dblSmall > 0) {
			try {
				height = ((anzahlItems / fitinrow) + 1) * 100;
			} catch (ArithmeticException e) {
				log.error("caught ArithmeticException ", e);
			}
		} else {
			try {
				height = (anzahlItems / fitinrow) * 100;
			} catch (ArithmeticException e) {
				log.error("caught ArithmeticException ", e);
			}
		}
		panPictureButtons.setPreferredSize(new Dimension(width, height));
		panPictureButtons.setSize(new Dimension(width, height));
	}

	public void addSaveActionListener(ActionListener al) {
		this.listenerList.add(ActionListener.class, al);
	}

	public void fireSaveActionListener(ActionEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			((ActionListener) listeners[i + 1]).actionPerformed(e);
		}
	}

	public PanPictures(boolean showRegionCombo, Integer viewComponentId) {
		this.viewComponentId = viewComponentId;
		this.showRegionCombo = showRegionCombo;
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
		tblPictures.getSelectionModel().addListSelectionListener(new PictureListSelectionListener());
		tblPictures.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizeScrollpane();
			}
		});
		intActUnit = ((ContentManager) getBean(Beans.CONTENT_MANAGER)).getActUnitId();
		intRootUnit = ((ContentManager) getBean(Beans.CONTENT_MANAGER)).getRootUnitId();

		if (showRegionCombo) {
			this.cboRegion.addItem(new CboModel(Messages.getString("DlgPictureBrowser.regionForThisUnit"), intActUnit, true));
			this.cboRegion.addItem(new CboModel(Messages.getString("DlgPictureBrowser.regionForAllUnits"), intRootUnit, true));
		}
		this.cboRegion.addItem(new CboModel(Messages.getString("DlgPictureBrowser.regionForThisComponent"), this.viewComponentId, false));

	}

	private void jbInit() throws Exception {
		//this.setIconImage(de.juwimm.cms.util.UIConstants.CMS.getImage());
		btnDelete.setText(Messages.getString("DlgPictureBrowser.deletePicture"));
		btnDelete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteActionPerformed(e);
			}
		});
		panBottom.setLayout(new BorderLayout());
		btnOk.setText(Messages.getString("DlgPictureBrowser.choose"));
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOkActionPerformed(e);
			}
		});
		btnCancel.setText(Messages.getString("DlgPictureBrowser.cancel"));
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed(e);
			}
		});
		progressBar.setVisible(false);
		panPictures.setLayout(new BorderLayout());
		panPictures.add(spMain, BorderLayout.CENTER);
		panPictures.add(progressBar, BorderLayout.SOUTH);
		panPictureButtons.setLayout(new FlowLayout());
		cboRegion.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cboRegionActionPerformed(e);
			}
		});
		this.add(panBottom, BorderLayout.SOUTH);
		panBottom.add(panLeftBottomButton, BorderLayout.WEST);
		if (showRegionCombo) {
			panLeftBottomButton.add(btnOk, null);
		}
		panBottom.add(panRightBottomButton, BorderLayout.EAST);
		panRightBottomButton.add(btnDelete, null);
		if (showRegionCombo) {
			panRightBottomButton.add(btnCancel, null);
		}
		this.setLayout(new BorderLayout());
		this.add(panPictures, BorderLayout.CENTER);
		if (showRegionCombo) {
			this.add(cboRegion, BorderLayout.NORTH);
		}
		this.add(getViewSelectPan(), BorderLayout.WEST);
		this.add(panBottom, BorderLayout.SOUTH);
		spMain.getViewport().add(panPictureButtons, null);
		spMain.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//		this.getRootPane().setDefaultButton(btnOk);
	}

	/**
	 * Runnable for asynchronous loading a list off all pictures for a specific unit.
	 * 
	 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
	 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @version $Id: DlgPictureBrowser.java 610 2009-11-05 14:05:30Z florin.zalum@gmail.com $
	 */
	private class LoadPictureListRunner implements Runnable {
		private int unit = -1;

		public LoadPictureListRunner(int unit) {
			this.unit = unit;
		}

		public void run() {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try {
				PictureSlimstValue[] pics = null;
				if (!showRegionCombo || !((CboModel) cboRegion.getSelectedItem()).isUnit()) {
					pics = comm.getAllSlimPictureValues4ViewComponent(viewComponentId);
				} else {
					pics = comm.getAllSlimPictureValues(unit);
				}
				tblPicturesModel = new PictureTableModel();
				tblPictureSorter = new TableSorter(tblPicturesModel, tblPictures.getTableHeader());
				tblPictures.getSelectionModel().clearSelection();
				tblPictures.setModel(tblPictureSorter);
				tblPicturesModel.addRows(pics);
				{
					// clean-up
					Component[] toggBtns = panPictureButtons.getComponents();
					for (int i = (toggBtns.length) - 1; i >= 0; i--) {
						if (toggBtns[i] instanceof AbstractButton) {
							ActionListener[] listeners = ((AbstractButton) toggBtns[i]).getActionListeners();
							for (int l = (listeners.length - 1); l >= 0; l--) {
								((AbstractButton) toggBtns[i]).removeActionListener(listeners[l]);
							}
						}
					}
					panPictureButtons.removeAll();
					anzahlItems = 0;
					bgrp = new ButtonGroup();
				}
				loadThumbsRunnerThread = new Thread(new LoadThumbsRunner(pics));
				loadThumbsRunnerThread.setName("LoadThumbsRunner");
				loadThumbsRunnerThread.setPriority(Thread.MIN_PRIORITY);
				loadThumbsRunnerThread.start();
			} catch (Exception exe) {
				log.error("Error loading list of pictures: ", exe);
			} finally {
				setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	/**
	 * Runnable for asynchronous loading of thumbnails for desired picures.<br/>
	 * Creates a ToggleButton for each picture and adds it to panPictureButtons.
	 * 
	 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
	 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @version $Id: DlgPictureBrowser.java 610 2009-11-05 14:05:30Z florin.zalum@gmail.com $
	 */
	private class LoadThumbsRunner implements Runnable {
		private PictureSlimstValue[] pics = null;

		public LoadThumbsRunner(PictureSlimstValue[] pics) {
			this.pics = pics;
		}

		public void run() {
			try {
				progressBar.setMaximum(pics.length - 1);
				if (pics.length == 0) {
					btnDelete.setEnabled(false);
					btnOk.setEnabled(false);
				} else if (pics.length > 0) {
					btnDelete.setEnabled(true);
					btnOk.setEnabled(true);
				}
				int pbState = 0;
				if (pics.length > 0) {
					for (int i = (pics.length - 1); i >= 0; i--, pbState++) {
						progressBar.setValue(pbState);
						JToggleButton togg = new JToggleButton();
						togg.setPreferredSize(new Dimension(95, 95));
						Icon ico = new ImageIcon(comm.getThumbnail(pics[i].getPictureId()));
						togg.setIcon(ico);
						togg.setActionCommand("" + pics[i].getPictureId());
						togg.setToolTipText(pics[i].getPictureName() != null ? pics[i].getPictureName() + " - " + sdf.format(new Date(pics[i].getTimeStamp())) : sdf.format(new Date(pics[i].getTimeStamp())));
						togg.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								btnSelectActionPerformed(e);
							}
						});
						panPictureButtons.add(togg, null);
						anzahlItems++;
						bgrp.add(togg);
						if (selectedPictureId != null && pics[i].getPictureId() == selectedPictureId.intValue()) {
							togg.doClick();
						}
						panPictureButtons.validate();
						panPictureButtons.repaint();
					}
				}
				progressBar.setVisible(false);
				resizeScrollpane();
			} catch (Exception exe) {
				log.error("Error loading thumbs", exe);
			}
		}
	}

	public void setPictureId(int pictureId) {
		this.selectedPictureId = Integer.valueOf(pictureId);
	}

	public Integer getPictureId() {
		return new Integer(this.selectedPictureId);
	}

	private void selectPicture(Integer pictureId) {
		int rowInModel = tblPicturesModel.getRowForPicture(pictureId);
		if (rowInModel >= 0) {
			int rowInView = tblPictureSorter.getRowInView(rowInModel);
			tblPictures.getSelectionModel().setSelectionInterval(rowInView, rowInView);
		}
	}

	private void btnSelectActionPerformed(ActionEvent e) {
		selectedPictureId = new Integer(bgrp.getSelection().getActionCommand());
		selectPicture(selectedPictureId);
	}

	void btnCancelActionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	void cboRegionActionPerformed(ActionEvent e) {
		int intSelItem = -1;
		if (showRegionCombo) {
			intSelItem = ((CboModel) this.cboRegion.getSelectedItem()).getRegionId();
		}
		Thread t = new Thread(new LoadPictureListRunner(intSelItem));
		t.setPriority(Thread.NORM_PRIORITY);
		t.setName("LoadPictureListRunner");
		t.start();
		try {
			if (intRootUnit == intSelItem) {
				if (comm.isUserInRole(UserRights.SITE_ROOT) || comm.isUserInUnit(intRootUnit)) {
					this.btnDelete.setVisible(true);
				} else {
					this.btnDelete.setVisible(false);
				}
			} else {
				this.btnDelete.setVisible(true);
			}
		} catch (Exception ex) {
			log.error("Error in 'isUserInRole' ", ex);
		}
	}

	void btnOkActionPerformed(ActionEvent e) {
		try {
			String acc = bgrp.getSelection().getActionCommand();
			if (log.isDebugEnabled()) log.debug(acc);
			ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, acc);
			this.fireSaveActionListener(ae);
			this.setVisible(false);
		} catch (NullPointerException ex) {
			log.error("Catched NullPointerException ", ex);
		}
	}

	void btnDeleteActionPerformed(ActionEvent e) {
		try {
			String acc = bgrp.getSelection().getActionCommand();
			if (log.isDebugEnabled()) log.debug(acc);
			int ret = JOptionPane.showConfirmDialog(this, Messages.getString("DlgPictureBrowser.reallyDeleteThisPicture", acc), Messages.getString("DlgPictureBrowser.deletePicture"), JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION) {
				comm.removePicture(new Integer(acc).intValue());
				Thread t = new Thread(new LoadPictureListRunner(((CboModel) this.cboRegion.getSelectedItem()).getRegionId()));
				t.setPriority(Thread.NORM_PRIORITY);
				t.start();
			}
		} catch (Exception ex) {
			if (ex.getMessage().contains("validation exception")) {
				JOptionPane.showConfirmDialog(this, rb.getString("panel.content.picture.delete.exception"), rb.getString("DlgPictureBrowser.deletePicture"), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			} else {
				log.warn("exception on delete document");
				if (log.isDebugEnabled()) {
					log.debug(ex);
				}
			}
		}
	}

	private JPanel getViewSelectPan() {
		if (panViewSelect == null) {
			panViewSelect = new PanViewSelect();
			panViewSelect.setPreferredSize(new Dimension(26, spMain.getHeight()));
			btnListView = new JToggleButton(UIConstants.BTN_LIST_VIEW, true);
			btnListView.setToolTipText(rb.getString("PanDocument.view.list"));
			btnListView.setPreferredSize(new Dimension(UIConstants.BTN_LIST_VIEW.getIconHeight() + 10, UIConstants.BTN_LIST_VIEW.getIconWidth() + 10));
			btnSymbolView = new JToggleButton(UIConstants.BTN_SYMBOL_VIEW, false);
			btnSymbolView.setToolTipText(rb.getString("PanDocument.view.symbol"));
			btnSymbolView.setPreferredSize(new Dimension(UIConstants.BTN_SYMBOL_VIEW.getIconHeight() + 10, UIConstants.BTN_SYMBOL_VIEW.getIconWidth() + 10));
			btnSymbolView.setSelected(true);
			btnSymbolView.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Component current = spMain.getViewport().getView();
					if (current != null) {
						spMain.getViewport().remove(current);
					}
					resizeScrollpane();
					spMain.getViewport().add(panPictureButtons, null);
					if (loadThumbsRunnerThread.isAlive()) progressBar.setVisible(true);
					resizeScrollpane();
				}
			});
			btnListView.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Component current = spMain.getViewport().getView();
					if (current != null) {
						spMain.getViewport().remove(current);
					}
					progressBar.setVisible(false);
					spMain.getViewport().add(tblPictures, null);
				}
			});
			panViewSelect.addButton(btnListView);
			panViewSelect.addButton(btnSymbolView);
		}
		return panViewSelect;
	}

	private class PictureListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				return;
			}
			if (tblPictures.getSelectedRow() >= 0) {
				PictureSlimstValue vo = (PictureSlimstValue) tblPictureSorter.getValueAt(tblPictures.getSelectedRow(), 6);
				selectedPictureId = vo.getPictureId();
			}
			if (selectedPictureId != null) {
				Enumeration en = bgrp.getElements();
				while (en.hasMoreElements()) {
					JToggleButton btn = (JToggleButton) en.nextElement();
					btn.setSelected(Integer.valueOf(btn.getActionCommand()).intValue() == selectedPictureId.intValue());
				}
			}
		}
	}

	public void setShowRegionCombo(boolean showRegionCombo) {
		this.showRegionCombo = showRegionCombo;
	}

	public void refresh() {
		cboRegionActionPerformed(null);
	}
	public void addCancelListener(ActionListener listener){
		btnCancel.addActionListener(listener);
	}
}