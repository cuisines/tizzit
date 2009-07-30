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


/**
 * This class is needed for the data-exchange with a PickListPanel.
 * 
 * @see de.juwimm.swing.PickListPanel()
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class PickListData {
	private String leftLabel = null;
	private String rightLabel = null;
	private AbstractPickListModel lstLeftModel = null;
	private AbstractPickListModel lstRightModel = null;
	private boolean isModified = false;
	
	/**
	 * The default constructor initializes the pick list data with {@link SortingListModel}s
	 * as {@code ListModel}s.
	 */
	public PickListData() {
		this(new SortingListModel(), new SortingListModel());
	}
	
	/**
	 * The value constructor initializes the pick list data with the spefified {@ code ListModel}s.
	 *  
	 * @param listModelLeft the {@link AbstractPickListModel} to use for the list on the left
	 * @param listModelRight the {@link AbstractPickListModel} to use for the list on the right
	 */
	public PickListData(AbstractPickListModel listModelLeft, AbstractPickListModel listModelRight) {
		this.leftLabel = "";
		this.rightLabel = "";
		this.lstLeftModel = listModelLeft;
		this.lstRightModel = listModelRight;
	}
	
	/**
	 * @return Returns the assignedLabel.
	 * @deprecated replaced by {@link #getLeftLabel()}
	 */
	public String getAssignedLabel() {
		return this.leftLabel;
	}
	
	/**
	 * @param assignedLabel The assignedLabel to set.
	 * @deprecated replaced by {@link #setLeftLabel(String)}
	 */
	public void setAssignedLabel(String assignedLabel) {
		this.leftLabel = assignedLabel;
	}
	
	/**
	 * @return Returns the availableLabel.
	 * @deprecated replaced by {@link #getRightLabel()}
	 */
	public String getAvailableLabel() {
		return this.rightLabel;
	}
	
	/**
	 * @param availableLabel The availableLabel to set.
	 * @deprecated replaced by {@link #setRightLabel(String)}
	 */
	public void setAvailableLabel(String availableLabel) {
		this.rightLabel = availableLabel;
	}
	
	/**
	 * @return Returns the lstAssignedModel.
	 * @deprecated replaced by {@link #getLstLeftModel()}
	 */
	public AbstractPickListModel getLstAssignedModel() {
		return this.lstLeftModel;
	}
	
	/**
	 * @param lstAssignedModel The lstAssignedModel to set.
	 * @deprecated replaced by {@link #setLstLeftModel(SortingListModel)}
	 */
	public void setLstAssignedModel(AbstractPickListModel lstAssignedModel) {
		this.lstLeftModel = lstAssignedModel;
	}
	
	/**
	 * @return Returns the lstAvailableModel.
	 * @deprecated replaced by {@link #getLstRightModel()}
	 */
	public AbstractPickListModel getLstAvailableModel() {
		return this.lstRightModel;
	}
	
	/**
	 * @param lstAvailableModel The lstAvailableModel to set.
	 * @deprecated replaced by {@link #setLstRightModel(SortingListModel)}
	 */
	public void setLstAvailableModel(AbstractPickListModel lstAvailableModel) {
		this.lstRightModel = lstAvailableModel;
	}
	
	/**
	 * Indicated if the data has been modified or both models are in their starting state
	 * @return
	 */
	public boolean isModified() {
		return this.isModified;
	}
	
	/**
	 * Set the flag that the models have changed
	 * @param isModified
	 */
	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}

	/**
	 * @return Returns the leftLabel.
	 */
	public String getLeftLabel() {
		return leftLabel;
	}

	/**
	 * @param leftLabel The leftLabel to set.
	 */
	public void setLeftLabel(String leftLabel) {
		this.leftLabel = leftLabel;
	}

	/**
	 * @return Returns the lstLeftModel.
	 */
	public AbstractPickListModel getLstLeftModel() {
		return lstLeftModel;
	}

	/**
	 * @param lstLeftModel The lstLeftModel to set.
	 */
	public void setLstLeftModel(AbstractPickListModel lstLeftModel) {
		this.lstLeftModel = lstLeftModel;
	}

	/**
	 * @return Returns the lstRightModel.
	 */
	public AbstractPickListModel getLstRightModel() {
		return lstRightModel;
	}

	/**
	 * @param lstRightModel The lstRightModel to set.
	 */
	public void setLstRightModel(AbstractPickListModel lstRightModel) {
		this.lstRightModel = lstRightModel;
	}

	/**
	 * @return Returns the rightLabel.
	 */
	public String getRightLabel() {
		return rightLabel;
	}

	/**
	 * @param rightLabel The rightLabel to set.
	 */
	public void setRightLabel(String rightLabel) {
		this.rightLabel = rightLabel;
	}

}
