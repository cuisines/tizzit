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
package de.juwimm.swing.domtotree;

/**
 * <p>Title: DomToTree</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: JuwiMacMillan</p>
 * @author Michael Meyer
 * @version 1.0
 */
public final class AktColorNode {
	private static MyTreeNode colorNode;

	private AktColorNode() {
	}

	/**
	 * @return Returns the colorNode.
	 */
	public static MyTreeNode getColorNode() {
		return colorNode;
	}

	/**
	 * @param colorNode The colorNode to set.
	 */
	public static void setColorNode(MyTreeNode colorNode) {
		AktColorNode.colorNode = colorNode;
	}
}