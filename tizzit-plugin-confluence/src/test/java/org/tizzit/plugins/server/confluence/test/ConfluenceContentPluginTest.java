package org.tizzit.plugins.server.confluence.test;

import java.io.InputStream;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.io.XMLWriter;
import org.tizzit.plugins.server.confluence.ConfluenceContentPlugin;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This class helps to debug the Plugin and print the SAX output - not realy a JUnit test!
 * All exceptions are caught!
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-plugin-sample 15.10.2009
 */
public class ConfluenceContentPluginTest extends TestCase {
	private static final Log log = LogFactory.getLog(ConfluenceContentPluginTest.class);

	public void testPlugin() throws Exception {
		if (log.isDebugEnabled()) log.debug("testPlugin() -> begin");
		try {

			ConfluenceContentPlugin transformer = new ConfluenceContentPlugin();

			XMLReader parser = XMLReaderFactory.createXMLReader();

			StringWriter sw = new StringWriter();
			XMLWriter xw = new XMLWriter(sw);

			transformer.configurePlugin(null, null, xw, null);
			//transformer.setContentHandler(xw);

			parser.setContentHandler(transformer);

			InputStream stream = this.getClass().getResourceAsStream("/" + this.getClass().getSimpleName() + ".xml");
			InputSource inputSource = new InputSource(stream);

			long parseTime = System.currentTimeMillis();

			parser.parse(inputSource);

			transformer.processContent();

			parseTime = System.currentTimeMillis() - parseTime;
			log.debug("Parse time: " + parseTime + "ms");

			String xmlResult = sw.toString();
			//assertNotNull(xmlResult);
			//assertFalse("".equals(xmlResult));

			log.info("XML Result: \n" + xmlResult);
			if (log.isDebugEnabled()) log.debug("testPlugin() -> end");
		} catch (Exception exe) {
			if (log.isDebugEnabled()) log.debug(exe.getMessage(), exe);
		}
	}
}
