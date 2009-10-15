package org.tizzit.plugins.server.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tizzit.plugins.server.impl.exceptions.IllegalTizzitPluginNamespaceUriException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import de.juwimm.cms.plugins.Constants;
import de.juwimm.cms.plugins.server.Request;
import de.juwimm.cms.plugins.server.Response;
import de.juwimm.cms.plugins.server.TizzitPlugin;

/**
 * <h5>How to implement:</h5>
 * <ul>
 * <li>Configure your plugin namespace with the {@link AbstractKeyValuePlugin#configurePluginNamespaceUri()} method.</li>
 * <li>Call the {@link AbstractKeyValuePlugin#getConfiguration()} method to get the key/value pairs as {@link Map} of your XML input.</li>
 * <li>Stream your result SAX events with the parent {@link org.xml.sax.ContentHandler} ({@link AbstractKeyValuePlugin#parent}).
 * </ul>
 *
 * <h5>Usage / Configuration:</h5>
 * <b>XML:</b>
 * <pre> &lt;testPlugin xmlns="http://plugins.tizzit.org/MyPluginName"&gt;
 *   &lt;myFirstKey&gt;myFirstValue&lt;/myFirstKey&gt;
 *   &lt;aSecondKeyValueConfiguration&gt;another value&lt;/aSecondKeyValueConfiguration&gt;
 *   &lt;aBooleanKeyVal&gt;true&lt;/aBooleanKeyVal&gt;
 * &lt;/testPlugin&gt;</pre>
 *
 * <h5>Output:</h5>
 * Use the method {@link AbstractKeyValuePlugin#processContent()} and the parent {@link org.xml.sax.ContentHandler} ({@link AbstractKeyValuePlugin#parent}) to stream your SAX events.
 *
 * <p>
 * <b>Namespace: Must be provided by the implementation ({@link AbstractKeyValuePlugin#configurePluginNamespaceUri()}).</b>
 * </p>
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-plugin-impl 15.10.2009
 */
public abstract class AbstractKeyValuePlugin implements TizzitPlugin {
	private static final Log log = LogFactory.getLog(AbstractKeyValuePlugin.class);

	private static final int PROCESS_NO_PROCESS = -1;
	private static final int PROCESS_CHARS = 10;

	private int processing = PROCESS_NO_PROCESS;

	protected ContentHandler parent = null;
	protected Request req;
	protected Response resp;
	protected Integer uniquePageId;

	private Map<String, String> configuration;
	private String namespaceUri = null;

	private String curLocalName = null;

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.TizzitPlugin#configurePlugin(de.juwimm.cms.plugins.server.Request, de.juwimm.cms.plugins.server.Response, org.xml.sax.ContentHandler, java.lang.Integer)
	 */
	public void configurePlugin(Request req, Response resp, ContentHandler ch, Integer uniquePageId) {
		if (log.isDebugEnabled()) log.debug("configurePlugin() -> begin");
		this.req = req;
		this.resp = resp;
		this.parent = ch;
		this.uniquePageId = uniquePageId;

		this.namespaceUri = this.configurePluginNamespaceUri();

		if (StringUtils.trimToNull(this.namespaceUri) == null) {
			String msg = "Namespace URI must not be null or empty! Please set a valid namespace URI for your plugin (e.g.: '" + Constants.PLUGIN_NAMESPACE + "MyPluginName" + "')!";
			log.error(msg, new IllegalTizzitPluginNamespaceUriException(msg));
		} else if (this.namespaceUri.equals(Constants.PLUGIN_NAMESPACE)) {
			String msg = "Namespace URI must not be equal to '" + Constants.PLUGIN_NAMESPACE + "'! Please append this URI with the name of your plugin!";
			log.error(msg, new IllegalTizzitPluginNamespaceUriException(msg));
		}

		if (log.isDebugEnabled()) log.debug("configurePlugin() -> end");
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
		if (this.namespaceUri.equals(uri)) {
			this.curLocalName = localName;
			this.processing = PROCESS_CHARS;
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		switch (this.processing) {
			case PROCESS_CHARS:
				if (this.configuration == null) {
					this.configuration = new HashMap<String, String>();
				}

				this.configuration.put(this.curLocalName, StringUtils.trimToNull(new String(ch, start, length)));

				this.processing = PROCESS_NO_PROCESS;
				break;
		}
	}

	public abstract String configurePluginNamespaceUri();

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.TizzitPlugin#getLastModifiedDate()
	 */
	public abstract Date getLastModifiedDate();

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.TizzitPlugin#isCacheable()
	 */
	public abstract boolean isCacheable();

	/* (non-Javadoc)
	 * @see de.juwimm.cms.plugins.server.TizzitPlugin#processContent()
	 */
	public abstract void processContent();

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String name) throws SAXException {
		//		if (this.namespaceUri.equals(uri)) {
		//			this.curLocalName = localName;
		//			if (this.configuration.containsKey(curLocalName)) {
		//				if (this.configuration.get(curLocalName) == null) {
		//					this.processing = PROCESS_CHARS;
		//				}
		//			}
		//		}
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

	/**
	 * @return the configuration
	 */
	public Map<String, String> getConfiguration() {
		return configuration;
	}
}
