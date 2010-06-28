package org.tizzit.plugins.server.transformer.test;

import java.io.InputStream;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.io.XMLWriter;
import org.tizzit.plugins.server.transformer.BaseContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This class helps to debug the Plugin and print the SAX output - not realy a JUnit test!
 * All exceptions are caught!
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id: ConfluenceContentPluginTest.java 453 2009-10-15 05:58:55Z eduard.siebert@online.de $
 * @since tizzit-plugin-sample 15.10.2009
 */
public class ContentTransformerPluginTest extends TestCase {
	private static final Log log = LogFactory.getLog(ContentTransformerPluginTest.class);

	public void testPlugin() throws Exception {
		if (log.isDebugEnabled()) log.debug("testPlugin() -> begin");
		try {

			//ContentTransformerPlugin ctp = new ContentTransformerPlugin();
			//BaseContentHandler transformer = new BaseContentHandler(ctp);

			XMLReader parser = XMLReaderFactory.createXMLReader();

			StringWriter sw = new StringWriter();
			XMLWriter xw = new XMLWriter(sw);

			BaseContentHandler transformer = new BaseContentHandler(xw);

			//			ctp.configurePlugin(null, null, xw, null);

			parser.setContentHandler(transformer);

			InputStream stream = this.getClass().getResourceAsStream("/" + this.getClass().getSimpleName() + ".xml");
			InputSource inputSource = new InputSource(stream);

			long parseTime = System.currentTimeMillis();

			parser.parse(inputSource);

			//transformer.processContent();

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
