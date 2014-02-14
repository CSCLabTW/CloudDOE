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
