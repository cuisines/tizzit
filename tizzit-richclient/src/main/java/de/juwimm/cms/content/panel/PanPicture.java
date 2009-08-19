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

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.UserRights;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.frame.DlgPictureBrowser;
import de.juwimm.cms.content.frame.DlgPictureEditor;
import de.juwimm.cms.content.frame.helper.ImageFileView;
import de.juwimm.cms.content.frame.helper.ImageFilter;
import de.juwimm.cms.content.frame.helper.ImagePreview;
import de.juwimm.cms.content.frame.helper.Utils;
import de.juwimm.cms.content.modules.Module;
import de.juwimm.cms.gui.FrmProgressDialog;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.Parameters;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.PictureSlimValue;

/**
 * 
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik </a>
 * @version $Id$
 */
public class PanPicture extends JPanel {
	private static Logger log = Logger.getLogger(PanPicture.class);
	protected Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private JButton btnUploadRoot = new JButton();
	protected JButton btnPreview = new JButton();
	private JButton btnUpload = new JButton();
	private JPanel pnlPreview = new JPanel();
	private JButton btnChoose = new JButton();
	private JButton btnEdit = new JButton();
	private JLabel lblPreview = new JLabel();
	protected JLabel lblPictId = new JLabel();
	private JLabel lblPictNo = new JLabel();
	protected JLabel lblFile = new JLabel();
	protected JLabel lblFileName = new JLabel();
	private JTextField txtPictureSubtext = new JTextField();
	private JLabel lblPictureSubtext = new JLabel();
	protected JTextField txtAltText = new JTextField();
	private JLabel lblAltText = new JLabel();
	private JLabel lblDirection = new JLabel();
	private JComboBox cboDirection = new JComboBox();
	private int pictureWidth = -1;
	private int pictureHeight = -1;
	private int pictureId = -1;

	public PanPicture() {
		try {
			jbInit();
			if (comm.isUserInRole(UserRights.SITE_ROOT)) {
				btnUploadRoot.setVisible(true);
			} else {
				btnUploadRoot.setVisible(false);
			}
		} catch (Exception exe) {
			log.error("Initialization error", exe);
		}
		if (Parameters.getBooleanParameter(Parameters.PARAM_PICTURE_POSITION_1)) {
			String txt = Parameters.getStringParameter(Parameters.PARAM_PICTURE_POSITION_1);
			if (txt == null || txt.equals("")) txt = rb.getString("panel.content.picture.directionCenter");
			this.cboDirection.addItem(new CboModel(txt, "center"));
		}
		if (Parameters.getBooleanParameter(Parameters.PARAM_PICTURE_POSITION_2)) {
			String txt = Parameters.getStringParameter(Parameters.PARAM_PICTURE_POSITION_2);
			if (txt == null || txt.equals("")) txt = rb.getString("panel.content.picture.directionLeft");
			this.cboDirection.addItem(new CboModel(txt, "left"));
		}
		if (Parameters.getBooleanParameter(Parameters.PARAM_PICTURE_POSITION_3)) {
			String txt = Parameters.getStringParameter(Parameters.PARAM_PICTURE_POSITION_3);
			if (txt == null || txt.equals("")) txt = rb.getString("panel.content.picture.directionRight");
			this.cboDirection.addItem(new CboModel(txt, "right"));
		}
	}

	public PanPicture(Module module) {
		this();
	}

	/**
	 * 
	 */
	public final class CboModel {
		private String strView;
		private String val;

		public CboModel(String view, String val) {
			this.strView = view;
			this.val = val;
		}

		public String getView() {
			return this.strView;
		}

		public String getValue() {
			return this.val;
		}

		public String toString() {
			return this.strView;
		}
	}

	public Integer getPictureId() {
		Integer inti = new Integer(0);
		try {
			inti = new Integer(this.lblPictId.getText());
		} catch (Exception exe) {
		}
		return inti;
	}

	public void setPictureId(int pictureId) {
		this.pictureId = pictureId;
		if (pictureId > 0) {
			loadPreview(pictureId);
		} else {
			this.btnPreview.setIcon(null);
			this.lblPictId.setText(" ");
			this.lblFileName.setText(" ");
			this.txtAltText.setText(" ");
			btnEdit.setEnabled(false);
		}
	}

	public void setPictureText(String text) {
		this.txtPictureSubtext.setText(text);
	}

	public String getPictureText() {
		return this.txtPictureSubtext.getText();
	}

	public void setType(String type) {
		if (type.equals("left")) {
			this.cboDirection.setSelectedIndex(1);
		} else if (type.equals("right")) {
			this.cboDirection.setSelectedIndex(2);
		} else if (type.equals("center")) {
			this.cboDirection.setSelectedIndex(0);
		}
	}

	public String getType() {
		return ((CboModel) this.cboDirection.getSelectedItem()).getValue();
	}

	private void jbInit() throws Exception {
		btnUploadRoot.setMaximumSize(new Dimension(95, 27));
		btnUploadRoot.setMinimumSize(new Dimension(95, 27));
		btnUploadRoot.setPreferredSize(new Dimension(95, 27));
		btnUploadRoot.setToolTipText(rb.getString("panel.content.picture.uploadRoot.tooltiptext"));
		btnUploadRoot.setText(rb.getString("panel.content.picture.uploadRoot"));
		btnUploadRoot.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				btnUploadRootActionPerformed(e);
			}
		});

		btnPreview.setBorder(null);
		btnPreview.setPreferredSize(new Dimension(120, 120));
		btnPreview.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				btnChooseActionPerformed(e);
			}
		});
		btnUpload.setMaximumSize(new Dimension(95, 27));
		btnUpload.setMinimumSize(new Dimension(95, 27));
		btnUpload.setPreferredSize(new Dimension(95, 27));
		btnUpload.setToolTipText(rb.getString("panel.content.picture.upload.tooltiptext"));
		btnUpload.setText(rb.getString("panel.content.picture.upload"));
		btnUpload.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				btnUploadActionPerformed(e);
			}
		});
		pnlPreview.setLayout(new BorderLayout());
		pnlPreview.setBorder(BorderFactory.createLoweredBevelBorder());
		btnChoose.setMaximumSize(new Dimension(95, 27));
		btnChoose.setMinimumSize(new Dimension(95, 27));
		btnChoose.setPreferredSize(new Dimension(95, 27));
		btnChoose.setToolTipText(rb.getString("panel.content.picture.choosePic.tooltiptext"));
		btnChoose.setText(rb.getString("panel.content.picture.choosePic"));
		btnChoose.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				btnChooseActionPerformed(e);
			}
		});
		btnEdit.setMaximumSize(new Dimension(95, 27));
		btnEdit.setMinimumSize(new Dimension(95, 27));
		btnEdit.setPreferredSize(new Dimension(95, 27));
		btnEdit.setToolTipText(rb.getString("panel.content.picture.editPic.tooltiptext"));
		btnEdit.setText(rb.getString("panel.content.picture.editPic"));
		btnEdit.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				btnEditActionPerformed(e);
			}
		});
		btnEdit.setEnabled(false);
		lblPreview.setToolTipText("");
		lblPreview.setText(rb.getString("panel.content.picture.PicPreview"));
		this.setLayout(new GridBagLayout());
		lblPictId.setBorder(BorderFactory.createLoweredBevelBorder());
		lblFileName.setBorder(BorderFactory.createLoweredBevelBorder());
		lblPictId.setText(rb.getString("panel.content.picture.fileName.blank"));
		lblFileName.setText(rb.getString("panel.content.picture.fileName.blank"));
		lblFile.setText(rb.getString("panel.content.picture.FileName"));
		lblPictNo.setText(rb.getString("panel.content.picture.PicNumber"));
		lblPictureSubtext.setText(rb.getString("panel.content.picture.PicSubline"));
		lblAltText.setText(rb.getString("panel.content.picture.altText"));
		lblDirection.setText(rb.getString("panel.content.picture.Direction"));
		pnlPreview.add(btnPreview, BorderLayout.CENTER);
		// upper part
		this.add(lblPictNo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(17, 15, 0, 13), 0, 0));
		this.add(lblPictId, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(17, 0, 0, 0), 60, 0));
		this.add(lblFile, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 15, 0, 13), 0, 0));
		this.add(lblFileName, new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 6), 60, 0));
		// center part
		this.add(lblPreview, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(26, 15, 0, 0), 10, 0));
		this.add(pnlPreview, new GridBagConstraints(0, 3, 3, 4, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 15, 0, 0), 119, -48));
		// center buttons
		this.add(btnChoose, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 18, 0, 6), 58, 0));
		this.add(btnUpload, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.NONE, new Insets(5, 18, 0, 6), 58, 0));
		this.add(btnUploadRoot, new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.NONE, new Insets(5, 18, 0, 6), 58, 0));
		this.add(btnEdit, new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.NONE, new Insets(5, 18, 0, 6), 58, 0));
		// lower part
		this.add(lblDirection, new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(11, 15, 0, 0), 7, 0));
		this.add(cboDirection, new GridBagConstraints(2, 7, 2, 2, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 0, 50, 6), 188, 2));
		this.add(lblPictureSubtext, new GridBagConstraints(0, 8, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(9, 15, 0, 0), 7, 0));
		this.add(txtPictureSubtext, new GridBagConstraints(2, 8, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 6), 188, 2));
		this.add(lblAltText, new GridBagConstraints(0, 9, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(9, 15, 10, 0), 7, 0));
		this.add(txtAltText, new GridBagConstraints(2, 9, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 10, 6), 307, 2));
	}

	protected void btnUploadActionPerformed(ActionEvent e) {
		Thread t = new Thread(new Runnable() {

			public void run() {
				upload(rb.getString("panel.content.picture.upload4thisUnit"), ((ContentManager) getBean(Beans.CONTENT_MANAGER)).getActUnitId());
			}
		});
		t.setPriority(Thread.NORM_PRIORITY);
		t.start();
	}

	protected void btnUploadRootActionPerformed(ActionEvent e) {
		Thread t = new Thread(new Runnable() {

			public void run() {
				upload(rb.getString("panel.content.picture.upload4allUnits"), ((ContentManager) getBean(Beans.CONTENT_MANAGER)).getRootUnitId());
			}
		});
		t.setPriority(Thread.NORM_PRIORITY);
		t.start();
	}

	protected void upload(String prosa, int unit) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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
		fc.setDialogTitle(prosa);
		fc.setMultiSelectionEnabled(true);
		fc.setCurrentDirectory(Constants.LAST_LOCAL_UPLOAD_DIR);
		int returnVal = fc.showDialog(PanPicture.this, rb.getString("panel.content.picture.addPicture"));

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] files = fc.getSelectedFiles();
			if (files != null) {
				for (int i = (files.length - 1); i >= 0; i--) {
					this.lblFile.setVisible(true);
					this.lblFileName.setVisible(true);
					this.lblFileName.setText(files[i].getName());
					Constants.LAST_LOCAL_UPLOAD_DIR = fc.getCurrentDirectory();
					if (files[i].length() > 4000000) {
						this.setCursor(Cursor.getDefaultCursor());
						JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.FileTooBig") + ": " + files[i].getName(),
								rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
						continue;
					}
					FrmProgressDialog prog = new FrmProgressDialog(rb.getString("panel.content.picture.addPicture"),
							rb.getString("panel.content.upload.ParseFile"), 100);
					prog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
					try {
						byte[] bty = getBytesFromFile(files[i]);
		
						ImageIcon tmpIcon = new ImageIcon(bty);
						ImageIcon thumbnail = null;
						if (tmpIcon.getIconWidth() > 90 || tmpIcon.getIconHeight() > 90) {
							thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
						} else {
							thumbnail = tmpIcon;
						}
						ByteArrayOutputStream out = manipulateImage(thumbnail.getImage());
		
						String fext = Utils.getExtension(files[i]);
						String mimetype = "image/jpeg";
						if (fext.equals(Utils.JPEG) || fext.equals(Utils.JPG)) {
							mimetype = "image/jpeg";
						} else if (fext.equals(Utils.GIF)) {
							mimetype = "image/gif";
						} else if (fext.equals(Utils.TIF) || fext.equals(Utils.TIFF)) {
							mimetype = "image/tif";
						}
		
						prog.setProgress(rb.getString("panel.content.upload.Uploading"), 50);
						retInt = this.comm.addPicture2Unit(unit, out.toByteArray(), bty, mimetype, "", files[i].getName());
		
						setPictureId(retInt);
					} catch (Exception exe) {
						log.error("Error during the upload of the picture " + files[i].getName(), exe);
					} finally {
						prog.setProgress(rb.getString("panel.content.upload.Finished"), 100);
						prog.dispose();
					}
				}
			}
			this.setCursor(Cursor.getDefaultCursor());
		} else {
			this.setCursor(Cursor.getDefaultCursor());
		}
	}

	public ByteArrayOutputStream manipulateImage(Image image) {
		ByteArrayOutputStream imageStream = null;
		Frame frame = null;
		Graphics graphics = null;
		try {
			frame = new Frame();
			MediaTracker mt = new MediaTracker(frame); // frame acts as an ImageObserver
			mt.addImage(image, 0);
			mt.waitForAll();
			int w = image.getWidth(frame);
			int h = image.getHeight(frame);
			BufferedImage offscreen = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
			graphics = offscreen.getGraphics();
			graphics.drawImage(image, 0, 0, frame);
			imageStream = new ByteArrayOutputStream();
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(imageStream);
			encoder.encode(offscreen);
		} catch (InterruptedException e) {
			log.error("Interrupted the manipulation of the image", e);
		} catch (IOException e) {
			log.error("IO ERROR manipulating the image", e);
		} finally {
			if (graphics != null) {
				graphics.dispose();
			}
		}
		return imageStream;
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();

		if (length > Integer.MAX_VALUE) { throw new IOException("File too big"); }

		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) { throw new IOException("Could not completely read file " + file.getName()); }

		is.close();
		return bytes;
	}

	protected void btnChooseActionPerformed(ActionEvent e) {
		DlgPictureBrowser dlgPictureBrowser = new DlgPictureBrowser();
		dlgPictureBrowser.setPictureId(this.pictureId);
		dlgPictureBrowser.addSaveActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				loadPreview(new Integer(ae.getActionCommand()).intValue());
				btnEdit.setEnabled(true);
			}
		});
		int frameHeight = 500;
		int frameWidth = 500;
		dlgPictureBrowser.setSize(frameWidth, frameHeight);
		dlgPictureBrowser.setLocationRelativeTo(UIConstants.getMainFrame());
 		dlgPictureBrowser.setVisible(true);
	}
	
	protected void btnEditActionPerformed(ActionEvent e) {
		DlgPictureEditor dlgPictureEditor = new DlgPictureEditor(this.pictureId);
		
		dlgPictureEditor.addSaveActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				loadPreview(new Integer(ae.getActionCommand()).intValue());
			}
		});
		int frameHeight = 600;
		int frameWidth = 800;
		dlgPictureEditor.setSize(frameWidth, frameHeight);
		dlgPictureEditor.setLocationRelativeTo(UIConstants.getMainFrame());
		dlgPictureEditor.setVisible(true);
	}

	public void loadPreview(int picture) {
		try {
			pictureHeight = -1;
			pictureWidth = -1;
			{
				PictureSlimValue pic = comm.getPicture(picture);
				ImageIcon img = new ImageIcon(pic.getThumbnail());
				this.btnPreview.setIcon(img);
				this.lblPictId.setText(picture + "");
				if (pic.getPictureName() != null) {
					this.lblFileName.setText(pic.getPictureName());
					this.lblFile.setVisible(true);
					this.lblFileName.setVisible(true);
				} else {
					this.lblFileName.setText(null);
					this.lblFile.setVisible(false);
					this.lblFileName.setVisible(false);
				}
				this.txtAltText.setText(pic.getAltText());
				setPictureHeight(pic.getHeight());
				setPictureWidth(pic.getWidth());
				this.pictureId = picture;
				btnEdit.setEnabled(true);
			}
		} catch (Exception ex) {
			log.error("catched exception while loading ImagePreview of imageId: "+picture, ex);
		}
	}

	public void hidePictureMessage() {
		this.lblDirection.setVisible(false);
		this.cboDirection.setVisible(false);
		this.lblPictureSubtext.setVisible(false);
		this.txtPictureSubtext.setVisible(false);
		this.lblAltText.setVisible(false);
		this.txtAltText.setVisible(false);
	}

	/**
	 * @return Returns the pictureHeight.
	 */
	public int getPictureHeight() {
		return this.pictureHeight;
	}

	/**
	 * @param pictureHeight The pictureHeight to set.
	 */
	public void setPictureHeight(int pictureHeight) {
		this.pictureHeight = pictureHeight;
	}

	/**
	 * @return Returns the pictureWidth.
	 */
	public int getPictureWidth() {
		return this.pictureWidth;
	}

	/**
	 * @param pictureWidth The pictureWidth to set.
	 */
	public void setPictureWidth(int pictureWidth) {
		this.pictureWidth = pictureWidth;
	}

	public String getPictureAltText() {
		return this.txtAltText.getText();
	}

	public String getPictureFileName() {
		return this.lblFileName.getText();
	}

}