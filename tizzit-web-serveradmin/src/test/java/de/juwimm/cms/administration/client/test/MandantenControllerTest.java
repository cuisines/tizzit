package de.juwimm.cms.administration.client.test;

import com.google.gwt.junit.client.GWTTestCase;

import de.juwimm.cms.client.controller.MandantenController;

public class MandantenControllerTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "de.juwimm.cms.ConquestAdministrationView";
	}
	
	public void testInitController(){
		MandantenController controller = new MandantenController();
		assertNotNull(controller);
	}

}
