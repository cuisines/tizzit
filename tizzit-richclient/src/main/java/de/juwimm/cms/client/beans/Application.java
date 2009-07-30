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
package de.juwimm.cms.client.beans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.juwimm.cms.common.Constants;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class Application {
	private static ApplicationContext ctx = null;

	private Application() {
	}

	public static final ApplicationContext getCtx() {
		return Application.ctx;
	}

	public static final void initializeContext() {
		System.setProperty("cq.remoteServer", Constants.SERVER_HOST);
		System.setProperty("cq.remotePort", Constants.SERVER_PORT + "");
		System.setProperty("cq.remoteContext", "remote");

		Application.ctx = new ClassPathXmlApplicationContext(new String[] { "/applicationContext-import-remoteServices.xml", "/applicationContext-client.xml" });
	}

	public static Object getBean(String beanName) {
		return Application.ctx.getBean(beanName);
	}

}
