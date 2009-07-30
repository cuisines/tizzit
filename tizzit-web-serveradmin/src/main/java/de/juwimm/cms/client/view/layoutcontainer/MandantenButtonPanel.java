package de.juwimm.cms.client.view.layoutcontainer;

import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;

public class MandantenButtonPanel extends HorizontalPanel {
	public MandantenButtonPanel() {
		Button refresh = new Button("Aktualisieren");
		Button startSiteSearch = new Button("Starte Suchindizierung für gewählte Seiten");

		this.add(refresh);
		this.add(startSiteSearch);

	}
}
