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
package de.juwimm.cms.safeguard.realmlogin;

import java.util.Collection;

/**
 * @author <a href="mailto:michael.meyer@juwimm.com">Michael Meyer</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public interface SafeguardLoginManager {
	public static final byte LOGIN_SUCCESSFULLY = 0;
	public static final byte LOGIN_UNAUTHENTICATED = -1;
	public static final byte LOGIN_UNAUTHORIZED = -2;

	public byte login();

	public Collection<String> getRoles();

}
