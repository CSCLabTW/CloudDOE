package tw.edu.sinica.iis.GUI.Deploy;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultCaret;

import tw.edu.sinica.iis.GUI.Drawing.FlowChatCanvas;
import tw.edu.sinica.iis.SSHadoop.SSHExec;
import tw.edu.sinica.iis.SSHadoop.SSHSession;
import tw.edu.sinica.iis.SSHadoop.SSHShell;
import tw.edu.sinica.iis.SSHadoop.SSHadoopCmd;

public class UninstallTabPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1073748253267361715L;

	public FlowChatCanvas DrawingPanel = null;
	public JLabel CardTitle = null;
	public JPanel CardPanel = null;
	public JButton NextButton = null;
	public JButton BackButton = null;
	public JButton StartButton = null;
	public JTextArea LogArea = null;

	public SSHadoopCmd HadoopCmd;
	public SSHSession HadoopSession;

	private SSHShell sshShell;

	private JTabbedPane parentPane;
	private int mutexTarget;

	// cards
	public JPanel RPanel;
	public BridgeConfigPanel BPanel;

	// parameters
	private enum panelOrder {
		OVERVIEW, NAMENODE, UNDEPLOY
	};

	public String[] panelTitle = { "Overview",
			"Step 1. Connect to Name Node",
			"Step 2. Hadoop Cloud Undeployment" };

	private int panelState = 0;

	private enum threadState {
		IDLE, UNDEPLOY, MONITOR, CLEANUP, FINISH
	};

	private static final String BINLOCATION = "~/workspace/bin";
	private static final String LOGFILENAME = "~/OCIuninstall.log";
	private static final String PIDFILENAME = "~/ociuninstall.pid";

	public UninstallTabPanel() {
		init();
	}

	public void init() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(getTitlePanel());
		this.add(getDrawPanel());
		this.add(getCardPanel());
		this.add(getButtonPanel());

		changeCard(0);
	}

	public void setMutex(final JTabbedPane parent, final int mutexTarget) {
		parentPane = parent;
	}

	public JPanel getTitlePanel() {
		CardTitle = new JLabel();
		JPanel tmpTP = new JPanel();
		tmpTP.add(CardTitle);
		return tmpTP;
	}

	public JPanel getDrawPanel() {
		DrawingPanel = new FlowChatCanvas();
		DrawingPanel.getWindowCanvas()
				.setPreferredSize(new Dimension(600, 170));
		JPanel DPanel = new JPanel();
		DPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		DPanel.add(DrawingPanel.getWindowCanvas());
		return DPanel;
	}

	public JPanel getCardPanel() {
		if (CardPanel == null) {
			CardPanel = new JPanel();
			CardPanel.setLayout(new CardLayout());
			CardPanel.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.RAISED));
			CardPanel.add(getRPanel(),
					panelTitle[panelOrder.OVERVIEW.ordinal()]);
			CardPanel.add(getBPanel(),
					panelTitle[panelOrder.NAMENODE.ordinal()]);
			CardPanel.add(getStepTagPanel(),
					panelTitle[panelOrder.UNDEPLOY.ordinal()]);
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

	public JButton getBackButton() {
		if (BackButton == null) {
			BackButton = new JButton("Back");
			BackButton.setActionCommand("back");
			BackButton.addActionListener(this);
		}
		return BackButton;
	}

	public JButton getNextButton() {
		if (NextButton == null) {
			NextButton = new JButton("Next");
			NextButton.setActionCommand("next");
			NextButton.addActionListener(this);
		}
		return NextButton;
	}

	public void changeCard(int state) {
		if (state < 0 || state >= panelTitle.length) {
			return;
		}

		panelState = state;
		
		mutexPanel();
		loadData();
		changeTitle();
		buttonConfig();

		changeCardPanel();
	}

	private void mutexPanel() {
		if (parentPane != null) {
			if (panelState != panelOrder.OVERVIEW.ordinal()) {
				parentPane.setEnabledAt(mutexTarget, false);
			} else {
				parentPane.setEnabledAt(mutexTarget, true);
			}
		}
	}

	public void loadData() {
		if (panelState == panelOrder.NAMENODE.ordinal()) {
			DrawingPanel.showPublicNN();
			getBPanel().loadNodeData();
		} else {
			DrawingPanel.allOff();
		}
	}

	public void changeTitle() {
		if (panelState >= 0 && panelState < panelTitle.length
				&& CardTitle != null) {
			CardTitle.setText("<HTML><FONT SIZE=6>" + panelTitle[panelState]
					+ "</FONT></HTML>");
		}
	}

	public void buttonConfig() {
		if (panelState > 0) {
			getBackButton().setVisible(true);
		} else {
			getBackButton().setVisible(false);
		}

		if (panelState < panelTitle.length - 1) {
			getNextButton().setVisible(true);
		} else {
			getNextButton().setVisible(false);
		}
	}

	public void changeCardPanel() {
		CardLayout cl = (CardLayout) CardPanel.getLayout();
		cl.show(CardPanel, panelTitle[panelState]);
	}

	public BridgeConfigPanel getBPanel() {
		if (BPanel == null) {
			BPanel = new BridgeConfigPanel();
			BPanel.setLabelText("Please provide access information of Name Node as follows:");
		}
		return BPanel;
	}

	public JPanel getStepTagPanel() {
		JPanel Stag = new JPanel();
		Stag.setLayout(new BoxLayout(Stag, BoxLayout.PAGE_AXIS));

		JPanel tmpUP = new JPanel();
		tmpUP.setLayout(new BoxLayout(tmpUP, BoxLayout.X_AXIS));
		tmpUP.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
		tmpUP.add(getStartButton());
		tmpUP.add(Box.createHorizontalStrut(20));
		tmpUP.add(Box.createGlue());

		Stag.add(tmpUP);

		JScrollPane tmpScr = new JScrollPane(getLogArea());
		tmpScr.setBorder(BorderFactory
				.createTitledBorder("Uninstallation Progress"));

		JPanel tmpDOWN = new JPanel();
		tmpDOWN.setLayout(new BorderLayout());
		tmpDOWN.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		tmpDOWN.add(tmpScr, BorderLayout.CENTER);
		Stag.add(tmpDOWN);

		return Stag;
	}

	public JPanel getRPanel() {
		if (RPanel == null) {
			RPanel = new JPanel();
			RPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			RPanel.setLayout(new BoxLayout(RPanel, BoxLayout.PAGE_AXIS));
			RPanel.setPreferredSize(new Dimension(500, 300));

			JEditorPane jep = new JEditorPane();
			jep.setEditable(false);
			jep.setBackground(RPanel.getBackground());
			jep.setContentType("text/html");
			jep.setText("<HTML><B>"
					+ "CloudDOE undeploy tool helps you to uninstall the Hadoop Cloud installed by CloudDOE. This tool will perform the following actions:"
					+ "<UL><LI>Stop Hadoop services</LI>"
					+ "<LI>Uninstall Hadoop environment</LI>"
					+ "<LI>Restore configurations</LI>"
					+ "</UL><BR>"
					+ "Note that"
					+ "<UL><LI>There will be errors in using the tool to undeploy a Hadoop Cloud installed by other procedures.</LI>"
					+ "<LI>This tool will read configurations stored on last deployment as default settings.</LI>"
					+ "<LI>The uninsttallation procedures cannot be undone, cancelled or stoped.</LI>"
					+ "</UL>" + "</B></HTML>");
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
					int r = JOptionPane
							.showConfirmDialog(
									UninstallTabPanel.this,
									"CloudDOE - Deploy Wizard will uninstall your Hadoop cloud. \nThe procedure cannot be interrupt or cancel. Continue?",
									"Confirm", JOptionPane.YES_NO_OPTION);
					if (r == JOptionPane.NO_OPTION) {
						return;
					}
					getLogArea().setText("");
					setState(threadState.UNDEPLOY);
				}
			});
		}
		return StartButton;
	}

	synchronized public void setState(threadState s) {
		switch (s) {
		case IDLE:
			// not start yet
			changeEnableFlag(true);
			break;
		case UNDEPLOY:
			changeEnableFlag(false);
			startUndeployThread();
			break;
		case MONITOR:
			changeEnableFlag(false);
			startMonitorThread();
			break;
		case CLEANUP:
			changeEnableFlag(false);
			startCleanupThread();
			break;
		case FINISH:
			changeEnableFlag(true);
			startFinishThread();
			// all finished
			break;
		}
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

	public void startUndeployThread() {
		new Thread() {
			@Override
			public void run() {
				boolean succ = ConnectHadoop();

				if (!succ) {
					JOptionPane.showMessageDialog(null,
							"Connect to Name Node failed.");
					setState(threadState.IDLE);
					return;
				}

				String testCmd = HadoopCmd.OCIRun("uninstall.sh", LOGFILENAME);
				sshShell = new SSHShell(HadoopSession.getSession(), testCmd);

				try {
					sshShell.call();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error occurs.");
					setState(threadState.IDLE);
				}

				setState(threadState.MONITOR);
			}
		}.start();
	}

	public void startMonitorThread() {
		new Thread() {
			@Override
			public void run() {
				try {
					ConnectHadoop();

					getLogArea().setText(
							"Fetching uninstallation progress messages...");

					long sleepTime = 500;
					do {
						sleep(sleepTime);
						replaceLog();
						sleepTime = 5000;
					} while (!checkFinish());

					setState(threadState.CLEANUP);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"Uninstallation Failed.");
					setState(threadState.IDLE);
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
			setState(threadState.IDLE);
		}

		return false;
	}

	public void replaceLog() {
		String testCmd = HadoopCmd.OCILog(LOGFILENAME, 30);
		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				testCmd);

		String log = "";
		try {
			log = channel.call();
			if (!"".equals(log)) {
				getLogArea().setText(log);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error occurs.");
			setState(threadState.IDLE);
		}
	}

	public void startCleanupThread() {
		new Thread() {
			@Override
			public void run() {
				ConnectHadoop();

				String testCmd = HadoopCmd.OCIClenup("~/workspace ~/OCI*");
				Callable<String> channel = new SSHExec(
						HadoopSession.getSession(), testCmd);

				try {
					channel.call();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error occurs.");
					setState(threadState.IDLE);
				}

				setState(threadState.FINISH);
			}
		}.start();
	}

	public void startFinishThread() {
		JOptionPane.showMessageDialog(null, "Uninstallation Finished.");
		setState(threadState.IDLE);
	}

	public void changeEnableFlag(boolean f) {
		getStartButton().setEnabled(f);
		NextButton.setEnabled(f);
		BackButton.setEnabled(f);
	}

	public JTextArea getLogArea() {
		if (LogArea == null) {
			LogArea = new JTextArea();
			LogArea.setEditable(false);
			LogArea.setBackground(new JPanel().getBackground());
			DefaultCaret caret = (DefaultCaret) LogArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		}
		return LogArea;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String msg = e.getActionCommand();
		if (msg.equals("back")) {
			changeCard(panelState - 1);
		} else if (msg.equals("next")) {
			changeCard(panelState + 1);
		}
	}

	public static void main(String[] args) {
	}

}
