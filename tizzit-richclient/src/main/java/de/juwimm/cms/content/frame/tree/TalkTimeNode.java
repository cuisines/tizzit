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
package de.juwimm.cms.content.frame.tree;

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.components.vo.TalktimeValue;
import de.juwimm.cms.content.frame.helper.IsolatedAggregationHelper;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;

/**
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik </a>
 * @version $Id$
 */
public class TalkTimeNode extends ComponentNode {
	private TalktimeValue talkTime;

	public boolean hasChildren() {
		return false;
	}

	public TalkTimeNode() {
	}

	public TalkTimeNode(TalktimeValue data, IsolatedAggregationHelper aggHelper) {
		this.setAggregationHelper(aggHelper);
		setIcon(UIConstants.ICON_TALKTIME);

		talkTime = data;
		try {
			setClicks(this.getAggregationHelper().getClickHashForNode("talkTime", talkTime.getTalkTimeId() + ""));
		} catch (Exception ex) {
		}
	}

	public String getToolTipText() {
		return rb.getString("PanDBCTalkTime.talkTime");
	}

	public String toString() {
		return talkTime.getTalkTimeType();
	}

	public TalktimeValue getData() {
		return talkTime;
	}

	public void remove() throws Exception {
		this.removeFromParent();
		((Communication) getBean(Beans.COMMUNICATION)).removeTalktime(talkTime.getTalkTimeId());
	}

	public long getId() {
		return talkTime.getTalkTimeId();
	}

	public String getDescription() {
		return talkTime.getTalkTimeType();
	}

	public Node getXmlRepresentation(Document doc, Node node) {
		Element include = doc.createElement("include");
		node.appendChild(include);

		include.setAttribute("type", "talkTime");
		include.setAttribute("id", talkTime.getTalkTimeId() + "");

		if (getHashClicks().size() > 0) {
			Element content = doc.createElement("content");
			include.appendChild(content);

			for (java.util.Enumeration e = getHashClicks().keys(); e.hasMoreElements();) {
				String elName = (String) e.nextElement();

				if (new Integer(1).equals(getHashClicks().get(elName))) {
					content.appendChild(doc.createElement(elName));
				}
			}
		}
		return include;
	}

}