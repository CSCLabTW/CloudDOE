package tw.edu.sinica.iis.SSHadoop;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;

public class SSHSftp {
	private StandardFileSystemManager manager = null;
	private SSHFSOptions opts = null;

	private String sshHost = "";
	private String sshUser = "";
	private String sshPass = "";
	private String sshPort = "";

	private String sshIdentity = "";
	private String sshKnownHost = "";

	public void setSSHIdent(final String identFileLoc) {
		sshIdentity = identFileLoc;
	}

	public void setSSHHost(final String host) {
		sshHost = host;
	}

	public void setSSHUser(final String user) {
		sshUser = user;
	}

	public void setSSHPass(final String pass) {
		sshPass = pass;
	}

	public void setSSHPort(final String port) {
		sshPort = port;
	}

	public void setSSHKnownHost(final String khFileLoc) {
		sshKnownHost = khFileLoc;
	}

	public SSHSftp(final String user, final String host) {
		manager = new StandardFileSystemManager();

		setSSHUser(user);
		setSSHHost(host);
	}

	public void initSftp() {
		opts = new SSHFSOptions();
		opts.setIdentity(sshIdentity);
		opts.setKnownHosts(sshKnownHost);
	}

	private String genConnectString() {
		String rst = "sftp://";

		rst = rst + sshUser;

		if (!"".equals(sshPass)) {
			try {
				rst = rst + ":" + URLEncoder.encode(sshPass, "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				rst = rst + ":" + sshPass;
			}
		}

		rst = rst + "@" + sshHost;

		if (!"".equals(sshPort))
			rst = rst + ":" + sshPort;

		rst = rst + "/";

		return rst;
	}

	public boolean sftpUpload(final String localDir, final String remoteDir) {
		try {
			manager.init();

			FileObject local = manager.resolveFile(localDir);
			FileObject remote = manager.resolveFile(genConnectString()
					+ remoteDir, opts.getOptions());

			remote.copyFrom(local, Selectors.SELECT_ALL);

		} catch (FileSystemException e) {
			e.printStackTrace();
			return false;
		} finally {
			manager.close();
		}
		return true;
	}

	public void sftpDownload(final String remoteDir, final String localDir) {
		try {
			manager.init();

			FileObject local = manager.resolveFile(localDir);
			FileObject remote = manager.resolveFile(genConnectString()
					+ remoteDir, opts.getOptions());

			local.copyFrom(remote, Selectors.SELECT_ALL);

		} catch (FileSystemException e) {
			e.printStackTrace();
		} finally {
			manager.close();
		}
	}

	public void sftpDelete(final String remoteDir) {
		try {
			manager.init();

			FileObject remote = manager.resolveFile(genConnectString()
					+ remoteDir, opts.getOptions());

			if (remote.exists()) {
				remote.delete(Selectors.SELECT_ALL);
			}

		} catch (FileSystemException e) {
			e.printStackTrace();
		} finally {
			manager.close();
		}
	}
}
