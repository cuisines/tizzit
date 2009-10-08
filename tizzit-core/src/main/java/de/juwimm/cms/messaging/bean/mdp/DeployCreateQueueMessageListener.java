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
package de.juwimm.cms.messaging.bean.mdp;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.authorization.remote.AuthorizationServiceSpring;
import de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.remote.EditionServiceSpring;

/**
 * This is the DeployCreateQueueBean, a Message-driven Bean, which only processes a single Item out of the queue
 * at a time. This MDB sends the Data at the end to the liveserver for deploying purposes.
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2002, 2003</p>
 * <p>Company: Juwi|MacMillan Group GmbH</p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 *
 */
public class DeployCreateQueueMessageListener implements MessageListener {
	private static Logger log = Logger.getLogger(DeployCreateQueueMessageListener.class);
	private static int id = 0;
	private EditionServiceSpring editionService = null;
	private AuthorizationServiceSpring authorizationService = null;
	private UserHbmDao userHbmDao = null;
	private final UserHbm user = null;
	private final SiteHbm previousSite = null;
	private TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring;

	public TizzitPropertiesBeanSpring getTizzitPropertiesBeanSpring() {
		return tizzitPropertiesBeanSpring;
	}

	public void setTizzitPropertiesBeanSpring(TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring) {
		this.tizzitPropertiesBeanSpring = tizzitPropertiesBeanSpring;
	}

	public void setEditionServiceSpring(EditionServiceSpring editionService) {
		this.editionService = editionService;
	}

	public void setAuthorizationServiceSpring(AuthorizationServiceSpring authorizationService) {
		this.authorizationService = authorizationService;
	}

	public void setUserHbmDao(UserHbmDao userHbmDao) {
		this.userHbmDao = userHbmDao;
	}

	/**
	 * <b>SECURITY INFORMATION:</b> Available only to: <i>deploy, siteRoot</i>
	 * @param message The Messageobject. Currently can contain following <i>StringProperties</i>:
	 * <ul><li><i>userName</i> - The username</li>
	 * <li><i>comment</i> - A Comment</li>
	 * <li><i>rootViewComponentId</i> - The rootViewComponentId of this Unit</li>
	 * <li><i>deploy</i> - Boolean if only create Edition or also deploy to the Liveserver</li>
	 * <li><i>showMessage</i> - The Message the User woll get on success</li>
	 * <li><i>liveServerIP</i> - The IP Address of the Liveserver</li></ul>
	 */
	public void onMessage(Message message) {
		log.debug("Started queue with Job: " + (++id));
		String messageType = "";
		try {
			messageType = message.getJMSType();

			//	if (messageType.equalsIgnoreCase(MessageConstants.MESSAGE_TYPE_LIVE_DEPLOY)) {
			createLiveEdition(message);

			//	}

		} catch (Exception exe) {
			log.error("Error occured in onMessage doing " + messageType + ": ", exe);
		}
		log.debug("Finished queue with Job: " + id);
	}

	private void createLiveEdition(Message message) {
		try {
			String userName = message.getStringProperty("userName");
			String comment = message.getStringProperty("comment");
			Integer rootViewComponentId = new Integer(message.getStringProperty("rootViewComponentId"));
			boolean deploy = message.getBooleanProperty("deploy");
			boolean showMessage = Boolean.parseBoolean(message.getStringProperty("showMessage"));
			Integer siteId = new Integer(message.getStringProperty("siteId"));

			editionService.createLiveDeploy(userName, comment, rootViewComponentId, deploy, showMessage);
			//createLiveDeploy(userName, comment, rootViewComponentId, deploy, showMessage);
			//restoreUserState();
		} catch (Exception exe) {
			log.error("Error occured in CreateLiveEditionPrivilegedAction: ", exe);
		}
	}

	/*
	private void createLiveDeploy(String userName, String comment, Integer rootViewComponentId, boolean deploy, boolean showMessage) throws Exception {
		if (rootViewComponentId != null) {
			EditionHbm edition = null;
			try {
				log.info("Start creating Edition for ViewComponent " + rootViewComponentId);
				edition = EditionHbm.Factory.newInstance();
				edition.create(comment, rootViewComponentId, null, !deploy);
				this.editionHbmDao.create(edition);
				log.info("Finished creating Edition for ViewComponent " + rootViewComponentId);
				Collection coll = this.editionHbmDao.findByUnitAndOnline(new Integer(edition.getUnitId()));
				log.debug("Finished findByUnitAndOnline");
				Iterator it = coll.iterator();
				while (it.hasNext()) {
					log.debug("Starting to disable old Edition");
					EditionHbm eded = (EditionHbm) it.next();
					eded.setStatus((byte) 0);
					log.debug("setStatus " + eded.getEditionId());
				}

				log.debug("setting status = 1");
				edition.setStatus((byte) 1);
				log.debug("finished setting status = 1");
			} catch (Exception exe) {
				if (edition == null) {
					log.error("Unknown Error while creating the Edition");
				} else {
					createTask(userName, exe.toString(), rootViewComponentId, Constants.TASK_SYSTEMMESSAGE_ERROR, true);
				}
				log.error("Error occured in createLiveDeploy: ", exe);
				return;
			}
			if (deploy) {
				try {
					log.debug("Starting call to EditionService regarding publishEditionToLiveserver");
					this.editionService.publishEditionToLiveserver(edition.getEditionId());
					log.debug("Publish to liveServer sucessfully called!");
					createTask(userName, "CreateEditionAndDeploy", rootViewComponentId,
							Constants.TASK_SYSTEMMESSAGE_INFORMATION, showMessage);
				} catch (Exception re) {
					log.error("Error occured in createLiveDeploy: ", re);
					String errMsg = "";
					errMsg = re.getMessage();
					if (re instanceof AxisFault && ((AxisFault) re).getFaultActor() != null) {
						// currently we only can throw AxisFaults, because of the limited serialization
						// of Axis. So we have to map this Exception on client-side manually.
						errMsg = ((AxisFault) re).getFaultActor();
					}
					createTask(userName, errMsg, rootViewComponentId, Constants.TASK_SYSTEMMESSAGE_ERROR, true);
					//ctx.setRollbackOnly();
				}
			} else {
				createTask(userName, "CreateEdition", rootViewComponentId, Constants.TASK_SYSTEMMESSAGE_INFORMATION,
						showMessage);
			}
		} else {
			log.error("Submitted rootViewComponentId has not been found while creating the Edition");
		}
	}

	private void createTask(String user, String why, Integer rootViewComponentId, byte taskType, boolean showMessage) {
		if (log.isDebugEnabled())
			log.debug("createTask: User " + user + " Message " + why + " rootViewComponent " + rootViewComponentId
					+ " taskType " + taskType + " showMessage " + Boolean.toString(showMessage));
		if (showMessage) {
			if (user != null && !user.equals("")) {
				try {
					Integer unitId = this.viewService.getUnit4ViewComponent(rootViewComponentId);
					this.userService.createTask(user, null, unitId, why, taskType);
					log.debug("Send the information-task to the user " + user);
				} catch (Exception exe) {
					log.error("Error occured creating task for user " + user, exe);
				}
			}
		}
	}
	*/

}
