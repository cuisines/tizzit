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
import javax.swing.plaf.*;

import de.muntjak.tinylookandfeel.Theme;

/**
 * ColoredFont
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class ColoredFont {

	private ColorReference[] ref;
	private FontUIResource font;
	private boolean isPlainFont, isBoldFont;

	public ColoredFont(String fontFamily, int style, int size) {
		font = new FontUIResource(fontFamily, style, size);
	}

	public ColoredFont() {
		font = new FontUIResource("sansserif", Font.PLAIN, 12);
		isPlainFont = true;
	}
	
	public ColoredFont(ColorReference[] ref) {
		this("sansserif", Font.PLAIN, 12, ref);
		isPlainFont = true;
	}
	
	public ColoredFont(String fontFamily, int style, int size, ColorReference[] ref) {
		font = new FontUIResource(fontFamily, style, size);
		this.ref = ref;
		
		if(ref[0] == null) {
			ref[0] = new ColorReference(new Color(0, 0, 0));
		}
		
		if(ref[1] == null) {
			ref[1] = new ColorReference(new Color(0, 0, 0));
		}
		
		if(ref[2] == null) {
			ref[2] = new ColorReference(new Color(0, 0, 0));
		}
		
		if(ref[3] == null) {
			ref[3] = new ColorReference(new Color(0, 0, 0));
		}
	}
	
	public void setPlainFont(boolean b) {
		isPlainFont = b;
		if(b) isBoldFont = false;
	}
	
	public void setBoldFont(boolean b) {
		isBoldFont = b;
		if(b) isPlainFont = false;
	}
	
	public boolean isPlainFont() {
		return isPlainFont;
	}
	
	public boolean isBoldFont() {
		return isBoldFont;
	}

	public void setFont(String fontFamily, int style, int size) {
		font = new FontUIResource(fontFamily, style, size);
	}
	
	public void setFont(Font font) {
		this.font = new FontUIResource(font);
	}
	
	public void setFont(FontUIResource font) {
		this.font = font;
	}
	
	public FontUIResource getFont() {
		if(isPlainFont) {
			return Theme.plainFont[Theme.style].font;
		}
		
		if(isBoldFont) {
			return Theme.boldFont[Theme.style].font;
		}
		
		return font;
	}
	
	public ColorReference[] getColorReference() {
		return ref;
	}
	
	public void setColorReference(ColorReference[] ref) {
		this.ref = ref;
	}
	
	public void save(DataOutputStream out) throws IOException {
		out.writeUTF(font.getFamily());	
		out.writeBoolean(font.isBold());	
		out.writeInt(font.getSize());
		out.writeBoolean(isPlainFont);
		out.writeBoolean(isBoldFont);
	}
	
	public void load(DataInputStream in) throws IOException {
		font = new FontUIResource(in.readUTF(),
			in.readBoolean() ? Font.BOLD : Font.PLAIN,
			in.readInt());
		isPlainFont = in.readBoolean();
		isBoldFont = in.readBoolean();
	}
}
