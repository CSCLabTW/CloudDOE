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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AboutPanel extends JPanel {

	private static final long serialVersionUID = 2846384582123517L;

	private static final String versionStr = "CloudDOE - Extend Wizard (v1.5.20140318)";
	
	public int default_w = 580;
	public int default_h = 260;

	public JLabel GUIVer;
	public JLabel JARVer;
	public JLabel UIDLabel;

	public AboutPanel() {
		super();
		init(default_w, default_h);
	}

	public AboutPanel(int w, int h) {
		super();
		init(w, h);
	}

	public void init(int w, int h) {
		this.setLayout(null);
		// this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setSize(w, h);
		this.setPreferredSize(new Dimension(w, h));
		int paddingLeft = 30;
		int paddingTop = 0;

		JLabel logoLabel = new JLabel();
		try {
			ImageIcon img = new ImageIcon(getClass().getResource("/assets/logo.png"));
			logoLabel = new JLabel(img);
			logoLabel.setBounds(paddingLeft - 10, 50, img.getIconWidth(),
					img.getIconHeight());
			this.add(logoLabel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paddingTop += logoLabel.getHeight();

		paddingLeft = 200;
		paddingTop = 10;
		GUIVer = new JLabel();
		GUIVer.setBounds(paddingLeft, paddingTop, default_w-paddingLeft, 20);
		GUIVer.setForeground(Color.BLUE);
		Font tmpFont = new Font(GUIVer.getFont().getName(), GUIVer.getFont()
				.getStyle(), 16);
		GUIVer.setFont(tmpFont);
		GUIVer.setText(versionStr);
		this.add(GUIVer);
		paddingTop += GUIVer.getHeight();

		Font tmpFont2 = new Font(GUIVer.getFont().getName(), Font.ITALIC, 12);

		paddingTop += 5;
		JLabel line2 = new JLabel();
		line2.setBounds(paddingLeft, paddingTop, 380, 20);
		line2.setForeground(Color.BLUE);
		line2.setFont(tmpFont2);
		line2.setText("An application store GUI interface to help user to");
		this.add(line2);
		paddingTop += line2.getHeight();

		JLabel line3 = new JLabel();
		line3.setBounds(paddingLeft, paddingTop, 380, 20);
		line3.setForeground(Color.BLUE);
		line3.setFont(tmpFont2);
		line3.setText("install third-party softwares/programs into their");
		this.add(line3);
		paddingTop += line3.getHeight();

		JLabel line4 = new JLabel();
		line4.setBounds(paddingLeft, paddingTop, 380, 20);
		line4.setForeground(Color.BLUE);
		line4.setFont(tmpFont2);
		line4.setText("Hadoop cluster through CloudDOE.");
		this.add(line4);
		paddingTop += line4.getHeight();

		Font tmpFont3 = new Font(GUIVer.getFont().getName(), GUIVer.getFont()
				.getStyle(), 12);

		paddingTop += 10;
		JLabel line5 = new JLabel();
		line5.setBounds(paddingLeft, paddingTop, 380, 20);
		line5.setForeground(Color.BLACK);
		line5.setFont(tmpFont3);
		line5.setText("Copyright © 2013,");
		this.add(line5);
		paddingTop += line5.getHeight();

		JLabel line11 = new JLabel();
		line11.setBounds(paddingLeft, paddingTop, 380, 20);
		line11.setForeground(Color.BLACK);
		line11.setFont(tmpFont3);
		line11.setText("Wei-Chun Chung (wcchung@iis.sinica.edu.tw) and");
		this.add(line11);
		paddingTop += line11.getHeight();

		JLabel line12 = new JLabel();
		line12.setBounds(paddingLeft, paddingTop, 380, 20);
		line12.setForeground(Color.BLACK);
		line12.setFont(tmpFont3);
		line12.setText("Yu-Chun Wang (zxaustin@iis.sinica.edu.tw),");
		this.add(line12);
		paddingTop += line12.getHeight();

		JLabel line13 = new JLabel();
		line13.setBounds(paddingLeft, paddingTop, 380, 20);
		line13.setForeground(Color.BLACK);
		line13.setFont(tmpFont3);
		line13.setText("IIS & CITI, Academia Sinica, Taiwan.");
		this.add(line13);
		paddingTop += line13.getHeight();
		
		Font tmpFont4 = new Font(GUIVer.getFont().getName(), GUIVer.getFont()
				.getStyle(), 14);

		paddingTop += 10;
		JLabel line6 = new JLabel();
		line6.setBounds(paddingLeft, paddingTop, 120, 20);
		line6.setForeground(Color.BLUE);
		line6.setFont(tmpFont4);
		line6.setText("Released under ");
		this.add(line6);

		JButton button = new JButton();
		button.setBorder(null);
		button.setText("<HTML><FONT size = \"14px\" color=\"#0000FF\"><U>Apache License 2.0</U></FONT></HTML>");
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setBorderPainted(false);
		button.setFocusable(false);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					AboutPanel.open(new URI(
							"http://www.apache.org/licenses/LICENSE-2.0"));
				} catch (Exception e) {
				}
			}
		});
		button.setOpaque(false);
		button.setBackground(Color.WHITE);
		button.setBounds(paddingLeft + line6.getWidth(), paddingTop, 130, 20);
		this.add(button);

		JLabel line6_end = new JLabel();
		line6_end.setBounds(paddingLeft + line6.getWidth() + button.getWidth(),
				paddingTop, 10, 20);
		line6_end.setForeground(Color.BLUE);
		line6_end.setFont(tmpFont4);
		line6_end.setText(".");
		this.add(line6_end);
		paddingTop += line6.getHeight();

		JLabel line7 = new JLabel();
		line7.setBounds(paddingLeft, paddingTop, 90, 20);
		line7.setForeground(Color.BLUE);
		line7.setFont(tmpFont4);
		line7.setText("Please visit ");
		this.add(line7);

		JButton button2 = new JButton();
		button2.setBorder(null);
		button2.setText("<HTML><FONT size = \"14px\" color=\"#0000FF\"><U>CloudDOE Project</U></FONT></HTML>");
		button2.setHorizontalAlignment(SwingConstants.CENTER);
		button2.setBorderPainted(false);
		button2.setFocusable(false);
		button2.setOpaque(false);
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					AboutPanel.open(new URI(
							"http://clouddoe.iis.sinica.edu.tw/"));
				} catch (Exception e) {
				}

			}
		});
		button2.setBackground(Color.WHITE);
		button2.setBounds(paddingLeft + line7.getWidth(), paddingTop, 140, 20);
		this.add(button2);

		JLabel line7_end = new JLabel();
		line7_end.setBounds(
				paddingLeft + line7.getWidth() + button2.getWidth(),
				paddingTop, 120, 20);
		line7_end.setForeground(Color.BLUE);
		line7_end.setFont(tmpFont4);
		line7_end.setText(" for details.");
		this.add(line7_end);

	}

	private static void open(URI uri) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
				/* TODO: error handling */
			}
		} else {
			/* TODO: error handling */
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame test = new JFrame();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.add(new AboutPanel());
		test.pack();
		test.setVisible(true);
	}

}
