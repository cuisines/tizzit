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
package de.juwimm.cms.cocoon.transformation;


/**
 *
 * sitemap.xml-snippets:
 * <map:transformers default="xalan">
 * 		...
 * 		<map:transformer name="adminTransformer" src="de.juwimm.cms.cocoon.transformation.GenericTransformer">
 *			<classname>de.juwimm.vfa.game.cocoon.transformation.AdminTransformer</classname>
 *			<classpath>
 *	 			<jar>juwimm-web-vfa-game-cocoon-1.0.jar</jar>
 *	 			<jar>juwimm-web-vfa-game-remote-1.0.jar</jar>
 *	 		</classpath>
 *	 		<siteShort>vfa-game</siteShort>
 *		</map:transformer>
 *		...
 * </map:transformers>
 *
 * <map:match pattern="generictransformertest/transformertest">
 *		<map:generate src="xml/transformertest.xml"/>
 *		<map:transform type="adminTransformer">
 *			<!-- optional if not configured at declaration -->
 *			<map:parameter name="clientCode" value="{global:clientCode}"/>
 *		</map:transform>
 *		<map:serialize type="xml"/>
 *	</map:match>
 *
 *	lifecycle (always pooled because it implements Recyclable):
 *	1.) configure
 *  2.) setup(...)
 *  3.) ...
 *  4.) recycle(...)
 *  5.) setup(...)
 *  6.) ...
 *  7.) recycle(...)
 *  ...
 *
 * @author toerberj
 * Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @deprecated Use {@link org.tizzit.cocoon.generic.transformation.GenericTransformer} instead!
 */
@Deprecated
public class GenericTransformer extends org.tizzit.cocoon.generic.transformation.GenericTransformer {
}
