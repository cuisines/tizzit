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
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package de.juwimm.cms.beans;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.tizzit.util.DateConverter;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

import de.juwimm.cms.common.Constants;
import de.juwimm.cms.common.annotation.HourCache;
import de.juwimm.cms.components.model.AddressHbm;
import de.juwimm.cms.components.model.AddressHbmDao;
import de.juwimm.cms.components.model.DepartmentHbm;
import de.juwimm.cms.components.model.DepartmentHbmDao;
import de.juwimm.cms.components.model.PersonHbm;
import de.juwimm.cms.components.model.PersonHbmDao;
import de.juwimm.cms.components.model.TalktimeHbm;
import de.juwimm.cms.components.model.TalktimeHbmDao;
import de.juwimm.cms.components.vo.AddressValue;
import de.juwimm.cms.components.vo.DepartmentValue;
import de.juwimm.cms.components.vo.PersonValue;
import de.juwimm.cms.components.vo.TalktimeValue;
import de.juwimm.cms.exceptions.ResourceNotFoundException;
import de.juwimm.cms.exceptions.UserException;
import de.juwimm.cms.model.ContentHbm;
import de.juwimm.cms.model.ContentHbmDao;
import de.juwimm.cms.model.ContentVersionHbm;
import de.juwimm.cms.model.DocumentHbm;
import de.juwimm.cms.model.DocumentHbmDao;
import de.juwimm.cms.model.HostHbm;
import de.juwimm.cms.model.HostHbmDao;
import de.juwimm.cms.model.PictureHbm;
import de.juwimm.cms.model.PictureHbmDao;
import de.juwimm.cms.model.ShortLinkHbm;
import de.juwimm.cms.model.ShortLinkHbmDao;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.model.UnitHbm;
import de.juwimm.cms.model.UnitHbmDao;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewComponentHbmDao;
import de.juwimm.cms.model.ViewComponentHbmImpl;
import de.juwimm.cms.model.ViewDocumentHbm;
import de.juwimm.cms.model.ViewDocumentHbmDao;
import de.juwimm.cms.remote.ViewServiceSpring;
import de.juwimm.cms.safeguard.realmlogin.SafeguardLoginManager;
import de.juwimm.cms.safeguard.remote.SafeguardServiceSpring;
import de.juwimm.cms.search.beans.SearchengineService;
import de.juwimm.cms.search.vo.XmlSearchValue;
import de.juwimm.cms.vo.ContentValue;
import de.juwimm.cms.vo.PictureValue;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.cms.vo.UnitValue;
import de.juwimm.cms.vo.ViewComponentValue;
import de.juwimm.cms.vo.ViewDocumentValue;

/**
 * @see de.juwimm.cms.remote.WebServiceSpring
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id$
 */

public class WebServiceSpring {
	private static Logger log = Logger.getLogger(WebServiceSpring.class);
	private static final DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private static Pattern patIsIpAddress = Pattern.compile("((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0]?[1-9][0-9]|[0]{0,2}[1-9])\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0]?[1-9][0-9]|[0]{0,2}[1-9]))|localhost");

	@Autowired
	private SearchengineService searchengineService;
	@Autowired
	private UnitHbmDao unitHbmDao;
	@Autowired
	private PersonHbmDao personHbmDao;
	@Autowired
	private ViewComponentHbmDao viewComponentHbmDao;
	@Autowired
	private ContentHbmDao contentHbmDao;
	@Autowired
	private DocumentHbmDao documentHbmDao;
	@Autowired
	private HostHbmDao hostHbmDao;
	@Autowired
	private SiteHbmDao siteHbmDao;
	@Autowired
	private ViewDocumentHbmDao viewDocumentHbmDao;
	@Autowired
	private AddressHbmDao addressHbmDao;
	@Autowired
	private DepartmentHbmDao departmentHbmDao;
	@Autowired
	private PictureHbmDao pictureHbmDao;
	@Autowired
	private TalktimeHbmDao talktimeHbmDao;
	@Autowired
	private ViewServiceSpring viewServiceSpring;
	@Autowired
	private SafeguardServiceSpring safeguardServiceSpring;
	@Autowired
	private ShortLinkHbmDao shortLinkHbmDao;

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getModifiedDate4Cache(java.lang.Integer)
	 */

	public Date getModifiedDate4Cache(Integer viewComponentId) throws Exception {
		try {
			if (log.isDebugEnabled()) log.debug("Start getModifiedDate4Cache");
			ViewComponentHbm vc = viewComponentHbmDao.load(viewComponentId);
			if (vc == null) throw new UserException("Error loading ViewComponent " + viewComponentId.toString() + "!");
			long modlong = viewComponentHbmDao.getPageModifiedDate(vc);
			if (log.isDebugEnabled()) log.debug("End getModifiedDate4Cache");
			return new Date(modlong);
		} catch (Exception e) {
			log.warn("Error getting ModifiedDate4Cache for ViewComponent " + viewComponentId.toString() + ": " + e.getMessage());
			throw new UserException("Error getting ModifiedDate4Cache for ViewComponent " + viewComponentId.toString() + ": " + e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpringBase#getModifiedDate4Info(java.lang.Integer)
	 */

	public Date getModifiedDate4Info(Integer viewComponentId) throws Exception {
		try {
			if (log.isDebugEnabled()) log.debug("Start getModifiedDate4Info");
			ViewComponentHbm vc = viewComponentHbmDao.load(viewComponentId);
			if (vc == null) throw new UserException("Error loading ViewComponent " + viewComponentId.toString() + "!");
			return new Date(vc.getUserLastModifiedDate());
		} catch (Exception e) {
			log.warn("Error getting ModifiedDate4Info for ViewComponent " + viewComponentId.toString() + ": " + e.getMessage());
			throw new UserException("Error getting ModifiedDate4Info for ViewComponent " + viewComponentId.toString() + ": " + e.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getMembersList(java.lang.Integer,
	 *      java.lang.Integer, java.lang.String, java.lang.String)
	 */
	//@RequestMapping(value = "/memberlist/{siteId}/{unitId}/{firstname}/{lastname}", method = RequestMethod.GET)
	public String getMembersList(Integer siteId, Integer unitId, String firstname, String lastname) throws Exception {
		StringBuffer sbPersons = new StringBuffer();
		sbPersons.append("<membersList>");
		try {
			if (siteId == null) throw new IllegalArgumentException("SiteId has to be specified to call getMembersList!");

			if (firstname.equalsIgnoreCase("%") && lastname.equalsIgnoreCase("%")) {
				// find all for anywhat
				if (unitId == null) {
					if (log.isDebugEnabled()) log.debug("FindAllPerSite " + siteId);
					Iterator<PersonHbm> personsIterator = null;
					try {
						Collection<PersonHbm> coll = personHbmDao.findAll(siteId);
						personsIterator = coll.iterator();
					} catch (Exception exe) {
					}
					if (personsIterator != null) {
						while (personsIterator.hasNext()) {
							appendPerson(siteId, personsIterator.next(), sbPersons);
						}
					}
				} else {
					if (log.isDebugEnabled()) log.debug("FindAllPerUnit " + unitId);
					try {
						UnitHbm unit = unitHbmDao.load(unitId);
						if (unit.getSite().getSiteId().intValue() == siteId.intValue()) {
							Collection<PersonHbm> persons = personHbmDao.findByUnit(unit.getUnitId());
							for (PersonHbm personHbm : persons) {
								appendPerson(siteId, personHbm, sbPersons);
							}
						}
					} catch (Exception exe) {
					}
				}
			} else {
				// find one specific
				if (unitId == null) {
					// in all units
					if (log.isDebugEnabled()) log.debug("FindSpecificPerSite " + siteId + " firstname:" + firstname + " lastname:" + lastname);
					Iterator<PersonHbm> personsIterator = null;
					try {
						Collection<PersonHbm> personList = personHbmDao.findByName(siteId, firstname, lastname);
						personsIterator = personList.iterator();
					} catch (Exception exe) {
					}
					if (personsIterator != null) {
						while (personsIterator.hasNext()) {
							Object p = personsIterator.next();
							appendPerson(siteId, (PersonHbm) p, sbPersons);
						}
					}
				} else {
					// in a specific unit
					if (log.isDebugEnabled()) {
						log.debug("FindSpecificPerUnit " + unitId + " firstname:" + firstname + " lastname:" + lastname);
					}
					Iterator<PersonHbm> personsIterator = null;
					try {
						Collection<PersonHbm> personList = personHbmDao.findByNameAndUnit(unitId, firstname, lastname);
						personsIterator = personList.iterator();
					} catch (Exception exe) {
					}
					if (personsIterator != null) {
						while (personsIterator.hasNext()) {
							PersonHbm p = personsIterator.next();
							appendPerson(siteId, p, sbPersons);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error in getMembersList: ", e);
			throw new UserException(e.getMessage());
		}
		sbPersons.append("</membersList>");
		return sbPersons.toString();
	}

	private void appendPerson(Integer siteId, PersonHbm person, StringBuffer sbPersons) {
		String rest = "</person>";
		String personXML = person.toXmlRecursive(0);
		int pidx = personXML.indexOf(rest);
		sbPersons.append(personXML.substring(0, pidx - 1));

		for (Iterator it = person.getUnits().iterator(); it.hasNext();) {
			UnitHbm unit = (UnitHbm) it.next();
			if (unit.getSite().getSiteId().intValue() == siteId.intValue()) {
				sbPersons.append(unit.toXml(0));
				break;
			}
		}
		sbPersons.append(rest);
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getNavigationXml(java.lang.Integer,
	 *      java.lang.String, int, boolean)
	 */
	//@RequestMapping(value = "/navigationxml/{refVcId}/{since}/{depth}/{getPUBLSVersion}", method = RequestMethod.GET)
	public String getNavigationXml(Integer refVcId, String since, int depth, boolean getPUBLSVersion) throws Exception {
		if (log.isDebugEnabled()) log.debug("getNavigationXML start");
		try {
			String retVal = "";
			Integer vclpk = this.navigationViewComponentSolver(refVcId, since, getPUBLSVersion);
			if (vclpk != null) {
				if (log.isDebugEnabled()) log.debug("starting at: " + vclpk);
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				PrintStream out = new PrintStream(byteOut, true, "UTF-8");
				ViewComponentHbm vcl = viewComponentHbmDao.load(vclpk);
				viewComponentHbmDao.toXml(vcl, null, false, true, depth, getPUBLSVersion, true, out);
				retVal = byteOut.toString("UTF-8");
			}
			return retVal;
		} catch (Exception e) {
			log.error("ERROR GET NAVIGATION XML ERROR " + e.getMessage());
			throw new UserException();
		}
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getNavigationAge(java.lang.Integer,
	 *      java.lang.String, int, boolean)
	 */
	//@RequestMapping(value = "/navigationage/{refVcID}/{since}/{depth}/{getPUBLSVersion}", method = RequestMethod.GET)
	public Date getNavigationAge(Integer refVcId, String since, int depth, boolean getPUBLSVersion) throws Exception {
		return viewComponentHbmDao.getNavigationAge(refVcId, since, depth, getPUBLSVersion);
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getNavigationBackwardXml(java.lang.Integer,
	 *      java.lang.String, int, boolean)
	 */
	//@RequestMapping(value = "/navigationbackwardxml/{refVcId}/{since}/{dontShowFirst}/{getPUBLSVersion}", method = RequestMethod.GET)
	public String getNavigationBackwardXml(Integer refVcId, String since, int dontShowFirst, boolean getPUBLSVersion) throws Exception {
		if (log.isDebugEnabled()) log.debug("getNavigationBackwardXML start");

		try {
			StringBuilder sb = new StringBuilder();
			ViewComponentHbm vcl = null;
			ViewComponentHbm sinceVcl = null;
			if (since.equalsIgnoreCase("root")) { // means root from the
				// ViewDocument
				//
				if (log.isDebugEnabled()) log.debug("means root from the ViewDocument");
				vcl = viewComponentHbmDao.load(refVcId);
				sinceVcl = vcl.getViewDocument().getViewComponent();
			} else {
				if (log.isDebugEnabled()) log.debug("lastNavigationRoot");
				vcl = viewComponentHbmDao.load(refVcId);
				ViewComponentHbm viewComp = vcl.getNavigationRoot();
				sinceVcl = new ViewComponentHbmImpl();
				Integer viewCompId = viewComp.getViewComponentId();
				sinceVcl.setViewComponentId(viewCompId);
			}

			ArrayList<ViewComponentHbm> al = new ArrayList<ViewComponentHbm>();
			while (true) {
				al.add(0, vcl);
				if (vcl == null || sinceVcl == null || vcl.getViewComponentId().equals(sinceVcl.getViewComponentId())) {
					break;
				}
				vcl = vcl.getParent();
			}
			for (int i = 0; i < dontShowFirst; i++) {
				if (al.size() > 0) {
					al.remove(0);
				}
			}
			Iterator it = al.iterator();
			while (it.hasNext()) {
				ViewComponentHbm aaa = (ViewComponentHbm) it.next();
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				PrintStream out = new PrintStream(byteOut, true, "UTF-8");

				viewComponentHbmDao.toXml(aaa, null, false, true, 1, getPUBLSVersion, true, out);

				/*
				 * Integer pk = aaa.getViewComponentId(); ViewComponentHbmHelper
				 * help = new ViewComponentHbmHelper(tpl); help.toXml(null,
				 * false, true, 1, 0, getPUBLSVersion, true, out, pk);
				 */
				sb.append(byteOut.toString("UTF-8"));
			}

			if (log.isDebugEnabled()) {
				log.debug("CONTENT: " + sb.toString());
				log.debug("LEAVING GOT NAVIGATION BACKWARD XML");
			}
			return sb.toString();
		} catch (Exception ex) {
			log.error("Error getting navigation Backward", ex);
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getContent(java.lang.Integer,
	 *      boolean)
	 */
	//@RequestMapping(value = "/content/{vcId}/{getPUBLSVersion}", method = RequestMethod.GET)
	public String getContent(Integer vcId, boolean getPUBLSVersion) throws Exception {
		if (log.isDebugEnabled()) log.debug("getting content at " + sdf.format(new Date()) + "for vcId " + vcId);
		StringBuilder sb = new StringBuilder();
		ViewComponentHbm view = null;
		ContentHbm contentHbm = null;
		try {
			view = viewComponentHbmDao.load(vcId);
		} catch (Exception e) {
			log.error("ViewComponent " + vcId + " could not be found: " + e.getMessage());
			throw new UserException("ViewComponent " + vcId + " could not be found: " + e.getMessage());
		}
		try {
			if (view.getViewType() == Constants.VIEW_TYPE_SYMLINK) {
				ViewComponentHbm vclHibSym = viewComponentHbmDao.load(Integer.valueOf(view.getReference()));
				Integer ref = Integer.valueOf(vclHibSym.getReference());
				contentHbm = contentHbmDao.load(ref);
			} else {
				String ref = view.getReference();
				Integer refInt = Integer.valueOf(ref);
				contentHbm = contentHbmDao.load(refInt);
			}
		} catch (Exception e) {
			log.error("Referenced Content " + view.getReference() + " for ViewComponent " + vcId + " could not be found: " + e.getMessage());
			throw new UserException("Referenced Content " + view.getReference() + " for ViewComponent " + vcId + " could not be found: " + e.getMessage());
		}
		try {
			if (getPUBLSVersion) {
				try {
					sb.append(contentHbm.getContentVersionForPublish().getText());
				} catch (Exception eee) {
					log.error("For the ViewComponent " + vcId + " has no Content been found - we are searching for LIVESERVER CONTENT. Maybe this content was never been approved?");
				}
			} else {
				try {
					ContentVersionHbm contVers = contentHbm.getLastContentVersion();
					String text = contVers.getText();
					sb.append(text);
				} catch (Exception eee) {
					log.error("For the ViewComponent " + vcId + " has no Content been found - we are searching for WORKSERVER CONTENT. Maybe this is a Live Server?");
				}
			}
		} catch (Exception e) {
			log.error("Content could not be resolved for VC " + vcId + ": " + e.getMessage());
			throw new UserException(e.getMessage());
		}
		return sb.toString();
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getDocument(java.lang.Integer)
	 */
	////@RequestMapping(value="/document/{documentId}", method=RequestMethod.GET)
	public byte[] getDocument(Integer documentId) throws Exception {
		byte[] ret = null;
		try {
			DocumentHbm docHbm = documentHbmDao.load(documentId);

			ret = documentHbmDao.getDocumentContent(documentId);
			//IOUtils.toByteArray(docHbm.getDocument().getBinaryStream());
		} catch (Exception e) {
			log.warn("Error getting Document " + documentId + ": " + e.getMessage());
		}
		return ret;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getUnit(java.lang.Integer)
	 */

	public UnitValue getUnit(Integer unitId) throws Exception {
		try {
			UnitHbm unit = unitHbmDao.load(unitId);
			return unitHbmDao.getDao(unit);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
	}

	private boolean isIpAddress(String adr) {
		return patIsIpAddress.matcher(adr).matches();
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getMandatorDir(java.lang.String)
	 */
	//@RequestMapping(value = "/mandatordir/{hostName}", method = RequestMethod.GET)
	@HourCache
	public String getMandatorDir(String hostName) throws Exception {
		try {
			if ((hostName == null || this.isIpAddress(hostName)) || ("".equalsIgnoreCase(hostName))) {
				return ("");
			}
			HostHbm hl = null;
			try {
				hl = hostHbmDao.load(hostName);
			} catch (Exception e) {
				if (log.isDebugEnabled()) log.debug("Could not find Host with name " + hostName, e);
			}
			if (hl == null) {
				if (log.isInfoEnabled()) log.info("\"" + hostName + "\" is not configured in Hostmanagement");
				return "";
			}
			SiteHbm sl = hl.getSite();
			if (sl != null) {
				String manDir = sl.getMandatorDir();
				if (manDir == null) {
					manDir = "";
				}
				return (manDir);
			}
			return ("");
		} catch (Exception e) {
			throw new UserException("unknown exception resolving host - " + hostName, e);
		}
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpringBase#handleResolveShortLink(java.lang.String,
	 *      java.lang.String)
	 */
	@HourCache
	public String resolveShortLink(String hostName, String requestPath) throws Exception {
		try {
			if ((hostName == null || this.isIpAddress(hostName)) || ("".equalsIgnoreCase(hostName))) {
				return ("");
			}
			HostHbm host = null;
			try {
				host = hostHbmDao.load(hostName);
			} catch (Exception e) {
				if (log.isDebugEnabled()) log.debug("Could not find Host with name " + hostName, e);
			}
			if (host == null) {
				if (log.isInfoEnabled()) log.info("\"" + hostName + "\" is not configured in Hostmanagement");
				return "";
			}
			SiteHbm site = host.getSite();
			if (site != null) {
				ShortLinkHbm shortlink = null;
				try {
					if (requestPath != null) {
						if (requestPath.startsWith("/")) requestPath = requestPath.substring(1, requestPath.length());
						shortlink = shortLinkHbmDao.findByShortLink(requestPath, site.getSiteId());
					}
				} catch (Exception e) {
					if (log.isDebugEnabled()) log.debug("No ShortLink found for \"" + requestPath + "\": " + e.getMessage(), e);
				}
				if (shortlink != null) {
					String redirectUrl = shortlink.getRedirectUrl();
					if (redirectUrl != null) {
						int startIndex = redirectUrl.indexOf("{");
						int endIndex = redirectUrl.indexOf("}");
						if (startIndex >= 0 && endIndex > startIndex) {
							// placeholder has to be substituted
							String placeHolder = redirectUrl.substring(startIndex + 1, endIndex);
							if (log.isDebugEnabled()) log.debug("PlaceHolder: " + placeHolder);
							String substitute = null;
							if ("unitName".equalsIgnoreCase(placeHolder) && host.getUnit() != null) {
								substitute = host.getUnit().getName().trim();
							} else {
								// replace placeholder by empty string -> delete
								// placeholder
								substitute = "";
							}
							// replace placeholder by substitute ;-)
							// perhaps use String.replaceAll if placeholder
							// occurs multiple times ?
							redirectUrl = redirectUrl.substring(0, startIndex) + substitute + redirectUrl.substring(endIndex + 1, redirectUrl.length());
						} else {
							if (log.isDebugEnabled()) log.debug("No (reasonable) placeholder to substitute found");
						}
						if (shortlink.getViewDocument() != null) {
							redirectUrl = "/" + shortlink.getViewDocument().getLanguage() + "/" + redirectUrl;
						} else {
							redirectUrl = "/" + redirectUrl;
						}
						if (log.isDebugEnabled()) log.debug("Redirect-Url: " + redirectUrl);
						return redirectUrl;
					}
					if (log.isDebugEnabled()) log.debug("No redirectUrl found for \"" + shortlink.getShortLink());
				}
			}
			return ("");
		} catch (Exception e) {
			throw new UserException("unknown exception resolving host - " + hostName, e);
		}
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getStartPage(java.lang.String)
	 */
	@HourCache
	public String getStartPage(String hostName) throws Exception {
		if (log.isDebugEnabled()) log.debug("GET START PAGE");

		try {
			if ((hostName == null) || ("".equalsIgnoreCase(hostName))) {
				return ("");
			}
			HostHbm hl = null;
			try {
				hl = hostHbmDao.load(hostName);
			} catch (Exception e) {
				log.error("getStartPage " + e.getMessage());
			}
			if (hl == null) {
				log.warn("host \"" + hostName + "\" not found");
				return ("");
			}
			ViewComponentHbm vcl = null;
			vcl = hl.getStartPage();
			if (vcl != null) {
				StringBuffer startPagePath = new StringBuffer();
				startPagePath.append(vcl.getViewDocument().getLanguage());
				startPagePath.append("/");
				String path = vcl.getPath();

				if (path != null && !"".equalsIgnoreCase(path)) {
					startPagePath.append(path).append("/");
				}
				return (startPagePath.toString());
			}
			return ("");
		} catch (Exception e) {
			throw new UserException("unknown host - " + hostName);
		}
	}

	public Integer getSiteForHost(String hostName) throws Exception {
		Integer siteId = null;
		if ((!("".equalsIgnoreCase(hostName)) || (hostName != null))) {
			try {
				String hostId = "";
				hostId = getSite(hostName);
				if (log.isDebugEnabled()) {
					log.debug("found host: " + hostId);
				}
				if (hostId != null) {
					if (!("".equalsIgnoreCase(hostId))) {
						siteId = Integer.valueOf(hostId);
					}
				}
			} catch (Exception e) {
				throw new UserException(e.getMessage());
			}
		}
		return (siteId);
	}

	public SiteValue getSiteValueForHost(String hostName) throws Exception {
		if ((!("".equalsIgnoreCase(hostName)) || (hostName != null))) {

			HostHbm hl = null;
			try {
				hl = hostHbmDao.load(hostName);
			} catch (Exception e) {
				throw new UserException("Host \"" + hostName + "\" not found!\n" + e.getMessage());
			}
			if (hl == null) {
				if (log.isDebugEnabled()) log.debug("Host with name \"" + hostName + "\" not found");
				return null;
			}
			SiteHbm sl = hl.getSite();
			if (sl != null) {
				return sl.getSiteValue();
			}
		}
		return null;
	}

	//
	//	public SiteValue getSiteForHost(Integer siteId) throws Exception {
	//		SiteHbm site = this.siteHbmDao.load(siteId);
	//		if (site != null) {
	//			return site.getSiteValue();
	//		} else {
	//			return null;
	//		}
	//	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getAllSites()
	 */

	public Collection getAllSites() throws Exception {
		Collection<SiteValue> sitesList = new ArrayList<SiteValue>();
		try {
			Collection<SiteHbm> result = siteHbmDao.loadAll();
			Iterator<SiteHbm> sitesIterator = result.iterator();
			while (sitesIterator.hasNext()) {
				SiteValue siteValue = sitesIterator.next().getSiteValue();
				sitesList.add(siteValue);
			}
		} catch (Exception e) {
			log.warn("ERROR GET ALL SITES: " + e.getMessage());
			throw new UserException(e.getMessage());
		}
		return sitesList;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getViewComponent4Id(java.lang.Integer)
	 */

	public ViewComponentValue getViewComponent4Id(Integer viewComponentId) throws Exception {
		ViewComponentValue vo = null;
		try {
			ViewComponentHbm vc = viewComponentHbmDao.load(viewComponentId);
			vo = vc.getDao();
		} catch (Exception e) {
			throw new UserException("Error getting ViewComponent for id " + viewComponentId + "\n" + e);
		}
		if (log.isDebugEnabled()) log.debug("LEAVING GET VIEWCOMPONENT 4 ID");
		return vo;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getSite4VCId(java.lang.Integer)
	 */

	public SiteValue getSite4VCId(Integer viewComponentId) throws Exception {
		SiteValue vo = null;
		try {
			if (viewComponentId != null) {
				ViewComponentHbm vc = viewComponentHbmDao.load(viewComponentId);

				SiteHbm site = vc.getViewDocument().getSite();
				vo = site.getSiteValue();
			}
		} catch (Exception e) {
			throw new UserException("getSite4VCId - Unknown error occured", e);
		}

		return vo;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getNavigationRootDistance4VCId(java.lang.Integer)
	 */

	public int getNavigationRootDistance4VCId(Integer viewComponentId) throws Exception {
		if (log.isDebugEnabled()) log.debug("getNavigationRootDistance4VCId start");
		try {
			ViewComponentHbm vc = viewComponentHbmDao.load(viewComponentId);
			int dist = vc.getNavigationRootDistance();
			if (log.isDebugEnabled()) log.debug("Distance " + dist);
			return dist;
		} catch (Exception ex) {
			log.warn("ERROR GET NAVIGATION ROOT DISTANCE 4 VC ID " + ex.getMessage());
			throw new UserException("Error getting NavigationRootDistance for id " + viewComponentId + "\n" + ex.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getPath4Unit(java.lang.Integer,
	 *      java.lang.Integer)
	 */

	public String getPath4Unit(Integer unitId, Integer viewDocumentId) throws Exception {
		try {
			ViewComponentHbm viewComponent = viewComponentHbmDao.find4Unit(unitId, viewDocumentId);
			return viewComponent == null ? null : viewComponent.getPath();
		} catch (Exception e) {
			log.warn("Error getting path for unit " + unitId + " and viewdocument " + viewDocumentId + ": ", e);
		}
		return null;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getViewDocument4ViewComponentId(java.lang.Integer)
	 */

	public ViewDocumentValue getViewDocument4ViewComponentId(Integer viewComponentId) throws Exception {
		try {
			ViewComponentHbm vc = viewComponentHbmDao.load(viewComponentId);
			ViewDocumentHbm vd = vc.getViewDocument();
			if (log.isDebugEnabled()) log.debug("LEAVING GET VIEW DOCUMENT 4 VIEW COMPONENT ID");
			return vd.getDao();
		} catch (Exception e) {
			throw new UserException("Error getting ViewDocument for ViewComponentId " + viewComponentId + "\n" + e);
		}
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getViewDocuments4Site(java.lang.Integer)
	 */

	public ViewDocumentValue[] getViewDocuments4Site(Integer siteId) throws Exception {
		ViewDocumentValue[] retArr = null;
		try {
			Collection<ViewDocumentHbm> coll = viewDocumentHbmDao.findAll(siteId);

			retArr = new ViewDocumentValue[coll.size()];
			Iterator it = coll.iterator();
			int counter = 0;

			while (it.hasNext()) {
				ViewDocumentHbm view = (ViewDocumentHbm) it.next();
				retArr[counter] = view.getDao();
				counter++;
			}

		} catch (Exception ex) {
			log.warn("ERROR GET VIEW DOCUMENTS 4 SITE " + ex.getMessage());
		}
		return retArr;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getPath4ViewComponent(java.lang.Integer)
	 */

	public String getPath4ViewComponent(Integer viewComponentId) throws Exception {
		if (log.isDebugEnabled()) log.debug("GET PATH 4 VIEW COMPONENT");
		try {
			ViewComponentHbm vc = viewComponentHbmDao.load(viewComponentId);
			if (log.isDebugEnabled()) log.debug("LEAVING GET PATH 4 VIEW COMPONENT");
			return vc.getPath();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getPerson(java.lang.Long)
	 */

	public PersonValue getPerson(Long personId) throws Exception {
		if (log.isDebugEnabled()) log.debug("getPerson start");
		try {
			PersonHbm per = personHbmDao.load(personId);
			return per.getDao(1);
		} catch (Exception e) {
			log.warn("Error getting Person " + personId + ": " + e.getMessage());
		}
		return null;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getDepartment(java.lang.Long)
	 */

	public DepartmentValue getDepartment(Long departmentId) throws Exception {
		DepartmentValue value = null;
		try {
			DepartmentHbm dep = departmentHbmDao.load(departmentId);
			value = dep.getDao(1);
		} catch (Exception e) {
			log.warn("Error getting Department " + departmentId + ": " + e.getMessage());
		}
		return value;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getAddress(java.lang.Long)
	 */

	public AddressValue getAddress(Long addressId) throws Exception {
		AddressValue value = null;
		try {
			AddressHbm adr = addressHbmDao.load(addressId);
			value = adr.getData();
		} catch (Exception e) {
			log.warn("Error getting Address " + addressId + ": " + e.getMessage());
		}
		return value;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getTalktime(java.lang.Long)
	 */

	public TalktimeValue getTalktime(Long talktimeId) throws Exception {
		TalktimeValue value = null;
		try {
			TalktimeHbm talk = talktimeHbmDao.load(talktimeId);
			value = talk.getData();
		} catch (Exception e) {
			log.warn("Error getting TalkTime " + talktimeId + ": " + e.getMessage());
		}
		return value;
	}
 
	public Map getSitemapParameters(Integer viewComponentId, Integer siteId, String language, String path, String viewType, String safeguardUsername, String safeguardPassword, Map safeguardMap) throws Exception {
		boolean needSafeguard = false;
		Map<String, String> sitemapParams = new HashMap<String, String>();

		if (viewComponentId != null) {
			String templateName = this.getContentTemplateName(viewComponentId);
			sitemapParams.put("viewComponentId", viewComponentId.toString());
			sitemapParams.put("template", templateName);
		} else {
			boolean redirect = false;
			String templateName = "";
			{
				// TODO this is NOT the final solution!
				GrantedAuthority[] mockAuthorities = {new GrantedAuthorityImpl("*")};
				Authentication mockUser = new UsernamePasswordAuthenticationToken("system", "pw", mockAuthorities);
				SecurityContextHolder.getContext().setAuthentication(mockUser);
			}
			if (language == null || language.equalsIgnoreCase("")) {
				// Fall: / (Startseite wird aufgerufen)
				ViewDocumentValue vdd = viewServiceSpring.getDefaultViewDocument4Site(siteId);
				viewComponentId = vdd.getViewId();
				viewType = vdd.getViewType();
				language = vdd.getLanguage();
				path = "";
			} else if (path == null || path.equalsIgnoreCase("")) {
				// Fall: /deutsch/ (Startseite der Language wird aufgerufen)
				// kann auch die Shortlinks auflösen
				if (viewType.endsWith("/")) {
					viewType = viewType.substring(0, viewType.length() - 1);
				}
				String[] retArr = viewServiceSpring.getViewComponentForLanguageOrShortlink(viewType, language, siteId);
				viewComponentId = new Integer(retArr[0]);
				path = retArr[1];
				language = retArr[2];
				byte viewComponentIype = Byte.parseByte(retArr[3]);
				if (viewComponentIype == Constants.VIEW_TYPE_INTERNAL_LINK) {
					redirect = true;
				}
			} else {
				// Fall: /deutsch/joekel/
				viewComponentId = viewServiceSpring.getViewComponentId4PathWithViewTypeAndLanguage(path, viewType, language, siteId);
			}
			if (viewComponentId == null) {
				if ("favicon.ico".equals(path)) {
					return null;
				}
				throw new ResourceNotFoundException("Could not read resource: " + path);
			}
			try {
				if (log.isDebugEnabled()) log.debug("found viewComponentId " + viewComponentId + " for path");
				sitemapParams.put("currentDate", DateConverter.getSql2String(new Date(System.currentTimeMillis())));
				templateName = this.getContentTemplateName(viewComponentId);
				sitemapParams.put("viewComponentId", viewComponentId.toString());
				sitemapParams.put("template", templateName);
				sitemapParams.put("language", language);
				sitemapParams.put("viewType", viewType);
				sitemapParams.put("redirect", Boolean.toString(redirect));
				// path = URLEncoder.encode(path, "ISO-8859-1");
				// path = path.replaceAll("[+]", "%20"); // replace "+" by %20
				// Spaces
				sitemapParams.put("path", path);
			} catch (Exception e) {
				throw new UserException("Error getting sitemap-parameters", e);
			}
		}
		if (safeguardUsername != null) {
			if (log.isDebugEnabled()) log.debug("logging in safeguard user: " + safeguardUsername);
			byte login = safeguardServiceSpring.login(safeguardUsername, safeguardPassword, viewComponentId);
			if (login == SafeguardLoginManager.LOGIN_SUCCESSFULLY) {
				if (log.isDebugEnabled()) log.debug("Login of safeguard user successfully");
				String[] realmKeys = safeguardServiceSpring.getRoles4UserAndRealm(safeguardUsername, safeguardPassword, viewComponentId);
				if (realmKeys != null) {
					for (int i = (realmKeys.length - 1); i >= 0; i--)
						safeguardMap.put(realmKeys[i], Boolean.TRUE);
				}
			}
			sitemapParams.put("login", Byte.toString(login));
		}
		needSafeguard = safeguardServiceSpring.isSafeguardAuthenticationNeeded(viewComponentId, safeguardMap);

		sitemapParams.put("safeguard", new Boolean(needSafeguard).toString());
		if (needSafeguard) {
			String loginpage = this.getSafeguardLoginPath(viewComponentId);
			if (log.isDebugEnabled()) log.debug("Safeguard needed; rediecting to login page: " + loginpage);
			// loginpage = URLEncoder.encode(loginpage,
			// "ISO-8859-1").replaceAll("[+]", "%20"); // replace "+" by %20
			// Spaces
			sitemapParams.put("redirecttologin", loginpage);
		}

		if (log.isDebugEnabled()) log.debug("getSitemapParameters end " + map2string(sitemapParams));
		return sitemapParams;
	}
	/**
	 * @see de.juwimm.cms.remote.WebServiceSpringBase#getSitemapParameters(java.util.Map,
	 *      java.util.Map)
	 * @deprecated use new getSitemapParameters now
	 */
	public Map getSitemapParameters(Map parameterMap, Map safeguardMap) throws Exception {
		if (log.isDebugEnabled()) log.debug("getSitemapParameters start");
		Integer viewComponentId = null;
		String language = null;
		Integer siteId = null;
		String path = null;
		String viewType = null;
		String safeguardUsername = null;
		String safeguardPassword = null;

		try {
			String vcId = (String) parameterMap.get("viewComponentId");
			if (vcId != null && !"".equalsIgnoreCase(vcId)) viewComponentId = Integer.valueOf(vcId);
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("Could not parse viewComponentId: " + e.getMessage());
		}
		try {
			String sId = (String) parameterMap.get("siteId");
			if (sId != null && !"".equalsIgnoreCase(sId)) siteId = Integer.valueOf(sId);
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("Could not parse siteId: " + e.getMessage());
		}
		{
			// values may be null or are for sure not null
			language = (String) parameterMap.get("language");
			path = (String) parameterMap.get("path");
			viewType = (String) parameterMap.get("viewType");
			safeguardUsername = (String) parameterMap.get("safeguardUsername");
			safeguardPassword = (String) parameterMap.get("safeguardPassword");
			if (safeguardPassword == null) safeguardPassword = "";
		}

		return getSitemapParameters(viewComponentId, siteId, language, path, viewType, safeguardUsername, safeguardPassword, safeguardMap);
	}

	private String map2string(Map m) {
		StringBuffer sb = new StringBuffer();
		Set keySet = m.keySet();
		for (Object object : keySet) {
			Object val = m.get(object);
			String vs = (val == null) ? "" : val.toString();
			sb.append(object.toString() + ":" + vs + " ");
		}
		return sb.toString();
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getThumbnail(java.lang.Integer)
	 */

	public byte[] getThumbnail(Integer pictureId) throws Exception {
		byte[] ret = null;
		try {
			PictureHbm pic = pictureHbmDao.load(pictureId);
			ret = pic.getThumbnail();
		} catch (Exception ex) {
			log.warn("Error getting Thumbnail: " + ex.getMessage());
			throw new Exception(ex.getMessage());
		}
		return ret;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpringBase#getPreview(java.lang.Integer)
	 */

	public byte[] getPreview(Integer pictureId) throws Exception {
		byte[] ret = null;
		try {
			PictureHbm pic = pictureHbmDao.load(pictureId);
			if (pic.getPreview() != null && pic.getPreview().length > 0) {
				ret = pic.getPreview();
			} else {
				ret = pic.getThumbnail();
			}
		} catch (Exception ex) {
			log.warn("Error getting Preview: " + ex.getMessage());
			throw new Exception(ex.getMessage());
		}
		return ret;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getPicture(java.lang.Integer)
	 */

	public byte[] getPicture(Integer pictureId) throws Exception {
		byte[] ret = null;
		try {
			PictureHbm pic = pictureHbmDao.load(pictureId);
			ret = pic.getPicture();
		} catch (Exception e) {
			log.warn("Error getting Picture " + pictureId + ": " + e.getMessage());
		}
		return ret;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getTimestamp4Picture(java.lang.Integer)
	 */

	public Long getTimestamp4Picture(Integer pictureId) throws Exception {
		try {
			PictureHbm pic = pictureHbmDao.load(pictureId);
			return pic.getTimeStamp();
		} catch (Exception ex) {
			log.warn("Error getting Timestamp for Picture " + pictureId + ": " + ex.getMessage());
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getMimetype4Picture(java.lang.Integer)
	 */

	public String getMimetype4Picture(Integer pictureId) throws Exception {
		try {
			PictureHbm pic = pictureHbmDao.load(pictureId);
			return pic.getMimeType();
		} catch (Exception ex) {
			log.warn("Error getting Mimetype for Picture " + pictureId + ": " + ex.getMessage());
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getDocumentName(java.lang.Integer)
	 */
	//@RequestMapping(value = "/documentname/{documentId}", method = RequestMethod.GET)
	public String getDocumentName(Integer documentId) throws Exception {
		String ret = null;
		try {
			DocumentHbm docHbm = documentHbmDao.load(documentId);
			ret = docHbm.getDocumentName();
		} catch (Exception e) {
			log.warn("Error getting DocumentName for Document " + documentId + ": " + e.getMessage());
		}
		return ret;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getMimetype4Document(java.lang.Integer)
	 */
	//@RequestMapping(value = "/mimetypefordocument/{documentId}", method = RequestMethod.GET)
	public String getMimetype4Document(Integer documentId) throws Exception {
		String ret = null;
		try {
			DocumentHbm docHbm = documentHbmDao.load(documentId);
			ret = docHbm.getMimeType();
		} catch (Exception e) {
			log.warn("Error getting Mimetype for Document " + documentId + ": " + e.getMessage());
		}
		return ret;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getTimestamp4Document(java.lang.Integer)
	 */

	public Long getTimestamp4Document(Integer documentId) throws Exception {
		Long ret = null;
		try {
			DocumentHbm docHbm = documentHbmDao.load(documentId);
			ret = docHbm.getTimeStamp();
		} catch (Exception e) {
			log.warn("Error getting Timestamp for Document " + documentId + ": " + e.getMessage());
		}
		return ret;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getSafeguardLoginPath(java.lang.Integer)
	 */
	//@RequestMapping(value = "/safegueardloginpath/{viewComponentId}", method = RequestMethod.GET)
	public String getSafeguardLoginPath(Integer viewComponentId) throws Exception {
		String loginPath = null;
		try {
			loginPath = safeguardServiceSpring.getLoginPath(viewComponentId);
		} catch (Exception e) {
			log.warn("Error getting SafeGuard login-path: " + e.getMessage(), e);
		}
		return loginPath;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getSafeguardRealmIdAndType(java.lang.Integer)
	 */
	//@RequestMapping(value = "/safeguardrealmidandtype/{viewComponentId}", method = RequestMethod.GET)
	public String getSafeguardRealmIdAndType(Integer viewComponentId) throws Exception {
		return safeguardServiceSpring.getRealmIdAndType(viewComponentId);
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getViewComponent4Unit(java.lang.Integer,
	 *      java.lang.Integer)
	 */

	public ViewComponentValue getViewComponent4Unit(Integer unitId, Integer viewDocumentId) throws Exception {
		try {
			ViewComponentHbm viewComponent = viewComponentHbmDao.find4Unit(unitId, viewDocumentId);
			if (viewComponent != null) return viewComponent.getViewComponentDao();
			// Maybe for this language there is no page for this unit
			return null;
		} catch (Exception e) {
			log.error("Error getting viewComponent for unit " + unitId + " and viewdocument " + viewDocumentId + ": ", e);
		}
		return null;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getContent(java.lang.Integer)
	 */

	public ContentValue getContent(Integer contentId) throws Exception {
		ContentValue contentValue = null;
		try {
			ContentHbm content = contentHbmDao.load(contentId);
			contentValue = content.getDao();
		} catch (Exception e) {
			log.warn("ERROR GET CONTENT 1 PARA " + e.getMessage());
		}
		return contentValue;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getAllUnitsXml(java.lang.Integer)
	 */
	//@RequestMapping(value = "/allunitsxml/{siteId}", method = RequestMethod.GET)
	public String getAllUnitsXml(Integer siteId) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<units>");
		try {
			Collection<UnitHbm> query = unitHbmDao.findBySite(siteId);
			Iterator<UnitHbm> unitsIterator = query.iterator();
			while (unitsIterator.hasNext()) {
				UnitHbm unit = unitsIterator.next();
				sb.append(unit.toXml(0));
			}
		} catch (Exception e) {
			log.error("Error getting all units for site " + siteId + ": ", e);
		}
		sb.append("</units>");
		return sb.toString();
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getAllUnitsXml(java.lang.Integer)
	 */
	//@RequestMapping(value = "/allunitlistxml/{siteId}", method = RequestMethod.GET)
	public String getUnitListXml(Integer siteId) throws Exception {
		StringBuilder sb = new StringBuilder();
		try {
			Collection<UnitHbm> query = unitHbmDao.findBySite(siteId);
			Iterator<UnitHbm> unitsIterator = query.iterator();
			while (unitsIterator.hasNext()) {
				UnitHbm unit = unitsIterator.next();
				sb.append(unit.toXml(0));
			}
		} catch (Exception e) {
			log.error("Error getting all units for site " + siteId + ": ", e);
		}
		return sb.toString();
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#hasPublishContentVersion(java.lang.Integer)
	 */

	public boolean hasPublishContentVersion(Integer viewComponentId) throws Exception {
		boolean has = false;
		try {
			ViewComponentHbm view = viewComponentHbmDao.load(viewComponentId);
			has = viewComponentHbmDao.hasPublishContentVersion(view);
		} catch (Exception ex) {
			log.warn("ERROR HAS PUBLISH CONTENT VERSION " + ex.getMessage());
		}
		return has;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getHeading(java.lang.Integer,
	 *      boolean)
	 */
	//@RequestMapping(value = "/heading/{contentId}/{liveServer}", method = RequestMethod.GET)
	public String getHeading(Integer contentId, boolean liveServer) throws Exception {
		String heading = "";
		ContentHbm content = null;
		try {
			content = contentHbmDao.load(contentId);
			if (content != null) {
				if (liveServer) {
					heading = content.getContentVersionForPublish().getHeading();
				} else {
					heading = content.getLastContentVersion().getHeading();
				}
			}
		} catch (Exception e) {
			log.warn("ERROR GET HEADING " + e.getMessage());
		}
		return heading;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getUnit4ViewComponent(java.lang.Integer)
	 */

	public UnitValue getUnit4ViewComponent(Integer viewComponentId) throws Exception {
		UnitValue value = null;
		try {
			ViewComponentHbm view = viewComponentHbmDao.load(viewComponentId);
			Integer unitId = view.getUnit4ViewComponent();
			value = this.getUnit(unitId);
		} catch (Exception ex) {
			log.warn("ERROR GET UNIT FOR VIEWCOMPONENT " + ex.getMessage());
		}
		return value;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getSite4Unit(java.lang.Integer)
	 */

	public SiteValue getSite4Unit(Integer unitId) throws Exception {
		SiteValue value = null;
		try {
			UnitHbm ul = unitHbmDao.load(unitId);
			value = ul.getSite().getSiteValue();
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		return value;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#isVisibleForLanguageVersion(de.juwimm.cms.vo.ViewComponentValue,
	 *      boolean)
	 */

	public boolean isVisibleForLanguageVersion(ViewComponentValue viewComponentValue, boolean liveServer) throws Exception {
		boolean retVal = true;
		boolean hasPublishContentVersion = false;
		try {
			hasPublishContentVersion = this.hasPublishContentVersion(viewComponentValue.getViewComponentId());
		} catch (Exception e) {
		}

		if ((viewComponentValue.getViewType() == Constants.VIEW_TYPE_CONTENT || viewComponentValue.getViewType() == Constants.VIEW_TYPE_UNIT) && liveServer && !hasPublishContentVersion) retVal = false;

		if (retVal) if (viewComponentValue.getOnlineStart() > 0 && new Date(viewComponentValue.getOnlineStart()).after(new Date(System.currentTimeMillis()))) retVal = false;

		if (retVal) if (viewComponentValue.getOnlineStop() > 0 && new Date(viewComponentValue.getOnlineStop()).before(new Date(System.currentTimeMillis()))) retVal = false;

		return retVal;
	}

	/**
	 * @see de.juwimm.cms.remote.WebServiceSpring#getDefaultLanguage(java.lang.Integer)
	 */
	//@RequestMapping(value = "/defaultLanguage/{siteId}", method = RequestMethod.GET)
	public String getDefaultLanguage(Integer siteId) throws Exception {
		if (log.isDebugEnabled()) log.debug("getDefaultLanguage start");
		String language = "deutsch";
		try {
			SiteHbm site = siteHbmDao.load(siteId);
			ViewDocumentHbm defaultView = site.getDefaultViewDocument();
			language = defaultView.getLanguage();
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("Using hard coded default \"deutsch\"");
			// throw new UserException();
			language = "deutsch";
		}
		if (log.isDebugEnabled()) log.debug("getDefaultLanguage end");
		return language;
	}

	private String getContentTemplateName(Integer viewComponentId) throws UserException {
		ContentHbm content = null;
		ViewComponentHbm view = null;
		try {
			view = viewComponentHbmDao.load(viewComponentId);
			if (view.getViewType() == Constants.VIEW_TYPE_INTERNAL_LINK || view.getViewType() == Constants.VIEW_TYPE_SYMLINK) {
				String ref = view.getReference();
				view = viewComponentHbmDao.load(new Integer(ref));
				content = contentHbmDao.load(new Integer(view.getReference()));
			} else {
				content = contentHbmDao.load(new Integer(view.getReference()));
			}

			return content.getTemplate();
		} catch (Exception e) {
			throw new UserException("Could not find referenced ContentVersion with Id: " + view.getReference() + " vcid:" + viewComponentId + "\n" + e.getMessage());
		}
	}

	private Integer navigationViewComponentSolver(Integer refVcId, String since, boolean getPUBLSVersion) throws Exception {
		if (log.isDebugEnabled()) log.debug("begin navigationViewComponentSolver");
		ViewComponentHbm viewComponent = null;

		if (since.equalsIgnoreCase("me")) {
			viewComponent = viewComponentHbmDao.load(refVcId);
		} else if (since.equalsIgnoreCase("parent")) {
			viewComponent = viewComponentHbmDao.load(refVcId);
			viewComponent = viewComponent.getParent();
		} else if (since.equalsIgnoreCase("unit")) {
			viewComponent = viewComponentHbmDao.load(refVcId);
			ViewComponentHbm vcwr = viewComponent.getViewComponentUnit();
			viewComponent = viewComponentHbmDao.load(vcwr.getViewComponentId());
		} else if (since.equalsIgnoreCase("root")) { // means root from the
			// ViewDocument
			//
			viewComponent = viewComponentHbmDao.load(refVcId);
			viewComponent = (viewComponent.isRoot()) ? viewComponent : viewComponent.getViewDocument().getViewComponent();
		} else if (since.startsWith("lastNavigationRoot")) {
			if (since.equalsIgnoreCase("lastNavigationRoot")) {
				viewComponent = viewComponentHbmDao.load(refVcId);
				ViewComponentHbm vcwr = viewComponent.getNavigationRoot();
				viewComponent = viewComponentHbmDao.load(vcwr.getViewComponentId());
			} else if (since.startsWith("lastNavigationRoot+")) {
				viewComponent = viewComponentHbmDao.load(refVcId);
				int count = new Integer(since.substring(19)).intValue();
				ViewComponentHbm lastNavRoot = null;
				if (viewComponent != null) lastNavRoot = viewComponent.getNavigationRoot();
				ArrayList<ViewComponentHbm> al = new ArrayList<ViewComponentHbm>();
				if (lastNavRoot != null) {
					while (viewComponent != null && !viewComponent.getViewComponentId().equals(lastNavRoot.getViewComponentId())) {
						if (log.isDebugEnabled()) {
							log.debug("count " + count + " lastNavRoot " + lastNavRoot.getViewComponentId() + " vcl " + viewComponent.getViewComponentId());
						}
						al.add(viewComponent);
						viewComponent = viewComponent.getParent();
					}
				}
				int elementToGet = al.size() - count;
				if (elementToGet > al.size() || elementToGet < 0) {
					log.error("Something went wrong during fetching: " + since + " count " + count + " elementToGet " + elementToGet + " al.size() " + al.size());
				} else {
					viewComponent = al.get(elementToGet);
				}
			} else if (since.startsWith("lastNavigationRoot--")) {
				// Searching down the count to the navigation roots
				viewComponent = viewComponentHbmDao.load(refVcId);
				ViewComponentHbm vcwr = viewComponent.getNavigationRoot();
				viewComponent = viewComponentHbmDao.load(vcwr.getViewComponentId());
				int count = new Integer(since.substring(20)).intValue();
				while (count > 0) {
					if (log.isDebugEnabled()) log.debug("Count to decrement: " + count);
					if (viewComponent == null) break;
					viewComponent = viewComponent.getParent();
					if (viewComponent == null) break;
					viewComponent = viewComponent.getNavigationRoot();
					count--;
				}
			} else if (since.startsWith("lastNavigationRoot-")) {
				viewComponent = viewComponentHbmDao.load(refVcId);
				ViewComponentHbm vcwr = viewComponent.getNavigationRoot();
				viewComponent = viewComponentHbmDao.load(vcwr.getViewComponentId());
				int count = new Integer(since.substring(19)).intValue();
				while (count > 0) {
					if (log.isDebugEnabled()) log.debug("Count to decrement: " + count);
					if (viewComponent == null) break;
					viewComponent = viewComponent.getParent();
					count--;
				}
			}
		} else if (since.equalsIgnoreCase("next")) {
			viewComponent = viewComponentHbmDao.load(refVcId);
			viewComponent = getNavigationNextTraversal(viewComponent, getPUBLSVersion);
		} else if (since.equalsIgnoreCase("prev")) {
			viewComponent = viewComponentHbmDao.load(refVcId);
			viewComponent = getNavigationPrevTraversal(viewComponent, getPUBLSVersion);
		} else { // in this case weare using the "since" attribute as
			// ViewComponentId
			viewComponent = viewComponentHbmDao.load(new Integer(since));
		}
		if (log.isDebugEnabled()) log.debug("end navigationViewComponentSolver");
		return viewComponent.getViewComponentId();
	}

	private ViewComponentHbm getNavigationNextTraversal(ViewComponentHbm vcl, boolean liveserver) {
		if (vcl.getFirstChild() != null) {
			vcl = vcl.getFirstChild();
		} else if (vcl.getNextNode() != null) {
			vcl = vcl.getNextNode();
		} else {
			ViewComponentHbm prevNext = null;
			while (prevNext == null) {
				if (vcl.getParent() == null) {
					vcl = null;
					break;
				}
				vcl = vcl.getParent();
				prevNext = vcl.getNextNode();
			}
			vcl = prevNext;
		}
		if (vcl != null && !viewComponentHbmDao.shouldBeVisible(vcl, liveserver)) {
			vcl = getNavigationNextTraversal(vcl, liveserver);
		}
		return vcl;
	}

	private ViewComponentHbm getNavigationPrevTraversal(ViewComponentHbm vcl, boolean liveserver) {
		if (vcl.getPrevNode() != null) {
			// prev durchtraversieren
			vcl = vcl.getPrevNode();
			while (vcl.getFirstChild() != null) {
				vcl = vcl.getFirstChild();
				while (vcl.getNextNode() != null) { // move to last Element
					vcl = vcl.getNextNode();
				}
			}
		} else {
			vcl = vcl.getParent();
		}
		if (vcl != null && !viewComponentHbmDao.shouldBeVisible(vcl, liveserver)) {
			vcl = getNavigationPrevTraversal(vcl, liveserver);
		}
		return vcl;
	}

	private String getSite(String hostName) throws UserException {
		HostHbm hl = null;
		try {
			hl = hostHbmDao.load(hostName);
		} catch (Exception e) {
			throw new UserException("Host \"" + hostName + "\" not found!\n" + e.getMessage());
		}
		if (hl == null) {
			if (log.isDebugEnabled()) log.debug("Host with name \"" + hostName + "\" not found");
			return ("");
		}
		SiteHbm sl = null;
		sl = hl.getSite();
		if (sl != null) {
			return (sl.getSiteId().toString());
		}
		return ("");
	}

	//@RequestMapping(value = "/includecontent/{currentViewComponentId}/{includeUnit}/{includeBy}/{getPublsVersion}/{xPathQuery}", method = RequestMethod.GET)
	public String getIncludeContent(Integer currentViewComponentId, boolean includeUnit, String includeBy, boolean getPublsVersion, String xPathQuery) throws Exception {
		String includeContent = "<contentInclude>";
		try {
			Integer targetViewComponentId = null;
			String content = null;
			if (!includeUnit) {
				targetViewComponentId = Integer.valueOf(includeBy);
			} else {
				targetViewComponentId = this.getTargetViewComponentId(currentViewComponentId, includeBy);
			}
			content = this.getContent(targetViewComponentId, getPublsVersion);
			if (xPathQuery == null || "".equalsIgnoreCase(xPathQuery)) {
				includeContent += content;
			} else {
				xPathQuery = xPathQuery.trim();
				if (xPathQuery.endsWith("/")) xPathQuery += "*";
				org.w3c.dom.Document contentDoc = XercesHelper.string2Dom(content);
				Iterator it = XercesHelper.findNodes(contentDoc, xPathQuery);
				while (it.hasNext()) {
					Node result = (Node) it.next();
					includeContent += XercesHelper.node2string(result);
				}
			}
		} catch (Exception e) {
			log.warn("Error getting includeContent: " + e.getMessage(), e);
		}
		includeContent += "</contentInclude>";
		return includeContent;
	}

	public String getIncludeTeaser(Integer currentViewComponentId, boolean getPublsVersion, String teaserRequest) throws Exception {
		StringBuffer includeContent = new StringBuffer("<teaserInclude>");
		try {
			Document doc = XercesHelper.string2Dom(teaserRequest);
			Node ndeIncludeType = doc.getDocumentElement().getFirstChild();
			while (ndeIncludeType != null) {
				if (ndeIncludeType.getNodeType() == Node.ELEMENT_NODE) {
					if ("teaserRandomized".equalsIgnoreCase(ndeIncludeType.getNodeName())) {
						String count = null;
						String unitOrigin = null;
						String xpath = ((Element) ndeIncludeType).getAttribute("xpathTeaserElement");
						if (xpath == null || xpath.length() == 0) {
							if (log.isDebugEnabled()) {
								log.debug("No xpath for teaser element specified for randomized teaser at viewComponent " + currentViewComponentId + " - skipping...");
							}
							ndeIncludeType = ndeIncludeType.getNextSibling();
							continue;
						}

						Node ndeChild = ndeIncludeType.getFirstChild();
						while (ndeChild != null) {
							if (ndeChild.getNodeType() == Node.ELEMENT_NODE) {
								if ("count".equalsIgnoreCase(ndeChild.getNodeName())) {
									count = ndeChild.getFirstChild().getNodeValue();
								} else if ("unit".equalsIgnoreCase(ndeChild.getNodeName())) {
									unitOrigin = ndeChild.getFirstChild().getNodeValue();
								} else {
									log.warn("unknown option in " + ndeIncludeType.getNodeName() + " - " + ndeChild.getNodeName() + " - skipping...");
								}
							}
							ndeChild = ndeChild.getNextSibling();
						}
						Integer currentViewDocument = getViewDocument4ViewComponentId(currentViewComponentId).getViewDocumentId();
						Integer targetUnitId = getTargetUnitId(currentViewComponentId, currentViewDocument, unitOrigin);
						XmlSearchValue[] searchResults = searchengineService.searchXmlByUnit(targetUnitId, currentViewDocument, xpath, false);
						int iCount = Integer.parseInt(count);
						if (searchResults != null && iCount > 0) {
							HashSet<XmlSearchValue> resultSet = new HashSet<XmlSearchValue>(iCount);
							if (iCount >= searchResults.length) {
								resultSet.addAll(Arrays.asList(searchResults));
							} else {
								while (resultSet.size() < iCount) {
									int index = new Double(Math.random() * searchResults.length).intValue();
									// hopefully XmlSearchValue overrides
									// Object.hashCode()
									// otherwise XmlSearchValue.equals is always
									// true and every second object overrides
									// the first in the HashSet
									resultSet.add(searchResults[index]);
								}
							}
							Iterator it = resultSet.iterator();
							while (it.hasNext()) {
								XmlSearchValue value = (XmlSearchValue) it.next();
								String content = this.getContent(value.getViewComponentId(), getPublsVersion);

								org.w3c.dom.Document contentDoc = XercesHelper.string2Dom(content);
								Iterator itContent = XercesHelper.findNodes(contentDoc, xpath);
								while (itContent.hasNext()) {
									Node result = (Node) itContent.next();
									includeContent.append("<teaser>").append(XercesHelper.node2string(result)).append("</teaser>");
								}
							}
						}
					} else if ("teaserRef".equalsIgnoreCase(ndeIncludeType.getNodeName())) {
						String viewComponentId, teaserIdentifier, xpathTeaserElement, xpathTeaserIdentifier, xPathQuery = null;
						Element elmIncludeType = (Element) ndeIncludeType;
						viewComponentId = elmIncludeType.getAttribute("viewComponentId");
						teaserIdentifier = elmIncludeType.getAttribute("teaserIdentifier");
						xpathTeaserElement = elmIncludeType.getAttribute("xpathTeaserElement");
						xpathTeaserIdentifier = elmIncludeType.getAttribute("xpathTeaserIdentifier");

						String content = null;
						try {
							content = this.getContent(Integer.valueOf(viewComponentId), getPublsVersion);
						} catch (Exception e) {
							log.warn("teaser's referenced viewComponentId " + viewComponentId + " does not exist");
							ndeIncludeType = ndeIncludeType.getNextSibling();
							continue;
						}
						xPathQuery = xpathTeaserElement;
						if (!"".equalsIgnoreCase(xpathTeaserIdentifier) && !"".equalsIgnoreCase(teaserIdentifier)) {
							if (!xPathQuery.endsWith("/") && !xpathTeaserElement.startsWith("/")) xPathQuery += "/";
							xPathQuery += "[" + xpathTeaserIdentifier + "=" + teaserIdentifier + "]";
						}
						if (log.isDebugEnabled()) log.debug("xPathQuery: " + xPathQuery);

						org.w3c.dom.Document contentDoc = XercesHelper.string2Dom(content);
						Iterator it = XercesHelper.findNodes(contentDoc, xPathQuery);
						while (it.hasNext()) {
							Node result = (Node) it.next();
							includeContent.append("<teaser>").append(XercesHelper.node2string(result)).append("</teaser>");
						}
					} else {
						log.warn("unknown teaserIncludeType " + ndeIncludeType.getNodeName() + " - skipping...");
					}
				}
				ndeIncludeType = ndeIncludeType.getNextSibling();
			}
		} catch (Exception e) {
			log.warn("Error getting teaserInclude: " + e.getMessage(), e);
		}
		includeContent.append("</teaserInclude>");
		return includeContent.toString();
	}

	public String getIncludeTeaser(String teaserType, Attributes attributes, boolean getPublsVersion) throws Exception {
		StringBuffer includeContent = new StringBuffer();
		try {
			if ("teaserRandomized".equalsIgnoreCase(teaserType)) {
				String count = attributes.getValue("count");
				String unitOrigin = attributes.getValue("unit");
				String viewComponentId = attributes.getValue("viewComponentId");
				String xpath = attributes.getValue("xpathTeaserElement");
				if (xpath == null || xpath.length() == 0) {
					if (log.isDebugEnabled()) {
						log.debug("No xpath for teaser element specified for randomized teaser at viewComponent " + viewComponentId + " - skipping...");
					}
					return null;
				}
				Integer currentViewDocument = getViewDocument4ViewComponentId(Integer.decode(viewComponentId)).getViewDocumentId();
				Integer targetUnitId = getTargetUnitId(Integer.decode(viewComponentId), currentViewDocument, unitOrigin);
				XmlSearchValue[] searchResults = searchengineService.searchXmlByUnit(targetUnitId, currentViewDocument, xpath, false);
				int iCount = Integer.parseInt(count);
				if (searchResults != null && iCount > 0) {
					HashSet<XmlSearchValue> resultSet = new HashSet<XmlSearchValue>(iCount);
					if (iCount >= searchResults.length) {
						resultSet.addAll(Arrays.asList(searchResults));
					} else {
						while (resultSet.size() < iCount) {
							int index = new Double(Math.random() * searchResults.length).intValue();
							resultSet.add(searchResults[index]);
						}
					}
					Iterator it = resultSet.iterator();
					while (it.hasNext()) {
						XmlSearchValue value = (XmlSearchValue) it.next();
						String content = this.getContent(value.getViewComponentId(), getPublsVersion);

						org.w3c.dom.Document contentDoc = XercesHelper.string2Dom(content);
						Iterator itContent = XercesHelper.findNodes(contentDoc, xpath);
						while (itContent.hasNext()) {
							Node result = (Node) itContent.next();
							includeContent.append("<teaser>").append(XercesHelper.node2string(result)).append("</teaser>");
						}
					}
				}
			} else if ("teaserRef".equalsIgnoreCase(teaserType)) {
				String viewComponentId, teaserIdentifier, xpathTeaserElement, xpathTeaserIdentifier, xPathQuery = null;
				viewComponentId = attributes.getValue("viewComponentId");
				teaserIdentifier = attributes.getValue("teaserIdentifier");
				xpathTeaserElement = attributes.getValue("xpathTeaserElement");
				xpathTeaserIdentifier = attributes.getValue("xpathTeaserIdentifier");

				String content = null;
				try {
					content = this.getContent(Integer.valueOf(viewComponentId), getPublsVersion);
				} catch (Exception e) {
					log.warn("teaser's referenced viewComponentId " + viewComponentId + " does not exist");
					return null;
				}
				xPathQuery = xpathTeaserElement;
				if (!"".equalsIgnoreCase(xpathTeaserIdentifier) && !"".equalsIgnoreCase(teaserIdentifier)) {
					if (!xPathQuery.endsWith("/") && !xpathTeaserElement.startsWith("/")) xPathQuery += "/";
					xPathQuery += "[" + xpathTeaserIdentifier + "=" + teaserIdentifier + "]";
				}
				if (log.isDebugEnabled()) log.debug("xPathQuery: " + xPathQuery);

				org.w3c.dom.Document contentDoc = XercesHelper.string2Dom(content);
				Iterator it = XercesHelper.findNodes(contentDoc, xPathQuery);
				while (it.hasNext()) {
					Node result = (Node) it.next();
					includeContent.append("<teaser>").append(XercesHelper.node2string(result)).append("</teaser>");
				}
			} else {
				log.warn("unknown teaserIncludeType " + teaserType + " - skipping...");
			}

		} catch (Exception e) {
			log.warn("Error getting teaserInclude: " + e.getMessage(), e);
		}
		return includeContent.toString();
	}

	private Integer getTargetViewComponentId(Integer currentViewComponentId, String unitRef) {
		Integer targetViewComponentId = null;
		if (unitRef != null) {
			unitRef = unitRef.trim();
			try {
				ViewComponentHbm currViewComponent = viewComponentHbmDao.load(currentViewComponentId);
				Integer currViewDocumentId = currViewComponent.getViewDocument().getViewDocumentId();
				if ("root".equalsIgnoreCase(unitRef)) {
					// ViewComponent in ViewDocument is RootUnit
					targetViewComponentId = currViewComponent.getViewDocument().getViewComponent().getViewComponentId();
				} else if ("this".equalsIgnoreCase(unitRef)) {
					Integer unitId = currViewComponent.getUnit4ViewComponent();
					targetViewComponentId = this.getViewComponent4Unit(unitId, currViewDocumentId).getViewComponentId();
				} else if ("parent".equalsIgnoreCase(unitRef)) {
					Integer unitId = currViewComponent.getUnit4ViewComponent();
					targetViewComponentId = this.getViewComponent4Unit(unitId, currViewDocumentId).getViewComponentId();
					ViewComponentHbm unitViewComponent = viewComponentHbmDao.load(targetViewComponentId);
					Integer parentUnitId = unitViewComponent.getParent() == null ? unitId : unitViewComponent.getParent().getUnit4ViewComponent();
					targetViewComponentId = this.getViewComponent4Unit(parentUnitId, currViewDocumentId).getViewComponentId();
				}
			} catch (Exception e) {
				log.error("Error resolving targetViewComponent for VC " + currentViewComponentId + " and unitRef " + unitRef + ": " + e.getMessage(), e);
			}
		}
		if (log.isDebugEnabled()) log.debug("targetViewComponentId for currentViewComponentId " + currentViewComponentId + " and unitRef " + unitRef + ": " + targetViewComponentId);
		return targetViewComponentId;
	}

	private Integer getTargetUnitId(Integer currentViewComponentId, Integer currentViewDocument, String unitRef) {
		Integer targetUnitId = null;
		if (unitRef != null) {
			unitRef = unitRef.trim();
			try {
				if ("root".equalsIgnoreCase(unitRef)) {
					// ViewComponent in ViewDocument is RootUnit
					targetUnitId = viewDocumentHbmDao.load(currentViewDocument).getViewComponent().getUnit4ViewComponent();
				} else if ("this".equalsIgnoreCase(unitRef)) {
					targetUnitId = viewComponentHbmDao.load(currentViewComponentId).getUnit4ViewComponent();
				} else if ("parent".equalsIgnoreCase(unitRef)) {
					ViewComponentHbm vc = viewComponentHbmDao.load(currentViewComponentId);
					vc = vc.getViewComponentUnit();
					if (vc != null && !vc.isRoot()) {
						targetUnitId = vc.getParent().getUnit4ViewComponent();
					}
				}
			} catch (Exception e) {
				log.error("Error resolving getTargetUnitId for VC " + currentViewComponentId + " and unitRef " + unitRef + ": " + e.getMessage(), e);
			}
		}
		if (log.isDebugEnabled()) log.debug("getTargetUnitId for currentViewComponentId " + currentViewComponentId + " and unitRef " + unitRef + ": " + targetUnitId);
		return targetUnitId;
	}

	public Integer getUnitIdForViewComponent(Integer viewComponentId) throws Exception {
		Integer unitId = null;
		try {
			ViewComponentHbm view = viewComponentHbmDao.load(viewComponentId);
			if (view.getAssignedUnit() != null) unitId = view.getAssignedUnit().getUnitId();
		} catch (Exception ex) {
			log.warn("Error getting unitId for ViewComponent " + viewComponentId + ": " + ex.getMessage());
		}
		return unitId;
	}

	/**
	 * Checks if for the host from the initial request there is some redirection
	 * configured.<br/> Redirects in the following order:
	 * <ol>
	 * <li>redirect-url for this host</li>
	 * <li>redirect-host for this host, if so with redirect-url</li>
	 * <li>redirect to resolved shortlink for this host</li>
	 * <li>empty string</li>
	 * </ol>
	 */
	@HourCache
	public String resolveRedirect(String hostName, String requestPath, Set<String> formerHostsSet) throws Exception {
		try {
			if ((hostName == null || this.isIpAddress(hostName)) || ("".equalsIgnoreCase(hostName))) {
				return ("");
			}
			HostHbm host = null;
			try {
				host = hostHbmDao.load(hostName);
			} catch (Exception e) {
				if (log.isDebugEnabled()) log.debug("Could not find Host with name " + hostName, e);
			}
			if (host == null) {
				if (log.isInfoEnabled()) log.info("\"" + hostName + "\" is not configured in Hostmanagement");
				return "";
			}
			String redirectUrl = host.getRedirectUrl();
			if (redirectUrl != null && redirectUrl.length() > 0)
			// host has a valid redirect url
				return redirectUrl;
			if (host.getRedirectHostName() != null) {
				// host points to a different host in tizzit
				if (formerHostsSet == null) formerHostsSet = new HashSet<String>();
				formerHostsSet.add(hostName);
				if (formerHostsSet.contains(host.getRedirectHostName().getHostName())) {
					log.fatal("Endless-Loop detected! " + hostName);
					return "";
				}
				return resolveRedirect(host.getRedirectHostName().getHostName(), requestPath, formerHostsSet);
			}
			String returnHost = "";
			if (formerHostsSet != null && formerHostsSet.size() > 0) {
				returnHost = hostName;
			}
			URL url = null;
			String path = "";
			if (requestPath != null) {
				url = new URL(requestPath);
				path = url.getPath();
			}
			// check for shortlink
			SiteHbm site = host.getSite();
			if (site != null) {
				ShortLinkHbm shortlink = null;
				try {
					if (url != null) {
						if (returnHost.length() > 0) {
							// add protocol to the new host
							returnHost = url.getProtocol() + "://" + returnHost;
							// add port?
							if (url.getPort() > 0 && url.getPort() != 80) returnHost += ":" + url.getPort();
						}
						if (path.startsWith("/")) path = path.substring(1, path.length());
						if (path.length() > 0) shortlink = shortLinkHbmDao.findByShortLink(path, site.getSiteId());
					}
				} catch (Exception e) {
					if (log.isDebugEnabled()) log.debug("No ShortLink found for \"" + requestPath + "\": " + e.getMessage(), e);
				}
				if (shortlink != null) {
					redirectUrl = shortlink.getRedirectUrl();
					if (redirectUrl != null) {
						int startIndex = redirectUrl.indexOf("{");
						int endIndex = redirectUrl.indexOf("}");
						if (startIndex >= 0 && endIndex > startIndex) {
							// placeholder has to be substituted
							String placeHolder = redirectUrl.substring(startIndex + 1, endIndex);
							if (log.isDebugEnabled()) log.debug("PlaceHolder: " + placeHolder);
							String substitute = null;
							if ("unitName".equalsIgnoreCase(placeHolder) && host.getUnit() != null) {
								substitute = host.getUnit().getName().trim();
							} else {
								// replace placeholder by empty string -> delete
								// placeholder
								substitute = "";
							}
							// replace placeholder by substitute ;-)
							// perhaps use String.replaceAll if placeholder
							// occurs multiple times ?
							redirectUrl = redirectUrl.substring(0, startIndex) + substitute + redirectUrl.substring(endIndex + 1, redirectUrl.length());
						} else {
							if (log.isDebugEnabled()) log.debug("No (reasonable) placeholder to substitute found");
						}
						if (shortlink.getViewDocument() != null) {
							redirectUrl = "/" + shortlink.getViewDocument().getLanguage() + "/" + redirectUrl;
						} else {
							redirectUrl = "/" + redirectUrl;
						}
						if (log.isDebugEnabled()) log.debug("Redirect-Url: " + redirectUrl);
						return returnHost + redirectUrl;
					}
					if (log.isDebugEnabled()) log.debug("No redirectUrl found for \"" + shortlink.getShortLink());
				}
			}
			if (path.length() > 0 && returnHost.length() > 0) returnHost += "/" + path;
			return (returnHost);
		} catch (Exception e) {
			throw new UserException("unknown exception resolving host - " + hostName, e);
		}
	}

	public PictureValue getPictureValue(Integer pictureId) throws Exception {
		PictureValue pictureValue = null;
		try {
			PictureHbm pic = pictureHbmDao.load(pictureId);
			pictureValue = pic.getPictureValue();
		} catch (Exception e) {
			log.warn("Error getting Picture " + pictureId + ": " + e.getMessage());
		}
		return pictureValue;
	}

	public String filterNavigation(String navigationXml, Map safeGuardMap) throws Exception {
		try {
			return safeguardServiceSpring.filterNavigation(navigationXml, safeGuardMap);
		} catch (Exception e) {
			log.error("Error filtering Navigation: " + e.getMessage(), e);
		}
		return navigationXml;
	}

	public long getMaxSiteLastModifiedDate() throws Exception {
		try {
			return siteHbmDao.getMaxLastModifiedDate();
		} catch (Exception e) {
			log.error("Error getting MaxSiteLastModifiedDate: " + e.getMessage(), e);
		}
		return 0L;
	}

	public Boolean getLiveserver(String hostName) {
		boolean retVal = false;
		HostHbm host = hostHbmDao.load(hostName);
		if (host != null) {
			retVal = host.isLiveserver();
		}
		return retVal;
	}

	//@RequestMapping(value = "/unitinfoxml/{unitId}", method = RequestMethod.GET)
	public String getUnitInfoXml(Integer unitId) throws Exception {
		StringBuffer sb = new StringBuffer("");
		try {
			UnitHbm unit = unitHbmDao.load(unitId);
			sb.append("<unit id=\"").append(unit.getUnitId().toString()).append("\" siteId=\"").append(unit.getSite().getSiteId().toString()).append("\">");
			sb.append("<unitName><![CDATA[").append(unit.getName().trim()).append("]]></unitName>");
			sb.append("<lastModified><![CDATA[").append(sdf.format(new Date(unit.getLastModifiedDate()))).append("]]></lastModified>");
			sb.append("<unitImage>");
			if (unit.getImageId() != null) {
				try {
					sb.append(this.getPictureInfoXml(unit.getImageId()));
				} catch (Exception e) {
					log.warn("Error getting UnitImage " + unit.getImageId().toString() + " for Unit " + unitId.toString());
				}
			}
			sb.append("</unitImage>");
			sb.append("<unitLogo>");
			if (unit.getLogoId() != null) {
				try {
					sb.append(this.getPictureInfoXml(unit.getLogoId()));
				} catch (Exception e) {
					log.warn("Error getting UnitLogo " + unit.getLogoId().toString() + " for Unit " + unitId.toString());
				}
			}
			sb.append("</unitLogo>");
			sb.append("</unit>");
		} catch (Exception e) {
			throw new UserException("Error getting UnitInfo  " + unitId + ": " + e.getMessage());
		}

		return sb.toString();
	}

	/**
	 * <pre>
	 * &lt;image id=&quot;29824&quot; width=&quot;235&quot; height=&quot;294&quot; mimeType=&quot;image/jpeg&quot;&gt;
	 *    &lt;fileName&gt;&lt;![CDATA[PKD1.JPG]]&gt;&lt;/fileName&gt;
	 *    &lt;altText/&gt;
	 *    &lt;timeStamp&gt;&lt;![CDATA[dd.MM.yyyy HH:mm:ss]]&gt;&lt;/timeStamp&gt;
	 * &lt;/image&gt;
	 * </pre>
	 *
	 * @param pictureId
	 */
	//@RequestMapping(value = "/pictureinfoxml/{pictureid}", method = RequestMethod.GET)
	private String getPictureInfoXml(Integer pictureId) throws Exception {
		StringBuffer sb = new StringBuffer("");
		PictureHbm pic = pictureHbmDao.load(pictureId);
		sb.append("<image id=\"").append(pictureId.toString()).append("\" width=\"").append(pic.getWidth().toString()).append("\" ");
		sb.append("height=\"").append(pic.getHeight().toString()).append("\" mimeType=\"").append(pic.getMimeType()).append("\">");
		sb.append("<fileName><![CDATA[").append(pic.getPictureName()).append("]]></fileName>");
		if (pic.getAltText() != null && pic.getAltText().length() > 0) {
			sb.append("<altText><![CDATA[").append(pic.getAltText()).append("]]></altText>");
		} else {
			sb.append("<altText/>");
		}
		sb.append("<timeStamp><![CDATA[").append(sdf.format(new Date(pic.getTimeStamp().longValue()))).append("]]></timeStamp>");
		sb.append("</image>");

		return sb.toString();
	}

	//@RequestMapping(value = "/lastmodifiedpages/{viewComponentId}/{unitId}/{numberOfPages}/{getPublsVersion}", method = RequestMethod.GET)
	public String getLastModifiedPages(Integer viewComponentId, Integer unitId, int numberOfPages, boolean getPublsVersion) throws Exception {
		if (log.isDebugEnabled()) log.debug("getLastModifiedPages start");
		try {
			ViewComponentHbm viewComponent = viewComponentHbmDao.load(viewComponentId);
			return viewComponentHbmDao.getLastModifiedPages(viewComponent.getViewDocument().getViewDocumentId(), unitId, numberOfPages, getPublsVersion);
		} catch (Exception e) {
			log.error("Error getting " + numberOfPages + " last-modified pages for viewComponent " + viewComponentId + " and unit " + unitId + ": " + e.getMessage());
			throw new UserException("Error getting " + numberOfPages + " last-modified pages for viewComponent " + viewComponentId + " and unit " + unitId + ": " + e.getMessage());
		}
	}

}
