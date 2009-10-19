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
package de.juwimm.cms.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

import spin.Spin;
import spin.proxy.CGLibProxyFactory;
import de.juwimm.cms.remote.ClientServiceSpring;

/**
 * <p>Title: Tizzit </p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class CommunicationFactory implements FactoryBean {
	private static Logger log = Logger.getLogger(CommunicationFactory.class);
	private static Communication instance = null;
	private ClientServiceSpring clientService;

	private CommunicationFactory() {
	}

	public Communication getObject() throws Exception {
		if (instance == null) {
			Spin.setDefaultProxyFactory(new CGLibProxyFactory());
			instance = (Communication) Spin.off(new Communication().initialize());
			instance.setClientService(clientService);
			if (log.isDebugEnabled()) log.debug("instanciated new Communication Object by Spring-getObject()");
		}
		return instance;
	}

	public Class getObjectType() {
		return Communication.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public ClientServiceSpring getClientService() {
		return clientService;
	}

	public void setClientService(ClientServiceSpring clientService) {
		this.clientService = clientService;
	}

}
