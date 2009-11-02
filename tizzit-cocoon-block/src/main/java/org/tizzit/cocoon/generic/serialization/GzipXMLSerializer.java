/*
 * Copyright (c) 2002-${year} Juwi MacMillan Group GmbH (JuwiMM)
 * Bockhorn 1, 29664 Walsrode, Germany
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of JuwiMM
 * ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with JuwiMM.
 */
package org.tizzit.cocoon.generic.serialization;

import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.avalon.framework.CascadingRuntimeException;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.cocoon.serialization.AbstractTextSerializer;

/**
 * <strong>Configuration:</strong>
 * <pre>
 * &lt;map:sitemap xmlns:map="http://apache.org/cocoon/sitemap/1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://apache.org/cocoon/sitemap/1.0 http://cocoon.apache.org/schema/sitemap/cocoon-sitemap-1.0.xsd"&gt;
 *   &lt;map:components&gt;
 *     &lt;map:serializers default="xhtml"&gt;
 *       &lt;map:serializer name="gzip" mime-type="application/x-gzip" src="de.juwimm.cocoon.components.serialization.GzipXMLSerializer" /&gt;
 *       ...
 *     &lt;/map:serializers&gt;
 *     ...
 *   &lt;/map:components&gt;
 *   ...
 *   &lt;map:pipelines&gt;
 *     &lt;map:pipeline type="noncaching"&gt;
 *       &lt;map:match pattern="funkyGZip"&gt;
 *         &lt;map:generate src="httpd/xml/dummy.xml" /&gt;
 *         &lt;map:serialize type="gzip" /&gt;
 *       &lt;/map:match&gt;
 *       ...
 *     &lt;/map:pipeline&gt;
 *     ...
 *   &lt;/map:pipelines&gt;
 * &lt;/map:sitemap&gt;
 * </pre>
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since juwimm-cocoon-core 31.03.2009
 */
public class GzipXMLSerializer extends AbstractTextSerializer {
	private TransformerHandler handler;
	private GZIPOutputStream gzop;

	public GzipXMLSerializer() {
	}

	/**
	 * Set the configurations for this serializer.
	 * @deprecated use property injection instead
	 */
	@Deprecated
	@Override
	public void configure(Configuration conf) throws ConfigurationException {
		super.configure(conf);
		this.format.put(OutputKeys.METHOD, "xml");
	}

	@Override
	public void setOutputStream(OutputStream out) {
		try {
			gzop = new GZIPOutputStream(out);
			super.setOutputStream(out);
			this.handler = getTransformerFactory().newTransformerHandler();
			handler.getTransformer().setOutputProperties(format);
			handler.setResult(new StreamResult(gzop));
			this.setContentHandler(handler);
			this.setLexicalHandler(handler);
		} catch (Exception e) {
			getLogger().error("XMLSerializer.setOutputStream()", e);
			throw new CascadingRuntimeException("XMLSerializer.setOutputStream()", e);
		}
	}

	@Override
	public void endDocument() {
		try {
			super.endDocument();
			gzop.finish();
		} catch (Exception e) {
			System.out.println("endDocument() died: " + e);
		}
	}

	/**
	 * Recycle the serializer. GC instance variables
	 */
	@Override
	public void recycle() {
		super.recycle();
		this.handler = null;
	}
}
