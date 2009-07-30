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
 * @(#)Tidy.java   1.11 2000/08/16
 *
 */

/*
 HTML parser and pretty printer

 Copyright (c) 1998-2000 World Wide Web Consortium (Massachusetts
 Institute of Technology, Institut National de Recherche en
 Informatique et en Automatique, Keio University). All Rights
 Reserved.

 Contributing Author(s):

 Dave Raggett <dsr@w3.org>
 Andy Quick <ac.quick@sympatico.ca> (translation to Java)

 The contributing author(s) would like to thank all those who
 helped with testing, bug fixes, and patience.  This wouldn't
 have been possible without all of you.

 COPYRIGHT NOTICE:
 
 This software and documentation is provided "as is," and
 the copyright holders and contributing author(s) make no
 representations or warranties, express or implied, including
 but not limited to, warranties of merchantability or fitness
 for any particular purpose or that the use of the software or
 documentation will not infringe any third party patents,
 copyrights, trademarks or other rights. 

 The copyright holders and contributing author(s) will not be
 liable for any direct, indirect, special or consequential damages
 arising out of any use of the software or documentation, even if
 advised of the possibility of such damage.

 Permission is hereby granted to use, copy, modify, and distribute
 this source code, or portions hereof, documentation and executables,
 for any purpose, without fee, subject to the following restrictions:

 1. The origin of this source code must not be misrepresented.
 2. Altered versions must be plainly marked as such and must
 not be misrepresented as being the original source.
 3. This Copyright notice may not be removed or altered from any
 source or altered source distribution.
 
 The copyright holders and contributing author(s) specifically
 permit, without fee, and encourage the use of this source code
 as a component for supporting the Hypertext Markup Language in
 commercial products. If you use this source code in a product,
 acknowledgment is not required but would be appreciated.
 */

package de.juwimm.util.tidy;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import java.io.IOException;
import java.io.FileNotFoundException;

/**
 *
 * <p>HTML parser and pretty printer</p>
 *
 * <p>
 * (c) 1998-2000 (W3C) MIT, INRIA, Keio University
 * See Tidy.java for the copyright notice.
 * Derived from <a href="http://www.w3.org/People/Raggett/tidy">
 * HTML Tidy Release 4 Aug 2000</a>
 * </p>
 *
 * <p>
 * Copyright (c) 1998-2000 World Wide Web Consortium (Massachusetts
 * Institute of Technology, Institut National de Recherche en
 * Informatique et en Automatique, Keio University). All Rights
 * Reserved.
 * </p>
 *
 * <p>
 * Contributing Author(s):<br>
 *    <a href="mailto:dsr@w3.org">Dave Raggett</a><br>
 *    <a href="mailto:ac.quick@sympatico.ca">Andy Quick</a> (translation to Java)
 * </p>
 *
 * <p>
 * The contributing author(s) would like to thank all those who
 * helped with testing, bug fixes, and patience.  This wouldn't
 * have been possible without all of you.
 * </p>
 *
 * <p>
 * COPYRIGHT NOTICE:<br>
 * 
 * This software and documentation is provided "as is," and
 * the copyright holders and contributing author(s) make no
 * representations or warranties, express or implied, including
 * but not limited to, warranties of merchantability or fitness
 * for any particular purpose or that the use of the software or
 * documentation will not infringe any third party patents,
 * copyrights, trademarks or other rights. 
 * </p>
 *
 * <p>
 * The copyright holders and contributing author(s) will not be
 * liable for any direct, indirect, special or consequential damages
 * arising out of any use of the software or documentation, even if
 * advised of the possibility of such damage.
 * </p>
 *
 * <p>
 * Permission is hereby granted to use, copy, modify, and distribute
 * this source code, or portions hereof, documentation and executables,
 * for any purpose, without fee, subject to the following restrictions:
 * </p>
 *
 * <p>
 * <ol>
 * <li>The origin of this source code must not be misrepresented.</li>
 * <li>Altered versions must be plainly marked as such and must
 * not be misrepresented as being the original source.</li>
 * <li>This Copyright notice may not be removed or altered from any
 * source or altered source distribution.</li>
 * </ol>
 * </p>
 *
 * <p>
 * The copyright holders and contributing author(s) specifically
 * permit, without fee, and encourage the use of this source code
 * as a component for supporting the Hypertext Markup Language in
 * commercial products. If you use this source code in a product,
 * acknowledgment is not required but would be appreciated.
 * </p>
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
 *
 */

public class Tidy implements java.io.Serializable {

	static final long serialVersionUID = -2794371560623987718L;

	private boolean initialized = false;
	private PrintWriter errout = null; /* error output stream */
	private PrintWriter stderr = null;
	private Configuration configuration = null;
	private String inputStreamName = "InputStream";
	private int parseErrors = 0;
	private int parseWarnings = 0;

	public Tidy() {
		init();
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public PrintWriter getStderr() {
		return stderr;
	}

	/**
	 * ParseErrors - the number of errors that occurred in the most
	 * recent parse operation
	 */

	public int getParseErrors() {
		return parseErrors;
	}

	/**
	 * ParseWarnings - the number of warnings that occurred in the most
	 * recent parse operation
	 */

	public int getParseWarnings() {
		return parseWarnings;
	}

	/**
	 * Errout - the error output stream
	 */

	public PrintWriter getErrout() {
		return errout;
	}

	public void setErrout(PrintWriter errout) {
		this.errout = errout;
	}

	/**
	 * Spaces - default indentation
	 * @see de.juwimm.util.tidy.Configuration#spaces
	 */

	public void setSpaces(int spaces) {
		configuration.spaces = spaces;
	}

	public int getSpaces() {
		return configuration.spaces;
	}

	/**
	 * Wraplen - default wrap margin
	 * @see de.juwimm.util.tidy.Configuration#wraplen
	 */

	public void setWraplen(int wraplen) {
		configuration.wraplen = wraplen;
	}

	public int getWraplen() {
		return configuration.wraplen;
	}

	/**
	 * CharEncoding
	 * @see de.juwimm.util.tidy.Configuration#CharEncoding
	 */

	public void setCharEncoding(int charencoding) {
		configuration.CharEncoding = charencoding;
	}

	public int getCharEncoding() {
		return configuration.CharEncoding;
	}

	/**
	 * Tabsize
	 * @see de.juwimm.util.tidy.Configuration#tabsize
	 */

	public void setTabsize(int tabsize) {
		configuration.tabsize = tabsize;
	}

	public int getTabsize() {
		return configuration.tabsize;
	}

	/**
	 * Errfile - file name to write errors to
	 * @see de.juwimm.util.tidy.Configuration#errfile
	 */

	public void setErrfile(String errfile) {
		configuration.errfile = errfile;
	}

	public String getErrfile() {
		return configuration.errfile;
	}

	/**
	 * Writeback - if true then output tidied markup
	 * NOTE: this property is ignored when parsing from an InputStream.
	 * @see de.juwimm.util.tidy.Configuration#writeback
	 */

	public void setWriteback(boolean writeback) {
		configuration.writeback = writeback;
	}

	public boolean getWriteback() {
		return configuration.writeback;
	}

	/**
	 * OnlyErrors - if true normal output is suppressed
	 * @see de.juwimm.util.tidy.Configuration#OnlyErrors
	 */

	public void setOnlyErrors(boolean OnlyErrors) {
		configuration.OnlyErrors = OnlyErrors;
	}

	public boolean getOnlyErrors() {
		return configuration.OnlyErrors;
	}

	/**
	 * ShowWarnings - however errors are always shown
	 * @see de.juwimm.util.tidy.Configuration#ShowWarnings
	 */

	public void setShowWarnings(boolean ShowWarnings) {
		configuration.ShowWarnings = ShowWarnings;
	}

	public boolean getShowWarnings() {
		return configuration.ShowWarnings;
	}

	/**
	 * Quiet - no 'Parsing X', guessed DTD or summary
	 * @see de.juwimm.util.tidy.Configuration#Quiet
	 */

	public void setQuiet(boolean Quiet) {
		configuration.Quiet = Quiet;
	}

	public boolean getQuiet() {
		return configuration.Quiet;
	}

	/**
	 * IndentContent - indent content of appropriate tags
	 * @see de.juwimm.util.tidy.Configuration#IndentContent
	 */

	public void setIndentContent(boolean IndentContent) {
		configuration.IndentContent = IndentContent;
	}

	public boolean getIndentContent() {
		return configuration.IndentContent;
	}

	/**
	 * SmartIndent - does text/block level content effect indentation
	 * @see de.juwimm.util.tidy.Configuration#SmartIndent
	 */

	public void setSmartIndent(boolean SmartIndent) {
		configuration.SmartIndent = SmartIndent;
	}

	public boolean getSmartIndent() {
		return configuration.SmartIndent;
	}

	/**
	 * HideEndTags - suppress optional end tags
	 * @see de.juwimm.util.tidy.Configuration#HideEndTags
	 */

	public void setHideEndTags(boolean HideEndTags) {
		configuration.HideEndTags = HideEndTags;
	}

	public boolean getHideEndTags() {
		return configuration.HideEndTags;
	}

	/**
	 * XmlTags - treat input as XML
	 * @see de.juwimm.util.tidy.Configuration#XmlTags
	 */

	public void setXmlTags(boolean XmlTags) {
		configuration.XmlTags = XmlTags;
	}

	public boolean getXmlTags() {
		return configuration.XmlTags;
	}

	/**
	 * XmlOut - create output as XML
	 * @see de.juwimm.util.tidy.Configuration#XmlOut
	 */

	public void setXmlOut(boolean XmlOut) {
		configuration.XmlOut = XmlOut;
	}

	public boolean getXmlOut() {
		return configuration.XmlOut;
	}

	/**
	 * XHTML - output extensible HTML
	 * @see de.juwimm.util.tidy.Configuration#xHTML
	 */

	public void setXHTML(boolean xHTML) {
		configuration.xHTML = xHTML;
	}

	public boolean getXHTML() {
		return configuration.xHTML;
	}

	/**
	 * RawOut - avoid mapping values > 127 to entities
	 * @see de.juwimm.util.tidy.Configuration#RawOut
	 */

	public void setRawOut(boolean RawOut) {
		configuration.RawOut = RawOut;
	}

	public boolean getRawOut() {
		return configuration.RawOut;
	}

	/**
	 * UpperCaseTags - output tags in upper not lower case
	 * @see de.juwimm.util.tidy.Configuration#UpperCaseTags
	 */

	public void setUpperCaseTags(boolean UpperCaseTags) {
		configuration.UpperCaseTags = UpperCaseTags;
	}

	public boolean getUpperCaseTags() {
		return configuration.UpperCaseTags;
	}

	/**
	 * UpperCaseAttrs - output attributes in upper not lower case
	 * @see de.juwimm.util.tidy.Configuration#UpperCaseAttrs
	 */

	public void setUpperCaseAttrs(boolean UpperCaseAttrs) {
		configuration.UpperCaseAttrs = UpperCaseAttrs;
	}

	public boolean getUpperCaseAttrs() {
		return configuration.UpperCaseAttrs;
	}

	/**
	 * MakeClean - remove presentational clutter
	 * @see de.juwimm.util.tidy.Configuration#MakeClean
	 */

	public void setMakeClean(boolean MakeClean) {
		configuration.MakeClean = MakeClean;
	}

	public boolean getMakeClean() {
		return configuration.MakeClean;
	}

	/**
	 * BreakBeforeBR - o/p newline before &lt;br&gt; or not?
	 * @see de.juwimm.util.tidy.Configuration#BreakBeforeBR
	 */

	public void setBreakBeforeBR(boolean BreakBeforeBR) {
		configuration.BreakBeforeBR = BreakBeforeBR;
	}

	public boolean getBreakBeforeBR() {
		return configuration.BreakBeforeBR;
	}

	/**
	 * BurstSlides - create slides on each h2 element
	 * @see de.juwimm.util.tidy.Configuration#BurstSlides
	 */

	public void setBurstSlides(boolean BurstSlides) {
		configuration.BurstSlides = BurstSlides;
	}

	public boolean getBurstSlides() {
		return configuration.BurstSlides;
	}

	/**
	 * NumEntities - use numeric entities
	 * @see de.juwimm.util.tidy.Configuration#NumEntities
	 */

	public void setNumEntities(boolean NumEntities) {
		configuration.NumEntities = NumEntities;
	}

	public boolean getNumEntities() {
		return configuration.NumEntities;
	}

	/**
	 * QuoteMarks - output " marks as &amp;quot;
	 * @see de.juwimm.util.tidy.Configuration#QuoteMarks
	 */

	public void setQuoteMarks(boolean QuoteMarks) {
		configuration.QuoteMarks = QuoteMarks;
	}

	public boolean getQuoteMarks() {
		return configuration.QuoteMarks;
	}

	/**
	 * QuoteNbsp - output non-breaking space as entity
	 * @see de.juwimm.util.tidy.Configuration#QuoteNbsp
	 */

	public void setQuoteNbsp(boolean QuoteNbsp) {
		configuration.QuoteNbsp = QuoteNbsp;
	}

	public boolean getQuoteNbsp() {
		return configuration.QuoteNbsp;
	}

	/**
	 * QuoteAmpersand - output naked ampersand as &amp;
	 * @see de.juwimm.util.tidy.Configuration#QuoteAmpersand
	 */

	public void setQuoteAmpersand(boolean QuoteAmpersand) {
		configuration.QuoteAmpersand = QuoteAmpersand;
	}

	public boolean getQuoteAmpersand() {
		return configuration.QuoteAmpersand;
	}

	/**
	 * WrapAttVals - wrap within attribute values
	 * @see de.juwimm.util.tidy.Configuration#WrapAttVals
	 */

	public void setWrapAttVals(boolean WrapAttVals) {
		configuration.WrapAttVals = WrapAttVals;
	}

	public boolean getWrapAttVals() {
		return configuration.WrapAttVals;
	}

	/**
	 * WrapScriptlets - wrap within JavaScript string literals
	 * @see de.juwimm.util.tidy.Configuration#WrapScriptlets
	 */

	public void setWrapScriptlets(boolean WrapScriptlets) {
		configuration.WrapScriptlets = WrapScriptlets;
	}

	public boolean getWrapScriptlets() {
		return configuration.WrapScriptlets;
	}

	/**
	 * WrapSection - wrap within &lt;![ ... ]&gt; section tags
	 * @see de.juwimm.util.tidy.Configuration#WrapSection
	 */

	public void setWrapSection(boolean WrapSection) {
		configuration.WrapSection = WrapSection;
	}

	public boolean getWrapSection() {
		return configuration.WrapSection;
	}

	/**
	 * AltText - default text for alt attribute
	 * @see de.juwimm.util.tidy.Configuration#altText
	 */

	public void setAltText(String altText) {
		configuration.altText = altText;
	}

	public String getAltText() {
		return configuration.altText;
	}

	/**
	 * Slidestyle - style sheet for slides
	 * @see de.juwimm.util.tidy.Configuration#slidestyle
	 */

	public void setSlidestyle(String slidestyle) {
		configuration.slidestyle = slidestyle;
	}

	public String getSlidestyle() {
		return configuration.slidestyle;
	}

	/**
	 * XmlPi - add &lt;?xml?&gt; for XML docs
	 * @see de.juwimm.util.tidy.Configuration#XmlPi
	 */

	public void setXmlPi(boolean XmlPi) {
		configuration.XmlPi = XmlPi;
	}

	public boolean getXmlPi() {
		return configuration.XmlPi;
	}

	/**
	 * DropFontTags - discard presentation tags
	 * @see de.juwimm.util.tidy.Configuration#DropFontTags
	 */

	public void setDropFontTags(boolean DropFontTags) {
		configuration.DropFontTags = DropFontTags;
	}

	public boolean getDropFontTags() {
		return configuration.DropFontTags;
	}

	/**
	 * DropEmptyParas - discard empty p elements
	 * @see de.juwimm.util.tidy.Configuration#DropEmptyParas
	 */

	public void setDropEmptyParas(boolean DropEmptyParas) {
		configuration.DropEmptyParas = DropEmptyParas;
	}

	public boolean getDropEmptyParas() {
		return configuration.DropEmptyParas;
	}

	/**
	 * FixComments - fix comments with adjacent hyphens
	 * @see de.juwimm.util.tidy.Configuration#FixComments
	 */

	public void setFixComments(boolean FixComments) {
		configuration.FixComments = FixComments;
	}

	public boolean getFixComments() {
		return configuration.FixComments;
	}

	/**
	 * WrapAsp - wrap within ASP pseudo elements
	 * @see de.juwimm.util.tidy.Configuration#WrapAsp
	 */

	public void setWrapAsp(boolean WrapAsp) {
		configuration.WrapAsp = WrapAsp;
	}

	public boolean getWrapAsp() {
		return configuration.WrapAsp;
	}

	/**
	 * WrapJste - wrap within JSTE pseudo elements
	 * @see de.juwimm.util.tidy.Configuration#WrapJste
	 */

	public void setWrapJste(boolean WrapJste) {
		configuration.WrapJste = WrapJste;
	}

	public boolean getWrapJste() {
		return configuration.WrapJste;
	}

	/**
	 * WrapPhp - wrap within PHP pseudo elements
	 * @see de.juwimm.util.tidy.Configuration#WrapPhp
	 */

	public void setWrapPhp(boolean WrapPhp) {
		configuration.WrapPhp = WrapPhp;
	}

	public boolean getWrapPhp() {
		return configuration.WrapPhp;
	}

	/**
	 * FixBackslash - fix URLs by replacing \ with /
	 * @see de.juwimm.util.tidy.Configuration#FixBackslash
	 */

	public void setFixBackslash(boolean FixBackslash) {
		configuration.FixBackslash = FixBackslash;
	}

	public boolean getFixBackslash() {
		return configuration.FixBackslash;
	}

	/**
	 * IndentAttributes - newline+indent before each attribute
	 * @see de.juwimm.util.tidy.Configuration#IndentAttributes
	 */

	public void setIndentAttributes(boolean IndentAttributes) {
		configuration.IndentAttributes = IndentAttributes;
	}

	public boolean getIndentAttributes() {
		return configuration.IndentAttributes;
	}

	/**
	 * DocType - user specified doctype
	 * omit | auto | strict | loose | <i>fpi</i>
	 * where the <i>fpi</i> is a string similar to
	 *    &quot;-//ACME//DTD HTML 3.14159//EN&quot;
	 * Note: for <i>fpi</i> include the double-quotes in the string.
	 * @see de.juwimm.util.tidy.Configuration#docTypeStr
	 * @see de.juwimm.util.tidy.Configuration#docTypeMode
	 */

	public void setDocType(String doctype) {
		if (doctype != null) configuration.docTypeStr = configuration.parseDocType(doctype, "doctype");
	}

	public String getDocType() {
		String result = null;
		switch (configuration.docTypeMode) {
			case Configuration.DOCTYPE_OMIT:
				result = "omit";
				break;
			case Configuration.DOCTYPE_AUTO:
				result = "auto";
				break;
			case Configuration.DOCTYPE_STRICT:
				result = "strict";
				break;
			case Configuration.DOCTYPE_LOOSE:
				result = "loose";
				break;
			case Configuration.DOCTYPE_USER:
				result = configuration.docTypeStr;
				break;
		}
		return result;
	}

	/**
	 * LogicalEmphasis - replace i by em and b by strong
	 * @see de.juwimm.util.tidy.Configuration#LogicalEmphasis
	 */

	public void setLogicalEmphasis(boolean LogicalEmphasis) {
		configuration.LogicalEmphasis = LogicalEmphasis;
	}

	public boolean getLogicalEmphasis() {
		return configuration.LogicalEmphasis;
	}

	/**
	 * XmlPIs - if set to true PIs must end with ?>
	 * @see de.juwimm.util.tidy.Configuration#XmlPIs
	 */

	public void setXmlPIs(boolean XmlPIs) {
		configuration.XmlPIs = XmlPIs;
	}

	public boolean getXmlPIs() {
		return configuration.XmlPIs;
	}

	/**
	 * EncloseText - if true text at body is wrapped in &lt;p&gt;'s
	 * @see de.juwimm.util.tidy.Configuration#EncloseBodyText
	 */

	public void setEncloseText(boolean EncloseText) {
		configuration.EncloseBodyText = EncloseText;
	}

	public boolean getEncloseText() {
		return configuration.EncloseBodyText;
	}

	/**
	 * EncloseBlockText - if true text in blocks is wrapped in &lt;p&gt;'s
	 * @see de.juwimm.util.tidy.Configuration#EncloseBlockText
	 */

	public void setEncloseBlockText(boolean EncloseBlockText) {
		configuration.EncloseBlockText = EncloseBlockText;
	}

	public boolean getEncloseBlockText() {
		return configuration.EncloseBlockText;
	}

	/**
	 * KeepFileTimes - if true last modified time is preserved<br>
	 * <b>this is NOT supported at this time.</b>
	 * @see de.juwimm.util.tidy.Configuration#KeepFileTimes
	 */

	public void setKeepFileTimes(boolean KeepFileTimes) {
		configuration.KeepFileTimes = KeepFileTimes;
	}

	public boolean getKeepFileTimes() {
		return configuration.KeepFileTimes;
	}

	/**
	 * Word2000 - draconian cleaning for Word2000
	 * @see de.juwimm.util.tidy.Configuration#Word2000
	 */

	public void setWord2000(boolean Word2000) {
		configuration.Word2000 = Word2000;
	}

	public boolean getWord2000() {
		return configuration.Word2000;
	}

	/**
	 * TidyMark - add meta element indicating tidied doc
	 * @see de.juwimm.util.tidy.Configuration#TidyMark
	 */

	public void setTidyMark(boolean TidyMark) {
		configuration.TidyMark = TidyMark;
	}

	public boolean getTidyMark() {
		return configuration.TidyMark;
	}

	/**
	 * XmlSpace - if set to yes adds xml:space attr as needed
	 * @see de.juwimm.util.tidy.Configuration#XmlSpace
	 */

	public void setXmlSpace(boolean XmlSpace) {
		configuration.XmlSpace = XmlSpace;
	}

	public boolean getXmlSpace() {
		return configuration.XmlSpace;
	}

	/**
	 * Emacs - if true format error output for GNU Emacs
	 * @see de.juwimm.util.tidy.Configuration#Emacs
	 */

	public void setEmacs(boolean Emacs) {
		configuration.Emacs = Emacs;
	}

	public boolean getEmacs() {
		return configuration.Emacs;
	}

	/**
	 * LiteralAttribs - if true attributes may use newlines
	 * @see de.juwimm.util.tidy.Configuration#LiteralAttribs
	 */

	public void setLiteralAttribs(boolean LiteralAttribs) {
		configuration.LiteralAttribs = LiteralAttribs;
	}

	public boolean getLiteralAttribs() {
		return configuration.LiteralAttribs;
	}

	/**
	 * InputStreamName - the name of the input stream (printed in the
	 * header information).
	 */
	public void setInputStreamName(String name) {
		if (name != null) inputStreamName = name;
	}

	public String getInputStreamName() {
		return inputStreamName;
	}

	/**
	 * Sets the configuration from a configuration file.
	 */

	public void setConfigurationFromFile(String filename) {
		configuration.parseFile(filename);
	}

	/**
	 * Sets the configuration from a properties object.
	 */

	public void setConfigurationFromProps(Properties props) {
		configuration.addProps(props);
	}

	/**
	 * first time initialization which should
	 * precede reading the command line
	 */

	private void init() {
		configuration = new Configuration();
		if (configuration == null) return;

		AttributeTable at = AttributeTable.getDefaultAttributeTable();
		if (at == null) return;
		TagTable tt = new TagTable();
		if (tt == null) return;
		tt.setConfiguration(configuration);
		configuration.tt = tt;
		EntityTable et = EntityTable.getDefaultEntityTable();
		if (et == null) return;

		/* Unnecessary - same initial values in Configuration
		 Configuration.XmlTags       = false;
		 Configuration.XmlOut        = false;
		 Configuration.HideEndTags   = false;
		 Configuration.UpperCaseTags = false;
		 Configuration.MakeClean     = false;
		 Configuration.writeback     = false;
		 Configuration.OnlyErrors    = false;
		 */

		configuration.errfile = null;
		stderr = new PrintWriter(System.err, true);
		errout = stderr;
		initialized = true;
	}

	/**
	 * Parses InputStream in and returns the root Node.
	 * If out is non-null, pretty prints to OutputStream out.
	 */

	public Node parse(InputStream in, OutputStream out) {
		Node document = null;

		try {
			document = parse(in, null, out);
		} catch (FileNotFoundException fnfe) {
		} catch (IOException e) {
		}

		return document;
	}

	/**
	 * Internal routine that actually does the parsing.  The caller
	 * can pass either an InputStream or file name.  If both are passed,
	 * the file name is preferred.
	 */

	private Node parse(InputStream in, String file, OutputStream out) throws FileNotFoundException, IOException {
		Lexer lexer;
		Node document = null;
		Node doctype;
		Out o = new OutImpl(); /* normal output stream */
		PPrint pprint;

		if (!initialized) return null;

		if (errout == null) return null;

		parseErrors = 0;
		parseWarnings = 0;

		/* ensure config is self-consistent */
		configuration.adjust();

		if (file != null) {
			in = new FileInputStream(file);
			inputStreamName = file;
		} else if (in == null) {
			in = System.in;
			inputStreamName = "stdin";
		}

		if (in != null) {
			lexer = new Lexer(new StreamInImpl(in, configuration.CharEncoding, configuration.tabsize), configuration);
			lexer.errout = errout;

			/*
			 store pointer to lexer in input stream
			 to allow character encoding errors to be
			 reported
			 */
			lexer.in.lexer = lexer;

			/* Tidy doesn't alter the doctype for generic XML docs */
			if (configuration.XmlTags)
				document = ParserImpl.parseXMLDocument(lexer);
			else {
				lexer.warnings = 0;
				if (!configuration.Quiet) Report.helloMessage(errout, Report.RELEASE_DATE, inputStreamName);

				document = ParserImpl.parseDocument(lexer);

				if (!document.checkNodeIntegrity()) {
					Report.badTree(errout);
					return null;
				}

				Clean cleaner = new Clean(configuration.tt);

				/* simplifies <b><b> ... </b> ...</b> etc. */
				cleaner.nestedEmphasis(document);

				/* cleans up <dir>indented text</dir> etc. */
				cleaner.list2BQ(document);
				cleaner.bQ2Div(document);

				/* replaces i by em and b by strong */
				if (configuration.LogicalEmphasis) cleaner.emFromI(document);

				if (configuration.Word2000 && cleaner.isWord2000(document, configuration.tt)) {
					/* prune Word2000's <![if ...]> ... <![endif]> */
					cleaner.dropSections(lexer, document);

					/* drop style & class attributes and empty p, span elements */
					cleaner.cleanWord2000(lexer, document);
				}

				/* replaces presentational markup by style rules */
				if (configuration.MakeClean || configuration.DropFontTags) cleaner.cleanTree(lexer, document);

				if (!document.checkNodeIntegrity()) {
					Report.badTree(errout);
					return null;
				}
				doctype = document.findDocType();
				if (document.content != null) {
					if (configuration.xHTML)
						lexer.setXHTMLDocType(document);
					else
						lexer.fixDocType(document);

					if (configuration.TidyMark) lexer.addGenerator(document);
				}

				/* ensure presence of initial <?XML version="1.0"?> */
				if (configuration.XmlOut && configuration.XmlPi) lexer.fixXMLPI(document);

				if (!configuration.Quiet && document.content != null) {
					Report.reportVersion(errout, lexer, inputStreamName, doctype);
					Report.reportNumWarnings(errout, lexer);
				}
			}

			parseWarnings = lexer.warnings;
			parseErrors = lexer.errors;

			// Try to close the InputStream but only if if we created it.

			if ((file != null) && (in != System.in)) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}

			if (lexer.errors > 0) Report.needsAuthorIntervention(errout);

			o.state = StreamIn.FSM_ASCII;
			o.encoding = configuration.CharEncoding;

			if (!configuration.OnlyErrors && lexer.errors == 0) {
				if (configuration.BurstSlides) {
					Node body;

					body = null;
					/*
					 remove doctype to avoid potential clash with
					 markup introduced when bursting into slides
					 */
					/* discard the document type */
					doctype = document.findDocType();

					if (doctype != null) Node.discardElement(doctype);

					/* slides use transitional features */
					lexer.versions |= Dict.VERS_HTML40_LOOSE;

					/* and patch up doctype to match */
					if (configuration.xHTML)
						lexer.setXHTMLDocType(document);
					else
						lexer.fixDocType(document);

					/* find the body element which may be implicit */
					body = document.findBody(configuration.tt);

					if (body != null) {
						pprint = new PPrint(configuration);
						Report.reportNumberOfSlides(errout, pprint.countSlides(body));
						pprint.createSlides(lexer, document);
					} else
						Report.missingBody(errout);
				} else if (configuration.writeback && (file != null)) {
					try {
						pprint = new PPrint(configuration);
						o.out = new FileOutputStream(file);

						if (configuration.XmlTags)
							pprint.printXMLTree(o, (short) 0, 0, lexer, document);
						else
							pprint.printTree(o, (short) 0, 0, lexer, document);

						pprint.flushLine(o, 0);
						o.out.close();
					} catch (IOException e) {
						errout.println(file + e.toString());
					}
				} else if (out != null) {
					pprint = new PPrint(configuration);
					o.out = out;

					if (configuration.XmlTags)
						pprint.printXMLTree(o, (short) 0, 0, lexer, document);
					else
						pprint.printTree(o, (short) 0, 0, lexer, document);

					pprint.flushLine(o, 0);
				}

			}

			Report.errorSummary(lexer);
		}
		return document;
	}

	/**
	 * Parses InputStream in and returns a DOM Document node.
	 * If out is non-null, pretty prints to OutputStream out.
	 */

	public org.w3c.dom.Document parseDOM(InputStream in, OutputStream out) {
		Node document = parse(in, out);
		if (document != null)
			return (org.w3c.dom.Document) document.getAdapter();
		else
			return null;
	}

	/**
	 * Creates an empty DOM Document.
	 */

	public static org.w3c.dom.Document createEmptyDocument() {
		Node document = new Node(Node.RootNode, new byte[0], 0, 0);
		Node node = new Node(Node.StartTag, new byte[0], 0, 0, "html", new TagTable());
		if (document != null && node != null) {
			Node.insertNodeAtStart(document, node);
			return (org.w3c.dom.Document) document.getAdapter();
		} else {
			return null;
		}
	}

	/**
	 * Pretty-prints a DOM Document.
	 */

	public void pprint(org.w3c.dom.Document doc, OutputStream out) {
		Out o = new OutImpl();
		PPrint pprint;
		Node document;

		if (!(doc instanceof DOMDocumentImpl)) { return; }
		document = ((DOMDocumentImpl) doc).adaptee;

		o.state = StreamIn.FSM_ASCII;
		o.encoding = configuration.CharEncoding;

		if (out != null) {
			pprint = new PPrint(configuration);
			o.out = out;

			if (configuration.XmlTags)
				pprint.printXMLTree(o, (short) 0, 0, null, document);
			else
				pprint.printTree(o, (short) 0, 0, null, document);

			pprint.flushLine(o, 0);
		}
	}

	/**
	 * Command line interface to parser and pretty printer.
	 */

	public static void main(String[] argv) {
		int totalerrors = 0;
		int totalwarnings = 0;
		String file;
		InputStream in;
		String prog = "Tidy";
		Node document;
		Node doctype;
		Lexer lexer;
		String s;
		Out out = new OutImpl(); /* normal output stream */
		PPrint pprint;
		int argc = argv.length + 1;
		int argIndex = 0;
		Tidy tidy;
		Configuration configuration;
		String arg;
		String current_errorfile = "stderr";

		tidy = new Tidy();
		configuration = tidy.getConfiguration();

		/* read command line */

		while (argc > 0) {
			if (argc > 1 && argv[argIndex].startsWith("-")) {
				/* support -foo and --foo */
				arg = argv[argIndex].substring(1);

				if (arg.length() > 0 && arg.charAt(0) == '-') arg = arg.substring(1);

				if (arg.equals("xml"))
					configuration.XmlTags = true;
				else if (arg.equals("asxml") || arg.equals("asxhtml"))
					configuration.xHTML = true;
				else if (arg.equals("indent")) {
					configuration.IndentContent = true;
					configuration.SmartIndent = true;
				} else if (arg.equals("omit"))
					configuration.HideEndTags = true;
				else if (arg.equals("upper"))
					configuration.UpperCaseTags = true;
				else if (arg.equals("clean"))
					configuration.MakeClean = true;
				else if (arg.equals("raw"))
					configuration.CharEncoding = Configuration.RAW;
				else if (arg.equals("ascii"))
					configuration.CharEncoding = Configuration.ASCII;
				else if (arg.equals("latin1"))
					configuration.CharEncoding = Configuration.LATIN1;
				else if (arg.equals("utf8"))
					configuration.CharEncoding = Configuration.UTF8;
				else if (arg.equals("iso2022"))
					configuration.CharEncoding = Configuration.ISO2022;
				else if (arg.equals("mac"))
					configuration.CharEncoding = Configuration.MACROMAN;
				else if (arg.equals("numeric"))
					configuration.NumEntities = true;
				else if (arg.equals("modify"))
					configuration.writeback = true;
				else if (arg.equals("change")) /* obsolete */
					configuration.writeback = true;
				else if (arg.equals("update")) /* obsolete */
					configuration.writeback = true;
				else if (arg.equals("errors"))
					configuration.OnlyErrors = true;
				else if (arg.equals("quiet"))
					configuration.Quiet = true;
				else if (arg.equals("slides"))
					configuration.BurstSlides = true;
				else if (arg.equals("help") || argv[argIndex].charAt(1) == '?' || argv[argIndex].charAt(1) == 'h') {
					Report.helpText(new PrintWriter(System.out, true), prog);
					System.exit(1);
				} else if (arg.equals("config")) {
					if (argc >= 3) {
						configuration.parseFile(argv[argIndex + 1]);
						--argc;
						++argIndex;
					}
				} else if (argv[argIndex].equals("-file") || argv[argIndex].equals("--file")
						|| argv[argIndex].equals("-f")) {
					if (argc >= 3) {
						configuration.errfile = argv[argIndex + 1];
						--argc;
						++argIndex;
					}
				} else if (argv[argIndex].equals("-wrap") || argv[argIndex].equals("--wrap")
						|| argv[argIndex].equals("-w")) {
					if (argc >= 3) {
						configuration.wraplen = Integer.parseInt(argv[argIndex + 1]);
						--argc;
						++argIndex;
					}
				} else if (argv[argIndex].equals("-version") || argv[argIndex].equals("--version")
						|| argv[argIndex].equals("-v")) {
					Report.showVersion(tidy.getErrout());
					System.exit(0);
				} else {
					s = argv[argIndex];

					for (int i = 1; i < s.length(); i++) {
						if (s.charAt(i) == 'i') {
							configuration.IndentContent = true;
							configuration.SmartIndent = true;
						} else if (s.charAt(i) == 'o')
							configuration.HideEndTags = true;
						else if (s.charAt(i) == 'u')
							configuration.UpperCaseTags = true;
						else if (s.charAt(i) == 'c')
							configuration.MakeClean = true;
						else if (s.charAt(i) == 'n')
							configuration.NumEntities = true;
						else if (s.charAt(i) == 'm')
							configuration.writeback = true;
						else if (s.charAt(i) == 'e')
							configuration.OnlyErrors = true;
						else if (s.charAt(i) == 'q')
							configuration.Quiet = true;
						else
							Report.unknownOption(tidy.getErrout(), s.charAt(i));
					}
				}

				--argc;
				++argIndex;
				continue;
			}

			/* ensure config is self-consistent */
			configuration.adjust();

			/* user specified error file */
			if (configuration.errfile != null) {
				/* is it same as the currently opened file? */
				if (!configuration.errfile.equals(current_errorfile)) {
					/* no so close previous error file */

					if (tidy.getErrout() != tidy.getStderr()) tidy.getErrout().close();

					/* and try to open the new error file */
					try {
						tidy.setErrout(new PrintWriter(new FileWriter(configuration.errfile), true));
						current_errorfile = configuration.errfile;
					} catch (IOException e) {
						/* can't be opened so fall back to stderr */
						current_errorfile = "stderr";
						tidy.setErrout(tidy.getStderr());
					}
				}
			}

			if (argc > 1) {
				file = argv[argIndex];
			} else {
				file = "stdin";
			}

			try {
				document = tidy.parse(null, file, System.out);
				totalwarnings += tidy.parseWarnings;
				totalerrors += tidy.parseErrors;
			} catch (FileNotFoundException fnfe) {
				Report.unknownFile(tidy.getErrout(), prog, file);
			} catch (IOException ioe) {
				Report.unknownFile(tidy.getErrout(), prog, file);
			}

			--argc;
			++argIndex;

			if (argc <= 1) break;
		}

		if (totalerrors + totalwarnings > 0) Report.generalInfo(tidy.getErrout());

		if (tidy.getErrout() != tidy.getStderr()) tidy.getErrout().close();

		/* return status can be used by scripts */

		if (totalerrors > 0) System.exit(2);

		if (totalwarnings > 0) System.exit(1);

		/* 0 signifies all is ok */
		System.exit(0);
	}
}