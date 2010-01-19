package de.juwimm.cms.test.hibernate.safeguard.model;

import java.util.Iterator;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.safeguard.model.RealmLdapHbm;
import de.juwimm.cms.safeguard.model.RealmLdapHbmDao;
import de.juwimm.cms.safeguard.model.RealmLdapHbmImpl;
import de.juwimm.cms.test.hibernate.HbmTestImpl;

public class RealmLdapDaoTest extends HbmTestImpl {

	@Autowired
	RealmLdapHbmDao realmLdapDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	/**
	 * Test Create
	 * expect: assign id
	 */
	public void testCreate() {
		RealmLdapHbm realmLdap = new RealmLdapHbmImpl();
		realmLdap.setRealmName("testName");

		try {
			realmLdap = realmLdapDao.create(realmLdap);
			Assert.assertNotNull(realmLdap.getLdapRealmId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test Create(Element e, boolean newId)
	 * expect: reuse the id given in the xml,
	 * 		   all the properties are set correctly	
	 */
	public void testCreate1() {
		String xmlString = "<realms><realmLdap><ldapRealmId>3</ldapRealmId><realmName><![CDATA[test]]></realmName><ldapPrefix>test</ldapPrefix><ldapSuffix>test</ldapSuffix><ldapUrl>test</ldapUrl><ldapAuthenticationType>simple</ldapAuthenticationType></realmLdap></realms>";
		try {
			Document doc = XercesHelper.string2Dom(xmlString);
			Iterator it = XercesHelper.findNodes(doc, "//realmLdap");
			while (it.hasNext()) {
				Node element = (Node) it.next();
				try {
					RealmLdapHbm realm = realmLdapDao.create((Element) element, false);
					Assert.assertNotNull(realm);
					Assert.assertEquals("test", realm.getRealmName());
					Assert.assertEquals("test", realm.getLdapPrefix());
					Assert.assertEquals("test", realm.getLdapSuffix());
					Assert.assertEquals("test", realm.getLdapUrl());
					Assert.assertEquals("simple", realm.getLdapAuthenticationType());
				} catch (Exception e) {
					Assert.assertTrue(false);
				}
			}
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test Create(Element e)
	 * expect: no exception thrown
	 * 
	 */
	public void testCreate2() {
		String xmlString = "<realms><realmLdap><ldapRealmId>3</ldapRealmId><realmName><![CDATA[test]]></realmName><ldapPrefix>test</ldapPrefix><ldapSuffix>test</ldapSuffix><ldapUrl>test</ldapUrl><ldapAuthenticationType>simple</ldapAuthenticationType></realmLdap></realms>";
		try {
			Document doc = XercesHelper.string2Dom(xmlString);
			Iterator it = XercesHelper.findNodes(doc, "//realmLdap");
			while (it.hasNext()) {
				Node element = (Node) it.next();
				try {
					realmLdapDao.create((Element) element);
				} catch (Exception e) {
					Assert.assertTrue(false);
				}
			}
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
