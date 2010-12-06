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
package de.juwimm.cms.content.modules;

import de.juwimm.cms.content.panel.PanPicture;
import de.juwimm.cms.content.panel.PanPictureCustomPreview;

/**
 * Special version of &quot;Picture&quot; where the user can specify the preview-image on his own.<br/>
 * When the user uploads a new picture the file-open dialog appears a second time to select the preview-image.<br/>
 * All other beahvior is exacly like &quot;Picture&quot;.
 * @see de.juwimm.cms.content.modules.Picture
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PictureCustomPreview extends Picture {
	public static final String CLASS_NAME = "de.juwimm.cms.content.modules.PictureCustomPreview";

	public String getIconImage() {
		return "16_bildvorschau.gif";
	}

	public PanPicture getPanPicture() {
		if (this.panPicture == null) {
			this.panPicture = new PanPictureCustomPreview(this);
		}
		return this.panPicture;
	}

}
