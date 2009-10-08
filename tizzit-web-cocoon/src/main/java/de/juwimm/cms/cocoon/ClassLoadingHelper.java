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
/**
 *
 */
package de.juwimm.cms.cocoon;

import org.apache.log4j.Logger;
import org.tizzit.core.classloading.ClassloadingHelper;

/**
 * Helper for loading new classes dynamically
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @deprecated Use {@link org.tizzit.core.classloading.ClassloadingHelper#getInstance(String)} instead!
 */
@Deprecated
public class ClassLoadingHelper {
	private static Logger log = Logger.getLogger(ClassLoadingHelper.class);

	/**
	 * Create an new helper
	 * @param mandatorShort
	 * @param jarNames
	 */
	public ClassLoadingHelper(String mandatorShort, String[] jarNames) {
		log.warn("This class is deprecated! Use the provided alternatives instead!");
	}

	/**
	 * instanciates a new instance of the desired class
	 * @param classname
	 * @return an instance of the desired class
	 */
	public Object instanciateClass(String classname) {
		try {
			return ClassloadingHelper.getInstance(classname);
		} catch (Exception exe) {
			log.error("Could not instantiate '" + classname + "' - " + exe.getMessage(), exe);
			return null;
		}
	}

	public void restoreContextClassLoader() {
		//do nothing
	}
}
