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
package de.juwimm.cms.cocoon.generation;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.configuration.*;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import de.juwimm.cms.cocoon.ClassLoadingHelper;
import de.juwimm.cms.cocoon.helper.ConfigurationHelper;

/**
 * Generic <code>Generator</code> for dynamically loading of site-specific generators.
 * 
 * <p><h5>Configuration:</h5>
 * <pre>
 * &lt;map:generator name="newGenerator" src="de.juwimm.novartis.tania.cocoon.generation.GenericGenerator"&gt;
 *    &lt;classname>de.juwimm.vfa.game.cocoon.generation.AdminGenerator&lt;/classname&gt;
 *    &lt;classpath&gt;
 *       &lt;jar&gt;juwimm-web-vfa-game-cocoon-1.0.jar&lt;/jar&gt;
 *       &lt;jar&gt;juwimm-web-vfa-game-remote-1.0.jar&lt;/jar&gt;
 *    &lt;/classpath&gt;
 *    &lt;siteShort&gt;vfa-game&lt;/siteShort&gt;
 * &lt;/map:generator&gt;
 * </pre>
 * </p>
 * <p><h5>Usage:</h5>
 * You can now use this generator like every other generator.
 * </p>
 * @author <a href="carsten.schalm@juwimm.com">Carsten Schalm</a>
 * Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class GenericGenerator extends AbstractGenerator implements Configurable {
	private static Logger log = Logger.getLogger(GenericGenerator.class);
	private AbstractGenerator generator = null;
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
		//if (this.generator == null) {
			try {
				String clientCode = parameters.getParameter("clientCode", "");
				String siteShort = clientCode.length() > 0 ? clientCode : ConfigurationHelper.getSiteShort(this.config);

				classHelper = new ClassLoadingHelper(siteShort, ConfigurationHelper.getJarNames(config));
				String clzName = ConfigurationHelper.getClassName(config);

				this.generator = (AbstractGenerator) classHelper.instanciateClass(clzName);
				if (this.generator instanceof Configurable) {
					this.configurable = (Configurable) this.generator;
					// during the first call of configure (see below) there was no instance of generator/configurable so we have to call this now
					if (this.config != null) {
						this.configurable.configure(this.config);
					}
				}
			} catch (Exception ex) {
				log.error(ex);
			}
		//}
		if (this.generator != null) this.generator.setup(resolver, objectModel, src, parameters);
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.generation.AbstractGenerator#generate()
	 */
	public void generate() throws IOException, SAXException, ProcessingException {
		if (log.isDebugEnabled()) log.debug("generate begin ...");
		try {
			this.generator.setContentHandler(contentHandler);
			if (this.generator != null) this.generator.generate();
		} catch (IOException ex) {
			throw ex;
		} catch (SAXException ex) {
			throw ex;
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.generation.AbstractGenerator#recycle()
	 */
	@Override
	public void recycle() {
		if (log.isDebugEnabled()) log.debug("recycle() begin");
		if (this.generator != null) this.generator.recycle();
	}

}
