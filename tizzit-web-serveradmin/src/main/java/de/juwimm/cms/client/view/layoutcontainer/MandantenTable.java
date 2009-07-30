package de.juwimm.cms.client.view.layoutcontainer;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;

import de.juwimm.cms.client.model.MandantenModel;
import de.juwimm.cms.client.util.Constants;
import de.juwimm.cms.client.util.TestData;

public class MandantenTable extends LayoutContainer {
	private EditorGrid<MandantenModel> mandantenTable;
	private int COLUMN_SITE_ID_WIDTH = 100;
	private int COLUMN_SITE_NAME_WIDTH = 100;
	private int COLUMN_SITE_SHORT_WIDTH = 100;
	private int COLUMN_START_SEARCH_WIDTH = 100;
	private int tableWidth = 0;
	private ListStore<MandantenModel> mandantenStore = new ListStore<MandantenModel>();

	public MandantenTable() {
		mandantenStore.add(TestData.getTableTestData());
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();
		column.setId(MandantenModel.SITE_ID);
		column.setHeader(Constants.I18N_CONSTANTS.mandantmodel_column_siteId());
		column.setWidth(COLUMN_SITE_ID_WIDTH);
		tableWidth += COLUMN_SITE_ID_WIDTH;
		configs.add(column);

		column = new ColumnConfig();
		column.setId(MandantenModel.SITE_SHORT);
		column.setHeader(Constants.I18N_CONSTANTS.mandantmodel_column_siteShort());
		column.setEditor(new CellEditor(new TextField<String>()));
		column.setWidth(COLUMN_SITE_SHORT_WIDTH);
		tableWidth += COLUMN_SITE_SHORT_WIDTH;

		configs.add(column);

		column = new ColumnConfig();
		column.setId(MandantenModel.SITE_NAME);
		column.setHeader(Constants.I18N_CONSTANTS.mandantmodel_column_siteName());
		column.setWidth(COLUMN_SITE_NAME_WIDTH);
		tableWidth += COLUMN_SITE_NAME_WIDTH;

		configs.add(column);

		CheckColumnConfig startSearch = new CheckColumnConfig(MandantenModel.START_SEARCH, Constants.I18N_CONSTANTS.mandantmodel_column_startSearch(), COLUMN_START_SEARCH_WIDTH);
		tableWidth += COLUMN_START_SEARCH_WIDTH;

		configs.add(startSearch);
		Log.debug("Table-width: " + tableWidth);
		CheckBoxSelectionModel<MandantenModel> checkBoxSelectionModel = new CheckBoxSelectionModel<MandantenModel>();
		configs.add(checkBoxSelectionModel.getColumn());
		ColumnModel cm = new ColumnModel(configs);

		mandantenTable = new EditorGrid<MandantenModel>(mandantenStore, cm);
		mandantenTable.setSelectionModel(checkBoxSelectionModel);
		mandantenTable.addPlugin(checkBoxSelectionModel);
		mandantenTable.addPlugin(startSearch);
		this.add(mandantenTable);
	}

}
