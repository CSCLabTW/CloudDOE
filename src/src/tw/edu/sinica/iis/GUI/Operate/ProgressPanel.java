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

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProgressPanel extends JPanel {

	private static final long serialVersionUID = -4011637594203073258L;

	public int default_w = 400;
	public int default_h = 150;

	public JLabel ConnectToHadoop;
	public JLabel UploadFileAndData;
	public JLabel ParameterSetting;

	public ProgressPanel() {
		super();
		init(default_w, default_h);
	}

	public ProgressPanel(int w, int h) {
		super();
		init(w, h);
	}

	public void init(int w, int h) {
		this.setLayout(null);
		// this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setSize(w, h);
		this.setPreferredSize(new Dimension(w, h));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame test = new JFrame();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.add(new ProgressPanel());
		test.pack();
		test.setVisible(true);
	}

}
