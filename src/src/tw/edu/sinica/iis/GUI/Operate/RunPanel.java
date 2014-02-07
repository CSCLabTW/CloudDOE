package tw.edu.sinica.iis.GUI.Operate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import tw.edu.sinica.iis.GUI.Operate.XMLConfigParser.paramType;

public class RunPanel extends JPanel {

	private static final long serialVersionUID = 4224485357881142712L;
	private static final String confExt = ".xml";
	
	public static final String SELECTOR_DEFAULT = "";
	
	public static int default_w = 400;
	public static int default_h = 400;

	public JLabel[] ParameterLabel;
	public JTextField[] ParameterText;

	public JButton programHelper;
	public JButton WorkRun;
	public JButton ResultDownload;
	public JButton ResultClear;

	public JProgressBar HadoopTotalBar;
	public JProgressBar HadoopBar;

	public JPanel programPanel;
	public JPanel parameterPanel;

	public String xmlPath;
	public String programConf;

	public JComboBox<String> programSelector;
	public XMLConfigParser xmlConfigParser;

	public RunPanel() {
		super();
		init(default_w, default_h);
	}
	
	public RunPanel(final String confXML) {
		super();
		programConf = confXML;
		init(default_w, default_h);
	}

	public RunPanel(int w, int h) {
		super();
		init(w, h);
	}

	public void init(int w, int h) {
		xmlPath = "workspace" + File.separator + "main";

		xmlConfigParser = new XMLConfigParser();

		this.setLayout(null);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setSize(w, h);
		this.setPreferredSize(new Dimension(w, h));

		JPanel pPanel = new JPanel();
		pPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		pPanel.setBounds(10, 25, 380, 330);
		pPanel.setLayout(null);

		JPanel mPanel = new JPanel();
		mPanel.setLayout(new BoxLayout(mPanel, BoxLayout.Y_AXIS));
		mPanel.setBorder(BorderFactory.createTitledBorder("Configuration"));
		mPanel.setBounds(10, 10, 360, 200);

		programPanel = new JPanel();
		programPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		programPanel.setMaximumSize(new Dimension(mPanel.getWidth(), 0));
		setProgramPanel();

		parameterPanel = new JPanel();
		parameterPanel.setBounds(10, 25, 340, 150);
		setParameterPanel();

		JPanel progressPanel = new JPanel();
		progressPanel.setBounds(10, 220, 360, 100);
		progressPanel.setBorder(BorderFactory.createTitledBorder("Progress"));
		progressPanel.setLayout(null);

		HadoopBar = new JProgressBar();
		HadoopBar.setStringPainted(true);
		HadoopBar.setBounds(10, 25, 340, 25);
		HadoopBar.setString("Server Ready");
		progressPanel.add(HadoopBar);

		HadoopTotalBar = new JProgressBar();
		HadoopTotalBar.setBounds(10, 60, 340, 25);
		HadoopTotalBar.setStringPainted(true);
		progressPanel.add(HadoopTotalBar);

		mPanel.add(programPanel);
		mPanel.add(parameterPanel);

		pPanel.add(mPanel);
		pPanel.add(progressPanel);

		this.add(pPanel);

		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setLayout(new FlowLayout());
		ButtonPanel.setBounds(20, 360, 360, 35);
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

	public void setProgramPanel() {
		File pluginDir = new File(xmlPath);

		String[] files = pluginDir.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(confExt)) {
					return true;
				}
				return false;
			}
		});

		programHelper = new JButton("Help");
		
		programSelector = new JComboBox<String>();
		programSelector.addItem(SELECTOR_DEFAULT);

		for (String item : files) {
			programSelector.addItem(item.replace(confExt, ""));
			if(programConf != null && programConf.equals(item)) {
				programSelector.setSelectedItem(item.replace(confExt, ""));
			}
		}
		
		programSelector.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					programConf = programSelector.getSelectedItem().toString()
							.concat(confExt);
					setParameterPanel();
				}
			}
		});

		programPanel.add(new JLabel("Program: "));
		programPanel.add(programSelector);
		programPanel.add(programHelper);
	}

	public void setParameterPanel() {
		if (programConf == null || programConf.equals(SELECTOR_DEFAULT)) {
			return;
		}

		parameterPanel.removeAll();
		xmlConfigParser.removeAll();
		
		if (xmlConfigParser.load(xmlPath + File.separator + programConf)) {
			
			int pn = xmlConfigParser.parameterItems.size();

			ParameterLabel = new JLabel[pn];
			ParameterText = new JTextField[pn];

			JPanel paramsPanel = new JPanel();
			paramsPanel.setLayout(new GridLayout(pn, 2));
			paramsPanel.setPreferredSize(new Dimension(0, pn * 25));

			for (int i = 0; i < pn; i++) {
				ParameterLabel[i] = new JLabel(
						xmlConfigParser.parameterItems.get(i).label + ": ");
				ParameterLabel[i].setHorizontalAlignment(SwingConstants.RIGHT);
				ParameterLabel[i].setVerticalAlignment(SwingConstants.CENTER);
				ParameterLabel[i].setEnabled(xmlConfigParser.parameterItems
						.get(i).editable);
				paramsPanel.add(ParameterLabel[i]);

				ParameterText[i] = new JTextField();
				ParameterText[i]
						.setText(xmlConfigParser.parameterItems.get(i).value);
				ParameterText[i].setEditable(xmlConfigParser.parameterItems
						.get(i).editable);
				paramsPanel.add(ParameterText[i]);
			}

			JScrollPane jsp = new JScrollPane(paramsPanel);
			jsp.setBorder(null);
			jsp.setPreferredSize(new Dimension(parameterPanel.getWidth(),
					parameterPanel.getHeight() - 10));
			jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			parameterPanel.add(jsp);
		}

		this.repaint();
		this.revalidate();
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

	public boolean getAndCheckParameter() {
		if (xmlConfigParser.parameterItems.size() == 0
				|| xmlConfigParser.getSingleParam(paramType.INPUT) == null
				|| xmlConfigParser.getSingleParam(paramType.OUTPUT) == null) {
			return false;
		}

		for (int i = 0; i < xmlConfigParser.parameterItems.size(); i++) {
			xmlConfigParser.parameterItems.get(i).value = ParameterText[i]
					.getText();
		}
		return true;
	}

	public static void main(String[] args) {
		final JFrame runPanel = new JFrame();
		runPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		RunPanel rp = new RunPanel("CloudRS.xml");
		runPanel.add(rp);
		runPanel.pack();
		runPanel.setVisible(true);
	}
}
