package de.juwimm.cms.test.hibernate.safeguard.model;

import java.util.Iterator;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.safeguard.model.RealmSimplePwHbm;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbmDao;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbmImpl;
import de.juwimm.cms.test.hibernate.HbmTestImpl;

public class RealmSimplePwDaoTest extends HbmTestImpl {

	@Autowired
	RealmSimplePwHbmDao realmSimplePwDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	/**
	 * Test Create
	 * expect: assign id
	 */
	public void testCreate() {
		RealmSimplePwHbm realmSimplePw = new RealmSimplePwHbmImpl();
		realmSimplePw.setRealmName("testName");

		try {
			realmSimplePw = realmSimplePwDao.create(realmSimplePw);
			Assert.assertNotNull(realmSimplePw.getSimplePwRealmId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test Create(Element e)
	 * expect: no exception thrown
	 */
	public void testCreate1() {
		String xmlString = "<realmSimplePw><simplePwRealmId>4</simplePwRealmId><realmName><![CDATA[testSimplePw]]></realmName><simplePwRealmUsers><realmSimplePwUser><userId>4</userId><userName><![CDATA[testUserName]]></userName><password><![CDATA[testPassword]]></password><roles><![CDATA[testRole]]></roles></realmSimplePwUser></simplePwRealmUsers></realmSimplePw>";
		try {
			Document doc = XercesHelper.string2Dom(xmlString);
			Iterator it = XercesHelper.findNodes(doc, "//realmSimplePw");
			while (it.hasNext()) {
				Node element = (Node) it.next();
				try {
					realmSimplePwDao.create((Element) element);
				} catch (Exception e) {
					Assert.assertTrue(false);
				}
			}
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}
}
