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
package de.juwimm.cms.cocoon.transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;

import org.apache.avalon.excalibur.pool.Recyclable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.tizzit.util.xml.SAXHelper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.cms.vo.ViewDocumentValue;

/**
 * Transformer parses input for the tag "unit" and fetches and appends unitpath, unitname and image-information</br>
 * 
 * <p><h5>Configuration:</h5>
 * <pre>
 * &lt;map:transformer name="unit" src="de.juwimm.cms.cocoon.transformation.UnitTransformer"/&gt;
 * </pre>
 * </p>
 * <p><h5>Usage:</h5>
 * Just put code like this in your pipeline:
 * <pre>
 * &lt;map:transform type="unit"/&gt;
 * </pre>
 * </p>
 * <p>The input must be something like this:</br>
 * <pre>&lt;unit id="58" siteId=6"/&gt;</pre></br>
 * </p>
 * <p><h5>Result:</h5>
 * The output will look like this:</br>
 * <pre>
 * &lt;unit id="58" siteId="6" logoId="614" imageId="727"&gt;
 *    &lt;unitname&gt;&lt;![CDATA[Confidential]]&gt;&lt;/unitname&gt;
 *    &lt;lastModified&gt;&lt;![CDATA[17.01.2008 19:31:50]]&gt;&lt;/lastModified&gt;
 *    &lt;unitImage&gt;
 *       &lt;image height="1126" id="727" mimeType="image/jpeg" width="902"&gt;
 *          &lt;fileName&gt;&lt;![CDATA[virgil.jpg]]&gt;&lt;/fileName&gt;
 *          &lt;altText&gt;&lt;![CDATA[alt. Text]]&gt;&lt;/altText&gt;
 *          &lt;timeStamp&gt;&lt;![CDATA[21.06.2007 18:12:32]]&gt;&lt;/timeStamp&gt;
 *       &lt;/image&gt;
 *    &lt;/unitImage&gt;
 *    &lt;unitLogo&gt;
 *       &lt;image height="412" id="614" mimeType="image/jpeg" width="914"&gt;
 *          &lt;fileName&gt;&lt;![CDATA[tuev.jpeg]]&gt;&lt;/fileName&gt;
 *          &lt;altText/&gt;
 *          &lt;timeStamp&gt;&lt;![CDATA[14.06.2007 11:19:27]]&gt;&lt;/timeStamp&gt;
 *       &lt;/image&gt;
 *    &lt;/unitLogo&gt;
 *    &lt;languagePath url="Confidential" language="deutsch" viewType="browser" viewDocumentId="18"/&gt;
 * &lt;/unit&gt;
 * </pre>
 * </p>
 * <p>
 * Additional attributes in the &quot;unit&quot;-tag are kept, like the attribute &quot;siteId&quot; in the example above.
 * </p>
 * <p>
 * A special feature of this transformer is, that you can parameterize for every &quot;unit&quot;-tag, how much information you like to get.<br/>
 * You can skip the transformation of a unit by adding the attribute &quot;disableParsing&quot; with the value &quot;true&quot;.<br/>
 * If you set the attribute &quot;disableUrlResolve&quot; with the value &quot;true&quot; the most expansive search for information is skipped.<br/>
 * If you set the attribute &quot;disableUrlResolve&quot; with the value &quot;false&quot; you get the element &quot;languagePath&quot; with the following information:<br/>
 * <ul>
 * <li>url - the path to this unit</li>
 * <li>language - the language for this unit</li>
 * <li>viewType - the viewType for this unit</li>
 * <li>viewDocumentId - the id for this viewDocument</li>
 * </ul>
 * </p>
 * <p>
 * If you like to get all available languages for a page or unit, please look at
 * @see de.juwimm.cms.cocoon.transformation.LanguageTransformer
 * </p>
 * 
 * @author <a href="carsten.schalm@juwimm.com">Carsten Schalm</a>
 * Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @since ConQuest 2.0
 */
public class UnitTransformer extends AbstractTransformer implements Recyclable {
	private static Logger log = Logger.getLogger(UnitTransformer.class);
	private WebServiceSpring webSpringBean = null;
	private HashMap<Integer, ArrayList> vdId4SitesCache = new HashMap<Integer, ArrayList>();

	/* (non-Javadoc)
	 * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(
	 * org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
	 */
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
		try {
			this.webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
		} catch (Exception exf) {
			log.error("Could not load webservicespringbean!", exf);
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		if (localName.equals("unit")) {
			boolean disableParsing = Boolean.valueOf(attrs.getValue("disableParsing")).booleanValue();
			if (!disableParsing) {
				AttributesImpl newAttr = new AttributesImpl(attrs);
				String unitName = null;
				Integer logoId = null;
				Integer imageId = null;
				Integer unitId = new Integer(attrs.getValue("id"));
				if (unitId == null) {
					log.error("UnitId not found!");
					super.contentHandler.startElement(uri, localName, qName, attrs);
				} else {
					try {
						UnitValue unitValue = this.webSpringBean.getUnit(unitId);
						if (unitValue == null) {
							log.error("Unit with id " + unitId.toString() + " not found!");
							super.contentHandler.startElement(uri, localName, qName, attrs);
						} else {
							try {
								logoId = unitValue.getLogoId();
								SAXHelper.setSAXAttr(newAttr, "logoId", logoId.toString());
							} catch (Exception exe) {
								if (log.isDebugEnabled()) log.debug("Error getting unit-logo: " + exe.getMessage());
							}
							try {
								imageId = unitValue.getImageId();
								SAXHelper.setSAXAttr(newAttr, "imageId", imageId.toString());
							} catch (Exception exe) {
								if (log.isDebugEnabled()) log.debug("Error getting unit-image: " + exe.getMessage());
							}
							unitName = unitValue.getName();
							if (log.isDebugEnabled()) log.debug("current Unit: " + unitName);
							super.contentHandler.startElement(uri, localName, qName, newAttr);
							this.convertUnitInfoToSax(unitId);
							boolean disableUrlResolve = Boolean.valueOf(attrs.getValue("disableUrlResolve")).booleanValue();
							if (!disableUrlResolve) {
								Integer siteId = null;
								try {
									siteId = this.webSpringBean.getSite4Unit(unitId).getSiteId();
								} catch (Exception e) {
									if (log.isDebugEnabled()) log.debug("Error getting site for unit " + unitId.toString() + ": " + e.getMessage());
								}
								if (siteId == null) {
									log.error("Site not found!");
								} else {
									Iterator vdIterator = getVdId4Site(siteId).iterator();
									while (vdIterator.hasNext()) {
										ViewDocumentValue viewDocumentValue = (ViewDocumentValue) vdIterator.next();
										if (log.isDebugEnabled()) log.debug("current viewDocument: " + viewDocumentValue.toString());
										AttributesImpl vdAttr = new AttributesImpl();
										String unitPath = null;
										String language = null;
										String viewType = null;
										try {
											unitPath = this.webSpringBean.getPath4Unit(unitId, viewDocumentValue.getViewDocumentId());
										} catch (Exception exe) {
											if (log.isDebugEnabled()) log.debug("Error getting unit-path: " + exe.getMessage());
										}
										// id unitPath is null this unit has no page associated in this language-version
										if (unitPath != null) {
											try {
												language = viewDocumentValue.getLanguage();
											} catch (Exception exe) {
												if (log.isDebugEnabled()) log.debug("Error getting unit-language: " + exe.getMessage());
											}
											try {
												viewType = viewDocumentValue.getViewType();
											} catch (Exception exe) {
												if (log.isDebugEnabled()) log.debug("Error getting unit-viewType: " + exe.getMessage());
											}
											SAXHelper.setSAXAttr(vdAttr, "url", unitPath);
											if (language != null) SAXHelper.setSAXAttr(vdAttr, "language", language);
											if (viewType != null) SAXHelper.setSAXAttr(vdAttr, "viewType", viewType);
											SAXHelper.setSAXAttr(vdAttr, "viewDocumentId", viewDocumentValue.getViewDocumentId().toString());
											super.contentHandler.startElement(uri, "languagePath", "languagePath", vdAttr);
											super.contentHandler.endElement(uri, "languagePath", "languagePath");
										}
									}
								}
							}
						}
					} catch (Exception exe) {
						if (log.isDebugEnabled()) log.debug("Error: " + exe.getMessage());
					}
				}
			} else {
				super.contentHandler.startElement(uri, localName, qName, attrs);
			}
		} else {
			super.contentHandler.startElement(uri, localName, qName, attrs);
		}
	}

	@Override
	public void recycle() {
		if (log.isDebugEnabled()) log.debug("begin recycle");
		this.vdId4SitesCache = null;
	}

	private ArrayList getVdId4Site(Integer siteId) {
		if (this.vdId4SitesCache == null) this.vdId4SitesCache = new HashMap<Integer, ArrayList>();
		if (this.vdId4SitesCache.containsKey(siteId)) { return this.vdId4SitesCache.get(siteId); }
		ArrayList<ViewDocumentValue> vdList = new ArrayList<ViewDocumentValue>();
		ViewDocumentValue[] viewDocuments = null;
		try {
			viewDocuments = this.webSpringBean.getViewDocuments4Site(siteId);
		} catch (Exception e) {
			log.error("Error getting viewDocuments for site " + siteId.toString() + ": " + e.getMessage());
		}
		if (viewDocuments != null) {
			for (int i = 0; i < viewDocuments.length; i++) {
				vdList.add(viewDocuments[i]);
			}
		}
		this.vdId4SitesCache.put(siteId, vdList);
		return (vdList);
	}

	private void convertUnitInfoToSax(Integer unitId) {
		try {
			SAXResult result = new SAXResult(super.contentHandler);
			String unitInfo = this.webSpringBean.getUnitInfoXml(unitId);
			Node ndeUnitInfo = XercesHelper.string2Dom(unitInfo).getDocumentElement();
			NodeList children = ndeUnitInfo.getChildNodes();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer xform = transformerFactory.newTransformer();
			DOMSource source = new DOMSource();
			for (int i = 0; i < children.getLength(); i++) {
				source.setNode(children.item(i));
				xform.transform(source, result);
			}
		} catch (Exception e) {
			log.warn("Error: " + e.getMessage(), e);
		}
	}

}
