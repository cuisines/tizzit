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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public class ContentBorderIterationPanel extends JPanel implements ContentBorder {
	private static Logger log = Logger.getLogger(ContentBorderIterationPanel.class);
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel panSouth = new JPanel();
	private JPanel jPanel3 = new JPanel();
	private JPanel panEast = new JPanel();
	private JPanel panWest = new JPanel();
	private BorderLayout borderLayout2 = new BorderLayout();
	private JToolBar toolBar = new JToolBar();
	private Component component1;
	private JLabel lblDescription = new JLabel();
	private JButton btnNew = new JButton();
	private JButton btnDeleteMe = new JButton();
	private JButton btnUp = new JButton();
	private JButton btnDown = new JButton();
	private JLabel jLabel1 = new JLabel();
	private EventListenerList listenerList = new EventListenerList();
	private boolean expanded = false;
	private boolean showCenter = true;
	private JPanel panCenter;

	public ContentBorderIterationPanel() {
		try {
			jbInit();
			getBtnDeleteMe().setIcon(UIConstants.MODULE_ITERATION_CONTENT_DELETE);
			getBtnNew().setIcon(UIConstants.MODULE_ITERATION_CONTENT_ADD);
			getBtnUp().setIcon(UIConstants.MODULE_ITERATION_CONTENT_UP);
			getBtnDown().setIcon(UIConstants.MODULE_ITERATION_CONTENT_DOWN);
		} catch (Exception exe) {
			log.error("initialization problem", exe);
		}
	}

	private void jbInit() throws Exception {
		component1 = Box.createVerticalStrut(16);
		this.setBorder(BorderFactory.createEtchedBorder());
		this.setLayout(borderLayout1);
		jPanel3.setBorder(BorderFactory.createRaisedBevelBorder());
		jPanel3.setPreferredSize(new Dimension(151, 21));
		jPanel3.setLayout(borderLayout2);
		getToolBar().setFloatable(false);
		lblDescription.setToolTipText("");
		lblDescription.setText("Aggregation");
		lblDescription.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				toolbarClicked(me);
			}
		});

		getBtnUp().setMaximumSize(new Dimension(20, 20));
		getBtnUp().setMinimumSize(new Dimension(20, 20));
		getBtnUp().setPreferredSize(new Dimension(16, 16));
		getBtnUp().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnUpActionPerformed(e);
			}
		});

		getBtnDown().setMaximumSize(new Dimension(20, 20));
		getBtnDown().setMinimumSize(new Dimension(20, 20));
		getBtnDown().setPreferredSize(new Dimension(16, 16));
		getBtnDown().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDownActionPerformed(e);
			}
		});

		getBtnNew().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewActionPerformed(e);
			}
		});

		getBtnNew().setMaximumSize(new Dimension(20, 20));
		getBtnNew().setMinimumSize(new Dimension(20, 20));
		getBtnNew().setPreferredSize(new Dimension(16, 16));

		getBtnDeleteMe().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteMeActionPerformed(e);
			}
		});

		getBtnDeleteMe().setMaximumSize(new Dimension(20, 20));
		getBtnDeleteMe().setMinimumSize(new Dimension(20, 20));
		getBtnDeleteMe().setPreferredSize(new Dimension(16, 16));

		jLabel1.setText("  ");
		panSouth.setPreferredSize(new Dimension(2, 2));
		panEast.setPreferredSize(new Dimension(2, 2));
		panWest.setPreferredSize(new Dimension(2, 2));
		this.add(jPanel3, BorderLayout.NORTH);
		jPanel3.add(getToolBar(), BorderLayout.CENTER);
		getToolBar().add(jLabel1, null);
		getToolBar().add(lblDescription, null);
		getToolBar().add(component1, null);
		getToolBar().add(getBtnUp(), null);
		getToolBar().add(getBtnDown(), null);
		getToolBar().add(new JToolBar.Separator(new Dimension(3, 16)), null);
		getToolBar().add(getBtnNew(), null);
		getToolBar().add(getBtnDeleteMe(), null);
		getToolBar().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				toolbarClicked(me);
			}
		});
		this.add(panEast, BorderLayout.EAST);
		this.add(panSouth, BorderLayout.SOUTH);
		this.add(panWest, BorderLayout.WEST);
	}

	public void setContentModulePanel(JPanel panel) {
		this.add(panel, BorderLayout.CENTER);
		panCenter = panel;
		this.panCenter.setVisible(showCenter);
	}

	/**
	 * Set the parameter to false and the Panel will not be expanded. Use this for the
	 * module 'newslist'.
	 * @param show
	 */
	public void setShowCenter(boolean show) {
		this.showCenter = show;
		this.panCenter.setVisible(showCenter);
	}

	public void setLabel(String strUserDescription) {
		lblDescription.setText(strUserDescription);
		this.revalidate();
		this.repaint();
	}

	public String getLabel() {
		return lblDescription.getText();
	}

	private void btnNewActionPerformed(ActionEvent e) {
		ActionEvent ae = new ActionEvent(this, ContentBorderIterationPanel.ACTION_ITERATION_GROUP_INSERT,
				this.lblDescription.getText());
		fireActionEvent(ae);
	}

	private void btnDeleteMeActionPerformed(ActionEvent e) {
		ActionEvent ae = new ActionEvent(this, ContentBorderIterationPanel.ACTION_ITERATION_GROUP_DELETE,
				this.lblDescription.getText());
		fireActionEvent(ae);
	}

	private void btnUpActionPerformed(ActionEvent e) {
		ActionEvent ae = new ActionEvent(this, ContentBorderIterationPanel.ACTION_ITERATION_GROUP_UP,
				this.lblDescription.getText());
		fireActionEvent(ae);
	}

	private void btnDownActionPerformed(ActionEvent e) {
		ActionEvent ae = new ActionEvent(this, ContentBorderIterationPanel.ACTION_ITERATION_GROUP_DOWN,
				this.lblDescription.getText());
		fireActionEvent(ae);
	}

	private void toolbarClicked(MouseEvent me) {
		expanded = !expanded;
		this.panEast.setVisible(expanded);
		this.panSouth.setVisible(expanded);
		this.panWest.setVisible(expanded);
		this.panCenter.setVisible(expanded);
	}

	public void addActionListener(ActionListener al) {
		this.listenerList.add(ActionListener.class, al);
	}

	public void removeActionListener(ActionListener al) {
		this.listenerList.remove(ActionListener.class, al);
	}

	public void fireActionEvent(ActionEvent ae) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			((ActionListener) listeners[i + 1]).actionPerformed(ae);
		}
	}

	/**
	 * @param btnNew The btnNew to set.
	 */
	public void setBtnNew(JButton btnNew) {
		this.btnNew = btnNew;
	}

	/**
	 * @return Returns the btnNew.
	 */
	public JButton getBtnNew() {
		return btnNew;
	}

	/**
	 * @param btnDeleteMe The btnDeleteMe to set.
	 */
	public void setBtnDeleteMe(JButton btnDeleteMe) {
		this.btnDeleteMe = btnDeleteMe;
	}

	/**
	 * @return Returns the btnDeleteMe.
	 */
	public JButton getBtnDeleteMe() {
		return btnDeleteMe;
	}

	/**
	 * @param btnUp The btnUp to set.
	 */
	public void setBtnUp(JButton btnUp) {
		this.btnUp = btnUp;
	}

	/**
	 * @return Returns the btnUp.
	 */
	public JButton getBtnUp() {
		return btnUp;
	}

	/**
	 * @param btnDown The btnDown to set.
	 */
	public void setBtnDown(JButton btnDown) {
		this.btnDown = btnDown;
	}

	/**
	 * @return Returns the btnDown.
	 */
	public JButton getBtnDown() {
		return btnDown;
	}

	/**
	 * @param jToolBar1 The jToolBar1 to set.
	 */
	protected void setToolBar(JToolBar jToolBar1) {
		this.toolBar = jToolBar1;
	}

	/**
	 * @return Returns the jToolBar1.
	 */
	protected JToolBar getToolBar() {
		return toolBar;
	}
}