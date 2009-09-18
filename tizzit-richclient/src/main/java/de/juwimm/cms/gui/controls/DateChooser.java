package de.juwimm.cms.gui.controls;

import com.toedter.calendar.JDateChooser;

import static de.juwimm.cms.common.Constants.rb;
import de.juwimm.cms.util.UIConstants;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class DateChooser extends JDateChooser{
	private static final long serialVersionUID = 3122411689996068892L;

	public DateChooser(){
		super(new CalendarChooser(), null,rb.getString("dateChooser.format"), null);
		this.calendarButton.setIcon(UIConstants.ICON_CALENDAR);
	}
}
