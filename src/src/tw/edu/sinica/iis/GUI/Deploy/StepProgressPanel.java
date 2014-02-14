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

import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StepProgressPanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 16543617428453276L;

	public LinkedList<StepProgressLabel> Steps = null;
	public long refreshTime = 5000;
	public int NowStep = 0;

	public StepProgressPanel() {
		super();
		init();
	}

	public void init() {
		Steps = new LinkedList<StepProgressLabel>();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	public void addStep(StepProgressLabel s) {
		if (Steps.size() != 0) {
			add(Box.createRigidArea(new Dimension(0, 10)));
		}
		Steps.add(s);
		add(s);
	}

	public boolean setStep(int n) {
		if (Steps == null)
			return false;
		if (n < -1 || n > Steps.size())
			return false;

		NowStep = n;
		updateStep();

		return true;
	}

	public boolean isFinished() {
		return NowStep == Steps.size();
	}

	public void updateStep() {
		if (NowStep == -1) {
			for (int i = 0; i < Steps.size(); i++) {
				Steps.get(i).setStatus(StepProgressLabel.PREPARECOLOR);
			}
			return;
		}
		for (int i = 0; i < NowStep; i++) {
			Steps.get(i).setStatus(StepProgressLabel.FINISHEDCOLOR);
		}
		if (NowStep < Steps.size()) {
			Steps.get(NowStep).setStatus(StepProgressLabel.PROCESSINGCOLOR);
		}
		if (NowStep < Steps.size() - 1) {
			for (int i = NowStep + 1; i < Steps.size(); i++) {
				Steps.get(i).setStatus(StepProgressLabel.PREPARECOLOR);
			}
		}
	}

	public void startMonitor() {
		new Thread(this).start();
	}

	// implements here
	public void getProgress() {
		NowStep++;
	}

	@Override
	public void run() {
		try {
			while (true) {
				getProgress();
				updateStep();
				if (isFinished()) {
					break;
				}
				Thread.sleep(refreshTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		JFrame tmp = new JFrame();
		StepProgressPanel p = new StepProgressPanel();
		p.addStep(new StepProgressLabel("step1. HI! I'm Austin!"));
		p.addStep(new StepProgressLabel("step2. And I'm a PIG!"));
		p.addStep(new StepProgressLabel(
				"step3. SATYFDJYKAFDYJKEWAFVDASAYFDGLIUEF"));
		p.addStep(new StepProgressLabel("step4. hey!hey!hey!!!  com'on Baby~~"));
		p.setStep(0);
		tmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tmp.add(p);
		tmp.pack();
		tmp.setVisible(true);
		p.startMonitor();
	}

}
