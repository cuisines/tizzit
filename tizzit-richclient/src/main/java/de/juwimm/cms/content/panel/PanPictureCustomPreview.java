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

import static de.juwimm.cms.common.Constants.rb;

import java.awt.Cursor;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.content.frame.helper.ImageFileView;
import de.juwimm.cms.content.frame.helper.ImageFilter;
import de.juwimm.cms.content.frame.helper.ImagePreview;
import de.juwimm.cms.content.frame.helper.Utils;
import de.juwimm.cms.content.modules.Module;
import de.juwimm.cms.content.panel.util.PictureUploadUtil;
import de.juwimm.cms.gui.FrmProgressDialog;
import de.juwimm.cms.util.UIConstants;

/**
 * Special Panel for adding Pictures with a custom Preview-Image
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PanPictureCustomPreview extends PanPicture {
	private final Logger log = Logger.getLogger(PanPictureCustomPreview.class);

	public PanPictureCustomPreview() {
		super();
	}

	public PanPictureCustomPreview(Module module) {
		super(module);
	}

	protected void upload(String prosa, int unit) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		File picFull = null;
		File picPreview = null;
		int retInt = 0;
		JFileChooser fc = new JFileChooser();
		int ff = fc.getChoosableFileFilters().length;
		FileFilter[] fft = fc.getChoosableFileFilters();
		for (int i = 0; i < ff; i++) {
			fc.removeChoosableFileFilter(fft[i]);
		}
		fc.addChoosableFileFilter(new ImageFilter());
		fc.setFileView(new ImageFileView());
		fc.setAccessory(new ImagePreview(fc));
		fc.setMultiSelectionEnabled(false);
		fc.setCurrentDirectory(Constants.LAST_LOCAL_UPLOAD_DIR);

		// get Picture in full size
		fc.setDialogTitle(rb.getString("panel.content.picture.addPictureFull"));
		int returnVal = fc.showDialog(PanPictureCustomPreview.this, rb.getString("panel.content.picture.addPictureFull"));

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			picFull = fc.getSelectedFile();
			this.lblFileName.setText(picFull.getName());
			Constants.LAST_LOCAL_UPLOAD_DIR = fc.getCurrentDirectory();
			if (picFull.length() > 4000000) {
				this.setCursor(Cursor.getDefaultCursor());
				JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.FileTooBig") + ": " + picFull.getName(), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			// get Picture in Preview size
			fc.setDialogTitle(rb.getString("panel.content.picture.addPicturePreview"));
			returnVal = fc.showDialog(PanPictureCustomPreview.this, rb.getString("panel.content.picture.addPicturePreview"));

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				picPreview = fc.getSelectedFile();
				this.lblFile.setVisible(true);
				this.lblFileName.setVisible(true);
				Constants.LAST_LOCAL_UPLOAD_DIR = fc.getCurrentDirectory();
				if (picPreview.length() > 4000000) {
					this.setCursor(Cursor.getDefaultCursor());
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.FileTooBig") + ": " + picPreview.getName(), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
					return;
				}
				// user selected two pictures to upload
				FrmProgressDialog prog = new FrmProgressDialog(rb.getString("panel.content.picture.addPicture"), rb.getString("panel.content.upload.ParseFile"), 100);
				prog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				try {
					byte[] btyPicFull = PictureUploadUtil.getBytesFromFile(picFull);
					byte[] btyPicPre = PictureUploadUtil.getBytesFromFile(picPreview);

					ImageIcon tmpIcon = new ImageIcon(btyPicFull);
					ImageIcon thumbnail = null;
					if (tmpIcon.getIconWidth() > 90 || tmpIcon.getIconHeight() > 90) {
						thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
					} else {
						thumbnail = tmpIcon;
					}
					ByteArrayOutputStream out = PictureUploadUtil.manipulateImage(thumbnail.getImage());

					String fext = Utils.getExtension(picFull);
					String mimetype = "image/jpeg";
					if (fext.equals(Utils.JPEG) || fext.equals(Utils.JPG)) {
						mimetype = "image/jpeg";
					} else if (fext.equals(Utils.GIF)) {
						mimetype = "image/gif";
					} else if (fext.equals(Utils.TIF) || fext.equals(Utils.TIFF)) {
						mimetype = "image/tif";
					}

					prog.setProgress(rb.getString("panel.content.upload.Uploading"), 50);
					retInt = this.comm.addPicture2Unit(unit, out.toByteArray(), btyPicFull, btyPicPre, mimetype, "", picFull.getName(), "");

					setPictureId(retInt);
				} catch (Exception exe) {
					log.error("Error during the upload of the picture " + picFull.getName(), exe);
				} finally {
					prog.setProgress(rb.getString("panel.content.upload.Finished"), 100);
					prog.dispose();
				}
			}
		}
		this.setCursor(Cursor.getDefaultCursor());
	}

}