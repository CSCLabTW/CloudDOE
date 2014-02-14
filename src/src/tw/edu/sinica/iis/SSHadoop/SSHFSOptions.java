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
