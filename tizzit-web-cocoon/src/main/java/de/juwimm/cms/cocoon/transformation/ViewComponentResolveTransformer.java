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
 * Created on 29.11.2004
 */
package de.juwimm.cms.cocoon.transformation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;

/**
 * Transformer parses input for XML like this:<br/>
 * <pre>
 * &lt;page viewComponentId="639"&gt;
 *	 &lt;pageName&gt;&lt;/pageName&gt;
 *	 &lt;pageUrl&gt;&lt;/pageUrl&gt;
 * &lt;/page&gt;
 * </pre>
 * <p><h5>Configuration:</h5>
 * <pre>
 * &lt;map:transformer name="vcresolve" src="de.juwimm.cms.cocoon.transformation.ViewComponentResolveTransformer"/&gt;
 * </pre>
 * </p>
 * <p><h5>Usage:</h5>
 * Just put code like this in your pipeline:
 * <pre>
 * &lt;map:transform type="vcresolve" /&gt;
 * </pre>
 * </p>
 * <p><h5>Result:</h5>
 * In your content you get a result like this:
 * <pre>
 * &lt;page viewComponentId="639"&gt;
 *	 &lt;pageName&gt;bä&lt;n&gt;ane&lt;/pageName&gt;
 *	 &lt;pageUrl&gt;/deutsch/banane&lt;/pageUrl&gt;
 * &lt;/page&gt;
 * </pre>
 * </p>
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @since ConQuest 2.4.13
 */
public class ViewComponentResolveTransformer extends AbstractTransformer {
	private static Logger log = Logger.getLogger(ViewComponentResolveTransformer.class);
	private Map<Integer, String> path4ViewComponentCacheMap = new HashMap<Integer, String>();
	private Map<Integer, String> name4ViewComponentCacheMap = new HashMap<Integer, String>();
	private WebServiceSpring webSpringBean = null;
	private Integer viewComponentId = null;

	/*
	 * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(
	 * org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
	 */
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
		try {
			this.webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
		} catch (Exception e) {
			log.error("Could not load WebServiceSpring: " + e.getMessage(), e);
		}
	}

	/*
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		super.startElement(uri, localName, qName, attrs);
		if (localName.equals("page")) {
			try {
				this.viewComponentId = Integer.valueOf(attrs.getValue("viewComponentId"));
			} catch (Exception e) {
				log.warn("Error parsing viewComponentId! " + e.getMessage());
			}
		} else if (localName.equals("pageUrl")) {
			String url = this.getPath4ViewComponent();
			if (url != null) super.characters(url.toCharArray(), 0, url.length());
		} else if (localName.equals("pageName")) {
			String displayName = this.getName4ViewComponent();
			if (displayName != null) super.characters(displayName.toCharArray(), 0, displayName.length());
		}
	}

	private String getPath4ViewComponent() {
		String result = this.path4ViewComponentCacheMap.get(this.viewComponentId);
		if (result == null) {
			String requestPath = "";
			String requestLang = "";
			try {
				requestLang = webSpringBean.getViewDocument4ViewComponentId(this.viewComponentId).getLanguage();
			} catch (Exception e) {
			}
			try {
				requestPath = webSpringBean.getPath4ViewComponent(this.viewComponentId);
			} catch (Exception e) {
			}
			result = "/" + requestLang + "/" + requestPath;
			this.path4ViewComponentCacheMap.put(this.viewComponentId, result);
		}

		return result;
	}

	private String getName4ViewComponent() {
		String displayName = this.name4ViewComponentCacheMap.get(this.viewComponentId);
		if (displayName == null) {
			try {
				displayName = webSpringBean.getViewComponent4Id(this.viewComponentId).getDisplayLinkName();
			} catch (Exception e) {
			}
			this.name4ViewComponentCacheMap.put(this.viewComponentId, displayName);
		}

		return displayName;
	}

}
