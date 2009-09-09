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
public class ImportFullThread extends Thread {
	private static Logger log = Logger.getLogger(ImportFullThread.class);
	private Integer rootVCid = null;
	private String fileSuffix = ".xml.gz";

	public ImportFullThread() {
		super("ImportSiteThread");
		fileSuffix = ".site.xml.gz";
	}

	public ImportFullThread(Integer rootVCid) {
		super("ImportUnitThread");
		this.rootVCid = rootVCid;
		fileSuffix = ".unit.xml.gz";
	}

	public void run() {				
		String warning = "";
		if (rootVCid == null) {
			warning = rb.getString("actions.importFullThread.fullWarning");
		} else {
			warning = rb.getString("actions.importFullThread.unitWarning");
		}
		int resp = JOptionPane.showConfirmDialog(UIConstants.getMainFrame(), warning, rb.getString("dialog.title"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if (resp == JOptionPane.OK_OPTION) {
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileFilter() {
				public boolean accept(File fle) {
					return (fle.getName().endsWith(fileSuffix) || fle.isDirectory());
				}

				public String getDescription() {
					return "XML-Gunzip Edition";
				}
			});
			fc.setDialogTitle("Choose Importfile");
			int returnVal = fc.showOpenDialog(UIConstants.getMainFrame());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					File file = fc.getSelectedFile();
					if (file.exists()) {
						UIConstants.setWorker(true);
						UIConstants.setStatusInfo("Reading File...");
						((Communication) getBean(Beans.COMMUNICATION)).importEditionFromImport(file, rootVCid);
						UIConstants.setStatusInfo("Finished Import!");						
						UIConstants.setWorker(false);
						JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("ImportFullThread.message.success"), rb.getString("ImportFullThread.title"), JOptionPane.INFORMATION_MESSAGE);
					} else {
						//File does not exist
						JOptionPane.showMessageDialog(UIConstants.getMainFrame(), "File does not exist", 
								rb.getString("dialog.title"), JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception exe) {
					log.error("Approving import error", exe);
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("ImportFullThread.message.failure"), rb.getString("ImportFullThread.title"), JOptionPane.ERROR_MESSAGE);
					UIConstants.setWorker(false);
				}
			} 
		}
		
	}

}
