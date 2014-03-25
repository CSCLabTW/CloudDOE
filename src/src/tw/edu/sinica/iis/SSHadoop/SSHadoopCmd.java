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

package tw.edu.sinica.iis.SSHadoop;

public class SSHadoopCmd {
	private String userBase = "";
	private String serverSpecialCmd = "";
	private String binaryLocation = "";

	private class cmd {
		final static String cd = "cd ";
		final static String sh = "sh ";
		final static String ls = "ls ";
		final static String rm = "rm ";
		final static String cat = "cat ";
		final static String tail = "tail ";
		final static String wget = "wget ";
		final static String grep = "grep ";
		final static String mkdir = "mkdir ";
		final static String pkill = "pkill ";
		final static String touch = "touch ";
	}

	private class hdpCmd {
		final static String ls = "hadoop fs -ls ";
		final static String put = "hadoop fs -put ";
		final static String get = "hadoop fs -get ";
		final static String rmr = "hadoop fs -rmr ";
		final static String mfl = "hadoop fs -moveFromLocal ";
		final static String cfl = "hadoop fs -copyFromLocal ";
		final static String mtl = "hadoop fs -moveToLocal ";
		final static String ctl = "hadoop fs -copyToLocal ";

		final static String getmerge = "hadoop fs -getmerge ";
		final static String mkdir = "hadoop fs -mkdir ";

		final static String jar = "hadoop jar ";

		final static String job_status = "hadoop job -status ";
	}

	public void setServerSpecialCmd(final String specialCmd) {
		serverSpecialCmd = specialCmd;
	}

	public void setBinaryLocation(final String binLoc) {
		binaryLocation = binLoc;
	}

	public void setUserBase(final String baseName) {
		if (!"".equals(baseName))
			userBase = baseName + "/";
	}

	public SSHadoopCmd() {
	}

	public SSHadoopCmd(final String userBase, final String specialCmd) {
		setUserBase(userBase);
		setServerSpecialCmd(specialCmd);
	}

	public String ls(final String dir) {
		return cmd.ls + userBase + dir;
	}

	public String cat(final String filePath) {
		return cmd.cat + userBase + filePath;
	}

	public String mkdir(final String filePath, final boolean recursive) {
		String recurMode = "";
		if (recursive) {
			recurMode = "-p ";
		}
		return cmd.mkdir + recurMode + userBase + filePath;
	}

	public String touch(final String filePath) {
		return cmd.touch + userBase + filePath;
	}

	public String rm(final String filePath) {
		return cmd.rm + userBase + filePath;
	}

	public String lsHdp(final String dir) {
		return serverSpecialCmd + "; " + hdpCmd.ls + userBase + dir;
	}

	public String putHdp(final String localDir, final String remoteDir) {
		return serverSpecialCmd + "; " + hdpCmd.put + " " + userBase + localDir
				+ " " + userBase + remoteDir;
	}

	public String getHdp(final String remoteDir, final String localDir) {
		return serverSpecialCmd + "; " + hdpCmd.get + " " + userBase
				+ remoteDir + " " + userBase + localDir;
	}

	public String rmrHdp(final String remoteDir) {
		return serverSpecialCmd + "; " + hdpCmd.rmr + userBase + remoteDir;
	}

	public String moveFromLocalHdp(final String localDir, final String remoteDir) {
		return serverSpecialCmd + "; " + hdpCmd.mfl + " " + userBase + localDir
				+ " " + userBase + remoteDir;
	}

	public String copyFromLocalHdp(final String localDir, final String remoteDir) {
		return serverSpecialCmd + "; " + hdpCmd.cfl + " " + userBase + localDir
				+ " " + userBase + remoteDir;
	}

	public String moveToLocalHdp(final String remoteDir, final String localDir) {
		return serverSpecialCmd + "; " + hdpCmd.mtl + " " + userBase
				+ remoteDir + " " + userBase + localDir;
	}

	public String copyToLocalHdp(final String remoteDir, final String localDir) {
		return serverSpecialCmd + "; " + hdpCmd.ctl + " " + userBase
				+ remoteDir + " " + userBase + localDir;
	}

	public String getMergeRstHdp(final String remoteRstDir,
			final String localFile) {
		return serverSpecialCmd + "; " + hdpCmd.getmerge + " " + userBase
				+ remoteRstDir + " " + userBase + localFile;
	}

	public String mkdirHdp(final String filePath, final String userName,
			final boolean recursive) {
		String recurMode = "";
		if (recursive) {
			recurMode = "-p ";
		}
		return serverSpecialCmd + "; " + hdpCmd.mkdir + recurMode + "/user/"
				+ userName + "/" + userBase + filePath;
	}

	public String jarHdp(final String jarPath, final String jarClsName,
			final String params) {
		return serverSpecialCmd + "; " + hdpCmd.jar + " " + jarPath + " "
				+ jarClsName + " " + params;
	}

	public String jobStatusHdp(final String jobId) {
		return serverSpecialCmd + "; " + hdpCmd.job_status + " " + jobId;
	}

	public String CBStepAndId(final String workDir, final String logFile,
			final int tailLineCnt) {
		String count = "";
		if (tailLineCnt > 0) {
			count = "-n " + tailLineCnt + " ";
		}
		return cmd.tail + count + userBase + workDir + "/" + logFile;
	}

	public String CBKillJob(final String UID) {
		return cmd.pkill + "-f " + UID;
	}

	public String OCIRun(final String shellScript, final String logFile) {
		return "cd " + binaryLocation + "; " + cmd.sh + " ./" + shellScript
				+ " >" + logFile;
	}

	public String OCILog(final String logFile, final int tailLineCnt) {
		String count = "";
		if (tailLineCnt > 0) {
			count = "-n " + tailLineCnt + " ";
		}
		return cmd.grep + " \"^\\(---- \\[\\).*\" " + logFile + " | "
				+ cmd.tail + count;
	}

	public String OCIClenup(final String location) {
		return cmd.rm + " -rf " + location;
	}

	public String PPIRun(final String installCmd, final String shellFile,
			final String logFile) {
		return cmd.cd + binaryLocation + "; " + cmd.rm + "plugins/* ;"
				+ cmd.wget + shellFile + " -P plugins/ ; " + cmd.sh + " ./"
				+ installCmd + " > " + logFile;
	}

	public String PPIStatus(final String shellScript, final String pidFilePath) {
		return "cd " + binaryLocation + "; " + cmd.sh + " ./" + shellScript
				+ " " + pidFilePath;
	}

	public String PPILog(final String logFile, final int tailLineCnt) {
		String count = "";
		if (tailLineCnt > 0) {
			count = "-n " + tailLineCnt + " ";
		}
		return cmd.grep + " \"^[^\\r\\n]\" " + logFile + " | " + cmd.tail
				+ count;
	}
}
