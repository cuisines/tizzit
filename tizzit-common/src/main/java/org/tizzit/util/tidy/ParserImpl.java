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
 * @(#)ParserImpl.java   1.11 2000/08/16
 *
 */

package org.tizzit.util.tidy;

/**
 *
 * HTML Parser implementation
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

public class ParserImpl {

	//private static int SeenBodyEndTag;  /* AQ: moved into lexer structure */

	private static void parseTag(Lexer lexer, Node node, short mode) {
		// Local fix by GLP 2000-12-21.  Need to reset insertspace if this 
		// is both a non-inline and empty tag (base, link, meta, isindex, hr, area).
		// Remove this code once the fix is made in Tidy.

		/******  (Original code follows)
		 if ((node.tag.model & Dict.CM_EMPTY) != 0)
		 {
		 lexer.waswhite = false;
		 return;
		 }
		 else if (!((node.tag.model & Dict.CM_INLINE) != 0))
		 lexer.insertspace = false;
		 *******/

		if (!((node.tag.model & Dict.CM_INLINE) != 0)) lexer.insertspace = false;

		if ((node.tag.model & Dict.CM_EMPTY) != 0) {
			lexer.waswhite = false;
			return;
		}

		if (node.tag.parser == null || node.type == Node.StartEndTag) return;

		node.tag.parser.parse(lexer, node, mode);
	}

	private static void moveToHead(Lexer lexer, Node element, Node node) {
		Node head;
		TagTable tt = lexer.configuration.tt;

		if (node.type == Node.StartTag || node.type == Node.StartEndTag) {
			Report.warning(lexer, element, node, Report.TAG_NOT_ALLOWED_IN);

			while (element.tag != tt.tagHtml)
				element = element.parent;

			for (head = element.content; head != null; head = head.next) {
				if (head.tag == tt.tagHead) {
					Node.insertNodeAtEnd(head, node);
					break;
				}
			}

			if (node.tag.parser != null) parseTag(lexer, node, Lexer.IgnoreWhitespace);
		} else {
			Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
		}
	}

	public static class ParseHTML implements Parser {

		public void parse(Lexer lexer, Node html, short mode) {
			Node node, head;
			Node frameset = null;
			Node noframes = null;

			lexer.configuration.XmlTags = false;
			lexer.seenBodyEndTag = 0;
			TagTable tt = lexer.configuration.tt;

			for (;;) {
				node = lexer.getToken(Lexer.IgnoreWhitespace);

				if (node == null) {
					node = lexer.inferredTag("head");
					break;
				}

				if (node.tag == tt.tagHead) break;

				if (node.tag == html.tag && node.type == Node.EndTag) {
					Report.warning(lexer, html, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(html, node)) continue;

				lexer.ungetToken();
				node = lexer.inferredTag("head");
				break;
			}

			head = node;
			Node.insertNodeAtEnd(html, head);
			getParseHead().parse(lexer, head, mode);

			for (;;) {
				node = lexer.getToken(Lexer.IgnoreWhitespace);

				if (node == null) {
					if (frameset == null) /* create an empty body */
					node = lexer.inferredTag("body");

					return;
				}

				/* robustly handle html tags */
				if (node.tag == html.tag) {
					if (node.type != Node.StartTag && frameset == null)
							Report.warning(lexer, html, node, Report.DISCARDING_UNEXPECTED);

					continue;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(html, node)) continue;

				/* if frameset document coerce <body> to <noframes> */
				if (node.tag == tt.tagBody) {
					if (node.type != Node.StartTag) {
						Report.warning(lexer, html, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					if (frameset != null) {
						lexer.ungetToken();

						if (noframes == null) {
							noframes = lexer.inferredTag("noframes");
							Node.insertNodeAtEnd(frameset, noframes);
							Report.warning(lexer, html, noframes, Report.INSERTING_TAG);
						}

						parseTag(lexer, noframes, mode);
						continue;
					}

					break; /* to parse body */
				}

				/* flag an error if we see more than one frameset */
				if (node.tag == tt.tagFrameset) {
					if (node.type != Node.StartTag) {
						Report.warning(lexer, html, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					if (frameset != null)
						Report.error(lexer, html, node, Report.DUPLICATE_FRAMESET);
					else
						frameset = node;

					Node.insertNodeAtEnd(html, node);
					parseTag(lexer, node, mode);

					/*
					 see if it includes a noframes element so
					 that we can merge subsequent noframes elements
					 */

					for (node = frameset.content; node != null; node = node.next) {
						if (node.tag == tt.tagNoframes) noframes = node;
					}
					continue;
				}

				/* if not a frameset document coerce <noframes> to <body> */
				if (node.tag == tt.tagNoframes) {
					if (node.type != Node.StartTag) {
						Report.warning(lexer, html, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					if (frameset == null) {
						Report.warning(lexer, html, node, Report.DISCARDING_UNEXPECTED);
						node = lexer.inferredTag("body");
						break;
					}

					if (noframes == null) {
						noframes = node;
						Node.insertNodeAtEnd(frameset, noframes);
					}

					parseTag(lexer, noframes, mode);
					continue;
				}

				if (node.type == Node.StartTag || node.type == Node.StartEndTag) {
					if (node.tag != null && (node.tag.model & Dict.CM_HEAD) != 0) {
						moveToHead(lexer, html, node);
						continue;
					}
				}

				lexer.ungetToken();

				/* insert other content into noframes element */

				if (frameset != null) {
					if (noframes == null) {
						noframes = lexer.inferredTag("noframes");
						Node.insertNodeAtEnd(frameset, noframes);
					} else
						Report.warning(lexer, html, node, Report.NOFRAMES_CONTENT);

					parseTag(lexer, noframes, mode);
					continue;
				}

				node = lexer.inferredTag("body");
				break;
			}

			/* node must be body */

			Node.insertNodeAtEnd(html, node);
			parseTag(lexer, node, mode);
		}

	};

	public static class ParseHead implements Parser {

		public void parse(Lexer lexer, Node head, short mode) {
			Node node;
			int HasTitle = 0;
			int HasBase = 0;
			TagTable tt = lexer.configuration.tt;

			while (true) {
				node = lexer.getToken(Lexer.IgnoreWhitespace);
				if (node == null) break;
				if (node.tag == head.tag && node.type == Node.EndTag) {
					head.closed = true;
					break;
				}

				if (node.type == Node.TextNode) {
					lexer.ungetToken();
					break;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(head, node)) continue;

				if (node.type == Node.DocTypeTag) {
					Node.insertDocType(lexer, head, node);
					continue;
				}

				/* discard unknown tags */
				if (node.tag == null) {
					Report.warning(lexer, head, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				if (!((node.tag.model & Dict.CM_HEAD) != 0)) {
					lexer.ungetToken();
					break;
				}

				if (node.type == Node.StartTag || node.type == Node.StartEndTag) {
					if (node.tag == tt.tagTitle) {
						++HasTitle;

						if (HasTitle > 1) Report.warning(lexer, head, node, Report.TOO_MANY_ELEMENTS);
					} else if (node.tag == tt.tagBase) {
						++HasBase;

						if (HasBase > 1) Report.warning(lexer, head, node, Report.TOO_MANY_ELEMENTS);
					} else if (node.tag == tt.tagNoscript)
							Report.warning(lexer, head, node, Report.TAG_NOT_ALLOWED_IN);

					Node.insertNodeAtEnd(head, node);
					parseTag(lexer, node, Lexer.IgnoreWhitespace);
					continue;
				}

				/* discard unexpected text nodes and end tags */
				Report.warning(lexer, head, node, Report.DISCARDING_UNEXPECTED);
			}

			if (HasTitle == 0) {
				Report.warning(lexer, head, null, Report.MISSING_TITLE_ELEMENT);
				Node.insertNodeAtEnd(head, lexer.inferredTag("title"));
			}
		}

	};

	public static class ParseTitle implements Parser {

		public void parse(Lexer lexer, Node title, short mode) {
			Node node;

			while (true) {
				node = lexer.getToken(Lexer.MixedContent);
				if (node == null) break;
				if (node.tag == title.tag && node.type == Node.EndTag) {
					title.closed = true;
					Node.trimSpaces(lexer, title);
					return;
				}

				if (node.type == Node.TextNode) {
					/* only called for 1st child */
					if (title.content == null) Node.trimInitialSpace(lexer, title, node);

					if (node.start >= node.end) {
						continue;
					}

					Node.insertNodeAtEnd(title, node);
					continue;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(title, node)) continue;

				/* discard unknown tags */
				if (node.tag == null) {
					Report.warning(lexer, title, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/* pushback unexpected tokens */
				Report.warning(lexer, title, node, Report.MISSING_ENDTAG_BEFORE);
				lexer.ungetToken();
				Node.trimSpaces(lexer, title);
				return;
			}

			Report.warning(lexer, title, node, Report.MISSING_ENDTAG_FOR);
		}

	};

	public static class ParseScript implements Parser {

		public void parse(Lexer lexer, Node script, short mode) {
			/*
			 This isn't quite right for CDATA content as it recognises
			 tags within the content and parses them accordingly.
			 This will unfortunately screw up scripts which include
			 < + letter,  < + !, < + ?  or  < + / + letter
			 */

			Node node;

			node = lexer.getCDATA(script);

			if (node != null) Node.insertNodeAtEnd(script, node);
		}

	};

	public static class ParseBody implements Parser {

		public void parse(Lexer lexer, Node body, short mode) {
			Node node;
			boolean checkstack, iswhitenode;

			mode = Lexer.IgnoreWhitespace;
			checkstack = true;
			TagTable tt = lexer.configuration.tt;

			while (true) {
				node = lexer.getToken(mode);
				if (node == null) break;
				if (node.tag == body.tag && node.type == Node.EndTag) {
					body.closed = true;
					Node.trimSpaces(lexer, body);
					lexer.seenBodyEndTag = 1;
					mode = Lexer.IgnoreWhitespace;

					if (body.parent.tag == tt.tagNoframes) break;

					continue;
				}

				if (node.tag == tt.tagNoframes) {
					if (node.type == Node.StartTag) {
						Node.insertNodeAtEnd(body, node);
						getParseBlock().parse(lexer, node, mode);
						continue;
					}

					if (node.type == Node.EndTag && body.parent.tag == tt.tagNoframes) {
						Node.trimSpaces(lexer, body);
						lexer.ungetToken();
						break;
					}
				}

				if ((node.tag == tt.tagFrame || node.tag == tt.tagFrameset) && body.parent.tag == tt.tagNoframes) {
					Node.trimSpaces(lexer, body);
					lexer.ungetToken();
					break;
				}

				if (node.tag == tt.tagHtml) {
					if (node.type == Node.StartTag || node.type == Node.StartEndTag)
							Report.warning(lexer, body, node, Report.DISCARDING_UNEXPECTED);

					continue;
				}

				iswhitenode = false;

				if (node.type == Node.TextNode && node.end <= node.start + 1
						&& node.textarray[node.start] == (byte) ' ') iswhitenode = true;

				/* deal with comments etc. */
				if (Node.insertMisc(body, node)) continue;

				if (lexer.seenBodyEndTag == 1 && !iswhitenode) {
					++lexer.seenBodyEndTag;
					Report.warning(lexer, body, node, Report.CONTENT_AFTER_BODY);
				}

				/* mixed content model permits text */
				if (node.type == Node.TextNode) {
					if (iswhitenode && mode == Lexer.IgnoreWhitespace) {
						continue;
					}

					if (lexer.configuration.EncloseBodyText && !iswhitenode) {
						Node para;

						lexer.ungetToken();
						para = lexer.inferredTag("p");
						Node.insertNodeAtEnd(body, para);
						parseTag(lexer, para, mode);
						mode = Lexer.MixedContent;
						continue;
					} else
						/* strict doesn't allow text here */
						lexer.versions &= ~(Dict.VERS_HTML40_STRICT | Dict.VERS_HTML20);

					if (checkstack) {
						checkstack = false;

						if (lexer.inlineDup(node) > 0) continue;
					}

					Node.insertNodeAtEnd(body, node);
					mode = Lexer.MixedContent;
					continue;
				}

				if (node.type == Node.DocTypeTag) {
					Node.insertDocType(lexer, body, node);
					continue;
				}
				/* discard unknown  and PARAM tags */
				if (node.tag == null || node.tag == tt.tagParam) {
					Report.warning(lexer, body, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/*
				 Netscape allows LI and DD directly in BODY
				 We infer UL or DL respectively and use this
				 boolean to exclude block-level elements so as
				 to match Netscape's observed behaviour.
				 */
				lexer.excludeBlocks = false;

				if (!((node.tag.model & Dict.CM_BLOCK) != 0) && !((node.tag.model & Dict.CM_INLINE) != 0)) {
					/* avoid this error message being issued twice */
					if (!((node.tag.model & Dict.CM_HEAD) != 0))
							Report.warning(lexer, body, node, Report.TAG_NOT_ALLOWED_IN);

					if ((node.tag.model & Dict.CM_HTML) != 0) {
						/* copy body attributes if current body was inferred */
						if (node.tag == tt.tagBody && body.implicit && body.attributes == null) {
							body.attributes = node.attributes;
							node.attributes = null;
						}

						continue;
					}

					if ((node.tag.model & Dict.CM_HEAD) != 0) {
						moveToHead(lexer, body, node);
						continue;
					}

					if ((node.tag.model & Dict.CM_LIST) != 0) {
						lexer.ungetToken();
						node = lexer.inferredTag("ul");
						Node.addClass(node, "noindent");
						lexer.excludeBlocks = true;
					} else if ((node.tag.model & Dict.CM_DEFLIST) != 0) {
						lexer.ungetToken();
						node = lexer.inferredTag("dl");
						lexer.excludeBlocks = true;
					} else if ((node.tag.model & (Dict.CM_TABLE | Dict.CM_ROWGRP | Dict.CM_ROW)) != 0) {
						lexer.ungetToken();
						node = lexer.inferredTag("table");
						lexer.excludeBlocks = true;
					} else {
						/* AQ: The following line is from the official C
						 version of tidy.  It doesn't make sense to me
						 because the '!' operator has higher precedence
						 than the '&' operator.  It seems to me that the
						 expression always evaluates to 0.

						 if (!node->tag->model & (CM_ROW | CM_FIELD))

						 AQ: 13Jan2000 fixed in C tidy
						 */
						if (!((node.tag.model & (Dict.CM_ROW | Dict.CM_FIELD)) != 0)) {
							lexer.ungetToken();
							return;
						}

						/* ignore </td> </th> <option> etc. */
						continue;
					}
				}

				if (node.type == Node.EndTag) {
					if (node.tag == tt.tagBr)
						node.type = Node.StartTag;
					else if (node.tag == tt.tagP) {
						Node.coerceNode(lexer, node, tt.tagBr);
						Node.insertNodeAtEnd(body, node);
						node = lexer.inferredTag("br");
					} else if ((node.tag.model & Dict.CM_INLINE) != 0) lexer.popInline(node);
				}

				if (node.type == Node.StartTag || node.type == Node.StartEndTag) {
					if (((node.tag.model & Dict.CM_INLINE) != 0) && !((node.tag.model & Dict.CM_MIXED) != 0)) {
						/* HTML4 strict doesn't allow inline content here */
						/* but HTML2 does allow img elements as children of body */
						if (node.tag == tt.tagImg)
							lexer.versions &= ~Dict.VERS_HTML40_STRICT;
						else
							lexer.versions &= ~(Dict.VERS_HTML40_STRICT | Dict.VERS_HTML20);

						if (checkstack && !node.implicit) {
							checkstack = false;

							if (lexer.inlineDup(node) > 0) continue;
						}

						mode = Lexer.MixedContent;
					} else {
						checkstack = true;
						mode = Lexer.IgnoreWhitespace;
					}

					if (node.implicit) Report.warning(lexer, body, node, Report.INSERTING_TAG);

					Node.insertNodeAtEnd(body, node);
					parseTag(lexer, node, mode);
					continue;
				}

				/* discard unexpected tags */
				Report.warning(lexer, body, node, Report.DISCARDING_UNEXPECTED);
			}
		}

	};

	public static class ParseFrameSet implements Parser {

		public void parse(Lexer lexer, Node frameset, short mode) {
			Node node;
			TagTable tt = lexer.configuration.tt;

			lexer.badAccess |= Report.USING_FRAMES;

			while (true) {
				node = lexer.getToken(Lexer.IgnoreWhitespace);
				if (node == null) break;
				if (node.tag == frameset.tag && node.type == Node.EndTag) {
					frameset.closed = true;
					Node.trimSpaces(lexer, frameset);
					return;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(frameset, node)) continue;

				if (node.tag == null) {
					Report.warning(lexer, frameset, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				if (node.type == Node.StartTag || node.type == Node.StartEndTag) {
					if (node.tag != null && (node.tag.model & Dict.CM_HEAD) != 0) {
						moveToHead(lexer, frameset, node);
						continue;
					}
				}

				if (node.tag == tt.tagBody) {
					lexer.ungetToken();
					node = lexer.inferredTag("noframes");
					Report.warning(lexer, frameset, node, Report.INSERTING_TAG);
				}

				if (node.type == Node.StartTag && (node.tag.model & Dict.CM_FRAMES) != 0) {
					Node.insertNodeAtEnd(frameset, node);
					lexer.excludeBlocks = false;
					parseTag(lexer, node, Lexer.MixedContent);
					continue;
				} else if (node.type == Node.StartEndTag && (node.tag.model & Dict.CM_FRAMES) != 0) {
					Node.insertNodeAtEnd(frameset, node);
					continue;
				}

				/* discard unexpected tags */
				Report.warning(lexer, frameset, node, Report.DISCARDING_UNEXPECTED);
			}

			Report.warning(lexer, frameset, node, Report.MISSING_ENDTAG_FOR);
		}

	};

	public static class ParseInline implements Parser {

		public void parse(Lexer lexer, Node element, short mode) {
			Node node, parent;
			TagTable tt = lexer.configuration.tt;

			if ((element.tag.model & Dict.CM_EMPTY) != 0) return;

			if (element.tag == tt.tagA) {
				if (element.attributes == null) {
					Report.warning(lexer, element.parent, element, Report.DISCARDING_UNEXPECTED);
					Node.discardElement(element);
					return;
				}
			}

			/*
			 ParseInline is used for some block level elements like H1 to H6
			 For such elements we need to insert inline emphasis tags currently
			 on the inline stack. For Inline elements, we normally push them
			 onto the inline stack provided they aren't implicit or OBJECT/APPLET.
			 This test is carried out in PushInline and PopInline, see istack.c
			 We don't push A or SPAN to replicate current browser behavior
			 */
			if (((element.tag.model & Dict.CM_BLOCK) != 0) || (element.tag == tt.tagDt))
				lexer.inlineDup(null);
			else if ((element.tag.model & Dict.CM_INLINE) != 0 && element.tag != tt.tagA && element.tag != tt.tagSpan)
					lexer.pushInline(element);

			if (element.tag == tt.tagNobr)
				lexer.badLayout |= Report.USING_NOBR;
			else if (element.tag == tt.tagFont) lexer.badLayout |= Report.USING_FONT;

			/* Inline elements may or may not be within a preformatted element */
			if (mode != Lexer.Preformatted) mode = Lexer.MixedContent;

			while (true) {
				node = lexer.getToken(mode);
				if (node == null) break;
				/* end tag for current element */
				if (node.tag == element.tag && node.type == Node.EndTag) {
					if ((element.tag.model & Dict.CM_INLINE) != 0 && element.tag != tt.tagA) lexer.popInline(node);

					if (!((mode & Lexer.Preformatted) != 0)) Node.trimSpaces(lexer, element);
					/*
					 if a font element wraps an anchor and nothing else
					 then move the font element inside the anchor since
					 otherwise it won't alter the anchor text color
					 */
					if (element.tag == tt.tagFont && element.content != null && element.content == element.last) {
						Node child = element.content;

						if (child.tag == tt.tagA) {
							child.parent = element.parent;
							child.next = element.next;
							child.prev = element.prev;

							if (child.prev != null)
								child.prev.next = child;
							else
								child.parent.content = child;

							if (child.next != null)
								child.next.prev = child;
							else
								child.parent.last = child;

							element.next = null;
							element.prev = null;
							element.parent = child;
							element.content = child.content;
							element.last = child.last;
							child.content = element;
							child.last = element;
							for (child = element.content; child != null; child = child.next)
								child.parent = element;
						}
					}
					element.closed = true;
					Node.trimSpaces(lexer, element);
					Node.trimEmptyElement(lexer, element);
					return;
				}

				/* <u>...<u>  map 2nd <u> to </u> if 1st is explicit */
				/* otherwise emphasis nesting is probably unintentional */
				/* big and small have cumulative effect to leave them alone */
				if (node.type == Node.StartTag && node.tag == element.tag && lexer.isPushed(node) && !node.implicit
						&& !element.implicit && node.tag != null && ((node.tag.model & Dict.CM_INLINE) != 0)
						&& node.tag != tt.tagA && node.tag != tt.tagFont && node.tag != tt.tagBig
						&& node.tag != tt.tagSmall) {
					if (element.content != null && node.attributes == null) {
						Report.warning(lexer, element, node, Report.COERCE_TO_ENDTAG);
						node.type = Node.EndTag;
						lexer.ungetToken();
						continue;
					}

					Report.warning(lexer, element, node, Report.NESTED_EMPHASIS);
				}

				if (node.type == Node.TextNode) {
					/* only called for 1st child */
					if (element.content == null && !((mode & Lexer.Preformatted) != 0))
							Node.trimSpaces(lexer, element);

					if (node.start >= node.end) {
						continue;
					}

					Node.insertNodeAtEnd(element, node);
					continue;
				}

				/* mixed content model so allow text */
				if (Node.insertMisc(element, node)) continue;

				/* deal with HTML tags */
				if (node.tag == tt.tagHtml) {
					if (node.type == Node.StartTag || node.type == Node.StartEndTag) {
						Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					/* otherwise infer end of inline element */
					lexer.ungetToken();
					if (!((mode & Lexer.Preformatted) != 0)) Node.trimSpaces(lexer, element);
					Node.trimEmptyElement(lexer, element);
					return;
				}

				/* within <dt> or <pre> map <p> to <br> */
				if (node.tag == tt.tagP
						&& node.type == Node.StartTag
						&& ((mode & Lexer.Preformatted) != 0 || element.tag == tt.tagDt || element
								.isDescendantOf(tt.tagDt))) {
					node.tag = tt.tagBr;
					node.element = "br";
					Node.trimSpaces(lexer, element);
					Node.insertNodeAtEnd(element, node);
					continue;
				}

				/* ignore unknown and PARAM tags */
				if (node.tag == null || node.tag == tt.tagParam) {
					Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				if (node.tag == tt.tagBr && node.type == Node.EndTag) node.type = Node.StartTag;

				if (node.type == Node.EndTag) {
					/* coerce </br> to <br> */
					if (node.tag == tt.tagBr)
						node.type = Node.StartTag;
					else if (node.tag == tt.tagP) {
						/* coerce unmatched </p> to <br><br> */
						if (!element.isDescendantOf(tt.tagP)) {
							Node.coerceNode(lexer, node, tt.tagBr);
							Node.trimSpaces(lexer, element);
							Node.insertNodeAtEnd(element, node);
							node = lexer.inferredTag("br");
							continue;
						}
					} else if ((node.tag.model & Dict.CM_INLINE) != 0 && node.tag != tt.tagA
							&& !((node.tag.model & Dict.CM_OBJECT) != 0) && (element.tag.model & Dict.CM_INLINE) != 0) {
						/* allow any inline end tag to end current element */
						lexer.popInline(element);

						if (element.tag != tt.tagA) {
							if (node.tag == tt.tagA && node.tag != element.tag) {
								Report.warning(lexer, element, node, Report.MISSING_ENDTAG_BEFORE);
								lexer.ungetToken();
							} else {
								Report.warning(lexer, element, node, Report.NON_MATCHING_ENDTAG);
							}

							if (!((mode & Lexer.Preformatted) != 0)) Node.trimSpaces(lexer, element);
							Node.trimEmptyElement(lexer, element);
							return;
						}

						/* if parent is <a> then discard unexpected inline end tag */
						Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
						continue;
					} /* special case </tr> etc. for stuff moved in front of table */
					else if (lexer.exiled && node.tag.model != 0 && (node.tag.model & Dict.CM_TABLE) != 0) {
						lexer.ungetToken();
						Node.trimSpaces(lexer, element);
						Node.trimEmptyElement(lexer, element);
						return;
					}
				}

				/* allow any header tag to end current header */
				if ((node.tag.model & Dict.CM_HEADING) != 0 && (element.tag.model & Dict.CM_HEADING) != 0) {
					if (node.tag == element.tag) {
						Report.warning(lexer, element, node, Report.NON_MATCHING_ENDTAG);
					} else {
						Report.warning(lexer, element, node, Report.MISSING_ENDTAG_BEFORE);
						lexer.ungetToken();
					}
					if (!((mode & Lexer.Preformatted) != 0)) Node.trimSpaces(lexer, element);
					Node.trimEmptyElement(lexer, element);
					return;
				}

				/*
				 an <A> tag to ends any open <A> element
				 but <A href=...> is mapped to </A><A href=...>
				 */
				if (node.tag == tt.tagA && !node.implicit && lexer.isPushed(node)) {
					/* coerce <a> to </a> unless it has some attributes */
					if (node.attributes == null) {
						node.type = Node.EndTag;
						Report.warning(lexer, element, node, Report.COERCE_TO_ENDTAG);
						lexer.popInline(node);
						lexer.ungetToken();
						continue;
					}

					lexer.ungetToken();
					Report.warning(lexer, element, node, Report.MISSING_ENDTAG_BEFORE);
					lexer.popInline(element);
					if (!((mode & Lexer.Preformatted) != 0)) Node.trimSpaces(lexer, element);
					Node.trimEmptyElement(lexer, element);
					return;
				}

				if ((element.tag.model & Dict.CM_HEADING) != 0) {
					if (node.tag == tt.tagCenter || node.tag == tt.tagDiv) {
						if (node.type != Node.StartTag && node.type != Node.StartEndTag) {
							Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
							continue;
						}

						Report.warning(lexer, element, node, Report.TAG_NOT_ALLOWED_IN);

						/* insert center as parent if heading is empty */
						if (element.content == null) {
							Node.insertNodeAsParent(element, node);
							continue;
						}

						/* split heading and make center parent of 2nd part */
						Node.insertNodeAfterElement(element, node);

						if (!((mode & Lexer.Preformatted) != 0)) Node.trimSpaces(lexer, element);

						element = lexer.cloneNode(element);
						element.start = lexer.lexsize;
						element.end = lexer.lexsize;
						Node.insertNodeAtEnd(node, element);
						continue;
					}

					if (node.tag == tt.tagHr) {
						if (node.type != Node.StartTag && node.type != Node.StartEndTag) {
							Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
							continue;
						}

						Report.warning(lexer, element, node, Report.TAG_NOT_ALLOWED_IN);

						/* insert hr before heading if heading is empty */
						if (element.content == null) {
							Node.insertNodeBeforeElement(element, node);
							continue;
						}

						/* split heading and insert hr before 2nd part */
						Node.insertNodeAfterElement(element, node);

						if (!((mode & Lexer.Preformatted) != 0)) Node.trimSpaces(lexer, element);

						element = lexer.cloneNode(element);
						element.start = lexer.lexsize;
						element.end = lexer.lexsize;
						Node.insertNodeAfterElement(node, element);
						continue;
					}
				}

				if (element.tag == tt.tagDt) {
					if (node.tag == tt.tagHr) {
						Node dd;

						if (node.type != Node.StartTag && node.type != Node.StartEndTag) {
							Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
							continue;
						}

						Report.warning(lexer, element, node, Report.TAG_NOT_ALLOWED_IN);
						dd = lexer.inferredTag("dd");

						/* insert hr within dd before dt if dt is empty */
						if (element.content == null) {
							Node.insertNodeBeforeElement(element, dd);
							Node.insertNodeAtEnd(dd, node);
							continue;
						}

						/* split dt and insert hr within dd before 2nd part */
						Node.insertNodeAfterElement(element, dd);
						Node.insertNodeAtEnd(dd, node);

						if (!((mode & Lexer.Preformatted) != 0)) Node.trimSpaces(lexer, element);

						element = lexer.cloneNode(element);
						element.start = lexer.lexsize;
						element.end = lexer.lexsize;
						Node.insertNodeAfterElement(dd, element);
						continue;
					}
				}

				/* 
				 if this is the end tag for an ancestor element
				 then infer end tag for this element
				 */
				if (node.type == Node.EndTag) {
					for (parent = element.parent; parent != null; parent = parent.parent) {
						if (node.tag == parent.tag) {
							if (!((element.tag.model & Dict.CM_OPT) != 0) && !element.implicit)
									Report.warning(lexer, element, node, Report.MISSING_ENDTAG_BEFORE);

							if (element.tag == tt.tagA) lexer.popInline(element);

							lexer.ungetToken();

							if (!((mode & Lexer.Preformatted) != 0)) Node.trimSpaces(lexer, element);

							Node.trimEmptyElement(lexer, element);
							return;
						}
					}
				}

				/* block level tags end this element */
				if (!((node.tag.model & Dict.CM_INLINE) != 0)) {
					if (node.type != Node.StartTag) {
						Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					if (!((element.tag.model & Dict.CM_OPT) != 0))
							Report.warning(lexer, element, node, Report.MISSING_ENDTAG_BEFORE);

					if ((node.tag.model & Dict.CM_HEAD) != 0 && !((node.tag.model & Dict.CM_BLOCK) != 0)) {
						moveToHead(lexer, element, node);
						continue;
					}

					/*
					 prevent anchors from propagating into block tags
					 except for headings h1 to h6
					 */
					if (element.tag == tt.tagA) {
						if (node.tag != null && !((node.tag.model & Dict.CM_HEADING) != 0))
							lexer.popInline(element);
						else if (!(element.content != null)) {
							Node.discardElement(element);
							lexer.ungetToken();
							return;
						}
					}

					lexer.ungetToken();

					if (!((mode & Lexer.Preformatted) != 0)) Node.trimSpaces(lexer, element);

					Node.trimEmptyElement(lexer, element);
					return;
				}

				/* parse inline element */
				if (node.type == Node.StartTag || node.type == Node.StartEndTag) {
					if (node.implicit) Report.warning(lexer, element, node, Report.INSERTING_TAG);

					/* trim white space before <br> */
					if (node.tag == tt.tagBr) Node.trimSpaces(lexer, element);

					Node.insertNodeAtEnd(element, node);
					parseTag(lexer, node, mode);
					continue;
				}

				/* discard unexpected tags */
				Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
			}

			if (!((element.tag.model & Dict.CM_OPT) != 0))
					Report.warning(lexer, element, node, Report.MISSING_ENDTAG_FOR);

			Node.trimEmptyElement(lexer, element);
		}
	};

	public static class ParseList implements Parser {

		public void parse(Lexer lexer, Node list, short mode) {
			Node node;
			Node parent;
			TagTable tt = lexer.configuration.tt;

			if ((list.tag.model & Dict.CM_EMPTY) != 0) return;

			lexer.insert = -1; /* defer implicit inline start tags */

			while (true) {
				node = lexer.getToken(Lexer.IgnoreWhitespace);
				if (node == null) break;

				if (node.tag == list.tag && node.type == Node.EndTag) {
					if ((list.tag.model & Dict.CM_OBSOLETE) != 0) Node.coerceNode(lexer, list, tt.tagUl);

					list.closed = true;
					Node.trimEmptyElement(lexer, list);
					return;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(list, node)) continue;

				if (node.type != Node.TextNode && node.tag == null) {
					Report.warning(lexer, list, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/* 
				 if this is the end tag for an ancestor element
				 then infer end tag for this element
				 */
				if (node.type == Node.EndTag) {
					if (node.tag == tt.tagForm) {
						lexer.badForm = 1;
						Report.warning(lexer, list, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					if (node.tag != null && (node.tag.model & Dict.CM_INLINE) != 0) {
						Report.warning(lexer, list, node, Report.DISCARDING_UNEXPECTED);
						lexer.popInline(node);
						continue;
					}

					for (parent = list.parent; parent != null; parent = parent.parent) {
						if (node.tag == parent.tag) {
							Report.warning(lexer, list, node, Report.MISSING_ENDTAG_BEFORE);
							lexer.ungetToken();

							if ((list.tag.model & Dict.CM_OBSOLETE) != 0) Node.coerceNode(lexer, list, tt.tagUl);

							Node.trimEmptyElement(lexer, list);
							return;
						}
					}

					Report.warning(lexer, list, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				if (node.tag != tt.tagLi) {
					lexer.ungetToken();

					if (node.tag != null && (node.tag.model & Dict.CM_BLOCK) != 0 && lexer.excludeBlocks) {
						Report.warning(lexer, list, node, Report.MISSING_ENDTAG_BEFORE);
						Node.trimEmptyElement(lexer, list);
						return;
					}

					node = lexer.inferredTag("li");
					node.addAttribute("style", "list-style: none");
					Report.warning(lexer, list, node, Report.MISSING_STARTTAG);
				}

				/* node should be <LI> */
				Node.insertNodeAtEnd(list, node);
				parseTag(lexer, node, Lexer.IgnoreWhitespace);
			}

			if ((list.tag.model & Dict.CM_OBSOLETE) != 0) Node.coerceNode(lexer, list, tt.tagUl);

			Report.warning(lexer, list, node, Report.MISSING_ENDTAG_FOR);
			Node.trimEmptyElement(lexer, list);
		}

	};

	public static class ParseDefList implements Parser {

		public void parse(Lexer lexer, Node list, short mode) {
			Node node, parent;
			TagTable tt = lexer.configuration.tt;

			if ((list.tag.model & Dict.CM_EMPTY) != 0) return;

			lexer.insert = -1; /* defer implicit inline start tags */

			while (true) {
				node = lexer.getToken(Lexer.IgnoreWhitespace);
				if (node == null) break;
				if (node.tag == list.tag && node.type == Node.EndTag) {
					list.closed = true;
					Node.trimEmptyElement(lexer, list);
					return;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(list, node)) continue;

				if (node.type == Node.TextNode) {
					lexer.ungetToken();
					node = lexer.inferredTag("dt");
					Report.warning(lexer, list, node, Report.MISSING_STARTTAG);
				}

				if (node.tag == null) {
					Report.warning(lexer, list, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/* 
				 if this is the end tag for an ancestor element
				 then infer end tag for this element
				 */
				if (node.type == Node.EndTag) {
					if (node.tag == tt.tagForm) {
						lexer.badForm = 1;
						Report.warning(lexer, list, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					for (parent = list.parent; parent != null; parent = parent.parent) {
						if (node.tag == parent.tag) {
							Report.warning(lexer, list, node, Report.MISSING_ENDTAG_BEFORE);

							lexer.ungetToken();
							Node.trimEmptyElement(lexer, list);
							return;
						}
					}
				}

				/* center in a dt or a dl breaks the dl list in two */
				if (node.tag == tt.tagCenter) {
					if (list.content != null)
						Node.insertNodeAfterElement(list, node);
					else /* trim empty dl list */
					{
						Node.insertNodeBeforeElement(list, node);
						Node.discardElement(list);
					}

					/* and parse contents of center */
					parseTag(lexer, node, mode);

					/* now create a new dl element */
					list = lexer.inferredTag("dl");
					Node.insertNodeAfterElement(node, list);
					continue;
				}

				if (!(node.tag == tt.tagDt || node.tag == tt.tagDd)) {
					lexer.ungetToken();

					if (!((node.tag.model & (Dict.CM_BLOCK | Dict.CM_INLINE)) != 0)) {
						Report.warning(lexer, list, node, Report.TAG_NOT_ALLOWED_IN);
						Node.trimEmptyElement(lexer, list);
						return;
					}

					/* if DD appeared directly in BODY then exclude blocks */
					if (!((node.tag.model & Dict.CM_INLINE) != 0) && lexer.excludeBlocks) {
						Node.trimEmptyElement(lexer, list);
						return;
					}

					node = lexer.inferredTag("dd");
					Report.warning(lexer, list, node, Report.MISSING_STARTTAG);
				}

				if (node.type == Node.EndTag) {
					Report.warning(lexer, list, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/* node should be <DT> or <DD>*/
				Node.insertNodeAtEnd(list, node);
				parseTag(lexer, node, Lexer.IgnoreWhitespace);
			}

			Report.warning(lexer, list, node, Report.MISSING_ENDTAG_FOR);
			Node.trimEmptyElement(lexer, list);
		}

	};

	public static class ParsePre implements Parser {

		public void parse(Lexer lexer, Node pre, short mode) {
			Node node, parent;
			TagTable tt = lexer.configuration.tt;

			if ((pre.tag.model & Dict.CM_EMPTY) != 0) return;

			if ((pre.tag.model & Dict.CM_OBSOLETE) != 0) Node.coerceNode(lexer, pre, tt.tagPre);

			lexer.inlineDup(null); /* tell lexer to insert inlines if needed */

			while (true) {
				node = lexer.getToken(Lexer.Preformatted);
				if (node == null) break;
				if (node.tag == pre.tag && node.type == Node.EndTag) {
					Node.trimSpaces(lexer, pre);
					pre.closed = true;
					Node.trimEmptyElement(lexer, pre);
					return;
				}

				if (node.tag == tt.tagHtml) {
					if (node.type == Node.StartTag || node.type == Node.StartEndTag)
							Report.warning(lexer, pre, node, Report.DISCARDING_UNEXPECTED);

					continue;
				}

				if (node.type == Node.TextNode) {
					/* if first check for inital newline */
					if (pre.content == null) {
						if (node.textarray[node.start] == (byte) '\n') ++node.start;

						if (node.start >= node.end) {
							continue;
						}
					}

					Node.insertNodeAtEnd(pre, node);
					continue;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(pre, node)) continue;

				/* discard unknown  and PARAM tags */
				if (node.tag == null || node.tag == tt.tagParam) {
					Report.warning(lexer, pre, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				if (node.tag == tt.tagP) {
					if (node.type == Node.StartTag) {
						Report.warning(lexer, pre, node, Report.USING_BR_INPLACE_OF);

						/* trim white space before <p> in <pre>*/
						Node.trimSpaces(lexer, pre);

						/* coerce both <p> and </p> to <br> */
						Node.coerceNode(lexer, node, tt.tagBr);
						Node.insertNodeAtEnd(pre, node);
					} else {
						Report.warning(lexer, pre, node, Report.DISCARDING_UNEXPECTED);
					}
					continue;
				}

				if ((node.tag.model & Dict.CM_HEAD) != 0 && !((node.tag.model & Dict.CM_BLOCK) != 0)) {
					moveToHead(lexer, pre, node);
					continue;
				}

				/* 
				 if this is the end tag for an ancestor element
				 then infer end tag for this element
				 */
				if (node.type == Node.EndTag) {
					if (node.tag == tt.tagForm) {
						lexer.badForm = 1;
						Report.warning(lexer, pre, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					for (parent = pre.parent; parent != null; parent = parent.parent) {
						if (node.tag == parent.tag) {
							Report.warning(lexer, pre, node, Report.MISSING_ENDTAG_BEFORE);

							lexer.ungetToken();
							Node.trimSpaces(lexer, pre);
							Node.trimEmptyElement(lexer, pre);
							return;
						}
					}
				}

				/* what about head content, HEAD, BODY tags etc? */
				if (!((node.tag.model & Dict.CM_INLINE) != 0)) {
					if (node.type != Node.StartTag) {
						Report.warning(lexer, pre, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					Report.warning(lexer, pre, node, Report.MISSING_ENDTAG_BEFORE);
					lexer.excludeBlocks = true;

					/* check if we need to infer a container */
					if ((node.tag.model & Dict.CM_LIST) != 0) {
						lexer.ungetToken();
						node = lexer.inferredTag("ul");
						Node.addClass(node, "noindent");
					} else if ((node.tag.model & Dict.CM_DEFLIST) != 0) {
						lexer.ungetToken();
						node = lexer.inferredTag("dl");
					} else if ((node.tag.model & Dict.CM_TABLE) != 0) {
						lexer.ungetToken();
						node = lexer.inferredTag("table");
					}

					Node.insertNodeAfterElement(pre, node);
					pre = lexer.inferredTag("pre");
					Node.insertNodeAfterElement(node, pre);
					parseTag(lexer, node, Lexer.IgnoreWhitespace);
					lexer.excludeBlocks = false;
					continue;
				}
				/*
				 if (!((node.tag.model & Dict.CM_INLINE) != 0))
				 {
				 Report.warning(lexer, pre, node, Report.MISSING_ENDTAG_BEFORE);
				 lexer.ungetToken();
				 return;
				 }
				 */
				if (node.type == Node.StartTag || node.type == Node.StartEndTag) {
					/* trim white space before <br> */
					if (node.tag == tt.tagBr) Node.trimSpaces(lexer, pre);

					Node.insertNodeAtEnd(pre, node);
					parseTag(lexer, node, Lexer.Preformatted);
					continue;
				}

				/* discard unexpected tags */
				Report.warning(lexer, pre, node, Report.DISCARDING_UNEXPECTED);
			}

			Report.warning(lexer, pre, node, Report.MISSING_ENDTAG_FOR);
			Node.trimEmptyElement(lexer, pre);
		}

	};

	public static class ParseBlock implements Parser {

		public void parse(Lexer lexer, Node element, short mode)
		/*
		 element is node created by the lexer
		 upon seeing the start tag, or by the
		 parser when the start tag is inferred
		 */
		{
			Node node, parent;
			boolean checkstack;
			int istackbase = 0;
			TagTable tt = lexer.configuration.tt;

			checkstack = true;

			if ((element.tag.model & Dict.CM_EMPTY) != 0) return;

			if (element.tag == tt.tagForm && element.isDescendantOf(tt.tagForm))
					Report.warning(lexer, element, null, Report.ILLEGAL_NESTING);

			/*
			 InlineDup() asks the lexer to insert inline emphasis tags
			 currently pushed on the istack, but take care to avoid
			 propagating inline emphasis inside OBJECT or APPLET.
			 For these elements a fresh inline stack context is created
			 and disposed of upon reaching the end of the element.
			 They thus behave like table cells in this respect.
			 */
			if ((element.tag.model & Dict.CM_OBJECT) != 0) {
				istackbase = lexer.istackbase;
				lexer.istackbase = lexer.istack.size();
			}

			if (!((element.tag.model & Dict.CM_MIXED) != 0)) lexer.inlineDup(null);

			mode = Lexer.IgnoreWhitespace;

			while (true) {
				node = lexer.getToken(mode /*Lexer.MixedContent*/);
				if (node == null) break;
				/* end tag for this element */
				if (node.type == Node.EndTag && node.tag != null
						&& (node.tag == element.tag || element.was == node.tag)) {

					if ((element.tag.model & Dict.CM_OBJECT) != 0) {
						/* pop inline stack */
						while (lexer.istack.size() > lexer.istackbase)
							lexer.popInline(null);
						lexer.istackbase = istackbase;
					}

					element.closed = true;
					Node.trimSpaces(lexer, element);
					Node.trimEmptyElement(lexer, element);
					return;
				}

				if (node.tag == tt.tagHtml || node.tag == tt.tagHead || node.tag == tt.tagBody) {
					if (node.type == Node.StartTag || node.type == Node.StartEndTag)
							Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);

					continue;
				}

				if (node.type == Node.EndTag) {
					if (node.tag == null) {
						Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);

						continue;
					} else if (node.tag == tt.tagBr)
						node.type = Node.StartTag;
					else if (node.tag == tt.tagP) {
						Node.coerceNode(lexer, node, tt.tagBr);
						Node.insertNodeAtEnd(element, node);
						node = lexer.inferredTag("br");
					} else {
						/* 
						 if this is the end tag for an ancestor element
						 then infer end tag for this element
						 */
						for (parent = element.parent; parent != null; parent = parent.parent) {
							if (node.tag == parent.tag) {
								if (!((element.tag.model & Dict.CM_OPT) != 0))
										Report.warning(lexer, element, node, Report.MISSING_ENDTAG_BEFORE);

								lexer.ungetToken();

								if ((element.tag.model & Dict.CM_OBJECT) != 0) {
									/* pop inline stack */
									while (lexer.istack.size() > lexer.istackbase)
										lexer.popInline(null);
									lexer.istackbase = istackbase;
								}

								Node.trimSpaces(lexer, element);
								Node.trimEmptyElement(lexer, element);
								return;
							}
						}
						/* special case </tr> etc. for stuff moved in front of table */
						if (lexer.exiled && node.tag.model != 0 && (node.tag.model & Dict.CM_TABLE) != 0) {
							lexer.ungetToken();
							Node.trimSpaces(lexer, element);
							Node.trimEmptyElement(lexer, element);
							return;
						}
					}
				}

				/* mixed content model permits text */
				if (node.type == Node.TextNode) {
					boolean iswhitenode = false;

					if (node.type == Node.TextNode && node.end <= node.start + 1
							&& lexer.lexbuf[node.start] == (byte) ' ') iswhitenode = true;

					if (lexer.configuration.EncloseBlockText && !iswhitenode) {
						lexer.ungetToken();
						node = lexer.inferredTag("p");
						Node.insertNodeAtEnd(element, node);
						parseTag(lexer, node, Lexer.MixedContent);
						continue;
					}

					if (checkstack) {
						checkstack = false;

						if (!((element.tag.model & Dict.CM_MIXED) != 0)) {
							if (lexer.inlineDup(node) > 0) continue;
						}
					}

					Node.insertNodeAtEnd(element, node);
					mode = Lexer.MixedContent;
					/*
					 HTML4 strict doesn't allow mixed content for
					 elements with %block; as their content model
					 */
					lexer.versions &= ~Dict.VERS_HTML40_STRICT;
					continue;
				}

				if (Node.insertMisc(element, node)) continue;

				/* allow PARAM elements? */
				if (node.tag == tt.tagParam) {
					if (((element.tag.model & Dict.CM_PARAM) != 0)
							&& (node.type == Node.StartTag || node.type == Node.StartEndTag)) {
						Node.insertNodeAtEnd(element, node);
						continue;
					}

					/* otherwise discard it */
					Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/* allow AREA elements? */
				if (node.tag == tt.tagArea) {
					if ((element.tag == tt.tagMap) && (node.type == Node.StartTag || node.type == Node.StartEndTag)) {
						Node.insertNodeAtEnd(element, node);
						continue;
					}

					/* otherwise discard it */
					Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/* ignore unknown start/end tags */
				if (node.tag == null) {
					Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/*
				 Allow Dict.CM_INLINE elements here.

				 Allow Dict.CM_BLOCK elements here unless
				 lexer.excludeBlocks is yes.

				 LI and DD are special cased.

				 Otherwise infer end tag for this element.
				 */

				if (!((node.tag.model & Dict.CM_INLINE) != 0)) {
					if (node.type != Node.StartTag && node.type != Node.StartEndTag) {
						Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					if (element.tag == tt.tagTd || element.tag == tt.tagTh) {
						/* if parent is a table cell, avoid inferring the end of the cell */

						if ((node.tag.model & Dict.CM_HEAD) != 0) {
							moveToHead(lexer, element, node);
							continue;
						}

						if ((node.tag.model & Dict.CM_LIST) != 0) {
							lexer.ungetToken();
							node = lexer.inferredTag("ul");
							Node.addClass(node, "noindent");
							lexer.excludeBlocks = true;
						} else if ((node.tag.model & Dict.CM_DEFLIST) != 0) {
							lexer.ungetToken();
							node = lexer.inferredTag("dl");
							lexer.excludeBlocks = true;
						}

						/* infer end of current table cell */
						if (!((node.tag.model & Dict.CM_BLOCK) != 0)) {
							lexer.ungetToken();
							Node.trimSpaces(lexer, element);
							Node.trimEmptyElement(lexer, element);
							return;
						}
					} else if ((node.tag.model & Dict.CM_BLOCK) != 0) {
						if (lexer.excludeBlocks) {
							if (!((element.tag.model & Dict.CM_OPT) != 0))
									Report.warning(lexer, element, node, Report.MISSING_ENDTAG_BEFORE);

							lexer.ungetToken();

							if ((element.tag.model & Dict.CM_OBJECT) != 0) lexer.istackbase = istackbase;

							Node.trimSpaces(lexer, element);
							Node.trimEmptyElement(lexer, element);
							return;
						}
					} else /* things like list items */
					{
						if (!((element.tag.model & Dict.CM_OPT) != 0) && !element.implicit)
								Report.warning(lexer, element, node, Report.MISSING_ENDTAG_BEFORE);

						if ((node.tag.model & Dict.CM_HEAD) != 0) {
							moveToHead(lexer, element, node);
							continue;
						}

						lexer.ungetToken();

						if ((node.tag.model & Dict.CM_LIST) != 0) {
							if (element.parent != null && element.parent.tag != null
									&& element.parent.tag.parser == getParseList()) {
								Node.trimSpaces(lexer, element);
								Node.trimEmptyElement(lexer, element);
								return;
							}

							node = lexer.inferredTag("ul");
							Node.addClass(node, "noindent");
						} else if ((node.tag.model & Dict.CM_DEFLIST) != 0) {
							if (element.parent.tag == tt.tagDl) {
								Node.trimSpaces(lexer, element);
								Node.trimEmptyElement(lexer, element);
								return;
							}

							node = lexer.inferredTag("dl");
						} else if ((node.tag.model & Dict.CM_TABLE) != 0 || (node.tag.model & Dict.CM_ROW) != 0) {
							node = lexer.inferredTag("table");
						} else if ((element.tag.model & Dict.CM_OBJECT) != 0) {
							/* pop inline stack */
							while (lexer.istack.size() > lexer.istackbase)
								lexer.popInline(null);
							lexer.istackbase = istackbase;
							Node.trimSpaces(lexer, element);
							Node.trimEmptyElement(lexer, element);
							return;

						} else {
							Node.trimSpaces(lexer, element);
							Node.trimEmptyElement(lexer, element);
							return;
						}
					}
				}

				/* parse known element */
				if (node.type == Node.StartTag || node.type == Node.StartEndTag) {
					if ((node.tag.model & Dict.CM_INLINE) != 0) {
						if (checkstack && !node.implicit) {
							checkstack = false;

							if (lexer.inlineDup(node) > 0) continue;
						}

						mode = Lexer.MixedContent;
					} else {
						checkstack = true;
						mode = Lexer.IgnoreWhitespace;
					}

					/* trim white space before <br> */
					if (node.tag == tt.tagBr) Node.trimSpaces(lexer, element);

					Node.insertNodeAtEnd(element, node);

					if (node.implicit) Report.warning(lexer, element, node, Report.INSERTING_TAG);

					parseTag(lexer, node, Lexer.IgnoreWhitespace /*Lexer.MixedContent*/);
					continue;
				}

				/* discard unexpected tags */
				if (node.type == Node.EndTag) lexer.popInline(node); /* if inline end tag */

				Report.warning(lexer, element, node, Report.DISCARDING_UNEXPECTED);
			}

			if (!((element.tag.model & Dict.CM_OPT) != 0))
					Report.warning(lexer, element, node, Report.MISSING_ENDTAG_FOR);

			if ((element.tag.model & Dict.CM_OBJECT) != 0) {
				/* pop inline stack */
				while (lexer.istack.size() > lexer.istackbase)
					lexer.popInline(null);
				lexer.istackbase = istackbase;
			}

			Node.trimSpaces(lexer, element);
			Node.trimEmptyElement(lexer, element);
		}

	};

	public static class ParseTableTag implements Parser {

		public void parse(Lexer lexer, Node table, short mode) {
			Node node, parent;
			int istackbase;
			TagTable tt = lexer.configuration.tt;

			lexer.deferDup();
			istackbase = lexer.istackbase;
			lexer.istackbase = lexer.istack.size();

			while (true) {
				node = lexer.getToken(Lexer.IgnoreWhitespace);
				if (node == null) break;
				if (node.tag == table.tag && node.type == Node.EndTag) {
					lexer.istackbase = istackbase;
					table.closed = true;
					Node.trimEmptyElement(lexer, table);
					return;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(table, node)) continue;

				/* discard unknown tags */
				if (node.tag == null && node.type != Node.TextNode) {
					Report.warning(lexer, table, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/* if TD or TH or text or inline or block then infer <TR> */

				if (node.type != Node.EndTag) {
					if (node.tag == tt.tagTd || node.tag == tt.tagTh || node.tag == tt.tagTable) {
						lexer.ungetToken();
						node = lexer.inferredTag("tr");
						Report.warning(lexer, table, node, Report.MISSING_STARTTAG);
					} else if (node.type == Node.TextNode || (node.tag.model & (Dict.CM_BLOCK | Dict.CM_INLINE)) != 0) {
						Node.insertNodeBeforeElement(table, node);
						Report.warning(lexer, table, node, Report.TAG_NOT_ALLOWED_IN);
						lexer.exiled = true;
						if (false) parseTag(lexer, node, Lexer.IgnoreWhitespace);

						lexer.exiled = false;
						continue;
					} else if ((node.tag.model & Dict.CM_HEAD) != 0) {
						moveToHead(lexer, table, node);
						continue;
					}
				}

				/* 
				 if this is the end tag for an ancestor element
				 then infer end tag for this element
				 */
				if (node.type == Node.EndTag) {
					if (node.tag == tt.tagForm) {
						lexer.badForm = 1;
						Report.warning(lexer, table, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					if (node.tag != null && (node.tag.model & (Dict.CM_TABLE | Dict.CM_ROW)) != 0) {
						Report.warning(lexer, table, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					for (parent = table.parent; parent != null; parent = parent.parent) {
						if (node.tag == parent.tag) {
							Report.warning(lexer, table, node, Report.MISSING_ENDTAG_BEFORE);
							lexer.ungetToken();
							lexer.istackbase = istackbase;
							Node.trimEmptyElement(lexer, table);
							return;
						}
					}
				}

				if (!((node.tag.model & Dict.CM_TABLE) != 0)) {
					lexer.ungetToken();
					Report.warning(lexer, table, node, Report.TAG_NOT_ALLOWED_IN);
					lexer.istackbase = istackbase;
					Node.trimEmptyElement(lexer, table);
					return;
				}

				if (node.type == Node.StartTag || node.type == Node.StartEndTag) {
					Node.insertNodeAtEnd(table, node);
					;
					parseTag(lexer, node, Lexer.IgnoreWhitespace);
					continue;
				}

				/* discard unexpected text nodes and end tags */
				Report.warning(lexer, table, node, Report.DISCARDING_UNEXPECTED);
			}

			Report.warning(lexer, table, node, Report.MISSING_ENDTAG_FOR);
			Node.trimEmptyElement(lexer, table);
			lexer.istackbase = istackbase;
		}

	};

	public static class ParseColGroup implements Parser {

		public void parse(Lexer lexer, Node colgroup, short mode) {
			Node node, parent;
			TagTable tt = lexer.configuration.tt;

			if ((colgroup.tag.model & Dict.CM_EMPTY) != 0) return;

			while (true) {
				node = lexer.getToken(Lexer.IgnoreWhitespace);
				if (node == null) break;
				if (node.tag == colgroup.tag && node.type == Node.EndTag) {
					colgroup.closed = true;
					return;
				}

				/* 
				 if this is the end tag for an ancestor element
				 then infer end tag for this element
				 */
				if (node.type == Node.EndTag) {
					if (node.tag == tt.tagForm) {
						lexer.badForm = 1;
						Report.warning(lexer, colgroup, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					for (parent = colgroup.parent; parent != null; parent = parent.parent) {

						if (node.tag == parent.tag) {
							lexer.ungetToken();
							return;
						}
					}
				}

				if (node.type == Node.TextNode) {
					lexer.ungetToken();
					return;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(colgroup, node)) continue;

				/* discard unknown tags */
				if (node.tag == null) {
					Report.warning(lexer, colgroup, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				if (node.tag != tt.tagCol) {
					lexer.ungetToken();
					return;
				}

				if (node.type == Node.EndTag) {
					Report.warning(lexer, colgroup, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/* node should be <COL> */
				Node.insertNodeAtEnd(colgroup, node);
				parseTag(lexer, node, Lexer.IgnoreWhitespace);
			}
		}

	};

	public static class ParseRowGroup implements Parser {

		public void parse(Lexer lexer, Node rowgroup, short mode) {
			Node node, parent;
			TagTable tt = lexer.configuration.tt;

			if ((rowgroup.tag.model & Dict.CM_EMPTY) != 0) return;

			while (true) {
				node = lexer.getToken(Lexer.IgnoreWhitespace);
				if (node == null) break;
				if (node.tag == rowgroup.tag) {
					if (node.type == Node.EndTag) {
						rowgroup.closed = true;
						Node.trimEmptyElement(lexer, rowgroup);
						return;
					}

					lexer.ungetToken();
					return;
				}

				/* if </table> infer end tag */
				if (node.tag == tt.tagTable && node.type == Node.EndTag) {
					lexer.ungetToken();
					Node.trimEmptyElement(lexer, rowgroup);
					return;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(rowgroup, node)) continue;

				/* discard unknown tags */
				if (node.tag == null && node.type != Node.TextNode) {
					Report.warning(lexer, rowgroup, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/*
				 if TD or TH then infer <TR>
				 if text or inline or block move before table
				 if head content move to head
				 */

				if (node.type != Node.EndTag) {
					if (node.tag == tt.tagTd || node.tag == tt.tagTh) {
						lexer.ungetToken();
						node = lexer.inferredTag("tr");
						Report.warning(lexer, rowgroup, node, Report.MISSING_STARTTAG);
					} else if (node.type == Node.TextNode || (node.tag.model & (Dict.CM_BLOCK | Dict.CM_INLINE)) != 0) {
						Node.moveBeforeTable(rowgroup, node, tt);
						Report.warning(lexer, rowgroup, node, Report.TAG_NOT_ALLOWED_IN);
						lexer.exiled = true;

						if (node.type != Node.TextNode) parseTag(lexer, node, Lexer.IgnoreWhitespace);

						lexer.exiled = false;
						continue;
					} else if ((node.tag.model & Dict.CM_HEAD) != 0) {
						Report.warning(lexer, rowgroup, node, Report.TAG_NOT_ALLOWED_IN);
						moveToHead(lexer, rowgroup, node);
						continue;
					}
				}

				/* 
				 if this is the end tag for ancestor element
				 then infer end tag for this element
				 */
				if (node.type == Node.EndTag) {
					if (node.tag == tt.tagForm) {
						lexer.badForm = 1;
						Report.warning(lexer, rowgroup, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					if (node.tag == tt.tagTr || node.tag == tt.tagTd || node.tag == tt.tagTh) {
						Report.warning(lexer, rowgroup, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					for (parent = rowgroup.parent; parent != null; parent = parent.parent) {
						if (node.tag == parent.tag) {
							lexer.ungetToken();
							Node.trimEmptyElement(lexer, rowgroup);
							return;
						}
					}
				}

				/*
				 if THEAD, TFOOT or TBODY then implied end tag

				 */
				if ((node.tag.model & Dict.CM_ROWGRP) != 0) {
					if (node.type != Node.EndTag) lexer.ungetToken();

					Node.trimEmptyElement(lexer, rowgroup);
					return;
				}

				if (node.type == Node.EndTag) {
					Report.warning(lexer, rowgroup, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				if (!(node.tag == tt.tagTr)) {
					node = lexer.inferredTag("tr");
					Report.warning(lexer, rowgroup, node, Report.MISSING_STARTTAG);
					lexer.ungetToken();
				}

				/* node should be <TR> */
				Node.insertNodeAtEnd(rowgroup, node);
				parseTag(lexer, node, Lexer.IgnoreWhitespace);
			}

			Node.trimEmptyElement(lexer, rowgroup);
		}

	};

	public static class ParseRow implements Parser {

		public void parse(Lexer lexer, Node row, short mode) {
			Node node, parent;
			boolean exclude_state;
			TagTable tt = lexer.configuration.tt;

			if ((row.tag.model & Dict.CM_EMPTY) != 0) return;

			while (true) {
				node = lexer.getToken(Lexer.IgnoreWhitespace);
				if (node == null) break;
				if (node.tag == row.tag) {
					if (node.type == Node.EndTag) {
						row.closed = true;
						Node.fixEmptyRow(lexer, row);
						return;
					}

					lexer.ungetToken();
					Node.fixEmptyRow(lexer, row);
					return;
				}

				/* 
				 if this is the end tag for an ancestor element
				 then infer end tag for this element
				 */
				if (node.type == Node.EndTag) {
					if (node.tag == tt.tagForm) {
						lexer.badForm = 1;
						Report.warning(lexer, row, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					if (node.tag == tt.tagTd || node.tag == tt.tagTh) {
						Report.warning(lexer, row, node, Report.DISCARDING_UNEXPECTED);
						continue;
					}

					for (parent = row.parent; parent != null; parent = parent.parent) {
						if (node.tag == parent.tag) {
							lexer.ungetToken();
							Node.trimEmptyElement(lexer, row);
							return;
						}
					}
				}

				/* deal with comments etc. */
				if (Node.insertMisc(row, node)) continue;

				/* discard unknown tags */
				if (node.tag == null && node.type != Node.TextNode) {
					Report.warning(lexer, row, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/* discard unexpected <table> element */
				if (node.tag == tt.tagTable) {
					Report.warning(lexer, row, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/* THEAD, TFOOT or TBODY */
				if (node.tag != null && (node.tag.model & Dict.CM_ROWGRP) != 0) {
					lexer.ungetToken();
					Node.trimEmptyElement(lexer, row);
					return;
				}

				if (node.type == Node.EndTag) {
					Report.warning(lexer, row, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/*
				 if text or inline or block move before table
				 if head content move to head
				 */

				if (node.type != Node.EndTag) {
					if (node.tag == tt.tagForm) {
						lexer.ungetToken();
						node = lexer.inferredTag("td");
						Report.warning(lexer, row, node, Report.MISSING_STARTTAG);
					} else if (node.type == Node.TextNode || (node.tag.model & (Dict.CM_BLOCK | Dict.CM_INLINE)) != 0) {
						Node.moveBeforeTable(row, node, tt);
						Report.warning(lexer, row, node, Report.TAG_NOT_ALLOWED_IN);
						lexer.exiled = true;

						if (node.type != Node.TextNode) parseTag(lexer, node, Lexer.IgnoreWhitespace);

						lexer.exiled = false;
						continue;
					} else if ((node.tag.model & Dict.CM_HEAD) != 0) {
						Report.warning(lexer, row, node, Report.TAG_NOT_ALLOWED_IN);
						moveToHead(lexer, row, node);
						continue;
					}
				}

				if (!(node.tag == tt.tagTd || node.tag == tt.tagTh)) {
					Report.warning(lexer, row, node, Report.TAG_NOT_ALLOWED_IN);
					continue;
				}

				/* node should be <TD> or <TH> */
				Node.insertNodeAtEnd(row, node);
				exclude_state = lexer.excludeBlocks;
				lexer.excludeBlocks = false;
				parseTag(lexer, node, Lexer.IgnoreWhitespace);
				lexer.excludeBlocks = exclude_state;

				/* pop inline stack */

				while (lexer.istack.size() > lexer.istackbase)
					lexer.popInline(null);
			}

			Node.trimEmptyElement(lexer, row);
		}

	};

	public static class ParseNoFrames implements Parser {

		public void parse(Lexer lexer, Node noframes, short mode) {
			Node node;
			boolean checkstack;
			TagTable tt = lexer.configuration.tt;

			lexer.badAccess |= Report.USING_NOFRAMES;
			mode = Lexer.IgnoreWhitespace;
			checkstack = true;

			while (true) {
				node = lexer.getToken(mode);
				if (node == null) break;
				if (node.tag == noframes.tag && node.type == Node.EndTag) {
					noframes.closed = true;
					Node.trimSpaces(lexer, noframes);
					return;
				}

				if ((node.tag == tt.tagFrame || node.tag == tt.tagFrameset)) {
					Report.warning(lexer, noframes, node, Report.MISSING_ENDTAG_BEFORE);
					Node.trimSpaces(lexer, noframes);
					lexer.ungetToken();
					return;
				}

				if (node.tag == tt.tagHtml) {
					if (node.type == Node.StartTag || node.type == Node.StartEndTag)
							Report.warning(lexer, noframes, node, Report.DISCARDING_UNEXPECTED);

					continue;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(noframes, node)) continue;

				if (node.tag == tt.tagBody && node.type == Node.StartTag) {
					Node.insertNodeAtEnd(noframes, node);
					parseTag(lexer, node, Lexer.IgnoreWhitespace /*MixedContent*/);
					continue;
				}

				/* implicit body element inferred */
				if (node.type == Node.TextNode || node.tag != null) {
					lexer.ungetToken();
					node = lexer.inferredTag("body");
					if (lexer.configuration.XmlOut) Report.warning(lexer, noframes, node, Report.INSERTING_TAG);
					Node.insertNodeAtEnd(noframes, node);
					parseTag(lexer, node, Lexer.IgnoreWhitespace /*MixedContent*/);
					continue;
				}
				/* discard unexpected end tags */
				Report.warning(lexer, noframes, node, Report.DISCARDING_UNEXPECTED);
			}

			Report.warning(lexer, noframes, node, Report.MISSING_ENDTAG_FOR);
		}

	};

	public static class ParseSelect implements Parser {

		public void parse(Lexer lexer, Node field, short mode) {
			Node node;
			TagTable tt = lexer.configuration.tt;

			lexer.insert = -1; /* defer implicit inline start tags */

			while (true) {
				node = lexer.getToken(Lexer.IgnoreWhitespace);
				if (node == null) break;
				if (node.tag == field.tag && node.type == Node.EndTag) {
					field.closed = true;
					Node.trimSpaces(lexer, field);
					return;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(field, node)) continue;

				if (node.type == Node.StartTag
						&& (node.tag == tt.tagOption || node.tag == tt.tagOptgroup || node.tag == tt.tagScript)) {
					Node.insertNodeAtEnd(field, node);
					parseTag(lexer, node, Lexer.IgnoreWhitespace);
					continue;
				}

				/* discard unexpected tags */
				Report.warning(lexer, field, node, Report.DISCARDING_UNEXPECTED);
			}

			Report.warning(lexer, field, node, Report.MISSING_ENDTAG_FOR);
		}

	};

	public static class ParseText implements Parser {

		public void parse(Lexer lexer, Node field, short mode) {
			Node node;
			TagTable tt = lexer.configuration.tt;

			lexer.insert = -1; /* defer implicit inline start tags */

			if (field.tag == tt.tagTextarea) mode = Lexer.Preformatted;

			while (true) {
				node = lexer.getToken(mode);
				if (node == null) break;
				if (node.tag == field.tag && node.type == Node.EndTag) {
					field.closed = true;
					Node.trimSpaces(lexer, field);
					return;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(field, node)) continue;

				if (node.type == Node.TextNode) {
					/* only called for 1st child */
					if (field.content == null && !((mode & Lexer.Preformatted) != 0)) Node.trimSpaces(lexer, field);

					if (node.start >= node.end) {
						continue;
					}

					Node.insertNodeAtEnd(field, node);
					continue;
				}

				if (node.tag == tt.tagFont) {
					Report.warning(lexer, field, node, Report.DISCARDING_UNEXPECTED);
					continue;
				}

				/* terminate element on other tags */
				if (!((field.tag.model & Dict.CM_OPT) != 0))
						Report.warning(lexer, field, node, Report.MISSING_ENDTAG_BEFORE);

				lexer.ungetToken();
				Node.trimSpaces(lexer, field);
				return;
			}

			if (!((field.tag.model & Dict.CM_OPT) != 0)) Report.warning(lexer, field, node, Report.MISSING_ENDTAG_FOR);
		}

	};

	public static class ParseOptGroup implements Parser {

		public void parse(Lexer lexer, Node field, short mode) {
			Node node;
			TagTable tt = lexer.configuration.tt;

			lexer.insert = -1; /* defer implicit inline start tags */

			while (true) {
				node = lexer.getToken(Lexer.IgnoreWhitespace);
				if (node == null) break;
				if (node.tag == field.tag && node.type == Node.EndTag) {
					field.closed = true;
					Node.trimSpaces(lexer, field);
					return;
				}

				/* deal with comments etc. */
				if (Node.insertMisc(field, node)) continue;

				if (node.type == Node.StartTag && (node.tag == tt.tagOption || node.tag == tt.tagOptgroup)) {
					if (node.tag == tt.tagOptgroup) Report.warning(lexer, field, node, Report.CANT_BE_NESTED);

					Node.insertNodeAtEnd(field, node);
					parseTag(lexer, node, Lexer.MixedContent);
					continue;
				}

				/* discard unexpected tags */
				Report.warning(lexer, field, node, Report.DISCARDING_UNEXPECTED);
			}
		}

	};

	public static Parser getParseHTML() {
		return _parseHTML;
	}

	public static Parser getParseHead() {
		return _parseHead;
	}

	public static Parser getParseTitle() {
		return _parseTitle;
	}

	public static Parser getParseScript() {
		return _parseScript;
	}

	public static Parser getParseBody() {
		return _parseBody;
	}

	public static Parser getParseFrameSet() {
		return _parseFrameSet;
	}

	public static Parser getParseInline() {
		return _parseInline;
	}

	public static Parser getParseList() {
		return _parseList;
	}

	public static Parser getParseDefList() {
		return _parseDefList;
	}

	public static Parser getParsePre() {
		return _parsePre;
	}

	public static Parser getParseBlock() {
		return _parseBlock;
	}

	public static Parser getParseTableTag() {
		return _parseTableTag;
	}

	public static Parser getParseColGroup() {
		return _parseColGroup;
	}

	public static Parser getParseRowGroup() {
		return _parseRowGroup;
	}

	public static Parser getParseRow() {
		return _parseRow;
	}

	public static Parser getParseNoFrames() {
		return _parseNoFrames;
	}

	public static Parser getParseSelect() {
		return _parseSelect;
	}

	public static Parser getParseText() {
		return _parseText;
	}

	public static Parser getParseOptGroup() {
		return _parseOptGroup;
	}

	private static Parser _parseHTML = new ParseHTML();
	private static Parser _parseHead = new ParseHead();
	private static Parser _parseTitle = new ParseTitle();
	private static Parser _parseScript = new ParseScript();
	private static Parser _parseBody = new ParseBody();
	private static Parser _parseFrameSet = new ParseFrameSet();
	private static Parser _parseInline = new ParseInline();
	private static Parser _parseList = new ParseList();
	private static Parser _parseDefList = new ParseDefList();
	private static Parser _parsePre = new ParsePre();
	private static Parser _parseBlock = new ParseBlock();
	private static Parser _parseTableTag = new ParseTableTag();
	private static Parser _parseColGroup = new ParseColGroup();
	private static Parser _parseRowGroup = new ParseRowGroup();
	private static Parser _parseRow = new ParseRow();
	private static Parser _parseNoFrames = new ParseNoFrames();
	private static Parser _parseSelect = new ParseSelect();
	private static Parser _parseText = new ParseText();
	private static Parser _parseOptGroup = new ParseOptGroup();

	/*
	 HTML is the top level element
	 */
	public static Node parseDocument(Lexer lexer) {
		Node node, document, html;
		Node doctype = null;
		TagTable tt = lexer.configuration.tt;

		document = lexer.newNode();
		document.type = Node.RootNode;

		while (true) {
			node = lexer.getToken(Lexer.IgnoreWhitespace);
			if (node == null) break;

			/* deal with comments etc. */
			if (Node.insertMisc(document, node)) continue;

			if (node.type == Node.DocTypeTag) {
				if (doctype == null) {
					Node.insertNodeAtEnd(document, node);
					doctype = node;
				} else
					Report.warning(lexer, document, node, Report.DISCARDING_UNEXPECTED);
				continue;
			}

			if (node.type == Node.EndTag) {
				Report.warning(lexer, document, node, Report.DISCARDING_UNEXPECTED); //TODO?
				continue;
			}

			if (node.type != Node.StartTag || node.tag != tt.tagHtml) {
				lexer.ungetToken();
				html = lexer.inferredTag("html");
			} else
				html = node;

			Node.insertNodeAtEnd(document, html);
			getParseHTML().parse(lexer, html, (short) 0); // TODO?
			break;
		}

		return document;
	}

	/**
	 *  Indicates whether or not whitespace should be preserved for this element.
	 *  If an <code>xml:space</code> attribute is found, then if the attribute value is
	 *  <code>preserve</code>, returns <code>true</code>.  For any other value, returns
	 *  <code>false</code>.  If an <code>xml:space</code> attribute was <em>not</em>
	 *  found, then the following element names result in a return value of <code>true:
	 *  pre, script, style,</code> and <code>xsl:text</code>.  Finally, if a
	 *  <code>TagTable</code> was passed in and the element appears as the "pre" element
	 *  in the <code>TagTable</code>, then <code>true</code> will be returned.
	 *  Otherwise, <code>false</code> is returned.
	 *  @param element The <code>Node</code> to test to see if whitespace should be
	 *                 preserved.
	 *  @param tt The <code>TagTable</code> to test for the <code>getNodePre()</code>
	 *            function.  This may be <code>null</code>, in which case this test
	 *            is bypassed.
	 *  @return <code>true</code> or <code>false</code>, as explained above.
	 */

	public static boolean XMLPreserveWhiteSpace(Node element, TagTable tt) {
		AttVal attribute;

		/* search attributes for xml:space */
		for (attribute = element.attributes; attribute != null; attribute = attribute.next) {
			if (attribute.attribute.equals("xml:space")) {
				if (attribute.value.equals("preserve")) return true;

				return false;
			}
		}

		/* kludge for html docs without explicit xml:space attribute */
		if (Lexer.wstrcasecmp(element.element, "pre") == 0 || Lexer.wstrcasecmp(element.element, "script") == 0
				|| Lexer.wstrcasecmp(element.element, "style") == 0) return true;

		if ((tt != null) && (tt.findParser(element) == getParsePre())) return true;

		/* kludge for XSL docs */
		if (Lexer.wstrcasecmp(element.element, "xsl:text") == 0) return true;

		return false;
	}

	/*
	 XML documents
	 */
	public static void parseXMLElement(Lexer lexer, Node element, short mode) {
		Node node;

		/* Jeff Young's kludge for XSL docs */

		if (Lexer.wstrcasecmp(element.element, "xsl:text") == 0) return;

		/* if node is pre or has xml:space="preserve" then do so */

		if (XMLPreserveWhiteSpace(element, lexer.configuration.tt)) mode = Lexer.Preformatted;

		while (true) {
			node = lexer.getToken(mode);
			if (node == null) break;
			if (node.type == Node.EndTag && node.element.equals(element.element)) {
				element.closed = true;
				break;
			}

			/* discard unexpected end tags */
			if (node.type == Node.EndTag) {
				Report.error(lexer, element, node, Report.UNEXPECTED_ENDTAG);
				continue;
			}

			/* parse content on seeing start tag */
			if (node.type == Node.StartTag) parseXMLElement(lexer, node, mode);

			Node.insertNodeAtEnd(element, node);
		}

		/*
		 if first child is text then trim initial space and
		 delete text node if it is empty.
		 */

		node = element.content;

		if (node != null && node.type == Node.TextNode && mode != Lexer.Preformatted) {
			if (node.textarray[node.start] == (byte) ' ') {
				node.start++;

				if (node.start >= node.end) Node.discardElement(node);
			}
		}

		/*
		 if last child is text then trim final space and
		 delete the text node if it is empty
		 */

		node = element.last;

		if (node != null && node.type == Node.TextNode && mode != Lexer.Preformatted) {
			if (node.textarray[node.end - 1] == (byte) ' ') {
				node.end--;

				if (node.start >= node.end) Node.discardElement(node);
			}
		}
	}

	public static Node parseXMLDocument(Lexer lexer) {
		Node node, document, doctype;

		document = lexer.newNode();
		document.type = Node.RootNode;
		doctype = null;
		lexer.configuration.XmlTags = true;

		while (true) {
			node = lexer.getToken(Lexer.IgnoreWhitespace);
			if (node == null) break;
			/* discard unexpected end tags */
			if (node.type == Node.EndTag) {
				Report.warning(lexer, null, node, Report.UNEXPECTED_ENDTAG);
				continue;
			}

			/* deal with comments etc. */
			if (Node.insertMisc(document, node)) continue;

			if (node.type == Node.DocTypeTag) {
				if (doctype == null) {
					Node.insertNodeAtEnd(document, node);
					doctype = node;
				} else
					Report.warning(lexer, document, node, Report.DISCARDING_UNEXPECTED);  
				continue;
			}

			/* if start tag then parse element's content */
			if (node.type == Node.StartTag) {
				Node.insertNodeAtEnd(document, node);
				parseXMLElement(lexer, node, Lexer.IgnoreWhitespace);
			}

		}

		if (false) { //#if 0
			/* discard the document type */
			node = document.findDocType();

			if (node != null) Node.discardElement(node);
		} // #endif

		if (doctype != null && !lexer.checkDocTypeKeyWords(doctype))
				Report.warning(lexer, doctype, null, Report.DTYPE_NOT_UPPER_CASE);

		/* ensure presence of initial <?XML version="1.0"?> */
		if (lexer.configuration.XmlPi) lexer.fixXMLPI(document);

		return document;
	}

	public static boolean isJavaScript(Node node) {
		boolean result = false;
		AttVal attr;

		if (node.attributes == null) return true;

		for (attr = node.attributes; attr != null; attr = attr.next) {
			if ((Lexer.wstrcasecmp(attr.attribute, "language") == 0 || Lexer.wstrcasecmp(attr.attribute, "type") == 0)
					&& Lexer.wsubstr(attr.value, "javascript")) result = true;
		}

		return result;
	}

}