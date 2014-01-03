package tw.edu.sinica.iis.SSHadoop;

import java.io.File;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

public class SSHFSOptions {
	FileSystemOptions opts = null;

	public SSHFSOptions() {
		opts = new FileSystemOptions();
		try {
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
					opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
					true);
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
	}

	public FileSystemOptions getOptions() {
		return opts;
	}

	public void setKnownHosts(final String hostFile) {
		if (!"".equals(hostFile)) {
			try {
				SftpFileSystemConfigBuilder.getInstance().setKnownHosts(opts,
						new File(hostFile));
			} catch (FileSystemException e) {
				e.printStackTrace();
			}
		}
	}

	public void setIdentity(final String identFile) {
		if (!"".equals(identFile)) {
			try {
				SftpFileSystemConfigBuilder.getInstance().setIdentities(opts,
						new File[] { new File(identFile) });
			} catch (FileSystemException e) {
				e.printStackTrace();
			}
		}
	}
}
