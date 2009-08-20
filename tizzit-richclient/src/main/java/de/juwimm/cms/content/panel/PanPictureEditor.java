package de.juwimm.cms.content.panel;

import static de.juwimm.cms.client.beans.Application.getBean;
import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.renderable.ParameterBlock;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;

import javax.media.jai.InterpolationBilinear;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import com.sun.media.jai.widget.DisplayJAI;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.content.frame.DlgSavePicture;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.PictureSlimValue;

/**
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a> 
 * @version $Id: DlgPictureEditor.java 6 2009-07-30 14:05:05Z skulawik@gmail.com $
 */
public class PanPictureEditor extends JLayeredPane {
	static
	{
		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
	} 
	
	private static final long serialVersionUID = -4307149141989890380L;
	private static Logger log = Logger.getLogger(PanPicture.class);
	protected Communication comm = ((Communication) getBean(Beans.COMMUNICATION));
	private PictureSlimValue picSlimVal;
	private PlanarImage picture;
	private PlanarImage scaledImage = null;
	private PanDisplayJAIScrollable panImage = null;
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
	private Rectangle selection = null;
	private enum ScaleKey {WIDTH, HEIGHT, PERCENT};
	private boolean startedSizeCalculation = false;
	
	
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
			picSlimVal = comm.getPicture(pic);
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
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOkActionPerformed(e);
			}
		});	
		btnCancel.setMaximumSize(new Dimension(95, 27));
		btnCancel.setMinimumSize(new Dimension(95, 27));
		btnCancel.setPreferredSize(new Dimension(95, 27));
//		btnCancel.setToolTipText(rb.getString("panel.content.pictureEditor.btnCancel.tooltiptext"));
		btnCancel.setText("Abbrechen");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed(e);
			}
		});		
		panButtons.setLayout(new GridBagLayout());
		panButtons.setBorder(BorderFactory.createLoweredBevelBorder());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx=0;
		c.gridy=0;
		c.anchor=GridBagConstraints.FIRST_LINE_END;
		c.insets=new Insets(20, 10, 10, 10);
		panButtons.add(btnCrop, c);
		c.gridy=1;
		c.gridheight=6;
		c.insets=new Insets(10, 10, 100, 10);
		panButtons.add(panResize,c);
		c.gridy=7;
		c.gridheight=1;
		c.insets=new Insets(100, 10, 5, 10);
		c.anchor=GridBagConstraints.LAST_LINE_END;
		panButtons.add(btnOk,c);
		c.gridy=8;
		c.insets=new Insets(5, 10, 20, 10);
		panButtons.add(btnCancel,c);
		
		panButtons.setMaximumSize(new Dimension(170, 600));
		panButtons.setMinimumSize(new Dimension(170, 600));
		panButtons.setPreferredSize(new Dimension(170, 600));
				
		panImage = new PanDisplayJAIScrollable(picture, picture.getWidth()/10);
		panImage.addMouseListener(new MouseListener(){
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
		panImage.addMouseMotionListener(new MouseMotionListener(){
			public void mouseDragged(MouseEvent e){
				if(picture.getBounds().contains(e.getX(), e.getY())){
					selectEnd.x = e.getX();
					selectEnd.y = e.getY();
					calculateSelection();
					repaint();
				}
			}
			public void mouseMoved(MouseEvent e) {
				calculateMousePosition(e.getX(), e.getY());
			}
		});
		JScrollPane spaRoot = new JScrollPane(panImage);	
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
		
		panPostions.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		panPostions.setBorder(BorderFactory.createLoweredBevelBorder());
		c.gridx=0;
		c.gridy=0;
		c.insets=new Insets(5, 20, 5, 75);
		panPostions.add(lblMousePosition, c);
		c.gridx=1;
		c.insets=new Insets(5, 75, 5, 75);
		panPostions.add(lblSelectedSize, c);
		c.gridx=2;
		c.insets=new Insets(5, 75, 5, 20);
		panPostions.add(lblCurrentSize, c);		
		panPostions.setMaximumSize(new Dimension(800, 30));
		panPostions.setMinimumSize(new Dimension(800, 30));
		panPostions.setPreferredSize(new Dimension(800, 30));
	}
	
	private void initResizePanel(){
		lblResizeHeader.setText(rb.getString("panel.content.pictureEditor.resize.header"));		
	
		SpinnerModel spnModel = new SpinnerNumberModel(100, 1, 100, 1);
		spnResizePercentalSize.setModel(spnModel);
		spnResizePercentalSize.setMaximumSize(new Dimension(100, 27));
		spnResizePercentalSize.setMinimumSize(new Dimension(100, 27));
		spnResizePercentalSize.setPreferredSize(new Dimension(100, 27));
		spnResizePercentalSize.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				calculatePictureSize(ScaleKey.PERCENT, ((Integer)spnResizePercentalSize.getValue()).longValue());
			}	
		});		
		lblResizePercentalSize.setText(rb.getString("panel.content.pictureEditor.resize.percent"));
		lblResizePercentalSize.setToolTipText(rb.getString("panel.content.pictureEditor.resize.percent.toolTip"));
		lblResizePercentalSize.setMaximumSize(new Dimension(50, 27));
		lblResizePercentalSize.setMinimumSize(new Dimension(50, 27));
		lblResizePercentalSize.setPreferredSize(new Dimension(50, 27));
		
		lblResizeWidth.setText(rb.getString("panel.content.pictureEditor.resize.width"));
		lblResizeWidth.setToolTipText(rb.getString("panel.content.pictureEditor.resize.widht.toolTip"));
		lblResizeWidth.setMaximumSize(new Dimension(50, 27));
		lblResizeWidth.setMinimumSize(new Dimension(50, 27));
		lblResizeWidth.setPreferredSize(new Dimension(50, 27));
		txtResizeWidth.setValue(picture.getWidth());
		txtResizeWidth.addPropertyChangeListener("value", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				calculatePictureSize(ScaleKey.WIDTH, (Long)evt.getNewValue());
			}		
		});
		txtResizeWidth.setMaximumSize(new Dimension(50, 27));
		txtResizeWidth.setMinimumSize(new Dimension(50, 27));
		txtResizeWidth.setPreferredSize(new Dimension(50, 27));
		
		lblResizeHeight.setText(rb.getString("panel.content.pictureEditor.resize.height"));
		lblResizeHeight.setToolTipText(rb.getString("panel.content.pictureEditor.resize.height.toolTip"));
		lblResizeHeight.setMaximumSize(new Dimension(50, 27));
		lblResizeHeight.setMinimumSize(new Dimension(50, 27));
		lblResizeHeight.setPreferredSize(new Dimension(50, 27));
		txtResizeHeight.setValue(picture.getHeight());
		txtResizeHeight.addPropertyChangeListener("value", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				calculatePictureSize(ScaleKey.HEIGHT, (Long)evt.getNewValue());
			}		
		});	
		txtResizeHeight.setMaximumSize(new Dimension(50, 27));
		txtResizeHeight.setMinimumSize(new Dimension(50, 27));
		txtResizeHeight.setPreferredSize(new Dimension(50, 27));
		
		panResize.setLayout(new GridLayout(2,1));
		//panResize.setBorder(BorderFactory.createLoweredBevelBorder());
		panResize.add(lblResizeHeader);
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridLayout(3,2));
		innerPanel.add(lblResizePercentalSize);
		innerPanel.add(spnResizePercentalSize);
		innerPanel.add(lblResizeWidth);
		innerPanel.add(txtResizeWidth);
		innerPanel.add(lblResizeHeight);
		innerPanel.add(txtResizeHeight);
		panResize.add(innerPanel);
		panResize.setMaximumSize(new Dimension(150, 150));
		panResize.setMinimumSize(new Dimension(150, 150));
		panResize.setPreferredSize(new Dimension(150, 150));
	}
	
	private PlanarImage scalePreviewImage(float scale){
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(picture);
		pb.add(scale);
		pb.add(scale);
		pb.add(0.0f);
		pb.add(0.0f);
		pb.add(new InterpolationBilinear());
		return JAI.create("scale", pb);
	}
	
	private void calculatePictureSize(ScaleKey sk, long value)
	{
		if(startedSizeCalculation){
			return;
		}
		startedSizeCalculation=true;
		if(sk == ScaleKey.WIDTH){
			long newWidth = value;
			if(newWidth > picture.getWidth()){
				newWidth = picture.getWidth();
				txtResizeWidth.setValue(newWidth);
			}
			if(value == picture.getWidth()){
				scaledImage = null;
			}
			long newHeight = newWidth*picture.getHeight()/picture.getWidth();
			txtResizeHeight.setValue(newHeight);
			long newPercent = newHeight*100/picture.getHeight();
			spnResizePercentalSize.setValue(new Long(newPercent).intValue());
		}
		if(sk == ScaleKey.HEIGHT){
			long newHeight = value;
			if(newHeight > picture.getHeight()){
				newHeight = picture.getHeight();
				txtResizeHeight.setValue(newHeight);
			}
			if(value == picture.getHeight()){
				scaledImage = null;
			}
			long newWidth = newHeight*picture.getWidth()/picture.getHeight();
			txtResizeWidth.setValue(newWidth);
			long newPercent = newWidth*100/picture.getWidth();
			spnResizePercentalSize.setValue(new Long(newPercent).intValue());
		}
		if(sk == ScaleKey.PERCENT){
			if(value == 100){
				scaledImage = null;
			}
			txtResizeHeight.setValue((picture.getHeight()*value/100));
			txtResizeWidth.setValue((picture.getWidth()*value/100));
		}	
		Thread resizer = new Thread(){
			public void run(){
				PlanarImage img = scalePreviewImage(((Long)txtResizeWidth.getValue()).floatValue()/picture.getWidth());
				panImage.set(img);
				scaledImage = img;
				deleteSelection();
			}};
		resizer.start();
		startedSizeCalculation=false;
	}
	
	private void resetSizes()
	{
		lblCurrentSize.setText(" Bildgroesse " + picture.getWidth() + " x " + picture.getHeight());
		spnResizePercentalSize.setValue(100);
		txtResizeHeight.setValue(new Integer(picture.getHeight()).longValue());
		txtResizeWidth.setValue(new Integer(picture.getWidth()).longValue());
	}
	
	private PlanarImage cropPreviewImage(){
		PlanarImage img;
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(picture);
		pb.add((float)selection.x);
		pb.add((float)selection.y);
		pb.add((float)selection.width);
		pb.add((float)selection.height);
		img = JAI.create("crop",pb, null);		
		// croped Image would stay at the same place like in the original
		// image - upper left corner has to be reseted to zero
		pb = new ParameterBlock();  
		pb.addSource(img);  
		pb.add(-((float)selection.x));  
		pb.add(-((float)selection.y));  
		return JAI.create("translate",pb,null);	
	}
	
	private PlanarImage createThumbnail()
	{
		if(picture.getHeight() <= 90){
			return picture;
		}
		//thumbnail height sould be 90
		float scale = 90f/picture.getHeight();
		return scalePreviewImage(scale);	
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
			selection = new Rectangle(minX, minY, maxX-minX, maxY-minY);
		}
		lblSelectedSize.setText(text);
	}
	
	private void deleteSelection()
	{
		lblSelectedSize.setText(" Markierung ");
		selectStart = null;
		selectEnd = null;
		selection = null;
		btnCrop.setEnabled(false);
		repaint();
	}
	
	private void btnCropActionPerformed(ActionEvent e){
		if(scaledImage != null){
			picture = scaledImage;
			scaledImage = null;
		}
		PlanarImage croped = cropPreviewImage();
		panImage.set(croped);
		picture = croped;
		deleteSelection();
		resetSizes();
	}
	
	private byte[] planarImage2ByteArray(PlanarImage img){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JAI.create("encode", img, out, "PNG", null);
		return out.toByteArray();
	}

	private void btnOkActionPerformed(ActionEvent e){
		if(scaledImage != null){
			picture = scaledImage;
		}
		DlgSavePicture saveDialog = new DlgSavePicture(picSlimVal, planarImage2ByteArray(picture), 
				planarImage2ByteArray(createThumbnail()));
		int frameHeight = 180;
		int frameWidth = 250;
		saveDialog.setSize(frameWidth, frameHeight);
		saveDialog.setLocationRelativeTo(UIConstants.getMainFrame());
		saveDialog.setModal(true);
		saveDialog.setVisible(true);
	}

	private void btnCancelActionPerformed(ActionEvent e){
		
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		int offsetX = panImage.getVisibleRect().x;
		int offsetY = panImage.getVisibleRect().y;	
		int thickness = 3;
		if(selectStart != null)
		{
			int x = selection.x-offsetX;
			int y = selection.y-offsetY;
			int width = (panImage.getVisibleRect().width-x<selection.width)?panImage.getVisibleRect().width-x:selection.width;
			int height = (panImage.getVisibleRect().height-y<selection.height)?panImage.getVisibleRect().height-y:selection.height;
			for(int i=0; i<thickness; i++){
				g.drawRect(x+i, y+i, width, height);
			}
		}
	}
}
