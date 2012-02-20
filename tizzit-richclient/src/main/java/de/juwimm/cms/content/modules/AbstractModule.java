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
package de.juwimm.cms.content.modules;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.xml.sax.ContentHandler;

import de.juwimm.cms.content.event.EditpaneFiredEvent;
import de.juwimm.cms.content.event.EditpaneFiredListener;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public abstract class AbstractModule implements Module, Cloneable {
	private static Logger log = Logger.getLogger(AbstractModule.class);
	private boolean mandatory = false;
	private String dcfname = "";
	private String label = "";
	private String description = "";
	private EventListenerList listenerList = new EventListenerList();
	private String errMsg = "";
	private String rootnodeName = "";
	private ArrayList<Object[]> customProperties = new ArrayList<Object[]>();
	private boolean customConfigurationReady = false;
	private boolean isLoaded = false;
	private boolean saveable = true;

	/**
	 * Timestamp is used to specify unique dcf module in iteration
	 */
	private long timeStamp = 0;

	public final long getTimeStamp() {
		return this.timeStamp;
	}

	public final void setTimeStamp(long newTimeStamp) {
		this.timeStamp = newTimeStamp;
	}

	public String getRootnodeName() {
		return this.rootnodeName;
	}

	public void setRootnodeName(String newRootnodeName) {
		rootnodeName = newRootnodeName;
	}

	public final boolean isMandatory() {
		return this.mandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.mandatory = isMandatory;
	}

	public String getValidationError() {
		return errMsg;
	}

	/**
	 * This Method should be called from within the Module to set the Errormessage, if there is one occuring.<br>
	 * The Method getValidationError will call the isValid Method -
	 * in this Method the validation should be checked and<br>
	 * the Module itself should set the ValidationError afterwords.
	 *
	 * @param errorMessage The errormessage
	 */
	protected final void setValidationError(String errorMessage) {
		if (errorMessage == null) {
			this.errMsg = "";
		} else {
			this.errMsg = errorMessage;
		}
	}

	/**
	 * Method for APPENDING an errormessage to the current one.<br>
	 * This is needful if you won't check everytime you append one errormessage to a big errormessage,
	 * if there is one in and if you need to append a carriage return to that String.<br>
	 * PLEASE MAKE SURE, that the errormessage was peviously resetted by setting setValidationError("")!
	 *
	 * @param errMsg The errormessage to append
	 */
	protected final void appendValidationError(String appendMsg) {
		if (appendMsg != null) {
			if (errMsg.equals("")) {
				errMsg = appendMsg;
			} else {
				errMsg += "\n" + appendMsg;
			}
		}
	}

	public final void setName(String moduleDcfname) {
		this.dcfname = moduleDcfname;
	}

	public final String getName() {
		return this.dcfname;
	}

	public final void setLabel(String dcfconfiglabel) {
		this.label = dcfconfiglabel;
	}

	public final String getLabel() {
		return this.label;
	}

	public final void setDescription(String userdescription) {
		this.description = userdescription;
	}

	public final String getDescription() {
		return this.description;
	}

	/**
	 * This encodes a String with URL Encoding and should be used in general.
	 * @param url
	 * @return The encoded String.
	 */
	public static final String getURLEncoded(String url) {
		String retVal = url;
		try {
			retVal = URLEncoder.encode(url, "UTF-8");
			retVal = retVal.replaceAll("[+]", "%20");
			retVal = retVal.replaceAll("(%2F)", "%20"); // Slash is now a Space
		} catch (UnsupportedEncodingException exe) {
			log.error("UNSUPPORTED ENCODING UTF-8!", exe);
		}
		return retVal;
	}

	/**
	 * This encodes a URL with URLEncoding for using URLs in Attributes of XML Files.<br>
	 * This MUST be a valid URL!
	 * @param urlTxt
	 * @return URL
	 */
	public static final String getURLURLEncoded(String urlTxt) {
		try {
			URL url = new URL(urlTxt);
			String retVal = url.getPath();
			try {
				String q = url.getQuery();
				if (q == null || q.equals("")) {
					retVal = URLEncoder.encode(url.getPath(), "UTF-8");
				} else {
					retVal = URLEncoder.encode(url.getPath() + "?" + q, "UTF-8");
				}
				retVal = retVal.replaceAll("%2F", "/");
				retVal = retVal.replaceAll("%3F", "?");
				retVal = retVal.replaceAll("%3D", "=");
				retVal = retVal.replaceAll("%26", "&");
			} catch (UnsupportedEncodingException exe) {
				log.error("UNSUPPORTED ENCODING UTF-8!", exe);
			}
			String adr = url.getProtocol() + "://" + url.getHost() + retVal;
			return adr;
		} catch (Exception exe) {
		}
		return "";
	}

	/**
	 * This encodes a String with ISO-8859-1 encoding and should be only used for the SVG Images -
	 * this means <b>the only use of this method is in getPaneImage()!</b>
	 * @param url The url to encode
	 * @return The encoded String
	 */
	public static final String getURLEncodedISO(String url) {
		String retVal = url;
		try {
			retVal = URLEncoder.encode(url, "ISO-8859-1");
			int fss = retVal.indexOf('.');
			if (fss != -1) {
				retVal = retVal.substring(0, fss) + "." + retVal.substring(fss).replaceAll("\\.", "");
			}
			retVal = retVal.replaceAll("[+]", "%20");
			retVal = retVal.replaceAll("(%2F)", "%20"); // Slash is now a Space
		} catch (UnsupportedEncodingException exe) {
			log.error("UNSUPPORTED ENCODING ISO-8859-1!", exe);
		}
		return retVal;
	}

	public static final String getURLDecoded(String encodedString) {
		String retVal = encodedString;
		try {
			retVal = URLDecoder.decode(encodedString, "UTF-8");
		} catch (UnsupportedEncodingException exe) {
			log.error("UNSUPPORTED ENCODING UTF-8!", exe);
		}
		return retVal;
	}

	/**
	 * Method for adding a listener for all save-operations of the guibuilder.
	 * These Listeners are saved until the GuibuilderSingleton Instance will be dropped.
	 * @param sav The SaveOperationListener Object
	 */
	public final void addEditpaneFiredListener(EditpaneFiredListener sav) {
		this.listenerList.add(EditpaneFiredListener.class, sav);
	}

	/**
	 * Method for removing a listener for all save-operations.
	 * @param sav The SaveOperationListener Object
	 */
	public final void removeEditpaneFiredListener(EditpaneFiredListener sav) {
		this.listenerList.remove(EditpaneFiredListener.class, sav);
	}

	public final boolean hasEditpaneFiredListener() {
		if (this.listenerList.getListenerCount(EditpaneFiredListener.class) > 0) { return true; }
		return false;
	}

	public final void runEditpaneFiredEvent(EditpaneFiredEvent efe) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 1; i >= 0; i --) {
			if(listeners[i] instanceof EditpaneFiredListener){
				((EditpaneFiredListener) listeners[i]).editpaneFiredPerformed(efe);
			}
		}
	}

	public final void runEditpaneCancelEvent(EditpaneFiredEvent efe) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			((EditpaneFiredListener) listeners[i + 1]).editpaneCancelPerformed(efe);
		}
	}

	/**
	 * This Implementation is only for "cloning-purposes" and should be overwritten if you need some custom values.<br/>
	 * Please beware, that you call this method during your overwriting with super.setCustomProperties()
	 *
	 * @param methodName
	 * @param parameters
	 */
	public void setCustomProperties(String methodName, Properties parameters) {
		customProperties.add(new Object[] {methodName, parameters});
		if (methodName.equals("CustomConfigurationReady")) {
			this.customConfigurationReady = true;
		}
	}

	public boolean isCustomConfigurationReady() {
		return this.customConfigurationReady;
	}

	public void waitWhileCustomConfigurationIsntReady() {
		try {
			Thread t = new Thread("Wait-For-Custom-Configuration") {
				public void run() {
					try {
						while (!customConfigurationReady) {
							Thread.sleep(20);
						}
					} catch (Exception exe) {
					}
				}
			};
			t.setPriority(Thread.NORM_PRIORITY);
			t.start();
			t.join();
		} catch (Exception exe) {
		}
	}

	public void waitWhileIsntLoaded() {
		try {
			Thread t = new Thread() {
				public void run() {
					try {
						while (!customConfigurationReady) {
							Thread.sleep(20);
						}
					} catch (Exception exe) {
					}
				}
			};
			t.setPriority(Thread.NORM_PRIORITY);
			t.start();
			t.join();
		} catch (Exception exe) {
		}
	}

	/**
	 * @param isLoaded The isLoaded to set.
	 */
	protected void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	/**
	 * @return Returns the isLoaded.
	 */
	protected boolean isLoaded() {
		return isLoaded;
	}

	public Object clone() {
		return clone(true);
	}

	public Object clone(boolean cloneAndSetProperties) {
		waitWhileCustomConfigurationIsntReady();
		Module module = null;
		try {
			module = this.getClass().newInstance();
			Iterator it = customProperties.iterator();
			boolean sendCCR = true;
			while (it.hasNext()) {
				Object[] cpr = (Object[]) it.next();
				module.setCustomProperties((String) cpr[0], (Properties) cpr[1]);
				if ("CustomConfigurationReady".equalsIgnoreCase((String) cpr[0])) sendCCR = false;
			}
			if (sendCCR) module.setCustomProperties("CustomConfigurationReady", new Properties());
			module.setRootnodeName(this.getRootnodeName());
			module.setDescription(this.getDescription());
			module.setLabel(this.getLabel());
			module.setMandatory(this.isMandatory());
			module.setName(this.getName());
			//module.setCustomProperties();
			if (cloneAndSetProperties) module.setProperties(this.getProperties());
		} catch (Exception exe) {
			log.error("Error during the clone phase of the module", exe);
		}
		return module;
	}

	/**
	 * @return Returns the saveable.
	 */
	public final boolean isSaveable() {
		return this.saveable;
	}

	/**
	 * @param isSaveable The saveable to set.
	 */
	public final void setSaveable(boolean isSaveable) {
		this.saveable = isSaveable;
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#load(org.xml.sax.ContentHandler)
	 */
	public void load(ContentHandler ch) {
		throw new UnsupportedOperationException("actually not supported");
	}

	/* (non-Javadoc)
	 * @see de.juwimm.cms.content.modules.Module#save(org.xml.sax.ContentHandler)
	 */
	public void save(ContentHandler contentHandler) {
		throw new UnsupportedOperationException("actually not supported");
	}
}