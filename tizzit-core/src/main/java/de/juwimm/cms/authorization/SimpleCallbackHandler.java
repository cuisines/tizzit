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

import javax.security.auth.callback.*;

/**
 * This is the SimpleCallbackHandler, a handler for authenticate a LoginContext again
 * Username and Password. This is needed for internal use of system user and password.
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Juwi|MacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class SimpleCallbackHandler implements CallbackHandler {
	private String u = "";
	private String p = "";

	public SimpleCallbackHandler(String username, String password) {
		this.u = username;
		this.p = password;
	}

	public void handle(Callback[] callbacks) throws UnsupportedCallbackException {

		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof NameCallback) {
				// prompt the user for a username
				NameCallback nc = (NameCallback) callbacks[i];
				nc.setName(this.u);
			} else if (callbacks[i] instanceof PasswordCallback) {
				PasswordCallback pc = (PasswordCallback) callbacks[i];
				pc.setPassword(this.p.toCharArray());
			}
		}
	}

}