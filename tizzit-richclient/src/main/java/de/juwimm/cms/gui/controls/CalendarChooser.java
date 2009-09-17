package de.juwimm.cms.gui.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicButtonUI;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;


/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id
 */
public class CalendarChooser extends JCalendar{
	private static final long serialVersionUID = -3814194514540073558L;
	public CalendarChooser(){
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
