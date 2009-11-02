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
 * input-snippet:
 * <?xml version="1.0" encoding="UTF-8"?>
 * ...
 * <!-- getuserdata wird konfiguriert mittels <map:parameter name="tagname" value="getuserdata"/> -->
 * <getuserdata>
 * 	  <!-- Als Bindvariablentypen sind derzeit nur String, Integer, Double und Date (im Format dd.mm.yyyy) erlaubt. -->
 *    <!--<param type="java.lang.Integer" value="3000"/>-->
 *    <param type="java.lang.String" value="jens@jens.de"/>
 * </getuserdata>
 * ...
 *
 * output-snippet:
 * <getuserdata>
 * 	<item>
 *  	<column_name1>value</column_name1>
 *  	<column_name2>value</column_name2>
 *  	...
 *  </item>
 *  ...
 * </getuserdata>
 *
 * sitemap.xml-snippets:
 * <map:transformers default="saxon">
 * 		...
 * 		<map:transformer name="sqlTransformer" src="de.juwimm.cms.cocoon.transformation.SQLTransformer">
 *			<dsJndiName>java:/SeronoDS</dsJndiName>
 *		</map:transformer>
 *		...
 * </map:transformers>
 *
 * <map:pipeline type="noncaching">
 *		<map:match pattern="sqltransformer">
 *			<map:generate src="xml/sqltransformertest.xml"/>
 *				<map:transform type="sqltransformer">
 *					<map:parameter name="sql" value="SELECT * FROM xuser WHERE email = ?"/>
 *					<map:parameter name="tagname" value="getuserdata"/>
 *				</map:transform>
 *				<map:transform src="xsl/nothing.xsl">
 *					<!--<map:parameter name="use-request-parameters" value="true"/>-->
 *				</map:transform>
 *			<map:serialize type="xml"/>
 *		</map:match>
 *	</map:pipeline>
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
 *
 * @deprecated Use {@link org.tizzit.cocoon.generic.transformation.SQLTransformer} instead!
 */
@Deprecated
public class SQLTransformer extends org.tizzit.cocoon.generic.transformation.SQLTransformer {
}
