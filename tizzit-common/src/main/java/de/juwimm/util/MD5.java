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

import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * @version $Id$
 */
public class MD5 {
	private static final Log log = LogFactory.getLog(MD5.class);

	public static final String computeHash(String stringToCompile) {
		String retVal = null;

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.reset();
			md5.update(stringToCompile.getBytes());
			byte[] result = md5.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < result.length; i++) {
				hexString.append(Integer.toHexString(0xFF & result[i]));
			}
			retVal = hexString.toString();
			if (log.isDebugEnabled()) log.debug("MD5 hash for \"" + stringToCompile + "\" is: " + retVal);
		} catch (Exception exe) {
			log.error(exe.getMessage(), exe);
		}

		return retVal;
	}
}
