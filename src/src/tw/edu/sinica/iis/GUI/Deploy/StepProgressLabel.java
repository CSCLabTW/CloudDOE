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
import java.awt.Font;

import javax.swing.JLabel;

public class StepProgressLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 73547239456L;
	public static final Color PREPARECOLOR = new Color(200, 200, 200);
	public static final Color PROCESSINGCOLOR = new Color(255, 0, 0);
	public static final Color FINISHEDCOLOR = new Color(0, 255, 0);

	public StepProgressLabel() {
		super();
	}

	public StepProgressLabel(final String text) {
		super(text);
		setStatus(PREPARECOLOR);
	}

	public StepProgressLabel(final String text, final int size) {
		super(text);
		setSize(size);
	}

	public void setSize(final int size) {
		Font tmp = getFont();
		this.setFont(new Font(tmp.getName(), tmp.getStyle(), size));
	}

	public void setStatus(final Color st) {
		this.setForeground(st);
	}
}
