package de.juwimm.cms.test.hibernate.safeguard.model;

import java.util.Iterator;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.tizzit.util.XercesHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.juwimm.cms.safeguard.model.RealmJdbcHbm;
import de.juwimm.cms.safeguard.model.RealmJdbcHbmDao;
import de.juwimm.cms.safeguard.model.RealmJdbcHbmImpl;
import de.juwimm.cms.test.hibernate.HbmTestImpl;

public class RealmJdbcDaoTest extends HbmTestImpl {

	@Autowired
	RealmJdbcHbmDao realmJdbcDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	/**
	 * Test Create
	 * expect: assign id
	 */
	public void testCreate() {
		RealmJdbcHbm realmJdbc = new RealmJdbcHbmImpl();
		realmJdbc.setRealmName("testName");

		try {
			realmJdbc = realmJdbcDao.create(realmJdbc);
			Assert.assertNotNull(realmJdbc.getJdbcRealmId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test Create(Element element)
	 * expect: no exception thrown
	 *		   		 
	 */
	public void testCreate1() {
		String xmlString = "<realms><realmJdbc><jdbcRealmId>2</jdbcRealmId><realmName><![CDATA[databaseProtection]]></realmName><jndiName>testJNDIName</jndiName><statementUser><![CDATA[testStatementUser]]></statementUser><statementRolePerUser><![CDATA[testStatementRolePerUser]]></statementRolePerUser></realmJdbc></realms>";
		try {
			Document doc = XercesHelper.string2Dom(xmlString);
			Iterator it = XercesHelper.findNodes(doc, "//realmJdbc");
			while (it.hasNext()) {
				Node element = (Node) it.next();
				try {
					realmJdbcDao.create((Element) element);
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
	 * expect: don't use new id, all the values of the properties are set correctly
	 */
	public void testCreate2() {
		String xmlString = "<realms><realmJdbc><jdbcRealmId>3</jdbcRealmId><realmName><![CDATA[databaseProtection]]></realmName><jndiName>testJNDIName</jndiName><statementUser><![CDATA[testStatementUser]]></statementUser><statementRolePerUser><![CDATA[testStatementRolePerUser]]></statementRolePerUser></realmJdbc></realms>";
		try {
			Document doc = XercesHelper.string2Dom(xmlString);
			Iterator it = XercesHelper.findNodes(doc, "//realmJdbc");
			while (it.hasNext()) {
				Node element = (Node) it.next();
				try {
					realmJdbcDao.create((Element) element, false);
					RealmJdbcHbm realm = realmJdbcDao.load(3);
					Assert.assertNotNull(realm);
					Assert.assertEquals(new Integer(3), realm.getJdbcRealmId());
					Assert.assertEquals("databaseProtection", realm.getRealmName());
					Assert.assertEquals("testJNDIName", realm.getJndiName());
					Assert.assertEquals("testStatementUser", realm.getStatementUser());
					Assert.assertEquals("testStatementRolePerUser", realm.getStatementRolePerUser());
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
	 * expect: assign new id
	 *         no exception thrown
	 */
	public void testCreate3() {
		String xmlString = "<realms><realmJdbc><jdbcRealmId>3</jdbcRealmId><realmName><![CDATA[databaseProtection]]></realmName><jndiName>testJNDIName</jndiName><statementUser><![CDATA[testStatementUser]]></statementUser><statementRolePerUser><![CDATA[testStatementRolePerUser]]></statementRolePerUser></realmJdbc></realms>";
		try {
			Document doc = XercesHelper.string2Dom(xmlString);
			Iterator it = XercesHelper.findNodes(doc, "//realmJdbc");
			while (it.hasNext()) {
				Node element = (Node) it.next();
				try {
					realmJdbcDao.create((Element) element, true);
				} catch (Exception e) {
					Assert.assertTrue(false);
				}
			}
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
