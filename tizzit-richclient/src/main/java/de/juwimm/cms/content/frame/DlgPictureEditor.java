package de.juwimm.cms.content.frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.content.panel.PanPictureEditor;
import de.juwimm.cms.util.UIConstants;

/**
 * @author <a href="mailto:rene.hertzfeldt@juwimm.com">Rene Hertzfeldt</a> 
 * @version $Id: DlgPictureEditor.java 6 2009-07-30 14:05:05Z skulawik@gmail.com $
 */
public class DlgPictureEditor extends JDialog {
	private static Logger log = Logger.getLogger(DlgPictureBrowser.class);
	private ResourceBundle rb = Constants.rb;
	private PanPictureEditor panPictureEditor = null;
	private EventListenerList listenerList = new EventListenerList();
	private int pictureId = 0;

	private DlgPictureEditor()
	{
		super(UIConstants.getMainFrame(), true);
		try{
			init();
		}
		catch(Exception ex)
		{
			log.error("Error while starting ImageEditor.", ex);
		}
	}
	
	public DlgPictureEditor(int picture){
		super(UIConstants.getMainFrame(), true);
		this.pictureId = picture;
		try{
			init();
		}
		catch(Exception ex)
		{
			log.error("Error while starting ImageEditor.", ex);
		}
		
	}
	
	private  void init() throws Exception{
		panPictureEditor = new PanPictureEditor(pictureId);
		this.setTitle("Picture Editor - Picture: " + pictureId);
		this.setLayout(new BorderLayout());
		this.add(panPictureEditor, BorderLayout.CENTER);
	}
	
	public void addSaveActionListener(ActionListener al) {
		this.listenerList.add(ActionListener.class, al);
	}

	public void fireSaveActionListener(ActionEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			((ActionListener) listeners[i + 1]).actionPerformed(e);
		}
	}

	/**
	 * @param pictureId the pictureId to set
	 */
	public void setPictureId(int pictureId) {
		this.pictureId = pictureId;
	}
}
