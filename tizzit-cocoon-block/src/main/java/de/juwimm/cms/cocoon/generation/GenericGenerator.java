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
package de.juwimm.cms.cocoon.generation;

/**
 * Generic <code>Generator</code> for dynamically loading of site-specific generators.
 *
 * <p><h5>Configuration:</h5>
 * <pre>
 * &lt;map:generator name="newGenerator" src="de.juwimm.novartis.tania.cocoon.generation.GenericGenerator"&gt;
 *    &lt;classname>de.juwimm.vfa.game.cocoon.generation.AdminGenerator&lt;/classname&gt;
 *    &lt;classpath&gt;
 *       &lt;jar&gt;juwimm-web-vfa-game-cocoon-1.0.jar&lt;/jar&gt;
 *       &lt;jar&gt;juwimm-web-vfa-game-remote-1.0.jar&lt;/jar&gt;
 *    &lt;/classpath&gt;
 *    &lt;siteShort&gt;vfa-game&lt;/siteShort&gt;
 * &lt;/map:generator&gt;
 * </pre>
 * </p>
 * <p><h5>Usage:</h5>
 * You can now use this generator like every other generator.
 * </p>
 * @author <a href="carsten.schalm@juwimm.com">Carsten Schalm</a>
 * Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 * @deprecated Use {@link org.tizzit.cocoon.generic.generation.GenericGenerator} instead!
 */
@Deprecated
public class GenericGenerator extends org.tizzit.cocoon.generic.generation.GenericGenerator {
}
