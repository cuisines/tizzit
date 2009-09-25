package de.juwimm.cms.content.frame;

import static de.juwimm.cms.client.beans.Application.getBean;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.util.Communication;

public class DlgSaveDocument extends JDialog {
	private static final long serialVersionUID = -84852827972953607L;
	private static Logger log = Logger.getLogger(DlgPictureBrowser.class);
	protected Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private ResourceBundle rb = Constants.rb;
	private JRadioButton btnFileOverwrite = new JRadioButton();
	private JRadioButton btnFileNew = new JRadioButton();
	private JTextField txtFileName = new JTextField();
	private JPanel panButtonPanel = new JPanel();
	private JButton btnSave = new JButton();
	private JButton btnCancel = new JButton();
	private EventListenerList listenerList = new EventListenerList();
	private File file;
	private Integer unitId;
	private String fileName;
	private String mimetype;
	private Integer documentId;

	public DlgSaveDocument(File file, Integer unitId, String fileName, String mimeType, Integer documentId) {
		this.file = file;
		this.unitId = unitId;
		this.fileName = fileName;
		this.mimetype = mimeType;
		this.documentId = documentId;
		init();
	}

	private void init() {
		btnFileNew.setMaximumSize(new Dimension(200, 27));
		btnFileNew.setMinimumSize(new Dimension(200, 27));
		btnFileNew.setPreferredSize(new Dimension(200, 27));
		btnFileNew.setToolTipText(rb.getString("dialog.saveItem.btnFileNew.tooltiptext"));
		btnFileNew.setText(rb.getString("dialog.saveItem.btnFileNew"));
		btnFileNew.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				radioButtonChangedActionFired(e);
			}
		});

		btnFileOverwrite.setMaximumSize(new Dimension(200, 27));
		btnFileOverwrite.setMinimumSize(new Dimension(200, 27));
		btnFileOverwrite.setPreferredSize(new Dimension(200, 27));
		btnFileOverwrite.setToolTipText(rb.getString("dialog.saveItem.btnFileOverwrite.tooltiptext"));
		btnFileOverwrite.setText(rb.getString("dialog.saveItem.btnFileOverwrite"));
		btnFileOverwrite.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				radioButtonChangedActionFired(e);
			}
		});

		ButtonGroup saveOptions = new ButtonGroup();
		saveOptions.add(btnFileOverwrite);
		saveOptions.add(btnFileNew);
		btnFileOverwrite.setSelected(true);

		txtFileName.setMaximumSize(new Dimension(180, 27));
		txtFileName.setMinimumSize(new Dimension(180, 27));
		txtFileName.setPreferredSize(new Dimension(180, 27));

		btnSave.setMaximumSize(new Dimension(95, 27));
		btnSave.setMinimumSize(new Dimension(95, 27));
		btnSave.setPreferredSize(new Dimension(95, 27));
		btnSave.setToolTipText(rb.getString("dialog.saveItem.btnSave.tooltiptext"));
		btnSave.setText(rb.getString("dialog.saveItem.btnSave"));
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSaveActionPerformed(e);
			}
		});

		btnCancel.setMaximumSize(new Dimension(95, 27));
		btnCancel.setMinimumSize(new Dimension(95, 27));
		btnCancel.setPreferredSize(new Dimension(95, 27));
		btnCancel.setToolTipText(rb.getString("dialog.saveItem.btnCancel.tooltiptext"));
		btnCancel.setText(rb.getString("dialog.saveItem.btnCancel"));
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed(e);
			}
		});

		panButtonPanel.setMaximumSize(new Dimension(245, 30));
		panButtonPanel.setMinimumSize(new Dimension(245, 30));
		panButtonPanel.setPreferredSize(new Dimension(245, 30));
		panButtonPanel.setLayout(new GridBagLayout());
		panButtonPanel.add(btnSave, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 35), 0, 0));
		panButtonPanel.add(btnCancel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));

		this.setTitle(rb.getString("dialog.save"));
		this.setLayout(new GridBagLayout());
		this.add(btnFileOverwrite, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 0, 0), 0, 0));
		this.add(btnFileNew, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 0, 0), 0, 0));
		this.add(txtFileName, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 40, 0, 0), 0, 0));
		this.add(panButtonPanel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(20, 5, 0, 0), 0, 0));
		this.setResizable(false);
	}

	private void radioButtonChangedActionFired(ChangeEvent e) {
		if (btnFileNew.isSelected()) {
			txtFileName.setEnabled(true);
		} else {
			txtFileName.setEnabled(false);
			txtFileName.setText("");
			txtFileName.setBackground(Color.white);
		}
	}

	private void btnSaveActionPerformed(ActionEvent e) {
		if (btnFileNew.isSelected()) {
			String localFileName = txtFileName.getText().trim();
			String filePattern = "[\\w.-_+]+";
			if (!localFileName.isEmpty() && Pattern.matches(filePattern, localFileName) && checkNewName(localFileName, unitId)) {
				try {
					documentId = comm.addOrUpdateDocument(file, unitId, localFileName, mimetype, null);
				} catch (Exception ex) {
					log.error("Could not create Document for unit: " + unitId, ex);
				}
			} else {
				txtFileName.setBackground(Color.RED);
				return;
			}
		}
		if (btnFileOverwrite.isSelected()) {
			try {
				comm.addOrUpdateDocument(file, unitId, fileName, mimetype, documentId);
			} catch (Exception ex) {
				log.error("Could not update Document: " + documentId, ex);
			}
		}
		//this.fireSaveActionListener(new ActionEvent(this, documentId, "" + documentId));
		this.setVisible(false);
	}

	private void btnCancelActionPerformed(ActionEvent e) {
		this.dispose();
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

	private boolean checkNewName(String name, Integer unitId) {
		if (comm.getDocumentIdForNameAndUnit(name, unitId) == 0) {
			return true;
		} else {
			return false;
		}
	}
}
