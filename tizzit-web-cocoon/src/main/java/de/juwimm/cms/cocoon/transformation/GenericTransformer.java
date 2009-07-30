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
package de.juwimm.cms.cocoon.transformation;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.excalibur.pool.Recyclable;
import org.apache.avalon.framework.configuration.*;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;

import de.juwimm.cms.cocoon.ClassLoadingHelper;
import de.juwimm.cms.cocoon.helper.ConfigurationHelper;

/**
 * 
 * sitemap.xml-snippets:
 * <map:transformers default="xalan">
 * 		...
 * 		<map:transformer name="adminTransformer" src="de.juwimm.cms.cocoon.transformation.GenericTransformer">
 *			<classname>de.juwimm.vfa.game.cocoon.transformation.AdminTransformer</classname>
 *			<classpath>
 *	 			<jar>juwimm-web-vfa-game-cocoon-1.0.jar</jar>
 *	 			<jar>juwimm-web-vfa-game-remote-1.0.jar</jar>
 *	 		</classpath>
 *	 		<siteShort>vfa-game</siteShort>
 *		</map:transformer>
 *		...			
 * </map:transformers>
 * 
 * <map:match pattern="generictransformertest/transformertest">
 *		<map:generate src="xml/transformertest.xml"/>
 *		<map:transform type="adminTransformer">				
 *			<!-- optional if not configured at declaration -->
 *			<map:parameter name="clientCode" value="{global:clientCode}"/>
 *		</map:transform>
 *		<map:serialize type="xml"/>
 *	</map:match>
 *
 *	lifecycle (always pooled because it implements Recyclable):
 *	1.) configure
 *  2.) setup(...)
 *  3.) ...
 *  4.) recycle(...)
 *  5.) setup(...)
 *  6.) ...
 *  7.) recycle(...)
 *  ...
 * 
 * @author toerberj
 * Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class GenericTransformer extends AbstractTransformer implements Recyclable, Configurable {
	private static Logger log = Logger.getLogger(GenericTransformer.class);
	/* the extern loadable transformer */
	private AbstractTransformer transformer = null;
	/* if transformer implements Configurable this is also set */
	private Configurable configurable = null;
	/* store Configuration gotten by config(...) and pass to external transformer if it implements Configurable */
	private Configuration config = null;
	private ClassLoadingHelper classHelper = null;

	public void configure(Configuration config) throws ConfigurationException {
		this.config = config;
	}

	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters params) throws ProcessingException, SAXException, IOException {
		if (log.isDebugEnabled()) log.debug("setup(...) begin");

		//if (this.transformer == null) {
			try {
				// during declaration in the sitemap you can't use global variables, but at usage time, so optionally allow this parameter 
				String clientCode = params.getParameter("clientCode", "");
				String siteShort = clientCode.length() > 0 ? clientCode : ConfigurationHelper.getSiteShort(this.config);

				this.classHelper = new ClassLoadingHelper(siteShort, ConfigurationHelper.getJarNames(this.config));
				String clzName = ConfigurationHelper.getClassName(this.config);

				this.transformer = (AbstractTransformer) this.classHelper.instanciateClass(clzName);
				if (this.transformer instanceof Configurable) {
					this.configurable = (Configurable) this.transformer;
					// during the first call of configure (see below) there was no instance of transformer/configurable so we have to call this now
					if (this.config != null) {
						this.configurable.configure(this.config);
					}
				}
			} catch (Exception ex) {
				log.error(ex);
			}
		//}
		if (this.transformer != null) this.transformer.setup(arg0, arg1, arg2, params);

		if (log.isDebugEnabled()) log.debug("setup(...) end");
	}

	@Override
	public void startDocument() throws SAXException {
		if (log.isDebugEnabled()) log.debug("startDocument()");
		if (this.transformer != null) this.transformer.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		if (log.isDebugEnabled()) log.debug("endDocument()");
		// if there are ejb-calls in this.transformer.endDocument setting this here is to early;
		// on the other side if this.transformer.enddocument throws exception it isn't set back       
		// Thread.currentThread().setContextClassLoader(prevContextClassloader);
		try {
			if (this.transformer != null) this.transformer.endDocument();
		} catch (SAXException ex) {
			throw ex;
			// is called always except for previous call of System.exit()
		}
	}

	@Override
	public void recycle() {
		if (log.isDebugEnabled()) log.debug("recycle()");

		/*
		 // Verschieben von endDocument hierhin bringt keine Verbesserung
		 if (this.classHelper != null) {
		 classHelper.retireBorderline();
		 }
		 */
		if (this.transformer != null) {
			this.transformer.recycle();
		}
	}

	/* ************************************** Mandatory implementation delegates ********************************** */

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		if (this.transformer != null) this.transformer.characters(arg0, arg1, arg2);
	}

	@Override
	public void comment(char[] arg0, int arg1, int arg2) throws SAXException {
		if (this.transformer != null) this.transformer.comment(arg0, arg1, arg2);
	}

	@Override
	public void endCDATA() throws SAXException {
		if (this.transformer != null) this.transformer.endCDATA();
	}

	@Override
	public void endDTD() throws SAXException {
		if (this.transformer != null) this.transformer.endDTD();
	}

	@Override
	public void endElement(String arg0, String arg1, String arg2) throws SAXException {
		if (this.transformer != null) this.transformer.endElement(arg0, arg1, arg2);
	}

	@Override
	public void endEntity(String arg0) throws SAXException {
		if (this.transformer != null) this.transformer.endEntity(arg0);
	}

	@Override
	public void endPrefixMapping(String arg0) throws SAXException {
		if (this.transformer != null) this.transformer.endPrefixMapping(arg0);
	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
		if (this.transformer != null) this.transformer.ignorableWhitespace(arg0, arg1, arg2);
	}

	@Override
	public void processingInstruction(String arg0, String arg1) throws SAXException {
		if (this.transformer != null) this.transformer.processingInstruction(arg0, arg1);
	}

	@Override
	public void setDocumentLocator(Locator arg0) {
		if (this.transformer != null) this.transformer.setDocumentLocator(arg0);
	}

	@Override
	public void skippedEntity(String arg0) throws SAXException {
		if (this.transformer != null) this.transformer.skippedEntity(arg0);
	}

	@Override
	public void startCDATA() throws SAXException {
		if (this.transformer != null) this.transformer.startCDATA();
	}

	@Override
	public void startDTD(String arg0, String arg1, String arg2) throws SAXException {
		if (this.transformer != null) this.transformer.startDTD(arg0, arg1, arg2);
	}

	@Override
	public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
		if (this.transformer != null) this.transformer.startElement(arg0, arg1, arg2, arg3);
	}

	@Override
	public void startEntity(String arg0) throws SAXException {
		if (this.transformer != null) this.transformer.startEntity(arg0);
	}

	@Override
	public void startPrefixMapping(String arg0, String arg1) throws SAXException {
		if (this.transformer != null) this.transformer.startPrefixMapping(arg0, arg1);
	}

	@Override
	public void setConsumer(XMLConsumer arg0) {
		if (this.transformer != null) this.transformer.setConsumer(arg0);
	}

	@Override
	public void setContentHandler(ContentHandler arg0) {
		if (this.transformer != null) this.transformer.setContentHandler(arg0);
	}

	@Override
	public void setLexicalHandler(LexicalHandler arg0) {
		if (this.transformer != null) this.transformer.setLexicalHandler(arg0);
	}

}
