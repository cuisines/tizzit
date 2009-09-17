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

class DateChooser extends JDateChooser{
	private static final long serialVersionUID = 3122411689996068892L;

	DateChooser(){
		super(new CalendarChooser(), null,null, null);
		this.calendarButton.setIcon(UIConstants.ICON_CALENDAR);
	}
}

class CalendarChooser extends JCalendar{
	private static final long serialVersionUID = -3814194514540073558L;
	CalendarChooser(){
		this(null, null, true, true);
	}
	public CalendarChooser(Date date, Locale locale, boolean monthSpinner, boolean weekOfYearVisible) {
		super(date, locale, monthSpinner, weekOfYearVisible);
		// needed for setFont() etc.
		remove(dayChooser);		
		dayChooser = new DayChooser(weekOfYearVisible);
		dayChooser.addPropertyChangeListener(this);
		add(dayChooser, BorderLayout.CENTER);
		monthChooser.setDayChooser(dayChooser);
		yearChooser.setDayChooser(dayChooser);
	}
	
}

class DayChooser extends JDayChooser{
	private static final long serialVersionUID = 8762184809200937023L;
	public DayChooser(boolean weekOfYearVisible){
		setName("JDayChooser");
		setBackground(Color.blue);
		this.weekOfYearVisible = weekOfYearVisible;
		locale = Locale.getDefault();
		days = new JButton[49];
		selectedDay = null;
		calendar = Calendar.getInstance(locale);
		today = (Calendar) calendar.clone();

		setLayout(new BorderLayout());

		dayPanel = new JPanel();
		dayPanel.setLayout(new GridLayout(7, 7));

		for (int y = 0; y < 7; y++) {
			for (int x = 0; x < 7; x++) {
				int index = x + (7 * y);
				if (y == 0) {			
					days[index] = new PlainButton(PlainButtonType.Day);
				} else {
					days[index] = new PlainButton(PlainButtonType.SelectionDay);
					days[index].addActionListener(this);
					days[index].addKeyListener(this);
					days[index].addFocusListener(this);
				}

				days[index].setMargin(new Insets(2, 2, 2, 2));							
				dayPanel.add(days[index]);
			}
		}

		weekPanel = new JPanel();
		weekPanel.setLayout(new GridLayout(7, 1));
		weeks = new JButton[7];

		for (int i = 0; i < 7; i++) {
			if(i ==0 ){
				weeks[i] = new PlainButton(PlainButtonType.Day);
			}else{
				weeks[i] = new PlainButton(PlainButtonType.Week);
			}
			
			weeks[i].setMargin(new Insets(2, 2, 2, 2));
			weeks[i].setFocusPainted(false);
			weeks[i].setForeground(new Color(100, 100, 100));

			if (i != 0) {
				weeks[i].setText("0" + (i + 1));
			}

			weekPanel.add(weeks[i]);
		}

		Calendar tmpCalendar = Calendar.getInstance();
		tmpCalendar.set(1, 0, 1, 1, 1);
		defaultMinSelectableDate = tmpCalendar.getTime();
		minSelectableDate = defaultMinSelectableDate;
		tmpCalendar.set(9999, 0, 1, 1, 1);
		defaultMaxSelectableDate = tmpCalendar.getTime();
		maxSelectableDate = defaultMaxSelectableDate;

		init();

		setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		add(dayPanel, BorderLayout.CENTER);

		if (weekOfYearVisible) {
			add(weekPanel, BorderLayout.WEST);
		}
		initialized = true;
		updateUI();

		this.weekPanel.setOpaque(true);
		this.selectedColor = Color.white;
		this.dayPanel.setBackground(new Color(187,187,187));
		this.decorationBackgroundColor = new Color(92, 92, 92);
		
	}
	
	@Override
	public void setDayBordersVisible(boolean dayBordersVisible) {
	
	}
	
	public void setFont(Font font) {
		if (days != null) {
			for (int i = 0; i < 49; i++) {
				days[i].setFont(font);
			}
		}
		Font newItalicButtonFont=new Font(font.getName(),Font.ITALIC,font.getSize());
		if (weeks != null) {
			for (int i = 0; i < 7; i++) {				  
				weeks[i].setFont(newItalicButtonFont);
			}
		}
	}
	
	enum PlainButtonType {
		SelectionDay,Day,Week
	}
	
	class PlainButton extends JButton{
		private static final long serialVersionUID = -7433645992591669725L;

		PlainButton(PlainButtonType type){			
			super();
			this.setBorderPainted(false);
			switch(type){
				case SelectionDay:
					this.ui = new PlainButtonUI(new Color(184,184,184),Color.white);
					break;
				case Day:
					this.ui = new PlainButtonUI(Color.white,null);
					break;
				case Week:
					this.setForeground(new Color(110,110,110));
					this.ui = new PlainButtonUI(new Color(219,219,219),null);					
			}
			
		}		
	}
	
	class PlainButtonUI extends BasicButtonUI{
		Color selectedColor = null;
		Color noSelectionColor = null;
		
		PlainButtonUI(Color noSelectionColor,Color selectedColor){
			this.selectedColor=selectedColor;
			this.noSelectionColor=noSelectionColor;
		}
		@Override
		public void paint(Graphics g, JComponent c) {
			if (selectedColor != null && (selectedDay.equals(c) || ((JButton)c).getModel().isRollover())) {
				c.setBackground(selectedColor);
			}else{
				c.setBackground(noSelectionColor);
			}
			super.paint(g, c);
		}
		@Override
		protected void paintButtonPressed(Graphics g, AbstractButton b) {
			if (selectedColor != null){
				b.setBackground(selectedColor);
			}else{
				b.setBackground(noSelectionColor);
			}
			super.paintButtonPressed(g, b);
		}		
	}	
}