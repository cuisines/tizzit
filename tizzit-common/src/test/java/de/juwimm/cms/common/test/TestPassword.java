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
package de.juwimm.cms.common.test;

import junit.framework.TestCase;
import de.juwimm.cms.common.DesEncrypter;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class TestPassword extends TestCase {
	public void testEncoding() throws Exception {
		DesEncrypter encrypter = new JUnitDesEncrypter("junitsecret");
		String prev = "DESTEST";
		String encrypted = encrypter.encrypt(prev);
		System.out.printf("Funny String was : %s", encrypted);
		// Decrypt
		String decrypted = encrypter.decrypt(encrypted);
		assertEquals(prev, decrypted);
	}

	public void testDecoding() throws Exception {
		DesEncrypter encrypter = new JUnitDesEncrypter("junitsecret");
		String decrypted = encrypter.decrypt("FkwZWUdz6AY=");
		assertEquals("DESTEST", decrypted);
	}

	/**
	 * 
	 */
	public class JUnitDesEncrypter extends DesEncrypter {
		public JUnitDesEncrypter(String passphrase) {
			super(passphrase, new byte[] {(byte) 0xB4, (byte) 0x1A, (byte) 0xD3, (byte) 0x1C, (byte) 0x77, (byte) 0xA6, (byte) 0x50, (byte) 0x93});
		}
	}

}
