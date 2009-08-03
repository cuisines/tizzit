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

/**
 * HSBReference
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class HSBReference extends ColorReference {
	
	protected int hue;
	protected boolean preserveGrey;

	public HSBReference(int hue, int sat, int bri, int ref) {
		this.hue = hue;
		this.sat = sat;
		this.bri = bri;
		this.ref = ref;
		preserveGrey = true;
	}
	
	public int getHue() {
		return hue;
	}
	
	public void setHue(int newHue) {
		hue = newHue;
	}

	public void load(DataInputStream in) throws IOException {
		try {
			hue = in.readInt();
			sat = in.readInt();
			bri = in.readInt();
			ref = in.readInt();
			preserveGrey = in.readBoolean();
		} catch(Exception ex) {
			throw new IOException("HSBReference.load() : " + ex.getMessage());
		}
	}
	
	public void save(DataOutputStream out) throws IOException {
		out.writeInt(hue);
		out.writeInt(sat);
		out.writeInt(bri);
		out.writeInt(ref);
		out.writeBoolean(preserveGrey);
	}

	public boolean isPreserveGrey() {
		return preserveGrey;
	}

	public void setPreserveGrey(boolean b) {
		preserveGrey = b;
	}
}
