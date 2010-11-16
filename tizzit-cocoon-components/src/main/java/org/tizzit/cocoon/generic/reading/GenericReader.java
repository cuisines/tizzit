/*
 * Created on 21.02.2006
 */
package org.tizzit.cocoon.generic.reading;

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
import org.apache.cocoon.reading.AbstractReader;
import org.apache.excalibur.source.SourceValidity;
import org.apache.log4j.Logger;
import org.tizzit.cocoon.generic.helper.ConfigurationHelper;
import org.tizzit.core.classloading.ClassloadingHelper;
import org.xml.sax.SAXException;

/**
 * all implemented interfaces: Component, Generator, LogEnabled, Poolable, Recyclable, SitemapModelComponent, XMLProducer
 *
 * sitemap-snippets: <map:readers> <map:reader name="AdminReader" src="de.juwimm.cocoon.reading.GenericReader"> <classname>de.juwimm.vfa.game.cocoon.reading.AdminReader</classname> <classpath>
 * <jar>juwimm-web-vfa-game-cocoon-1.0.jar</jar> <jar>juwimm-web-vfa-game-remote-1.0.jar</jar> </classpath> <siteShort>vfa-game</siteShort> </map:reader> </map:readers>
 *
 * lifecycle: 1.) configure 2.) setup 3.) generate 4.) recycle 5.) configure 6.) setup 7.) generate 8.) recycle
 *
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id:GenericReader.java 4371 2008-05-28 09:37:42Z kulawik $
 */
public class GenericReader extends AbstractCacheableReader implements Configurable, CacheableProcessingComponent {
	private static Logger log = Logger.getLogger(GenericReader.class);
	private AbstractReader reader = null;
	private Configurable configurable = null;
	private Configuration config = null;

	/*
	 * (non-Javadoc)
	 *
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
		//if (this.reader == null) {
		try {
			String clzName = ConfigurationHelper.getClassName(config);

			this.reader = (AbstractReader) ClassloadingHelper.getInstance(clzName);
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
			this.reader.setOutputStream(super.out);
			if (this.reader != null) this.reader.generate();
		} catch (IOException ex) {
			throw ex;
		} catch (SAXException ex) {
			throw ex;
		}
		if (log.isDebugEnabled()) log.debug("generate() -> end");
	}

	@Override
	public String getMimeType() {
		return this.reader.getMimeType();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.cocoon.generation.AbstractGenerator#recycle()
	 */
	@Override
	public void recycle() {
		if (log.isDebugEnabled()) log.debug("recycle() -> begin");
		if (this.reader != null) this.reader.recycle();
		if (log.isDebugEnabled()) log.debug("recycle() -> end");
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.caching.CacheableProcessingComponent#getKey()
	 */
	@Override
	public Serializable getKey() {
		if (log.isDebugEnabled()) log.debug("getKey() -> begin");
		Serializable retVal = null;
		try {
			if (this.reader != null) retVal = ((CacheableProcessingComponent) this.reader).getKey();
		} catch (ClassCastException exe) {
			//do nothing
		}
		if (log.isDebugEnabled()) log.debug("getKey() -> end");
		return retVal;
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.caching.CacheableProcessingComponent#getValidity()
	 */
	@Override
	public SourceValidity getValidity() {
		if (log.isDebugEnabled()) log.debug("getValidity() -> begin");
		SourceValidity retVal = null;
		try {
			if (this.reader != null) retVal = ((CacheableProcessingComponent) this.reader).getValidity();
		} catch (ClassCastException exe) {
			//do nothing
		}
		if (log.isDebugEnabled()) log.debug("getValidity() -> end");
		return retVal;
	}

}
