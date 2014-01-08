package tw.edu.sinica.iis.GUI.Operate;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class TableSelectPanel extends JPanel {

	private static final long serialVersionUID = 8129273028487951122L;

	public JTable table;
	public HDFSTableModel model;

	public TableSelectPanel(String[] str) {
		super();
		this.setLayout(new BorderLayout());
		model = new HDFSTableModel(str);
		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane tmp = new JScrollPane(table);
		this.add(tmp, BorderLayout.CENTER);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
