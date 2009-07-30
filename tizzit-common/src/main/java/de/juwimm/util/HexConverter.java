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
package de.juwimm.util;

import java.io.PrintWriter;

/**
 * Utility-Class for converting Strings to hex-format and the other way round
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @since 2.0.2
 */
public final class HexConverter {
	/** maximum int value that can be converted to char */
	public static final int MAX_CHAR_INT = 35;

	/**
	 *   encodes a string of 4-digit hex-numbers to unicode
	 *   <br />
	 *   Note: this method is slower than hexToString(String,StringBuffer)
	 *   @param hex-string of 4-digit hex-numbers
	 *   @return string-representation of hex-numbers
	 */
	public static final String hexToString(String hex) {
		if (hex == null) return "";
		StringBuffer result = new StringBuffer();
		hexToString(hex, result);
		return result.toString();
	}

	/**
	 *   encodes a string of 4-digit hex-numbers to unicode
	 *   @param hex-string of 4-digit hex-numbers
	 *   @param out string-representation of hex-numbers
	 *   @todo to improve performance, implement a table-lookup instead of parseInt(substring())
	 */
	public static final void hexToString(String hex, StringBuffer out) {
		if (hex == null) return;
		int length = hex.length() & -4;
		for (int pos = 0; pos < length; pos += 4) {
			int thisChar = 0;
			try {
				thisChar = Integer.parseInt(hex.substring(pos, pos + 4), 16);
			} catch (NumberFormatException nfe) { /* dont care*/
			}
			out.append((char) thisChar);
		}
	}

	/**
	 *   encodes a unicode string to a string of 4-digit hex-numbers
	 *   <br />
	 *   Note: this method is slower than stringToHex(String,StringBuffer)
	 *   @param java normal java string
	 *   @return string of 4-digit hex-numbers
	 */
	public static final String stringToHex(String java) {
		if (java == null) return "";
		int length = java.length();
		StringBuffer result = new StringBuffer(length * 4);
		stringToHex(java, result);
		return result.toString();
	}

	/**
	 *   encodes a unicode string to a string of 4-digit hex-numbers
	 *   @param java normal java string
	 *   @param out string of 4-digit hex-numbers
	 *   @todo to improve performance, implement a table-lookup instead of if/then cases  
	 */
	public static final void stringToHex(String java, StringBuffer out) {
		if (java == null) return;
		int length = java.length();
		for (int pos = 0; pos < length; pos++) {
			int thisChar = (int) java.charAt(pos);
			for (int digit = 0; digit < 4; digit++) {
				int thisDigit = thisChar & 0xf000;
				thisChar = thisChar << 4;
				thisDigit = (thisDigit >> 12);
				if (thisDigit >= 10)
					out.append((char) (thisDigit + 87));
				else
					out.append((char) (thisDigit + 48));
			}
		}
	}

	/**
	 *   encodes a unicode string to a string of 4-digit hex-numbers
	 *   @param java normal java string
	 *   @param out string of 4-digit hex-numbers
	 *   @todo to improve performance, implement a table-lookup instead of if/then cases  
	 */
	public static final void stringToHex(String java, PrintWriter out) {
		if (java == null) return;
		int length = java.length();
		for (int pos = 0; pos < length; pos++) {
			int this_char = (int) java.charAt(pos);
			for (int digit = 0; digit < 4; digit++) {
				int this_digit = this_char & 0xf000;
				this_char = this_char << 4;
				this_digit = (this_digit >> 12);
				if (this_digit >= 10)
					out.print((char) (this_digit + 87));
				else
					out.print((char) (this_digit + 48));
			}
		}
	}

}
