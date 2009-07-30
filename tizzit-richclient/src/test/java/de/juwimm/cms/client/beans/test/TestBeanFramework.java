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
package de.juwimm.cms.client.beans.test;

import java.util.Properties;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.util.Communication;

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
		prop.setProperty("log4j.rootLogger", "INFO, STDOUT");
		prop.setProperty("log4j.category.org.apache", "INFO");
		prop.setProperty("log4j.category.httpclient.wire", "INFO");
		prop.setProperty("log4j.category.de.juwimm", "DEBUG");
		prop.setProperty("log4j.appender.STDOUT", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.STDOUT.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.STDOUT.layout.ConversionPattern", "%d %-5p [%-16t] %c{1} - %m%n");
		PropertyConfigurator.configure(prop);
		log.debug("starting");
		ctx = new ClassPathXmlApplicationContext("/de/juwimm/cms/client/beans/test/beans.xml");
	}

	public void testBeans() throws Exception {
		log.debug("starting testBeans");
		//BeanFactoryLocator bfLocator = SingletonBeanFactoryLocator.getInstance();
		//BeanFactoryReference bfReference = bfLocator.useBeanFactory("examples.spring");
		/*PoolTestItem comm = (PoolTestItem) ctx.getBean("item");
		PoolTestItem comm2 = (PoolTestItem) ctx.getBean("item");*/
/*
		comm = null;
		assertTrue(comm != comm2);*/
		log.debug("finishing testBeans");
	}
	
	public void testCommunication() throws Exception {
		log.debug("starting testCommunication");
		Communication comm = (Communication) ctx.getBean(Beans.COMMUNICATION);
		Communication comm2  = (Communication) ctx.getBean(Beans.COMMUNICATION);
		assertEquals(comm, comm2);
		assertTrue(comm instanceof Communication);
		assertFalse(comm instanceof Communication);
		log.debug("finishing testCommunication");
	}
}
