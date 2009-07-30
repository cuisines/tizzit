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
package de.juwimm.cms.cocoon.helper;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import de.juwimm.cms.cocoon.support.ConquestMail;
import de.juwimm.cms.cocoon.support.FlowscriptUtils;

/**
 * Helper class for instantiating FlowscriptUtils.
 * 
 * @author <a href="christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 * @version $$Id$$
 */
public class FlowscriptSpringHelper {
	private static Logger log = Logger.getLogger(FlowscriptSpringHelper.class);
	private static WebApplicationContext webApplicationContext = null;

	public static FlowscriptUtils getFlowscriptUtils(ServletContext servletContext) {
		if (webApplicationContext == null) {
			if (log.isDebugEnabled()) {
				log.debug("FlowScriptSpringHelper resolves WebApplicationContext");
			}
			webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		}
		return ((FlowscriptUtils) webApplicationContext.getBean("flowscriptUtils"));
	}
	
	public static ConquestMail getConquestMail(ServletContext servletContext) {
		if (webApplicationContext == null) {
			if (log.isDebugEnabled()) {
				log.debug("FlowScriptSpringHelper resolves WebApplicationContext");
			}
			webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		}
		return ((ConquestMail) webApplicationContext.getBean("conquestMail"));
	}
}
