package de.juwimm.cms.content.panel.util;

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.content.frame.DlgSavePicture;
import de.juwimm.cms.content.frame.helper.ImageFileView;
import de.juwimm.cms.content.frame.helper.ImageFilter;
import de.juwimm.cms.content.frame.helper.ImagePreview;
import de.juwimm.cms.content.frame.helper.Utils;
import de.juwimm.cms.gui.FrmProgressDialog;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.PictureSlimValue;

public abstract class PictureUploadUtil {
	private JComponent parentComponent;
	private Integer unitId;
	private Integer viewComponentId;
	private String titleBarText;

	protected Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private static Logger log = Logger.getLogger(PictureUploadUtil.class);

	/**
	 * Initialize the picture upload utility by passing a tile bar text and either 
	 * a unitId or a viewComponentId that will be the entity the picture is attached to. 
	 * If both are not null the utility will throw an exception. 
	 * <br><br>
	 * The upload will be done in a separate thread.
	 * 
	 * @param titleBarText
	 * @param unitId
	 * @param viewComponentId
	 */
	public PictureUploadUtil(JComponent parentComponent, final String titleBarText, final Integer unitId, final Integer viewComponentId) {
		this.parentComponent = parentComponent;
		if (unitId != null && viewComponentId != null) {
			throw new IllegalArgumentException("Only one of unitId and viewComponentId are supposed to be not null");
		}
		this.unitId = unitId;
		this.viewComponentId = viewComponentId;
		this.titleBarText=titleBarText;

	}
	
	public void show(){
		Thread t = new Thread(new Runnable() {

			public void run() {
				upload(titleBarText);
			}
		});
		t.setPriority(Thread.NORM_PRIORITY);
		t.start();
	}

	protected void upload(String prosa) {
		parentComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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
		int returnVal = fc.showDialog(parentComponent, rb.getString("panel.content.picture.addPicture"));

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] files = fc.getSelectedFiles();
			uploadFiles(files, fc.getCurrentDirectory());
			parentComponent.setCursor(Cursor.getDefaultCursor());
		} else {
			parentComponent.setCursor(Cursor.getDefaultCursor());
		}
	}

	/**
	 * Upload the selected images
	 * @param files
	 * @param unit
	 * @param localUploadDir
	 */
	public void uploadFiles(File[] files, File localUploadDir) {
		if (files != null) {
			for (int i = (files.length - 1); i >= 0; i--) {
				Constants.LAST_LOCAL_UPLOAD_DIR = localUploadDir;
				if (files[i].length() > 4000000) {
					parentComponent.setCursor(Cursor.getDefaultCursor());
					JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("exception.FileTooBig") + ": " + files[i].getName(), rb.getString("dialog.title"), JOptionPane.ERROR_MESSAGE);
					continue;
				}
				FrmProgressDialog prog = new FrmProgressDialog(rb.getString("panel.content.picture.addPicture"), rb.getString("panel.content.upload.ParseFile"), 100);
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
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					try {
						out = manipulateImage(thumbnail.getImage());
					} catch (Exception e) {
						JOptionPane.showMessageDialog(UIConstants.getMainFrame(), rb.getString("panel.content.picture.onlyPictures"), rb.getString("dialog.title"), JOptionPane.INFORMATION_MESSAGE);
						return;
					}

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
					int existingPicId = 0;
					try {
						existingPicId = findExistingPicture(files[i].getName());

					} catch (Exception e) {
						log.error("Error during getting getPictureIdForUnitAndName");
					}
					if (existingPicId == 0) {
						pictureSelectedAction(out.toByteArray(), bty, mimetype, files[i].getName());
					} else {
						/**picture name already exists=>dialog message*/
						PictureSlimValue picSlimVal = comm.getPicture(existingPicId);
						DlgSavePicture saveDialog = new DlgSavePicture(picSlimVal, bty, out.toByteArray());
						saveDialog.addSaveActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								pictureExistsAction(e.getID());
							}
						});
						int frameHeight = 180;
						int frameWidth = 250;
						saveDialog.setSize(frameWidth, frameHeight);
						saveDialog.setLocationRelativeTo(UIConstants.getMainFrame());
						saveDialog.setModal(true);
						saveDialog.setVisible(true);
					}
				} catch (Exception exe) {
					log.error("Error during the upload of the picture " + files[i].getName(), exe);
				} finally {
					prog.setProgress(rb.getString("panel.content.upload.Finished"), 100);
					prog.dispose();
				}
			}
		}
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			throw new IOException("File too big");
		}

		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		is.close();
		return bytes;
	}

	public static ByteArrayOutputStream manipulateImage(Image image) {
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

	public abstract void pictureExistsAction(int i);

	public abstract void pictureSelectedAction(byte[] thumbnail, byte[] picture, String mimeType, String pictureName) throws Exception;

	public abstract int findExistingPicture(String fileName);

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getViewComponentId() {
		return viewComponentId;
	}

	public void setViewComponentId(Integer viewComponentId) {
		this.viewComponentId = viewComponentId;
	}
	
	
}
