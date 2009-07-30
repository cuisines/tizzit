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
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import de.juwimm.cms.Messages;
import de.juwimm.cms.content.frame.DlgSimpleDate;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.util.DateConverter;

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
	private Logger log = Logger.getLogger(PanSimpleDate.class);
	private DlgSimpleDate frmSimpleDate = null;
	private JPanel panMain = new JPanel();
	private JButton dateButton = new JButton();
	private JTextField dateDisplayLabel = new JTextField();

	public PanSimpleDate() {
		try {
			jbInit();
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}

	void jbInit() throws Exception {
		frmSimpleDate = new DlgSimpleDate(this);
		frmSimpleDate.setLocationRelativeTo(UIConstants.getMainFrame());
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

		dateButton.setText(Messages.getString("PanSimpleDate.dateButton"));
		dateButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateActionPerformed(e);
			}
		});
		panMain.setLayout(new GridBagLayout());
		dateDisplayLabel.setEnabled(true);
		dateDisplayLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		dateDisplayLabel.setMinimumSize(new Dimension(70, 25));
		dateDisplayLabel.setPreferredSize(new Dimension(70, 25));
		dateDisplayLabel.setToolTipText("");
		dateDisplayLabel.setEditable(false);
		dateDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(panMain, null);
		panMain.add(dateButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
		panMain.add(dateDisplayLabel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
	}

	private void dateActionPerformed(ActionEvent e) {
		if ((dateDisplayLabel.getText().trim()).length() != 0) {
			Calendar cal = DateConverter.getString2Calendar(dateDisplayLabel.getText());
			setCalendar(cal);
		}
		frmSimpleDate.setVisible(true);
	}

	/**
	 * Funtion to set the calendar in the frame
	 * which shows the calendar.
	 * @param txt
	 */
	public void setCalendar(Calendar cal) {
		frmSimpleDate.setCalendar(cal);
	}

	/**
	 * Funtion to get the calendar from the frame
	 * which shows the calendar.
	 * @param txt
	 */
	public Calendar getCalendar() {
		return frmSimpleDate.getCalendar();
	}

	/**
	 * Funtion to set the date in the TextField.
	 * @param txt
	 */
	public void setDateTextField(String txt) {
		dateDisplayLabel.setText(txt);
	}

	/**
	 * Funtion to get the date TextField.
	 * @param txt
	 */
	public void addDocumentListener(DocumentListener listener) {
		this.dateDisplayLabel.getDocument().addDocumentListener(listener);
	}

	/**
	 * Funtion to get the date in the TextField.
	 * @param txt
	 */
	public String getDateTextField() {
		return dateDisplayLabel.getText();
	}

	public Date getSelectedDate() {
		return this.frmSimpleDate.getCalendar().getTime();
	}

	/**
	 * Funtion to set the label date button, so that different
	 * application can use different label. For egs: like
	 * Online Date and Offline Date, by default the label will be
	 * Date.
	 * @param txt
	 */
	public void setDateButtonText(String txt) {
		dateButton.setText(txt);
	}

	public void setDateButtonEnabled(boolean enabled) {
		this.dateButton.setEnabled(enabled);
	}

	public void showDialog() {
		if ((dateDisplayLabel.getText().trim()).length() != 0) {
			Calendar cal = DateConverter.getString2Calendar(dateDisplayLabel.getText());
			setCalendar(cal);
		}
		frmSimpleDate.setVisible(true);
	}

}
