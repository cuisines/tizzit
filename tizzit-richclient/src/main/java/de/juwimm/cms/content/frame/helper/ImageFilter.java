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

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class ImageFilter extends FileFilter {

	// Accept all directories and all gif, jpg, or tiff files.
	public boolean accept(File f) {
		if (f.isDirectory()) { return true; }

		String extension = Utils.getExtension(f);
		if (extension != null) {
			return (extension.equals(Utils.GIF) 
					|| extension.equals(Utils.PNG) 
					|| extension.equals(Utils.JPEG)
					|| extension.equals(Utils.JPG)) ? true : false;
		}
		return false;
	}

	// The description of this filter
	public String getDescription() {
		return "Bilddateien";
	}
}