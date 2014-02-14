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

package tw.edu.sinica.iis.GUI.Operate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ConnectPanel extends JPanel {

	private static final long serialVersionUID = 3673480019949195702L;

	public int default_w = 400;
	public int default_h = 400;

	public JTextField ipText;
	public JTextField portText;
	public JTextField IDText;
	public JPasswordField PasswordText;

	public JButton connectButton;
	public JButton loadButton;

	public ConnectPanel() {
		super();
		init(default_w, default_h);

	}

	public ConnectPanel(int w, int h) {
		super();
		init(w, h);
	}

	public void init(int w, int h) {
		this.setLayout(null);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setSize(w, h);
		this.setPreferredSize(new Dimension(w, h));

		JPanel centerP = new JPanel();
		centerP.setLayout(null);

		JLabel t1 = new JLabel("IP: ");
		t1.setHorizontalAlignment(SwingConstants.RIGHT);
		t1.setBounds(0, 0, 100, 20);
		centerP.add(t1);

		ipText = new JTextField();
		ipText.setBounds(110, 0, 150, 20);
		centerP.add(ipText);

		JLabel t4 = new JLabel("Port: ");
		t4.setHorizontalAlignment(SwingConstants.RIGHT);
		t4.setBounds(0, 25, 100, 20);
		centerP.add(t4);

		portText = new JTextField();
		portText.setBounds(110, 25, 150, 20);
		centerP.add(portText);

		JLabel t2 = new JLabel("Username: ");
		t2.setHorizontalAlignment(SwingConstants.RIGHT);
		t2.setBounds(0, 50, 100, 20);
		centerP.add(t2);

		IDText = new JTextField();
		IDText.setBounds(110, 50, 150, 20);
		centerP.add(IDText);

		JLabel t3 = new JLabel("Password: ");
		t3.setHorizontalAlignment(SwingConstants.RIGHT);
		t3.setBounds(0, 75, 100, 20);
		centerP.add(t3);

		PasswordText = new JPasswordField();
		PasswordText.setBounds(110, 75, 150, 20);
		centerP.add(PasswordText);

		centerP.setSize(260, 100);
		centerP.setLocation(this.getSize().width / 2 - centerP.getSize().width
				/ 2, this.getSize().height / 2 - centerP.getSize().height / 2);

		this.add(centerP);

		JLabel step_tip = new JLabel("Step 1. Connect to Hadoop Cloud");
		step_tip.setHorizontalAlignment(SwingConstants.LEFT);
		step_tip.setForeground(Color.BLUE);
		step_tip.setFont(new Font(step_tip.getFont().getFontName(), step_tip
				.getFont().getStyle(), 15));
		step_tip.setBounds(10, 5, 400, 15);
		this.add(step_tip);

		JLabel title = new JLabel("Login to Master Node");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font(title.getFont().getFontName(), title.getFont()
				.getStyle(), 20));
		title.setLocation(centerP.getLocation().x, centerP.getLocation().y - 60);
		title.setSize(260, 25);
		this.add(title);

		connectButton = new JButton("Connect");
		connectButton.setBounds(this.getSize().width / 2 - 50+60,
				centerP.getLocation().y + centerP.getSize().height + 20, 100,
				25);
		
		loadButton = new JButton("Load");
		loadButton.setBounds(this.getSize().width / 2 - 50-60,
				centerP.getLocation().y + centerP.getSize().height + 20, 100,
				25);
		
		this.add(connectButton);
		this.add(loadButton);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame test = new JFrame();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.add(new ConnectPanel());
		test.pack();
		test.setVisible(true);
	}

}
