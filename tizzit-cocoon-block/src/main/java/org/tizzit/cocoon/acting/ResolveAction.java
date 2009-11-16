/*
 * Copyright (c) 2002-2009 Juwi MacMillan Group GmbH (JuwiMM)
 * Bockhorn 1, 29664 Walsrode, Germany
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of JuwiMM
 * ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with JuwiMM.
 */
package org.tizzit.cocoon.acting;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tizzit.cocoon.generic.helper.MapHelper;
import org.tizzit.cocoon.generic.helper.RequestHelper;

import de.juwimm.cms.vo.SiteValue;

// TODO: Class description
/**
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-cocoon-block 09.11.2009
 */
public class ResolveAction extends HostSelectorAction {
	private static final Log log = LogFactory.getLog(ResolveAction.class);

	//	private static final String VIEW_COMPONENT_ID = "viewComponentId";
	private static final String RTL = "rtl";
	private static final String TEMPLATE_NAME = "templateName";

	private static final String SITE_SHORT = "siteShort";
	private static final String SITE_ID = "siteId";
	private static final String LANGUAGE_CODE = "languageCode";

	/* (non-Javadoc)
	 * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector, org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		if (log.isDebugEnabled()) log.debug("act() -> begin");
		Map<String, String> sitemapParameter = super.act(redirector, resolver, objectModel, source, parameters);

		//TODO: implement safeguard features

		sitemapParameter.put(ResolveAction.RTL, "false");
		sitemapParameter.put(ResolveAction.TEMPLATE_NAME, sitemapParameter.get("template"));
		sitemapParameter.put(ResolveAction.LANGUAGE_CODE, sitemapParameter.get("language"));

		SiteValue sv = this.webServiceSpring.getSiteValueForHost(RequestHelper.getHost(objectModel));
		sitemapParameter.put(ResolveAction.SITE_SHORT, sv.getShortName());
		sitemapParameter.put(ResolveAction.SITE_ID, sv.getSiteId().toString());

		if (log.isDebugEnabled()) log.debug(MapHelper.mapToString(sitemapParameter));

		if (log.isDebugEnabled()) log.debug("act() -> end");
		return sitemapParameter;
	}
}
