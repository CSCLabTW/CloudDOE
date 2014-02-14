/*
 * (C) Copyright 2013 The CloudDOE Project and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *      Wei-Chun Chung (wcchung@iis.sinica.edu.tw)
 *      Yu-Chun Wang (zxaustin@iis.sinica.edu.tw)
 * 
 * CloudDOE Project:
 *      http://clouddoe.iis.sinica.edu.tw/
 */

package tw.edu.sinica.iis.GUI.Extend;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import tw.edu.sinica.iis.GUI.Operate.TextExtractor;
import tw.edu.sinica.iis.SSHadoop.SSHSession;
import tw.edu.sinica.iis.SSHadoop.SSHSftp;
import tw.edu.sinica.iis.SSHadoop.SSHadoopCmd;

public class Extend extends JFrame{

	private static final long serialVersionUID = 19384237534L;
	
	private Dimension webSize = new Dimension(640, 480);
	private Dimension windowSize = new Dimension(0, 0);
	
	public static final String PLUGINBIN = "~/workspace/plugin";
	
	public static SSHSession HadoopSession;
	public static SSHadoopCmd HadoopCmd;
	
	public CardLayout cl;
	public JPanel mPanel;
	public LoginPanel lPanel;
	public WebPanel wPanel;
	
	public static String IP;
	public static String Port;
	public static String Username;
	public static String Password;
	
	public Extend(){
		super("CloudDOE - Extend Wizard");
		initial();
	}
	
	public void initial(){
		this.getContentPane().add(getRootPanel());
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				closeHadoop();
				super.windowClosing(arg0);
			}
		});
		
		HadoopSession = new SSHSession();
		
		HadoopCmd = new SSHadoopCmd();
		HadoopCmd.setBinaryLocation(PLUGINBIN);
	}
	
	public JTabbedPane getRootPanel(){
		JTabbedPane rootPanel = new JTabbedPane();
		rootPanel.add("Login & Install", getMainPanel());
		rootPanel.add("About",new AboutPanel());
		return rootPanel;
	}
	
	public JPanel getMainPanel(){
		mPanel = new JPanel();
		cl = new CardLayout();
		mPanel.setLayout(cl);
		mPanel.add(getLoginPanel(), "LOGIN");
		mPanel.add(getWebPabel(), "WEB");
		
		return mPanel;
	}
	
	public JPanel getLoginPanel(){
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout(FlowLayout.CENTER));
		lPanel = new LoginPanel();
		
		lPanel.ConnectBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				IP = lPanel.IPTF.getText();
				Port = lPanel.PTTF.getText();
				Username = lPanel.USNTF.getText();
				Password = new String(lPanel.PSWTF.getPassword());
				
				final WorkingDialog connDialog = new WorkingDialog(Extend.this, true, "Connecting");
				new Thread(new Runnable() {
					@Override
					public void run() {
						connDialog.setVisible(true);
					}
				}).start();
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						if (connectHadoop(IP, Port, Username, Password)) {
							if(!checkFile()){
								connDialog.Content = "Initializing";
								uploadWorkspace(IP, Username, Password);
							}
							cl.show(mPanel, "WEB");
							
							windowSize = Extend.this.getSize();
							Extend.this.setSize(webSize);
						} else {
							JOptionPane.showMessageDialog(null,
									"Login failed.");
						}
						connDialog.dispose();
					}
				}).start();
			}
		});
		
		lPanel.LoadBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Load NP file first
				String NNFile = TextExtractor.getFileText(new File("workspace"
						+ File.separator + "config" + File.separator + "common"
						+ File.separator + "NP"));
				
				// Load NN file if there is no NP file
				if (NNFile.length() == 0) {
					NNFile = TextExtractor.getFileText(new File("workspace"
							+ File.separator + "config" + File.separator + "common"
							+ File.separator + "NN"));
				}
				
				if(NNFile.length() > 1) {
					String[] sp = NNFile.replace("\n", "").split("\t");
					if (sp.length == 3) {
						lPanel.IPTF.setText(sp[0]);
						lPanel.USNTF.setText(sp[1]);
						lPanel.PSWTF.setText(sp[2]);
					}
				} else {
					JOptionPane
							.showMessageDialog(null, "Can not find NN file.");
				}
			}
		});
		
		tmp.add(lPanel);
		
		return tmp;
	}
	
	public JPanel getWebPabel(){
		wPanel = new WebPanel();
		
		wPanel.logoutBT.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				closeHadoop();
				Extend.this.setSize(windowSize);
				cl.show(mPanel, "LOGIN");
			}
		});
		
		wPanel.registerAction("app", new JFXAction(Extend.this));
		
		return wPanel;
	}
	
	public boolean connectHadoop(String ip, String port, String id, String psw) {
		try {
			HadoopSession.setHost(ip);
			HadoopSession.setUser(id);
			HadoopSession.setPass(psw);

			HadoopSession.setPort(Integer.parseInt(port));
			HadoopSession.initSession();
			
			if(HadoopSession.openSession()){
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
	
	public void closeHadoop(){
		HadoopSession.closeSession();
	}
	
	// TODO: check if remote exist plugin directories
	public boolean checkFile(){
		return false;
	}

	public boolean uploadWorkspace(final String ip, final String uname, final String pw){
		SSHSftp sftp = new SSHSftp(uname, ip);
		sftp.setSSHPass(pw);
		sftp.initSftp();
		
		if (sftp.sftpUpload(new File("workspace/plugin").getAbsolutePath(), "workspace/plugin")) {
			return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		Extend window = new Extend();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}

}
