package org.tizzit.cocoon.generic.acting;

import org.apache.avalon.framework.thread.SingleThreaded;

/**
 * We already have some Actions which implement Interface SingleThreaded, so we assume we need a GenericAction which also implements SingleThreaded;
 * we also assume that single threaded is enough at the moment;
 *
 * @see de.juwimm.cms.cocoon.acting.GenericAction
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-cocoon-components 30.10.2009
 */
public class GenericActionSingleThreaded extends GenericAction implements SingleThreaded {
}
