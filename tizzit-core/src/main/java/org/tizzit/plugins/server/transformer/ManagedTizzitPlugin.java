package org.tizzit.plugins.server.transformer;

import org.xml.sax.ContentHandler;

import de.juwimm.cms.beans.WebServiceSpring;
import de.juwimm.cms.plugins.server.TizzitPlugin;
import de.juwimm.cms.vo.ViewComponentValue;

public interface ManagedTizzitPlugin extends TizzitPlugin {

	public void setup(ContentHandler pluginManager, String nameSpace, WebServiceSpring wss, ViewComponentValue viewComponent, boolean liveServer);
}
