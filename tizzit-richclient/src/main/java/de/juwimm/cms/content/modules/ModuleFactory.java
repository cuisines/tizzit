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
package de.juwimm.cms.content.modules;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public interface ModuleFactory {
	public Iterator<Module> getAllModules();
	
    public Module getModuleInstance(Element dcfelement, Node contentdata);

    public Module getModuleInstanceUnconfigured(String classname, List<String> additionalJarFiles);

    public JPanel getPanelForModule(Module module);

    public Module getModuleByDCFName(String dcfname);

    public void reconfigureModules(Hashtable<String, Element> htModuleDcfNameDcfElement);

	public void setEnabled(boolean enabling);

	/**
	 * Sets the CustomProperties for an <b>unconfigured</b> Module.
	 * This Method should be used outside of the ModuleFactory Implementation Classes
	 * very carefully.
	 * @param module The Module, where the CustomProperties should be setted.
	 * @param propertyNodes An Iterator over Property-Nodes. Must contain org.w3c.dom.Element Objects.
	 */
	public void setCustomProperties(Module module, Iterator propertyNodes);

	/**
	 * This has to validate every Module contained inside the Factory-Hash.<br>
	 * Depending on the implementation this should interact with the user.
	 *
	 * @return The Errormessage to throw or the message who was been shown to the user.
	 * If there is no error, this MUST return an empty String, NOT a NULL object.
	 */
	public String isModuleValid();
}