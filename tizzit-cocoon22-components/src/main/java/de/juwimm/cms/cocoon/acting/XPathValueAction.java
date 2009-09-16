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
package de.juwimm.cms.cocoon.acting;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.avalon.excalibur.pool.Poolable;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ResourceNotFoundException;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.TimeStampValidity;
import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;

/**
 * This action can extract a value from the content of a cms-page.<br/>
 * Therefore it needs the viewComponentId of a page and a key or XPath-Query.<br/>
 * If both parameters are given and set, the XPathQuery is evaluated.<br/>
 * The result is send back to the sitemap.<br/>
 * One usage could be the selection of an email-address for sendung a mail without<br/>
 * transfering the address with a form from the client via http. Now you can give<br/>
 * an id and a key or query and this action passes the requested value to the sitemap.<br/>
 * 
 * <p><h5>Configuration:</h5>
 * <pre>
 * &lt;map:action name="cms.actions.xpathvalue" src="de.juwimm.cms.cocoon.acting.XPathValueAction"/&gt;
 * </pre>
 * <p><h5>Usage:</h5><br/>Normally you will use this action inside the CmsTemplateAction.
 * <pre>
 * &lt;map:match pattern="* /** /kvp-*.*"&gt;
 * 	&lt;map:act type="cms.actions.template"&gt;
 * 		&lt;map:parameter name="siteId" value="{global:clientId}"/&gt;
 * 		&lt;map:parameter name="parameters" value="true"/&gt;
 * 		&lt;map:parameter name="language" value="{1}"/&gt;
 * 		&lt;map:parameter name="path" value="{2}"/&gt;
 * 		&lt;map:parameter name="viewType" value="browser"/&gt;
 * 		&lt;map:act type="cms.actions.xpathvalue"&gt;
 * 			&lt;map:parameter name="viewComponentId" value="{viewComponentId}"/&gt;
 * 			&lt;!-- either --&gt;
 * 			&lt;map:parameter name="XPathQuery" value="//node()[&part;id='1']/*[position() = 2]"/&gt;
 * 			&lt;!-- or --&gt;
 * 			&lt;map:parameter name="key" value="{../3}"/&gt;
 * 			&lt;!-- if both parameters are given and set, the XPathQuery is evaluated --&gt;
 * 			&lt;map:parameter name="liveserver" value="{conquest-properties:liveserver}"/&gt;
 * 			&lt;map:generate src="xml/nix.xml"/&gt;
 * 			&lt;map:transform src="templates/keyvaluepair.xsl"&gt;
 * 			    &lt;map:parameter name="xPathValue" value="{XPathValue}"/&gt;
 * 			    &lt;map:parameter name="viewComponentId" value="{../viewComponentId}"/&gt;
 * 			    &lt;map:parameter name="key" value="{../../3}"/&gt;
 * 			&lt;/map:transform&gt;
 * 		&lt;/map:act&gt;
 * 		&lt;map:match pattern="* /** /kvp-*.xml"&gt;
 * 			&lt;map:serialize type="xml"/&gt;
 * 		&lt;/map:match&gt;
 * 	&lt;/map:act&gt;
 * &lt;/map:match&gt;
 * </pre>
 * If you want to search for a &quot;key&quot;-parameter the xml-content must contain this structure:
 * <pre>
 * &lt;keyvalue dcfname="keyvalue" label="Key-Value-Pair"&gt;
 *     &lt;item description="key1" id="1" timestamp="1097487299283"&gt;
 *         &lt;key dcfname="key" description="key1"&gt;&lt;![CDATA[key1]]&gt;&lt;/key&gt;
 *         &lt;value dcfname="value" description="value1"&gt;&lt;![CDATA[value1]]&gt;&lt;/value&gt;
 *     &lt;/item&gt;
 *     &lt;item description="key2" id="2" timestamp="1097487322357"&gt;
 *         &lt;key dcfname="key" description="key2"&gt;&lt;![CDATA[key2]]&gt;&lt;/key&gt;
 *         &lt;value dcfname="value" description="value2"&gt;&lt;![CDATA[value2]]&gt;&lt;/value&gt;
 *     &lt;/item&gt;
 * &lt;/keyvalue&gt;
 * </pre> 
 * </p>
 * <p><h5>Result:</h5>
 * This action returns the value &quot;XPathValue&quot; to the pipeline.
 * </p>
 * @see de.juwimm.cms.cocoon.acting.CmsTemplateAction
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @since ConQuest 2.0
 */
public class XPathValueAction extends AbstractAction implements CacheableProcessingComponent, Poolable {
	private Logger log = Logger.getLogger(XPathValueAction.class);
	private boolean iAmTheLiveserver = false;
	private WebServiceSpring webSpringBean = null;
	private Serializable uniqueKey;
	private Integer viewComponentId = null;
	private long chgDate = 0;

	private static final String PARENT_OF_KEY = "keyvalue";
	private static final String RESULT_PARAMETER_NAME = "XPathValue";

	/* (non-Javadoc)
	 * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector,
	 * org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
	 */
	public Map<String, String> act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		if (log.isDebugEnabled()) log.debug("start acting");
		try {
			webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
		} catch (Exception exf) {
			log.error("Could not load webservicespringbean!", exf);
		}
		Map<String, String> sitemapParams = new HashMap<String, String>();
		Document contentDoc = null;
		this.viewComponentId = new Integer(parameters.getParameter("viewComponentId"));
		String xPathQuery = "";
		String key = "";
		Request request = ObjectModelHelper.getRequest(objectModel);
		uniqueKey = request.getRequestURI();
		try {
			xPathQuery = parameters.getParameter("XPathQuery");
		} catch (ParameterException pe) {
			if (log.isDebugEnabled()) log.debug("Parameter \"XPathQuery\" is missing " + pe.getMessage());
		}
		try {
			key = parameters.getParameter("key");
		} catch (ParameterException pe) {
			if (log.isDebugEnabled()) log.debug("Parameter \"key\" is missing " + pe.getMessage());
		}
		try {
			iAmTheLiveserver = new Boolean(parameters.getParameter("liveserver")).booleanValue();
		} catch (Exception exe) {
		}
		if (log.isDebugEnabled()) log.debug("begin generate");
		if (log.isDebugEnabled()) log.debug("GETTING XML FROM BEAN");
		try {
			String content = webSpringBean.getContent(viewComponentId, iAmTheLiveserver);
			if (log.isDebugEnabled()) log.debug("GOT XML FROM BEAN");
			contentDoc = XercesHelper.string2Dom(content);
			if (log.isDebugEnabled()) log.debug("end generate");
			if (!("".equals(xPathQuery))) {
				Element value = (Element) XercesHelper.findNode(contentDoc, xPathQuery);
				if (value != null) {
					String strValue = value.getFirstChild().getNodeValue();
					if (strValue != null) {
						sitemapParams.put(XPathValueAction.RESULT_PARAMETER_NAME, strValue);
					} else {
						sitemapParams.put(XPathValueAction.RESULT_PARAMETER_NAME, "");
					}
				} else {
					sitemapParams.put(XPathValueAction.RESULT_PARAMETER_NAME, "");
				}
			} else if (!("".equals(key))) {
				// search for key in content
				Iterator it = XercesHelper.findNodes(contentDoc, "//" + XPathValueAction.PARENT_OF_KEY);
				while (it.hasNext()) {
					Element currentIteration = (Element) it.next();
					Iterator itemIt = XercesHelper.findNodes(currentIteration, "./item");
					while (itemIt.hasNext()) {
						Element currItem = (Element) itemIt.next();
						Node currentKey = XercesHelper.findNode(currItem, "./key");
						String currKey = currentKey.getFirstChild().getNodeValue();
						if (currKey.equalsIgnoreCase(key)) {
							Node valueNode = XercesHelper.findNode(currItem, "./value");
							String value = valueNode.getFirstChild().getNodeValue();
							sitemapParams.put(XPathValueAction.RESULT_PARAMETER_NAME, value);
							break;
						}
					}
				}
			} else {
				// no key or XPathQuery given
				if (log.isDebugEnabled()) log.debug("can't fetch value, neither key nor xpathquery given!");
				sitemapParams.put(XPathValueAction.RESULT_PARAMETER_NAME, "");
			}
		} catch (Exception exe) {
			if (log.isDebugEnabled()) log.debug("An unknown error occured " + exe.getMessage());
		}
		if (contentDoc == null) { throw new ResourceNotFoundException("Could not find resource with viewComponentId " + viewComponentId);
		}
		if (log.isDebugEnabled()) log.debug("finished acting");

		return sitemapParams;
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.caching.CacheableProcessingComponent#getKey()
	 */
	public Serializable getKey() {
		return this.uniqueKey;
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.caching.CacheableProcessingComponent#getValidity()
	 */
	public SourceValidity getValidity() {
		chgDate = 0;
		try {
			chgDate = getModifiedDate().getTime();
		} catch (Exception exe) {
			log.error("An error occured", exe);
		}
		if (chgDate != 0) {
			SourceValidity sv = new TimeStampValidity(chgDate);
			return sv;
		}
		return null;
	}

	/**
	 * Returns the last modified Date of this ViewComponent. <br>
	 * It will also check, if there are some contained DatabaseComponents or other dynamic content, which have to be
	 * checked.
	 */
	private Date getModifiedDate() {
		if (log.isDebugEnabled()) log.debug("start getModifiedDate");
		Date retDte = new Date(System.currentTimeMillis());
		try {
			retDte = webSpringBean.getModifiedDate4Cache(viewComponentId);
		} catch (Exception exe) {
			log.error("an unknown error occured", exe);
		}
		if (log.isDebugEnabled()) log.debug("end getModifiedDate with " + retDte);
		return retDte;
	}

	public void recycle() {
		if (log.isDebugEnabled()) log.debug("begin recycle");
		uniqueKey = null;
		viewComponentId = null;
		chgDate = 0;
		webSpringBean = null;
		if (log.isDebugEnabled()) log.debug("end recycle");
	}

}
