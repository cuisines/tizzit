/*
 * Created on 09.08.2004
 */
package de.muntjak.tinylookandfeel.controlpanel;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * TestDialog
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class TestDialog extends JDialog {

	TestDialog(Frame owner) {
		super(owner, "JDialog", true);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 32));		
		JLabel l = new JLabel("<html><center>" +
			"A <font color=\"#0000ff\">JDialog</font> for testing<br>" +
			"dialog decoration.");
			
		p.add(l);
		
		getContentPane().add(p, BorderLayout.CENTER);
		
		p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 16));
		JButton b = new JButton("Close");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestDialog.this.dispose();
			}
		});
		
		p.add(b);
		
		getContentPane().add(p, BorderLayout.SOUTH);
		
		pack();
		
		int w = Math.max(320, getWidth() + 32), h = getHeight();		
		Point loc = new Point(
			owner.getLocationOnScreen().x + (owner.getWidth() - w) / 2,
			owner.getLocationOnScreen().y + (owner.getHeight() - w) * 2 / 3);
			
		setSize(w, h);
		setLocation(loc);
		setVisible(true);
	}
}
