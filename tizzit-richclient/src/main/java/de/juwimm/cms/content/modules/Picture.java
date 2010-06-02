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

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanOnlyButton;
import de.juwimm.cms.content.panel.PanPicture;
import de.juwimm.cms.content.panel.PanPictureCustomPreview;
import de.juwimm.cms.util.Communication;

/**
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class Picture extends AbstractModule {
	private static Logger log = Logger.getLogger(Picture.class);
	public static final String CLASS_NAME = "de.juwimm.cms.content.modules.Picture";
	private final Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	protected PanPicture panPicture = null;
	private PanOnlyButton panBtn;
	private boolean imEnabled = true;
	private boolean customPreview = false;

	@Override
	public void setCustomProperties(String methodname, Properties parameters) {
		super.setCustomProperties(methodname, parameters);
		if (methodname.equalsIgnoreCase("PreviewType")) {
			this.customPreview = Boolean.parseBoolean(parameters.get("custom").toString());
		}
	}

	public JDialog viewModalUI(boolean modal) {
		int frameHeight = 400;
		int frameWidth = 440;
		DlgModalModule frm = new DlgModalModule(this, getPanPicture(), frameHeight, frameWidth, modal);
		frm.setVisible(true);
		return frm;
	}

	public JPanel viewPanelUI() {
		panBtn = new PanOnlyButton(this, true);
		panBtn.setEnabled(imEnabled);
		return panBtn;
	}

	public boolean isModuleValid() {
		if (isMandatory()) {
			boolean retVal = true;

			setValidationError("");
			if (getPanPicture().getPictureId() == null || getPanPicture().getPictureId().intValue() <= 0) {
				appendValidationError(rb.getString("exception.PictureRequired"));
				retVal = false;
			}

			if (getPanPicture().getPictureAltText() == null || getPanPicture().getPictureAltText().trim().isEmpty()) {
				appendValidationError(rb.getString("exception.AltTextRequired"));
				retVal = false;
			}

			return retVal;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void load() {
	}

	public Node getProperties() {
		Element root = ContentManager.getDomDoc().createElement("picture");
		if (getPanPicture().getPictureId() != null && getPanPicture().getPictureId().intValue() > 0) {
			Element elm = ContentManager.getDomDoc().createElement("image");
			elm.setAttribute("src", getPanPicture().getPictureId().toString());
			elm.setAttribute("type", getPanPicture().getType());
			elm.setAttribute("height", "" + getPanPicture().getPictureHeight());
			elm.setAttribute("width", "" + getPanPicture().getPictureWidth());
			elm.setAttribute("popup", "" + getPanPicture().getPictureThumbnailPopup());
			Element elmLegend = ContentManager.getDomDoc().createElement("legend");
			String tmp = getPanPicture().getPictureText();
			if (tmp == null) tmp = "";
			CDATASection txtLegend = ContentManager.getDomDoc().createCDATASection(tmp);
			elmLegend.appendChild(txtLegend);
			elm.appendChild(elmLegend);
			Element elmFileName = ContentManager.getDomDoc().createElement("filename");
			tmp = getPanPicture().getPictureFileName();
			if (tmp == null) tmp = "";
			CDATASection txtNodeFileName = ContentManager.getDomDoc().createCDATASection(tmp);
			elmFileName.appendChild(txtNodeFileName);
			elm.appendChild(elmFileName);
			Element elmAltText = ContentManager.getDomDoc().createElement("alttext");
			tmp = getPanPicture().getPictureAltText();
			if (tmp == null) tmp = "";
			CDATASection txtNodeAltText = ContentManager.getDomDoc().createCDATASection(tmp);
			elmAltText.appendChild(txtNodeAltText);
			elm.appendChild(elmAltText);
			root.appendChild(elm);
			setDescription(elm.getAttribute("src"));
			try {
				if (getPanPicture().getPictureAltText() != null) {
					comm.updatePictureAltText(getPanPicture().getPictureId().intValue(), getPanPicture().getPictureAltText());
				} else {
					comm.updatePictureAltText(getPanPicture().getPictureId().intValue(), "");
				}
			} catch (Exception e) {
				log.error("Error updating pictureAltText " + getPanPicture().getPictureId().intValue() + " " + getPanPicture().getPictureAltText());
			}
			try {
				comm.updatePictureThumbnailPopup(getPanPicture().getPictureThumbnailPopup(), getPanPicture().getPictureId());
			} catch (Exception e) {
				if (log.isDebugEnabled()) log.error("Error updating picture thumbnail with popup " + getPanPicture().getPictureId());
			}
		}
		return root;
	}

	public void setProperties(Node node) {
		boolean found = false;
		if (node != null) {
			try {
				Element imageElm = (Element) XercesHelper.findNode(node, "./image");
				if (imageElm != null) {
					int inti = new Integer(imageElm.getAttribute("src")).intValue();
					if (inti > 0) {
						try {
							getPanPicture().setPictureId(inti);
						} catch (Exception exe) {
						}
						try {
							getPanPicture().setPictureWidth(new Integer(imageElm.getAttribute("width")).intValue());
						} catch (Exception exe) {
						}
						try {
							getPanPicture().setPictureHeight(new Integer(imageElm.getAttribute("height")).intValue());
						} catch (Exception exe) {
						}
					}
					getPanPicture().setType(imageElm.getAttribute("type"));
					Element elmLegend = (Element) XercesHelper.findNode(imageElm, "./legend");
					if (elmLegend == null) {
						// old structure
						getPanPicture().setPictureText(XercesHelper.getNodeValue(imageElm));
					} else {
						// new structure with separate tag for bu
						getPanPicture().setPictureText(XercesHelper.getNodeValue(elmLegend));
					}
					found = true;
				}
			} catch (Exception exe) {
			}
		}
		if (!found) {
			getPanPicture().setPictureId(0);
			getPanPicture().setType("");
			getPanPicture().setPictureText("");
		}
	}

	public String getIconImage() {
		return "16_bild.gif";
	}

	public String getPaneImage() {
		try {
			return "ejbimage?typ=t&id=" + getURLEncodedISO(getDescription());
		} catch (Exception exe) {
			log.error("Error returning pane image", exe);
			return "16_komp_zwischenueberschr.png";
		}
	}

	public void setEnabled(boolean enabling) {
		if (panBtn != null) panBtn.setEnabled(enabling);
		imEnabled = enabling;
	}

	public void recycle() {
		getPanPicture().setPictureId(0);
		getPanPicture().setType("");
		getPanPicture().setPictureText("");
	}

	protected PanPicture getPanPicture() {
		if (this.panPicture == null) {
			if (log.isDebugEnabled()) log.debug("PanPictureCustomPreview: " + this.customPreview);
			if (this.customPreview) {
				this.panPicture = new PanPictureCustomPreview(this);
			} else {
				this.panPicture = new PanPicture(this);
			}
		}
		return this.panPicture;
	}

}
