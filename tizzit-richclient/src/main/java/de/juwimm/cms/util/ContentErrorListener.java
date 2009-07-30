/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.juwimm.cms.util;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class should only receive error messages from the underlying SaxHandler for returning content.
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class ContentErrorListener implements ErrorHandler, ErrorListener {
	private static Logger log = Logger.getLogger(ContentErrorListener.class);
	private Exception exceptionGot = null;

	/* (non-Javadoc)
	 * @see javax.xml.transform.ErrorListener#error(javax.xml.transform.TransformerException)
	 */
	public void error(TransformerException exception) throws TransformerException {
		if (log.isDebugEnabled()) log.debug("error parsing content", exception);
		exceptionGot = exception;
	}

	/* (non-Javadoc)
	 * @see javax.xml.transform.ErrorListener#fatalError(javax.xml.transform.TransformerException)
	 */
	public void fatalError(TransformerException exception) throws TransformerException {
		log.error("fatal error parsing content", exception);
		exceptionGot = exception;
	}

	/* (non-Javadoc)
	 * @see javax.xml.transform.ErrorListener#warning(javax.xml.transform.TransformerException)
	 */
	public void warning(TransformerException exception) throws TransformerException {
		if (log.isDebugEnabled()) log.debug("", exception);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */
	public void error(SAXParseException exception) throws SAXException {
		if (log.isDebugEnabled()) log.debug("error parsing content", exception);
		exceptionGot = exception;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	public void fatalError(SAXParseException exception) throws SAXException {
		log.error("fatal error parsing content", exception);
		exceptionGot = exception;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */
	public void warning(SAXParseException exception) throws SAXException {
		if (log.isDebugEnabled()) log.debug("", exception);
	}

	/**
	 * @return Returns the exceptionGot.
	 */
	public Exception getExceptionGot() {
		return this.exceptionGot;
	}

}
