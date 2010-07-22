package org.tizzit.plugins.server.transformer;

import java.util.Date;
import java.util.Iterator;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tizzit.util.XercesHelper;
import org.tizzit.util.xml.SAXHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.sun.xml.internal.stream.events.AttributeImpl;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.components.model.AddressHbm;
import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.components.vo.DepartmentValue;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.components.vo.TalktimeValue;
import de.juwimm.cms.plugins.Constants;
import de.juwimm.cms.plugins.server.Request;
import de.juwimm.cms.plugins.server.Response;
import de.juwimm.cms.plugins.server.TizzitPlugin;
import de.juwimm.cms.vo.UnitValue;

/**
 * <p>
 * <b>Namespace: <code>http://plugins.tizzit.org/AggregationTransformerPlugin</code></b>
 * </p>
 *
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id: AggregationTransformerPlugin.java 759 2010-05-05 13:34:28Z rene.hertzfeldt $
 */
public class AggregationTransformerPlugin implements ManagedTizzitPlugin {
	private static final Log log = LogFactory.getLog(AggregationTransformerPlugin.class);

	private ContentHandler parent;
	
	private Integer viewComponentId = null;

	private WebServiceSpring webSpringBean = null;

	private ContentHandler manager;
	private String nameSpace;
	
	private Stack<Object> lastOpenedObject = new Stack<Object>();
	private boolean hasValue = false;
	
	public void setup(ContentHandler pluginManager, String nameSpace) {
		this.manager = pluginManager;
		this.nameSpace = nameSpace;
	}
	
	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.ConquestPlugin#configurePlugin(de.juwimm.cms.plugins.server.Request, de.juwimm.cms.plugins.server.Response, org.xml.sax.ContentHandler, java.lang.Integer)
	 */
	public void configurePlugin(Request req, Response resp, ContentHandler ch, Integer uniquePageId) {
		if (log.isDebugEnabled()) log.debug("configurePlugin() -> begin");
		this.parent = ch;
		//webSpringBean = (WebServiceSpring) PluginSpringHelper.getBean(objectModel, PluginSpringHelper.WEB_SERVICE_SPRING);
		viewComponentId = uniquePageId;
		if (log.isDebugEnabled()) log.debug("configurePlugin() -> end");
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.ConquestPlugin#getLastModifiedDate()
	 */
	public Date getLastModifiedDate() {
		return new Date();
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.ConquestPlugin#isCacheable()
	 */
	public boolean isCacheable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.ConquestPlugin#processContent()
	 */
	public void processContent() {
		if (log.isDebugEnabled()) log.debug("processContent() -> begin");

		if (log.isDebugEnabled()) log.debug("processContent() -> end");
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		if (log.isDebugEnabled()) log.debug("startElement: " + localName + " in nameSpace: " + uri + " found " + attrs.getLength() + " attributes");
		AttributesImpl newAttrs = null;
			if(localName.compareTo("include") == 0){
				try{
					Object o = null;
					try{
						if(attrs.getValue("type").compareTo("unit") == 0){
							o = webSpringBean.getUnit(Integer.decode(attrs.getValue("id")));
						}
						else if(attrs.getValue("type").compareTo("person") == 0){
							o = webSpringBean.getPerson(Long.decode(attrs.getValue("id")));
							try{
								newAttrs = new AttributesImpl(attrs);
								SAXHelper.setSAXAttr(newAttrs, "sortOrder", ""+((PersonValue)o).getPosition());
							} catch (Exception e){
								log.warn("Error getting sortOrder for person ", e);
							}
						}
						else if(attrs.getValue("type").compareTo("talktime") == 0){
							o = webSpringBean.getTalktime(Long.decode(attrs.getValue("id")));
						}
						else if(attrs.getValue("type").compareTo("department") == 0){
							o = webSpringBean.getDepartment(Long.decode(attrs.getValue("id")));
						}
						else if(attrs.getValue("type").compareTo("address") == 0){
							o = webSpringBean.getAddress(Long.decode(attrs.getValue("id")));
						}
					} catch(Exception e){
						if(log.isInfoEnabled()) log.info("could not load: " + attrs.getValue("type") + " with id: " + attrs.getValue("id"));
					}
					if(o != null){
						lastOpenedObject.push(o);
					}	
				} catch(Exception e){
				
				}
		}
	}
	

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (log.isDebugEnabled()) log.debug("characters: " + length + " long");
		if(!(new String(ch, start, length).trim().isEmpty())){
			hasValue = true;
		}
		parent.characters(ch, start, length);

	}
	

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (log.isDebugEnabled()) log.debug("endElement: " + localName + " in nameSpace: " + uri);
			if(!lastOpenedObject.isEmpty()){
				if(!hasValue){
					Object o = lastOpenedObject.pop();
					if(o instanceof AddressValue){
						fillAddress((AddressValue)o, localName);
					} else if(o instanceof PersonValue){
						fillPerson((PersonValue)o, localName);
					} else if(o instanceof UnitValue){
						fillUnit((UnitValue)o, localName);
					} else if(o instanceof TalktimeValue){
						fillTalktime((TalktimeValue)o, localName);
					} 
				}
				else{
					lastOpenedObject.pop();
				}
			}
		parent.endElement(uri, localName, qName);
	}

	private void fillPerson(PersonValue value, String name) {
		char[] ch = null;
			try {
				if (name.equalsIgnoreCase("firstname")) {
					ch = value.getFirstname().toCharArray();
				} else if (name.equalsIgnoreCase("lastname")) {
					ch = value.getLastname().toCharArray();
				} else if (name.equalsIgnoreCase("birthDay")) {
					ch = value.getBirthDay().toCharArray();
				} else if (name.equalsIgnoreCase("salutation")) {
					ch = value.getSalutation().toCharArray();
				} else if (name.equalsIgnoreCase("title")) {
					ch = value.getTitle().toCharArray();
				} else if (name.equalsIgnoreCase("job")){
					ch = value.getJob().toCharArray();
				} else if (name.equalsIgnoreCase("jobTitle")) {
					ch = value.getJobTitle().toCharArray();
				} else if (name.equalsIgnoreCase("medicalAssociation")) {
					ch = value.getMedicalAssociation().toCharArray();
				} else if (name.equalsIgnoreCase("linkMedicalAssociation")) {
					ch = value.getLinkMedicalAssociation().toCharArray();
				} else if (name.equalsIgnoreCase("countryJob")) {
					ch = value.getCountryJob().toCharArray();
				} else if (name.equalsIgnoreCase("image")) {
					ch = value.getImageId().toString().toCharArray();
				} else if (name.equalsIgnoreCase("sex")) {
					ch = Byte.toString(value.getSex()).toCharArray();
				}
				characters(ch, 0, ch.length);
			} catch (Exception e) {
				log.warn("Error while filling xml with values from Person: " + value.getPersonId() + " - valueName: " + name, e);
			}
	}

	private void fillAddress(AddressValue value, String name){
		char[] ch = null;
		try{
			if(name.equalsIgnoreCase("roomNr")){
				 ch = value.getRoomNr().toCharArray();
			} else if(name.equalsIgnoreCase("buildingLevel")){
				 ch = value.getBuildingLevel().toCharArray();
			} else if(name.equalsIgnoreCase("buildingNr")){
				 ch = value.getBuildingNr().toCharArray();
			} else if(name.equalsIgnoreCase("street")){
				 ch = value.getStreet().toCharArray();
			} else if(name.equalsIgnoreCase("streetNr")){
				 ch = value.getStreetNr().toCharArray();
			} else if(name.equalsIgnoreCase("zipcode")){
				 ch = value.getZipCode().toCharArray();
			} else if(name.equalsIgnoreCase("country")){
				 ch = value.getCountry().toCharArray();
			} else if(name.equalsIgnoreCase("countryCode")){
				 ch = value.getCountryCode().toCharArray();
			} else if(name.equalsIgnoreCase("city")){
				 ch = value.getCity().toCharArray();
			} else if(name.equalsIgnoreCase("postOfficeBox")){
				 ch = value.getPostOfficeBox().toCharArray();
			} else if(name.equalsIgnoreCase("phone1")){
				 ch = value.getPhone1().toCharArray();
			} else if(name.equalsIgnoreCase("phone2")){
				 ch = value.getPhone2().toCharArray();
			} else if(name.equalsIgnoreCase("fax")){
				 ch = value.getFax().toCharArray();
			} else if(name.equalsIgnoreCase("homepage")){
				 ch = value.getHomepage().toCharArray();
			} else if(name.equalsIgnoreCase("misc")){
				 ch = value.getMisc().toCharArray();
			} else if(name.equalsIgnoreCase("mobilePhone")){
				 ch = value.getMobilePhone().toCharArray();
			} else if(name.equalsIgnoreCase("email")){
				 ch = value.getEmail().toCharArray();
			} 
			characters(ch, 0, ch.length);
		} catch (Exception e) {
			log.warn("Error while filling xml with values from Address: " + value.getAddressId() + " - valueName: " + name, e);
		}
			
	}
	
	private void fillTalktime(TalktimeValue value, String name) {
		char[] ch = null;
		try{
			if (name.equalsIgnoreCase("talkTimeType")) {
				ch = value.getTalkTimeType().toCharArray();
			} else if (name.equalsIgnoreCase("talkTimes")) {
				ch = value.getTalkTimes().toCharArray();
			}
			characters(ch, 0, ch.length);
		} catch (Exception e) {
			log.warn("Error while filling xml with values from TalkTime: " + value.getTalkTimeId() + " - valueName: " + name, e);
		}
	}
	
	private void fillUnit(UnitValue value, String name) {
		char[] ch = null;
		try {
			if (name.equalsIgnoreCase("name")) {
				ch = value.getName().toCharArray();
			}
			characters(ch, 0, ch.length);
		} catch (Exception e) {
			log.warn("Error while filling xml with values from Unit: " + value.getUnitId() + " - valueName: " + name, e);
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	public void processingInstruction(String target, String data) throws SAXException {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator locator) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	public void skippedEntity(String name) throws SAXException {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// do nothing
	}

}
