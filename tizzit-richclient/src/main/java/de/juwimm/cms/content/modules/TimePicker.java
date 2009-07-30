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
/*
 * Created on 22.02.2005
 */
package de.juwimm.cms.content.modules;

import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.w3c.dom.Node;

import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanTimePicker;

/**
 * Module for choosing a point in time in hours, minutes and seconds.<br/>
 * <p>
 * The format of the time for choosing in the client can be specified in the DCF by giving a pattern.<br/>
 * The output from the Generator looks like this regardless of the display-format:<br/>
 * </p>
 * <p>
 * &lt;time dcfname="time" label="Zeitauswahl"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;hour&gt;05&lt;/hour&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;minute&gt;06&lt;/minute&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;second&gt;56&lt;/second&gt;<br/>
 * &lt;/time&gt;<br/>
 * </p>
 * This module can be parameterized regarding the display-format of the time.<br/>
 * <p>
 * &lt;time dcfname="time" label="Zeitauswahl"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;dcfConfig&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;classname&gt;de.juwimm.cms.content.modules.TimePicker&lt;/classname&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="customTimeFormat"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;displayPattern&gt;HH:mm:ss&lt;/displayPattern&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/property&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;/dcfConfig&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;dcfInitial/&gt;<br/>
 * &lt;/time&gt;<br/>
 * </p>
 * <p>
 * The default time-format is &quot;HH:mm&quot;. For entering a time e.g. with seconds use &quot;HH:mm:ss&quot;.<br/>
 * </p>
 * @see <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html">java.text.SimpleDateFormat</a>
 * <br/>
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class TimePicker extends AbstractModule {
	private PanTimePicker panTimePicker = null;
	private String timeFormat = "HH:mm";

	/* This panel is always valid
	 * @see de.juwimm.cms.content.modules.Module#isModuleValid()
	 */
	public boolean isModuleValid() {
		return true;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#viewModalUI(boolean)
	 */
	public JDialog viewModalUI(boolean modal) {
		DlgModalModule frm = new DlgModalModule(this, this.getPanTimePicker(), 190, 380, modal);
		frm.setVisible(true);
		return frm;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#viewPanelUI()
	 */
	public JPanel viewPanelUI() {
		return this.getPanTimePicker();
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#setProperties(org.w3c.dom.Node)
	 */
	public void setProperties(Node node) {
		this.getPanTimePicker().setProperties(node);
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#getProperties()
	 */
	public Node getProperties() {
		return this.getPanTimePicker().getProperties();
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#getPaneImage()
	 */
	public String getPaneImage() {
		return "";
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#getIconImage()
	 */
	public String getIconImage() {
		return "";
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabling) {
		this.getPanTimePicker().setEnabled(enabling);
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#load()
	 */
	public void load() {
	}
	
	public void recycle() {
		
	}

	private PanTimePicker getPanTimePicker() {
		if (this.panTimePicker == null) {
			this.panTimePicker = new PanTimePicker(this.timeFormat);
		}
		return this.panTimePicker;
	}

	/*
	 *  @see de.juwimm.cms.content.modules.AbstractModule#setCustomProperties(java.lang.String, java.util.Properties)
	 */
	@Override
	public void setCustomProperties(String methodName, Properties parameters) {
		super.setCustomProperties(methodName, parameters);
		if ("customTimeFormat".equalsIgnoreCase(methodName)) {
			try {
				this.timeFormat = parameters.get("displayPattern").toString();
			} catch (Exception e) {
				// parameter may not be present - that's ok
			}
		}
	}

}
