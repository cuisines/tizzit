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
package de.juwimm.cms.cocoon.serialization;

import java.io.OutputStream;
import java.util.HashMap;

import org.apache.avalon.framework.configuration.*;
import org.apache.cocoon.serialization.AbstractSerializer;
import org.apache.log4j.Logger;
import org.ujac.print.DocumentHandler;
import org.ujac.print.DocumentPrinter;
import org.ujac.util.io.HttpResourceLoader;
import org.ujac.util.template.TemplateContext;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:m.frankfurter@juwimm.com">Michael Frankfurter</a>
 * @version $Id$
 */
public final class UJACPdfSerializer extends AbstractSerializer implements Configurable {
	private static Logger log = Logger.getLogger(UJACPdfSerializer.class);
	private String mimetype = "application/pdf";
	private boolean setContentLength = true;
	private boolean supportToc = false;

	public void configure(Configuration conf) throws ConfigurationException {
		this.setContentLength = conf.getChild("set-content-length").getValueAsBoolean(true);
		this.mimetype = conf.getAttribute("mime-type");
		this.supportToc = conf.getChild("support-toc").getValueAsBoolean(false);

		if (log.isDebugEnabled()) log.debug("UJACSerializerXmlToPdf mime-type:" + mimetype);
	}

	@Override
	public String getMimeType() {
		return mimetype;
	}

	@Override
	public void startDocument() throws SAXException {
		if (log.isDebugEnabled()) log.debug("starting PDF document");
		super.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		if (log.isDebugEnabled()) log.debug("finished PDF document");
	}

	@Override
	public void setOutputStream(OutputStream out) {
		DocumentHandler dh = null;

		if (this.supportToc) {
			DocumentPrinter dp = new DocumentPrinter();
			dp.setXmlReaderClass("org.apache.xerces.parsers.SAXParser");
			dp.setResourceLoader(new HttpResourceLoader());

			try {
				dh = dp.printDocument(out, this.supportToc);
			} catch (Exception e) {
				log.error("could not create DocumentHandler by dp.printDocument(), using DocumentHandler directly\n" + e.getMessage());

				dh = new DocumentHandler(out, new TemplateContext(new HashMap()));
				dh.setXmlReaderClass("org.apache.xerces.parsers.SAXParser");
				dh.setResourceLoader(new HttpResourceLoader());
			}
		} else {
			dh = new DocumentHandler(out, new TemplateContext(new HashMap()));
			dh.setXmlReaderClass("org.apache.xerces.parsers.SAXParser");
			dh.setResourceLoader(new HttpResourceLoader());
		}

		this.contentHandler = dh;
	}

	@Override
	public boolean shouldSetContentLength() {
		return this.setContentLength;
	}

}
