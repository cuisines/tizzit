package de.juwimm.cms.gui.admin;

import static de.juwimm.cms.common.Constants.rb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.juwimm.cms.gui.controls.GradientBar;
import de.juwimm.cms.util.SmallSiteConfigReader;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.SiteValue;
import de.juwimm.swing.PickListData;
import de.juwimm.swing.PickListPanel;
import de.juwimm.swing.SortableListModel;

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
	private JButton addUrlButton;
	private JButton removeUrlButton;
	private JTextField urlInput;
	private JLabel urlLabel;
	private JLabel depthLabel;
	private JSpinner depthInput;
	private JScrollPane urlTableScrollPane;
	private JButton okButton;
	private JButton cancelButton;
	private SmallSiteConfigReader configReader;
	private final Color backgroundColorError = new Color(0xed4044);
	private GradientBar headerPanel;

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
		depthInput.setMinimumSize(new Dimension(40, 20));
		urlLabel.setSize(new Dimension(70, 12));
		urlLabel.setPreferredSize(new Dimension(70, 12));
		urlLabel.setMinimumSize(new Dimension(70, 12));

		urlTable.getColumnModel().getColumn(0).setWidth(50);
		urlTable.setShowGrid(false);
		urlTable.setMaximumSize(new Dimension(150, 300));
		urlTableScrollPane = new JScrollPane(urlTable);
		urlTableScrollPane.setMinimumSize(new Dimension(400, 100));
		urlPanel = new JPanel(new GridBagLayout());
		urlInput = new JTextField();
		urlInput.setColumns(13);
		urlInput.setMinimumSize(new Dimension(150, 20));

		addUrlButton = new JButton(rb.getString("dialog.externalSearchConfig.add"), new ImageIcon(UIConstants.ICON_PLUS.getImage()));
		addUrlButton.addMouseListener(new MyMouseListener());
		addUrlButton.setOpaque(false);
		removeUrlButton = new JButton(rb.getString("dialog.externalSearchConfig.remove"), new ImageIcon(UIConstants.ICON_MINUS.getImage()));
		removeUrlButton.addMouseListener(new MyMouseListener());
		removeUrlButton.setOpaque(false);

		urlTable.setFillsViewportHeight(true);
		urlTable.setTableHeader(null);

		headerPanel = new GradientBar();
		headerPanel.setLayout(new BorderLayout());
		JLabel label = new JLabel(rb.getString("dialog.externalSearchConfig.header"));
		label.setMinimumSize(new Dimension(100, 100));
		label.setForeground(Color.white);
		label.setFont(new Font("SansSerif", Font.BOLD, 14));
		headerPanel.add(label, BorderLayout.LINE_START);

		urlPanel.add(depthLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
		urlPanel.add(depthInput, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
		urlPanel.add(urlLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(14, 0, 0, 0), 0, 0));
		urlPanel.add(urlInput, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(11, 0, 0, 100), 0, 0));
		urlPanel.add(addUrlButton, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
		urlPanel.add(removeUrlButton, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(15, 4, 0, 0), 0, 0));
		urlPanel.add(urlTableScrollPane, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(headerPanel, BorderLayout.PAGE_START);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		contentPanel.add(urlPanel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		contentPanel.add(filtersPanel, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 0, 0));
		contentPanel.add(protocolsPanel, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 0, 0));
		contentPanel.add(okButton, new GridBagConstraints(1, 4, 1, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 100, 3, 300), 0, 0));
		contentPanel.add(cancelButton, new GridBagConstraints(1, 4, 1, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 200, 3, 200), 0, 0));

		this.getContentPane().add(contentPanel, BorderLayout.CENTER);

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
		this.setSize(600, 850);
		this.setResizable(false);
		this.setLocationRelativeTo(UIConstants.getMainFrame());
		this.setModal(true);
		this.setTitle(rb.getString("dialog.title"));
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
		private JButton addButton;
		private JButton removeButton;
		private JTextField valueInput;
		private JTable positiveNegativeTable;
		private ButtonGroup buttonGroup = new ButtonGroup();
		private JRadioButton positiveRadioButton;
		private JRadioButton negativeRadioButton;
		private JScrollPane tableScrollpane;
		private String propertyToEdit;

		private PickListData pickListData;
		private PickListPanel pickListPanel;

		public PositiveNegativePanel(String title, SiteValue site, String propertyToEdit) {
			super();
			this.propertyToEdit = propertyToEdit;
			initComponents(title, site);

		}

		public void saveValues() {
			List<String> positive = new ArrayList<String>();
			for (int i = (pickListData.getLstLeftModel().getSize() - 1); i >= 0; i--) {
				Object current = pickListData.getLstLeftModel().getElementAt(i);
				positive.add(current.toString());
			}
			List<String> negative = new ArrayList<String>();
			for (int i = (pickListData.getLstRightModel().getSize() - 1); i >= 0; i--) {
				Object current = pickListData.getLstRightModel().getElementAt(i);
				negative.add(current.toString());
			}
			configReader.saveValues(SmallSiteConfigReader.getPositiveListTag(propertyToEdit), positive);
			configReader.saveValues(SmallSiteConfigReader.getNegativeListTag(propertyToEdit), negative);

		}

		private void initComponents(String title, SiteValue site) {
			this.setLayout(new GridBagLayout());
			titleLabel = new JLabel(title);
			titleLabel.setSize(new Dimension(70, 12));
			titleLabel.setPreferredSize(new Dimension(70, 12));
			titleLabel.setMinimumSize(new Dimension(70, 12));

			valueInput = new JTextField();
			valueInput.setMinimumSize(new Dimension(150, 20));
			valueInput.setPreferredSize(new Dimension(150, 20));

			this.pickListData = new PickListData(new SortableListModel(), new SortableListModel());
			fillPickListData(propertyToEdit);
			this.pickListData.setLeftLabel(rb.getString("dialog.externalSearchConfig.positive"));
			this.pickListData.setRightLabel(rb.getString("dialog.externalSearchConfig.negative"));
			this.pickListPanel = new PickListPanel(this.pickListData, false);
			this.pickListPanel.setEnabled(true);

			addButton = new JButton(rb.getString("dialog.externalSearchConfig.add"), new ImageIcon(UIConstants.ICON_PLUS.getImage()));
			addButton.addMouseListener(new MyMouseListener());
			addButton.setOpaque(false);

			removeButton = new JButton(rb.getString("dialog.externalSearchConfig.remove"), new ImageIcon(UIConstants.ICON_MINUS.getImage()));
			removeButton.addMouseListener(new MyMouseListener());
			removeButton.setOpaque(false);
			removeButton.setMaximumSize(new Dimension(15, 10));

			this.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(4, 0, 0, 0), 0, 0));
			this.add(valueInput, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(1, 0, 0, 100), 0, 0));
			this.add(addButton, new GridBagConstraints(1, 0, 2, 0, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			this.add(removeButton, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
			this.add(pickListPanel, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

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
					pickListPanel.removeSelectedItem();
				}

			});
		}

		private void fillPickListData(String property) {
			List<String> positives = configReader.readValues(SmallSiteConfigReader.getPositiveListTag(property));
			List<String> negatives = configReader.readValues(SmallSiteConfigReader.getNegativeListTag(property));
			for (String value : positives) {
				pickListData.getLstLeftModel().addElement(value);
			}
			for (String value : negatives) {
				pickListData.getLstRightModel().addElement(value);
			}
		}

		private void insertNewValue() {
			if (valueInput.getText() == null || valueInput.getText().equals("")) {
				return;
			}
			pickListPanel.insertElementInLeftList(valueInput.getText());
		}
	}

}

class MyMouseListener implements MouseListener {

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		JButton button = (JButton) e.getComponent();
		button.setOpaque(true);

	}

	public void mouseExited(MouseEvent e) {
		JButton button = (JButton) e.getComponent();
		button.setOpaque(false);

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
