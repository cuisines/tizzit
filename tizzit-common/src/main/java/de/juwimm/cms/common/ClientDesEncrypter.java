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
package de.juwimm.cms.common;


/**
 * @author <a href="jens.toerber@juwimm.com">Jens Törber</a>
 * @version $Id$
 */
public class ClientDesEncrypter extends DesEncrypter {
	
	public ClientDesEncrypter() {
		//ConQuest2.2ClientKeyWhichWeWantToUse
		this("CQ22ckWwWtU");
	}
	
	public ClientDesEncrypter(String passphrase) {
		super(passphrase, new byte[] {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35,
				(byte) 0xE3, (byte) 0x03});
	}
	
	
}
