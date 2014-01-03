package examples;

import java.io.File;

import tw.edu.sinica.iis.SSHadoop.SSHKeyGen;

public class SSHKeyGenExample {
	public static void main(String[] args) {
		final String filePath = "workspace" + File.separator + "config"
				+ File.separator + "common";
		final String fileName = "hadoop";

		SSHKeyGen s = new SSHKeyGen(filePath, fileName, "hadoop@CloudBrush");
		if (!s.checkFileExist()) {
			s.genKeyPair();
		} else {
			System.out.println("File existed.");
		}
	}
}
