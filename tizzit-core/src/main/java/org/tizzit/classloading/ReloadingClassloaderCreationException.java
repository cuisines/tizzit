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

// TODO: Class description
/**
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-core 07.10.2009
 */
public class ReloadingClassloaderCreationException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -8257550273235512619L;

	public ReloadingClassloaderCreationException(String msg) {
		super(msg);
	}

	public ReloadingClassloaderCreationException(String msg, Exception e) {
		super(msg, e);
	}

}
