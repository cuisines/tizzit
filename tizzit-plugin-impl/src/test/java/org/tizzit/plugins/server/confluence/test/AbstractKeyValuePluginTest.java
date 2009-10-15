package org.tizzit.plugins.server.confluence.test;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.io.XMLWriter;
import org.tizzit.plugins.server.impl.AbstractKeyValuePlugin;
import org.tizzit.util.xml.SAXHelper;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import de.juwimm.cms.plugins.Constants;

/**
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-plugin-sample 08.10.2009
 */
public class AbstractKeyValuePluginTest extends TestCase {
	private static final Log log = LogFactory.getLog(AbstractKeyValuePluginTest.class);

	public void testPlugin() throws Exception {
		if (log.isDebugEnabled()) log.debug("testPlugin() -> begin");
		KeyValuePluginTestImpl transformer = new KeyValuePluginTestImpl();

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

		assertNull(transformer.getConfiguration().get("testPlugin"));
		assertEquals("TestValue1", transformer.getConfiguration().get("TestKey1"));
		assertEquals("TestValue2", transformer.getConfiguration().get("TestKey2"));
		assertEquals("TestValue3", transformer.getConfiguration().get("TestKey3"));
		assertNull(transformer.getConfiguration().get("TestKey4"));
		assertEquals("TestValue5", transformer.getConfiguration().get("TestKey5"));
		assertEquals("TestValue6", transformer.getConfiguration().get("TestKey6"));
		assertEquals("TestValue7", transformer.getConfiguration().get("TestKey7"));
		// TODO: assertEquals("TestValue8", transformer.getConfiguration().get("TestKey8"));
		assertEquals("TestValue9", transformer.getConfiguration().get("TestKey9"));

		//		transformer.testNamespace = Constants.PLUGIN_NAMESPACE + "Test-2";
		//		transformer.processContent();
		//
		//		assertEquals("Test2Value1", transformer.getConfiguration().get("Test2Key1"));
		//		assertEquals("Test2Value2", transformer.getConfiguration().get("Test2Key2"));
		//		assertEquals("Test2Value3", transformer.getConfiguration().get("Test2Key3"));
		//		assertNull(transformer.getConfiguration().get("Test2Key4"));
		//		assertEquals("Test2Value5", transformer.getConfiguration().get("Test2Key5"));
		//		assertEquals("Test2Value6", transformer.getConfiguration().get("Test2Key6"));
		//		assertEquals("Test2Value7", transformer.getConfiguration().get("Test2Key7"));
		//		// TODO: assertEquals("Test2Value8", transformer.getConfiguration().get("Test2Key8"));
		//		assertEquals("Test2Value9", transformer.getConfiguration().get("Test2Key9"));

		parseTime = System.currentTimeMillis() - parseTime;
		log.debug("Parse time: " + parseTime + "ms");

		String xmlResult = sw.toString();
		assertNotNull(xmlResult);
		assertFalse("".equals(xmlResult));

		log.info("XML Result: \n" + xmlResult);
		if (log.isDebugEnabled()) log.debug("testPlugin() -> end");
	}

	private class KeyValuePluginTestImpl extends AbstractKeyValuePlugin {
		public String testNamespace = null;

		@Override
		public String configurePluginNamespaceUri() {
			testNamespace = Constants.PLUGIN_NAMESPACE + "Test-1";
			return testNamespace;
		}

		@Override
		public Date getLastModifiedDate() {
			return null;
		}

		@Override
		public boolean isCacheable() {
			return false;
		}

		@Override
		public void processContent() {

			final AttributesImpl emptyAttribs = new AttributesImpl();
			try {

				this.parent.startElement("", "testResult", "testResult", emptyAttribs);
				for (String key : getConfiguration().keySet()) {
					SAXHelper.addElement(this.parent, key, getConfiguration().get(key));
				}
				this.parent.endElement("", "testResult", "testResult");

			} catch (Exception exe) {
				log.error(exe.getMessage(), exe);
			}

			if (log.isDebugEnabled()) {
				StringWriter sw = new StringWriter();
				sw.append("\nResult: {\n");
				for (String key : getConfiguration().keySet()) {
					sw.append("\t" + key + " = " + getConfiguration().get(key) + "\n");
				}
				sw.append("}");
				log.debug(sw.toString());
			}
		}
	}
}
