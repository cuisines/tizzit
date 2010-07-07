package org.tizzit.cocoon.generic.serialization;

import java.io.OutputStream;
import java.util.HashMap;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.cocoon.serialization.AbstractSerializer;
import org.apache.log4j.Logger;
import org.ujac.print.DocumentHandler;
import org.ujac.print.DocumentPrinter;
import org.ujac.util.io.HttpResourceLoader;
import org.ujac.util.template.TemplateContext;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:m.frankfurter@juwimm.com">Michael Frankfurter</a>
 * @version $Id:UJACPdfSerializer.java 4371 2008-05-28 09:37:42Z kulawik $
 */
public final class UJACPdfSerializer extends AbstractSerializer implements Configurable {
	private static Logger log = Logger.getLogger(UJACPdfSerializer.class);
	private String mimetype = "application/pdf"; //$NON-NLS-1$
	private boolean setContentLength = true;
	private boolean supportToc = false;

	public void configure(Configuration conf) throws ConfigurationException {
		this.setContentLength = conf.getChild("set-content-length").getValueAsBoolean(true); //$NON-NLS-1$
		this.mimetype = conf.getAttribute("mime-type"); //$NON-NLS-1$
		this.supportToc = conf.getChild("support-toc").getValueAsBoolean(false); //$NON-NLS-1$

		if (log.isDebugEnabled()) log.debug("UJACSerializerXmlToPdf mime-type:" + mimetype); //$NON-NLS-1$
	}

	@Override
	public String getMimeType() {
		return mimetype;
	}

	@Override
	public void startDocument() throws SAXException {
		if (log.isDebugEnabled()) log.debug("starting PDF document"); //$NON-NLS-1$
		super.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		if (log.isDebugEnabled()) log.debug("finished PDF document"); //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setOutputStream(OutputStream out) {
		DocumentHandler dh = null;
		DocumentPrinter dp = new DocumentPrinter();

		if (this.supportToc) {

			dp.setXmlReaderClass("org.apache.xerces.parsers.SAXParser"); //$NON-NLS-1$
			dp.setResourceLoader(new HttpResourceLoader());

			try {
				dh = dp.printDocument(out, this.supportToc);
			} catch (Exception e) {
				log.error("could not create DocumentHandler by dp.printDocument(), using DocumentHandler directly\n" + e.getMessage()); //$NON-NLS-1$

				dh = new DocumentHandler(out, new TemplateContext(new HashMap()), dp.getTagFactory());
				dh.setXmlReaderClass("org.apache.xerces.parsers.SAXParser"); //$NON-NLS-1$
				dh.setResourceLoader(new HttpResourceLoader());
			}
		} else {
			dh = new DocumentHandler(out, new TemplateContext(new HashMap()), dp.getTagFactory());
			dh.setXmlReaderClass("org.apache.xerces.parsers.SAXParser"); //$NON-NLS-1$
			dh.setResourceLoader(new HttpResourceLoader());
		}

		this.contentHandler = dh;
	}

	@Override
	public boolean shouldSetContentLength() {
		return this.setContentLength;
	}

}
