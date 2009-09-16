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
/*
 * Created on 22.02.2005
 */
package de.juwimm.cms.content.panel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.JSpinner.DateEditor;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import de.juwimm.cms.content.ContentManager;

/**
 * Panel for module for choosing a point in time in hours, minutes and seconds.
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PanTimePicker extends JPanel {
	private static Logger log = Logger.getLogger(PanTimePicker.class);
	private JSpinner spTime = null;
	private SpinnerDateModel timeModel = null;
	private String timeFormat = "HH:mm";

	public PanTimePicker(String timeFormat) {
		super();
		this.timeFormat = timeFormat;
		this.initialize();
	}

	private void initialize() {
		this.setLayout(new FlowLayout());
		this.setSize(348, 100);
		this.timeModel = new SpinnerDateModel();
		this.spTime = new JSpinner(timeModel);
		DateEditor nEdit = new JSpinner.DateEditor(spTime, this.timeFormat);
		spTime.setEditor(nEdit);
		this.spTime.setPreferredSize(new Dimension(this.spTime.getPreferredSize().width + 2, this.spTime.getPreferredSize().height + 2));
		this.add(spTime);
	}

	/*
	 * This method is called on loading the page
	 * 
	 * @param nde The content of the page
	 */
	public void setProperties(Node nde) {
		if (nde != null) {
			try {
				String hour = "0";
				String minute = "0";
				String second = "0";
				Element elmHour = (Element) XercesHelper.findNode(nde, "./hour");
				if (elmHour != null) {
					hour = elmHour.getFirstChild().getNodeValue();
					if (hour.startsWith("0")) {
						hour = hour.substring(1);
					}
				}
				Element elmMinute = (Element) XercesHelper.findNode(nde, "./minute");
				if (elmMinute != null) {
					minute = elmMinute.getFirstChild().getNodeValue();
					if (minute.startsWith("0")) {
						minute = minute.substring(1);
					}
				}
				Element elmSecond = (Element) XercesHelper.findNode(nde, "./second");
				if (elmSecond != null) {
					second = elmSecond.getFirstChild().getNodeValue();
					if (second.startsWith("0")) {
						second = second.substring(1);
					}
				}
				Date date = (Date) spTime.getValue();
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
				cal.set(Calendar.MINUTE, Integer.parseInt(minute));
				cal.set(Calendar.SECOND, Integer.parseInt(second));
				date = cal.getTime();
				spTime.setValue(date);
			} catch (Exception e) {
				log.error("Error setting properties in TimePicker" + e.getMessage());
			}
		}
	}

	/**
	 * This method is called on saving the page
	 * 
	 * @return Document Node with following syntax:<br>
	 * <pre>
	 * &lt;time dcfname="time" label="Uhrzeit"&gt;
	 * 	&lt;hour&gt;hh&lt;/hour&gt;
	 * 	&lt;minute&gt;mm&lt;/minute&gt;
	 * &lt;/time&gt;
	 * </pre>
	 */
	public Node getProperties() {
		Document doc = ContentManager.getDomDoc();
		Element elmRoot = doc.createElement("time");
		Element elmHour = doc.createElement("hour");
		Element elmMinute = doc.createElement("minute");
		Element elmSecond = doc.createElement("second");
		Date date = (Date) spTime.getValue();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String hour = Integer.toString(cal.get(java.util.Calendar.HOUR_OF_DAY));
		String minute = Integer.toString(cal.get(java.util.Calendar.MINUTE));
		String second = Integer.toString(cal.get(java.util.Calendar.SECOND));
		if (hour.length() == 1) {
			hour = "0" + hour;
		}
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		if (second.length() == 1) {
			second = "0" + second;
		}
		Text hourData = doc.createTextNode(hour);
		elmHour.appendChild(hourData);
		Text minuteData = doc.createTextNode(minute);
		elmMinute.appendChild(minuteData);
		Text secondData = doc.createTextNode(second);
		elmSecond.appendChild(secondData);
		elmRoot.appendChild(elmHour);
		elmRoot.appendChild(elmMinute);
		elmRoot.appendChild(elmSecond);
		
		return (elmRoot);
	}

	/*
	 * This method set the state of the current page
	 * Thestate of some subelement is set, too
	 * @see java.awt.Component#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabling) {
		this.spTime.setEnabled(enabling);
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
