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

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import de.muntjak.tinylookandfeel.Theme;

/**
 * ColorReference
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class ColorReference {
	
	// Possible ref values
	public static final int ABS_COLOR 		= 1;
	public static final int MAIN_COLOR 		= 2;
	public static final int BACK_COLOR 		= 3;
	public static final int DIS_COLOR 		= 4;
	public static final int FRAME_COLOR 	= 5;
	public static final int SUB1_COLOR 		= 6;
	public static final int SUB2_COLOR 		= 7;
	public static final int SUB3_COLOR 		= 8;
	public static final int SUB4_COLOR 		= 9;
	public static final int SUB5_COLOR 		= 10;
	public static final int SUB6_COLOR 		= 11;
	public static final int SUB7_COLOR 		= 12;
	public static final int SUB8_COLOR 		= 13;

	protected ColorUIResource c;
	protected int sat, bri;
	protected int ref;
	protected boolean locked;
	protected ColorIcon icon;
	protected static ColorIcon absolueIcon;
	
	public ColorReference() {}

	public ColorReference(Color c) {
		this.c = new ColorUIResource(c);
		sat = 0;
		bri = 0;
		ref = ABS_COLOR;
	}
	
	public ColorReference(Color c, int sat, int bri, int ref) {
		this.c = new ColorUIResource(c);
		this.sat = sat;
		this.bri = bri;
		this.ref = ref;
	}
	
	public ColorReference(Color c, int sat, int bri, int ref, boolean locked) {
		this.c = new ColorUIResource(c);
		this.sat = sat;
		this.bri = bri;
		this.ref = ref;
		this.locked = locked;
	}
	
	/**
	 * Copy-constructor for derived ColorReferences,
	 * called only from Theme.loadTheme().
	 * @param other
	 */
	public ColorReference(ColorReference other) {
		this.c = other.c;
		this.sat = other.sat;
		this.bri = other.bri;
		this.ref = other.ref;
	}
	
	/**
	 * Copy-constructor for absolute ColorReferences,
	 * called only from Theme.loadTheme().
	 * @param other
	 */
	public ColorReference(ColorReference other, int ref) {
			this.c = other.c;
			this.sat = 0;
			this.bri = 0;
			this.ref = ref;
	}
	
	public void reset() {
		sat = 0;
		bri = 0;
	}
	
	public ColorUIResource getColor() {
		return c;
	}
	
	public int getSaturation() { return sat; }
	
	public int getBrightness() { return bri; }
	
	public int getReference() { return ref; }
	
	public ColorUIResource getReferenceColor() {
		switch(ref) {
			case MAIN_COLOR:
				return Theme.mainColor[Theme.style].getColor();
			case BACK_COLOR:
				return Theme.backColor[Theme.style].getColor();
			case DIS_COLOR:
				return Theme.disColor[Theme.style].getColor();
			case FRAME_COLOR:
				return Theme.frameColor[Theme.style].getColor();
			case SUB1_COLOR:
				return Theme.sub1Color[Theme.style].getColor();
			case SUB2_COLOR:
				return Theme.sub2Color[Theme.style].getColor();
			case SUB3_COLOR:
				return Theme.sub3Color[Theme.style].getColor();
			case SUB4_COLOR:
				return Theme.sub4Color[Theme.style].getColor();
			case SUB5_COLOR:
				return Theme.sub5Color[Theme.style].getColor();
			case SUB6_COLOR:
				return Theme.sub6Color[Theme.style].getColor();
			case SUB7_COLOR:
				return Theme.sub7Color[Theme.style].getColor();
			case SUB8_COLOR:
				return Theme.sub8Color[Theme.style].getColor();
			default:
				return c;
		}
	}
	
	public static ColorUIResource getReferenceColor(int ref) {
		switch(ref) {
			case MAIN_COLOR:
				return Theme.mainColor[Theme.style].getColor();
			case BACK_COLOR:
				return Theme.backColor[Theme.style].getColor();
			case DIS_COLOR:
				return Theme.disColor[Theme.style].getColor();
			case FRAME_COLOR:
				return Theme.frameColor[Theme.style].getColor();
			case SUB1_COLOR:
				return Theme.sub1Color[Theme.style].getColor();
			case SUB2_COLOR:
				return Theme.sub2Color[Theme.style].getColor();
			case SUB3_COLOR:
				return Theme.sub3Color[Theme.style].getColor();
			case SUB4_COLOR:
				return Theme.sub4Color[Theme.style].getColor();
			case SUB5_COLOR:
				return Theme.sub5Color[Theme.style].getColor();
			case SUB6_COLOR:
				return Theme.sub6Color[Theme.style].getColor();
			case SUB7_COLOR:
				return Theme.sub7Color[Theme.style].getColor();
			case SUB8_COLOR:
				return Theme.sub8Color[Theme.style].getColor();
			default:
				return null;
		}
	}
	
	public String getReferenceString() {
		switch(ref) {
			case MAIN_COLOR:
				return "Main Color";
			case BACK_COLOR:
				return "Back Color";
			case DIS_COLOR:
				return "Disabled Color";
			case FRAME_COLOR:
				return "Frame Color";
			case SUB1_COLOR:
				return "Sub1 Color";
			case SUB2_COLOR:
				return "Sub2 Color";
			case SUB3_COLOR:
				return "Sub3 Color";
			case SUB4_COLOR:
				return "Sub4 Color";
			case SUB5_COLOR:
				return "Sub5 Color";
			case SUB6_COLOR:
				return "Sub6 Color";
			case SUB7_COLOR:
				return "Sub7 Color";
			case SUB8_COLOR:
				return "Sub8 Color";
			default:
				return "";
		}
	}
	
	public void setColor(Color newColor) {
		if(!isAbsoluteColor()) return;
		
		c = new ColorUIResource(newColor);
	}

	public void setSaturation(int newSat) { sat = newSat; }
	
	public void setBrightness(int newBri) { bri = newBri; }
	
	public void setReference(int newRef) { ref = newRef; }
	
	public void setColor(int sat, int bri) {
		if(isAbsoluteColor()) return;
		
		this.sat = sat;
		this.bri = bri;
		
		switch(ref) {
			case MAIN_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.mainColor[Theme.style].getColor(), sat, bri));
				break;
			case BACK_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.backColor[Theme.style].getColor(), sat, bri));
				break;
			case DIS_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.disColor[Theme.style].getColor(), sat, bri));
				break;
			case FRAME_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.frameColor[Theme.style].getColor(), sat, bri));
				break;
			case SUB1_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.sub1Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB2_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.sub2Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB3_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
						Theme.sub3Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB4_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
						Theme.sub4Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB5_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
						Theme.sub5Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB6_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
						Theme.sub6Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB7_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
						Theme.sub7Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB8_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
						Theme.sub8Color[Theme.style].getColor(), sat, bri));
				break;
		}
	}
	
	public ColorUIResource update() {
		if(isAbsoluteColor()) return c;
		
		switch(ref) {
			case MAIN_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.mainColor[Theme.style].getColor(), sat, bri));
				break;
			case BACK_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.backColor[Theme.style].getColor(), sat, bri));
				break;
			case DIS_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.disColor[Theme.style].getColor(), sat, bri));
				break;
			case FRAME_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.frameColor[Theme.style].getColor(), sat, bri));
				break;
			case SUB1_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.sub1Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB2_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.sub2Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB3_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.sub3Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB4_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.sub4Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB5_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.sub5Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB6_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.sub6Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB7_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.sub7Color[Theme.style].getColor(), sat, bri));
				break;
			case SUB8_COLOR:
				c = new ColorUIResource(
					SBChooser.getAdjustedColor(
					Theme.sub8Color[Theme.style].getColor(), sat, bri));
				break;
		}
		
		return c;
	}
	
	public boolean isAbsoluteColor() { return (ref == ABS_COLOR); }
	
	public void setLocked(boolean newLocked) { locked = newLocked; }
	
	public boolean isLocked() { return locked; }
	
	public String toString() {
		return c.toString();
	}
	
	public Icon getIcon() {
		if(icon == null) {
			icon = new ColorIcon(false);
		}
		
		return icon;
	}
	
	public static Icon getAbsoluteIcon() {		
		if(absolueIcon == null) {
			absolueIcon = new ColorReference(Color.BLACK).new ColorIcon(true);
		}
		
		return absolueIcon;
	}
	
	public void save(DataOutputStream out) throws IOException {
		out.writeInt(c.getRGB());
		out.writeInt(sat);
		out.writeInt(bri);
		out.writeInt(ref);
		out.writeBoolean(locked);
	}
	
	public void load(DataInputStream in) throws IOException {
		try {
			if(Theme.fileID >= Theme.FILE_ID_3A) {
				c = new ColorUIResource(in.readInt());
			}
			else {
				c = new ColorUIResource(in.readInt(), in.readInt(), in.readInt());
			}
			
			sat = in.readInt();
			bri = in.readInt();
			ref = in.readInt();
			locked = in.readBoolean();
		} catch(Exception ex) {
			throw new IOException("ColorReference.load() : " + ex.getMessage());
		}
	}
	
	class ColorIcon implements Icon {

		private boolean paintGradients;
		
		ColorIcon(boolean paintGradients) {
			this.paintGradients = paintGradients;
		}
		
		public int getIconHeight() {
			return 16;
		}

		public int getIconWidth() {
			return 16;
		}
		
		public void paintIcon(Component comp, Graphics g, int x, int y) {
			Color tempCol = g.getColor();

			g.setColor(Color.GRAY);
			g.drawRect(x, y, getIconWidth(), getIconHeight());
			
			if(paintGradients) {
				float hue = 0.0f;

				for(int i = 0; i < 15; i++) {
					g.setColor(Color.getHSBColor(hue, 0.5f, 1.0f));
					g.drawLine(x + 1 + i, y + 1, x + 1 + i, y + getIconHeight() - 1);
					hue += 1.0 / 16.0;
				}
			}
			else {
				g.setColor(c);
				g.fillRect(x + 1, y + 1, getIconWidth() - 1, getIconHeight() - 1);
			}
			
			// draw arrow
			if(comp instanceof AbstractButton) {
				if(((AbstractButton)comp).isSelected()) {
					g.setColor(Color.WHITE);
					drawArrow(g, x + 1, y + 1);
					
					g.setColor(Color.BLACK);
					drawArrow(g, x, y);
				}
			}
			
			g.setColor(tempCol);
		}
		
		private void drawArrow(Graphics g, int x, int y) {
			g.drawLine(x + 3, y + 5, x + 3, y + 7);
			g.drawLine(x + 4, y + 6, x + 4, y + 8);
			g.drawLine(x + 5, y + 7, x + 5, y + 9);
			g.drawLine(x + 6, y + 6, x + 6, y + 8);
			g.drawLine(x + 7, y + 5, x + 7, y + 7);
			g.drawLine(x + 8, y + 4, x + 8, y + 6);
			g.drawLine(x + 9, y + 3, x + 9, y + 5);
		}
	}
}
