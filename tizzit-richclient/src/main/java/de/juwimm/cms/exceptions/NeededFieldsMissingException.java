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
package de.juwimm.cms.exceptions;

import java.util.ResourceBundle;

import de.juwimm.cms.common.Constants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class NeededFieldsMissingException extends Exception {
	private static ResourceBundle rb = ResourceBundle.getBundle("FIELDS", Constants.CMS_LOCALE);
	private String[] missingFields = null;

	/**
	 * 
	 */
	public NeededFieldsMissingException() {
		super();
	}

	/**
	 * @param message
	 */
	public NeededFieldsMissingException(String message) {
		super(message);
	}

	/**
	 * @return Returns the missingFields.
	 */
	public String[] getMissingFields() {
		return missingFields;
	}

	/**
	 * @return Returns the missingFields.
	 */
	public String getMissingFieldsLocaleString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < missingFields.length; i++) {
			if (i != 0) sb.append(", ");
			sb.append(rb.getString(missingFields[i]));
		}
		return sb.toString();
	}

	/**
	 * @param missingFields The missingFields to set.
	 */
	public void setMissingFields(String[] missingFields) {
		this.missingFields = missingFields;
	}

	/**
	 * @param missingFieldsCSV The missingFields as comma seperated values to set.
	 */
	public void setMissingFields(String missingFieldsCSV) {
		missingFields = missingFieldsCSV.split(",");
	}
}