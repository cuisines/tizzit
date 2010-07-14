package org.tizzit.cocoon.transformation.test;

import java.io.InputStream;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.io.XMLWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.rcp.RemoteAuthenticationManager;
import org.tizzit.cocoon.transformation.BaseContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.juwimm.cms.beans.WebServiceSpring;

/**
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id: ContentTransformerPluginTest.java 759 2010-05-05 13:34:28Z rene.hertzfeldt $
 */
public class ContentTransformerPluginTest extends TestCase {
	private static final Log log = LogFactory.getLog(ContentTransformerPluginTest.class);
	
	protected WebServiceSpring webServiceSpring;

	public void testPlugin() throws Exception {
		if (log.isDebugEnabled()) log.debug("testPlugin() -> begin");
		try {

			//ContentTransformerPlugin ctp = new ContentTransformerPlugin();
			//BaseContentHandler transformer = new BaseContentHandler(ctp);

			XMLReader parser = XMLReaderFactory.createXMLReader();

			StringWriter sw = new StringWriter();
			XMLWriter xw = new XMLWriter(sw);

			BaseContentHandler transformer = new BaseContentHandler(xw);
			log.info("----------------> webserviceSpring: " + webServiceSpring);
			
			ApplicationContext atx = new ClassPathXmlApplicationContext("/applicationContext.xml");
			ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-transformerPlugin.xml");
			log.info("----------------> ApplicationContext: "+ctx);
			WebServiceSpring directWebServiceSpring = (WebServiceSpring) ctx.getBean("webServiceSpring");
			
			log.info("----------------> directWebServiceSpring: " + directWebServiceSpring);
			
			//assertNotNull("webService not found... ", webServiceSpring);

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
	
	/**
	 * @param webServiceSpring the webServiceSpring to set
	 */
	public void setWebServiceSpring(WebServiceSpring webServiceSpring) {
		this.webServiceSpring = webServiceSpring;
	}

}
