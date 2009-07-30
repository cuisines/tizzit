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
/*
 * Created on 21.02.2006
 */
package de.juwimm.cms.cocoon.reading;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.configuration.*;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.AbstractReader;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import de.juwimm.cms.cocoon.ClassLoadingHelper;
import de.juwimm.cms.cocoon.helper.ConfigurationHelper;

/**
 * all implemented interfaces: 
 * Component, Generator, LogEnabled, Poolable, Recyclable, SitemapModelComponent, XMLProducer
 * 
 * sitemap-snippets:
 * <map:readers>
 *		<map:reader name="AdminReader"	src="de.juwimm.cocoon.reading.GenericReader">
 *			<classname>de.juwimm.vfa.game.cocoon.reading.AdminReader</classname>
 *			<classpath>
 *	 			<jar>juwimm-web-vfa-game-cocoon-1.0.jar</jar>
 *	 			<jar>juwimm-web-vfa-game-remote-1.0.jar</jar>
 *	 		</classpath>
 *	 		<siteShort>vfa-game</siteShort>
 * 		</map:reader>
 * </map:readers>
 * 
 * lifecycle:
 * 1.) configure
 * 2.) setup
 * 3.) generate
 * 4.) recycle
 * 5.) configure
 * 6.) setup
 * 7.) generate
 * 8.) recycle
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class GenericReader extends AbstractReader implements Configurable {
	private static Logger log = Logger.getLogger(GenericReader.class);
	private AbstractReader reader = null;
	private Configurable configurable = null;
	private Configuration config = null;
	private ClassLoadingHelper classHelper = null;

	/* (non-Javadoc)
	 * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
	 */
	public void configure(Configuration config) throws ConfigurationException {
		this.config = config;
	}

	/*
	 * setting up this generator
	 */
	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters parameters) throws ProcessingException, SAXException, IOException {
		if (log.isDebugEnabled()) log.debug("begin setup");
		//if (this.reader == null) {
			try {
				String clientCode = parameters.getParameter("clientCode", "");
				String siteShort = clientCode.length() > 0 ? clientCode : ConfigurationHelper.getSiteShort(this.config);

				classHelper = new ClassLoadingHelper(siteShort, ConfigurationHelper.getJarNames(config));
				String clzName = ConfigurationHelper.getClassName(config);

				this.reader = (AbstractReader) classHelper.instanciateClass(clzName);
				if (this.reader instanceof Configurable) {
					this.configurable = (Configurable) this.reader;
					// during the first call of configure (see below) there was no instance of generator/configurable so we have to call this now
					if (this.config != null) {
						this.configurable.configure(this.config);
					}
				}
			} catch (Exception ex) {
				log.error(ex);
			}
		//}
		if (this.reader != null) this.reader.setup(resolver, objectModel, src, parameters);
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.generation.AbstractGenerator#generate()
	 */
	public void generate() throws IOException, SAXException, ProcessingException {
		if (log.isDebugEnabled()) log.debug("generate begin ...");
		try {
			this.reader.setOutputStream(super.out);
			this.reader.generate();
			if (this.reader != null) this.reader.generate();
		} catch (IOException ex) {
			throw ex;
		} catch (SAXException ex) {
			throw ex;
		}
	}

	@Override
	public String getMimeType() {
		return this.reader.getMimeType();
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.generation.AbstractGenerator#recycle()
	 */
	@Override
	public void recycle() {
		if (log.isDebugEnabled()) log.debug("recycle() begin");
		if (this.reader != null) this.reader.recycle();
	}

}
