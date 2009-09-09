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
package de.juwimm.cms.cocoon.acting;

import java.util.Map;

import org.apache.avalon.framework.component.*;
import org.apache.avalon.framework.configuration.*;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Logger;

import de.juwimm.cms.cocoon.ClassLoadingHelper;
import de.juwimm.cms.cocoon.helper.ConfigurationHelper;

/**
 * An <code>Action</code> for dynamically loading of site-specific Actions.
 * 
 * <p>
 * <h5>Configuration:</h5>
 * 
 * <pre>
 *    &lt;map:action name=&quot;web20action&quot; src=&quot;de.juwimm.cms.cocoon.acting.GenericAction&quot;&gt; 
 *       &lt;siteShort&gt;qmd&lt;/siteShort&gt;
 *       &lt;classname&gt;de.juwimm.web20.cocoon.acting.Web20InsertAction&lt;/classname&gt;
 *       &lt;classpath&gt;
 *          &lt;jar&gt;juwimm-web20-cocoon-1.0.0.jar&lt;/jar&gt;
 *          &lt;jar&gt;juwimm-web20-common-1.0.0.jar&lt;/jar&gt;
 *          &lt;jar&gt;juwimm-web20-core-1.0.0-client.jar&lt;/jar&gt;
 *       &lt;/classpath&gt;
 *    &lt;/map:action&gt;
 * </pre>
 * 
 * </p>
 * <p>
 * <h5>Usage:</h5>
 * You can use this dynamically loaded action like every other action:
 * 
 * <pre>
 *    &lt;map:match pattern=&quot;test/web20insert.xml&quot;&gt;
 *       &lt;map:act type=&quot;web20action&quot;&gt;
 *          &lt;map:parameter name=&quot;use-request-parameters&quot; value=&quot;true&quot; /&gt;
 *          &lt;map:parameter name=&quot;siteId&quot; value=&quot;6&quot; /&gt;
 *          &lt;map:parameter name=&quot;viewComponentId&quot; value=&quot;639&quot; /&gt;
 *          &lt;map:parameter name=&quot;cqUserName&quot; value=&quot;username&quot; /&gt;
 *          &lt;map:generate src=&quot;xml/web20.xml&quot; /&gt;
 *          &lt;map:transform type=&quot;web20&quot;&gt;
 *             &lt;map:parameter name=&quot;siteId&quot; value=&quot;6&quot; /&gt;
 *             &lt;map:parameter name=&quot;viewComponentId&quot; value=&quot;639&quot; /&gt;
 *             &lt;map:parameter name=&quot;cqUserName&quot; value=&quot;username&quot; /&gt;
 *          &lt;/map:transform&gt;
 *          &lt;map:transform type=&quot;vcresolve&quot; /&gt;
 *          &lt;map:serialize type=&quot;xml&quot; /&gt;
 *       &lt;/map:act&gt;
 *    &lt;/map:match&gt;
 * </pre>
 * 
 * </p>
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class GenericAction extends AbstractAction implements Configurable, Composable {
	private static Logger log = Logger.getLogger(GenericAction.class);
	private ComponentManager manager = null;
	private AbstractAction action = null;
	private Composable composable = null; // deprecated (which version?), now you should use Serviceable 
	private Configurable configurable = null;
	private ClassLoadingHelper classHelper = null;

	public void compose(ComponentManager arg0) throws ComponentException {
		if (log.isDebugEnabled()) log.debug("compose begin");

		this.manager = arg0;
		if (composable != null) composable.compose(arg0);
	}

	public void configure(Configuration config) throws ConfigurationException {
		if (log.isDebugEnabled()) log.debug("configure begin");

		//if (this.action == null) {
			classHelper = new ClassLoadingHelper(ConfigurationHelper.getSiteShort(config), ConfigurationHelper.getJarNames(config));
			String clzName = ConfigurationHelper.getClassName(config);

			try {
				this.action = (AbstractAction) classHelper.instanciateClass(clzName);

				if (this.action instanceof Configurable) this.configurable = (Configurable) this.action;
				if (this.action instanceof Composable) {
					if (this.composable == null) {
						this.composable = (Composable) this.action;
						this.composable.compose(this.manager);
					}
				}
			} catch (ComponentException e) {
				log.error(e);
				throw new ConfigurationException("could not call compose(...) on " + clzName);
			}
			if (configurable != null) configurable.configure(config);
			if (log.isDebugEnabled()) log.debug("configure end");
		//}
	}

	public Map act(Redirector arg0, SourceResolver arg1, Map arg2, String arg3, Parameters arg4) throws Exception {
		if (log.isDebugEnabled()) log.debug("act begin");
		Map map = null;

		try {
			if (action != null) map = action.act(arg0, arg1, arg2, arg3, arg4);
		} catch (Exception ex) {
			throw ex;
		}
		
		if (log.isDebugEnabled()) log.debug("act end");
		return map;
	}

}
