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

import java.net.Proxy;
import java.net.URI;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.juwimm.cms.http.ProxyHelper;

public class TestProxyHelper extends TestCase {
	private static Logger log = Logger.getLogger(TestProxyHelper.class);
	
	private final static String username = "kulawik";
	private final static String password = "system";
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Properties prop = new Properties();
		prop.setProperty("log4j.rootLogger", "INFO, STDOUT");
		prop.setProperty("log4j.category.org.apache", "INFO");
		prop.setProperty("log4j.category.httpclient.wire", "INFO");
		prop.setProperty("log4j.category.de.juwimm", "DEBUG");
		prop.setProperty("log4j.appender.STDOUT", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.STDOUT.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.STDOUT.layout.ConversionPattern", "%d %-5p [%-16t] %c{1} - %m%n");
		PropertyConfigurator.configure(prop);
	}
	
	public void testProxies() throws Exception {
		ProxyHelper ph = new ProxyHelper();
		URI dest = new URI("http://work.conquest-cms.net/admin");
		
		ph.init(dest, true);
		log.info("Listing proxies");
		List<Proxy> lp = ph.getHttpProxies();
		for (Proxy proxy : lp) {
			log.info(proxy.toString());
		}
		log.info("Listed proxies");
		/*
		
		
		// ph.setHttpProxyUser(TestProxyHelper.username, TestProxyHelper.password);
		
		ph.setHttpProxySystemProperties("10.0.0.99", "3128"); // squid ohne
		testProxyWithoutAuthentication();
		
		ph.setHttpProxySystemProperties("10.0.0.250", "3128"); // squid mit Base
		// testProxyWithBaseAuthentication("", "");
		testProxyWithBaseAuthentication(TestProxyHelper.username, TestProxyHelper.password);
		
		ph.setHttpProxySystemProperties("10.0.0.253", "8080"); // ISA NTLM
		// testProxyWithNTLMAuthentication("", "");
		testProxyWithNTLMAuthentication(TestProxyHelper.username, TestProxyHelper.password);
		*/
	}
	/*
	public void testProxyWithoutAuthentication() throws HttpException {
		HttpClientWrapper clientWrapper = HttpClientWrapper.getInstance();		
		
		clientWrapper.testAndConfigureConnection("https://ris.stadt-soltau.de");		
	}
	
	public void testProxyWithBaseAuthentication(String username, String password) throws HttpException {
		HttpClientWrapper clientWrapper = HttpClientWrapper.getInstance();		
		
		clientWrapper.testAndConfigureConnection("https://ris.stadt-soltau.de", username, password);
	}
	
	public void testProxyWithNTLMAuthentication(String username, String password) throws HttpException {
		HttpClientWrapper clientWrapper = HttpClientWrapper.getInstance();		
		
		clientWrapper.testAndConfigureConnection("https://ris.stadt-soltau.de", username, password);
	}*/
}
