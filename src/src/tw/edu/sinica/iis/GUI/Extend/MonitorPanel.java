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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.concurrent.Callable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import tw.edu.sinica.iis.SSHadoop.SSHExec;

public class MonitorPanel extends JPanel implements Runnable{

	private static final long serialVersionUID = 233213875234871L;

	private String pidFilePath = null;
	private String logFilePath = null;
	
	private long refreshPeriod = 1000;
	private boolean closeSwitch = false;
	
	public JTextArea monitorText;
	public JButton closeButton;
	
	public MonitorPanel(){
		super();
		init();
	}
	
	public MonitorPanel(final String pidFilePath, final String logFilePath){
		super();
		
		this.pidFilePath = pidFilePath;
		this.logFilePath = logFilePath;
		
		init();
	}
	
	public void setRefreshPeriod(long refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}
	
	public void init(){
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setLayout(new BorderLayout());
		
		this.add(getCenterPanel(),BorderLayout.CENTER);
		this.add(getSouthPanel(),BorderLayout.SOUTH);
	}
	
	public JPanel getCenterPanel(){
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new BorderLayout());
		monitorText = new JTextArea();
		monitorText.setEditable(false);
		monitorText.setBackground(Color.LIGHT_GRAY);
		monitorText.setText("Fetching installation progress...");
		JScrollPane tmp = new JScrollPane(monitorText);
		tmp.setPreferredSize(new Dimension(300, 300));
		cPanel.add(tmp,BorderLayout.CENTER);
		return cPanel;
	}
	
	public JPanel getSouthPanel(){
		JPanel sPanel = new JPanel();
		sPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		closeButton = new JButton("Close");
		sPanel.add(closeButton);
		return sPanel;
	}
	
	public void startMonitor(){
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		try {
			closeButton.setEnabled(false);
			while(!closeSwitch){
				getMonitorData();
				if(isFinished()){
					closeButton.setEnabled(true);
					break;
				}
				Thread.sleep(refreshPeriod);
			}
			getMonitorData();
			closeSwitch = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean getMonitorData(){
		// TODO: need refinement
		String testCmd = Extend.HadoopCmd.PPILog(logFilePath, 30);
		System.out.println(testCmd);
		Callable<String> channel = new SSHExec(Extend.HadoopSession.getSession(), testCmd);

		String log = "";
		try {
			log = channel.call();
			if (!"".equals(log)) {
				monitorText.setText(log);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error.");
		}
		
		return true;
	}
	
	public boolean isFinished(){
		// TODO: need refinement
		String testCmd = Extend.HadoopCmd.PPIStatus("checkpid.sh", pidFilePath);
		Callable<String> channel = new SSHExec(Extend.HadoopSession.getSession(), testCmd);

		try {
			String log = channel.call();
			if ("end\n".equals(log)) {
				return true;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error.");
		}

		return false;
	}
	
	public static void main(String[] args) {
		MonitorPanel m = new MonitorPanel();
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().add(m);
		window.pack();
		window.setVisible(true);
		
		m.startMonitor();
	}

	

}
