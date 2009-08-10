package de.juwimm.cms.gui.controls;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public abstract class DirtyInputListener{
	
	public abstract void onChangeValue();
	
	public static void addDirtyInputListener(JComponent component,final DirtyInputListener listener){		
		if(component instanceof JTextField){
			((JTextField)component).getDocument().addDocumentListener(new DocumentListener(){

				public void changedUpdate(DocumentEvent e) {
					listener.onChangeValue();	
				}

				public void insertUpdate(DocumentEvent e) {
					listener.onChangeValue();					
				}

				public void removeUpdate(DocumentEvent e) {
					listener.onChangeValue();						
				}
				
			});			
		}else if(component instanceof JComboBox){
			((JComboBox)component).addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					listener.onChangeValue();					
				}				
			});			
		}else if(component instanceof JSpinner){
			((JSpinner)component).addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent e) {
					listener.onChangeValue();					
				}
				
			});
		}else if(component instanceof JTable){
			JTable table = (JTable)component;			
			table.getModel().addTableModelListener(new TableModelListener(){
				public void tableChanged(TableModelEvent e) {
					listener.onChangeValue();				
				}			
			});
		}
	}
	
}
