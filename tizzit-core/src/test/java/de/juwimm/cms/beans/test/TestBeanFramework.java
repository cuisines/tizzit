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

import java.util.Properties;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class TestBeanFramework extends TestCase {
	private static Logger log = Logger.getLogger(TestBeanFramework.class);
	private ApplicationContext ctx = null;

	protected void setUp() throws Exception {
		super.setUp();
		Properties prop = new Properties();
		prop.setProperty("log4j.rootLogger", "ERROR, STDOUT");
		prop.setProperty("log4j.category.org.apache", "INFO");
		prop.setProperty("log4j.category.httpclient.wire", "INFO");
		prop.setProperty("log4j.category.de.juwimm", "ERROR");
		prop.setProperty("log4j.appender.STDOUT", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.STDOUT.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.STDOUT.layout.ConversionPattern", "%d %-5p [%-16t] %c{1} - %m%n");
		PropertyConfigurator.configure(prop);
		log.debug("starting");
		ctx = new ClassPathXmlApplicationContext("/de/juwimm/cms/beans/test/beans.xml");
	}

	public void testInvocationCache() throws Exception {
		log.debug("starting testBeans");
		TestResult tr = (TestResult) ctx.getBean("cacheable");
		/*		for (int i = 0; i < 100; i++) {
		 System.out.println(tr.testResultAdder());
		 Thread.sleep(10);
		 }*/
		int b = tr.testResultAdder();
		int b2 = tr.testResultAdder();
		assertEquals(b, b2);
		log.debug("finishing testBeans");
	}

	public void testInvocationCacheWithMethodAttributes() throws Exception {
		TestResult tr = (TestResult) ctx.getBean("cacheable");
		int q = tr.testResultAdder2("q");
		int b = tr.testResultAdder2("b");
		int b2 = tr.testResultAdder2("b");

		assertTrue(q != b);
		assertEquals(b, b2);
	}
}
