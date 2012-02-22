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

/**
 * 
 *         This transformer is fetching all document slim values from the
 *         database for a specific unit or an entire site. The tag that will
 *         trigger the transformation is "getAllDocuments"
 * 
 * @author nitun
 * 
 */
public class GetDocumentsTransformer extends AbstractTransformer implements
		Recyclable {
	private static Logger log = Logger.getLogger(GetDocumentsTransformer.class);
	private WebServiceSpring webSpringBean = null;

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
	}

	
	/*
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attrs) throws SAXException {
		if (localName.equals("getAllDocuments")) {
//			super.startElement(uri, localName, qName, attrs);
			// get site value and unit value based on attributes
			Document doc = XercesHelper.getNewDocument();
			if (log.isDebugEnabled())
				log.debug("getAllDocuments entered.");
			String documentsXml = "";
			Integer unitId=null;
			Integer siteId=null;
			SiteValue siteValue=null;
			try {
				unitId = new Integer(attrs.getValue("unitId"));
				siteValue = webSpringBean.getSite4Unit(unitId);
			} catch (Exception exe) {
				if (log.isDebugEnabled())
					log.debug("value for 'unitId' not found or invalid");
			}
			if (siteValue == null) {
				try {
					siteId = new Integer(attrs.getValue("siteId"));
					if (siteId != null) {
						siteValue = this.webSpringBean.getSiteValue(siteId);
					}
				} catch (Exception exe) {
					if (log.isDebugEnabled())
						log.debug("value for 'siteId' not found or invalid");
				}
			}
			try {
				if (unitId != null) {
					documentsXml = "<documents>"+webSpringBean.getDocumentsForUnitXml(unitId)+"</documents>";
				} else {
					documentsXml = "<documents>"+webSpringBean.getDocumentsForSiteXml(siteId)+"</documents>";
				}
				Document smdoc = XercesHelper.string2Dom(documentsXml);
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
