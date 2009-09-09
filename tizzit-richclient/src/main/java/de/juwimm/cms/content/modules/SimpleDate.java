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
package de.juwimm.cms.content.modules;

import static de.juwimm.cms.common.Constants.*;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanSimpleDate;
import de.juwimm.util.DateConverter;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class SimpleDate extends AbstractModule {
	private PanSimpleDate pan = new PanSimpleDate();

	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
	}

	public JDialog viewModalUI(boolean modal) {
		DlgModalModule frm = new DlgModalModule(this, pan, 250, 250, modal);
		frm.setVisible(true);
		return frm;
	}

	public JPanel viewPanelUI() {
		return this.pan;
	}

	public boolean isModuleValid() {
		setValidationError(null);
		if (isMandatory() && this.pan.getDateTextField().equals("")) {
			setValidationError(rb.getString("exception.SimpleDateRequired"));
			return false;
		}
		return true;
	}

	public void setProperties(Node node) {
		if (node != null && node.hasChildNodes()) {
			String day = XercesHelper.getNodeValue(node, "./day");
			String month = XercesHelper.getNodeValue(node, "./month");
			String year = XercesHelper.getNodeValue(node, "./year");
			//To see if the day, month or year contains any value or not, if yes set the calendar and label
			//else empty the label
			if (day.trim().length() != 0 && month.trim().length() != 0 && year.trim().length() != 0) {
				Calendar cal = DateConverter.getString2Calendar(day + "." + month + "." + year);
				this.pan.setCalendar(cal);
				this.pan.setDateTextField(cal);
			} else {
				this.pan.setDateTextField((Calendar)null);
			}
		} else {
			//To empty the label when the node is null.
			this.pan.setDateTextField((Calendar)null);
		}
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void load() {
	}

	public Node getProperties() {
		String day = "";
		String month = "";
		String year = "";
		//In case when the dateTextField is not empty, then we will set the node values
		if ((this.pan.getDateTextField().trim()).length() != 0) {
			day = new Integer(this.pan.getCalendar().get(Calendar.DAY_OF_MONTH)).toString();
			month = new Integer(this.pan.getCalendar().get(Calendar.MONTH) + 1).toString();
			year = new Integer(this.pan.getCalendar().get(Calendar.YEAR)).toString();
		}
		Element root = ContentManager.getDomDoc().createElement("date");

		Element eday = ContentManager.getDomDoc().createElement("day");
		Text txtDay = ContentManager.getDomDoc().createTextNode(day);
		eday.appendChild(txtDay);
		root.appendChild(eday);

		Element emonth = ContentManager.getDomDoc().createElement("month");
		Text txtMonth = ContentManager.getDomDoc().createTextNode(month);
		emonth.appendChild(txtMonth);
		root.appendChild(emonth);

		Element eyear = ContentManager.getDomDoc().createElement("year");
		Text txtNode = ContentManager.getDomDoc().createTextNode(year);
		eyear.appendChild(txtNode);
		root.appendChild(eyear);

		return root;
	}

	public String getPaneImage() {
		return "16_datum.gif";
	}

	public String getIconImage() {
		return "16_datum.gif";
	}

	/**
	 * Function to get the string value of the date in the format dd.mm.yyyy
	 * from the calendar
	 */
	public String getDateFromCalendarToString(Calendar cal) {		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		java.util.Date d = cal.getTime();
		return sdf.format(d);
	}

	public void setEnabled(boolean enabling) {
		pan.setDateButtonEnabled(enabling);
	}
	
	public void recycle() {
		pan.setDateButtonEnabled(false);
		pan.setDateTextField((Calendar)null);
	}

}