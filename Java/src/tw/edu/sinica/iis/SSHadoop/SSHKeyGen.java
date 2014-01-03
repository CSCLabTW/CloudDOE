package tw.edu.sinica.iis.SSHadoop;

import java.io.File;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;

public class SSHKeyGen {
	private final int keypairType = KeyPair.RSA;
	private final int keypairSize = 1024;

	private File keypairFile = null;

	private String keypairFileName = "";
	private String keypairComment = "";

	private void setKeypairFile(final String keypairFile) {
		this.keypairFileName = keypairFile;
	}

	private void setKeypairComment(final String keypairComment) {
		this.keypairComment = keypairComment;
	}

	public SSHKeyGen(final String keyPairPath, final String keyPairName,
			final String keyPairComment) {
		setKeypairFile(keyPairPath + File.separator + keyPairName);
		setKeypairComment(keyPairComment);

		keypairFile = new File(keypairFileName);
	}

	public boolean checkFileExist() {
		return (keypairFile.exists());
	}

	private void createFilePath() {
		if (!checkFileExist()) {
			keypairFile.getParentFile().mkdirs();
		}
	}

	public void genKeyPair() {
		createFilePath();

		KeyPair kp = null;
		try {
			kp = KeyPair.genKeyPair(new JSch(), keypairType, keypairSize);
			kp.writePrivateKey(keypairFileName);
			kp.writePublicKey(keypairFileName + ".pub", keypairComment);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (kp != null) {
				kp.dispose();
			}
		}
	}
}
