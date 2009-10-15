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
package org.tizzit.plugins.server.impl.exceptions;

/**
 * Marker class.
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-plugin-impl 15.10.2009
 */
public class IllegalTizzitPluginNamespaceUriException extends TizzitPluginException {
	private static final long serialVersionUID = 7604414549687340680L;

	/* (non-Javadoc)
	 * @see java.lang.Exception()
	 */
	public IllegalTizzitPluginNamespaceUriException() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Exception(java.lang.String message, java.lang.Throwable cause)
	 */
	public IllegalTizzitPluginNamespaceUriException(String message, Throwable cause) {
		super(message, cause);
	}

	/* (non-Javadoc)
	 * @see java.lang.Exception(java.lang.Throwable cause)
	 */
	public IllegalTizzitPluginNamespaceUriException(Throwable cause) {
		super(cause);
	}

	/* (non-Javadoc)
	 * @see java.lang.Exception(java.lang.String message)
	 */
	public IllegalTizzitPluginNamespaceUriException(String message) {
		super(message);
	}
}