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

import org.apache.cocoon.generation.ExceptionGenerator;
import org.apache.cocoon.spring.configurator.impl.RunningModeHelper;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since juwimm-tridion-cocoon22 30.10.2009
 */
public class ExceptionHelper {

	public static void throwableToSAX(Throwable t, ContentHandler ch, String cocoonRunningMode) throws SAXException {
		if (RunningModeHelper.determineRunningMode(cocoonRunningMode).equals(cocoonRunningMode)) {
			ExceptionHelper.throwableToSAX(t, ch);
		}
	}

	public static void throwableToSAX(Throwable t, ContentHandler ch) throws SAXException {
		ExceptionGenerator.toSAX(t, ch);
	}
}
