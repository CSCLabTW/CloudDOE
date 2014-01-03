package examples;

import tw.edu.sinica.iis.SSHadoop.SSHSftp;

public class SSHSftpExample {
	public static void main(String[] args) {
		String localDir = System.getProperty("user.dir") + "/test";
		String remoteDir = "usrbase";

		SSHSftp sftp = new SSHSftp("moneycat", "140.109.18.202");
		sftp.setSSHKnownHost(System.getProperty("user.home")
				+ "/.ssh/known_hosts");
		sftp.setSSHIdent(System.getProperty("user.home")
				+ "/.ssh/id_rsa_hadoop");
		sftp.initSftp();

		sftp.sftpUpload(localDir, remoteDir);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sftp.sftpDelete(remoteDir);
	}
}
