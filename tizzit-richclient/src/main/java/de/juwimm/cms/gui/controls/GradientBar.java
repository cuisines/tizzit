package de.juwimm.cms.gui.controls;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * 
 * @author diftimi
 *
 */
public class GradientBar extends JPanel {
	private static final long serialVersionUID = 1L;
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
			gradientPaint = new GradientPaint(0, 0, new Color(94, 129, 149), 0, panelHeight, new Color(130, 161, 181), true);
		} else {
			gradientPaint = new GradientPaint(0, 0, new Color(130, 161, 181), 0, panelHeight, new Color(94, 129, 149), true);

		}

		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.setPaint(gradientPaint);
		graphics2D.fillRect(0, 0, panelWidth, panelHeight);

	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}
