package tw.edu.sinica.iis.GUI.Extend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 91252934298L;

	public JButton ConnectBt;
	public JTextField IPTF;
	public JTextField PTTF;
	public JTextField USNTF;
	public JPasswordField PSWTF;
	
	public LoginPanel(){
		super();
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(30, 5, 5, 5));
//		this.setBorder(BorderFactory.createLineBorder(Color.black));
		
		this.add(getNorthPanel(),BorderLayout.NORTH);
		this.add(getSouthPanel(),BorderLayout.SOUTH);
		this.add(getCenterPanel(),BorderLayout.CENTER);
	}
	
	public JPanel getNorthPanel(){
		JPanel nPanel = new JPanel();
		nPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		nPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JLabel title = new JLabel("Login to Master Node");
		title.setFont(title.getFont().deriveFont(24f));
		nPanel.add(title);
		return nPanel;
	}
	
	public JPanel getSouthPanel(){
		JPanel sPanel = new JPanel();
		sPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		sPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		ConnectBt = new JButton("Connect");
		sPanel.add(ConnectBt);
		
		return sPanel;
	}
	
	public JPanel getCenterPanel(){
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new GridLayout(4, 2, 5, 10));
		cPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
		
		Dimension textfieldSize = new Dimension(120, 20);
		
		JPanel tmp1 = new JPanel();
		tmp1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		tmp1.add(new JLabel("IP : "));
		cPanel.add(tmp1);
		
		IPTF = new JTextField();
		IPTF.setPreferredSize(textfieldSize);
		cPanel.add(IPTF);
		
		JPanel tmp4 = new JPanel();
		tmp4.setLayout(new FlowLayout(FlowLayout.RIGHT));
		tmp4.add(new JLabel("Port : "));
		cPanel.add(tmp4);
		
		PTTF = new JTextField();
		PTTF.setText("22");
		PTTF.setPreferredSize(textfieldSize);
		cPanel.add(PTTF);
		
		JPanel tmp2 = new JPanel();
		tmp2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		tmp2.add(new JLabel("Username : "));
		cPanel.add(tmp2);
		
		USNTF = new JTextField();
		USNTF.setPreferredSize(textfieldSize);
		cPanel.add(USNTF);
		
		JPanel tmp3 = new JPanel();
		tmp3.setLayout(new FlowLayout(FlowLayout.RIGHT));
		tmp3.add(new JLabel("Password : "));
		cPanel.add(tmp3);
		
		PSWTF = new JPasswordField();
		PSWTF.setPreferredSize(textfieldSize);
		cPanel.add(PSWTF);
		
		return cPanel;
	}
	

	public static void main(String[] args) {
		JFrame window = new JFrame();
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout());
		tmp.add(new LoginPanel());
		window.getContentPane().add(tmp);
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

}
