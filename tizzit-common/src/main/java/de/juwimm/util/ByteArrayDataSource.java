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
package de.juwimm.util;

import javax.activation.DataSource; 
import java.io.*;

/***
 * This class implements a typed DataSource from:<br>
 *
 * - an InputStream<br>
 * - a byte array<br>
 * - a String<br>
 *
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @version $Id$
 */
public class ByteArrayDataSource implements DataSource {
	/*** Data. */
	private byte[] data;
	/*** Content-type. */
	private String type;
	private ByteArrayOutputStream baos;

	/***
	 * Create a datasource from a byte array.
	 *
	 * @param data A byte[].
	 * @param type A String.
	 */
	public ByteArrayDataSource(byte[] data, String type) {
		this.data = data;
		this.type = type;
    }

	/***
	 * Create a datasource from an input stream.
	 *
	 * @param is An InputStream.
	 * @param type A String.
	 */
	public ByteArrayDataSource(InputStream is, String type) {
		this.type = type;
		try {
			int ch;
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			BufferedInputStream isReader = new BufferedInputStream(is);
			BufferedOutputStream osWriter = new BufferedOutputStream(os);
			while ((ch = isReader.read()) != -1) {
				osWriter.write(ch);
			}
			data = os.toByteArray();
		} catch (IOException ioex) {
			// Do something!
		}
	}

	/***
	 * Create a datasource from a String.
	 *
	 * @param data A String.
	 * @param type A String.
	 */
	public ByteArrayDataSource(String data, String type) {
		this.type = type;
		try {
			// Assumption that the string contains only ASCII
			// characters!  Else just pass in a charset into this
			// constructor and use it in getBytes().
			this.data = data.getBytes("iso-8859-1");
		} catch (UnsupportedEncodingException uex) {
			// Do something!
		}
	}

	/***
	 * Get the content type.
	 *
	 * @return A String.
	 */
	public String getContentType() {
		return (type == null) ? "application/octet-stream" : type;
	}

	/***
	 * Get the input stream.
	 *
     * t<br>fdfdsfd</b>
     *
	 * @return An InputStream.
	 * @exception IOException.
     * @deprecated Is shit
     *
	 */
	public InputStream getInputStream() throws IOException {
		if (data == null) throw new IOException("no data");
		return new ByteArrayInputStream(data);
	}

	/***
	 * Get the name.
	 *
	 * @return A String.
	 */
	public String getName() {
		return "ByteArrayDataSource";
	}

	/***
	 * Get the output stream.
	 *
	 * @return An OutputStream.
	 * @exception IOException.
	 */
	public OutputStream getOutputStream() throws IOException {
		baos = new ByteArrayOutputStream();
		return baos;
	}
}