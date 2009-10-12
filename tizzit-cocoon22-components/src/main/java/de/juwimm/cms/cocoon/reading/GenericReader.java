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
 * Created on 21.02.2006
 */
package de.juwimm.cms.cocoon.reading;


/**
 * all implemented interfaces:
 * Component, Generator, LogEnabled, Poolable, Recyclable, SitemapModelComponent, XMLProducer
 *
 * sitemap-snippets:
 * <map:readers>
 *		<map:reader name="AdminReader"	src="de.juwimm.cocoon.reading.GenericReader">
 *			<classname>de.juwimm.vfa.game.cocoon.reading.AdminReader</classname>
 *			<classpath>
 *	 			<jar>juwimm-web-vfa-game-cocoon-1.0.jar</jar>
 *	 			<jar>juwimm-web-vfa-game-remote-1.0.jar</jar>
 *	 		</classpath>
 *	 		<siteShort>vfa-game</siteShort>
 * 		</map:reader>
 * </map:readers>
 *
 * lifecycle:
 * 1.) configure
 * 2.) setup
 * 3.) generate
 * 4.) recycle
 * 5.) configure
 * 6.) setup
 * 7.) generate
 * 8.) recycle
 *
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 * @deprecated Use {@link org.tizzit.cocoon.generic.reading.GenericReader} instead!
 */
@Deprecated
public class GenericReader extends org.tizzit.cocoon.generic.reading.GenericReader {
}
