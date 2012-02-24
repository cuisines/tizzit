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
/*
 * Created on 27.06.2005
 */
package de.juwimm.cms.gui.tree;

import static de.juwimm.cms.client.beans.Application.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import de.juwimm.cms.client.beans.Beans;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.ViewDocumentValue;
import de.juwimm.swing.DropDownHolder;


/**
 * Helper for storing a InternalLinkTreeModel for a given ViewDocument.<br/>
 * Once you have created an instance, you can invalidate the stored model. If you access the stored model,<br/>
 * it revalidates itsself when not valid any more.
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */
public class InternalLinkCache {
	private static Logger log = Logger.getLogger(InternalLinkCache.class);
	private Communication comm = ((Communication) getBean(Beans.COMMUNICATION));

	/*
	 * key = DropDownHolder with Site
	 * value = HashMap with key = DropDownHolder(ViewDocument) value = LinkTreeModelDecorator
	 */
	private HashMap<DropDownHolder, HashMap<DropDownHolder, LinkTreeModelDecorator>> siteViewDocumentCache;
	
	/**
	 * Creates a new InternalLinkCache and fills it with all sites, the user has access to.
	 */
	public InternalLinkCache() {
		this.siteViewDocumentCache = new HashMap<DropDownHolder, HashMap<DropDownHolder, LinkTreeModelDecorator>>();
		SiteValue[] sites = this.comm.getAllRelatedSites(this.comm.getSiteId());
		if (sites != null) {
			for (int i = sites.length - 1; i >= 0; i--) {
				DropDownHolder siteDdh = new DropDownHolder(sites[i], sites[i].getName());
				HashMap<DropDownHolder, LinkTreeModelDecorator> viewDocumentsMap = new HashMap<DropDownHolder, LinkTreeModelDecorator>();
				ViewDocumentValue[] vds = this.comm.getAllViewDocuments4Site(sites[i].getSiteId());
				if (vds != null) {
					for (int j = vds.length - 1; j >= 0; j--) {
						DropDownHolder vdddh = new DropDownHolder(vds[j], vds[j].getViewType() + ", " + vds[j].getLanguage());
						viewDocumentsMap.put(vdddh, null);
					}
				}
				this.siteViewDocumentCache.put(siteDdh, viewDocumentsMap);
			}
		}
	}
	
	private HashMap<DropDownHolder, LinkTreeModelDecorator> getViewDocuments4Site(int siteId) {
		HashMap<DropDownHolder, LinkTreeModelDecorator> viewDocumentsCache = null;
		Iterator it = this.siteViewDocumentCache.keySet().iterator();
		while (it.hasNext()) {
			DropDownHolder ddh = (DropDownHolder) it.next();
			if (((SiteValue) ddh.getObject()).getSiteId() == siteId) {
				viewDocumentsCache = this.siteViewDocumentCache.get(ddh);
				break;
			}
		}
		
		return viewDocumentsCache;
	}

	/**
	 * Add an new model to the cache.
	 * @param key
	 * @param model
	 */
	public void addLinkTree(int siteId, DropDownHolder key, InternallinkTreeModel model) {
		if(this.getViewDocuments4Site(siteId)==null){
			SiteValue newSite=this.comm.getSite(siteId);
			DropDownHolder siteDdh=new DropDownHolder(newSite, newSite.getName());
			HashMap<DropDownHolder, LinkTreeModelDecorator> viewDocumentsMap = new HashMap<DropDownHolder, LinkTreeModelDecorator>();
			this.siteViewDocumentCache.put(siteDdh, viewDocumentsMap);
		}
		this.getViewDocuments4Site(siteId).put(key, new LinkTreeModelDecorator(model));
	}
	
	/**
	 * Clean the cache for the selected site.
	 */
	public void clearCache(int siteId) {
		if(this.getViewDocuments4Site(siteId)!=null){
			this.getViewDocuments4Site(siteId).clear();
			System.gc();
		}
	}
	
	/**
	 * Get all sites contained in the cache.
	 * @return a set of DropDownHolder
	 */
	public Set<DropDownHolder> getSites() {
		return this.siteViewDocumentCache.keySet();
	}

	/**
	 * Get all viewdocuments contained in the cache for the selected site.
	 * @return a set of DropDownHolder
	 */
	public Set<DropDownHolder> getViewDocuments(int siteId) {
		HashMap<DropDownHolder, LinkTreeModelDecorator> vdSet = this.getViewDocuments4Site(siteId);
		if (vdSet != null)
			return vdSet.keySet();
		return null;
	}

	/**
	 * Get the model for the key. If no model exists, the cache fetches, stores and returns the model for this key.
	 * @param siteId
	 * @param key
	 * @return
	 */
	public InternallinkTreeModel getModel(int siteId, DropDownHolder key) {
		LinkTreeModelDecorator model = this.getViewDocuments4Site(siteId).get(key);
		if (model == null || !model.isModuleValid() || model.getModel() == null) {
			revalidate(siteId, key);
			model = this.getViewDocuments4Site(siteId).get(key);
		}
		return model.getModel();
	}
	
	private void revalidate(int siteId, DropDownHolder key) {
		ViewDocumentValue vd = (ViewDocumentValue) key.getObject();
		InternallinkTreeModel model = null;
		try {
			log.info("Loading TreeModel \"" + key.toString() + "\" for Internal Link... Please wait :)");
			model = new InternallinkTreeModel(new PageNode(this.comm.getViewComponent(vd.getViewId())));
		} catch (Exception e) {
			log.error("Error on fetching TreeModel for InternalLink: " + e.getMessage());
		}
		if (model != null) {
			this.getViewDocuments4Site(siteId).remove(key);
			this.addLinkTree(siteId, key, model);
		}
	}

	/**
	 * Invalidate the model for the given key.<br/>
	 * Force the cache to update the state of the model on the next access.
	 * @param key
	 */
	public void invalidateModel(int siteId, DropDownHolder key) {
		LinkTreeModelDecorator current = this.getViewDocuments4Site(siteId).get(key);
		if (current != null) current.invalidate();
	}

	public DropDownHolder getSiteDropDownHolder(int siteId) {
		Iterator it = this.siteViewDocumentCache.keySet().iterator();
		while (it.hasNext()) {
			DropDownHolder ddh = (DropDownHolder) it.next();
			if (((SiteValue) ddh.getObject()).getSiteId() == siteId) {
				return ddh;
			}
		}
		return null;
	}

	public DropDownHolder getViewDocumentDropDownHolder(int siteId, int viewDocumentId) {
		Set<DropDownHolder> viewDocuments = this.getViewDocuments(siteId);
		if (viewDocuments != null) {
			Iterator it = viewDocuments.iterator();
			while (it.hasNext()) {
				DropDownHolder ddh = (DropDownHolder) it.next();
				if (((ViewDocumentValue) ddh.getObject()).getViewDocumentId() == viewDocumentId) {
					return ddh;
				}
			}
		}
		return null;
	}

	/**
	 * Inner class for storing a InternalLinkTreeModel and a validity-state.
	 */
	private class LinkTreeModelDecorator {
		private InternallinkTreeModel model;
		private boolean valid;
		
		public LinkTreeModelDecorator(InternallinkTreeModel model) {
			this.model = model;
			this.valid = true;
		}
		
		public boolean isModuleValid() {
			return this.valid;
		}
		
		public InternallinkTreeModel getModel() {
			return this.model;
		}
		
		public void invalidate() {
			this.valid = false;
		}
	}

}
