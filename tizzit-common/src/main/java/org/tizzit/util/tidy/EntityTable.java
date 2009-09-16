/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * @(#)EntityTable.java   1.11 2000/08/16
 *
 */

package org.tizzit.util.tidy;

/**
 *
 * Entity hash table
 *
 * (c) 1998-2000 (W3C) MIT, INRIA, Keio University
 * See Tidy.java for the copyright notice.
 * Derived from <a href="http://www.w3.org/People/Raggett/tidy">
 * HTML Tidy Release 4 Aug 2000</a>
 *
 * @author  Dave Raggett <dsr@w3.org>
 * @author  Andy Quick <ac.quick@sympatico.ca> (translation to Java)
 * @version 1.0, 1999/05/22
 * @version 1.0.1, 1999/05/29
 * @version 1.1, 1999/06/18 Java Bean
 * @version 1.2, 1999/07/10 Tidy Release 7 Jul 1999
 * @version 1.3, 1999/07/30 Tidy Release 26 Jul 1999
 * @version 1.4, 1999/09/04 DOM support
 * @version 1.5, 1999/10/23 Tidy Release 27 Sep 1999
 * @version 1.6, 1999/11/01 Tidy Release 22 Oct 1999
 * @version 1.7, 1999/12/06 Tidy Release 30 Nov 1999
 * @version 1.8, 2000/01/22 Tidy Release 13 Jan 2000
 * @version 1.9, 2000/06/03 Tidy Release 30 Apr 2000
 * @version 1.10, 2000/07/22 Tidy Release 8 Jul 2000
 * @version 1.11, 2000/08/16 Tidy Release 4 Aug 2000
 */

import java.util.Hashtable;
import java.util.Enumeration;

public class EntityTable {

	public EntityTable() {
	}

	public Entity lookup(String name) {
		return (Entity) entityHashtable.get(name);
	}

	public Entity install(String name, short code) {
		Entity ent = lookup(name);
		if (ent == null) {
			ent = new Entity(name, code);
			entityHashtable.put(name, ent);
		} else {
			ent.code = code;
		}
		return ent;
	}

	public Entity install(Entity ent) {
		return (Entity) entityHashtable.put(ent.name, ent);
	}

	/* entity starting with "&" returns zero on error */
	public short entityCode(String name) {
		int c;

		if (name.length() <= 1) return 0;

		/* numeric entitity: name = "&#" followed by number */
		if (name.charAt(1) == '#') {
			c = 0; /* zero on missing/bad number */

			/* 'x' prefix denotes hexadecimal number format */
			try {
				if (name.length() >= 4 && name.charAt(2) == 'x') {
					c = Integer.parseInt(name.substring(3), 16);
				} else if (name.length() >= 3) {
					c = Integer.parseInt(name.substring(2));
				}
			} catch (NumberFormatException e) {
			}

			return (short) c;
		}

		/* Named entity: name ="&" followed by a name */
		Entity ent = lookup(name.substring(1));
		if (ent != null) { return ent.code; }

		return 0; /* zero signifies unknown entity name */
	}

	public String entityName(short code) {
		String name = null;
		Entity ent;
		Enumeration en = entityHashtable.elements();
		while (en.hasMoreElements()) {
			ent = (Entity) en.nextElement();
			if (ent.code == code) {
				name = ent.name;
				break;
			}
		}
		return name;
	}

	private Hashtable entityHashtable = new Hashtable();

	private static EntityTable defaultEntityTable = null;

	private static Entity[] entities = {

	new Entity("nbsp", 160), new Entity("iexcl", 161), new Entity("cent", 162), new Entity("pound", 163),
			new Entity("curren", 164), new Entity("yen", 165), new Entity("brvbar", 166), new Entity("sect", 167),
			new Entity("uml", 168), new Entity("copy", 169), new Entity("ordf", 170), new Entity("laquo", 171),
			new Entity("not", 172), new Entity("shy", 173), new Entity("reg", 174), new Entity("macr", 175),
			new Entity("deg", 176), new Entity("plusmn", 177), new Entity("sup2", 178), new Entity("sup3", 179),
			new Entity("acute", 180), new Entity("micro", 181), new Entity("para", 182), new Entity("middot", 183),
			new Entity("cedil", 184), new Entity("sup1", 185), new Entity("ordm", 186), new Entity("raquo", 187),
			new Entity("frac14", 188), new Entity("frac12", 189), new Entity("frac34", 190), new Entity("iquest", 191),
			new Entity("Agrave", 192), new Entity("Aacute", 193), new Entity("Acirc", 194), new Entity("Atilde", 195),
			new Entity("Auml", 196), new Entity("Aring", 197), new Entity("AElig", 198), new Entity("Ccedil", 199),
			new Entity("Egrave", 200), new Entity("Eacute", 201), new Entity("Ecirc", 202), new Entity("Euml", 203),
			new Entity("Igrave", 204), new Entity("Iacute", 205), new Entity("Icirc", 206), new Entity("Iuml", 207),
			new Entity("ETH", 208), new Entity("Ntilde", 209), new Entity("Ograve", 210), new Entity("Oacute", 211),
			new Entity("Ocirc", 212), new Entity("Otilde", 213), new Entity("Ouml", 214), new Entity("times", 215),
			new Entity("Oslash", 216), new Entity("Ugrave", 217), new Entity("Uacute", 218), new Entity("Ucirc", 219),
			new Entity("Uuml", 220), new Entity("Yacute", 221), new Entity("THORN", 222), new Entity("szlig", 223),
			new Entity("agrave", 224), new Entity("aacute", 225), new Entity("acirc", 226), new Entity("atilde", 227),
			new Entity("auml", 228), new Entity("aring", 229), new Entity("aelig", 230), new Entity("ccedil", 231),
			new Entity("egrave", 232), new Entity("eacute", 233), new Entity("ecirc", 234), new Entity("euml", 235),
			new Entity("igrave", 236), new Entity("iacute", 237), new Entity("icirc", 238), new Entity("iuml", 239),
			new Entity("eth", 240), new Entity("ntilde", 241), new Entity("ograve", 242), new Entity("oacute", 243),
			new Entity("ocirc", 244), new Entity("otilde", 245), new Entity("ouml", 246), new Entity("divide", 247),
			new Entity("oslash", 248), new Entity("ugrave", 249), new Entity("uacute", 250), new Entity("ucirc", 251),
			new Entity("uuml", 252), new Entity("yacute", 253), new Entity("thorn", 254), new Entity("yuml", 255),
			new Entity("fnof", 402), new Entity("Alpha", 913), new Entity("Beta", 914), new Entity("Gamma", 915),
			new Entity("Delta", 916), new Entity("Epsilon", 917), new Entity("Zeta", 918), new Entity("Eta", 919),
			new Entity("Theta", 920), new Entity("Iota", 921), new Entity("Kappa", 922), new Entity("Lambda", 923),
			new Entity("Mu", 924), new Entity("Nu", 925), new Entity("Xi", 926), new Entity("Omicron", 927),
			new Entity("Pi", 928), new Entity("Rho", 929), new Entity("Sigma", 931), new Entity("Tau", 932),
			new Entity("Upsilon", 933), new Entity("Phi", 934), new Entity("Chi", 935), new Entity("Psi", 936),
			new Entity("Omega", 937), new Entity("alpha", 945), new Entity("beta", 946), new Entity("gamma", 947),
			new Entity("delta", 948), new Entity("epsilon", 949), new Entity("zeta", 950), new Entity("eta", 951),
			new Entity("theta", 952), new Entity("iota", 953), new Entity("kappa", 954), new Entity("lambda", 955),
			new Entity("mu", 956), new Entity("nu", 957), new Entity("xi", 958), new Entity("omicron", 959),
			new Entity("pi", 960), new Entity("rho", 961), new Entity("sigmaf", 962), new Entity("sigma", 963),
			new Entity("tau", 964), new Entity("upsilon", 965), new Entity("phi", 966), new Entity("chi", 967),
			new Entity("psi", 968), new Entity("omega", 969), new Entity("thetasym", 977), new Entity("upsih", 978),
			new Entity("piv", 982), new Entity("bull", 8226), new Entity("hellip", 8230), new Entity("prime", 8242),
			new Entity("Prime", 8243), new Entity("oline", 8254), new Entity("frasl", 8260),
			new Entity("weierp", 8472), new Entity("image", 8465), new Entity("real", 8476), new Entity("trade", 8482),
			new Entity("alefsym", 8501), new Entity("larr", 8592), new Entity("uarr", 8593), new Entity("rarr", 8594),
			new Entity("darr", 8595), new Entity("harr", 8596), new Entity("crarr", 8629), new Entity("lArr", 8656),
			new Entity("uArr", 8657), new Entity("rArr", 8658), new Entity("dArr", 8659), new Entity("hArr", 8660),
			new Entity("forall", 8704), new Entity("part", 8706), new Entity("exist", 8707), new Entity("empty", 8709),
			new Entity("nabla", 8711), new Entity("isin", 8712), new Entity("notin", 8713), new Entity("ni", 8715),
			new Entity("prod", 8719), new Entity("sum", 8721), new Entity("minus", 8722), new Entity("lowast", 8727),
			new Entity("radic", 8730), new Entity("prop", 8733), new Entity("infin", 8734), new Entity("ang", 8736),
			new Entity("and", 8743), new Entity("or", 8744), new Entity("cap", 8745), new Entity("cup", 8746),
			new Entity("int", 8747), new Entity("there4", 8756), new Entity("sim", 8764), new Entity("cong", 8773),
			new Entity("asymp", 8776), new Entity("ne", 8800), new Entity("equiv", 8801), new Entity("le", 8804),
			new Entity("ge", 8805), new Entity("sub", 8834), new Entity("sup", 8835), new Entity("nsub", 8836),
			new Entity("sube", 8838), new Entity("supe", 8839), new Entity("oplus", 8853), new Entity("otimes", 8855),
			new Entity("perp", 8869), new Entity("sdot", 8901), new Entity("lceil", 8968), new Entity("rceil", 8969),
			new Entity("lfloor", 8970), new Entity("rfloor", 8971), new Entity("lang", 9001), new Entity("rang", 9002),
			new Entity("loz", 9674), new Entity("spades", 9824), new Entity("clubs", 9827), new Entity("hearts", 9829),
			new Entity("diams", 9830), new Entity("quot", 34), new Entity("amp", 38), new Entity("lt", 60),
			new Entity("gt", 62), new Entity("OElig", 338), new Entity("oelig", 339), new Entity("Scaron", 352),
			new Entity("scaron", 353), new Entity("Yuml", 376), new Entity("circ", 710), new Entity("tilde", 732),
			new Entity("ensp", 8194), new Entity("emsp", 8195), new Entity("thinsp", 8201), new Entity("zwnj", 8204),
			new Entity("zwj", 8205), new Entity("lrm", 8206), new Entity("rlm", 8207), new Entity("ndash", 8211),
			new Entity("mdash", 8212), new Entity("lsquo", 8216), new Entity("rsquo", 8217), new Entity("sbquo", 8218),
			new Entity("ldquo", 8220), new Entity("rdquo", 8221), new Entity("bdquo", 8222),
			new Entity("dagger", 8224), new Entity("Dagger", 8225), new Entity("permil", 8240),
			new Entity("lsaquo", 8249), new Entity("rsaquo", 8250), new Entity("euro", 8364)

	};

	public static EntityTable getDefaultEntityTable() {
		if (defaultEntityTable == null) {
			defaultEntityTable = new EntityTable();
			for (int i = 0; i < entities.length; i++) {
				defaultEntityTable.install(entities[i]);
			}
		}
		return defaultEntityTable;
	}

}