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

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class MonitorDialog extends JDialog {

	private static final long serialVersionUID = 2854142358L;

	private String pidFilePath = null;
	private String logFilePath = null;
	
	public MonitorPanel mp;

	public MonitorDialog(JFrame f, boolean b) {
		super(f, b);
	}
	
	public void setPIDFile(final String pidFile) {
		pidFilePath = pidFile;
	}
	
	public void setLogFile(final String logFile) {
		logFilePath = logFile;
	}

	public void init() {
		this.setTitle("Installation Progress...");
		mp = new MonitorPanel(pidFilePath, logFilePath);
		
		mp.setRefreshPeriod(5000);
		
		mp.closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MonitorDialog.this.setVisible(false);
			}
		});
		
		this.add(mp);
		this.pack();
		
		mp.startMonitor();
	}

	@Override
	protected void processEvent(AWTEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			int res = JOptionPane.showConfirmDialog(MonitorDialog.this, "Close program without completed installation may lead to\n unexpeteced errors. Quit anyway?");
			if(res == JOptionPane.OK_OPTION){
				super.processEvent(e);
			}else{
				return;
			}
		}
		super.processEvent(e);
	}
	
	public static void main(String[] args) {
		final JFrame window = new JFrame();
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton bt = new JButton("test");
		bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MonitorDialog md = new MonitorDialog(window, true);
				md.init();
				md.setVisible(true);
			}
		});
		
		window.getContentPane().add(bt);
		window.pack();
		window.setVisible(true);
	}
}
