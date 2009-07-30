package de.juwimm.cms.client.view;

import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.LayoutContainer;

import de.juwimm.cms.client.controller.MandantenController;
import de.juwimm.cms.client.util.Constants;
import de.juwimm.cms.client.view.layoutcontainer.MainContentTabPanel;
import de.juwimm.cms.client.view.layoutcontainer.MandantenButtonPanel;
import de.juwimm.cms.client.view.layoutcontainer.MandantenTable;

public class MandantenView extends View {
	private MandantenTable mandantenTable;
	private LayoutContainer mandantenContent;
	private MandantenButtonPanel mandantenButtonPanel;
	public MandantenView(MandantenController controller) {
		super(controller);
	}

	@Override
	protected void handleEvent(AppEvent< ? > event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initialize() {
		mandantenContent = new LayoutContainer();
		mandantenButtonPanel= new MandantenButtonPanel();
		mandantenTable = new MandantenTable();
		mandantenContent.add(mandantenTable);
		mandantenContent.add(mandantenButtonPanel);
		
		MainContentTabPanel.getInstance().addTab(mandantenContent, Constants.I18N_CONSTANTS.mandantenService());
	}

}
