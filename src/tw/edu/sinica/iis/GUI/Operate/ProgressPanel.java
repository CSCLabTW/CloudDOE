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
