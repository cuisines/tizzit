package org.tizzit.cocoon.generic.acting;

import java.io.Serializable;
import java.util.Map;

import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.excalibur.source.SourceValidity;
import org.apache.log4j.Logger;
import org.tizzit.cocoon.generic.util.ConfigurationHelper;
import org.tizzit.core.classloading.ClassloadingHelper;

/**
 * An <code>Action</code> for dynamically loading of site-specific Actions.
 *
 * <p>
 * <h5>Configuration:</h5>
 *
 * <pre>
 *     &lt;map:action name=&quot;web20action&quot; src=&quot;de.juwimm.cms.cocoon.acting.GenericAction&quot;&gt;
 *        &lt;siteShort&gt;qmd&lt;/siteShort&gt;
 *        &lt;classname&gt;de.juwimm.web20.cocoon.acting.Web20InsertAction&lt;/classname&gt;
 *        &lt;classpath&gt;
 *           &lt;jar&gt;juwimm-web20-cocoon-1.0.0.jar&lt;/jar&gt;
 *           &lt;jar&gt;juwimm-web20-common-1.0.0.jar&lt;/jar&gt;
 *           &lt;jar&gt;juwimm-web20-core-1.0.0-client.jar&lt;/jar&gt;
 *        &lt;/classpath&gt;
 *     &lt;/map:action&gt;
 * </pre>
 *
 * </p>
 * <p>
 * <h5>Usage:</h5>
 * You can use this dynamically loaded action like every other action:
 *
 * <pre>
 *     &lt;map:match pattern=&quot;test/web20insert.xml&quot;&gt;
 *        &lt;map:act type=&quot;web20action&quot;&gt;
 *           &lt;map:parameter name=&quot;use-request-parameters&quot; value=&quot;true&quot; /&gt;
 *           &lt;map:parameter name=&quot;siteId&quot; value=&quot;6&quot; /&gt;
 *           &lt;map:parameter name=&quot;viewComponentId&quot; value=&quot;639&quot; /&gt;
 *           &lt;map:parameter name=&quot;cqUserName&quot; value=&quot;username&quot; /&gt;
 *           &lt;map:generate src=&quot;xml/web20.xml&quot; /&gt;
 *           &lt;map:transform type=&quot;web20&quot;&gt;
 *              &lt;map:parameter name=&quot;siteId&quot; value=&quot;6&quot; /&gt;
 *              &lt;map:parameter name=&quot;viewComponentId&quot; value=&quot;639&quot; /&gt;
 *              &lt;map:parameter name=&quot;cqUserName&quot; value=&quot;username&quot; /&gt;
 *           &lt;/map:transform&gt;
 *           &lt;map:transform type=&quot;vcresolve&quot; /&gt;
 *           &lt;map:serialize type=&quot;xml&quot; /&gt;
 *        &lt;/map:act&gt;
 *     &lt;/map:match&gt;
 * </pre>
 *
 * </p>
 *
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id:GenericAction.java 4371 2008-05-28 09:37:42Z kulawik $
 */
public class GenericAction extends AbstractCacheableAction implements Configurable, Composable {
	private static Logger log = Logger.getLogger(GenericAction.class);
	private ComponentManager manager = null;
	private AbstractAction action = null;
	private Composable composable = null; // deprecated (which version?), now you should use Serviceable
	private Configurable configurable = null;

	public void compose(ComponentManager arg0) throws ComponentException {
		if (log.isDebugEnabled()) log.debug("compose() -> begin");
		this.manager = arg0;
		if (this.composable != null) this.composable.compose(arg0);
		if (log.isDebugEnabled()) log.debug("compose() -> end");
	}

	public void configure(Configuration config) throws ConfigurationException {
		if (log.isDebugEnabled()) log.debug("configure() -> begin");
		//if (this.action == null) {
		String clzName = ConfigurationHelper.getClassName(config);
		try {
			this.action = (AbstractAction) ClassloadingHelper.getInstance(clzName);
			if (this.action instanceof Configurable) this.configurable = (Configurable) this.action;
			if (this.action instanceof Composable) {
				if (this.composable == null) {
					this.composable = (Composable) this.action;
					this.composable.compose(this.manager);
				}
			}
		} catch (ComponentException e) {
			log.error(e);
			throw new ConfigurationException("could not call compose(...) on " + clzName); //$NON-NLS-1$
		} catch (Exception exe) {
			throw new ConfigurationException("Could not instantiate " + clzName); //$NON-NLS-1$
		}
		if (this.configurable != null) this.configurable.configure(config);
		if (log.isDebugEnabled()) log.debug("configure() -> end");
		//}
	}

	@SuppressWarnings("unchecked")
	public Map act(Redirector arg0, SourceResolver arg1, Map arg2, String src, Parameters param) throws Exception {
		if (log.isDebugEnabled()) log.debug("act() -> begin");
		Map map = null;

		try {
			if (this.action != null) map = this.action.act(arg0, arg1, arg2, src, param);
		} catch (Exception ex) {
			throw ex;
		}

		if (log.isDebugEnabled()) log.debug("act() -> end");
		return map;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cocoon.generation.AbstractCacheableGenerator#getKey()
	 */
	@Override
	public Serializable getKey() {
		if (log.isDebugEnabled()) log.debug("getKey() -> begin");
		Serializable retVal = null;
		try {
			if (this.action != null) retVal = ((CacheableProcessingComponent) this.action).getKey();
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
			if (this.action != null) retVal = ((CacheableProcessingComponent) this.action).getValidity();
		} catch (ClassCastException exe) {
			//do nothing
		}
		if (log.isDebugEnabled()) log.debug("getValidity() -> end");
		return retVal;
	}
}
