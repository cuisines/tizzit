/*
 * Created on 21.02.2006
 */
package org.tizzit.cocoon.generic.generation;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.excalibur.source.SourceValidity;
import org.apache.log4j.Logger;
import org.tizzit.classloading.ClassloadingHelper;
import org.tizzit.cocoon.generic.util.ConfigurationHelper;
import org.xml.sax.SAXException;

/**
 * Generic <code>Generator</code> for dynamically loading of site-specific generators.
 *
 * <p>
 * <h5>Configuration:</h5>
 *
 * <pre>
 *    &lt;map:generator name=&quot;newGenerator&quot; src=&quot;de.juwimm.novartis.tania.cocoon.generation.GenericGenerator&quot;&gt;
 *       &lt;classname&gt;de.juwimm.vfa.game.cocoon.generation.AdminGenerator&lt;/classname&gt;
 *       &lt;classpath&gt;
 *          &lt;jar&gt;juwimm-web-vfa-game-cocoon-1.0.jar&lt;/jar&gt;
 *          &lt;jar&gt;juwimm-web-vfa-game-remote-1.0.jar&lt;/jar&gt;
 *       &lt;/classpath&gt;
 *       &lt;siteShort&gt;vfa-game&lt;/siteShort&gt;
 *    &lt;/map:generator&gt;
 * </pre>
 *
 * </p>
 * <p>
 * <h5>Usage:</h5>
 * You can now use this generator like every other generator.
 * </p>
 *
 * @author <a href="carsten.schalm@juwimm.com">Carsten Schalm</a> , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id:GenericGenerator.java 4371 2008-05-28 09:37:42Z kulawik $
 */
public class GenericGenerator extends AbstractCacheableGenerator implements Configurable {
	private static Logger log = Logger.getLogger(GenericGenerator.class);
	private AbstractGenerator generator = null;
	private Configurable configurable = null;
	private Configuration config = null;
	private ClassloadingHelper classloadingHelper = null;

	/**
	 * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
	 */
	public void configure(Configuration config) throws ConfigurationException {
		if (log.isDebugEnabled()) log.debug("configure() -> begin");
		this.config = config;
		if (log.isDebugEnabled()) log.debug("configure() -> end");
	}

	/*
	 * setting up this generator
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters parameters) throws ProcessingException, SAXException, IOException {
		if (log.isDebugEnabled()) log.debug("setup() -> begin");
		//if (this.generator == null) {
		try {
			this.classloadingHelper = new ClassloadingHelper();

			String clazzName = ConfigurationHelper.getClassName(this.config);
			this.generator = (AbstractGenerator) this.classloadingHelper.getInstance(clazzName);

			if (this.generator instanceof Configurable) {
				this.configurable = (Configurable) this.generator;
				// during the first call of configure (see below) there was
				// no instance of generator/configurable so we have to call
				// this now
				if (this.config != null) {
					this.configurable.configure(this.config);
				}
			}
		} catch (Exception ex) {
			log.error(ex);
		}
		//}
		if (this.generator != null) this.generator.setup(resolver, objectModel, src, parameters);
		if (log.isDebugEnabled()) log.debug("setup() -> end");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.cocoon.generation.AbstractGenerator#generate()
	 */
	public void generate() throws IOException, SAXException, ProcessingException {
		if (log.isDebugEnabled()) log.debug("generate() -> begin");
		try {
			this.generator.setContentHandler(contentHandler);
			if (this.generator != null) this.generator.generate();
		} catch (IOException ex) {
			throw ex;
		} catch (SAXException ex) {
			throw ex;
		}
		if (log.isDebugEnabled()) log.debug("generate() -> end");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.cocoon.generation.AbstractGenerator#recycle()
	 */
	@Override
	public void recycle() {
		if (log.isDebugEnabled()) log.debug("recycle() -> begin");
		if (this.generator != null) this.generator.recycle();
		if (log.isDebugEnabled()) log.debug("recycle() -> end");
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cocoon.generation.AbstractCacheableGenerator#getKey()
	 */
	@Override
	public Serializable getKey() {
		if (log.isDebugEnabled()) log.debug("getKey() -> begin");
		Serializable retVal = null;
		try {
			if (this.generator != null) retVal = ((CacheableProcessingComponent) this.generator).getKey();
		} catch (ClassCastException exe) {
			//do nothing
		}
		if (log.isDebugEnabled()) log.debug("getKey() -> end");
		return retVal;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cocoon.generation.AbstractCacheableGenerator#getValidity()
	 */
	@Override
	public SourceValidity getValidity() {
		if (log.isDebugEnabled()) log.debug("getValidity() -> begin");
		SourceValidity retVal = null;
		try {
			if (this.generator != null) retVal = ((CacheableProcessingComponent) this.generator).getValidity();
		} catch (ClassCastException exe) {
			//do nothing
		}
		if (log.isDebugEnabled()) log.debug("getValidity() -> end");
		return retVal;
	}
}
