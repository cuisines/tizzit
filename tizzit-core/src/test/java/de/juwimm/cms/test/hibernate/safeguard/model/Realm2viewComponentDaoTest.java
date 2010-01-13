package de.juwimm.cms.test.hibernate.safeguard.model;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewComponentHbmDao;
import de.juwimm.cms.model.ViewComponentHbmImpl;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbm;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbmDao;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbmDaoImpl;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbmImpl;
import de.juwimm.cms.safeguard.model.RealmJaasHbm;
import de.juwimm.cms.safeguard.model.RealmJaasHbmDao;
import de.juwimm.cms.safeguard.model.RealmJaasHbmImpl;
import de.juwimm.cms.safeguard.model.RealmJdbcHbm;
import de.juwimm.cms.safeguard.model.RealmJdbcHbmDao;
import de.juwimm.cms.safeguard.model.RealmJdbcHbmImpl;
import de.juwimm.cms.safeguard.model.RealmLdapHbm;
import de.juwimm.cms.safeguard.model.RealmLdapHbmDao;
import de.juwimm.cms.safeguard.model.RealmLdapHbmImpl;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbm;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbmDao;
import de.juwimm.cms.safeguard.model.RealmSimplePwHbmImpl;
import de.juwimm.cms.safeguard.vo.RealmJaasValue;
import de.juwimm.cms.safeguard.vo.RealmJdbcValue;
import de.juwimm.cms.safeguard.vo.RealmLdapValue;
import de.juwimm.cms.safeguard.vo.RealmSimplePwValue;
import de.juwimm.cms.test.hibernate.HbmTestImpl;

public class Realm2viewComponentDaoTest extends HbmTestImpl {

	@Autowired
	Realm2viewComponentHbmDao realm2ViewComponentDao;
	ViewComponentHbmDao viewComponentDao;
	RealmSimplePwHbmDao realmSimplePwDao;
	RealmJdbcHbmDao realmJdbcDao;
	RealmLdapHbmDao realmLdapDao;
	RealmJaasHbmDao realmJaasDao;

	public void initializeServiceBeans() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();

		viewComponentDao = EasyMock.createMock(ViewComponentHbmDao.class);
		((Realm2viewComponentHbmDaoImpl) realm2ViewComponentDao).setViewComponentHbmDao(viewComponentDao);

		realmSimplePwDao = EasyMock.createMock(RealmSimplePwHbmDao.class);
		((Realm2viewComponentHbmDaoImpl) realm2ViewComponentDao).setRealmSimplePwHbmDao(realmSimplePwDao);

		realmJdbcDao = EasyMock.createMock(RealmJdbcHbmDao.class);
		((Realm2viewComponentHbmDaoImpl) realm2ViewComponentDao).setRealmJdbcHbmDao(realmJdbcDao);

		realmLdapDao = EasyMock.createMock(RealmLdapHbmDao.class);
		((Realm2viewComponentHbmDaoImpl) realm2ViewComponentDao).setRealmLdapHbmDao(realmLdapDao);

		realmJaasDao = EasyMock.createMock(RealmJaasHbmDao.class);
		((Realm2viewComponentHbmDaoImpl) realm2ViewComponentDao).setRealmJaasHbmDao(realmJaasDao);
	}

	/**
	 * Test Create
	 * expect: assign id
	 *         no exception thrown
	 */
	public void testCreate() {
		Realm2viewComponentHbm realm2VC = new Realm2viewComponentHbmImpl();

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		realm2VC.setViewComponent(viewComponent);

		try {
			realm2VC = realm2ViewComponentDao.create(realm2VC);
			Assert.assertNotNull(realm2VC);
			Assert.assertNotNull(realm2VC.getRealm2viewComponentId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

	/**
	 * Test Create(RealmSimplePwValue realmSimplePwValue, Integer viewComponentId, String roleNeeded)
	 * expect: no exception thrown
	 */
	public void testCreate1() {
		RealmSimplePwValue realmSimplePwValue;
		Integer viewComponentId = 1;
		String roleNeeded = "testRole";

		realmSimplePwValue = new RealmSimplePwValue();
		realmSimplePwValue.setSimplePwRealmId(1);

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		RealmSimplePwHbm realmSimplePw = new RealmSimplePwHbmImpl();
		realmSimplePw.setSimplePwRealmId(1);

		try {
			EasyMock.expect(viewComponentDao.load(EasyMock.eq(1))).andReturn(viewComponent);
			EasyMock.expect(realmSimplePwDao.load(EasyMock.eq(1))).andReturn(realmSimplePw);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDao);
		EasyMock.replay(realmSimplePwDao);

		try {
			realm2ViewComponentDao.create(realmSimplePwValue, viewComponentId, roleNeeded);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDao);
		EasyMock.verify(realmSimplePwDao);
	}

	/**
	 * Test Create(RealmJdbcValue realmJdbcValue, Integer viewComponentId, String roleNeeded)
	 * expect: no exception thrown
	 */
	public void testCreate2() {
		RealmJdbcValue realmJdbcValue;
		Integer viewComponentId = 1;
		String roleNeeded = "testRole";

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		realmJdbcValue = new RealmJdbcValue();
		realmJdbcValue.setJdbcRealmId(1);

		RealmJdbcHbm realmJdbc = new RealmJdbcHbmImpl();
		realmJdbc.setJdbcRealmId(1);

		try {
			EasyMock.expect(viewComponentDao.load(EasyMock.eq(1))).andReturn(viewComponent);
			EasyMock.expect(realmJdbcDao.load(EasyMock.eq(1))).andReturn(realmJdbc);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDao);
		EasyMock.replay(realmJdbcDao);

		try {
			realm2ViewComponentDao.create(realmJdbcValue, viewComponentId, roleNeeded);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDao);
		EasyMock.verify(realmJdbcDao);

	}

	/**
	 * Test Create(RealmLdapValue realmLdapValue, Integer viewComponentId, String roleNeeded)
	 * expect: no exception thrown 
	 */
	public void testCreate3() {
		RealmLdapValue realmLdapValue;
		Integer viewComponentId = 1;
		String roleNeeded = "testRole";

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		realmLdapValue = new RealmLdapValue();
		realmLdapValue.setLdapRealmId(1);

		RealmLdapHbm realmLdap = new RealmLdapHbmImpl();
		realmLdap.setLdapRealmId(1);

		try {
			EasyMock.expect(viewComponentDao.load(EasyMock.eq(1))).andReturn(viewComponent);
			EasyMock.expect(realmLdapDao.load(EasyMock.eq(1))).andReturn(realmLdap);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDao);
		EasyMock.replay(realmLdapDao);

		try {
			realm2ViewComponentDao.create(realmLdapValue, viewComponentId, roleNeeded);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDao);
		EasyMock.verify(realmLdapDao);
	}

	/**
	 * Test Create(RealmJaasValue realmJaasValue, Integer viewComponentId, String roleNeeded)
	 * expect: no exception thrown
	 */
	public void testCreate4() {
		RealmJaasValue realmJaasValue;
		Integer viewComponentId = 1;
		String roleNeeded = "testRole";

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		realmJaasValue = new RealmJaasValue();
		realmJaasValue.setJaasRealmId(1);

		RealmJaasHbm realmJaas = new RealmJaasHbmImpl();
		realmJaas.setJaasRealmId(1);

		try {
			EasyMock.expect(viewComponentDao.load(EasyMock.eq(1))).andReturn(viewComponent);
			EasyMock.expect(realmJaasDao.load(EasyMock.eq(1))).andReturn(realmJaas);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDao);
		EasyMock.replay(realmJaasDao);

		try {
			realm2ViewComponentDao.create(realmJaasValue, viewComponentId, roleNeeded);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDao);
		EasyMock.verify(realmJaasDao);
	}
}
