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
package de.juwimm.cms.cocoon;

import java.util.*;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.components.modules.input.AbstractInputModule;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class AllRequestParameterModule extends AbstractInputModule implements ThreadSafe {

	/* (non-Javadoc)
	 * @see org.apache.cocoon.components.modules.input.InputModule#getAttribute(java.lang.String, org.apache.avalon.framework.configuration.Configuration, java.util.Map)
	 */
	@Override
	public Object getAttribute(String name, Configuration modeConf, Map objectModel) throws ConfigurationException {
		StringBuffer rp = new StringBuffer();
		Request req = ObjectModelHelper.getRequest(objectModel);
		Enumeration enume = req.getParameterNames();
		while (enume.hasMoreElements()) {
			String attrName = (String) enume.nextElement();
			rp.append(attrName).append("=").append(req.getParameter(attrName).toString());
			if (enume.hasMoreElements()) rp.append("&");
		}
		return rp.toString();
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.components.modules.input.InputModule#getAttributeNames(org.apache.avalon.framework.configuration.Configuration, java.util.Map)
	 */
	@Override
	public Iterator getAttributeNames(Configuration modeConf, Map objectModel) throws ConfigurationException {
		ArrayList<String> al = new ArrayList<String>(1);
		al.add("allrequest");
		return al.iterator();
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.components.modules.input.InputModule#getAttributeValues(java.lang.String, org.apache.avalon.framework.configuration.Configuration, java.util.Map)
	 */
	@Override
	public Object[] getAttributeValues(String name, Configuration modeConf, Map objectModel) throws ConfigurationException {
		StringBuffer rp = new StringBuffer();
		Request req = ObjectModelHelper.getRequest(objectModel);
		Enumeration enume = req.getParameterNames();
		while (enume.hasMoreElements()) {
			String attrName = (String) enume.nextElement();
			rp.append(attrName).append("=").append(req.getParameter(attrName).toString());
			if (enume.hasMoreElements()) rp.append("&");
		}
		return new Object[] {rp.toString()};
	}

}
