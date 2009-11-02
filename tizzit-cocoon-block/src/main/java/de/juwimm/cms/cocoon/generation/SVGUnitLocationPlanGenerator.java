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
package de.juwimm.cms.cocoon.generation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.excalibur.pool.Poolable;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.ResourceNotFoundException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.TimeStampValidity;
import org.apache.log4j.Logger;
import org.tizzit.cocoon.generic.sax.SVGMarkContentHandler;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;

/**
 *
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @since ConQuest 2.0
 */
public class SVGUnitLocationPlanGenerator extends AbstractGenerator implements CacheableProcessingComponent, Poolable, Composable {
	private static final String PAR_POLYGON_ID = "polygonId";
	private static final String NODE_POLYGONS = "polygons";
	private static final String NODE_POLYGON = "polygon";
	private static final String NODE_SVG_DOCUMENT = "svgDocument";
	private static final String ATTR_ID = "id";
	private static final String ATTR_UNIQUE_NAME = "uniqueName";
	private static Logger log = Logger.getLogger(SVGUnitLocationPlanGenerator.class);
	private Serializable uniqueKey;
	private Integer svgDocumentId = null;
	private Integer viewComponentId = null;
	private StringTokenizer polygonIdTokens = null;
	private WebServiceSpring webSpringBean = null;
	private boolean iAmTheLiveserver = false;
	private long chgDate = 0;

	public void compose(ComponentManager componentManager) {
		if (log.isDebugEnabled()) log.debug("begin compose");
		try {
			this.webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
		} catch (Exception exf) {
			log.fatal("COULD NOT LOAD WEB SPRING BEAN " + exf.getMessage());
		}
		if (log.isDebugEnabled()) log.debug("end compose");
	}

	/**
	 * setting up this generator
	 */
	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters parameters) {
		if (log.isDebugEnabled()) log.debug("begin setup with src: " + src);
		try {
			super.setup(resolver, objectModel, src, parameters);

			String polygonIds = parameters.getParameter(SVGUnitLocationPlanGenerator.PAR_POLYGON_ID);
			this.polygonIdTokens = new StringTokenizer(polygonIds, "-");
			this.viewComponentId = new Integer(parameters.getParameter("viewComponentId"));
			Request request = ObjectModelHelper.getRequest(objectModel);
			this.uniqueKey = request.getRequestURI();
		} catch (Exception exe) {
			new ProcessingException(exe.getMessage());
		}
		try {
			this.iAmTheLiveserver = new Boolean(parameters.getParameter("liveserver")).booleanValue();
		} catch (Exception exe) {
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.generation.Generator#generate()
	 */
	public void generate() throws IOException, SAXException, ProcessingException {
		if (log.isDebugEnabled()) log.debug("begin generate");
		Document contentDoc = null;

		if (log.isDebugEnabled()) log.debug("GETTING XML FROM BEAN");
		try {
			String content = webSpringBean.getContent(viewComponentId, iAmTheLiveserver);
			contentDoc = XercesHelper.string2Dom(content);
			Element root = (Element) XercesHelper.findNode(contentDoc, "//" + SVGUnitLocationPlanGenerator.NODE_SVG_DOCUMENT);
			this.svgDocumentId = new Integer(root.getAttribute(SVGUnitLocationPlanGenerator.ATTR_ID));
		} catch (Exception exe) {
		}
		if (contentDoc == null) { throw new ResourceNotFoundException("Could not find resource with "); }
		if (log.isDebugEnabled()) log.debug("start streaming to sax");
		HashSet poly = getPolygonsForMarking(contentDoc);
		ContentHandler svgContentModifier = new SVGMarkContentHandler(this.contentHandler, poly);
		try {
			byte[] svgDoc = this.webSpringBean.getDocument(this.svgDocumentId);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(svgDoc);
			SAXResult result = new SAXResult(svgContentModifier);
			javax.xml.transform.Source source = new StreamSource(inputStream);
			Transformer transform;
			try {
				transform = TransformerFactory.newInstance().newTransformer();
				try {
					transform.transform(source, result);
				} catch (TransformerException e1) {
					if (log.isDebugEnabled()) log.debug(e1.getMessage());
				}
			} catch (TransformerConfigurationException e) {
				if (log.isDebugEnabled()) log.debug(e.getMessage());
			} catch (TransformerFactoryConfigurationError e) {
				if (log.isDebugEnabled()) log.debug(e.getMessage());
			}
		} catch (Exception exe) {
			log.error("an unknown error occured", exe);
		}
		if (log.isDebugEnabled()) log.debug("end generate");
	}

	@Override
	public void recycle() {
		if (log.isDebugEnabled()) log.debug("begin recycle");
		super.recycle();
		this.uniqueKey = null;
		this.svgDocumentId = null;
		this.polygonIdTokens = null;
		this.viewComponentId = null;
		this.chgDate = 0;
		if (log.isDebugEnabled()) log.debug("end recycle");
	}

	private HashSet getPolygonsForMarking(Document contentDoc) {
		HashSet<String> polygons = new HashSet<String>();
		while (this.polygonIdTokens.hasMoreTokens()) {
			try {
				Integer groupId = new Integer(this.polygonIdTokens.nextToken());
				// search with XPath for polygons to mark
				Element currentPolygon = (Element) XercesHelper.findNode(contentDoc, "//" + SVGUnitLocationPlanGenerator.NODE_POLYGONS + "/" + SVGUnitLocationPlanGenerator.NODE_POLYGON + "[@" + SVGUnitLocationPlanGenerator.ATTR_ID + "='" + groupId + "']");
				if (currentPolygon != null) {
					// polygon found
					// get polygonID
					String polygonId = currentPolygon.getAttribute(SVGUnitLocationPlanGenerator.ATTR_UNIQUE_NAME);
					polygons.add(polygonId);
				}
			} catch (Exception e) {
				if (log.isDebugEnabled()) log.debug("error on getting polygons for marking " + e.getMessage());
			}
		}

		return (polygons);
	}

	/**
	 * Generate the unique key. This key must be unique inside the space of this component.
	 *
	 * @return The generated key hashes the src
	 */
	public Serializable getKey() {
		return this.uniqueKey;
	}

	/**
	 * Generate the validity object.
	 *
	 * @return The generated validity object or <code>null</code> if the component is currently not cacheable.
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

	/*
	 * Returns the last modified Date of this ViewComponent. <br>
	 * It will also check, if there are some contained DatabaseComponents or other dynamic content, who has to been
	 * checked.
	 */
	private Date getModifiedDate() {
		if (log.isDebugEnabled()) log.debug("start getModifiedDate");
		Date retDte = new Date(System.currentTimeMillis());
		try {
			retDte = this.webSpringBean.getModifiedDate4Cache(viewComponentId);
		} catch (Exception exe) {
			log.error("an unknown error occured", exe);
		}
		if (log.isDebugEnabled()) log.debug("end getModifiedDate with " + retDte);
		return retDte;
	}

}
