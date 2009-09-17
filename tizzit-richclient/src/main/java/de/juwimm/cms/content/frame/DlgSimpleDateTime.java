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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.JSpinner.DateEditor;

import org.apache.log4j.Logger;

import com.toedter.calendar.JCalendar;

import de.juwimm.cms.gui.controls.CalendarChooser;
import de.juwimm.cms.util.UIConstants;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class DlgSimpleDateTime extends JDialog {
	private static Logger log = Logger.getLogger(DlgSimpleDateTime.class);
	private JCalendar cal = null;
	private Date dateOnStart = null;
	private JPanel panButtons = new JPanel();
	private JButton btnOk = new JButton();
	private JButton btnCancel = new JButton();
	private JButton btnClear = new JButton();
	private JSpinner spTime = null;
	private SpinnerDateModel timeModel = null;
	private String timeFormat = "HH:mm:ss";
	private ArrayList<ActionListener> okListenerList = new ArrayList<ActionListener>();

	public DlgSimpleDateTime() {
		super(UIConstants.getMainFrame(), true);
		super.setTitle(rb.getString("frame.simpledate.title"));
		this.cal = new CalendarChooser();
		try {
			this.jbInit();
			this.getRootPane().setDefaultButton(this.btnOk);
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(new GridBagLayout());
		this.cal.setMaximumSize(new Dimension(180, 180));
		this.cal.setMinimumSize(new Dimension(180, 180));
		this.cal.setPreferredSize(new Dimension(180, 180));
		this.panButtons.setLayout(new GridBagLayout());
		this.btnOk.setText(rb.getString("dialog.ok"));
		this.btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				doOkButtonAction(evt);
			}
		});
		this.btnCancel.setText(rb.getString("dialog.cancel"));
		this.btnCancel.addActionListener(new ActionListener() {
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
		this.timeModel = new SpinnerDateModel();
		this.spTime = new JSpinner(timeModel);
		DateEditor nEdit = new JSpinner.DateEditor(spTime, this.timeFormat);
		spTime.setEditor(nEdit);

		panButtons.add(btnOk, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		panButtons.add(btnClear, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		panButtons.add(btnCancel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		this.getContentPane().add(this.cal, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(10, 7, 0, 6), 141, 92));
		this.getContentPane().add(this.spTime, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
		this.getContentPane().add(this.panButtons, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 292, 20));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				doCancelButtonAction();
			}
		});
		try {
			pack();
		} catch (Exception exe) {
		}
		this.setSize(320, 275);
		this.setLocationRelativeTo(UIConstants.getMainFrame());
	}

	private Calendar getCalendar() {
		return this.cal.getCalendar();
	}

	public void setDate(Date date) {
		if (date != null) {
			this.getCalendar().setTime(date);
			Calendar c = this.getCalendar();
			c.setTime(date);
			this.cal.setCalendar(c);
			this.dateOnStart = date;
			this.spTime.setValue(date);
		} else {
			this.getCalendar().clear();
		}
	}

	public Date getDate() {
		Calendar c = this.getCalendar();
		if (c.isSet(Calendar.YEAR))
			return c.getTime();
		return null;
	}

	private void doOkButtonAction(ActionEvent evt) {
		Date spDate = (Date) spTime.getValue();
		Calendar cal = this.getCalendar();
		Calendar calcCal = Calendar.getInstance();
		calcCal.setTime(spDate);
		cal.set(Calendar.HOUR_OF_DAY, calcCal.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, calcCal.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, calcCal.get(Calendar.SECOND));
		this.dateOnStart = this.getDate();
		Iterator<ActionListener> it = this.okListenerList.iterator();
		while (it.hasNext()) {
			ActionListener al = (ActionListener) it.next();
			al.actionPerformed(evt);
		}
		this.setVisible(false);
	}

	private void doCancelButtonAction() {
		this.setDate(this.dateOnStart);
		this.setVisible(false);
	}

	private void doClearButtonAction() {
		this.setDate(null);
		this.setVisible(false);
	}

	public void setDatePurgeable(boolean purgeable) {
		this.btnClear.setVisible(purgeable);
	}

	public void addOkListner(ActionListener e) {
		this.okListenerList.add(e);
	}

}
