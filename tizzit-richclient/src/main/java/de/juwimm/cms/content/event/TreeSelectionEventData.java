package de.juwimm.cms.content.event;

import de.juwimm.cms.vo.ViewDocumentValue;

/**
 * @author fzalum
 *
 */
public class TreeSelectionEventData {
	private Integer viewComponentId;
	private Integer unitId;
	private ViewDocumentValue viewDocument;

	public Integer getViewComponentId() {
		return viewComponentId;
	}

	public void setViewComponentId(Integer viewComponentId) {
		this.viewComponentId = viewComponentId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public ViewDocumentValue getViewDocument() {
		return viewDocument;
	}

	public void setViewDocument(ViewDocumentValue documentValue) {
		this.viewDocument = documentValue;
	}

}
