package de.juwimm.cms.gui.admin;

import static de.juwimm.cms.common.Constants.rb;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.jvnet.flamingo.common.CommandButtonDisplayState;
import org.jvnet.flamingo.common.JCommandMenuButton;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;

import de.juwimm.cms.Messages;
import de.juwimm.cms.common.Constants.LiveserverDeployStatus;
import de.juwimm.cms.gui.controls.ReloadablePanel;
import de.juwimm.cms.gui.ribbon.CommandButtonUI;
import de.juwimm.cms.gui.table.TableSorter;
import de.juwimm.cms.util.Communication;
import de.juwimm.cms.util.UIConstants;
import de.juwimm.cms.vo.EditionValue;

/**
 * @author <a href="florin.zalum@juwimm.com">Florin Zalum</a>
 * @version $Id$
 */
public class PanEditionView extends JPanel implements ReloadablePanel {
	private static final long serialVersionUID = 1L;
	public static final String TitleKey = "panEditionView.title";
	private static Logger log = Logger.getLogger(PanEditionView.class);
	private Communication comm;
	private JPanel editionsControlPanel;
	private JTable editionsTable;
	private JCommandMenuButton refresh;
	private EditionTableModel tableModel;

	public PanEditionView(Communication comm) {
		this.setLayout(new GridBagLayout());
		this.comm = comm;
		tableModel = new EditionTableModel();
		editionsTable = new JTable();
		editionsControlPanel = new JPanel();
		refresh = new JCommandMenuButton(rb.getString("panEditionView.refresh"), ImageWrapperResizableIcon.getIcon(UIConstants.ACTION_TREE_REFRESH.getImage(), new Dimension(16, 16)));
		refresh.setUI(new CommandButtonUI());
		refresh.setDisplayState(CommandButtonDisplayState.MEDIUM);
		refresh.setHorizontalAlignment(SwingUtilities.LEFT);
		editionsControlPanel.setLayout(new GridBagLayout());
		editionsControlPanel.add(refresh, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

		JScrollPane tableScrollPane = new JScrollPane(editionsTable);
		editionsTable.setFillsViewportHeight(true);
		this.editionsTable.setModel(new TableSorter(tableModel, editionsTable.getTableHeader()));
		EditionsTreeCellRenderer cellRenderer = new EditionsTreeCellRenderer();
		editionsTable.getColumnModel().getColumn(0).setMaxWidth(200);
		editionsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		editionsTable.getColumnModel().getColumn(0).setMaxWidth(250);
		editionsTable.getColumnModel().getColumn(0).setPreferredWidth(250);
		editionsTable.getColumnModel().getColumn(2).setMaxWidth(120);
		editionsTable.getColumnModel().getColumn(2).setPreferredWidth(120);
		editionsTable.getColumnModel().getColumn(3).setPreferredWidth(75);
		editionsTable.getColumnModel().getColumn(3).setMaxWidth(75);

		editionsTable.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
		editionsTable.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
		editionsTable.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
		this.add(tableScrollPane, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
		this.add(editionsControlPanel, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

		refresh.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				reload();
			}

		});
	}

	public void reload() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				editionsControlPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				UIConstants.setWorker(true);
				loadEditions();
				UIConstants.setWorker(false);
				editionsControlPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}

	private void loadEditions() {
		List<EditionValue> editions = null;
		try {
			editions = comm.getEditions();
		} catch (Exception e) {
			log.warn("error on loading editions");
			if (log.isDebugEnabled()) {
				log.debug(e);
			}
			return;
		}

		this.tableModel.setRows(editions);
	}

	private class EditionTableModel extends DefaultTableModel {
		public EditionTableModel() {
			super();
			columnIdentifiers.add(rb.getString("panEditionView.table.creator"));
			columnIdentifiers.add(rb.getString("panEditionView.table.userComment"));
			columnIdentifiers.add(rb.getString("panEditionView.table.lastAction"));
			columnIdentifiers.add(rb.getString("panEditionView.table.time"));
			columnIdentifiers.add(rb.getString("panEditionView.table.status"));
		}

		private void addRow(EditionValue value) {
			Vector rowDate = new Vector();
			rowDate.add(value.getCreatorName());
			rowDate.add(value.getComment());
			if (value.getStartActionTimestamp() != null) {
				rowDate.add(new SimpleDateFormat(rb.getString("General.ShortDateTimeFormat")).format(value.getStartActionTimestamp()));
			} else {
				rowDate.add("");
			}
			Long time = null;
			if (value.getStartActionTimestamp() != null) {
				time = value.getEndActionTimestamp().getTime() - value.getStartActionTimestamp().getTime();
				if (time > 0) {
					long hours = time / (1000 * 60 * 60);
					long minutes = (time % (1000 * 60 * 60)) / (1000 * 60);
					long seconds = (time % (1000 * 60)) / 1000;
					rowDate.add(hours + ":" + ((minutes > 9) ? minutes : "0" + minutes) + ":" + ((seconds > 9) ? seconds : "0" + seconds));
				} else {
					rowDate.add(null);
				}
			} else {
				rowDate.add(null);
			}
			if (value != null && value.getDeployState() != null && !value.getDeployState().equals("")) {
				if (value.isException()) {
					String[] stringParams = new String[value.getExtraMessages() == null ? 1 : (value.getExtraMessages().length + 1)];
					int index = 1;
					if (value.getExtraMessages() != null) {
						for (String extraMessage : value.getExtraMessages()) {
							stringParams[index++] = extraMessage;
						}
					}
					stringParams[0] = Messages.getString("panEditionView.status." + value.getDeployState());
					rowDate.add(Messages.getString("panEditionView.status." + LiveserverDeployStatus.Exception.name(), stringParams));
				} else {
					rowDate.add(Messages.getString("panEditionView.status." + value.getDeployState(), value.getExtraMessages()));
				}

			} else {
				rowDate.add("");
			}
			super.addRow(rowDate);
		}

		public void setRows(List<EditionValue> edition) {
			this.dataVector.removeAllElements();
			if (edition != null) {
				for (EditionValue adition : edition) {
					this.addRow(adition);
				}
			}
			fireTableRowsInserted(getRowCount(), getRowCount());
		}
	}

	private static class EditionsTreeCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			switch (column) {
				case 2:
					setHorizontalAlignment(SwingConstants.CENTER);
				case 3:
					setHorizontalAlignment(SwingConstants.CENTER);
					break;
			}
			return this;
		}
	}

	public void save() {

	}

	public void unload() {

	}
}
