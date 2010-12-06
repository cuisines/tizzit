package de.juwimm.cms.content.panel.util;

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.common.Constants;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.frame.helper.ImageFileView;
import de.juwimm.cms.content.frame.helper.ImageFilter;
import de.juwimm.cms.content.frame.helper.ImagePreview;
import de.juwimm.cms.content.panel.PanDocuments;
import de.juwimm.cms.content.panel.PanPicture;
import de.juwimm.cms.content.panel.PanPictures;
import de.juwimm.cms.gui.controls.PopupComponent;
import de.juwimm.cms.gui.views.page.PanelContent;

public class PopupPictures extends PopupComponent {
	private PanPictures panPictures;
	private final ResourceBundle rb = Constants.rb;
	private JPanel content = new JPanel();
	private JButton btnAdd;
	private Integer viewComponentId;

	public PopupPictures(JComponent owner, Integer viewComponentId) {
		super(new JPanel(), true, 0, true);
		this.setContent(content);
		this.viewComponentId=viewComponentId;
		content.setBorder(new BevelBorder(BevelBorder.RAISED));
		content.setSize(600, 300);
		content.setLayout(new BorderLayout());
		panPictures = new PanPictures(false, viewComponentId);
		panPictures.setSize(600, 300);
		panPictures.setPreferredSize(new Dimension(600, 300));
		panPictures.setPictureId(0);
		//		panPictures.resizeScrollpane();
		panPictures.addSaveActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hidePopup();
			}
		});
		content.add(panPictures, BorderLayout.CENTER);
		content.add(getPanButtons(), BorderLayout.SOUTH);

		int x = owner.getWidth() - panPictures.getWidth();
		int y = owner.getHeight();
		showPopup(owner, x, y);

	}

	private JPanel getPanButtons() {
		JPanel panButtons = new JPanel();
		btnAdd = new JButton(rb.getString("dialog.add"));
		btnAdd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				btnUploadRootActionPerformed(e);
			}
		});
		panButtons.setLayout(new GridBagLayout());
		panButtons.add(btnAdd, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		return panButtons;
	}

	public void addSaveActionListener(ActionListener al) {
		panPictures.addSaveActionListener(al);
	}

	public PanPictures getPanPictures() {
		return panPictures;
	}

	protected void btnUploadRootActionPerformed(ActionEvent e) {
		PictureUploadUtil util=new PictureUploadUtil(panPictures, rb.getString("panel.content.picture.upload4allUnits"), ((ContentManager) getBean(Beans.CONTENT_MANAGER)).getRootUnitId(), null) {

			@Override
			public void pictureExistsAction(int i) {
				// TODO Auto-generated method stub

			}

			@Override
			public void pictureSelectedAction(byte[] thumbnail, byte[] picture, String mimeType, String pictureName) throws Exception {
				@SuppressWarnings("unused")
				Integer retInt = this.comm.addPicture2ViewComponent(viewComponentId, thumbnail, picture, mimeType, "", pictureName, "");
				panPictures.refresh();
				panPictures.setPictureId(retInt);
			}

			@Override
			public int findExistingPicture(String fileName) {
				Integer pictureId=comm.getPictureIdForViewComponentAndName(viewComponentId,fileName );
				return pictureId;
			}

		};
		util.show();
	}

}
