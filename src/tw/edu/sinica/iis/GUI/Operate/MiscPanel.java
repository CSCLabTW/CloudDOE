package tw.edu.sinica.iis.GUI.Operate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MiscPanel extends JPanel {

	private static final long serialVersionUID = -6358176685306106633L;

	public int default_w = 400;
	public int default_h = 500;

	public JButton ClearData;
	public JButton ClearJob;
	public JButton ClearUser;

	public MiscPanel() {
		super();
		init(default_w, default_h);
	}

	public MiscPanel(int w, int h) {
		super();
		init(w, h);
	}

	public void init(int w, int h) {
		this.setLayout(null);
		// this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setSize(w, h);
		this.setPreferredSize(new Dimension(w, h));

		JPanel ButtonOut = new JPanel();
		ButtonOut.setLayout(new BorderLayout());
		// ButtonOut.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		ButtonOut.setSize(200, 200);
		ButtonOut.setLocation(this.getSize().width / 2
				- ButtonOut.getSize().width / 2, this.getSize().height / 2
				- ButtonOut.getSize().height / 2);

		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setLayout(new GridLayout(4, 1, 10, 10));
		ButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel t1 = new JLabel("Wipe");
		t1.setFont(new Font(t1.getFont().getName(), t1.getFont().getStyle(), 16));
		ButtonPanel.add(t1);

		ButtonPanel.add(getClearJob());
		ButtonPanel.add(getClearData());
		ButtonPanel.add(getClearUser());

		ButtonOut.add(ButtonPanel, BorderLayout.CENTER);
		this.add(ButtonOut);

	}

	public JButton getClearData() {
		if (ClearData == null) {
			ClearData = new JButton("Data");
			ClearData.setBounds(20, 60, 120, 30);
		}
		return ClearData;
	}

	public JButton getClearJob() {
		if (ClearJob == null) {
			ClearJob = new JButton("Result");
			ClearJob.setBounds(20, 200, 120, 30);
		}
		return ClearJob;
	}

	public JButton getClearUser() {
		if (ClearUser == null) {
			ClearUser = new JButton("Account");
		}
		return ClearUser;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame test = new JFrame();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.add(new MiscPanel());
		test.pack();
		test.setVisible(true);
	}

}
