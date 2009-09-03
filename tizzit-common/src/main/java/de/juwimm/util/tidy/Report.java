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
 * @(#)Report.java   1.11 2000/08/16
 *
 */

package de.juwimm.util.tidy;

/**
 *
 * Error/informational message reporter.
 *
 * You should only need to edit the file TidyMessages.properties
 * to localize HTML tidy.
 *
 * (c) 1998-2000 (W3C) MIT, INRIA, Keio University
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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.io.PrintWriter;

public class Report {

	/* used to point to Web Accessibility Guidelines */
	public static final String ACCESS_URL = "http://www.w3.org/WAI/GL";

	public static final String RELEASE_DATE = "4th August 2000";

	public static String currentFile; /* sasdjb 01May00 for GNU Emacs error parsing */

	/* error codes for entities */

	public static final short MISSING_SEMICOLON = 1;
	public static final short UNKNOWN_ENTITY = 2;
	public static final short UNESCAPED_AMPERSAND = 3;

	/* error codes for element messages */

	public static final short MISSING_ENDTAG_FOR = 1;
	public static final short MISSING_ENDTAG_BEFORE = 2;
	public static final short DISCARDING_UNEXPECTED = 3;
	public static final short NESTED_EMPHASIS = 4;
	public static final short NON_MATCHING_ENDTAG = 5;
	public static final short TAG_NOT_ALLOWED_IN = 6;
	public static final short MISSING_STARTTAG = 7;
	public static final short UNEXPECTED_ENDTAG = 8;
	public static final short USING_BR_INPLACE_OF = 9;
	public static final short INSERTING_TAG = 10;
	public static final short SUSPECTED_MISSING_QUOTE = 11;
	public static final short MISSING_TITLE_ELEMENT = 12;
	public static final short DUPLICATE_FRAMESET = 13;
	public static final short CANT_BE_NESTED = 14;
	public static final short OBSOLETE_ELEMENT = 15;
	public static final short PROPRIETARY_ELEMENT = 16;
	public static final short UNKNOWN_ELEMENT = 17;
	public static final short TRIM_EMPTY_ELEMENT = 18;
	public static final short COERCE_TO_ENDTAG = 19;
	public static final short ILLEGAL_NESTING = 20;
	public static final short NOFRAMES_CONTENT = 21;
	public static final short CONTENT_AFTER_BODY = 22;
	public static final short INCONSISTENT_VERSION = 23;
	public static final short MALFORMED_COMMENT = 24;
	public static final short BAD_COMMENT_CHARS = 25;
	public static final short BAD_XML_COMMENT = 26;
	public static final short BAD_CDATA_CONTENT = 27;
	public static final short INCONSISTENT_NAMESPACE = 28;
	public static final short DOCTYPE_AFTER_TAGS = 29;
	public static final short MALFORMED_DOCTYPE = 30;
	public static final short UNEXPECTED_END_OF_FILE = 31;
	public static final short DTYPE_NOT_UPPER_CASE = 32;
	public static final short TOO_MANY_ELEMENTS = 33;

	/* error codes used for attribute messages */

	public static final short UNKNOWN_ATTRIBUTE = 1;
	public static final short MISSING_ATTRIBUTE = 2;
	public static final short MISSING_ATTR_VALUE = 3;
	public static final short BAD_ATTRIBUTE_VALUE = 4;
	public static final short UNEXPECTED_GT = 5;
	public static final short PROPRIETARY_ATTR_VALUE = 6;
	public static final short REPEATED_ATTRIBUTE = 7;
	public static final short MISSING_IMAGEMAP = 8;
	public static final short XML_ATTRIBUTE_VALUE = 9;
	public static final short UNEXPECTED_QUOTEMARK = 10;
	public static final short ID_NAME_MISMATCH = 11;

	/* accessibility flaws */

	public static final short MISSING_IMAGE_ALT = 1;
	public static final short MISSING_LINK_ALT = 2;
	public static final short MISSING_SUMMARY = 4;
	public static final short MISSING_IMAGE_MAP = 8;
	public static final short USING_FRAMES = 16;
	public static final short USING_NOFRAMES = 32;

	/* presentation flaws */

	public static final short USING_SPACER = 1;
	public static final short USING_LAYER = 2;
	public static final short USING_NOBR = 4;
	public static final short USING_FONT = 8;
	public static final short USING_BODY = 16;

	/* character encoding errors */
	public static final short WINDOWS_CHARS = 1;
	public static final short NON_ASCII = 2;
	public static final short FOUND_UTF16 = 4;

	private static short optionerrors;

	private static ResourceBundle res = null;

	public static void tidyPrint(PrintWriter p, String msg) {
		p.print(msg);
	}

	public static void tidyPrintln(PrintWriter p, String msg) {
		p.println(msg);
	}

	public static void tidyPrintln(PrintWriter p) {
		p.println();
	}

	public static void showVersion(PrintWriter p) {
		tidyPrintln(p, "Java HTML Tidy release date: " + RELEASE_DATE);
		tidyPrintln(p, "See http://www.w3.org/People/Raggett for details");
	}

	public static void tag(Lexer lexer, Node tag) {
		if (tag != null) {
			if (tag.type == Node.StartTag)
				tidyPrint(lexer.errout, "<" + tag.element + ">");
			else if (tag.type == Node.EndTag)
				tidyPrint(lexer.errout, "</" + tag.element + ">");
			else if (tag.type == Node.DocTypeTag)
				tidyPrint(lexer.errout, "<!DOCTYPE>");
			else if (tag.type == Node.TextNode)
				tidyPrint(lexer.errout, "plain text");
			else
				tidyPrint(lexer.errout, tag.element);
		}
	}

	/* lexer is not defined when this is called */
	public static void unknownOption(String option) {
		optionerrors++;
		try {
			System.err.println(MessageFormat.format(getRes().getString("unknown_option"), new Object[] {option}));
		} catch (MissingResourceException e) {
			System.err.println(e.toString());
		}
	}

	/* lexer is not defined when this is called */
	public static void badArgument(String option) {
		optionerrors++;
		try {
			System.err.println(MessageFormat.format(getRes().getString("bad_argument"), new Object[] {option}));
		} catch (MissingResourceException e) {
			System.err.println(e.toString());
		}
	}

	public static void position(Lexer lexer) {
		try {
			/* Change formatting to be parsable by GNU Emacs */
			if (lexer.configuration.Emacs) {
				tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("emacs_format"), new Object[] {currentFile, new Integer(lexer.lines), new Integer(lexer.columns)}));
				tidyPrint(lexer.errout, " ");
			} else /* traditional format */
			{
				tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("line_column"), new Object[] {new Integer(lexer.lines), new Integer(lexer.columns)}));
			}
		} catch (MissingResourceException e) {
			lexer.errout.println(e.toString());
		}
	}

	public static void encodingError(Lexer lexer, short code, int c) {
		lexer.warnings++;

		if (lexer.configuration.ShowWarnings) {
			position(lexer);

			if (code == WINDOWS_CHARS) {
				lexer.badChars |= WINDOWS_CHARS;
				try {
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("illegal_char"), new Object[] {new Integer(c)}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}

			tidyPrintln(lexer.errout);
		}
	}

	public static void entityError(Lexer lexer, short code, String entity, int c) {
		lexer.warnings++;

		if (lexer.configuration.ShowWarnings) {
			position(lexer);

			if (code == MISSING_SEMICOLON) {
				try {
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("missing_semicolon"), new Object[] {entity}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == UNKNOWN_ENTITY) {
				try {
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("unknown_entity"), new Object[] {entity}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == UNESCAPED_AMPERSAND) {
				try {
					tidyPrint(lexer.errout, getRes().getString("unescaped_ampersand"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}

			tidyPrintln(lexer.errout);
		}
	}

	public static void attrError(Lexer lexer, Node node, String attr, short code) {
		lexer.warnings++;

		/* keep quiet after 6 errors */
		if (lexer.errors > 6) return;

		if (lexer.configuration.ShowWarnings) {
			/* on end of file adjust reported position to end of input */
			if (code == UNEXPECTED_END_OF_FILE) {
				lexer.lines = lexer.in.curline;
				lexer.columns = lexer.in.curcol;
			}

			position(lexer);

			if (code == UNKNOWN_ATTRIBUTE) {
				try {
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("unknown_attribute"), new Object[] {attr}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == MISSING_ATTRIBUTE) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
					tag(lexer, node);
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("missing_attribute"), new Object[] {attr}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == MISSING_ATTR_VALUE) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
					tag(lexer, node);
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("missing_attr_value"), new Object[] {attr}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == MISSING_IMAGEMAP) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
					tag(lexer, node);
					tidyPrint(lexer.errout, getRes().getString("missing_imagemap"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				lexer.badAccess |= MISSING_IMAGE_MAP;
			} else if (code == BAD_ATTRIBUTE_VALUE) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
					tag(lexer, node);
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("bad_attribute_value"), new Object[] {attr}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == XML_ATTRIBUTE_VALUE) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
					tag(lexer, node);
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("xml_attribute_value"), new Object[] {attr}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == UNEXPECTED_GT) {
				try {
					tidyPrint(lexer.errout, getRes().getString("error"));
					tag(lexer, node);
					tidyPrint(lexer.errout, getRes().getString("unexpected_gt"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				lexer.errors++;;
			} else if (code == UNEXPECTED_QUOTEMARK) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
					tag(lexer, node);
					tidyPrint(lexer.errout, getRes().getString("unexpected_quotemark"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == REPEATED_ATTRIBUTE) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
					tag(lexer, node);
					tidyPrint(lexer.errout, getRes().getString("repeated_attribute"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == PROPRIETARY_ATTR_VALUE) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
					tag(lexer, node);
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("proprietary_attr_value"), new Object[] {attr}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == UNEXPECTED_END_OF_FILE) {
				try {
					tidyPrint(lexer.errout, getRes().getString("unexpected_end_of_file"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == ID_NAME_MISMATCH) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
					tag(lexer, node);
					tidyPrint(lexer.errout, getRes().getString("id_name_mismatch"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}

			tidyPrintln(lexer.errout);
		} else if (code == UNEXPECTED_GT) {
			position(lexer);
			try {
				tidyPrint(lexer.errout, getRes().getString("error"));
				tag(lexer, node);
				tidyPrint(lexer.errout, getRes().getString("unexpected_gt"));
			} catch (MissingResourceException e) {
				lexer.errout.println(e.toString());
			}
			tidyPrintln(lexer.errout);
			lexer.errors++;;
		}
	}

	public static void warning(Lexer lexer, Node element, Node node, short code) {

		TagTable tt = lexer.configuration.tt;

		lexer.warnings++;

		/* keep quiet after 6 errors */
		if (lexer.errors > 6) return;

		if (lexer.configuration.ShowWarnings) {
			/* on end of file adjust reported position to end of input */
			if (code == UNEXPECTED_END_OF_FILE) {
				lexer.lines = lexer.in.curline;
				lexer.columns = lexer.in.curcol;
			}

			position(lexer);

			if (code == MISSING_ENDTAG_FOR) {
				try {
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("missing_endtag_for"), new Object[] {element.element}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == MISSING_ENDTAG_BEFORE) {
				try {
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("missing_endtag_before"), new Object[] {element.element}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, node);
			} else if (code == DISCARDING_UNEXPECTED) {
				try {
					tidyPrint(lexer.errout, getRes().getString("discarding_unexpected"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, node);
			} else if (code == NESTED_EMPHASIS) {
				try {
					tidyPrint(lexer.errout, getRes().getString("nested_emphasis"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, node);
			} else if (code == COERCE_TO_ENDTAG) {
				try {
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("coerce_to_endtag"), new Object[] {element.element}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == NON_MATCHING_ENDTAG) {
				try {
					tidyPrint(lexer.errout, getRes().getString("non_matching_endtag_1"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, node);
				try {
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("non_matching_endtag_2"), new Object[] {element.element}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == TAG_NOT_ALLOWED_IN) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, node);
				try {
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("tag_not_allowed_in"), new Object[] {element.element}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == DOCTYPE_AFTER_TAGS) {
				try {
					tidyPrint(lexer.errout, getRes().getString("doctype_after_tags"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == MISSING_STARTTAG) {
				try {
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("missing_starttag"), new Object[] {node.element}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == UNEXPECTED_ENDTAG) {
				try {
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("unexpected_endtag"), new Object[] {node.element}));
					if (element != null) tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("unexpected_endtag_suffix"), new Object[] {element.element}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == TOO_MANY_ELEMENTS) {
				try {
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("too_many_elements"), new Object[] {node.element}));
					if (element != null) tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("too_many_elements_suffix"), new Object[] {element.element}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == USING_BR_INPLACE_OF) {
				try {
					tidyPrint(lexer.errout, getRes().getString("using_br_inplace_of"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, node);
			} else if (code == INSERTING_TAG) {
				try {
					tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("inserting_tag"), new Object[] {node.element}));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == CANT_BE_NESTED) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, node);
				try {
					tidyPrint(lexer.errout, getRes().getString("cant_be_nested"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == PROPRIETARY_ELEMENT) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, node);
				try {
					tidyPrint(lexer.errout, getRes().getString("proprietary_element"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}

				if (node.tag == tt.tagLayer)
					lexer.badLayout |= USING_LAYER;
				else if (node.tag == tt.tagSpacer)
					lexer.badLayout |= USING_SPACER;
				else if (node.tag == tt.tagNobr) lexer.badLayout |= USING_NOBR;
			} else if (code == OBSOLETE_ELEMENT) {
				try {
					if (element.tag != null && (element.tag.model & Dict.CM_OBSOLETE) != 0)
						tidyPrint(lexer.errout, getRes().getString("obsolete_element"));
					else
						tidyPrint(lexer.errout, getRes().getString("replacing_element"));

				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, element);
				try {
					tidyPrint(lexer.errout, getRes().getString("by"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, node);
			} else if (code == TRIM_EMPTY_ELEMENT) {
				try {
					tidyPrint(lexer.errout, getRes().getString("trim_empty_element"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, element);
			} else if (code == MISSING_TITLE_ELEMENT) {
				try {
					tidyPrint(lexer.errout, getRes().getString("missing_title_element"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == ILLEGAL_NESTING) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, element);
				try {
					tidyPrint(lexer.errout, getRes().getString("illegal_nesting"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == NOFRAMES_CONTENT) {
				try {
					tidyPrint(lexer.errout, getRes().getString("warning"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, node);
				try {
					tidyPrint(lexer.errout, getRes().getString("noframes_content"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == INCONSISTENT_VERSION) {
				try {
					tidyPrint(lexer.errout, getRes().getString("inconsistent_version"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == MALFORMED_DOCTYPE) {
				try {
					tidyPrint(lexer.errout, getRes().getString("malformed_doctype"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == CONTENT_AFTER_BODY) {
				try {
					tidyPrint(lexer.errout, getRes().getString("content_after_body"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == MALFORMED_COMMENT) {
				try {
					tidyPrint(lexer.errout, getRes().getString("malformed_comment"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == BAD_COMMENT_CHARS) {
				try {
					tidyPrint(lexer.errout, getRes().getString("bad_comment_chars"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == BAD_XML_COMMENT) {
				try {
					tidyPrint(lexer.errout, getRes().getString("bad_xml_comment"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == BAD_CDATA_CONTENT) {
				try {
					tidyPrint(lexer.errout, getRes().getString("bad_cdata_content"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == INCONSISTENT_NAMESPACE) {
				try {
					tidyPrint(lexer.errout, getRes().getString("inconsistent_namespace"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == DTYPE_NOT_UPPER_CASE) {
				try {
					tidyPrint(lexer.errout, getRes().getString("dtype_not_upper_case"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			} else if (code == UNEXPECTED_END_OF_FILE) {
				try {
					tidyPrint(lexer.errout, getRes().getString("unexpected_end_of_file"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
				tag(lexer, element);
			}

			tidyPrintln(lexer.errout);
		}
	}

	public static void error(Lexer lexer, Node element, Node node, short code) {
		lexer.warnings++;

		/* keep quiet after 6 errors */
		if (lexer.errors > 6) return;

		lexer.errors++;

		position(lexer);

		if (code == SUSPECTED_MISSING_QUOTE) {
			try {
				tidyPrint(lexer.errout, getRes().getString("suspected_missing_quote"));
			} catch (MissingResourceException e) {
				lexer.errout.println(e.toString());
			}
		} else if (code == DUPLICATE_FRAMESET) {
			try {
				tidyPrint(lexer.errout, getRes().getString("duplicate_frameset"));
			} catch (MissingResourceException e) {
				lexer.errout.println(e.toString());
			}
		} else if (code == UNKNOWN_ELEMENT) {
			try {
				tidyPrint(lexer.errout, getRes().getString("error"));
			} catch (MissingResourceException e) {
				lexer.errout.println(e.toString());
			}
			tag(lexer, node);
			try {
				tidyPrint(lexer.errout, getRes().getString("unknown_element"));
			} catch (MissingResourceException e) {
				lexer.errout.println(e.toString());
			}
		} else if (code == UNEXPECTED_ENDTAG) {
			try {
				tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("unexpected_endtag"), new Object[] {node.element}));
				if (element != null) tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("unexpected_endtag_suffix"), new Object[] {element.element}));
			} catch (MissingResourceException e) {
				lexer.errout.println(e.toString());
			}
		}

		tidyPrintln(lexer.errout);
	}

	public static void errorSummary(Lexer lexer) {
		/* adjust badAccess to that its null if frames are ok */
		if ((lexer.badAccess & (USING_FRAMES | USING_NOFRAMES)) != 0) {
			if (!(((lexer.badAccess & USING_FRAMES) != 0) && ((lexer.badAccess & USING_NOFRAMES) == 0))) lexer.badAccess &= ~(USING_FRAMES | USING_NOFRAMES);
		}

		if (lexer.badChars != 0) {
			if ((lexer.badChars & WINDOWS_CHARS) != 0) {
				try {
					tidyPrint(lexer.errout, getRes().getString("badchars_summary"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}
		}

		if (lexer.badForm != 0) {
			try {
				tidyPrint(lexer.errout, getRes().getString("badform_summary"));
			} catch (MissingResourceException e) {
				lexer.errout.println(e.toString());
			}
		}

		if (lexer.badAccess != 0) {
			if ((lexer.badAccess & MISSING_SUMMARY) != 0) {
				try {
					tidyPrint(lexer.errout, getRes().getString("badaccess_missing_summary"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}

			if ((lexer.badAccess & MISSING_IMAGE_ALT) != 0) {
				try {
					tidyPrint(lexer.errout, getRes().getString("badaccess_missing_image_alt"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}

			if ((lexer.badAccess & MISSING_IMAGE_MAP) != 0) {
				try {
					tidyPrint(lexer.errout, getRes().getString("badaccess_missing_image_map"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}

			if ((lexer.badAccess & MISSING_LINK_ALT) != 0) {
				try {
					tidyPrint(lexer.errout, getRes().getString("badaccess_missing_link_alt"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}

			if (((lexer.badAccess & USING_FRAMES) != 0) && ((lexer.badAccess & USING_NOFRAMES) == 0)) {
				try {
					tidyPrint(lexer.errout, getRes().getString("badaccess_frames"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}

			try {
				tidyPrint(lexer.errout, MessageFormat.format(getRes().getString("badaccess_summary"), new Object[] {ACCESS_URL}));
			} catch (MissingResourceException e) {
				lexer.errout.println(e.toString());
			}
		}

		if (lexer.badLayout != 0) {
			if ((lexer.badLayout & USING_LAYER) != 0) {
				try {
					tidyPrint(lexer.errout, getRes().getString("badlayout_using_layer"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}

			if ((lexer.badLayout & USING_SPACER) != 0) {
				try {
					tidyPrint(lexer.errout, getRes().getString("badlayout_using_spacer"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}

			if ((lexer.badLayout & USING_FONT) != 0) {
				try {
					tidyPrint(lexer.errout, getRes().getString("badlayout_using_font"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}

			if ((lexer.badLayout & USING_NOBR) != 0) {
				try {
					tidyPrint(lexer.errout, getRes().getString("badlayout_using_nobr"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}

			if ((lexer.badLayout & USING_BODY) != 0) {
				try {
					tidyPrint(lexer.errout, getRes().getString("badlayout_using_body"));
				} catch (MissingResourceException e) {
					lexer.errout.println(e.toString());
				}
			}
		}
	}

	public static void unknownOption(PrintWriter errout, char c) {
		try {
			tidyPrintln(errout, MessageFormat.format(getRes().getString("unrecognized_option"), new Object[] {new String(new char[] {c})}));
		} catch (MissingResourceException e) {
			errout.println(e.toString());
		}
	}

	public static void unknownFile(PrintWriter errout, String program, String file) {
		try {
			tidyPrintln(errout, MessageFormat.format(getRes().getString("unknown_file"), new Object[] {program, file}));
		} catch (MissingResourceException e) {
			errout.println(e.toString());
		}
	}

	public static void needsAuthorIntervention(PrintWriter errout) {
		try {
			tidyPrintln(errout, getRes().getString("needs_author_intervention"));
		} catch (MissingResourceException e) {
			errout.println(e.toString());
		}
	}

	public static void missingBody(PrintWriter errout) {
		try {
			tidyPrintln(errout, getRes().getString("missing_body"));
		} catch (MissingResourceException e) {
			errout.println(e.toString());
		}
	}

	public static void reportNumberOfSlides(PrintWriter errout, int count) {
		try {
			tidyPrintln(errout, MessageFormat.format(getRes().getString("slides_found"), new Object[] {new Integer(count)}));
		} catch (MissingResourceException e) {
			errout.println(e.toString());
		}
	}

	public static void generalInfo(PrintWriter errout) {
		try {
			tidyPrintln(errout, getRes().getString("general_info"));
		} catch (MissingResourceException e) {
			errout.println(e.toString());
		}
	}

	public static void helloMessage(PrintWriter errout, String date, String filename) {
		currentFile = filename; /* for use with Gnu Emacs */

		try {
			tidyPrintln(errout, MessageFormat.format(getRes().getString("hello_message"), new Object[] {date, filename}));
		} catch (MissingResourceException e) {
			errout.println(e.toString());
		}
	}

	public static void reportVersion(PrintWriter errout, Lexer lexer, String filename, Node doctype) {
		int i, c;
		int state = 0;
		String vers = lexer.HTMLVersionName();
		MutableInteger cc = new MutableInteger();

		try {
			if (doctype != null) {
				tidyPrint(errout, MessageFormat.format(getRes().getString("doctype_given"), new Object[] {filename}));

				for (i = doctype.start; i < doctype.end; ++i) {
					c = (int) doctype.textarray[i];

					/* look for UTF-8 multibyte character */
					if (c < 0) {
						i += PPrint.getUTF8(doctype.textarray, i, cc);
						c = cc.value;
					}

					if (c == (char) '"')
						++state;
					else if (state == 1) errout.print((char) c);
				}

				errout.print('"');
			}

			tidyPrintln(errout, MessageFormat.format(getRes().getString("report_version"), new Object[] {filename, (vers != null ? vers : "HTML proprietary")}));
		} catch (MissingResourceException e) {
			errout.println(e.toString());
		}
	}

	public static void reportNumWarnings(PrintWriter errout, Lexer lexer) {
		if (lexer.warnings > 0) {
			try {
				tidyPrintln(errout, MessageFormat.format(getRes().getString("num_warnings"), new Object[] {new Integer(lexer.warnings)}));
			} catch (MissingResourceException e) {
				errout.println(e.toString());
			}
		} else {
			try {
				tidyPrintln(errout, getRes().getString("no_warnings"));
			} catch (MissingResourceException e) {
				errout.println(e.toString());
			}
		}
	}

	public static void helpText(PrintWriter out, String prog) {
		try {
			tidyPrintln(out, MessageFormat.format(getRes().getString("help_text"), new Object[] {prog, RELEASE_DATE}));
		} catch (MissingResourceException e) {
			out.println(e.toString());
		}
	}

	public static void badTree(PrintWriter errout) {
		try {
			tidyPrintln(errout, getRes().getString("bad_tree"));
		} catch (MissingResourceException e) {
			errout.println(e.toString());
		}
	}

	private static void setRes(ResourceBundle res) {
		Report.res = res;
	}

	private static ResourceBundle getRes() {
		if (res == null) {
			try {
				res = ResourceBundle.getBundle("TidyMessages");
			} catch (MissingResourceException e) {
				e.printStackTrace();
				throw new Error(e.toString());
			}
		}
		return res;
	}

}