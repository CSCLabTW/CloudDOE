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

package tw.edu.sinica.iis.GUI.Extend;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import tw.edu.sinica.iis.SSHadoop.SSHShell;

public class JFXAction {
	public Extend frame;

	private SSHShell sshShell;
	
	private final String protocol = "cbppi://";
	private final String shscript = ".sh";
	private final String CBPURL   = "http://clouddoe.iis.sinica.edu.tw/plugins/";
	
	private String cbppiToURL(final String internalURI) {
		String url = null;
		
		if(internalURI.startsWith(protocol)) {
			url = CBPURL + internalURI.substring(protocol.length()) + shscript;
		}
		
		return url;
	}
	
	private String replaceProtocol(final String internalURI) {
		if(internalURI.startsWith(protocol)) {
			return internalURI.substring(protocol.length());
		}
		
		return null;
	}
	
	public JFXAction(Extend f) {
		frame = f;
	}
	
	public void intro(final String pkgName, final String pkgDesc) {
		JOptionPane.showMessageDialog(frame, new JLabel("<html><body style='width:400px'>"+pkgDesc+"</body></html>"),
				"Introduction of "+pkgName, JOptionPane.INFORMATION_MESSAGE );
	}

	public void install(final String pkgName, final String pkgContent) {
		if (JOptionPane.showConfirmDialog(frame, "Install " + pkgName + " ?") == JOptionPane.OK_OPTION) {
			final String instScript = cbppiToURL(pkgContent);
			if(instScript == null) {
				JOptionPane.showMessageDialog(frame, "Server error. Please contact to CloudBrushPack team for support.",
						"Error message", JOptionPane.ERROR_MESSAGE);
			}
			else {
				// TODO: need refinement
				final String logFilePath = "~/" + replaceProtocol(pkgContent) + ".log";
				final String pidFilePath = "~/" + replaceProtocol(pkgContent) + "_install.pid";
				
				final String instCmd = "install.sh " + Extend.IP + " " + Extend.Username + " " + Extend.Password + " " + replaceProtocol(pkgContent);
				
				String testCmd = Extend.HadoopCmd.PPIRun(instCmd, instScript, logFilePath);

				sshShell = new SSHShell(Extend.HadoopSession.getSession(), testCmd);

				try {
					sshShell.call();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error.");
				}
				
				MonitorDialog md = new MonitorDialog(frame, true);
				md.setPIDFile(pidFilePath);
				md.setLogFile(logFilePath);
				md.init();
				md.setVisible(true);
			}
		}
	}
}
