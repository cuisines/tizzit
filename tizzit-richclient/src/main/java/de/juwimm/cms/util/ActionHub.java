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
package de.juwimm.cms.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.gui.event.ExitEvent;
import de.juwimm.cms.gui.event.ExitListener;
import de.juwimm.cms.gui.event.FinishedActionListener;
import de.juwimm.cms.gui.event.ViewComponentEvent;
import de.juwimm.cms.gui.event.ViewComponentListener;

/**
 * <b>Tizzit Enterprise Content Management</b><br/>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class ActionHub {
	private static Logger log = Logger.getLogger(ActionHub.class);
	private static EventListenerList listenerList = new EventListenerList();
	private static int loadingProcesses = 0;

	private ActionHub() {
	}

	/* ---------------------------------------------------------------------------------------------
	 * 							KEY / MOUSE LISTENER FOR CONTENT EDITING MODE
	 * ---------------------------------------------------------------------------------------------
	 */

	/**
	 * This KeyListener is the default KeyListener for all GUI Elements which want to
	 * get an "Do you want to save" Dialog if someone changes content and will switch
	 * between the Nodes in the Tree
	 */
	private static KeyListener klContentEdit = new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent ke) {
			if (!Constants.EDIT_CONTENT) {
				Constants.EDIT_CONTENT = true;
			}
		}
	};

	private static MouseListener mlContentEdit = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (!Constants.EDIT_CONTENT) {
				Constants.EDIT_CONTENT = true;
			}
		}
	};

	public static KeyListener getContentEditKeyListener() {
		return klContentEdit;
	}

	public static MouseListener getContentEditMouseListener() {
		return mlContentEdit;
	}

	/* ---------------------------------------------------------------------------------------------
	 * 									PROPAGATION OF PROPERTIES
	 * ---------------------------------------------------------------------------------------------
	 */
	private static Hashtable<String, PropertyActionEvent> htPropProperties = new Hashtable<String, PropertyActionEvent>();

	public static void registerProperty(int propertyType, String uniqueId, String parentId, String descName, String descDetail, String actionCommand) {
		PropertyActionEvent pae = null;
		if (!htPropProperties.containsKey(uniqueId)) {
			pae = new PropertyActionEvent(UIConstants.getMainFrame(), ActionEvent.ACTION_PERFORMED, Constants.PROPERTY_PROPAGATION, uniqueId, propertyType);
			pae.setParentId(parentId);
			pae.setDescName(descName);
			pae.setDescDetail(descDetail);
			pae.setAction(actionCommand);

			htPropProperties.put(uniqueId, pae);
		} else {
			pae = htPropProperties.get(uniqueId);
		}
		ActionHub.fireActionPerformed(pae, true);
	}

	public static void unregisterProperty(String uniqueId) {
		if (htPropProperties.containsKey(uniqueId)) {
			PropertyActionEvent pae = new PropertyActionEvent(UIConstants.getMainFrame(), ActionEvent.ACTION_PERFORMED, Constants.PROPERTY_DEPROPAGATION, uniqueId, -1);
			ActionHub.fireActionPerformed(pae, true);
		}
	}

	public static void configureProperty(String uniqueId, String key, String value) {
		if (htPropProperties.containsKey(uniqueId)) {
			PropertyConfigurationEvent pce = new PropertyConfigurationEvent(UIConstants.getMainFrame(), ActionEvent.ACTION_PERFORMED, Constants.PROPERTY_CONFIGURATION, uniqueId);
			pce.setKey(key);
			pce.setValue(value);
			ActionHub.fireActionPerformed(pce, true);
		}
	}

	/* ---------------------------------------------------------------------------------------------
	 * 										ACTION HANDLING
	 * ---------------------------------------------------------------------------------------------
	 */
	public static void addActionListener(ActionListener l) {
		EventListener[] el = listenerList.getListeners(ActionListener.class);
		boolean found = false;
		for (int i = 0; i < el.length; i++) {
			if (el[i] == l) {
				found = true;
				break;
			}
		}
		if (!found) listenerList.add(ActionListener.class, l);
	}

	public static void addExitListener(ExitListener l) {
		listenerList.add(ExitListener.class, l);
	}

	public static void addViewComponentListener(ViewComponentListener l) {
		listenerList.add(ViewComponentListener.class, l);
	}

	public static void removeActionListener(ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

	// Notify all listeners that have registered interest for
	// notification on this event type.  The event instance
	// is lazily created using the parameters passed into
	// the fire method.
	public static void fireActionPerformed(ActionEvent event) {
		fireActionPerformed(event, false);
	}

	public static void fireActionPerformed(ActionEvent event, boolean ensureOrder) {
		loadingProcesses++;
		UIConstants.setWorker(true);
		if (log.isDebugEnabled()) log.debug("Fired Action: " + event.getActionCommand() + " loading processes: " + loadingProcesses);
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		UIConstants.setActionStatus(event.getActionCommand());
		// Process the listeners last to first, notifying
		// those that are interested in this event
		Vector<Thread> regThreads = new Vector<Thread>();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				ActionListener al = ((ActionListener) listeners[i + 1]);
				Thread t = new Thread(new FireActionPerformedEvent(al, event, ensureOrder));
				t.setName("FireActionPerformedEvent");
				if (ensureOrder) {
					t.run();
				} else {
					t.setPriority(Thread.NORM_PRIORITY);
					t.start();
				}
				regThreads.add(t);
			}
		}
		Thread t = new Thread(new JoinThreadsAndResetStatusInfo(regThreads, event));
		t.setName("JoinThreadsAndResetStatusInfo");
		t.setPriority(Thread.NORM_PRIORITY);
		t.start();
	}

	/**
	 * <p>Title: Tizzit</p>
	 * <p>Description: Enterprise Content Management</p>
	 * <p>Copyright: Copyright (c) 2004</p>
	 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 * @version $Revision: 3168 $
	 */
	private static class FireActionPerformedEvent implements Runnable {
		private ActionListener al = null;
		private ActionEvent event = null;
		private boolean eventDispatcherThread = false;

		public FireActionPerformedEvent(ActionListener actionListener, ActionEvent actionEvent, boolean isEventDispatcherThread) {
			this.al = actionListener;
			this.event = actionEvent;
			this.eventDispatcherThread = isEventDispatcherThread;
		}

		public void run() {
			try {
				if (eventDispatcherThread) {
					al.actionPerformed(event);
				} else {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							al.actionPerformed(event);
						}
					});
				}
			} catch (Exception exe) {
			}
		}
	}

	public static boolean fireExitPerformed(ExitEvent event) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ExitListener.class) {
				if (!((ExitListener) listeners[i + 1]).exitPerformed(event)) { return false; }
			}
		}
		return true;
	}

	public static void fireViewComponentPerformed(ViewComponentEvent event) {
		loadingProcesses++;
		UIConstants.setWorker(true);
		Object[] listeners = listenerList.getListenerList();
		Vector<Thread> regThreads = new Vector<Thread>();
		String actionMessage = new Integer(event.getType()).toString();
		UIConstants.setActionStatus(actionMessage);
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ViewComponentListener.class) {
				ViewComponentListener vcl = ((ViewComponentListener) listeners[i + 1]);
				Thread t = new Thread(new FireViewComponentPerformedEvent(vcl, event));
				t.setName("FireViewComponentPerformedEvent");
				t.setPriority(Thread.NORM_PRIORITY);
				t.start();
				synchronized (regThreads) {
					regThreads.add(t);
				}
			}
		}
		Thread t = new Thread(new JoinThreadsAndResetStatusInfo(regThreads, new ActionEvent(UIConstants.getMainFrame(), ActionEvent.ACTION_PERFORMED, actionMessage)));
		t.setName("JoinThreadsAndResetStatusInfo");
		t.setPriority(Thread.NORM_PRIORITY);
		t.start();
	}

	/**
	 * <p>Title: Tizzit</p>
	 * <p>Description: Enterprise Content Management</p>
	 * <p>Copyright: Copyright (c) 2004</p>
	 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 * @version $Revision: 3168 $
	 */
	private static class FireViewComponentPerformedEvent implements Runnable {
		private ViewComponentListener vcl = null;
		private ViewComponentEvent event = null;

		public FireViewComponentPerformedEvent(ViewComponentListener vcListener, ViewComponentEvent vcEvent) {
			this.vcl = vcListener;
			this.event = vcEvent;
		}

		public void run() {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						vcl.actionViewComponentPerformed(event);
						UIConstants.repaintApp();
					}
				});
			} catch (Exception exe) {
			}
		}
	}

	/**
	 * <p>Title: Tizzit</p>
	 * <p>Description: Enterprise Content Management</p>
	 * <p>Copyright: Copyright (c) 2004</p>
	 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 * @version $Revision: 3168 $
	 */
	private static class JoinThreadsAndResetStatusInfo implements Runnable {
		private Vector regThreads = null;
		private ActionEvent event = null;

		public JoinThreadsAndResetStatusInfo(Vector vecRegThreads, ActionEvent actionEvent) {
			this.regThreads = vecRegThreads;
			this.event = actionEvent;
		}

		public void run() {
			//String idName = System.currentTimeMillis() + " " + event.getActionCommand();
			synchronized (regThreads) {
				Iterator it = regThreads.iterator();
				while (it.hasNext()) {
					UIConstants.setActionStatus(event.getActionCommand());
					Thread t = (Thread) it.next();
					try {
						t.join();
					} catch (Exception exe) {
					}
				}
			}

			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						UIConstants.setActionStatus("");

						if (--loadingProcesses == 0) {
							if (log.isDebugEnabled()) log.debug("finished all processes");
							Object[] listeners = listenerList.getListenerList();
							for (int i = listeners.length - 2; i >= 0; i -= 2) {
								if (listeners[i] == ActionListener.class) {
									if (listeners[i + 1] instanceof FinishedActionListener) {
										FinishedActionListener fal = (FinishedActionListener) listeners[i + 1];
										if (log.isDebugEnabled()) log.debug("Sending finishedinfo to " + fal.toString());
										fal.actionFinished();
									}
								}
							}
							UIConstants.setWorker(false);
							UIConstants.repaintApp();
						} else {
							if (log.isDebugEnabled()) log.debug("finished process. remaining: " + loadingProcesses);
						}
					}
				});
			} catch (Exception exe) {
			}
		}
	}

	/**
	 * This is a nonblocking method to show a message window.
	 * @param message The message to show
	 * @param messageType The JOptionPane-messageType
	 */
	public static void showMessageDialog(String message, int messageType) {
		new MessageDlgOwnerThread(message, messageType).start();
	}

	/**
	 * <p>Title: Tizzit</p>
	 * <p>Description: Enterprise Content Management</p>
	 * <p>Copyright: Copyright (c) 2004</p>
	 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 * @version $Revision: 3168 $
	 */
	private static class MessageDlgOwnerThread extends Thread {
		private String message = "";
		private int messageType = JOptionPane.ERROR_MESSAGE;

		public MessageDlgOwnerThread(String strMessage, int msgType) {
			this.setName("MessageDlgOwnerThread");
			this.message = strMessage;
			this.messageType = msgType;
		}

		@Override
		public void run() {
			try {
				SwingUtilities.invokeAndWait(new MessageDlgSwingRunnable(message, messageType));
			} catch (Exception exe) {
				log.error("Error showing OptionPane", exe);
			}
		}
	}

	/**
	 * <p>Title: Tizzit</p>
	 * <p>Description: Enterprise Content Management</p>
	 * <p>Copyright: Copyright (c) 2004</p>
	 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
	 * @version $Revision: 3168 $
	 */
	private static class MessageDlgSwingRunnable implements Runnable {
		private String message = null;
		private final int messageType;

		public MessageDlgSwingRunnable(String strMessage, int msgType) {
			this.message = strMessage;
			this.messageType = msgType;
		}

		public void run() {
			JOptionPane.showMessageDialog(UIConstants.getMainFrame(), message, Constants.rb.getString("dialog.title"), messageType);
		}
	}

}
