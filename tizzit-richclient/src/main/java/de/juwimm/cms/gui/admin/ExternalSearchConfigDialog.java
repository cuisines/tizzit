package de.juwimm.cms.gui.admin;

import static de.juwimm.cms.common.Constants.rb;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jvnet.flamingo.common.JCommandMenuButton;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;

import de.juwimm.cms.util.SmallSiteConfigReader;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.SiteValue;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class ExternalSearchConfigDialog extends JDialog {
	private static Log log = LogFactory.getLog(ExternalSearchConfigDialog.class);
	private static final long serialVersionUID = 1L;
	private static final Number DEFAULT_DEPTH = 2;
	private SiteValue site;
	private JTable urlTable;
	private PositiveNegativePanel filtersPanel;
	private PositiveNegativePanel protocolsPanel;
	private JPanel urlPanel;
	private JCommandMenuButton addUrlButton;
	private JCommandMenuButton removeUrlButton;
	private JTextField urlInput;
	private JLabel urlLabel;
	private JLabel depthLabel;
	private JSpinner depthInput;
	private JScrollPane urlTableScrollPane;
	private JButton okButton;
	private JButton cancelButton;
	private SmallSiteConfigReader configReader;
	private final Color backgroundColorError = new Color(0xed4044);

	public ExternalSearchConfigDialog(SiteValue site) {
		super();
		this.site = site;
		initialize();
		initControls();
	}

	private void initControls() {
		okButton = new JButton(rb.getString("dialog.ok"));
		cancelButton = new JButton(rb.getString("dialog.cancel"));
		configReader = new SmallSiteConfigReader(site);
		urlTable = new JTable(new UrlTableModel(site));
		filtersPanel = new PositiveNegativePanel(rb.getString("dialog.externalSearchConfig.filters"), site, "filters");
		protocolsPanel = new PositiveNegativePanel(rb.getString("dialog.externalSearchConfig.protocols"), site, "protocols");
		urlLabel = new JLabel(rb.getString("dialog.externalSearchConfig.urls"));
		depthLabel = new JLabel(rb.getString("dialog.externalSearchConfig.depth"));
		String depthValue = configReader.readValue(SmallSiteConfigReader.EXTERNAL_SEARCH_DEPTH_PATH);
		Number depth = depthValue == null || depthValue.equals("") ? DEFAULT_DEPTH : Integer.valueOf(depthValue);
		depthInput = new JSpinner();
		SpinnerModel spinnerModel = new SpinnerNumberModel(depth, 0, 8, 1);
		depthInput.setModel(spinnerModel);

		urlTable.getColumnModel().getColumn(0).setWidth(50);
		urlTable.setShowGrid(false);
		urlTable.setMaximumSize(new Dimension(150, 300));
		urlTableScrollPane = new JScrollPane(urlTable);
		urlTableScrollPane.setMinimumSize(new Dimension(400, 100));
		urlPanel = new JPanel(new GridBagLayout());
		urlInput = new JTextField();
		urlInput.setColumns(13);
		addUrlButton = new JCommandMenuButton("", ImageWrapperResizableIcon.getIcon(UIConstants.ICON_PLUS.getImage(), new Dimension(16, 16)));
		removeUrlButton = new JCommandMenuButton("", ImageWrapperResizableIcon.getIcon(UIConstants.ICON_MINUS.getImage(), new Dimension(16, 16)));

		urlTable.setFillsViewportHeight(true);
		urlTable.setTableHeader(null);

		urlPanel.add(depthLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		urlPanel.add(depthInput, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		urlPanel.add(urlLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		urlPanel.add(urlInput, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 0), 0, 0));
		urlPanel.add(addUrlButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		urlPanel.add(removeUrlButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		urlPanel.add(urlTableScrollPane, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.getContentPane().setLayout(new GridBagLayout());
		this.getContentPane().add(urlPanel, new GridBagConstraints(0, 0, 2, 1, 0.1, 0.1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(filtersPanel, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(protocolsPanel, new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(okButton, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 0), 0, 0));
		this.getContentPane().add(cancelButton, new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 0), 0, 0));

		removeUrlButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = urlTable.getSelectedRows();
				if (selectedRows != null && selectedRows.length > 0) {
					for (int i = 0; i < selectedRows.length; i++) {
						((DefaultTableModel) urlTable.getModel()).removeRow(selectedRows[i]);
					}
				}

			}

		});

		addUrlButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				insertNewUrl();
			}

		});

		urlInput.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				insertNewUrl();
			}

		});

		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (validateForm()) {
					saveUrls();
				}

			}

		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();

			}
		});

	}

	private boolean validateForm() {
		List<String> urls = ((UrlTableModel) urlTable.getModel()).getValues();
		if (urls == null || urls.size() == 0) {
			urlTable.setBorder(BorderFactory.createLineBorder(backgroundColorError));
			return false;
		}
		return true;
	}

	/**
	 * 
	 */
	private void insertNewUrl() {
		if (urlInput.getText() == null || urlInput.getText().equals("")) {
			return;
		}
		if (urlTable.getBorder() != null) {
			urlTable.setBorder(null);
		}
		((DefaultTableModel) urlTable.getModel()).addRow(new Object[] {urlInput.getText()});
		urlInput.setText("");
	}

	private void saveUrls() {
		configReader.setConfdoc(site);
		configReader.saveValue(SmallSiteConfigReader.EXTERNAL_SEARCH_DEPTH_PATH, depthInput.getValue().toString());
		configReader.saveValues(SmallSiteConfigReader.EXTERNAL_SEARCH_URLS_PATH, ((UrlTableModel) urlTable.getModel()).getValues());
		filtersPanel.saveValues();
		protocolsPanel.saveValues();
		configReader.updateConfigXml(site);
		this.setVisible(false);
	}

	private void cancel() {
		this.setVisible(false);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(500, 420);
		this.setResizable(false);
		this.setLocationRelativeTo(UIConstants.getMainFrame());
		this.setModal(true);
		this.setTitle(rb.getString("dialog.title"));
	}

	private class PositiveNegativeTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		public PositiveNegativeTableModel(SiteValue site, String property) {
			super();
			this.columnIdentifiers.add("");
			this.columnIdentifiers.add("");
			readData(property);
		}

		private void readData(String property) {

			List<String> positives = configReader.readValues(SmallSiteConfigReader.getPositiveListTag(property));
			List<String> negatievs = configReader.readValues(SmallSiteConfigReader.getNegativeListTag(property));
			if (positives == null || negatievs == null) {
				return;
			}
			int pozLen = positives.size();
			int negLen = negatievs.size();
			int maxLength = Math.max(pozLen, negLen);
			if (maxLength == 0) {
				return;
			}
			for (int i = 0; i < maxLength; i++) {
				Object[] data = new Object[2];
				if (i < pozLen) {
					data[0] = positives.get(i);
				}
				if (i < negLen) {
					data[1] = negatievs.get(i);
				}
				this.addRow(data);
			}
		}

		public List<String> getValues(int columnIndex) {
			List<String> result = new ArrayList<String>();
			for (int i = 0; i < this.getRowCount(); i++) {
				Object value = this.getValueAt(i, columnIndex);
				if (value == null || ((String) value).equals("")) {
					continue;
				}
				result.add((String) value);
			}
			return result;
		}
	}

	private class UrlTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		private SiteValue site;

		public UrlTableModel(SiteValue siteValue) {
			super();
			this.columnIdentifiers.add("");
			site = siteValue;
			readData();
		}

		private void readData() {
			List<String> values = configReader.readValues(SmallSiteConfigReader.EXTERNAL_SEARCH_URLS_PATH);
			if (values == null || values.size() == 0) {
				return;
			}
			for (String value : values) {
				Vector data = new Vector();
				data.add(value);
				this.dataVector.add(data);
			}
		}

		@Override
		public void removeRow(int row) {
			super.removeRow(row);
		}

		public List<String> getValues() {
			List<String> result = new ArrayList<String>();
			for (int i = 0; i < this.getRowCount(); i++) {
				Object value = this.getValueAt(i, 0);
				if (value == null || ((String) value).equals("")) {
					continue;
				}
				result.add((String) value);
			}
			return result;
		}

	}

	private class PositiveNegativePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private JLabel titleLabel;
		private JCommandMenuButton addButton;
		private JCommandMenuButton removeButton;
		private JTextField valueInput;
		private JTable positiveNegativeTable;
		private ButtonGroup buttonGroup = new ButtonGroup();
		private JRadioButton positiveRadioButton;
		private JRadioButton negativeRadioButton;
		private JScrollPane tableScrollpane;
		private String propertyToEdit;

		public PositiveNegativePanel(String title, SiteValue site, String propertyToEdit) {
			super();
			this.propertyToEdit = propertyToEdit;
			initComponents(title, site);

		}

		public void saveValues() {
			configReader.saveValues(SmallSiteConfigReader.getPositiveListTag(propertyToEdit), ((PositiveNegativeTableModel) positiveNegativeTable.getModel()).getValues(0));
			configReader.saveValues(SmallSiteConfigReader.getNegativeListTag(propertyToEdit), ((PositiveNegativeTableModel) positiveNegativeTable.getModel()).getValues(1));
		}

		private void initComponents(String title, SiteValue site) {
			this.setLayout(new GridBagLayout());
			titleLabel = new JLabel(title);
			valueInput = new JTextField();
			positiveRadioButton = new JRadioButton(rb.getString("dialog.externalSearchConfig.positive"));
			negativeRadioButton = new JRadioButton(rb.getString("dialog.externalSearchConfig.negative"));

			buttonGroup.add(positiveRadioButton);
			buttonGroup.add(negativeRadioButton);
			positiveNegativeTable = new JTable(new PositiveNegativeTableModel(site, propertyToEdit));
			positiveNegativeTable.setShowGrid(false);
			positiveNegativeTable.setCellSelectionEnabled(true);
			positiveNegativeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableScrollpane = new JScrollPane(positiveNegativeTable);
			tableScrollpane.setMinimumSize(new Dimension(215, 120));
			positiveNegativeTable.setTableHeader(null);
			positiveNegativeTable.setFillsViewportHeight(true);
			valueInput.setColumns(13);

			addButton = new JCommandMenuButton("", ImageWrapperResizableIcon.getIcon(UIConstants.ICON_PLUS.getImage(), new Dimension(16, 16)));
			removeButton = new JCommandMenuButton("", ImageWrapperResizableIcon.getIcon(UIConstants.ICON_MINUS.getImage(), new Dimension(16, 16)));

			this.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			this.add(valueInput, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 0), 0, 0));
			this.add(addButton, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			this.add(removeButton, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			this.add(positiveRadioButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			this.add(negativeRadioButton, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			this.add(tableScrollpane, new GridBagConstraints(0, 3, 5, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			addButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					insertNewValue();
				}

			});

			valueInput.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					insertNewValue();
				}
			});

			removeButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					positiveNegativeTable.getSelectedRow();
					DefaultTableModel model = (DefaultTableModel) positiveNegativeTable.getModel();
					int column = positiveNegativeTable.getSelectedColumn();
					int row = positiveNegativeTable.getSelectedRow();
					model.setValueAt("", row, column);
					//move values
					int i = row;
					for (; i < model.getRowCount() - 1; i++) {
						String value = (String) model.getValueAt(i + 1, column);
						if (value != null && value != "") {
							model.setValueAt(value, i, column);
						} else {
							break;
						}
					}

					//check last row
					String nearValue = (String) model.getValueAt(model.getRowCount() - 1, (column + 1) % 2);
					if (nearValue == null || nearValue == "") {
						model.removeRow(model.getRowCount() - 1);
					} else {
						model.setValueAt("", i, column);
					}

				}

			});
		}

		private void insertNewValue() {
			if (valueInput.getText() == null || valueInput.getText().equals("")) {
				return;
			}
			int column;
			if (positiveRadioButton.isSelected()) {
				column = 0;
			} else if (negativeRadioButton.isSelected()) {
				column = 1;
			} else {
				return;
			}

			DefaultTableModel model = ((DefaultTableModel) positiveNegativeTable.getModel());
			int i = model.getRowCount() - 1;
			if (model.getRowCount() == 0 || (model.getValueAt(i, column) != null && !model.getValueAt(i, column).equals(""))) {
				//add new row;
				Object[] data = new Object[2];
				data[column] = valueInput.getText();
				model.addRow(data);
				urlInput.setText("");
				return;
			}

			while (i > 0) {
				i--;
				String value = (String) model.getValueAt(i, column);
				if (value == null || value == "") {
					continue;
				} else {
					model.setValueAt(valueInput.getText(), i + 1, column);
					urlInput.setText("");
					return;
				}
			}

			model.setValueAt(valueInput.getText(), 0, column);
		}
	}

}
