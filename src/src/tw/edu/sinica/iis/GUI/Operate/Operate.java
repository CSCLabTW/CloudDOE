package tw.edu.sinica.iis.GUI.Operate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import tw.edu.sinica.iis.GUI.Operate.XMLConfigParser.DownloadItem;
import tw.edu.sinica.iis.GUI.Operate.XMLConfigParser.downloadType;
import tw.edu.sinica.iis.GUI.Operate.XMLConfigParser.logType;
import tw.edu.sinica.iis.GUI.Operate.XMLConfigParser.paramType;
import tw.edu.sinica.iis.SSHadoop.SSHRun;
import tw.edu.sinica.iis.SSHadoop.SSHExec;
import tw.edu.sinica.iis.SSHadoop.SSHSession;
import tw.edu.sinica.iis.SSHadoop.SSHSftp;
import tw.edu.sinica.iis.SSHadoop.SSHadoopCmd;
import tw.edu.sinica.iis.SSHadoop.SSHadoopUtils;
import tw.edu.sinica.iis.SSHadoop.SSHadoopUtils.OperateStatus;

public class Operate extends JPanel {

	private static final long serialVersionUID = -1666416040585522565L;

	private Properties prop;

	public JFrame parent;

	public JTabbedPane Tabs;

	public ConnectPanel cPanel;
	public UploadPanel uPanel;
	public RunPanel rPanel;
	public AboutPanel aPanel;

	public JLabel StatusBar;
	public boolean hasStatusBar = false;

	public WorkingDialog progressDialog;

	public JDialog selectDialog;

	public SSHSession HadoopSession;
	public SSHadoopCmd HadoopCmd;

	public MonitorThread mThread;

	public static enum jobStatus {
		NOT_CONNECTED, JOB_READY, JOB_WORKING, JOB_FINISHED
	}

	public final static String PROPERTYFILE = "clouddoe.properties";

	public final static String INITIAL = "INITIAL";
	public final static String DEF_IP = "DEF_IP";
	public final static String DEF_PORT = "DEF_PORT";
	public final static String DEF_USERNAME = "DEF_USERNAME";
	public final static String JOB_ID = "JOB_ID";
	public final static String JOB_RESULT = "JOB_RESULT";
	public final static String JOB_PARAS = "JOB_PARAS";
	public final static String LOAD_PROG = "LOAD_PROG";

	public final static String SPECIALCMD = "export HADOOP_HOME=/opt/hadoop; source /etc/profile; export PATH=$PATH:$HADOOP_HOME/bin";

	public jobStatus Status;

	public String UID;

	public boolean initialled;

	public String Ip;
	public String Port;
	public String ID;
	public String Password;

	public String job_id;

	public String job_paras_label;
	public String job_prog_load;

	public LinkedList<String> fileList;

	public Operate(JFrame p) {
		super();
		parent = p;

		initProperties();
		readProperties();
		init();

		StatusChange(jobStatus.NOT_CONNECTED);
	}

	private boolean saveProperties(final boolean initial) {
		try {
			FileWriter fw = new FileWriter(PROPERTYFILE);
			if (!initial) {
				prop.store(fw, "CloudDOE Properties");
			}
			fw.close();

			return true;
		} catch (IOException e1) {
		}

		return false;
	}

	public void initProperties() {
		if (prop == null) {
			prop = new Properties();
		} else {
			prop.clear();
		}

		try {
			prop.load(new FileInputStream(PROPERTYFILE));
		} catch (FileNotFoundException e) {
			saveProperties(true);
		} catch (IOException e) {
		}

		UID = prop.getProperty("UID", "");
		if (UID.equals("")) {
			String user = System.getProperty("user.name");
			if (user == null || user.equals("")) {
				user = "unknown";
			}

			UID = System.currentTimeMillis() + "_" + user;

			prop.setProperty("UID", UID);
			prop.setProperty(INITIAL, "no");

			saveProperties(false);
		}
	}

	public void readProperties() {
		initialled = Boolean.parseBoolean(prop.getProperty(INITIAL, "false"));

		Ip = prop.getProperty(DEF_IP, "");
		Port = prop.getProperty(DEF_PORT, "22");
		ID = prop.getProperty(DEF_USERNAME, "");
		Password = "";

		job_id = prop.getProperty(JOB_ID, "");
		job_paras_label = prop.getProperty(JOB_PARAS, "");
		job_prog_load = prop.getProperty(LOAD_PROG, "");
	}

	public void resetProperties() {
		prop.setProperty(INITIAL, "false");

		prop.setProperty(DEF_IP, "");
		prop.setProperty(DEF_PORT, "");
		prop.setProperty(DEF_USERNAME, "");
		prop.setProperty(JOB_ID, "");
		prop.setProperty(JOB_PARAS, "");
		prop.setProperty(LOAD_PROG, "");

		saveProperties(false);
	}

	public void updateProperties() {
		prop.setProperty(INITIAL, String.valueOf(initialled));

		prop.setProperty(DEF_IP, Ip);
		prop.setProperty(DEF_PORT, Port);
		prop.setProperty(DEF_USERNAME, ID);
		prop.setProperty(JOB_ID, job_id);
		prop.setProperty(JOB_PARAS, job_paras_label);
		prop.setProperty(LOAD_PROG, job_prog_load);

		saveProperties(false);
	}

	public void StatusChange(final jobStatus jobs) {
		switch (jobs) {
		case NOT_CONNECTED:
			Tabs.setEnabledAt(1, false);
			Tabs.setEnabledAt(2, false);

			boolean job_complete = "".equals(job_id);
			cPanel.ipText.setEditable(job_complete);
			cPanel.portText.setEditable(job_complete);
			cPanel.IDText.setEditable(job_complete);
			cPanel.PasswordText.setEditable(true);
			cPanel.loadButton.setEnabled(true);
			cPanel.connectButton.setText("Connect");

			Status = jobStatus.NOT_CONNECTED;
			break;
		case JOB_WORKING:
			Tabs.setEnabledAt(1, false);
			Tabs.setEnabledAt(2, true);

			cPanel.ipText.setEditable(false);
			cPanel.portText.setEditable(false);
			cPanel.IDText.setEditable(false);
			cPanel.PasswordText.setEditable(false);
			cPanel.loadButton.setEnabled(false);
			cPanel.connectButton.setText("Disconnect");

			rPanel.programSelector.setEnabled(false);
			if (rPanel.ParameterText != null) {
				for (int i = 0; i < rPanel.ParameterText.length; i++) {
					rPanel.ParameterText[i].setEnabled(false);
				}
			}
			rPanel.HadoopBar.setString("Refreshing Status...");
			rPanel.ResultDownload.setEnabled(false);
			rPanel.WorkRun.setEnabled(false);
			rPanel.ResultClear.setEnabled(true);
			rPanel.ResultClear.setText("Cancel");
			Status = jobStatus.JOB_WORKING;
			break;
		case JOB_FINISHED:
			Tabs.setEnabledAt(1, false);
			Tabs.setEnabledAt(2, true);

			cPanel.ipText.setEditable(false);
			cPanel.portText.setEditable(false);
			cPanel.IDText.setEditable(false);
			cPanel.PasswordText.setEditable(false);
			cPanel.loadButton.setEnabled(false);
			cPanel.connectButton.setText("Disconnect");

			rPanel.programSelector.setEnabled(true);
			if (rPanel.ParameterText != null) {
				for (int i = 0; i < rPanel.ParameterText.length; i++) {
					rPanel.ParameterText[i].setEnabled(true);
				}
			}
			rPanel.ResultDownload.setEnabled(true);
			rPanel.ResultClear.setEnabled(true);
			rPanel.WorkRun.setEnabled(true);
			rPanel.ResultClear.setText("Reset");
			Status = jobStatus.JOB_FINISHED;
			break;
		case JOB_READY:
			Tabs.setEnabledAt(1, true);
			Tabs.setEnabledAt(2, true);

			cPanel.ipText.setEditable(false);
			cPanel.portText.setEditable(false);
			cPanel.IDText.setEditable(false);
			cPanel.PasswordText.setEditable(false);
			cPanel.loadButton.setEnabled(false);
			cPanel.connectButton.setText("Disconnect");

			rPanel.programSelector.setEnabled(true);
			if (rPanel.ParameterText != null) {
				for (int i = 0; i < rPanel.ParameterText.length; i++) {
					rPanel.ParameterText[i].setEnabled(true);
				}
			}
			rPanel.ResultDownload.setEnabled(false);
			rPanel.ResultClear.setEnabled(false);
			rPanel.ResultClear.setText("Reset");
			rPanel.WorkRun.setEnabled(true);
			rPanel.HadoopBar.setString("Server Ready");
			Status = jobStatus.JOB_READY;
			break;
		}
	}

	public void statusMSG(String s) {
		if (hasStatusBar) {
			StatusBar.setText(s);
		}

	}

	public void init() {

		this.setLayout(new BorderLayout());

		HadoopSession = new SSHSession();

		fileList = new LinkedList<String>();

		Tabs = new JTabbedPane();

		cPanel = new ConnectPanel();
		Tabs.addTab("Connect", cPanel);
		uPanel = new UploadPanel();
		Tabs.addTab("Upload", uPanel);
		rPanel = new RunPanel(job_prog_load);
		Tabs.addTab("Run", rPanel);
		aPanel = new AboutPanel();

		// TODO: clean up
		JScrollPane scrollPane = new JScrollPane(new AboutPanel());
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(400, 300));
		Tabs.addTab("About", scrollPane);

		this.add(Tabs, BorderLayout.CENTER);

		if (hasStatusBar) {
			StatusBar = new JLabel();
			StatusBar.setText("Status Bar");
			StatusBar.setBorder(BorderFactory.createEtchedBorder());
			this.add(StatusBar, BorderLayout.SOUTH);
		}

		ConnectPanelSetting();
		UploadPanelSetting();
		RunPanelSetting();
	}

	public void ConnectPanelSetting() {
		cPanel.ipText.setText(Ip);
		cPanel.portText.setText(Port);
		cPanel.IDText.setText(ID);
		cPanel.PasswordText.setText(Password);

		cPanel.connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Status == jobStatus.NOT_CONNECTED) {

					// check ip changed
					if (Ip.length() > 1 && !Ip.equals(cPanel.ipText.getText())) {
						int ans = JOptionPane
								.showConfirmDialog(
										Operate.this,
										"Warning!\nYou're trying to login into another Hadoop cluster with\n an existed connection settings. All the settings will\n be erased when you click OK.");
						if (ans == JOptionPane.OK_OPTION) {
							initProperties();
							resetProperties();
							readProperties();
						} else {
							return;
						}
					}
					// ----------------

					Ip = cPanel.ipText.getText();
					Port = cPanel.portText.getText();
					ID = cPanel.IDText.getText();
					Password = new String(cPanel.PasswordText.getPassword());

					if (!initialled) {
						progressDialog = new WorkingDialog(parent, true,
								"Initializing");
					} else {
						progressDialog = new WorkingDialog(parent, true,
								"Connecting");
					}

					new Thread(new Runnable() {
						@Override
						public void run() {
							progressDialog.setVisible(true);
						}
					}).start();

					new Thread(new Runnable() {
						@Override
						public void run() {
							if (connectHadoop(Ip, Port, ID, Password)) {
								refreshFileList();
								updateProperties();
								if ("".equals(job_id)) {
									StatusChange(jobStatus.JOB_READY);
								} else {
									StatusChange(jobStatus.JOB_WORKING);
									startThread();
									String[] paras = job_paras_label.split(";");
									if (rPanel.ParameterText != null) {
										for (int i = 0; i < paras.length; i++) {
											rPanel.ParameterText[i]
													.setText(paras[i]);
										}
									}
								}
								statusMSG("Connected!");
								if (Status == jobStatus.JOB_WORKING) {
									Tabs.setSelectedIndex(2);
								} else {
									Tabs.setSelectedIndex(1);
								}
							} else {
								JOptionPane.showMessageDialog(null,
										"Login failed.");
							}
							progressDialog.dispose();

						}
					}).start();

				} else {
					if (disconnect()) {
						StatusChange(jobStatus.NOT_CONNECTED);
						statusMSG("Disconnected!");
					}
				}

			}
		});

		cPanel.loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Load NP file first
				String NNFile = TextExtractor.getFileText(new File("workspace"
						+ File.separator + "config" + File.separator + "common"
						+ File.separator + "NP"));

				// Load NN file if there is no NP file
				if (NNFile.length() == 0) {
					NNFile = TextExtractor.getFileText(new File("workspace"
							+ File.separator + "config" + File.separator
							+ "common" + File.separator + "NN"));
				}

				if (NNFile.length() > 1) {
					String[] sp = NNFile.replace("\n", "").split("\t");
					if (sp.length == 3) {
						cPanel.ipText.setText(sp[0]);
						cPanel.IDText.setText(sp[1]);
						cPanel.PasswordText.setText(sp[2]);
					}
				} else {
					JOptionPane
							.showMessageDialog(null, "Can not find NN file.");
				}
			}
		});
	}

	public void UploadPanelSetting() {
		uPanel.TableData.setFiles(fileList);
		uPanel.TableData.fireTableDataChanged();

		uPanel.UploadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(new File("workspace"
						+ File.separator + "data"));
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

				int returnVal = fc.showDialog(Operate.this, "Upload");
				String path = "";
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					path = fc.getSelectedFile().getAbsolutePath();

					int r = JOptionPane.showConfirmDialog(Operate.this,
							"Upload selected file : \n " + path, "WARNING",
							JOptionPane.YES_NO_OPTION);
					if (r == JOptionPane.NO_OPTION) {
						return;
					}
				} else {
					return;
				}

				try {

					statusMSG("Uploading File.");
					progressDialog = new WorkingDialog(parent, true,
							"Uploading");
					new Thread(new Runnable() {
						@Override
						public void run() {
							progressDialog.setVisible(true);
						}
					}).start();

					final File upFile = new File(path);
					new Thread(new Runnable() {

						@Override
						public void run() {
							if (upload(upFile)) {

								statusMSG("File Uploaded.");
							} else {
								JOptionPane.showMessageDialog(null,
										"Upload Failed.");
								statusMSG("Upload Failed.");
							}
							progressDialog.dispose();
						}
					}).start();

				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Upload Failed");
					statusMSG("Upload Failed.");
					return;
				}

			}
		});

		uPanel.RemoveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int r = JOptionPane.showConfirmDialog(Operate.this,
							"Remove selected file(s)?", "WARNING",
							JOptionPane.YES_NO_OPTION);
					if (r == JOptionPane.NO_OPTION) {
						return;
					}

					statusMSG("Removing File(s).");
					progressDialog = new WorkingDialog(parent, true, "Removing");
					new Thread(new Runnable() {
						@Override
						public void run() {
							progressDialog.setVisible(true);
						}
					}).start();
					new Thread(new Runnable() {
						@Override
						public void run() {
							if (removeSelected()) {
								statusMSG("File(s) Removed.");
							} else {
								JOptionPane.showMessageDialog(null,
										"Remove Failed.");
								statusMSG("Remove Failed.");
							}
							progressDialog.dispose();
						}
					}).start();

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Remove Failed");
					return;
				}
			}
		});

		uPanel.RefreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					statusMSG("Refreshing Input List.");
					progressDialog = new WorkingDialog(parent, true,
							"Refreshing");
					new Thread(new Runnable() {
						@Override
						public void run() {
							progressDialog.setVisible(true);
						}
					}).start();
					new Thread(new Runnable() {
						@Override
						public void run() {
							if (refreshFileList()) {

								statusMSG("Input List Refreshed.");
							} else {
								JOptionPane.showMessageDialog(null,
										"Refresh Failed.");
								statusMSG("Refresh Failed.");
							}
							progressDialog.dispose();
						}
					}).start();

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Refresh Failed");
					return;
				}
			}
		});
	}

	public void RunPanelSetting() {
		rPanel.programHelper.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String information = "Program help information is not avalable.";
				if (!rPanel.xmlConfigParser.programInfo.website.equals("")) {
					information = "Program help information is avaliable at \n"
							+ rPanel.xmlConfigParser.programInfo.website;
				}

				JOptionPane.showMessageDialog(Operate.this, information,
						"Information", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		rPanel.WorkRun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (job_id.length() > 0) {
					int r = JOptionPane
							.showConfirmDialog(
									Operate.this,
									"Previous results will be cleared before next run, contine?",
									"WARNING", JOptionPane.YES_NO_OPTION);
					if (r == JOptionPane.NO_OPTION) {
						return;
					} else {
						ClearJob();
					}
				}

				if (fileList.size() < 1) {
					JOptionPane.showMessageDialog(null, "No input file.");
					Tabs.setSelectedIndex(1);
					return;
				}

				if (!rPanel.getAndCheckParameter()) {
					JOptionPane.showMessageDialog(null, "Format error!");
					return;
				}

				rPanel.HadoopBar.setString("Running");
				job_id = RunJob();
				if (job_id != null) {
					updateProperties();
					StatusChange(jobStatus.JOB_WORKING);
					startThread();
				} else {
					job_id = "";
					JOptionPane.showMessageDialog(null, "Job failed");
					JobError();
				}
			}
		});

		rPanel.ResultDownload.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showSaveDialog(Operate.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					statusMSG("Downloading Result.");
					progressDialog = new WorkingDialog(parent, true,
							"Downloading");
					new Thread(new Runnable() {
						@Override
						public void run() {
							progressDialog.setVisible(true);
						}
					}).start();
					new Thread(new Runnable() {
						@Override
						public void run() {
							if (ResultDownload(fc)) {
								JOptionPane.showMessageDialog(null,
										"Download Completed.");
								statusMSG("Result Downloaded.");
							} else {
								JOptionPane.showMessageDialog(null,
										"Download Failed.");
								statusMSG("Download Failed.");
							}
							progressDialog.dispose();
						}
					}).start();
				}

			}
		});

		rPanel.ResultClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (Status == jobStatus.JOB_WORKING) {
						// Cancel
						int r = JOptionPane.showConfirmDialog(Operate.this,
								"Cancel this running job?", "WARNING",
								JOptionPane.YES_NO_OPTION);
						if (r == JOptionPane.NO_OPTION) {
							return;
						}
						statusMSG("Waiting for unfinish modules.");
						if (CancelJob()) {
							rPanel.ResultClear.setEnabled(false);
						}
					} else {
						// Clear
						int r = JOptionPane.showConfirmDialog(Operate.this,
								"Clear result and input data?", "WARNING",
								JOptionPane.YES_NO_OPTION);
						if (r == JOptionPane.NO_OPTION) {
							return;
						}

						statusMSG("Resetting.");
						progressDialog = new WorkingDialog(parent, true,
								"Resetting");
						new Thread(new Runnable() {
							@Override
							public void run() {
								progressDialog.setVisible(true);
							}
						}).start();
						new Thread(new Runnable() {
							@Override
							public void run() {
								if (ClearJob()) {
									ClearDataDir();
									if (Status == jobStatus.JOB_FINISHED) {
										updateProperties();

										rPanel.programSelector
												.setSelectedItem(RunPanel.SELECTOR_DEFAULT);
										rPanel.HadoopTotalBar.setValue(0);
										rPanel.HadoopBar.setValue(0);
										StatusChange(jobStatus.JOB_READY);

										statusMSG("Resetted.");
									} else {
										JOptionPane.showMessageDialog(null,
												"Reset Failed.");
										statusMSG("Reset Failed.");
									}
									progressDialog.dispose();
									Tabs.setSelectedIndex(1);
								}
							}
						}).start();
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}

	public void startThread() {
		if (mThread == null || !mThread.isAlive()) {
			mThread = new MonitorThread(paramType.WORK.toString().toLowerCase());
			rPanel.HadoopTotalBar.setValue(0);
			rPanel.HadoopBar.setValue(0);
			mThread.start();
		}
		statusMSG("Job Running.");
	}

	public boolean connectHadoop(String ip, String port, String id, String psw) {
		try {
			HadoopSession.setHost(ip);
			HadoopSession.setUser(id);
			HadoopSession.setPass(psw);

			HadoopSession.setPort(Integer.parseInt(port));
			HadoopSession.initSession();

			if (HadoopSession.openSession()) {
				HadoopCmd = new SSHadoopCmd();
				HadoopCmd.setUserBase(UID);
				HadoopCmd.setServerSpecialCmd(SPECIALCMD);

				if (!initialled) {
					HDFSInitial();
				}

				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public boolean HDFSInitial() {
		SSHSftp sftp = new SSHSftp(ID, Ip);
		sftp.setSSHPass(Password);
		sftp.initSftp();
		sftp.sftpUpload(new File("workspace" + File.separator + "main")
				.getAbsolutePath(), UID + "/main");

		initialled = true;
		updateProperties();
		return true;
	}

	public boolean disconnect() {
		try {
			HadoopSession.closeSession();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public boolean upload(File path) {
		SSHSftp sftp = new SSHSftp(ID, Ip);
		sftp.setSSHPass(Password);
		sftp.initSftp();
		sftp.sftpUpload(path.getAbsolutePath(), UID + "/data/" + path.getName());

		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				HadoopCmd.moveFromLocalHdp(
						"data/" + path.getName(),
						paramType.INPUT.toString().toLowerCase() + "/"
								+ path.getName()));
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		Thread thread = new Thread(futureTask);
		thread.start();

		while (true) {
			if (futureTask.isDone()) {
				refreshFileList();
				break;
			}
		}

		return true;
	}

	public boolean removeSelected() {
		LinkedList<Integer> tmpi = new LinkedList<Integer>();
		for (int c : uPanel.FileTable.getSelectedRows()) {
			tmpi.add(new Integer(c));
		}
		for (int i = tmpi.size() - 1; i >= 0; i--) {
			int t = tmpi.get(i);

			String rmFileName = fileList.get(t);
			Callable<String> channel = new SSHExec(HadoopSession.getSession(),
					HadoopCmd.rmrHdp(paramType.INPUT.toString().toLowerCase()
							+ "/" + rmFileName));
			FutureTask<String> futureTask = new FutureTask<String>(channel);

			Thread thread = new Thread(futureTask);
			thread.start();

			while (true) {
				if (futureTask.isDone()) {
					fileList.remove(t);
					break;
				}
			}

		}
		updateProperties();
		uPanel.TableData.fireTableDataChanged();
		return true;
	}

	public boolean refreshFileList() {
		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				HadoopCmd.lsHdp(paramType.INPUT.toString().toLowerCase() + "/"));
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		Thread thread = new Thread(futureTask);
		thread.start();

		while (true) {
			if (futureTask.isDone()) {
				fileList.clear();

				try {
					String[] result = futureTask.get().split("\n");
					for (int i = 1; i < result.length; i++) {
						String dirName = result[i].substring(result[i]
								.lastIndexOf("/") + 1);

						fileList.add(dirName);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				updateProperties();
				uPanel.TableData.fireTableDataChanged();

				break;
			}
		}

		return true;
	}

	public String RunJob() {

		if (!rPanel.getAndCheckParameter()) {
			return null;
		}

		job_prog_load = rPanel.programConf;

		job_paras_label = rPanel.xmlConfigParser.genParamValList(';');

		String runCmd = HadoopCmd.touch(job_prog_load.replace(".xml", ".pid"))
				+ ";"
				+ HadoopCmd
						.mkdir(paramType.WORK.toString().toLowerCase(), true)
				+ ";"
				+ HadoopCmd.jarHdp(
						rPanel.xmlConfigParser.genJarPath(UID, "main"), "",
						rPanel.xmlConfigParser.genProgramArgs(UID, "main"))
				+ ";" + HadoopCmd.rm(job_prog_load.replace(".xml", ".pid"));

		Callable<String> channel = new SSHRun(HadoopSession.getSession(),
				runCmd);
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		Thread thread = new Thread(futureTask);
		thread.start();

		String jobId = UID + "/" + paramType.OUTPUT.toString().toLowerCase();
		while (true) {
			if (futureTask.isDone()) {

				break;
			}
		}

		return jobId;
	}

	private String genDownloadCmd() {
		String rst = "";

		for (DownloadItem d : rPanel.xmlConfigParser.downloadItems) {
			if (d.method == downloadType.GET) {
				rst += HadoopCmd.getHdp(paramType.OUTPUT.toString()
						.toLowerCase() + "/" + d.src, paramType.OUTPUT
						.toString().toLowerCase() + "/" + d.dst);
			} else if (d.method == downloadType.GETMERGE) {
				rst += HadoopCmd.getMergeRstHdp(paramType.OUTPUT.toString()
						.toLowerCase() + "/" + d.src, paramType.OUTPUT
						.toString().toLowerCase() + "/" + d.dst);
			}

			rst += ";";
		}

		return rst;
	}

	public boolean ResultDownload(JFileChooser fc) {
		if (!rPanel.getAndCheckParameter()) {
			return false;
		}

		try {
			Callable<String> channel = new SSHExec(HadoopSession.getSession(),
					genDownloadCmd());
			FutureTask<String> futureTask = new FutureTask<String>(channel);

			Thread thread = new Thread(futureTask);
			thread.start();

			while (true) {
				if (futureTask.isDone()) {
					break;
				}
			}

			SSHSftp sftp = new SSHSftp(ID, Ip);
			sftp.setSSHPass(Password);
			sftp.initSftp();
			sftp.sftpDownload(UID + "/"
					+ paramType.OUTPUT.toString().toLowerCase(), fc
					.getSelectedFile().getPath());

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	public boolean ClearJob() {
		if (!rPanel.getAndCheckParameter()) {
			return false;
		}

		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				HadoopCmd.rmrHdp(paramType.OUTPUT.toString().toLowerCase()
						+ "*"));
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		Thread thread = new Thread(futureTask);
		thread.start();

		while (true) {
			if (futureTask.isDone()) {
				break;
			}
		}

		job_id = "";
		job_paras_label = "";
		job_prog_load = "";

		SSHSftp sftp = new SSHSftp(ID, Ip);
		sftp.setSSHPass(Password);
		sftp.initSftp();
		sftp.sftpDelete(UID + "/" + paramType.WORK.toString().toLowerCase());
		sftp.sftpDelete(UID + "/" + paramType.OUTPUT.toString().toLowerCase());

		return true;
	}

	public boolean ClearDataDir() {
		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				HadoopCmd
						.rmrHdp(paramType.INPUT.toString().toLowerCase() + "/"));
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		Thread thread = new Thread(futureTask);
		thread.start();

		while (true) {
			if (futureTask.isDone()) {
				break;
			}
		}

		fileList.clear();

		uPanel.TableData.fireTableDataChanged();
		return true;
	}

	public boolean CancelJob() {

		if (job_id == null || "cancel".equals(job_id)) {
			return false;
		}
		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				HadoopCmd.rm(job_prog_load.replace(".xml", ".pid")) + ";"
						+ HadoopCmd.CBKillJob(UID));
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		Thread thread = new Thread(futureTask);
		thread.start();

		while (true) {
			if (futureTask.isDone()) {
				break;
			}
		}
		rPanel.HadoopBar.setString("Waiting for terminate");
		job_id = "cancel";
		updateProperties();

		return true;
	}

	public boolean MakeDataDir() {
		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				"hadoop fs -mkdir " + UID + "/"
						+ paramType.INPUT.toString().toLowerCase() + "/");
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		Thread thread = new Thread(futureTask);
		thread.start();

		while (true) {
			if (futureTask.isDone()) {
				break;
			}
		}
		return true;
	}

	public void ThreadOver() {
		StatusChange(jobStatus.JOB_FINISHED);
		statusMSG("Job Finished.");
	}

	public void JobError() {
		StatusChange(jobStatus.JOB_FINISHED);
		rPanel.ResultDownload.setEnabled(false);
		statusMSG("Job Ended with Error.");
	}

	public class MonitorThread extends Thread {

		public String monitorName = paramType.WORK.toString().toLowerCase();
		public SSHadoopUtils utils = null;

		public MonitorThread(String jobMonName) {
			monitorName = jobMonName;
			utils = new SSHadoopUtils();
		}

		public OperateStatus isJobDone() {
			OperateStatus opStatus = OperateStatus.DEFAULT;

			Callable<String> channel2 = new SSHExec(
					HadoopSession.getSession(),
					HadoopCmd.ls(job_prog_load.replace(".xml", ".pid"))
							+ ";echo -n ';';"
							+ HadoopCmd.lsHdp(paramType.OUTPUT.toString()
									.toLowerCase()
									+ "/"
									+ rPanel.xmlConfigParser
											.getSingleParam(paramType.OUTPUT).value
									+ "/_SUCCESS"));

			FutureTask<String> futureTask2 = new FutureTask<String>(channel2);

			Thread thread2 = new Thread(futureTask2);
			thread2.start();

			while (true) {
				if (futureTask2.isDone()) {
					try {
						opStatus = utils.getOPStatus(futureTask2.get());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					break;
				}
			}
			return opStatus;
		}

		public String[] getJobNameID() {

			Callable<String> channel3 = new SSHExec(HadoopSession.getSession(),
					HadoopCmd.CBStepAndId(monitorName,
							rPanel.xmlConfigParser.getLog(logType.DETAIL).name,
							80));

			String toolNameAndJobId[] = { "", "", "" };

			FutureTask<String> futureTask3 = new FutureTask<String>(channel3);

			Thread thread3 = new Thread(futureTask3);
			thread3.start();

			// get step and job id
			while (true) {
				if (futureTask3.isDone()) {
					try {
						toolNameAndJobId = utils.getCBStepAndJobId(futureTask3
								.get());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					break;
				}
			}

			return toolNameAndJobId;
		}

		public double[] getJobProcess(String jobID) {
			double[] results = { 0.0, 0.0 };
			Callable<String> channel4 = new SSHExec(HadoopSession.getSession(),
					HadoopCmd.jobStatusHdp(jobID));

			// new a job for get status
			while (true) {
				FutureTask<String> futureTask4 = new FutureTask<String>(
						channel4);

				Thread thread4 = new Thread(futureTask4);
				thread4.start();

				// if we get the status
				while (true) {
					if (futureTask4.isDone()) {
						try {
							results = utils.getJobStatus(futureTask4.get());
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
						break;
					}
				}

				return results;
			}

		}

		@Override
		public void run() {

			OperateStatus opStatus = OperateStatus.DEFAULT;

			while ((opStatus = isJobDone()) == OperateStatus.DEFAULT) {

				String[] jNameID = { "", "", "" };
				while (true) {
					String[] tmpjNameID = getJobNameID();
					if (tmpjNameID[1] != null) {
						jNameID = tmpjNameID;
					}

					if (!"".equals(jNameID[0]) && !"".equals(jNameID[1])) {
						rPanel.HadoopTotalBar.setValue(Integer
								.valueOf(jNameID[1]));
					}

					if (jNameID[2] != null) {

						double[] res = getJobProcess(jNameID[2]);
						if ("cancel".equals(job_id)) {
							rPanel.HadoopBar.setString("Waiting for terminate");
							rPanel.ResultClear.setEnabled(false);
						} else {
							rPanel.HadoopBar.setString("Running ");
							rPanel.HadoopBar.setString(rPanel.HadoopBar
									.getString() + jNameID[0]);
						}

						int total_per = (int) ((res[0] + res[1]) / 2);
						rPanel.HadoopBar.setValue(total_per);
						if (total_per >= 100 || total_per < 0) {
							if ("cancel".equals(job_id)) {
								rPanel.HadoopTotalBar.setValue(100);
								rPanel.HadoopBar.setValue(100);
								rPanel.HadoopBar.setString("Job canceled");
								JobError();
								return;
							}

							break;
						}
						try {
							sleep(10000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						try {
							sleep(5000);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				}
				try {
					sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			rPanel.HadoopTotalBar.setValue(100);
			rPanel.HadoopBar.setValue(100);

			if (opStatus == OperateStatus.SUCCESS) {
				rPanel.HadoopBar.setString("Finished");
				ThreadOver();
			} else if (opStatus == OperateStatus.ERROR) {
				rPanel.HadoopBar.setString("Error found");
				JobError();
			}

		}
	}

	public static void main(String[] args) {
		JFrame window = new JFrame("CloudDOE - Operate Wizard");
		window.setResizable(false);
		window.setLocationByPlatform(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Operate mainPanel = new Operate(window);
		window.getContentPane().add(mainPanel);
		window.pack();
		window.setVisible(true);
	}

}
