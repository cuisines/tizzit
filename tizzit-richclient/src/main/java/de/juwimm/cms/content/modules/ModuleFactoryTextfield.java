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

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import de.juwimm.util.XercesHelper;


/**
 * A {@link ModuleFactory} implementation for {@code Textfield}s and its subcomponents.
 * 
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class ModuleFactoryTextfield extends ModuleFactoryStandardImpl implements ModuleFactory {
	
	private static Logger log = Logger.getLogger(ModuleFactoryTextfield.class);
	
	/** Preserves all custom properties that were read from the dcf */
	private Hashtable<String, String> hshModuleCustomProperties = new Hashtable<String, String>();
	
	public ModuleFactoryTextfield() {
		super();
		log.debug("Created ModuleFactoryTextfield!");
	}

	/**
	 * Preserves the custom properties for later retrieval.
	 * 
	 * @param rootNodeAndClassName
	 * @param properties
	 */
	public void saveCustomProperties(String rootNodeAndClassName, String properties) {
		this.hshModuleCustomProperties.put(rootNodeAndClassName, properties);
	}
	
	/**
	 * Tries to find a configuration for the specified rootnode name and class name. 
	 * If a configuration was found, the specified {@code module} is configured appropriately. 
	 * 
	 * @param rootNodeAndClassName the root node's name + "#" + the class name
	 * @param module the module to configure
	 */
	public void resetCustomProperties(String rootNodeAndClassName, Module module) {
		if (this.hshModuleCustomProperties.containsKey(rootNodeAndClassName)) {
			String stringProperties = "<properties>" + this.hshModuleCustomProperties.get(rootNodeAndClassName) + "</properties>";
			try {
				Document dom = XercesHelper.string2Dom(stringProperties);
				Iterator iterator = XercesHelper.findNodes(dom, "//property");
				super.setCustomProperties(module, iterator);
			} catch (Exception exception) {
				log.error(exception);
			}
		}
	}
    
    /*
    public Module getModuleInstance(Element dcfelement, Node contentdata) {

    }

    public JPanel getPanelForModule(Module module) {

    }

    public Module getModuleByDCFName(String dcfname) {

    }

    public void reconfigureModules(Hashtable contentdata) {

    }
    */
}