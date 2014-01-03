package tw.edu.sinica.iis.GUI.Deploy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Utils {
	private final static String ENDLINE = "\n";
	private final static String WORDSPLIT = "\t";

	public static String configPath = "workspace" + File.separator + "config"
			+ File.separator;

	public static void genXMLDN() {
		genPartialEnv();
		genDNcore();
		genDNhdfs();
		genDNmapred();
	}

	public static void genXMLNN() {
		genPartialEnv();
		genNNcore();
		genNNhdfs();
		genNNmapred();
	}
	
	private static String genFilePath(final String dir, final String file) {
		return configPath + File.separator + dir + File.separator + file;
	}

	private static void chkAndCreateSavePath(final String filePath) {
		if (!"".equals(filePath)) {
			File file = new File(filePath);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
			}
		}
	}

	private static boolean genPartialEnv() {
		try {
			String filePath = genFilePath("common", "hadoop-env.sh");

			chkAndCreateSavePath(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write("export JAVA_HOME=" + ENDLINE);
			bw.write("export HADOOP_HOME=\"/opt/hadoop\"" + ENDLINE);
			bw.write("export HADOOP_CONF_DIR=\"/opt/hadoop/conf\"" + ENDLINE);
			bw.write("export HADOOP_SSH_OPTS=\"-o StrictHostKeyChecking=no\""
					+ ENDLINE);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean genDNcore() {
		try {
			String filePath = genFilePath("DN", "core-site.xml");

			chkAndCreateSavePath(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write("<?xml version=\"1.0\"?>" + ENDLINE);
			bw
					.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>"
							+ ENDLINE);
			bw.write("<configuration>" + ENDLINE);
			bw.write("  <property>" + ENDLINE);
			bw.write("    <name>fs.default.name</name>" + ENDLINE);
			bw.write("    <value>hdfs://hadoop:9000</value>" + ENDLINE);
			bw.write("  </property>" + ENDLINE);
			bw.write("  <property>" + ENDLINE);
			bw.write("    <name>hadoop.tmp.dir</name>" + ENDLINE);
			bw.write("    <value>/var/hadoop/hadoop-${user.name}</value>"
					+ ENDLINE);
			bw.write("  </property>" + ENDLINE);
			bw.write("</configuration>" + ENDLINE);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean genDNhdfs() {
		try {
			String filePath = genFilePath("DN", "hdfs-site.xml");

			chkAndCreateSavePath(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write("<?xml version=\"1.0\"?>" + ENDLINE);
			bw
					.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>"
							+ ENDLINE);
			bw.write("<configuration>" + ENDLINE);
			bw.write("  <property>" + ENDLINE);
			bw.write("    <name>dfs.replication</name>" + ENDLINE);
			bw.write("    <value>1</value>" + ENDLINE);
			bw.write("  </property>" + ENDLINE);
			bw.write("</configuration>" + ENDLINE);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean genDNmapred() {
		try {
			String filePath = genFilePath("DN", "mapred-site.xml");

			chkAndCreateSavePath(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write("<?xml version=\"1.0\"?>" + ENDLINE);
			bw
					.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>"
							+ ENDLINE);
			bw.write("<configuration>" + ENDLINE);
			bw.write("  <property>" + ENDLINE);
			bw.write("    <name>mapred.job.tracker</name>" + ENDLINE);
			bw.write("    <value>hadoop:9001</value>" + ENDLINE);
			bw.write("  </property>" + ENDLINE);
			bw.write("</configuration>" + ENDLINE);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean genNNcore() {
		try {
			String filePath = genFilePath("NN", "core-site.xml");

			chkAndCreateSavePath(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write("<?xml version=\"1.0\"?>" + ENDLINE);
			bw
					.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>"
							+ ENDLINE);
			bw.write("<configuration>" + ENDLINE);
			bw.write("  <property>" + ENDLINE);
			bw.write("    <name>fs.default.name</name>" + ENDLINE);
			bw.write("    <value>hdfs://hadoop:9000</value>" + ENDLINE);
			bw.write("  </property>" + ENDLINE);
			bw.write("  <property>" + ENDLINE);
			bw.write("    <name>hadoop.tmp.dir</name>" + ENDLINE);
			bw.write("    <value>/var/hadoop/hadoop-${user.name}</value>"
					+ ENDLINE);
			bw.write("  </property>" + ENDLINE);
			bw.write("</configuration>" + ENDLINE);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean genNNhdfs() {
		try {
			String filePath = genFilePath("NN", "hdfs-site.xml");

			chkAndCreateSavePath(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write("<?xml version=\"1.0\"?>" + ENDLINE);
			bw
					.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>"
							+ ENDLINE);
			bw.write("<configuration>" + ENDLINE);
			bw.write("  <property>" + ENDLINE);
			bw.write("    <name>dfs.replication</name>" + ENDLINE);
			bw.write("    <value>1</value>" + ENDLINE);
			bw.write("  </property>" + ENDLINE);
			bw.write("</configuration>" + ENDLINE);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean genNNmapred() {
		try {
			String filePath = genFilePath("NN", "mapred-site.xml");

			chkAndCreateSavePath(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write("<?xml version=\"1.0\"?>" + ENDLINE);
			bw
					.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>"
							+ ENDLINE);
			bw.write("<configuration>" + ENDLINE);
			bw.write("  <property>" + ENDLINE);
			bw.write("    <name>mapred.job.tracker</name>" + ENDLINE);
			bw.write("    <value>hadoop:9001</value>" + ENDLINE);
			bw.write("  </property>" + ENDLINE);
			bw.write("</configuration>" + ENDLINE);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean saveNodeFiles(final LinkedList<NodeStructure> content, final String file){
		String NNFile = genFilePath("common", file);
		
		chkAndCreateSavePath(NNFile);
		
		boolean genFinish = true;
		
		StringBuilder NNs = new StringBuilder();
		
		for (int i = 0; i < content.size(); i++) {
			String serverLine = content.get(i).IP + WORDSPLIT
					+ content.get(i).User + WORDSPLIT + content.get(i).Password
					+ ENDLINE;

			if (content.get(i).Master.booleanValue()) {
				NNs.append(serverLine);
			}
		}
		
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(NNFile));
			bw.write(NNs.toString());
			bw.close();
		} catch (IOException e) {
			genFinish = false;
			e.printStackTrace();
		}
		
		return genFinish;
	}
	
	public static boolean saveHostFiles(final LinkedList<NodeStructure> content) {
		String HostFile = genFilePath("common", "hosts");
		String NNFile   = genFilePath("common", "NN");
		String DNFile   = genFilePath("common", "DN");
		String MSFile   = genFilePath("common", "masters");
		String SLFile   = genFilePath("common", "slaves");

		chkAndCreateSavePath(HostFile);
		chkAndCreateSavePath(NNFile);
		chkAndCreateSavePath(DNFile);
		chkAndCreateSavePath(MSFile);
		chkAndCreateSavePath(SLFile);

		boolean genFinish = true;

		StringBuilder hosts = new StringBuilder();
		StringBuilder NNs = new StringBuilder();
		StringBuilder DNs = new StringBuilder();
		StringBuilder master = new StringBuilder();
		StringBuilder slave = new StringBuilder();

		hosts.append("127.0.0.1" + WORDSPLIT + "localhost" + ENDLINE);

		int nodeNo = 1;
		for (int i = 0; i < content.size(); i++) {
			String hostLine = content.get(i).IP + WORDSPLIT;
			String serverLine = content.get(i).IP + WORDSPLIT
					+ content.get(i).User + WORDSPLIT + content.get(i).Password
					+ ENDLINE;

			if (content.get(i).Master.booleanValue()) {
				hostLine += "hadoop";
				master.append("hadoop" + ENDLINE);
				slave.append("hadoop" + ENDLINE);
				NNs.append(serverLine);
			} else {
				hostLine += "node" + nodeNo;
				slave.append("node" + nodeNo + ENDLINE);
				DNs.append(serverLine);

				nodeNo++;
			}

			hosts.append(hostLine + ENDLINE);
		}

		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(HostFile));
			bw.write(hosts.toString());
			bw.close();
		} catch (IOException e) {
			genFinish = false;
			e.printStackTrace();
		}

		try {
			bw = new BufferedWriter(new FileWriter(NNFile));
			bw.write(NNs.toString());
			bw.close();
		} catch (IOException e) {
			genFinish = false;
			e.printStackTrace();
		}

		try {
			bw = new BufferedWriter(new FileWriter(DNFile));
			bw.write(DNs.toString());
			bw.close();
		} catch (IOException e) {
			genFinish = false;
			e.printStackTrace();
		}

		try {
			bw = new BufferedWriter(new FileWriter(MSFile));
			bw.write(master.toString());
			bw.close();
		} catch (IOException e) {
			genFinish = false;
			e.printStackTrace();
		}

		try {
			bw = new BufferedWriter(new FileWriter(SLFile));
			bw.write(slave.toString());
			bw.close();
		} catch (IOException e) {
			genFinish = false;
			e.printStackTrace();
		}

		return genFinish;
	}
	
	public static LinkedList<NodeStructure> loadNodeFiles(final String fileName, final boolean isMaster) {
		String nodeFile = genFilePath("common", fileName);

		LinkedList<NodeStructure> res = new LinkedList<NodeStructure>();

		File readf = new File(nodeFile);
		if (readf.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(readf));
				String line = "";
				while ((line = br.readLine()) != null) {
					String[] sp = line.split(WORDSPLIT);
					if (sp.length == 3) {
						res.add(new NodeStructure(isMaster, sp[0], sp[1], sp[2]));
					}
				}
				br.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return res;
	}

	public static LinkedList<NodeStructure> loadHostFiles() {
		LinkedList<NodeStructure> res = loadNodeFiles("NN", true);
		if(res.size() == 0) res = loadNodeFiles("NP", true);
		
		res.addAll(loadNodeFiles("DN", false));
		
		return res;
	}

}
