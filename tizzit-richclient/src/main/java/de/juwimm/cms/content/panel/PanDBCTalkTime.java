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

import static de.juwimm.cms.client.beans.Application.*;
import static de.juwimm.cms.common.Constants.*;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.border.MatteBorder;

import org.apache.log4j.Logger;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import de.juwimm.cms.Messages;
import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.components.vo.TalktimeValue;
import de.juwimm.cms.content.frame.tree.ComponentNode;
import de.juwimm.cms.content.frame.tree.TalkTimeNode;
import de.juwimm.cms.content.panel.util.VisibilityCheckBox;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.DBCDao;
import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class PanDBCTalkTime extends AbstractTreePanel implements DBCDao {
	private static Logger log = Logger.getLogger(PanDBCTalkTime.class);
	
	private TalkTimeNode talktimeNode = null;
	private JLabel lblCaptionVisibility = new JLabel(UIConstants.DBC_VISIBILTY);
	private JLabel lblCaptionDB = new JLabel(rb.getString("PanDBC.component"));
	private JLabel lblTalktimeType = new JLabel();
	private VisibilityCheckBox vcbTalktimeType = new VisibilityCheckBox(getCheckActionListener());
	private VisibilityCheckBox vcbTalktimes = new VisibilityCheckBox(getCheckActionListener());
	private JTextField txtTalktimeType = new JTextField();
	private JLabel lblTalktime = new JLabel();
	private JTextArea txtTalktimes = new JTextArea();

	/**
	 * The default constructor initializes the instance.
	 */
	public PanDBCTalkTime() {
		try {
			this.setLayout(new GridBagLayout());
			lblTalktimeType.setText(Messages.getString("PanDBCTalkTime.typeOfTalkTime"));
			txtTalktimeType.getDocument().addDocumentListener(getChangedDocumentListener());
			lblTalktime.setText(Messages.getString("PanDBCTalkTime.talkTime"));
			txtTalktimes.getDocument().addDocumentListener(getChangedDocumentListener());
			txtTalktimes.setBorder(BorderFactory.createLoweredBevelBorder());
			txtTalktimes.setLineWrap(true);
			
			add(this.lblCaptionVisibility, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			add(this.lblCaptionDB, new GridBagConstraints(1, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			JPanel horizontalRuler = new JPanel();
			horizontalRuler.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
			add(horizontalRuler, new GridBagConstraints(0, 1, 5, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
			
			this.add(vcbTalktimeType, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			this.add(lblTalktimeType, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			this.add(txtTalktimeType, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 25), 0, 4));

			this.add(vcbTalktimes, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			this.add(lblTalktime, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
			this.add(txtTalktimes, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 25, 25), 0, 0));
		} catch (Exception exe) {
			log.error("Initialization problem", exe);
		}
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#updateCheckHash() */
	public void updateCheckHash() {
		Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
		ht.put("talkTimeType", vcbTalktimeType.isSelected() ? new Integer(1) : new Integer(0));
		ht.put("talkTimes", vcbTalktimes.isSelected() ? new Integer(1) : new Integer(0));
		setCheckHash(ht);
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#setCheckHash(java.util.Hashtable) */
	public void setCheckHash(Hashtable ht) {
		super.setCheckHash(ht);
		this.vcbTalktimeType.setSelected(getCheckValueForName("talkTimeType"));
		this.vcbTalktimes.setSelected(getCheckValueForName("talkTimes"));
		if (hasClicks()) this.setAllChecksEnabled(true);
	}

	/** @see de.juwimm.cms.content.panel.AbstractTreePanel#setFieldsEditable(boolean) */
	public void setFieldsEditable(boolean editable) {
		txtTalktimeType.setEditable(editable);
		txtTalktimes.setEditable(editable);
	}

	/** @see de.juwimm.cms.util.DBCDao#load(de.juwimm.cms.content.frame.tree.ComponentNode) */
	public void load(ComponentNode talktimeComponent) {
		if (!(talktimeComponent instanceof TalkTimeNode)) return;
		this.talktimeNode = (TalkTimeNode) talktimeComponent;
		TalktimeValue talkTimeValue = this.talktimeNode.getData();
		setCheckHash(this.talktimeNode.getClicks());

		txtTalktimeType.setText(talkTimeValue.getTalkTimeType());
		StringBuffer sb = new StringBuffer();
		try {
			Document doc = XercesHelper.string2Dom(talkTimeValue.getTalkTimes());
			for (Iterator it = XercesHelper.findNodes(doc, "//time"); it.hasNext();) {
				sb.append(XercesHelper.getNodeValue(((Node) it.next()))).append("\n");
			}
		} catch (Exception ex) {
		}
		txtTalktimes.setText(sb.toString());
	}

	/** @see de.juwimm.cms.util.DBCDao#save() */
	public void save() {
		this.talktimeNode.setClicks(getCheckHash());
		TalktimeValue talktimeValue = this.talktimeNode.getData();

		Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
		//<times><time>Montags von 10 - 12 Uhr und 14 - 16 Uhr</time><time>Dienstags und Donnerstags von 14 - 16 Uhr</time></times>
		Document doc = XercesHelper.getNewDocument();
		Element root = doc.createElement("times");
		doc.appendChild(root);
		StringTokenizer tok = new StringTokenizer(this.txtTalktimes.getText(), "\n");
		while (tok.hasMoreTokens()) {
			String next = tok.nextToken();
			Element elm = doc.createElement("time");
			Text textElem = doc.createTextNode(next);
			elm.appendChild(textElem);
			root.appendChild(elm);
		}
		String outp = XercesHelper.doc2String(doc);

		talktimeValue.setTalkTimes(outp);
		talktimeValue.setTalkTimeType(txtTalktimeType.getText());
		try {
			comm.updateTalktime(talktimeValue);
		} catch (Exception exe) {
			JOptionPane.showMessageDialog(this.getParent().getParent().getParent(), Messages.getString("PanDBCTalkTime.errorSaving") + exe.getMessage(), "CMS", JOptionPane.ERROR_MESSAGE);
			log.error(Messages.getString("PanDBCTalkTime.errorSaving"), exe);
		}
	}

	/** @see de.juwimm.cms.util.DBCDao#validateNode() */
	public String validateNode() {
		return null;
	}

}