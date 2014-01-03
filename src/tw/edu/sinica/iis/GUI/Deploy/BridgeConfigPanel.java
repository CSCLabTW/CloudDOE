package tw.edu.sinica.iis.GUI.Deploy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BridgeConfigPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9831534876L;
	public JTextField tIP;
	public JTextField tUser;
	public JTextField tPassword;
	
	public JTextField tpIP;
	public JTextField tpUser;
	public JTextField tpPassword;
	public JCheckBox pDataChkBox;
	
	public JTextArea Des = null;
	
	public BridgeConfigPanel(){
		init();
	}
	
	public void init(){
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
//		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		this.setLayout(fl);
//		this.setLayout(new GridLayout(4, 1, 0, 0));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//		this.add(Box.createHorizontalGlue());
		
		
		JPanel pubNN = new JPanel();
		
//		pubNN.setBorder(BorderFactory.createLineBorder(Color.RED));
		pubNN.setLayout(new GridLayout(4, 1));
		
		
		String des = "<HTML><Font size = '3'>Please provide access information of the controller for the wizard running on THE PC.</Font></HTML>";
		pubNN.add(new JLabel(des));
		
		JPanel pIP = new JPanel();
		pIP.setLayout(new BorderLayout());
		tIP = new JTextField();
		tIP.setPreferredSize(new Dimension(100, 25));
		JPanel tmp1 = new JPanel();
		tmp1.add(new JLabel("IP : "));
		tmp1.add(tIP);
		pIP.add(tmp1,BorderLayout.LINE_START);
		pubNN.add(pIP);
		
		JPanel pUN = new JPanel();
		pUN.setLayout(new BorderLayout());
		tUser = new JTextField();
		tUser.setPreferredSize(new Dimension(100, 25));
		JPanel tmp2 = new JPanel();
		tmp2.add(new JLabel("UserName : "));
		tmp2.add(tUser);
		pUN.add(tmp2,BorderLayout.LINE_START);
		pubNN.add(pUN);
		
		JPanel pPW = new JPanel();
		pPW.setLayout(new BorderLayout());
		tPassword = new JTextField();
		tPassword.setPreferredSize(new Dimension(100, 25));
		JPanel tmp3 = new JPanel();
		tmp3.add(new JLabel("Password : "));
		tmp3.add(tPassword);
		pPW.add(tmp3,BorderLayout.LINE_START);
		pubNN.add(pPW);
		
		this.add(pubNN);
	}
	
	public JPanel getBullet(String title, JTextField textfeild, String description){
		
		JPanel tmp = new JPanel();
		tmp.setLayout(new BoxLayout(tmp, BoxLayout.PAGE_AXIS));
//		tmp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JPanel up = new JPanel();
		up.setLayout(new BoxLayout(up, BoxLayout.X_AXIS));
		up.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
//		up.setBorder(BorderFactory.createLineBorder(Color.red));
		
		up.add(new JLabel(title+" : "));
		textfeild.setPreferredSize(new Dimension(100, 25));
		textfeild.setMaximumSize(new Dimension(100, 25));
		textfeild.setHorizontalAlignment(JLabel.LEFT);
		up.add(textfeild);
		up.add(Box.createGlue());
		
		JPanel down = new JPanel();
		
		down.setLayout(new BoxLayout(down, BoxLayout.X_AXIS));
		down.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
//		down.setBorder(BorderFactory.createLineBorder(Color.blue));
		down.add(new JLabel(description));
				
		tmp.add(up);
		tmp.add(down);
		
		return tmp;
	}
	
	public JPanel getSetPanel(){
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout());
		tmp.setBorder(BorderFactory.createTitledBorder("Setting for Your PC conneciton"));
		tmp.setMaximumSize(new Dimension(600,40));
		
		tmp.add(new JLabel("IP : "));
		tIP = new JTextField();
		tIP.setPreferredSize(new Dimension(100, 25));
		tmp.add(tIP);
		tmp.add(new JLabel("Username : "));
		tUser = new JTextField();
		tUser.setPreferredSize(new Dimension(100, 25));
		tmp.add(tUser);
		tmp.add(new JLabel("Password : "));
		tPassword = new JTextField();
		tPassword.setPreferredSize(new Dimension(100, 25));
		tmp.add(tPassword);
		return tmp;
	}
	
	public JPanel getPrivateSetPanel(){
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout());
		tmp.setBorder(BorderFactory.createTitledBorder("Setting for Data Node connection"));
		tmp.setMaximumSize(new Dimension(600,40));
		
		tmp.add(new JLabel("IP : "));
		tpIP = new JTextField();
		tpIP.setPreferredSize(new Dimension(100, 25));
		tmp.add(tpIP);
		tmp.add(new JLabel("Username : "));
		tpUser = new JTextField();
		tpUser.setPreferredSize(new Dimension(100, 25));
		tmp.add(tpUser);
		tmp.add(new JLabel("Password : "));
		tpPassword = new JTextField();
		tpPassword.setPreferredSize(new Dimension(100, 25));
		tmp.add(tpPassword);
		return tmp;
	}
	
	public void loadNodeData(){
		LinkedList<NodeStructure> tmp = Utils.loadNodeFiles("NP", true);
		if(tmp.size()>0){
			tIP.setText(tmp.get(0).IP);
			tUser.setText(tmp.get(0).User);
			tPassword.setText(tmp.get(0).Password);
		}
	}
	
	public boolean saveNodeData(){
		LinkedList<NodeStructure> tmp = new LinkedList<NodeStructure>();
		tmp.add(new NodeStructure(true, tIP.getText(), tUser.getText(), tPassword.getText()));
		return Utils.saveNodeFiles(tmp, "NP");
	}
	
	public JTextArea iniDes(){
		if(Des==null){
			Des = new JTextArea();
			Des.setLineWrap(true);
			Des.setEditable(false);
			Des.setBackground(this.getBackground());
			Des.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			Des.append("Setting for Your PC connection\n");
			Des.append("Your PC will use this setting connecting to Name Node for MapReduce installation.\n");
			Des.append("Please ensure Your PC has directly access to Name Node with this setting.\n");
			Des.append("\n");
			Des.append("Setting for Data Node connection\n");
			Des.append("Data Node will use this setting to form a Hadoop cluster.\n");
			Des.append("MapReduce-related services and web monitoring pages will be constructed based on this setting.\n");
			Des.append("Please ensure all data nodes have directly access to Name Node.\n");
		
		}
		
		return Des;
	}
	

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
