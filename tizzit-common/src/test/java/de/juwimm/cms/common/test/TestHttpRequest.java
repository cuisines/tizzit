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
/*
 * Created on 22.11.2005
 */
package de.juwimm.cms.common.test;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Ignore;

import de.juwimm.cms.common.http.HttpClientWrapper;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
@Ignore
public class TestHttpRequest {
	private static Logger log = Logger.getLogger(TestHttpRequest.class);
	public static final String URL = "http://qmd.local/deutsch/home/page.xml";
	public static final int NUMBER_OF_THREADS = 1;
	public static final int NUMBER_OF_REQUESTS = 1;

	protected TestHttpRequest() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		TestHttpRequest test = new TestHttpRequest();
		for (int i = 0; i < TestHttpRequest.NUMBER_OF_THREADS; i++) {
			log.info("Starting TestThread" + i + "...");
			TestThread ts = test.new TestThread(TestHttpRequest.URL, i);
			ts.start();
		}
		log.info("Finished!");
	}

	/**
	 * 
	 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
	 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
	 * @version $Id$
	 */
	public class TestThread extends Thread {
		private String url = null;
		private int threadNo = 0;

		public TestThread(String url, int threadNo) {
			log.debug("Thread" + threadNo + " created!");
			this.url = url;
			this.threadNo = threadNo;
		}

		@Override
		public void run() {
			HttpClientWrapper http = HttpClientWrapper.getInstance();
			try {
				for (int i = 0; i < TestHttpRequest.NUMBER_OF_REQUESTS; i++) {
					String tmp = http.getString(url);
					String result = tmp;
					if (tmp != null || tmp.length() > 30) {
						result = tmp.substring(0, 30) + "...";
					}
					log.info("Thread" + this.threadNo + " Request" + i + ": " + result);
				}
			} catch (IOException e) {
				log.error("Thread" + this.threadNo + ":\t" + e.getMessage());
			}
		}

	}

}
