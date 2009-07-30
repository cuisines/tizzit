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
package de.juwimm.cms.authorization.jaas.ldap;

import java.security.PrivilegedExceptionAction;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;

import org.jboss.security.plugins.JaasSecurityDomainMBean;
import org.jboss.mx.util.MBeanServerLocator;

/**
 * PriviledgedActions used by login modules for decoding passwords
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57203 $
 */
class DecodeAction implements PrivilegedExceptionAction {
	String password;
	ObjectName serviceName;

	DecodeAction(String password, ObjectName serviceName) {
		this.password = password;
		this.serviceName = serviceName;
	}

	/**
	 * @throws Exception
	 */
	public Object run() throws Exception {
		MBeanServer server = MBeanServerLocator.locateJBoss();
		JaasSecurityDomainMBean securityDomain = (JaasSecurityDomainMBean) MBeanServerInvocationHandler.newProxyInstance(server, serviceName, JaasSecurityDomainMBean.class, false);

		// Invoke the jaasSecurityDomain.decodeb64 op
		byte[] secret = securityDomain.decode64(password);
		// Convert to UTF-8 base char array
		String secretPassword = new String(secret, "UTF-8");
		return secretPassword.toCharArray();
	}

	static char[] decode(String password, ObjectName serviceName) throws Exception {
		DecodeAction action = new DecodeAction(password, serviceName);
		try {
			char[] decode = (char[]) AccessController.doPrivileged(action);
			return decode;
		} catch (PrivilegedActionException e) {
			throw e.getException();
		}
	}

}
