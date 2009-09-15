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
/*
 * Created on 20.10.2004
 */
package de.juwimm.cms.cocoon.acting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.SingleThreaded;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Logger;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;

/**
 * An <code>Action</code> that matches a string from within the host parameter of the HTTP request.</br>
 * This action searches for the host parameter in the database to enable redirection in cocoon.</br>
 * </pre>
 * <p><h5>Configuration:</h5>
 * <pre>
 * &lt;map:action name="cms.actions.hostselector" pool-max="128" src="de.juwimm.cms.cocoon.acting.HostSelectorAction"/&gt;
 * </pre>
 * <p><h5>Usage:</h5>
 * <pre>
 * &lt;map:match pattern=".*" type="regexp"&gt;
 *    &lt;map:match pattern=""&gt;
 *       &lt;map:act type="cms.actions.hostselector"&gt;
 *          &lt;map:select type="parameter"&gt;
 *             &lt;map:parameter name="parameter-selector-test" value="{redirectURL}"/&gt;
 *             &lt;map:when test="0"&gt;
 *                &lt;map:select type="parameter"&gt;
 *                   &lt;map:parameter name="parameter-selector-test" value="{startpageURL}"/&gt;
 *                   &lt;map:when test="0"&gt;
 *                      &lt;map:mount check-reload="yes" src="{system-property:cqMandatorParent}{mandatorDir}" uri-prefix=""/&gt;
 *                   &lt;/map:when&gt;
 *                   &lt;map:otherwise&gt;
 *                      &lt;map:redirect-to uri="{startpageURL}"/&gt;
 *                   &lt;/map:otherwise&gt;
 *                &lt;/map:select&gt;
 *             &lt;/map:when&gt;
 *             &lt;map:otherwise&gt;
 *                &lt;map:redirect-to uri="{redirectURL}"/&gt;
 *             &lt;/map:otherwise&gt;
 *          &lt;/map:select&gt;
 *       &lt;/map:act&gt;
 *    &lt;/map:match&gt;
 *    &lt;map:match pattern="*"&gt;
 *       &lt;map:act type="cms.actions.hostselector"&gt;
 *          &lt;map:select type="parameter"&gt;
 *             &lt;map:parameter name="parameter-selector-test" value="{redirectURL}"/&gt;
 *             &lt;map:when test="0"&gt;
 *                &lt;map:mount check-reload="yes" src="{system-property:cqMandatorParent}{mandatorDir}" uri-prefix=""/&gt;
 *             &lt;/map:when&gt;
 *             &lt;map:otherwise&gt;
 *                &lt;map:redirect-to uri="{redirectURL}"/&gt;
 *             &lt;/map:otherwise&gt;
 *          &lt;/map:select&gt;
 *       &lt;/map:act&gt;
 *    &lt;/map:match&gt;
 *    &lt;map:match pattern="* /**"&gt;
 *       &lt;map:act type="cms.actions.hostselector"&gt;
 *          &lt;map:select type="parameter"&gt;
 *             &lt;map:parameter name="parameter-selector-test" value="{redirectURL}"/&gt;
 *             &lt;map:when test="0"&gt;
 *                &lt;map:mount check-reload="yes" src="{system-property:cqMandatorParent}{mandatorDir}" uri-prefix=""/&gt;
 *             &lt;/map:when&gt;
 *             &lt;map:otherwise&gt;
 *                &lt;map:redirect-to uri="{redirectURL}"/&gt;
 *             &lt;/map:otherwise&gt;
 *          &lt;/map:select&gt;
 *       &lt;/map:act&gt;
 *    &lt;/map:match&gt;
 * &lt;/map:match&gt;
 * </pre>
 * </p>
 * <p><h5>Result:</h5>The action returns up to three values as parameters to the sitemap:</br>
 * <ul>
 * <li>redirectURL - an url if this host is configured to redirect to another host or external url, &quot;0&quot; if no redirect configured</li>
 * <li>mandatorDir - the path to the sitemap for the site for this host. This value is found in the field <codeA>mandator_dir</code> in the table <code>site</code>.</li>
 * <li>startpageURL - an url for redirecting the client. If no url is found this value equals &quot;0&quot;.</li>
 * </ul>
 * This action returns &quot;null&quot; if the request does not contain a host value, the host is empty,</br>
 * the host isn't found in the database or the field &quot;mandatorDir&quot; equals the empty string.
 * </p>
 *
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @since ConQuest 2.0
 */
public class HostSelectorAction extends AbstractAction implements SingleThreaded {
	private Logger log = Logger.getLogger(HostSelectorAction.class);
	private WebServiceSpring webSpringBean = null;

	private static final String MANDATOR_DIR = "mandatorDir";
	private static final String STARTPAGE_URL = "startpageURL";
	private static final String REDIRECT_URL = "redirectURL";

	public HostSelectorAction() {

	}

	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		if (log.isDebugEnabled()) log.debug("start acting");
		try {
			webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
		} catch (Exception exf) {
			log.error("Could not load WebServiceSpring!", exf);
		}
		Map<String, String> sitemapParams = new HashMap<String, String>();
		// Get the host request header
		Request request = ObjectModelHelper.getRequest(objectModel);
		String requestPath = this.getRequestedURL(objectModel);
		String host = request.getHeader("Host");
		int portPosition = host.lastIndexOf(":");
		if (portPosition > 0) {
			host = host.substring(0, portPosition);
		}
		if (host == null) {
			if (log.isDebugEnabled()) log.debug("No Host header -- failing.");
			return null;
		} else if ("".equalsIgnoreCase(host)) {
			if (log.isDebugEnabled()) log.debug("Host header empty -- failing.");
			return null;
		} else if ("127.0.0.1".equalsIgnoreCase(host) || "localhost".equalsIgnoreCase(host) && "prod".equals(System.getProperty("org.apache.cocoon.mode", "prod"))) {
			if (log.isDebugEnabled()) log.debug("Host \"localhost\" -- skipping (" + requestPath + ")");
			return null;
		} else {
			if (log.isDebugEnabled()) log.debug("found host: " + host);
			// first check for some redirects for this host
			String redirectUrl = this.webSpringBean.resolveRedirect(host, requestPath, new HashSet<String>());
			if (redirectUrl != null && !"".equalsIgnoreCase(redirectUrl)) {
				if (log.isDebugEnabled()) log.debug("found redirectUrl: " + redirectUrl);
				sitemapParams.put(HostSelectorAction.REDIRECT_URL, redirectUrl);
				return sitemapParams;
			}
			sitemapParams.put(HostSelectorAction.REDIRECT_URL, "0");
			// look mandatorDir of site and startpage
			String mandatorDir = this.webSpringBean.getMandatorDir(host);
			if ("".equalsIgnoreCase(mandatorDir)) { return null; }
			String startPageUrl = this.webSpringBean.getStartPage(host);
			if (log.isDebugEnabled()) {
				log.debug("found " + HostSelectorAction.MANDATOR_DIR + ": " + mandatorDir);
				log.debug("found " + HostSelectorAction.STARTPAGE_URL + ": " + startPageUrl);
			}
			if ("".equalsIgnoreCase(startPageUrl)) {
				startPageUrl = "0";
			}
			sitemapParams.put(HostSelectorAction.MANDATOR_DIR, mandatorDir);
			sitemapParams.put(HostSelectorAction.STARTPAGE_URL, startPageUrl);
		}
		if (log.isDebugEnabled()) log.debug("finished acting");
		return sitemapParams;
	}

	public String getRequestedURL(Map objectModel) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		StringBuffer uribuf = null;
		boolean isSecure = request.isSecure();
		int port = request.getServerPort();

		if (isSecure) {
			uribuf = new StringBuffer("https://");
		} else {
			uribuf = new StringBuffer("http://");
		}

		uribuf.append(request.getServerName());
		if (isSecure) {
			if (port != 443) {
				uribuf.append(":").append(port);
			}
		} else {
			if (port != 80) {
				uribuf.append(":").append(port);
			}
		}

		uribuf.append(request.getRequestURI());
		return uribuf.toString();
	}

}
