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

package tw.edu.sinica.iis.GUI.Deploy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class InstallProgressPanel extends JPanel {

	private static final long serialVersionUID = 452098832258126580L;

	public String[] Steps;
	public JLabel[] StepLabels;
	public JPanel[] StepPanels;

	public InstallProgressPanel(String[] s) {
		super();
		Steps = s;
		init();
	}

	public void init() {
		GridLayout layout = new GridLayout(Steps.length, 1, 5, 20);

		this.setLayout(layout);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		StepLabels = new JLabel[Steps.length];
		StepPanels = new JPanel[Steps.length];
		for (int i = 0; i < StepLabels.length; i++) {
			StepPanels[i] = new JPanel();
			StepPanels[i].setMinimumSize(new Dimension(100, 100));
			StepPanels[i].setBackground(Color.BLUE);

			StepLabels[i] = new JLabel();
			StepLabels[i].setText(Steps[i]);
			StepLabels[i].setSize(100, 100);
			// StepLabels[i].setPreferredSize(new Dimension(100,20));
			StepLabels[i].setMinimumSize(new Dimension(100, 90));
			StepLabels[i].setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
			this.add(StepPanels[i]);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] s = new String[] { "step1", "step2", "step3" };
		JFrame tmp = new JFrame();
		tmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tmp.getContentPane().add(new InstallProgressPanel(s));
		tmp.pack();
		tmp.setVisible(true);
	}

}
