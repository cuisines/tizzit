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
package de.juwimm.cms.util;

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.juwimm.cms.client.beans.Beans;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class Parameters {
	private static Logger log = Logger.getLogger(Parameters.class);
	private static Parameter[] availableSiteParameter;
	private static Hashtable<String, Object> actualSiteParameterValues = new Hashtable<String, Object>();
	public static final String PARAM_SHOW_PREVIEW_FRAMESET = "showPreviewFrameset";
	public static final String PARAM_PICTURE_POSITION_1 = "picturePosition1";
	public static final String PARAM_PICTURE_POSITION_2 = "picturePosition2";
	public static final String PARAM_PICTURE_POSITION_3 = "picturePosition3";
	public static final String PARAM_EXTLINK_OPENWITHSTYLE = "extlinkOpenWithStyle";
	public static final String PARAM_SHOWTYPE_0 = "showtype0";
	public static final String PARAM_SHOWTYPE_1 = "showtype1";
	public static final String PARAM_SHOWTYPE_2 = "showtype2";
	public static final String PARAM_SHOWTYPE_3 = "showtype3";
	public static final String PARAM_MAX_EDITION_STACK = "maxEditionStack";
	public static final String PARAM_MAX_DISPLAY_LINK_NAME_LENGTH = "maxLinkNameLength";
	public static final String PARAM_INCLUDE_XML_SEARCH_NAME = "includeXmlSearch";
	public static final String PARAM_USER_CHANGE_PAGE_MODIFIED_DATE = "userChangePageModifiedDate";

	private Parameters() {
	}

	static {
		// this are the only the currently supported parameterTypes for parameters.
		SiteParameter.ParameterType[] onlyBoolean = new SiteParameter.ParameterType[] {
				new SiteParameter.ParameterType(Parameter.PARAMETER_TYPE_BOOLEAN, Boolean.FALSE)
				};
		SiteParameter.ParameterType[] boolAndTextFalse = new SiteParameter.ParameterType[] {
				new SiteParameter.ParameterType(Parameter.PARAMETER_TYPE_BOOLEAN, Boolean.FALSE),
				new SiteParameter.ParameterType(Parameter.PARAMETER_TYPE_STRING, "")
				};
		
		SiteParameter.ParameterType[] boolAndTextTrue = new SiteParameter.ParameterType[] {
				new SiteParameter.ParameterType(Parameter.PARAMETER_TYPE_BOOLEAN, Boolean.TRUE),
				new SiteParameter.ParameterType(Parameter.PARAMETER_TYPE_STRING, "")
				};
		/*SiteParameter.ParameterType[] onlyText = new SiteParameter.ParameterType[] {
				new SiteParameter.ParameterType(Parameter.PARAMETER_TYPE_STRING, "")
				};*/
		//These are the Parameters
		availableSiteParameter = new Parameter[13];
		availableSiteParameter[0] = new SiteParameter(PARAM_SHOW_PREVIEW_FRAMESET, rb.getString("PARAM_SHOW_PREVIEW_FRAMESET"), onlyBoolean);
		availableSiteParameter[1] = new SiteParameter(PARAM_PICTURE_POSITION_1, rb.getString("PARAM_PICTURE_POSITION_1"), boolAndTextTrue);
		availableSiteParameter[2] = new SiteParameter(PARAM_PICTURE_POSITION_2, rb.getString("PARAM_PICTURE_POSITION_2"), boolAndTextTrue);
		availableSiteParameter[3] = new SiteParameter(PARAM_PICTURE_POSITION_3, rb.getString("PARAM_PICTURE_POSITION_3"), boolAndTextTrue);
		availableSiteParameter[4] = new SiteParameter(PARAM_EXTLINK_OPENWITHSTYLE, rb.getString("PARAM_EXTLINK_OPENWITHSTYLE"), onlyBoolean);
		availableSiteParameter[5] = new SiteParameter(PARAM_SHOWTYPE_0, "ShowType 0", boolAndTextTrue);
		availableSiteParameter[6] = new SiteParameter(PARAM_SHOWTYPE_1, "ShowType 1", boolAndTextTrue);
		availableSiteParameter[7] = new SiteParameter(PARAM_SHOWTYPE_2, "ShowType 2", boolAndTextTrue);
		availableSiteParameter[8] = new SiteParameter(PARAM_SHOWTYPE_3, "ShowType 3", boolAndTextTrue);
		availableSiteParameter[9] = new SiteParameter(PARAM_MAX_EDITION_STACK, "max. Editions", boolAndTextFalse);
		availableSiteParameter[10] = new SiteParameter(PARAM_MAX_DISPLAY_LINK_NAME_LENGTH, rb.getString("PARAM_MAX_DISPLAY_LINK_NAME_LENGTH"), boolAndTextFalse);
		availableSiteParameter[11] = new SiteParameter(PARAM_INCLUDE_XML_SEARCH_NAME, rb.getString("PARAM_INCLUDE_XML_SEARCH_NAME"), boolAndTextFalse);
		availableSiteParameter[12] = new SiteParameter(PARAM_USER_CHANGE_PAGE_MODIFIED_DATE, rb.getString("PARAM_USER_CHANGE_PAGE_MODIFIED_DATE"), onlyBoolean);
	}

	public static Parameter[] getAvailableSiteParameter() {
		return availableSiteParameter;
	}

	public static void clear() {
		actualSiteParameterValues = new Hashtable<String, Object>();
	}

	public static void loadRolesetForActiveSite() { //should load the settings for the active site.
		log.info("Loading Roleset for active Domain");
		Parameters.clear();
		ConfigReader cfg = new ConfigReader(((Communication) getBean(Beans.COMMUNICATION)).getSiteConfigXML(), ConfigReader.CONF_NODE_DEFAULT);
		NodeList nl = cfg.getConfigSubelements("parameters");
		if (nl != null) {
			for (int i = 0; i < nl.getLength(); i++) {
				Node nde = nl.item(i);
				actualSiteParameterValues.put(nde.getNodeName(), XercesHelper.getNodeValue(nde));
			}
		}
	}

	public static void setParameter(String id, int paramType, Object value) {
		actualSiteParameterValues.put(id + "_" + paramType, value);
	}

	public static Object getParameter(String id, int paramType) {
		if (actualSiteParameterValues.containsKey(id + "_" + paramType)) {
			return actualSiteParameterValues.get(id + "_" + paramType);
		}
		// return default value
		for (int i = 0; i < availableSiteParameter.length; i++) {
			if (availableSiteParameter[i].getId().equalsIgnoreCase(id)) {
				return availableSiteParameter[i].getDefaultValue(paramType);
			}
		}
		return null;
	}

	public static boolean getBooleanParameter(String id) {
		return Boolean.valueOf(getParameter(id, Parameter.PARAMETER_TYPE_BOOLEAN).toString()).booleanValue();
	}

	public static String getStringParameter(String id) {
		return ((String) getParameter(id, Parameter.PARAMETER_TYPE_STRING));
	}

}
