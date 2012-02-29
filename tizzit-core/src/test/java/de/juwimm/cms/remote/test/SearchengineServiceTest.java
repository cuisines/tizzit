package de.juwimm.cms.remote.test;

import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.compass.core.Compass;
import org.compass.core.CompassHits;
import org.compass.core.CompassQuery;
import org.compass.core.CompassSession;
import org.compass.core.engine.spellcheck.SearchEngineSpellCheckManager;
import org.compass.core.engine.spellcheck.SearchEngineSpellCheckSuggestBuilder;
import org.compass.core.engine.spellcheck.SearchEngineSpellSuggestions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.powermock.api.easymock.PowerMock.*;
import static junit.framework.Assert.*;

import de.juwimm.cms.search.beans.SearchengineService;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {SearchengineService.class})
@SuppressStaticInitializationFor("de.juwimm.cms.search.beans.SearchengineService")
public class SearchengineServiceTest {
	private static Logger log = Logger.getLogger(SearchengineServiceTest.class);
	SearchengineService service;

	@Before
	public void setUp() {
	}

	@Test
	public void testSearchWebSuggestions() throws Exception {
		String searchItem = "test";
		Integer siteId = 0;
		Integer unitId = null;
		boolean isLiveServer=false;
		String[] searchSugestions = new String[] {"test1", "test2"};
		Map safeGuardCookieMap = createNiceMock(Map.class);

		//mock dependencies
		Compass compass = PowerMock.createNiceMock(Compass.class);
		Logger loggerMock=PowerMock.createNiceMock(Logger.class);
		service = createPartialMock(SearchengineService.class, "buildRatedWildcardQuery");
		Whitebox.setInternalState(service, "compass", compass);
		Whitebox.setInternalState(SearchengineService.class, "log", loggerMock);
		CompassSession compassSession = createNiceMock(CompassSession.class);
		SearchEngineSpellCheckManager spellCheckManager = createNiceMock(SearchEngineSpellCheckManager.class);
		SearchEngineSpellCheckSuggestBuilder suggestBuilder = createNiceMock(SearchEngineSpellCheckSuggestBuilder.class);
		SearchEngineSpellSuggestions spellSuggestions = createNiceMock(SearchEngineSpellSuggestions.class);
		CompassQuery query = createNiceMock(CompassQuery.class);
		CompassHits hits = createNiceMock(CompassHits.class);

		//expected method run
		compass.openSession();
		expectLastCall().andReturn(compassSession);
		compass.getSpellCheckManager();
		expectLastCall().andReturn(spellCheckManager);
		spellCheckManager.suggestBuilder(searchItem);
		expectLastCall().andReturn(suggestBuilder);
		suggestBuilder.numberOfSuggestions(10);
		expectLastCall().andReturn(null);
		suggestBuilder.accuracy(0.3f);
		expectLastCall().andReturn(null);
		suggestBuilder.suggest();
		expectLastCall().andReturn(spellSuggestions);
		loggerMock.info(spellSuggestions.toString());
		expectLastCall();
		spellSuggestions.getSuggestions();
		expectLastCall().andReturn(searchSugestions).times(9);
		for (int i = 0; i < searchSugestions.length; i++) {
			PowerMock.expectPrivate(service, "buildRatedWildcardQuery", compassSession, siteId, unitId, searchSugestions[i], null, safeGuardCookieMap, isLiveServer).andReturn(query);
			query.hits();
			expectLastCall().andReturn(hits);
			hits.getLength();
			expectLastCall().andReturn(2);
		}
		PowerMock.replayAll();

		//actual method call
		String[][] strings=service.searchWebSuggestions(siteId, searchItem, safeGuardCookieMap);
		assertEquals(searchSugestions[0], strings[0][0]);
		assertEquals("2", strings[0][1]);
		assertEquals(searchSugestions[1], strings[1][0]);
		assertEquals("2", strings[1][1]);
		
	}
}
