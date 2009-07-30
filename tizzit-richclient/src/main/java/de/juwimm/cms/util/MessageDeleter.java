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

import org.apache.log4j.Logger;

import de.juwimm.cms.gui.PanStatusbar;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author Dirk Bogun
 * @version $Id$
 */
public class MessageDeleter extends Thread {
	private static Logger log = Logger.getLogger(MessageDeleter.class);
	private PanStatusbar panStatusline;
	private long addTime;

	public MessageDeleter(PanStatusbar panel) {
		panStatusline = panel;
	}

	public void addTime() {
		if (addTime == 0) addTime = 5000;
	}

	public void run() {
		long temp;
		try {
			Thread.sleep(5000);

			while (addTime != 0) {
				temp = addTime;
				addTime = 0;
				Thread.sleep(temp);
			}
			panStatusline.removeMessage();
		} catch (InterruptedException exe) {
			log.error("Interrupted the message deleter", exe);
		}
	}

}
