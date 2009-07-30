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
package de.juwimm.cms.authorization.test;

import java.security.MessageDigest;

import junit.framework.Assert;
import junit.framework.TestCase;
import de.juwimm.util.Base64;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Revision: 3012 $
 */
public class TestAuthorization extends TestCase {
	/**
	 * Sets up the fixture, for example, open a network connection.
	 * This method is called before a test is executed.
	 * @throws Exception
	 */
	protected void setUp() throws Exception {
	}
	
	public void testPasswordEncoding() throws Throwable {
		String atHashAlgorithm    = "SHA-1";
		//String atHashEncoding     = "base64";
		String newPassword     	  = "joekel";
		byte[] hash = MessageDigest.getInstance(atHashAlgorithm).digest(newPassword.getBytes());
		Assert.assertEquals("zFaW6CdmxKJMxNPVannhwAywmWo=", Base64.encodeBytes(hash));
	}
	
	/*public void testPasswordEncodingWithUmlauts() throws Throwable {
		String atHashAlgorithm    = "SHA-1";
		String atHashEncoding     = "base64";
		String newPassword     	  = "jökel";
		byte[] hash = MessageDigest.getInstance(atHashAlgorithm).digest(newPassword.getBytes());
		Assert.assertEquals("zy08ihQBowTKxXehSRkaLIPEIl4=", Base64.encodeBytes(hash));
	}*/
	
	/*
	 *SHA-1/base64 inputPassword PElZgzoHcKvmJUvaIYFZhHUopfg= expectedPassword zy08ihQBowTKxXehSRkaLIPEIl4=
	 *PLAINTEXT inputPassword luN4TSq3HxFVcnfCdZ3JjFtJP4w= expectedPassword zy08ihQBowTKxXehSRkaLIPEIl4=
	 *SHA-1/URL inputPassword %3CIY%3F%3A%07p%3F%3F%25K%3F%21%3FY%3Fu%28%3F%3F expectedPassword zy08ihQBowTKxXehSRkaLIPEIl4=
	 *
	 *SHA-1/base64 inputPassword luN4TSq3HxFVcnfCdZ3JjFtJP4w= expectedPassword zy08ihQBowTKxXehSRkaLIPEIl4=
	 *PLAINTEXT inputPassword j?kel expectedPassword zy08ihQBowTKxXehSRkaLIPEIl4=
	 *SHA-1/URL inputPassword %3F%3FxM*%3F%1F%11Urw%3Fu%3F%3F%5BI%3F%3F expectedPassword zy08ihQBowTKxXehSRkaLIPEIl4=
	 *
	 *SHA-1/base64 inputPassword 7xqezGPykhlFNN8lQ2R345Cuhmw= expectedPassword luN4TSq3HxFVcnfCdZ3JjFtJP4w=
	 *PLAINTEXT inputPassword jökel expectedPassword luN4TSq3HxFVcnfCdZ3JjFtJP4w=
	 *
	 */
}
