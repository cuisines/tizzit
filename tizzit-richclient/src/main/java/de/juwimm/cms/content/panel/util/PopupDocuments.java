package de.juwimm.cms.content.panel.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.content.panel.PanDocuments;
import de.juwimm.cms.gui.controls.PopupComponent;

public class PopupDocuments extends PopupComponent {
	private PanDocuments panDocuments;
	private final ResourceBundle rb = Constants.rb;
	private JPanel content = new JPanel();


	public PopupDocuments(JComponent owner) {
		super(new JPanel(), true, 0, true);
		this.setContent(content);
		content.setBorder(new BevelBorder(BevelBorder.RAISED));
		content.setSize(600, 300);
		content.setLayout(new BorderLayout());
		panDocuments = new PanDocuments(false);
		panDocuments.setSize(600, 300);
		panDocuments.setPreferredSize(new Dimension(600, 300));
		panDocuments.setDocumentId(null);
		panDocuments.setDocumentDescription("");
//		panDocuments.resizeScrollpane();
//		panDocuments.regionSelected();
		content.add(panDocuments, BorderLayout.CENTER);

		int x = owner.getWidth() - panDocuments.getWidth();
		int y = owner.getHeight();
		showPopup(owner, x, y);

	}

}
