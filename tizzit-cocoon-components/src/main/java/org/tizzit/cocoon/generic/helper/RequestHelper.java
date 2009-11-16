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

import java.util.Map;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.commons.lang.StringUtils;

// TODO: Class description
/**
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-cocoon-components 09.11.2009
 */
public class RequestHelper {

	public static String getHost(Map< ? , ? > objectModel) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		return getHost(request);
	}

	public static String getHost(Request request) {
		String host = request.getHeader("Host");
		if (StringUtils.isNotBlank(host)) {
			int portPosition = host.lastIndexOf(":");
			if (portPosition > 0) {
				host = host.substring(0, portPosition);
			}
		}
		return host;
	}

	public static String getRequestedURL(Map< ? , ? > objectModel) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		StringBuffer uribuf = null;
		boolean isSecure = request.isSecure();
		int port = request.getServerPort();

		if (isSecure) {
			uribuf = new StringBuffer("https://");
		} else {
			uribuf = new StringBuffer("http://");
		}

		uribuf.append(request.getServerName());
		if (isSecure) {
			if (port != 443) {
				uribuf.append(":").append(port);
			}
		} else {
			if (port != 80) {
				uribuf.append(":").append(port);
			}
		}

		uribuf.append(request.getRequestURI());
		return uribuf.toString();
	}
}
