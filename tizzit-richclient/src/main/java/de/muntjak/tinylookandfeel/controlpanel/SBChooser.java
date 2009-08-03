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
 * SBChooser
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class SBChooser extends JDialog {

	private static SBChooser myInstance;
	private static int sat, bri;
	private Color reference, outColor;
	private JSlider satSlider, briSlider;
	private JTextField satField, briField;
	private JTextField redField, greenField, blueField;
	private TwoColorField twoColorField;
	private ColorField referenceField;
	private boolean keyInput = false;
	private boolean valueIsAdjusting = false;
	
	public SBChooser(Frame frame) {
		super(frame, "Saturation/Lightness", true);		
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		setupUI(frame);
	}
	
	private void setupUI(Frame frame) {
		ChangeListener sliderAction = new SliderAction();
		
		JPanel p1 = new JPanel(new BorderLayout(12, 0));
		JPanel p2 = new JPanel(new GridLayout(2, 1, 0, 8));
		JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 4));
		JPanel p4 = new JPanel(new BorderLayout(4, 0));
		
		// sliders
		JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		p4.add(new JLabel("Saturation"), BorderLayout.NORTH);
		satSlider = new JSlider(-100, 100, sat);
		satSlider.addChangeListener(sliderAction);
		satSlider.setMajorTickSpacing(100);
		satSlider.setPaintTicks(true);
		p4.add(satSlider, BorderLayout.CENTER);
		
		satField = new JTextField("" + satSlider.getValue(), 4);
		satField.getDocument().addDocumentListener(new SatInputListener());
		satField.addKeyListener(new ArrowKeyAction(satField, -100, 100));
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
		
		// color panel
		p2 = new JPanel(new BorderLayout(0, 6));
		p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
		twoColorField = new TwoColorField(reference);
		p2.add(twoColorField, BorderLayout.NORTH);
		
		referenceField = new ColorField(reference);
		p2.add(referenceField, BorderLayout.CENTER);
		
		p3.add(p2);
		
		p1.add(p3, BorderLayout.EAST);
		
		// RGB fields
		p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 8));
		p3.add(new JLabel("R:"));
		redField = new JTextField(4);
		redField.setHorizontalAlignment(JTextField.CENTER);
		redField.setEditable(false);
		p3.add(redField);
		
		p3.add(new JLabel("  G:"));
		greenField = new JTextField(4);
		greenField.setHorizontalAlignment(JTextField.CENTER);
		greenField.setEditable(false);
		p3.add(greenField);
		
		p3.add(new JLabel("  B:"));
		blueField = new JTextField(4);
		blueField.setHorizontalAlignment(JTextField.CENTER);
		blueField.setEditable(false);
		p3.add(blueField);
		
		p1.add(p3, BorderLayout.SOUTH);
		
		getContentPane().add(p1, BorderLayout.CENTER);
		
		// buttons
		p3 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 4));
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
	
	public static Color showSBChooser(Frame frame, Color ref, Color inColor, int s, int b) {
		if(myInstance == null) {
			myInstance = new SBChooser(frame);
		}
		
		myInstance.setColor(ref, inColor, s, b);
		myInstance.setVisible(true);
		
		return myInstance.outColor;
	}
	
	public static Color showSBChooser(Frame frame, ControlPanel.SBField hsb) {
		if(myInstance == null) {
			myInstance = new SBChooser(frame);
		}
		
		myInstance.setColor(hsb);
		myInstance.setVisible(true);
		
		return myInstance.outColor;
	}
	
	public static void deleteInstance() {
		myInstance = null;
	}
	
	public void setColor(ControlPanel.SBField hsb) {
		reference = hsb.getColorReference().getReferenceColor();
		outColor = hsb.getBackground();
		sat = hsb.getColorReference().getSaturation();
		bri = hsb.getColorReference().getBrightness();
		
		valueIsAdjusting = true;
		satSlider.setValue(sat);
		briSlider.setValue(bri);
		valueIsAdjusting = false;
		
		referenceField.setBackground(reference);
		twoColorField.setLowerColor(outColor);
		adjustColor();
	}
	
	public void setColor(Color ref, Color inColor, int s, int b) {
		reference = ref;
		outColor = inColor;
		sat = s;
		bri = b;
		
		valueIsAdjusting = true;
		satSlider.setValue(sat);
		briSlider.setValue(bri);
		valueIsAdjusting = false;
		
		referenceField.setBackground(reference);
		twoColorField.setLowerColor(inColor);
		adjustColor();
	}
	
	private void showColor(int s, int b) {
		sat = s;
		bri = b;
		adjustColor();
	}
	
	private void adjustColor() {
		outColor = getAdjustedColor(reference, sat, bri);
		
		twoColorField.setUpperColor(outColor);
	}
	
	public static Color getAdjustedColor(Color inColor, int sat, int bri) {
		Color briColor = inColor;
		
		// first do brightening
		if(bri < 0) {
			briColor = ColorRoutines.darken(inColor, -bri);
		}
		else if(bri > 0) {
			briColor = ColorRoutines.lighten(inColor, bri);
		}

		// then do saturation
		Color satColor = ColorRoutines.getMaxSaturation(
			briColor,
			ColorRoutines.getHue(inColor));
		int r, g, b;
		
		if(sat >= 0) {
			int dr = briColor.getRed() - satColor.getRed();
			int dg = briColor.getGreen() - satColor.getGreen();
			int db = briColor.getBlue() - satColor.getBlue();
			
			r = briColor.getRed() - (int)Math.round(dr * sat / 100.0);
			g = briColor.getGreen() - (int)Math.round(dg * sat / 100.0);
			b = briColor.getBlue() - (int)Math.round(db * sat / 100.0);
		}
		else {
			float d = ColorRoutines.getGreyValue(briColor);
			float dr = briColor.getRed() - d;
			float dg = briColor.getGreen() - d;
			float db = briColor.getBlue() - d;
			
			r = (int)Math.round(briColor.getRed() + dr * sat / 100.0);
			g = (int)Math.round(briColor.getGreen() + dg * sat / 100.0);
			b = (int)Math.round(briColor.getBlue() + db * sat / 100.0);
		}

		return new Color(r, g, b);
	}
	
	public static int getSat() { return sat; }
	public static int getBri() { return bri; }

	class TwoColorField extends JPanel {
		
		private Dimension size = new Dimension(60, 68);
		private Color upperColor, lowerColor;
		
		TwoColorField(Color c) {
			setBorder(new LineBorder(Color.BLACK, 1));
			
			upperColor = outColor;
			lowerColor = c;
		}
		
		public Dimension getPreferredSize() {
			return size;
		}
		
		void setUpperColor(Color c) {
			upperColor = c;
			redField.setText("" + c.getRed());
			greenField.setText("" + c.getGreen());
			blueField.setText("" + c.getBlue());
			repaint(0);
		}
		
		void setLowerColor(Color c) {
			lowerColor = c;
			repaint(0);
		}
		
		public void paint(Graphics g) {
			super.paintBorder(g);
			
			g.setColor(upperColor);
			g.fillRect(1, 1, 58, 33);
			
			g.setColor(lowerColor);
			g.fillRect(1, 34, 58, 33);
		}
	}
	
	class ColorField extends JPanel {
		
		private Dimension size = new Dimension(60, 38);
		
		ColorField(Color c) {
			setBorder(new LineBorder(Color.GRAY, 1));
			setBackground(c);
		}
		
		public Dimension getPreferredSize() {
			return size;
		}
	}
	
	class SliderAction implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			if(!keyInput) {
				if(e.getSource().equals(satSlider)) {
					satField.setText("" + satSlider.getValue());
				}
				else {
					briField.setText("" + briSlider.getValue());
				}
			}
			
			if(valueIsAdjusting) return;

			showColor(satSlider.getValue(), briSlider.getValue());
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
					satSlider.setValue(val);
					keyInput = false;
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
					briSlider.setValue(val);
					keyInput = false;
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
	
	class OKAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}
	}
	
	class CancelAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			outColor = null;
			setVisible(false);
		}
	}
}
