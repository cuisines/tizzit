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
package org.tizzit.classloading;

import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.beans.foreign.CqPropertiesBeanSpring;

// TODO: Class description
/**
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-core 07.10.2009
 */
public class ClassloadingHelper {
	@Autowired
	private CqPropertiesBeanSpring cqPropertiesBeanSpring;

	public Object getInstance(String clazzName) throws Exception {
		return this.loadClass(clazzName).newInstance();
	}

	public static Object getInstance(String clazzName, CqPropertiesBeanSpring cqPropertiesBeanSpring) throws Exception {
		return ClassloadingHelper.getInstance(clazzName, ReloadingClassloaderManager.getClassLoader(cqPropertiesBeanSpring));
	}

	public static Object getInstance(String clazzName, ClassLoader classLoader) throws Exception {
		return classLoader.loadClass(clazzName).newInstance();
	}

	public Class< ? > loadClass(String clazz) throws Exception {
		return ReloadingClassloaderManager.getClassLoader(this.cqPropertiesBeanSpring).loadClass(clazz);
	}

	public static Class< ? > loadClass(String clazzName, CqPropertiesBeanSpring cqPropertiesBeanSpring) throws Exception {
		return ClassloadingHelper.loadClass(clazzName, ReloadingClassloaderManager.getClassLoader(cqPropertiesBeanSpring));
	}

	public static Class< ? > loadClass(String clazzName, ClassLoader classLoader) throws Exception {
		return classLoader.loadClass(clazzName);
	}

}
