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

/**
 * ColorRoutines
 * 
 * @version 1.0
 * @author Hans Bickel
 */
public class ColorRoutines {
	private static final int RGB = 1;
	private static final int RBG = 2;
	private static final int GBR = 3;
	private static final int GRB = 4;
	private static final int BRG = 5;
	private static final int BGR = 6;
	private boolean preserveGrey;
	private int chue, csat, cbri;
	private int fr, fg, fb;
	int hi, lo, md;
	boolean hiIsR, hiIsG, hiIsB;
	boolean mdIsR, mdIsG, mdIsB;
	boolean loIsR, loIsG, loIsB;

	public ColorRoutines(Color c) {
		setHSB(c.getRed(), c.getGreen(), c.getBlue());
	}

	public ColorRoutines(int hue, int sat, int bri, boolean preserveGrey) {
		chue = hue;
		csat = sat;
		cbri = bri;
		this.preserveGrey = preserveGrey;

		Color c = Color.getHSBColor((float) ((double)chue / 360.0), 1.0f, 1.0f);
		fr = c.getRed();
		fg = c.getGreen();
		fb = c.getBlue();

		// sort colors - 6 options
		if (fr >= fg && fg >= fb) {
			hi = fr;
			md = fg;
			lo = fb;
			hiIsR = true;
			mdIsG = true;
			loIsB = true;
		}
		else if (fr >= fb && fb >= fg) {
			hi = fr;
			md = fb;
			lo = fg;
			hiIsR = true;
			mdIsB = true;
			loIsG = true;
		}
		else if (fg >= fr && fr >= fb) {
			hi = fg;
			md = fr;
			lo = fb;
			hiIsG = true;
			mdIsR = true;
			loIsB = true;
		}
		else if (fg >= fb && fb >= fr) {
			hi = fg;
			md = fb;
			lo = fr;
			hiIsG = true;
			mdIsB = true;
			loIsR = true;
		}
		else if (fb >= fg && fg >= fr) {
			hi = fb;
			md = fg;
			lo = fr;
			hiIsB = true;
			mdIsG = true;
			loIsR = true;
		}
		else if (fb >= fr && fr >= fg) {
			hi = fb;
			md = fr;
			lo = fg;
			hiIsB = true;
			mdIsR = true;
			loIsG = true;
		}
	}

	private void setHSB(int r, int g, int b) {
		chue = getHue(r, g, b);
		csat = getSaturation(r, g, b);
		cbri = getBrightness(r, g, b);
	}

	public static Color getAverage(Color c1, Color c2) {
		int r = (int)Math.round((c1.getRed() + c2.getRed()) / 2.0);
		int g = (int)Math.round((c1.getGreen() + c2.getGreen()) / 2.0);
		int b = (int)Math.round((c1.getBlue() + c2.getBlue()) / 2.0);

		return new Color(r, g, b);
	}

	// i >= 0 <= d
	// c1 ist Einblendfarbe
	// c2 ist Hintergrundfarbe
	public static Color getGradient(Color c1, Color c2, int d, int i) {
		if (i == 0)
			return c1;
		if (i == d)
			return c2;

		double d2 = i * 1.1 / d;
		double d1 = 1.0 - d2;

		int r = (int)Math.round(c1.getRed() * d1 + c2.getRed() * d2);
		int g = (int)Math.round(c1.getGreen() * d1 + c2.getGreen() * d2);
		int b = (int)Math.round(c1.getBlue() * d1 + c2.getBlue() * d2);

		return new Color(r, g, b);
	}

	public static Color getMaxSaturation(Color c, int memH) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		if (r == g && r == b)
			return c;

		int ta = 0, tb = 0, tc = 0;
		int mapping = RGB;

		if (r >= g && r >= b) {
			tc = r;

			if (g == b) {
				ta = g;
				tb = b;
				mapping = RGB;
			}
			else if (g > b) {
				ta = g;
				tb = b;
				mapping = RGB;
			}
			else {
				tb = g;
				ta = b;
				mapping = RBG;
			}
		}
		else if (g >= r && g >= b) {
			tc = g;

			if (r == b) {
				ta = r;
				tb = b;
				mapping = GRB;
			}
			else if (r > b) {
				ta = r;
				tb = b;
				mapping = GRB;
			}
			else {
				tb = r;
				ta = b;
				mapping = GBR;
			}
		}
		else if (b >= r && b >= g) {
			tc = b;

			if (r == g) {
				ta = r;
				tb = g;
				mapping = BRG;
			}
			else if (r > g) {
				ta = r;
				tb = g;
				mapping = BRG;
			}
			else {
				tb = r;
				ta = g;
				mapping = BGR;
			}
		}

		if (tb == 0) {
			return c;
		}

		int nc = Math.min(255, tc + tb);
		int nb = Math.max(0, tc + tb - 255);
		int na = ta;
		int h = 0, mh = 0;
		int ba = 0, delta = 360;
		Color rc = null;

		switch (mapping) {
			case RGB :
				h = getHue(nc, na, nb);
				mh = h;
				while (h != memH && na < 256) {
					h = getHue(nc, ++na, nb);
					if (na == 256)
						break;

					if (h == memH) {
						return new Color(nc, na, nb);
					}
					else if ((mh < memH && h > memH) || (mh > memH && h < memH)) {
						return new Color(nc, na, nb);
					}
					else if (Math.abs(h - memH) < delta) {
						delta = Math.abs(h - memH);
						ba = na;
					}
					mh = h;
				}

				if (h != memH) {
					h = getHue(nc, na, nb);
					mh = h;
					na = ta;
					while (h != memH && na >= 0) {
						h = getHue(nc, --na, nb);
						if (na == -1)
							break;

						if (h == memH) {
							return new Color(nc, na, nb);
						}
						else if ((mh < memH && h > memH) || (mh > memH && h < memH)) {
							return new Color(nc, na, nb);
						}
						else if (Math.abs(h - memH) < delta) {
							delta = Math.abs(h - memH);
							ba = na;
						}
						mh = h;
					}
				}
				if (na == 256 | na == -1) {
					na = ba;
				}
				rc = new Color(nc, na, nb);
				break;
			case RBG :
				h = getHue(nc, nb, na);
				mh = h;
				while (h != memH && na < 256) {
					h = getHue(nc, nb, ++na);
					if (na == 256)
						break;

					if (h == memH) {
						return new Color(nc, nb, na);
					}
					else if ((mh < memH && h > memH) || (mh > memH && h < memH)) {
						return new Color(nc, nb, na);
					}
					else if (Math.abs(h - memH) < delta) {
						delta = Math.abs(h - memH);
						ba = na;
					}
					mh = h;
				}

				if (h != memH) {
					h = getHue(nc, na, nb);
					mh = h;
					na = ta;
					while (h != memH && na >= 0) {
						h = getHue(nc, nb, --na);
						if (na == -1)
							break;

						if (h == memH) {
							return new Color(nc, nb, na);
						}
						else if ((mh < memH && h > memH) || (mh > memH && h < memH)) {
							return new Color(nc, nb, na);
						}
						else if (Math.abs(h - memH) < delta) {
							delta = Math.abs(h - memH);
							ba = na;
						}
						mh = h;
					}
				}
				if (na == 256 | na == -1) {
					na = ba;
				}
				rc = new Color(nc, nb, na);
				break;
			case GBR :
				h = getHue(nb, nc, na);
				mh = h;
				while (h != memH && na < 256) {
					h = getHue(nb, nc, ++na);
					if (na == 256)
						break;

					if (h == memH) {
						return new Color(nb, nc, na);
					}
					else if ((mh < memH && h > memH) || (mh > memH && h < memH)) {
						return new Color(nb, nc, na);
					}
					else if (Math.abs(h - memH) < delta) {
						delta = Math.abs(h - memH);
						ba = na;
					}
					mh = h;
				}

				if (h != memH) {
					h = getHue(nc, na, nb);
					mh = h;
					na = ta;
					while (h != memH && na >= 0) {
						h = getHue(nb, nc, --na);
						if (na == -1)
							break;

						if (h == memH) {
							return new Color(nb, nc, na);
						}
						else if ((mh < memH && h > memH) || (mh > memH && h < memH)) {
							return new Color(nb, nc, na);
						}
						else if (Math.abs(h - memH) < delta) {
							delta = Math.abs(h - memH);
							ba = na;
						}
						mh = h;
					}
				}

				if (na == 256 | na == -1) {
					na = ba;
				}

				rc = new Color(nb, nc, na);
				break;
			case GRB :
				h = getHue(na, nc, nb);
				mh = h;
				while (h != memH && na < 256) {
					h = getHue(++na, nc, nb);
					if (na == 256)
						break;

					if (h == memH) {
						return new Color(na, nc, nb);
					}
					else if ((mh < memH && h > memH) || (mh > memH && h < memH)) {
						return new Color(na, nc, nb);
					}
					else if (Math.abs(h - memH) < delta) {
						delta = Math.abs(h - memH);
						ba = na;
					}
					mh = h;
				}

				if (h != memH) {
					h = getHue(nc, na, nb);
					mh = h;
					na = ta;
					while (h != memH && na >= 0) {
						h = getHue(--na, nc, nb);
						if (na == -1)
							break;

						if (h == memH) {
							return new Color(na, nc, nb);
						}
						else if ((mh < memH && h > memH) || (mh > memH && h < memH)) {
							return new Color(na, nc, nb);
						}
						else if (Math.abs(h - memH) < delta) {
							delta = Math.abs(h - memH);
							ba = na;
						}
						mh = h;
					}
				}
				if (na == 256 | na == -1) {
					na = ba;
				}
				rc = new Color(na, nc, nb);
				break;
			case BRG :
				h = getHue(na, nb, nc);
				mh = h;
				while (h != memH && na < 256) {
					h = getHue(++na, nb, nc);
					if (na == 256)
						break;

					if (h == memH) {
						return new Color(na, nb, nc);
					}
					else if ((mh < memH && h > memH) || (mh > memH && h < memH)) {
						return new Color(na, nb, nc);
					}
					else if (Math.abs(h - memH) < delta) {
						delta = Math.abs(h - memH);
						ba = na;
					}
					mh = h;
				}

				if (h != memH) {
					h = getHue(nc, na, nb);
					mh = h;
					na = ta;
					while (h != memH && na >= 0) {
						h = getHue(--na, nb, nc);
						if (na == -1)
							break;

						if (h == memH) {
							return new Color(na, nb, nc);
						}
						else if ((mh < memH && h > memH) || (mh > memH && h < memH)) {
							return new Color(na, nb, nc);
						}
						else if (Math.abs(h - memH) < delta) {
							delta = Math.abs(h - memH);
							ba = na;
						}
						mh = h;
					}
				}
				if (na == 256 | na == -1) {
					na = ba;
				}
				rc = new Color(na, nb, nc);
				break;
			case BGR :
				h = getHue(nb, na, nc);
				mh = h;
				while (h != memH && na < 256) {
					h = getHue(nb, ++na, nc);
					if (na == 256)
						break;

					if (h == memH) {
						return new Color(nb, na, nc);
					}
					else if ((mh < memH && h > memH) || (mh > memH && h < memH)) {
						return new Color(nb, na, nc);
					}
					else if (Math.abs(h - memH) < delta) {
						delta = Math.abs(h - memH);
						ba = na;
					}
					mh = h;
				}

				if (h != memH) {
					h = getHue(nc, na, nb);
					mh = h;
					na = ta;
					while (h != memH && na >= 0) {
						h = getHue(nb, --na, nc);
						if (na == -1)
							break;

						if (h == memH) {
							return new Color(nb, na, nc);
						}
						else if ((mh < memH && h > memH) || (mh > memH && h < memH)) {
							return new Color(nb, na, nc);
						}
						else if (Math.abs(h - memH) < delta) {
							delta = Math.abs(h - memH);
							ba = na;
						}
						mh = h;
					}
				}
				if (na == 256 | na == -1) {
					na = ba;
				}
				rc = new Color(nb, na, nc);
				break;
		}

		return rc;
	}

	public static float getGreyValue(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int ta = 0, tb = 0, tc = 0;

		if (r >= g && r >= b) {
			if (r == 0)
				return 0; // black

			tc = r;

			if (g >= b) {
				ta = g;
				tb = b;
			}
			else {
				tb = g;
				ta = b;
			}
		}
		else if (g >= r && g >= b) {
			tc = g;

			if (r >= b) {
				ta = r;
				tb = b;
			}
			else {
				tb = r;
				ta = b;
			}
		}
		else if (b >= r && b >= g) {
			tc = b;

			if (r >= g) {
				ta = r;
				tb = g;
			}
			else {
				tb = r;
				ta = g;
			}
		}

		return (float) ((tc + tb) / 2.0);
	}

	public static int getBrightness(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		return getBrightness(r, g, b);
	}

	public static int getBrightness(int r, int g, int b) {
		if (r >= g && r >= b) {
			return (int)Math.round(100 * r / 255.0);
		}
		else if (g >= r && g >= b) {
			return (int)Math.round(100 * g / 255.0);
		}
		else if (b >= r && b >= g) {
			return (int)Math.round(100 * b / 255.0);
		}

		return -1;
	}

	public static int getSaturation(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		return getSaturation(r, g, b);
	}

	public static int getSaturation(int r, int g, int b) {
		int ta = 0, tb = 0, tc = 0;

		if (r >= g && r >= b) {
			if (r == 0)
				return 0; // black

			tc = r;

			if (g >= b) {
				ta = g;
				tb = b;
			}
			else {
				tb = g;
				ta = b;
			}
		}
		else if (g >= r && g >= b) {
			tc = g;

			if (r >= b) {
				ta = r;
				tb = b;
			}
			else {
				tb = r;
				ta = b;
			}
		}
		else if (b >= r && b >= g) {
			tc = b;

			if (r >= g) {
				ta = r;
				tb = g;
			}
			else {
				tb = r;
				ta = g;
			}
		}

		return 100 - (int)Math.round(100.0 * tb / tc);
	}

	public static int getHue(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		return getHue(r, g, b);
	}

	public static int calculateHue(Color c) {
		float f[] = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		return (int)Math.round(360.0 * f[0]);
	}

	public static int getHue(int r, int g, int b) {
		int ta = 0, tb = 0, tc = 0;
		int mapping = RGB;

		if (r >= g && r >= b) {
			tc = r;

			if (g == b) {
				return 0;
			}
			else if (g > b) {
				ta = g;
				tb = b;
				mapping = RGB;
			}
			else {
				tb = g;
				ta = b;
				mapping = RBG;
			}
		}
		else if (g >= r && g >= b) {
			tc = g;

			if (r == b) {
				return 120;
			}
			else if (r > b) {
				ta = r;
				tb = b;
				mapping = GRB;
			}
			else {
				tb = r;
				ta = b;
				mapping = GBR;
			}
		}
		else if (b >= r && b >= g) {
			tc = b;

			if (r == g) {
				return 240;
			}
			else if (r > g) {
				ta = r;
				tb = g;
				mapping = BRG;
			}
			else {
				tb = r;
				ta = g;
				mapping = BGR;
			}
		}

		// normalize
		double na = (ta * 255.0 / tc);
		double nb = (tb * 255.0 / tc);

		double val = ((na - nb) * 255.0 / (255 - nb));

		int w = (int)Math.round(60 * val / 255.0);

		switch (mapping) {
			case RGB :
				return w; // 0 - 60
			case RBG :
				return 360 - w; // 300 - 360
			case GBR :
				return 120 + w; // 120 - 180
			case GRB :
				return 120 - w; // 60 - 120
			case BRG :
				return 240 + w; // 240 - 300
			case BGR :
				return 240 - w; // 180 - 240
			default :
				return -1;
		}
	}

	public static Color getHSB(int h, int s, int b) {
		double cr = 0, cg = 0, cb = 0;
		int mapping = RGB;

		if (h == 360)
			h = 0;

		// compute hue
		int winkel = h / 60;
		int amount = h % 60;

		switch (winkel) {
			case 0 : // 0 - 60
				cr = 255;
				cg = (255 * amount / 60.0);
				mapping = RGB;
				break;
			case 1 : // 60 - 120
				cg = 255;
				cr = 255 - (255 * amount / 60.0);
				mapping = GBR;
				break;
			case 2 : // 120 - 180
				cg = 255;
				cb = (255 * amount / 60.0);
				mapping = GBR;
				break;
			case 3 : // 180 - 240
				cb = 255;
				cg = 255 - (255 * amount / 60.0);
				mapping = BRG;
				break;
			case 4 : // 240 - 300
				cb = 255;
				cr = (255 * amount / 60.0);
				mapping = BRG;
				break;
			case 5 : // 300 - 360
				cr = 255;
				cb = 255 - (255 * amount / 60.0);
				mapping = RGB;
				break;
		}

		// compute brightness
		cr = (cr * b / 100.0);
		cg = (cg * b / 100.0);
		cb = (cb * b / 100.0);

		// compute saturation
		int d = 100 - s;

		switch (mapping) {
			case RGB :
				cg += ((cr - cg) * d / 100.0);
				cb += ((cr - cb) * d / 100.0);
				break;
			case GBR :
				cr += ((cg - cr) * d / 100.0);
				cb += ((cg - cb) * d / 100.0);
				break;
			case BRG :
				cr += ((cb - cr) * d / 100.0);
				cg += ((cb - cg) * d / 100.0);
				break;
		}

		return new Color((int)Math.round(cr), (int)Math.round(cg), (int)Math.round(cb));
	}

	// Parameter: the original icon pixel
	public int colorize(int r, int g, int b, int a) {
		if (cbri == 100) {
			return colorToInt(255, 255, 255, a);
		}
		else if (cbri == -100) {
			return colorToInt(0, 0, 0, a);
		}

		// first calculate the grey value			
		int hi1 = r;
		if (g >= r && g >= b)
			hi1 = g;
		else if (b >= r && b >= g)
			hi1 = b;

		int lo1 = r;
		if (g <= r && g <= b)
			lo1 = g;
		else if (b <= r && b <= g)
			lo1 = b;

		int grey = (hi1 + lo1) / 2; // floor

		// compute with cbri
		if (cbri < 0) {
			grey += grey * cbri / 100;
		}
		else if (cbri > 0) {
			grey += (255 - grey) * cbri / 100;
		}

		// if in-colors are equal and preserveGrey is true,
		// return grey values
		if (preserveGrey) {
			if (r == g && r == b) {
				return colorToInt(grey, grey, grey, a);
			}
		}

		// now calculate the output colors
		// csat = 0 => output = grey
		// grey = 127 => output = full saturation
		int hr = 0;
		int hg = 0;
		int hb = 0;
		int diff = 0;

		if (grey >= 127) {
			diff = 255 - grey;
		}
		else {
			diff = grey;
		}
		// hi value is always 255
		if (hiIsR) {
			hr = grey + diff * csat / 100;
		}
		else if (hiIsG) {
			hg = grey + diff * csat / 100;
		}
		else if (hiIsB) {
			hb = grey + diff * csat / 100;
		}

		// md value is between 0 and 255
		if (mdIsR) {
			if (grey >= 127) {
				diff = fr + (255 - fr) * (grey - 127) / 128 - grey;
			}
			else {
				diff = fr * grey / 127 - grey;
			}
			hr = grey + diff * csat / 100;
		}
		else if (mdIsG) {
			if (grey >= 127) {
				diff = fg + (255 - fg) * (grey - 127) / 128 - grey;
			}
			else {
				diff = fg * grey / 127 - grey;
			}
			hg = grey + diff * csat / 100;
		}
		else if (mdIsB) {
			if (grey >= 127) {
				diff = fb + (255 - fb) * (grey - 127) / 128 - grey;
			}
			else {
				diff = fb * grey / 127 - grey;
			}
			hb = grey + diff * csat / 100;
		}

		diff = grey - (255 - grey);
		if (diff < 0)
			diff = 0;
		diff = grey - diff;

		// lo value = 0
		if (loIsR) {
			hr = grey - diff * csat / 100;
		}
		else if (loIsG) {
			hg = grey - diff * csat / 100;
		}
		else if (loIsB) {
			hb = grey - diff * csat / 100;
		}

		return colorToInt(hr, hg, hb, a);
	}

	public static Color getInverseColor(Color c) {
		int r = 255 - c.getRed();
		int g = 255 - c.getGreen();
		int b = 255 - c.getBlue();

		return new Color(r, g, b);
	}

	public static Color getRandomColor() {
		int r = (int) (Math.random() * 255);
		int g = (int) (Math.random() * 255);
		int b = (int) (Math.random() * 255);

		return new Color(r, g, b);
	}

	public static Color getAlphaColor(Color c, int a) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		return new Color(r, g, b, a);
	}

	protected static int colorToInt(Color c, int a) {
		return c.getBlue() + c.getGreen() * 256 + c.getRed() * (256 * 256) + a * (256 * 256 * 256);
	}

	protected static int colorToInt(int r, int g, int b, int a) {
		return b + g * 256 + r * (256 * 256) + a * (256 * 256 * 256);
	}

	public static Color lighten(Color c, int amount) {
		if (amount < 0) return c;
		
		if (amount > 100) amount = 100;

		int dr = (int)Math.round((255 - c.getRed()) * amount / 100.0);
		int dg = (int)Math.round((255 - c.getGreen()) * amount / 100.0);
		int db = (int)Math.round((255 - c.getBlue()) * amount / 100.0);

		return new Color(c.getRed() + dr, c.getGreen() + dg, c.getBlue() + db, c.getAlpha());
	}

	public static Color darken(Color c, int amount) {
		if (amount < 0 || amount > 100) return c;

		int r = (int)Math.round(c.getRed() * (100 - amount) / 100.0);
		int g = (int)Math.round(c.getGreen() * (100 - amount) / 100.0);
		int b = (int)Math.round(c.getBlue() * (100 - amount) / 100.0);

		return new Color(r, g, b, c.getAlpha());
	}

	public static Color lighten(int grey, int amount) {
		if (amount < 0 || amount > 100) return new Color(grey, grey, grey);

		int val = (255 - grey) * amount / 100 + grey;

		return new Color(val, val, val);
	}

	public static Color darken(int grey, int amount) {
		if (amount < 0 || amount > 100) return new Color(grey, grey, grey);

		int val = grey * (100 - amount) / 100;

		return new Color(val, val, val);
	}

}
