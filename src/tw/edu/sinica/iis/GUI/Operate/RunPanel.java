package tw.edu.sinica.iis.GUI.Operate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RunPanel extends JPanel {

	private static final long serialVersionUID = 4224485357881142712L;

	public int default_w = 400;
	public int default_h = 300;

	public String[] RunParameters;

	public JLabel[] ParameterLabel;
	public JTextField[] ParameterText;
	public String[] ParameterName;

	public JButton WorkRun;
	public JButton ResultDownload;
	public JButton ResultClear;

	public JLabel JobNameLabel;
	public JProgressBar HadoopTotalBar;
	public JProgressBar HadoopBar;

	public RunPanel() {
		super();
		init(default_w, default_h);
	}

	public RunPanel(int w, int h) {
		super();
		init(w, h);
	}

	public void init(int w, int h) {
		// this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS ));
		this.setLayout(null);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setSize(w, h);
		this.setPreferredSize(new Dimension(w, h));

		JPanel pPanel = new JPanel();
		// pPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		pPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		pPanel.setBounds(10, 25, 380, 230);
		pPanel.setLayout(null);

		ParameterName = getParameterName();

		int pn = ParameterName.length;
		ParameterLabel = new JLabel[pn];
		ParameterText = new JTextField[pn];
		RunParameters = new String[pn];

		int LineHeight = 35;
		int StartHeight = 20;

		JPanel parameterPanel = new JPanel();
		parameterPanel.setBounds(10, 10, 360, 95);
		parameterPanel
				.setBorder(BorderFactory.createTitledBorder("Parameters"));
		parameterPanel.setLayout(null);

		for (int i = 0; i < pn; i++) {
			ParameterLabel[i] = new JLabel(ParameterName[i]);
			ParameterLabel[i].setBounds(0, StartHeight + LineHeight * i, 185,
					25);
			ParameterLabel[i].setHorizontalAlignment(SwingConstants.RIGHT);
			ParameterLabel[i].setVerticalAlignment(SwingConstants.CENTER);
			parameterPanel.add(ParameterLabel[i]);

			ParameterText[i] = new JTextField();
			ParameterText[i].setText("0");
			ParameterText[i].setBounds(195, StartHeight + LineHeight * i, 150,
					25);
			parameterPanel.add(ParameterText[i]);
		}

		// JobNameLabel = new JLabel();
		// JobNameLabel.setForeground(Color.RED);
		// JobNameLabel.setText("Server Ready");
		// JobNameLabel.setBounds(10, 130, 360, 20);
		// JobNameLabel.setHorizontalAlignment(JLabel.CENTER);
		// pPanel.add(JobNameLabel);

		JPanel progressPanel = new JPanel();
		progressPanel.setBounds(10, 120, 360, 100);
		progressPanel.setBorder(BorderFactory.createTitledBorder("Progress"));
		progressPanel.setLayout(null);

		HadoopBar = new JProgressBar();
		HadoopBar.setStringPainted(true);
		HadoopBar.setBounds(10, 25, 340, 25);
		// HadoopBar.setBorderPainted(false);
		// HadoopBar.setBorder(BorderFactory.createTitledBorder("title"));
		// HadoopBar.setForeground(new Color(255, 100, 100));
		// HadoopBar.setValue(10);
		HadoopBar.setString("Server Ready");
		progressPanel.add(HadoopBar);

		HadoopTotalBar = new JProgressBar();
		HadoopTotalBar.setBounds(10, 60, 340, 25);
		HadoopTotalBar.setStringPainted(true);
		// HadoopTotalBar.setValue(10);
		progressPanel.add(HadoopTotalBar);

		pPanel.add(parameterPanel);
		pPanel.add(progressPanel);

		this.add(pPanel);

		JPanel ButtonPanel = new JPanel();
		// ButtonPanel.setLayout(new GridLayout(1, 3, 10, 10));
		ButtonPanel.setLayout(new FlowLayout());
		ButtonPanel.setBounds(20, 260, 360, 35);
		// ButtonPanel.add(Box.createHorizontalGlue());
		ButtonPanel.add(getWorkRun());
		ButtonPanel.add(Box.createHorizontalStrut(10));
		ButtonPanel.add(getResultDownload());
		ButtonPanel.add(Box.createHorizontalStrut(10));
		ButtonPanel.add(getResultClear());

		this.add(ButtonPanel);

		JLabel step_tip = new JLabel(
				"Step 3. Run CloudBrush and download the assembly");
		step_tip.setHorizontalAlignment(SwingConstants.LEFT);
		step_tip.setForeground(Color.BLUE);
		step_tip.setFont(new Font(step_tip.getFont().getFontName(), step_tip
				.getFont().getStyle(), 15));
		step_tip.setBounds(10, 5, 400, 15);
		this.add(step_tip);

	}

	public String[] getParameterName() {
		String[] res = new String[2];
		res[0] = "Read length:";
		res[1] = "Minimum overlap length:";
		return res;
	}

	public JButton getWorkRun() {
		if (WorkRun == null) {
			WorkRun = new JButton("Submit");
		}
		return WorkRun;
	}

	public JButton getResultDownload() {
		if (ResultDownload == null) {
			ResultDownload = new JButton("Download Results");
		}
		return ResultDownload;
	}

	public JButton getResultClear() {
		if (ResultClear == null) {
			ResultClear = new JButton("Reset");
		}
		return ResultClear;
	}

	public boolean getAndCheckParameter(){
		try {
			for(int i=0;i<getParameterName().length;i++){
				RunParameters[i] = ParameterText[i].getText();
				Integer.parseInt(RunParameters[i]);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame test = new JFrame();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.add(new RunPanel());
		test.pack();
		test.setVisible(true);
	}

}
