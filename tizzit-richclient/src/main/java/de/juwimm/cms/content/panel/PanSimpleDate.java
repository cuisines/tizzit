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
package de.juwimm.cms.content.panel;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicButtonUI;

import org.apache.log4j.Logger;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDayChooser;

import de.juwimm.cms.Messages;
import de.juwimm.cms.gui.controls.DateChooser;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.ayyappan@juwimm.com">Sabarinath Ayyappan</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PanSimpleDate extends JPanel {
	private static final long serialVersionUID = -8614407644532085680L;
	private Logger log = Logger.getLogger(PanSimpleDate.class);
	private JPanel panMain = new JPanel();
	private JLabel dateLabel = new JLabel();
	private JDateChooser dateChooser = new DateChooser();

	public PanSimpleDate() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}

	void jbInit() throws Exception {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

		dateLabel.setText(Messages.getString("PanSimpleDate.dateButton"));
		
		panMain.setLayout(new GridBagLayout());
		this.add(panMain, null);
		panMain.add(dateLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
		panMain.add(dateChooser, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
	}

	/**
	 * Funtion to set the calendar in the frame
	 * which shows the calendar.
	 * @param txt
	 */
	public void setCalendar(Calendar cal) {
		dateChooser.setCalendar(cal);
	}

	/**
	 * Funtion to get the calendar from the frame
	 * which shows the calendar.
	 * @param txt
	 */
	public Calendar getCalendar() {
		
		if(dateChooser.getDate() == null){
			Calendar emptyCalendar = Calendar.getInstance();
			emptyCalendar.setTimeInMillis(0);
			return emptyCalendar;
		}
		
		return dateChooser.getCalendar();
	}
	public Date getDate() {
		if(dateChooser.getDate() == null){
			return new Date(0);
		}
		return dateChooser.getDate();
	}

	/**
	 * Funtion to set the date in the TextField.
	 * @param txt
	 */
	public void setDateTextField(Calendar calendar) {
		dateChooser.setCalendar(calendar);
	}
	
	public void setDateTextField(Date date) {
		dateChooser.setDate(date);
	}
	
	public void setDateTextField(long time) {
		dateChooser.setDate(new Date(time));
	}
	
	public void setDateTextField(String date){
		if(date == null || date.isEmpty()){
			dateChooser.setDate((Date)null);
			return;
		}
		try {
			dateChooser.setDate(new SimpleDateFormat().parse(date));
		} catch (ParseException e) {			
			e.printStackTrace();
		}
	}

	/**
	 * Funtion to get the date TextField.
	 * @param txt
	 */
	public void addDocumentListener(DocumentListener listener) {
		((JFormattedTextField)this.dateChooser.getComponent(1)).getDocument().addDocumentListener(listener);
	}

	/**
	 * Funtion to get the date in the TextField.
	 * @param txt
	 */
	public String getDateTextField() {
		return new SimpleDateFormat("dd.MM.yyyy").format(dateChooser.getCalendar());
	}	

	/**
	 * Funtion to set the label date button, so that different
	 * application can use different label. For egs: like
	 * Online Date and Offline Date, by default the label will be
	 * Date.
	 * @param txt
	 */
	public void setDateButtonText(String txt) {
		dateLabel.setText(txt);
	}

	public void setDateButtonEnabled(boolean enabled) {
		this.dateChooser.setEnabled(enabled);
	}

}
