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
package de.juwimm.cms.deploy.actions;

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.Cursor;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2002, 2003</p>
 * <p>Company: JuwiMacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class ExportFullThread extends Thread {
	private static Logger log = Logger.getLogger(ExportFullThread.class);
	private int rootVCid = 0;
	private String fileSuffix = ".xml.gz";

	public ExportFullThread() {
		this.setName("ExportSiteThread");
		fileSuffix = ".site.xml.gz";
	}

	public ExportFullThread(int rootVCid) {
		this.setName("ExportUnitThread");
		this.rootVCid = rootVCid;
		fileSuffix = ".unit.xml.gz";
	}

	public void run() {
		UIConstants.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileFilter() {
			public boolean accept(File fle) {
				return (fle.getName().endsWith(fileSuffix) || fle.isDirectory());
			}

			public String getDescription() {
				return "XML-Gunzip Edition (" + fileSuffix + ")";
			}
		});
		fc.setDialogTitle("Choose Exportfile");
		int returnVal = fc.showSaveDialog(UIConstants.getMainFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				File file = fc.getSelectedFile();
				if (!file.getName().endsWith(fileSuffix)) {
					file = new File(file.getPath() + fileSuffix);
				}
				if (file.exists()) {
					file.delete();
					file.createNewFile();
				}
				UIConstants.setStatusInfo("Fetching Data from Server...");
				((Communication) getBean(Beans.COMMUNICATION)).createEditionForExport(file, rootVCid);
				UIConstants.setStatusInfo("Finished Export!");
				UIConstants.getMainFrame().setCursor(Cursor.getDefaultCursor());
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("ExportFullThread.message.success"), rb.getString("ExportFullThread.title"), JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception exe) {
				log.error("Error during the export to file", exe);
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("ExportFullThread.message.failure"), rb.getString("ExportFullThread.title"), JOptionPane.ERROR_MESSAGE);
			}
		} else {
			UIConstants.getMainFrame().setCursor(Cursor.getDefaultCursor());
		}
	}

}
