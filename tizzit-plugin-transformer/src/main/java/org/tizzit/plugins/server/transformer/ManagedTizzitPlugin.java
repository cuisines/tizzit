package org.tizzit.plugins.server.transformer;

import org.xml.sax.ContentHandler;

import de.juwimm.cms.plugins.server.TizzitPlugin;

public interface ManagedTizzitPlugin extends TizzitPlugin {

	public void setup(ContentHandler pluginManager, String nameSpace);
}
