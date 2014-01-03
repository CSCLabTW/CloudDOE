package tw.edu.sinica.iis.GUI.Operate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class UploadPanel extends JPanel {

	private static final long serialVersionUID = -2797660625566368460L;

	public int default_w = 400;
	public int default_h = 300;

	public JPanel LoadPanel;
	public JPanel RemoteViewPanel;

	public JTable FileTable;
	public HDFSTableModel TableData;

	public JButton LoadButton;
	public JButton UploadButton;
	public JButton RefreshButton;
	public JButton RemoveButton;

	public JLabel LoadPath;
	public String UploadPath;

	public UploadPanel() {
		super();
		init(default_w, default_h);
	}

	public UploadPanel(int w, int h) {
		super();
		init(w, h);
	}

	public void init(int w, int h) {
		// this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS ));
		this.setLayout(null);
		// this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setSize(w, h);
		this.setPreferredSize(new Dimension(w, h));

		this.add(getRemoteViewPanel());
		JLabel step_tip = new JLabel("Step 2. Upload the fastq file(s)");
		step_tip.setHorizontalAlignment(SwingConstants.LEFT);
		step_tip.setForeground(Color.BLUE);
		step_tip.setFont(new Font(step_tip.getFont().getFontName(), step_tip
				.getFont().getStyle(), 15));
		step_tip.setBounds(10, 5, 400, 15);
		this.add(step_tip);
		// this.add(getLoadPanel());
	}

	public JPanel getRemoteViewPanel() {
		if (RemoteViewPanel == null) {
			RemoteViewPanel = new JPanel();
			RemoteViewPanel.setLayout(null);
			// RemoteViewPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			RemoteViewPanel.setBounds(10, 25, 380, 275);

			// TableData = new HDFSTableModel(getData());
			TableData = new HDFSTableModel();
			FileTable = new JTable();
			FileTable.setModel(TableData);
			FileTable.setRowHeight(22);
			FileTable.setAutoscrolls(true);
			JScrollPane tmpTable = new JScrollPane(FileTable);
			tmpTable.setBounds(0, 0, 380, 225);
			RemoteViewPanel.add(tmpTable);

			JPanel ButtonPanel = new JPanel();
			ButtonPanel.setLayout(new GridLayout(1, 2, 10, 10));
			ButtonPanel.setBounds(10, 235, 360, 30);
			ButtonPanel.add(getUploadButton());
			ButtonPanel.add(getRemoveButton());
			// ButtonPanel.add(Box.createHorizontalGlue());
			ButtonPanel.add(getRefreshButton());
			RemoteViewPanel.add(ButtonPanel);
		}
		return RemoteViewPanel;
	}

	public String[] getData() {
		String[] data = { "work010001", "work010002", "work010005" };
		return data;
	}

	public JPanel getLoadPanel() {
		if (LoadPanel == null) {
			LoadPanel = new JPanel();
			LoadPanel.setLayout(new BorderLayout());
			// LoadPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			LoadPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			LoadPanel.setBounds(10, 200, 380, 90);
			// LoadPanel.setMaximumSize(new Dimension(380, 90));

			JPanel tmpPanel = new JPanel();
			tmpPanel.setLayout(new BorderLayout());
			tmpPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

			JLabel t1 = new JLabel();
			t1.setText("Upload file from local :");
			tmpPanel.add(t1, BorderLayout.NORTH);

			LoadPath = new JLabel();
			LoadPath.setText("[Upload Path...]");
			LoadPath.setBorder(BorderFactory.createLineBorder(Color.black));
			tmpPanel.add(LoadPath, BorderLayout.CENTER);

			JPanel Line = new JPanel();
			Line.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			Line.setLayout(new GridLayout(1, 2, 10, 10));

			Line.add(Box.createHorizontalGlue());
			Line.add(getLoadButton());
			Line.add(getUploadButton());
			Line.add(Box.createHorizontalGlue());
			tmpPanel.add(Line, BorderLayout.SOUTH);

			LoadPanel.add(tmpPanel);
		}
		return LoadPanel;
	}

	public JButton getLoadButton() {
		if (LoadButton == null) {
			LoadButton = new JButton("Open");
		}
		return LoadButton;
	}

	public JButton getUploadButton() {
		if (UploadButton == null) {
			UploadButton = new JButton("Upload");
		}
		return UploadButton;
	}

	public JButton getRefreshButton() {
		if (RefreshButton == null) {
			RefreshButton = new JButton("Refresh");
		}
		return RefreshButton;
	}

	public JButton getRemoveButton() {
		if (RemoveButton == null) {
			RemoveButton = new JButton("Remove");
		}
		return RemoveButton;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame test = new JFrame();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.add(new UploadPanel());
		test.pack();
		test.setVisible(true);
	}
}
