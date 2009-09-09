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
package de.juwimm.cms.cocoon.components.store.impl;

import java.io.InputStream;
import java.util.Properties;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.components.store.impl.CocoonStoreJanitor;

/**
 * Special CocoonStoreJanitor loading values from conquest.properties-file.<br/>
 * The file &quot;cocoon.xconf&quot; for configuring the behavior of the StoreJanitor contains fixed absolute values.<br/>
 * To adjust these values for every server the <code>ConquestCocoonStoreJanitor</code> takes absolute or - for memory-settings - relative values<br/>
 * from the &quot;conquest.properties&quot;.<br/>
 * On error we use all the values of &quot;cocoon.xconf&quot; untouched.<br/>
 * Possible values:
 * <ul>
 * <li>&quot;cqCocoonJanitorFreeMemoryRatio&quot; - freememory (X% of Xmx) or &quot;cqCocoonJanitorFreeMemory&quot; for fixed absolute value, default 2097152 Bytes = 2 MB</li>
 * <li>&quot;cqCocoonJanitorHeapSizeRatio&quot; - heapsize (Xmx - X%) or &quot;cqCocoonJanitorHeapSize&quot; for fixed absolute value, default 66600000 Bytes</li>
 * <li>&quot;cqCocoonCleanupThreadIntervalSecs&quot; - cleanupthreadinterval, default 15 seconds</li>
 * <li>&quot;cqCocoonPercentToFree&quot; - percent_to_free, default 10 %</li>
 * <li>&quot;cqCocoonInvokeGC&quot; - invokegc, default true</li>
 * </ul>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @since ConQuest 2.4.8
 */
public class ConquestCocoonStoreJanitor extends CocoonStoreJanitor implements Parameterizable {
	private static final String PROPERTIES_FILENAME = "conquest.properties";

	/**
	 * Normally this method is called with the values from the file &quot;cocoon.xconf&quot;.<br/>
	 * Here we try to load the settings from the &quot;conquest.properties&quot; and replace these of the &quot;cocoon.xconf&quot;.<br/>
	 * Values for the memory-setting are calculated in relation of Xmx.<br/>
	 * On error we use all the values of &quot;cocoon.xconf&quot; untouched.
	 * @see org.apache.cocoon.components.store.impl.CocoonStoreJanitor#parameterize(org.apache.avalon.framework.parameters.Parameters)
	 */
	// toCocoon22: @Override
	public void parameterize(Parameters params) throws ParameterException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
		if (is == null) {
			super.getLogger().warn("Unable to load \"" + PROPERTIES_FILENAME + "\", using values from cocoon.xconf!");
		} else {
			Properties props = new Properties();
			try {
				long xmx = Runtime.getRuntime().maxMemory();
				props.load(is);

				String minFreeMemory = "";
				String maxHeapSize = "";
				String janitorFreeMemoryRatio = props.getProperty("cqPropertiesBeanSpring.cocoon.janitorFreeMemoryRatio");
				String janitorHeapSizeRatio = props.getProperty("cqPropertiesBeanSpring.cocoon.janitorHeapSizeRatio");
				String cleanupThreadIntervalSecs = props.getProperty("cqPropertiesBeanSpring.cocoon.cleanupThreadIntervalSecs", "15");
				String percentToFree = props.getProperty("cqPropertiesBeanSpring.cocoon.percentToFree", "10");
				String invokeGC = props.getProperty("cqPropertiesBeanSpring.cocoon.invokeGC", "true");
				if (janitorFreeMemoryRatio == null) {
					minFreeMemory = props.getProperty("cqPropertiesBeanSpring.cocoon.janitorFreeMemory", "2097152"); // 2 MB
				} else {
					try {
						int ratio = Integer.parseInt(janitorFreeMemoryRatio);
						minFreeMemory = Long.toString(xmx * ratio / 100); // ratio % of Xmx
					} catch (Exception e) {
						super.getLogger().warn("Error parsing value " + janitorFreeMemoryRatio + " for cqCocoonJanitorFreeMemoryRatio: " + e.getMessage(), e);
						minFreeMemory = props.getProperty("cqPropertiesBeanSpring.cocoon.janitorFreeMemory", "2097152"); // 2 MB
					}
				}
				if (janitorHeapSizeRatio == null) {
					maxHeapSize = props.getProperty("cqPropertiesBeanSpring.cocoon.janitorHeapSize", "66600000");
				} else {
					try {
						int ratio = Integer.parseInt(janitorHeapSizeRatio);
						maxHeapSize = Long.toString(xmx * (100 - ratio) / 100); // Xmx - ratio %
					} catch (Exception e) {
						super.getLogger().warn("Error parsing value " + janitorHeapSizeRatio + " for cqCocoonJanitorHeapSizeRatio: " + e.getMessage(), e);
						maxHeapSize = props.getProperty("cqPropertiesBeanSpring.cocoon.janitorHeapSize", "66600000");
					}
				}
				// toCocoon22 start:
				// params.setParameter("freememory", minFreeMemory);
				// params.setParameter("heapsize", maxHeapSize);
				// params.setParameter("cleanupthreadinterval", cleanupThreadIntervalSecs);
				// params.setParameter("percent_to_free", percentToFree);
				// params.setParameter("invokegc", invokeGC);
				try {
					super.setFreeMemory(Integer.parseInt(minFreeMemory));
				} catch (Exception exe) {
				}
				try {
					super.setHeapSize(Integer.parseInt(maxHeapSize));
				} catch (Exception exe) {
				}
				try {
					super.setCleanupThreadInterval(Integer.parseInt(cleanupThreadIntervalSecs));
				} catch (Exception exe) {
				}
				try {
					super.setPercentToFree(Double.parseDouble(percentToFree));
				} catch (Exception exe) {
				}
				super.setInvokeGC(Boolean.parseBoolean(invokeGC));
				// toCocoon22: end
				if (super.getLogger().isDebugEnabled()) {
					super.getLogger().debug("freememory: " + minFreeMemory + " Bytes\nheapsize: " + maxHeapSize + " Bytes\ncleanupthreadinterval: " + cleanupThreadIntervalSecs + " Secs\npercent_to_free: " + percentToFree + " %\ninvokegc: " + invokeGC);
				}
			} catch (Exception e) {
				super.getLogger().error("Error loading properties or replacing values from cocoon.xconf: " + e.getMessage(), e);
			}
		}
		// toCocoon22: super.parameterize(params);
	}
}
