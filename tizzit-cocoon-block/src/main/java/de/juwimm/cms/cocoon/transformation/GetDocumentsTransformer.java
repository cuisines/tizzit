/**
 * @author nitun
 * @lastChange 4:24:36 PM
 */
package de.juwimm.cms.cocoon.transformation;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.avalon.excalibur.pool.Recyclable;
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
 * This transformer is fetching all document slim values from the database for a
 * specific unit or an entire site. The tag that will trigger the transformation
 * is "getAllDocuments"
 * 
 * @author nitun
 * 
 */
public class GetDocumentsTransformer extends AbstractTransformer implements
		CacheableProcessingComponent, Recyclable {
	private static Logger log = Logger.getLogger(GetDocumentsTransformer.class);
	private WebServiceSpring webSpringBean = null;
	private Integer viewComponentId = null;
	private ViewComponentValue viewComponentValue = null;
	private Serializable uniqueKey;
	private long chgDate = 0;
	private Request request = null;

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
			request = ObjectModelHelper.getRequest(objectModel);
			uniqueKey = viewComponentId + src + "?" + request.getQueryString();
			if (log.isDebugEnabled()) {
				log.debug("UniqueKey: " + uniqueKey);
			}
			try {
				viewComponentValue = webSpringBean
						.getViewComponent4Id(viewComponentId);
			} catch (Exception e) {
				if (log.isDebugEnabled()) {
					log.debug(e.getMessage());
				}
			}

		} catch (Exception exe) {
			viewComponentId = null;
		}

	}

	/*
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attrs) throws SAXException {
		if (localName.equals("getAllDocuments")) {
			super.startElement(uri, localName, qName, attrs);
			// get site value and unit value based on attributes
			Document doc = XercesHelper.getNewDocument();
			if (log.isDebugEnabled())
				log.debug("getAllDocuments entered.");
			StringBuilder documentsXml = new StringBuilder();

			Integer viewComponentId = null;
			String docsFor = null;
			try {
				viewComponentId = new Integer(attrs.getValue("viewComponentId"));
			} catch (Exception exe) {
				if (log.isDebugEnabled())
					log.debug("value for 'viewComponentId' not found or invalid");
			}
			try {
				docsFor = attrs.getValue("showDocuments");
			} catch (Exception exe) {
				if (log.isDebugEnabled())
					log.debug("value for 'showDocuments' not found or invalid");
			}
			documentsXml.append("<documents>");
			try {
				if (docsFor.equalsIgnoreCase("unit")) {
					ViewDocumentValue viewDocumentValue = webSpringBean
							.getViewDocument4ViewComponentId(viewComponentId);
					UnitValue unitValue = webSpringBean
							.getUnit4ViewComponent(viewComponentId);
					ViewComponentValue rootViewComponent = webSpringBean
							.getViewComponent4Unit(unitValue.getUnitId(),
									viewDocumentValue.getViewDocumentId());
					documentsXml.append(webSpringBean
							.getDocumentsForUnitXml(rootViewComponent
									.getViewComponentId()));
				} else if (docsFor.equalsIgnoreCase("site")) {
					SiteValue siteValue = webSpringBean
							.getSite4VCId(viewComponentId);
					documentsXml.append(webSpringBean
							.getDocumentsForSiteXml(siteValue.getSiteId()));
				} else if (docsFor.equalsIgnoreCase("viewComponent")) {
					documentsXml.append(webSpringBean
							.getDocumentsForViewComponentXml(viewComponentId));
				}

				documentsXml.append("</documents>");

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

	public Serializable getKey() {
		return uniqueKey;
	}

	/**
	 * The current validity date returned for this transformer is based only on
	 * the current system time and it always fetches the documents from
	 * database. This is implemented so that the other components of the
	 * pipeline can be cached.
	 * 
	 * @return
	 */
	public SourceValidity getValidity() {
		SourceValidity sv = new TimeStampValidity(System.currentTimeMillis());
		return sv;
	}

}
