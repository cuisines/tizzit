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

import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

import de.juwimm.cms.content.event.EditpaneFiredEvent;
import de.juwimm.cms.content.event.EditpaneFiredListener;

/**
 * Interface for the description of every UI Interfacemodule displaying a DataCaptureForm.
 * <p><b>Copyright: JuwiMacMillan Group GmbH (c) 2002</b></p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public interface Module extends Cloneable {
	/**
	 * Timestamp is used to specify unique dcf module in iteration
	 * return long-value
	 */
	public long getTimeStamp();

	public void setTimeStamp(long timeStamp);

	/**
	 * Currently only used in Textfield, but can be also used in Iterations later
	 * for avoiding the "renaming" of Modules.
	 * Should contain the unique Rootnode Name.
	 */
	public String getRootnodeName();

	public void setRootnodeName(String rootnodeName);

	/**
	 * Gives the response if this Module is mandatory.
	 * @return boolean-value
	 */
	public boolean isMandatory();

	public void setMandatory(boolean isMandatory);

	/**
	 * This Method verifys the given Values.<br>
	 * This Method SHOULD NOT THROW AN ERROR or NOTIFY THE USER BY ITSELF!<br>
	 * This is the job of the referring caller - so it could be Iteration, ContentSingleton or the WYSIWYG Panel.
	 * @return If something goes wrong, the result would be <b>false</b>
	 */
	public boolean isModuleValid();

	/**
	 * If this module is not valid, here we will get the specific errormessage<br>
	 * for this Module.
	 *
	 * @return The Errormessage. If there is no error, the returned String is empty.
	 * It will NOT return a NULL Object!
	 */
	public String getValidationError();

	/**
	 * Sets the UNIQUE Name of this Module for the given DCF.<br>
	 * This Name will be referenced by saving and getting Data from the XML Page.
	 * To find the correct field for filling this element in the destination XML it <b>must</b> be unique. <br>
	 * The Name must be supplied inside the DCF and in the XML Source-File
	 * @param dcfname
	 */
	public void setName(String dcfname);

	/**
	 * Reads the UNIQUE Name of this ElementHandler for the actual DCF.
	 * @return The Name
	 */
	public String getName();

	/**
	 * The "Label" is the description of this Module from inside the DCF. <br>
	 * The Administrator can define a useful name for this instance of a Module and shows this the User.
	 * @param dcfconfiglabel
	 */
	public void setLabel(String dcfconfiglabel);

	/**
	 * Return the Label.
	 * See {@link #setLabel() setLabel()} for more.
	 * @return
	 */
	public String getLabel();

	/**
	 * The User can define a Description for a Module. <br>
	 * Currently this is only needed inside an iteration or inside the WYSIWYG-Field for displaying
	 * a short description.
	 * @param userdescription
	 */
	public void setDescription(String userdescription);

	/**
	 * Return the description.
	 * See {@link #setDescription() setDescription()} for more.
	 * @return
	 */
	public String getDescription();

	/**
	 * Shows the Interface in a new Window<br>
	 * The returning JFrame can also be null.
	 * 
	 * @return a JDialog or {@code null}.
	 */
	public JDialog viewModalUI(boolean modal);

	/**
	 * Returns the JPanel Interface for this Module.
	 * This is the Interface for this Module for calling it from outside the XPEditorPane
	 */
	public JPanel viewPanelUI();

	/**
	 * Returns the properties of this Module.
	 * It is not expected, that all values are valid while calling this procedure - to get sure, that all
	 * Values are correct, call {@link #isModuleValid() isModuleValid()} before.
	 * @return The current properties.
	 */
	public void setCustomProperties(String methodName, Properties parameters);

	/**
	 * If the last CustomProperty has been set, this will be true
	 * @return
	 */
	public boolean isCustomConfigurationReady();

	public void setProperties(Node node);

	public Node getProperties();

	/**
	 * Returns the NAME of the Image that will repesentate this Module.<br>
	 * Is been used inside the WYSIWYG Panel for showing the Images with <br>
	 * <code>de.juwimm.cms.guibuilder.GuibuilderSingleton.getBaseImagePath()</code> as Path to this Image,<br>
	 * @return Name of the Imagefile
	 */
	public String getPaneImage();

	/**
	 * Returns the NAME of the Image that will repesentate this Module.<br>
	 * Is been used as Button above the WYSIWYG Panel for showing the Images.<br>
	 * Therefor it gets it Image though <code>UIConstants.getWYSIWYGicon()</code>
	 * @return Name of the Imagefile
	 */
	public String getIconImage();

	public void addEditpaneFiredListener(EditpaneFiredListener sav);

	/**
	 * Method for removing a listener for all save-operations.
	 * @param sav The SaveOperationListener Object
	 */
	public void removeEditpaneFiredListener(EditpaneFiredListener sav);

	public void runEditpaneFiredEvent(EditpaneFiredEvent efe);

	public void runEditpaneCancelEvent(EditpaneFiredEvent efe);

	public boolean hasEditpaneFiredListener();

	/**
	 * @param id
	 */
	public Object clone();

	public void setEnabled(boolean enabling);

	/**
	 * This is for the new Textfield2 and is the replacement of the Editpanefired_cancel event.<br>
	 * This should be false, if the user presses cancel, otherwise it should be true
	 * @return
	 */
	public boolean isSaveable();

	/**
	 * This is for the new Textfield2 and is the replacement of the Editpanefired_cancel event.<br>
	 * This should be false, if the user presses cancel, otherwise it should be true
	 * @return
	 */
	public void setSaveable(boolean saveable);

	/**
	 * The load-method indicates that the module is fully loaded and will be displayed - some
	 * tasks need to be performed asynchronous and some others should only be done if there 
	 * will be no content set. So this is an replacement for setProperties(null) or even load(contentHandler).
	 */
	public void load();

	/**
	 * The new load method 
	 * @param ch
	 */
	public void load(ContentHandler ch);

	/**
	 * The save-Method will be the new "getProperties()"-method for content modules.<br/>
	 * The module is proposed to send the XML node values of it's properties through 
	 * SAX events.<br/>
	 * The node which describes the module will be created through the module-container, so the
	 * module <b>must</b> only return the content of its properties.<br/>
	 * This is analog to the existing methodology in setProperties();
	 * 
	 * @param contentHandler The contentHandler which should receive the SAX Events 
	 */
	public void save(ContentHandler contentHandler);
	
	/**
	 * Will be called after unload of the panel. Useage should be to clean up every module for reuse.
	 */
	public void recycle();
}