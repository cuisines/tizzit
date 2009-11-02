package org.tizzit.cocoon.generic;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.components.modules.input.AbstractInputModule;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id:AllRequestParameterModule.java 4371 2008-05-28 09:37:42Z kulawik $
 */
public class AllRequestParameterModule extends AbstractInputModule implements ThreadSafe {

	/* (non-Javadoc)
	 * @see org.apache.cocoon.components.modules.input.InputModule#getAttribute(java.lang.String, org.apache.avalon.framework.configuration.Configuration, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAttribute(String name, Configuration modeConf, Map objectModel) throws ConfigurationException {
		StringBuffer rp = new StringBuffer();
		Request req = ObjectModelHelper.getRequest(objectModel);
		Enumeration enume = req.getParameterNames();
		while (enume.hasMoreElements()) {
			String attrName = (String) enume.nextElement();
			rp.append(attrName).append("=").append(req.getParameter(attrName).toString()); //$NON-NLS-1$
			if (enume.hasMoreElements()) rp.append("&"); //$NON-NLS-1$
		}
		return rp.toString();
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.components.modules.input.InputModule#getAttributeNames(org.apache.avalon.framework.configuration.Configuration, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator getAttributeNames(Configuration modeConf, Map objectModel) throws ConfigurationException {
		ArrayList<String> al = new ArrayList<String>(1);
		al.add("allrequest"); //$NON-NLS-1$
		return al.iterator();
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.components.modules.input.InputModule#getAttributeValues(java.lang.String, org.apache.avalon.framework.configuration.Configuration, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getAttributeValues(String name, Configuration modeConf, Map objectModel) throws ConfigurationException {
		StringBuffer rp = new StringBuffer();
		Request req = ObjectModelHelper.getRequest(objectModel);
		Enumeration enume = req.getParameterNames();
		while (enume.hasMoreElements()) {
			String attrName = (String) enume.nextElement();
			rp.append(attrName).append("=").append(req.getParameter(attrName).toString()); //$NON-NLS-1$
			if (enume.hasMoreElements()) rp.append("&"); //$NON-NLS-1$
		}
		return new Object[] {rp.toString()};
	}

}
