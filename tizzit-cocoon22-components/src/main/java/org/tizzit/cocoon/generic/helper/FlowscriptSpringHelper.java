package org.tizzit.cocoon.generic.helper;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.tizzit.cocoon.generic.support.FlowscriptUtils;

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
}
