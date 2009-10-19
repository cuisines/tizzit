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
package de.juwimm.cms.authorization;

import java.lang.reflect.Method;
import java.rmi.Remote;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

/**
 * <p>Title: Tizzit</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author Richard Hightower
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class JbossFlushAuthCache {
	private static Logger log = Logger.getLogger(JbossFlushAuthCache.class);
	private Remote server = null;
	private Context initialContext = null;

	public void flushAuthCache() {
		if (log.isDebugEnabled()) log.debug("begin flushAuthCache");
		try {
			ObjectName jaasMgr = new ObjectName("jboss.security:service=JaasSecurityManager");
			Object[] params = {"juwimm-cms-security-domain"};
			String[] signature = {"java.lang.String"};
			invoke(jaasMgr, "flushAuthenticationCache", params, signature);
		} catch (Exception exe) {
			log.error("Error flushing the authentication cache!", exe);
		}
		if (log.isDebugEnabled()) log.debug("end flushAuthCache");
	}

	protected Object invoke(ObjectName name, String method, Object[] args, String[] sig) throws Exception {
		return invoke(getServer(), name, method, args, sig);
	}

	protected Object invoke(Remote remoteServer, ObjectName name, String method, Object[] args, String[] sig) throws Exception {
		//((org.jboss.jmx.adaptor.rmi.RMIAdaptor) server).
		//invoke(name, method, args, sig);
		Class< ? >[] argTypes = new Class[] {ObjectName.class, String.class, Object[].class, String[].class};
		Method m = remoteServer.getClass().getMethod("invoke", argTypes);
		return m.invoke(remoteServer, new Object[] {name, method, args, sig});
	}

	private Remote getServer() throws Exception {
		init();
		return server;
	}

	protected void init() throws Exception {
		if (initialContext == null) {
			initialContext = new InitialContext();
		}

		if (server == null) {
			server = (Remote) initialContext.lookup("jmx/invoker/RMIAdaptor");
		}
	}

}
