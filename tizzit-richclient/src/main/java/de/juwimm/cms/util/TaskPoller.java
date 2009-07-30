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
package de.juwimm.cms.util;

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.event.ActionEvent;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;

/**
 * <p>Title: ConQuest </p>
 * <p>Description: Content Management System</p>
 * <p>Copyright: Copyright (c) 2002, 2003</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class TaskPoller extends TimerTask {
	private static Logger log = Logger.getLogger(TaskPoller.class);

	public void run() {
		Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
		if (comm.isLoggedIn()) {
			try {
				int showTask = 0;
				if (comm.isNewTask4User() && !UIConstants.isNewTask()
						&& Constants.CMS_CLIENT_VIEW == Constants.CLIENT_VIEW_CONTENT) {
					int retVal = JOptionPane.showConfirmDialog(UIConstants.getMainFrame(),
							rb.getString("msgbox.taskPoller.newTask"), rb.getString("dialog.title"),
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

					if (retVal == JOptionPane.YES_OPTION) {
						showTask = 1;
					}
				}
				ActionHub.fireActionPerformed(new ActionEvent(this, showTask, Constants.ACTION_NEW_TASK_FOR_USER));
				// call here msg-poller for the JMS Bean
			} catch (RuntimeException af) {
				log.error("Error polling tasks", af);
			} catch (Exception ex) {
				UIConstants.setStatusInfo("TaskPoller:" + ex.getMessage());
			}
		} else {
			if (log.isDebugEnabled()) log.debug("Task called, but user isnt logged in - ignoring");
		}
	}

}
