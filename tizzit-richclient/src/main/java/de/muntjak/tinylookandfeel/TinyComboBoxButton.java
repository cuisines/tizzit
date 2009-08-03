/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*	Tiny Look and Feel                                                         *
*                                                                              *
*  (C) Copyright 2003 - 2007 Hans Bickel                                       *
*                                                                              *
*   For licensing information and credits, please refer to the                 *
*   comment in file de.muntjak.tinylookandfeel.TinyLookAndFeel                 *
*                                                                              *
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package de.muntjak.tinylookandfeel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.*;

import de.muntjak.tinylookandfeel.controlpanel.*;

/**
 * TinyComboBoxButton
 * 
 * @version 1.3
 * @author Hans Bickel
 */
public class TinyComboBoxButton extends JButton {

    protected JComboBox comboBox;
    protected JList listBox;
    protected CellRendererPane rendererPane;
    protected Icon comboIcon;
    protected boolean iconOnly = false;
    private static BufferedImage focusImg;

    public final JComboBox getComboBox() {
        return comboBox;
    }
    
    public final void setComboBox(JComboBox cb) {
        comboBox = cb;
    }

    public final Icon getComboIcon() {
        return comboIcon;
    }
    
    public final void setComboIcon(Icon i) {
        comboIcon = i;
    }

    public final boolean isIconOnly() {
        return iconOnly;
    }
    
    public final void setIconOnly(boolean isIconOnly) {
        iconOnly = isIconOnly;
    }

    TinyComboBoxButton() {
        super("");
        
        DefaultButtonModel model = new DefaultButtonModel() {
            public void setArmed(boolean armed) {
                super.setArmed(isPressed() ? true : armed);
            }
        };

        setModel(model);

        // Set the background and foreground to the combobox colors.
        setBackground(UIManager.getColor("ComboBox.background"));
        setForeground(UIManager.getColor("ComboBox.foreground"));
        
        if(focusImg == null) {
        	ImageIcon icon = TinyLookAndFeel.loadIcon("ComboBoxFocus.png", this);
        	
        	if(icon != null) {
        		focusImg = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		        Graphics g = focusImg.getGraphics();
		        icon.paintIcon(this, g, 0, 0);
        	}
        }
    }

    public TinyComboBoxButton(JComboBox cb, Icon i,
    	boolean onlyIcon, CellRendererPane pane, JList list)
    {
    	this();
        comboBox = cb;
        comboIcon = i;
        rendererPane = pane;
        listBox = list;
        setEnabled(comboBox.isEnabled());
    }

    /**
     * Mostly taken from the swing sources
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    public void paintComponent(Graphics g) {
    	// Note: border was already painted in TinyButtonBorder
        boolean leftToRight = getComponentOrientation().isLeftToRight();
 		
 		if(comboBox.isEnabled()) {
 			if(comboBox.isEditable()) {
 				g.setColor(Theme.textBgColor[Theme.style].getColor());
 			}
 			else {
 				g.setColor(comboBox.getBackground());
 			}
 		}
 		else {
 			g.setColor(Theme.textDisabledBgColor[Theme.style].getColor());
 		}

		g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);

		// paint border background - next parent is combo box
		Color bg = getParent().getParent().getBackground();
		g.setColor(bg);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		Color col = null;	
			
		if(!isEnabled()) {
			col = Theme.comboButtDisabledColor[Theme.style].getColor();
		}
		else if(model.isPressed()) {
			col = Theme.comboButtPressedColor[Theme.style].getColor();
		}
		else if(model.isRollover()) {
			col = Theme.comboButtRolloverColor[Theme.style].getColor();
		}
		else {
			col = Theme.comboButtColor[Theme.style].getColor();
		}
		
		g.setColor(col);
		
		Rectangle buttonRect = new Rectangle(
			getWidth() - Theme.comboButtonWidth[Theme.derivedStyle[Theme.style]],
			1, Theme.comboButtonWidth[Theme.derivedStyle[Theme.style]], getHeight() - 2);
		
		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinyButton(g, buttonRect);
				break;
			case Theme.W99_STYLE:
				drawWinButton(g, buttonRect);
				break;
			case Theme.YQ_STYLE:
				drawXpButton(g, buttonRect, col);
				break;
		}
		
		// draw arrow
		if (isEnabled()) {
			g.setColor(Theme.comboArrowColor[Theme.style].getColor());
		}
		else {
			g.setColor(Theme.comboArrowDisabledColor[Theme.style].getColor());
		}

		switch(Theme.derivedStyle[Theme.style]) {
			case Theme.TINY_STYLE:
				drawTinyArrow(g, buttonRect);
				break;
			case Theme.W99_STYLE:
				drawWinArrow(g, buttonRect);
				break;
			case Theme.YQ_STYLE:
				drawXpArrow(g, buttonRect);
				break;
		}

        Insets insets = new Insets(
        	Theme.comboInsets[Theme.style].top,
        	Theme.comboInsets[Theme.style].left,
        	Theme.comboInsets[Theme.style].bottom, 0);

        int width = getWidth() - (insets.left + insets.right);
        int widthFocus = width;
        int height = getHeight() - (insets.top + insets.bottom);

        if (height <= 0 || width <= 0) {
            return;
        }

        int left = insets.left;
        int top = insets.top;
        int right = left + (width - 1);
        int bottom = top + (height - 1);

        int iconWidth = Theme.comboButtonWidth[Theme.derivedStyle[Theme.style]];
        int iconLeft = (leftToRight) ? right : left;

        // Let the renderer paint
        Component c = null;
        boolean mustResetOpaque = false;
        boolean savedOpaque = false;
        boolean paintFocus = false;
        
        if(!iconOnly && comboBox != null) {
            ListCellRenderer renderer = comboBox.getRenderer();
            boolean renderPressed = getModel().isPressed();
            c = renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, renderPressed, false);
            c.setFont(rendererPane.getFont());

            if (model.isArmed() && model.isPressed()) {
                if (isOpaque()) {
                    c.setBackground(UIManager.getColor("Button.select"));
                }

                c.setForeground(comboBox.getForeground());
            } else if (!comboBox.isEnabled()) {
                if (isOpaque()) {
                    c.setBackground(Theme.textDisabledBgColor[Theme.style].getColor());
                }
                else {
                	comboBox.setBackground(Theme.textDisabledBgColor[Theme.style].getColor());
                }
                
                c.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
            } else if(comboBox.hasFocus() && !comboBox.isPopupVisible()) {
            	if(comboBox.isEditable()) {
            		c.setForeground(Theme.mainColor[Theme.style].getColor());
            	}
            	else {
            		c.setForeground(UIManager.getColor("ComboBox.selectionForeground"));
            	}
            	
                c.setBackground(UIManager.getColor("ComboBox.focusBackground"));
                
                if (c instanceof JComponent) {
                    mustResetOpaque = true;
                    JComponent jc = (JComponent) c;
                    savedOpaque = jc.isOpaque();
                    jc.setOpaque(true);
                    paintFocus = true;
                }
            } else {
                c.setForeground(comboBox.getForeground());
                c.setBackground(comboBox.getBackground());
            }

            int cWidth = width - (insets.right + iconWidth);

            // Fix for 4238829: should lay out the JPanel.
            boolean shouldValidate = false;
            if (c instanceof JPanel) {
                shouldValidate = true;
            }

            if (leftToRight) {
                rendererPane.paintComponent(g, c, this, left, top, cWidth, height, shouldValidate);
            } else {
                rendererPane.paintComponent(g, c, this, left + iconWidth, top, cWidth, height, shouldValidate);
            }
            
            if(paintFocus &&
            	Theme.derivedStyle[Theme.style] == Theme.YQ_STYLE &&
            	Theme.comboFocus[Theme.style])
            {
                g.setColor(Color.black);
                Graphics2D g2d = (Graphics2D) g;
                Rectangle r = new Rectangle(left, top, 2, 2);
                TexturePaint tp = new TexturePaint(focusImg, r);        

                g2d.setPaint(tp);
                g2d.draw(new Rectangle(left,top,cWidth, height));
            }
        }
        
        if (mustResetOpaque) {
            JComponent jc = (JComponent) c;
            jc.setOpaque(savedOpaque);
        }
    }

    private void drawTinyButton(Graphics g, Rectangle r) {
	}
	
	private void drawWinButton(Graphics g, Rectangle r) {
		int x2 = r.x + r.width - 1;
		int y2 = r.y + r.height - 1;

		g.fillRect(r.x, r.y,  r.width - 2, r.height - 2);
		
		if(model.isPressed()) {
			g.setColor(Theme.comboButtDarkColor[Theme.style].getColor());
			g.drawRect(r.x, r.y + 1,  r.width - 2, r.height - 3);
		}
		else {
			if(!isEnabled()) {
				g.setColor(Theme.comboButtLightDisabledColor[Theme.style].getColor());
			}
			else {
				g.setColor(Theme.comboButtLightColor[Theme.style].getColor());
			}
			g.drawLine(r.x + 1, r.y + 2,  x2 - 3, r.y + 2);
			g.drawLine(r.x + 1, r.y + 2,  r.x + 1, y2 - 3);
		
			if(!isEnabled()) {
				g.setColor(Theme.comboButtDarkDisabledColor[Theme.style].getColor());
			}
			else {
				g.setColor(Theme.comboButtDarkColor[Theme.style].getColor());
			}
			g.drawLine(x2 - 2, r.y + 2, x2 - 2, y2 - 3);
			g.drawLine(r.x + 1, y2 - 2, x2 - 2, y2 - 2);
		
			if(!isEnabled()) {
				g.setColor(Theme.comboButtBorderDisabledColor[Theme.style].getColor());
			}
			else {
				g.setColor(Theme.comboButtBorderColor[Theme.style].getColor());
			}
			g.drawLine(x2 - 1, r.y + 1, x2 - 1, y2 - 2);
			g.drawLine(r.x, y2 - 1, x2 - 1, y2 - 1);
		}
	}

	private void drawXpButton(Graphics g, Rectangle r, Color c) {
		int x2 = r.x + r.width;
		int y2 = r.y + r.height;
		
		int spread1 = Theme.comboSpreadLight[Theme.style];
		int spread2 = Theme.comboSpreadDark[Theme.style];
		if(!isEnabled()) {
			spread1 = Theme.comboSpreadLightDisabled[Theme.style];
			spread2 = Theme.comboSpreadDarkDisabled[Theme.style];
		}
		
		int h = r.height - 2;
		float spreadStep1 = 10.0f * spread1 / (h - 3);
		float spreadStep2 = 10.0f * spread2 / (h - 3);
		int halfY = h / 2;
		int yd;

		for(int y = 1; y < h - 1; y++) {
			if(y < halfY) {
				yd = halfY - y;
				g.setColor(ColorRoutines.lighten(c, (int)(yd * spreadStep1)));
			}
			else if(y == halfY) {
				g.setColor(c);
			}
			else {
				yd = y - halfY;
				g.setColor(ColorRoutines.darken(c, (int)(yd * spreadStep2)));
			}
			
			g.drawLine(r.x + 1, r.y + y + 1, r.x + r.width - 3, r.y + y + 1);
		}
			
		// draw the button border
		Color col = null;	
		if(!isEnabled()) {
			col = Theme.comboButtBorderDisabledColor[Theme.style].getColor();
		}
		else {
			col = Theme.comboButtBorderColor[Theme.style].getColor();
		}
		g.setColor(col);
		g.drawLine(r.x + 2, r.y + 1, x2 - 4, r.y + 1);
		g.drawLine(r.x + 1, r.y + 2, r.x + 1, y2 - 3);
		g.drawLine(x2 - 3, r.y + 2, x2 - 3, y2 - 3);
		g.drawLine(r.x + 2, y2 - 2, x2 - 4, y2 - 2);
				
		// ecken
		col = new Color(col.getRed(), col.getGreen(), col.getBlue(), 128);
		g.setColor(col);
		g.drawLine(r.x + 1, r.y + 1, r.x + 1, r.y + 1);
		g.drawLine(x2 - 3, r.y + 1, x2 - 3, r.y + 1);
		g.drawLine(r.x + 1, y2 - 2, r.x + 1, y2 - 2);
		g.drawLine(x2 - 3, y2 - 2, x2 - 3, y2 - 2);
	}
	
	private void drawTinyArrow(Graphics g, Rectangle r) {
	}
	
	private void drawWinArrow(Graphics g, Rectangle r) {
		int x = r.x + (r.width - 6) / 2 - 2;
		int y = r.y + (r.height - 4) / 2;
		
		if(model.isPressed()) {
			x ++;
			y ++;
		}
		
		g.drawLine(x, y, x + 6, y);
		g.drawLine(x + 1, y + 1, x + 5, y + 1);
		g.drawLine(x + 2, y + 2, x + 4, y + 2);
		g.drawLine(x + 3, y + 3, x + 3, y + 3);
		
		if(!isEnabled()) {
			g.setColor(ColorRoutines.lighten(Theme.comboArrowDisabledColor[Theme.style].getColor(), 60));
			g.drawLine(x + 4, y + 4, x + 4, y + 4);
			g.drawLine(x + 4, y + 3, x + 5, y + 3);
			g.drawLine(x + 5, y + 2, x + 6, y + 2);
			g.drawLine(x + 6, y + 1, x + 7, y + 1);
		}
	}
	
	private void drawXpArrow(Graphics g, Rectangle r) {
		int x = r.x + (r.width - 8) / 2 - 1;
		int y = r.y + (r.height - 6) / 2 + 1;
		
		g.drawLine(x + 1, y, x + 1, y);
		g.drawLine(x + 7, y, x + 7, y);
		g.drawLine(x, y + 1, x + 2, y + 1);
		g.drawLine(x + 6, y + 1, x + 8, y + 1);
		g.drawLine(x + 1, y + 2, x + 3, y + 2);
		g.drawLine(x + 5, y + 2, x + 7, y + 2);
		g.drawLine(x + 2, y + 3, x + 6, y + 3);
		g.drawLine(x + 3, y + 4, x + 5, y + 4);
		g.drawLine(x + 4, y + 5, x + 4, y + 5);
	}
}
