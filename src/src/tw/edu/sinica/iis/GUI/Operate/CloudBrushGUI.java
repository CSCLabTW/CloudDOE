package tw.edu.sinica.iis.GUI.Operate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
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
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import tw.edu.sinica.iis.SSHadoop.SSHCBRun;
import tw.edu.sinica.iis.SSHadoop.SSHExec;
import tw.edu.sinica.iis.SSHadoop.SSHSession;
import tw.edu.sinica.iis.SSHadoop.SSHSftp;
import tw.edu.sinica.iis.SSHadoop.SSHadoopCmd;
import tw.edu.sinica.iis.SSHadoop.SSHadoopUtils;
import tw.edu.sinica.iis.SSHadoop.SSHadoopUtils.CBStatus;

public class CloudBrushGUI extends JPanel {

	private static final long serialVersionUID = -1666416040585522565L;

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

	public int Status;

	// ----------Hadoop--------------
	public SSHSession HadoopSession;
	public SSHadoopCmd HadoopCmd;

	// -------monitor-thread----------------
	public MonitorThread mThread;
	public static int per = 0;

	// --------status---------------------
	public final static int NOT_CONNECTED = 0;
	public final static int JOB_READY = 1;
	public final static int JOB_WORKING = 2;
	public final static int JOB_FINISHED = 3;

	// --------saved in properties--------
	public final static String PROPERTYNAME = "setting.prop";

	public final static String INITIALED = "INITIALED";

	public final static String DEF_IP = "DEF_IP";
	public final static String DEF_PORT = "DEF_PORT";
	public final static String DEF_USERNAME = "DEF_USERNAME";
	public final static String DEF_PASSWORD = "HDFDEF_PASSWORDS_FILES";

	public final static String JOB_ID = "JOB_ID";
	public final static String JOB_RESULT = "JOB_RESULT";
	public final static String JOB_PARAS = "JOB_PARAS";

	public final static String HDFS_FILES = "HDFS_FILES";

	public final static String SPECIALCMD = "source /etc/profile; export PATH=$PATH:/opt/hadoop/bin";

	public String UID;

	public String initialed;

	public String Ip;
	public String Port;
	public String ID;
	public String Password;

	public String job_id;
	public String job_result;

	public String job_paras_label;

	public LinkedList<String> fileList;

	public CloudBrushGUI(JFrame p) {
		super();
		parent = p;

		identifyUID();
		readProperties();
		init();

		StatusChange(NOT_CONNECTED);
	}

	// ----------properties-------------------

	public void initialPropertyFile() {
		String un = System.getProperty("user.name") == null ? "unknown"
				: System.getProperty("user.name");
		UID = System.currentTimeMillis() + "_" + un;
		String defPro = "UID=" + UID + "\r\n" + INITIALED + "=no";
		TextExtractor.textToFileUTF8(System.getProperty("user.dir")
				+ File.separator + PROPERTYNAME, defPro);
	}

	public void identifyUID() {
		UID = PropertyUtility.readValue(PROPERTYNAME, "UID");
		if (UID == null) {
			// property rebuilt
			initialPropertyFile();
		}
	}

	public void readProperties() {
		initialed = PropertyUtility.readValue(PROPERTYNAME, INITIALED) == null ? ""
				: PropertyUtility.readValue(PROPERTYNAME, INITIALED);

		Ip = PropertyUtility.readValue(PROPERTYNAME, DEF_IP) == null ? ""
				: PropertyUtility.readValue(PROPERTYNAME, DEF_IP);
		Port = PropertyUtility.readValue(PROPERTYNAME, DEF_PORT) == null ? "22"
				: PropertyUtility.readValue(PROPERTYNAME, DEF_PORT);
		ID = PropertyUtility.readValue(PROPERTYNAME, DEF_USERNAME) == null ? ""
				: PropertyUtility.readValue(PROPERTYNAME, DEF_USERNAME);
		Password = "";

		job_id = PropertyUtility.readValue(PROPERTYNAME, JOB_ID) == null ? ""
				: PropertyUtility.readValue(PROPERTYNAME, JOB_ID);
		job_result = PropertyUtility.readValue(PROPERTYNAME, JOB_RESULT) == null ? ""
				: PropertyUtility.readValue(PROPERTYNAME, JOB_RESULT);

		job_paras_label = PropertyUtility.readValue(PROPERTYNAME, JOB_PARAS) == null ? "0;0"
				: PropertyUtility.readValue(PROPERTYNAME, JOB_PARAS);

		String RemoteFiles = PropertyUtility
				.readValue(PROPERTYNAME, HDFS_FILES);
		fileList = new LinkedList<String>();
		if (RemoteFiles != null && !"".equals(RemoteFiles)) {
			String[] sp = RemoteFiles.split(";");
			for (int i = 0; i < sp.length; i++) {
				fileList.add(sp[i]);
			}
		}
	}

	public void updateProperties() {

		PropertyUtility.writeProperties(PROPERTYNAME, INITIALED, initialed);

		PropertyUtility.writeProperties(PROPERTYNAME, DEF_IP, Ip);
		PropertyUtility.writeProperties(PROPERTYNAME, DEF_PORT, Port);
		PropertyUtility.writeProperties(PROPERTYNAME, DEF_USERNAME, ID);

		PropertyUtility.writeProperties(PROPERTYNAME, JOB_ID, job_id);
		PropertyUtility.writeProperties(PROPERTYNAME, JOB_RESULT, job_result);
		PropertyUtility.writeProperties(PROPERTYNAME, JOB_PARAS,
				job_paras_label);

		String RemoteFiles = "";
		for (int i = 0; i < fileList.size(); i++) {
			RemoteFiles += fileList.get(i) + ";";
		}
		PropertyUtility.writeProperties(PROPERTYNAME, HDFS_FILES, RemoteFiles);
		// System.out.println("updated.");
	}

	// -----------------------------------------

	public void StatusChange(int st) {
		switch (st) {
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

			Status = NOT_CONNECTED;
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

			for (int i = 0; i < rPanel.ParameterText.length; i++) {
				rPanel.ParameterText[i].setEnabled(false);
			}
			rPanel.HadoopBar.setString("Refreshing Status...");
			rPanel.ResultDownload.setEnabled(false);
			rPanel.WorkRun.setEnabled(false);
			rPanel.ResultClear.setEnabled(true);
			rPanel.ResultClear.setText("Cancel");
			Status = JOB_WORKING;
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

			for (int i = 0; i < rPanel.ParameterText.length; i++) {
				rPanel.ParameterText[i].setEnabled(true);
			}
			rPanel.ResultDownload.setEnabled(true);
			rPanel.ResultClear.setEnabled(true);
			rPanel.WorkRun.setEnabled(true);
			rPanel.ResultClear.setText("Reset");
			Status = JOB_FINISHED;
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

			for (int i = 0; i < rPanel.ParameterText.length; i++) {
				rPanel.ParameterText[i].setEnabled(true);
			}
			rPanel.ResultDownload.setEnabled(false);
			rPanel.ResultClear.setEnabled(false);
			rPanel.ResultClear.setText("Reset");
			rPanel.WorkRun.setEnabled(true);
			rPanel.HadoopBar.setString("Server Ready");
			Status = JOB_READY;
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

		Tabs = new JTabbedPane();

		cPanel = new ConnectPanel();
		Tabs.addTab("Connect", cPanel);
		uPanel = new UploadPanel();
		Tabs.addTab("Upload", uPanel);
		rPanel = new RunPanel();
		Tabs.addTab("Run", rPanel);
		aPanel = new AboutPanel();
		// TODO: clean up
		JScrollPane scrollPane = new JScrollPane(new AboutPanel());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
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
		RunPanelSetting();
		UploadPanelSetting();
	}

	public void ConnectPanelSetting() {
		cPanel.ipText.setText(Ip);
		cPanel.portText.setText(Port);
		cPanel.IDText.setText(ID);
		cPanel.PasswordText.setText(Password);

		cPanel.connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Status == NOT_CONNECTED) {

					// check ip changed
					if (Ip.length() > 1 && !Ip.equals(cPanel.ipText.getText())) {
						int ans = JOptionPane.showConfirmDialog(CloudBrushGUI.this,
								"Warning!\nYou're trying to login into another Hadoop cluster with\n an existed connection settings. All the settings will\n be erased when you click OK.");
						if (ans == JOptionPane.OK_OPTION) {
							initialPropertyFile();
							readProperties();
						}else{
							return;
						}
					}
					// ----------------

					Ip = cPanel.ipText.getText();
					Port = cPanel.portText.getText();
					ID = cPanel.IDText.getText();
					Password = new String(cPanel.PasswordText.getPassword());

					if ("no".endsWith(initialed)) {
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

								updateProperties();
								if ("".equals(job_id)) {
									StatusChange(JOB_READY);
								} else {
									StatusChange(JOB_WORKING);
									startThread();
									String[] paras = job_paras_label.split(";");
									for (int i = 0; i < paras.length; i++) {
										rPanel.ParameterText[i]
												.setText(paras[i]);
									}
								}
								statusMSG("Connected!");
								if (Status == JOB_WORKING) {
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
						StatusChange(NOT_CONNECTED);
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
							+ File.separator + "config" + File.separator + "common"
							+ File.separator + "NN"));
				}
				
				if(NNFile.length() > 1) {
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
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
//				fc.setFileFilter(new ExtensionFileFilter(new String[] {
//						".fastq", ".sfq" }, "Sequence files (*.fastq|sfq)"));
//				fc.setAcceptAllFileFilterUsed(false);

				int returnVal = fc.showDialog(CloudBrushGUI.this, "Upload");
				String path = "";
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					path = fc.getSelectedFile().getAbsolutePath();

					int r = JOptionPane.showConfirmDialog(CloudBrushGUI.this,
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

					// convert
//					if (path.toLowerCase().endsWith(".fastq")) {
//						String newpath = fc.getSelectedFile().getPath();
//						newpath += fc.getSelectedFile().getName().replaceFirst(
//								".fastq", "_fastq.sfq");
//						FastqToSfq tmpConverter = new FastqToSfq(path, newpath);
//						tmpConverter.convert();
//						path = newpath;
//					}
					// ----
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
					int r = JOptionPane.showConfirmDialog(CloudBrushGUI.this,
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
		rPanel.WorkRun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (job_id.length() > 0) {
					int r = JOptionPane
							.showConfirmDialog(
									CloudBrushGUI.this,
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

				if(!rPanel.getAndCheckParameter()){
					JOptionPane.showMessageDialog(null, "Format error!");
					return;
				}
				
				rPanel.HadoopBar.setString("Running");
				job_id = jobRun(rPanel.RunParameters);
				if (job_id != null) {
					updateProperties();
					StatusChange(JOB_WORKING);
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
				int returnVal = fc.showSaveDialog(CloudBrushGUI.this);
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
					if (Status == JOB_WORKING) {
						// Cancel
						int r = JOptionPane.showConfirmDialog(
								CloudBrushGUI.this, "Cancel this running job?",
								"WARNING", JOptionPane.YES_NO_OPTION);
						if (r == JOptionPane.NO_OPTION) {
							return;
						}
						statusMSG("Waiting for unfinish modules.");
						if (CancelJob()) {
							rPanel.ResultClear.setEnabled(false);
						}
					} else {
						// Clear
						int r = JOptionPane.showConfirmDialog(
								CloudBrushGUI.this,
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
									if (Status == JOB_FINISHED) {
										updateProperties();

										rPanel.HadoopTotalBar.setValue(0);
										rPanel.HadoopBar.setValue(0);
										StatusChange(JOB_READY);

										statusMSG("Resetted.");
									} else {
										JOptionPane.showMessageDialog(null,
												"Reset Failed.");
										statusMSG("Reset Failed.");
									}
									progressDialog.dispose();
									Tabs.setSelectedIndex(1);
									rPanel.ParameterText[0].setText("0");
									rPanel.ParameterText[1].setText("0");
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
			mThread = new MonitorThread("log");
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

				if ("no".endsWith(initialed)) {
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
		File tmp = new File("workspace/main");

		SSHSftp sftp = new SSHSftp(ID, Ip);
		sftp.setSSHPass(Password);
		sftp.initSftp();
		sftp.sftpUpload(tmp.getAbsolutePath(), UID + File.separator + "main");

		initialed = "done";
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
		sftp.sftpUpload(path.getAbsolutePath(), UID + File.separator + "data/"
				+ path.getName());

		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				HadoopCmd.moveFromLocalHdp("data/" + path.getName(), "data/"
						+ path.getName()));
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		Thread thread = new Thread(futureTask);
		thread.start();

		while (true) {
			if (futureTask.isDone()) {
				break;
			}
		}

		refreshFileList();
		return true;
	}

	public boolean uploadDir(File localPath, File remotePath) {
		SSHSftp sftp = new SSHSftp(ID, Ip);
		sftp.setSSHPass(Password);
		sftp.initSftp();
		sftp.sftpUpload(localPath.getPath(), remotePath.getPath());

		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				HadoopCmd.putHdp(localPath.getName(), ""));
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		Thread thread = new Thread(futureTask);
		thread.start();

		while (true) {
			if (futureTask.isDone()) {
				break;
			}
		}

		refreshFileList();
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
					HadoopCmd.rmrHdp("data/" + rmFileName));
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
		for (int i = fileList.size() - 1; i >= 0; i--) {
			fileList.remove(i);
		}
		HadoopSession.openSession();

		SSHadoopCmd tmpCMD = new SSHadoopCmd(UID, SPECIALCMD);
		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				tmpCMD.lsHdp("data"));
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		Thread thread = new Thread(futureTask);
		thread.start();

		while (true) {
			if (futureTask.isDone()) {
				try {
					String[] result = futureTask.get().split("\n");
					for (int i = 1; i < result.length; i++) {
						String[] sp = result[i].split("/");
						String dirName = sp[sp.length - 1];

						// ignore .tmp dir
						if (dirName.indexOf(".tmp") > 0)
							continue;

						fileList.add(dirName);
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		updateProperties();
		uPanel.TableData.fireTableDataChanged();

		return true;
	}

	public String jobRun(String[] args) {
		// JOptionPane.showMessageDialog(null, args[0] + args[1] + args[2] +
		// args[3]);

		if (args[0] == null || args[1] == null) {
			return null;
		}

		job_paras_label = args[0] + ";" + args[1];
		job_result = "output";

		String testCmd = HadoopCmd.jarHdp(UID + "/main/CloudBrush2.jar", "",
				"-asm " + UID + "/" + job_result + " -reads " + UID + "/data"
						+ " -readlen " + args[0] + " -k " + args[1] + " -work "
						+ UID + "/log");
		// System.out.println(testCmd);
		Callable<String> channel = new SSHCBRun(HadoopSession.getSession(),
				testCmd);
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		Thread thread = new Thread(futureTask);
		thread.start();

		String jobId = UID + "/" + job_result;
		while (true) {
			if (futureTask.isDone()) {

				break;
			}
		}

		return jobId;
	}

	public boolean ResultDownload(JFileChooser fc) {
		try {
			if (job_result == null || "".equals(job_result))
				return false;
			Callable<String> channel = new SSHExec(HadoopSession.getSession(),
					HadoopCmd.getMergeRstHdp(job_result, "output/output.fasta")
							+ ";"
							+ HadoopCmd.getHdp("output.jpeg/stats.jpg",
									"output/stats.jpg"));
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
			sftp.sftpDownload(UID + "/output", fc.getSelectedFile().getPath());
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	public boolean ClearJob() {

		if (job_result == null || "".equals(job_result)) {
			return false;
		}
		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				HadoopCmd.rmrHdp(job_result) + ";"
						+ HadoopCmd.rmrHdp("output.jpeg"));
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		Thread thread = new Thread(futureTask);
		thread.start();

		while (true) {
			if (futureTask.isDone()) {
				break;
			}
		}
		job_id = "";
		job_result = "";
		job_paras_label = "";

		SSHSftp sftp = new SSHSftp(ID, Ip);
		sftp.setSSHPass(Password);
		sftp.initSftp();
		sftp.sftpDelete(UID + "/log");
		sftp.sftpDelete(UID + "/output");

		return true;
	}

	public boolean ClearDataDir() {
		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				HadoopCmd.rmrHdp("data"));
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		Thread thread = new Thread(futureTask);
		thread.start();

		while (true) {
			if (futureTask.isDone()) {
				break;
			}
		}
		while (fileList.size() > 0) {
			fileList.removeFirst();
		}
		uPanel.TableData.fireTableDataChanged();
		return true;
	}

	public boolean CancelJob() {

		if (job_id == null || "cancel".equals(job_id)) {
			return false;
		}
		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				HadoopCmd.CBKillJob(UID));
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

	public boolean MakeDatDir() {
		Callable<String> channel = new SSHExec(HadoopSession.getSession(),
				"hadoop fs -mkdir " + UID + "/data");
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
		StatusChange(JOB_FINISHED);
		statusMSG("Job Finished.");
	}

	public void JobError() {
		// job_id = "";
		// job_result = "";
		// updateProperties();
		StatusChange(JOB_FINISHED);
		rPanel.ResultDownload.setEnabled(false);
		statusMSG("Job Ended with Error.");
	}

	// --------------------inner------------------

	public class selectList implements ListSelectionListener {

		public JTable Tparent;
		public int index;

		public selectList(JTable t, int i) {
			Tparent = t;
			index = i;
		}

		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			if (arg0.getValueIsAdjusting()) {
				return;
			}
			for (int c : Tparent.getSelectedRows()) {
				String data = Tparent.getValueAt(c, 0).toString();
				rPanel.RunParameters[index] = data;
				rPanel.ParameterText[index].setText(data);
				selectDialog.dispose();
			}
		}

	}

	public class MonitorThread extends Thread {

		public String monitorName = "bioFinal";
		public SSHadoopUtils utils = null;

		public MonitorThread(String jobMonName) {
			monitorName = jobMonName;
			utils = new SSHadoopUtils();
		}

		public CBStatus isJobDone() {
			Callable<String> channel2 = new SSHExec(HadoopSession.getSession(),
					HadoopCmd.CBStatus(monitorName, "brush.log"));
			CBStatus cbs = CBStatus.DEFAULT;

			FutureTask<String> futureTask2 = new FutureTask<String>(channel2);

			Thread thread2 = new Thread(futureTask2);
			thread2.start();

			// get whether cb is done
			while (true) {
				if (futureTask2.isDone()) {
					try {
						cbs = utils.getCBStatus(futureTask2.get());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					break;
				}
			}
			return cbs;
		}

		public String[] getJobNameID() {

			Callable<String> channel3 = new SSHExec(HadoopSession.getSession(),
					HadoopCmd.CBStepAndId(monitorName, "brush.details.log", 80));

			String toolNameAndJobId[] = { "", "", "" };

			FutureTask<String> futureTask3 = new FutureTask<String>(channel3);

			Thread thread3 = new Thread(futureTask3);
			thread3.start();

			// get cb step and job id
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

			CBStatus cbs = CBStatus.DEFAULT;

			while ((cbs = isJobDone()) == CBStatus.DEFAULT) {

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
									.getString()
									+ jNameID[0]);
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

			if (cbs == CBStatus.SUCCESS) {
				rPanel.HadoopBar.setString("Finished");
				ThreadOver();
			} else if (cbs == CBStatus.ERROR) {
				rPanel.HadoopBar.setString("Error found");
				JobError();
			}

		}
	}

	// -------------------------------------------
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("CloudDOE - Operate Wizard");
		window.setResizable(false);
		window.setLocationByPlatform(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CloudBrushGUI mainPanel = new CloudBrushGUI(window);
		window.getContentPane().add(mainPanel);
		window.pack();
		window.setVisible(true);
	}

}
