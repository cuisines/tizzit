package org.tizzit.cocoon.generic.transformation;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.transform.OutputKeys;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.configuration.Settings;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractSAXTransformer;
import org.apache.cocoon.xml.IncludeXMLConsumer;
import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.source.Source;
import org.apache.log4j.Logger;
import org.tizzit.util.tidy.Tidy;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <b>Patched version of <code>org.apache.cocoon.transformation.HTMLTransformer</code>.</b><br/>
 * Converts (escaped) HTML snippets into JTidied HTML.
 * This transformer expects a list of elements, passed as comma separated
 * values of the "tags" parameter. It records the text enclosed in such
 * elements and pass it thru JTidy to obtain valid XHTML.
 *
 * <p>TODO: Add namespace support.
 * <p><strong>WARNING:</strong> This transformer should be considered unstable.
 *
 * @cocoon.sitemap.component.documentation
 * Converts (escaped) HTML snippets into JTidied HTML.
 * This transformer expects a list of elements, passed as comma separated
 * values of the "tags" parameter. It records the text enclosed in such
 * elements and pass it thru JTidy to obtain valid XHTML.
 * @cocoon.sitemap.component.documentation.caching Not Implemented
 *
 * @version $Id$
 */
public class TidyHTMLTransformer extends AbstractSAXTransformer implements Configurable {
	private static final Logger log = Logger.getLogger(TidyHTMLTransformer.class);
	private static final String PARAM_NAME_ENCODING = "encoding";

	/**
	 * The properties.
	 */
	protected Properties properties = new Properties();

	/**
	 * Tags that must be normalized
	 */
	private Map<String, String> tags;

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
	protected String defaultEncoding = "UTF-8";

	/**
	 * React on endElement calls that contain a tag to be
	 * tidied and run Jtidy on it, otherwise passthru.
	 *
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String name, String raw) throws SAXException {
		if (this.tags.containsKey(name)) {
			String toBeNormalized = this.endTextRecording();
			try {
				this.normalize(toBeNormalized);
			} catch (ProcessingException exe) {
				exe.printStackTrace();
			}
		}
		super.endElement(uri, name, raw);
	}

	/**
	 * Start buffering text if inside a tag to be normalized,
	 * passthru otherwise.
	 *
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String name, String raw, Attributes attr) throws SAXException {
		super.startElement(uri, name, raw, attr);
		if (this.tags.containsKey(name)) {
			this.startTextRecording();
		}
	}

	/**
	 * Configure this transformer, possibly passing to it
	 * a jtidy configuration file location.
	 *
	 * @deprecated use property injection instead
	 */
	public void configure(Configuration config) throws ConfigurationException {
		super.configure(config);
		String configUrl = config.getChild("jtidy-config").getValue(null);
		try {
			if (this.resolver == null) {
				this.resolver = (SourceResolver) this.manager.lookup(SourceResolver.ROLE);
			}
			loadJTidyProperties(configUrl, this.resolver);
		} catch (Exception exe) {
			log.error(exe.getMessage(), exe);
		}
	}

	/**
	 * The beef: run JTidy on the buffered text and stream
	 * the result
	 *
	 * @param text the string to be tidied
	 */
	private void normalize(String text) throws ProcessingException {
		try {
			// Setup an instance of Tidy.
			Tidy tidy = new Tidy();
			tidy.setXmlOut(true);

			if (this.properties == null) {
				tidy.setXHTML(true);
			} else {
				tidy.setConfigurationFromProps(this.properties);
			}

			//Set Jtidy final result summary on-off
			tidy.setQuiet(!log.isInfoEnabled());
			//Set Jtidy infos to a String (will be logged) instead of System.out
			StringWriter stringWriter = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(stringWriter);
			tidy.setErrout(errorWriter);

			// Extract the document using JTidy and stream it.
			ByteArrayInputStream bais = new ByteArrayInputStream(text.getBytes(this.defaultEncoding));
			org.w3c.dom.Document doc = tidy.parseDOM(new BufferedInputStream(bais), null);

			// FIXME: Jtidy doesn't warn or strip duplicate attributes in same
			// tag; stripping.
			XMLUtils.stripDuplicateAttributes(doc, null);

			errorWriter.flush();
			errorWriter.close();

			if (tidy.getShowWarnings()) {
				log.warn(stringWriter.toString());
			}

			IncludeXMLConsumer.includeNode(doc, this.contentHandler, this.lexicalHandler);
		} catch (Exception exe) {
			throw new ProcessingException("Exception in TidyHTMLTransformer.normalize()", exe);
		}
	}

	/**
	 * Setup this component, passing the tag names to be tidied.
	 */
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
		super.setup(resolver, objectModel, src, par);
		String tagsParam = par.getParameter("tags", "");
		if (log.isDebugEnabled()) {
			log.debug("tags: " + tagsParam);
		}
		this.tags = new HashMap<String, String>();
		StringTokenizer tokenizer = new StringTokenizer(tagsParam, ",");
		while (tokenizer.hasMoreElements()) {
			String tok = tokenizer.nextToken().trim();
			this.tags.put(tok, tok);
		}

		// sets the encoding if possible
		try {
			this.defaultEncoding = par.getParameter(PARAM_NAME_ENCODING);
		} catch (Exception exe) {
		}

		if (!this.properties.containsKey(OutputKeys.ENCODING) && this.defaultEncoding != null) {
			this.properties.put(OutputKeys.ENCODING, this.defaultEncoding);
		}

		String configUrl = this.properties.getProperty("jtidy-config");
		loadJTidyProperties(configUrl, resolver);
	}

	/**
	 * Set the default encoding. This will be overided if an encoding parameter is set.
	 * This is mainly useful together with Spring
	 * bean inheritance.
	 *
	 * @param defaultEncoding the defaultEncoding to set
	 * @see de.juwimm.cocoon.components.transformation.TidyHTMLTransformer#encoding
	 */
	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	/**
	 * @see org.apache.avalon.framework.service.Serviceable#service(ServiceManager)
	 * @deprecated use property injection instead
	 */
	public void service(ServiceManager manager) throws ServiceException {
		super.service(manager);
		final Settings settings = (Settings) manager.lookup(Settings.ROLE);
		String defaultEncoding = settings.getFormEncoding();
		if (defaultEncoding != null) {
			this.defaultEncoding = defaultEncoding;
		}
		this.properties.setProperty(OutputKeys.ENCODING, this.defaultEncoding);
		manager.release(settings);
	}

	/**
	 * This is mainly useful together with Spring bean inheritance.
	 *
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * Loads and sets the JTidy properties.
	 *
	 * @param configUrl the URL to the properties (e.g. "resource://jtidy.properties")
	 */
	private void loadJTidyProperties(String configUrl, SourceResolver resolver) {
		try {
			if (configUrl != null) {
				Source configSource = null;
				try {
					configSource = resolver.resolveURI(configUrl);
					if (log.isDebugEnabled()) {
						log.debug("Loading configuration from " + configSource.getURI());
					}
					this.properties.load(configSource.getInputStream());
				} catch (Exception exe) {
					log.warn("Cannot load configuration from " + configUrl);
					throw new ConfigurationException("Cannot load configuration from " + configUrl, exe);
				} finally {
					if (configSource != null) {
						resolver.release(configSource);
						configSource = null;
					}
				}
			}
		} catch (Exception exe) {
			log.warn(exe.getMessage(), exe);
		}
	}
}
