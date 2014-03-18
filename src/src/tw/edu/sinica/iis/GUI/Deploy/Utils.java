/*
 * (C) Copyright 2013 The CloudDOE Project and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *      Wei-Chun Chung (wcchung@iis.sinica.edu.tw)
 *      Yu-Chun Wang (zxaustin@iis.sinica.edu.tw)
 * 
 * CloudDOE Project:
 *      http://clouddoe.iis.sinica.edu.tw/
 */

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

	public static void genPartialEnv() {
		genPartialHadoopEnv();
		genPartialYARNEnv();
	}

	public static void genXML(final String nodeType, final int DNs) {
		genXMLYarn(nodeType);

		genXMLCore(nodeType, false);
		genXMLHdfs(nodeType, DNs, false);
		genXMLMapred(nodeType, false);

		genXMLCore(nodeType, true);
		genXMLHdfs(nodeType, DNs, true);
		genXMLMapred(nodeType, true);
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

	private static boolean genPartialHadoopEnv() {
		try {
			String filePath = genFilePath("common", "hadoop-env.sh");

			chkAndCreateSavePath(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write("export JAVA_HOME=" + ENDLINE);
			bw.write("export HADOOP_HOME=\"/opt/hadoop\"" + ENDLINE);
			bw.write("export HADOOP_SSH_OPTS=\"-o StrictHostKeyChecking=no\""
					+ ENDLINE);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean genPartialYARNEnv() {
		try {
			String filePath = genFilePath("common", "yarn-env.sh");

			chkAndCreateSavePath(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write("export JAVA_HOME=" + ENDLINE);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean genXMLYarn(final String nodeType) {
		try {
			String filePath = genFilePath(nodeType, "yarn-site.xml");

			chkAndCreateSavePath(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write("<?xml version=\"1.0\"?>" + ENDLINE);
			bw.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>"
					+ ENDLINE);
			bw.write("<configuration>" + ENDLINE);

			bw.write("  <property>" + ENDLINE);
			bw.write("    <name>yarn.resourcemanager.hostname</name>" + ENDLINE);
			bw.write("    <value>hadoop</value>" + ENDLINE);
			bw.write("  </property>" + ENDLINE);

			bw.write("  <property>" + ENDLINE);
			bw.write("    <name>yarn.nodemanager.aux-services</name>" + ENDLINE);
			bw.write("    <value>mapreduce_shuffle</value>" + ENDLINE);
			bw.write("  </property>" + ENDLINE);

			bw.write("  <property>" + ENDLINE);
			bw.write("    <name>yarn.nodemanager.aux-services.mapreduce.shuffle.class</name>"
					+ ENDLINE);
			bw.write("    <value>org.apache.hadoop.mapred.ShuffleHandler</value>"
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

	private static boolean genXMLCore(final String nodeType,
			final boolean isYARN) {

		try {
			String filePath = genFilePath(nodeType, "core-site.xml");
			if (isYARN) {
				filePath = genFilePath(nodeType, "core-site.xml.yarn");
			}

			chkAndCreateSavePath(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write("<?xml version=\"1.0\"?>" + ENDLINE);
			bw.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>"
					+ ENDLINE);
			bw.write("<configuration>" + ENDLINE);

			bw.write("  <property>" + ENDLINE);
			if (isYARN) {
				bw.write("    <name>fs.defaultFS</name>" + ENDLINE);
			} else {
				bw.write("    <name>fs.default.name</name>" + ENDLINE);
			}
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

	private static boolean genXMLHdfs(final String nodeType, final int DNs,
			final boolean isYARN) {

		try {
			String filePath = genFilePath(nodeType, "hdfs-site.xml");
			if (isYARN) {
				filePath = genFilePath(nodeType, "hdfs-site.xml.yarn");
			}

			chkAndCreateSavePath(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write("<?xml version=\"1.0\"?>" + ENDLINE);
			bw.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>"
					+ ENDLINE);
			bw.write("<configuration>" + ENDLINE);

			bw.write("  <property>" + ENDLINE);
			bw.write("    <name>dfs.replication</name>" + ENDLINE);
			bw.write("    <value>" + DNs + "</value>" + ENDLINE);
			bw.write("  </property>" + ENDLINE);

			bw.write("</configuration>" + ENDLINE);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean genXMLMapred(final String nodeType,
			final boolean isYARN) {

		try {
			String filePath = genFilePath(nodeType, "mapred-site.xml");
			if (isYARN) {
				filePath = genFilePath(nodeType, "mapred-site.xml.yarn");
			}

			chkAndCreateSavePath(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write("<?xml version=\"1.0\"?>" + ENDLINE);
			bw.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>"
					+ ENDLINE);
			bw.write("<configuration>" + ENDLINE);

			if (isYARN) {
				bw.write("  <property>" + ENDLINE);
				bw.write("    <name>mapreduce.framework.name</name>" + ENDLINE);
				bw.write("    <value>yarn</value>" + ENDLINE);
				bw.write("  </property>" + ENDLINE);
			} else {
				bw.write("  <property>" + ENDLINE);
				bw.write("    <name>mapred.job.tracker</name>" + ENDLINE);
				bw.write("    <value>hadoop:9001</value>" + ENDLINE);
				bw.write("  </property>" + ENDLINE);
			}

			bw.write("</configuration>" + ENDLINE);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean saveNodeFiles(
			final LinkedList<NodeStructure> content, final String file) {
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
		String NNFile = genFilePath("common", "NN");
		String DNFile = genFilePath("common", "DN");
		String MSFile = genFilePath("common", "masters");
		String SLFile = genFilePath("common", "slaves");

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

	public static LinkedList<NodeStructure> loadNodeFiles(
			final String fileName, final boolean isMaster) {
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
		if (res.size() == 0) {
			res = loadNodeFiles("NP", true);
		} else {
			LinkedList<NodeStructure> resNP = loadNodeFiles("NP", true);

			if (resNP.size() > 0) {
				res.get(0).User = resNP.get(0).User;
				res.get(0).Password = resNP.get(0).Password;
			}

			resNP.clear();
		}

		res.addAll(loadNodeFiles("DN", false));

		return res;
	}

}
