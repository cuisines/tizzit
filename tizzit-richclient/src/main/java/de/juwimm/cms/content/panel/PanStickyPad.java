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
package de.juwimm.cms.content.panel;

import static de.juwimm.cms.common.Constants.*;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.frame.FrmStickyPad;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.ayyappan@juwimm.com">Sabarinath Ayyappan</a>
 * @version $Id$
 */
public class PanStickyPad extends JPanel {
	private static FrmStickyPad stickyPad = FrmStickyPad.getInstance();
	private JButton button = null;

	public PanStickyPad() {
		setLayout(new FlowLayout());
		button = new JButton(rb.getString("stickypad.viewbutton"));
		add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				stickyPad.showStickyPad();
			}
		});
	}

	public void setText(String message) {
		stickyPad.setText(message);
	}

	public String getText() {
		return stickyPad.getText();
	}

	public void setProperties(Node prop) {
		setText(XercesHelper.getNodeValue(prop));
	}

	public Node getProperties() {
		Element elm = null;
		elm = ContentManager.getDomDoc().createElement("stickypad");
		Text txtNode = ContentManager.getDomDoc().createTextNode(getText());
		elm.appendChild(txtNode);
		return elm;
	}

	public void setEnabled(boolean enable) {
		stickyPad.setEnabled(enable);
		button.setEnabled(enable);
	}
}