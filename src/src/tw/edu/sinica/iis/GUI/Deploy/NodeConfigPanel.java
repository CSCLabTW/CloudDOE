package tw.edu.sinica.iis.GUI.Deploy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

public class NodeConfigPanel extends JPanel {
	private static final long serialVersionUID = 322834861L;

	private int default_width = 750;
	
	public JTextField IPs = null;
	public JTextField UserName = null;
	public JTextField PSWtext = null;
	public JButton NodeAdd = null;
	public JButton NodeDel = null;
	public JTable NodeTable = null;
	public NodeTableModel TableContent = null;

	public JTextField pIPs = null;
	public JTextField pUserName = null;
	public JTextField pPSWtext = null;
	
	public NodeConfigPanel() {
		super();
		init();
	}

	public void init() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel title = new JPanel();
		title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		title.setLayout(new BorderLayout());
		JLabel titleL = new JLabel("<HTML>Please provide access information of the cluster nodes including <BR>"+
				" privileged user name/password and IP address of each cluster node.</HTML>");
		title.add(titleL,BorderLayout.LINE_START);
		this.add(title);
		
		this.add(getNNPanel());
		
		JPanel DNpanel = new JPanel();
//		DNpanel.setBorder(BorderFactory.createTitledBorder("Data Nodes"));
		DNpanel.setLayout(new BoxLayout(DNpanel, BoxLayout.PAGE_AXIS));
		JPanel tmp = new JPanel();
		tmp.setLayout(new BorderLayout());
		tmp.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
		tmp.add(new JLabel("<HTML><FONT color='#FF0000'>Data Nodes</FONT></HTML>"),BorderLayout.LINE_START);
		DNpanel.add(tmp);
		DNpanel.add(getUpPanel());
		DNpanel.add(getTablePanel());
		this.add(DNpanel);
		
		

	}

	public JPanel getNNPanel(){
		JPanel NNPanel = new JPanel();
		NNPanel.setLayout(new BoxLayout(NNPanel, BoxLayout.PAGE_AXIS));
//		NNPanel.setBorder(BorderFactory.createTitledBorder("Name Node"));
		
		JPanel title = new JPanel();
		title.setLayout(new BorderLayout());
		title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		title.add(new JLabel("<HTML><FONT color='#FF0000'>IP of the Name Node </FONT>if it differs from the one for external PCs.</HTML>"),BorderLayout.LINE_START);
		NNPanel.add(title);
		
		FocusAdapter fa = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				NodeStructure ns = TableContent.getNN();
				if(ns!=null){
					ns.IP = pIPs.getText();
//					ns.User = pUserName.getText();
//					ns.Password = pPSWtext.getText();
					TableContent.fireTableDataChanged();
				}
				
			}
		};
		
		JPanel txs = new JPanel();
		txs.setLayout(new BoxLayout(txs, BoxLayout.LINE_AXIS));
		txs.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		Dimension inputSize = new Dimension(100, 25);
		txs.add(new JLabel(" IP : "));
		pIPs = new JTextField();
		pIPs.setPreferredSize(inputSize);
		pIPs.setMaximumSize(inputSize);
		pIPs.addFocusListener(fa);
		txs.add(pIPs);
		
		txs.add(Box.createGlue());	
		
		NNPanel.add(txs);
		
		return NNPanel;
	}
	
	public JPanel getUpPanel() {
		JPanel UpPanel = new JPanel();
		UpPanel.setMaximumSize(new Dimension(default_width, 30));
		UpPanel.setLayout(new BoxLayout(UpPanel, BoxLayout.LINE_AXIS));
		UpPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		UpPanel.add(getInputPanel());
		UpPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		UpPanel.add(getButtonPanel());
		return UpPanel;
	}

	public JPanel getInputPanel() {
		JPanel InputPanel = new JPanel();
		InputPanel.setLayout(new BoxLayout(InputPanel, BoxLayout.LINE_AXIS));
		InputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		InputPanel.add(new JLabel("IP: "));

		Dimension inputSize = new Dimension(100, 25);
		
		IPs = new JTextField();
		IPs.setPreferredSize(inputSize);
		IPs.setText("0.0.0.0");
		InputPanel.add(IPs);

		InputPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		InputPanel.add(new JLabel(" UserName: "));
		UserName = new JTextField();
		UserName.setPreferredSize(inputSize);
		InputPanel.add(UserName);

		InputPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		InputPanel.add(new JLabel(" Password: "));
		PSWtext = new JTextField();
		PSWtext.setPreferredSize(inputSize);
		InputPanel.add(PSWtext);

		return InputPanel;
	}

	public JPanel getButtonPanel() {
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setLayout(new BoxLayout(ButtonPanel, BoxLayout.LINE_AXIS));
		ButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		ButtonPanel.add(getNodeAdd());
		ButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		ButtonPanel.add(getNodeDel());
		return ButtonPanel;
	}
	
	public void modelReload(){
		TableContent.clear();
		TableContent.add(Utils.loadHostFiles());
		NodeStructure ns = TableContent.getNN();
		pIPs.setText(ns.IP);
		//pUserName.setText(ns.User);
		//pPSWtext.setText(ns.Password);
		
		/* user friendly, to fill information as name node */
		UserName.setText(ns.User);
		PSWtext.setText(ns.Password);
		
		TableContent.fireTableDataChanged();
	}
	
	public JPanel getTablePanel() {
		JPanel TablePanel = new JPanel();
		TablePanel.setMaximumSize(new Dimension(default_width, 300));
		TablePanel.setLayout(new BoxLayout(TablePanel, BoxLayout.PAGE_AXIS));
		TablePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		TableContent = new NodeTableModel() {
			private static final long serialVersionUID = 8328439265L;

			@Override
			public void ChangeEvent() {
				Utils.saveHostFiles(this.Content);
			}

		};
		
//		modelReload();

		NodeTable = new JTable(TableContent);
		NodeTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		NodeTable.getColumnModel().getColumn(0).setMaxWidth(200);
		NodeTable.getColumnModel().getColumn(0).setMinWidth(200);
		NodeTable.getColumnModel().getColumn(1).setMaxWidth(200);
		NodeTable.getColumnModel().getColumn(1).setMinWidth(200);
		NodeTable.setRowHeight(25);
		
		/* used to solve the first time into stage two, the NN will show in DN list*/
		/* TODO: need refinement */
		TableRowSorter<NodeTableModel> sorter = new TableRowSorter<NodeTableModel>(TableContent);
		sorter.setRowFilter(new RowFilter<NodeTableModel,Integer>() {
			public boolean include(Entry<? extends NodeTableModel, ? extends Integer> entry) {
				return !(pIPs.getText().equals(entry.getValue(0)));
			}
		});
		NodeTable.setRowSorter(sorter);

		JScrollPane ScrollPanel = new JScrollPane(NodeTable);
		TablePanel.add(ScrollPanel);
		TablePanel.setPreferredSize(new Dimension(100, 150));

		return TablePanel;
	}

	public JButton getNodeAdd() {
		if (NodeAdd == null) {
			NodeAdd = new JButton("Add");
			NodeAdd.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (checkValid()) {
						if(!TableContent.checkDup(IPs.getText())) {
							TableContent.add(IPs.getText(), UserName.getText(),
									new String(PSWtext.getText()));
							IPs.setText("0.0.0.0");
							//PSWtext.setText("");
							//UserName.setText("");
							TableContent.fireTableDataChanged();
						} else {
							JOptionPane.showMessageDialog(null, "ERROR: Information is duplicated.");
						}
					} else {
						JOptionPane.showMessageDialog(null, "ERROR: Information format is invalid.");
					}
				}
			});
		}
		return NodeAdd;
	}

	public JButton getNodeDel() {
		if (NodeDel == null) {
			NodeDel = new JButton("Delete");
			NodeDel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int[] selectedRows = NodeTable.getSelectedRows();
					for (int i = selectedRows.length - 1; i >= 0; i--) {
						TableContent.del(selectedRows[i]);
					}
					TableContent.fireTableDataChanged();
				}
			});
		}
		return NodeDel;
	}

	public boolean checkValid() {
		try {
			if (IPs.getText().equals("0.0.0.0") || !IPs.getText().matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}")) {
				return false;
			}
			if (PSWtext.getText().length() < 1) {
				return false;
			}
			if (UserName.getText().length() < 1) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean generateXML() {
		try {
			Utils.genXMLDN(TableContent.getRowCount() + 1);
			Utils.genXMLNN();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		JFrame tmp = new JFrame();
		final NodeConfigPanel n = new NodeConfigPanel();
		tmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tmp.setLayout(new BorderLayout());
		tmp.add(n, BorderLayout.CENTER);
		tmp.pack();
		tmp.setVisible(true);
	}
}
