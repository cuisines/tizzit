package de.juwimm.cms.gui.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.juwimm.cms.util.UIConstants;

public class ColapsePanel extends JPanel {
	private static final long serialVersionUID = 6001767654899738989L;
	private JLabel arrow;
	private GradientBar headerPanel;
	private JPanel contentPanel;
	private ImageIcon arrowDown;
	private ImageIcon arrowRight;
	private boolean isDocked = true;

	public ColapsePanel() {
		super();
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		headerPanel = new GradientBar();
		contentPanel = new JPanel();
		arrow = new JLabel();
		arrowDown = UIConstants.ICON_ARROW_DOWN;
		arrowRight = UIConstants.ICON_ARROW_RIGHT;
		arrow.setIcon(arrowDown);
		contentPanel.setBackground(new Color(247, 247, 247));
		contentPanel.setBorder(BorderFactory.createLineBorder(new Color(113, 113, 113)));
		arrow.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				isDocked = !isDocked;
				updateDocking();
			}
		});
		arrow.setForeground(Color.white);
		arrow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		headerPanel.setLayout(new BorderLayout());
		headerPanel.add(arrow, BorderLayout.WEST);
		headerPanel.setBackground(Color.black);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setLayout(new BorderLayout());

		super.add(headerPanel, BorderLayout.NORTH);
		super.add(contentPanel, BorderLayout.SOUTH);
		updateDocking();
	}

	@Override
	public Component add(Component comp) {
		return contentPanel.add(comp);
	}

	@Override
	public void add(Component comp, Object constraints) {
		contentPanel.add(comp, constraints);
	}

	@Override
	public Component add(String name, Component comp) {
		return contentPanel.add(name, comp);
	}

	@Override
	public Component add(Component comp, int index) {
		return contentPanel.add(comp, index);
	}

	@Override
	public void add(Component comp, Object constraints, int index) {
		contentPanel.add(comp, constraints, index);
	}

	/* (non-Javadoc)
	 * @see java.awt.Container#setLayout(java.awt.LayoutManager)
	 */
	@Override
	public void setLayout(LayoutManager mgr) {
		if (contentPanel != null) {
			contentPanel.setLayout(mgr);
		} else {
			super.setLayout(mgr);
		}

	}

	private void updateDocking() {
		contentPanel.setVisible(!isDocked);
		headerPanel.setDirection(isDocked ? GradientBar.DOWN_UP : GradientBar.UP_DOWN);
		arrow.setIcon(!isDocked ? arrowDown : arrowRight);
	}

	public void setText(String value) {
		arrow.setText(value);
	}

	public void setDocked(boolean state) {
		isDocked = state;
		updateDocking();
	}

	private class GradientBar extends JPanel {
		private int direction = UP_DOWN;
		public static final int UP_DOWN = 1;
		public static final int DOWN_UP = 2;

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int panelHeight = getHeight();
			int panelWidth = getWidth();
			GradientPaint gradientPaint;
			if (direction == UP_DOWN) {
				gradientPaint = new GradientPaint(0, 0, new Color(55, 55, 55), 0, panelHeight, new Color(113, 113, 113), true);
			} else {
				gradientPaint = new GradientPaint(0, 0, new Color(113, 113, 113), 0, panelHeight, new Color(55, 55, 55), true);
			}

			Graphics2D graphics2D = (Graphics2D) g;
			graphics2D.setPaint(gradientPaint);
			graphics2D.fillRect(0, 0, panelWidth, panelHeight);

		}

		public void setDirection(int direction) {
			this.direction = direction;
		}
	}

}
