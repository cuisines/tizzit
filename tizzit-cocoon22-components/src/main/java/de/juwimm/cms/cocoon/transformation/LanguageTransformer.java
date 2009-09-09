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
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.TimeStampValidity;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.cms.vo.ViewDocumentValue;

/**
 * Transformer parses input for the tag "languageVersions" and fetches and appends language-name and language-url.<br/>
 * It needs three parameters from the sitemap:
 * <ul>
 * <li>viewComponentId</li>
 * <li>language</li>
 * <li>liveserver</li>
 * </ul>
 * <p><h5>Configuration:</h5>
 * <pre>
 * &lt;map:transformer name="language" src="de.juwimm.cms.cocoon.transformation.LanguageTransformer"/&gt;
 * </pre>
 * </p>
 * <p><h5>Usage:</h5>
 * Just put code like this in your pipeline:
 * <pre>
 * &lt;map:transform type="language"&gt;
 *	 &lt;map:parameter name="language" value="{../1}"/&gt;
 *	 &lt;map:parameter name="liveserver" value="{conquest-properties:liveserver} "/&gt;
 *	 &lt;map:parameter name="viewComponentId" value="{viewComponentId}"/&gt;
 * &lt;/map:transform&gt;
 * </pre>
 * </p>
 * <p><h5>Result:</h5>
 * In your content you get a result like this:
 * <pre>
 * &lt;languageVersions&gt;
 * 	  &lt;language&gt;
 * 		 &lt;langName&gt;englisch&lt;/langName&gt;
 * 		 &lt;langUrl&gt;url/to/english/startpage/of/current/unit&lt;/langUrl&gt;
 * 	  &lt;/language&gt;
 * &lt;/languageVersions&gt;
 * </pre>
 * </p>
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @since ConQuest 2.0
 */
public class LanguageTransformer extends AbstractTransformer implements CacheableProcessingComponent {
	private static Logger log = Logger.getLogger(LanguageTransformer.class);
	private Integer viewComponentId = null;
	private WebServiceSpring webSpringBean = null;
	private Serializable uniqueKey;
	private long chgDate = 0;
	private String currentLanguage = "";
	private boolean iAmTheLiveserver = false;

	/* (non-Javadoc)
	 * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(
	 * org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
	 */
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {

		Request request = ObjectModelHelper.getRequest(objectModel);
		this.uniqueKey = request.getRequestURI();
		try {
			webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
		} catch (Exception exf) {
			log.error("Could not load webservicespringbean!", exf);
		}
		try {
			this.viewComponentId = new Integer(par.getParameter("viewComponentId"));
			this.currentLanguage = par.getParameter("language");
			iAmTheLiveserver = new Boolean(par.getParameter("liveserver")).booleanValue();
		} catch (ParameterException e) {
			log.error("an unknown error occured", e);
		}
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
		this.chgDate = 0;
		try {
			this.chgDate = getModifiedDate().getTime();
		} catch (Exception exe) {
			log.error("An error occured", exe);
		}
		if (this.chgDate != 0) {
			SourceValidity sv = new TimeStampValidity(this.chgDate);
			return sv;
		}
		return null;
	}

	/**
	 * Returns the last modified Date of this ViewComponent. <br>
	 * It will also check, if there are some contained DatabaseComponents or other dynamic content, who has to been
	 * checked.
	 */
	private Date getModifiedDate() {
		if (log.isDebugEnabled()) log.debug("start getModifiedDate");
		Date retDte = new Date(System.currentTimeMillis());
		try {
			retDte = this.webSpringBean.getModifiedDate4Cache(this.viewComponentId);
		} catch (Exception exe) {
			log.error("an unknown error occured", exe);
		}
		if (log.isDebugEnabled()) log.debug("end getModifiedDate with " + retDte);
		return retDte;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		AttributesImpl newAttr = new AttributesImpl(attrs);
		contentHandler.startElement(uri, localName, qName, newAttr);
		if (localName.equals("languageVersions")) {
			UnitValue currentUnit = null;
			ViewDocumentValue[] vdd = null;
			try {
				currentUnit = webSpringBean.getUnit4ViewComponent(viewComponentId);
				vdd = webSpringBean.getViewDocuments4Site(webSpringBean.getSite4VCId(viewComponentId).getSiteId());
				for (int i = 0; i < vdd.length; i++) {
					String lang = vdd[i].getLanguage();
					if (!lang.equals(this.currentLanguage)) { // give me only those vc's from other languages
						try {
							Integer unitId = currentUnit.getUnitId();
							Integer viewDocumentId = vdd[i].getViewDocumentId();
							ViewComponentValue viewComponentValue = webSpringBean.getViewComponent4Unit(unitId, viewDocumentId);
							if (viewComponentValue != null) {
								// Maybe for this language there is no page for this unit
								if (webSpringBean.isVisibleForLanguageVersion(viewComponentValue, iAmTheLiveserver)) {
									String langpath = webSpringBean.getPath4ViewComponent(viewComponentValue.getViewComponentId());
									contentHandler.startElement(uri, "language", "language", new AttributesImpl());
									contentHandler.startElement(uri, "langName", "langName", new AttributesImpl());
									contentHandler.characters(lang.toCharArray(), 0, lang.length());
									contentHandler.endElement(uri, "langName", "langName");
									contentHandler.startElement(uri, "langUrl", "langUrl", new AttributesImpl());
									contentHandler.characters(langpath.toCharArray(), 0, langpath.length());
									contentHandler.endElement(uri, "langUrl", "langUrl");
									contentHandler.endElement(uri, "language", "language");
								}
							}
						} catch (Exception exe) { //if the vcl wont be found. thats ok :)
							if (log.isDebugEnabled()) {
								log.debug("An error occured", exe);
							}
						}
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

}
