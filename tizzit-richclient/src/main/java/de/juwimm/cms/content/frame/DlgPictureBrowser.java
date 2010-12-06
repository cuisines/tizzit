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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;


import de.juwimm.cms.content.panel.PanPictures;
import de.juwimm.cms.gui.views.PanContentView;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class DlgPictureBrowser extends JDialog {
	PanPictures panPictures;
	public DlgPictureBrowser() {
		super(UIConstants.getMainFrame(), true);
		this.getContentPane().setLayout(new BorderLayout());
		Integer viewComponentId=PanContentView.getInstance().getViewComponent().getViewComponentId();
		panPictures=new PanPictures(true,viewComponentId);
		int frameHeight = 500;
		int frameWidth = 500;
		panPictures.setSize(frameWidth, frameHeight);
		panPictures.setPreferredSize(new Dimension(frameWidth, frameHeight));
		panPictures.addSaveActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		panPictures.addCancelListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		this.add(panPictures,BorderLayout.CENTER);
		this.pack();
	}
	public void setPictureId(int pictureId) {
		panPictures.setPictureId(pictureId);
	}
	public void addSaveActionListener(ActionListener al) {
		panPictures.addSaveActionListener(al);
	}

}