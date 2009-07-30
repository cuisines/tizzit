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
package de.juwimm.cms.content.frame;

import static de.juwimm.cms.common.Constants.*;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.toedter.calendar.JCalendar;

import de.juwimm.cms.content.panel.PanSimpleDate;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.util.DateConverter;

/**
 * 
 * @author <a href="mailto:s.ayyappan@juwimm.com">Sabarinath Ayyappan </a>
 * @version $Id$
 */
public class DlgSimpleDate extends JDialog {
	private static Logger log = Logger.getLogger(DlgSimpleDate.class);
	private JCalendar cal = null;
	private Calendar calBeforeUserSelectionChange = null;
	private String day = "";
	private String month = "";
	private String year = "";
	private PanSimpleDate panSimpleDate = null;
	private JPanel panButtons = new JPanel();
	private JButton btnOk = new JButton();
	private JButton btnCancel = new JButton();
	private JButton btnClear = new JButton();

	public DlgSimpleDate() {
		super(UIConstants.getMainFrame(), true);
		setTitle(rb.getString("frame.simpledate.title"));
		cal = new JCalendar();
		try {
			jbInit();
			this.getRootPane().setDefaultButton(btnOk);
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	public DlgSimpleDate(PanSimpleDate panSimpleDate) {
		this();
		this.panSimpleDate = panSimpleDate;
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(new GridBagLayout());
		day = new Integer(cal.getCalendar().get(Calendar.DAY_OF_MONTH)).toString();
		month = new Integer(cal.getCalendar().get(Calendar.MONTH) + 1).toString();
		year = new Integer(cal.getCalendar().get(Calendar.YEAR)).toString();

		cal.setMaximumSize(new Dimension(180, 180));
		cal.setMinimumSize(new Dimension(180, 180));
		cal.setPreferredSize(new Dimension(180, 180));
		panButtons.setLayout(new GridBagLayout());
		btnOk.setText(rb.getString("dialog.ok"));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				doOkButtonAction();
			}
		});
		btnCancel.setText(rb.getString("dialog.cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				doCancelButtonAction();
			}
		});
		btnClear.setText(rb.getString("frame.simpledate.emptyDate"));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				doClearButtonAction();
			}
		});

		this.getContentPane().add(cal, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(10, 7, 0, 6), 141, 92));
		this.getContentPane().add(panButtons, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 292, 20));
		panButtons.add(btnOk, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		panButtons.add(btnClear, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		panButtons.add(btnCancel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				doCancelButtonAction();
			}

			public void windowActivated(WindowEvent evt) {
				doWindowActivatedAction();
			}
		});
		try {
			pack();
		} catch (Exception exe) {
		}

		this.setSize(320, 275);
		this.setLocationRelativeTo(UIConstants.getMainFrame());
	}

	/**
	 * Funtion to set the calendar.
	 * 
	 * @param txt
	 */
	public void setCalendar(Calendar calendar) {
		this.cal.setCalendar(calendar);
	}

	/**
	 * Funtion to get the calendar.
	 * 
	 * @param txt
	 */
	public Calendar getCalendar() {
		return this.cal.getCalendar();
	}

	/**
	 * Funtion to get String format of a date from the JCalendar.
	 * 
	 * @param txt
	 */
	public String getDateFormatedString(JCalendar jCalendar) {
		day = new Integer(jCalendar.getCalendar().get(Calendar.DAY_OF_MONTH)).toString();
		month = new Integer(jCalendar.getCalendar().get(Calendar.MONTH) + 1).toString();
		year = new Integer(jCalendar.getCalendar().get(Calendar.YEAR)).toString();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		java.util.Date d = sdf.parse(day + "." + month + "." + year, new ParsePosition(0));
		return sdf.format(d);
	}

	private void doOkButtonAction() {
		calBeforeUserSelectionChange = cal.getCalendar();
		panSimpleDate.setDateTextField(getDateFormatedString(cal));
		setVisible(false);
	}

	private void doCancelButtonAction() {
		cal.setCalendar(calBeforeUserSelectionChange);
		if (panSimpleDate.getDateTextField().trim().length() != 0) {
			panSimpleDate.setDateTextField(getDateFormatedString(cal));
		} else {
			panSimpleDate.setDateTextField("");
		}
		setVisible(false);
	}

	private void doClearButtonAction() {
		this.panSimpleDate.setDateTextField("");
		super.setVisible(false);
	}

	/**
	 * Funtion which performs the action when window is activated, which is necessary to store the calendar so that we
	 * can retain the calendar when the cancel button is pressed.
	 * 
	 * @param txt
	 */
	public void doWindowActivatedAction() {
		if (panSimpleDate.getDateTextField().trim().length() != 0) {
			Calendar calFromTextField = DateConverter.getString2Calendar(panSimpleDate.getDateTextField());
			calBeforeUserSelectionChange = calFromTextField;
		} else {
			calBeforeUserSelectionChange = cal.getCalendar();
		}
	}

}
