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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.util.Communication;

public class DlgPdfDocumentPassword extends JDialog {
	private static final long serialVersionUID = -84852827972953607L;
	private static Logger log = Logger.getLogger(DlgPdfDocumentPassword.class);
	private ResourceBundle rb = Constants.rb;
	private JPanel panButtonPanel = new JPanel();
	private JButton btnSave = new JButton();
	private JButton btnCancel = new JButton();
	private JPasswordField fieldPassword1=new JPasswordField();
	private JPasswordField fieldPassword2=new JPasswordField();
	private JLabel labelMessage=new JLabel(rb.getString("DlgPdfDocumentPassword.info.message"));
	private JLabel labelPassword1=new JLabel(rb.getString("DlgPdfDocumentPassword.info.password1"));
	private JLabel labelPassword2=new JLabel(rb.getString("DlgPdfDocumentPassword.info.password2"));
	
	private String password;

	public DlgPdfDocumentPassword(String password) {
		this.password=password;
		init();
	}

	private void init() {
		
		fieldPassword1.setMaximumSize(new Dimension(180, 27));
		fieldPassword1.setMinimumSize(new Dimension(180, 27));
		fieldPassword1.setPreferredSize(new Dimension(180, 27));

		fieldPassword2.setMaximumSize(new Dimension(180, 27));
		fieldPassword2.setMinimumSize(new Dimension(180, 27));
		fieldPassword2.setPreferredSize(new Dimension(180, 27));

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
		this.add(labelMessage,     new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 0, 0), 0, 0));
		this.add(labelPassword1,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 0, 0), 0, 0));
		this.add(fieldPassword1,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 40, 0, 0), 0, 0));
		this.add(labelPassword2,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 0, 0), 0, 0));
		this.add(fieldPassword2,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 40, 0, 0), 0, 0));
		this.add(panButtonPanel,   new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(20, 5, 0, 0), 0, 0));
		this.setResizable(true);
		this.pack();
	}

	private void btnSaveActionPerformed(ActionEvent e) {
		log.debug("save pressed");
		if(!fieldPassword1.getText().equals(fieldPassword2.getText())){
			return;
		}
		password=fieldPassword1.getText();
		this.setVisible(false);
	}

	private void btnCancelActionPerformed(ActionEvent e) {
		this.dispose();
	}
	
	public String getPassword(){
		return password;
	}
	
}
