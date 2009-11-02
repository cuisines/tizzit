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
package org.tizzit.cocoon.generic.generation;

import java.io.Serializable;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.excalibur.source.SourceValidity;

/**
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since juwimm-cocoon-core 10.06.2009
 */
public abstract class AbstractCacheableGenerator extends AbstractGenerator implements CacheableProcessingComponent {

	/* (non-Javadoc)
	 * @see org.apache.cocoon.caching.CacheableProcessingComponent#getKey()
	 */
	public Serializable getKey() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.caching.CacheableProcessingComponent#getValidity()
	 */
	public SourceValidity getValidity() {
		return null;
	}
}
