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
package de.juwimm.cms.deploy.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public interface Wizard {
	public void showWizard();

	public void addWizardListener(ActionListener al);

	public void removeWizardListener(ActionListener al);

	public void runWizardFiredEvent(ActionEvent e);

	public void setPanel(JPanel pan, String strWizardDescription);

	public JPanel getPanel();

	public void setBackEnabled(boolean back);

	public void setNextEnabled(boolean next);

	public void setNextAsFinally(boolean fin);

	public void setCancelEnabled(boolean cancel);
}
