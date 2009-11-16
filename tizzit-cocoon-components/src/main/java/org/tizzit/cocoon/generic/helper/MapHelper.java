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
package org.tizzit.cocoon.generic.helper;

import java.io.StringWriter;
import java.util.Map;

// TODO: Class description
/**
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-cocoon-block 09.11.2009
 */
public class MapHelper {

	public static String mapToString(String headLine, Map< ? , ? > map) {
		StringWriter sw = new StringWriter();
		sw.append("\n" + headLine + ": {\n");
		for (Object key : map.keySet()) {
			sw.append("\t" + key + " = " + map.get(key) + "\n");
		}
		sw.append("}");
		return sw.toString();
	}

	public static String mapToString(Map< ? , ? > map) {
		return mapToString("Result", map);
	}
}
