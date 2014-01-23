package tw.edu.sinica.iis.GUI.Deploy;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;

import tw.edu.sinica.iis.GUI.Drawing.FlowChatCanvas;
import tw.edu.sinica.iis.SSHadoop.SSHExec;
import tw.edu.sinica.iis.SSHadoop.SSHKeyGen;
import tw.edu.sinica.iis.SSHadoop.SSHSession;
import tw.edu.sinica.iis.SSHadoop.SSHSftp;
import tw.edu.sinica.iis.SSHadoop.SSHShell;
import tw.edu.sinica.iis.SSHadoop.SSHadoopCmd;

public class HadoopInstall extends JFrame {
	private static final long serialVersionUID = 1230711203L;
	
	private enum tabOrder { INSTALL, UNINSTALL, ABOUT };
	
	public JPanel RPanel;
	public NodeConfigPanel NPanel;
	public StepProgressPanel SPanel;
	public BridgeConfigPanel BPanel;

	public FlowChatCanvas DrawFrame;
	public UninstallTabPanel UnistallPanel = null;
	
	public JTabbedPane TagPanel;
	public JButton StartButton;
	public JTextArea InstallArea;

	public JPanel CardPanel;
	public JButton NextButton;
	public JButton BackButton;
	public String[] StepTitle = { "Overview",
			"Step 1. Hadoop Cloud Connection",
			"Step 2. Hadoop Cloud Configuration",
			"Step 3. Hadoop Cloud Deployment" };
	public JLabel CardTitle;
	public int StepState = 0;

	public SSHadoopCmd HadoopCmd;
	public SSHSession HadoopSession;

	private SSHShell sshShell;

	private static final String CONFIGPATH = "workspace" + File.separator
			+ "config";
	private static final String KEYPATH = CONFIGPATH + File.separator
			+ "common";
	private static final String KEYNAME = "hadoop";
	private static final String KEYCOMT = "hadoop@CloudBrush";
	private static final String STATEFILENAME = "_setting";

	private static final String BINLOCATION = "~/workspace/bin";
	private static final String LOGFILENAME = "~/OCIinstall.log";
	private static final String PIDFILENAME = "~/ociinstall.pid";

	// -1:setting 0:started 1~3:step1~3
	private int InstallState = -1;

	public HadoopInstall() {
		init();
	}

	public void genStateFile() {
		try {
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(STATEFILENAME));
			bw.write(InstallState + "");
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readStateFile() {
		try {
			File readSetting = new File(STATEFILENAME);
			if (!readSetting.exists()) {
				setState(-1);
			} else {
				BufferedReader br = new BufferedReader(new FileReader(
						readSetting));
				setState(Integer.parseInt(br.readLine()));
				br.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	synchronized public void setState(int s) {
		InstallState = s;
		if (InstallState >= 0) {
			SPanel.setStep(InstallState);
		} else {
			SPanel.setStep(-1);
		}
		// genStateFile();
		switch (InstallState) {
		case -1:
			// not start yet
			changeEnableFlag(true);
			break;
		case 0:
			changeEnableFlag(false);
			startStep1Thread();
			// begin with step 1
			break;
		case 1:
			changeEnableFlag(false);
			startStep2Thread();
			// finish step 1, going on step 2
			break;
		case 2:
			changeEnableFlag(false);
			startStep3Thread();
			// finish step 2, going on step 3
			break;
		case 3:
			changeEnableFlag(true);
			startStep4Thread();
			// all finished
			break;
		}

	}

	public void startStep1Thread() {
		new Thread() {
			@Override
			public void run() {
				chkAndGenKey();
				getNPanel().generateXML();
				Utils.saveHostFiles(getNPanel().TableContent.Content);
				setState(1);
			}
		}.start();
	}

	public NodeStructure getMaster() {
		LinkedList<NodeStructure> tmp = getNPanel().TableContent.Content;
		for (int i = 0; i < tmp.size(); i++) {
			if (tmp.get(i).Master.booleanValue()) {
				return tmp.get(i);
			}
		}
		return null;
	}

	public boolean ConnectHadoop() {
		if (HadoopSession == null) {

			HadoopSession = new SSHSession();

			HadoopSession.setHost(BPanel.tIP.getText());
			HadoopSession.setUser(BPanel.tUser.getText());
			HadoopSession.setPass(BPanel.tPassword.getText());

			HadoopSession.initSession();
			if (HadoopSession.openSession()) {
				HadoopCmd = new SSHadoopCmd();
				HadoopCmd.setBinaryLocation(BINLOCATION);

				return true;
			}
		}
		return false;
	}

	public void startStep2Thread() {
		new Thread() {

			@Override
			public void run() {
				NodeStructure master = getMaster();
				if (master == null) {
					JOptionPane.showMessageDialog(null, "No Master Node found.");
					setState(-1);
					return;
				}
				SSHSftp sftp = new SSHSftp(BPanel.tUser.getText(), BPanel.tIP
						.getText());
				sftp.setSSHPass(BPanel.tPassword.getText());
				sftp.initSftp();

				if (sftp.sftpUpload(new File("workspace/").getAbsolutePath(),
						"workspace/")) {

					ConnectHadoop();

					String testCmd = HadoopCmd
							.OCIRun("install.sh", LOGFILENAME);
					sshShell = new SSHShell(HadoopSession.getSession(), testCmd);

					try {
						sshShell.call();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error occurs.");
						setState(-1);
					}

					setState(2);
				} else {
					JOptionPane.showMessageDialog(null,
							"Connect to Master Node failed.");
					setState(-1);
					return;
				}
			}
		}.start();
	}

	public void replaceLog() {
		String testCmd = HadoopCmd.OCILog(LOGFILENAME, 30);
		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				testCmd);

		String log = "";
		try {
			log = channel.call();
			if (!"".equals(log)) {
				getInstallArea().setText(log);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error occurs.");
			setState(-1);
		}
	}

	public void startStep3Thread() {
		new Thread() {
			@Override
			public void run() {
				try {
					ConnectHadoop();

					getInstallArea().setText(
							"Fetching installation progress messages...");

					long sleepTime = 500;
					do {
						sleep(sleepTime);
						replaceLog();
						sleepTime = 5000;
					} while (!checkFinish());

					setState(3);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Installation Failed.");
					setState(-1);
				}
			}
		}.start();
	}

	public boolean checkFinish() {
		String testCmd = HadoopCmd.OCIStatus("checkpid.sh", PIDFILENAME);
		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				testCmd);

		try {
			String log = channel.call();
			if ("end\n".equals(log)) {
				return true;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error occurs.");
			setState(-1);
		}

		return false;
	}

	public void startStep4Thread() {
		JOptionPane.showMessageDialog(null, "Installation Finished.");
		setState(-1);
	}

	public void changeEnableFlag(boolean f) {
		getStartButton().setEnabled(f);
		NextButton.setEnabled(f);
		BackButton.setEnabled(f);
	}

	public void chkAndGenKey() {
		SSHKeyGen s = new SSHKeyGen(KEYPATH, KEYNAME, KEYCOMT);
		if (!s.checkFileExist()) {
			s.genKeyPair();
		}
	}

	public void init() {
		chkAndGenKey();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("CloudDOE - Deploy Wizard");

		TagPanel = new JTabbedPane();
		
		UnistallPanel = new UninstallTabPanel();
		UnistallPanel.setMutex(TagPanel, tabOrder.INSTALL.ordinal());
		
		TagPanel.add("Installation", getInstallPanel());
		TagPanel.add("Uninstallation", UnistallPanel);
		TagPanel.add("About", new AboutPanel());
		TagPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JTabbedPane) {
                    JTabbedPane pane = (JTabbedPane) e.getSource();
                    int index = pane.getSelectedIndex();
                    if(index==tabOrder.INSTALL.ordinal()){
                    	DrawFrame.start();
                    	UnistallPanel.DrawingPanel.stop();
                    }else if(index==tabOrder.UNINSTALL.ordinal()){
                    	UnistallPanel.DrawingPanel.start();
                    	DrawFrame.stop();
                    }
                }
            }
        });
		
		setState(-1);
		CardPageChanged();

		this.getContentPane().add(TagPanel);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}

	public JPanel getInstallPanel() {
		JPanel tmpIP = new JPanel();

		CardTitle = new JLabel(StepTitle[StepState]);
		JPanel tmpTP = new JPanel();
		tmpTP.add(CardTitle);

		DrawFrame = new FlowChatCanvas();
		DrawFrame.getWindowCanvas().setPreferredSize(new Dimension(600, 170));
		JPanel DPanel = new JPanel();
		DPanel.add(DrawFrame.getWindowCanvas());
		DPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		tmpIP.setLayout(new BoxLayout(tmpIP, BoxLayout.Y_AXIS));
		tmpIP.add(tmpTP);
		tmpIP.add(DPanel);
		tmpIP.add(getCardPanel());
		tmpIP.add(getButtonPanel());
		return tmpIP;
	}

	public JPanel getCardPanel() {
		if (CardPanel == null) {
			CardPanel = new JPanel();
			CardPanel.setLayout(new CardLayout());
			CardPanel.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.RAISED));
			CardPanel.add(getRPanel(), StepTitle[0]);
			CardPanel.add(getBPanel(), StepTitle[1]);
			CardPanel.add(getNPanel(), StepTitle[2]);
			CardPanel.add(getStepTagPanel(), StepTitle[3]);

		}
		return CardPanel;
	}

	public JPanel getButtonPanel() {
		JPanel tmpBP = new JPanel();
		tmpBP.setLayout(new BoxLayout(tmpBP, BoxLayout.X_AXIS));
		tmpBP.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tmpBP.add(getBackButton());
		tmpBP.add(Box.createHorizontalGlue());
		tmpBP.add(getNextButton());
		return tmpBP;
	}

	public void CardPageChanged() {
		CardTitle.setText("<HTML><FONT SIZE=6>" + StepTitle[StepState]
				+ "</FONT></HTML>");
		
		if(StepState > 0) {
			TagPanel.setEnabledAt(tabOrder.UNINSTALL.ordinal(), false);
		} else {
			TagPanel.setEnabledAt(tabOrder.UNINSTALL.ordinal(), true);
		}
		
		switch (StepState) {
		case 1:
			DrawFrame.showPublicNN();
			getBPanel().loadNodeData();
			break;
		case 2:
			DrawFrame.showPrivateDN();
			getNPanel().modelReload();
			break;
		default:
			DrawFrame.allOff();
		}
		checkButtonVisible();
	}

	public void checkButtonVisible() {
		if (StepState == 0) {
			BackButton.setVisible(false);
			NextButton.setVisible(true);
		} else if (StepState == StepTitle.length - 1) {
			BackButton.setVisible(true);
			NextButton.setVisible(false);
		} else {
			BackButton.setVisible(true);
			NextButton.setVisible(true);
		}
	}

	public JButton getNextButton() {
		if (NextButton == null) {
			NextButton = new JButton("Next");
			NextButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					if (StepState == 1) {
						if(getBPanel().tIP.getText().length() * getBPanel().tUser.getText().length() * getBPanel().tPassword.getText().length() == 0){
							JOptionPane.showMessageDialog(null,
							"Please fill all the information.");
							return;
						}
						getBPanel().saveNodeData();
					}

					if (StepState == 2 && !getNPanel().TableContent.checkPass()) {
						JOptionPane.showMessageDialog(null,
								"You should have at lease TWO nodes (master and slaves).");
						return;
					}

					if (StepState < StepTitle.length - 1) {
						StepState++;

						CardLayout cl = (CardLayout) CardPanel.getLayout();
						cl.show(CardPanel, StepTitle[StepState]);
					}
					CardPageChanged();
				}
			});
		}
		return NextButton;
	}

	public JButton getBackButton() {
		if (BackButton == null) {
			BackButton = new JButton("Back");
			BackButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					if (StepState > 0) {
						StepState--;
						CardLayout cl = (CardLayout) CardPanel.getLayout();
						cl.show(CardPanel, StepTitle[StepState]);
					}
					CardPageChanged();
				}
			});
		}
		return BackButton;
	}

	@Override
	protected void processEvent(AWTEvent arg0) {
		if (arg0.getID() == WindowEvent.WINDOW_CLOSING && InstallState != -1) {
			int tmp = JOptionPane
					.showConfirmDialog(
							HadoopInstall.this,
							"It may cause unrecoverable error of your cluster installation at this time.\nExit anyway?",
							"Confirm", JOptionPane.YES_NO_OPTION);
			if (tmp == JOptionPane.YES_OPTION) {
				super.processEvent(arg0);
			} else {
				return;
			}
		}
		super.processEvent(arg0);
	}

	public JPanel getStepTagPanel() {
		JPanel Stag = new JPanel();
		Stag.setLayout(new BoxLayout(Stag, BoxLayout.PAGE_AXIS));

		JPanel tmpUP = new JPanel();
		tmpUP.setLayout(new BoxLayout(tmpUP, BoxLayout.X_AXIS));
		tmpUP.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
		tmpUP.add(getStartButton());
		tmpUP.add(Box.createHorizontalStrut(20));
		tmpUP.add(getSPanel());
		tmpUP.add(Box.createGlue());

		Stag.add(tmpUP);

		JScrollPane tmpScr = new JScrollPane(getInstallArea());
		tmpScr.setBorder(BorderFactory
				.createTitledBorder("Installation Progress"));

		JPanel tmpDOWN = new JPanel();
		tmpDOWN.setLayout(new BorderLayout());
		tmpDOWN.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		tmpDOWN.add(tmpScr, BorderLayout.CENTER);
		Stag.add(tmpDOWN);

		return Stag;
	}

	public StepProgressPanel getSPanel() {
		if (SPanel == null) {
			SPanel = new StepProgressPanel();
			SPanel.addStep(new StepProgressLabel(
					"Step1. Generating configurations...", 20));
			SPanel.addStep(new StepProgressLabel(
					"Step2. Preparing installation...", 20));
			SPanel.addStep(new StepProgressLabel(
					"Step3. Deploying Hadoop Cloud...", 20));

		}
		return SPanel;
	}

	public NodeConfigPanel getNPanel() {
		if (NPanel == null) {
			NPanel = new NodeConfigPanel();

		}
		return NPanel;
	}

	public BridgeConfigPanel getBPanel() {
		if (BPanel == null) {
			BPanel = new BridgeConfigPanel();
			BPanel.setLabelText("Please provide access information of the Master Node as follows.");
		}
		return BPanel;
	}

	public JPanel getRPanel() {
		if (RPanel == null) {
			RPanel = new JPanel();
			RPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			RPanel.setLayout(new BoxLayout(RPanel,BoxLayout.PAGE_AXIS));
			RPanel.setPreferredSize(new Dimension(500, 300));
			
			JEditorPane jep = new JEditorPane();
			jep.setEditable(false);
			jep.setBackground(RPanel.getBackground());
			jep.setContentType("text/html");
			jep.setText("<HTML><B>CloudDOE Deploy tool is an easy-to-use wizard to install and configure a <a href='http://wiki.apache.org/hadoop/HadoopMapReduce'>Hadoop</a>"+
					" Cloud on a cluster of PCs running <a href='http://www.ubuntu.com/'>Ubuntu</a> Linux.<BR><BR>"+
					"Note that:"
					+ "<UL><LI>CloudDOE assumes that each PCs has access to the Internet to download Hadoop and Java packages from their official sites.</LI>"
					+ "<LI>CloudDOE requires access information of PCs of the cluster, i.e., privileged username and password, and IP address, for deploying a Hadoop Cloud.</LI>"
					+ "<LI>CloudDOE configures one of the PCs of the cluster as master node, and the other PCs of the cluster as slave nodes."
					+ "<UL><LI>A master node functions as <a href='http://wiki.apache.org/hadoop/NameNode'>Name Node</a> and <a href='http://wiki.apache.org/hadoop/JobTracker'>JobTracker</a>.</LI>"
					+ "<LI>A slave node acts functions  as <a href='http://wiki.apache.org/hadoop/DataNode'>Data Node</a> and <a href='http://wiki.apache.org/hadoop/TaskTracker'>TaskTracker</a>.</LI></UL>"
					+ "<LI>CloudDOE will connect to the master node and then continuing to configure the Hadoop Cloud.</LI></UL>"
					+ "</B></HTML>");
			jep.addHyperlinkListener(new HyperlinkListener() {
				
				@Override
				public void hyperlinkUpdate(HyperlinkEvent arg0) {
					if(arg0.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
						try {
							if (Desktop.isDesktopSupported()) {
								Desktop.getDesktop().browse(arg0.getURL().toURI());
							} else {
								/* TODO: error handling */
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			RPanel.add(jep);
		}
		return RPanel;
	}

	public JButton getStartButton() {
		if (StartButton == null) {
			StartButton = new JButton("GO!");
			StartButton.setFont(new Font("Arial", Font.PLAIN, 30));
			StartButton.setPreferredSize(new Dimension(150, 70));
			StartButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (getNPanel().TableContent.Content.size() < 1) {
						JOptionPane.showMessageDialog(null,
								"Cloud Node list is empty.");
						return;
					}
					if (getMaster() == null) {
						JOptionPane.showMessageDialog(null, "No Mater Node.");
						return;
					}
					int r = JOptionPane
							.showConfirmDialog(
									HadoopInstall.this,
									"CloudDOE - Deploy Wizard will change settings of your cluster computer. \nContinue to install?",
									"Confirm", JOptionPane.YES_NO_OPTION);
					if (r == JOptionPane.NO_OPTION) {
						return;
					}
					getInstallArea().setText("");
					setState(0);
				}
			});
		}
		return StartButton;
	}

	public JTextArea getInstallArea() {
		if (InstallArea == null) {
			InstallArea = new JTextArea();
			InstallArea.setEditable(false);
			InstallArea.setBackground(new JPanel().getBackground());
			DefaultCaret caret = (DefaultCaret) InstallArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		}
		return InstallArea;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				HadoopInstall tmp = new HadoopInstall();
				tmp.DrawFrame.start();
				tmp.UnistallPanel.DrawingPanel.start();
			}
		});

	}

}
