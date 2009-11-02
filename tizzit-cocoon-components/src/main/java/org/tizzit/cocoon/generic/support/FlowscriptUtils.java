package org.tizzit.cocoon.generic.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * This type allows easy creation of DOM documents providing the document's URL. 
 * In addition, it may be used for extracting the parameters from an iteration.
 * 
 * @author <a href="mailto:christiane.hausleiter@juwimm.com">Christiane Hausleiter</a>
 * @version $Id$
 */
public class FlowscriptUtils {
	private static Logger log = Logger.getLogger(FlowscriptUtils.class);

	/**
	 * Returns a DOM document received from the specified URL. 
	 * Only standard protocols (http, ftp) are supported - the {@code cocoon} protocol cannot be used.
	 * 
	 * @param httpUrl the URL
	 * @return a DOM document
	 */
	public Document getDomDocument(String httpUrl) {
		try {
			new URL(httpUrl);
			String content = this.getHttpContent(httpUrl);
			return XercesHelper.string2Dom(content);

		} catch (MalformedURLException exception) {
			log.error("Invalid URL: " + httpUrl, exception);
		} catch (Exception exception) {
			log.error("Error creating DOM document for URL " + httpUrl, exception);
		}
		return null;
	}

	/**
	 * Returns the extracted key-value pairs from the document's iteration as a HashMap.
	 * The document is supposed to contain the following structure:
	 * <code><br>
	 * &lt;item&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&lt;param-name&gt;parameterName&lt;/param-name&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&lt;param-value&gt;parameterValue&lt;/param-value&gt;<br>
	 * &lt;/item&gt;
	 * 
	 * @param domDocument the DOM document
	 * @return a {@link HashMap} containing the parameter names and its values or an 
	 * empty HashMap if no key-value pairs were found.
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getParamHashmap(Document domDocument) {
		HashMap<String, String> result = new HashMap<String, String>();
		Iterator iterator = XercesHelper.findNodes(domDocument, "//item");
		while (iterator.hasNext()) {
			Node node = (Node) iterator.next();
			result.put(XercesHelper.getNodeValue(node, "param-name"), XercesHelper.getNodeValue(node, "param-value")); //$NON-NLS-2$
		}
		return result;
	}

	/**
	 * Returns the document's content of the specified URL. 
	 * 
	 * @param url the URL to read
	 * @return the content 
	 */
	private String getHttpContent(String url) {
		StringBuffer sb = new StringBuffer();
		HttpClient client = new HttpClient();

		GetMethod method = new GetMethod(url);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				log.warn("Method failed: " + method.getStatusLine());
			}

			InputStream is = method.getResponseBodyAsStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = br.readLine();
			while (line != null) {
				sb.append(line).append("\n");
				line = br.readLine();
			}
			br.close();
			// Use caution: ensure correct character encoding and is not binary data
			if (log.isDebugEnabled()) log.debug("Response: " + sb.toString());

		} catch (HttpException e) {
			log.warn("Fatal protocol violation: " + e.getMessage(), e);
		} catch (IOException e) {
			log.warn("Fatal transport error: " + e.getMessage(), e);
		} catch (Exception e) {
			log.warn("Fatal unknown error: " + e.getMessage(), e);
		} finally {
			method.releaseConnection();
		}
		return sb.toString();
	}

}
