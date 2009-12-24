package de.juwimm.cms.remote.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Assert;

import de.juwimm.cms.authorization.model.UserHbm;
import de.juwimm.cms.authorization.model.UserHbmDao;
import de.juwimm.cms.authorization.model.UserHbmImpl;
import de.juwimm.cms.model.SiteHbm;
import de.juwimm.cms.model.SiteHbmDao;
import de.juwimm.cms.model.SiteHbmImpl;
import de.juwimm.cms.model.ViewComponentHbm;
import de.juwimm.cms.model.ViewComponentHbmDao;
import de.juwimm.cms.model.ViewComponentHbmImpl;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbm;
import de.juwimm.cms.safeguard.model.Realm2viewComponentHbmDao;
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
import de.juwimm.cms.safeguard.model.RealmSimplePwUserHbm;
import de.juwimm.cms.safeguard.model.RealmSimplePwUserHbmDao;
import de.juwimm.cms.safeguard.model.RealmSimplePwUserHbmImpl;
import de.juwimm.cms.safeguard.remote.SafeguardServiceSpringImpl;
import de.juwimm.cms.safeguard.vo.ActiveRealmValue;
import de.juwimm.cms.safeguard.vo.RealmJdbcValue;
import de.juwimm.cms.safeguard.vo.RealmSimplePwUserValue;
import de.juwimm.cms.safeguard.vo.RealmSimplePwValue;

public class SafeguardServiceTest extends AbstractServiceTest {

	private SiteHbmDao siteDaoMock;
	private SafeguardServiceSpringImpl safeguardService;
	private RealmJdbcHbmDao realmJdbcDaoMock;
	private RealmSimplePwUserHbmDao realmSimplePwUserDaoMock;
	private RealmSimplePwHbmDao realmSimplePwDaoMock;
	private RealmJaasHbmDao realmJaasDaoMock;
	private RealmLdapHbmDao realmLdapDaoMock;
	private ViewComponentHbmDao viewComponentDaoMock;
	private Realm2viewComponentHbmDao realm2viewComponentDaoMock;
	private UserHbmDao userDaoMock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		safeguardService = new SafeguardServiceSpringImpl();

		siteDaoMock = EasyMock.createMock(SiteHbmDao.class);
		safeguardService.setSiteHbmDao(siteDaoMock);

		realmJdbcDaoMock = EasyMock.createMock(RealmJdbcHbmDao.class);
		safeguardService.setRealmJdbcHbmDao(realmJdbcDaoMock);

		realmSimplePwUserDaoMock = EasyMock.createMock(RealmSimplePwUserHbmDao.class);
		safeguardService.setRealmSimplePwUserHbmDao(realmSimplePwUserDaoMock);

		realmSimplePwDaoMock = EasyMock.createMock(RealmSimplePwHbmDao.class);
		safeguardService.setRealmSimplePwHbmDao(realmSimplePwDaoMock);

		viewComponentDaoMock = EasyMock.createMock(ViewComponentHbmDao.class);
		safeguardService.setViewComponentHbmDao(viewComponentDaoMock);

		realmLdapDaoMock = EasyMock.createMock(RealmLdapHbmDao.class);
		safeguardService.setRealmLdapHbmDao(realmLdapDaoMock);

		realmJaasDaoMock = EasyMock.createMock(RealmJaasHbmDao.class);
		safeguardService.setRealmJaasHbmDao(realmJaasDaoMock);

		realm2viewComponentDaoMock = EasyMock.createMock(Realm2viewComponentHbmDao.class);
		safeguardService.setRealm2viewComponentHbmDao(realm2viewComponentDaoMock);

		userDaoMock = EasyMock.createMock(UserHbmDao.class);
		safeguardService.setUserHbmDao(userDaoMock);
	}

	/**
	 * Test addJdbcRealmToSite
	 * expect: create realmJdbc with the given properties
	 */
	public void testAddJdbcRealmToSite() {
		RealmJdbcValue realm = new RealmJdbcValue();
		realm.setJdbcRealmId(1);
		realm.setRealmName("testRealm");
		realm.setLoginPageId("1");
		realm.setStatementRolePerUser("testRole");
		realm.setStatementUser("testStatementUser");
		realm.setJndiName("testJndiName");

		RealmJdbcHbm realmHbm = new RealmJdbcHbmImpl();
		realmHbm.setJdbcRealmId(1);
		realmHbm.setRealmName("testRealm");
		realmHbm.setLoginPageId("1");
		realmHbm.setStatementRolePerUser("testRole");
		realmHbm.setStatementUser("testStatementUser");
		realmHbm.setJndiName("testJndiName");

		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		try {
			EasyMock.expect(siteDaoMock.load(EasyMock.eq(1))).andReturn(site);
			EasyMock.expect(realmJdbcDaoMock.create((RealmJdbcHbm) EasyMock.anyObject())).andReturn(realmHbm);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(siteDaoMock);
		EasyMock.replay(realmJdbcDaoMock);

		try {
			Integer realmHbmId = safeguardService.addJdbcRealmToSite(1, realm);
			Assert.assertEquals(1, realmHbmId.intValue());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(siteDaoMock);
		EasyMock.verify(realmJdbcDaoMock);

	}

	/**
	 * Test addUserToSimpleRealm
	 * 
	 */
	public void testAddUserToSimpleRealm() {
		RealmSimplePwUserValue realmSimplePwUserValue = new RealmSimplePwUserValue();
		realmSimplePwUserValue.setSimplePwRealmUserId(1);
		realmSimplePwUserValue.setUserName("testRealmUser");
		realmSimplePwUserValue.setPassword("testPassword");
		realmSimplePwUserValue.setRoles("testRole");

		RealmSimplePwUserHbm realmSimplePwUserHbm = new RealmSimplePwUserHbmImpl();
		realmSimplePwUserHbm.setSimplePwRealmUserId(1);
		realmSimplePwUserHbm.setUserName("testRealmUser");
		realmSimplePwUserHbm.setPassword("testPassword");
		realmSimplePwUserHbm.setRoles("testRole");

		RealmSimplePwHbm simplePwRealm = new RealmSimplePwHbmImpl();
		simplePwRealm.setSimplePwRealmId(1);
		simplePwRealm.setLoginPageId("1");
		simplePwRealm.setRealmName("testSimplePwRealm");

		try {
			EasyMock.expect(realmSimplePwUserDaoMock.findByUsernameAndRealmId(realmSimplePwUserValue.getUserName(), 1)).andReturn(null);
			EasyMock.expect(realmSimplePwUserDaoMock.create((RealmSimplePwUserHbm) EasyMock.anyObject())).andReturn(realmSimplePwUserHbm);
			EasyMock.expect(realmSimplePwDaoMock.load(1)).andReturn(simplePwRealm);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(realmSimplePwUserDaoMock);
		EasyMock.replay(realmSimplePwDaoMock);

		try {
			safeguardService.addUserToSimpleRealm(1, realmSimplePwUserValue);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(realmSimplePwUserDaoMock);
		EasyMock.verify(realmSimplePwDaoMock);

	}

	/**
	 * Test IsSafeguardAuthenticationNeeded
	 * expect: isLoginPage and returns false
	 */
	public void testIsSafeguardAuthenticationNeeded() {
		Integer viewComponentId = 1;
		Map safeGuardCookieMap = new Hashtable<Integer, Integer>();
		Collection realm4login = new ArrayList<String>();
		realm4login.add("test1");
		realm4login.add("test2");

		ActiveRealmValue realm = new ActiveRealmValue(false, false, false, false, false, -1, "", "", null);
		realm.setRealmId(1);
		realm.setRealmKey("testKey");
		realm.setRoleNeeded("testRole");

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);
		viewComponent.setRealm4login(realm4login);
		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(viewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(viewComponentDaoMock);

		try {
			boolean result = safeguardService.isSafeguardAuthenticationNeeded(viewComponentId, safeGuardCookieMap);
			Assert.assertEquals(result, false);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(viewComponentDaoMock);
	}

	/**
	 * Test IsSafeguardAuthenticationNeeded 
	 * expect: no realm associated 
	 */
	public void testIsSafeguardAuthenticationNeeded1() {
		Integer viewComponentId = 1;
		Map safeGuardCookieMap = new Hashtable<Integer, Integer>();
		Collection realm4login = new ArrayList<String>();
		Collection realms = new ArrayList<String>();

		ActiveRealmValue realm = new ActiveRealmValue(false, false, false, false, false, -1, "", "", null);
		realm.setRealmId(1);
		realm.setRealmKey("testKey");
		realm.setRoleNeeded("testRole");

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);
		viewComponent.setRealm4login(realm4login);

		ViewComponentHbm parent = new ViewComponentHbmImpl();
		parent.setViewComponentId(2);

		viewComponent.setParent(null);
		try {
			EasyMock.expect(viewComponentDaoMock.load(EasyMock.eq(1))).andReturn(viewComponent).times(2);
			EasyMock.expect(realmSimplePwDaoMock.findByLoginPage("1")).andReturn(realms);
			EasyMock.expect(realmJaasDaoMock.findByLoginPage("1")).andReturn(realms);
			EasyMock.expect(realmLdapDaoMock.findByLoginPage("1")).andReturn(realms);
			EasyMock.expect(realmJdbcDaoMock.findByLoginPage("1")).andReturn(realms);

		} catch (Exception e) {
			Assert.assertTrue(false);
		}
		EasyMock.replay(viewComponentDaoMock);
		EasyMock.replay(realmSimplePwDaoMock);
		EasyMock.replay(realmJaasDaoMock);
		EasyMock.replay(realmJdbcDaoMock);
		EasyMock.replay(realmLdapDaoMock);

		try {
			boolean result = safeguardService.isSafeguardAuthenticationNeeded(viewComponentId, safeGuardCookieMap);
			Assert.assertEquals(false, result);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

	/**
	 * Test AssignLdapRealmToViewComponent. Create a Realm2viewComponent Object and set the properties required.
	 * expect: no exception thrown
	 */
	public void testAssignLdapRealmToViewComponent() {
		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		RealmLdapHbm ldapRealm = new RealmLdapHbmImpl();
		ldapRealm.setLdapRealmId(1);

		Realm2viewComponentHbm realm2viewComponent = new Realm2viewComponentHbmImpl();
		realm2viewComponent.setRealm2viewComponentId(1);

		ViewComponentHbm loginPage = new ViewComponentHbmImpl();
		loginPage.setViewComponentId(2);

		try {
			EasyMock.expect(viewComponentDaoMock.load(1)).andReturn(viewComponent);
			EasyMock.expect(realmLdapDaoMock.load(1)).andReturn(ldapRealm);
			EasyMock.expect(realm2viewComponentDaoMock.findByViewComponent(1)).andReturn(realm2viewComponent);
			EasyMock.expect(viewComponentDaoMock.load(2)).andReturn(loginPage);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);
		EasyMock.replay(realmLdapDaoMock);
		EasyMock.replay(realm2viewComponentDaoMock);

		try {
			safeguardService.assignLdapRealmToViewComponent(1, 1, "testRole", 2);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

	/**
	 * Test AssignSimplePwRealmToViewComponent. Create a Realm2viewComponent Object and set the properties required.
	 * expect: no exception thrown
	 */
	public void testAssignSimplePwRealmToViewComponent() {
		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		RealmSimplePwHbm simpleRealm = new RealmSimplePwHbmImpl();
		simpleRealm.setSimplePwRealmId(1);

		Realm2viewComponentHbm realm2viewComponent = new Realm2viewComponentHbmImpl();
		realm2viewComponent.setRealm2viewComponentId(1);

		ViewComponentHbm loginPage = new ViewComponentHbmImpl();
		loginPage.setViewComponentId(2);

		try {
			EasyMock.expect(viewComponentDaoMock.load(1)).andReturn(viewComponent);
			EasyMock.expect(realmSimplePwDaoMock.load(1)).andReturn(simpleRealm);
			EasyMock.expect(realm2viewComponentDaoMock.findByViewComponent(1)).andReturn(realm2viewComponent);
			EasyMock.expect(viewComponentDaoMock.load(2)).andReturn(loginPage);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);
		EasyMock.replay(realmLdapDaoMock);
		EasyMock.replay(realm2viewComponentDaoMock);

		try {
			safeguardService.assignSimplePwRealmToViewComponent(1, 1, "testRole", 2);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

	/**
	 * Test AssignJaasRealmToViewComponent. Create a Realm2viewComponent Object and set the properties required.
	 * expect: no exception thrown
	 */
	public void testAssignJaasRealmToViewComponent() {
		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		RealmJaasHbm jaasRealm = new RealmJaasHbmImpl();
		jaasRealm.setJaasRealmId(1);

		Realm2viewComponentHbm realm2viewComponent = new Realm2viewComponentHbmImpl();
		realm2viewComponent.setRealm2viewComponentId(1);

		ViewComponentHbm loginPage = new ViewComponentHbmImpl();
		loginPage.setViewComponentId(2);

		try {
			EasyMock.expect(viewComponentDaoMock.load(1)).andReturn(viewComponent);
			EasyMock.expect(realmJaasDaoMock.load(1)).andReturn(jaasRealm);
			EasyMock.expect(realm2viewComponentDaoMock.findByViewComponent(1)).andReturn(realm2viewComponent);
			EasyMock.expect(viewComponentDaoMock.load(2)).andReturn(loginPage);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);
		EasyMock.replay(realmLdapDaoMock);
		EasyMock.replay(realm2viewComponentDaoMock);

		try {
			safeguardService.assignJaasRealmToViewComponent(1, 1, "testRole", 2);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

	/**
	 * Test AssignJdbcRealmToViewComponent. Create a Realm2viewComponent Object and set the properties required.
	 * expect: no exception thrown
	 */
	public void testAssignJdbcRealmToViewComponent() {
		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setViewComponentId(1);

		RealmJdbcHbm sqlRealm = new RealmJdbcHbmImpl();
		sqlRealm.setJdbcRealmId(1);

		Realm2viewComponentHbm realm2viewComponent = new Realm2viewComponentHbmImpl();
		realm2viewComponent.setRealm2viewComponentId(1);

		ViewComponentHbm loginPage = new ViewComponentHbmImpl();
		loginPage.setViewComponentId(2);

		try {
			EasyMock.expect(viewComponentDaoMock.load(1)).andReturn(viewComponent);
			EasyMock.expect(realmJdbcDaoMock.load(1)).andReturn(sqlRealm);
			EasyMock.expect(realm2viewComponentDaoMock.findByViewComponent(1)).andReturn(realm2viewComponent);
			EasyMock.expect(viewComponentDaoMock.load(2)).andReturn(loginPage);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(viewComponentDaoMock);
		EasyMock.replay(realmLdapDaoMock);
		EasyMock.replay(realm2viewComponentDaoMock);

		try {
			safeguardService.assignJdbcRealmToViewComponent(1, 1, "testRole", 2);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test GetSimplePwRealms4CurrentUser
	 */
	public void testGetSimplePwRealms4CurrentUser() {
		UserHbm user = new UserHbmImpl();
		user.setUserId("testUser");

		SiteHbm site = new SiteHbmImpl();
		site.setSiteId(1);

		RealmSimplePwHbm realmSimple = new RealmSimplePwHbmImpl();
		realmSimple.setSimplePwRealmId(1);

		RealmSimplePwValue realmSimpleValue = new RealmSimplePwValue();
		realmSimpleValue.setSimplePwRealmId(1);
		realmSimpleValue.setRealmName("testRealmSimpleName");

		realmSimple.setRealmSimplePwValue(realmSimpleValue);

		Collection<RealmSimplePwHbm> col = new ArrayList<RealmSimplePwHbm>();
		col.add(realmSimple);

		try {
			EasyMock.expect(userDaoMock.load("testUser")).andReturn(user);
			EasyMock.expect(realmSimplePwDaoMock.findByOwnerAndSite("testUser", 1)).andReturn(col);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(userDaoMock);
		EasyMock.replay(realmSimplePwDaoMock);

		try {
			RealmSimplePwValue[] values = safeguardService.getSimplePwRealms4CurrentUser(1);
			Assert.assertEquals(1, values.length);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test GetActiveRealm
	 * expect: for a realm2viewComponent object that has set a simplePwRealm to return that protection as active
	 *         isRealmNone false
	 *         id of the RealmSimplePw
	 */
	public void testGetActiveRealm1() {
		ViewComponentHbm loginpage = new ViewComponentHbmImpl();
		loginpage.setViewComponentId(1);

		RealmSimplePwHbm realmSimple = new RealmSimplePwHbmImpl();
		realmSimple.setSimplePwRealmId(1);

		Realm2viewComponentHbm realm2viewComponent = new Realm2viewComponentHbmImpl();
		realm2viewComponent.setRealm2viewComponentId(1);

		realm2viewComponent.setSimplePwRealm(realmSimple);
		realm2viewComponent.setLoginPage(loginpage);

		try {
			EasyMock.expect(realm2viewComponentDaoMock.findByViewComponent(EasyMock.eq(1))).andReturn(realm2viewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(realm2viewComponentDaoMock);

		try {
			ActiveRealmValue value = safeguardService.getActiveRealm(1);
			Assert.assertEquals(false, value.isRealmNone());
			Assert.assertEquals(true, value.isRealmSimplePw());
			Assert.assertEquals(1, value.getRealmId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(realm2viewComponentDaoMock);
	}

	/**
	 * Test GetActiveRealm
	 * expect: for a realm2viewComponent object that has set a RealmJdbc to return that protection as active
	 *         isRealmNone false
	 *         id of the RealmJdbc
	 */
	public void testGetActiveRealm2() {
		ViewComponentHbm loginpage = new ViewComponentHbmImpl();
		loginpage.setViewComponentId(1);

		RealmJdbcHbm realmJdbc = new RealmJdbcHbmImpl();
		realmJdbc.setJdbcRealmId(1);

		Realm2viewComponentHbm realm2viewComponent = new Realm2viewComponentHbmImpl();
		realm2viewComponent.setRealm2viewComponentId(1);

		realm2viewComponent.setJdbcRealm(realmJdbc);
		realm2viewComponent.setLoginPage(loginpage);

		try {
			EasyMock.expect(realm2viewComponentDaoMock.findByViewComponent(EasyMock.eq(1))).andReturn(realm2viewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(realm2viewComponentDaoMock);

		try {
			ActiveRealmValue value = safeguardService.getActiveRealm(1);
			Assert.assertEquals(false, value.isRealmNone());
			Assert.assertEquals(true, value.isRealmJdbc());
			Assert.assertEquals(1, value.getRealmId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(realm2viewComponentDaoMock);
	}

	/**
	 * Test GetActiveRealm
	 * expect: for a realm2viewComponent object that has set a RealmLdap to return that protection as active
	 *         isRealmNone false
	 *         id of the RealmLdap
	 */
	public void testGetActiveRealm3() {
		ViewComponentHbm loginpage = new ViewComponentHbmImpl();
		loginpage.setViewComponentId(1);

		RealmLdapHbm realmLdap = new RealmLdapHbmImpl();
		realmLdap.setLdapRealmId(1);

		Realm2viewComponentHbm realm2viewComponent = new Realm2viewComponentHbmImpl();
		realm2viewComponent.setRealm2viewComponentId(1);

		realm2viewComponent.setLdapRealm(realmLdap);
		realm2viewComponent.setLoginPage(loginpage);

		try {
			EasyMock.expect(realm2viewComponentDaoMock.findByViewComponent(EasyMock.eq(1))).andReturn(realm2viewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(realm2viewComponentDaoMock);

		try {
			ActiveRealmValue value = safeguardService.getActiveRealm(1);
			Assert.assertEquals(false, value.isRealmNone());
			Assert.assertEquals(true, value.isRealmLdap());
			Assert.assertEquals(1, value.getRealmId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(realm2viewComponentDaoMock);
	}

	/**
	 * Test GetActiveRealm
	 * expect: for a realm2viewComponent object that has set a RealmJaas to return that protection as active
	 *         isRealmNone false
	 *         id of the RealmJaas
	 */
	public void testGetActiveRealm4() {
		ViewComponentHbm loginpage = new ViewComponentHbmImpl();
		loginpage.setViewComponentId(1);

		RealmJaasHbm realmJaas = new RealmJaasHbmImpl();
		realmJaas.setJaasRealmId(1);

		Realm2viewComponentHbm realm2viewComponent = new Realm2viewComponentHbmImpl();
		realm2viewComponent.setRealm2viewComponentId(1);

		realm2viewComponent.setJaasRealm(realmJaas);
		realm2viewComponent.setLoginPage(loginpage);

		try {
			EasyMock.expect(realm2viewComponentDaoMock.findByViewComponent(EasyMock.eq(1))).andReturn(realm2viewComponent);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(realm2viewComponentDaoMock);

		try {
			ActiveRealmValue value = safeguardService.getActiveRealm(1);
			Assert.assertEquals(false, value.isRealmNone());
			Assert.assertEquals(true, value.isRealmJaas());
			Assert.assertEquals(1, value.getRealmId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.verify(realm2viewComponentDaoMock);
	}

	/**
	 * Test GetLoginPath
	 * expect : loginPath for a SimplePwRealm
	 */
	public void testGetLoginPath() {
		Realm2viewComponentHbm realm2viewComponent = new Realm2viewComponentHbmImpl();
		realm2viewComponent.setRealm2viewComponentId(1);

		RealmSimplePwHbm realmSimple = new RealmSimplePwHbmImpl();
		realmSimple.setSimplePwRealmId(1);
		realmSimple.setLoginPageId("1");

		realm2viewComponent.setSimplePwRealm(realmSimple);

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setUrlLinkName("testChildUrlLinkName");

		ViewComponentHbm parent = new ViewComponentHbmImpl();
		parent.setViewComponentId(2);
		parent.setUrlLinkName("parentTestUrlLinkName");

		ViewComponentHbm root = new ViewComponentHbmImpl();
		root.setViewComponentId(3);

		viewComponent.setParent(parent);
		parent.setParent(root);

		try {
			EasyMock.expect(realm2viewComponentDaoMock.findByViewComponent(EasyMock.eq(1))).andReturn(realm2viewComponent);
			EasyMock.expect(viewComponentDaoMock.load(1)).andReturn(viewComponent);
			EasyMock.expect(realmSimplePwDaoMock.load(1)).andReturn(realmSimple);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(realm2viewComponentDaoMock);
		EasyMock.replay(viewComponentDaoMock);
		EasyMock.replay(realmSimplePwDaoMock);

		try {
			String path = safeguardService.getLoginPath(1);
			Assert.assertEquals("parentTestUrlLinkName/testChildUrlLinkName", path);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test GetLoginPath
	 * expect : loginPath for a RealmJaas
	 */
	public void testGetLoginPath1() {
		Realm2viewComponentHbm realm2viewComponent = new Realm2viewComponentHbmImpl();
		realm2viewComponent.setRealm2viewComponentId(1);

		RealmJaasHbm realmJaas = new RealmJaasHbmImpl();
		realmJaas.setJaasRealmId(1);
		realmJaas.setLoginPageId("1");

		realm2viewComponent.setJaasRealm(realmJaas);

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setUrlLinkName("testChildUrlLinkName");

		ViewComponentHbm parent = new ViewComponentHbmImpl();
		parent.setViewComponentId(2);
		parent.setUrlLinkName("parentTestUrlLinkName");

		ViewComponentHbm root = new ViewComponentHbmImpl();
		root.setViewComponentId(3);

		viewComponent.setParent(parent);
		parent.setParent(root);

		try {
			EasyMock.expect(realm2viewComponentDaoMock.findByViewComponent(EasyMock.eq(1))).andReturn(realm2viewComponent);
			EasyMock.expect(viewComponentDaoMock.load(1)).andReturn(viewComponent);
			EasyMock.expect(realmJaasDaoMock.load(1)).andReturn(realmJaas);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(realm2viewComponentDaoMock);
		EasyMock.replay(viewComponentDaoMock);
		EasyMock.replay(realmJaasDaoMock);

		try {
			String path = safeguardService.getLoginPath(1);
			Assert.assertEquals("parentTestUrlLinkName/testChildUrlLinkName", path);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test GetLoginPath
	 * expect : loginPath for a RealmJdbc
	 */
	public void testGetLoginPath2() {
		Realm2viewComponentHbm realm2viewComponent = new Realm2viewComponentHbmImpl();
		realm2viewComponent.setRealm2viewComponentId(1);

		RealmJdbcHbm realmJdbc = new RealmJdbcHbmImpl();
		realmJdbc.setJdbcRealmId(1);
		realmJdbc.setLoginPageId("1");

		realm2viewComponent.setJdbcRealm(realmJdbc);

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setUrlLinkName("testChildUrlLinkName");

		ViewComponentHbm parent = new ViewComponentHbmImpl();
		parent.setViewComponentId(2);
		parent.setUrlLinkName("parentTestUrlLinkName");

		ViewComponentHbm root = new ViewComponentHbmImpl();
		root.setViewComponentId(3);

		viewComponent.setParent(parent);
		parent.setParent(root);

		try {
			EasyMock.expect(realm2viewComponentDaoMock.findByViewComponent(EasyMock.eq(1))).andReturn(realm2viewComponent);
			EasyMock.expect(viewComponentDaoMock.load(1)).andReturn(viewComponent);
			EasyMock.expect(realmJdbcDaoMock.load(1)).andReturn(realmJdbc);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(realm2viewComponentDaoMock);
		EasyMock.replay(viewComponentDaoMock);
		EasyMock.replay(realmJdbcDaoMock);

		try {
			String path = safeguardService.getLoginPath(1);
			Assert.assertEquals("parentTestUrlLinkName/testChildUrlLinkName", path);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test GetLoginPath
	 * expect : loginPath for a RealmLdap
	 */
	public void testGetLoginPath3() {
		Realm2viewComponentHbm realm2viewComponent = new Realm2viewComponentHbmImpl();
		realm2viewComponent.setRealm2viewComponentId(1);

		RealmLdapHbm realmLdap = new RealmLdapHbmImpl();
		realmLdap.setLdapRealmId(1);
		realmLdap.setLoginPageId("1");

		realm2viewComponent.setLdapRealm(realmLdap);

		ViewComponentHbm viewComponent = new ViewComponentHbmImpl();
		viewComponent.setUrlLinkName("testChildUrlLinkName");

		ViewComponentHbm parent = new ViewComponentHbmImpl();
		parent.setViewComponentId(2);
		parent.setUrlLinkName("parentTestUrlLinkName");

		ViewComponentHbm root = new ViewComponentHbmImpl();
		root.setViewComponentId(3);

		viewComponent.setParent(parent);
		parent.setParent(root);

		try {
			EasyMock.expect(realm2viewComponentDaoMock.findByViewComponent(EasyMock.eq(1))).andReturn(realm2viewComponent);
			EasyMock.expect(viewComponentDaoMock.load(1)).andReturn(viewComponent);
			EasyMock.expect(realmLdapDaoMock.load(1)).andReturn(realmLdap);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

		EasyMock.replay(realm2viewComponentDaoMock);
		EasyMock.replay(viewComponentDaoMock);
		EasyMock.replay(realmLdapDaoMock);

		try {
			String path = safeguardService.getLoginPath(1);
			Assert.assertEquals("parentTestUrlLinkName/testChildUrlLinkName", path);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
