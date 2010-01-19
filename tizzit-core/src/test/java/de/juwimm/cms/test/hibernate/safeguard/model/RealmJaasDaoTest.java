package de.juwimm.cms.test.hibernate.safeguard.model;

import java.util.Iterator;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.safeguard.model.RealmJaasHbm;
import de.juwimm.cms.safeguard.model.RealmJaasHbmDao;
import de.juwimm.cms.safeguard.model.RealmJaasHbmDaoImpl;
import de.juwimm.cms.safeguard.model.RealmJaasHbmImpl;
import de.juwimm.cms.test.hibernate.HbmTestImpl;

public class RealmJaasDaoTest extends HbmTestImpl {

	@Autowired
	RealmJaasHbmDao realmJaasDao;
	SiteHbmDao siteDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();

		siteDao = EasyMock.createMock(SiteHbmDao.class);
		((RealmJaasHbmDaoImpl) realmJaasDao).setSiteHbmDao(siteDao);
	}

	/**
	 * Test Create
	 * expect: assign id
	 */
	public void testCreate() {
		RealmJaasHbm realmJaas = new RealmJaasHbmImpl();
		realmJaas.setRealmName("testName");

		try {
			realmJaas = realmJaasDao.create(realmJaas);
			Assert.assertNotNull(realmJaas);
			Assert.assertNotNull(realmJaas.getJaasRealmId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test Create(Element element)
	 * expect: no exception thrown
	 */
	public void testCreate1() {
		String xmlString = "<realms><realmJaas><jaasRealmId>1</jaasRealmId><realmName><![CDATA[newJaas]]></realmName><jaasPolicyName><![CDATA[testJaasPolicyname]]></jaasPolicyName></realmJaas></realms>";
		try {
			Document doc = XercesHelper.string2Dom(xmlString);
			Iterator it = XercesHelper.findNodes(doc, "//realmJaas");
			while (it.hasNext()) {
				Node element = (Node) it.next();
				try {
					realmJaasDao.create((Element) element);
				} catch (Exception e) {
					Assert.assertTrue(false);
				}
			}
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test Create(Element element, boolean newId)
	 * expect: no exception thrown
	 *         the id for the realm is the one from the xml 
	 *         the values of the properties are set correctly from the xml         
	 */
	public void testCreate2() {
		String xmlString = "<realms><realmJaas><jaasRealmId>3</jaasRealmId><realmName><![CDATA[newJaas]]></realmName><jaasPolicyName><![CDATA[testJaasPolicyname]]></jaasPolicyName></realmJaas></realms>";
		try {
			Document doc = XercesHelper.string2Dom(xmlString);
			Iterator it = XercesHelper.findNodes(doc, "//realmJaas");
			while (it.hasNext()) {
				Node element = (Node) it.next();
				try {
					realmJaasDao.create((Element) element, false);
					RealmJaasHbm realm = realmJaasDao.load(3);
					Assert.assertNotNull(realm);
					Assert.assertEquals("newJaas", realm.getRealmName());
					Assert.assertEquals("testJaasPolicyname", realm.getJaasPolicyName());
				} catch (Exception e) {
					Assert.assertTrue(false);
				}
			}
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test Create(Element element, boolean newId)
	 * expect: no exception thrown
	 */
	public void testCreate3() {
		String xmlString = "<realms><realmJaas><jaasRealmId>3</jaasRealmId><realmName><![CDATA[newJaas]]></realmName><jaasPolicyName><![CDATA[testJaasPolicyname]]></jaasPolicyName></realmJaas></realms>";
		try {
			Document doc = XercesHelper.string2Dom(xmlString);
			Iterator it = XercesHelper.findNodes(doc, "//realmJaas");
			while (it.hasNext()) {
				Node element = (Node) it.next();
				try {
					realmJaasDao.create((Element) element, true);
				} catch (Exception e) {
					Assert.assertTrue(false);
				}
			}
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
