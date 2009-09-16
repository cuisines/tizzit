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
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package de.juwimm.cms.model;

import javax.swing.ImageIcon;

import org.tizzit.util.Base64;

import de.juwimm.cms.vo.PictureSlimValue;
import de.juwimm.cms.vo.PictureSlimstValue;
import de.juwimm.cms.vo.PictureValue;

/**
 * @see de.juwimm.cms.model.PictureHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PictureHbmImpl extends PictureHbm {
	private static final long serialVersionUID = 789271321041565274L;
 
	/**
	 * @see de.juwimm.cms.model.PictureHbm#toXml(int)
	 */
	@Override
	public String toXml(int tabdepth) {
		// NOTE: Height and width of picture are calculated when picture is created. So these information don't have to be exported here
		StringBuffer sb = new StringBuffer();
		sb.append("<picture id=\"");
		sb.append(this.getPictureId());
		sb.append("\" mimeType=\"");
		sb.append(this.getMimeType());
		sb.append("\" unitId=\"");
		sb.append(this.getUnit().getUnitId());
		sb.append("\">\n");
		sb.append("<thumbnail>").append(Base64.encodeBytes(this.getThumbnail())).append("</thumbnail>\n");
		sb.append("<file>").append(Base64.encodeBytes(this.getPicture())).append("</file>\n");
		// preview may be null or empty
		if (this.getPreview() != null && this.getPreview().length > 0) {
			sb.append("<preview>").append(Base64.encodeBytes(this.getPreview())).append("</preview>\n");
		}
		String altText = this.getAltText();
		if (altText == null) altText = "";
		sb.append("<altText><![CDATA[").append(altText).append("]]></altText>\n");
		String pictureName = this.getPictureName();
		if (pictureName == null) pictureName = "";
		sb.append("<pictureName><![CDATA[").append(pictureName).append("]]></pictureName>\n");
		sb.append("</picture>\n");
		return sb.toString();
	}

	@Override
	public PictureSlimValue getSlimValue() {
		PictureSlimValue pictureSlimValue = new PictureSlimValue();
		pictureSlimValue.setPictureId(this.getPictureId());
		pictureSlimValue.setMimeType(this.getMimeType());
		pictureSlimValue.setThumbnail(this.getThumbnail());
		pictureSlimValue.setTimeStamp(this.getTimeStamp());
		if (this.getHeight() == null || this.getWidth() == null) {
			// size is calculated on creating picture.
			// for existing pictures size has to be calculated once on first use in client
			ImageIcon img = new ImageIcon(this.getPicture());
			this.setHeight(Integer.valueOf(img.getIconHeight()));
			this.setWidth(Integer.valueOf(img.getIconWidth()));
		}
		pictureSlimValue.setHeight(this.getHeight());
		pictureSlimValue.setWidth(this.getWidth());
		pictureSlimValue.setAltText(this.getAltText());
		pictureSlimValue.setPictureName(this.getPictureName());
		return pictureSlimValue;
	}

	@Override
	public PictureSlimstValue getSlimstValue() {
		PictureSlimstValue pictureSlimValue = new PictureSlimstValue();
		pictureSlimValue.setPictureId(this.getPictureId());
		pictureSlimValue.setMimeType(this.getMimeType());
		pictureSlimValue.setTimeStamp(this.getTimeStamp());
		if (this.getHeight() == null || this.getWidth() == null) {
			// size is calculated on creating picture.
			// for existing pictures size has to be calculated once on first use in client
			ImageIcon img = new ImageIcon(this.getPicture());
			this.setHeight(Integer.valueOf(img.getIconHeight()));
			this.setWidth(Integer.valueOf(img.getIconWidth()));
		}
		pictureSlimValue.setHeight(this.getHeight());
		pictureSlimValue.setWidth(this.getWidth());
		pictureSlimValue.setAltText(this.getAltText());
		pictureSlimValue.setPictureName(this.getPictureName());
		return pictureSlimValue;
	}

	@Override
	public PictureValue getPictureValue() {
		PictureValue pictureValue = new PictureValue();
		pictureValue.setPictureId(this.getPictureId());
		pictureValue.setPicture(this.getPicture());
		pictureValue.setPreview(this.getPreview());
		pictureValue.setMimeType(this.getMimeType());
		pictureValue.setTimeStamp(this.getTimeStamp());
		if (this.getHeight() == null || this.getWidth() == null) {
			// size is calculated on creating picture.
			// for existing pictures size has to be calculated once on first use in client
			ImageIcon img = new ImageIcon(this.getPicture());
			this.setHeight(Integer.valueOf(img.getIconHeight()));
			this.setWidth(Integer.valueOf(img.getIconWidth()));
		}
		pictureValue.setHeight(this.getHeight());
		pictureValue.setWidth(this.getWidth());
		pictureValue.setAltText(this.getAltText());
		pictureValue.setPictureName(this.getPictureName());
		return pictureValue;
	}

}
