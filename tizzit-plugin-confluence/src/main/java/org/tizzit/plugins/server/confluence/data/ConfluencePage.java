/*
 * Copyright (c) 2002-2009 Juwi MacMillan Group GmbH (JuwiMM)
 * Bockhorn 1, 29664 Walsrode, Germany
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of JuwiMM
 * ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with JuwiMM.
 */
package org.tizzit.plugins.server.confluence.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Content holder for XML-RPC results from Confluence.
 *
 * @author <a href="mailto:eduard.siebert@juwimm.com">Eduard Siebert</a>
 * company Juwi MacMillan Group GmbH, Walsrode, Germany
 * @version $Id$
 * @since tizzit-plugin-confluence 15.10.2009
 */
public class ConfluencePage implements Serializable {
	private static final Log log = LogFactory.getLog(ConfluencePage.class);

	private static final long serialVersionUID = 483651286519081691L;

	public ConfluencePage() {
	}

	private String space;
	private String url;
	private String id;
	private boolean homePage;
	private String creator;
	private String modifier;
	private String contentStatus;
	private Date created;
	private String parentId;
	private boolean current;
	private String content;
	private int version;
	private int permissions;
	private String title;
	private Date modified;

	/**
	 * @return the space
	 */
	public String getSpace() {
		return space;
	}

	/**
	 * @param space the space to set
	 */
	public void setSpace(String space) {
		this.space = space;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the homePage
	 */
	public boolean isHomePage() {
		return homePage;
	}

	/**
	 * @param homePage the homePage to set
	 */
	public void setHomePage(boolean homePage) {
		this.homePage = homePage;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * @param modifier the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * @return the contentStatus
	 */
	public String getContentStatus() {
		return contentStatus;
	}

	/**
	 * @param contentStatus the contentStatus to set
	 */
	public void setContentStatus(String contentStatus) {
		this.contentStatus = contentStatus;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the current
	 */
	public boolean isCurrent() {
		return current;
	}

	/**
	 * @param current the current to set
	 */
	public void setCurrent(boolean current) {
		this.current = current;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the permissions
	 */
	public int getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(int permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	public void transform(Hashtable<Object, Object> page) {
		if (log.isDebugEnabled()) log.debug("transform() -> begin");

		this.setSpace((String) page.get("space"));
		this.setUrl((String) page.get("url"));
		this.setId((String) page.get("id"));
		this.setHomePage(Boolean.parseBoolean((String) page.get("homePage")));
		this.setCreator((String) page.get("creator"));
		this.setModifier((String) page.get("modifier"));
		this.setContentStatus((String) page.get("contentStatus"));
		this.setCreated((Date) page.get("created"));
		this.setParentId((String) page.get("parentId"));
		this.setCurrent(Boolean.parseBoolean((String) page.get("current")));
		this.setContent((String) page.get("content"));
		this.setVersion(Integer.parseInt((String) page.get("version")));
		this.setPermissions(Integer.parseInt((String) page.get("permissions")));
		this.setTitle((String) page.get("title"));
		this.setModified((Date) page.get("modified"));

		if (log.isDebugEnabled()) log.debug("transform() -> end");
	}
}
