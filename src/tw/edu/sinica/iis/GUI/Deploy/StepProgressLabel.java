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
