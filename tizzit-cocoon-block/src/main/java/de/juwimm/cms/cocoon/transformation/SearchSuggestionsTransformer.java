/**
 * @author nitun
 * @lastChange 4:24:36 PM
 */
package de.juwimm.cms.cocoon.transformation;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.avalon.excalibur.pool.Recyclable;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.tizzit.util.xml.SAXHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.cms.vo.ViewDocumentValue;

/**
 * 
 * This transformer is fetching search suggestions for a specific unit or an
 * entire site. The tag that will trigger the transformation is
 * "searchSugestions"
 * 
 * @author nitun
 * 
 */
public class SearchSuggestionsTransformer extends AbstractTransformer implements
		Recyclable {
	private static Logger log = Logger
			.getLogger(SearchSuggestionsTransformer.class);
	private WebServiceSpring webSpringBean = null;
	private Integer viewComponentId = null;
	private ViewComponentValue viewComponentValue = null;
	private UnitValue unitValue = null;
	private SiteValue siteValue = null;
	private String scope = null;
	private String searchQuery = null;
	private Map<String, String> safeguardMap = null;
	private Request request=null;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon
	 * .environment.SourceResolver, java.util.Map, java.lang.String,
	 * org.apache.avalon.framework.parameters.Parameters)
	 */
	public void setup(SourceResolver resolver, Map objectModel, String src,
			Parameters par) throws ProcessingException, SAXException,
			IOException {

		if (log.isDebugEnabled())
			log.debug("begin setup with src: " + src);
		try {
			webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(
					objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
		} catch (Exception exf) {
			log.error("could not load webServiceSpringBean ", exf);
		}
		try {
			viewComponentId = new Integer(par.getParameter("viewComponentId"));
			scope = par.getParameter("scope");
			searchQuery = par.getParameter("searchQuery");

		} catch (Exception exe) {
			viewComponentId = null;
		}
		request = ObjectModelHelper.getRequest(objectModel);
		HttpSession session = request.getSession(true);
		try {
			this.safeguardMap = (Map<String, String>) session.getAttribute("safeGuardService");
			if (this.safeguardMap == null) {
				if (log.isDebugEnabled()) log.debug("no SafeguardMap");
				this.safeguardMap = new HashMap<String, String>();
				if (log.isDebugEnabled()) log.debug("created new SafeguardMap");
				session.setAttribute("safeGuardService", this.safeguardMap);
				if (log.isDebugEnabled()) log.debug("put SafeguardMap into Session");
			} else {
				if (log.isDebugEnabled()) log.debug("found SafeguardMap");
			}
		} catch (Exception cookieex) {
			log.warn("SafeGuard-Error: " + cookieex.getMessage());
		}


	}

	/*
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attrs) throws SAXException {
		if (localName.equals("searchSuggestions")) {
			super.startElement(uri, localName, qName, attrs);
			// get site value and unit value based on attributes
			Document doc = XercesHelper.getNewDocument();
			if (log.isDebugEnabled())
				log.debug("getAllDocuments entered.");
			StringBuilder documentsXml = new StringBuilder();

			documentsXml.append("<suggestions>");
			try {
				documentsXml.append(webSpringBean
						.getSearchSuggestionsXml(viewComponentId,scope,searchQuery,safeguardMap));

				documentsXml.append("</suggestions>");

				Document smdoc = XercesHelper.string2Dom(documentsXml
						.toString());
				Node page = doc.importNode(smdoc.getFirstChild(), true);
				SAXHelper.string2sax(XercesHelper.node2string(page), this);
			} catch (Exception e) {
				log.error("An error occured while creating the documents xml",
						e);
			}

		} else {
			super.startElement(uri, localName, qName, attrs);

		}
	}

}
