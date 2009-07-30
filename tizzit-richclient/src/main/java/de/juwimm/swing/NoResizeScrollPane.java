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
package de.juwimm.swing;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;

/**
 * The NoResizeScrollPane is a no-resizing-scrollpane.<br>
 * It should be generally used, if you need a Scrollpane inside a GridBagLayout and wants the
 * full control over the Size of the Scrollpane. In general a Scrollpane will resize if the contained content
 * has a size bigger then the Scrollpane.<br><br>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Juwi|MacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class NoResizeScrollPane extends JScrollPane {
	private boolean iw = true;
	private boolean ih = true;

	public NoResizeScrollPane() {
		super();
	}

	public NoResizeScrollPane(Component lst) {
		super(lst);
	}

	public void ignoreWidth(boolean iw) {
		this.iw = iw;
	}

	public void ignoreHeight(boolean ih) {
		this.ih = ih;
	}

	/**
	 * Returns an empty Dimension as PreferredSize.
	 * @return
	 */
	public Dimension getPreferredSize() {
		if (iw && ih) {
			return new Dimension(0, 0);
		} else if (iw && !ih) {
			Dimension dim = super.getPreferredSize();
			return new Dimension(0, dim.height);
		} else if (!iw && ih) {
			Dimension dim = super.getPreferredSize();
			return new Dimension(dim.width, 0);
		} else {
			// why do you're using me?!
			return super.getPreferredSize();
		}
	}
}