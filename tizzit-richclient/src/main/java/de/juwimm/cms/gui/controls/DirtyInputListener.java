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
 * Used for different control types(JTextField,JComboBox,JSpinner,JTable) to detect value changes.
 */
public abstract class DirtyInputListener{
	
	/**
	 * Called when a control changes his value.
	 */
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
