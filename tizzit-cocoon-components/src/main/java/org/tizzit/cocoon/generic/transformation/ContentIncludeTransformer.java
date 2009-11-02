package org.tizzit.cocoon.generic.transformation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.sf.saxon.om.NamespaceConstant;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.configuration.Settings;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.commons.io.IOUtils;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.log4j.Logger;
import org.tizzit.util.xml.DOMHelper;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author skulawik
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-cocoon-components 02.11.2009
 */
public class ContentIncludeTransformer extends AbstractTransformer implements Serviceable {// implements CacheableProcessingComponent {
	private static Logger log = Logger.getLogger(ContentIncludeTransformer.class);
	public static final String NAMESPACE_CONTENT_INCLUDE_DEPRECATED = "http://cocoon.juwimm.com/contentInclude/1.0";
	public static final String NAMESPACE_CONTENT_INCLUDE = "http://cocoon.tizzit.org/contentInclude/1.0";
	public static final String CONTENT_INCLUDE_ELEMENT = "contentInclude";
	private static final String PARAM_NAME_ENCODING = "encoding";
	private SourceResolver resolver;
	private HashMap<String, String> namespaces = new HashMap<String, String>();

	private long startParseTime = 0L;

	/**
	 * The encoding.
	 * <p>
	 * A parameter could be used, to set the encoding <code>&lt;map:parameter name="encoding" value="UTF-8"/&gt;</code>.<br/>
	 * Default value: <code>org.apache.cocoon.containerencoding</code>.<br/>
	 * Fallback value if an error occurred: <code>UTF-8</code>
	 * </p>
	 * <p>
	 * <b>Please make sure, that you use the same encoding in your tidy.properties (<code>char-encoding=utf8</code>).</b>
	 * </p>
	 */
	private String defaultEncoding = "UTF-8";

	/**
	 * The source.
	 */
	private String source;

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		super.startPrefixMapping(prefix, uri);
		if (!namespaces.containsKey(prefix)) {
			namespaces.put(prefix, uri);
		}
	}

	@Override
	public void startDocument() throws SAXException {
		this.startParseTime = System.currentTimeMillis();
		contentHandler.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		contentHandler.endDocument();
		if (log.isDebugEnabled() && this.source != null) log.debug("'" + this.source + "'" + " Processed by " + this.getClass().getSimpleName() + " in " + (System.currentTimeMillis() - this.startParseTime) + " ms.");
	}

	public void setResolver(SourceResolver resolver) {
		this.resolver = resolver;
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
	 */
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
		this.resolver = resolver;
		try {
			this.defaultEncoding = par.getParameter(PARAM_NAME_ENCODING);
		} catch (Exception exe) {
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.xml.AbstractXMLPipe#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		contentHandler.startElement(uri, localName, qName, attrs);
		if (CONTENT_INCLUDE_ELEMENT.equalsIgnoreCase(localName) && (NAMESPACE_CONTENT_INCLUDE.equals(uri) || NAMESPACE_CONTENT_INCLUDE_DEPRECATED.equals(uri))) {
			if (NAMESPACE_CONTENT_INCLUDE_DEPRECATED.equals(uri)) {
				log.warn("The namespace '" + NAMESPACE_CONTENT_INCLUDE_DEPRECATED + "' for '" + CONTENT_INCLUDE_ELEMENT + "' is deprecated! Please use '" + NAMESPACE_CONTENT_INCLUDE + "' instead!");
			}

			String src = attrs.getValue("src");
			this.source = src;
			Source inputSource = null;
			try {
				inputSource = resolver.resolveURI(src);
			} catch (Exception se) {
				log.error("Error resolving source " + src, se);
			}

			String xpathStr = attrs.getValue("xpath");
			InputStream stream = null;
			try {
				stream = inputSource.getInputStream();

				if ("/*".equals(xpathStr) || "/".equals(xpathStr) || "".equals(xpathStr) || xpathStr == null) {
					String xml = IOUtils.toString(stream, this.defaultEncoding);

					DOMHelper.string2sax(xml, contentHandler, this.defaultEncoding);
				} else {
					Document doc = DOMHelper.inputstream2Dom(stream);

					System.setProperty("javax.xml.xpath.XPathFactory:" + NamespaceConstant.OBJECT_MODEL_SAXON, "net.sf.saxon.xpath.XPathFactoryImpl");
					XPath xpath = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON).newXPath();
					xpath.setNamespaceContext(new LocalNamespaceContext());

					NodeList nl = (NodeList) xpath.evaluate(xpathStr, doc, XPathConstants.NODESET);
					for (int i = 0; i < nl.getLength(); i++) {
						DOMHelper.node2sax(nl.item(i), new DOMHelper.OmitXmlDeclarationContentHandler(contentHandler));
					}

					/*
					 * DOMHelper.findNodes uses org.jaxen.BaseXPath
					 */
					//					Iterator<Node> it = DOMHelper.findNodes(doc, xpath, namespaces);
					//					while (it.hasNext()) {
					//						DOMHelper.node2sax(it.next(), new DOMHelper.OmitXmlDeclarationContentHandler(contentHandler));
					//					}
				}

			} catch (Exception e) {
				log.error("Error executing xpath for source " + src + " with xpath " + xpathStr, e);
			} finally {
				IOUtils.closeQuietly(stream);
				resolver.release(inputSource);
				//if (log.isDebugEnabled()) log.debug("'" + src + "'" + " Processed by " + this.getClass().getSimpleName() + " in " + (System.currentTimeMillis() - startParseTime) + " ms.");
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.xml.AbstractXMLPipe#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		contentHandler.endElement(uri, localName, qName);
	}

	/**
	 * Set the default encoding. This will be overided if an encoding parameter is set.
	 * This is mainly useful together with Spring
	 * bean inheritance.
	 *
	 * @param defaultEncoding the defaultEncoding to set
	 * @see de.juwimm.cocoon.components.transformation.ContentIncludeTransformer#encoding
	 */
	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	/**
	 * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
	 * @deprecated use property injection instead
	 */
	@Deprecated
	public void service(ServiceManager manager) throws ServiceException {
		final Settings settings = (Settings) manager.lookup(Settings.ROLE);
		String defaultEncoding = settings.getContainerEncoding();
		if (defaultEncoding != null) {
			this.defaultEncoding = defaultEncoding;
		}
		manager.release(settings);
	}

	/**
	 * <p>Return the caching key associated with this transformation.</p>
	 *
	 * <p>When including <code>cocoon://</code> sources with dynamic
	 * content depending on environment (request parameters, session attributes,
	 * etc), it makes sense to provide such environment values to the transformer
	 * to be included into the key using <code>key</code> sitemap parameter.</p>
	 *
	 * @see CacheableProcessingComponent#getKey()
	 */
	/*public Serializable getKey() {

	     * In case of including "cocoon://" or other dynamic sources key
	     * ideally has to include ProcessingPipelineKey of the included
	     * "cocoon://" sources, but it's not possible as at this time
	     * we don't know yet which sources will get included into the
	     * response.
	     *
	     * Hence, javadoc recommends providing key using sitemap parameter.

	    return key == null? "I": "I" + key;
	}*/

	/**
	 * <p>Generate (or return) the {@link SourceValidity} instance used to
	 * possibly validate cached generations.</p>
	 *
	 * @return a <b>non null</b> {@link SourceValidity}.
	 * @see org.apache.cocoon.caching.CacheableProcessingComponent#getValidity()
	 */
	/*public SourceValidity getValidity() {
	    if (validity == null) {
	        validity = new MultiSourceValidity(resolver, -1);
	    }
	    return validity;
	}*/

	private class LocalNamespaceContext implements NamespaceContext {

		public String getNamespaceURI(String prefix) {
			String retVal = null;

			if (namespaces.containsKey(prefix)) {
				retVal = namespaces.get(prefix);
			}

			return retVal;
		}

		public String getPrefix(String namespaceURI) {
			return null;
		}

		public Iterator<String> getPrefixes(String namespaceURI) {
			return null;
		}

	}
}
