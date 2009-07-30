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
package de.juwimm.cms.client.http.test;

import java.util.Properties;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.juwimm.cms.http.HttpClientWrapper;

/**
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class TestHTTPClientWrapper extends TestCase {
	private static Logger log = Logger.getLogger(TestHTTPClientWrapper.class);
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		Properties prop = new Properties();
		prop.setProperty("log4j.rootLogger", "INFO, STDOUT");
		prop.setProperty("log4j.category.org.apache", "INFO");
		prop.setProperty("log4j.category.httpclient.wire", "INFO");
		prop.setProperty("log4j.category.de.juwimm", "INFO");
		prop.setProperty("log4j.appender.STDOUT", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.STDOUT.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.STDOUT.layout.ConversionPattern", "%d %-5p [%-16t] %c{1} - %m%n");
		PropertyConfigurator.configure(prop);
	}
	
	public void testHTTPSwithoutAuthentication() throws Exception {
		log.info("testHTTPSwithoutAuthentication");
		HttpClientWrapper clientWrapper = HttpClientWrapper.getInstance();
		clientWrapper.testAndConfigureConnection("https://ris.stadt-soltau.de");
	}
	
	public void testHTTPSandAuthentication() throws Exception {
		log.info("testHTTPSandAuthentication");
		HttpClientWrapper clientWrapper = HttpClientWrapper.getInstance();
		clientWrapper.testAndConfigureConnection("https://dev.juwimm.net/maven/", "kulawik", "dks4juwimm");
	}
	
	/*public void testHTTPSandAuthenticationandNTLMProxy() throws Exception {
		System.setProperty("https.proxyHost", "10.0.0.168");
		System.setProperty("https.proxyPort", "3128");
		testHTTPSandAuthentication();
	}*/
	
	public void testHTTPSandAuthenticationandSquidProxy() throws Exception {
		log.info("testHTTPSandAuthenticationandSquidProxy");
		System.setProperty("https.proxyHost", "10.0.0.201");
		System.setProperty("https.proxyPort", "3128");
		//assertTrue(testHTTPSandAuthentication().isUsingProxy());
		testHTTPSandAuthentication();
	}
}
