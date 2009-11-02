/*
 * Copyright (c) 2002-2009 Juwi MacMillan Group GmbH (JuwiMM)
 * Bockhorn 1, 29664 Walsrode, Germany
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of JuwiMM
 * ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with JuwiMM.
 */
package org.tizzit.cocoon.generic.generation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Map;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.source.util.SourceUtil;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.tizzit.cocoon.generic.helper.ParameterHelper;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Generates the value of the <code>content</code> parameter or the input stream into SAX events.
 * The <code>content</code> parameter has a higher priority and will be used, if the value is not null or empty!
 *
 * <h5>Configuration:</h5>
 * <pre> &lt;bean class="org.tizzit.cocoon.generic.generation.XMLRequestParserGenerator" name="org.apache.cocoon.generation.Generator/XMLRequestParserGenerator" scope="prototype"&gt;
 *   &lt;property name="parser" ref="org.apache.cocoon.core.xml.SAXParser" /&gt;
 * &lt;/bean&gt;</pre>
 *
 * <h5>Usage:</h5>
 * <pre> &lt;map:generate type="XMLRequestParserGenerator" /&gt;</pre>
 *
 * <h5>Usage with src attribute:</h5>
 * <pre> &lt;map:generate type="XMLRequestParserGenerator" src="cocoon://yourPipelineOrFile"&gt;
 *   &lt;map:parameter name="forceUseSrcAttribute" value="true" /&gt; &lt;-- Possible values: true / yes / false / no --&gt;
 * &lt;/map:generate&gt;</pre>
 *
 * <h5>Parameters</h5>
 * <ul>
 *   <li>
 *    INGOING (pipeline)<br/>
 *    <b>optional:</b>
 *    <ul>
 *      <li><b>forceUseSrcAttribute</b> - default: <code>false</code> - Set this parameter to <code>true</code> or <code>yes</code>. to provide a source with the <code>src</code> attribute.</li>
 *      <li><b>encoding</b> - default: <code>org.apache.cocoon.containerencoding</code> - fallback on errors: <code>UTF-8</code> - Overrides the Cocoon container encoding.</li>
 *    </ul>
 *   </li>
 *   <li>
 *    INGOING (request)<br/>
 *    <ul>
 *      <li><b>content</b> - XMl content, which will be converted into SAX events. The generator tries to convert the input stream into SAX events, if this parameter is empty or null!</li>
 *    </ul>
 *   </li>
 * </ul>
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-cocoon-components 02.11.2009
 */
public class XMLRequestParserGenerator extends AbstractGenerator {
	private static final Log log = LogFactory.getLog(XMLRequestParserGenerator.class);

	private static final String PARAM_NAME_CONTENT = "content";
	private static final String PARAM_NAME_ENCODING = "encoding";
	private static final String PARAM_NAME_FORCE_USE_SRC_ATTRIBUTE = "forceUseSrcAttribute";

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

	private boolean forceUseSrcAttribute = false;
	private Source cocoonSource = null;

	private Request request = null;

	/** The SAX Parser. */
	protected SAXParser parser;

	/* (non-Javadoc)
	 * @see org.apache.cocoon.generation.AbstractGenerator#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
		if (log.isDebugEnabled()) log.debug("setup() -> begin");

		super.setup(resolver, objectModel, src, par);
		this.request = ObjectModelHelper.getRequest(objectModel);

		try {
			this.forceUseSrcAttribute = ParameterHelper.booleanValue(par.getParameter(PARAM_NAME_FORCE_USE_SRC_ATTRIBUTE));

			if (this.forceUseSrcAttribute) {
				try {
					this.cocoonSource = super.resolver.resolveURI(this.source);
				} catch (SourceException se) {
					throw SourceUtil.handle("Error during resolving of '" + this.source + "'.", se);
				}

				if (log.isDebugEnabled()) {
					log.debug("Source " + super.source + " resolved to " + this.cocoonSource.getURI());
				}
			}
		} catch (ParameterException exe) {
		}

		try {
			this.defaultEncoding = par.getParameter(PARAM_NAME_ENCODING);
		} catch (Exception exe) {
		}

		if (log.isDebugEnabled()) log.debug("setup() -> end");
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.generation.Generator#generate()
	 */
	public void generate() throws IOException, SAXException, ProcessingException {
		if (log.isDebugEnabled()) log.debug("generate() -> begin");

		if (this.forceUseSrcAttribute) {
			try {
				SourceUtil.parse(this.parser, this.cocoonSource, super.xmlConsumer);
			} catch (SAXException e) {
				SourceUtil.handleSAXException(this.cocoonSource.getURI(), e);
			}
		} else {
			String content = this.request.getParameter(PARAM_NAME_CONTENT);
			if (StringUtils.isNotBlank(content)) {
				if (log.isDebugEnabled()) log.debug("Found '" + PARAM_NAME_CONTENT + "' parameter. Parsing the value: \n" + content);
				InputSource inputSource = new InputSource(new StringReader(content));
				this.parser.parse(inputSource, this.contentHandler);
			} else {
				if (log.isDebugEnabled()) log.debug("Parameter '" + PARAM_NAME_CONTENT + "' is null or empty! Parsing the input stream...");

				InputStream is = this.request.getInputStream();
				int contentLength = 0;
				try {
					contentLength = Integer.parseInt(this.request.getHeader("Content-Length"));
				} catch (Exception exe) {
					log.warn("Could not parse Content-Length:" + exe.getMessage());
				}
				try {
					if (is != null && contentLength > 0) {
						InputSource inputSource = new InputSource(new InputStreamReader(is, this.defaultEncoding));
						this.parser.parse(inputSource, this.contentHandler);
					} else {
						this.printErrorAsXML("Parameter '" + PARAM_NAME_CONTENT + "' and the input stream are null or empty! Please fill at least one of it!");
					}
				} catch (IOException exe) {
					throw exe;
				} catch (SAXException exe) {
					throw exe;
				} finally {
					if (is != null) {
						IOUtils.closeQuietly(is);
						is = null;
					}
				}
			}
		}
		if (log.isDebugEnabled()) log.debug("generate() -> end");
	}

	private void printErrorAsXML(String msg) throws IOException, SAXException {
		if (log.isDebugEnabled()) log.debug("printErrorAsXML() -> begin");
		log.warn(msg);

		String error = "<error>";
		error += msg;
		error += "</error>";
		InputSource inputSource = new InputSource(new StringReader(error));
		this.parser.parse(inputSource, this.contentHandler);
		if (log.isDebugEnabled()) log.debug("printErrorAsXML() -> end");
	}

	/**
	 * @param parser the parser to set
	 */
	public void setParser(SAXParser parser) {
		this.parser = parser;
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.generation.AbstractGenerator#recycle()
	 */
	@Override
	public void recycle() {
		super.recycle();
		if (this.cocoonSource != null) {
			super.resolver.release(this.cocoonSource);
			this.cocoonSource = null;
		}
	}

	/**
	 * @param defaultEncoding the defaultEncoding to set
	 */
	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}
}
