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
package de.juwimm.cms.content.frame.helper;

import java.awt.Image;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class ImageFileView extends FileView {
	public String getName(File f) {
		return null; // let the L&F FileView figure this out
	}

	public String getDescription(File f) {
		return null; // let the L&F FileView figure this out
	}

	public Boolean isTraversable(File f) {
		return null; // let the L&F FileView figure this out
	}

	public String getTypeDescription(File f) {
		String extension = Utils.getExtension(f);
		String type = null;

		if (extension != null) {
			if (extension.equals(Utils.JPEG) || extension.equals(Utils.JPG)) {
				type = "JPEG Image";
			} else if (extension.equals(Utils.GIF)) {
				type = "GIF Image";
			} else if (extension.equals(Utils.TIFF) || extension.equals(Utils.TIF)) {
				type = "TIFF Image";
			}
		}
		return type;
	}

	public Icon getIcon(File f) {
		if (f.isDirectory()) {
			return super.getIcon(f);
		}
		String extension = Utils.getExtension(f);
		ImageIcon icon = null;
		ImageIcon thumbnail = null;
		if (extension != null) {
			icon = Utils.getIcon4Extension(extension);
			if (icon.getIconWidth() > 16 || icon.getIconHeight() > 16) {
				thumbnail = new ImageIcon(icon.getImage().getScaledInstance(16, -1, Image.SCALE_DEFAULT));
			} else {
				thumbnail = icon;
			}
		}
		return thumbnail;
	}
}