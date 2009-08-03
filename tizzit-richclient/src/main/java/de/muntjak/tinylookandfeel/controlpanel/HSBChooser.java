/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*	Tiny Look and Feel                                                         *
*                                                                              *
*  (C) Copyright 2003 - 2007 Hans Bickel                                       *
*                                                                              *
*   For licensing information and credits, please refer to the                 *
*   comment in file de.muntjak.tinylookandfeel.TinyLookAndFeel                 *
*                                                                              *
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package de.muntjak.tinylookandfeel.controlpanel;

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.text.*;

import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * ColorizeDialog
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class HSBChooser extends JDialog {

	private static HSBChooser myInstance;
	private static int hue, sat, bri;
	private boolean result = false;
	private ControlPanel.HSBField hsbField;
	private JSlider hueSlider, satSlider, briSlider;
	private JTextField hueField, satField, briField;
	private JCheckBox preserveGrey;
	private boolean keyInput = false;
	private boolean valueIsAdjusting = false;
	
	public HSBChooser(Frame frame) {
		super(frame, "Hue/Saturation/Lightness", true);		
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		setupUI(frame);
	}
	
	private void setupUI(Frame frame) {
		ChangeListener sliderAction = new SliderAction();
		
		JPanel p1 = new JPanel(new BorderLayout(12, 0));
		p1.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		JPanel p2 = new JPanel(new GridLayout(3, 1, 0, 8));
		JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 4));
		JPanel p4 = new JPanel(new BorderLayout(4, 0));
		
		// sliders
		JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		p4.add(new JLabel("Hue"), BorderLayout.NORTH);
		hueSlider = new JSlider(0, 360, hue);
		hueSlider.addChangeListener(sliderAction);
		hueSlider.setMajorTickSpacing(180);
		hueSlider.setPaintTicks(true);
		p4.add(hueSlider, BorderLayout.CENTER);
		
		hueField = new JTextField("" + hueSlider.getValue(), 4);
		hueField.getDocument().addDocumentListener(new HueInputListener());
		hueField.addKeyListener(new ArrowKeyAction(hueField, 0, 360));
		hueField.setHorizontalAlignment(JTextField.CENTER);
		p5.add(hueField);
		p4.add(p5, BorderLayout.EAST);
		
		p2.add(p4);
		
		p5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		p4 = new JPanel(new BorderLayout(4, 0));
		p4.add(new JLabel("Saturation"), BorderLayout.NORTH);
		satSlider = new JSlider(0, 100, sat);
		satSlider.addChangeListener(sliderAction);
		satSlider.setMajorTickSpacing(50);
		satSlider.setPaintTicks(true);
		p4.add(satSlider, BorderLayout.CENTER);
		
		satField = new JTextField("" + satSlider.getValue(), 4);
		satField.getDocument().addDocumentListener(new SatInputListener());
		satField.addKeyListener(new ArrowKeyAction(satField, 0, 100));
		satField.setHorizontalAlignment(JTextField.CENTER);
		p5.add(satField);
		p4.add(p5, BorderLayout.EAST);
		
		p2.add(p4);
		
		p5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		p4 = new JPanel(new BorderLayout(4, 0));
		p4.add(new JLabel("Lightness"), BorderLayout.NORTH);
		briSlider = new JSlider(-100, 100, bri);
		briSlider.addChangeListener(sliderAction);
		briSlider.setMajorTickSpacing(100);
		briSlider.setPaintTicks(true);
		p4.add(briSlider, BorderLayout.CENTER);
		
		briField = new JTextField("" + briSlider.getValue(), 4);
		briField.getDocument().addDocumentListener(new BriInputListener());
		briField.addKeyListener(new ArrowKeyAction(briField, -100, 100));
		briField.setHorizontalAlignment(JTextField.CENTER);
		p5.add(briField);
		p4.add(p5, BorderLayout.EAST);
		
		p2.add(p4);
		p3.add(p2);
		p1.add(p3, BorderLayout.CENTER);
		
		p5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 6));
		preserveGrey = new JCheckBox("Preserve grey values", true);
		preserveGrey.addActionListener(new PreserveAction());
		p5.add(preserveGrey);
		
		p1.add(p5, BorderLayout.SOUTH);

		getContentPane().add(p1, BorderLayout.CENTER);
		
		// buttons
		p3 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
		p3.setBorder(new EtchedBorder());
		
		JButton b = new JButton("Cancel");
		b.addActionListener(new CancelAction());
		p3.add(b);
		
		b = new JButton("OK");
		getRootPane().setDefaultButton(b);
		b.addActionListener(new OKAction());
		p3.add(b);
		
		getContentPane().add(p3, BorderLayout.SOUTH);
		
		pack();
		
		Dimension size = getSize();
		setLocation(frame.getLocationOnScreen().x + 
			(frame.getWidth() - getSize().width) / 2,
			frame.getLocationOnScreen().y + 
			(frame.getHeight() - getSize().height) / 2);		
	}

	public static boolean showColorizeDialog(Frame frame, ControlPanel.HSBField cf) {
		if(myInstance == null) {
			myInstance = new HSBChooser(frame);
		}
		
		myInstance.hsbField = cf;
		myInstance.result = false;
		myInstance.setParams(cf);
		myInstance.setVisible(true);
		
		return myInstance.result;
	}
	
	public static void deleteInstance() {
		myInstance = null;
	}
	
	public static int getHue() {
			return hue;
		}
		
	public static int getSaturation() {
		return sat;
	}

	public static int getBrightness() {
		return bri;
	}
	
	public static boolean isPreserveGrey() {
		return myInstance.preserveGrey.isSelected();
	}
	
	private void setParams(ControlPanel.HSBField cf) {
		hue = cf.getHue();
		sat = cf.getSaturation();
		bri = cf.getBrightness();
		
		valueIsAdjusting = true;
		hueSlider.setValue(hue);
		satSlider.setValue(sat);
		briSlider.setValue(bri);
		preserveGrey.setSelected(cf.isPreserveGrey());
		valueIsAdjusting = false;
		
		performAction();
	}
	
	private void performAction() {
		if(valueIsAdjusting) return;
		
		hsbField.setHue(hue);
		hsbField.setSaturation(sat);
		hsbField.setBrightness(bri);
		hsbField.setPreserveGrey(preserveGrey.isSelected());
		ActionEvent ae = new ActionEvent(hsbField, Event.ACTION_EVENT, "");
		hsbField.getAction().actionPerformed(ae);
	}

	class SliderAction implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			if(!keyInput) {
				if(e.getSource().equals(hueSlider)) {
					hue = hueSlider.getValue();
					hueField.setText("" + hue);					
				}
				else if(e.getSource().equals(briSlider)) {
					bri = briSlider.getValue();
					briField.setText("" + bri);
				}
				else if(e.getSource().equals(satSlider)) {
					sat = satSlider.getValue();
					satField.setText("" + sat);
				}
				
				performAction();
			}
		}
	}
	
	class HueInputListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
		}
		
		public void insertUpdate(DocumentEvent e) {
			update(e);
		}

		public void removeUpdate(DocumentEvent e) {
			update(e);
		}
		
		private void update(DocumentEvent e) {
			Document doc = e.getDocument();
			
			try {
				String text = doc.getText(0, doc.getLength());
				
				try {
					int val = Integer.parseInt(text);
					
					keyInput = true;
					hue = val;
					hueSlider.setValue(val);
					keyInput = false;
					performAction();
				} catch(NumberFormatException ignore) {}
			} catch (BadLocationException ignore) {}
		}
	}
	
	class SatInputListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
		}
		
		public void insertUpdate(DocumentEvent e) {
			update(e);
		}

		public void removeUpdate(DocumentEvent e) {
			update(e);
		}
		
		private void update(DocumentEvent e) {
			Document doc = e.getDocument();
			
			try {
				String text = doc.getText(0, doc.getLength());
				
				try {
					int val = Integer.parseInt(text);
					
					keyInput = true;
					sat = val;
					satSlider.setValue(val);
					keyInput = false;
					performAction();
				} catch(NumberFormatException ignore) {}
			} catch (BadLocationException ignore) {}
		}
	}
	
	class BriInputListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
		}
		
		public void insertUpdate(DocumentEvent e) {
			update(e);
		}

		public void removeUpdate(DocumentEvent e) {
			update(e);
		}
		
		private void update(DocumentEvent e) {
			Document doc = e.getDocument();
			
			try {
				String text = doc.getText(0, doc.getLength());
				
				try {
					int val = Integer.parseInt(text);
					
					keyInput = true;
					bri = val;
					briSlider.setValue(val);
					keyInput = false;
					performAction();
				} catch(NumberFormatException ignore) {}
			} catch (BadLocationException ignore) {}
		}
	}
	
	class ArrowKeyAction extends KeyAdapter implements ActionListener {
		
		private JTextField theField;
		private javax.swing.Timer keyTimer;
		private int step, min, max;
		
		ArrowKeyAction(JTextField field, int min, int max) {
			theField = field;
			this.min = min;
			this.max = max;
			keyTimer = new javax.swing.Timer(20, this);
		}
		
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == 38) {	// up => decrease
				step = 1;
				if(e.getModifiers() == KeyEvent.SHIFT_MASK) {
					step = 10;
				}
				
				changeVal();
				keyTimer.setInitialDelay(300);
				keyTimer.start();
			}
			else if(e.getKeyCode() == 40) {	// up => increase
				step = -1;
				if(e.getModifiers() == KeyEvent.SHIFT_MASK) {
					step = -10;
				}
				
				changeVal();
				keyTimer.setInitialDelay(300);
				keyTimer.start();
			}
		}
		
		public void keyReleased(KeyEvent e) {
			keyTimer.stop();
		}
		
		public void actionPerformed(ActionEvent e) {
			changeVal();
		}
		
		private void changeVal() {
			int val = Integer.parseInt(theField.getText()) + step;
			
			if(val > max) val = max;
			else if(val < min) val = min;

			theField.setText("" + val);
		}
	}
	
	class PreserveAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			performAction();
		}
	}
	
	class OKAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			result = true;
			setVisible(false);
		}
	}
	
	class CancelAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			result = false;
			setVisible(false);
		}
	}
}