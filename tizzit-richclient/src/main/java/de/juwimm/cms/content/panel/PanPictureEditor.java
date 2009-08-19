package de.juwimm.cms.content.panel;

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.media.jai.InterpolationBilinear;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import com.sun.media.jai.widget.DisplayJAI;


import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.vo.PictureSlimValue;
import de.juwimm.cms.vo.PictureValue;

/**
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a> 
 * @version $Id: DlgPictureEditor.java 6 2009-07-30 14:05:05Z skulawik@gmail.com $
 */
public class PanPictureEditor extends JLayeredPane {
	private static Logger log = Logger.getLogger(PanPicture.class);
	protected Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private PlanarImage picture;
	private PlanarImage picturePreview;
	private DisplayJAI rootPanel = null;
	private JButton btnCrop = new JButton();
	private JButton btnOk = new JButton();
	private JButton btnCancel = new JButton();
	private JSpinner spnResizePercentalSize = new JSpinner();
	private JFormattedTextField txtResizeWidth = new JFormattedTextField(NumberFormat.getIntegerInstance());
	private JFormattedTextField txtResizeHeight = new JFormattedTextField(NumberFormat.getIntegerInstance());
	private JLabel lblResizeHeader = new JLabel();
	private JLabel lblResizePercentalSize = new JLabel();
	private JLabel lblResizeWidth = new JLabel();
	private JLabel lblResizeHeight = new JLabel();
	private JLabel lblCurrentSize = new JLabel();
	private JLabel lblSelectedSize = new JLabel();
	private JLabel lblMousePosition = new JLabel();
	private JPanel panPostions = new JPanel();
	private JPanel panButtons = new JPanel();
	private JPanel panResize = new JPanel();
	private Point selectStart = null;
	private Point selectEnd = null;
	
	
	public PanPictureEditor(int pictureID){
		try{
			loadPicture(pictureID);
			init();
		}
		catch(Exception ex)
		{
			log.error("Error while starting PanPictureEditor.", ex);
		}
	}
	
	private void loadPicture(int pic) {
		try {
			PictureSlimValue picSlimVal = comm.getPicture(pic);
			byte[] picData = comm.getPictureData(pic);
			Image img = new ImageIcon(picData).getImage();
			ParameterBlock pb = new ParameterBlock();
		    pb.add(img);
			picture = (PlanarImage)JAI.create("AWTImage", pb);
		} catch (Exception ex) {
			log.error("catched exception while loading ImagePreview of imageId: "+picture, ex);
		}	
	}

	private  void init() throws Exception{
		initPositionsPanel();
		initResizePanel();
		
		btnCrop.setMaximumSize(new Dimension(95, 27));
		btnCrop.setMinimumSize(new Dimension(95, 27));
		btnCrop.setPreferredSize(new Dimension(95, 27));
		btnCrop.setToolTipText(rb.getString("panel.content.pictureEditor.btnCrop.tooltiptext"));
		btnCrop.setText(rb.getString("panel.content.pictureEditor.btnCrop"));
		btnCrop.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCropActionPerformed(e);
			}
		});
		btnCrop.setEnabled(false);
		
		btnOk.setMaximumSize(new Dimension(95, 27));
		btnOk.setMinimumSize(new Dimension(95, 27));
		btnOk.setPreferredSize(new Dimension(95, 27));
//		btnOk.setToolTipText(rb.getString("panel.content.pictureEditor.btnOk.tooltiptext"));
		btnOk.setText("Speichern");
		btnCrop.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOkActionPerformed(e);
			}
		});	
		btnCancel.setMaximumSize(new Dimension(95, 27));
		btnCancel.setMinimumSize(new Dimension(95, 27));
		btnCancel.setPreferredSize(new Dimension(95, 27));
//		btnCancel.setToolTipText(rb.getString("panel.content.pictureEditor.btnCancel.tooltiptext"));
		btnCancel.setText("Abbrechen");
		btnCrop.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed(e);
			}
		});		
		panButtons.setLayout(new GridLayout(4,1));
		panButtons.add(btnCrop);
		panButtons.add(panResize);
		panButtons.add(btnOk);
		panButtons.add(btnCancel);
		
		panButtons.setMaximumSize(new Dimension(100, 600));
		panButtons.setMinimumSize(new Dimension(100, 600));
		panButtons.setPreferredSize(new Dimension(100, 600));
				
		rootPanel = new DisplayJAI(picture);
		rootPanel.addMouseListener(new MouseListener(){
			public void mousePressed(MouseEvent e) {
				if(picture.getBounds().contains(e.getX(), e.getY())){
					selectStart = new Point(e.getX(), e.getY());
					selectEnd = new Point();
				}
			}
			public void mouseReleased(MouseEvent e) {
				calculateSelection();
				if(selectStart != null){
					btnCrop.setEnabled(true);
				}
			}
			public void mouseClicked(MouseEvent e) {	
				deleteSelection();
			}
			public void mouseEntered(MouseEvent e) {
				
			}
			public void mouseExited(MouseEvent e) {
				calculateMousePosition(0, 0);
			}			
		});
		rootPanel.addMouseMotionListener(new MouseMotionListener(){
			public void mouseDragged(MouseEvent e){
				if(picture.getBounds().contains(e.getX(), e.getY())){
					selectEnd.x = e.getX();
					selectEnd.y = e.getY();
					calculateSelection();
				}
			}
			public void mouseMoved(MouseEvent e) {
				calculateMousePosition(e.getX(), e.getY());
			}
		});
		JScrollPane spaRoot = new JScrollPane(rootPanel);
		spaRoot.setMaximumSize(new Dimension(700, 400));
		spaRoot.setMinimumSize(new Dimension(700, 400));
		spaRoot.setPreferredSize(new Dimension(700, 400));
		
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		
		this.add(panButtons, BorderLayout.EAST);
		this.add(spaRoot, BorderLayout.CENTER);
		this.add(panPostions, BorderLayout.SOUTH);		
	}
	
	private void initPositionsPanel(){
		lblCurrentSize.setText(" Bildgroesse " + picture.getWidth() + " x " + picture.getHeight());
		lblCurrentSize.setToolTipText(rb.getString("panel.content.pictureEditor.currentSize.toolTip"));
		
		calculateMousePosition(0, 0);
		lblMousePosition.setToolTipText(rb.getString("panel.content.pictureEditor.mousePosition.toolTip"));
		lblMousePosition.setMaximumSize(new Dimension(150, 25));
		lblMousePosition.setMinimumSize(new Dimension(150, 25));
		lblMousePosition.setPreferredSize(new Dimension(150, 25));
		
		calculateSelection();
		lblSelectedSize.setToolTipText(rb.getString("panel.content.pictureEditor.selectedSize.toolTip"));
		
		panPostions.setLayout(new BorderLayout());
		panPostions.setBorder(BorderFactory.createLoweredBevelBorder());
		panPostions.add(lblCurrentSize, BorderLayout.EAST);
		panPostions.add(lblMousePosition, BorderLayout.WEST);
		panPostions.add(lblSelectedSize, BorderLayout.CENTER);
		panPostions.setMaximumSize(new Dimension(800, 27));
		panPostions.setMinimumSize(new Dimension(800, 27));
		panPostions.setPreferredSize(new Dimension(800, 27));
	}
	
	private void initResizePanel(){
		lblResizeHeader.setText(rb.getString("panel.content.pictureEditor.resize.header"));		
	
		SpinnerModel spnModel = new SpinnerNumberModel(100, 1, 100, 1);
		spnResizePercentalSize.setModel(spnModel);
		spnResizePercentalSize.setMaximumSize(new Dimension(95, 27));
		spnResizePercentalSize.setMinimumSize(new Dimension(95, 27));
		spnResizePercentalSize.setPreferredSize(new Dimension(95, 27));
		spnResizePercentalSize.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				if(e.getSource().equals(spnResizePercentalSize))
				{
					txtResizeHeight.setText(""+(picture.getHeight()*(Integer)spnResizePercentalSize.getValue()/100));
					txtResizeWidth.setText(""+(picture.getWidth()*(Integer)spnResizePercentalSize.getValue()/100));
					
					Thread resizer = new Thread(){
						public void run(){
							scalePreviewImage(((Integer)spnResizePercentalSize.getValue()).floatValue()/100);
						}};
					resizer.start();
				}
			}	
		});		
		lblResizePercentalSize.setText(rb.getString("panel.content.pictureEditor.resize.percent"));
		lblResizePercentalSize.setToolTipText(rb.getString("panel.content.pictureEditor.resize.percent.toolTip"));
		lblResizePercentalSize.setMaximumSize(new Dimension(95, 27));
		lblResizePercentalSize.setMinimumSize(new Dimension(95, 27));
		lblResizePercentalSize.setPreferredSize(new Dimension(95, 27));
		
		lblResizeWidth.setText(rb.getString("panel.content.pictureEditor.resize.width"));
		lblResizeWidth.setToolTipText(rb.getString("panel.content.pictureEditor.resize.widht.toolTip"));
		lblResizeWidth.setMaximumSize(new Dimension(95, 27));
		lblResizeWidth.setMinimumSize(new Dimension(95, 27));
		lblResizeWidth.setPreferredSize(new Dimension(95, 27));
		txtResizeWidth.setValue(picture.getWidth());
		txtResizeWidth.addPropertyChangeListener("value", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				int newWidth = ((Long)evt.getNewValue()).intValue();
				if(newWidth > picture.getWidth()){
					newWidth = picture.getWidth();
				}
				int newHeight = newWidth*picture.getHeight()/picture.getWidth();
				txtResizeHeight.setValue(new Integer(newHeight).longValue());
				int newPercent = newHeight*100/picture.getHeight();
				spnResizePercentalSize.setValue(newPercent);
				
			}		
		});		
		lblResizeHeight.setText(rb.getString("panel.content.pictureEditor.resize.height"));
		lblResizeHeight.setToolTipText(rb.getString("panel.content.pictureEditor.resize.height.toolTip"));
		lblResizeHeight.setMaximumSize(new Dimension(95, 27));
		lblResizeHeight.setMinimumSize(new Dimension(95, 27));
		lblResizeHeight.setPreferredSize(new Dimension(95, 27));
		txtResizeHeight.setValue(picture.getHeight());
		txtResizeHeight.addPropertyChangeListener("value", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				int newHeight = ((Long)evt.getNewValue()).intValue();
				if(newHeight > picture.getWidth()){
					newHeight = picture.getWidth();
				}
				int newWidth = newHeight*picture.getHeight()/picture.getWidth();
				txtResizeHeight.setValue(new Integer(newWidth).longValue());
				int newPercent = newWidth*100/picture.getHeight();
				spnResizePercentalSize.setValue(newPercent);
			}		
		});		
		panResize.setLayout(new GridLayout(2,1));
		panResize.add(lblResizeHeader);
		JPanel innerResize = new JPanel();
		innerResize.setLayout(new GridLayout(3,2));
		innerResize.add(lblResizePercentalSize);
		innerResize.add(spnResizePercentalSize);
		innerResize.add(lblResizeWidth);
		innerResize.add(txtResizeWidth);
		innerResize.add(lblResizeHeight);
		innerResize.add(txtResizeHeight);
		panResize.add(innerResize);
		panResize.setMaximumSize(new Dimension(150, 27));
		panResize.setMinimumSize(new Dimension(150, 27));
		panResize.setPreferredSize(new Dimension(150, 27));
	}
	
	private void scalePreviewImage(float scale){
		if(scale < 0){
			log.error("ScaleFactor expectet positiv - was :"+scale);
			return;
		}
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(picture);
		pb.add(scale);
		pb.add(scale);
		pb.add(0.0f);
		pb.add(0.0f);
		pb.add(new InterpolationBilinear());
		PlanarImage scaledImage = JAI.create("scale", pb);
		rootPanel.set(scaledImage);
		deleteSelection();
	}
	
	private void deleteSelection()
	{
		lblSelectedSize.setText(" Markierung ");
		selectStart = null;
		selectEnd = null;
		btnCrop.setEnabled(false);
	}
	
	private void resetSizes()
	{
		lblCurrentSize.setText(" Bildgroesse " + picture.getWidth() + " x " + picture.getHeight());
		spnResizePercentalSize.setValue(100);
		txtResizeHeight.setValue(new Integer(picture.getHeight()).longValue());
		txtResizeWidth.setValue(new Integer(picture.getWidth()).longValue());
	}
	
	private void cropPreviewImage(){
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(picture);
		pb.add((float)selectStart.x);
		pb.add((float)selectStart.y);
		pb.add((float)(selectEnd.x-selectStart.x));
		pb.add((float)(selectEnd.y-selectStart.y));
		picture = JAI.create("crop",pb, null);		
		// croped Image would stay at the same place like in the original
		// image - upper left corner has to be reseted to zero
		pb = new ParameterBlock();  
		pb.addSource(picture);  
		pb.add(-((float)selectStart.x));  
		pb.add(-((float)selectStart.y));  
		picture = JAI.create("translate",pb,null);		
		rootPanel.set(picture);
		deleteSelection();
		resetSizes();
	}
	
	private void calculateMousePosition(int x, int y)
	{
		String text = new String(" MausPosition ");
		if(!picture.getBounds().contains(x, y)){
			x = 0;
			y = 0;
		}
		text = text + x + " : " + y;
		lblMousePosition.setText(text);
	}
	
	private void calculateSelection()
	{
		String text = new String(" Markierung ");
		if(selectStart != null){
			int minX = (selectStart.x < selectEnd.x)?selectStart.x:selectEnd.x;
			int maxX = (selectStart.x == minX)?selectEnd.x:selectStart.x;
			int minY = (selectStart.y < selectEnd.y)?selectStart.y:selectEnd.y;
			int maxY = (selectStart.y != minY)?selectStart.y:selectEnd.y;
			text = text + (maxX - minX) + " x " + (maxY-minY);
		}
		lblSelectedSize.setText(text);
	}
	
	private void btnCropActionPerformed(ActionEvent e){
		cropPreviewImage();
	}

	private void btnOkActionPerformed(ActionEvent e){
		
	}

	private void btnCancelActionPerformed(ActionEvent e){
		
	}
}
