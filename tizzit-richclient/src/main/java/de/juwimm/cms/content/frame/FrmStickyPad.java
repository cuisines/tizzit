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
package de.juwimm.cms.content.frame;

import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.juwimm.cms.util.UIConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.ayyappan@juwimm.com">Sabarinath Ayyappan</a>
 * @version $Id$
 */
public final class FrmStickyPad {
	private JDialog stickyPadFrame = null;
	private JTextArea stickyPadTextArea = null;
	private JPanel textFieldPanel = null;
	private ImagePanel imgAdditionPanel = null;
	private JScrollPane scrollingArea = null;
	private String storedMessage = "";
	private static FrmStickyPad instance = null;

	private FrmStickyPad() {
		doStickyPadLayout();
	}

	/**
	 * Funtion which does the layout of the StickyPadFrame.
	 * It is set to resizeable false, so that it maintains a proper size.
	 */
	private void doStickyPadLayout() {
		stickyPadFrame = new JDialog(UIConstants.getMainFrame(), rb.getString("stickypad.title"));
		imgAdditionPanel = new ImagePanel();
		imgAdditionPanel.setBackground(new Color(241, 255, 202));

		textFieldPanel = new JPanel();
		textFieldPanel.setLayout(new FlowLayout());
		textFieldPanel.setBackground(new Color(241, 255, 202));

		stickyPadTextArea = new JTextArea(7, 20);
		stickyPadTextArea.setBackground(new Color(241, 255, 202));
		stickyPadTextArea.setFont(new Font("Dialog", Font.PLAIN, 12));
		stickyPadTextArea.setEditable(true);
		stickyPadTextArea.setLineWrap(true);
		stickyPadTextArea.setWrapStyleWord(true);
		stickyPadTextArea.setBorder(BorderFactory.createEmptyBorder());

		scrollingArea = new JScrollPane(stickyPadTextArea);
		scrollingArea.setBorder(BorderFactory.createEmptyBorder());
		scrollingArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollingArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		textFieldPanel.add(scrollingArea);

		//stickyPadFrame.getContentPane().add(imgAdditionPanel, BorderLayout.CENTER);
		//stickyPadFrame.getContentPane().add(textFieldPanel, BorderLayout.SOUTH);
		stickyPadFrame.getContentPane().add(textFieldPanel, BorderLayout.CENTER);

		//Moving of stickypad is not so good code.
		//so commented out the code.
		//stickyPadFrame.setUndecorated(true);

		stickyPadFrame.setResizable(false);

		// Setting the StickyPadFrame in the middle of the screen.
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = stickyPadFrame.getPreferredSize();
		int frameWidth = frameSize.width;
		int frameHeight = frameSize.height;
		stickyPadFrame.setBounds((screenSize.width / 2) - (frameWidth / 2), (screenSize.height / 2) - (frameHeight / 2), 240, 175);
	}

	/**
	 * Funtion to show the StickyPadFrame.
	 */
	public void showStickyPad() {
		stickyPadTextArea.setCaretPosition(0);
		stickyPadFrame.setVisible(true);
		stickyPadFrame.validate();
	}

	/**
	 * Funtion to close the StickyPadFrame.
	 */
	public void closeStickyPad() {
		stickyPadFrame.setVisible(false);
	}

	/**
	 * Funtion to get the instance of FrmStickyPad.
	 */
	public static FrmStickyPad getInstance() {
		if (instance == null) {
			instance = new FrmStickyPad();
		}
		return instance;
	}

	/**
	 * Funtion to check if FrmStickyPad is empty or not
	 * If not empty while check out, it should be opened
	 * automatically.
	 */
	public boolean checkStickyPadEmpty() {
		boolean stickyPadEmpty = true;
		if (stickyPadTextArea.getText().trim().length() != 0) {
			stickyPadEmpty = false;
		} else {
			stickyPadEmpty = true;
		}
		return stickyPadEmpty;
	}

	/**
	 * Funtion to show the StickyPadFrame.
	 */
	public JDialog getFrmStickyPad() {
		return stickyPadFrame;
	}

	/**
	 * Funtion to set the message in the stickyPadTextArea.
	 */
	public void setText(String message) {
		storedMessage = message;
		stickyPadTextArea.setText(storedMessage);
	}

	/**
	 * Funtion to get the message from the stickyPadTextArea.
	 */
	public String getText() {
		if (stickyPadTextArea.getText() != "") {
			storedMessage = stickyPadTextArea.getText();
		}
		return storedMessage;
	}

	/**
	 * Inner Class which does the addition of the tile bar and close button image.
	 * And also handles the close operation of the StickyPadFrame
	 */
	class ImagePanel extends JPanel {
		private String imgName = null;
		private Point start = null;
		private ImagePanel instance = null;
		private Point origin = null;

		public ImagePanel() {
			instance = this;
			setBackground(Color.yellow);
			origin = new Point();
		}

		public void paintComponent(Graphics g) {
			g.drawImage(UIConstants.MODULE_POSTIT_TITLE.getImage(), 0, 1, this);
			g.drawImage(UIConstants.MODULE_POSTIT_CLOSE.getImage(), 220, 1, this);

			addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent e) {
					//TODO : Have to find code so that mouse point is pointed at the middle of the title
					Point p = stickyPadFrame.getLocation();
					stickyPadFrame.setBounds((p.x + e.getX()) - origin.x, (p.y + e.getY()) - origin.y, 240, 175);
				}
			});

			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getPoint().getX() >= 220) {
						closeStickyPad();
					}
				}

				public void mousePressed(MouseEvent e) {
					// Remember offset into window for dragging
					origin.x = e.getX();
					origin.y = e.getY();
				}
			});
		}
	}

	public void setEnabled(boolean enabled) {
		stickyPadTextArea.setEditable(enabled);
	}
}