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
package org.tizzit.cocoon.generic.acting;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.acting.ServiceableAction;
import org.apache.cocoon.environment.Redirector;
import org.apache.commons.io.IOUtils;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;
import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * 
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha Kulawik</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class XPathValueFromSourceAction extends ServiceableAction {
	private SourceResolver resolver;
	private static Logger log = Logger.getLogger(XPathValueFromSourceAction.class);

	@Override
	public void service(ServiceManager manager) throws ServiceException {
		super.service(manager);
		this.resolver = (SourceResolver) manager.lookup(SourceResolver.ROLE);
	}

	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, org.apache.cocoon.environment.SourceResolver oldResolver, Map objectModel, String source, Parameters par) throws Exception {
		Map retVal = new HashMap();
		try {
			String xpath = par.getParameter("xpath");
			String source2 = source.replaceAll("[%]2F", "/");

			Source src = resolver.resolveURI(source2);
			InputStream in = src.getInputStream();
			String content = IOUtils.toString(in);
			resolver.release(src);

			Document contentDoc = XercesHelper.string2Dom(content);

			Element value = (Element) XercesHelper.findNode(contentDoc, xpath);
			if (value != null) {
				String strValue = value.getFirstChild().getNodeValue();
				if (log.isDebugEnabled()) log.debug("value: " + strValue);
				retVal.put("nodevalue", strValue);
			}
		} catch (Exception e) {
			log.error(e);
		}
		return retVal;
	}

}
