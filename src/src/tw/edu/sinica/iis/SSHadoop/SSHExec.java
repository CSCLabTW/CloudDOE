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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHExec implements Callable<String> {
	protected Session session = null;
	protected Channel channel = null;
	protected String execCommand = "";
	protected InputStream execStdout = null;

	public boolean execFinish = false;
	public int execStatus = -1;

	public SSHExec() {
	}

	public SSHExec(final Session sess, final String command) {
		setSession(sess);
		setCommand(command);
	}

	public void setSession(final Session sess) {
		session = sess;
	}

	public void setCommand(final String command) {
		execCommand = command;
	}

	protected void openChannel() {
		if (session != null && session.isConnected()) {
			try {
				channel = session.openChannel("exec");

				// if(channel!=null) System.out.println("Open Channel");

				if (channel != null) {
					((ChannelExec) channel).setCommand(execCommand);
					channel.setInputStream(null);
					try {
						execStdout = channel.getInputStream();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			} catch (JSchException e) {
				e.printStackTrace();
			}
		}
	}

	protected void execChannel() {
		if (session != null && session.isConnected()) {
			if (channel != null) {
				try {
					channel.connect();
				} catch (JSchException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void closeChannel() {
		if (channel != null && channel.isConnected()) {
			channel.disconnect();
		}

		// if(channel==null || !channel.isConnected())
		// System.out.println("Close Channel");
	}

	private void waitForFinish() {
		if (channel != null && channel.isConnected()) {
			while (true) {
				if (channel.isClosed()) {
					execStatus = channel.getExitStatus();
					execFinish = true;
					break;
				}
			}
		}
	}

	public String getResult() {
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(execStdout));

		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return stringBuffer.toString();
	}

	@Override
	public String call() throws Exception {
		openChannel();
		execChannel();
		waitForFinish();
		closeChannel();

		return getResult();
	}

}
