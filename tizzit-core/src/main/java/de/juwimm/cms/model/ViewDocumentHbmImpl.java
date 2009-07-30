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

import de.juwimm.cms.vo.ViewDocumentValue;

/**
 * @see de.juwimm.cms.model.ViewDocumentHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a> , Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class ViewDocumentHbmImpl extends ViewDocumentHbm {
	private static final long serialVersionUID = -8361727647402289733L;

	/**
	* @see de.juwimm.cms.model.ViewDocumentHbm#getDao()
	*/
	@Override
	public ViewDocumentValue getDao() {
		ViewDocumentValue val = new ViewDocumentValue();
		val.setLanguage(this.getLanguage());
		val.setViewDocumentId(this.getViewDocumentId());
		val.setViewType(this.getViewType());
		val.setSiteId(this.getSite().getSiteId());
		val.setViewId(getViewComponent().getViewComponentId());
		ViewDocumentHbm defaultViewDocument = this.getSite().getDefaultViewDocument();
		if (defaultViewDocument != null && defaultViewDocument.getViewDocumentId() != null) val.setIsVdDefault(this.getViewDocumentId().intValue() == defaultViewDocument.getViewDocumentId().intValue());
		return val;
	}

	/**
	 * @see de.juwimm.cms.model.ViewDocumentHbm#toXml(int)
	 */
	@Override
	public String toXml(int tabdepth) {
		boolean isDefault = false;
		try {
			isDefault = this.getViewDocumentId().intValue() == this.getSite().getDefaultViewDocument().getViewDocumentId().intValue();
		} catch (Exception e) {
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<viewDocument id=\"");
		sb.append(getViewDocumentId());
		sb.append("\" rootViewComponentId=\"");
		sb.append(getViewComponent().getViewComponentId());
		sb.append("\" language=\"");
		sb.append(getLanguage());
		sb.append("\" viewType=\"");
		sb.append(getViewType());
		sb.append("\" siteId=\"");
		sb.append(getSite().getSiteId());
		sb.append("\" default=\"");
		sb.append(Boolean.toString(isDefault));
		sb.append("\" />\n");
		return sb.toString();
	}

}
