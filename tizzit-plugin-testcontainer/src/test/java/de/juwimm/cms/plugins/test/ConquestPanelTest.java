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
package de.juwimm.cms.plugins.test;

import junit.framework.TestCase;

public class ConquestPanelTest extends TestCase {

	private final String JARPATH = "C:\\svnroot\\juwimm-cms-plugin-simpletable\\plugin\\target\\juwimm-cms-plugin-simpletable-plugin-1.0.0.jar";
	private final String JARDIR = "C:\\svnroot\\juwimm-cms-plugin-simpletable\\plugin\\target\\";
	private final String DCFPATH = "C:\\svnroot\\juwimm-web-qmd\\web\\dcf\\simpletable.xml";

	private final String NAMESPACE = "simpletablePlugin";

	public void testLoadPlugin() {
		//		TizzitPanel conquest = new TizzitPanel();
		//		conquest.configurePlugin(JARPATH, DCFPATH);
		//		boolean ret = conquest.loadPlugin();
		//		assertEquals(true, ret);
	}

	//	public void testJarDirectory() {
	//		TizzitPanel conquest = new TizzitPanel();
	//		conquest.configurePlugin(JARPATH, DCFPATH);
	//
	//		String ret = "";
	//		Object[] ob = new Object[0];
	//
	//		Method[] methods = conquest.getClass().getDeclaredMethods();
	//		for (int i = 0; i < methods.length; i++) {
	//			String methodName = methods[i].getName();
	//			if (methodName.equals("getJarDir")) {
	//				methods[i].setAccessible(true);
	//				try {
	//					ret = (String) methods[i].invoke(conquest, ob);
	//				} catch (Exception ex) {
	//					System.out.println("CANNOT ACCESS METHOD " + ex.getMessage());
	//				}
	//				break;
	//			}
	//		}
	//
	//		assertEquals(JARDIR, ret);
	//	}

	/*	public void testNamespace() {
			
			ConquestPanel conquest = new ConquestPanel();
			conquest.configurePlugin(JARPATH,DCFPATH);
			
			String ret = "";
			Object[] ob = new Object[0];
			
			Method[] methods = conquest.getClass().getDeclaredMethods();
			for(int i=0; i<methods.length; i++) {
				String methodName = methods[i].getName();
				if(methodName.equals("getNamespace")) {
					methods[i].setAccessible(true);
					try {
						ret = (String)methods[i].invoke(conquest,ob);
					} catch (Exception ex) { 
						System.out.println("CANNOT ACCESS METHOD " + ex.getMessage());
					}
					break;
				}
			}
				
			assertEquals(NAMESPACE,ret);
			
		} */

}
