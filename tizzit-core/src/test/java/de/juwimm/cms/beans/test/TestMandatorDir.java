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
package de.juwimm.cms.beans.test;

import java.util.regex.*;

import junit.framework.TestCase;

public class TestMandatorDir extends TestCase {

	
	public void testIpRegex() throws Exception {
		assertTrue(isIpAddress("192.168.1.1"));
		assertTrue(isIpAddress("localhost"));
		assertTrue(isIpAddress("127.0.0.1"));
		assertTrue(isIpAddress("222.222.222.122"));
		assertTrue(isIpAddress("1.1.1.1"));
		assertTrue(isIpAddress("01.1.1.1"));
		assertTrue(isIpAddress("001.1.1.1"));
		assertFalse(isIpAddress("0001.1.1.1"));
		assertTrue(isIpAddress("1.001.1.1"));
		assertTrue(isIpAddress("1.001.001.1"));
		assertTrue(isIpAddress("1.001.1.001"));
		assertTrue(isIpAddress("1.000.000.1"));
		assertTrue(isIpAddress("1.0.1.1"));
		assertFalse(isIpAddress("1.1.1.0"));
		assertFalse(isIpAddress(".0.0.1"));
		assertTrue(isIpAddress("100.0.0.1"));
		assertFalse(isIpAddress("256.0.0.1"));
		assertFalse(isIpAddress("1.256.0.1"));
		assertFalse(isIpAddress("0.255.0.1"));
		assertFalse(isIpAddress("novwork.cqcms.net"));
		assertFalse(isIpAddress("www.192.168.1.1.ranzmade.de"));
		assertFalse(isIpAddress("www.heise.de"));
		assertFalse(isIpAddress("www.localhost.de"));
	}
	
	private static Pattern pat = Pattern.compile("((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0]?[1-9][0-9]|[0]{0,2}[1-9])\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0]?[1-9][0-9]|[0]{0,2}[1-9]))|localhost");
	
	
	private boolean isIpAddress(String adr) {
		if(pat.matcher(adr).matches()) {
			System.out.println("IP ADDRESS: " + adr);
			return true;
		} else {
			System.out.println("HOST" + adr);
			return false;
		}
	}
}
