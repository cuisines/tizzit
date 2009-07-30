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
 * @(#)AttributeTable.java   1.11 2000/08/16
 *
 */

package de.juwimm.util.tidy;

import java.util.Hashtable;

/**
 *
 * HTML attribute hash table
 *
 * (c) 1998-2000 (W3C) MIT, INRIA, Keio University
 * See Tidy.java for the copyright notice.
 * Derived from <a href="http://www.w3.org/People/Raggett/tidy">
 * HTML Tidy Release 4 Aug 2000</a>
 *
 * @author  Dave Raggett <dsr@w3.org>
 * @author  Andy Quick <ac.quick@sympatico.ca> (translation to Java)
 * @version 1.0, 1999/05/22
 * @version 1.0.1, 1999/05/29
 * @version 1.1, 1999/06/18 Java Bean
 * @version 1.2, 1999/07/10 Tidy Release 7 Jul 1999
 * @version 1.3, 1999/07/30 Tidy Release 26 Jul 1999
 * @version 1.4, 1999/09/04 DOM support
 * @version 1.5, 1999/10/23 Tidy Release 27 Sep 1999
 * @version 1.6, 1999/11/01 Tidy Release 22 Oct 1999
 * @version 1.7, 1999/12/06 Tidy Release 30 Nov 1999
 * @version 1.8, 2000/01/22 Tidy Release 13 Jan 2000
 * @version 1.9, 2000/06/03 Tidy Release 30 Apr 2000
 * @version 1.10, 2000/07/22 Tidy Release 8 Jul 2000
 * @version 1.11, 2000/08/16 Tidy Release 4 Aug 2000
 */

public class AttributeTable {

	public AttributeTable() {
	}

	public Attribute lookup(String name) {
		return (Attribute) attributeHashtable.get(name);
	}

	public Attribute install(Attribute attr) {
		return (Attribute) attributeHashtable.put(attr.name, attr);
	}

	/* public method for finding attribute definition by name */
	public Attribute findAttribute(AttVal attval) {
		Attribute np;

		if (attval.attribute != null) {
			np = lookup(attval.attribute);
			return np;
		}

		return null;
	}

	public boolean isUrl(String attrname) {
		Attribute np;

		np = lookup(attrname);
		return (np != null && np.attrchk == AttrCheckImpl.getCheckUrl());
	}

	public boolean isScript(String attrname) {
		Attribute np;

		np = lookup(attrname);
		return (np != null && np.attrchk == AttrCheckImpl.getCheckScript());
	}

	public boolean isLiteralAttribute(String attrname) {
		Attribute np;

		np = lookup(attrname);
		return (np != null && np.literal);
	}

	/*
	 Henry Zrepa reports that some folk are
	 using embed with script attributes where
	 newlines are signficant. These need to be
	 declared and handled specially!
	 */
	public void declareLiteralAttrib(String name) {
		Attribute attrib = lookup(name);

		if (attrib == null) attrib = install(new Attribute(name, Dict.VERS_PROPRIETARY, null));

		attrib.literal = true;
	}

	private Hashtable attributeHashtable = new Hashtable();

	private static AttributeTable defaultAttributeTable = null;

	private static Attribute[] attrs = {

	new Attribute("abbr", Dict.VERS_HTML40, null), new Attribute("accept-charset", Dict.VERS_HTML40, null),
			new Attribute("accept", Dict.VERS_ALL, null), new Attribute("accesskey", Dict.VERS_HTML40, null),
			new Attribute("action", Dict.VERS_ALL, AttrCheckImpl.getCheckUrl()),
			new Attribute("add_date", Dict.VERS_NETSCAPE, null), /* A */
			new Attribute("align", Dict.VERS_ALL, AttrCheckImpl.getCheckAlign()), /* set varies with element */
			new Attribute("alink", Dict.VERS_LOOSE, null), new Attribute("alt", Dict.VERS_ALL, null),
			new Attribute("archive", Dict.VERS_HTML40, null), /* space or comma separated list */
			new Attribute("axis", Dict.VERS_HTML40, null),
			new Attribute("background", Dict.VERS_LOOSE, AttrCheckImpl.getCheckUrl()),
			new Attribute("bgcolor", Dict.VERS_LOOSE, null),
			new Attribute("bgproperties", Dict.VERS_PROPRIETARY, null), /* BODY "fixed" fixes background */
			new Attribute("border", Dict.VERS_ALL, AttrCheckImpl.getCheckBool()), /* like LENGTH + "border" */
			new Attribute("bordercolor", Dict.VERS_MICROSOFT, null), /* used on TABLE */
			new Attribute("bottommargin", Dict.VERS_MICROSOFT, null), /* used on BODY */
			new Attribute("cellpadding", Dict.VERS_FROM32, null), /* % or pixel values */
			new Attribute("cellspacing", Dict.VERS_FROM32, null), new Attribute("char", Dict.VERS_HTML40, null),
			new Attribute("charoff", Dict.VERS_HTML40, null), new Attribute("charset", Dict.VERS_HTML40, null),
			new Attribute("checked", Dict.VERS_ALL, AttrCheckImpl.getCheckBool()), /* i.e. "checked" or absent */
			new Attribute("cite", Dict.VERS_HTML40, AttrCheckImpl.getCheckUrl()),
			new Attribute("class", Dict.VERS_HTML40, null),
			new Attribute("classid", Dict.VERS_HTML40, AttrCheckImpl.getCheckUrl()),
			new Attribute("clear", Dict.VERS_LOOSE, null), /* BR: left, right, all */
			new Attribute("code", Dict.VERS_LOOSE, null), /* APPLET */
			new Attribute("codebase", Dict.VERS_HTML40, AttrCheckImpl.getCheckUrl()), /* OBJECT */
			new Attribute("codetype", Dict.VERS_HTML40, null), /* OBJECT */
			new Attribute("color", Dict.VERS_LOOSE, null), /* BASEFONT, FONT */
			new Attribute("cols", Dict.VERS_IFRAMES, null), /* TABLE & FRAMESET */
			new Attribute("colspan", Dict.VERS_FROM32, null),
			new Attribute("compact", Dict.VERS_ALL, AttrCheckImpl.getCheckBool()), /* lists */
			new Attribute("content", Dict.VERS_ALL, null), /* META */
			new Attribute("coords", Dict.VERS_FROM32, null), /* AREA, A */
			new Attribute("data", Dict.VERS_HTML40, AttrCheckImpl.getCheckUrl()), /* OBJECT */
			new Attribute("datafld", Dict.VERS_MICROSOFT, null), /* used on DIV, IMG */
			new Attribute("dataformatas", Dict.VERS_MICROSOFT, null), /* used on DIV, IMG */
			new Attribute("datapagesize", Dict.VERS_MICROSOFT, null), /* used on DIV, IMG */
			new Attribute("datasrc", Dict.VERS_MICROSOFT, AttrCheckImpl.getCheckUrl()), /* used on TABLE */
			new Attribute("datetime", Dict.VERS_HTML40, null), /* INS, DEL */
			new Attribute("declare", Dict.VERS_HTML40, AttrCheckImpl.getCheckBool()), /* OBJECT */
			new Attribute("defer", Dict.VERS_HTML40, AttrCheckImpl.getCheckBool()), /* SCRIPT */
			new Attribute("dir", Dict.VERS_HTML40, null), /* ltr or rtl */
			new Attribute("disabled", Dict.VERS_HTML40, AttrCheckImpl.getCheckBool()), /* form fields */
			new Attribute("enctype", Dict.VERS_ALL, null), /* FORM */
			new Attribute("face", Dict.VERS_LOOSE, null), /* BASEFONT, FONT */
			new Attribute("for", Dict.VERS_HTML40, null), /* LABEL */
			new Attribute("frame", Dict.VERS_HTML40, null), /* TABLE */
			new Attribute("frameborder", Dict.VERS_FRAMES, null), /* 0 or 1 */
			new Attribute("framespacing", Dict.VERS_PROPRIETARY, null), /* pixel value */
			new Attribute("gridx", Dict.VERS_PROPRIETARY, null), /* TABLE Adobe golive*/
			new Attribute("gridy", Dict.VERS_PROPRIETARY, null), /* TABLE Adobe golive */
			new Attribute("headers", Dict.VERS_HTML40, null), /* table cells */
			new Attribute("height", Dict.VERS_ALL, null), /* pixels only for TH/TD */
			new Attribute("href", Dict.VERS_ALL, AttrCheckImpl.getCheckUrl()), /* A, AREA, LINK and BASE */
			new Attribute("hreflang", Dict.VERS_HTML40, null), /* A, LINK */
			new Attribute("hspace", Dict.VERS_ALL, null), /* APPLET, IMG, OBJECT */
			new Attribute("http-equiv", Dict.VERS_ALL, null), /* META */
			new Attribute("id", Dict.VERS_HTML40, AttrCheckImpl.getCheckId()),
			new Attribute("ismap", Dict.VERS_ALL, AttrCheckImpl.getCheckBool()), /* IMG */
			new Attribute("label", Dict.VERS_HTML40, null), /* OPT, OPTGROUP */
			new Attribute("lang", Dict.VERS_HTML40, null), new Attribute("language", Dict.VERS_LOOSE, null), /* SCRIPT */
			new Attribute("last_modified", Dict.VERS_NETSCAPE, null), /* A */
			new Attribute("last_visit", Dict.VERS_NETSCAPE, null), /* A */
			new Attribute("leftmargin", Dict.VERS_MICROSOFT, null), /* used on BODY */
			new Attribute("link", Dict.VERS_LOOSE, null), /* BODY */
			new Attribute("longdesc", Dict.VERS_HTML40, AttrCheckImpl.getCheckUrl()), /* IMG */
			new Attribute("lowsrc", Dict.VERS_PROPRIETARY, AttrCheckImpl.getCheckUrl()), /* IMG */
			new Attribute("marginheight", Dict.VERS_IFRAMES, null), /* FRAME, IFRAME, BODY */
			new Attribute("marginwidth", Dict.VERS_IFRAMES, null), /* ditto */
			new Attribute("maxlength", Dict.VERS_ALL, null), /* INPUT */
			new Attribute("media", Dict.VERS_HTML40, null), /* STYLE, LINK */
			new Attribute("method", Dict.VERS_ALL, null), /* FORM: get or post */
			new Attribute("multiple", Dict.VERS_ALL, AttrCheckImpl.getCheckBool()), /* SELECT */
			new Attribute("name", Dict.VERS_ALL, AttrCheckImpl.getCheckName()),
			new Attribute("nohref", Dict.VERS_FROM32, AttrCheckImpl.getCheckBool()), /* AREA */
			new Attribute("noresize", Dict.VERS_FRAMES, AttrCheckImpl.getCheckBool()), /* FRAME */
			new Attribute("noshade", Dict.VERS_LOOSE, AttrCheckImpl.getCheckBool()), /* HR */
			new Attribute("nowrap", Dict.VERS_LOOSE, AttrCheckImpl.getCheckBool()), /* table cells */
			new Attribute("object", Dict.VERS_HTML40_LOOSE, null), /* APPLET */
			new Attribute("onblur", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onchange", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onclick", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("ondblclick", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onkeydown", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onkeypress", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onkeyup", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onload", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onmousedown", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onmousemove", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onmouseout", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onmouseover", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onmouseup", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onsubmit", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onreset", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onselect", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onunload", Dict.VERS_HTML40, AttrCheckImpl.getCheckScript()), /* event */
			new Attribute("onafterupdate", Dict.VERS_MICROSOFT, AttrCheckImpl.getCheckScript()), /* form fields */
			new Attribute("onbeforeupdate", Dict.VERS_MICROSOFT, AttrCheckImpl.getCheckScript()), /* form fields */
			new Attribute("onerrorupdate", Dict.VERS_MICROSOFT, AttrCheckImpl.getCheckScript()), /* form fields */
			new Attribute("onrowenter", Dict.VERS_MICROSOFT, AttrCheckImpl.getCheckScript()), /* form fields */
			new Attribute("onrowexit", Dict.VERS_MICROSOFT, AttrCheckImpl.getCheckScript()), /* form fields */
			new Attribute("onbeforeunload", Dict.VERS_MICROSOFT, AttrCheckImpl.getCheckScript()), /* form fields */
			new Attribute("ondatasetchanged", Dict.VERS_MICROSOFT, AttrCheckImpl.getCheckScript()), /* object, applet */
			new Attribute("ondataavailable", Dict.VERS_MICROSOFT, AttrCheckImpl.getCheckScript()), /* object, applet */
			new Attribute("ondatasetcomplete", Dict.VERS_MICROSOFT, AttrCheckImpl.getCheckScript()), /* object, applet */
			new Attribute("profile", Dict.VERS_HTML40, AttrCheckImpl.getCheckUrl()), /* HEAD */
			new Attribute("prompt", Dict.VERS_LOOSE, null), /* ISINDEX */
			new Attribute("readonly", Dict.VERS_HTML40, AttrCheckImpl.getCheckBool()), /* form fields */
			new Attribute("rel", Dict.VERS_ALL, null), /* A, LINK */
			new Attribute("rev", Dict.VERS_ALL, null), /* A, LINK */
			new Attribute("rightmargin", Dict.VERS_MICROSOFT, null), /* used on BODY */
			new Attribute("rows", Dict.VERS_ALL, null), /* TEXTAREA */
			new Attribute("rowspan", Dict.VERS_ALL, null), /* table cells */
			new Attribute("rules", Dict.VERS_HTML40, null), /* TABLE */
			new Attribute("scheme", Dict.VERS_HTML40, null), /* META */
			new Attribute("scope", Dict.VERS_HTML40, null), /* table cells */
			new Attribute("scrolling", Dict.VERS_IFRAMES, null), /* yes, no or auto */
			new Attribute("selected", Dict.VERS_ALL, AttrCheckImpl.getCheckBool()), /* OPTION */
			new Attribute("shape", Dict.VERS_FROM32, null), /* AREA, A */
			new Attribute("showgrid", Dict.VERS_PROPRIETARY, AttrCheckImpl.getCheckBool()), /* TABLE Adobe golive */
			new Attribute("showgridx", Dict.VERS_PROPRIETARY, AttrCheckImpl.getCheckBool()), /* TABLE Adobe golive*/
			new Attribute("showgridy", Dict.VERS_PROPRIETARY, AttrCheckImpl.getCheckBool()), /* TABLE Adobe golive*/
			new Attribute("size", Dict.VERS_LOOSE, null), /* HR, FONT, BASEFONT, SELECT */
			new Attribute("span", Dict.VERS_HTML40, null), /* COL, COLGROUP */
			new Attribute("src", (short) (Dict.VERS_ALL | Dict.VERS_FRAMES), AttrCheckImpl.getCheckUrl()), /* IMG, FRAME, IFRAME */
			new Attribute("standby", Dict.VERS_HTML40, null), /* OBJECT */
			new Attribute("start", Dict.VERS_ALL, null), /* OL */
			new Attribute("style", Dict.VERS_HTML40, null), new Attribute("summary", Dict.VERS_HTML40, null), /* TABLE */
			new Attribute("tabindex", Dict.VERS_HTML40, null), /* fields, OBJECT  and A */
			new Attribute("target", Dict.VERS_HTML40, null), /* names a frame/window */
			new Attribute("text", Dict.VERS_LOOSE, null), /* BODY */
			new Attribute("title", Dict.VERS_HTML40, null), /* text tool tip */
			new Attribute("topmargin", Dict.VERS_MICROSOFT, null), /* used on BODY */
			new Attribute("type", Dict.VERS_FROM32, null), /* also used by SPACER */
			new Attribute("usemap", Dict.VERS_ALL, AttrCheckImpl.getCheckBool()), /* things with images */
			new Attribute("valign", Dict.VERS_FROM32, AttrCheckImpl.getCheckValign()),
			new Attribute("value", Dict.VERS_ALL, null), /* OPTION, PARAM */
			new Attribute("valuetype", Dict.VERS_HTML40, null), /* PARAM: data, ref, object */
			new Attribute("version", Dict.VERS_ALL, null), /* HTML */
			new Attribute("vlink", Dict.VERS_LOOSE, null), /* BODY */
			new Attribute("vspace", Dict.VERS_LOOSE, null), /* IMG, OBJECT, APPLET */
			new Attribute("width", Dict.VERS_ALL, null), /* pixels only for TD/TH */
			new Attribute("wrap", Dict.VERS_NETSCAPE, null), /* textarea */
			new Attribute("xml:lang", Dict.VERS_XML, null), /* XML language */
			new Attribute("xmlns", Dict.VERS_ALL, null), /* name space */

	};

	public static Attribute attrHref = null;
	public static Attribute attrSrc = null;
	public static Attribute attrId = null;
	public static Attribute attrName = null;
	public static Attribute attrSummary = null;
	public static Attribute attrAlt = null;
	public static Attribute attrLongdesc = null;
	public static Attribute attrUsemap = null;
	public static Attribute attrIsmap = null;
	public static Attribute attrLanguage = null;
	public static Attribute attrType = null;
	public static Attribute attrTitle = null;
	public static Attribute attrXmlns = null;
	public static Attribute attrValue = null;
	public static Attribute attrContent = null;
	public static Attribute attrDatafld = null;
	public static Attribute attrWidth = null;
	public static Attribute attrHeight = null;

	public static AttributeTable getDefaultAttributeTable() {
		if (defaultAttributeTable == null) {
			defaultAttributeTable = new AttributeTable();
			for (int i = 0; i < attrs.length; i++) {
				defaultAttributeTable.install(attrs[i]);
			}
			attrHref = defaultAttributeTable.lookup("href");
			attrSrc = defaultAttributeTable.lookup("src");
			attrId = defaultAttributeTable.lookup("id");
			attrName = defaultAttributeTable.lookup("name");
			attrSummary = defaultAttributeTable.lookup("summary");
			attrAlt = defaultAttributeTable.lookup("alt");
			attrLongdesc = defaultAttributeTable.lookup("longdesc");
			attrUsemap = defaultAttributeTable.lookup("usemap");
			attrIsmap = defaultAttributeTable.lookup("ismap");
			attrLanguage = defaultAttributeTable.lookup("language");
			attrType = defaultAttributeTable.lookup("type");
			attrTitle = defaultAttributeTable.lookup("title");
			attrXmlns = defaultAttributeTable.lookup("xmlns");
			attrValue = defaultAttributeTable.lookup("value");
			attrContent = defaultAttributeTable.lookup("content");
			attrDatafld = defaultAttributeTable.lookup("datafld");
			;
			attrWidth = defaultAttributeTable.lookup("width");
			;
			attrHeight = defaultAttributeTable.lookup("height");
			;

			attrAlt.nowrap = true;
			attrValue.nowrap = true;
			attrContent.nowrap = true;
		}
		return defaultAttributeTable;
	}

}